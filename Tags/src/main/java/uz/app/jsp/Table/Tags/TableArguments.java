package uz.app.jsp.Table.Tags;

import java.util.HashMap;

import com.google.gson.annotations.SerializedName;

public class TableArguments
{
    String targetTable;
    @SerializedName(value="page_number", alternate = {"pageNumber", "active_page_number"})
    Integer activePageNum;
    @SerializedName(value="rows_in_page", alternate = {"rowsInPage"})
    Integer rowsInPage;
    HashMap<String, Object> filters;
    
    public String getTargetTable() {
        return targetTable;
    }
    public void setTargetTable(String targetTable) {
        this.targetTable = targetTable;
    }

    public Integer getActivePageNum() {
        return activePageNum;
    }
    public void setActivePageNum(Integer activePageNum) {
        this.activePageNum = activePageNum;
    }
    
    public Integer getRowsInPage() {
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
}