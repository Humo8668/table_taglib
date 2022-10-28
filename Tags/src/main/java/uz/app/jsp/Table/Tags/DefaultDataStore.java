package uz.app.jsp.Table.Tags;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import uz.app.jsp.Table.DataStore;

class DefaultDataStore implements DataStore {
    List<Map<String, Object>> rows;
    
    public DefaultDataStore(List<Map<String, Object>> allRows) {
        rows = new LinkedList<>(allRows);
    }

    @Override
    public List<Map<String, Object>> getAllRows() {
        return this.rows;
    }

    @Override
    public List<Map<String, Object>> getRows(int rowsInPage, int pageNum) {
        if(rowsInPage <= 0)
            throw new IllegalArgumentException("Count of rows in page must be greater than zero");
        if(pageNum <= 0) {
            throw new IllegalArgumentException("Number of page must be greater than zero");
        }
        if(pageNum * rowsInPage > getRowsCount()) {
            pageNum = getRowsCount() / rowsInPage + 1; // last page that correspond to rows count in page
        }
        if(rowsInPage > getRowsCount()) {
            rowsInPage = getRowsCount();
            pageNum = 1;
        }

        Collection<Map<String, Object>> result = new LinkedList<>();
        int skipFirstN = (pageNum - 1) * rowsInPage;
        int skipped = 0;

        for (Map<String,Object> row : result) {
            if(skipped <= skipFirstN) {
                skipped++;
                continue;
            }
            if(result.size() <= rowsInPage)
                result.add(row);
            else
                break;
        }

        return this.rows;
    }

    @Override
    public int getRowsCount() {
        return this.rows.size();
    }

    @Override
    public DataStore setOrdering(String colName, Ordering order) {
        return this.setOrdering(colName, order, false);
    }

    @Override
    public DataStore setOrdering(String colName, int orderSign) {
        return this.setOrdering(colName, (orderSign>=0)?Ordering.ASC:Ordering.DESC, false);
    }

    @Override
    public DataStore setOrdering(String colName, Ordering order, boolean nullsFirst) {
        
        this.rows.sort((first, second) -> {
            Object firstValue = first.get(colName);
            Object secondValue = second.get(colName);
            if(firstValue == null && nullsFirst) 
                return 1;

            if(secondValue == null && nullsFirst)
                return -1;
            
            if(firstValue instanceof Integer ||
                firstValue instanceof Short ||
                firstValue instanceof Character ||
                firstValue instanceof Byte)
            {
                Integer f = (Integer)firstValue; 
                Integer s = (Integer)secondValue;
                return (f > s)?1:(f < s)?-1:0;
            } else if(firstValue instanceof Double ||
                    firstValue instanceof Float) 
            {
                Double f = (Double)firstValue;
                Double s = (Double)secondValue;
                return (f > s)?1:(f < s)?-1:0;
            } else if(firstValue instanceof String) {
                return ((String)firstValue).compareTo((String)secondValue);
            } else {
                return 0;
            }
        });
        return this;
    }

    @Override
    public DataStore setFilterEquals(String colName, Object value) {
        return this;
    }

    @Override
    public DataStore setFilterBetween(String colName, Object leftBorder, Object rightBorder) {
        return this;
    }
}
