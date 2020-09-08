package Trading;

import Trading.Quote;
import java.util.ArrayList;
import java.util.Calendar;
import utils.PrintUtils;



public class ConvertLib {
	
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
	 
	 public static ArrayList<Quote> createDailyData(ArrayList<Quote> data){
	        	        
	        ArrayList<Quote> days = new ArrayList<Quote>();
	        int lastDay=-1;
	        Quote lastDayQ=new Quote();
	        Quote actualDayQ= new Quote();
	        Calendar qCal = Calendar.getInstance();
	        for (int i=0;i<data.size();i++){
	            Quote q0 = data.get(i);
	            Quote q = new Quote();
	            q.copy(q0);
	            qCal.setTime(q0.getDate());
	            int h = qCal.get(Calendar.HOUR_OF_DAY);
	            int actualDay = qCal.get(Calendar.DAY_OF_YEAR);
	            int dayWeek = qCal.get(Calendar.DAY_OF_WEEK);
	            q.setDate(qCal.getTime());
	            
	            //System.out.println("actualDay lastDay h: "+actualDay+" "+lastDay+" "+h+" "+dayWeek);
	            //no contamos los sabados ni domingos
	            if (dayWeek==Calendar.SATURDAY || dayWeek==Calendar.SUNDAY){
	            	continue;
	            }
	            
	            if (actualDay!=lastDay){
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
	        //LAST
	        Quote qNew = new Quote();
            qNew.copy(actualDayQ);
            days.add(qNew);
            
	        return days;
	    }
	    
	    public static ArrayList<Quote> createWeeklyData(ArrayList<Quote> data) {
			// TODO Auto-generated method stub
			ArrayList<Quote> weeks = new ArrayList<Quote>(); 
			int lastWeek=-1;
			Quote lastWeekQ=new Quote();
			Quote actualWeekQ= new Quote();
			//System.out.println("tama�o data en calculate: "+data.size());
			Calendar qCal = Calendar.getInstance();
			for (int i=0;i<data.size();i++){			
				Quote q = data.get(i);
	            qCal.setTime(q.getDate());
				int h = qCal.get(Calendar.HOUR_OF_DAY);
				int actualWeek = qCal.get(Calendar.WEEK_OF_YEAR);
				int dayWeek = qCal.get(Calendar.DAY_OF_WEEK);
				//no contamos los sabados ni domingos
				if (dayWeek==Calendar.SATURDAY || dayWeek==Calendar.SUNDAY){
					continue;
				}
				
				if (actualWeek!=lastWeek){
					if (lastWeek!=-1){
						Quote qNew = new Quote();
						qNew.copy(actualWeekQ);
						weeks.add(qNew);
						//System.out.println("a�adiendo dailyData: "+DateUtils.datePrint(qNew.getDate()));
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
	    
	    public static ArrayList<Quote> createWeeklyData(ArrayList<Quote> data, ArrayList<Calendar> gmtAdjusted) {
			// TODO Auto-generated method stub
			ArrayList<Quote> weeks = new ArrayList<Quote>(); 
			int lastWeek=-1;
			Quote lastWeekQ=new Quote();
			Quote actualWeekQ= new Quote();
			//System.out.println("tama�o data en calculate: "+data.size());
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
				
				if (actualWeek!=lastWeek){
					if (lastWeek!=-1){
						Quote qNew = new Quote();
						qNew.copy(actualWeekQ);
						weeks.add(qNew);
						//System.out.println("a�adiendo dailyData: "+DateUtils.datePrint(qNew.getDate()));
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
	    
	    public  static ArrayList<Quote> createMonthlyData(ArrayList<Quote> data) {
			// TODO Auto-generated method stub
			ArrayList<Quote> months = new ArrayList<Quote>(); 
			int lastMonth=-1;
			Quote lastMonthQ=new Quote();
			Quote actualMonthQ= new Quote();
			Calendar qCal = Calendar.getInstance();
			//System.out.println("tama�o data en calculate: "+data.size());
			for (int i=0;i<data.size();i++){			
				Quote q = data.get(i);
				qCal.setTime(q.getDate());
				int h = qCal.get(Calendar.HOUR_OF_DAY);
				int actualMonth = qCal.get(Calendar.MONTH);
				int dayMonth = qCal.get(Calendar.DAY_OF_MONTH);
				//no contamos los sabados ni domingos
				if (dayMonth==Calendar.SATURDAY || dayMonth==Calendar.SUNDAY){
					continue;
				}
				
				if (actualMonth!=lastMonth){
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
			Quote qNew = new Quote();
            qNew.copy(actualMonthQ);
            months.add(qNew);
            
			return months;
		}
	    
	    public  static ArrayList<Quote> createMonthlyData(ArrayList<Quote> data, ArrayList<Calendar> gmtAdjusted) {
			// TODO Auto-generated method stub
			ArrayList<Quote> months = new ArrayList<Quote>(); 
			int lastMonth=-1;
			Quote lastMonthQ=new Quote();
			Quote actualMonthQ= new Quote();
			//System.out.println("tama�o data en calculate: "+data.size());
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
				
				if (actualMonth!=lastMonth){
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
			 Quote qNew = new Quote();
             qNew.copy(actualMonthQ);
             months.add(qNew);
             
			return months;
        }
            
        public static ArrayList<Quote> convert(ArrayList<Quote> data,int factor){
            ArrayList<Quote> result = new ArrayList<Quote>();
            
            int j=0;
            Quote qNew = null;
            for (int i=0;i<data.size();i++){
                Quote q = data.get(i);
                //System.out.println("original: "+DateUtils.datePrint(q.getDate())+" "+PrintUtils.Print(q));
                if (i%(factor)==0){//insertamos
                    if (qNew!=null){//añadimos una nueva
                        result.add(qNew);
                        //System.out.println("QUOTE ADDED: "+DateUtils.datePrint(qNew.getDate())+" "+PrintUtils.Print(qNew));
                        qNew = null;
                    }
                    qNew = new Quote();
                    qNew.copy(q);
                    //System.out.println("NEW QUOTE: "+DateUtils.datePrint(qNew.getDate())+" "+PrintUtils.Print(qNew));
                }else{
                    if (q.getHigh()>qNew.getHigh())
                        qNew.setHigh(q.getHigh());
                    if (q.getLow()<qNew.getLow())
                        qNew.setLow(q.getLow());
                    qNew.setClose(q.getClose());
                } 
            }
            if ((data.size()-1)%factor!=0){//insertamos
                if (qNew!=null){//añadimos una nueva
                        result.add(qNew);
                        //System.out.println("QUote added: "+DateUtils.datePrint(qNew.getDate())+" "+PrintUtils.Print(qNew));
                        qNew = null;
                }
            }
            return result;
        }
	        
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
