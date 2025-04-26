package com.medicalclinixproxy.exception;

public class PatientCannotRegisterException extends RuntimeException{


    public PatientCannotRegisterException(String message) {
        super(message);
    }
}
