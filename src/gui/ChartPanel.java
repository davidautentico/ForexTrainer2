/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import Trading.Currency;
import Trading.DataGranularity;
import Trading.DateUtils;
import Trading.HedgeManagement;
import Trading.LineType;
import Trading.NewsImpact;
import Trading.NewsItem;
import Trading.PhilDay;
import Trading.PhilLine;
import Trading.Quote;
import indicators.TMA;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.font.FontRenderContext;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import utils.DataUtils;
import utils.PrintUtils;

/**
 *
 * @author david
 */
public class ChartPanel extends javax.swing.JPanel {
    
    ArrayList<Quote> dataSource = null;
    ArrayList<Quote> dataBars   = null;
    ArrayList<Quote> data15m    = null;
    ArrayList<PhilDay> philDays = null;
    ArrayList<NewsItem> news    = null;
    ArrayList<TMA> tma = null;
    ArrayList<TMA> tma15 = null;
    ArrayList<CandlePoint> candlePoints = null;
    final static BasicStroke stroke = new BasicStroke(1.5f);

    final int PADw = 60;
    final int PADh = 20;
    int maxW = 300;
    int maxH = 350;
    final int beginX = 60;
    final int beginY = 10;
    final int candleW = 10;
    int sliderFactor = 0;
    boolean gameMode = false;
    Quote lastQuote    = null;
    Quote initialQuote = null;
    DataGranularity dataGranularity = DataGranularity.MIN5;
    int actualZoom = 8;
    
    
    /**
     * Creates new form ChartPanel
     */
    public ChartPanel() {
        initComponents();
    }
    
    public void setData(ArrayList<Quote> dataSource,ArrayList<Quote> dataBars,
            ArrayList<PhilDay> philDays,ArrayList<NewsItem> news,ArrayList<TMA> tma,ArrayList<TMA> tma15,
            boolean gameMode,DataGranularity granularity,int actualZoom){
        
        this.dataSource = dataSource;
        this.dataBars = dataBars;
        this.gameMode = gameMode;
        this.philDays = philDays;
        this.tma = tma;
        this.tma15 = tma15;
        this.dataGranularity = granularity;
        this.actualZoom = actualZoom;
        this.news = news;
      
        if (this.candlePoints!=null && this.candlePoints.size()>0){
            this.candlePoints.clear();
        }else{
            this.candlePoints = new ArrayList<CandlePoint>();
        }
    }
    
    private double localPhilLine(ArrayList<PhilLine> lines,LineType lineType){
        
        if (lines==null) return -1;
        for (int j=0;j<lines.size();j++){
            if (lines.get(j).getLineType()==lineType)
                return lines.get(j).getValue();
        }
        return -1;
    }
    
    private  PhilDay findPhilDay(Date d){
        Calendar cFind = Calendar.getInstance();
        cFind.setTime(d);
        int dayF = cFind.get(Calendar.DAY_OF_YEAR);
        int yearF = cFind.get(Calendar.YEAR);
               
        for (int i=0;i<philDays.size();i++){
            PhilDay pd = philDays.get(i);
            Calendar actual = pd.getDay();
            int day  = actual.get(Calendar.DAY_OF_YEAR);
            int year = actual.get(Calendar.YEAR);
            if (day==dayF && year==yearF)
                return pd;
        }
        return null;
    }
    
    public  int findTMAIndex(Date d){
        Calendar cFind = Calendar.getInstance();
        cFind.setTime(d);
        int dayF = cFind.get(Calendar.DAY_OF_YEAR);
        int yearF = cFind.get(Calendar.YEAR);
               
        for (int i=0;i<tma.size();i++){
            TMA tmaObj = tma.get(i);
            Calendar actual = tmaObj.getDate();
            int day  = actual.get(Calendar.DAY_OF_YEAR);
            int year = actual.get(Calendar.YEAR);
            if (day==dayF && year==yearF)
                return i;
        }
        return -1;
    }
    
    public  int findTMAIndex15(Date d){
        Calendar cFind = Calendar.getInstance();
        cFind.setTime(d);
        int dayF = cFind.get(Calendar.DAY_OF_YEAR);
        int yearF = cFind.get(Calendar.YEAR);
               
        for (int i=0;i<tma15.size();i++){
            TMA tmaObj = tma15.get(i);
            Calendar actual = tmaObj.getDate();
            int day  = actual.get(Calendar.DAY_OF_YEAR);
            int year = actual.get(Calendar.YEAR);
            if (day==dayF && year==yearF)
                return i;
        }
        return -1;
    }
    
    private double getMax(ArrayList<Quote> data,int begin,int end) {
            if (end>data.size())
                end = data.size();
            double max = -Integer.MAX_VALUE;
            for(int i = begin; i < end; i++) {
                if (data.get(i).getHigh() > max)
                    max = data.get(i).getHigh();
            }
            return max;
    }
        
    private double getMin(ArrayList<Quote> data,int begin,int end) {
            if (end>data.size())
                end = data.size();
            double min = 99999999;
            for(int i = begin; i < end; i++) {
                if(data.get(i).getLow() < min)
                    min = data.get(i).getLow();
            }
            return min;
    }
       
    public void drawPoint(Graphics2D  g,double pointValue,String label,Color color,double min,double max,
            double X1,double X2,double razon){
        
        if (pointValue<min || pointValue >max) return;
        double pointAdjusted = (pointValue-min)*razon;
        double yValue = beginY+maxH - pointAdjusted ;
        g.setPaint(color);
        g.draw(new Line2D.Double(X1 , yValue, X2, yValue));//DO
        float sy = (float) yValue; 
        float sx = beginX+maxW+2;
        g.setPaint(Color.MAGENTA);
        Font font = new Font("Arial", Font.PLAIN, 14);
        g.setFont(font);
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.drawString(label+" "+PrintUtils.Print(pointValue), sx, sy);
    }
    
      /**
     * Se crea la ultima barra en caso de 1sec source
     * @return 
     */
    private Quote createLastBar(int factor){
        Quote last = new Quote();
        int num = sliderFactor%factor;
        boolean first = true;
        //System.out.println("ultima barra num: "+num);
        for (int i=0;i<=num;i++){
            if ((sliderFactor-num+1)>dataSource.size()-1) return null;
            Quote q = this.dataSource.get(sliderFactor-num+i);
            if (first){
                last.copy(q);
                first = false;
            }
            if (q.getHigh()>last.getHigh())
                last.setHigh(q.getHigh());
            if (q.getLow()<last.getLow())
                last.setLow(q.getLow());
            last.setClose(q.getClose());
            last.setDate(q.getDate());
        }
        
        return last;
    }
    
    private void addCandlePoint(Quote q,double x,double y,double w,double h){
        if (this.candlePoints==null){
            this.candlePoints = new ArrayList<CandlePoint>();            
        }
        CandlePoint cPoint = new CandlePoint();
        cPoint.setQ(q);
        cPoint.setX(x);
        cPoint.setY(y);
        cPoint.setH(h);
        cPoint.setW(w);
        candlePoints.add(cPoint);
    }
            
    public void printCandle(Graphics2D g2,Quote q,double razon,double min,
            double xInc,double pos){

        if (q==null) return ;
        
        double candleWith = 4.0;
        if (this.dataGranularity==DataGranularity.HOUR1)
            candleWith = 20;
        
        g2.setStroke(new BasicStroke((float)1.0));
        double newH = (q.getHigh()-min)*razon;
        double newL = (q.getLow()-min)*razon;
        double newO = (q.getOpen()-min)*razon;
        double newC = (q.getClose()-min)*razon;

        if (q.getClose()>=q.getOpen()){
            g2.setPaint(Color.green.darker());
        }else{
            g2.setPaint(Color.red.darker());
        }
        //lines
        double x1 = beginX + (pos)*xInc;
        double y1 = beginY+maxH - newH;
        double y2 = beginY+maxH - newL;
        double yOpen1 = beginY+maxH-newO;
        double yClose1 =beginY+maxH-newC;
        double xOpen = beginX + (pos)*xInc-2;     
        //***CANDLE***
        Color col = Color.white;
        g2.setPaint(Color.green);
        if (q.getClose()<q.getOpen())
            col = Color.BLACK;
        ///*
        //body
        x1 = xOpen+(candleWith)/2;
        g2.setPaint(Color.BLACK);
        g2.draw(new Line2D.Double(x1, y1, x1, y2));//body
        
        //area
        double size = Math.abs(yOpen1-yClose1);
        double recOpen = yOpen1;
        if (yClose1<yOpen1)
            recOpen = yClose1;
        g2.setPaint(col);
        if (col==Color.black){
            g2.fill(new Rectangle2D.Double(xOpen, recOpen, candleWith, size));
        }else{
            g2.setPaint(Color.black);
            g2.fill(new Rectangle2D.Double(xOpen, recOpen, candleWith, size));
            g2.setPaint(Color.white);
            g2.fill(new Rectangle2D.Double(xOpen+1, recOpen+1, candleWith-2, size-2));  
        }
        
        addCandlePoint(q,xOpen,recOpen,candleWith,size);
    }
    
    private void printTMA(Graphics2D g2,int indexTMA,int indexChart,double razon,double min,
            double xInc,double pos,boolean mode15){
        
        ArrayList<TMA> tmaPrint = tma;
        int index = indexTMA+indexChart;
        Color col = Color.BLUE;
        int mod = 0;
        if (mode15==true){
            tmaPrint = tma15;
            int newIndex = indexChart/3; //por cada 3 barras de 5min hay un 15min
            mod = indexChart%3;
            index = indexTMA+newIndex;
            col = Color.red;
        }
                        
        //System.out.println("PRINTTMA "+index);
        if (index<=0) return;
        TMA lastTMA   = tmaPrint.get(index-1);
        TMA actualTMA = tmaPrint.get(index);
        //eje y
        double yu1 = beginY+maxH-(lastTMA.getUpper()-min)*razon;    
        double yu2 = beginY+maxH-(actualTMA.getUpper()-min)*razon;    
        double yl1 = beginY+maxH-(lastTMA.getLower()-min)*razon;    
        double yl2 = beginY+maxH-(actualTMA.getLower()-min)*razon;        
        //eje x
        double x1 = beginX + (pos-1)*xInc;               
        double x2 = beginX + (pos)*xInc; 
        //caso especial para 15 min para continuar en modo recta
        if (mod!=0){
            yu1=yu2;
            yl1=yl2;
        }
        
        double ym1 = yu1+(yl1-yu1)/2.0;
        double ym2 = yu2+(yl2-yu2)/2.0;
        //upper
        g2.setStroke(new BasicStroke((float)1.6));
        g2.setPaint(col);
        g2.draw(new Line2D.Double(x1, yu1, x2, yu2));
        //middle
        g2.setStroke(new BasicStroke(1));
        g2.setPaint(col);
        g2.draw(new Line2D.Double(x1, ym1, x2, ym2));
        //lower
        g2.setStroke(new BasicStroke((float)1.6));
        g2.setPaint(col);
        g2.draw(new Line2D.Double(x1, yl1, x2, yl2));
        //default
        g2.setStroke(new BasicStroke(2));
    }
    
    private void printNewsItem(Graphics2D g2,NewsItem item,double x){
        if (item.getImpact()==NewsImpact.HIGH)
         g2.setPaint(Color.RED);
        if (item.getImpact()==NewsImpact.MEDIUM)
         g2.setPaint(Color.ORANGE);
        if (item.getImpact()==NewsImpact.LOW)
         g2.setPaint(Color.yellow);
        
         g2.draw(new Line2D.Double(x, beginY, x, beginY+maxH));//separacion dia
    }
    
    private void printNews(Graphics2D g2,double xInc){
        
        Calendar today = Calendar.getInstance();
        today.setTime(dataSource.get(0).getDate());
        Calendar initial = Calendar.getInstance();
        initial.setTime(this.initialQuote.getDate());
        int initialTime = initial.get(Calendar.HOUR_OF_DAY)*60
                +initial.get(Calendar.MINUTE);
        if (news==null) return;
        for (int i=0;i<news.size();i++){
            NewsItem item    = news.get(i);
            Calendar dayItem = news.get(i).getDate();
            if (DateUtils.isSameDay(today,dayItem) && 
                    (item.getCurrency()==Currency.EUR 
                    || item.getCurrency()==Currency.USD)){
                
                int h     = dayItem.get(Calendar.HOUR_OF_DAY);
                int min   = dayItem.get(Calendar.MINUTE);
                int mins  = h*60+min;
                //int pos   = mins/5;
                
                int diff = mins-initialTime;
                int pos = diff/5;
                //int hInitial = this.initialQuote.getDate().
                
                //System.out.println(item.toString()+" "+pos);
                //dibujar
                 double x = beginX+pos*xInc;
                 
                 if (x<(beginX+maxW)){
                     printNewsItem(g2,item,x);
                 }
            }
        }
    }
    
    @Override
     protected void paintComponent(Graphics g) {
        //Preparacion lienzo
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D)g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                            RenderingHints.VALUE_ANTIALIAS_ON);
        int w = getWidth();
        int h = getHeight();
        maxW = w-180;
        maxH = h-50;
        g.drawRect(beginX, beginY, maxW, maxH);
        g2.setStroke( stroke );
        //5min parameters
        int factor = 300;
        int actualPoint = sliderFactor/300;//segundos en 5 min  
        double xInc = 5.0;
        double chartValueBounds = 0.0001*this.actualZoom;
        //1Hour parameters
        if (this.dataGranularity==DataGranularity.HOUR1){
            xInc = 50.0;
            actualPoint = sliderFactor/3600;//segundos en 5 min
            chartValueBounds = 0.0001*this.actualZoom+0.0030;
            factor = 3600;
        }
        int maxPointsY = maxH/beginY;
        int maxPointsX = (int) ((maxW-10)/xInc);                 
        //PUNTOS Y
        for (int i=0;i<maxPointsY ;i++){
            g2.draw(new Line2D.Double(beginX, (beginY+maxH)-beginY*i, beginX-5,(beginY+maxH)-beginY*i));
        }
        //PUNTOS X
        for (int i=0;i<maxPointsX ;i++){
            g2.draw(new Line2D.Double(beginX+i*xInc, beginY+maxH, beginX+i*xInc,beginY+maxH+5));
        }
        //FIN preparacion lienzo  
        //PUNTOS
        double doX1=-1.0;
        double doX2=-1.0;
      
        if (dataBars!=null && dataBars.size()>0){
            int begin = actualPoint;
            int end = dataBars.size();
            if (gameMode){
                begin = 0;
                end   = actualPoint;
                if (end>=maxPointsX){
                    begin = actualPoint-maxPointsX;
                }
            }else{
                end = actualPoint+maxPointsX;
            }
                
            //BUSCAMOS DIA EN philDays
            ArrayList<PhilLine> lines = null;
            if (dataBars!=null && philDays!=null && dataBars.size()>0){
               PhilDay pDay = findPhilDay(dataBars.get(begin).getDate());           
               lines = pDay.getLines();
            }
            //buscamos index para TMA
            int indexTMA = -1;
            int indexTMA15 = -1;
            if (begin<dataBars.size()){
                indexTMA   = findTMAIndex(dataBars.get(begin).getDate());
                indexTMA15 = findTMAIndex15(dataBars.get(begin).getDate());
            }
            //VALORES de las lineas
            double DO  = localPhilLine(lines,LineType.DO);
            double DP  = localPhilLine(lines,LineType.DP);
            double DR1 = localPhilLine(lines,LineType.DR1);
            double DR2 = localPhilLine(lines,LineType.DR2);
            double DR3 = localPhilLine(lines,LineType.DR3);
            double DS1 = localPhilLine(lines,LineType.DS1);
            double DS2 = localPhilLine(lines,LineType.DS2);
            double DS3 = localPhilLine(lines,LineType.DS3);
            double YH = localPhilLine(lines,LineType.YH);
            double YL = localPhilLine(lines,LineType.YL);
            double WP  = localPhilLine(lines,LineType.WP);
            double WR1 = localPhilLine(lines,LineType.WR1);
            double WR2 = localPhilLine(lines,LineType.WR2);
            double WR3 = localPhilLine(lines,LineType.WR3);
            double WS1 = localPhilLine(lines,LineType.WS1);
            double WS2 = localPhilLine(lines,LineType.WS2);
            double WS3 = localPhilLine(lines,LineType.WS3);
            double WH = localPhilLine(lines,LineType.WH);
            double WL = localPhilLine(lines,LineType.WL);
            double MP  = localPhilLine(lines,LineType.MP);
            double MR1 = localPhilLine(lines,LineType.MR1);
            double MR2 = localPhilLine(lines,LineType.MR2);
            double MR3 = localPhilLine(lines,LineType.MR3);
            double MS1 = localPhilLine(lines,LineType.MS1);
            double MS2 = localPhilLine(lines,LineType.MS2);
            double MS3 = localPhilLine(lines,LineType.MS3);
            double FIBR1 = localPhilLine(lines,LineType.FIBR1);
            double FIBS1 = localPhilLine(lines,LineType.FIBS1);
            double FIBR2 = localPhilLine(lines,LineType.FIBR2);
            double FIBS2 = localPhilLine(lines,LineType.FIBS2);
            double FIBR3 = localPhilLine(lines,LineType.FIBR3);
            double FIBS3 = localPhilLine(lines,LineType.FIBS3);
            double FIBR4 = localPhilLine(lines,LineType.FIBR4);
            double FIBS4 = localPhilLine(lines,LineType.FIBS4);
            double FIBR5 = localPhilLine(lines,LineType.FIBR5);
            double FIBS5 = localPhilLine(lines,LineType.FIBS5);

            int maxAllowed = maxH;
            double max = getMax(dataBars,begin,end);
            double min = getMin(dataBars,begin,end);
            Quote lastBar = null;
            max += chartValueBounds;
            min -= chartValueBounds;
            
            lastBar = createLastBar(factor);
            if (lastBar!=null){
                if (lastBar.getHigh()>max)
                    max = lastBar.getHigh();
                if (lastBar.getLow()<min)
                    min = lastBar.getLow();
            }
            
            double spread =(max-min);
            double razon =(double)maxAllowed/spread;
           
            float sy = 0;
            float sw = 0;
            float sx = 0;
            
            //Maximo y minimo
            Font font = g2.getFont();
            g2.setFont(font.deriveFont(12.0f));
            FontRenderContext frc = g2.getFontRenderContext();
            Calendar cal = Calendar.getInstance();
            Calendar cal3 = Calendar.getInstance();
            
            String s = String.valueOf(PrintUtils.Print(max));
            sy = (float) beginY +4; 
            sw = (float)font.getStringBounds(s, frc).getWidth();
            sx = (beginX - sw - 0)/2;            
            g2.drawString(s, sx, sy);
            //Ponemos minimo
            s = String.valueOf(PrintUtils.Print(min));
            sy = (float) beginY+maxH; 
            sw = (float)font.getStringBounds(s, frc).getWidth();
            sx = (beginX - sw - 0)/2;
            g2.drawString(s, sx, sy);
            //Ponemos mitad
            double middle = min+(max-min)/2;
            s = String.valueOf(PrintUtils.Print(middle));
            sy = (float) (beginY+maxH)/2 ; 
            sw = (float)font.getStringBounds(s, frc).getWidth();
            sx = (beginX - sw)/2;           
            g2.drawString(s, sx, sy);
          
            int j=0;
                        
            //Si no hay barras completas de 5m pinto la ultima y salgo
            if ((end-1)<0 || (end-1)>=dataBars.size()){
                //formo la ultima barra con las de 1 min que nos sobran                
                if (DataUtils.getGranularity(dataSource)!=DataGranularity.MIN5){
                    lastBar = createLastBar(factor);
                    printCandle(g2,lastBar,razon,min,xInc,j);    
                    //System.out.println("IMPRESO LA PRIMERA");
                    //***TMA***
                    if (dataGranularity!=DataGranularity.HOUR1){
                        printTMA(g2,indexTMA15,end-1,razon,min,xInc,j,true);
                        printTMA(g2,indexTMA,end-1,razon,min,xInc,j,false);
                    }
                    g2.setStroke( stroke );
                }
                return;
            }else{
                this.lastQuote    = dataBars.get(end-1);
                this.initialQuote = dataBars.get(begin);
                for (int i = begin; i < end; i++) {
                    cal.setTime(dataBars.get(i).getDate());
                    cal3.setTime(dataBars.get(i).getDate());                     

                    double newO = (dataBars.get(i).getOpen()-min)*razon;               
                    //lines
                    double x1 = beginX + (j)*xInc;               
                    double yOpen1 = beginY+maxH-newO;              
                    //***CANDLE***
                    printCandle(g2,dataBars.get(i),razon,min,xInc,j);
                    //***TMA***
                    if (dataGranularity!=DataGranularity.HOUR1){
                        printTMA(g2,indexTMA15,i,razon,min,xInc,j,true);
                        printTMA(g2,indexTMA,i,razon,min,xInc,j,false);
                    }
                    g2.setStroke( stroke );
                    //DIBUJAR LINEA  VERTICAL OPEN DAY?
                    if (i==begin){
                        g2.setPaint(Color.GRAY);
                        g2.draw(new Line2D.Double(x1, beginY, x1, beginY+maxH));//separacion dia
                    }
                    j++;
                }//end for                           
                //formo la ultima barra con las de 1 min que nos sobran
                if (DataUtils.getGranularity(dataSource)!=DataGranularity.MIN5){
                    printCandle(g2,lastBar,razon,min,xInc,j);
                    if (dataGranularity!=DataGranularity.HOUR1){
                        printTMA(g2,indexTMA15,end-1,razon,min,xInc,j,true);
                        printTMA(g2,indexTMA,end-1,razon,min,xInc,j,false);
                    }
                    g2.setStroke( stroke );
                }
            }
          
            this.lastQuote = lastBar ;

            //FECHAS EN X
            if (begin<dataBars.size() && begin>=0){
                Date date0 = dataBars.get(begin).getDate();
                cal3.setTime(date0);
                //cal3.add(Calendar.HOUR_OF_DAY, gmtOffset);
                s = DateUtils.datePrint(cal3.getTime());
                //String s = DateUtils.datePrint(date0);
                sy = (float) beginY +maxH+20; 
                sw = (float)font.getStringBounds(s, frc).getWidth();
                sx = beginX;
                g2.setPaint(Color.BLACK);
                g2.drawString(s, sx, sy);
            }    
             
            //fecha media
            if (j>60){
                if ((begin+end)/2<dataBars.size()){
                    Date date1 = dataBars.get((begin+end)/2).getDate();
                    cal3.setTime(date1);
                    //cal3.add(Calendar.HOUR_OF_DAY, gmtOffset);
                    s = DateUtils.datePrint(cal3.getTime());
                    //String s = DateUtils.datePrint(date0);
                    sy = (float) beginY +maxH+20; 
                    sx = (float) (beginX+(j*xInc-beginX)/2);
                    //g2.setPaint(Color.white);
                    g2.drawString(s, sx, sy);
                }
            }
            //FECHA FINAL
            if (j>30){
                if (lastBar!=null){
                    Date date2 = lastBar.getDate();
                    cal3.setTime(date2);
                    //cal3.add(Calendar.HOUR_OF_DAY, 3);
                    s = DateUtils.datePrint(cal3.getTime());
                    sy = (float) beginY +maxH+20; 
                    sw = (float) font.getStringBounds(s, frc).getWidth();
                    sx = (float) (beginX + j*xInc);
                    //g2.setPaint(Color.white);
                    g2.drawString(s, sx, sy);
                }
            }
                     
            //IMPORTANT LINES
            doX1 = beginX;
            doX2 = beginX+maxW;
            //System.out.println("DO value : "+PrintUtils.Print(DO));
            drawPoint(g2,DO,"DO",Color.MAGENTA,min,max,doX1,doX2,razon);
            drawPoint(g2,DP,"DP",Color.ORANGE,min,max,doX1,doX2,razon);
            drawPoint(g2,DR1,"DR1",Color.ORANGE,min,max,doX1,doX2,razon);
            drawPoint(g2,DR2,"DR2",Color.ORANGE,min,max,doX1,doX2,razon);
            drawPoint(g2,DR3,"DR3",Color.ORANGE,min,max,doX1,doX2,razon);
            drawPoint(g2,DS1,"DS1",Color.ORANGE,min,max,doX1,doX2,razon);
            drawPoint(g2,DS2,"DS2",Color.ORANGE,min,max,doX1,doX2,razon);
            drawPoint(g2,DS3,"DS3",Color.ORANGE,min,max,doX1,doX2,razon);
            drawPoint(g2,YH,"YH",Color.ORANGE,min,max,doX1,doX2,razon);
            drawPoint(g2,YL,"YL",Color.ORANGE,min,max,doX1,doX2,razon);
            drawPoint(g2,WP,"WP",Color.CYAN,min,max,doX1,doX2,razon);
            drawPoint(g2,WR1,"WR1",Color.CYAN,min,max,doX1,doX2,razon);
            drawPoint(g2,WR2,"WR2",Color.CYAN,min,max,doX1,doX2,razon);
            drawPoint(g2,WR3,"WR3",Color.CYAN,min,max,doX1,doX2,razon);
            drawPoint(g2,WS1,"WS1",Color.CYAN,min,max,doX1,doX2,razon);
            drawPoint(g2,WS2,"WS2",Color.CYAN,min,max,doX1,doX2,razon);
            drawPoint(g2,WS3,"WS3",Color.CYAN,min,max,doX1,doX2,razon);
            drawPoint(g2,WH,"WH",Color.ORANGE,min,max,doX1,doX2,razon);
            drawPoint(g2,WL,"WL",Color.ORANGE,min,max,doX1,doX2,razon);         
            drawPoint(g2,MP,"MP",Color.GREEN,min,max,doX1,doX2,razon);
            drawPoint(g2,MR1,"MR1",Color.GREEN,min,max,doX1,doX2,razon);
            drawPoint(g2,MR2,"MR2",Color.GREEN,min,max,doX1,doX2,razon);
            drawPoint(g2,MR3,"MR3",Color.GREEN,min,max,doX1,doX2,razon);
            drawPoint(g2,MS1,"MS1",Color.GREEN,min,max,doX1,doX2,razon);
            drawPoint(g2,MS2,"MS2",Color.GREEN,min,max,doX1,doX2,razon);
            drawPoint(g2,MS3,"MS3",Color.GREEN,min,max,doX1,doX2,razon);               
            Color colFib = Color.red;
            drawPoint(g2,FIBR1,"FIBR1",colFib,min,max,doX1,doX2,razon);
            drawPoint(g2,FIBR2,"FIBR2",colFib,min,max,doX1,doX2,razon);
            drawPoint(g2,FIBR3,"FIBR3",colFib,min,max,doX1,doX2,razon);
            drawPoint(g2,FIBR4,"FIBR4",colFib,min,max,doX1,doX2,razon);
            drawPoint(g2,FIBR5,"FIBR5",colFib,min,max,doX1,doX2,razon);
            drawPoint(g2,FIBS1,"FIBS1",colFib,min,max,doX1,doX2,razon);
            drawPoint(g2,FIBS2,"FIBS2",colFib,min,max,doX1,doX2,razon);
            drawPoint(g2,FIBS3,"FIBS3",colFib,min,max,doX1,doX2,razon);
            drawPoint(g2,FIBS4,"FIBS4",colFib,min,max,doX1,doX2,razon);
            drawPoint(g2,FIBS5,"FIBS5",colFib,min,max,doX1,doX2,razon);

            //PONEMOS LAST CLOSE
            Calendar lastCal = Calendar.getInstance();
            lastCal.setTime(lastBar.getDate());
            printLastClose(g2,lastBar.getClose(),lastCal.getTime());
            //ponemos diferencias con last tma
            printTMAdiff(g2,lastBar.getClose(),indexTMA,end-1);
            
            //news del dia
             printNews(g2,xInc);
        }
    }
    
    private Color decodeTMAdiffColor(int value){
        
        if (value>=0 && value <=9)
            return Color.MAGENTA.darker();
        if (value>=10)
            return Color.GREEN.darker();
        return Color.BLACK;
    }
    
    private void printTMAdiff(Graphics2D g,double value,int indexTMA,int indexChart){
            
            TMA tmaObj = tma.get(indexTMA+indexChart);
            
            int upperDiff = HedgeManagement.getPipsDiff(value,tmaObj.getUpper());
            int lowerDiff  = HedgeManagement.getPipsDiff(tmaObj.getLower(),value);
            
            Color upperColor = decodeTMAdiffColor(upperDiff);
            Color lowerColor  = decodeTMAdiffColor(lowerDiff);
            
            String s = String.valueOf(upperDiff);
            float sy = beginY+5;
            float sx=  beginX+280;
            g.setPaint(upperColor);
            Font biggerFont = new Font("Arial", Font.BOLD, 18); 
            g.setFont(biggerFont);
            g.drawString(s, sx, sy);

            s = String.valueOf(lowerDiff);
            sy = (float) beginY + 5;                
            sx = beginX+310;
            g.setPaint(lowerColor);               
            g.setFont(biggerFont);
            g.drawString(s, sx, sy);
    }
    
    private void printLastClose(Graphics2D g,double value,Date date){
            String s = String.valueOf(PrintUtils.Print(value));
            float sy = beginY+5;
            float sx=  beginX;
            //g2.setPaint(Color.WHITE);
            Font biggerFont = new Font("Arial", Font.BOLD, 18); 
            g.setFont(biggerFont);
            g.drawString(s, sx, sy);
            
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            String dayWeek = cal.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.SHORT, Locale.ENGLISH);
            s = dayWeek +" "+DateUtils.datePrint(date);
            sy = (float) beginY + 5;                
            sx = beginX+60;
            //g2.setPaint(Color.white);               
            g.setFont(biggerFont);
            g.drawString(s, sx, sy);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setBackground(new java.awt.Color(255, 255, 255));
        addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseMoved(java.awt.event.MouseEvent evt) {
                formMouseMoved(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents

    private void formMouseMoved(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_formMouseMoved
        // TODO add your handling code here:
        //System.out.println("candleP: "+candlePoints.size());
        this.setToolTipText("");
        if (candlePoints==null || candlePoints.size()<=0) return;
        Point p = evt.getPoint();
       // System.out.println("Punto: "+p.x+" "+p.y);
        
        for (int i=0;i<candlePoints.size();i++){
            CandlePoint cp = candlePoints.get(i);
            if (cp.match(p)){
                //System.out.println("MATCH!: "+PrintUtils.Print(cp.getQ()));
                //this.setToolTipText(PrintUtils.Print(cp.getQ()));
                this.setToolTipText("<html>DATE: "+DateUtils.datePrint(cp.getQ().getDate())
                        + "<br>OPEN: "+PrintUtils.Print(cp.getQ().getOpen())
                        + "<br>HIGH: "+PrintUtils.Print(cp.getQ().getHigh())
                        + "<br>LOW: "+PrintUtils.Print(cp.getQ().getLow())
                        + "<br>CLOSE: "+PrintUtils.Print(cp.getQ().getClose())
                        + "</html>");
            }
        }
        /* JOptionPane.showMessageDialog(this,
                        "Prueba: "+p.x+" "+p.y,
                        "Prueba",
                        JOptionPane.ERROR_MESSAGE);
        */
    }//GEN-LAST:event_formMouseMoved

    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables

   public void setSliderFactor(int value) {        
        this.sliderFactor = value;
    }

    public Quote getLastQuote() {
        return lastQuote;
    }

    public void setLastQuote(Quote lastQuote) {
        this.lastQuote = lastQuote;
    }
    
}
