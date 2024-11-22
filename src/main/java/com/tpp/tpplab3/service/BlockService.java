package com.tpp.tpplab3.service;

import com.tpp.tpplab3.models.Block;
import com.tpp.tpplab3.repository.BlockRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BlockService {

    @Autowired
    private BlockRepository blockRepository;

    public List<Block> getAllBlocks() {
        return blockRepository.findAll();
    }

    public Optional<Block> findBlockById(int id) {
        return blockRepository.findById(id);
    }

    public void saveBlock(Block block) {
        blockRepository.save(block);
    }

    public void updateBlock(Block updatedBlock) {
        Block existingBlock = blockRepository.findById(updatedBlock.getBlockId())
                .orElseThrow(() -> new IllegalArgumentException("Block not found"));

        existingBlock.setBlockName(updatedBlock.getBlockName());

        blockRepository.save(existingBlock);
    }

    public void deleteBlock(int id) {
        blockRepository.deleteById(id);
    }
}
