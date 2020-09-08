/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import Trading.DateUtils;
import Trading.TestResults;
import java.awt.Color;
import java.awt.Dimension;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import javax.swing.BorderFactory;
import javax.xml.parsers.*;
import org.xml.sax.*;
import org.w3c.dom.*;
import utils.PrintUtils;

/**
 *
 * @author drosa
 */
public class StatsPanel extends javax.swing.JDialog {

    int totalTests = 0;
    double maxEq = 0;
    double maxEqAvg = 0;
    EquitityChart eqChart = null;
    ArrayList<TestResults> tests = null;
    String currentDirectory ="";
    
    /**
     * Creates new form StatsPanel
     */
    public StatsPanel(java.awt.Frame parent, boolean modal,String currentDir) {
        super(parent, modal);
        initComponents();
        this.currentDirectory = currentDir;
        loadData();
        initPanels();                
        
    }

    public String getCurrentDirectory() {
        return currentDirectory;
    }

    public void setCurrentDirectory(String currentDirectory) {
       // System.out.println("currentDir: "+currentDirectory);
        this.currentDirectory = currentDirectory;
    }
            
    private void initPanels(){
        System.out.println("initpanels: "+maxEq);
        eqChart = new EquitityChart(maxEq);
        eqChart.setTests(tests);
        eqChart.setPreferredSize(new Dimension(50,250));
        eqChart.setBorder(BorderFactory.createLineBorder (Color.blue, 2));
        eqChart.setBackground(Color.white);
        getContentPane().add(eqChart, java.awt.BorderLayout.CENTER);
        pack();
    }
    
    private String getExtension(String fileName){
        String extension = "";

        int i = fileName.lastIndexOf('.');
        if (i > 0) 
            extension = fileName.substring(i+1);
        return extension;
    }
    
    private String getTextValue(String def, Element doc, String tag) {
        String value = def;
        NodeList nl;
        nl = doc.getElementsByTagName(tag);
        if (nl.getLength() > 0 && nl.item(0).hasChildNodes()) {
            value = nl.item(0).getFirstChild().getNodeValue();
        }
        return value.trim();
    }
    
    public TestResults readXML(String fileName) {
        
        TestResults testResult = null;
        Document dom;
        // Make an  instance of the DocumentBuilderFactory
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        try {
            // use the factory to take an instance of the document builder
            DocumentBuilder db = dbf.newDocumentBuilder();
            // parse using the builder to get the DOM mapping of the    
            // XML file
            dom = db.parse(fileName);

            Element doc = dom.getDocumentElement();
            String totalTrades="";
            String maxEq="";
            
            totalTrades = getTextValue(totalTrades, doc, "TotalTrades");
            if (totalTrades != null) {
                if (!totalTrades.isEmpty()){                  
                    testResult = new TestResults();
                    testResult.setTotalTests(Double.valueOf(totalTrades).intValue());
                }
            }
            maxEq = getTextValue(totalTrades, doc, "MaxEquitity");
            if (maxEq != null) {
                if (!maxEq.isEmpty()){    
                    
                    testResult.setMaxEquitity(Double.valueOf(maxEq));
                }
            }                    
        } catch (ParserConfigurationException pce) {
            System.out.println(pce.getMessage());
        } catch (SAXException se) {
            System.out.println(se.getMessage());
        } catch (IOException ioe) {
            System.err.println(ioe.getMessage());
        }
        return  testResult;
    }
    
    private double calculateAvg(ArrayList<TestResults> tests){
        double avg = 0;
        int begin = tests.size()-1;
        int end = tests.size()-10;
        
        if (end<0) end =0;
        
        for (int i=begin; i>= end;i--){
            avg += tests.get(i).getMaxEquitity();
        }
        
        return avg/(begin-end+1);
    }
    
    private void addTestByDate(TestResults test,Calendar cal){
        
        for (int i=0;i<tests.size();i++){
            if (tests.get(i).getCal().getTimeInMillis()>cal.getTimeInMillis()){
                tests.add(i, test);
                return;
            }
        }
        tests.add(test);
    }
            
    private void loadData(){
        String workingDir = this.currentDirectory;
        File dir = new File(workingDir);        
        File[] files = dir.listFiles();
        int total=0;
        double maxEqAvg = 0;
        double maxEq=-9999;
        tests = new ArrayList<TestResults>();
        
        System.out.println("stats dir: "+workingDir);
        
        if (files==null) return;
        
        for (int i=0;i<files.length;i++){
            File file = files[i];
            if (file.isFile()){
                               
                String fileName = file.getName();
                String ext = getExtension(file.getName());
                
                int idx = fileName.indexOf('_');
                if (ext.equalsIgnoreCase("xml") && idx!=-1){
                    Calendar cal = Calendar.getInstance();
                   cal.setTimeInMillis(file.lastModified());
                    System.out.println("name ext: "+file.getAbsolutePath()
                            +" "+ext+" "+DateUtils.datePrint(cal.getTime()));
                    total++;
                    TestResults res = this.readXML(file.getAbsolutePath());
                    res.setCal(cal);
                    if (maxEq<res.getMaxEquitity())
                        maxEq = res.getMaxEquitity();
                    if (res!=null){
                        //tests.add(res);
                        addTestByDate(res,cal);
                    }
                }
            }                     
        }
        this.eTotalTests.setText(String.valueOf(total));
        this.eMaxEqAvg.setText(PrintUtils.Print2(calculateAvg(tests)));   
        this.eMaxEq.setText(PrintUtils.Print2(maxEq));  
        this.maxEq = maxEq;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel2 = new javax.swing.JPanel();
        eTotalTests = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        eMaxEq = new javax.swing.JTextField();
        eMaxEqAvg = new javax.swing.JTextField();
        bClose = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Games");

        eTotalTests.setText("0");
        eTotalTests.setEnabled(false);

        jLabel1.setText("Total tests:");

        jLabel2.setText("Max Equitity:");

        jLabel3.setText("MaxEqAvg (10 tests):");

        eMaxEq.setText("0");
        eMaxEq.setEnabled(false);

        eMaxEqAvg.setEditable(false);
        eMaxEqAvg.setText("0");

        bClose.setText("Close");
        bClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bCloseActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1)
                    .addComponent(jLabel2)
                    .addComponent(jLabel3))
                .addGap(47, 47, 47)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(eTotalTests, javax.swing.GroupLayout.DEFAULT_SIZE, 65, Short.MAX_VALUE)
                    .addComponent(eMaxEq)
                    .addComponent(eMaxEqAvg))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 104, Short.MAX_VALUE)
                .addComponent(bClose)
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(eTotalTests, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1)
                    .addComponent(bClose))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(eMaxEq, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(eMaxEqAvg, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(14, Short.MAX_VALUE))
        );

        getContentPane().add(jPanel2, java.awt.BorderLayout.PAGE_START);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void bCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bCloseActionPerformed
        // TODO add your handling code here:
        this.setVisible(false);
    }//GEN-LAST:event_bCloseActionPerformed

   
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton bClose;
    private javax.swing.JTextField eMaxEq;
    private javax.swing.JTextField eMaxEqAvg;
    private javax.swing.JTextField eTotalTests;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JPanel jPanel2;
    // End of variables declaration//GEN-END:variables
}
