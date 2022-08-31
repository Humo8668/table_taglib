package uz.app.jsp.Table.Tags;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.BodyTagSupport;


public class TableTag extends BodyTagSupport {
    public static final String TABLES_LIST = "TABLES_LIST";
    public static final int DEFAULT_NUMBER_OF_ROWS_IN_PAGE = 20;
    public static final int DEFAULT_PAGE_NUMBER = 1;

    private String tableName;
    private String dataJson;
    private String caption;
    private String dataSourceUrl;
    private String dataStoreClass;
    private boolean hasNumeration = false;

    private Table table;
    
    public void setPageContext(PageContext pageContext) {
        super.setPageContext(pageContext);
        List<Table> tables = (List<Table>)pageContext.getAttribute(TABLES_LIST);
        if(tables == null) {
            tables = new LinkedList<Table>();
            pageContext.setAttribute(TABLES_LIST, tables);
        }
        
    }

    private void initTableAttributes() {
        this.table = new Table(this.getName());
        this.table.setDataJson(this.dataJson);
        this.table.setCaption(this.caption);
        this.table.setNumeration(this.hasNumeration);
        try {
            this.table.setDataStoreClass(this.dataStoreClass);
        } catch (ClassNotFoundException e1) {
            throw new RuntimeException(e1);
        }
    }

    private int getCurrentPageNumber() {
        return DEFAULT_PAGE_NUMBER;
    }

    private int getCurrentRowsNumberInPage() {
        return DEFAULT_NUMBER_OF_ROWS_IN_PAGE;
    }

    public int doStartTag() {
        JspWriter out = pageContext.getOut();//returns the instance of JspWriter  
        List<Table> tables = (List<Table>)pageContext.getAttribute(TABLES_LIST);
        if(tables.stream().anyMatch((t) -> t.getName().equals(this.getName())))
            throw new RuntimeException("Duplicate name of table <"+ this.getName() +">");

        initTableAttributes();
        tables.add(this.table);
        return EVAL_BODY_INCLUDE;
    }

    public int doEndTag() {
        JspWriter out = pageContext.getOut();//returns the instance of JspWriter
        try{
            List<Column> showableColumns = this.table.getShowableColumns(); // columns list sorted by order value
            /*showableColumns.sort(
                (o1, o2) -> (o1.getOrderValue() < o2.getOrderValue())? -1:
                            (o1.getOrderValue() == o2.getOrderValue())? 0 : 1
            );*/
            out.append("<link rel=\"stylesheet\" href=\""+pageContext.getRequest().getServletContext().getContextPath()+"/css/bootstrap.min.css\">");
            out.append("<script type=\"text/javascript\" src=\""+pageContext.getRequest().getServletContext().getContextPath()+"/js/bootstrap.min.js\"></script>");
            // ***************************************************************************
            

            out.append("<table class=\"table table-striped table-bordered table-hover table-sm\" >");
            if(this.table.getCaption() != null) {
                out.append("<caption>"+this.table.getCaption()+"</caption>");
            }
            out.append("<thead class=\"thead-dark\">");
            out.append("<tr>");
            if(this.table.hasNumeration()) {
                out.append("<th scope=\"col\">#</th>");
            }
            for (Column column: showableColumns) {
                out.append("<th scope=\"col\">"+column.getLabel()+"</th>");
            }
            
            out.append("</tr>");
            out.append("</thead>");
            
            int rowNumeration = 1;
            out.append("<tbody>");

            int currentPage = getCurrentPageNumber();
            int rowsNumberInPage = getCurrentRowsNumberInPage();
            Iterable<Map<String, Object>> rowsData = this.table.getRows(rowsNumberInPage, currentPage);
            Iterator<Map<String, Object>> rowIterator = rowsData.iterator();
            // if there's no any data, then print row with "no data" text.
            if(!rowIterator.hasNext()) { 
                out.append("<tr>");
                out.append("<td align=\"center\" colspan='100%'>(No data)</td>");
                out.append("</tr>");
            } else {
                Map<String,Object> rowObject;
                while(rowIterator.hasNext()) {
                    rowObject = rowIterator.next();
                    out.append("<tr>");
                    if(this.table.hasNumeration()) {
                        out.append("<td>"+rowNumeration+"</td>");
                    }
                    for (Column column : showableColumns) {
                        String formattedValue = column.formatValue(rowObject.getOrDefault(column.getName(), "").toString());
                        out.append("<td>"+formattedValue+"</td>");
                    }
                    out.append("</tr>");
                    rowNumeration++;
                }
            }
            
            out.append("</tbody>");
            out.append("</table>");
            
            out.flush();
        }catch(Exception e){
            throw new RuntimeException(e);
        }  
        return EVAL_PAGE;
    }

    public void setName(String name) {
        this.tableName = name;
    } 

    public String getName() {
        return this.tableName;
    }

    protected Table getTable() {
        return this.table;
    }

    public void setDataJson(String jsonStr) {
        this.dataJson = jsonStr;
    }
    
    public void setCaption(String caption) {
        this.caption = caption;
    }
    
    public void setDataSourceUrl(String dataSourceUrl) {
        if(dataSourceUrl == null)
            return;
        this.dataSourceUrl = dataSourceUrl;
    }

    public void setDataStoreClass(String className) {
        if(className == null)
            return;
        this.dataStoreClass = className;
    }

    public void setNumeration(String enableNumeration) {
        this.hasNumeration = Boolean.parseBoolean(enableNumeration);
    }
}
