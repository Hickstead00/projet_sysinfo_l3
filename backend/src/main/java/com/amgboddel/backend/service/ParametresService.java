package com.amgboddel.backend.service;

import com.amgboddel.backend.dto.ParametresRequest;
import com.amgboddel.backend.dto.ParametresResponse;
import com.amgboddel.backend.entity.Parametres;
import com.amgboddel.backend.repository.ParametresRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ParametresService {

    private final ParametresRepository parametresRepository;

    public ParametresResponse get() {
        return toResponse(getOrCreate());
    }

    @Transactional
    public ParametresResponse update(ParametresRequest request) {
        Parametres parametres = getOrCreate();
        parametres.setTarifCm(request.getTarifCm());
        parametres.setTarifTd(request.getTarifTd());
        parametres.setTarifTp(request.getTarifTp());
        parametres.setBudgetMax(request.getBudgetMax());
        parametres.setAlertesEctsActives(request.getAlertesEctsActives());
        parametres.setAlertesPrerequisActives(request.getAlertesPrerequisActives());
        return toResponse(parametresRepository.save(parametres));
    }

    @Transactional
    public Parametres getOrCreate() {
        return parametresRepository.findById(1L)
                .orElseGet(() -> parametresRepository.save(new Parametres()));
    }

    private ParametresResponse toResponse(Parametres parametres) {
        return new ParametresResponse(
                parametres.getTarifCm(),
                parametres.getTarifTd(),
                parametres.getTarifTp(),
                parametres.getBudgetMax(),
                parametres.getAlertesEctsActives(),
                parametres.getAlertesPrerequisActives()
        );
    }
}
