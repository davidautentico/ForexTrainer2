/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package  Trading;

/**
 *
 * @author david
 */
public class Trade {

    TradeStatus tradeStatus;
    TradeType tradeType;
    double openValue;
    double closeValue;
    int openBar;

    public TradeStatus getTradeStatus() {
        return tradeStatus;
    }

    public void setTradeStatus(TradeStatus tradeStatus) {
        this.tradeStatus = tradeStatus;
    }

    public TradeType getTradeType() {
        return tradeType;
    }

    public void setTradeType(TradeType tradeType) {
        this.tradeType = tradeType;
    }
    
    public double getOpenValue() {
        return openValue;
    }

    public void setOpenValue(double openValue) {
        this.openValue = openValue;
    }

    public double getCloseValue() {
        return closeValue;
    }

    public void setCloseValue(double closeValue) {
        this.closeValue = closeValue;
    }

    public int getOpenBar() {
        return openBar;
    }

    public void setOpenBar(int openBar) {
        this.openBar = openBar;
    }
    
    
    
}
