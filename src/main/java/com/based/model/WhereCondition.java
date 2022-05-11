package com.based.model;

import java.io.Serializable;
import java.util.List;
import com.based.entity.dto.TableDTO;

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

    public Object evaluate(TableDTO tableDto, Row row) throws Exception{
        if(type.equals("operator")){
            if(value.equals("==")){
                if(children.size() == 2) {
                    Object child1 = children.get(0).evaluate(tableDto,row);
                    Object child2 = children.get(1).evaluate(tableDto,row);
                    return child1.equals(child2);
                }
                else {
                    throw new Exception("No children Exception");
                }
            }
        }
        else if(type.equals("column")){
            int index = tableDto.getColumnIndex(value);
            return row.getValue(index);
        }
        return value;
    }
}
