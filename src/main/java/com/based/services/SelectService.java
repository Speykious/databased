package com.based.services;

import java.util.List;
import com.based.exception.MissingTableException;
import com.based.model.Database;
import com.based.model.Row;
import com.based.model.Table;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class SelectService {
    public List<Row> selectAll(String tableName) throws MissingTableException, MissingColumnException {
        return selectAll(tableName, null);
    }

    public List<Row> selectAll(String tableName, List<String> columnNames)
            throws MissingTableException, MissingColumnException {
        Table table = Database.getTable(tableName);
        return table.getStorage().getRows(table.getColumnIndexes(columnNames));
    }

    public List<Row> selectWhere(String tableName) throws MissingTableException {
        return Database.getTable(tableName).getStorage().getRows();
    }

    public List<Row> selectWhere(String tableName) throws MissingTableException {
        return Database.getTable(tableName).getStorage().getRows();
    }

    //const myCondition = "column_name == 'bonjour' and ( id > 3 and desh != 'real' )"

    //column

    //[
    //   { type: "ident", value: "column_name" },
    //   { type: "operator", value: "==" },
    //   { type: "string", value: "bonjour" },
    //   { type: "operator", value: "and" },
    //   { type: "bracket", value: "(" },
    //   { type: "ident", value: "id" },
    //   { type: "operator", value: ">" },
    //   { type: "number", value: "3" },
    //   { type: "operator", value: "and" },
    //   { type: "ident", value: "desh" },
    //   { type: "operator", value: "!=" },
    //   { type: "string", value: "real" },
    //   { type: "bracket", value: ")" },
    // ]


    public void parseWhereCondition(String condition) {
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();

        String json = gson.toJson(1);
        int fromJsonInt = gson.fromJson(json, int.class);
        System.out.println("1 -> " + fromJsonInt);
        
        String[] splitCondition = condition.split(" ");

        for(int i= 0; i < splitCondition.length; i++){
            if(i != splitCondition.length){

            }
        }


    }

}
