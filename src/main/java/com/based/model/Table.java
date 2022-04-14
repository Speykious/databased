package com.based.model;

import com.based.entity.dto.TableDTO;
import com.based.exception.InvalidTableDTOException;

public class Table {
    private TableDTO dto;
    private Storage storage;

    public Table(TableDTO dto) throws InvalidTableDTOException {
        this(dto, new StupidStorage());
    }

    public Table(TableDTO dto, Storage storage) throws InvalidTableDTOException {
        if (dto == null)
            throw new InvalidTableDTOException("Table info cannot be null");
        else if (dto.getName() == null || dto.getName().equals(""))
            throw new InvalidTableDTOException("Empty table names are not allowed");
        else if (dto.getColumns() == null || dto.getColumns().size() == 0)
            throw new InvalidTableDTOException("Table needs at least one column");

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
