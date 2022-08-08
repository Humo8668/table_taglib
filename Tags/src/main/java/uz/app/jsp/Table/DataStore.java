package uz.app.jsp.Table;

import java.util.Collection;
import java.util.Map;

public interface DataStore {
    public int getRowsCount();

    public Collection<Map<String, Object>> getAllRows();

    public Collection<Map<String, Object>> getRows(int rowsInPage, int pageNum);
}
