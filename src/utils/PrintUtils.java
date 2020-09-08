package utils;

import Trading.Quote;
import java.text.DecimalFormat;
import java.util.Formatter;



public class PrintUtils {

	
	public static String getOHLC(Quote q){
		String str=PrintUtils.Print(q.getOpen())+
				" "+PrintUtils.Print(q.getHigh())+
				" "+PrintUtils.Print(q.getLow())+
				" "+PrintUtils.Print(q.getClose());
		
		
		return str;
	}
	
        public static String Print(double d) {
		// TODO Auto-generated method stub		
           // DecimalFormat df = new DecimalFormat("#.####");                 
	   //return df.format(d).replace(',', '.');
            Formatter df= new Formatter();
           
            return df.format("%.4f",d).toString().replace(',', '.');
	}
	
        public static String PrintPer(double d) {
            DecimalFormat df = new DecimalFormat("000.00");
            return df.format(d).replace(',', '.');
        }
	
	public static String Print2(double d) {
		// TODO Auto-generated method stub
		DecimalFormat df = new DecimalFormat("0.00");
		 return df.format(d).replace(',', '.');
	}
        
        public static String Print4(double d) {
		// TODO Auto-generated method stub
		DecimalFormat df = new DecimalFormat("0.0000");
		 return df.format(d).replace(',', '.');
	}

	 public static String Print(Quote q) {//OHLC
		// TODO Auto-generated method stub
		String str = Print(q.getOpen())+"/"+Print(q.getHigh())
					+"/"+Print(q.getLow())+"/"+Print(q.getClose());
	   return str;
	}
	
	
	
}
