/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Trading;

import Trading.Quote;
import java.util.ArrayList;
import java.util.Calendar;


/**
 *
 * @author david
 */
public class PhilUtils {
    
    static double getAverageRange(ArrayList<Quote> dailyData,int bar){
	        int i;
	        int longADRPeriod  = 100;
	        int shortADRPeriod = 3;
	        double localHigh   = 0;
	        double localLow    = 0;
	        double highMALong  = 0;
	        double lowMALong   = 0;
	        double highMAShort = 0;
	        double lowMAShort  = 0;
	        i=0;
	        Calendar cal = Calendar.getInstance();
	        int total=0;
	        //System.out.println("[getAverageRange] bar "+bar);
	        while (total<longADRPeriod)
		{
	            if ((bar-i)>=0)
	            {
	                Quote q = dailyData.get(bar-i);
	                cal.setTime(q.getDate());
	                if (cal.get(Calendar.DAY_OF_WEEK)>=Calendar.MONDAY 
	                        && cal.get(Calendar.DAY_OF_WEEK)<=Calendar.FRIDAY){
	                    localLow  = dailyData.get(bar-i).getLow();
	                    localHigh = dailyData.get(bar-i).getHigh();
	                    lowMALong += localLow;
	                    highMALong += localHigh;
	                    if ( i < shortADRPeriod ){
	                        lowMAShort += localLow;
	                        highMAShort += localHigh;
	                    }
	                    total++;
	                   /* System.out.println("lowMAShort highMAShort localLow localHigh:" 
	                            +PrintUtils.Print(lowMAShort)
	                            +" "+PrintUtils.Print(highMAShort)
	                            +" "+PrintUtils.Print(localLow)
	                            +" "+PrintUtils.Print(localHigh)
	                            );
	                      */
	                }
	            }else{
	                break;
	            }
	            i++;
		}
		lowMALong /= longADRPeriod;
		highMALong /= longADRPeriod;
		lowMAShort /= shortADRPeriod;
		highMAShort /= shortADRPeriod;

	        if ((highMALong - lowMALong)<=(highMAShort - lowMAShort)){
	            return (highMALong - lowMALong);
	        }else{
	            return (highMAShort - lowMAShort);
	        }
	    }
    
    public static ArrayList<PhilDay> calculateLines(ArrayList<Quote> data,ArrayList<Quote> dailyData,
				ArrayList<Quote> weeklyData,ArrayList<Quote> monthlyData){
		 
        ArrayList<PhilDay> philDays = new ArrayList<PhilDay>();
		 
        Calendar qCal = Calendar.getInstance();
        int beforeDay = -1;
        //POINTS
        double DO = -1;
	double DP = -1;double DR1=-1;double DR2=-1;double DR3=-1;double DS1=-1;double DS2=-1;double DS3=-1;
	double WP = -1;double WR1=-1;double WR2=-1;double WR3=-1;double WS1=-1;double WS2=-1;double WS3=-1;
        double MP = -1;double MR1=-1;double MR2=-1;double MR3=-1;double MS1=-1;double MS2=-1;double MS3=-1;
        double FIBR1=-1;double FIBS1=-1;double FIBR2=-1;double FIBS2=-1;double FIBR3=-1;double FIBS3=-1;
        double FIBR4=-1;double FIBS4=-1;double FIBR5=-1;double FIBS5=-1;
        double lastHighD = -1;double lastLowD = -1;double lastCloseD = -1;
        double lastHighW = -1;double lastLowW = -1;double lastCloseW = -1;
        double lastHighM = -1;double lastLowM = -1;double lastCloseM = -1;

        int lastDay=-1;
        int lastWeek=-1;
        int lastMonth=-1;
        for (int i=0;i<data.size();i++){
            Quote q = data.get(i);
            qCal.setTime(q.getDate());
            int actualDay   = qCal.get(Calendar.DAY_OF_YEAR);
            int actualMonth = qCal.get(Calendar.MONTH);
            int actualWeek  = qCal.get(Calendar.WEEK_OF_YEAR);
            int dayWeek = qCal.get(Calendar.DAY_OF_WEEK);

            if (dayWeek==Calendar.SATURDAY || dayWeek==Calendar.SUNDAY){
                continue;
            }

            double range = -1;
            if (actualDay!=beforeDay){
                //int lastDay   =  DateUtils.findLastDateIndex(dailyData,q,1);
                lastWeek  =  DateUtils.findLastDateIndex(weeklyData,q,2);
                lastMonth =  DateUtils.findLastDateIndex(monthlyData,q,3);
                //double range = getAverageRange(dailyData,lastDay);
                ArrayList<PhilLine> lines = new ArrayList<PhilLine>(); 
                if (lastDay!=-1){
                    lastHighD = dailyData.get(lastDay).getHigh();
                    lastLowD = dailyData.get(lastDay).getLow();
                    lastCloseD = dailyData.get(lastDay).getClose();
                    DO = q.getOpen();
                    range = getAverageRange(dailyData,lastDay);                                       					
                    beforeDay = actualDay;					
                    /*System.out.println("**NEW Day DO: "+DateUtils.datePrint(q.getDate())
                                    +" "+PrintUtils.Print(DO)
                    );*/
                    DP = ( lastHighD + lastLowD + lastCloseD ) / 3;
                    DR1 = ( 2 * DP ) - lastLowD;
                    DS1 = ( 2 * DP ) - lastHighD;
                    DR2 = DP + ( lastHighD - lastLowD );
                    DS2 = DP - ( lastHighD - lastLowD );
                    DR3 = ( 2 * DP ) + ( lastHighD - ( 2 * lastLowD ) );
                    DS3 = ( 2 * DP ) - ( ( 2 * lastHighD ) - lastLowD );
                    lines.add(PhilLine.createLine(LineType.DO, DO));//0
                    lines.add(PhilLine.createLine(LineType.DP, DP));//1
                    lines.add(PhilLine.createLine(LineType.DR1, DR1));//2
                    lines.add(PhilLine.createLine(LineType.DS1, DS1));//3
                    lines.add(PhilLine.createLine(LineType.DR2, DR2));//4
                    lines.add(PhilLine.createLine(LineType.DS2, DS2));//5
                    lines.add(PhilLine.createLine(LineType.DR3, DR3));//6
                    lines.add(PhilLine.createLine(LineType.DS3, DS3));//7
                }
                if (lastWeek!=-1){
                    lastHighW = weeklyData.get(lastWeek).getHigh();
                    lastLowW = weeklyData.get(lastWeek).getLow();
                    lastCloseW = weeklyData.get(lastWeek).getClose();
                    WP = ( lastHighW + lastLowW + lastCloseW ) / 3;
                    WR1 = ( 2 * WP ) - lastLowW;
                    WS1 = ( 2 * WP ) - lastHighW;
                    WR2 = WP + ( lastHighW - lastLowW );
                    WS2 = WP - ( lastHighW - lastLowW );
                    WR3 = ( 2 * WP ) + ( lastHighW - ( 2 * lastLowW ) );
                    WS3 = ( 2 * WP ) - ( ( 2 * lastHighW ) - lastLowW );

                    lines.add(PhilLine.createLine(LineType.WP, WP));//8
                    lines.add(PhilLine.createLine(LineType.WR1, WR1));//9
                    lines.add(PhilLine.createLine(LineType.WS1, WS1));//10
                    lines.add(PhilLine.createLine(LineType.WR2, WR2));//11
                    lines.add(PhilLine.createLine(LineType.WS2, WS2));//12
                    lines.add(PhilLine.createLine(LineType.WR3, WR3));//13
                    lines.add(PhilLine.createLine(LineType.WS3, WS3));//14
                    //System.out.println("new WP: "+WP);
                }
                if (lastMonth!=-1){
                    lastHighM  = monthlyData.get(lastMonth).getHigh();
                    lastLowM   = monthlyData.get(lastMonth).getLow();
                    lastCloseM = monthlyData.get(lastMonth).getClose();
                    MP = ( lastHighM + lastLowM + lastCloseM ) / 3;
                    MR1 = ( 2 * MP ) - lastLowM;
                    MS1 = ( 2 * MP ) - lastHighM;
                    MR2 = MP + ( lastHighM - lastLowM );
                    MS2 = MP - ( lastHighM - lastLowM );
                    MR3 = ( 2 * MP ) + ( lastHighM - ( 2 * lastLowM ) );
                    MS3 = ( 2 * MP ) - ( ( 2 * lastHighM ) - lastLowM );

                    lines.add(PhilLine.createLine(LineType.MP, MP));//15
                    lines.add(PhilLine.createLine(LineType.MR1, MR1));//16
                    lines.add(PhilLine.createLine(LineType.MS1, MS1));//17
                    lines.add(PhilLine.createLine(LineType.MR2, MR2));//18
                    lines.add(PhilLine.createLine(LineType.MS2, MS2));//19
                    lines.add(PhilLine.createLine(LineType.MR3, MR3));//20
                    lines.add(PhilLine.createLine(LineType.MS3, MS3));//21
                }

                FIBR1 = DO + ( range * 0.382 );
                FIBS1 = DO - ( range * 0.382 );
                FIBR2 = DO + ( range * 0.618 );
                FIBS2 = DO - ( range * 0.618 );
                FIBR3 = DO + ( range * 0.764 );
                FIBS3 = DO - ( range * 0.764 );
                FIBR4 = DO + ( range * 1.000 );
                FIBS4 = DO - ( range * 1.000 );
                FIBR5 = DO + ( range * 1.382 );
                FIBS5 = DO - ( range * 1.382 );
                lines.add(PhilLine.createLine(LineType.FIBR1, FIBR1));//22
                lines.add(PhilLine.createLine(LineType.FIBR2, FIBR2));//23          
                lines.add(PhilLine.createLine(LineType.FIBR3, FIBR3));//24
                lines.add(PhilLine.createLine(LineType.FIBR4, FIBR4));//25
                lines.add(PhilLine.createLine(LineType.FIBR5, FIBR5));//26
                lines.add(PhilLine.createLine(LineType.FIBS1, FIBS1));//27
                lines.add(PhilLine.createLine(LineType.FIBS2, FIBS2));//28          
                lines.add(PhilLine.createLine(LineType.FIBS3, FIBS3));//29
                lines.add(PhilLine.createLine(LineType.FIBS4, FIBS4));//30
                lines.add(PhilLine.createLine(LineType.FIBS5, FIBS5));//31

                /*System.out.println("Testing LINE FIBS1 range "+breachedLine
                                +" "+PrintUtils.Print(breachedLineValue)
                                +" "+PrintUtils.Print(FIBS1)
                                +" range "+PrintUtils.Print(range)
                                );
                */

                //add ney day
                Calendar dayCal = Calendar.getInstance();
                dayCal.setTime(q.getDate());
                PhilDay pDay = new PhilDay();
                pDay.setDay(dayCal);
                pDay.setIndex(i);
                pDay.setLines(lines);
                philDays.add(pDay);

                beforeDay=actualDay;
                lastDay++;
            }
        }
        return philDays;
    }
    
}
