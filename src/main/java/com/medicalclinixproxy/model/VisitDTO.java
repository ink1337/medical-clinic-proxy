package com.medicalclinixproxy.model;
import java.time.OffsetDateTime;

public record VisitDTO(
        Long id,
        OffsetDateTime startTime,
        OffsetDateTime endTime,
        DoctorSimpleDTO doctor,
        PatientDTO patient
) {
}
