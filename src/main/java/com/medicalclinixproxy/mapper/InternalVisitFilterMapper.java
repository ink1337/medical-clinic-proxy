package com.medicalclinixproxy.mapper;

import com.medicalclinixproxy.model.ExternalVisitFilter;
import com.medicalclinixproxy.model.InternalVisitFilter;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;

@Mapper(componentModel = "spring")
public interface InternalVisitFilterMapper {
    @Mapping(target = "onlyAvailable", expression = "java(true)")
    @Mapping(target = "startTime", source = "visitDate", qualifiedByName = "startTimeOfTheDay")
    @Mapping(target = "endTime", source = "visitDate", qualifiedByName = "endTimeOfTheDay")
    ExternalVisitFilter toExternalFilter(InternalVisitFilter internalVisitFilter);

    @Named("startTimeOfTheDay")
    default OffsetDateTime startTimeOfTheDay(LocalDate visitDate) {
        return (visitDate != null) ? visitDate.atStartOfDay().atOffset(ZoneOffset.UTC) : null;
    }

    @Named("endTimeOfTheDay")
    default OffsetDateTime endTimeOfTheDay(LocalDate visitDate) {
        return (visitDate != null) ? visitDate.atStartOfDay().plusDays(1).atOffset(ZoneOffset.UTC) : null;
    }
}