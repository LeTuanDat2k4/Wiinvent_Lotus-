package com.Wiinvent.Lotus.core.exception;

public class DuplicatePhoneException extends RuntimeException {

    public DuplicatePhoneException(String phone) {
        super("Số điện thoại " + phone + " đã được đăng ký. Vui lòng sử dụng số điện thoại khác.");
    }
}
