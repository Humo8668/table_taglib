package uz.app.jsp.Table.TestModule;

import java.util.LinkedHashMap;

import uz.app.jsp.Table.Options;

public class DepartmentOptions implements Options {
    @Override
    public LinkedHashMap<Object, String> getOptions() {
        LinkedHashMap<Object, String> result = new LinkedHashMap<Object, String>();
        result.put("1", "Department of Development");
        result.put("2", "Department of Foreign Investments");
        result.put("3", "IT department");
        return result;
    }
}
