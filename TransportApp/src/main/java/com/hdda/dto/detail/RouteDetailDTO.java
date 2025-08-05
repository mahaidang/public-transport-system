/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.hdda.dto.detail;

import com.hdda.dto.base.RouteVariantBaseDTO;
import java.util.List;
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
public class RouteDetailDTO {

    private Long id;
    private String code;
    private String name;
    private String description;
    private Boolean isActive;
    private List<RouteVariantBaseDTO> variants;
}
