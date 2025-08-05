package com.hdda.dto.request;

import jakarta.validation.constraints.*;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RouteVariantRequestDTO {

    private Long id;

    @NotBlank(message = "Tên biến thể không được để trống")
    @Size(max = 100, message = "Tên tối đa 100 ký tự")
    private String name;

    @PositiveOrZero(message = "Quãng đường phải là số không âm")
    private double distanceKm;

    @PositiveOrZero(message = "Thời gian phải là số không âm")
    private int timeMinute;

    @Positive(message = "Thứ tự phải là số dương")
    private int seq;

    @NotNull(message = "Tuyến không được để trống")
    private Long routeId;

    @NotNull(message = "Điểm bắt đầu không được để trống")
    private Long startId;

    @NotNull(message = "Điểm kết thúc không được để trống")
    private Long endId;
}
