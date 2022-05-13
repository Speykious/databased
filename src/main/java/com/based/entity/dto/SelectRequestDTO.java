package com.based.entity.dto;

import java.io.Serializable;
import com.based.model.Select;
import com.based.model.WhereCondition;

public class SelectRequestDTO implements Serializable {
    private Select select;
    private WhereCondition where;
    
    public Select getSelect() {
        return select;
    }
    public void setSelect(Select select) {
        this.select = select;
    }
    public WhereCondition getWhere() {
        return where;
    }
    public void setWhere(WhereCondition where) {
        this.where = where;
    }
    
}
