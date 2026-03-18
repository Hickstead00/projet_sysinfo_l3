package com.amgboddel.backend.service;

import com.amgboddel.backend.dto.ProfesseurRequest;
import com.amgboddel.backend.dto.ProfesseurResponse;
import com.amgboddel.backend.dto.ProfesseurStatsProjection;
import com.amgboddel.backend.dto.TagResponse;
import com.amgboddel.backend.entity.Professeur;
import com.amgboddel.backend.entity.Tag;
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
public class ProfesseurService {

    private final ProfesseurRepository professeurRepository;
    private final TagRepository tagRepository;
    private final UERepository ueRepository;


    @Transactional
    public List<ProfesseurResponse> getAll(){
        return professeurRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional
    public ProfesseurResponse getById(Long id){
        Professeur professeur = professeurRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Professeur non trouvable"));
        return toResponse(professeur);
    }

    @Transactional
    public List<ProfesseurResponse> search(String s){
        return professeurRepository
                .findByNomContainingIgnoreCaseOrPrenomContainingIgnoreCase(s, s)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional
    public ProfesseurResponse create(ProfesseurRequest request){
        if (professeurRepository.existsByEmail(request.getEmail())){
            throw new DuplicateResourceException("Un professeur avec cet email existe déjà");
        }

        Professeur professeur = new Professeur();
        professeur.setNom(request.getNom());
        professeur.setPrenom(request.getPrenom());
        professeur.setEmail(request.getEmail());
        professeur.setTags(resolveTagIds(request.getTagIds()));
        return toResponse(professeurRepository.save(professeur));

    }

    @Transactional
    public ProfesseurResponse update(Long id, ProfesseurRequest request){
        Professeur professeur = professeurRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Professeur non trouvable"));

        if (!professeur.getEmail().equals(request.getEmail()) && professeurRepository.existsByEmail(request.getEmail())){
            throw new DuplicateResourceException("Un professeur avec et email existe déjà");
        }

        professeur.setNom(request.getNom());
        professeur.setPrenom(request.getPrenom());
        professeur.setEmail(request.getEmail());
        professeur.setTags(resolveTagIds(request.getTagIds()));
        return toResponse(professeurRepository.save(professeur));

    }

    @Transactional
    public void delete(Long id){
        Professeur professeur = professeurRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Professeur non trouvable avec l'id : " + id));

        ueRepository.findByEnseignantsId(id)
                .forEach(ue -> ue.getEnseignants().remove(professeur));

        ueRepository.findByReferentsId(id)
                .forEach(ue -> ue.getReferents().remove(professeur));

        professeurRepository.delete(professeur);
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

    private ProfesseurResponse toResponse(Professeur prof){
        List<TagResponse> tagResponses = prof.getTags() == null
                ? Collections.emptyList()
                : prof.getTags().stream()
                .map(t -> new TagResponse(t.getId(), t.getNomTag(), t.getCouleur()))
                .toList();

        ProfesseurStatsProjection stats = ueRepository.findStatsByEnseignantId(prof.getId());
        Long nbReferent = ueRepository.countByReferentId(prof.getId());

        return ProfesseurResponse.builder()
                .id(prof.getId())
                .nom(prof.getNom())
                .prenom(prof.getPrenom())
                .email(prof.getEmail())
                .tags(tagResponses)
                .nbUe(stats.getNbUe().intValue())
                .nbReferent(nbReferent.intValue())
                .totalCm(stats.getTotalCm().intValue())
                .totalTd(stats.getTotalTd().intValue())
                .totalTp(stats.getTotalTp().intValue())
                .build();
    }

}
