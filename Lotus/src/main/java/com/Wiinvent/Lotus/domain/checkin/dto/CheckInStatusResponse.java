package com.Wiinvent.Lotus.domain.checkin.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CheckInStatusResponse {

    private CheckInButtonState buttonState;
    private String timeWindowMessage;
    private int checkedInDaysThisMonth;
    private int maxDaysPerMonth;
    private List<CheckInDayStatus> dailyCheckInList;
}
