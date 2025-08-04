/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.hdda.dto.base;

import java.util.Date;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

/**
 *
 * @author mahai
 */
@Getter
@Setter
public class ScheduleBaseDTO {
    private Long id;
    private String name;
    private String dayGroup;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date effectiveFrom;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date effectiveTo;
}
