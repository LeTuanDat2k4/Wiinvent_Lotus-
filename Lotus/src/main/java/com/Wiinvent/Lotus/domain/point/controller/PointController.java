package com.Wiinvent.Lotus.domain.point.controller;

import com.Wiinvent.Lotus.core.dto.ApiResponse;
import com.Wiinvent.Lotus.core.dto.PageResponse;
import com.Wiinvent.Lotus.core.security.SecurityUtils;
import com.Wiinvent.Lotus.domain.point.dto.DeductPointRequest;
import com.Wiinvent.Lotus.domain.point.dto.PointTransactionResponse;
import com.Wiinvent.Lotus.domain.point.service.PointService;
import com.Wiinvent.Lotus.core.ratelimit.RateLimit;
import com.Wiinvent.Lotus.core.ratelimit.RateLimitType;
import org.redisson.api.RateIntervalUnit;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/points")
@RequiredArgsConstructor
public class PointController {

    private final PointService pointService;

    @GetMapping("/history")
    public ResponseEntity<ApiResponse<PageResponse<PointTransactionResponse>>> getHistory(
            @PageableDefault(size = 20) Pageable pageable) {
        PageResponse<PointTransactionResponse> history =
                pointService.getHistory(SecurityUtils.getCurrentUserId(), pageable);
        return ResponseEntity.ok(ApiResponse.success(history));
    }

    @PostMapping("/deduct")
    @RateLimit(rate = 10, rateInterval = 1, rateIntervalUnit = RateIntervalUnit.MINUTES, type = RateLimitType.USER)
    public ResponseEntity<ApiResponse<PointTransactionResponse>> deductPoints(
            @Valid @RequestBody DeductPointRequest request) {
        PointTransactionResponse response = pointService.deductPoints(request);
        return ResponseEntity.ok(ApiResponse.success(response));
    }
}
