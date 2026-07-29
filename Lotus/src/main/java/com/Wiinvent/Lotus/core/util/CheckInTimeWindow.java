package com.Wiinvent.Lotus.core.util;

import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

public final class CheckInTimeWindow {

    public static final ZoneId ZONE_ID = ZoneId.of("Asia/Ho_Chi_Minh");
    public static final int MAX_CHECK_INS_PER_MONTH = 7;
    public static final String TIME_WINDOW_MESSAGE = "Khung giờ điểm danh:9h-11h và 19h-21h";

    private static final LocalTime MORNING_START = LocalTime.of(9, 0);
    private static final LocalTime MORNING_END = LocalTime.of(11, 0);
    private static final LocalTime EVENING_START = LocalTime.of(19, 0);
    private static final LocalTime EVENING_END = LocalTime.of(21, 0);

    private CheckInTimeWindow() {
    }

    public static ZonedDateTime now() {
        return ZonedDateTime.now(ZONE_ID);
    }

    public static boolean isWithinCheckInWindow(ZonedDateTime dateTime) {
        LocalTime time = dateTime.toLocalTime();
        boolean inMorning = !time.isBefore(MORNING_START) && !time.isAfter(MORNING_END);
        boolean inEvening = !time.isBefore(EVENING_START) && !time.isAfter(EVENING_END);
        return inMorning || inEvening;
    }
}
