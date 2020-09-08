/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import Trading.TestResults;
import static gui.ChartPanel.stroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.font.FontRenderContext;
import java.awt.geom.Line2D;
import java.util.ArrayList;
import utils.PrintUtils;

/**
 *
 * @author drosa
 */
public class EquitityChart extends javax.swing.JPanel {

    ArrayList<TestResults> tests = null;
    final int beginX = 60;
    final int beginY = 10;
    double maxEq=200;
    
    /**
     * Creates new form EquitityChart
     */
    public EquitityChart(double equitity) {
        this.maxEq = equitity;
        initComponents();        
    }
    
    public void setTests(ArrayList<TestResults> tests){
        this.tests = tests;
    }

    public double getMaxEq() {
        return maxEq;
    }

    public void setMaxEq(double maxEq) {
        this.maxEq = maxEq;
    }
            
    private double calculateMAidx(int idx,int n){
    
        int end = idx;
        int begin = idx-n+1;
        if (begin<0) begin =0;
        double avg=0;
        int total=0;
        for (int i=begin;i<=end;i++){
            avg += tests.get(i).getMaxEquitity();
            total++;
        }
        return avg*1.0/total;
    }
    
    private ArrayList<Double> calculateMA(int n){
        ArrayList<Double> ma = new ArrayList<Double>();
        
        for (int i=0;i<tests.size();i++){
            double val = calculateMAidx(i,n);
            ma.add(val);
        }
        return ma;
    }
    
    protected void paintComponent(Graphics g) {
        
        //Preparacion lienzo
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D)g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                            RenderingHints.VALUE_ANTIALIAS_ON);
        int w = getWidth();
        int h = getHeight();
        int maxW = w-130;
        int maxH = h-50;
        
        g.drawRect(beginX, beginY, maxW, maxH);
        g2.setStroke( stroke );
        
        
        if (tests==null || tests.size()<=0) return;
        int maxAllowed = maxH;
        //int maxAllowed = 100;
        double xInc = maxW*1.0/tests.size();
        int maxPointsY = maxH/beginY;
        int maxPointsX = tests.size();                 
        //PUNTOS Y
        for (int i=0;i<maxPointsY ;i++){
            g2.draw(new Line2D.Double(beginX, (beginY+maxH)-beginY*i, beginX-5,(beginY+maxH)-beginY*i));
        }
        
        //PUNTOS X
        for (int i=0;i<maxPointsX ;i++){
            g2.draw(new Line2D.Double(beginX+i*xInc, beginY+maxH, beginX+i*xInc,beginY+maxH+5));
        }
        
        double max = this.maxEq;
        double min = 200.0;
        double spread =(max-min);
        double razon =(double)maxAllowed/spread;
        g2.setPaint(Color.red);
        ArrayList<Double> eqMA = calculateMA(10);
        for (int i = 1; i < tests.size(); i++) {
            double lastValue   = tests.get(i-1).getMaxEquitity();
            double actualValue = tests.get(i).getMaxEquitity();
            //System.out.println("actual: "+PrintUtils.Print(actualValue));
            
            double y1 = beginY+maxH-(lastValue-min)*razon;    
            double y2 = beginY+maxH-(actualValue-min)*razon;   
             //lines
            double x1 = beginX + (i-1)*xInc;               
            double x2 = beginX + (i)*xInc;   
            g2.setPaint(Color.red);
            g2.draw(new Line2D.Double(x1, y1, x2, y2));
            
            //ma
            double lastMA = eqMA.get(i-1);
            double actualMA = eqMA.get(i);
            y1 = beginY+maxH-(lastMA-min)*razon;    
            y2 = beginY+maxH-(actualMA-min)*razon;   
             //lines 
            g2.setPaint(Color.blue);
            g2.draw(new Line2D.Double(x1, y1, x2, y2));
        }
        Font font = g2.getFont();
        g2.setFont(font.deriveFont(10.0f));
        FontRenderContext frc = g2.getFontRenderContext();  
         //Ponemos maximo y minimo
         String s = String.valueOf(PrintUtils.Print(max));
         float sy = (float) beginY +4; 
         float sw = (float)font.getStringBounds(s, frc).getWidth();
         float sx = (beginX - sw - 0)/2;
         //g2.setPaint(Color.white);
        g2.drawString(s, sx, sy);
            //Ponemos maximo y minimo
            s = String.valueOf(PrintUtils.Print(min));
            sy = (float) beginY+maxH; 
            sw = (float)font.getStringBounds(s, frc).getWidth();
            sx = (beginX - sw - 0)/2;
           // g2.setPaint(Color.white);
            g2.drawString(s, sx, sy);
            //Ponemos mitad
            s = String.valueOf(PrintUtils.Print(min+(max-min)/2));
            sy = (float) (beginY+maxH)/2 ; 
            sw = (float)font.getStringBounds(s, frc).getWidth();
            sx = (beginX - sw)/2;
           // g2.setPaint(Color.white);
            g2.drawString(s, sx, sy);

        
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 432, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 354, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
