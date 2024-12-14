package com.tpp.rgrtpp.controllers;

import com.tpp.rgrtpp.models.Room;
import com.tpp.rgrtpp.service.BlockService;
import com.tpp.rgrtpp.service.RoomService;
import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.ui.Model;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/rooms")
public class RoomController {

    @Autowired
    private RoomService roomService;

    @Autowired
    private BlockService blockService;

    private boolean isAdmin(UserDetails userDetails) {
        return userDetails != null && userDetails.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
    }

    @GetMapping
    public String listRooms(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        model.addAttribute("rooms", roomService.getAllRooms());
        model.addAttribute("isAdmin", isAdmin(userDetails));
        return "rooms";
    }

    @GetMapping("/add")
    public String addRoomForm(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        if (!isAdmin(userDetails)) {
            return "access-denied";
        }
        model.addAttribute("room", new Room());
        model.addAttribute("blocks", blockService.getAllBlocks());
        return "add-room";
    }

    @PostMapping("/add")
    public String addRoom(@AuthenticationPrincipal UserDetails userDetails,
                          @Valid @ModelAttribute("room") Room room,
                          BindingResult result, Model model) {
        if (!isAdmin(userDetails)) {
            return "access-denied";
        }
        if (result.hasErrors()) {
            model.addAttribute("blocks", blockService.getAllBlocks());
            return "add-room";
        }
        roomService.saveRoom(room);
        return "redirect:/rooms";
    }

    @GetMapping("/edit/{id}")
    public String editRoomForm(@AuthenticationPrincipal UserDetails userDetails,
                               @PathVariable("id") Integer id,
                               Model model) {
        if (!isAdmin(userDetails)) {
            return "access-denied";
        }
        Room room = roomService.findRoomById(id).orElse(null);
        if (room != null) {
            model.addAttribute("room", room);
            model.addAttribute("blocks", blockService.getAllBlocks());
            return "edit-room";
        } else {
            return "redirect:/rooms";
        }
    }

    @PostMapping("/update/{id}")
    public String updateRoom(@AuthenticationPrincipal UserDetails userDetails,
                             @PathVariable("id") Integer id,
                             @Valid @ModelAttribute("room") Room room,
                             BindingResult result, Model model) {
        if (!isAdmin(userDetails)) {
            return "access-denied";
        }
        if (result.hasErrors()) {
            model.addAttribute("blocks", blockService.getAllBlocks());
            return "edit-room";
        }
        room.setRoomId(id);
        roomService.updateRoom(room);
        return "redirect:/rooms";
    }

    @GetMapping("/delete/{id}")
    public String deleteRoom(@AuthenticationPrincipal UserDetails userDetails,
                             @PathVariable("id") Integer id) {
        if (!isAdmin(userDetails)) {
            return "access-denied";
        }
        roomService.deleteRoom(id);
        return "redirect:/rooms";
    }

    public RoomService getRoomService() {
        return roomService;
    }

    public void setRoomService(RoomService roomService) {
        this.roomService = roomService;
    }

    public BlockService getBlockService() {
        return blockService;
    }

    public void setBlockService(BlockService blockService) {
        this.blockService = blockService;
    }
}
