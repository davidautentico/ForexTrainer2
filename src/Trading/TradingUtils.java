/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Trading;

/**
 *
 * @author david
 */
public class TradingUtils {
    
    public static double calculateRawLots(double balance,double lotsPer1000){
        
        return balance*lotsPer1000/1000.0;
    }
    
    public static double calculateMaxLots(double balance,double price,int leverage){
        return leverage*1.0*balance/(price*100000.0);
    }
    
    public static double calculateEffectiveLots(double initialLots,double balance,double price,int leverage){      
        double maxLots = TradingUtils.calculateMaxLots(balance, price, leverage);
        if (initialLots>maxLots) return maxLots;
        return initialLots;
    }
    
}
