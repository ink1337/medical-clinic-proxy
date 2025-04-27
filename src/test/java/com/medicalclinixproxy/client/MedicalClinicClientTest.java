package com.medicalclinixproxy.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.matching.UrlPattern;
import com.medicalclinixproxy.exception.PatientCannotRegisterException;
import com.medicalclinixproxy.model.DoctorSimpleDTO;
import com.medicalclinixproxy.model.PageableDataDTO;
import com.medicalclinixproxy.model.PatientDTO;
import com.medicalclinixproxy.model.VisitDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.wiremock.spring.ConfigureWireMock;
import org.wiremock.spring.EnableWireMock;
import org.wiremock.spring.InjectWireMock;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.badRequest;
import static com.github.tomakehurst.wiremock.client.WireMock.equalTo;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.getRequestedFor;
import static com.github.tomakehurst.wiremock.client.WireMock.noContent;
import static com.github.tomakehurst.wiremock.client.WireMock.okJson;
import static com.github.tomakehurst.wiremock.client.WireMock.patch;
import static com.github.tomakehurst.wiremock.client.WireMock.patchRequestedFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlPathEqualTo;
import static com.github.tomakehurst.wiremock.client.WireMock.urlPathTemplate;
import static com.github.tomakehurst.wiremock.client.WireMock.verify;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@EnableWireMock(@ConfigureWireMock(name = "medical-clinic", port = 8080))
public class MedicalClinicClientTest {
    @InjectWireMock("medical-clinic")
    private WireMockServer server;
    @Autowired
    private MedicalClinicClient client;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void getVisits_ReturnsPageableContentDto() throws JsonProcessingException {
        Pageable pageable = PageRequest.of(0, 10);
        String response = objectMapper.writeValueAsString(buildPageableContent(pageable));
        server.stubFor(get(urlPathEqualTo("/visits"))
                .withQueryParam("size", equalTo(String.valueOf(pageable.getPageSize())))
                .withQueryParam("page", equalTo(String.valueOf(pageable.getPageNumber())))
                .willReturn(okJson(response)));

        PageableDataDTO<VisitDTO> pageableContentDto = client.getVisits(null, pageable);

        assertEquals(2, pageableContentDto.getTotalElements());
        assertEquals(1, pageableContentDto.getTotalPages());
        assertEquals(0, pageableContentDto.getCurrentPage());
        assertEquals(2, pageableContentDto.getData().size());
        for (int i = 0; i < pageableContentDto.getData().size(); i++) {
            VisitDTO visitDto = pageableContentDto.getData().get(i);
            assertEquals(i + 1, visitDto.id());
            assertEquals(getDefaultTime(), visitDto.startTime());
            assertEquals(getDefaultTime().plusHours(1), visitDto.endTime());
            DoctorSimpleDTO doctor = visitDto.doctor();
            assertEquals(i + 1, doctor.getId());
            assertEquals("email", doctor.getEmail());
            assertEquals("firstName", doctor.getFirstName());
            assertEquals("lastName", doctor.getLastName());
            assertEquals("specialization", doctor.getSpecialization());
            PatientDTO patient = visitDto.patient();
            assertEquals(i + 1, patient.getId());
            assertEquals("email", patient.getEmail());
            assertEquals("idCardNo", patient.getIdCardNo());
            assertEquals("firstName", patient.getFirstName());
            assertEquals("lastName", patient.getLastName());
            assertEquals("phoneNumber", patient.getPhoneNumber());
            assertEquals("2012-12-12", patient.getBirthday().toString());
        }
    }

    @Test
    public void getVisits_SourceNotAvailable_ReturnsEmptyPageableContentDto() {
        Pageable pageable = PageRequest.of(0, 10);
        server.stubFor(get(urlPathEqualTo("/visits"))
                .withQueryParam("size", equalTo(String.valueOf(pageable.getPageSize())))
                .withQueryParam("page", equalTo(String.valueOf(pageable.getPageNumber())))
                .willReturn(aResponse().withStatus(503)));

        PageableDataDTO<VisitDTO> pageableContentDto = client.getVisits(null, pageable);

        verify(3, getRequestedFor(urlPathEqualTo("/visits"))
                .withQueryParam("size", equalTo(String.valueOf(pageable.getPageSize())))
                .withQueryParam("page", equalTo(String.valueOf(pageable.getPageNumber()))));
        assertEquals(0, pageableContentDto.getTotalElements());
        assertEquals(0, pageableContentDto.getTotalPages());
        assertEquals(0, pageableContentDto.getCurrentPage());
        assertEquals(0, pageableContentDto.getData().size());
    }

    @Test
    public void getPatientVisits_ReturnsPageableContentDto() throws JsonProcessingException {
        Pageable pageable = PageRequest.of(0, 10);
        Long patientId = 1L;
        String response = objectMapper.writeValueAsString(buildPageableContent(pageable));
        server.stubFor(get(urlPathEqualTo("/visits"))
                .withQueryParam("size", equalTo(String.valueOf(pageable.getPageSize())))
                .withQueryParam("page", equalTo(String.valueOf(pageable.getPageNumber())))
                .withQueryParam("patientId", equalTo(String.valueOf(patientId)))
                .willReturn(okJson(response)));

        PageableDataDTO<VisitDTO> pageableContentDto = client.getPatientVisits(patientId, pageable);

        assertEquals(2, pageableContentDto.getTotalElements());
        assertEquals(1, pageableContentDto.getTotalPages());
        assertEquals(0, pageableContentDto.getCurrentPage());
        assertEquals(2, pageableContentDto.getData().size());
        for (int i = 0; i < pageableContentDto.getData().size(); i++) {
            VisitDTO visitDto = pageableContentDto.getData().get(i);
            assertEquals(i + 1, visitDto.id());
            assertEquals(getDefaultTime(), visitDto.startTime());
            assertEquals(getDefaultTime().plusHours(1), visitDto.endTime());
            DoctorSimpleDTO doctor = visitDto.doctor();
            assertEquals(i + 1, doctor.getId());
            assertEquals("email", doctor.getEmail());
            assertEquals("firstName", doctor.getFirstName());
            assertEquals("lastName", doctor.getLastName());
            assertEquals("specialization", doctor.getSpecialization());
            PatientDTO patient = visitDto.patient();
            assertEquals(i + 1, patient.getId());
            assertEquals("email", patient.getEmail());
            assertEquals("idCardNo", patient.getIdCardNo());
            assertEquals("firstName", patient.getFirstName());
            assertEquals("lastName", patient.getLastName());
            assertEquals("phoneNumber", patient.getPhoneNumber());
            assertEquals("2012-12-12", patient.getBirthday().toString());
        }
    }

    @Test
    public void getPatientVisits_SourceNotAvailable_ReturnsEmptyPageableContentDto() {
        Pageable pageable = PageRequest.of(0, 10);
        Long patientId = 1L;
        server.stubFor(get(urlPathEqualTo("/visits"))
                .withQueryParam("size", equalTo(String.valueOf(pageable.getPageSize())))
                .withQueryParam("page", equalTo(String.valueOf(pageable.getPageNumber())))
                .withQueryParam("patientId", equalTo(String.valueOf(patientId)))
                .willReturn(aResponse().withStatus(503)));

        PageableDataDTO<VisitDTO> pageableContentDto = client.getPatientVisits(patientId, pageable);

        verify(3, getRequestedFor(urlPathEqualTo("/visits"))
                .withQueryParam("size", equalTo(String.valueOf(pageable.getPageSize())))
                .withQueryParam("page", equalTo(String.valueOf(pageable.getPageNumber())))
                .withQueryParam("patientId", equalTo(String.valueOf(patientId))));
        assertEquals(0, pageableContentDto.getTotalElements());
        assertEquals(0, pageableContentDto.getTotalPages());
        assertEquals(0, pageableContentDto.getCurrentPage());
        assertEquals(0, pageableContentDto.getData().size());
    }

    @Test
    public void registerPatientToVisit_ReturnsStatus204OnSuccess() {
        Long patientId = 1L;
        Long visitId = 1L;
        UrlPattern pattern = urlPathTemplate("/registerPatientToVisit/{visitId}/{patientId}");
        server.stubFor(patch(pattern)
                .withPathParam("visitId", equalTo(String.valueOf(visitId)))
                .withPathParam("patientId", equalTo(String.valueOf(patientId)))
                .willReturn(noContent()));

        client.registerPatientToVisit(visitId, patientId);

        verify(1, patchRequestedFor(pattern)
                .withPathParam("visitId", equalTo(String.valueOf(visitId)))
                .withPathParam("patientId", equalTo(String.valueOf(patientId))));
    }

    @Test
    public void registerPatientToVisit_RequestFailed_ThrowsPatientCannotRegisterException() {
        Long patientId = 1L;
        Long visitId = 1L;
        UrlPattern pattern = urlPathTemplate("/registerPatientToVisit/{visitId}/{patientId}");
                server.stubFor(patch(pattern)
                .withPathParam("visitId", equalTo(String.valueOf(visitId)))
                .withPathParam("patientId", equalTo(String.valueOf(patientId)))
                .willReturn(badRequest()));

        PatientCannotRegisterException exception = assertThrows(PatientCannotRegisterException.class, () -> client.registerPatientToVisit(visitId, patientId));

        verify(1, patchRequestedFor(pattern)
                .withPathParam("visitId", equalTo(String.valueOf(visitId)))
                .withPathParam("patientId", equalTo(String.valueOf(patientId))));
        assertEquals("Patient could not be registered.", exception.getReason());
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
    }

    public static Clock getTestClock() {
        return Clock.fixed(Instant.parse("2012-12-12T12:00:00Z"), ZoneOffset.UTC);
    }

    public static OffsetDateTime getDefaultTime() {
        return OffsetDateTime.now(getTestClock());
    }

    public static PageableDataDTO<VisitDTO> buildPageableContent(Pageable pageable) {
        return PageableDataDTO.<VisitDTO>builder()
                .totalElements(2)
                .totalPages((int) Math.ceil((double) 2 / pageable.getPageSize()))
                .currentPage(pageable.getPageNumber())
                .data(List.of(buildVisitDto(1L), buildVisitDto(2L)))
                .build();
    }

    public static VisitDTO buildVisitDto(Long id) {
        return new VisitDTO(
                id,
                getDefaultTime(),
                getDefaultTime().plusHours(1),
                DoctorSimpleDTO.builder()
                        .email("email")
                        .firstName("firstName")
                        .id(id)
                        .lastName("lastName")
                        .specialization("specialization")
                        .build(),
                PatientDTO.builder()
                        .id(id)
                        .email("email")
                        .idCardNo("idCardNo")
                        .firstName("firstName")
                        .lastName("lastName")
                        .phoneNumber("phoneNumber")
                        .birthday(LocalDate.of(2012, 12, 12))
                        .build());
    }
}