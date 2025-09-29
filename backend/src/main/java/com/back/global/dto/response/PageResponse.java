package com.back.global.dto.response;

import org.springframework.data.domain.Page;

import java.util.List;

public record PageResponse<T>(
        List<T> content,        // 실제 데이터
        int page,               // 현재 페이지 번호 (0부터 시작)
        int size,               // 페이지 크기
        long totalElements,     // 전체 데이터 개수
        int totalPages,         // 전체 페이지 수
        boolean first,          // 첫 페이지 여부
        boolean last,           // 마지막 페이지 여부
        boolean hasNext,        // 다음 페이지 존재 여부
        boolean hasPrevious     // 이전 페이지 존재 여부
) {
    public static <T> PageResponse<T> of(Page<T> page) {
        return new PageResponse<>(
                page.getContent(),
                page.getNumber() + 1,
                page.getSize(),
                page.getTotalElements(),
                page.getTotalPages(),
                page.isFirst(),
                page.isLast(),
                page.hasNext(),
                page.hasPrevious()
        );
    }
}
