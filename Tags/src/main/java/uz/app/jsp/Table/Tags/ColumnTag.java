package uz.app.jsp.Table.Tags;

import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.TagSupport;


public class ColumnTag extends TagSupport {
    private Table table;
    private Column column;

    public void setPageContext(PageContext pageContext) {
        super.setPageContext(pageContext);
        this.column = new Column();
    }

    public int doStartTag() {
        if(!(getParent() instanceof TableTag))
            throw new RuntimeException("INVALID_USAGE: Column-tag must be child tag for table-tag.");
        
        TableTag parentTableTag = (TableTag) getParent();
        this.table = parentTableTag.getTable();
        this.table.AddColumn(this.column);
        return SKIP_BODY;
    }

    public int doEndTag() {
        return EVAL_PAGE;
    }

    public void setOrderValue(String orderValue) {
        try {
            this.column.setOrderValue(Integer.parseInt(orderValue));
        } catch(NumberFormatException ex) {
            throw new RuntimeException("Invalid value for attribute 'id'", ex);
        }
    }

    public void setName(String name){
        this.column.setName(name);
    }
    
    public void setShow(String show) {
        if(show != null && !"".equals(show.trim()) && !"false".equals(show.trim().toLowerCase())) {
            this.column.setShowColumn(true);
        } else {
            this.column.setShowColumn(false);
        }
    }

    public void setLabel(String label) {
        this.column.setLabel(label);
    }
    
    public void setType(String type) {
        this.column.setType(type);
    }

    public void setFormat(String format) {
        this.column.setFormat(format);
    }

    public void setFilterType(String type) {
        if(type == null)
            return;
        Filter filter = this.column.getFilter();
        if(filter == null) {
            filter = new Filter(this.column);
        }
        type = type.toUpperCase();
        Filter.Type tp = null;
        try {
            tp = Filter.Type.valueOf(Filter.Type.class, type);
        } catch (IllegalArgumentException ex) {
            throw new RuntimeException("The filter type " + type + " is not defined.", ex);
        }
        filter.setType(tp);
    } 

    public void setFilterOptionsClass(String className) {
        if(className == null)
            return;
        
        Filter filter = this.column.getFilter();
        if(filter == null) {
            filter = new Filter(this.column);
            filter.setType(Filter.Type.OPTION);
        }
        try {
            filter.setOptionsClass(className);
        } catch (ClassNotFoundException | ClassCastException ex) {
            throw new RuntimeException("The class "+className+" not found or is not subclass of <uz.app.jsp.Options> class.", ex);
        }
    }
}
