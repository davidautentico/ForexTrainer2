/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Trading;

import java.util.Calendar;

/**
 *
 * @author drosa
 */
public class TestResults {
    Calendar cal = null;
    int totalTests;
    int totalTrades;
    double maxEquitity;

    public Calendar getCal() {
        return cal;
    }

    public void setCal(Calendar cal) {
        this.cal = cal;
    }

    
    
    
    public int getTotalTests() {
        return totalTests;
    }

    public void setTotalTests(int totalTests) {
        this.totalTests = totalTests;
    }

    public int getTotalTrades() {
        return totalTrades;
    }

    public void setTotalTrades(int totalTrades) {
        this.totalTrades = totalTrades;
    }

    public double getMaxEquitity() {
        return maxEquitity;
    }

    public void setMaxEquitity(double maxEquitity) {
        this.maxEquitity = maxEquitity;
    }
    
    
    
}
