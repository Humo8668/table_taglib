package uz.app.jsp.Table;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Util {
    public static final String HTML_DATE_FORMAT = "yyyy-MM-dd";
    public static final String SHOW_DATE_FORMAT = "yyyy-MM-dd";
    
    public static String getDateInHtmlFormat(Date date) {
        SimpleDateFormat format = new SimpleDateFormat(HTML_DATE_FORMAT);
        return format.format(date);
    }
    public static Date parseDateFromHtmlFormat(String dateStr) throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat(HTML_DATE_FORMAT);
        return format.parse(dateStr);
    }
    public static String getDateInShowFormat(Date date) {
        SimpleDateFormat showFormat = new SimpleDateFormat(SHOW_DATE_FORMAT);
        return showFormat.format(date);
    }
}
