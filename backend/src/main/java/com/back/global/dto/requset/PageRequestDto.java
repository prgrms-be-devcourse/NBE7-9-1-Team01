package com.back.global.dto.requset;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

public record PageRequestDto (
        int page,           // 요청 페이지 번호 (기본 0 or 1 선택 가능)
        int size,           // 페이지 크기
        String sort,        // 정렬 기준 필드명 (예: "orderDate")
        String direction    // 정렬 방향 (asc / desc)
) {

    public PageRequestDto {
        if (page < 0) page = 0;
        if (size <= 0) size = 10;
        if (sort == null || sort.isBlank()) sort = "id";
        if (direction == null || direction.isBlank()) direction = "desc";
    }

    public Pageable toPageable() {
        Sort.Direction dir = direction.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;
        return PageRequest.of(page, size, Sort.by(dir, sort));
    }
}
