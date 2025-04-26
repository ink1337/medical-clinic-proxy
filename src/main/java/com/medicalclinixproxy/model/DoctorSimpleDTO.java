package com.medicalclinixproxy.model;


import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class DoctorSimpleDTO {
    private Long id;
    private String email;
    private String password;
    private String firstName;
    private String lastName;
    private String specialization;
}