package com.tpp.tpplab3.service;

import com.tpp.tpplab3.models.Room;
import com.tpp.tpplab3.repository.RoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RoomService {

    @Autowired
    private RoomRepository roomRepository;

    public List<Room> getAllRooms() {
        return roomRepository.findAll();
    }

    public Optional<Room> findRoomById(int id) {
        return roomRepository.findById(id);
    }

    public void saveRoom(Room room) {
        roomRepository.save(room);
    }

    public void updateRoom(Room updatedRoom) {
        Room existingRoom = roomRepository.findById(updatedRoom.getRoomId())
                .orElseThrow(() -> new IllegalArgumentException("Room not found"));

        existingRoom.setRoomNumber(updatedRoom.getRoomNumber());
        existingRoom.setCapacity(updatedRoom.getCapacity());
        existingRoom.setBlock(updatedRoom.getBlock());

        roomRepository.save(existingRoom);
    }

    public void deleteRoom(int id) {
        roomRepository.deleteById(id);
    }

    
}
