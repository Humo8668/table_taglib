package uz.app.jsp.Table;

import java.util.List;
import java.util.Map;

public interface DataStore {
    public enum Ordering { ASC, DESC };

    public int getRowsCount();

    public List<Map<String, Object>> getAllRows();

    public List<Map<String, Object>> getRows(int rowsInPage, int pageNum);

    /**
     * For ordering the output result. Designed to provide chained code writing. 
     * Note that the order of calling this function is important for ordering.
     * @param colName Column name to order
     * @param orderSign ASC / DESC
     * @return This object of data store.
     */
    public DataStore setOrdering(String colName, Ordering order);

    /**
     * For ordering the output result. Designed to provide chained code writing.
     * Note that the order of calling this function is important for ordering.
     * @param colName
     * @param orderSign orderSign < 0 => DESC; orderSign >= 0 => ASC;
     * @return This object of data store.
     */
    public DataStore setOrdering(String colName, int orderSign);

    /**
     * For ordering the output result. Designed to provide chained code writing.
     * Note that the order of calling this function is important for ordering.
     * @param colName
     * @param order ASC / DESC
     * @param nullsFirst Is null values must be first on ordering?
     * @return This object of data store.
     */
    public DataStore setOrdering(String colName, Ordering order, boolean nullsFirst);

    /**
     * Function for filtering the output rows by specified column through checking equality to given value. 
     * Designed to provide chained code writing.
     * Note that the order of calling this function can impact the performance.
     * @param colName Column for filtering
     * @param value Value to compare.
     * @return This object of data store.
     */
    public DataStore setFilterEquals(String colName, Object value);

    /**
     * Function for filtering the output rows by comparable columns by setting value borders.
     * Designed to provide chained code writing.
     * Note that the order of calling this function can impact the performance.
     * @param colName Column for filtering. Type of column must be comparable type i.e. must be defined order.
     * @param leftBorder Column values that greater than this value will pass the filter.
     * @param rightBorder Column values that less than this value will pass the filter.
     * @return This object of data store.
     */
    public DataStore setFilterBetween(String colName, Object leftBorder, Object rightBorder);
}
