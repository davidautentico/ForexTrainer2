/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Trading;

import java.util.Calendar;
import utils.PrintUtils;

/**
 *
 * @author drosa
 */
public class Position {
    PositionType positionType = PositionType.NONE;
    Calendar date = Calendar.getInstance();
    Calendar closeDate = Calendar.getInstance();
    double lots=0.0;
    double margin=0.0;
    double openPrice=-1;
    double closePrice=-1;
    double sl=-1;
    double tp=-1;
    PositionStatus positionStatus = PositionStatus.NONE;
    boolean win = true;

    public PositionType getPositionType() {
        return positionType;
    }

    public void setPositionType(PositionType positionType) {
        this.positionType = positionType;
    }

    public double getOpenPrice() {
        return openPrice;
    }

    public void setOpenPrice(double openPrice) {
        this.openPrice = openPrice;
    }

    public double getClosePrice() {
        return closePrice;
    }

    public void setClosePrice(double closePrice) {
        this.closePrice = closePrice;
    }

    public double getSl() {
        return sl;
    }

    public void setSl(double sl) {
        this.sl = sl;
    }

    public double getTp() {
        return tp;
    }

    public void setTp(double tp) {
        this.tp = tp;
    }

    public PositionStatus getPositionStatus() {
        return positionStatus;
    }

    public void setPositionStatus(PositionStatus positionStatus) {
        this.positionStatus = positionStatus;
    }

    public Calendar getDate() {
        return date;
    }

    public void setDate(Calendar date) {
        this.date = date;
    }

    public Calendar getCloseDate() {
        return closeDate;
    }

    public void setCloseDate(Calendar closeDate) {
        this.closeDate = closeDate;
    }
    
    
    public double getLots() {
        return lots;
    }

    public void setLots(double lots) {
        this.lots = lots;
    }

    public double getMargin() {
        return margin;
    }

    public void setMargin(double margin) {
        this.margin = margin;
    }

    public boolean isWin() {
        return win;
    }

    public void setWin(boolean win) {
        this.win = win;
    }
    
    
    
    public void copy(Position pos){
        this.date.setTimeInMillis(pos.getDate().getTimeInMillis());
        this.closeDate.setTimeInMillis(pos.getCloseDate().getTimeInMillis());
        this.closePrice = pos.getClosePrice();
        this.openPrice = pos.getOpenPrice();
        this.positionStatus = pos.getPositionStatus();
        this.positionType = pos.getPositionType();
        this.sl = pos.getSl();
        this.tp = pos.getTp();
        this.lots = pos.getLots();
    }
    
    public String toString(){
        String str = this.positionType.name()
                +" "+this.positionStatus.name()
                +" "+PrintUtils.Print4(this.openPrice)
                +" "+PrintUtils.Print2(this.lots)
                +" "+PrintUtils.Print4(this.sl)
                +" "+PrintUtils.Print4(this.tp)
                ;
        
        return str;
    }
    
     public String toString2(){
        String str = DateUtils.datePrint(this.date)
                +" "+DateUtils.datePrint(this.closeDate)
                +" "+this.positionType.name()
                +" "+PrintUtils.Print4(this.openPrice)
                +" "+PrintUtils.Print4(this.sl)
                +" "+PrintUtils.Print4(this.tp)
                +" "+this.win
                ;
        
        return str;
    }
    
}