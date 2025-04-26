package com.medicalclinixproxy.model;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Builder
@Data
public class PatientDTO {
    private final Long id;
    private final String email;
    private final String firstName;
    private final String lastName;
    private final LocalDate birthday;
    private final String idCardNo;
    private final String phoneNumber;
}
