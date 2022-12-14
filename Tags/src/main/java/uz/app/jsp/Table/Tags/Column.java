package uz.app.jsp.Table.Tags;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import uz.app.jsp.Table.Util;

public class Column {
    public enum ColumnType {STRING, NUMBER, DATE}


    private String name;
    private boolean showColumn;
    private String label;
    private int orderValue;
    private ColumnType type = ColumnType.STRING;
    private String format = null;
    private Filter filter;

    private DecimalFormatSymbols decimalFormatSymbols;
    private DecimalFormat decimalFormat;

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
            this.decimalFormatSymbols = new DecimalFormatSymbols();
            this.decimalFormatSymbols.setGroupingSeparator(formatSymbols[0].charAt(0));
            this.decimalFormatSymbols.setDecimalSeparator(formatSymbols[1].charAt(0));
            
            this.decimalFormat = new DecimalFormat("###,###.##########", this.decimalFormatSymbols);
            this.decimalFormat.setMaximumFractionDigits(Integer.parseInt(formatSymbols[2].charAt(0) + ""));
        } catch(IndexOutOfBoundsException ex) {
            throw new RuntimeException("Wrong format for column <" + this.name + ">", ex);
        }
    }
    public String getFormat() {
        return this.format;
    }
    public String formatValue(Object value) {
        String formattedValue = "";
        if(value == null)
            return "";

        switch(this.type) {
            case NUMBER: {
                Double number;
                try {
                    number = Double.parseDouble(value.toString());
                } catch(NumberFormatException ex) {
                    throw new RuntimeException("Wrong string value for parsing!", ex);
                }
                
                formattedValue = decimalFormat.format(number);
                
                break;
            }
            case DATE: {
                Date valueDate;
                if(!(value instanceof Date)) {
                    try {
                        valueDate = Util.parseDateFromHtmlFormat(value.toString());
                    } catch(ParseException ex) {
                        throw new IllegalArgumentException("Wrong format of date: " + value.toString(), ex);
                    }
                } else {
                    valueDate = (Date)value;
                }
                formattedValue = Util.getDateInHtmlFormat(valueDate);
                break;
            }
            case STRING:
            default:
                formattedValue = value.toString();
        }
        return formattedValue;
    }

    public void setFilter(Filter filter) {
        this.filter = filter;
    }
    public Filter getFilter() {
        return this.filter;
    }
    public boolean hasFilterConstraints() {
        if(this.filter == null)
            return false;
        return this.filter.hasConstraints();
    }

    public Object Convert(Object value) throws ParseException {
        if(value == null)
            return null;
        Object result = value;
        switch(this.type) {
            case STRING: {
                result = result.toString();
                break;
            }
            case NUMBER: {
                if(!(value instanceof Number)) {
                    if("".equals(result.toString().trim()))
                        break;
                        //result = "0";
                    result = Double.valueOf(result.toString());
                }
                    
                break;
            }
            case DATE: {
                if(!(value instanceof Date)) {
                    if("".equals(result.toString().trim())) {
                        result = null;
                    } else {
                        try {
                            result = Util.parseDateFromHtmlFormat(result.toString());
                        } catch(ParseException ex) {
                            throw new IllegalArgumentException(ex);
                        }
                    }
                }
                break;
            }
        }
        return result;
    }
}
