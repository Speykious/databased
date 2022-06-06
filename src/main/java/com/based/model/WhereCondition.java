package com.based.model;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import com.based.entity.dto.TableDTO;
import com.based.exception.InvalidOperationException;
import com.based.exception.MissingChildrenException;
import com.based.exception.MissingColumnException;

public class WhereCondition implements Serializable {
    private String type;
    private String value;
    private List<WhereCondition> children;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public List<WhereCondition> getChildren() {
        return children;
    }

    public Object evaluate(TableDTO tableDto, Row row)
            throws InvalidOperationException, MissingChildrenException, MissingColumnException {
        switch (type) {
            case "operator":
                // An operator always has 2 children
                WhereCondition cond1, cond2;
                Object child1, child2;

                if (children.size() == 2) {
                    cond1 = children.get(0);
                    cond2 = children.get(1);
                    child1 = cond1.evaluate(tableDto, row);
                    child2 = cond2.evaluate(tableDto, row);
                } else {
                    throw new MissingChildrenException(value);
                }

                switch (value) {
                    case "==":
                        return child1.equals(child2);
                    case "!=":
                        return !child1.equals(child2);
                    case ">":
                        if (child1 instanceof Integer && child2 instanceof Integer)
                            return (int) child1 > (int) child2;
                        else if (child1 instanceof Long && child2 instanceof Long)
                            return (long) child1 > (long) child2;
                        else if (child1 instanceof Float && child2 instanceof Float)
                            return (float) child1 > (float) child2;
                        else if (child1 instanceof LocalDateTime && child2 instanceof LocalDateTime)
                            return ((LocalDateTime) child1).isAfter((LocalDateTime) child2);
                        else
                            break;
                    case "<":
                        if (child1 instanceof Integer && child2 instanceof Integer)
                            return (int) child1 < (int) child2;
                        else if (child1 instanceof Long && child2 instanceof Long)
                            return (long) child1 < (long) child2;
                        else if (child1 instanceof Float && child2 instanceof Float)
                            return (float) child1 < (float) child2;
                        else if (child1 instanceof LocalDateTime && child2 instanceof LocalDateTime)
                            return ((LocalDateTime) child1).isBefore((LocalDateTime) child2);
                        else
                            break;
                    case "and":
                        if (child1 instanceof Boolean && child2 instanceof Boolean)
                            return (boolean) child1 && (boolean) child2;
                        else
                            break;
                    default:
                        throw new InvalidOperationException("Unknown operator '" + value + "'");
                }

                // Fallback in case the operator can't evaluate
                throw new InvalidOperationException(String.format(
                        "Cannot evaluate expression: [%s] %s [%s]",
                        cond1.getType(), value, cond2.getType()));

            case "column":
                return row.getValue(tableDto.getColumnIndex(value));

            default:
                // Value node types
                DataType dataType = DataType.DATATYPE_MAP.get(type);
                if (dataType == null)
                    throw new InvalidOperationException("Unknown node type '" + type + "'");

                return dataType.parse(value, true);
        }
    }
}
