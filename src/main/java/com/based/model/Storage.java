package com.based.model;

import java.util.List;
import java.util.function.Predicate;

/**
 * Class that actually stores data.
 */
public interface Storage {
    /**
     * @param predicate The predicate to use to include a row in the filtered
     *                  subset.
     * @return a filtered subset of the storage.
     */
    public List<Row> filter(Predicate<Row> predicate, int[] columns);

    /**
     * @return all rows contained in the storage.
     */
    public List<Row> getRows();

    /**
     * @return all rows contained in the storage, containing only specified columns.
     */
    public List<Row> getRows(int[] columns);

    /**
     * Adds a row to the storage.
     * 
     * @param values Row to add to the storage.
     * @return the row that was added.
     */
    public Row add(Row values);

    /**
     * Removes a row from the storage.
     * 
     * @param values Row to remove from the storage.
     * @return the row that was removed.
     */
    public Row remove(Row values);
}
