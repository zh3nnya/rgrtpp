package com.tpp.rgrtpp.controllers;

import com.tpp.rgrtpp.models.Block;
import com.tpp.rgrtpp.service.BlockService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

@Controller
@RequestMapping("/blocks")
public class BlockController {

    @Autowired
    private BlockService blockService;

    private boolean isAdmin(UserDetails userDetails) {
        return userDetails != null && userDetails.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
    }

    @GetMapping
    public String listBlocks(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        model.addAttribute("blocks", blockService.getAllBlocks());
        model.addAttribute("isAdmin", isAdmin(userDetails));
        return "blocks";
    }

    @GetMapping("/add")
    public String addBlockForm(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        if (!isAdmin(userDetails)) {
            return "access-denied";
        }
        model.addAttribute("block", new Block());
        return "add-block";
    }

    @PostMapping("/add")
    public String addBlock(@AuthenticationPrincipal UserDetails userDetails,
                         @Valid @ModelAttribute("block") Block block, 
                         BindingResult result) {
        if (!isAdmin(userDetails)) {
            return "access-denied";
        }
        if (result.hasErrors()) {
            return "add-block";
        }
        blockService.saveBlock(block);
        return "redirect:/blocks";
    }

    @GetMapping("/edit/{id}")
    public String editBlockForm(@AuthenticationPrincipal UserDetails userDetails,
                              @PathVariable("id") Integer id, 
                              Model model) {
        if (!isAdmin(userDetails)) {
            return "access-denied";
        }
        Block block = blockService.findBlockById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Block not found"));
        model.addAttribute("block", block);
        return "edit-block";
    }

    @PostMapping("/update/{id}")
    public String updateBlock(@AuthenticationPrincipal UserDetails userDetails,
                            @PathVariable("id") Integer id, 
                            @Valid @ModelAttribute("block") Block block,
                            BindingResult result) {
        if (!isAdmin(userDetails)) {
            return "access-denied";
        }
        if (result.hasErrors()) {
            return "edit-block";
        }
        block.setBlockId(id);
        blockService.updateBlock(block);
        return "redirect:/blocks";
    }

    @GetMapping("/delete/{id}")
    public String deleteBlock(@AuthenticationPrincipal UserDetails userDetails,
                            @PathVariable("id") Integer id) {
        if (!isAdmin(userDetails)) {
            return "access-denied";
        }
        blockService.deleteBlock(id);
        return "redirect:/blocks";
    }

    @ExceptionHandler(ResponseStatusException.class)
    public String handleNotFound(ResponseStatusException ex, Model model) {
        model.addAttribute("error", ex.getReason());
        return "error";
    }
}