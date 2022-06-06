package com.based.model;

import java.util.List;

import com.based.entity.dto.TableDTO;
import com.based.exception.InvalidColumnFormatException;
import com.based.exception.InvalidTableFormatException;
import com.based.exception.MissingColumnException;

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

    public int[] getColumnIndexes(List<String> columnNames) throws MissingColumnException {
        int[] indexes = new int[columnNames.size()];
        if(indexes.length > 0){
            List<Column> columns = dto.getColumns();
            for (int j = 0; j < columnNames.size(); j++) {
                String columnName = columnNames.get(j);

                boolean found = false;
                for (int i = 0; i < columns.size(); i++) {
                    if (columns.get(i).getName().equals(columnName)) {
                        indexes[j] = i;
                        found = true;
                        break;
                    }
                }

                if (!found)
                    throw new MissingColumnException(columnName);
            }
        }

        return indexes;
    }

    public int getColumnIndex(String columnName) throws MissingColumnException {
        int indexe = 0;
        List<Column> columns = dto.getColumns();
        boolean found = false;
        for (int i = 0; i < columns.size(); i++) {
            if (columns.get(i).getName().equals(columnName)) {
                indexe = i;
                found = true;
                break;
            }
        }

        if (!found)
            throw new MissingColumnException(columnName);

        return index;
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
