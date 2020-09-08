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
public class GameStats {
    Calendar gameDateTime;
    int totalTrades=0;
    int totalHedges=0;
    double avgPips=0;
    double avgWinPips=0;

    public Calendar getGameDateTime() {
        return gameDateTime;
    }

    public void setGameDateTime(Calendar gameDateTime) {
        this.gameDateTime = gameDateTime;
    }

    
    public int getTotalTrades() {
        return totalTrades;
    }

    public void setTotalTrades(int totalTrades) {
        this.totalTrades = totalTrades;
    }

    public int getTotalHedges() {
        return totalHedges;
    }

    public void setTotalHedges(int totalHedges) {
        this.totalHedges = totalHedges;
    }

    public double getAvgPips() {
        return avgPips;
    }

    public void setAvgPips(double avgPips) {
        this.avgPips = avgPips;
    }

    public double getAvgWinPips() {
        return avgWinPips;
    }

    public void setAvgWinPips(double avgWinPips) {
        this.avgWinPips = avgWinPips;
    }
    
    
}
