/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import javax.swing.JTextField;

/**
 *
 * @author david
 */
public class ConfigDialog extends javax.swing.JDialog {

    private boolean gameMode = true;
    private int barInterval = 3;
    private int bePips= 4;
    private int hedgePips=10;
    private boolean hedgeEnabled=true;
    private double brokerCommision=0.75;
    
    /**
     * Creates new form ConfigDialog
     */
    public ConfigDialog(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        chGameMode = new javax.swing.JCheckBox();
        bOK = new javax.swing.JButton();
        bCancel = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        eInterval = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        eBrokerCommision = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        eHedgePips = new javax.swing.JTextField();
        chHedgeEnabled = new javax.swing.JCheckBox();
        jLabel4 = new javax.swing.JLabel();
        eBEPips = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        chGameMode.setSelected(true);
        chGameMode.setText("GameMode");

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

        jLabel1.setText("Speed (ms):  ");

        eInterval.setText("500");

        jLabel2.setText("BE+1 pips:");

        eBrokerCommision.setText("1.05");

        jLabel3.setText("HedgeManagement");

        eHedgePips.setText("10");

        chHedgeEnabled.setSelected(true);
        chHedgeEnabled.setText("Enabled");

        jLabel4.setText("BrokerCommision");

        eBEPips.setText("4");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(bOK)
                .addGap(18, 18, 18)
                .addComponent(bCancel)
                .addContainerGap())
            .addGroup(layout.createSequentialGroup()
                .addGap(23, 23, 23)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(chGameMode)
                    .addComponent(jLabel1)
                    .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel4, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(eBrokerCommision, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(eHedgePips, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(eInterval, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(chHedgeEnabled))
                    .addComponent(eBEPips, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(40, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addComponent(chGameMode)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(eInterval, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(eBEPips, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(eHedgePips, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(chHedgeEnabled))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(eBrokerCommision, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 39, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(bCancel)
                    .addComponent(bOK))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void bOKActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bOKActionPerformed
        // TODO add your handling code here:
        if (chGameMode.isSelected()){
            gameMode =true;
        }else{
            gameMode=false;
        }
        barInterval = Integer.valueOf(eInterval.getText());
        bePips = Integer.valueOf(eBEPips.getText()); 
        if (chHedgeEnabled.isSelected()){
            this.hedgeEnabled = true;
        }else{
            this.hedgeEnabled = false;
        }
        this.hedgePips = Integer.valueOf(eHedgePips.getText());
        this.brokerCommision = Double.valueOf(eBrokerCommision.getText());
        this.setVisible(false);
    }//GEN-LAST:event_bOKActionPerformed

    private void bCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bCancelActionPerformed
        // TODO add your handling code here:
        this.setVisible(false);
    }//GEN-LAST:event_bCancelActionPerformed

    public boolean isGameMode() {
        return this.gameMode;
    }

    public int getBarInterval() {
        return barInterval;
    }

    public void setBarInterval(int barInterval) {
        this.barInterval = barInterval;
    }

    public int getBePips() {
        return bePips;
    }

    public void setBePips(int bePips) {
        this.bePips = bePips;
    }

    public int getHedgePips() {
        return hedgePips;
    }

    public void setHedgePips(int hedgePips) {
        this.hedgePips = hedgePips;
    }

    public boolean isHedgeEnabled() {
        return hedgeEnabled;
    }

    public void setHedgeEnabled(boolean hedgeEnabled) {
        this.hedgeEnabled = hedgeEnabled;
    }

    public double getBrokerCommision() {
        return brokerCommision;
    }

    public void setBrokerCommision(double brokerCommision) {
        this.brokerCommision = brokerCommision;
    }

    
    
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton bCancel;
    private javax.swing.JButton bOK;
    private javax.swing.JCheckBox chGameMode;
    private javax.swing.JCheckBox chHedgeEnabled;
    private javax.swing.JTextField eBEPips;
    private javax.swing.JTextField eBrokerCommision;
    private javax.swing.JTextField eHedgePips;
    private javax.swing.JTextField eInterval;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    // End of variables declaration//GEN-END:variables
}
