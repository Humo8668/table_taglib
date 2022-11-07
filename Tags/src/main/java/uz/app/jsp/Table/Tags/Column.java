package uz.app.jsp.Table.Tags;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

public class Column {
    public enum ColumnType {STRING, NUMBER, DATE}

    private String name;
    private boolean showColumn;
    private String label;
    private int orderValue;
    private ColumnType type = ColumnType.STRING;
    private String format = null;
    private Filter filter;

    private DecimalFormatSymbols formatSymbols;
    private DecimalFormat formatObject;

    public Column(String name) {
        this.name = name;
    }

    protected Column() {
        // There's no order of initialization of attributes. We must be prepared for skipping NullPointerException.
        this.setFormat(" |.|2");
    }

    public String getName() {
        return name;
    }
    protected void setName(String name) {
        this.name = name;
    }
    public boolean isShowColumn() {
        return showColumn;
    }
    public void setShowColumn(boolean showColumn) {
        this.showColumn = showColumn;
    }
    public String getLabel() {
        return label;
    }
    public void setLabel(String label) {
        this.label = label;
    }
    public int getOrderValue() {
        return orderValue;
    }
    public void setOrderValue(int orderValue) {
        this.orderValue = orderValue;
    }
    public void setType(String type) {
        this.type = ColumnType.valueOf(type.toUpperCase());
    }
    public ColumnType getType() {
        return this.type;
    }
    public void setFormat(String format) {
        this.format = format;
        String[] formatSymbols = this.format.split("[|]");
        try {
            this.formatSymbols = new DecimalFormatSymbols();
            this.formatSymbols.setGroupingSeparator(formatSymbols[0].charAt(0));
            this.formatSymbols.setDecimalSeparator(formatSymbols[1].charAt(0));
            
            this.formatObject = new DecimalFormat("###,###.##########", this.formatSymbols);
            this.formatObject.setMaximumFractionDigits(Integer.parseInt(formatSymbols[2].charAt(0) + ""));
        } catch(IndexOutOfBoundsException ex) {
            throw new RuntimeException("Wrong format for column <" + this.name + ">", ex);
        }
    }
    public String getFormat() {
        return this.format;
    }
    public String formatValue(String value) {
        String formattedValue = "";
        switch(this.type) {
            case NUMBER: {
                Double number;
                try {
                    number = Double.parseDouble(value);
                } catch(NumberFormatException ex) {
                    throw new RuntimeException("Wrong string value for parsing!", ex);
                }
                
                formattedValue = formatObject.format(number);
                
                break;
            }
            case STRING:
            default:
                formattedValue = value;
        }
        return formattedValue;
    }

    public void setFilter(Filter filter) {
        this.filter = filter;
    }
    public Filter getFilter() {
        return this.filter;
    }
}
