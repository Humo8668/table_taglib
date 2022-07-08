package uz.app.jsp.Table.Tags;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class Table {
    private List<Column> columns;
    private String name;
    private String dataJson;
    private Gson jsonParser;
    private List<Map<String, Object>> rowsData;

    public Table(String name) {
        this.name = name;
        this.columns = new LinkedList<Column>();
        jsonParser = (new GsonBuilder()).create();
    }

    public void AddColumn(Column column) {
        for (Column otherColumn : columns) {
            if(otherColumn.getName().equals(column.getName()))
                throw new RuntimeException("Dublicate column name: " + column.getName());
        }
        this.columns.add(column);
    }

    public List<Column> getColumns() {
        return new LinkedList<Column>(this.columns);
    }

    public List<Column> getShowableColumns() {
        return this.columns.stream().filter((t) -> t.isShowColumn()).collect(Collectors.toList());
    }

    public String getName() {
        return this.name;
    }

    public void setDataJson(String dataJson) {
        this.dataJson = dataJson;
        if(jsonParser == null) 
            return;
        
        HashMap<String, Object> parsedData = jsonParser.fromJson(dataJson, HashMap.class);
        if(!parsedData.containsKey("rows"))
            throw new RuntimeException("Invalid json format for parsing to table-data");

        rowsData = (List<Map<String, Object>>)parsedData.get("rows");
    }

    protected List<Map<String, Object>> getRows() {
        return new LinkedList<>(this.rowsData);
    }
}
