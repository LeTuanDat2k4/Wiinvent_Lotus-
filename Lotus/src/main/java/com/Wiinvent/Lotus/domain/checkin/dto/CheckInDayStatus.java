package com.Wiinvent.Lotus.domain.checkin.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CheckInDayStatus {

    private Integer dayNumber;
    private Long rewardPoint;
    private boolean checkedIn;
}
