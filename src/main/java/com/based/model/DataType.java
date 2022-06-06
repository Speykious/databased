package com.based.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

public abstract class DataType {
    public static final DataType INT_32 = new DataType() {
        @Override
        public Object parse(Object value, boolean isNullable) throws NumberFormatException {
            if (value instanceof String) {
                String s = (String) value;
                if (s.isEmpty()) {
                    if (isNullable)
                        return null;
                    else
                        throw new IllegalArgumentException("INT_32 string cannot be empty as it is not nullable");
                }
                return Integer.parseInt(s);
            }
            if (value instanceof Integer)
                return (Integer) value;

            throw new NumberFormatException("Cannot parse object of class " + value.getClass());
        }

        @Override
        public Class<?> getInternalClass() {
            return int.class;
        }

        @Override
        public String getName() {
            return "int32";
        }
    };

    public static final DataType INT_64 = new DataType() {
        @Override
        public Object parse(Object value, boolean isNullable) throws NumberFormatException {
            if (value instanceof String) {
                String s = (String) value;
                if (s.isEmpty()) {
                    if (isNullable)
                        return null;
                    else
                        throw new IllegalArgumentException("INT_64 string cannot be empty as it is not nullable");
                }
                return Long.parseLong(s);
            }
            if (value instanceof Long || value instanceof Integer)
                return (Long) value;

            throw new NumberFormatException("Cannot parse object of class " + value.getClass());
        }

        @Override
        public Class<?> getInternalClass() {
            return long.class;
        }

        @Override
        public String getName() {
            return "int64";
        }
    };

    public static final DataType FLOAT_32 = new DataType() {
        @Override
        public Object parse(Object value, boolean isNullable) throws NumberFormatException {
            if (value instanceof String) {
                String s = (String) value;
                if (s.isEmpty()) {
                    if (isNullable)
                        return null;
                    else
                        throw new IllegalArgumentException("FLOAT_32 string cannot be empty as it is not nullable");
                }
                return Float.parseFloat(s);
            }
            if (value instanceof Float)
                return (Float) value;

            throw new NumberFormatException("Cannot parse object of class " + value.getClass());
        }

        @Override
        public Class<?> getInternalClass() {
            return float.class;
        }

        @Override
        public String getName() {
            return "float32";
        }
    };

    public static final DataType STRING = new DataType() {
        @Override
        public Object parse(Object value, boolean isNullable) throws IllegalArgumentException {
            if (value instanceof String) {
                String s = (String) value;
                if (isNullable && s.isEmpty())
                    return null;
                return s;
            }

            throw new IllegalArgumentException("Value has to be an instance of String");
        }

        @Override
        public Class<?> getInternalClass() {
            return String.class;
        }

        @Override
        public String getName() {
            return "string";
        }
    };

    public static final DataType DATE = new DataType() {
        private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("uuuu-MM-dd HH:mm:ss");

        @Override
        public Object parse(Object value, boolean isNullable) throws IllegalArgumentException {
            if (value instanceof String) {
                String s = (String) value;
                if (s.isEmpty()) {
                    if (isNullable)
                        return null;
                    else
                        throw new IllegalArgumentException("DATE string cannot be empty as it is not nullable");
                }
                return LocalDateTime.parse(s, formatter);
            }

            throw new IllegalArgumentException("Value has to be an instance of String");
        }

        @Override
        public Class<?> getInternalClass() {
            return LocalDateTime.class;
        }

        @Override
        public String getName() {
            return "date";
        }
    };

    public static final Map<String, DataType> DATATYPE_MAP = new HashMap<>();
    static {
        DATATYPE_MAP.put(INT_32.getName(), INT_32);
        DATATYPE_MAP.put(INT_64.getName(), INT_64);
        DATATYPE_MAP.put(FLOAT_32.getName(), FLOAT_32);
        DATATYPE_MAP.put(DATE.getName(), DATE);
        DATATYPE_MAP.put(STRING.getName(), STRING);
    }

    public abstract Object parse(Object value, boolean isNullable);

    public abstract String getName();

    public abstract Class<?> getInternalClass();
}
