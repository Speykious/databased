package com.based.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

public abstract class DataType {
    public static final DataType INT_32 = new DataType() {
        @Override
        public Object parse(String s) throws NumberFormatException {
            return Integer.parseInt(s);
        }

        @Override
        public Class<?> getInternalClass() {
            return long.class;
        }
    };

    public static final DataType INT_64 = new DataType() {
        @Override
        public Object parse(String s) throws NumberFormatException {
            return Long.parseLong(s);
        }

        @Override
        public Class<?> getInternalClass() {
            return long.class;
        }
    };

    public static final DataType FLOAT_32 = new DataType() {
        @Override
        public Object parse(String s) throws NumberFormatException {
            return Float.parseFloat(s);
        }

        @Override
        public Class<?> getInternalClass() {
            return long.class;
        }
    };

    public static final DataType STRING = new DataType() {
        @Override
        public Object parse(String s) {
            return s;
        }

        @Override
        public Class<?> getInternalClass() {
            return String.class;
        }
    };

    public static final DataType DATE = new DataType() {
        private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("uuuu-MM-dd HH:mm:ss");

        @Override
        public Object parse(String s) {
            return LocalDateTime.parse(s, formatter);
        }

        @Override
        public Class<?> getInternalClass() {
            return LocalDateTime.class;
        }
    };

    public static final Map<String, DataType> DATATYPE_MAP = new HashMap<>();
    static {
        DATATYPE_MAP.put("int32", INT_32);
        DATATYPE_MAP.put("int64", INT_64);
        DATATYPE_MAP.put("float32", FLOAT_32);
        DATATYPE_MAP.put("date", DATE);
        DATATYPE_MAP.put("string", STRING);
    }

    public abstract Object parse(String value);

    public abstract Class<?> getInternalClass();
}
