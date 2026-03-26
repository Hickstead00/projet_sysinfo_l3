package com.amgboddel.backend.service;

import com.amgboddel.backend.dto.MaquetteRequest;
import com.amgboddel.backend.dto.MaquetteResponse;
import com.amgboddel.backend.dto.SemestreRequest;
import com.amgboddel.backend.dto.SemestreResponse;
import com.amgboddel.backend.entity.Maquette;
import com.amgboddel.backend.entity.Parametres;
import com.amgboddel.backend.entity.TypeMaquette;
import com.amgboddel.backend.exception.DuplicateResourceException;
import com.amgboddel.backend.exception.ResourceNotFoundException;
import com.amgboddel.backend.repository.MaquetteRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MaquetteService {

    private final MaquetteRepository maquetteRepository;
    private final SemestreService semestreService;
    private final ParametresService parametresService;

    @Transactional
    public List<MaquetteResponse> getAll() {
        return maquetteRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional
    public MaquetteResponse getById(Long id) {
        Maquette maquette = maquetteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Maquette non trouvée avec l'ID : " + id));
        return toResponse(maquette);
    }

    @Transactional
    public List<MaquetteResponse> search(String s) {
        return maquetteRepository.findByNomMaquetteContainingIgnoreCase(s)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional
    public MaquetteResponse create(MaquetteRequest request) {
        if (maquetteRepository.existsByNomMaquette(request.getNomMaquette())) {
            throw new DuplicateResourceException("Une maquette avec le nom '" + request.getNomMaquette() + "' existe déjà");
        }

        Maquette maquette = new Maquette();
        maquette.setNomMaquette(request.getNomMaquette());
        maquette.setTypeMaquette(request.getTypeMaquette());

        maquette = maquetteRepository.save(maquette);

        int nbSemestres = request.getTypeMaquette() == TypeMaquette.LICENCE ? 6 : 4;
        for (int i = 1; i <= nbSemestres; i++) {
            SemestreRequest semestreRequest = new SemestreRequest();
            semestreRequest.setNumeroSemestre(i);
            semestreService.create(maquette.getId(), semestreRequest);
        }

        // Recharger la maquette avec ses semestres
        maquette = maquetteRepository.findById(maquette.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Erreur lors de la création de la maquette"));

        return toResponse(maquette);
    }

    @Transactional
    public MaquetteResponse update(Long id, MaquetteRequest request) {
        Maquette maquette = maquetteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Maquette non trouvée avec l'ID : " + id));

        if (!maquette.getNomMaquette().equals(request.getNomMaquette())) {
            if (maquetteRepository.existsByNomMaquette(request.getNomMaquette())) {
                throw new DuplicateResourceException(
                        "Une maquette avec le nom '" + request.getNomMaquette() + "' existe déjà"
                );
            }
        }

        maquette.setNomMaquette(request.getNomMaquette());
        maquette.setTypeMaquette(request.getTypeMaquette());
        return toResponse(maquetteRepository.save(maquette));
    }

    @Transactional
    public void delete(Long id) {
        if (!maquetteRepository.existsById(id)) {
            throw new ResourceNotFoundException("Maquette non trouvée avec l'ID : " + id);
        }
        maquetteRepository.deleteById(id);
    }


    public MaquetteResponse toResponse(Maquette maquette) {
        List<SemestreResponse> semestresResponse = maquette.getSemestres() == null
                ? Collections.emptyList()
                : maquette.getSemestres().stream()
                .map(semestreService::toResponse)
                .toList();

        int nbSemestres = semestresResponse.size();

        int ectsTotal = semestresResponse.stream()
                .mapToInt(SemestreResponse::getEctsTotal)
                .sum();

        int volumeHoraireTotal = semestresResponse.stream()
                .mapToInt(SemestreResponse::getVolumeHoraireTotal)
                .sum();

        int nbUeTotal = semestresResponse.stream()
                .mapToInt(SemestreResponse::getNbUe)
                .sum();

        // Validation des ECTS (60 ECTS par année, donc 60 * nombre d'années)
        // On considère que 2 semestres = 1 année
        int nbAnnees = (nbSemestres + 1) / 2;
        int ectsAttendu = nbAnnees * 60;

        boolean respecteEcts = (ectsTotal == ectsAttendu);
        int ectsManquants = Math.max(0, ectsAttendu - ectsTotal);
        int ectsSurplus = Math.max(0, ectsTotal - ectsAttendu);

        Parametres params = parametresService.getOrCreate();
        double coutEstime = semestresResponse.stream()
                .flatMap(s -> s.getUes().stream())
                .mapToDouble(ue ->
                        ue.getCm() * params.getTarifCm() +
                        ue.getTd() * params.getTarifTd() +
                        ue.getTp() * params.getTarifTp()
                ).sum();

        double budgetMax = params.getBudgetMax();
        boolean budgetDepasse = budgetMax > 0 && coutEstime > budgetMax;

        return MaquetteResponse.builder()
                .id(maquette.getId())
                .nomMaquette(maquette.getNomMaquette())
                .typeMaquette(maquette.getTypeMaquette())
                .semestres(semestresResponse)
                .nbSemestres(nbSemestres)
                .ectsTotal(ectsTotal)
                .volumeHoraireTotal(volumeHoraireTotal)
                .nbUeTotal(nbUeTotal)
                .respecteEcts(respecteEcts)
                .ectsManquants(ectsManquants)
                .ectsSurplus(ectsSurplus)
                .coutEstime(coutEstime)
                .budgetMax(budgetMax)
                .budgetDepasse(budgetDepasse)
                .build();
    }
}
