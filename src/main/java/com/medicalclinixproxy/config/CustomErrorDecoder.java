package com.medicalclinixproxy.config;


import feign.FeignException;
import feign.Response;
import feign.RetryableException;
import feign.codec.ErrorDecoder;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CustomErrorDecoder implements ErrorDecoder {
    @Override
    public Exception decode(String methodKey, Response response) {
        log.warn("Feign call failed. Status: {}", response.status());
        FeignException exception = FeignException.errorStatus(methodKey, response);
        boolean serviceIsUnavailable = response.status() == 503;
        return serviceIsUnavailable
                ? new RetryableException(response.status(), exception.getMessage(), response.request().httpMethod(), exception, (Long) null, response.request())
                : exception;
    }
}