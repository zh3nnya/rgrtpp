package com.tpp.rgrtpp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tpp.rgrtpp.models.Resident;

@Repository
public interface ResidentRepository extends JpaRepository<Resident, Integer> {
}
