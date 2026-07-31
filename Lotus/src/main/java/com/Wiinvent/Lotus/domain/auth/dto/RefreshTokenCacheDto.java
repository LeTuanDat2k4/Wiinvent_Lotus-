package com.Wiinvent.Lotus.domain.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RefreshTokenCacheDto implements Serializable {

    private Long userId;
    private String tokenHash;
    private LocalDateTime expiresAt;
    private Boolean revoked;
}
