package com.Wiinvent.Lotus.domain.checkin.controller;

import com.Wiinvent.Lotus.core.dto.ApiResponse;
import com.Wiinvent.Lotus.core.security.SecurityUtils;
import com.Wiinvent.Lotus.domain.checkin.dto.CheckInResponse;
import com.Wiinvent.Lotus.domain.checkin.dto.CheckInStatusResponse;
import com.Wiinvent.Lotus.domain.checkin.service.CheckInService;
import com.Wiinvent.Lotus.core.ratelimit.RateLimit;
import com.Wiinvent.Lotus.core.ratelimit.RateLimitType;
import org.redisson.api.RateIntervalUnit;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/check-ins")
@RequiredArgsConstructor
public class CheckInController {

    private final CheckInService checkInService;

    @GetMapping("/status")
    public ResponseEntity<ApiResponse<CheckInStatusResponse>> getStatus() {
        CheckInStatusResponse status = checkInService.getStatus(SecurityUtils.getCurrentUserId());
        return ResponseEntity.ok(ApiResponse.success(status));
    }

    @PostMapping
    @RateLimit(rate = 10, rateInterval = 1, rateIntervalUnit = RateIntervalUnit.MINUTES, type = RateLimitType.USER)
    public ResponseEntity<ApiResponse<CheckInResponse>> checkIn() {
        CheckInResponse response = checkInService.checkIn(SecurityUtils.getCurrentUserId());
        return ResponseEntity.ok(ApiResponse.success(response));
    }
}
