package com.based.model;

import com.based.entity.dto.TableDTO;
import com.based.exception.InvalidTableFormatException;

public class Table {
    private TableDTO dto;
    private Storage storage;

    public Table(TableDTO dto) throws InvalidTableFormatException {
        this(dto, new StupidStorage());
    }

    public Table(TableDTO dto, Storage storage) throws InvalidTableFormatException {
        if (dto == null)
            throw new InvalidTableFormatException("Table info cannot be null");
        else if (dto.getName() == null || dto.getName().equals(""))
            throw new InvalidTableFormatException("Empty table names are not allowed");
        else if (dto.getColumns() == null || dto.getColumns().size() == 0)
            throw new InvalidTableFormatException("Table needs at least one column");

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
