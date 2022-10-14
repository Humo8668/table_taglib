// package uz.app.jsp.Table.Tags;

// import java.io.IOException;
// import java.util.Iterator;
// import java.util.LinkedList;
// import java.util.List;
// import java.util.Map;

// import javax.servlet.jsp.JspWriter;
// import javax.servlet.jsp.PageContext;
// import javax.servlet.jsp.tagext.BodyTagSupport;


// public class TableTag extends BodyTagSupport {
//     public static final String TABLES_LIST = "TABLES_LIST";
//     public static final int DEFAULT_NUMBER_OF_ROWS_IN_PAGE = 20;
//     public static final int DEFAULT_PAGE_NUMBER = 1;

//     private String tableName;
//     private String dataJson;
//     private String caption;
//     private String dataSourceUrl;
//     private String dataStoreClass;
//     private boolean hasNumeration = false;

//     private Table table;
    
//     public void setPageContext(PageContext pageContext) {
//         super.setPageContext(pageContext);
//         List<Table> tables = (List<Table>)pageContext.getAttribute(TABLES_LIST);
//         if(tables == null) {
//             tables = new LinkedList<Table>();
//             pageContext.setAttribute(TABLES_LIST, tables);
//         }
        
//     }

//     private void initTableAttributes() {
//         this.table = new Table(this.getName());
//         this.table.setDataJson(this.dataJson);
//         this.table.setCaption(this.caption);
//         this.table.setNumeration(this.hasNumeration);
//         try {
//             this.table.setDataStoreClass(this.dataStoreClass);
//         } catch (ClassNotFoundException e1) {
//             throw new RuntimeException(e1);
//         }
//     }

//     private int getCurrentPageNumber() {
//         return DEFAULT_PAGE_NUMBER;
//     }

//     private int getCurrentRowsNumberInPage() {
//         return DEFAULT_NUMBER_OF_ROWS_IN_PAGE;
//     }

//     public int doStartTag() {
//         JspWriter out = pageContext.getOut();//returns the instance of JspWriter  
//         List<Table> tables = (List<Table>)pageContext.getAttribute(TABLES_LIST);
//         if(tables.stream().anyMatch((t) -> t.getName().equals(this.getName())))
//             throw new RuntimeException("Duplicate name of table <"+ this.getName() +">");

//         initTableAttributes();
//         tables.add(this.table);
//         return EVAL_BODY_INCLUDE;
//     }

//     private class HtmlHeader implements HtmlPart {
//         @Override
//         public void print(JspWriter out) throws IOException {
//             out.append("<link rel=\"stylesheet\" href=\""+pageContext.getRequest().getServletContext().getContextPath()+"/css/bootstrap.min.css\">");
//             out.append("<script type=\"text/javascript\" src=\""+pageContext.getRequest().getServletContext().getContextPath()+"/js/bootstrap.min.js\"></script>");
//         }
//     }

//     private class HtmlTable implements HtmlPart {
//         private HtmlPart tableForm;

//         public HtmlTable(HtmlPart tableForm) {
//             this.tableForm = tableForm;
//         }

//         @Override
//         public void print(JspWriter out) throws IOException {
//             tableForm.print(out);
//         }
//     }

//     private class HtmlPagination implements HtmlPart {
//         @Override
//         public void print(JspWriter out) throws IOException {
//             this.print(out, 1, 1);
//         }

//         public void print(JspWriter out, int pagesCount, int activePage) throws IOException {
//             out.append("<div class=\"btn-toolbar justify-content-center\" role=\"toolbar\" aria-label=\"Toolbar with button groups\">");
//             out.append("<div class=\"btn-group mr-2\" role=\"group\" aria-label=\"First group\">");
//             for(int i = 1; i <= pagesCount; i++) {
//                 if(i == activePage) {
//                     out.append("<button type=\"button\" class=\"btn btn-secondary active\" onclick=\"usersTable.onPageChanged("+i+")\">"+i+"</button>");
//                 } else {
//                     out.append("<button type=\"button\" class=\"btn btn-secondary\" onclick=\"usersTable.onPageChanged("+i+")\">"+i+"</button>");
//                 }
                
//             }
//             out.append("</div></div>");
//         }
//     }

//     private class HtmlTableControls implements HtmlPart {
//         private boolean hasPrinted = false;
//         @Override
//         public void print(JspWriter out) throws IOException {
//             if(hasPrinted)
//                 throw new RuntimeException("Printing table controls multiple times is inacceptable due to Javascript part support.");
//             this.hasPrinted = true;
//             out.append("<table><tr><td>");
//             out.append("<div class=\"input-group\"> <div class=\"input-group-prepend\">");
//             out.append("<span class=\"input-group-text\" id=\"rows-in-page-label\"><i class=\"fa fa-solid fa-list-ol\"></i></span></div>");
//             out.append("<input class=\"form-control\" name=\"rows_in_page\" type=\"number\" placeholder=\"Number of rows in page\" aria-describedby=\"rows-in-page-label\"  label=\"Number of rows in page\" value=\"20\" style=\"text-align: right;\">");
//             out.append("<input type=\"hidden\" name=\"page_number\" value=\"1\">");
//             out.append("</div> </td>");
//             out.append("<td> <button id=\"btnRefresh\" class=\"btn btn-dark\" onclick=\"usersTable.onRefresh();\">");
// 			out.append("<i class=\"fa fa-arrows-rotate\"></i>&nbsp;<span>Refresh</span> </button> </td>");
// 			out.append("<td> <button id=\"btnFilter\" class=\"btn btn-dark\"> <i class=\"fa fa-filter\"></i>&nbsp;<span>Filter</span> </button></td>");
// 			out.append("</tr> </table>");
//             out.append("<input type=\"hidden\" name=\"users\" value=\"page;rows_in_page\">");
//         }
//     }

//     private class HtmlTableForm implements HtmlPart {
//         private HtmlPart pagination;
//         private HtmlPart tableControls;
//         private HtmlPart table;

//         HtmlTableForm() {
//             this.pagination = new HtmlPagination();
//             this.tableControls = new HtmlTableControls();
//             this.table = new HtmlTable();
//         }

//         @Override
//         public void print(JspWriter out) throws IOException {
//             pagination.print(out);

//         }
//     }
 
//     private void printTable(JspWriter out) throws IOException {
//         List<Column> showableColumns = this.table.getShowableColumns(); // columns list sorted by order value
//         /*showableColumns.sort(
//             (o1, o2) -> (o1.getOrderValue() < o2.getOrderValue())? -1:
//                         (o1.getOrderValue() == o2.getOrderValue())? 0 : 1
//         );*/
//         out.append("<table class=\"table table-striped table-bordered table-hover table-sm\" >");
//         if(this.table.getCaption() != null) {
//             out.append("<caption>"+this.table.getCaption()+"</caption>");
//         }
//         out.append("<thead class=\"thead-dark\">");
//         out.append("<tr>");
//         if(this.table.hasNumeration()) {
//             out.append("<th scope=\"col\">#</th>");
//         }
//         for (Column column: showableColumns) {
//             out.append("<th scope=\"col\">"+column.getLabel()+"</th>");
//         }
        
//         out.append("</tr>");
//         out.append("</thead>");
        
//         int rowNumeration = 1;
//         out.append("<tbody>");

//         int currentPage = getCurrentPageNumber();
//         int rowsNumberInPage = getCurrentRowsNumberInPage();
//         Iterable<Map<String, Object>> rowsData = this.table.getRows(rowsNumberInPage, currentPage);
//         Iterator<Map<String, Object>> rowIterator = rowsData.iterator();
//         // if there's no any data, then print row with "no data" text.
//         if(!rowIterator.hasNext()) { 
//             out.append("<tr>");
//             out.append("<td align=\"center\" colspan='100%'>(No data)</td>");
//             out.append("</tr>");
//         } else {
//             Map<String,Object> rowObject;
//             while(rowIterator.hasNext()) {
//                 rowObject = rowIterator.next();
//                 out.append("<tr>");
//                 if(this.table.hasNumeration()) {
//                     out.append("<td>"+rowNumeration+"</td>");
//                 }
//                 for (Column column : showableColumns) {
//                     String formattedValue = column.formatValue(rowObject.getOrDefault(column.getName(), "").toString());
//                     out.append("<td>"+formattedValue+"</td>");
//                 }
//                 out.append("</tr>");
//                 rowNumeration++;
//             }
//         }
        
//         out.append("</tbody>");
//         out.append("</table>");
//     }

//     public int doEndTag() {
//         JspWriter out = pageContext.getOut();//returns the instance of JspWriter
//         try{
            
            
//             // ***************************************************************************
//             printHeader(out);
//             printTable(out);

//             out.flush();
//         }catch(Exception e){
//             throw new RuntimeException(e);
//         }  
//         return EVAL_PAGE;
//     }

//     public void setName(String name) {
//         this.tableName = name;
//     } 

//     public String getName() {
//         return this.tableName;
//     }

//     protected Table getTable() {
//         return this.table;
//     }

//     public void setDataJson(String jsonStr) {
//         this.dataJson = jsonStr;
//     }
    
//     public void setCaption(String caption) {
//         this.caption = caption;
//     }
    
//     public void setDataSourceUrl(String dataSourceUrl) {
//         if(dataSourceUrl == null)
//             return;
//         this.dataSourceUrl = dataSourceUrl;
//     }

//     public void setDataStoreClass(String className) {
//         if(className == null)
//             return;
//         this.dataStoreClass = className;
//     }

//     public void setNumeration(String enableNumeration) {
//         this.hasNumeration = Boolean.parseBoolean(enableNumeration);
//     }
// }
