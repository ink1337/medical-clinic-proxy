package com.medicalclinixproxy.exception;

import org.springframework.http.HttpStatusCode;
import org.springframework.web.server.ResponseStatusException;

public class PatientCannotRegisterException extends ResponseStatusException {

    public PatientCannotRegisterException(HttpStatusCode status, String reason) {
        super(status, reason);
    }
}
