package com.Wiinvent.Lotus.domain.checkin.service;

import com.Wiinvent.Lotus.core.exception.BusinessException;
import com.Wiinvent.Lotus.core.exception.ResourceNotFoundException;
import com.Wiinvent.Lotus.core.util.CheckInTimeWindow;
import com.Wiinvent.Lotus.domain.checkin.dto.CheckInButtonState;
import com.Wiinvent.Lotus.domain.checkin.dto.CheckInDayStatus;
import com.Wiinvent.Lotus.domain.checkin.dto.CheckInResponse;
import com.Wiinvent.Lotus.domain.checkin.dto.CheckInStatusResponse;
import com.Wiinvent.Lotus.domain.checkin.entity.CheckInRecord;
import com.Wiinvent.Lotus.domain.checkin.repository.CheckInRecordRepository;
import com.Wiinvent.Lotus.domain.point.service.PointService;
import com.Wiinvent.Lotus.domain.reward.entity.RewardConfig;
import com.Wiinvent.Lotus.domain.reward.service.RewardService;
import com.Wiinvent.Lotus.domain.user.entity.User;
import com.Wiinvent.Lotus.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CheckInService {

    private static final String CHECK_IN_LOCK_PREFIX = "checkin:lock:";

    private final CheckInRecordRepository checkInRecordRepository;
    private final RewardService rewardService;
    private final CheckInCacheService checkInCacheService;
    private final UserService userService;
    private final PointService pointService;
    private final RedissonClient redissonClient;

    public CheckInStatusResponse getStatus(Long userId) {
        ZonedDateTime now = CheckInTimeWindow.now();
        LocalDate today = now.toLocalDate();
        YearMonth currentMonth = YearMonth.from(today);

        List<RewardConfig> rewardConfigs = rewardService.getAllRewardConfigs();
        List<CheckInRecord> records = checkInRecordRepository.findByUserIdAndYearMonth(userId, currentMonth);
        Map<Integer, CheckInRecord> recordByDayNumber = records.stream()
                .collect(Collectors.toMap(CheckInRecord::getDayNumberInMonth, Function.identity()));

        List<CheckInDayStatus> dailyList = rewardConfigs.stream()
                .map(config -> CheckInDayStatus.builder()
                        .dayNumber(config.getDayNumber())
                        .rewardPoint(config.getRewardPoint())
                        .checkedIn(recordByDayNumber.containsKey(config.getDayNumber()))
                        .build())
                .toList();

        int checkedInDays = checkInCacheService.getCheckedInCountThisMonth(userId, currentMonth);
        boolean checkedInToday = checkInCacheService.isCheckedInDate(userId, today);
        CheckInButtonState buttonState = resolveButtonState(now, checkedInToday, checkedInDays);

        return CheckInStatusResponse.builder()
                .buttonState(buttonState)
                .timeWindowMessage(CheckInTimeWindow.TIME_WINDOW_MESSAGE)
                .checkedInDaysThisMonth(checkedInDays)
                .maxDaysPerMonth(CheckInTimeWindow.MAX_CHECK_INS_PER_MONTH)
                .dailyCheckInList(dailyList)
                .build();
    }

    @Transactional
    public CheckInResponse checkIn(Long userId) {
        ZonedDateTime now = CheckInTimeWindow.now();
        LocalDate today = now.toLocalDate();
        YearMonth currentMonth = YearMonth.from(today);

        if (!CheckInTimeWindow.isWithinCheckInWindow(now)) {
            throw new BusinessException(CheckInTimeWindow.TIME_WINDOW_MESSAGE);
        }
        if (checkInCacheService.isCheckedInDate(userId, today)) {
            throw new BusinessException("Bạn đã điểm danh hôm nay.");
        }

        long checkedInDays = checkInCacheService.getCheckedInCountThisMonth(userId, currentMonth);
        if (checkedInDays >= CheckInTimeWindow.MAX_CHECK_INS_PER_MONTH) {
            throw new BusinessException("Bạn đã điểm danh đủ 7 ngày trong tháng này.");
        }

        String lockKey = CHECK_IN_LOCK_PREFIX + userId + ":" + today;
        RLock lock = redissonClient.getLock(lockKey);
        boolean acquired = false;
        try {
            acquired = lock.tryLock(5, 30, TimeUnit.SECONDS);
            if (!acquired) {
                throw new BusinessException("Hệ thống đang xử lý yêu cầu điểm danh. Vui lòng thử lại.");
            }

            if (checkInCacheService.isCheckedInDate(userId, today)) {
                throw new BusinessException("Bạn đã điểm danh hôm nay.");
            }

            int dayNumberInMonth = (int) checkedInDays + 1;
            RewardConfig rewardConfig = rewardService.getRewardConfigByDay(dayNumberInMonth)
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "RewardConfig", "dayNumber", dayNumberInMonth));

            User user = userService.getUserById(userId);
            long newBalance = user.getLotusBalance() + rewardConfig.getRewardPoint();
            user.setLotusBalance(newBalance);

            CheckInRecord record = CheckInRecord.builder()
                    .userId(userId)
                    .checkInDate(today)
                    .dayNumberInMonth(dayNumberInMonth)
                    .rewardPoint(rewardConfig.getRewardPoint())
                    .build();
            checkInRecordRepository.save(record);

            checkInCacheService.addCheckInDate(userId, today);

            pointService.recordEarn(
                    userId,
                    rewardConfig.getRewardPoint(),
                    newBalance,
                    "Điểm danh ngày " + dayNumberInMonth);

            userService.evictUserProfileCache(userId);

            return CheckInResponse.builder()
                    .checkInDate(today)
                    .dayNumberInMonth(dayNumberInMonth)
                    .rewardPoint(rewardConfig.getRewardPoint())
                    .lotusBalance(newBalance)
                    .build();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new BusinessException("Không thể xử lý yêu cầu điểm danh. Vui lòng thử lại.");
        } finally {
            if (acquired && lock.isHeldByCurrentThread()) {
                lock.unlock();
            }
        }
    }

    private CheckInButtonState resolveButtonState(ZonedDateTime now, boolean checkedInToday, int checkedInDays) {
        if (checkedInToday) {
            return CheckInButtonState.CHECKED_IN;
        }
        if (checkedInDays >= CheckInTimeWindow.MAX_CHECK_INS_PER_MONTH) {
            return CheckInButtonState.MONTHLY_LIMIT_REACHED;
        }
        if (!CheckInTimeWindow.isWithinCheckInWindow(now)) {
            return CheckInButtonState.OUT_OF_WINDOW;
        }
        return CheckInButtonState.CAN_CHECK_IN;
    }
}
