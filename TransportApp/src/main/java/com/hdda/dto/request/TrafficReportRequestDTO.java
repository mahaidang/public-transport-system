/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.hdda.dto.request;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 *
 * @author mahai
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TrafficReportRequestDTO {

    @NotNull(message = "Latitude is required")
    @DecimalMin(value = "-90.0", message = "Latitude must be ≥ -90.0")
    @DecimalMax(value = "90.0", message = "Latitude must be ≤ 90.0")
    private BigDecimal latitude;

    @NotNull(message = "Longitude is required")
    @DecimalMin(value = "-180.0", message = "Longitude must be ≥ -180.0")
    @DecimalMax(value = "180.0", message = "Longitude must be ≤ 180.0")
    private BigDecimal longitude;

    @Size(max = 255, message = "Description must be under 255 characters")
    private String description;

    @Size(max = 255, message = "Image URL must be under 255 characters")
    private String imageUrl;
}
