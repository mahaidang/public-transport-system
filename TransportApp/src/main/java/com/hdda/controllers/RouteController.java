/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.hdda.controllers;

import com.hdda.pojo.Route;
import com.hdda.pojo.RouteVariant;
import com.hdda.services.RouteService;
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
@RequestMapping("/routes")
public class RouteController {

    @Autowired
    private RouteService routeService;

    @GetMapping
    public String list(@RequestParam Map<String, String> params,
            @RequestParam(value = "page", defaultValue = "1") int page,
            Model model) {

        int pageSize = 10;
        params.put("page", String.valueOf(page));

        List<Route> data = routeService.getRoutes(params);
        long totalItems = routeService.countRoutes(params);  // Hàm bạn cần có
        int totalPages = (int) Math.ceil((double) totalItems / pageSize);

        model.addAttribute("routes", data);
        model.addAttribute("currentPage", page);
        model.addAttribute("pages", totalPages);
        return "routes/list";
    }

    @GetMapping("/add")
    public String addForm(Model model) {
        model.addAttribute("route", new Route());
        model.addAttribute("formMode", "new");

        return "routes/form";
    }

    @PostMapping("/save")
    public String save(@ModelAttribute("route") @Valid Route route,
            BindingResult rs, RedirectAttributes ra, @RequestParam("mode") String formMode,
            Model model) {
        if (rs.hasErrors()) {
            model.addAttribute("formMode", formMode);
            return "routes/form";
        }

        if (formMode.equals("new")) {
//            if (routeService.existsProduct(product.getId())) {
//                model.addAttribute("formMode", formMode);
//                model.addAttribute("cates", cateService.getAll());
//                model.addAttribute("duplicated", "Duplicated Id");
//                return "product-form";
//            }
        }

        routeService.addOrUpdateRoute(route);
        ra.addFlashAttribute("msg", "Saved!");
        return "redirect:/routes";
    }

    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable("id") Long id, Model model) {
        Route r = routeService.getRouteById(id);
        model.addAttribute("route", r);
        model.addAttribute("formMode", "edit");

        return "routes/form";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable("id") Long id, RedirectAttributes ra) {
        routeService.deleteRoute(id);
        ra.addFlashAttribute("msg", "Deleted!");
        return "redirect:/routes";
    }

    @GetMapping("/{routeId}/variants")
    public String listRouteVariants(@PathVariable("routeId") Long routeId, Model model) {
        List<RouteVariant> variants = this.routeService.getVariantsByRouteId(routeId);
        model.addAttribute("variants", variants);
        model.addAttribute("routeId", routeId);
        return "variants/list";
    }

}
