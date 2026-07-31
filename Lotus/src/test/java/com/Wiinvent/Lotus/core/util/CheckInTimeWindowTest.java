package com.Wiinvent.Lotus.core.util;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZonedDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class CheckInTimeWindowTest {

    @Test
    void shouldAcceptInclusiveBoundaries() {
        LocalDate date = LocalDate.of(2026, 7, 29);
        assertThat(CheckInTimeWindow.isWithinCheckInWindow(
                ZonedDateTime.of(date, LocalTime.of(11, 0), CheckInTimeWindow.ZONE_ID))).isTrue();
        assertThat(CheckInTimeWindow.isWithinCheckInWindow(
                ZonedDateTime.of(date, LocalTime.of(21, 0), CheckInTimeWindow.ZONE_ID))).isTrue();
    }

    @Test
    void shouldRejectOutsideWindow() {
        LocalDate date = LocalDate.of(2026, 7, 29);
        assertThat(CheckInTimeWindow.isWithinCheckInWindow(
                ZonedDateTime.of(date, LocalTime.of(11, 1), CheckInTimeWindow.ZONE_ID))).isFalse();
        assertThat(CheckInTimeWindow.isWithinCheckInWindow(
                ZonedDateTime.of(date, LocalTime.of(8, 59), CheckInTimeWindow.ZONE_ID))).isFalse();
    }
}
