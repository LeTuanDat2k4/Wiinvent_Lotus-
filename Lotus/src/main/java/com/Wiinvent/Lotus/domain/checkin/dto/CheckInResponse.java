package com.Wiinvent.Lotus.domain.checkin.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CheckInResponse {

    private LocalDate checkInDate;
    private Integer dayNumberInMonth;
    private Long rewardPoint;
    private Long lotusBalance;
}
