/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.hdda.controllers;

import com.hdda.dto.base.RouteVariantBaseDTO;
import com.hdda.dto.request.RouteVariantRequestDTO;
import com.hdda.pojo.RouteVariant;
import com.hdda.services.RouteService;
import com.hdda.services.RouteVariantService;
import com.hdda.services.StopService;
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
@RequestMapping("/variants")
public class RouteVariantController {

    @Autowired
    private RouteVariantService variantService;

    @Autowired
    private RouteService routeService;

    @Autowired
    private StopService stopService;

    /* ====================== LIST ====================== */
    @GetMapping
    public String list(@RequestParam Map<String, String> params,
            @RequestParam(value = "page", defaultValue = "1") int page,
            Model model) {

        int pageSize = 10;                         // giống RouteController
        params.put("page", String.valueOf(page));
        List<RouteVariantBaseDTO> data = variantService.getRouteVariants(params);
        long totalItems = data.size();             // Có thể thay bằng hàm đếm trong repo
        int totalPages = (int) Math.ceil((double) totalItems / pageSize);

        model.addAttribute("variants", data);
        model.addAttribute("currentPage", page);
        model.addAttribute("pages", totalPages);

        return "variants/list";
    }

    /* ====================== ADD FORM ====================== */
    @GetMapping("/add")
    public String addForm(@RequestParam(value = "routeId", required = false) Long routeId, Model model) {
        RouteVariantRequestDTO v = new RouteVariantRequestDTO();
        v.setRouteId((routeId));
        model.addAttribute("variant", v);
        model.addAttribute("formMode", "new");
        model.addAttribute("stops", stopService.getStops(null));
        model.addAttribute("routeId", routeId);
        return "variants/form";
    }

    /* ====================== SAVE (CREATE/UPDATE) ====================== */
    @PostMapping("/save")
    public String save(@ModelAttribute("variant") @Valid RouteVariantRequestDTO variant,
            BindingResult rs,
            RedirectAttributes ra,
            @RequestParam("mode") String formMode,
            Model model) {

        if (rs.hasErrors()) {
            model.addAttribute("formMode", formMode);
            return "variants/form";
        }

        variantService.addOrUpdateRouteVariant(variant);

        ra.addFlashAttribute("msg", "Saved!");
        return "redirect:/routes/" + variant.getRouteId() + "/variants";

    }

    /* ====================== EDIT FORM ====================== */
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") Long id, Model model) {
        RouteVariant rv = variantService.getRouteVariantById(id);
        RouteVariantRequestDTO v = new RouteVariantRequestDTO();
        v.setId(rv.getId());
        v.setDistanceKm(rv.getDistanceKm());
        v.setTimeMinute(rv.getTimeMinute());
        v.setSeq(rv.getSeq());
        v.setRouteId(rv.getRoute().getId());
        v.setStartId(rv.getStartStop().getId());
        v.setEndId(rv.getEndStop().getId());
        model.addAttribute("variant", v);
        model.addAttribute("formMode", "edit");
        model.addAttribute("stops", stopService.getStops(null));
        model.addAttribute("routeId", rv.getRoute().getId());

        return "variants/form";
    }

    /* ====================== DELETE ====================== */
    @GetMapping("/delete/{id}")
    public String delete(@RequestParam(value = "routeId", required = false) Long routeId,
            @PathVariable("id") Long id,
            RedirectAttributes ra) {
        variantService.deleteRouteVariant(id);
        ra.addFlashAttribute("msg", "Deleted!");
        if (routeId != null) {
            return "redirect:/routes/" + routeId + "/variants";
        }
        return "redirect:/variants";
    }
}
