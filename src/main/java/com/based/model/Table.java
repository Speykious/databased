package com.based.model;

import com.based.entity.dto.TableDTO;

public class Table {
    private TableDTO dto;
    private Storage storage;

    public Table(TableDTO dto) {
        this(dto, new StupidStorage());
    }

    public Table(TableDTO dto, Storage storage) {
        this.dto = dto;
        this.storage = storage;
    }

    public TableDTO getDTO() {
        return dto;
    }

    public String getName() {
        return dto.getName();
    }

    public Storage getStorage() {
        return storage;
    }
}
