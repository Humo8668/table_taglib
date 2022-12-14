package uz.app.jsp.Table.Tags;

import java.util.HashMap;

import com.google.gson.annotations.SerializedName;

public class TableArguments
{
    String targetTable;
    @SerializedName(value="page_number", alternate = {"pageNumber", "active_page_number"})
    int activePageNum;
    @SerializedName(value="rows_in_page", alternate = {"rowsInPage"})
    int rowsInPage;
    @SerializedName(value="filters")
    HashMap<String, Object> filters;
    
    public String getTargetTable() {
        return targetTable;
    }
    public void setTargetTable(String targetTable) {
        this.targetTable = targetTable;
    }

    public int getActivePageNum() {
        return activePageNum;
    }
    public void setActivePageNum(Integer activePageNum) {
        this.activePageNum = activePageNum;
    }
    
    public int getRowsInPage() {
        return rowsInPage;
    }
    public void setRowsInPage(Integer rowsInPage) {
        this.rowsInPage = rowsInPage;
    }

    public HashMap<String, Object> getFilters() {
        return filters;
    }
    public void setFilters(HashMap<String, Object> filters) {
        this.filters = filters;
    }

    public void Impose(TableArguments prioritizedArguments) {
        if(prioritizedArguments.activePageNum > 0) {
            this.activePageNum = prioritizedArguments.activePageNum;
        }
        if(prioritizedArguments.rowsInPage > 0) {
            this.rowsInPage = prioritizedArguments.rowsInPage;
        }
        if(prioritizedArguments.filters != null) {
            this.filters = prioritizedArguments.filters;
        }
    }
}