package com.back.domain.member.entity;

import com.back.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@AllArgsConstructor
public class Member extends BaseEntity {
        private String email;
        private String password;
        private String address;
        private String postcode;

        @Enumerated(EnumType.STRING)
        private Role role;
}
