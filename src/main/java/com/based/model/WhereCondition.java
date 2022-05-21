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

    //TODO : create custom exceptions ?
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
            else if(value.equals("!=")){
                if(children.size() == 2) {
                    Object child1 = children.get(0).evaluate(tableDto,row);
                    Object child2 = children.get(1).evaluate(tableDto,row);
                    return !child1.equals(child2);
                }
                else {
                    throw new Exception("No children Exception");
                }
            }
            else if(value.equals(">")){
                if(children.size() == 2) {
                    Object child1 = children.get(0).evaluate(tableDto,row);
                    Object child2 = children.get(1).evaluate(tableDto,row);
                    if(child1 instanceof Integer && child2 instanceof Integer){
                        return (int) child1 > (int) child2;
                    }
                    else if(child1 instanceof Float && child2 instanceof Float){
                        return (Float) child1 > (Float) child2;
                    }
                    else {
                        throw new Exception("Superiority comparison with a non Integer type");
                    }
                }
                else {
                    throw new Exception("No children Exception");
                }
            }
            else if(value.equals("<")){
                if(children.size() == 2) {
                    Object child1 = children.get(0).evaluate(tableDto,row);
                    Object child2 = children.get(1).evaluate(tableDto,row);
                    if(child1 instanceof Integer && child2 instanceof Integer){
                        return (int) child1 < (int) child2;
                    }
                    else if(child1 instanceof Float && child2 instanceof Float){
                        return (Float) child1 < (Float) child2;
                    }
                    else {
                        throw new Exception("Inferiority comparison with a non Integer type");
                    }
                }
                else {
                    throw new Exception("No children Exception");
                }
            }
            else if(value.equals("and")){
                if(children.size() == 2) {
                    Object child1 = children.get(0).evaluate(tableDto,row);
                    Object child2 = children.get(1).evaluate(tableDto,row);
                    if(child1 instanceof Boolean && child2 instanceof Boolean){
                        return (Boolean) child1 && (Boolean) child2;
                    }
                    else {
                        throw new Exception("Require boolean children for 'and' operator");
                    }
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
        else if(type.equals("int32")){
            int v = Integer.parseInt(value);
            return v;
        }
        else if(type.equals("float32")){
            Float v = Float.parseFloat(value);
            return v;
        }
        return value;
    }
}
