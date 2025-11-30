package Utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateTimeUtils {

    public static String currentDate_YYYY_MM_DD;
    public static String currentDate_yyyyMMdd;
    public static String currentTime_hhMMss;
    public static String currentTime_hh;
    public static String currentTime_MM;
    public static String currentTime_ss;
    public static String currentTime_SSS;
    public static String currentTime_hhMMssSSS;

    public DateTimeUtils(){
        Date currDateTm = new Date();
        SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
        String currentDate = sdfDate.format(currDateTm);

        currentDate_YYYY_MM_DD = currentDate.substring(0,10);
        currentTime_hh = currentDate.substring(11,13);
        currentTime_MM = currentDate.substring(14,16);
        currentTime_ss = currentDate.substring(17,19);
        currentTime_SSS = currentDate.substring(20,23);
        currentTime_hhMMss = currentTime_hh+currentTime_MM+currentTime_ss;
        currentTime_hhMMssSSS = currentTime_hh+currentTime_MM+currentTime_ss+currentTime_SSS;

        sdfDate = new SimpleDateFormat("yyyyMMdd");
        currentDate = sdfDate.format(currDateTm);

        currentDate_yyyyMMdd = currentDate;
    }//end constructor

}//end class DateTimeUtils
