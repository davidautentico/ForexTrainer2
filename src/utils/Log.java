/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

/**
 *
 * @author david
 */
public class Log {
    
    public static void addToLog(String logPath,String msg){
        //System.out.println("logPath :"+logPath);
        try {
            PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(logPath, true)));
            out.println(msg);
            out.close();
        } catch (IOException e) {
    //oh noes!
        }        
    }
}
