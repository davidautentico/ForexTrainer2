/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import java.io.File;
import javax.swing.JFileChooser;

/**
 *
 * @author drosa
 */
public class LoadDataDialog extends javax.swing.JDialog {
    
    private boolean ok = false;
    private String fxDataPath;
    private String baseFileName;
    
    /**
     * Creates new form LoadDataDialog
     */
    public LoadDataDialog(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
    }

    public String getFxDataPath() {
        return fxDataPath;
    }

    public void setFxDataPath(String fxDataPath) {
        this.fxDataPath = fxDataPath;
    }

    public String getBaseFileName() {
        return baseFileName;
    }

    public void setBaseFileName(String baseFileName) {
        this.baseFileName = baseFileName;
    }

    public boolean isOk() {
        return ok;
    }

    public void setOk(boolean ok) {
        this.ok = ok;
    }
    
    

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        bOk = new javax.swing.JButton();
        bCancel = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        eFxDataPath = new javax.swing.JTextField();
        eBaseFile = new javax.swing.JTextField();
        bFXPathSearch = new javax.swing.JButton();
        bBaseFileSearch = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jLabel1.setText("FX DATA PATH:");

        bOk.setText("OK");
        bOk.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bOkActionPerformed(evt);
            }
        });

        bCancel.setText("Cancel");
        bCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bCancelActionPerformed(evt);
            }
        });

        jLabel2.setText("Base File (5min):");

        eFxDataPath.setText("c:\\fxdata");

        eBaseFile.setText("EURUSD_5 Mins_Bid_2008.01.01_2013.07.31.csv");

        bFXPathSearch.setText("Search");
        bFXPathSearch.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bFXPathSearchActionPerformed(evt);
            }
        });

        bBaseFileSearch.setText("Search");
        bBaseFileSearch.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bBaseFileSearchActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(bOk)
                .addGap(18, 18, 18)
                .addComponent(bCancel)
                .addGap(16, 16, 16))
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel2)
                    .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, 87, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(eFxDataPath)
                    .addComponent(eBaseFile, javax.swing.GroupLayout.DEFAULT_SIZE, 243, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(bFXPathSearch)
                    .addComponent(bBaseFileSearch))
                .addContainerGap(18, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(eFxDataPath, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(bFXPathSearch))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(eBaseFile, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(bBaseFileSearch))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 15, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(bOk)
                    .addComponent(bCancel))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void bOkActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bOkActionPerformed
        // TODO add your handling code here:
        this.ok = true;
        this.setVisible(false);
        this.baseFileName = eBaseFile.getText();
        this.fxDataPath = eFxDataPath.getText();
    }//GEN-LAST:event_bOkActionPerformed

    private void bCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bCancelActionPerformed
        // TODO add your handling code here:
        this.setVisible(false);
    }//GEN-LAST:event_bCancelActionPerformed

    private void bFXPathSearchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bFXPathSearchActionPerformed
        // TODO add your handling code here:
        JFileChooser chooser = new JFileChooser();              
        chooser.setCurrentDirectory(new File("C:\\Users\\drosa\\Personal\\trading\\fxdata"));
        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        //chooser.setCurrentDirectory(new File(workingdir));
        chooser.showOpenDialog(null);
        File f = chooser.getSelectedFile();
        if (f!=null){                        
            eFxDataPath.setText(f.getAbsolutePath());
            this.fxDataPath= f.getAbsolutePath();
        }
    }//GEN-LAST:event_bFXPathSearchActionPerformed

    private void bBaseFileSearchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bBaseFileSearchActionPerformed
        // TODO add your handling code here:
        JFileChooser chooser = new JFileChooser();       
        chooser.setCurrentDirectory(new File("C:\\fxdata"));
        //chooser.setCurrentDirectory(new File(workingdir));
        chooser.showOpenDialog(null);
        File f = chooser.getSelectedFile();
        if (f!=null){                        
            eBaseFile.setText(f.getName());
            this.baseFileName= f.getName();
        }
    }//GEN-LAST:event_bBaseFileSearchActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton bBaseFileSearch;
    private javax.swing.JButton bCancel;
    private javax.swing.JButton bFXPathSearch;
    private javax.swing.JButton bOk;
    private javax.swing.JTextField eBaseFile;
    private javax.swing.JTextField eFxDataPath;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    // End of variables declaration//GEN-END:variables
}
