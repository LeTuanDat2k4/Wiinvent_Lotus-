package com.Wiinvent.Lotus.domain.checkin.service;

import com.Wiinvent.Lotus.domain.checkin.entity.CheckInRecord;
import com.Wiinvent.Lotus.domain.checkin.repository.CheckInRecordRepository;
import lombok.RequiredArgsConstructor;
import org.redisson.api.RSet;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CheckInCacheService {

    private static final String CHECK_IN_USER_KEY_PREFIX = "checkin:user:";
    private static final String EMPTY_MARKER = "__EMPTY__";
    private static final long CACHE_TTL_DAYS = 35;

    private final RedissonClient redissonClient;
    private final CheckInRecordRepository checkInRecordRepository;

    public boolean isCheckedInDate(Long userId, LocalDate date) {
        RSet<String> set = getCheckInSet(userId, YearMonth.from(date));
        return set.contains(date.toString());
    }

    public void addCheckInDate(Long userId, LocalDate date) {
        RSet<String> set = getCheckInSet(userId, YearMonth.from(date));
        set.remove(EMPTY_MARKER);
        set.add(date.toString());
        set.expire(CACHE_TTL_DAYS, TimeUnit.DAYS);
    }

    public int getCheckedInCountThisMonth(Long userId, YearMonth yearMonth) {
        RSet<String> set = getCheckInSet(userId, yearMonth);
        int size = set.size();
        return set.contains(EMPTY_MARKER) ? size - 1 : size;
    }

    public Set<String> getCheckedInDatesThisMonth(Long userId, YearMonth yearMonth) {
        RSet<String> set = getCheckInSet(userId, yearMonth);
        return set.readAll().stream()
                .filter(d -> !EMPTY_MARKER.equals(d))
                .collect(Collectors.toSet());
    }

    private RSet<String> getCheckInSet(Long userId, YearMonth yearMonth) {
        String key = CHECK_IN_USER_KEY_PREFIX + userId + ":" + yearMonth;
        RSet<String> set = redissonClient.getSet(key);
        if (!set.isExists()) {
            populateCacheFromDatabase(set, userId, yearMonth);
        }
        return set;
    }

    private synchronized void populateCacheFromDatabase(RSet<String> set, Long userId, YearMonth yearMonth) {
        if (!set.isExists()) {
            List<CheckInRecord> records = checkInRecordRepository.findByUserIdAndYearMonth(userId, yearMonth);
            Set<String> dates = records.stream()
                    .map(record -> record.getCheckInDate().toString())
                    .collect(Collectors.toSet());
            if (!dates.isEmpty()) {
                set.addAll(dates);
            } else {
                set.add(EMPTY_MARKER);
            }
            set.expire(CACHE_TTL_DAYS, TimeUnit.DAYS);
        }
    }
}
