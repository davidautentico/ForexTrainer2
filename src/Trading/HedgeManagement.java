/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Trading;


import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import utils.PrintUtils;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import utils.DataUtils;
import utils.FileUtils;
import utils.Log;
import utils.TRADINGEVENT;


/**
 *
 * @author drosa
 */
public class HedgeManagement {
    
    double actualBalance = 200;
    double actualProfitLoss = 0;
    double equitity = 200;
    double margin = 0;
    double freeMargin = 200;
    double marginLevel = 0;
    double maxEquitity = 0;
    
    Calendar actualDay = Calendar.getInstance();
    Calendar gameDateTime = Calendar.getInstance();
    long lastUpdateTime=0;
    double totalProfit=0;
    double totalWinProfit=0;
    int wins=0;
    double avgWin=0;
    double hedgeLimit=0.0010;
    int totalHedges=0;
    double totalPips = 0;
    Position buyPosition = new Position();
    Position sellPosition = new Position();
    ArrayList<Position> openPositions  = new ArrayList<Position>();
    ArrayList<Position> closePositions = new ArrayList<Position>();
    ArrayList<Calendar> hedgeDays = new ArrayList<Calendar>();
    String logPath="";
    
    //Opciones
    boolean SLEnable = false;
    double BEPips = 4 ;
    boolean hedgeEnabled = true;
    int hedgePips = 10;
    int halfLots = 50;
    double brokerCommision = 0.75;
    //hedgeManagement
    int cycleHedges=0;

    public Calendar initGame(double balance,int bePips,boolean hedgeEnabled
            ,int hedgePips,int halfLots,double brokerCommision,String logDir){
        this.actualBalance = balance;
        this.actualProfitLoss = 0;
        this.equitity = balance;
        this.margin = 0;
        this.freeMargin = balance;
        this.marginLevel = 0;
        this.BEPips = bePips;
        this.hedgeEnabled = hedgeEnabled;
        this.hedgePips = hedgePips;
        this.halfLots = halfLots;
        this.brokerCommision = brokerCommision;
        this.maxEquitity = 0;
        lastUpdateTime=0;
        totalProfit=0;
        totalWinProfit=0;
        wins=0;
        avgWin=0;
        closePositions.clear();
        hedgeDays.clear();
        totalHedges=0;
        gameDateTime = Calendar.getInstance();
        buyPosition.setPositionStatus(PositionStatus.NONE);
        sellPosition.setPositionStatus(PositionStatus.NONE);
        this.cycleHedges=0;
        this.logPath = logDir+"\\"+FileUtils.generateFileNameFromDate(gameDateTime)+".log";
        
        Log.addToLog(logPath,"[NEW_GAME] Started a new game "+DateUtils.datePrint(gameDateTime));  
        
        return gameDateTime;
    }
    
    public void setConfiguration(int bePips
            ,int hedgePips,int halfLots,double brokerCommision){
        
        this.BEPips          = bePips;
        this.hedgePips       = hedgePips;
        this.brokerCommision = brokerCommision;
        this.halfLots        = halfLots;
    }
    
    public static String completeNumber(String num){
        String com=num;
        
        if (com.length()==4)
            com+="0";
        if (com.length()==3)
            com+="00";
        if (com.length()==2)
            com+="000";
        if (com.length()==1)
            com+="0000";
        return com;
    }
    
    public static int getPipsDiff(double val1,double val2){
        String val1Str = PrintUtils.Print(val1);
        String val2Str = PrintUtils.Print(val2);
        val1Str = completeNumber(val1Str.substring(0, 1)+val1Str.substring(2, val1Str.length()));
        val2Str = completeNumber(val2Str.substring(0, 1)+val2Str.substring(2, val2Str.length()));
        
        //System.out.println("[getPipsDiff] "+val1Str+" "+val2Str);
        int diff =Integer.valueOf(val1Str)-Integer.valueOf(val2Str);
        return diff;
    }
    
    public void calculateStats(double actualPrice){
       
        this.actualProfitLoss = 0;
        this.margin = 0;
        if (buyPosition.getPositionStatus()==PositionStatus.OPENED){
            int pips = getPipsDiff(actualPrice,buyPosition.getOpenPrice());
            double pipValue = 10.0*buyPosition.getLots();
            double profitLoss = pips*pipValue;
            double posMargin = buyPosition.getMargin();
            this.actualProfitLoss+=profitLoss;
            this.margin+=posMargin;
        }
        if (sellPosition.getPositionStatus()==PositionStatus.OPENED){
            double pips = getPipsDiff(sellPosition.getOpenPrice(),actualPrice);
            double pipValue = 10.0*sellPosition.getLots();
            double profitLoss = pips*pipValue;
            double posMargin = sellPosition.getMargin();
                    
            this.actualProfitLoss+=profitLoss;
            this.margin+=posMargin;
        }
        this.margin = Math.abs(this.margin);
        if (buyPosition.getPositionStatus()==PositionStatus.OPENED
                && sellPosition.getPositionStatus()==PositionStatus.OPENED)
            this.margin = 0;
        this.equitity = this.actualBalance + this.actualProfitLoss;
        this.freeMargin = this.equitity - this.margin;
        this.marginLevel = this.equitity*100.0/this.margin;
        if (this.equitity>this.maxEquitity)
            this.maxEquitity = this.equitity;
    }

    public Calendar getGameDateTime() {
        return gameDateTime;
    }

    public void setGameDateTime(Calendar gameDateTime) {
        this.gameDateTime = gameDateTime;
    }

    
    
    public ArrayList<Calendar> getHedgeDays() {
        return hedgeDays;
    }

    public void setHedgeDays(ArrayList<Calendar> hedgeDays) {
        this.hedgeDays = hedgeDays;
    }
    
    
    
    public double getTotalWinProfit() {
        return totalWinProfit;
    }

    public void setTotalWinProfit(double totalWinProfit) {
        this.totalWinProfit = totalWinProfit;
    }
    
    public void setSLOptions(boolean slEnable,double pips){
        this.SLEnable = slEnable;
        this.BEPips = pips;
    }
    
    public double getHedgeLimit() {
        return hedgeLimit;
    }

    public void setHedgeLimit(double hedgeLimit) {
        this.hedgeLimit = hedgeLimit;
    }

    public int getTotalHedges() {
        return totalHedges;
    }

    public void setTotalHedges(int totalHedges) {
        this.totalHedges = totalHedges;
    }

    public Position getBuyPosition() {
        return buyPosition;
    }

    public void setBuyPosition(Position buyPosition) {
        this.buyPosition = buyPosition;
    }

    public Position getSellPosition() {
        return sellPosition;
    }

    public void setSellPosition(Position sellPosition) {
        this.sellPosition = sellPosition;
    }

    public ArrayList<Position> getClosePositions() {
        return closePositions;
    }

    public void setClosePositions(ArrayList<Position> closePositions) {
        this.closePositions = closePositions;
    }

    public double getTotalProfit() {
        return totalProfit;
    }

    public void setTotalProfit(double totalProfit) {
        this.totalProfit = totalProfit;
    }

    public double getMaxEquitity() {
        return maxEquitity;
    }

    public void setMaxEquitity(double maxEquitity) {
        this.maxEquitity = maxEquitity;
    }

    
    
    public int getWins() {
        return wins;
    }

    public void setWins(int wins) {
        this.wins = wins;
    }

    public double getAvgWin() {
        return avgWin;
    }

    public void setAvgWin(double avgWin) {
        this.avgWin = avgWin;
    }

    public double getActualBalance() {
        return actualBalance;
    }

    public void setActualBalance(double actualBalance) {
        this.actualBalance = actualBalance;
    }

    public double getEquitity() {
        return equitity;
    }

    public void setEquitity(double equitity) {
        this.equitity = equitity;
    }

    public double getMargin() {
        return margin;
    }

    public void setMargin(double margin) {
        this.margin = margin;
    }

    public double getFreeMargin() {
        return freeMargin;
    }

    public void setFreeMargin(double freeMargin) {
        this.freeMargin = freeMargin;
    }

    public double getMarginLevel() {
        return marginLevel;
    }

    public void setMarginLevel(double marginLevel) {
        this.marginLevel = marginLevel;
    }

    public double getTotalPips() {
        return totalPips;
    }

    public void setTotalPips(double totalPips) {
        this.totalPips = totalPips;
    }
     
    public Position openPosition(PositionType positionType,
            PositionStatus positionStatus,
            double price,
            int leverage,
            double initialLots,
            double lotsPer1000,
            double sl,
            double tp,
            boolean hedged,
            Calendar date){
        
        //calculamos el balance efectivo
        double effectiveBalance = this.actualBalance+this.actualProfitLoss;
        //calculamos los lots necesarios
        double rawLots = TradingUtils.calculateRawLots(effectiveBalance, lotsPer1000);
        double lots    = 0.0;
        
        if (!hedged){//no es hedged
            if (initialLots<0.0){ // no se calcula desde fuera
                lots = TradingUtils.calculateEffectiveLots(rawLots, effectiveBalance, price, leverage);
            }else{
                lots = TradingUtils.calculateEffectiveLots(initialLots, effectiveBalance, price, leverage);
            }
        }else{
            lots = initialLots;
        }
       
        Position pos = null;
        if (positionType==PositionType.BUY){
            pos = buyPosition;           
        }else if (positionType==PositionType.SELL){          
            pos = sellPosition;           
        }
        if (pos!=null){
            lots = DataUtils.truncateDoubleTwoDecimals(lots);
            Calendar cal = Calendar.getInstance();
            cal.setTimeInMillis(date.getTimeInMillis());
            cal.set(Calendar.SECOND, 0);
            cal.set(Calendar.MILLISECOND, 0);
            Date d = cal.getTime();
            pos.setPositionStatus(positionStatus);
            pos.setPositionType(positionType);
            pos.setOpenPrice(price);
            pos.setLots(lots);
            pos.setMargin(100000*lots*price/400);
            pos.setSl(sl);
            pos.setTp(tp);
            pos.setDate(cal);
          
            System.out.println("[OPEN] "
                    +" "+pos.getPositionType()
                    +" "+pos.getPositionStatus()
                    +" "+DateUtils.datePrint(d)
                    +" "+PrintUtils.Print(price)
                    +" "+PrintUtils.Print(lots) +" lots"
                    );
            
            Log.addToLog(logPath,"[OPEN] "+pos.toString());
        }
        
        return pos;
    }
    
    private int calculateTotalPositions(PositionStatus status){
        int total=0;
        if (buyPosition.getPositionStatus()==status)
            total++;
        if (sellPosition.getPositionStatus()==status)
            total++;
        return total;
    }
    
    /**Cierra la mitad de los lotes de las operaciones**/
    public void closeHalf(double closeValue){
        double profit1 = 0;
        double profit2 = 0;
        double buyLots = 0;
        double sellLots = 0;
        PositionStatus buyStatus  = buyPosition.getPositionStatus();
        PositionStatus sellStatus = sellPosition.getPositionStatus();
        if (buyStatus!=PositionStatus.CLOSED){
            //buyPosition.setLots(this.buyPosition.getLots()*0.5);
            if (buyStatus==PositionStatus.OPENED){
                profit1 = HedgeManagement.getPipsDiff(closeValue,buyPosition.getOpenPrice());
                buyLots = buyPosition.getLots()*0.5;
            }
        }
        if (sellStatus!=PositionStatus.CLOSED){
            //sellPosition.setLots(this.sellPosition.getLots()*0.5);
            if (sellStatus==PositionStatus.OPENED){
                profit2 = HedgeManagement.getPipsDiff(sellPosition.getOpenPrice(),closeValue);
                sellLots = sellPosition.getLots()*0.5;
            }
        }
        
        double totalProfit=(profit1*buyLots+profit2*sellLots-this.brokerCommision)*10;
        totalWinProfit+=profit1+profit2-this.brokerCommision;
        this.actualBalance    += totalProfit;
        
        buyPosition.setLots(buyPosition.getLots()*0.5);
        sellPosition.setLots(sellPosition.getLots()*0.5);
        buyPosition.setMargin(buyPosition.getMargin()*0.5);
        sellPosition.setMargin(sellPosition.getMargin()*0.5);
        System.out.println("[CLOSE HALF] "
                    +" "+closeValue
                    +" "+profit2
                    +" "+sellLots
                    +" "+this.brokerCommision
                    +" "+(profit2*sellLots-this.brokerCommision)
                    +" "+PrintUtils.Print4(closeValue)
                    +" "+PrintUtils.Print2(buyPosition.getLots())+" lots"
                    +" New balance= "+PrintUtils.Print2(this.actualBalance)
                    +" New margin= "+PrintUtils.Print2(buyPosition.getMargin())
                    );
    }
    public void closePosition(Position pos,double closeValue,boolean closeHalf,Calendar date){
        
        if (pos==null){
            return;
        }
        
        if (closeHalf){ //creamos una posicion clonica y cerramos la mitad de los lotes
            Position posHalf = new Position();
            posHalf.copy(pos);
            posHalf.setClosePrice(closeValue);
            posHalf.setPositionStatus(PositionStatus.CLOSED);
            closePositions.add(posHalf);
            double profit=0;
            if (posHalf.getPositionType()==PositionType.BUY){
                profit = HedgeManagement.getPipsDiff(closeValue,posHalf.getOpenPrice());
            }
            if (posHalf.getPositionType()==PositionType.SELL){
                profit = HedgeManagement.getPipsDiff(posHalf.getOpenPrice(),closeValue);
            }
            //commision
            profit-=this.brokerCommision;
        
            if (profit>=0){
                totalWinProfit+=profit;
                wins++;
            }
        
            totalProfit+=profit;
            this.actualBalance    += profit*pos.lots*10.0*0.5;
            pos.setLots(pos.getLots()*0.5); //reducimos lots a la mitad del original
            pos.setMargin(pos.getMargin()*0.5); //margen requerido tambien baja
            System.out.println("[CLOSE HALF] "+pos.getPositionType().name()
                    +" "+PrintUtils.Print4(closeValue)
                    +" "+PrintUtils.Print2(profit)+" pips"
                    +" "+PrintUtils.Print2(pos.getLots())+" lots"
                    +" New balance= "+PrintUtils.Print2(this.actualBalance)
                    +" New margin= "+PrintUtils.Print2(this.margin)
                    );
        }else{        
            pos.setClosePrice(closeValue);
            pos.setPositionStatus(PositionStatus.CLOSED);
            Position newPos = new Position();
            newPos.copy(pos);
            closePositions.add(newPos);

            double profit=0;
            if (pos.getPositionType()==PositionType.BUY){
                profit = HedgeManagement.getPipsDiff(closeValue,pos.getOpenPrice());
            }
            if (pos.getPositionType()==PositionType.SELL){
                profit = HedgeManagement.getPipsDiff(pos.getOpenPrice(),closeValue);
            }
            //commision
            profit-=this.brokerCommision;

            if (profit>=0){
                totalWinProfit+=profit;
                wins++;
            }

            totalProfit+=profit;
            this.actualBalance    += profit*pos.lots*10.0;

            Date d = date.getTime();

            Log.addToLog(logPath,"[CLOSE] "+pos.getPositionType().name()
                    +" "+PrintUtils.Print4(closeValue)
                    +" "+PrintUtils.Print2(profit)+" pips"
                    +" "+PrintUtils.Print2(pos.lots)+" lots"
                    );

            System.out.println("[CLOSE] "+pos.getPositionType().name()+" value pips equitity: "
                    +DateUtils.datePrint(d)
                    +" "+PrintUtils.Print4(closeValue)
                    +" "+PrintUtils.Print2(profit)+" pips"
                    +" "+PrintUtils.Print2(pos.lots)+" lots"
                    );
            if (calculateTotalPositions(PositionStatus.OPENED)==0)
                cycleHedges=0;
        }
    }
    
    public GameStats getGameStats(){
        GameStats gameStats = new GameStats();
        gameStats.setGameDateTime(gameDateTime);
        gameStats.setTotalHedges(this.totalHedges);
        gameStats.setTotalTrades(closePositions.size());
        double avgPips = this.totalProfit/closePositions.size();
        if (closePositions.size()==0)
            avgPips = 0;
        gameStats.setAvgPips(avgPips);
        double avgWinPips = this.totalWinProfit*1.0/this.wins;
        if (this.wins==0)
            avgWinPips = 0;
        gameStats.setAvgWinPips(avgWinPips);
        
        return gameStats;
    }
    
    public void resetDay(Calendar day){
        actualDay.setTime(day.getTime());
        lastUpdateTime = 0;
        /*System.out.println("lastUpdateTime: "
                +lastUpdateTime
                +" "+actualDay.get(Calendar.YEAR)                
                );
        */
    }
    
    public boolean sameDay(Calendar cal1,Calendar cal2){
        if (cal1.get(Calendar.DAY_OF_MONTH) == cal2.get(Calendar.DAY_OF_MONTH)
                &&
                cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR)
                )
            return true;
        return false;
    }
    
    public void saveResults(String currentDir){
        
        String gameDir = currentDir+"\\TrainerResults";
        File dir = new File(gameDir);
        if (!dir.exists()){
            dir.mkdir();
        }
        
        String fileName = FileUtils.getFileNameFromCalendar(this.gameDateTime)+".xml";
        File file = new File(fileName);
        file.delete();
          
        System.out.println("FileName: "+fileName);
        File fnew=new File(gameDir+"\\"+fileName);           
        try {
            FileWriter f2 = new FileWriter(fnew, false);
            f2.write("<Game>\n");
            f2.write("  <TotalTrades> "+PrintUtils.Print2(this.closePositions.size())+"</TotalTrades>\n");
            f2.write("  <Balance> "+PrintUtils.Print2(this.actualBalance)+"</Balance>\n");
            f2.write("  <MaxEquitity> "+PrintUtils.Print2(this.maxEquitity)+"</MaxEquitity>\n");
            f2.write("  <Equitity> "+PrintUtils.Print2(this.equitity)+"</Equitity>\n");
            f2.write("</Game>\n");
            f2.close();
             
        } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
        }                    
    }
    
    public void update(Quote q){
        //System.out.println("[UPDATE]");
        if (q==null) return;
        Calendar cal = Calendar.getInstance();
        cal.setTime(q.getDate());
        //cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
       
        if (!sameDay(actualDay,cal) || cal.getTimeInMillis()<=actualDay.getTimeInMillis()){
            return;
        } //no se actualiza con anteriores
 
        if (buyPosition.getPositionStatus()==PositionStatus.OPENED
                && (buyPosition.getDate().getTimeInMillis()<cal.getTimeInMillis())){
            //vemos si se activa el SL
           if (buyPosition.getSl()>0 &&  HedgeManagement.getPipsDiff(buyPosition.getSl(),q.getLow())>=0){                 
                    System.out.println("[update] BUY CLOSED by SL");
                    closePosition(buyPosition,buyPosition.getSl(),false,cal);   
                    calculateStats(q.getClose());
                    return;
            }    
           
            //vemos si se activa el TP
           if (buyPosition.getTp()>0 &&  HedgeManagement.getPipsDiff(q.getHigh(),buyPosition.getTp())>=0){                 
                    System.out.println("[update] BUY CLOSED by TP");
                    closePosition(buyPosition,buyPosition.getTp(),false,cal);   
                    calculateStats(q.getClose());
                    return;
            }  
           
           //movemos el sl si no hay hedge
           if (calculateTotalPositions(PositionStatus.OPENED)<2){
                // vemos si movemos el SL en positivo
                double newSL   = buyPosition.getOpenPrice()+0.0001;
                double trigger = buyPosition.getOpenPrice()+this.BEPips*0.0001;
                if ( HedgeManagement.getPipsDiff(q.getHigh(),trigger)>=0 
                        && (buyPosition.getSl()<newSL || buyPosition.getSl()<=0)){
                    System.out.println("[update] BUY Moving SL to "+PrintUtils.Print(newSL));
                    buyPosition.setSl(newSL);
                    sellPosition.setPositionStatus(PositionStatus.CLOSED);
                    calculateStats(q.getClose());
                    return;
                }
           }
           //vemos si se abre un hedge. CAMBIOS AHORA SE ABRE PENDINGS
           /*int pips = HedgeManagement.getPipsDiff(q.getLow(),buyPosition.getOpenPrice());
           if (this.hedgeEnabled  && cycleHedges==0){
               if (pips<=-this.hedgePips){
                   double hedgeOpen = buyPosition.getOpenPrice()-hedgePips*0.0001;
                   System.out.println("SELL HEDGE OPEN at "+PrintUtils.Print(hedgeOpen));
                   openPosition(PositionType.SELL, PositionStatus.OPENED,hedgeOpen,-1
                           ,buyPosition.getLots(), -1, -1, -1,true, cal);
                   cycleHedges++;
                   totalHedges++;
               }
           }*/
        }else if (buyPosition.getPositionStatus()==PositionStatus.PENDING){
            if (HedgeManagement.getPipsDiff(q.getHigh(),buyPosition.getOpenPrice())>=0){
                buyPosition.setPositionStatus(PositionStatus.OPENED); 
                System.out.println("[update] BUY PENDING ORDER AT "
                        +PrintUtils.Print(buyPosition.getOpenPrice())
                        +" CHANGE STATUS TO OPEN");
            }
        } 
                
        //sellPositions
        if (sellPosition.getPositionStatus()==PositionStatus.OPENED
                && sellPosition.getDate().getTimeInMillis()<cal.getTimeInMillis()){
           
            //Vemos si se cierra por SL
           if (sellPosition.getSl()>0 && HedgeManagement.getPipsDiff(q.getHigh(),sellPosition.getSl())>=0){
                    System.out.println("[update] SELL CLOSED by SL");
                    closePosition(sellPosition,sellPosition.getSl(),false,cal);
                    calculateStats(q.getClose());
                    return;
           }       
           
             //Vemos si se cierra por TP
           if (sellPosition.getTp()>0 &&  HedgeManagement.getPipsDiff(sellPosition.getTp(),q.getLow())>=0){
                    System.out.println("[update] SELL CLOSED by TP");
                    closePosition(sellPosition,sellPosition.getTp(),false,cal);
                    calculateStats(q.getClose());
                    return;
           }    
           
           // vemos si movemos el SL si no hay hedge
           if (calculateTotalPositions(PositionStatus.OPENED)<2){
                double newSL   = sellPosition.getOpenPrice()-0.0001;
                double trigger = sellPosition.getOpenPrice()-this.BEPips*0.0001;
                if ( HedgeManagement.getPipsDiff(trigger,q.getLow())>=0
                        && (sellPosition.getSl()>newSL ||sellPosition.getSl()<=0)){
                    System.out.println("[update] SELL Moving SL to "+PrintUtils.Print(newSL));
                    sellPosition.setSl(newSL);
                    //cerramos pending
                    buyPosition.setPositionStatus(PositionStatus.CLOSED);
                    calculateStats(q.getClose());
                    return;
                }
           }
           
           //vemos si se abre un hedge. CAMBIO= AHORA SON PENDINGS
           /*int pips = HedgeManagement.getPipsDiff(sellPosition.getOpenPrice(),q.getHigh());
           if (this.hedgeEnabled && cycleHedges==0){
               if (pips<=-this.hedgePips){
                   double hedgeOpen = sellPosition.getOpenPrice()+hedgePips*0.0001;
                   System.out.println("BUY HEDGE OPEN at "+PrintUtils.Print(hedgeOpen));
                   openPosition(PositionType.BUY, PositionStatus.OPENED, hedgeOpen
                           ,-1,sellPosition.getLots(), -1, -1, -1, true,cal);
                   cycleHedges++;
                   totalHedges++;
               }
           }*/
        }else if (sellPosition.getPositionStatus()==PositionStatus.PENDING){
            if (HedgeManagement.getPipsDiff(q.getLow(),sellPosition.getOpenPrice())<=0){
                sellPosition.setPositionStatus(PositionStatus.OPENED);  
                System.out.println("[update] SELL PENDING ORDER AT "
                        +PrintUtils.Print(buyPosition.getOpenPrice())
                        +" OPENED");
            }            
        }  
        calculateStats(q.getClose());
    }
    
    public TRADINGEVENT eventDetected(Position pos, Quote q){
        
        TRADINGEVENT tradingEvent = TRADINGEVENT.NONE;
        int pipsSL = -1;
        int pipsSH = -1;            
        int pipsTL = -1;
        int pipsTH = -1;
        int pipsBH = -1;
        int pipsBL = -1;
        
        if (pos.getPositionStatus()==PositionStatus.OPENED){ //posiciones abiertas                        
            if (pos.getSl()>0){
                pipsSL = HedgeManagement.getPipsDiff(pos.getSl(),q.getLow());
                pipsSH = HedgeManagement.getPipsDiff(q.getHigh(),pos.getSl());  
            }
            if (pos.getTp()>0){
                pipsTL = HedgeManagement.getPipsDiff(pos.getTp(),q.getLow());
                pipsTH = HedgeManagement.getPipsDiff(q.getHigh(),pos.getTp());
            }
            
            if (pos.getPositionType() == PositionType.BUY){
                double hedgeOpenH = buyPosition.getOpenPrice()+BEPips*0.0001;
                pipsBH = HedgeManagement.getPipsDiff(q.getHigh(),hedgeOpenH); 
               // System.out.println("BUY hedgeH: "+PrintUtils.Print4(hedgeOpenH)+" "+pipsBH+" "+pipsSL);
                if (pipsSL>=0) return TRADINGEVENT.STOPLOSS;
                if (pipsTH>=0) return TRADINGEVENT.TAKEPROFIT;
                //no hay sl y no hay hedges
                if (pos.getSl()<=0 && getTotalOpenPositions()<=1 && cycleHedges==0 && pipsBH>=0){
                    System.out.println("[eventDetected] BUY TRADINGEVENT.MOVEBE");
                    return TRADINGEVENT.MOVEBE;
                }
            }
            if (pos.getPositionType() == PositionType.SELL){
                double hedgeOpenL = sellPosition.getOpenPrice()-BEPips*0.0001;
                pipsBL = HedgeManagement.getPipsDiff(hedgeOpenL,q.getLow()); 
                if (pipsSH>=0) return TRADINGEVENT.STOPLOSS;
                if (pipsTL>=0) return TRADINGEVENT.TAKEPROFIT;
                //no hay sl y no hay hedges
                if (pos.getSl()<=0 && getTotalOpenPositions()<=1 && cycleHedges==0 && pipsBL>=0){
                    System.out.println("[eventDetected] SELL TRADINGEVENT.MOVEBE");
                    return TRADINGEVENT.MOVEBE;
                }
            }
        }else if (pos.getPositionStatus() == PositionStatus.PENDING){
            int pipsL = HedgeManagement.getPipsDiff(pos.getOpenPrice(),q.getLow());
            int pipsH = HedgeManagement.getPipsDiff(q.getHigh(),pos.getOpenPrice());
            
            if (pos.getPositionType() == PositionType.BUY && pipsH>=0){                
                    return TRADINGEVENT.TRADEOPEN;              
            }
            
            if (pos.getPositionType() == PositionType.SELL && pipsL>=0){                
                    return TRADINGEVENT.TRADEOPEN; 
            }
        }
        
        return tradingEvent;
    }
    
    public int getTotalOpenPositions(){
        int total = 0;
        
        if (buyPosition.getPositionStatus()==PositionStatus.OPENED)
            total++;
        if (sellPosition.getPositionStatus()==PositionStatus.OPENED)
            total++;
        
        return total;
    }
    
    public int evaluatePositions(ArrayList<Quote> data,int begin,int end){
        int result = end;
        
        if (end>=data.size()-1) end= data.size()-1;
        for (int i=begin;i<=end;i++){
            Quote q = data.get(i);
            TRADINGEVENT te1 = eventDetected(buyPosition,q);
            TRADINGEVENT te2 = eventDetected(sellPosition,q);
            if (te1!=TRADINGEVENT.NONE || te2!=TRADINGEVENT.NONE) return i;
        }
        
        
        return result;
    }
  
}
