package com.Wiinvent.Lotus.domain.point.dto;

import com.Wiinvent.Lotus.domain.point.entity.PointTransactionType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PointTransactionResponse {

    private Long id;
    private PointTransactionType type;
    private Long amount;
    private Long balanceAfter;
    private String reason;
    private LocalDateTime createdAt;
}
