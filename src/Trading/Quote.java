/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Trading;

import java.util.Date;
import utils.PrintUtils;

/**
 *
 * @author david
 */
public class Quote {

    Date date;
    double open;
    double high;
    double low;
    double close;
    long volume;

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public double getOpen() {
        return open;
    }

    public void setOpen(double open) {
        this.open = open;
    }

    public double getHigh() {
        return high;
    }

    public void setHigh(double high) {
        this.high = high;
    }

    public double getLow() {
        return low;
    }

    public void setLow(double low) {
        this.low = low;
    }

    public double getClose() {
        return close;
    }

    public void setClose(double close) {
        this.close = close;
    }

    public long getVolume() {
        return volume;
    }

    public void setVolume(long volume) {
        this.volume = volume;
    }
    
    public String toString(){
        return DateUtils.datePrint(date)
                        +" "+PrintUtils.Print(this);
    }
    public void copy(Quote last) {
	// TODO Auto-generated method stub
	this.date = last.date;
	this.open=last.open;
	this.high=last.high;
	this.low=last.low;
	this.close=last.close;
    }
}
