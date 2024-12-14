package com.tpp.rgrtpp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tpp.rgrtpp.models.Block;

@Repository
public interface BlockRepository extends JpaRepository<Block, Integer> {
}
