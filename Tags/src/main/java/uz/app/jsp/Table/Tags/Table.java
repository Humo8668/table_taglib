package uz.app.jsp.Table.Tags;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import uz.app.jsp.Table.DataStore;
import uz.app.jsp.Table.DataStore.Ordering;

public class Table {
    private List<Column> columns;
    private String name;
    private String dataJson;
    private String caption;
    private String dataSourceClass;
    private DataStore dataStore;
    private boolean hasNumeration = false;

    private Integer rowsInPage;
    private Integer activePageNum;

    private Gson jsonParser;

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
        return this.columns.stream()
            .filter((t) -> t.isShowColumn())
            .sorted((o1, o2) -> (o1.getOrderValue() < o2.getOrderValue())? -1: (o1.getOrderValue() == o2.getOrderValue())? 0 : 1)
            .collect(Collectors.toList());
    }

    public String getName() {
        return this.name;
    }

    public void setDataJson(String dataJson) {
        if(dataJson == null) {
            this.dataStore = new DefaultDataStore(new LinkedList<>());
            return;
        }
        this.dataJson = dataJson;
        if(jsonParser == null) 
            return;
        
        HashMap<String, Object> parsedData = jsonParser.fromJson(dataJson, HashMap.class);
        if(!parsedData.containsKey("rows"))
            throw new RuntimeException("Invalid json format for parsing to table-data");

        this.dataStore = new DefaultDataStore((List<Map<String, Object>>)parsedData.get("rows"));
    }

    public Iterable<Map<String, Object>> getRows(int rowsInPage, int pageNum) {
        return this.dataStore.setOrdering("name", Ordering.ASC) .getRows(rowsInPage, pageNum);
    }
    public int getOverallRowsCount() {
        return this.dataStore.getRowsCount();
    }
    public void setCaption(String caption) {
        this.caption = caption;
    }
    public String getCaption() {
        return caption;
    }

    public void setDataStoreClass(String dataSourceClass) throws ClassNotFoundException {
        if(dataSourceClass == null)
            return;
        this.dataSourceClass = dataSourceClass;
        Class<? extends DataStore> cl = (Class<? extends DataStore>) Class.forName(dataSourceClass);
        Constructor<? extends DataStore> constructor = null;
        if(cl.getConstructors().length <= 0)
            throw new RuntimeException("Datasource class must have default constructor");
        try {
            constructor = cl.getConstructor();
        } catch (NoSuchMethodException | SecurityException e) {
            throw new RuntimeException("Couldn't get default constructor for datasource class ("+dataSourceClass+")", e);
        }

        try {
            this.dataStore = constructor.newInstance();
        } catch (InstantiationException | IllegalAccessException | IllegalArgumentException
                | InvocationTargetException e) {
            throw new RuntimeException("Couldn't instantiate from data source class", e);
        }
    }
    public String getDataStoreClass() {
        return dataSourceClass;
    }

    public List<Map<String, Object>> getRows() {
        if(this.dataSourceClass == null) {
            throw new RuntimeException("Data source class was not initialized.");
        }
        return this.dataStore.getRows(this.rowsInPage, this.activePageNum);
    }
    
    public void setNumeration(boolean hasNumeration) {
        this.hasNumeration = hasNumeration;
    }
    public boolean hasNumeration() {
        return this.hasNumeration;
    }

    public void setRowsNumerPerPage(Integer rowsPerPage) {
        this.rowsInPage = rowsPerPage;
    }
    public Integer getRowsNumberPerPage() {
        return this.rowsInPage;
    }

    public void setActivePageNumber(Integer activePageNum) {
        this.activePageNum = activePageNum;
    }
    public Integer getActivePageNumber() {
        return this.activePageNum;
    }
}
