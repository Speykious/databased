package com.based.model;

import com.based.exception.InvalidSelectException;
import com.based.exception.MissingColumnException;

public interface CallBackInterface {
    
    int getTargetIndex(String columnName, int[] indexes) throws InvalidSelectException, MissingColumnException;

    //int getTargetIndex(String columnName, int[] indexes) throws Exception;
}
