package com.tpp.tpplab3.controllers;

import com.tpp.tpplab3.models.Block;
import com.tpp.tpplab3.service.BlockService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/blocks")
public class BlockController {

    @Autowired
    private BlockService blockService;

    @GetMapping
    public String listBlocks(Model model) {
        model.addAttribute("blocks", blockService.getAllBlocks());
        return "blocks";
    }

    @GetMapping("/add")
    public String addBlockForm(Model model) {
        model.addAttribute("block", new Block());
        return "add-block";
    }

    @PostMapping("/add")
    public String addBlock(@Valid @ModelAttribute("block") Block block, BindingResult result) {
        if (result.hasErrors()) {
            return "add-block";
        }
        blockService.saveBlock(block);
        return "redirect:/blocks";
    }

    @GetMapping("/edit/{id}")
    public String editBlockForm(@PathVariable("id") Integer id, Model model) {
        Block block = blockService.findBlockById(id).orElse(null);
        if (block != null) {
            model.addAttribute("block", block);
            return "edit-block";
        } else {
            return "redirect:/blocks";
        }
    }

    @PostMapping("/update/{id}")
    public String updateBlock(@PathVariable("id") Integer id, @Valid @ModelAttribute("block") Block block,
                              BindingResult result) {
        if (result.hasErrors()) {
            return "edit-block";
        }
        block.setBlockId(id);
        blockService.updateBlock(block);
        return "redirect:/blocks";
    }

    @GetMapping("/delete/{id}")
    public String deleteBlock(@PathVariable("id") Integer id) {
        blockService.deleteBlock(id);
        return "redirect:/blocks";
    }
}
