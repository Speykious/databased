package com.based.model;

import java.util.List;

import com.based.entity.dto.TableDTO;
import com.based.exception.InvalidColumnFormatException;
import com.based.exception.InvalidTableFormatException;

public class Table {
    private TableDTO dto;
    private Storage storage;

    public Table(TableDTO dto) throws InvalidTableFormatException, InvalidColumnFormatException {
        this(dto, new StupidStorage());
    }

    public Table(TableDTO dto, Storage storage) throws InvalidTableFormatException, InvalidColumnFormatException {
        if (dto == null)
            throw new InvalidTableFormatException("Table info cannot be null");
        
        String name = dto.getName();
        List<Column> columns = dto.getColumns();
        
        if (name == null || name.equals(""))
            throw new InvalidTableFormatException("Empty table names are not allowed");
        if (columns == null || columns.size() == 0)
            throw new InvalidTableFormatException("Table needs at least one column");
        
        for (Column column : columns)
            column.assertIsValid();

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
