package com.medicalclinixproxy.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

@Getter
@AllArgsConstructor
@Builder
public class PageableDataDTO<T> {
    List<T> data;
    private int totalPages;
    private int currentPage;
    private long totalElements;

    public static <T> PageableDataDTO<T> from(List<T> data, Page result, Pageable pageable) {
        return PageableDataDTO.<T>builder()
                .data(data)
                .totalPages(result.getTotalPages())
                .totalElements(result.getTotalElements())
                .currentPage(pageable.getPageNumber())
                .build();
    }
}
