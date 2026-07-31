package com.Wiinvent.Lotus.domain.point.dto;

import com.Wiinvent.Lotus.domain.point.entity.PointTransactionType;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DeductPointRequest {

    @NotNull(message = "User ID không được để trống.")
    private Long userId;

    @NotNull(message = "Số điểm không được để trống.")
    @Min(value = 1, message = "Số điểm trừ phải lớn hơn 0.")
    private Long amount;

    @NotBlank(message = "Lý do trừ điểm không được để trống.")
    private String reason;
}
