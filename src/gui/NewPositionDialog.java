/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import Trading.HedgeManagement;
import Trading.Position;
import Trading.PositionStatus;
import Trading.PositionType;
import javax.swing.JOptionPane;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;


/**
 *
 * @author david
 */
public class NewPositionDialog extends javax.swing.JDialog {

    double actualPrice=0;
    boolean ok = false;
    Position marketPos = null;
    Position pendingPos = null;
    private boolean buyAlready;
    private boolean sellAlready;
    
    
    /**
     * Creates new form NewPositionDialog
     */
    public NewPositionDialog(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        initValues();
    }

    public void setActualPrice(double actualPrice){
        this.lActualPrice.setText(String.valueOf(actualPrice));
        this.actualPrice = actualPrice;
        this.eMarketOrderPrice.setText(String.valueOf(actualPrice));
        this.sPendingOrderPrice.setValue(actualPrice);
    }
    
    public void initValues(){       
        ok = false;  
        //spinners
        JSpinner.NumberEditor editor = new JSpinner.NumberEditor(sPendingOrderPrice, "0.0000");
        sPendingOrderPrice.setEditor(editor);
        SpinnerNumberModel model = (SpinnerNumberModel) sPendingOrderPrice.getModel();
        model.setStepSize(0.0001);
        sPendingOrderPrice.setValue(0.0000);
        
        editor = new JSpinner.NumberEditor(sPendingOrderSL, "0.0000");
        sPendingOrderSL.setEditor(editor);
        model = (SpinnerNumberModel) sPendingOrderSL.getModel();
        model.setStepSize(0.0001);
        sPendingOrderSL.setModel(model);
        sPendingOrderSL.setValue(0.0000);
        
        editor = new JSpinner.NumberEditor(sPendingOrderTP, "0.0000");
        sPendingOrderTP.setEditor(editor);
        model = (SpinnerNumberModel) sPendingOrderTP.getModel();
        model.setStepSize(0.0001);
        sPendingOrderTP.setModel(model);
        sPendingOrderTP.setValue(0.0000);
        
        editor = new JSpinner.NumberEditor(sMarketOrderSL, "0.0000");
        sMarketOrderSL.setEditor(editor);
        model = (SpinnerNumberModel) sMarketOrderSL.getModel();
        model.setStepSize(0.0001);
        sMarketOrderSL.setModel(model);
        sMarketOrderSL.setValue(0.0000);
        
        editor = new JSpinner.NumberEditor(sMarketOrderTP, "0.0000");
        sMarketOrderTP.setEditor(editor);
        model = (SpinnerNumberModel) sMarketOrderTP.getModel();
        model.setStepSize(0.0001);
        sMarketOrderTP.setModel(model);
        sMarketOrderTP.setValue(0.0000);
    }
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        bOK = new javax.swing.JButton();
        bCancel = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        cbMarketOrderType = new javax.swing.JComboBox();
        cbPendingOrderType = new javax.swing.JComboBox();
        jLabel3 = new javax.swing.JLabel();
        lActualPrice = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        cMarketOrderEnabled = new javax.swing.JCheckBox();
        cPendingOrderEnabled = new javax.swing.JCheckBox();
        sPendingOrderPrice = new javax.swing.JSpinner();
        sMarketOrderSL = new javax.swing.JSpinner();
        sMarketOrderTP = new javax.swing.JSpinner();
        sPendingOrderSL = new javax.swing.JSpinner();
        sPendingOrderTP = new javax.swing.JSpinner();
        eMarketOrderPrice = new javax.swing.JTextField();
        bMarketSl = new javax.swing.JButton();
        bPendingSL = new javax.swing.JButton();
        bPendingTp = new javax.swing.JButton();
        bMarketTp = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setModal(true);

        bOK.setText("OK");
        bOK.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bOKActionPerformed(evt);
            }
        });

        bCancel.setText("Cancel");
        bCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bCancelActionPerformed(evt);
            }
        });

        jLabel1.setText("MARKET ORDER");

        jLabel2.setText("PENDING ORDER");

        cbMarketOrderType.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "BUY", "SELL" }));

        cbPendingOrderType.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "BUY", "SELL" }));

        jLabel3.setText("ACTUAL PRICE:");

        jLabel5.setText("Price");

        jLabel6.setText("SL");

        jLabel7.setText("TP");

        cMarketOrderEnabled.setText("ENABLED");

        cPendingOrderEnabled.setText("ENABLED");

        sPendingOrderPrice.setModel(new javax.swing.SpinnerNumberModel(Double.valueOf(0.0d), null, null, Double.valueOf(1.0d)));

        sMarketOrderSL.setModel(new javax.swing.SpinnerNumberModel(Double.valueOf(0.0d), null, null, Double.valueOf(1.0d)));

        sMarketOrderTP.setModel(new javax.swing.SpinnerNumberModel(Double.valueOf(0.0d), null, null, Double.valueOf(1.0d)));

        sPendingOrderSL.setModel(new javax.swing.SpinnerNumberModel(Double.valueOf(0.0d), null, null, Double.valueOf(1.0d)));

        sPendingOrderTP.setModel(new javax.swing.SpinnerNumberModel(Double.valueOf(0.0d), null, null, Double.valueOf(1.0d)));

        eMarketOrderPrice.setEditable(false);
        eMarketOrderPrice.setText("0");

        bMarketSl.setText("actual");
        bMarketSl.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bMarketSlActionPerformed(evt);
            }
        });

        bPendingSL.setText("actual");
        bPendingSL.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bPendingSLActionPerformed(evt);
            }
        });

        bPendingTp.setText("actual");
        bPendingTp.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bPendingTpActionPerformed(evt);
            }
        });

        bMarketTp.setText("actual");
        bMarketTp.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bMarketTpActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(jLabel1)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jLabel5))
                                    .addComponent(jLabel2))
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addGap(0, 0, Short.MAX_VALUE)
                                .addComponent(bOK)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(bCancel)))
                        .addContainerGap())
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(cbMarketOrderType, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(eMarketOrderPrice)
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(bMarketSl, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(sMarketOrderSL))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(bMarketTp, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(sMarketOrderTP))
                        .addGap(12, 12, 12)
                        .addComponent(cMarketOrderEnabled)
                        .addGap(30, 30, 30))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(cbPendingOrderType, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(sPendingOrderPrice, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(bPendingSL, javax.swing.GroupLayout.DEFAULT_SIZE, 63, Short.MAX_VALUE)
                            .addComponent(sPendingOrderSL))
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(bPendingTp)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addGroup(layout.createSequentialGroup()
                                .addGap(18, 18, 18)
                                .addComponent(sPendingOrderTP, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 15, Short.MAX_VALUE)))
                        .addComponent(cPendingOrderEnabled)
                        .addGap(20, 20, 20))))
            .addGroup(layout.createSequentialGroup()
                .addGap(82, 82, 82)
                .addComponent(jLabel3)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(20, 20, 20)
                        .addComponent(jLabel6)
                        .addGap(46, 46, 46)
                        .addComponent(jLabel7))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addComponent(lActualPrice, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel3)
                    .addComponent(lActualPrice, javax.swing.GroupLayout.PREFERRED_SIZE, 15, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jLabel5)
                    .addComponent(jLabel6)
                    .addComponent(jLabel7))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cbMarketOrderType, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cMarketOrderEnabled)
                    .addComponent(sMarketOrderSL, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(sMarketOrderTP, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(eMarketOrderPrice, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(bMarketSl, javax.swing.GroupLayout.PREFERRED_SIZE, 12, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(bMarketTp, javax.swing.GroupLayout.PREFERRED_SIZE, 12, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(27, 27, 27)
                .addComponent(jLabel2)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(cbPendingOrderType, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(cPendingOrderEnabled))
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(sPendingOrderSL, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(sPendingOrderPrice, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(sPendingOrderTP, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(18, 18, 18)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(bPendingSL, javax.swing.GroupLayout.PREFERRED_SIZE, 12, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(bPendingTp, javax.swing.GroupLayout.PREFERRED_SIZE, 12, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addGap(19, 48, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(bOK)
                            .addComponent(bCancel))
                        .addContainerGap())))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void bCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bCancelActionPerformed
        // TODO add your handling code here:
        this.ok = false;
        this.setVisible(false);        
    }//GEN-LAST:event_bCancelActionPerformed

    private void bOKActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bOKActionPerformed
        // TODO add your handling code here:
        //Analizar las que están enabled
        //Análisis market order
        marketPos=null;
        pendingPos=null;
        this.ok = false;
        if (cMarketOrderEnabled.isSelected() && cPendingOrderEnabled.isSelected()){
            if (cbMarketOrderType.getSelectedIndex()==cbPendingOrderType.getSelectedIndex()){
                //lanzar error
                JOptionPane.showMessageDialog(this,
                    "market and pending orders can not be the same type",
                    "error",
                    JOptionPane.ERROR_MESSAGE);
                return;
            }
        }
        
        if (cMarketOrderEnabled.isSelected()){            
            double sl = (double)sMarketOrderSL.getValue();
            double tp = (double)sMarketOrderTP.getValue();            
            int buySell = cbMarketOrderType.getSelectedIndex();
            if (buySell==0 && buyAlready){
                JOptionPane.showMessageDialog(this,
                    "A buy order already exists",
                    "error",
                    JOptionPane.ERROR_MESSAGE);
                    return;
            }
            if (buySell==1 && sellAlready){
                JOptionPane.showMessageDialog(this,
                    "A sell order already exists",
                    "error",
                    JOptionPane.ERROR_MESSAGE);
                    return;
            }
            if (buySell==0){
                if (sl>0 && HedgeManagement.getPipsDiff(actualPrice,sl)<=1){
                    //lanzar error
                    JOptionPane.showMessageDialog(this,
                    "SL must be at least 2 pips",
                    "error",
                    JOptionPane.ERROR_MESSAGE);
                    return;
                }
                if (tp>0 && HedgeManagement.getPipsDiff(tp,actualPrice)<=1){
                    //lanzar error
                    JOptionPane.showMessageDialog(this,
                    "TP must be at least 2 pips",
                    "error",
                    JOptionPane.ERROR_MESSAGE);
                    return;                  
                }
            }else{
                 
                 if (sl>0 && HedgeManagement.getPipsDiff(sl,actualPrice)<=1){
                    //lanzar error
                     JOptionPane.showMessageDialog(this,
                    "SL must be at least 2 pips",
                    "error",
                    JOptionPane.ERROR_MESSAGE);
                    return;                   
                }
                if (tp>0 && HedgeManagement.getPipsDiff(actualPrice,tp)<=1){
                    //lanzar error
                    JOptionPane.showMessageDialog(this,
                    "TP must be at least 2 pips",
                    "error",
                    JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }
            //no hay más errores crear posición
            this.marketPos = new Position();
            marketPos.setPositionStatus(PositionStatus.OPENED);
            marketPos.setOpenPrice(actualPrice);
            if (buySell==0)
                marketPos.setPositionType(PositionType.BUY);
            if (buySell==1)
                marketPos.setPositionType(PositionType.SELL);
            if (tp>0)
                marketPos.setTp(tp);
            if (sl>0)
                marketPos.setSl(sl);         
        }
        
        //Análisis pendingOrder
        if (cPendingOrderEnabled.isSelected()){
            double pendingPrice = (double)sPendingOrderPrice.getValue(); 
            double sl = (double)sPendingOrderSL.getValue();
            double tp = (double)sPendingOrderTP.getValue();            
            int buySell = cbPendingOrderType.getSelectedIndex();
            if (buySell==0){
                if (pendingPrice-actualPrice<0.0002){
                    //lanzar error
                    JOptionPane.showMessageDialog(this,
                    "Price must be at least 2 pips away",
                    "error",
                    JOptionPane.ERROR_MESSAGE);
                    return;
                }
                if (sl>0 && actualPrice-sl<0.0002){
                    //lanzar error
                    JOptionPane.showMessageDialog(this,
                    "SL must be at least 2 pips",
                    "error",
                    JOptionPane.ERROR_MESSAGE);
                    return;
                }
                if (tp>0 && tp-actualPrice<0.0002){
                    //lanzar error
                    JOptionPane.showMessageDialog(this,
                    "TP must be at least 2 pips",
                    "error",
                    JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }else{
                 if (actualPrice-pendingPrice<0.0002){
                    //lanzar error
                     JOptionPane.showMessageDialog(this,
                    "Price must be at least 2 pips away",
                    "error",
                    JOptionPane.ERROR_MESSAGE);
                    return;
                }
                if (sl>0 && sl-actualPrice<0.0002){
                    //lanzar error
                    JOptionPane.showMessageDialog(this,
                    "SL must be at least 2 pips",
                    "error",
                    JOptionPane.ERROR_MESSAGE);
                     return;
                }
                if (tp>0 && actualPrice-tp<0.0002){
                    //lanzar error
                    JOptionPane.showMessageDialog(this,
                    "TP must be at least 2 pips",
                    "error",
                    JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }
            //no hay más errores crear posición   
            this.pendingPos = new Position();
            pendingPos.setPositionStatus(PositionStatus.PENDING);
            pendingPos.setOpenPrice(pendingPrice);
            if (buySell==0)
                pendingPos.setPositionType(PositionType.BUY);
            if (buySell==1)
                pendingPos.setPositionType(PositionType.SELL);
            if (tp>0)
                pendingPos.setTp(tp);
            if (sl>0)
                pendingPos.setSl(sl);
        }
        
        this.ok = true;
        this.setVisible(false);
    }//GEN-LAST:event_bOKActionPerformed

    private void bMarketSlActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bMarketSlActionPerformed
        // TODO add your handling code here:
        this.sMarketOrderSL.setValue(this.actualPrice);
    }//GEN-LAST:event_bMarketSlActionPerformed

    private void bMarketTpActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bMarketTpActionPerformed
        // TODO add your handling code here:
        this.sMarketOrderTP.setValue(this.actualPrice);
    }//GEN-LAST:event_bMarketTpActionPerformed

    private void bPendingSLActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bPendingSLActionPerformed
        // TODO add your handling code here:
        this.sPendingOrderSL.setValue(this.actualPrice);
    }//GEN-LAST:event_bPendingSLActionPerformed

    private void bPendingTpActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bPendingTpActionPerformed
        // TODO add your handling code here:
        this.sMarketOrderTP.setValue(this.actualPrice);
    }//GEN-LAST:event_bPendingTpActionPerformed

    public boolean isOk() {
        return ok;
    }

    public void setOk(boolean ok) {
        this.ok = ok;
    }

    public Position getMarketPos() {
        return marketPos;
    }

    public void setMarketPos(Position marketPos) {
        this.marketPos = marketPos;
    }

    public Position getPendingPos() {
        return pendingPos;
    }

    public void setPendingPos(Position pendingPos) {
        this.pendingPos = pendingPos;
    }

    
    
    
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton bCancel;
    private javax.swing.JButton bMarketSl;
    private javax.swing.JButton bMarketTp;
    private javax.swing.JButton bOK;
    private javax.swing.JButton bPendingSL;
    private javax.swing.JButton bPendingTp;
    private javax.swing.JCheckBox cMarketOrderEnabled;
    private javax.swing.JCheckBox cPendingOrderEnabled;
    private javax.swing.JComboBox cbMarketOrderType;
    private javax.swing.JComboBox cbPendingOrderType;
    private javax.swing.JTextField eMarketOrderPrice;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel lActualPrice;
    private javax.swing.JSpinner sMarketOrderSL;
    private javax.swing.JSpinner sMarketOrderTP;
    private javax.swing.JSpinner sPendingOrderPrice;
    private javax.swing.JSpinner sPendingOrderSL;
    private javax.swing.JSpinner sPendingOrderTP;
    // End of variables declaration//GEN-END:variables

    void setBuySellOpened(boolean buyAlready, boolean sellAlready) {
        this.buyAlready = buyAlready;
        this.sellAlready = sellAlready;
    }
}