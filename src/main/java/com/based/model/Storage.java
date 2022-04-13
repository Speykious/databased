package com.based.model;

import java.util.List;
import java.util.function.Predicate;

/**
 * Class that actually stores data.
 */
public interface Storage {
    /**
     * Returns a filtered subset of the storage.
     * 
     * @param predicate
     * @return
     */
    public List<Row> filter(Predicate<Row> predicate);

    /**
     * Adds a row to the storage.
     * 
     * @param values Row to add to the storage.
     */
    public Row add(Row values);

    /**
     * Removes a row from the storage.
     * 
     * @param values Row to remove from the storage.
     * @return
     */
    public Row remove(Row values);
}
