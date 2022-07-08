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

    private String tableName;
    private String dataJson;
    private Table table;
    
    public void setPageContext(PageContext pageContext) {
        super.setPageContext(pageContext);
        List<Table> tables = (List<Table>)pageContext.getAttribute(TABLES_LIST);
        if(tables == null) {
            tables = new LinkedList<Table>();
            pageContext.setAttribute(TABLES_LIST, tables);
        }
        
    }

    public int doStartTag() {
        this.table = new Table(this.getName());
        this.table.setDataJson(this.dataJson);
        List<Table> tables = (List<Table>)pageContext.getAttribute(TABLES_LIST);
        tables.add(this.table);

        JspWriter out = pageContext.getOut();//returns the instance of JspWriter  
        try{
            out.print("<link rel=\"stylesheet\" href=\"css/bootstrap.min.css\">");
            out.print("<script type=\"text/javascript\" src=\"js/bootstrap.min.js\"></script>");
            // ***************************************************************************
            out.print("<table class=\"table table-striped table-bordered table-hover table-sm\" >");

        }catch(Exception e){System.out.println(e);}  
        return EVAL_BODY_INCLUDE;
    }

    public int doEndTag() {
        JspWriter out = pageContext.getOut();//returns the instance of JspWriter
        try{
            List<Column> showableColumns = this.table.getShowableColumns();
            showableColumns.sort(
                (o1, o2) -> (o1.getOrderValue() < o2.getOrderValue())? -1:
                            (o1.getOrderValue() == o2.getOrderValue())? 0 : 1
            );
            
            out.print("<thead class=\"thead-dark\">");
            out.print("<tr>");
            out.print("<th scope=\"col\">#</th>");
            for (Column column: showableColumns) {
                out.print("<th scope=\"col\">"+column.getLabel()+"</th>");
            }
            
            out.print("</tr>");
            out.print("</thead>");
            
            int rowNumeration = 1;
            out.print("<tbody>");

            List<Map<String, Object>> rowsData = this.table.getRows();
            Iterator<Map<String, Object>> rowIterator = rowsData.iterator();
            Map<String,Object> rowObject;
            while(rowIterator.hasNext()) {
                rowObject = rowIterator.next();
                out.print("<tr>");
                out.print("<td>"+rowNumeration+"</td>");
                for (Column column : showableColumns) {
                    out.print("<td>"+rowObject.getOrDefault(column.getName(), "<no data>")+"</td>");
                }
                out.print("</tr>");
                rowNumeration++;
            }
            out.print("</tbody>");
            out.print("</table>");
        }catch(Exception e){System.out.println(e);}  
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
}
