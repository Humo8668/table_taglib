package uz.app.jsp.Table.Tags;

public class Column {
    private String name;
    private boolean showColumn;
    private String label;
    private int orderValue;

    public Column(String name) {
        this.name = name;
    }

    protected Column() {}

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
}
