package com.based.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.based.exception.InvalidOperationException;

public abstract class DataType {
    public static final DataType BOOL = new DataType() {
        @Override
        public Object parse(Object value, boolean isNullable) {
            if (value instanceof String) {
                String s = (String) value;
                if (s.isEmpty()) {
                    if (isNullable)
                        return null;
                }
                return Boolean.parseBoolean(s);
            }
            if (value instanceof Boolean)
                return (Boolean) value;
            if (value instanceof Integer)
                return (int) value > 0;

            throw new IllegalArgumentException("Cannot parse BOOL from " + value.getClass());
        }

        @Override
        public Class<?> getInternalClass() {
            return boolean.class;
        }

        @Override
        public String getName() {
            return "bool";
        }

        @Override
        public Object sum(List<Object> terms) throws InvalidOperationException {
            throw new InvalidOperationException("Cannot sum booleans together. The fuck are you thinking? '-'");
        }

        @Override
        public boolean greaterThan(Object a, Object b) throws InvalidOperationException {
            throw new InvalidOperationException("Cannot compare booleans");
        }

        @Override
        public boolean lesserThan(Object a, Object b) throws InvalidOperationException {
            throw new InvalidOperationException("Cannot compare booleans");
        }

        @Override
        public boolean equal(Object a, Object b) {
            if (a == null && b == null)
                return true;
            if (a == null || b == null)
                return false;

            return (Boolean) a == (Boolean) b;
        }
    };

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

        @Override
        public Object sum(List<Object> terms) {
            Integer result = 0;
            for (Object term : terms)
                result += (Integer) term;
            return result;
        }

        @Override
        public boolean greaterThan(Object a, Object b) {
            return (Integer) a > (Integer) b;
        }

        @Override
        public boolean lesserThan(Object a, Object b) {
            return (Integer) a < (Integer) b;
        }

        @Override
        public boolean equal(Object a, Object b) {
            if (a == null && b == null)
                return true;
            if (a == null || b == null)
                return false;

            return (Integer) a == (Integer) b;
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

        @Override
        public Object sum(List<Object> terms) {
            Long result = 0L;
            for (Object term : terms)
                result += (Long) term;
            return result;
        }

        @Override
        public boolean greaterThan(Object a, Object b) {
            return (Long) a > (Long) b;
        }

        @Override
        public boolean lesserThan(Object a, Object b) {
            return (Long) a < (Long) b;
        }

        @Override
        public boolean equal(Object a, Object b) {
            if (a == null && b == null)
                return true;
            if (a == null || b == null)
                return false;

            return (Long) a == (Long) b;
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

        @Override
        public Object sum(List<Object> terms) {
            Float result = 0f;
            for (Object term : terms)
                result += (Float) term;
            return result;
        }

        @Override
        public boolean greaterThan(Object a, Object b) {
            return (Float) a > (Float) b;
        }

        @Override
        public boolean lesserThan(Object a, Object b) {
            return (Float) a < (Float) b;
        }

        @Override
        public boolean equal(Object a, Object b) {
            if (a == null && b == null)
                return true;
            if (a == null || b == null)
                return false;

            return (Float) a == (Float) b;
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

        // Concatenates strings. Maybe we should just not do that?
        @Override
        public Object sum(List<Object> terms) {
            StringBuilder sb = new StringBuilder();
            for (Object term : terms)
                sb.append((String) term);
            return sb.toString();
        }

        // Comparison on lexicographic order
        @Override
        public boolean greaterThan(Object a, Object b) {
            return ((String) a).compareToIgnoreCase(((String) b)) == 1;
        }

        // Comparison on lexicographic order
        @Override
        public boolean lesserThan(Object a, Object b) {
            return ((String) a).compareToIgnoreCase(((String) b)) == -1;
        }

        @Override
        public boolean equal(Object a, Object b) {
            if (a == null && b == null)
                return true;
            if (a == null || b == null)
                return false;

            return ((String) a).equals(((String) b));
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
                if (s.endsWith(" UTC"))
                    s = s.substring(0, s.length() - 4);
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

        // Concatenates strings. Maybe we should just not do that?
        @Override
        public Object sum(List<Object> terms) throws InvalidOperationException {
            throw new InvalidOperationException("Cannot sum dates together. The fuck are you thinking? '-'");
        }

        // if date A is after date B
        @Override
        public boolean greaterThan(Object a, Object b) {
            return ((LocalDateTime) a).isAfter(((LocalDateTime) b));
        }

        // if date A is before date B
        @Override
        public boolean lesserThan(Object a, Object b) {
            return ((LocalDateTime) a).isBefore(((LocalDateTime) b));
        }

        @Override
        public boolean equal(Object a, Object b) {
            if (a == null && b == null)
                return true;
            if (a == null || b == null)
                return false;

            return ((LocalDateTime) a).equals(((LocalDateTime) b));
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

    public abstract Object sum(List<Object> terms) throws InvalidOperationException;
    public abstract boolean greaterThan(Object a, Object b) throws InvalidOperationException;
    public abstract boolean lesserThan(Object a, Object b) throws InvalidOperationException;
    public abstract boolean equal(Object a, Object b) throws InvalidOperationException;

    public abstract Class<?> getInternalClass();
}
