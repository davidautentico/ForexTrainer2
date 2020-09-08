/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

import Trading.DataGranularity;
import Trading.Quote;
import java.io.File;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;

/**
 *
 * @author david
 */
public class DataUtils {
    
    public static DataGranularity getGranularity(ArrayList<Quote> data){
        Quote q0 = data.get(0);
        Quote q1 = data.get(1);
        
        Calendar cal0 = Calendar.getInstance();
        Calendar cal1 = Calendar.getInstance();
        
        cal0.setTime(q0.getDate());
        cal1.setTime(q1.getDate());
        
        int minDiff = cal1.get(Calendar.MINUTE)-cal0.get(Calendar.MINUTE);
        
        if (minDiff==1)
            return DataGranularity.MIN1;
        if (minDiff==5)
            return DataGranularity.MIN5;
        
        //System.out.println("DIFF: "+minDiff);
        return DataGranularity.UNKNOWN;
    }
    
    public static ArrayList<String> loadDataFilesFromPath(String path){
        ArrayList<String> files = new ArrayList<String>();
        
        //load 1 sec files
        File folder = new File(path);
        File[] listOfFiles = folder.listFiles();

        for (File file : listOfFiles) {
            //System.out.println("Fichero: "+file.getName());
            String fileDataName = file.getName();
            String fileAbsolutePath= path+"\\"+fileDataName;
            if (file.isFile() && fileDataName.contains("_data") && !fileDataName.contains("trim")){                            
                //System.out.println(file.getName());
                files.add(fileAbsolutePath);
            }
        }
        
        return files;
    }
    
    public static double truncateDoubleTwoDecimals(double value){
        DecimalFormat df = new DecimalFormat("#.##");
        df.setRoundingMode(RoundingMode.DOWN);
        return Double.valueOf(df.format(value).replace(',', '.'));
    }
}
