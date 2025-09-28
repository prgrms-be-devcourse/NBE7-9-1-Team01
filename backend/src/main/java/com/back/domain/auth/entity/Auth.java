package com.back.domain.auth.entity;

import com.back.global.entity.BaseEntity;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class Auth extends BaseEntity {

    private String email;
    private String password;
}
