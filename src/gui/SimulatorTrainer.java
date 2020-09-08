/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import Trading.ConvertLib;
import Trading.DataGranularity;
import Trading.DataProvider;
import Trading.DateUtils;
import Trading.HedgeManagement;
import Trading.NewsItem;
import Trading.PhilDay;
import Trading.Position;
import Trading.PositionStatus;
import Trading.PositionType;
import Trading.Quote;
import Trading.SimulationResults;
import indicators.TMA;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.SplashScreen;
import java.awt.geom.Rectangle2D;
import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import javax.swing.BorderFactory;
import javax.swing.JOptionPane;
import utils.DataUtils;
import utils.FileUtils;
import utils.PrintUtils;

/**
 *
 * @author david
 */
public class SimulatorTrainer extends javax.swing.JFrame {

    String rootPath = "";
    
    ArrayList<PhilDay> philDays = null;
    ArrayList<NewsItem> news    = null;
    ArrayList<TMA> tmas         = null;
    ArrayList<TMA> tmas15       = null;
    ArrayList<Quote> dataSource = null;
    ArrayList<Quote> data5m = null;
    ArrayList<Quote> data1h = null;
    ArrayList<Quote> dataPaint = null;
    ArrayList<Quote> dailyData = null;
    ArrayList<Quote> weeklyData = null;
    ArrayList<Quote> monthlyData = null;    
    ArrayList<String> dataFiles = null;
    ChartPanel chartPanel = null;
    SimulationResults results = null;
    boolean chartPanelMode = false;
    Timer gameTimer = null;
    MyTask gameTask=null; 
    HedgeManagement hedgeManagement = new HedgeManagement();
    Quote lastQuote = null;
    GameState gameState     = GameState.STOPPED;
    GameState chartState    = GameState.STOPPED;
    int actualSliderValue = 0;
    boolean runExe = false;
    public static SplashScreen mySplash          = null;
    public static Graphics2D splashGraphics      = null;
    public static Rectangle2D splashTextArea     = null;
    public static Rectangle2D splashProgressArea = null;
    String logDir   = "";
    String gameDir  = "";
    
    int actualZoom      = 8;
    int bePips          = 5;
    int hedgePips       = 15;
    int halfLots        = 50;
    int tickSpeed       = 100;
    int leverage        = 400;
    double lotsPer1000  = 2.5;
    double commissions  = 1.05;
    int ffStep          = 100;
    
    int actualDay = 0;
    int actualDayIndex = 0;
    int absoluteIndex = 0;
    int speedIndy = 1;
    
    /**
     * Creates new form MainFram
     */
    public SimulatorTrainer(String rootPath) {
        initComponents();
        setDefaultConfiguration();
        initCustomComponents();
        initGameTimer();
        this.rootPath = rootPath;
        createFolders();
    }
    
    private void createFolders(){
        this.gameDir = this.rootPath+"\\TrainerResults";
        this.logDir = this.rootPath+"\\TrainerLogs";
        File dir = new File(gameDir);
        if (!dir.exists()){
            dir.mkdir();
        }
        File dirLog = new File(logDir);
        if (!dirLog.exists()){
            dirLog.mkdir();
        }
    }
    
    private void setDefaultConfiguration(){
        bePips          = 5;
        hedgePips       = 15;
        tickSpeed       = 100;
        leverage        = 400;
        lotsPer1000     = 2.5;
        commissions     = 1.05;
        ffStep          = 100;
        halfLots        = 50;
        
        cbTickSpeed.setSelectedIndex(7);
        cbBePips.setSelectedIndex(2);
        cbHedgePips.setSelectedIndex(10);
        cbLeverage.setSelectedIndex(4);
        cbFFStep.setSelectedIndex(3);
        cbCommisions.setSelectedIndex(6);
        cbLotsPer1000.setSelectedIndex(4);
        cbHalfLots.setSelectedIndex(4);
    }
    
    private void setCurrentConfiguration(){
        bePips          = Integer.valueOf((String)cbBePips.getSelectedItem());
        hedgePips       = Integer.valueOf((String)cbHedgePips.getSelectedItem());
        tickSpeed       = Integer.valueOf((String)cbTickSpeed.getSelectedItem());
        leverage        = Integer.valueOf((String)cbLeverage.getSelectedItem());
        lotsPer1000     = Double.valueOf((String)cbLotsPer1000.getSelectedItem());
        commissions     = Double.valueOf((String)cbCommisions.getSelectedItem());
        ffStep          = Integer.valueOf((String)cbFFStep.getSelectedItem());
        halfLots        = Integer.valueOf((String)cbHalfLots.getSelectedItem());
        this.hedgeManagement.setConfiguration(bePips, hedgePips, halfLots,commissions);
    }
    
    public void initGameTimer(){
         //1- Taking an instance of Timer class.
        gameTimer = new Timer("GameTimer");
        //2- Taking an instance of class contains your repeated method.
        gameTask = new MyTask();        
    }
    
    public void initCustomComponents(){
        slider.setMinorTickSpacing(1);
        slider.setMinimum(0);
        slider.setMaximum(100);
        slider.setValue(0);
        chartPanel = new ChartPanel();
        chartPanel.setPreferredSize(new Dimension(50,250));
        chartPanel.setBorder(BorderFactory.createLineBorder (Color.blue, 2));
        chartPanel.setBackground(Color.white);
        getContentPane().add(chartPanel, java.awt.BorderLayout.CENTER);
        pack();
    }
 
   public void renderChart(){
        //System.out.println("[renderChart] Entrado");
        if (chartPanel!=null){
            //como segundo siempre paso el data5m
            if (this.b5mSelected.isSelected())
                chartPanel.setData(dataPaint,data5m,philDays,news,tmas,tmas15,true,DataGranularity.SEC1,this.actualZoom);
            if (this.b1HourSelected.isSelected())
                chartPanel.setData(dataPaint,data1h,philDays,news,tmas,tmas15,true,DataGranularity.HOUR1,this.actualZoom);
            chartPanel.setSliderFactor(this.actualDayIndex);
            //System.out.println("slider value : "+actualDayIndex);
            chartPanel.repaint();
        }
    }
   
    private void resumeGame(){
        //System.out.println("[resumeGame] chartState: "+chartState.name());
        if (chartState == GameState.PAUSED){
            if (gameState!=null){
                gameTask.cancel();
                gameTimer.purge(); 
                //System.out.println("[resumeGame] resume");
            }
            if (gameTimer!=null){
                
            }
            gameTask = new MyTask(); 
            gameTimer = new Timer("GameTimer");
            gameTimer.schedule(gameTask, 0, Integer.valueOf(this.tickSpeed));
            chartState = GameState.STARTED;
        }
    }
    
    private void pauseGame(){
        //System.out.println("[PauseGame] gamestate: "+chartState.name());
        if (gameTask!=null && gameTimer!=null){
            gameTask.cancel();
            gameTimer.purge();
            chartState = GameState.PAUSED;
        }
    }
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jPanel5 = new javax.swing.JPanel();
        jPanel7 = new javax.swing.JPanel();
        eNewPosition = new javax.swing.JButton();
        lBuyStatus = new javax.swing.JLabel();
        lSellStatus = new javax.swing.JLabel();
        lBuyPrice = new javax.swing.JLabel();
        lSellPrice = new javax.swing.JLabel();
        bBuyChangeSL = new javax.swing.JButton();
        bSellChangeSL = new javax.swing.JButton();
        bCloseBuyPosition = new javax.swing.JButton();
        bCloseSellPosition = new javax.swing.JButton();
        lBuySL = new javax.swing.JLabel();
        lBuyTP = new javax.swing.JLabel();
        lSellSL = new javax.swing.JLabel();
        lSellTP = new javax.swing.JLabel();
        lBuyLots = new javax.swing.JLabel();
        lSellLots = new javax.swing.JLabel();
        lBuyProfit = new javax.swing.JLabel();
        lSellProfit = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        eTotalPips = new javax.swing.JTextField();
        eClosedTrades = new javax.swing.JTextField();
        eTotalHedges = new javax.swing.JTextField();
        eAvgPips = new javax.swing.JTextField();
        jLabel10 = new javax.swing.JLabel();
        eAvgWinPips = new javax.swing.JTextField();
        eBalance = new javax.swing.JTextField();
        eEquitity = new javax.swing.JTextField();
        eMargin = new javax.swing.JTextField();
        jLabel15 = new javax.swing.JLabel();
        eFreeMargin = new javax.swing.JTextField();
        jLabel11 = new javax.swing.JLabel();
        eMarginLevel = new javax.swing.JTextField();
        jLabel12 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        eMaxEquitity = new javax.swing.JTextField();
        jPanel6 = new javax.swing.JPanel();
        slider = new javax.swing.JSlider();
        jPanel2 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        bStartGame = new javax.swing.JButton();
        bPause = new javax.swing.JButton();
        bResume = new javax.swing.JButton();
        bRewind = new javax.swing.JButton();
        bForward = new javax.swing.JButton();
        bSaveResults = new javax.swing.JButton();
        bStats = new javax.swing.JButton();
        bNextDay = new javax.swing.JButton();
        cbFFStep = new javax.swing.JComboBox();
        b5mSelected = new javax.swing.JToggleButton();
        b1HourSelected = new javax.swing.JToggleButton();
        bZoomIn = new javax.swing.JButton();
        bZoomOut = new javax.swing.JButton();
        jPanel4 = new javax.swing.JPanel();
        bLoad = new javax.swing.JButton();
        eLoadFile = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel20 = new javax.swing.JLabel();
        jLabel21 = new javax.swing.JLabel();
        jLabel22 = new javax.swing.JLabel();
        cbBePips = new javax.swing.JComboBox();
        cbHedgePips = new javax.swing.JComboBox();
        cbLeverage = new javax.swing.JComboBox();
        cbLotsPer1000 = new javax.swing.JComboBox();
        cbCommisions = new javax.swing.JComboBox();
        cbTickSpeed = new javax.swing.JComboBox();
        bSetConfig = new javax.swing.JButton();
        jButton1 = new javax.swing.JButton();
        cbHalfLots = new javax.swing.JComboBox();
        jLabel23 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Forex Trainer 1.5 @DaveRosa");

        jPanel1.setBackground(new java.awt.Color(255, 0, 0));
        jPanel1.setPreferredSize(new java.awt.Dimension(1058, 170));
        jPanel1.setLayout(new java.awt.BorderLayout());

        jPanel5.setPreferredSize(new java.awt.Dimension(1096, 145));
        jPanel5.setLayout(new java.awt.GridLayout(1, 0));

        jPanel7.setBackground(new java.awt.Color(204, 204, 204));
        jPanel7.setPreferredSize(new java.awt.Dimension(533, 145));

        eNewPosition.setBackground(new java.awt.Color(51, 204, 0));
        eNewPosition.setText("NEW POSITION");
        eNewPosition.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                eNewPositionActionPerformed(evt);
            }
        });

        lBuyStatus.setForeground(new java.awt.Color(51, 153, 0));
        lBuyStatus.setText("NONE");

        lSellStatus.setForeground(new java.awt.Color(255, 0, 0));
        lSellStatus.setText("NONE");

        lBuyPrice.setText("NONE");

        lSellPrice.setText("NONE");

        bBuyChangeSL.setText("SL TP");
        bBuyChangeSL.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bBuyChangeSLActionPerformed(evt);
            }
        });

        bSellChangeSL.setText("SL TP");
        bSellChangeSL.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bSellChangeSLActionPerformed(evt);
            }
        });

        bCloseBuyPosition.setText("CLOSE");
        bCloseBuyPosition.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bCloseBuyPositionActionPerformed(evt);
            }
        });

        bCloseSellPosition.setText("CLOSE");
        bCloseSellPosition.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bCloseSellPositionActionPerformed(evt);
            }
        });

        lBuySL.setText("NONE");

        lBuyTP.setText("NONE");

        lSellSL.setText("NONE");

        lSellTP.setText("NONE");

        lBuyLots.setText("NONE");

        lSellLots.setText("NONE");

        lBuyProfit.setText("NONE");

        lSellProfit.setText("NONE");

        jLabel2.setText("Profit");

        jLabel3.setText("Open");

        jLabel4.setText("SL");

        jLabel5.setText("TP");

        jLabel16.setText("Lots");

        jLabel6.setText("TOTAL PIPS:");
        jLabel6.setPreferredSize(new java.awt.Dimension(67, 14));

        eTotalPips.setEditable(false);
        eTotalPips.setText("0");

        eClosedTrades.setEditable(false);
        eClosedTrades.setText("0");

        eTotalHedges.setEditable(false);
        eTotalHedges.setText("0");

        eAvgPips.setEditable(false);
        eAvgPips.setText("0");

        jLabel10.setText("AVGWINPIPS:");

        eAvgWinPips.setEditable(false);
        eAvgWinPips.setText("0");

        eBalance.setEditable(false);
        eBalance.setText("0");

        eEquitity.setEditable(false);
        eEquitity.setText("0");

        eMargin.setEditable(false);
        eMargin.setText("0");

        jLabel15.setText("FREE MARGIN");
        jLabel15.setPreferredSize(new java.awt.Dimension(75, 14));

        eFreeMargin.setEditable(false);
        eFreeMargin.setText("0");

        jLabel11.setText("MARGIN LEVEL");
        jLabel11.setPreferredSize(new java.awt.Dimension(75, 14));

        eMarginLevel.setEditable(false);
        eMarginLevel.setText("0");

        jLabel12.setText("BALANCE          ");

        jLabel14.setText("MARGIN");

        jLabel17.setText("EQUITITY");

        jLabel13.setText("AVGPIPS:");
        jLabel13.setPreferredSize(new java.awt.Dimension(67, 14));

        jLabel9.setText("TOTAL TRADES");
        jLabel9.setPreferredSize(new java.awt.Dimension(67, 14));

        jLabel18.setText("TOTAL HEDGES");
        jLabel18.setPreferredSize(new java.awt.Dimension(67, 14));

        jLabel19.setText("MAX EQUITITY");

        eMaxEquitity.setEditable(false);
        eMaxEquitity.setText("0");

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(eNewPosition)
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(lBuyStatus, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(lSellStatus, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(lBuyPrice, javax.swing.GroupLayout.DEFAULT_SIZE, 42, Short.MAX_VALUE)
                                .addComponent(lSellPrice, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addComponent(jLabel3))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lSellSL, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lBuySL, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel4))
                        .addGap(10, 10, 10)
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lBuyTP, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lSellTP, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel5))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lBuyLots, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lSellLots, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel16))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel2)
                            .addGroup(jPanel7Layout.createSequentialGroup()
                                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(lSellProfit)
                                    .addComponent(lBuyProfit))
                                .addGap(18, 18, 18)
                                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel7Layout.createSequentialGroup()
                                        .addComponent(bSellChangeSL)
                                        .addGap(18, 18, 18)
                                        .addComponent(bCloseSellPosition))
                                    .addGroup(jPanel7Layout.createSequentialGroup()
                                        .addComponent(bBuyChangeSL)
                                        .addGap(18, 18, 18)
                                        .addComponent(bCloseBuyPosition)))))))
                .addGap(18, 18, 18)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel6, javax.swing.GroupLayout.DEFAULT_SIZE, 85, Short.MAX_VALUE)
                    .addComponent(jLabel10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel13, javax.swing.GroupLayout.DEFAULT_SIZE, 85, Short.MAX_VALUE)
                    .addComponent(jLabel9, javax.swing.GroupLayout.DEFAULT_SIZE, 85, Short.MAX_VALUE)
                    .addComponent(jLabel18, javax.swing.GroupLayout.DEFAULT_SIZE, 85, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(eTotalPips)
                            .addComponent(eClosedTrades)
                            .addComponent(eAvgWinPips, javax.swing.GroupLayout.PREFERRED_SIZE, 94, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addComponent(eTotalHedges, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 94, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(eAvgPips, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 94, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(jLabel11, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel15, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel12))
                    .addComponent(jLabel14)
                    .addComponent(jLabel17))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(eFreeMargin, javax.swing.GroupLayout.PREFERRED_SIZE, 106, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(eMarginLevel, javax.swing.GroupLayout.PREFERRED_SIZE, 106, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(eMargin, javax.swing.GroupLayout.PREFERRED_SIZE, 106, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(eEquitity, javax.swing.GroupLayout.PREFERRED_SIZE, 106, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addComponent(eBalance, javax.swing.GroupLayout.PREFERRED_SIZE, 106, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel19)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(eMaxEquitity, javax.swing.GroupLayout.PREFERRED_SIZE, 106, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(189, 189, 189))
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addComponent(eNewPosition)
                .addGap(9, 9, 9)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(jLabel3)
                    .addComponent(jLabel4)
                    .addComponent(jLabel5)
                    .addComponent(jLabel16))
                .addGap(6, 6, 6)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lBuyStatus)
                    .addComponent(lBuyPrice)
                    .addComponent(bBuyChangeSL)
                    .addComponent(bCloseBuyPosition)
                    .addComponent(lBuySL)
                    .addComponent(lBuyTP)
                    .addComponent(lBuyLots)
                    .addComponent(lBuyProfit))
                .addGap(18, 18, 18)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lSellStatus)
                    .addComponent(lSellPrice)
                    .addComponent(bSellChangeSL)
                    .addComponent(bCloseSellPosition)
                    .addComponent(lSellSL)
                    .addComponent(lSellTP)
                    .addComponent(lSellLots)
                    .addComponent(lSellProfit))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(eTotalPips, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(eBalance, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel12)
                    .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel19)
                    .addComponent(eMaxEquitity, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(9, 9, 9)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(eClosedTrades, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(eEquitity, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel17)
                    .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(eTotalHedges, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(eMargin, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel14)
                    .addComponent(jLabel18, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(eFreeMargin, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel15, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(eAvgPips, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(eAvgWinPips, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(eMarginLevel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(0, 6, Short.MAX_VALUE))
        );

        jPanel5.add(jPanel7);

        jPanel1.add(jPanel5, java.awt.BorderLayout.CENTER);

        jPanel6.setLayout(new java.awt.BorderLayout());

        slider.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                sliderStateChanged(evt);
            }
        });
        jPanel6.add(slider, java.awt.BorderLayout.CENTER);

        jPanel1.add(jPanel6, java.awt.BorderLayout.PAGE_START);

        getContentPane().add(jPanel1, java.awt.BorderLayout.SOUTH);

        jPanel2.setLayout(new java.awt.BorderLayout());

        jPanel3.setBackground(new java.awt.Color(153, 153, 153));
        jPanel3.setPreferredSize(new java.awt.Dimension(1235, 35));

        bStartGame.setText("NEW GAME");
        bStartGame.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bStartGameActionPerformed(evt);
            }
        });

        bPause.setText("PAUSE");
        bPause.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bPauseActionPerformed(evt);
            }
        });

        bResume.setText("RESUME");
        bResume.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bResumeActionPerformed(evt);
            }
        });

        bRewind.setText("RW");
        bRewind.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bRewindActionPerformed(evt);
            }
        });

        bForward.setText("FF");
        bForward.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bForwardActionPerformed(evt);
            }
        });

        bSaveResults.setText("SAVE RESULTS");
        bSaveResults.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bSaveResultsActionPerformed(evt);
            }
        });

        bStats.setText("STATS");
        bStats.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bStatsActionPerformed(evt);
            }
        });

        bNextDay.setText("NEXT DAY");
        bNextDay.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bNextDayActionPerformed(evt);
            }
        });

        cbFFStep.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "1", "10", "50", "100", "150", "200", "300", "400", "500", "600", "700", "800", "900", "1000", "2000", "3000", "4000", "5000" }));

        b5mSelected.setText("5M");
        b5mSelected.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        b5mSelected.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                b5mSelectedActionPerformed(evt);
            }
        });

        b1HourSelected.setText("1H");
        b1HourSelected.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        b1HourSelected.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                b1HourSelectedActionPerformed(evt);
            }
        });

        bZoomIn.setText("ZOOM IN");
        bZoomIn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bZoomInActionPerformed(evt);
            }
        });

        bZoomOut.setText("ZOOM OUT");
        bZoomOut.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bZoomOutActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(6, 6, 6)
                .addComponent(b5mSelected, javax.swing.GroupLayout.PREFERRED_SIZE, 57, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(b1HourSelected, javax.swing.GroupLayout.PREFERRED_SIZE, 57, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(25, 25, 25)
                .addComponent(bStartGame)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(bNextDay)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(bPause)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(bResume)
                .addGap(18, 18, 18)
                .addComponent(bRewind)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(bForward)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cbFFStep, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(bZoomIn)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(bZoomOut)
                .addGap(29, 29, 29)
                .addComponent(bSaveResults)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(bStats)
                .addContainerGap(196, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(bStartGame)
                    .addComponent(bPause)
                    .addComponent(bResume)
                    .addComponent(bRewind)
                    .addComponent(bForward)
                    .addComponent(bSaveResults)
                    .addComponent(bStats)
                    .addComponent(bNextDay)
                    .addComponent(cbFFStep, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(b5mSelected)
                    .addComponent(b1HourSelected)
                    .addComponent(bZoomIn)
                    .addComponent(bZoomOut))
                .addContainerGap())
        );

        jPanel2.add(jPanel3, java.awt.BorderLayout.CENTER);

        jPanel4.setPreferredSize(new java.awt.Dimension(1058, 35));

        bLoad.setText("LOAD");
        bLoad.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bLoadActionPerformed(evt);
            }
        });

        eLoadFile.setEnabled(false);

        jLabel1.setText("Speed");

        jLabel7.setText("BE+1");

        jLabel8.setText("Hedge at:");

        jLabel20.setText("Lots per $1000:");

        jLabel21.setText("Leverage:");

        jLabel22.setText("Commisions:");

        cbBePips.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", " " }));

        cbHedgePips.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "25", "30", "35", "40", "45", "50" }));

        cbLeverage.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "50", "100", "200", "300", "400", "500" }));

        cbLotsPer1000.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "0.5", "1", "1.5", "2", "2.5" }));

        cbCommisions.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "0.75", "0.80", "0.85", "0.90", "0.95", "1.00", "1.05", "1.10", "1.15", "1.20", "1.30", "1.40", "1.50", "2.00" }));

        cbTickSpeed.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "1", "10", "50", "60", "70", "80", "90", "100", "150", "200", "250", "300", "350", "400", "450", "500", "550", "600", "650", "700", "750", "800", "850", "900", "1000" }));

        bSetConfig.setText("SET CONFIG");
        bSetConfig.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bSetConfigActionPerformed(evt);
            }
        });

        jButton1.setText("AUTO FAST");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        cbHalfLots.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "90", "80", "70", "60", "50", "40", "30", "20", "10", "0" }));
        cbHalfLots.setSelectedIndex(4);

        jLabel23.setText("HalfLots");

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(bLoad)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(eLoadFile, javax.swing.GroupLayout.PREFERRED_SIZE, 227, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cbTickSpeed, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel7)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cbBePips, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel8)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cbHedgePips, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel21)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cbLeverage, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel23)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 15, Short.MAX_VALUE)
                .addComponent(cbHalfLots, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel20)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cbLotsPer1000, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel22)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(cbCommisions, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(bSetConfig)
                .addGap(18, 18, 18)
                .addComponent(jButton1)
                .addGap(29, 29, 29))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(bLoad)
                    .addComponent(eLoadFile, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1)
                    .addComponent(jLabel7)
                    .addComponent(jLabel8)
                    .addComponent(jLabel20)
                    .addComponent(jLabel21)
                    .addComponent(jLabel22)
                    .addComponent(cbBePips, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cbHedgePips, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cbLeverage, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cbLotsPer1000, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cbCommisions, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cbTickSpeed, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(bSetConfig)
                    .addComponent(jButton1)
                    .addComponent(cbHalfLots, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel23))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel2.add(jPanel4, java.awt.BorderLayout.PAGE_START);

        getContentPane().add(jPanel2, java.awt.BorderLayout.NORTH);

        pack();
    }// </editor-fold>//GEN-END:initComponents

     public static ArrayList<Quote> calculateCalendarAdjusted(ArrayList<Quote> data){
	        
        ArrayList<Quote> transformed = new ArrayList<Quote>();
        if (data==null) return null;	      
	for (int i=0;i<data.size();i++){
            Quote q = data.get(i);
	    Quote qNew = new Quote();
	    qNew.copy(q);
	    Date d = data.get(i).getDate();
	    Calendar cal = Calendar.getInstance();
            cal.setTime(d);
	    int offset = DateUtils.calculatePepperGMTOffset(cal);
	    cal.add(Calendar.HOUR_OF_DAY, offset);
	    qNew.setDate(cal.getTime());
            /*System.out.println("before and new: "
                    +DateUtils.datePrint(q.getDate())+" "+PrintUtils.Print(q.getOpen())
                    +" "+DateUtils.datePrint(qNew.getDate())+" "+PrintUtils.Print(qNew.getOpen())
                    );*/
	    transformed.add(qNew);
        }
	return transformed;
    }
    
     private boolean newDay(boolean first){
     
        this.actualDayIndex = 0;
        if (first){//cargamos de inicio
            ArrayList<String> dataFiles = DataUtils.loadDataFilesFromPath(rootPath+"\\fxdata");
            //ArrayList<String> dataFiles = DataUtils.loadDataFilesFromPath(System.getProperty("user.dir")+"\\data");

            TrainingFileDialog tfDialog = new TrainingFileDialog(this,true);            
            tfDialog.setTrainingFiles (dataFiles);
            tfDialog.setVisible(true);
            if (tfDialog.isOk()){
                String fileChoosed = tfDialog.getFileChoosed();
                //System.out.println("Training file choosed: "+fileChoosed); 
                File fileData = new File(fileChoosed);
                
                this.eLoadFile.setText(fileData.getName());
                
                String linesFileName = fileData.getParent()+"\\"+fileData.getName().substring(0, 14)+"_LINES.csv";
                String tmaFileName   = fileData.getParent()+"\\"+fileData.getName().substring(0, 14)+"_5m_TMA.csv";
                String tma15FileName   = fileData.getParent()+"\\"+fileData.getName().substring(0, 14)+"_15m_TMA.csv";
                
                if (FileUtils.containsDayOfWeek(fileData.getName())){
                    String dayOfWeek = FileUtils.getDayOfWeek(fileData.getName());
                    String interval  = FileUtils.getInterval(fileData.getName());
                    linesFileName = fileData.getParent()+"\\"+fileData.getName().substring(0, 11)
                            +"_"+dayOfWeek+"_LINES_"+interval+".csv";
                    tmaFileName   = fileData.getParent()+"\\"+fileData.getName().substring(0, 11)
                            +"_"+dayOfWeek+"_5m_TMA_"+interval+".csv";
                    tma15FileName = fileData.getParent()+"\\"+fileData.getName().substring(0, 11)
                            +"_"+dayOfWeek+"_15m_TMA_"+interval+".csv";
                }
                System.out.println("Training file choosed tma: "
                            +fileData.getName()
                            +" "+linesFileName
                            +" "+tmaFileName
                            +" "+tma15FileName
                            ); 
                File tmaFile = new File(tmaFileName);
                File tma15File = new File(tma15FileName);
                File linesFile = new File(linesFileName);

                if (!tmaFile.exists()){
                    System.out.println("tmaFile does not exist");
                    return false;
                }
                 if (!tma15File.exists()){
                    System.out.println("tmaFile 15min does not exist");
                    return false;
                }
                if (!linesFile.exists()){
                    System.out.println("linesFile does not exist");
                    return false;
                }
                 if (dataSource!=null)
                        dataSource.clear();
                    else dataSource = new ArrayList<Quote>();
                    if (tmas!=null)
                        tmas.clear();
                    else tmas = new ArrayList<TMA>();
                    if (tmas15!=null)
                        tmas15.clear();
                    else tmas15 = new ArrayList<TMA>();
                    if (philDays!=null)
                        philDays.clear();
                    else philDays = new ArrayList<PhilDay>();

                    //Cargo datos del mes
                    dataSource = DAO.retrieveData(fileData.getAbsolutePath(),DataProvider.DUKASCOPY_FOREX,0);//1s
                    tmas       = TMA.loadFromFile(tmaFileName);//tmas 5m
                    tmas15     = TMA.loadFromFile(tma15FileName);//tmas 15m
                    philDays   = PhilDay.loadFromFile(linesFileName);//phildays
                    this.actualDay = 0; //primer dia del philday
                    this.absoluteIndex = 0;
                    
                    System.out.println("dataSource: "+dataSource.size());
                    System.out.println("tmas 5min: "+tmas.size());
                    System.out.println("tmas 15min: "+tmas15.size());
                    System.out.println("philDays: "+philDays.size());

                    /*for (int i=0;i<philDays.size();i++){
                        System.out.println(philDays.get(i).toString());
                    }*/
             }//if dialog
        }else{//siguiente dia
            this.actualDay++;
            
        }
        
        if (philDays==null || philDays.size()==0) return false;
        //no hay más días en este mes
        if (actualDay>=philDays.size()){
            return false;
        }
        if (data5m!=null)
            data5m.clear();
        else
            data5m = new ArrayList<Quote>();
        if (dataPaint!=null)
            dataPaint.clear();
        else
            dataPaint = new ArrayList<Quote>();
        
        Calendar actualCal = philDays.get(actualDay).getDay();
        int daySearch   = actualCal.get(Calendar.DAY_OF_MONTH);
        int monthSearch = actualCal.get(Calendar.MONTH);
        this.news = DAO.loadNews(rootPath+"\\fxdata\\news.txt");
        /*for (int i=0;i<=news.size();i++){
            System.out.println(news.get(i).toString());
        }*/
        System.out.println("news loaded: "+news.size());
        Calendar sourceCal = Calendar.getInstance();
        for (int i=0;i<dataSource.size();i++){
            Quote q = dataSource.get(i);
            sourceCal.setTime(q.getDate());
            int daySource   = sourceCal.get(Calendar.DAY_OF_MONTH);
            int monthSource = sourceCal.get(Calendar.MONTH);
            if (daySource==daySearch && monthSource==monthSearch){
                Quote qNew = new Quote();
                qNew.copy(q);
                dataPaint.add(qNew);
            }                        
        }    
        this.absoluteIndex    = actualDay*86400;
        this.absoluteIndex   += 700;
        this.actualDayIndex  += 700;
        //creamos los datos de 5min necesarios
        data5m   = ConvertLib.convert(dataPaint, 300);
        //creamos los datos de 1h
        data1h   = ConvertLib.convert(dataPaint, 3600);
        
        System.out.println("NEW DAY-> dataPaint data5m data1h tma: "
                +" "+DateUtils.datePrint(actualCal)
                +dataPaint.size()+" "+data5m.size()
                +" "+data1h.size()
                +" "+tmas.size());
        hedgeManagement.resetDay(actualCal);
        
        return true;
    }
    
      
    
    private void sliderStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_sliderStateChanged
        // TODO add your handling code here:
        if (dataSource!=null && dataSource.size()>0){
            //System.out.println("[sliderStateChanged] a renderChart");
            renderChart();
        }
    }//GEN-LAST:event_sliderStateChanged

    private void closePendingOrder(PositionType type){
        if (type==PositionType.BUY){
            if (hedgeManagement.getBuyPosition()!=null
                    && hedgeManagement.getBuyPosition().getPositionStatus()==PositionStatus.PENDING){
                hedgeManagement.getBuyPosition().setPositionStatus(PositionStatus.CLOSED);
            }
        }else if (type==PositionType.SELL){
            if (hedgeManagement.getBuyPosition()!=null
                     && hedgeManagement.getBuyPosition().getPositionStatus()==PositionStatus.PENDING){
                hedgeManagement.getSellPosition().setPositionStatus(PositionStatus.CLOSED);
            }
        }
    }
    private PositionType getPosContrarian(PositionType posType){
        
        if (posType==PositionType.BUY) return PositionType.SELL;
        if (posType==PositionType.SELL) return PositionType.BUY;
        
        return PositionType.NONE;
    }
    
    private void eNewPositionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_eNewPositionActionPerformed

        if (this.gameState==GameState.STOPPED ){
              JOptionPane.showMessageDialog(this,
                    "START A NEW GAME FIRST",
                    "info",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }
            
        //pause game
        pauseGame();

        boolean buyAlready  = false;
        boolean sellAlready = false;
        if (hedgeManagement.getBuyPosition()!=null 
                && hedgeManagement.getBuyPosition().getPositionStatus()==PositionStatus.OPENED)
             buyAlready = true;
        if (hedgeManagement.getSellPosition()!=null
                && hedgeManagement.getSellPosition().getPositionStatus()==PositionStatus.OPENED)
             sellAlready = true;
              
        //Open new Position
        NewPositionDialog positionDialog = new NewPositionDialog(this,true);
        positionDialog.setActualPrice(Double.valueOf(PrintUtils.Print(chartPanel.getLastQuote().getClose())));
        positionDialog.setBuySellOpened(buyAlready,sellAlready);
        positionDialog.setVisible(true);
        
        if (positionDialog.isOk()){
            //Guardamos posicion
            if (positionDialog.getMarketPos()!=null){//market
                Position pos = new Position();
                pos.copy (positionDialog.getMarketPos());
                closePendingOrder(pos.getPositionType());
                addPosition(pos);
                updatePosInterface(pos,-1);
                //abrimos pending si no hay ya una
                if (hedgeManagement.getTotalOpenPositions()<=1){
                    Position posPending = new Position();
                    double entryValue = pos.getOpenPrice() + this.hedgePips*0.0001;
                    if (pos.getPositionType() == PositionType.BUY)
                        entryValue = pos.getOpenPrice() - this.hedgePips*0.0001;
                    posPending.setLots(pos.getLots());
                    posPending.setPositionType(getPosContrarian(pos.getPositionType()));
                    posPending.setOpenPrice(entryValue);
                    posPending.setPositionStatus(PositionStatus.PENDING);
                    addPosition(posPending);
                    updatePosInterface(posPending,-1);
                }
            }
            if (positionDialog.getPendingPos()!=null){//pending
                Position pos = new Position();
                pos.copy (positionDialog.getPendingPos());
                closePendingOrder(pos.getPositionType());
                addPosition(pos);
                updatePosInterface(pos,-1);
            }
        }
        //resume game
        resumeGame();
    }//GEN-LAST:event_eNewPositionActionPerformed

    private void updatePosInterface(Position pos,double value){
        
        boolean clean=false;
        if (pos.getPositionStatus()==PositionStatus.CLOSED
                || pos.getPositionStatus()==PositionStatus.NONE){
            clean = true;
        }
        
        PositionType type     = pos.getPositionType();
        PositionStatus status = pos.getPositionStatus();
        
        //System.out.println("[UPDATEPOSINTERFACE] Position: "+pos.toString());
        
        if (type==PositionType.BUY){
            if (!clean){
                lBuyStatus.setText("BUY "+status.name()); 
                lBuyPrice.setText(PrintUtils.Print(pos.getOpenPrice()));
                lBuySL.setText(PrintUtils.Print(pos.getSl()));
                lBuyTP.setText(PrintUtils.Print(pos.getTp()));
                lBuyLots.setText(PrintUtils.Print2(pos.getLots()));
                int profit =0;
                if (value>=0){
                    //System.out.println("buy actual y open: "+PrintUtils.Print(value)
                    //        +" "+PrintUtils.Print(pos.getOpenPrice()));
                    profit = HedgeManagement.getPipsDiff(value, pos.getOpenPrice());
                }
                if (status==PositionStatus.OPENED){
                    lBuyProfit.setForeground(Color.BLACK);

                    if (profit>0)
                        lBuyProfit.setForeground(Color.GREEN.darker());
                    if (profit<0)
                        lBuyProfit.setForeground(Color.RED);
                    lBuyProfit.setText(String.valueOf(profit));
                }
            }else{
                lBuyStatus.setText("NONE"); 
                lBuyPrice.setText("NONE");
                lBuySL.setText("NONE");
                lBuyTP.setText("NONE");
                lBuyLots.setText("NONE");
                lBuyProfit.setForeground(Color.BLACK);
                lBuyProfit.setText("NONE");
            }
        }
        if (type==PositionType.SELL){
            if (!clean){
                lSellStatus.setText("SELL "+status.name());          
                lSellPrice.setText(PrintUtils.Print(pos.getOpenPrice()));
                lSellSL.setText(PrintUtils.Print(pos.getSl()));
                lSellTP.setText(PrintUtils.Print(pos.getTp()));
                lSellLots.setText(PrintUtils.Print2(pos.getLots()));
                int profit =0;
                if (value>=0){
                    profit = HedgeManagement.getPipsDiff(pos.getOpenPrice(),value);
                }
                if (status==PositionStatus.OPENED){
                    lSellProfit.setForeground(Color.BLACK);
                    if (profit>0)
                        lSellProfit.setForeground(Color.GREEN.darker());
                    if (profit<0)
                        lSellProfit.setForeground(Color.RED);
                    lSellProfit.setText(String.valueOf(profit));
                }
            }else{
                lSellStatus.setText("NONE"); 
                lSellPrice.setText("NONE");
                lSellSL.setText("NONE");
                lSellTP.setText("NONE");
                lSellLots.setText("NONE");
                lSellProfit.setForeground(Color.BLACK);
                lSellProfit.setText("NONE");
            }
        }
        
    }
    
    private void addPosition(Position pos){
        int leverage = Integer.valueOf(this.leverage);
        double lotsPer1000 = Double.valueOf(this.lotsPer1000);
        Calendar cal = Calendar.getInstance();
        cal.setTime(chartPanel.getLastQuote().getDate()); 
        //Se abre una posicion no hedge
        boolean hedged = false;
        boolean buyAlready = false;
        boolean sellAlready = false;
        
        double lots = -1.0;
        
        if (hedgeManagement.getBuyPosition()!=null 
                && hedgeManagement.getBuyPosition().getPositionStatus()==PositionStatus.OPENED){
             buyAlready = true;
             lots = hedgeManagement.getBuyPosition().getLots();
        }
        if (hedgeManagement.getSellPosition()!=null
                && hedgeManagement.getSellPosition().getPositionStatus()==PositionStatus.OPENED){
             sellAlready = true;
            lots = hedgeManagement.getSellPosition().getLots();
        }
        if (buyAlready || sellAlready){
            //System.out.println("[ADDPOSITION] Es un hedge lots "+lots);
            hedged=true;
        }
        
        Position newPos = hedgeManagement.openPosition(pos.getPositionType(),pos.getPositionStatus()
                ,pos.getOpenPrice()
                ,leverage
                ,lots
                ,lotsPer1000
                ,pos.getSl(),pos.getTp(),hedged,cal);
        
        
        if (!hedged){ //pending order
            
        }
        
        
        pos.copy(newPos);
    }
    
    private void bBuyChangeSLActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bBuyChangeSLActionPerformed
        // TODO add your handling code here:
        if (gameState == GameState.STOPPED) return;
        
         while (runExe){
            try{
                Thread.sleep(100);
            }catch(Exception e){
                System.out.println("error: "+e.getMessage());
            }
        }
        gameTask.cancel();
        gameTimer.purge();
        actualSliderValue = slider.getValue();
        //Open new Position
        ModifyPositionDialog positionDialog = new ModifyPositionDialog(this,true);
        positionDialog.setConfig(Double.valueOf(PrintUtils.Print(chartPanel.getLastQuote().getClose()))
                ,hedgeManagement.getBuyPosition());
        positionDialog.setVisible(true);
        if (positionDialog.isOk()){
            //Guardamos posicion
            hedgeManagement.setBuyPosition(positionDialog.getPos());
            updatePosInterface(positionDialog.getPos(),-1);
        }
        //RESUMO JUEGO
        if (gameState == GameState.STARTED){
            int speed = Integer.valueOf(this.tickSpeed);
            gameTask = new MyTask(); 
            gameTimer.schedule(gameTask, speed, speed); 
        }
    }//GEN-LAST:event_bBuyChangeSLActionPerformed

    private void bCloseBuyPositionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bCloseBuyPositionActionPerformed
        // TODO add your handling code here:
        if (gameState == GameState.STOPPED) return;
        
       if (hedgeManagement.getBuyPosition().getPositionStatus()==PositionStatus.OPENED){
            Calendar calDate = Calendar.getInstance();
            calDate.setTime(chartPanel.getLastQuote().getDate());
            hedgeManagement.closePosition(hedgeManagement.getBuyPosition(), chartPanel.getLastQuote().getClose(),
                     false, calDate);
       }else if (hedgeManagement.getBuyPosition().getPositionStatus()==PositionStatus.PENDING){
           hedgeManagement.getBuyPosition().setPositionStatus(PositionStatus.CLOSED);
       }
    }//GEN-LAST:event_bCloseBuyPositionActionPerformed

    private void closeHalfLots(){
        if (gameState == GameState.STOPPED) return;
        hedgeManagement.closeHalf(chartPanel.getLastQuote().getClose());
        hedgeManagement.calculateStats(chartPanel.getLastQuote().getClose());
    }
    
    private void updateGeneralStats(){
        eBalance.setText(PrintUtils.Print2(hedgeManagement.getActualBalance()));
        eEquitity.setText(PrintUtils.Print2(hedgeManagement.getEquitity()));
        eMargin.setText(PrintUtils.Print2(hedgeManagement.getMargin()));
        eFreeMargin.setText(PrintUtils.Print2(hedgeManagement.getFreeMargin()));
        eMarginLevel.setText(PrintUtils.Print2(hedgeManagement.getMarginLevel()));
        eMaxEquitity.setText(PrintUtils.Print2(hedgeManagement.getMaxEquitity()));
        eTotalPips.setText(PrintUtils.Print2(hedgeManagement.getTotalPips()));
        try{
            double balance = Double.valueOf(eBalance.getText());
            double equitity = Double.valueOf(eEquitity.getText());
            double marginLevel = Double.valueOf(eMarginLevel.getText());
            if (equitity<balance && marginLevel<=20){
                JOptionPane.showMessageDialog(this,
                    "MarginLevel belows 20. Game over",
                    "error",
                    JOptionPane.ERROR_MESSAGE);
                stopSaveGame();
                return;
            }
            
             if (equitity<balance && marginLevel<=this.halfLots){
                JOptionPane.showMessageDialog(this,
                    "MarginLevel belows "+this.halfLots+". Closing half lots",
                    "Warning",
                    JOptionPane.WARNING_MESSAGE);
                closeHalfLots();
                return;
            } 
        }catch(Exception e){
            //System.out.println("Error: "+e.getMessage());
        }
    }
    
    public void cleanInterface(){
        lBuyStatus.setText("NONE"); 
        lBuyPrice.setText("NONE");
        lBuySL.setText("NONE");
        lBuyTP.setText("NONE");
        lBuyLots.setText("NONE");
        lBuyProfit.setForeground(Color.BLACK);
        lBuyProfit.setText("NONE");
        
        lSellStatus.setText("NONE"); 
        lSellPrice.setText("NONE");
        lSellSL.setText("NONE");
        lSellTP.setText("NONE");
        lSellLots.setText("NONE");
        lSellProfit.setForeground(Color.BLACK);
        lSellProfit.setText("NONE");
    }
    
    private void stopSaveGame(){
        gameTask.cancel();
        gameTimer.purge(); //
        gameState = GameState.STOPPED;
        cleanInterface();
    }
    
    private void bStartGameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bStartGameActionPerformed
        // TODO add your handling code here:
        //Nuevo juego
        Calendar gameDate = null;
        cleanInterface();
        int bePips       = Integer.valueOf(this.bePips);
        int hedgePips    = Integer.valueOf(this.hedgePips);
        double comm      = Double.valueOf(this.commissions);
        
        gameDate = hedgeManagement.initGame(200,bePips
                    ,true,hedgePips,halfLots
                    ,comm,this.logDir);
            updateGeneralStats();
    
        this.actualSliderValue=0;
        JOptionPane.showMessageDialog(this,
                    "NEW GAME : "+DateUtils.datePrint(gameDate.getTime()),
                    "info",
                    JOptionPane.INFORMATION_MESSAGE);
        
  
        this.gameState = GameState.STARTED;
              
    }//GEN-LAST:event_bStartGameActionPerformed

    
    
    private void bCloseSellPositionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bCloseSellPositionActionPerformed
        // TODO add your handling code here:
        if (gameState == GameState.STOPPED) return;
        
        if (hedgeManagement.getSellPosition().getPositionStatus()==PositionStatus.OPENED){
            Calendar calDate = Calendar.getInstance();
            calDate.setTime(chartPanel.getLastQuote().getDate());
            hedgeManagement.closePosition(hedgeManagement.getSellPosition(), chartPanel.getLastQuote().getClose(),
                     false, calDate);
       } else if (hedgeManagement.getSellPosition().getPositionStatus()==PositionStatus.PENDING){
           hedgeManagement.getSellPosition().setPositionStatus(PositionStatus.CLOSED);
       }
    }//GEN-LAST:event_bCloseSellPositionActionPerformed

    private void bPauseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bPauseActionPerformed
        // TODO add your handling code here:
        pauseGame();
    }//GEN-LAST:event_bPauseActionPerformed

    private void bResumeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bResumeActionPerformed
        // TODO add your handling code here:
        resumeGame();
    }//GEN-LAST:event_bResumeActionPerformed

    private void bSellChangeSLActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bSellChangeSLActionPerformed
        // TODO add your handling code here:
        
        if (gameState == GameState.STOPPED) return;
        
         while (runExe){
            try{
                Thread.sleep(100);
            }catch(Exception e){
                System.out.println("error: "+e.getMessage());
            }
        }
        gameTask.cancel();
        gameTimer.purge();
        actualSliderValue = slider.getValue();
        //Open new Position
        ModifyPositionDialog positionDialog = new ModifyPositionDialog(this,true);
        positionDialog.setConfig(Double.valueOf(PrintUtils.Print(chartPanel.getLastQuote().getClose()))
                ,hedgeManagement.getSellPosition());
        positionDialog.setVisible(true);
        if (positionDialog.isOk()){
            //Guardamos posicion
            hedgeManagement.setSellPosition(positionDialog.getPos());
            updatePosInterface(positionDialog.getPos(),-1);
        }
        //RESUMO JUEGO
        if (gameState == GameState.STARTED){
            gameTask = new MyTask(); 
            gameTimer.schedule(gameTask,this.tickSpeed,this.tickSpeed); 
        }
    }//GEN-LAST:event_bSellChangeSLActionPerformed

    private void bRewindActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bRewindActionPerformed
        // TODO add your handling code here:
        this.actualDayIndex -= Integer.valueOf(this.ffStep);
        this.absoluteIndex  -= Integer.valueOf(this.ffStep);
        renderChart();
    }//GEN-LAST:event_bRewindActionPerformed

    private void bForwardActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bForwardActionPerformed
        // TODO add your handling code here:
        int futureIndex = this.actualDayIndex+this.ffStep; 
        int newIndex = hedgeManagement.evaluatePositions(dataPaint,this.actualDayIndex,futureIndex);
        int diff = newIndex-this.actualDayIndex;
        this.actualDayIndex += Integer.valueOf(diff);
        this.absoluteIndex  += Integer.valueOf(diff);
       // this.actualDayIndex += Integer.valueOf(this.ffStep);
       // this.absoluteIndex  += Integer.valueOf(this.ffStep);
        renderChart();
    }//GEN-LAST:event_bForwardActionPerformed

    private void bSaveResultsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bSaveResultsActionPerformed
        // TODO add your handling code here:
        //salvamos el resultando en un XML
        hedgeManagement.saveResults(this.rootPath);
    }//GEN-LAST:event_bSaveResultsActionPerformed

    private void bStatsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bStatsActionPerformed
        // TODO add your handling code here:
        if (gameTask!=null){
            gameTask.cancel();
            gameTimer.purge();
            //pauso el juego
            while (runExe){
                try{
                    Thread.sleep(100);
                }catch(Exception e){
                    System.out.println("error: "+e.getMessage());
                }
            }
        }
      
        //Open new Position
        StatsPanel statsPanel = new StatsPanel(this,true,this.gameDir);  
        statsPanel.setVisible(true);
       
        //RESUMO JUEGO
        if (gameState == GameState.STARTED){
            gameTask = new MyTask(); 
            gameTimer.schedule(gameTask, this.tickSpeed, this.tickSpeed); 
        } 
    }//GEN-LAST:event_bStatsActionPerformed

    private void bNextDayActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bNextDayActionPerformed
        //iniciar un timer que cada x seconds llame a bAvance
        gameTask.cancel();
        gameTimer.purge();
        boolean res = newDay(false);
        if (res){
            gameTask = new MyTask(); 
            gameTimer.schedule(gameTask, 0, this.tickSpeed);
            gameState = GameState.STARTED;
        }
    }//GEN-LAST:event_bNextDayActionPerformed
 
    private void bLoadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bLoadActionPerformed
        // TODO add your handling code here:
        try{
            gameTask.cancel();
            gameTimer.purge();
            this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
            eLoadFile.setText("Loading, wait please...");
            boolean res = newDay(true); 
            if (res){
                JOptionPane.showMessageDialog(this,
                            "DATA LOADED",
                            "info",
                            JOptionPane.INFORMATION_MESSAGE);
                gameTask = new MyTask(); 
                gameTimer.schedule(gameTask, 0, this.tickSpeed);
                gameState   = GameState.STOPPED;
                chartState  = GameState.STARTED;

                this.b5mSelected.setSelected(true);
            }
        }finally{
            this.setCursor(Cursor.getDefaultCursor());
        }    
    }//GEN-LAST:event_bLoadActionPerformed

    private void bSetConfigActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bSetConfigActionPerformed
        pauseGame();
        this.setCurrentConfiguration();
        resumeGame();
    }//GEN-LAST:event_bSetConfigActionPerformed

    private void b5mSelectedActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_b5mSelectedActionPerformed
        // TODO add your handling code here:
        this.b1HourSelected.setSelected(false);
    }//GEN-LAST:event_b5mSelectedActionPerformed

    private void b1HourSelectedActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_b1HourSelectedActionPerformed
        // TODO add your handling code here:
        this.b5mSelected.setSelected(false);
    }//GEN-LAST:event_b1HourSelectedActionPerformed

    private void bZoomInActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bZoomInActionPerformed
        // TODO add your handling code here:
        if (this.actualZoom>4){
            this.actualZoom-=4;
        }
    }//GEN-LAST:event_bZoomInActionPerformed

    private void bZoomOutActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bZoomOutActionPerformed
        // TODO add your handling code here:
        if (this.actualZoom<120){
            this.actualZoom+=4;
        }
    }//GEN-LAST:event_bZoomOutActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        pauseGame();
        speedIndy = 500;
        resumeGame();
    }//GEN-LAST:event_jButton1ActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JToggleButton b1HourSelected;
    private javax.swing.JToggleButton b5mSelected;
    private javax.swing.JButton bBuyChangeSL;
    private javax.swing.JButton bCloseBuyPosition;
    private javax.swing.JButton bCloseSellPosition;
    private javax.swing.JButton bForward;
    private javax.swing.JButton bLoad;
    private javax.swing.JButton bNextDay;
    private javax.swing.JButton bPause;
    private javax.swing.JButton bResume;
    private javax.swing.JButton bRewind;
    private javax.swing.JButton bSaveResults;
    private javax.swing.JButton bSellChangeSL;
    private javax.swing.JButton bSetConfig;
    private javax.swing.JButton bStartGame;
    private javax.swing.JButton bStats;
    private javax.swing.JButton bZoomIn;
    private javax.swing.JButton bZoomOut;
    private javax.swing.JComboBox cbBePips;
    private javax.swing.JComboBox cbCommisions;
    private javax.swing.JComboBox cbFFStep;
    private javax.swing.JComboBox cbHalfLots;
    private javax.swing.JComboBox cbHedgePips;
    private javax.swing.JComboBox cbLeverage;
    private javax.swing.JComboBox cbLotsPer1000;
    private javax.swing.JComboBox cbTickSpeed;
    private javax.swing.JTextField eAvgPips;
    private javax.swing.JTextField eAvgWinPips;
    private javax.swing.JTextField eBalance;
    private javax.swing.JTextField eClosedTrades;
    private javax.swing.JTextField eEquitity;
    private javax.swing.JTextField eFreeMargin;
    private javax.swing.JTextField eLoadFile;
    private javax.swing.JTextField eMargin;
    private javax.swing.JTextField eMarginLevel;
    private javax.swing.JTextField eMaxEquitity;
    private javax.swing.JButton eNewPosition;
    private javax.swing.JTextField eTotalHedges;
    private javax.swing.JTextField eTotalPips;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JLabel lBuyLots;
    private javax.swing.JLabel lBuyPrice;
    private javax.swing.JLabel lBuyProfit;
    private javax.swing.JLabel lBuySL;
    private javax.swing.JLabel lBuyStatus;
    private javax.swing.JLabel lBuyTP;
    private javax.swing.JLabel lSellLots;
    private javax.swing.JLabel lSellPrice;
    private javax.swing.JLabel lSellProfit;
    private javax.swing.JLabel lSellSL;
    private javax.swing.JLabel lSellStatus;
    private javax.swing.JLabel lSellTP;
    private javax.swing.JSlider slider;
    // End of variables declaration//GEN-END:variables
    
    class MyTask extends TimerTask {
        
        public void run() {
            //System.out.println("Actual Index : "+actualDayIndex);
            //compruebo si es nuevo dia
            /*System.out.println("actualDayIndex data PAint: "+actualDayIndex
                    +" "+(dataPaint.size()-1));*/
            if (actualDayIndex>=dataPaint.size()-1){
                newDay(false);
                return;
            }   
            //renderizo el chart
            renderChart();
        
            if (actualDayIndex<0) return;
            lastQuote = dataPaint.get(actualDayIndex);
            Quote actualQuote = dataPaint.get(actualDayIndex);
            double actualValue = dataPaint.get(actualDayIndex).getClose();
            
            //actualizamos posiciones
            hedgeManagement.update(actualQuote);
            double percent = hedgeManagement.getTotalHedges()*100.0/hedgeManagement.getClosePositions().size();
            eTotalHedges.setText(String.valueOf(hedgeManagement.getTotalHedges())
                    +" ("+PrintUtils.Print(percent)+"%)");
            //test buy
            Position pos = hedgeManagement.getBuyPosition();
            updatePosInterface(pos,actualValue);            
            //test sell
            pos = hedgeManagement.getSellPosition();
            updatePosInterface(pos,actualValue);
            //total closeTrades
            eClosedTrades.setText(String.valueOf(hedgeManagement.getClosePositions().size()));
            //avgPips
            eAvgPips.setText(PrintUtils.Print2(hedgeManagement.getTotalProfit()/hedgeManagement.getClosePositions().size()));
            //avgWinPips
            double avgWinPips = hedgeManagement.getTotalWinProfit()*1.0/hedgeManagement.getWins();
            eAvgWinPips.setText(PrintUtils.Print2(avgWinPips));
            updateGeneralStats();
            
            actualDayIndex += speedIndy;
            absoluteIndex  += speedIndy;
        }
    }
}
