package com.tpp.tpplab3.controllers;

import com.tpp.tpplab3.models.Room;
import com.tpp.tpplab3.service.BlockService;
import com.tpp.tpplab3.service.RoomService;
import jakarta.validation.Valid;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.ColumnMapRowMapper;
import org.springframework.stereotype.Controller;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/rooms")
public class RoomController {

    @Autowired
    private RoomService roomService;

    @Autowired
    private BlockService blockService;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @GetMapping
    public String listRooms(Model model) {
        model.addAttribute("rooms", roomService.getAllRooms());
        return "rooms";
    }

    @GetMapping("/add")
    public String addRoomForm(Model model) {
        model.addAttribute("room", new Room());
        model.addAttribute("blocks", blockService.getAllBlocks());
        return "add-room";
    }

    @PostMapping("/add")
    public String addRoom(@Valid @ModelAttribute("room") Room room, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("blocks", blockService.getAllBlocks());
            return "add-room";
        }
        roomService.saveRoom(room);
        return "redirect:/rooms";
    }

    @GetMapping("/edit/{id}")
    public String editRoomForm(@PathVariable("id") Integer id, Model model) {
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
    public String updateRoom(@PathVariable("id") Integer id, @Valid @ModelAttribute("room") Room room,
                             BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("blocks", blockService.getAllBlocks());
            return "edit-room";
        }
        room.setRoomId(id);
        roomService.updateRoom(room);
        return "redirect:/rooms";
    }

    @GetMapping("/delete/{id}")
    public String deleteRoom(@PathVariable("id") Integer id) {
        roomService.deleteRoom(id);
        return "redirect:/rooms";
    }

    @PostMapping("/execute-query")
    public String executeQuery(@RequestParam("sqlQuery") String sqlQuery, Model model) {
        try {
            List<Map<String, Object>> result = jdbcTemplate.query(sqlQuery, new ColumnMapRowMapper());
            if (!result.isEmpty()) {
                model.addAttribute("queryResult", Map.of(
                    "columns", result.get(0).keySet(),
                    "rows", result.stream().map(Map::values).toList()
                ));
            } else {
                model.addAttribute("queryResult", null);
            }
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Error executing query: " + e.getMessage());
        }
        model.addAttribute("rooms", roomService.getAllRooms());
        return "rooms";
    }

}
