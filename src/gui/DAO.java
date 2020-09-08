/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import Trading.Currency;
import Trading.DataProvider;
import Trading.DateUtils;
import Trading.GameStats;
import Trading.NewsImpact;
import Trading.NewsItem;
import Trading.Quote;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import utils.PrintUtils;
import utils.SQLConnectionUtils;

/**
 *
 * @author david
 */
public class DAO {
    private static Quote decodeQuote(String linea,int type){
        
        Date date = null;
	double open = 0;
	double high = 0;
	double low = 0;
	double close = 0;
	long volume= 0;
        //System.out.println(linea);
       
	if (type==1){
            //hora		
            date = DateUtils.getCSIDate(linea.split(" ")[0].trim());
			
            open = Float.valueOf(linea.split(" ")[1].trim());
            high = Float.valueOf(linea.split(" ")[2].trim());
            low = Float.valueOf(linea.split(" ")[3].trim());
            close = Float.valueOf(linea.split(" ")[4].trim());
            volume= Long.valueOf(linea.split(" ")[5].trim());
        }
        
        if (type==2){
            //hora		
            date = DateUtils.getDukasDate(linea.split(" ")[0].trim(),linea.split(" ")[1].trim());
			
            open = Float.valueOf(linea.split(" ")[2].trim());
            high = Float.valueOf(linea.split(" ")[3].trim());
            low = Float.valueOf(linea.split(" ")[4].trim());
            close = Float.valueOf(linea.split(" ")[5].trim());
            //volume= Long.valueOf(linea.split(" ")[6].trim());
        }
	
        if (date!=null){
            Quote fq = new Quote();
            fq.setDate(date);
            fq.setOpen(open);
            fq.setClose(close);
            fq.setHigh(high);
            fq.setLow(low);		
            //fq.setVolume(volume);
            return fq;
        }
            return null;
	
    }
    
    private static Quote decodeQuote(String linea,DataProvider type){
			Date date = null;
			double open = 0;
			double high = 0;
			double low = 0;
			double close = 0;
			long volume= 0;
		
			if (type==DataProvider.DUKASCOPY_FOREX){
				 //hora		
	            date = DateUtils.getDukasDate(linea.split(" ")[0].trim(),linea.split(" ")[1].trim());
				
	            open = Float.valueOf(linea.split(" ")[2].trim());
	            high = Float.valueOf(linea.split(" ")[3].trim());
	            low = Float.valueOf(linea.split(" ")[4].trim());
	            close = Float.valueOf(linea.split(" ")[5].trim());
			}
			
			if (type==DataProvider.PEPPERSTONE_FOREX){
				 //hora		
	            date = DateUtils.getPepperDate(linea.split(",")[0].trim(),linea.split(",")[1].trim());
				
	            open = Float.valueOf(linea.split(",")[2].trim());
	            high = Float.valueOf(linea.split(",")[3].trim());
	            low = Float.valueOf(linea.split(",")[4].trim());
	            close = Float.valueOf(linea.split(",")[5].trim());
			}
						
			Quote fq = new Quote();
			
			fq.setDate(date);
			fq.setOpen(open);
			fq.setClose(close);
			fq.setHigh(high);
			fq.setLow(low);		
			fq.setVolume(volume);
			
			//System.out.println(DateUtils.datePrint(date)+" "+PrintUtils.getOHLC(fq));
			return fq;
		}
		
    public static ArrayList<Quote> retrieveData(String fileName){
			
        ArrayList<Quote> data = new  ArrayList<Quote>();
	File archivo = null;
        FileReader fr = null;
        BufferedReader br = null;

        try {
            // Apertura del fichero y creacion de BufferedReader para poder
            // hacer una lectura comoda (disponer del metodo readLine()).
            archivo = new File (fileName);
            fr = new FileReader (archivo);
            br = new BufferedReader(fr);
            // Lectura del fichero
            String line;
            int i=0;
            while((line=br.readLine())!=null){
                if (i>0){
                    Quote q = decodeQuote(line,2);
                    if (q!=null)                    
                        data.add(q);
		}
		i++;
            }    
        }catch(Exception e){
            e.printStackTrace();
        }finally{
            // En el finally cerramos el fichero, para asegurarnos
            // que se cierra tanto si todo va bien como si salta 
            // una excepcion.
            try{                    
               if( null != fr ){   
                  fr.close();     
               }                  
            }catch (Exception e2){ 
               e2.printStackTrace();
            }
      }	
       return data;
    }
    
    
    
    public static ArrayList<Quote> retrieveData(String fileName,DataProvider providerType){
			ArrayList<Quote> data = new  ArrayList<Quote>();
			File archivo = null;
		    FileReader fr = null;
		    BufferedReader br = null;

		    try {
		    	// Apertura del fichero y creacion de BufferedReader para poder
		        // hacer una lectura comoda (disponer del metodo readLine()).
		        archivo = new File (fileName);
		        fr = new FileReader (archivo);
		        br = new BufferedReader(fr);

		        // Lectura del fichero
		        String line;
		        int i=0;
		        while((line=br.readLine())!=null){
		        	if (i>0){
		        		 Quote q = decodeQuote(line,providerType);     	
		        		 data.add(q);
		        	}
		        	i++;
		        }    
		    }catch(Exception e){
		    	e.printStackTrace();
		    }finally{
		         // En el finally cerramos el fichero, para asegurarnos
		         // que se cierra tanto si todo va bien como si salta 
		         // una excepcion.
		         try{                    
		            if( null != fr ){   
		               fr.close();     
		            }                  
		         }catch (Exception e2){ 
		            e2.printStackTrace();
		         }
		   }	
		    return data;
		}
    
    
    public static ArrayList<Quote> retrieveData(String fileName,DataProvider providerType,int startLine){
			ArrayList<Quote> data = new  ArrayList<Quote>();
			File archivo = null;
		    FileReader fr = null;
		    BufferedReader br = null;

		    try {
		    	// Apertura del fichero y creacion de BufferedReader para poder
		        // hacer una lectura comoda (disponer del metodo readLine()).
		        archivo = new File (fileName);
		        fr = new FileReader (archivo);
		        br = new BufferedReader(fr);

		        // Lectura del fichero
		        String line;
		        int i=0;
		        while((line=br.readLine())!=null){
		        	if (i>=startLine){
		        		 Quote q = decodeQuote(line,providerType);     	
		        		 data.add(q);
		        	}
		        	i++;
		        }    
		    }catch(Exception e){
		    	e.printStackTrace();
		    }finally{
		         // En el finally cerramos el fichero, para asegurarnos
		         // que se cierra tanto si todo va bien como si salta 
		         // una excepcion.
		         try{                    
		            if( null != fr ){   
		               fr.close();     
		            }                  
		         }catch (Exception e2){ 
		            e2.printStackTrace();
		         }
		   }	
		    return data;
		}
    
    
    public static void insertGameStats(SQLConnectionUtils sql,GameStats gameStats){
        String qry = null;
						
        try{
            Connection conn = sql.getConnection();
            Statement stmt = conn.createStatement();	
				
            //prepare date
            java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            System.out.println("[dao NEW GAME]: "+DateUtils.datePrint((GregorianCalendar)gameStats.getGameDateTime()));
            Date date = gameStats.getGameDateTime().getTime();
            String currentDate = sdf.format(date);
            //String currentDate = DateUtils.datePrint((GregorianCalendar)gameStats.getGameDateTime());
                    // Print the users table
                    qry = "INSERT INTO gameScores (gamedatetime,totaltrades,totalhedges,avgwinpips,avgpips)"+
                                     " VALUES('"+
                                     currentDate+
                                     "',"+gameStats.getTotalTrades()+
                                     ","+gameStats.getTotalHedges()+
                                     ","+PrintUtils.Print(gameStats.getAvgWinPips())+						
                                     ","+PrintUtils.Print(gameStats.getAvgPips())
                            +")";

                    stmt.executeUpdate(qry);

                    //rs.close();
                    stmt.close();							
            }catch(Exception e){
                    System.out.println("[error] insertGameStats(: "+e.getMessage());
                    System.out.println("query: "+qry);
                    //e.printStackTrace();				
            }
    }
    
    public static void insertHedgeDays(SQLConnectionUtils sql,Calendar day,ArrayList<Calendar> hedgeDays){
        String qry = null;
						
        try{
            Connection conn = sql.getConnection();
            Statement stmt = conn.createStatement();	
				
            //prepare date
            java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            System.out.println("[dao insertHedgeDays]: "+DateUtils.datePrint((GregorianCalendar)day));
            Date date = day.getTime();
            String gameDate = sdf.format(date);
            
            for (int i=0;i<hedgeDays.size();i++){
                Calendar hedgeDay = hedgeDays.get(i);
                String hedgeDate = sdf.format(hedgeDay.getTime());
                qry = "INSERT INTO hedgeDays (gamedatetime,day)"+
                                     " VALUES('"+
                                     gameDate+
                                     "','"+hedgeDate
                            +"')";

                    stmt.executeUpdate(qry);
            }
            stmt.close();
            }catch(Exception e){
                    System.out.println("[error] insertGameStats(: "+e.getMessage());
                    System.out.println("query: "+qry);
                    //e.printStackTrace();				
            }
    }
    
    private static NewsItem decodeNewsItem(String line){
                   
        NewsItem item = new NewsItem();

        String dateStr 	= line.split(",")[0].trim().split(" ")[0];
        String timeStr	= line.split(",")[0].trim().split(" ")[1];
        String currency	= line.split(",")[1].trim();
        String impact	= line.split(",")[2].trim();
        String descr	= line.split(",")[3].trim();

        Integer day 	= Integer.valueOf(dateStr.substring(0,2));
        Integer month	= Integer.valueOf(dateStr.substring(3,5));
        Integer year= Integer.valueOf(dateStr.substring(6,10));
        Integer hour 	= Integer.valueOf(timeStr.substring(0,2));
        Integer min 	= Integer.valueOf(timeStr.substring(3,5));

        Calendar cal = Calendar.getInstance();
        cal.set(year, month-1, day, hour, min);

        item.setCurrency(Currency.valueOf(currency));
        item.setImpact(NewsImpact.valueOf(impact));
        item.setDate(cal);
        item.setDescription(descr);
        
        return item;
    }
    
    public static ArrayList<NewsItem> loadNews(String fileName){
    
        ArrayList<NewsItem> news = new ArrayList<NewsItem>();
        File archivo = null;
        FileReader fr = null;
        BufferedReader br = null;

        try {
            archivo = new File(fileName);
            fr = new FileReader(archivo);
            br = new BufferedReader(fr);
            
            String line;
            int i=0;
            while ((line=br.readLine())!=null){
                if (i>=0){
                    //System.out.println("line: "+line);
                    NewsItem item = decodeNewsItem(line);
                    news.add(item);
                    //System.out.println("item: "+item.toString());
                }
            }
        }catch(Exception e){
            e.printStackTrace();
        }finally{
            try{
                if ( fr!=null){
                    fr.close();
                }
            }catch(Exception e2){
                e2.printStackTrace();
            }
        }

        return news;
    }
  
}
