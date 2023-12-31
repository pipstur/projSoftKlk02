/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package pkg0203server;

import domain.Poruka;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import server.Server;

/**
 *
 * @author vojislav
 */
public class FrmServer extends javax.swing.JFrame {

    /**
     * Creates new form FrmServer
     */
    Server server;
    public FrmServer() {
        initComponents();
        btnIskljuci.setEnabled(false);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        btnUkljuci = new javax.swing.JButton();
        btnIskljuci = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        txtChat = new javax.swing.JTextArea();
        btnPrikaziChat = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        btnUkljuci.setText("Ukljuci");
        btnUkljuci.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnUkljuciActionPerformed(evt);
            }
        });

        btnIskljuci.setText("Iskljuci");
        btnIskljuci.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnIskljuciActionPerformed(evt);
            }
        });

        jLabel1.setText("Ne radi");

        txtChat.setColumns(20);
        txtChat.setRows(5);
        jScrollPane1.setViewportView(txtChat);

        btnPrikaziChat.setText("Prikazi chat");
        btnPrikaziChat.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPrikaziChatActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 364, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(btnPrikaziChat)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addComponent(jLabel1)
                                .addGap(32, 32, 32)))
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(btnIskljuci, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(btnUkljuci, javax.swing.GroupLayout.Alignment.TRAILING))))
                .addContainerGap(14, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel1)
                    .addComponent(btnUkljuci))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnPrikaziChat)
                    .addComponent(btnIskljuci))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 205, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(13, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnIskljuciActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnIskljuciActionPerformed
        // stopServer je custom metoda koja radi svasta samo pogledajte klasu Server
        if(!server.isInterrupted()){
            try {
                server.stopServer();
                jLabel1.setText("Ne radi");
                btnIskljuci.setEnabled(false);
                btnUkljuci.setEnabled(true);
            } catch (IOException ex) {
                Logger.getLogger(FrmServer.class.getName()).log(Level.SEVERE, null, ex);
            }
            
        }
    }//GEN-LAST:event_btnIskljuciActionPerformed

    private void btnUkljuciActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnUkljuciActionPerformed
        // server je thread i ne zapocinje se na pocetku vec kad se klikne dugme
        if(server==null||server.isInterrupted()){
            server = new Server();
            server.start();
            jLabel1.setText("Radi");
            btnIskljuci.setEnabled(true);
            btnUkljuci.setEnabled(false);
        }
    }//GEN-LAST:event_btnUkljuciActionPerformed

    private void btnPrikaziChatActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPrikaziChatActionPerformed
        // Samo vraca sve poruke i tjt bukv prejednostavno ez 5 poena
        if(server!=null&&!server.isInterrupted()){
            List<Poruka> poruke = server.getPoruke();
            if(!poruke.isEmpty()){
               for(Poruka poruka : poruke){
                txtChat.setText(txtChat.getText()+"\n"+poruka);
                } 
            }
            
            
        }
    }//GEN-LAST:event_btnPrikaziChatActionPerformed

    /**
     * @param args the command line arguments
     */

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnIskljuci;
    private javax.swing.JButton btnPrikaziChat;
    private javax.swing.JButton btnUkljuci;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextArea txtChat;
    // End of variables declaration//GEN-END:variables
}
