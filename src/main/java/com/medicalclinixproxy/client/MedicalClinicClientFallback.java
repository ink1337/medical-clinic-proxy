package com.medicalclinixproxy.client;

import com.medicalclinixproxy.exception.PatientCannotRegisterException;
import com.medicalclinixproxy.model.ExternalVisitFilter;
import com.medicalclinixproxy.model.PageableDataDTO;
import com.medicalclinixproxy.model.VisitDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class MedicalClinicClientFallback implements MedicalClinicClient {

    @Override
    public PageableDataDTO<VisitDTO> getVisits(ExternalVisitFilter visitFilter, Pageable pageable) {
        return PageableDataDTO.<VisitDTO>builder()
                .totalElements(0)
                .totalPages(0)
                .currentPage(pageable.getPageNumber())
                .data(List.of())
                .build();
    }

    @Override
    public PageableDataDTO<VisitDTO> getPatientVisits(Long patientId, Pageable pageable) {
        return PageableDataDTO.<VisitDTO>builder()
                .totalElements(0)
                .totalPages(0)
                .currentPage(pageable.getPageNumber())
                .data(List.of())
                .build();
    }

    @Override
    public void registerPatientToVisit(Long visitId, Long patientId) {
        throw new PatientCannotRegisterException(HttpStatus.BAD_REQUEST, "Patient could not be registered.");
    }
}