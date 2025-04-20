package com.medicalclinixproxy.client;

import com.medicalclinixproxy.model.ExternalVisitFilter;
import com.medicalclinixproxy.model.PageableDataDTO;
import com.medicalclinixproxy.model.VisitDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "medical-clinic")
public interface MedicalClinicClient {
    @GetMapping("/visits")
    PageableDataDTO<VisitDTO> getVisits(@SpringQueryMap ExternalVisitFilter visitFilter, Pageable pageable);

    @GetMapping("/visits")
    PageableDataDTO<VisitDTO> getPatientVisits(@RequestParam Long patientId, Pageable pageable);

    @PatchMapping("/{visitId}/patient/{patientId}")
    void registerPatientToVisit(@PathVariable("visitId") Long visitId, @PathVariable("patientId") Long patientId);
}
