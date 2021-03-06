package com.based.model;

import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

import com.based.exception.InvalidGroupByException;
import com.based.exception.InvalidOperationException;
import com.based.exception.InvalidSelectException;
import com.based.exception.MissingColumnException;

/**
 * Class that actually stores data.
 */
public interface Storage {
    /**
     * @param predicate The predicate to use to include a row in the filtered
     *                  subset.
     * @return a filtered subset of the storage.
     */
    public List<Row> filter(Predicate<Row> predicate, int[] columns, List<Aggregate> aggregates, CallBackInterface callback) throws InvalidSelectException, MissingColumnException, InvalidOperationException;

    public Map<String, List<Row>> groupByFilter(Predicate<Row> predicate, int[] columns, int groupby) throws InvalidGroupByException;

    /**
     * @return all rows contained in the storage.
     */
    public List<Row> getRows();

    /**
     * @return all rows contained in the storage, containing only specified columns.
     */
    public List<Row> getRows(int[] columns);

    /**
     * @return the number of rows stored in the storage.
     */
    public long getSize();

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
