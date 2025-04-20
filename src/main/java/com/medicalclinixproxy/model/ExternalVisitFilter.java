package com.medicalclinixproxy.model;

import lombok.Builder;
import lombok.Data;

import java.time.OffsetDateTime;

@Builder
@Data
public class ExternalVisitFilter {
    private Long visitId;
    private Long doctorId;
    private String doctorSpecialization;
    private Boolean onlyAvailable;
    private OffsetDateTime startTime;
    private OffsetDateTime endTime;
}