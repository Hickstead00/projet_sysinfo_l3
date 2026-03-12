package com.amgboddel.backend.service;

import com.amgboddel.backend.dto.ParametresRequest;
import com.amgboddel.backend.dto.ParametresResponse;
import com.amgboddel.backend.entity.Parametres;
import com.amgboddel.backend.exception.DuplicateResourceException;
import com.amgboddel.backend.exception.ResourceNotFoundException;
import com.amgboddel.backend.repository.ParametresRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ParametresService {

    private final ParametresRepository parametresRepository;

    public List<ParametresResponse> getAll() {
        return parametresRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public ParametresResponse getByName(String nomParametre) {
        Parametres parametre = parametresRepository.findById(nomParametre)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Paramètre introuvable avec le nom : " + nomParametre));
        return toResponse(parametre);
    }

    @Transactional
    public ParametresResponse create(ParametresRequest request) {
        if (parametresRepository.existsById(request.getNomParametre())) {
            throw new DuplicateResourceException(
                    "Un paramètre avec le nom : " + request.getNomParametre() + " existe déjà");
        }

        Parametres parametre = new Parametres();
        parametre.setNomParametre(request.getNomParametre());
        parametre.setValeur(request.getValeur());

        Parametres saved = parametresRepository.save(parametre);
        return toResponse(saved);
    }

    @Transactional
    public ParametresResponse update(String nomParametre, ParametresRequest request) {
        Parametres parametre = parametresRepository.findById(nomParametre)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Paramètre introuvable avec le nom : " + nomParametre));

        if (!nomParametre.equals(request.getNomParametre()) &&
                parametresRepository.existsById(request.getNomParametre())) {
            throw new DuplicateResourceException(
                    "Un paramètre avec le nom : " + request.getNomParametre() + " existe déjà");
        }

        parametre.setNomParametre(request.getNomParametre());
        parametre.setValeur(request.getValeur());

        Parametres saved = parametresRepository.save(parametre);
        return toResponse(saved);
    }

    @Transactional
    public void delete(String nomParametre) {
        Parametres parametre = parametresRepository.findById(nomParametre)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Paramètre introuvable avec le nom : " + nomParametre));

        parametresRepository.delete(parametre);
    }

    private ParametresResponse toResponse(Parametres parametre) {
        return new ParametresResponse(parametre.getNomParametre(), parametre.getValeur());
    }
}