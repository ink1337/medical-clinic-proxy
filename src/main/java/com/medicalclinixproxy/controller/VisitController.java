package com.medicalclinixproxy.controller;

import com.medicalclinixproxy.model.InternalVisitFilter;
import com.medicalclinixproxy.model.PageableDataDTO;
import com.medicalclinixproxy.model.VisitDTO;
import com.medicalclinixproxy.service.VisitService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.api.ErrorMessage;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/visits")
@RequiredArgsConstructor
@Tag(name = "Visit Operations")
public class VisitController {
    private final VisitService visitService;

    @Operation(summary = "Get all visits")
    @ApiResponse(
            responseCode = "200",
            description = "Returns paged visits"
    )
    @GetMapping
    public PageableDataDTO<VisitDTO> getVisits(
            @Parameter(name = "visitFilter", description = "Optional filters for visits") InternalVisitFilter internalVisitFilter,
            Pageable pageable
    ) {
        return visitService.getVisits(internalVisitFilter, pageable);
    }

    @Operation(summary = "Get all patient's visits")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Found patient visits",
                    useReturnTypeSchema = true
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Patient ID is null",
                    content = @Content(schema = @Schema(implementation = ErrorMessage.class))
            )
    })
    @GetMapping("/patient/{patientId}")
    public PageableDataDTO<VisitDTO> getPatientVisits(
            @Parameter(description = "Id of the patient") @PathVariable Long patientId,
            Pageable pageable
    ) {
        return visitService.getPatientVisits(patientId, pageable);
    }

    @Operation(summary = "Register patient to visit")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "204",
                    description = "Patient registered to visit"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Visit or patient ID is null",
                    content = @Content(schema = @Schema(implementation = ErrorMessage.class))
            )
    })
    @PatchMapping("/registerPatientToVisit/{visitId}/{patientId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void registerPatientToVisit(
            @Parameter(description = "Id of the visit") @PathVariable("visitId") Long visitId,
            @Parameter(description = "Id of the patient") @PathVariable("patientId") Long patientId
    ) {
        visitService.registerPatientToVisit(visitId, patientId);
    }
}