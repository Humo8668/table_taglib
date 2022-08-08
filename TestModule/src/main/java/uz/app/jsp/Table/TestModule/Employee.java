package uz.app.jsp.Table.TestModule;

import java.lang.reflect.Field;
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
    private int postId;
    private float salary;

    public Employee(String name, int depId, int postId, float salary) {
        this.id = getNextId();
        this.name = name;
        this.depId = depId;
        this.postId = postId;
        this.salary = salary;
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
}
