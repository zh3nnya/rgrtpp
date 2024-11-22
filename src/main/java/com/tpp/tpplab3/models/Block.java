package com.tpp.tpplab3.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "blocks")
public class Block {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer blockId;

    @NotBlank(message = "Block name is required")
    private String blockName;

    @OneToMany(mappedBy = "block", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Room> rooms = new ArrayList<>();

    // Гетери та сетери
    public Integer getBlockId() {
        return blockId;
    }
    public void setBlockId(Integer blockId) {
        this.blockId = blockId;
    }
    public String getBlockName() {
        return blockName;
    }
    public void setBlockName(String blockName) {
        this.blockName = blockName;
    }
    public List<Room> getRooms() {
        return rooms;
    }
    public void setRooms(List<Room> rooms) {
        this.rooms.clear();
        if (rooms != null) {
            this.rooms.addAll(rooms);
        }
    }
}
