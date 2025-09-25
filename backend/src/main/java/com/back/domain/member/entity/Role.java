package com.back.domain.member.entity;

import lombok.Getter;

@Getter
public enum Role {
    ROLE_ADMIN("관리자"),
    ROLE_USER("멤버");

    private final String roleName;

    Role(String roleName) {
        this.roleName = roleName;
    }
}
