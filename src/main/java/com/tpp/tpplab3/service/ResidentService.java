package com.tpp.tpplab3.service;

import com.tpp.tpplab3.models.Resident;
import com.tpp.tpplab3.repository.ResidentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ResidentService {

    @Autowired
    private ResidentRepository residentRepository;

    public List<Resident> getAllResidents() {
        return residentRepository.findAll();
    }

    public Optional<Resident> findResidentById(int id) {
        return residentRepository.findById(id);
    }

    public void saveResident(Resident resident) {
        residentRepository.save(resident);
    }

    public void updateResident(Resident updatedResident) {
        Resident existingResident = residentRepository.findById(updatedResident.getResidentId())
                .orElseThrow(() -> new IllegalArgumentException("Resident not found"));

        existingResident.setFullName(updatedResident.getFullName());
        existingResident.setDateOfBirth(updatedResident.getDateOfBirth());
        existingResident.setRoom(updatedResident.getRoom());

        residentRepository.save(existingResident);
    }

    public void deleteResident(int id) {
        residentRepository.deleteById(id);
    }
}
