package uz.app.jsp.Table.TestModule;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import uz.app.jsp.Table.DataStore;

public class EmployeeDataStore implements DataStore {
    GsonBuilder gsonBuilder;
    Gson gson;
    String jsonStr = "";
    Set<Employee> employees;

    public EmployeeDataStore() {
        gsonBuilder = new GsonBuilder();
        gson = gsonBuilder.create();

        employees = new HashSet<>();
        employees.add(new Employee("Tom Bridgie", 1, 1, 1000.0f));
        employees.add(new Employee("Anna Helman", 1, 2, 800.0f));
        employees.add(new Employee("Frederic Bellman", 1, 3, 700.0f));
        employees.add(new Employee("Abdullah Mustafa", 2, 1, 1100.0f));
        employees.add(new Employee("Michael Muligan", 2, 2, 900.0f));
        employees.add(new Employee("Klark Lindstone", 2, 3, 650.0f));
    }

    @Override
    public int getRowsCount() {
        return employees.size();
    }

    @Override
    public Collection<Map<String, Object>> getAllRows() {
        Collection<Map<String, Object>> result = new LinkedList<>();
        for(Employee emp : employees) {
            result.add(emp.toMap());
        }
        return result;
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
        Collection<Map<String, Object>> empsInPage = new HashSet<>();
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
    
}
