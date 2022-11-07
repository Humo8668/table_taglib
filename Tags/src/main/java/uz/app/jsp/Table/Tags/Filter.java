package uz.app.jsp.Table.Tags;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import uz.app.jsp.Table.Options;

public class Filter {
    public enum Type { EQUAL, LIKE, RANGE, OPTION }

    private Column column;
    private Type type;
    private Class<? extends Options> optionsClass;
    private Options options;

    public Filter(Column column) {
        this.column = column;
        this.type = Type.EQUAL;
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
    public Options getOptions() {
        return this.options;
    } 

    public Column.ColumnType getColumnType() {
        return column.getType();
    }
}
