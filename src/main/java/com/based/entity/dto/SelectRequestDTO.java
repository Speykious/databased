package com.based.entity.dto;

import java.io.Serializable;
import com.based.model.Select;
import com.based.model.WhereCondition;

public class SelectRequestDTO implements Serializable {
    private Select select;
    private WhereCondition where;
    private String groupby;
    
    public Select getSelect() {
        return select;
    }
    public void setSelect(Select select) {
        this.select = select;
    }
    public String getGroupby() {
        return groupby;
    }
    public void setGroupby(String groupby) {
        this.groupby = groupby;
    }
    
    public WhereCondition getWhere() {
        return where;
    }
    public void setWhere(WhereCondition where) {
        this.where = where;
    }
    
}
