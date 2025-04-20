package com.medicalclinixproxy.model;

import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

public record InternalVisitFilter(
        Long visitId,
        Long doctorId,
        String doctorSpecialization,
        @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate visitDate
) {
}