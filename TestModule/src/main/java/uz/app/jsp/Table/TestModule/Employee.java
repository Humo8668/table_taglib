package uz.app.jsp.Table.TestModule;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Employee {
    private static int empIdSequence = 0;

    private static int getNextId() {
        return ++empIdSequence;
    }

    private int id;
    private String name;
    private int depId;
    private String depName;
    private int postId;
    private String postName;
    private float salary;
    private Date hiredDate;

    private static DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");

 

    public Employee(String name, int depId, int postId, float salary, String depName, String postName, String hiredDate) {
        this.id = getNextId();
        this.name = name;
        this.depId = depId;
        this.postId = postId;
        this.salary = salary;
        this.depName = depName;
        this.postName = postName;
        try {
            this.hiredDate = dateFormat.parse(hiredDate);
        } catch (ParseException e) {
            this.hiredDate = new Date();
            e.printStackTrace();
        }
    }
    
    
    public int getId() {
        return id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public int getDepId() {
        return depId;
    }
    public void setDepId(int depId) {
        this.depId = depId;
    }
    public int getPostId() {
        return postId;
    }
    public void setPostId(int postId) {
        this.postId = postId;
    }
    public float getSalary() {
        return salary;
    }
    public void setSalary(float salary) {
        this.salary = salary;
    }
    public String getDepName() {
        return depName;
    }
    public void setDepName(String depName) {
        this.depName = depName;
    }
    public String getPostName() {
        return postName;
    }
    public void setPostName(String postName) {
        this.postName = postName;
    }
    public Date getHiredDate() {
        return hiredDate;
    }
    public void setHiredDate(Date hiredDate) {
        this.hiredDate = hiredDate;
    }


    public Map<String, Object> toMap() {
        HashMap<String, Object> mapping = new HashMap<>();
        for(Field field : this.getClass().getDeclaredFields()){
            field.setAccessible(true);
            try {
                mapping.put(field.getName(), field.get(this));
            } catch (Exception e) {
                e.printStackTrace();
                continue;
            }
            field.setAccessible(false);
        }
        return mapping;
    }

    public static Comparator<Employee> getComparator(String fieldName) {
        Field field;
        Method getter;

        String getterName = "get" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
        try {
            field = Employee.class.getDeclaredField(fieldName);
            getter = Employee.class.getMethod(getterName);
        } catch (NoSuchFieldException e) {
            throw new RuntimeException("There's no field \""+fieldName+"\". ");
        } catch(NoSuchMethodException ex) {
            throw new RuntimeException("No getter \""+getterName+"\" for field \""+fieldName+"\". ");
        } catch(Exception ex) {
            throw new RuntimeException("Error on getting getter for field \""+fieldName+"\": " +ex.getMessage());
        }
        
        

        return new Comparator<Employee>() {
            @Override
            public int compare(Employee a, Employee b) {
                Object firstValue = null;
                Object secondValue = null;
                try {
                    firstValue = getter.invoke(a, null);
                    secondValue = getter.invoke(b, null);

                    if(firstValue == null || secondValue == null) 
                        return 0;

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

                        
                        Double f = (double)((float)firstValue);
                        Double s = (double)((float)secondValue);
                        return (f > s)?1:(f < s)?-1:0;
                    } else if(firstValue instanceof String) {
                        return ((String)firstValue).compareTo((String)secondValue);
                    } else {
                        return 0;
                    }
                } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
                    throw new RuntimeException("Error on calling getter \""+getterName+"\" for " + Employee.class.getName(), e);
                }
            }
        };
    }
}
