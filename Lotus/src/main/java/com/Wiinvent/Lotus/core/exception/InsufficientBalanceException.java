package com.Wiinvent.Lotus.core.exception;

public class InsufficientBalanceException extends RuntimeException {

    public InsufficientBalanceException(long currentBalance, long deductAmount) {
        super(String.format(
                "Số dư Lotus+ không đủ. Số dư hiện tại: %d, số điểm cần trừ: %d.",
                currentBalance, deductAmount));
    }
}
