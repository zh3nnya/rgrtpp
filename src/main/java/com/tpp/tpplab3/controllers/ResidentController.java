package com.tpp.tpplab3.controllers;

import com.tpp.tpplab3.models.Resident;
import com.tpp.tpplab3.service.ResidentService;
import com.tpp.tpplab3.service.RoomService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/residents")
public class ResidentController {

    @Autowired
    private ResidentService residentService;

    @Autowired
    private RoomService roomService;

    @GetMapping
    public String listResidents(Model model) {
        model.addAttribute("residents", residentService.getAllResidents());
        return "residents";
    }

    @GetMapping("/add")
    public String addResidentForm(Model model) {
        model.addAttribute("resident", new Resident());
        model.addAttribute("rooms", roomService.getAllRooms());
        return "add-resident";
    }

    @PostMapping("/add")
    public String addResident(@Valid @ModelAttribute("resident") Resident resident, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("rooms", roomService.getAllRooms());
            return "add-resident";
        }
        residentService.saveResident(resident);
        return "redirect:/residents";
    }

    @GetMapping("/edit/{id}")
    public String editResidentForm(@PathVariable("id") Integer id, Model model) {
        Resident resident = residentService.findResidentById(id).orElse(null);
        if (resident != null) {
            model.addAttribute("resident", resident);
            model.addAttribute("rooms", roomService.getAllRooms());
            return "edit-resident";
        } else {
            return "redirect:/residents";
        }
    }

    @PostMapping("/update/{id}")
    public String updateResident(@PathVariable("id") Integer id, @Valid @ModelAttribute("resident") Resident resident,
                                 BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("rooms", roomService.getAllRooms());
            return "edit-resident";
        }
        resident.setResidentId(id);
        residentService.updateResident(resident);
        return "redirect:/residents";
    }

    @GetMapping("/delete/{id}")
    public String deleteResident(@PathVariable("id") Integer id) {
        residentService.deleteResident(id);
        return "redirect:/residents";
    }
}
