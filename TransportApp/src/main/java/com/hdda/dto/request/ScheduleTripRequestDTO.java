package com.hdda.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import jakarta.validation.constraints.*;
import java.time.LocalDateTime;


@Getter
@Setter
@NoArgsConstructor
public class ScheduleTripRequestDTO {

    private Long id;

    @NotNull(message = "Thời gian khởi hành không được để trống")
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime departTime;

    @NotNull(message = "Thời gian đến không được để trống")
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime arriveTime;

    @NotNull(message = "Lịch trình không được để trống")
    private Long scheduleId;
}
