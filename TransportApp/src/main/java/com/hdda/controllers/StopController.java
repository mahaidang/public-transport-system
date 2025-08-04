/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.hdda.controllers;

import com.hdda.pojo.Stop;
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
@RequestMapping("/stops")
public class StopController {

    @Autowired
    private StopService stopService;

    @GetMapping
    public String list(@RequestParam Map<String, String> params,
            @RequestParam(value = "page", defaultValue = "1") int page,
            Model model) {

        int pageSize = 10;
        params.put("page", String.valueOf(page));

        List<Stop> data = stopService.getStops(params);
        long totalItems = stopService.countStops(params);  // Hàm bạn cần có
        int totalPages = (int) Math.ceil((double) totalItems / pageSize);

        model.addAttribute("stops", data);
        model.addAttribute("currentPage", page);
        model.addAttribute("pages", totalPages);
        return "stops/list";
    }

    @GetMapping("/add")
    public String addForm(Model model) {
        model.addAttribute("stop", new Stop());
        model.addAttribute("formMode", "new");

        return "stops/form";
    }

    @PostMapping("/save")
    public String save(@ModelAttribute("stop") @Valid Stop stop,
            BindingResult rs, RedirectAttributes ra, @RequestParam("mode") String formMode,
            Model model) {
        if (rs.hasErrors()) {
            model.addAttribute("formMode", formMode);
            return "stops/form";
        }

        if (formMode.equals("new")) {
//            if (stopService.existsProduct(product.getId())) {
//                model.addAttribute("formMode", formMode);
//                model.addAttribute("cates", cateService.getAll());
//                model.addAttribute("duplicated", "Duplicated Id");
//                return "product-form";
//            }
        }

        stopService.addOrUpdateStop(stop);
        ra.addFlashAttribute("msg", "Saved!");
        return "redirect:/stops";
    }

    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable("id") Long id, Model model) {
        Stop r = stopService.getStopById(id);
        model.addAttribute("stop", r);
        model.addAttribute("formMode", "edit");

        return "stops/form";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable("id") Long id, RedirectAttributes ra) {
        stopService.deleteStop(id);
        ra.addFlashAttribute("msg", "Deleted!");
        return "redirect:/stops";
    }
}
