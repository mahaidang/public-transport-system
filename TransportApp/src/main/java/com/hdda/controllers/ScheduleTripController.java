/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.hdda.controllers;

import com.hdda.dto.request.ScheduleTripRequestDTO;
import com.hdda.services.ScheduleTripService;
import jakarta.validation.Valid;
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
@RequestMapping("/schedules/{scheduleId}/trips")
public class ScheduleTripController {

    @Autowired
    private ScheduleTripService tripService;

    /* LIST */
    @GetMapping
    public String list(@PathVariable("scheduleId") Long scheduleId, Model model) {
        model.addAttribute("trips", tripService.getTripsBySchedule(scheduleId));
        model.addAttribute("scheduleId", scheduleId);
        return "trips/list";
    }

    /* ADD FORM */
    @GetMapping("/add")
    public String addForm(@PathVariable("scheduleId") Long scheduleId, Model model) {
        ScheduleTripRequestDTO dto = new ScheduleTripRequestDTO();
        dto.setScheduleId(scheduleId);
        model.addAttribute("trip", dto);
        model.addAttribute("formMode", "new");
        return "trips/form";
    }

    /* EDIT FORM */
    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable("scheduleId") Long scheduleId,
            @PathVariable("id") Long id,
            Model model) {
        model.addAttribute("trip", tripService.getTripDTO(id));
        model.addAttribute("formMode", "edit");
        return "trips/form";
    }

    /* SAVE */
    @PostMapping
    public String save(@PathVariable("scheduleId") Long scheduleId,
            @ModelAttribute("trip") @Valid ScheduleTripRequestDTO dto,
            BindingResult rs,
            RedirectAttributes ra,
            @RequestParam("mode") String formMode,
            Model model) {
        if (rs.hasErrors()) {
            model.addAttribute("formMode", formMode);
            return "trips/form";
        }
        tripService.addOrUpdate(dto);
        ra.addFlashAttribute("msg", "Saved!");
        return "redirect:/schedules/" + scheduleId + "/trips";
    }

    /* DELETE */
    @PostMapping("/{id}/delete")
    public String delete(@PathVariable("scheduleId") Long scheduleId,
            @PathVariable("id") Long id,
            RedirectAttributes ra) {
        tripService.delete(id);
        ra.addFlashAttribute("msg", "Deleted!");
        return "redirect:/schedules/" + scheduleId + "/trips";
    }
}
