package uz.app.jsp.Table;

import java.util.List;
import java.util.Map;

public interface DataStore {
    public int getRowsCount();

    public List<Map<String, Object>> getAllRows();

    public List<Map<String, Object>> getRows(int rowsInPage, int pageNum);
}
