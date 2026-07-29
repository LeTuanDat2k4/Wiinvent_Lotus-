package com.Wiinvent.Lotus.domain.user.entity;

import com.Wiinvent.Lotus.core.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "users")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User extends BaseEntity<Long> {

    @Column(nullable = false, unique = true, length = 20)
    private String phone;

    @Column(name = "display_name", nullable = false)
    private String displayName;

    @Column(nullable = false)
    private String password;

    @Column(name = "avatar_url", length = 500)
    private String avatarUrl;

    @Column(name = "lotus_balance", nullable = false)
    @Builder.Default
    private Long lotusBalance = 0L;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private UserRole role;
}
