/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package  Trading;

import Trading.Quote;
import java.util.ArrayList;
import java.util.Calendar;

/**
 *
 * @author david
 */
public class StrategyLib {
    
    public static Quote getHighLowQuote(ArrayList<Quote> data,int begin,int end, boolean usingClose){
		Quote q = new Quote();
		
		double max = -999;
		double min = 999;
		if (begin<0) begin =0;
		for (int i=begin;i<=end;i++){
			Quote q0 = data.get(i);
			if (!usingClose){
				if (q0.getHigh()>max) max= q0.getHigh();
				if (q0.getLow()<min) min = q0.getLow();
			}else{
				if (q0.getClose()>max) max= q0.getClose();
				if (q0.getClose()<min) min = q0.getClose();
			}
		}
		
		q.setHigh(max);
		q.setLow(min);
		
		return q;
	}
	
    
    public static ArrayList<Quote> calculateHighLow(ArrayList<Quote> data,
			int n,boolean usingClose) {
		// TODO Auto-generated method stub
		
        ArrayList<Quote> highLow = new ArrayList<Quote>();
        for (int i=0;i<data.size();i++){
            Quote q = getHighLowQuote(data,i-n+1,i,usingClose);
                highLow.add(q);
            }
		
            return highLow;
    }
    
    public static ArrayList<Quote> createDailyData(ArrayList<Quote> data,ArrayList<Calendar> gmtAdjusted){
        
        System.out.println("data size: "+data.size());
        ArrayList<Quote> days = new ArrayList<Quote>();
        int lastDay=-1;
        Quote lastDayQ=new Quote();
	Quote actualDayQ= new Quote();
        for (int i=0;i<data.size();i++){
            Quote q0 = data.get(i);
            Quote q = new Quote();
            q.copy(q0);
            Calendar qCal = gmtAdjusted.get(i);
            int h = qCal.get(Calendar.HOUR_OF_DAY);
            int actualDay = qCal.get(Calendar.DAY_OF_YEAR);
            int dayWeek = qCal.get(Calendar.DAY_OF_WEEK);
            q.setDate(qCal.getTime());
            
            //System.out.println("actualDay lastDay h: "+actualDay+" "+lastDay+" "+h+" "+dayWeek);
            //no contamos los sabados ni domingos
            if (dayWeek==Calendar.SATURDAY || dayWeek==Calendar.SUNDAY){
		continue;
            }
            
            if (actualDay!=lastDay && h==0){
		if (lastDay!=-1){
                    Quote qNew = new Quote();
                    qNew.copy(actualDayQ);
                    days.add(qNew);
		//System.out.println("anyadiendo dailyData: "+DateUtils.datePrint(qNew.getDate())+" "+PrintUtils.getOHLC(qNew));
		}
		lastDayQ.copy(actualDayQ);
		actualDayQ.copy(q);
		lastDay=actualDay;
            }
			
            //actualiamos actualDayQ
            actualDayQ.setClose(q.getClose());
            if (q.getHigh()>actualDayQ.getHigh())
		actualDayQ.setHigh(q.getHigh());
            if (q.getLow()<actualDayQ.getLow())
		actualDayQ.setLow(q.getLow());
        }
        
        return days;
    }
    
    public static ArrayList<Quote> createWeeklyData(ArrayList<Quote> data, ArrayList<Calendar> gmtAdjusted) {
		// TODO Auto-generated method stub
		ArrayList<Quote> weeks = new ArrayList<Quote>(); 
		int lastWeek=-1;
		Quote lastWeekQ=new Quote();
		Quote actualWeekQ= new Quote();
		//System.out.println("tamaï¿½o data en calculate: "+data.size());
		for (int i=0;i<data.size();i++){			
			Quote q = data.get(i);
                        Calendar qCal = gmtAdjusted.get(i);
			int h = qCal.get(Calendar.HOUR_OF_DAY);
			int actualWeek = qCal.get(Calendar.WEEK_OF_YEAR);
			int dayWeek = qCal.get(Calendar.DAY_OF_WEEK);
			//no contamos los sabados ni domingos
			if (dayWeek==Calendar.SATURDAY || dayWeek==Calendar.SUNDAY){
				continue;
			}
			
			if (actualWeek!=lastWeek && h==0){
				if (lastWeek!=-1){
					Quote qNew = new Quote();
					qNew.copy(actualWeekQ);
					weeks.add(qNew);
					//System.out.println("aï¿½adiendo dailyData: "+DateUtils.datePrint(qNew.getDate()));
				}
				lastWeekQ.copy(actualWeekQ);
				actualWeekQ.copy(q);
				lastWeek=actualWeek;
			}
			
			//actualiamos actualDayQ
			actualWeekQ.setClose(q.getClose());
			if (q.getHigh()>actualWeekQ.getHigh())
				actualWeekQ.setHigh(q.getHigh());
			if (q.getLow()<actualWeekQ.getLow())
				actualWeekQ.setLow(q.getLow());			
		}
		
		Quote qNew = new Quote();
		qNew.copy(actualWeekQ);
		weeks.add(qNew);
		
		return weeks;
	}
    
        public  static ArrayList<Quote> createMonthlyData(ArrayList<Quote> data, ArrayList<Calendar> gmtAdjusted) {
		// TODO Auto-generated method stub
		ArrayList<Quote> months = new ArrayList<Quote>(); 
		int lastMonth=-1;
		Quote lastMonthQ=new Quote();
		Quote actualMonthQ= new Quote();
		//System.out.println("tamaï¿½o data en calculate: "+data.size());
		for (int i=0;i<data.size();i++){			
			Quote q = data.get(i);
			Calendar qCal = gmtAdjusted.get(i);
			int h = qCal.get(Calendar.HOUR_OF_DAY);
			int actualMonth = qCal.get(Calendar.MONTH);
			int dayMonth = qCal.get(Calendar.DAY_OF_MONTH);
			//no contamos los sabados ni domingos
			if (dayMonth==Calendar.SATURDAY || dayMonth==Calendar.SUNDAY){
				continue;
			}
			
			if (actualMonth!=lastMonth && h==0){
                            if (lastMonth!=-1){
                                Quote qNew = new Quote();
				qNew.copy(actualMonthQ);
				months.add(qNew);
                            }
                            lastMonthQ.copy(actualMonthQ);
                            actualMonthQ.copy(q);
                            lastMonth=actualMonth;
                        }
			
			//actualiamos actualDayQ
			actualMonthQ.setClose(q.getClose());
			if (q.getHigh()>actualMonthQ.getHigh())
				actualMonthQ.setHigh(q.getHigh());
			if (q.getLow()<actualMonthQ.getLow())
				actualMonthQ.setLow(q.getLow());			
		}
		
		return months;
	}
}
