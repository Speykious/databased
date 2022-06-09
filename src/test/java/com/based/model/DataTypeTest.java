package com.based.model;

import org.junit.Assert;
import org.junit.Test;

public class DataTypeTest {
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

    @Test
    public void parseBoolFromNull_ButNotNullable() {
        Assert.assertThrows("Expected a BOOL, got null", IllegalArgumentException.class, () -> {
            DataType.BOOL.parse(null, false);
        });
    }
}