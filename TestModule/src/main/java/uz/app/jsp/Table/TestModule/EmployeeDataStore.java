package uz.app.jsp.Table.TestModule;

import java.util.List;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import uz.app.jsp.Table.DataStore;

public class EmployeeDataStore implements DataStore {
    GsonBuilder gsonBuilder;
    Gson gson;
    String jsonStr = "";
    List<Employee> employees;

    public EmployeeDataStore() {
        gsonBuilder = new GsonBuilder();
        gson = gsonBuilder.create();

        employees = new LinkedList<>();
        employees.add(new Employee("Tom Bridgie", 1, 1, 1000.0f));
        employees.add(new Employee("Anna Helman", 1, 2, 800.0f));
        employees.add(new Employee("Frederic Bellman", 1, 3, 700.0f));
        employees.add(new Employee("Abdullah Mustafa", 2, 1, 1100.0f));
        employees.add(new Employee("Michael Muligan", 2, 2, 900.0f));
        employees.add(new Employee("Klark Lindstone", 2, 3, 650.0f));
        employees.add(new Employee("Emily Gordon", 2, 3, 450.0f));
        employees.add(new Employee("George Washington", 1, 4, 950.0f));
        employees.add(new Employee("Cameron Urban", 3, 1, 1500.0f));
        employees.add(new Employee("Barbara Straze", 3, 2, 1200f));
        employees.add(new Employee("Li Juan Yu", 3, 3, 1100f));
        employees.add(new Employee("Natan Jurevski", 3, 4, 900f));
        employees.add(new Employee("Salah Farhadi", 3, 5, 1000f));
        employees.add(new Employee("Umar Lamar", 3, 6, 600f));
    }

    @Override
    public int getRowsCount() {
        return employees.size();
    }

    @Override
    public List<Map<String, Object>> getAllRows() {
        List<Map<String, Object>> result = new LinkedList<>();
        for(Employee emp : employees) {
            result.add(emp.toMap());
        }
        return result;
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
        List<Map<String, Object>> empsInPage = new LinkedList<>();
        Iterator<Employee> iter = employees.iterator();
        // run through, while actual page not reached
        int currentPage = 1;
        while(currentPage != pageNum) {
            for(int i = 0; i < rowsInPage; i++)
                iter.next();
            
            currentPage++;
        }
        // collect rows
        for(int i = 0; i < rowsInPage; i++) {
            if(iter.hasNext()) {
                empsInPage.add(iter.next().toMap());
            } else {
                break;
            }
        }

        return empsInPage;
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
        this.employees.sort(Employee.getComparator(colName));
        return this;
    }

    @Override
    public DataStore setFilterEquals(String colName, Object value) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public DataStore setFilterBetween(String colName, Object leftBorder, Object rightBorder) {
        // TODO Auto-generated method stub
        return null;
    }
    
}
