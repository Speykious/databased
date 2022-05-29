package com.based.model;

public interface CallBackInterface {
    
    //List<Row> getRowsWithColumnName(String columnName) throws Exception;

    int getTargetIndexe(String columnName, int[] indexes) throws Exception;

}
