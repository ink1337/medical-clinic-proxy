package com.medicalclinixproxy.model;


import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class DoctorSimpleDTO {
    private String email;
    private String password;
    private String firstName;
    private String lastName;
    private String speciality;
}