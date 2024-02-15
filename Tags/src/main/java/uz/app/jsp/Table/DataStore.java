package uz.app.jsp.Table;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import uz.app.jsp.Table.Tags.Filter;

public interface DataStore {
    public enum Ordering { ASC, DESC };

    /**
     * Count of rows that passes through the set filters. If no filters set, then returns count of all rows.
     * @return 
     */
    public int getRowsCount();

    /**
     * Before getting rows, client should set filters. 
     * If no filters specified, then all rows will be returned on calling {@code}getRows{@code} function.
     * @param filters
     */
    public void setFilters(List<Filter> filters);

    public Collection<Map<String, Object>> getAllRows();

    /**
     * Returns the rows that correspond to currently set filters, by page.
     * @param rowsInPage Number of rows in one page
     * @param pageNum Requesting page number
     * @return The list of rows. Rows are represented as Map(ColumnName, Value).
     */
    public Collection<Map<String, Object>> getRows(int rowsInPage, int pageNum);

    /**
     * Callback function for getting informed about filtering. Filters list will be collected by Tag-library itself.
     * @param filterType 
     * @param colName
     * @param values Array of values set to filter. 
     * Exaple: for filters type of {@code}EQUAL{@code} or {@code}LIKE{@code} there will be only one value in array
     */
    //public void OnFilterSet(Type filterType, String colName, Object[] values);
}
