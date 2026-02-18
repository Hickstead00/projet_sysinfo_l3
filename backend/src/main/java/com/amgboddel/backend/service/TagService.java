package com.amgboddel.backend.service;

import com.amgboddel.backend.dto.TagRequest;
import com.amgboddel.backend.dto.TagResponse;
import com.amgboddel.backend.entity.Tag;
import com.amgboddel.backend.exception.DuplicateResourceException;
import com.amgboddel.backend.exception.ResourceNotFoundException;
import com.amgboddel.backend.repository.TagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TagService {

    private final TagRepository tagRepository;

    public List<TagResponse> getAll(){
        return tagRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public TagResponse getById(Long id){
        Tag tag = tagRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Tag introuvable avec l'id : " + id));
        return toResponse(tag);
    }

    public TagResponse create(TagRequest request){
        if (tagRepository.existsByNomTag(request.getNomTag())){
            throw new DuplicateResourceException("Un tag avec le nom : " + request.getNomTag() + " existe déjà");
        }

        Tag tag = new Tag();
        tag.setNomTag(request.getNomTag());
        tag.setCouleur(request.getCouleur());
        Tag saved = tagRepository.save(tag);
        return toResponse(saved);
    }

    public TagResponse update(Long id, TagRequest request){
        Tag tag = tagRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Tag introuvable avec l'id : " + id));

        tagRepository.findByNomTag(request.getNomTag())
                .ifPresent(existing -> {
                    if (!existing.getId().equals(id)){
                        throw new DuplicateResourceException("Un tag avec le nom : " + request.getNomTag() + " existe déjà");
                    }
                });

        tag.setNomTag(request.getNomTag());
        tag.setCouleur(request.getCouleur());
        Tag saved = tagRepository.save(tag);
        return toResponse(saved);
    }

    public void delete(Long id){
        if (!tagRepository.existsById(id)){
            throw new ResourceNotFoundException("Tag introuvable avec l'id : " + id);
        }
        tagRepository.deleteById(id);
    }


    private TagResponse toResponse(Tag tag){
        return new TagResponse(tag.getId(), tag.getNomTag(), tag.getCouleur());
    }

}
