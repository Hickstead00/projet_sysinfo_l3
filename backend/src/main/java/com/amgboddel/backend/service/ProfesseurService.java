package com.amgboddel.backend.service;

import com.amgboddel.backend.dto.ProfesseurRequest;
import com.amgboddel.backend.dto.ProfesseurResponse;
import com.amgboddel.backend.dto.TagResponse;
import com.amgboddel.backend.entity.Professeur;
import com.amgboddel.backend.entity.Tag;
import com.amgboddel.backend.exception.DuplicateResourceException;
import com.amgboddel.backend.exception.ResourceNotFoundException;
import com.amgboddel.backend.repository.ProfesseurRepository;
import com.amgboddel.backend.repository.TagRepository;
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

    public List<ProfesseurResponse> getAll(){
        return professeurRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public ProfesseurResponse getById(Long id){
        Professeur professeur = professeurRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Professeur non trouvable"));
        return toResponse(professeur);
    }

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
        if (!professeurRepository.existsById(id)){
            throw new ResourceNotFoundException("Professeur non trouvable avec l'id : " + id);
        }
        professeurRepository.deleteById(id);
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

        return ProfesseurResponse.builder()
                .id(prof.getId())
                .nom(prof.getNom())
                .prenom(prof.getPrenom())
                .email(prof.getEmail())
                .tags(tagResponses)
                // TODO PLUS TARD : Remplacer par vraies valeurs calculer, valeurs de 0 pour fournir des valeurs à consommer au frontend
                .nbUe(0)
                .nbReferent(0)
                .totalCm(0)
                .totalTd(0)
                .totalTp(0)
                .build();
    }

}
