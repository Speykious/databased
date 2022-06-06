package com.based.model;

import java.io.Serializable;
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
                Object child1, child2;
                if (children.size() == 2) {
                    child1 = children.get(0).evaluate(tableDto, row);
                    child2 = children.get(1).evaluate(tableDto, row);
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
                        else if (child1 instanceof Float && child2 instanceof Float)
                            return (Float) child1 > (Float) child2;
                        else
                            throw new InvalidOperationException("Superiority comparison with a non Integer type");
                    case "<":
                        if (child1 instanceof Integer && child2 instanceof Integer)
                            return (int) child1 < (int) child2;
                        else if (child1 instanceof Float && child2 instanceof Float)
                            return (Float) child1 < (Float) child2;
                        else
                            throw new InvalidOperationException("Inferiority comparison with a non Integer type");
                    case "and":
                        if (child1 instanceof Boolean && child2 instanceof Boolean)
                            return (Boolean) child1 && (Boolean) child2;
                        else
                            throw new InvalidOperationException("Require boolean children for 'and' operator");
                    default:
                        throw new InvalidOperationException("Unknown operator '" + value + "'");
                }

            case "column":
                return row.getValue(tableDto.getColumnIndex(value));

            // Value node types
            case "int32":
                return Integer.parseInt(value);
            case "float32":
                return Float.parseFloat(value);

            default:
                throw new InvalidOperationException("Unknown node type '" + value + "'");
        }
    }
}
