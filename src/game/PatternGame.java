/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package game;

import Trading.Position;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import utils.PrintUtils;

/**
 *
 * @author david
 */
public class PatternGame {
    
    int TP=0;
    int SL=0;
    int BE=0;
    int totalTrades = 0;
    int totalWins=0;
    int totalBes=0;
    int bestTrack=0;
    int actualTrack=0;
    Calendar gameDateTime = null;

    
    public void initGame(int tp,int sl){
        this.TP = sl;
        this.SL = sl;
        this.totalTrades = 0;
        this.totalWins = 0;
        this.bestTrack = 0;
        this.actualTrack = 0;
        gameDateTime = Calendar.getInstance();
    }
    
    public int getTP() {
        return TP;
    }

    public void setTP(int TP) {
        this.TP = TP;
    }

    public int getSL() {
        return SL;
    }

    public void setSL(int SL) {
        this.SL = SL;
    }

    public int getTotalTrades() {
        return totalTrades;
    }

    public void setTotalTrades(int totalTrades) {
        this.totalTrades = totalTrades;
    }

    public int getTotalWins() {
        return totalWins;
    }

    public int getBE() {
        return BE;
    }

    public void setBE(int BE) {
        this.BE = BE;
    }

    public int getTotalBes() {
        return totalBes;
    }

    public void setTotalBes(int totalBes) {
        this.totalBes = totalBes;
    }

    public void setTotalWins(int totalWins) {
        this.totalWins = totalWins;
    }

    public int getBestTrack() {
        return bestTrack;
    }

    public void setBestTrack(int bestTrack) {
        this.bestTrack = bestTrack;
    }

    public int getActualTrack() {
        return actualTrack;
    }

    public void setActualTrack(int actualTrack) {
        this.actualTrack = actualTrack;
    }

    public Calendar getGameDateTime() {
        return gameDateTime;
    }

    public void setGameDateTime(Calendar gameDateTime) {
        this.gameDateTime = gameDateTime;
    }
    
    public void updateStats(int res){
        this.totalTrades++;
        if (res==-1){
            this.actualTrack = 0;
        }else{
            this.actualTrack++;
            if (this.actualTrack>this.bestTrack){
                this.bestTrack = this.actualTrack;
            }
        }
    }
    
    public void saveGame(String fileName,ArrayList<Position> positions,boolean details){
        System.out.println("FileName: "+fileName);
        if (!details){
            File fnew=new File(fileName);           
            String res = String.valueOf(this.TP)+","+String.valueOf(this.SL)
                    +","+String.valueOf(this.totalTrades)
                    +","+PrintUtils.PrintPer(this.totalWins*100.0/this.totalTrades)
                    +","+String.valueOf(this.bestTrack);
            try {
                FileWriter f2 = new FileWriter(fnew, true);
                f2.write(res+"\n");
                f2.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } 
        }else{
            File fnew=new File(fileName);
            if (fnew.exists()){
                fnew.delete();
            }
            String header = "TP,SL,TotalTrades,Win(%),BestTrack";
            String header2 = "TRADES";
            String res = String.valueOf(this.TP)+","+String.valueOf(this.SL)
                    +","+String.valueOf(this.totalTrades)
                    +","+PrintUtils.PrintPer(this.totalWins*100.0/this.totalTrades)
                    +","+String.valueOf(this.bestTrack);
            try {
                FileWriter f2 = new FileWriter(fnew, true);
                f2.write(header+"\n");
                f2.write(res+"\n");
                f2.write(header2+"\n");
                for (int i=0;i<positions.size();i++){
                    Position pos = positions.get(i);
                    f2.write(pos.toString2()+"\n");
                }
                f2.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } 
        }
    }
}
