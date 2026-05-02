package com.amgboddel.backend.service;

import com.amgboddel.backend.dto.*;
import com.amgboddel.backend.entity.Professeur;
import com.amgboddel.backend.entity.Tag;
import com.amgboddel.backend.entity.UE;
import com.amgboddel.backend.exception.DuplicateResourceException;
import com.amgboddel.backend.exception.ResourceNotFoundException;
import com.amgboddel.backend.repository.ProfesseurRepository;
import com.amgboddel.backend.repository.TagRepository;
import com.amgboddel.backend.repository.UERepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UEService {

    private final UERepository ueRepository;
    private final TagRepository tagRepository;
    private final ProfesseurRepository professeurRepository;

    @Transactional
    public List<UEResponse> getAll(){
        return ueRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional
    public UEResponse getById(Long id){
        UE ue = ueRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("UE non trouvable"));
        return toResponse(ue);
    }

    @Transactional
    public List<UEResponse> search(String s){
        return ueRepository
                .findByNomUeContainingIgnoreCase(s)
                .stream()
                .map(this::toResponse)
                .toList();
    }


    private void requestToEntity(UERequest request, UE ue){
        ue.setNomUe(request.getNomUe());
        ue.setEcts(request.getEcts());
        ue.setCm(request.getCm());
        ue.setTd(request.getTd());
        ue.setTp(request.getTp());
        ue.setDescription(request.getDescription());
        ue.setUeObligatoire(request.getUeObligatoire());

        ue.setTags(resolveTagIds(request.getTagIds()));
        ue.setEnseignants(resolveProfIds(request.getEnseignantIds()));
        ue.setReferents(resolveProfIds(request.getReferentIds()));
        ue.setPrerequis(resolveUeIds(request.getPrerequisIds()));
    }

    @Transactional
    public UEResponse create(UERequest request){
        UE ue = new UE();
        requestToEntity(request, ue);
        return toResponse(ueRepository.save(ue));
    }

    @Transactional
    public UEResponse update(Long id, UERequest request){
        UE ue = ueRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("UE non trouvable"));
        requestToEntity(request, ue);
        return toResponse(ueRepository.save(ue));
    }

    @Transactional
    public void delete(Long id){
        if (!ueRepository.existsById(id)){
            throw new ResourceNotFoundException("UE non trouvable avec l'id : " + id);
        }
        ueRepository.deleteById(id);
    }

    private Set<Tag> resolveTagIds(List<Long> tagIds){
        if (tagIds == null || tagIds.isEmpty()){
            return new HashSet<>();
        }

        List<Tag> tags = tagRepository.findAllById(tagIds);

        if (tags.size() != tagIds.size()){
            Set<Long> foundIds = tags.stream()
                    .map(Tag::getId)
                    .collect(Collectors.toSet());

            List<Long> missing = tagIds.stream()
                    .filter(id -> !foundIds.contains(id))
                    .toList();

            throw new ResourceNotFoundException("Tags non trouvés avec les ids suivants : " + missing);
        }

        return new HashSet<>(tags);
    }

    private Set<Professeur> resolveProfIds(List<Long> profIds){
        if (profIds == null || profIds.isEmpty()){
            return new HashSet<>();
        }

        List<Professeur> profs = professeurRepository.findAllById(profIds);

        if (profs.size() != profIds.size()){
            Set<Long> foundIds = profs.stream()
                    .map(Professeur::getId)
                    .collect(Collectors.toSet());

            List<Long> missing = profIds.stream()
                    .filter(id -> !foundIds.contains(id))
                    .toList();

            throw new ResourceNotFoundException("Professeurs non trouvés avec les ids suivants : " + missing);
        }

        return new HashSet<>(profs);
    }

    private Set<UE> resolveUeIds(List<Long> ueIds){
        if (ueIds == null || ueIds.isEmpty()){
            return new HashSet<>();
        }

        List<UE> ues = ueRepository.findAllById(ueIds);

        if (ues.size() != ueIds.size()){
            Set<Long> foundIds = ues.stream()
                    .map(UE::getId)
                    .collect(Collectors.toSet());

            List<Long> missing = ueIds.stream()
                    .filter(id -> !foundIds.contains(id))
                    .toList();

            throw new ResourceNotFoundException("UE prérequis non trouvées avec les ids suivants : " + missing);
        }

        return new HashSet<>(ues);
    }

    private UEResponse toResponse(UE ue){

        List<TagResponse> tagResponses = ue.getTags() == null
                ? Collections.emptyList()
                : ue.getTags().stream()
                .map(t -> new TagResponse(t.getId(), t.getNomTag(), t.getCouleur()))
                .toList();

        List<ProfesseurResponse> enseignants = ue.getEnseignants() == null
                ? Collections.emptyList()
                : ue.getEnseignants().stream()
                .map(p -> ProfesseurResponse.builder()
                        .id(p.getId())
                        .nom(p.getNom())
                        .prenom(p.getPrenom())
                        .email(p.getEmail())
                        .tags(Collections.emptyList())
                        .nbUe(0)
                        .nbReferent(0)
                        .totalCm(0)
                        .totalTd(0)
                        .totalTp(0)
                        .build())
                .toList();

        List<ProfesseurResponse> referents = ue.getReferents() == null
                ? Collections.emptyList()
                : ue.getReferents().stream()
                .map(p -> ProfesseurResponse.builder()
                        .id(p.getId())
                        .nom(p.getNom())
                        .prenom(p.getPrenom())
                        .email(p.getEmail())
                        .tags(Collections.emptyList())
                        .nbUe(0)
                        .nbReferent(0)
                        .totalCm(0)
                        .totalTd(0)
                        .totalTp(0)
                        .build())
                .toList();

        List<UEResponse> prerequis = ue.getPrerequis() == null
                ? Collections.emptyList()
                : ue.getPrerequis().stream()
                .map(this::toResponseLight)
                .toList();

        int volumeTotal = ue.getCm() + ue.getTd() + ue.getTp();

        long nbMaquettes = ue.getSemestres() == null ? 0 :
                ue.getSemestres().stream()
                        .map(s -> s.getMaquette().getId())
                        .distinct()
                        .count();

        return UEResponse.builder()
                .id(ue.getId())
                .nomUe(ue.getNomUe())
                .ects(ue.getEcts())
                .cm(ue.getCm())
                .td(ue.getTd())
                .tp(ue.getTp())
                .description(ue.getDescription())
                .ueObligatoire(ue.getUeObligatoire())
                .tags(tagResponses)
                .enseignants(enseignants)
                .referents(referents)
                .prerequis(prerequis)
                .nbEnseignants(enseignants.size())
                .nbReferents(referents.size())
                .nbPrerequis(prerequis.size())
                .nbSemestres(ue.getSemestres().size())
                .nbMaquettes((int) nbMaquettes)
                .volumeHoraireTotal(volumeTotal)
                .build();
    }

    /**
     * Version allégée de toResponse pour éviter les boucles infinies
     * lors de l'inclusion dans les réponses de Semestre/Maquette.
     * Retourne uniquement les infos de base de l'UE sans charger
     * les relations complexes (enseignants, référents, prérequis).
     */
    public UEResponse toResponseLight(UE ue) {
        List<TagResponse> tagResponses = ue.getTags() == null
                ? Collections.emptyList()
                : ue.getTags().stream()
                .map(t -> new TagResponse(t.getId(), t.getNomTag(), t.getCouleur()))
                .toList();

        int volumeTotal = ue.getCm() + ue.getTd() + ue.getTp();

        long nbMaquettes = ue.getSemestres() == null ? 0 :
                ue.getSemestres().stream()
                        .map(s -> s.getMaquette().getId())
                        .distinct()
                        .count();

        return UEResponse.builder()
                .id(ue.getId())
                .nomUe(ue.getNomUe())
                .ects(ue.getEcts())
                .cm(ue.getCm())
                .td(ue.getTd())
                .tp(ue.getTp())
                .description(ue.getDescription())
                .ueObligatoire(ue.getUeObligatoire())
                .tags(tagResponses)
                .enseignants(Collections.emptyList())   // Non chargé dans version light
                .referents(Collections.emptyList())   // Non chargé dans version light
                .prerequis(Collections.emptyList())   // Non chargé dans version light
                .nbEnseignants(0)
                .nbReferents(0)
                .nbPrerequis(0)
                .nbSemestres(0)
                .nbMaquettes((int) nbMaquettes)
                .volumeHoraireTotal(volumeTotal)
                .build();
    }

    public Long count() {
        return ueRepository.count();
    }

    public Long countUEattachToTeachers(){
        return ueRepository.countDistinctByEnseignantsIsNotEmpty();
    }

    public int hourlyVolume(){
        return ueRepository.findAll()
                .stream()
                .mapToInt(ue -> ue.getCm() + ue.getTd() + ue.getTp())
                .sum();
    }
}