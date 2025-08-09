package com.hdda.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import jakarta.validation.constraints.*;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ScheduleRequestDTO {

    private Long id;

    @Size(max = 7, message = "Nhóm ngày không được vượt quá 7 ký tự (VD: Mon-Fri)")
    private String dayGroup;

    @NotNull(message = "Ngày bắt đầu hiệu lực không được để trống")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date effectiveFrom;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date effectiveTo;

    @Size(max = 255, message = "Ghi chú không được vượt quá 255 ký tự")
    private String note;

    @NotNull(message = "Biến thể tuyến không được để trống")
    private Long variantId;
}
