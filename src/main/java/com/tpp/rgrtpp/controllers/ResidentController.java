package com.tpp.rgrtpp.controllers;

import com.tpp.rgrtpp.models.Resident;
import com.tpp.rgrtpp.service.ResidentService;
import com.tpp.rgrtpp.service.RoomService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@Controller
@RequestMapping("/residents")
public class ResidentController {

    @Autowired
    private ResidentService residentService;

    @Autowired
    private RoomService roomService;

    private boolean isAdmin(UserDetails userDetails) {
        return userDetails != null && userDetails.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
    }

    @GetMapping
    public String listResidents(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        model.addAttribute("residents", residentService.getAllResidents());
        model.addAttribute("isAdmin", isAdmin(userDetails));
        return "residents";
    }

    @GetMapping("/add")
    public String addResidentForm(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        if (!isAdmin(userDetails)) {
            return "access-denied";
        }
        model.addAttribute("resident", new Resident());
        model.addAttribute("rooms", roomService.getAllRooms());
        return "add-resident";
    }

    @PostMapping("/add")
    public String addResident(@AuthenticationPrincipal UserDetails userDetails,
                              @Valid @ModelAttribute("resident") Resident resident,
                              BindingResult result, Model model) {
        if (!isAdmin(userDetails)) {
            return "access-denied";
        }
        if (result.hasErrors()) {
            model.addAttribute("rooms", roomService.getAllRooms());
            return "add-resident";
        }
        residentService.saveResident(resident);
        return "redirect:/residents";
    }

    @GetMapping("/edit/{id}")
    public String editResidentForm(@AuthenticationPrincipal UserDetails userDetails,
                                   @PathVariable("id") Integer id, Model model) {
        if (!isAdmin(userDetails)) {
            return "access-denied";
        }
        Resident resident = residentService.findResidentById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Resident not found"));
        model.addAttribute("resident", resident);
        model.addAttribute("rooms", roomService.getAllRooms());
        return "edit-resident";
    }

    @PostMapping("/update/{id}")
    public String updateResident(@AuthenticationPrincipal UserDetails userDetails,
                                 @PathVariable("id") Integer id,
                                 @Valid @ModelAttribute("resident") Resident resident,
                                 BindingResult result, Model model) {
        if (!isAdmin(userDetails)) {
            return "access-denied";
        }
        if (result.hasErrors()) {
            model.addAttribute("rooms", roomService.getAllRooms());
            return "edit-resident";
        }
        resident.setResidentId(id);
        residentService.updateResident(resident);
        return "redirect:/residents";
    }

    @GetMapping("/delete/{id}")
    public String deleteResident(@AuthenticationPrincipal UserDetails userDetails,
                                 @PathVariable("id") Integer id) {
        if (!isAdmin(userDetails)) {
            return "access-denied";
        }
        residentService.deleteResident(id);
        return "redirect:/residents";
    }

    @ExceptionHandler(ResponseStatusException.class)
    public String handleNotFound(ResponseStatusException ex, Model model) {
        model.addAttribute("error", ex.getReason());
        return "error";
    }
}