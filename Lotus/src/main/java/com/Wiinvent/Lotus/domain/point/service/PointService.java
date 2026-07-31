package com.Wiinvent.Lotus.domain.point.service;

import com.Wiinvent.Lotus.core.dto.PageResponse;
import com.Wiinvent.Lotus.core.exception.InsufficientBalanceException;
import com.Wiinvent.Lotus.domain.point.dto.DeductPointRequest;
import com.Wiinvent.Lotus.domain.point.dto.PointTransactionResponse;
import com.Wiinvent.Lotus.domain.point.entity.PointTransaction;
import com.Wiinvent.Lotus.domain.point.entity.PointTransactionType;
import com.Wiinvent.Lotus.domain.point.repository.PointTransactionRepository;
import com.Wiinvent.Lotus.domain.user.entity.User;
import com.Wiinvent.Lotus.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PointService {

    private final PointTransactionRepository pointTransactionRepository;
    private final UserService userService;

    public PageResponse<PointTransactionResponse> getHistory(Long userId, Pageable pageable) {
        return PageResponse.from(
                pointTransactionRepository.findByUserIdOrderByCreatedAtDesc(userId, pageable)
                        .map(this::toResponse));
    }

    @Transactional
    public PointTransactionResponse deductPoints(DeductPointRequest request) {
        User user = userService.getUserById(request.getUserId());
        if (user.getLotusBalance() < request.getAmount()) {
            throw new InsufficientBalanceException(user.getLotusBalance(), request.getAmount());
        }

        long newBalance = user.getLotusBalance() - request.getAmount();
        user.setLotusBalance(newBalance);

        PointTransaction transaction = PointTransaction.builder()
                .userId(user.getId())
                .type(PointTransactionType.DEDUCT)
                .amount(request.getAmount())
                .balanceAfter(newBalance)
                .reason(request.getReason())
                .build();

        PointTransactionResponse response = toResponse(pointTransactionRepository.save(transaction));
        userService.evictUserProfileCache(user.getId());
        return response;
    }

    @Transactional
    public PointTransaction recordEarn(Long userId, Long amount, Long balanceAfter, String reason) {
        PointTransaction transaction = PointTransaction.builder()
                .userId(userId)
                .type(PointTransactionType.EARN)
                .amount(amount)
                .balanceAfter(balanceAfter)
                .reason(reason)
                .build();
        return pointTransactionRepository.save(transaction);
    }

    private PointTransactionResponse toResponse(PointTransaction transaction) {
        return PointTransactionResponse.builder()
                .id(transaction.getId())
                .type(transaction.getType())
                .amount(transaction.getAmount())
                .balanceAfter(transaction.getBalanceAfter())
                .reason(transaction.getReason())
                .createdAt(transaction.getCreatedAt())
                .build();
    }
}
