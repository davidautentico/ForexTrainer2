/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

import java.util.Calendar;

/**
 *
 * @author drosa
 */
public class FileUtils {
    
     
     
     public  static String getInterval(String fileName){
         if (fileName.contains("1_6")) return "1_6";
         if (fileName.contains("7_12")) return "7_12";
         return "";
     }
     
    
    public static boolean containsDayOfWeek(String fileName){
        
         if (fileName.contains("Mondays"))
                 return true;
         if (fileName.contains("Tuesdays"))
                 return true;
         if (fileName.contains("Wednesdays"))
                 return true;
         if (fileName.contains("Thursdays"))
                 return true;
         if (fileName.contains("Fridays"))
                 return true;
         
         return false;
     }
     
     public static String getDayOfWeek(String fileName){
         if (fileName.contains("Mondays"))
                 return "Mondays";
         if (fileName.contains("Tuesdays"))
                 return "Tuesdays";
         if (fileName.contains("Wednesdays"))
                 return "Wednesdays";
         if (fileName.contains("Thursdays"))
                 return "Thursdays";
         if (fileName.contains("Fridays"))
                 return "Fridays";
         
         return "";
     }
     
    public static String generateFileNameFromDate(Calendar cal){
        String fileName = "";
        
        int dayMonth    = cal.get(Calendar.DAY_OF_MONTH);
        int month       = cal.get(Calendar.MONTH);
        int year        = cal.get(Calendar.YEAR);
        
        fileName = String.valueOf(dayMonth)+String.valueOf(month)
                +String.valueOf(year);
        
        return fileName;
    }
  
    public static String getFileNameFromCalendar(Calendar cal){
        String fileName ="";
        fileName = cal.get(Calendar.DAY_OF_MONTH)
                +"_"+cal.get(Calendar.MONTH)
                +"_"+cal.get(Calendar.YEAR)
                +"_"+cal.get(Calendar.HOUR_OF_DAY)
                +cal.get(Calendar.MINUTE)
                +cal.get(Calendar.SECOND)
                ;
        return fileName;
    }
}
