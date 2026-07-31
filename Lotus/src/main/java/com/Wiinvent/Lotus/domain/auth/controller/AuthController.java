package com.Wiinvent.Lotus.domain.auth.controller;

import com.Wiinvent.Lotus.core.dto.ApiResponse;
import com.Wiinvent.Lotus.domain.auth.dto.LoginRequest;
import com.Wiinvent.Lotus.domain.auth.dto.RefreshTokenRequest;
import com.Wiinvent.Lotus.domain.auth.dto.TokenResponse;
import com.Wiinvent.Lotus.domain.auth.service.AuthService;
import com.Wiinvent.Lotus.core.ratelimit.RateLimit;
import com.Wiinvent.Lotus.core.ratelimit.RateLimitType;
import org.redisson.api.RateIntervalUnit;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    @RateLimit(rate = 5, rateInterval = 1, rateIntervalUnit = RateIntervalUnit.MINUTES, type = RateLimitType.IP)
    public ResponseEntity<ApiResponse<TokenResponse>> login(@Valid @RequestBody LoginRequest request) {
        return ResponseEntity.ok(ApiResponse.success(authService.login(request)));
    }

    @PostMapping("/refresh")
    @RateLimit(rate = 10, rateInterval = 1, rateIntervalUnit = RateIntervalUnit.MINUTES, type = RateLimitType.IP)
    public ResponseEntity<ApiResponse<TokenResponse>> refresh(@Valid @RequestBody RefreshTokenRequest request) {
        return ResponseEntity.ok(ApiResponse.success(authService.refresh(request)));
    }
}
