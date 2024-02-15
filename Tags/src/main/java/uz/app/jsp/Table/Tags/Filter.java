package uz.app.jsp.Table.Tags;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.text.ParseException;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

import uz.app.jsp.Table.Options;

public class Filter {
    public enum Type { EQUAL, LIKE, RANGE, OPTION }

    private Column column;
    private Type type;
    private Class<? extends Options> optionsClass;
    private Options options;
    private Object[] values; // Defined as array for further extending filter types list.

    public Filter(Column column) {
        this.column = column;
        this.type = Type.EQUAL;
        column.setFilter(this);
        this.values = new Object[2];
    }

    public String getColumnName() {
        return column.getName();
    }

    public String getLabel() {
        return column.getLabel();
    }

    public Type getType() {
        return this.type;
    }
    public void setType(Type type) {
        this.type = type;
    }

    public void setOptionsClass(String optionsClass) throws ClassNotFoundException, ClassCastException {
        if(optionsClass == null)
            return;
        
        this.optionsClass = (Class<? extends Options>) Class.forName(optionsClass);
        Constructor<? extends Options> constructor = null;
        if(this.optionsClass.getConstructors().length <= 0)
            throw new RuntimeException("Options class must have default constructor");
        try {
            constructor = this.optionsClass.getConstructor();
        } catch (NoSuchMethodException | SecurityException e) {
            throw new RuntimeException("Couldn't get default constructor for options class ("+optionsClass+")", e);
        }

        try {
            this.options = constructor.newInstance();
        } catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
            throw new RuntimeException("Couldn't instantiate from options class", e);
        }
    }
    public Map<Object, String> getOptions() {
        if(this.options == null) {
            return new LinkedHashMap<Object, String>();
        }
        LinkedHashMap<Object, String> result = this.options.getOptions();
        if(result == null)
            result = new LinkedHashMap<Object, String>();

        return result;
    } 

    public Column.ColumnType getColumnType() {
        return column.getType();
    }
    public Object getValue() {
        if(this.values.length == 0)
            return null;
        return this.values[0];
    }
    public String getFormattedValue() {
        if(this.values.length == 0)
            return "";
        return column.formatValue(this.values[0]);
    }
    public void setValue(Object value) {
        if(this.values.length == 0) {
            this.values = new Object[2];
        }
        try {
            this.values[0] = column.Convert(value);
        } catch (ParseException e) {
            throw new IllegalArgumentException(e);
        }
    }
    public void setValuesArray(Object[] values) {
        if(this.values.length < values.length) {
            this.values = new Object[values.length];
        }
        for (int i = 0; i < values.length; i++) {
            try {
                this.values[i] = column.Convert(values[i]);
            } catch (ParseException e) {
                throw new IllegalArgumentException(e);
            }
        }
    }
    public void setValuesArray(Collection<Object> values) {
        setValuesArray(values.toArray());
    }
    public Object[] getValuesArray() {
        return this.values;
    }

    public Object getLeftBorder() {
        if(this.values.length == 0)
            return null;
        return this.values[0];
    }
    public String getFormattedLeftBorder() {
        if(this.values.length == 0)
            return "";
        return column.formatValue(this.values[0]);
    }
    public Object getRightBorder() {
        if(this.values.length < 2) {
            return null;
        }
        return this.values[1];
    }
    public String getFormattedRightBorder() {
        if(this.values.length < 2) {
            return "";
        }
        return column.formatValue(this.values[1]);
    }

    public void setLeftBorder(Object value) {
        if(this.values.length == 0) {
            this.values = new Object[2];
        }
        try {
            this.values[0] = column.Convert(value);
        } catch (ParseException e) {
            throw new IllegalArgumentException(e);
        }
    }
    public void setRightBorder(Object value) {
        if(this.values.length < 2){
            Object[] oldValues = this.values;
            this.values = new Object[2];
            if(oldValues.length > 0)
                this.values[0] = oldValues[0];
        }
        this.values[1] = value;
    }
    public boolean hasConstraints() {
        for (Object object : values) {
            if(object != null && !"".equals(object.toString().trim()))
                return true;
        }
        return false;
    }
    
}
