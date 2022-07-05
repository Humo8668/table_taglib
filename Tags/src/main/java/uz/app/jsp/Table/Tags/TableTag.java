package uz.app.jsp.Table.Tags;

import java.util.Date;

import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;

public class TableTag extends TagSupport {
    public int doStartTag() {
        JspWriter out = pageContext.getOut();//returns the instance of JspWriter  
        try{
            out.print("<h1>Begin of table tag</h1><br>");//printing date and time using JspWriter
            out.print(new Date());//printing date and time using JspWriter  
        }catch(Exception e){System.out.println(e);}  
        return SKIP_BODY;//will not evaluate the body content of the tag  
    }

    public int doEndTag() {
        JspWriter out = pageContext.getOut();//returns the instance of JspWriter  
        try{  
            out.print("<br>");
            out.print("<h1>End of table tag</h1>");//printing date and time using JspWriter  
        }catch(Exception e){System.out.println(e);}  
        return SKIP_BODY;
    }
}
