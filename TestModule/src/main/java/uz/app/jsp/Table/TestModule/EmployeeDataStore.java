package uz.app.jsp.Table.TestModule;

import java.util.List;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import uz.app.jsp.Table.DataStore;
import uz.app.jsp.Table.Tags.Filter;
import uz.app.jsp.Table.Tags.Filter.Type;

public class EmployeeDataStore implements DataStore {
    GsonBuilder gsonBuilder;
    Gson gson;
    String jsonStr = "";
    List<Employee> employees;
    List<Employee> filteredEmployees;

    public EmployeeDataStore() {
        gsonBuilder = new GsonBuilder();
        gson = gsonBuilder.create();

        employees = new LinkedList<>();
        employees.add(new Employee("Tom Bridgie", 1, 1, 1000.0f, "Department of Development", "Director", "09.12.2020"));
        employees.add(new Employee("Anna Helman", 1, 2, 800.0f, "Department of Development", "Architect", "10.05.2010"));
        employees.add(new Employee("Frederic Bellman", 1, 3, 700.0f, "Department of Development", "Developer", "10.05.2010"));
        employees.add(new Employee("Abdullah Mustafa", 2, 1, 1100.0f, "Department of Foreign Investments", "Director", "10.05.2010"));
        employees.add(new Employee("Michael Muligan", 2, 2, 900.0f, "Department of Foreign Investments", "Manager", "10.05.2010"));
        employees.add(new Employee("Klark Lindstone", 2, 3, 650.0f, "Department of Foreign Investments", "Manager", "10.05.2010"));
        employees.add(new Employee("Emily Gordon", 2, 3, 450.0f, "Department of Foreign Investments", "Manager", "10.05.2010"));
        employees.add(new Employee("George Washington", 1, 4, 950.0f, "Department of Development", "Junior dev", "10.05.2010"));
        employees.add(new Employee("Cameron Urban", 3, 1, 1500.0f, "IT department", "Director", "10.05.2010"));
        employees.add(new Employee("Barbara Straze", 3, 2, 1200f, "IT department", "Manager", "10.05.2010"));
        employees.add(new Employee("Li Juan Yu", 3, 3, 1100f, "IT department", "Sys-admin", "10.05.2010"));
        employees.add(new Employee("Natan Jurevski", 3, 4, 900f, "IT department", "Devops engineer", "10.05.2010"));
        employees.add(new Employee("Salah Farhadi", 3, 5, 1000f, "IT department", "Manager of digital development", "10.05.2010"));
        employees.add(new Employee("Umar Lamar", 3, 6, 600f, "IT department", "Developer", "10.05.2010"));

        filteredEmployees = new LinkedList<>(employees);
    }

    @Override
    public int getRowsCount() {
        return filteredEmployees.size();
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
    public void setFilters(List<Filter> filters) {
        this.filteredEmployees = employees.stream().filter((emp) -> {
            return EmployeeDataStore.correspondsToFilter(emp, filters);
        }).collect(Collectors.toList());
    }

    protected static boolean correspondsToFilter(Employee emp, List<Filter> filters) {
        Map<String, Object> employee = emp.toMap();
        for (Filter filter : filters) {
            if(!employee.containsKey(filter.getColumnName())) {
                continue; // there's no such fields in employee object
            }
            String filterColName = filter.getColumnName();
            Type filterType = filter.getType();
            switch(filterType) {
                case EQUAL: 
                case OPTION: {
                    Object filterValue = filter.getValue();
                    if(filterValue == null || "".equals(filterValue))
                        continue; // continue the loop if there's no value for filter
                    if(!filterValue.equals(employee.get(filterColName).toString())) {
                        return false;
                    }
                    break;
                }
                case LIKE: {
                    Object filterValue = filter.getValue();
                    if(filterValue == null || "".equals(filterValue))
                        continue; // continue the loop if there's no value for filter
                    String similarStr = filterValue.toString();
                    if(!(employee.get(filterColName) instanceof String))
                        return false;
                    String originalStr = employee.get(filterColName).toString();
                    if(originalStr.indexOf(similarStr) < 0)
                        return false; 
                    break;
                }
                case RANGE: {
                    if((employee.get(filterColName) instanceof Number) && 
                        (filter.getLeftBorder() instanceof Number) &&
                        (filter.getRightBorder() instanceof Number)) 
                    {
                        Number empValue = (Number)employee.get(filterColName);
                        Number left = (Number)filter.getLeftBorder();
                        Number right = (Number)filter.getRightBorder();
                        if(left.doubleValue() > empValue.doubleValue() || right.doubleValue() < empValue.doubleValue()) {
                            return false;
                        }

                    } else 
                    if ((employee.get(filterColName) instanceof Date) && 
                        (filter.getLeftBorder() instanceof Date) &&
                        (filter.getRightBorder() instanceof Date))
                    {
                        Date leftBorder = (Date)filter.getLeftBorder();
                        Date rightBorder = (Date)filter.getRightBorder();
                        Date empValue = (Date)employee.get(filterColName);
                        if(leftBorder.compareTo(empValue) > 0 || rightBorder.compareTo(empValue) < 0) {
                            return false;
                        }
                    } else {
                        continue;
                    }
                    break;
                }
                default:
                    continue;
            }
        }
        return true;
    }


    @Override
    public List<Map<String, Object>> getRows(int rowsInPage, int pageNum) {
        if(rowsInPage <= 0)
            throw new IllegalArgumentException("Count of rows in page must be greater than zero");
        if(pageNum <= 0) {
            throw new IllegalArgumentException("Number of page must be greater than zero");
        }

        if(pageNum * rowsInPage > filteredEmployees.size()) {
            pageNum = filteredEmployees.size() / rowsInPage + 1; // last page that correspond to rows count in page
        }
        if(rowsInPage > filteredEmployees.size()) {
            rowsInPage = filteredEmployees.size();
            pageNum = 1;
        }
        
        List<Map<String, Object>> empsInPage = new LinkedList<>();
        Iterator<Employee> iter = filteredEmployees.listIterator(rowsInPage * (pageNum - 1));
        // collect the rows
        for(int i = 0; i < rowsInPage; i++) {
            if(iter.hasNext()) {
                empsInPage.add(iter.next().toMap());
            } else {
                break;
            }
        }

        return empsInPage;
    }

    /*@Override
    public void OnFilterSet(Type filterType, String colName, Object[] values) {
        return;
    }*/
    
}
