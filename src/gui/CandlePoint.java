/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import Trading.Quote;
import java.awt.Point;
import utils.PrintUtils;

/**
 *
 * @author drosa
 */
public class CandlePoint {
    double x;
    double y;
    double w;
    double h;
    Quote q;

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getW() {
        return w;
    }

    public void setW(double w) {
        this.w = w;
    }

    public double getH() {
        return h;
    }

    public void setH(double h) {
        this.h = h;
    }

    public Quote getQ() {
        return q;
    }

    public void setQ(Quote q) {
        this.q = q;
    }
    
    
        
    public boolean match(Point p){
        /*System.out.println("cuadro buscar: "+PrintUtils.Print(x)
                +" "+PrintUtils.Print(x+w)
                +" "+PrintUtils.Print(y)
                +" "+PrintUtils.Print(y+h)
                +" // "+PrintUtils.Print(p.x)
                +" "+PrintUtils.Print(p.y)
                );*/
        if (x<=p.x && p.x<=(x+w)
                && y<=p.y && p.y<=(y+h)){
            return true;
        }
        
        return false;
    }
}
