package com.Wiinvent.Lotus.domain.checkin.repository;

import com.Wiinvent.Lotus.core.repository.BaseRepository;
import com.Wiinvent.Lotus.domain.checkin.entity.CheckInRecord;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import java.util.Optional;

@Repository
public interface CheckInRecordRepository extends BaseRepository<CheckInRecord, Long> {

    Optional<CheckInRecord> findByUserIdAndCheckInDate(Long userId, LocalDate checkInDate);

    boolean existsByUserIdAndCheckInDate(Long userId, LocalDate checkInDate);

    long countByUserIdAndCheckInDateBetween(Long userId, LocalDate start, LocalDate end);

    List<CheckInRecord> findByUserIdAndCheckInDateBetweenOrderByDayNumberInMonthAsc(
            Long userId, LocalDate start, LocalDate end);

    default long countByUserIdAndYearMonth(Long userId, YearMonth yearMonth) {
        return countByUserIdAndCheckInDateBetween(
                userId, yearMonth.atDay(1), yearMonth.atEndOfMonth());
    }

    default List<CheckInRecord> findByUserIdAndYearMonth(Long userId, YearMonth yearMonth) {
        return findByUserIdAndCheckInDateBetweenOrderByDayNumberInMonthAsc(
                userId, yearMonth.atDay(1), yearMonth.atEndOfMonth());
    }
}
