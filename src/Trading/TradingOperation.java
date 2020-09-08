/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Trading;

import java.util.Calendar;

/**
 *
 * @author david
 */
public class TradingOperation {

    TradeType tradeType = null;
    Calendar cal = Calendar.getInstance();
    double entryValue = 0;
    double TP = 0;
    double SL = 0;
    double BE = 0;
    int index = -1;

    public TradeType getTradeType() {
        return tradeType;
    }

    public Calendar getCal() {
        return cal;
    }

    public void setCal(Calendar cal) {
        this.cal = cal;
    }

    
    public void setTradeType(TradeType tradeType) {
        this.tradeType = tradeType;
    }

    public double getEntryValue() {
        return entryValue;
    }

    public void setEntryValue(double entryValue) {
        this.entryValue = entryValue;
    }

    public double getTP() {
        return TP;
    }

    public void setTP(double TP) {
        this.TP = TP;
    }

    public double getSL() {
        return SL;
    }

    public void setSL(double SL) {
        this.SL = SL;
    }

    public double getBE() {
        return BE;
    }

    public void setBE(double BE) {
        this.BE = BE;
    }

    
    
    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }
            
            
            
}