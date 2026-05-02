package com.amgboddel.backend.service;

import com.amgboddel.backend.dto.SemestreRequest;
import com.amgboddel.backend.dto.SemestreResponse;
import com.amgboddel.backend.dto.UEResponse;
import com.amgboddel.backend.entity.Maquette;
import com.amgboddel.backend.entity.Semestre;
import com.amgboddel.backend.entity.UE;
import com.amgboddel.backend.exception.DuplicateResourceException;
import com.amgboddel.backend.exception.PrerequisiteViolationException;
import com.amgboddel.backend.exception.ResourceNotFoundException;
import com.amgboddel.backend.repository.MaquetteRepository;
import com.amgboddel.backend.repository.SemestreRepository;
import com.amgboddel.backend.repository.UERepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class SemestreService {

    private final SemestreRepository semestreRepository;
    private final MaquetteRepository maquetteRepository;
    private final UERepository ueRepository;
    private final UEService ueService;
    private final ParametresService parametresService;

    @Transactional
    public List<SemestreResponse> getAll() {
        return semestreRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional
    public SemestreResponse getById(Long semestreId, Long maquetteId) {
        Semestre semestre = semestreRepository.findById(semestreId)
                .orElseThrow(() -> new ResourceNotFoundException("Semestre non trouvé avec l'ID : " + semestreId));

        validateSemestreBelongsToMaquette(semestre, maquetteId);

        return toResponse(semestre);
    }

    @Transactional
    public List<SemestreResponse> getByMaquetteId(Long maquetteId) {
        return semestreRepository.findByMaquetteId(maquetteId)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional
    public SemestreResponse create(Long maquetteId, SemestreRequest request) {
        // Vérifier que la maquette existe
        Maquette maquette = maquetteRepository.findById(maquetteId)
                .orElseThrow(() -> new ResourceNotFoundException("Maquette non trouvée avec l'ID : " + maquetteId));

        // Vérifier qu'il n'existe pas déjà un semestre avec ce numéro dans cette maquette
        if (semestreRepository.existsByMaquetteIdAndNumeroSemestre(maquetteId, request.getNumeroSemestre())) {
            throw new DuplicateResourceException(
                    "Un semestre avec le numéro " + request.getNumeroSemestre() +
                            " existe déjà dans cette maquette"
            );
        }

        Semestre semestre = new Semestre();
        semestre.setMaquette(maquette);
        requestToEntity(request, semestre);

        return toResponse(semestreRepository.save(semestre));
    }

    @Transactional
    public SemestreResponse update(Long semestreId, Long maquetteId, SemestreRequest request) {
        Semestre semestre = semestreRepository.findById(semestreId)
                .orElseThrow(() -> new ResourceNotFoundException("Semestre non trouvé avec l'ID : " + semestreId));

        validateSemestreBelongsToMaquette(semestre, maquetteId);

        if (!semestre.getNumeroSemestre().equals(request.getNumeroSemestre())) {
            if (semestreRepository.existsByMaquetteIdAndNumeroSemestre(
                    maquetteId,
                    request.getNumeroSemestre())) {
                throw new DuplicateResourceException(
                        "Un semestre avec le numéro " + request.getNumeroSemestre() +
                                " existe déjà dans cette maquette"
                );
            }
        }

        requestToEntity(request, semestre);
        return toResponse(semestreRepository.save(semestre));
    }

    @Transactional
    public void delete(Long semestreId, Long maquetteId) {
        Semestre semestre = semestreRepository.findById(semestreId)
                .orElseThrow(() -> new ResourceNotFoundException("Semestre non trouvé avec l'ID : " + semestreId));

        validateSemestreBelongsToMaquette(semestre, maquetteId);

        semestreRepository.deleteById(semestreId);
    }

    @Transactional
    public SemestreResponse addUE(Long semestreId, Long maquetteId, Long ueId) {
        Semestre semestre = semestreRepository.findById(semestreId)
                .orElseThrow(() -> new ResourceNotFoundException("Semestre non trouvé avec l'ID : " + semestreId));

        validateSemestreBelongsToMaquette(semestre, maquetteId);

        UE ue = ueRepository.findById(ueId)
                .orElseThrow(() -> new ResourceNotFoundException("UE non trouvée avec l'ID : " + ueId));

        List<Semestre> semestresMaquette = semestreRepository.findByMaquetteId(maquetteId);

        boolean dejaPresente = semestresMaquette.stream()
                .flatMap(s -> s.getUes().stream())
                .anyMatch(u -> u.getId().equals(ueId));

        if (dejaPresente) {
            throw new DuplicateResourceException(
                    "L'UE " + ueId + " est déjà présente dans un semestre de cette maquette");
        }


        if (parametresService.getOrCreate().getAlertesPrerequisActives()) {
            Map<Long, Integer> ueIdVersNumeroSemestre = new HashMap<>();
            for (Semestre s : semestresMaquette) {
                for (UE u : s.getUes()) {
                    ueIdVersNumeroSemestre.put(u.getId(), s.getNumeroSemestre());
                }
            }

            List<String> prerequisManquants = new ArrayList<>();
            for (UE prereq : ue.getPrerequis()) {
                Integer numeroSemestrePrereq = ueIdVersNumeroSemestre.get(prereq.getId());
                if (numeroSemestrePrereq == null || numeroSemestrePrereq >= semestre.getNumeroSemestre()) {
                    prerequisManquants.add(prereq.getNomUe());
                }
            }

            if (!prerequisManquants.isEmpty()) {
                throw new PrerequisiteViolationException(
                        "Prérequis non respectés : " + String.join(", ", prerequisManquants) +
                        " doit être placé dans un semestre antérieur.");
            }
        }

        semestre.getUes().add(ue);
        return toResponse(semestreRepository.save(semestre));
    }

    @Transactional
    public SemestreResponse removeUE(Long semestreId, Long maquetteId, Long ueId) {
        Semestre semestre = semestreRepository.findById(semestreId)
                .orElseThrow(() -> new ResourceNotFoundException("Semestre non trouvé avec l'ID : " + semestreId));

        validateSemestreBelongsToMaquette(semestre, maquetteId);

        UE ue = ueRepository.findById(ueId)
                .orElseThrow(() -> new ResourceNotFoundException("UE non trouvée avec l'ID : " + ueId));


        if (parametresService.getOrCreate().getAlertesPrerequisActives()) {
            List<String> ueDependantes = new ArrayList<>();
            for (Semestre s : semestreRepository.findByMaquetteId(maquetteId)) {
                if (s.getNumeroSemestre() > semestre.getNumeroSemestre()) {
                    for (UE u : s.getUes()) {
                        boolean dependDeCetteUe = u.getPrerequis().stream()
                                .anyMatch(p -> p.getId().equals(ueId));
                        if (dependDeCetteUe) {
                            ueDependantes.add(u.getNomUe() + " (S" + s.getNumeroSemestre() + ")");
                        }
                    }
                }
            }

            if (!ueDependantes.isEmpty()) {
                throw new PrerequisiteViolationException(
                        "Impossible de retirer \"" + ue.getNomUe() + "\" : " +
                        String.join(", ", ueDependantes) + " en dépend.");
            }
        }

        semestre.getUes().remove(ue);
        return toResponse(semestreRepository.save(semestre));
    }


    private void validateSemestreBelongsToMaquette(Semestre semestre, Long maquetteId) {
        if (!semestre.getMaquette().getId().equals(maquetteId)) {
            throw new ResourceNotFoundException(
                    "Le semestre " + semestre.getId() +
                            " n'appartient pas à la maquette " + maquetteId
            );
        }
    }

    private void requestToEntity(SemestreRequest request, Semestre semestre) {
        semestre.setNumeroSemestre(request.getNumeroSemestre());
    }

    public SemestreResponse toResponse(Semestre semestre) {
        List<UEResponse> uesResponse = semestre.getUes() == null
                ? Collections.emptyList()
                : semestre.getUes().stream()
                .map(ueService::toResponseLight) // Utiliser une version light pour éviter les boucles infinies
                .toList();

        int ectsTotal = uesResponse.stream()
                .mapToInt(UEResponse::getEcts)
                .sum();

        int volumeCm = uesResponse.stream()
                .mapToInt(UEResponse::getCm)
                .sum();

        int volumeTd = uesResponse.stream()
                .mapToInt(UEResponse::getTd)
                .sum();

        int volumeTp = uesResponse.stream()
                .mapToInt(UEResponse::getTp)
                .sum();

        int volumeTotal = volumeCm + volumeTd + volumeTp;

        boolean respecteEcts = (ectsTotal == 30);
        int ectsManquants = Math.max(0, 30 - ectsTotal);
        int ectsSurplus = Math.max(0, ectsTotal - 30);

        // Vérifier les prérequis non respectés
        List<String> prerequisNonRespectes = new ArrayList<>();
        if (semestre.getUes() != null && semestre.getMaquette() != null) {
            Map<Long, Integer> ueIdVersNumero = new HashMap<>();
            for (Semestre s : semestreRepository.findByMaquetteId(semestre.getMaquette().getId())) {
                for (UE u : s.getUes()) {
                    ueIdVersNumero.put(u.getId(), s.getNumeroSemestre());
                }
            }

            for (UE ue : semestre.getUes()) {
                for (UE prereq : ue.getPrerequis()) {
                    Integer numPrereq = ueIdVersNumero.get(prereq.getId());
                    if (numPrereq == null || numPrereq >= semestre.getNumeroSemestre()) {
                        prerequisNonRespectes.add(ue.getNomUe() + " requiert " + prereq.getNomUe());
                    }
                }
            }
        }

        return SemestreResponse.builder()
                .id(semestre.getId())
                .numeroSemestre(semestre.getNumeroSemestre())
                .ues(uesResponse)
                .nbUe(uesResponse.size())
                .ectsTotal(ectsTotal)
                .volumeHoraireCm(volumeCm)
                .volumeHoraireTd(volumeTd)
                .volumeHoraireTp(volumeTp)
                .volumeHoraireTotal(volumeTotal)
                .respecteEcts(respecteEcts)
                .ectsManquants(ectsManquants)
                .ectsSurplus(ectsSurplus)
                .prerequisNonRespectes(prerequisNonRespectes)
                .build();
    }
}
