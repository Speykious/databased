package com.based.model;

import org.junit.Assert;
import org.junit.Test;

import com.based.entity.NbLinesResponse;

public class DataTypeTest {
    // Boolean
    @Test
    public void parseBoolFromBool() {
        Object result = DataType.BOOL.parse(true, false);
        Assert.assertTrue(result instanceof Boolean);
        Assert.assertEquals(true, (boolean) result);

        result = DataType.BOOL.parse(false, false);
        Assert.assertTrue(result instanceof Boolean);
        Assert.assertEquals(false, (boolean) result);
    }

    @Test
    public void parseBoolFromString() {
        Object result = DataType.BOOL.parse("true", false);
        Assert.assertTrue(result instanceof Boolean);
        Assert.assertEquals(true, (boolean) result);

        result = DataType.BOOL.parse("false", false);
        Assert.assertTrue(result instanceof Boolean);
        Assert.assertEquals(false, (boolean) result);
    }

    @Test
    public void parseBoolFromNull() {
        Object result = DataType.BOOL.parse(null, true);
        Assert.assertEquals(null, result);
    }

    // Int 32
    @Test
    public void parseInt32FromInt32() {
        Object result = DataType.INT_32.parse(5, false);
        Assert.assertTrue(result instanceof Integer);
        Assert.assertEquals(5, (int) result);
    }

    @Test
    public void parseInt32FromString() {
        Object result = DataType.INT_32.parse("249", false);
        Assert.assertTrue(result instanceof Integer);
        Assert.assertEquals(249, (int) result);
    }

    // Nullability tests
    @Test
    public void parseFromNull_NotNullable() {
        for (DataType dataType : DataType.DATATYPE_MAP.values()) {
            Assert.assertThrows("Expected a " + dataType.getName() + ", got null",
                    IllegalArgumentException.class,
                    () -> {
                        dataType.parse(null, false);
                    });
        }
    }

    @Test
    public void parseFromNull_Nullable() {
        for (DataType dataType : DataType.DATATYPE_MAP.values()) {
            Object result = dataType.parse(null, true);
            Assert.assertEquals(null, result);
        }
    }

    // General sanity
    @Test
    public void parseBoolRejectUnknownClass() {
        for (DataType dataType : DataType.DATATYPE_MAP.values()) {
            Assert.assertThrows("Cannot parse " + dataType.getName() + " from " + NbLinesResponse.class,
                    IllegalArgumentException.class, () -> {
                        dataType.parse(new NbLinesResponse(727), true);
                    });
        }
    }
}
