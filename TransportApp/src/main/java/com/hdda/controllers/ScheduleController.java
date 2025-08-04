/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.hdda.controllers;

import com.hdda.dto.base.ScheduleBaseDTO;
import com.hdda.dto.request.ScheduleRequestDTO;
import com.hdda.pojo.Schedule;
import com.hdda.services.RouteVariantService;
import com.hdda.services.ScheduleService;
import jakarta.validation.Valid;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 *
 * @author mahai
 */
@Controller
@RequestMapping("/schedules")
public class ScheduleController {

    @Autowired
    private ScheduleService scheduleService;
    @Autowired
    private RouteVariantService variantService;   // để hiển thị thông tin variant

    /* =========== LIST =========== */
    @GetMapping
    public String list(@RequestParam Map<String, String> params,
            @RequestParam(value = "page", defaultValue = "1") int page,
            Model model) {

        int pageSize = 10;
        params.put("page", String.valueOf(page));

        List<ScheduleBaseDTO> data = scheduleService.getSchedules(params);
        long totalItems = scheduleService.countSchedules(params);
        int totalPages = (int) Math.ceil((double) totalItems / pageSize);
        
        model.addAttribute("schedules", data);
        model.addAttribute("currentPage", page);
        model.addAttribute("pages", totalPages);
        return "schedules/list";
    }

    /* =========== ADD FORM =========== */
    @GetMapping("/add")
    public String addForm(@RequestParam(value = "variantId", required = false) Long variantId, Model model) {
        ScheduleRequestDTO dto = new ScheduleRequestDTO();
        dto.setVariantId(variantId);
        model.addAttribute("schedule", dto);
        model.addAttribute("formMode", "new");
        model.addAttribute(variantId);
        return "schedules/form";
    }

    /* =========== SAVE (CREATE / UPDATE) =========== */
    @PostMapping("/save")
    public String save(@RequestParam(value = "variantId", required = false) Long variantId,
            @ModelAttribute("schedule") @Valid ScheduleRequestDTO dto,
            BindingResult rs,
            RedirectAttributes ra,
            @RequestParam("mode") String formMode,
            Model model) {

        if (rs.hasErrors()) {
            model.addAttribute("formMode", formMode);
            return "schedules/form";
        }

        dto.setVariantId(variantId);          // đảm bảo đúng khóa ngoài
        scheduleService.addOrUpdateSchedule(dto);

        ra.addFlashAttribute("msg", "Saved!");
        return "redirect:/schedules";
    }

    /* =========== EDIT FORM =========== */
    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable("id") Long id, Model model) {
        Schedule s = scheduleService.getScheduleById(id);
        ScheduleRequestDTO d = new ScheduleRequestDTO(
                s.getId(), s.getDayGroup(), s.getEffectiveFrom(),
                s.getEffectiveTo(), s.getNote(), s.getVariant().getId()
        );
        model.addAttribute("schedule", d);
        model.addAttribute("formMode", "edit");
        return "schedules/form";
    }

    /* =========== DELETE =========== */
    @GetMapping("/delete/{id}")
    public String delete(
            @PathVariable("id") Long id,
            RedirectAttributes ra) {
        scheduleService.deleteSchedule(id);
        ra.addFlashAttribute("msg", "Deleted!");
        return "redirect:/schedules";
    }
}
