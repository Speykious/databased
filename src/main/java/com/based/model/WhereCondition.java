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
    private DataType valueDataType;
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

    /**
     * Warning: It is only non-null if the condition has been evaluated.
     * 
     * @return the datatype of the value.
     */
    public DataType getValueDataType() {
        return valueDataType;
    }

    private void setValueDataType(DataType valueDataType) throws InvalidOperationException {
        if (valueDataType == null)
            throw new InvalidOperationException("Unknown value type '" + type + "'");

        this.valueDataType = valueDataType;
    }

    public List<WhereCondition> getChildren() {
        return children;
    }

    public Object evaluate(TableDTO tableDto, Row row)
            throws InvalidOperationException, MissingChildrenException, MissingColumnException {
        switch (type) {
            case "operator": {
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

                if (false == cond1.getValueDataType().equals(cond2.getValueDataType())) {
                    throw new InvalidOperationException(String.format(
                            "Cannot evaluate expression with 2 differently typed values: [%s] %s [%s]",
                            cond1.getValueDataType(), value, cond2.getValueDataType()));
                }

                setValueDataType(cond1.getValueDataType());

                try {
                    switch (value) {
                        case "==":
                            return valueDataType.equal(child1, child2);
                        case "!=":
                            return !valueDataType.equal(child1, child2);
                        case ">":
                            return valueDataType.greaterThan(child1, child2);
                        case "<":
                            return valueDataType.lesserThan(child1, child2);
                        case "and":
                            if (valueDataType == DataType.BOOL)
                                return (boolean) child1 && (boolean) child2;
                            else
                                throw new Exception(
                                        "Expected " + DataType.BOOL.getName() + ", got " + valueDataType.getName());
                        default:
                            throw new Exception("Unknown operator '" + value + "'");
                    }
                } catch (Exception e) {
                    // Fallback in case the operator can't evaluate
                    throw new InvalidOperationException(String.format(
                            "Cannot evaluate expression: [%s] %s [%s] - %s: %s",
                            cond1.getType(), value, cond2.getType(), e, e.getMessage()));
                }
            }
            case "column": {
                int columnIndex = tableDto.getColumnIndex(value);
                Column column = tableDto.getColumns().get(columnIndex);
                setValueDataType(column.getType());
                return row.getValue(columnIndex);
            }
            default: {
                // Value node types
                setValueDataType(DataType.DATATYPE_MAP.get(type));
                return valueDataType.parse(value, true);
            }
        }
    }
}
