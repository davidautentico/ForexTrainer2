/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Trading;

import Trading.Quote;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Formatter;
import java.util.GregorianCalendar;
import java.util.Locale;

/**
 *
 * @author david
 */
public class DateUtils {
    	public static final String DATE_FORMAT_NOW = "yyyy-MM-dd HH:mm:ss";
	public static final String FORMAT_YYYYMMDD = "yyyy-MM-dd";
        
        
        public static int calculatePepperGMTOffset(Calendar cal){
        int offset = 2;
        
        int calY = cal.get(Calendar.YEAR);
        int calM = cal.get(Calendar.MONTH);
        int calD = cal.get(Calendar.DAY_OF_MONTH);
        
        if (calY==2009){
            if ((calM>Calendar.MARCH && calM<Calendar.NOVEMBER) 
                    || (calM==Calendar.MARCH && calD>=8)
                    || (calM==Calendar.NOVEMBER && calD<=1)
                    ){
                return 3;
            }
        }
        
        if (calY==2010){
            if ((calM>Calendar.MARCH && calM<Calendar.NOVEMBER) 
                    || (calM==Calendar.MARCH && calD>=14)
                    || (calM==Calendar.NOVEMBER && calD<7)
                    ){
                return 3;
            }
        }
        
        if (calY==2011){
            if ((calM>Calendar.MARCH && calM<Calendar.NOVEMBER) 
                    || (calM==Calendar.MARCH && calD>=13)
                    || (calM==Calendar.NOVEMBER && calD<6)
                    ){
                return 3;
            }
        }
        
        if (calY==2012){
            if ((calM>Calendar.MARCH && calM<Calendar.NOVEMBER) 
                    || (calM==Calendar.MARCH && calD>=11)
                    || (calM==Calendar.NOVEMBER && calD<4)
                    ){
                return 3;
            }
        }
        
        if (calY==2013){
            if (calM>Calendar.MARCH || (calM==Calendar.MARCH && calD>=10)){
                return 3;
            }
        }
        
        return offset;
    }
	
	public static boolean isDateEqual(GregorianCalendar gc1,GregorianCalendar gc2){
		
		if (gc1.get(Calendar.YEAR)==gc2.get(Calendar.YEAR)
				&& gc1.get(Calendar.MONTH)==gc2.get(Calendar.MONTH)
				&& gc1.get(Calendar.DAY_OF_MONTH)==gc2.get(Calendar.DAY_OF_MONTH))
			return true;
		
		
		return false;
	}
	
        public static boolean isDateEqual2(GregorianCalendar gc1,GregorianCalendar gc2){
		
		if (gc1.get(Calendar.YEAR)==gc2.get(Calendar.YEAR)
				&& gc1.get(Calendar.MONTH)==gc2.get(Calendar.MONTH)
				&& gc1.get(Calendar.DAY_OF_MONTH)==gc2.get(Calendar.DAY_OF_MONTH)
				&& gc1.get(Calendar.HOUR_OF_DAY)==gc2.get(Calendar.HOUR_OF_DAY)
				&& gc1.get(Calendar.MINUTE)==gc2.get(Calendar.MINUTE)
				)
			return true;
		
		
		return false;
	}
        
        public static Date getDukasDate(String dateStr,String timeStr) {
		// TODO Auto-generated method stub
            //System.out.println(dateStr+" "+timeStr);
            Integer hh = Integer.valueOf(timeStr.substring(0,2));
            Integer mm = Integer.valueOf(timeStr.substring(3,5));
            Integer ss = Integer.valueOf(timeStr.substring(6,8));
                
            Date date = getDate(dateStr);
            date.setHours(hh);
            date.setMinutes(mm);
            date.setSeconds(ss);
            //System.out.println("date: "+DateUtils.datePrint(date)+" "+hh+" "+mm+" "+ss);
  		
            return date;
	}
	
	public static final String calendarToString(Calendar cal, String dateformat) {
	    StringBuffer ret = new StringBuffer();
	    if(dateformat.equals(FORMAT_YYYYMMDD) ) {
	      ret.append(cal.get(Calendar.YEAR));
	      ret.append("-");
	      
	      String month = null;
	      int mo = cal.get(Calendar.MONTH) + 1; /* Calendar month is zero indexed, string months are not */
	      if(mo < 10) {
	        month = "0" + mo;
	      }
	      else {
	        month = "" + mo;
	      }
	      ret.append(month);      
	      
	      ret.append("-");
	      
	      String date = null;
	      int dt = cal.get(Calendar.DATE);
	      if(dt < 10) {
	        date = "0" + dt;
	      }
	      else {
	        date = "" + dt;
	      }
	      ret.append(date);
	    }
	    
	    return ret.toString();
	  }
        
        public static String getAlways2digits(int number){
		int res = number+100;
		
		String resStr = String.valueOf(res).substring(1);
		
		return resStr;
	}
	
	public static String datePrint(Calendar cal){
		
		String dateStr = String.valueOf(cal.get(Calendar.YEAR))
					+'.'+getAlways2digits(cal.get(Calendar.MONTH)+1)
					+'.'+getAlways2digits(cal.get(Calendar.DAY_OF_MONTH))
					+" "+getAlways2digits(cal.get(Calendar.HOUR_OF_DAY))
					+":"+getAlways2digits(cal.get(Calendar.MINUTE))
					+":"+getAlways2digits(cal.get(Calendar.SECOND))
					;
		
		return dateStr;
	}
	
	
	public static String datePrint(Date dt){
		GregorianCalendar cal = new GregorianCalendar(); 
		cal.setTime(dt);
		return datePrint(cal);
	}
	
	public static String datePrint3(Date dt){
		GregorianCalendar cal = new GregorianCalendar(); 
		cal.setTime(dt);
		String str=String.valueOf(cal.get(Calendar.DAY_OF_MONTH))+"-"+String.valueOf(cal.get(Calendar.MONTH)+1);
		return str;
	}
	
		
	public static String yearPrint(Date dt){
		Calendar cal = DateUtils.getCalendar(dt);
		return String.valueOf(cal.get(Calendar.YEAR));
	}
	
	public static String monthPrint(Date dt){
		Calendar cal = DateUtils.getCalendar(dt);
		return String.valueOf((cal.get(Calendar.MONTH)+1));
	}
	
	public static GregorianCalendar getTodayDate()
	{
		GregorianCalendar cal = (GregorianCalendar) GregorianCalendar.getInstance();
		try{			
			Date dt = cal.getTime();
			SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
			sdf.format(dt);
			cal.setTime(dt);		
		}catch(Exception e){
			System.out.println("[error] getTodayDate: "+e.getMessage());
			return null;
		}		
		return cal;
	}
	
	public static Date stringToDate(String dateStr)
	{
		//System.out.println("[ stringToDateFormat] datestr: "+dateStr);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");	
		Date d = null;	
		try{
			d = sdf.parse(dateStr);
		}catch (Exception e){
			System.out.println("[Error] stringToDateFormat: "+e.getMessage());
		}		
		return d;
	}
	
	public static GregorianCalendar stringToDateFormat(String dateStr) 
	{
		//System.out.println("[ stringToDateFormat] datestr: "+dateStr);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");	
		Date d = null;	
		try{
			d = sdf.parse(dateStr);
		}catch (Exception e){
			System.out.println("[Error] stringToDateFormat: "+e.getMessage());
		}
		//System.out.println("[ stringToDateFormat] date before gregorian: "+d);
		GregorianCalendar cal = (GregorianCalendar)GregorianCalendar.getInstance();
		cal.setTime(d);
		return cal;
	}
	
	public static GregorianCalendar stringToDateFormat2(String dateStr) 
	{
		//System.out.println("[ stringToDateFormat] datestr: "+dateStr);
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");	
		Date d = null;	
		try{
			d = sdf.parse(dateStr);
		}catch (Exception e){
			System.out.println("[Error] stringToDateFormat: "+e.getMessage());
		}
		//System.out.println("[ stringToDateFormat] date before gregorian: "+d);
		GregorianCalendar cal = (GregorianCalendar)GregorianCalendar.getInstance();
		cal.setTime(d);
		return cal;
	}
	
	 public static String now() {
		    Calendar cal = Calendar.getInstance();
		    SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT_NOW);
		    return sdf.format(cal.getTime());

		  }
	
	public static GregorianCalendar getCalendar(String timestamp)
    throws Exception {
   /*
   ** we specify Locale.US since months are in english
   */
   SimpleDateFormat sdf = new SimpleDateFormat
     ("dd-MM-yyyy", Locale.US);
   Date d = sdf.parse(timestamp);
   GregorianCalendar cal = (GregorianCalendar)GregorianCalendar.getInstance();
   cal.setTime(d);
   return cal;
 }
	
	public static Calendar getCalendar(Date d){
		Calendar cal=Calendar.getInstance();
		cal.setTime(d);  
		
		return cal;
	}

	public static boolean isTodayDate(Date dateTime) {
		if (dateTime== null) return false;
		// TODO Auto-generated method stub
		//System.out.println("[isTodayDate] date: "+dateTime);
		Calendar cal = Calendar.getInstance();
		Calendar currentcal = Calendar.getInstance();		
		cal.setTime(dateTime);		
		currentcal.set(currentcal.get(Calendar.YEAR),
		currentcal.get(Calendar.MONTH), currentcal.get(Calendar.DAY_OF_MONTH));
		
		
		if(currentcal.get(Calendar.DAY_OF_MONTH)==cal.get(Calendar.DAY_OF_MONTH) 
				&& currentcal.get(Calendar.MONTH)==cal.get(Calendar.MONTH)
				&& currentcal.get(Calendar.YEAR)==cal.get(Calendar.YEAR)){
			
			//System.out.println("[isTodayDate] ES EL MISMO DIA");
			return true;
		}
		else
			return false;				
	}
	
	public static Date getDate(String dateStr){
		Date date = new Date();
		
		//System.out.println(dateStr.split("\\.")[0].trim()
                //        +" "+dateStr.split("\\.")[1].trim()
                //        +" "+dateStr.split("\\.")[2].trim()
                //        );
		if (dateStr.contains(".")){
			Integer y = Integer.valueOf(dateStr.split("\\.")[0].trim());
			Integer m = Integer.valueOf(dateStr.split("\\.")[1].trim());
			Integer d = Integer.valueOf(dateStr.split("\\.")[2].trim());
			
			date.setDate(d);
			date.setMonth(m-1);
			date.setYear(y-1900);
			
			//System.out.println("date : "+datePrint(date));
		}
		return date;		
	}
	
	public static Date getDateTime(String dateStr,String timeStr){
		Date date = new Date();
		
		String datefor = null;
		//fecha
		if (dateStr.contains(".")){
			Integer y = Integer.valueOf(dateStr.split("\\.")[0].trim());
			Integer m = Integer.valueOf(dateStr.split("\\.")[1].trim());
			Integer d = Integer.valueOf(dateStr.split("\\.")[2].trim());
			
			datefor =  String.valueOf(y)+"-"+String.valueOf(m)+"-"+String.valueOf(d);				
		}
		
		//hora		
		DateFormat dfm = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		//dfm.setTimeZone(TimeZone.getTimeZone("Europe/Zurich"));
		try {
			//System.out.println("Antes de parse: "+datefor+" "+timeStr+":00");	
			date = dfm.parse(datefor+" "+timeStr+":00");
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
					
		return date;		
	}

	public static Date getDateTime2(String date, String time) {
		// TODO Auto-generated method stub
		return null;
	}

	public static Date stringToDate2(String dateStr) {
		// TODO Auto-generated method stub
		Date date = new Date();
			
		DateFormat dfm = new SimpleDateFormat("yyyy-MM-dd");
		//dfm.setTimeZone(TimeZone.getTimeZone("Europe/Zurich"));
		try {
			//System.out.println("Antes de parse: "+datefor+" "+timeStr+":00");	
			date = dfm.parse(dateStr);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
		
		return date;
	}

	public static String datePrint2(Date d) {
		// TODO Auto-generated method stub
		return String.valueOf(d.getYear()+1900)+"-"+String.valueOf(d.getMonth()+1)+"-"+
			   String.valueOf(d.getDate());
	}

	public static Date getCSIDate(String dateStr) {
		// TODO Auto-generated method stub
		Date date = new Date();
		
		Integer y = Integer.valueOf(dateStr.substring(0,4));
		Integer m = Integer.valueOf(dateStr.substring(4,6));
		Integer d = Integer.valueOf(dateStr.substring(6,8));
		
		date.setDate(d);
		date.setMonth(m-1);
		date.setYear(y-1900);
		
		
		return date;
	}
      
       public static String getDukasFormat(Calendar cal){
		
		String dateStr = String.valueOf(cal.get(Calendar.YEAR))
					+'.'+getAlways2digits(cal.get(Calendar.MONTH)+1)
					+'.'+getAlways2digits(cal.get(Calendar.DAY_OF_MONTH))
					+" "+getAlways2digits(cal.get(Calendar.HOUR_OF_DAY))
					+":"+getAlways2digits(cal.get(Calendar.MINUTE))
					+":"+getAlways2digits(cal.get(Calendar.SECOND))
					;
		
		return dateStr;
	}
        public static int calculateGMTOffset(Calendar cal){
            int offset = 2;
            
            int calY = cal.get(Calendar.YEAR);
            int calM = cal.get(Calendar.MONTH);
            int calD = cal.get(Calendar.DAY_OF_MONTH);
            
            if (calY==2009){
                if ((calM>Calendar.MARCH && calM<Calendar.NOVEMBER) 
                        || (calM==Calendar.MARCH && calD>=8)
                        || (calM==Calendar.NOVEMBER && calD<=1)
                        ){
                    return 3;
                }
            }
            
            if (calY==2010){
                if ((calM>Calendar.MARCH && calM<Calendar.NOVEMBER) 
                        || (calM==Calendar.MARCH && calD>=14)
                        || (calM==Calendar.NOVEMBER && calD<7)
                        ){
                    return 3;
                }
            }
            
            if (calY==2011){
                if ((calM>Calendar.MARCH && calM<Calendar.NOVEMBER) 
                        || (calM==Calendar.MARCH && calD>=13)
                        || (calM==Calendar.NOVEMBER && calD<6)
                        ){
                    return 3;
                }
            }
            
            if (calY==2012){
                if ((calM>Calendar.MARCH && calM<Calendar.NOVEMBER) 
                        || (calM==Calendar.MARCH && calD>=11)
                        || (calM==Calendar.NOVEMBER && calD<4)
                        ){
                    return 3;
                }
            }
            
            if (calY==2013){
                if (calM>Calendar.MARCH || (calM==Calendar.MARCH && calD>=10)){
                    return 3;
                }
            }
            
            return offset;
        }
        
        
        public static String getDayName(int dayWeek){
            
            if (dayWeek==Calendar.MONDAY) return "MONDAY";
            if (dayWeek==Calendar.TUESDAY) return "TUESDAY";
            if (dayWeek==Calendar.WEDNESDAY) return "WEDNESDAY";
            if (dayWeek==Calendar.THURSDAY) return "THRUSDAY";
            if (dayWeek==Calendar.FRIDAY) return "FRIDAY";
            if (dayWeek==Calendar.SATURDAY) return "SATURDAY";
            if (dayWeek==Calendar.SUNDAY) return "SUNDAY";
            
            return "NONE";
        }
        
        public static int findLastDateIndex(ArrayList<Quote> data,Quote toFind,int type){
            Calendar calOr = Calendar.getInstance();
            calOr.setTime(toFind.getDate());
            Calendar calF = Calendar.getInstance();
            calF.setTime(toFind.getDate());
            if (type==1){
                int day = calF.get(Calendar.DAY_OF_WEEK);  
                if (day==Calendar.SUNDAY) calF.add(Calendar.DAY_OF_YEAR, -2);
                else if (day==Calendar.MONDAY) calF.add(Calendar.DAY_OF_YEAR, -3);
                else calF.add(Calendar.DAY_OF_YEAR, -1);
            }else if (type==2){
                calF.add(Calendar.WEEK_OF_YEAR, -1);
            }else if (type==3){
                calF.add(Calendar.MONTH, -1);
            }
            /*System.out.println("[findLastDateIndex] original - actualizado "
                    +DateUtils.getDayName(calOr.get(Calendar.DAY_OF_WEEK))+" "+calOr.get(Calendar.DAY_OF_YEAR)
                    +"-"+DateUtils.getDayName(calF.get(Calendar.DAY_OF_WEEK))+" "+calF.get(Calendar.DAY_OF_YEAR)
                    );
           */
            Calendar actual = Calendar.getInstance();
            for (int i=0;i<data.size();i++){
                Quote q = data.get(i);
                actual.setTime(q.getDate());
                int year = actual.get(Calendar.YEAR);
                int week = actual.get(Calendar.WEEK_OF_YEAR);
                int month = actual.get(Calendar.MONTH);
                int day = actual.get(Calendar.DAY_OF_YEAR);
                int dayWeek = actual.get(Calendar.DAY_OF_WEEK);
                
               /* System.out.println("[findLastDateIndex] actual "
                    +year
                    +" "+day
                    +" "+DateUtils.getDayName(dayWeek)
                    );
                */
                if (year == calF.get(Calendar.YEAR)){
                    if (type==1 && day == calF.get(Calendar.DAY_OF_YEAR))
                        return i;
                    if (type==2 && week == calF.get(Calendar.WEEK_OF_YEAR))
                        return i;
                    if (type==3 && month == calF.get(Calendar.MONTH))
                        return i;
                }
                
            }
            return -1;
        }
        
        public static Date getPepperDate(String dateStr,String timeStr) {
		// TODO Auto-generated method stub
            //System.out.println(dateStr+" "+timeStr);
            Integer hh = Integer.valueOf(timeStr.substring(0,2));
            Integer mm = Integer.valueOf(timeStr.substring(3,5));
            Integer ss = Integer.valueOf(timeStr.substring(6,8));
                
            Date date = getDate(dateStr);
            date.setHours(hh);
            date.setMinutes(mm);
            date.setSeconds(00);
            //System.out.println("date: "+DateUtils.datePrint(date)+" "+hh+" "+mm+" "+ss);
  		
            return date;
	}
       
        public static boolean isSameDay(Calendar cal1,Calendar cal2){
            
            int y1 = cal1.get(Calendar.YEAR);
            int m1 = cal1.get(Calendar.MONTH);
            int d1 = cal1.get(Calendar.DAY_OF_MONTH);
            int y2 = cal2.get(Calendar.YEAR);
            int m2 = cal2.get(Calendar.MONTH);
            int d2 = cal2.get(Calendar.DAY_OF_MONTH);
		
            if (y1==y2 && m1==m2 && d1==d2 ) return true;
            return false;   
        }
        
        public static String generateFileName(Calendar cal,String ext){
            String fileName = String.valueOf(cal.get(Calendar.YEAR))
					+getAlways2digits(cal.get(Calendar.MONTH)+1)
					+getAlways2digits(cal.get(Calendar.DAY_OF_MONTH))
					+"_"+getAlways2digits(cal.get(Calendar.HOUR_OF_DAY))
					+getAlways2digits(cal.get(Calendar.MINUTE))
					+getAlways2digits(cal.get(Calendar.SECOND))
					+"_"+ext
                                        ;
            return fileName;
        }
}
