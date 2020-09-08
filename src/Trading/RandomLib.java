/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Trading;


import utils.PrintUtils;
import java.util.ArrayList;
import java.util.Random;


/**
 *
 * @author drosa
 */
public class RandomLib {

    /**
     * DiffP: drif+randomVariations+gaps
     * @param initial
     * @param drift
     * @return 
     */
    public static ArrayList<Double> generateRandomValues(double initial,
            int n,double drift,double gap){
        Random ran = new Random();
        ArrayList<Double> values = new ArrayList<Double>();
        
        values.add(initial);
        double actualValue = initial;
        double totalRands = 0.0;
        for (int i=1; i<n;i++){
            double lastValue = actualValue;
            //double randValue = ran.nextGaussian();
                      
            double gapValue = 0.0;
            if (Math.random()>0.99){//1 entre 100 de un gap
                gapValue = (Math.random()-0.5)*gap*10.0;
            }
            lastValue = values.get(i-1);
            actualValue = lastValue + (Math.random()-0.5)*3*(Math.random()*2)
                    +gapValue+drift;
            values.add(actualValue);
        }       
        return values;
    }
    
    public static int randomInt(int aStart, int aEnd){
		Random random = new Random();
		long range = (long)aEnd - (long)aStart + 1;
		    // compute a fraction of the range, 0 <= frac < range
		long fraction = (long)(range * random.nextDouble());
		int randomNumber =  (int)(fraction + aStart);    
		return randomNumber;
	}
    
     public static void main(String[] args) {
         
         for (int i=1;i<=100;i++){
            ArrayList<Double> values = RandomLib.generateRandomValues(30,250, 0.10, 2);
            double var =3.6;
            System.out.println(PrintUtils.Print(values.get(values.size()-1)));
         }
            /*for (int i=0;i<values.size();i++){
             System.err.println("value "+i+": "+PrintUtils.Print(values.get(i)));
         }*/
         
     }
}
