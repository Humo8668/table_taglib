package uz.app.jsp.Table.Tags;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import uz.app.jsp.Table.DataStore;
import uz.app.jsp.Table.Tags.Filter.Type;

class DefaultDataStore implements DataStore {
    List<Map<String, Object>> rows;
    
    public DefaultDataStore(List<Map<String, Object>> allRows) {
        rows = new LinkedList<>(allRows);
    }

    @Override
    public int getRowsCount() {
        return this.rows.size();
    }

    @Override
    public List<Map<String, Object>> getAllRows() {
        return this.rows;
    }

    @Override
    public Collection<Map<String, Object>> getRows(int rowsInPage, int pageNum) {
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

        return result;
    }

    @Override
    public void setFilters(List<Filter> filters) {
        
    }
}
