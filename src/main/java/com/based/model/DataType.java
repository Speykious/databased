package com.based.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public abstract class DataType {
    public static DataType INT_32 = new DataType() {
        @Override
        public Object parse(String s) throws NumberFormatException {
            return Integer.parseInt(s);
        }

        @Override
        public Class<?> getInternalClass() {
            return long.class;
        }
    };

    public static DataType INT_64 = new DataType() {
        @Override
        public Object parse(String s) throws NumberFormatException {
            return Long.parseLong(s);
        }

        @Override
        public Class<?> getInternalClass() {
            return long.class;
        }
    };

    public static DataType FLOAT_32 = new DataType() {
        @Override
        public Object parse(String s) throws NumberFormatException {
            return Float.parseFloat(s);
        }

        @Override
        public Class<?> getInternalClass() {
            return long.class;
        }
    };

    public static DataType STRING = new DataType() {
        @Override
        public Object parse(String s) {
            return s;
        }

        @Override
        public Class<?> getInternalClass() {
            return String.class;
        }
    };

    public static DataType DATE = new DataType() {
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

    public abstract Object parse(String value);

    public abstract Class<?> getInternalClass();
}
