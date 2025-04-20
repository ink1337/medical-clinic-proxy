package com.medicalclinixproxy.service;

import com.medicalclinixproxy.client.MedicalClinicClient;
import com.medicalclinixproxy.mapper.InternalVisitFilterMapper;
import com.medicalclinixproxy.model.ExternalVisitFilter;
import com.medicalclinixproxy.model.InternalVisitFilter;
import com.medicalclinixproxy.model.PageableDataDTO;
import com.medicalclinixproxy.model.VisitDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class VisitService {
    private final MedicalClinicClient medicalClinicClient;
    private final InternalVisitFilterMapper internalVisitFilterMapper;

    public PageableDataDTO<VisitDTO> getVisits(InternalVisitFilter internalVisitFilter, Pageable pageable) {
        ExternalVisitFilter externalVisitFilter = internalVisitFilterMapper.toExternalFilter(internalVisitFilter);
        return medicalClinicClient.getVisits(externalVisitFilter, pageable);
    }

    public PageableDataDTO<VisitDTO> getPatientVisits(Long patientId, Pageable pageable) {
        return medicalClinicClient.getPatientVisits(patientId, pageable);
    }

    public void registerPatientToVisit(Long visitId, Long patientId) {
        medicalClinicClient.registerPatientToVisit(visitId, patientId);
    }
}
