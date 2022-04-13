package com.based.model;

import java.util.List;
import java.util.function.Predicate;

/**
 * Class that actually stores data.
 */
public abstract class Storage {
    public abstract List<Row> filter(Predicate<Row> predicate);

    /**
     * Adds a row to the storage.
     * 
     * @param values
     */
    public abstract void add(Row values);

    /**
     * Adds rows in bulk to the storage.
     * 
     * @param valueLines
     */
    public void addBulk(List<Row> valueLines) {
        for (Row values : valueLines)
            add(values);
    }

    public abstract Row remove(Row values);

    public void removeBulk(List<Row> valueLines) {
        for (Row values : valueLines)
            remove(values);
    }
}
