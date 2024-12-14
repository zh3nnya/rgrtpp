package com.tpp.rgrtpp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tpp.rgrtpp.models.Room;

@Repository
public interface RoomRepository extends JpaRepository<Room, Integer> {
}
