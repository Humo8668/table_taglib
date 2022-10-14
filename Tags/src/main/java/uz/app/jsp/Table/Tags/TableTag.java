package uz.app.jsp.Table.Tags;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.PatternSyntaxException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.BodyTagSupport;


public class TableTag extends BodyTagSupport {
    static final String TABLES_LIST = "TABLES_LIST";
    static final int DEFAULT_NUMBER_OF_ROWS_IN_PAGE = 20;
    static final int DEFAULT_PAGE_NUMBER = 1;

    private String tableName;
    private String dataJson;
    private String caption;
    private String dataSourceUrl;
    private String dataStoreClass;
    private boolean hasNumeration = false;
    private int activePageNumber = DEFAULT_PAGE_NUMBER;
    private int rowsInPage = DEFAULT_NUMBER_OF_ROWS_IN_PAGE;

    private Table table;
    private ServletRequest request;
    private ServletResponse response;
    private HttpSession session;
    
    public void setPageContext(PageContext pageContext) {
        super.setPageContext(pageContext);
        List<Table> tables = (List<Table>)pageContext.getAttribute(TABLES_LIST);
        if(tables == null) {
            tables = new LinkedList<Table>();
            pageContext.setAttribute(TABLES_LIST, tables);
        }
        this.request = pageContext.getRequest();
        this.response = pageContext.getResponse();
        this.session = pageContext.getSession();
    }

    private void initArguments() {
        String tableArgumentsStr = this.request.getParameter(this.tableName);
        if(tableArgumentsStr == null || "".equals(tableArgumentsStr)) {
            String key_tableArgsInSession = "table_" + this.tableName;
            Object tableArgumentsInSession = this.session.getAttribute(key_tableArgsInSession);
            if(tableArgumentsInSession != null && tableArgumentsInSession instanceof String) {
                tableArgumentsStr = tableArgumentsInSession.toString();
            } else {
                tableArgumentsStr = DEFAULT_PAGE_NUMBER + ";" + DEFAULT_NUMBER_OF_ROWS_IN_PAGE;
            }
        }
        
        try {
            String[] tableArguments = tableArgumentsStr.split(";");
            this.activePageNumber = Integer.valueOf(tableArguments[0]);
            this.rowsInPage = Integer.valueOf(tableArguments[1]);
        } catch(NumberFormatException | PatternSyntaxException ex) {
            this.activePageNumber = DEFAULT_PAGE_NUMBER;
            this.rowsInPage = DEFAULT_NUMBER_OF_ROWS_IN_PAGE;
            tableArgumentsStr = DEFAULT_PAGE_NUMBER + ";" + DEFAULT_NUMBER_OF_ROWS_IN_PAGE;
        }
        this.session.setAttribute("table_" + this.tableName, tableArgumentsStr);
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

        initArguments();
    }

    private int getCurrentActivePage() {
        return this.activePageNumber;
    }

    private int getCurrentRowsNumberInPage() {
        return this.rowsInPage;
    }

    private int getCurrentPagesCount() {
        int overall = table.getOverallRowsCount();
        return overall / this.rowsInPage + ((overall % this.rowsInPage != 0)?1:0);
    }

    public int doStartTag() {
        JspWriter out = pageContext.getOut();//returns the instance of JspWriter  
        try {
            out.flush();
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
        List<Table> tables = (List<Table>)pageContext.getAttribute(TABLES_LIST);
        if(tables.stream().anyMatch((t) -> t.getName().equals(this.getName())))
            throw new RuntimeException("Duplicate name of table <"+ this.getName() +">");

        initTableAttributes();
        tables.add(this.table);
        return EVAL_BODY_INCLUDE;
    }

    private void includeTableTemplate() {
        final String LOCALHOST = "http://127.0.0.1:3500";
        final String TEMPLATE_URL = "/templates/TableTemplate.jsp";

        RequestDispatcher rd = pageContext.getServletContext().getRequestDispatcher(TEMPLATE_URL);
        request.setAttribute("table", this.table);
        request.setAttribute("pages_count", getCurrentPagesCount());
        request.setAttribute("active_page", getCurrentActivePage());
        request.setAttribute("rows_in_page", getCurrentRowsNumberInPage());
        request.setAttribute("overall_rows", this.getTable().getOverallRowsCount());
        try {
            rd.include(request, response);
        } catch (ServletException | IOException ex) {
            throw new RuntimeException(ex);
        }
    }
 
    private void printTable(JspWriter out) throws IOException {
        includeTableTemplate();
    }

    public int doEndTag() {
        JspWriter out = pageContext.getOut();//returns the instance of JspWriter
        try{
            printTable(out);
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
