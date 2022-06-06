package com.based.model;

public interface CallBackInterface {
    // List<Row> getRowsWithColumnName(String columnName) throws Exception;

    int getTargetIndex(String columnName, int[] indexes) throws Exception;
}
