package comunicacion;

import prueba.CarGame;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.*;


public class ingreso extends javax.swing.JFrame {

    private Socket socket;
    private PrintWriter out;
    
    public ingreso() {
        initComponents();
        connectToServer();
    }
    
    private void connectToServer() {
        try {
            String serverIP = "192.168.100.74"; // Cambia esto a la IP del servidor
            socket = new Socket(serverIP, 12345); // Conexión al servidor en el puerto 12345
            out = new PrintWriter(socket.getOutputStream(), true);
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "No se pudo conectar al servidor", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel2 = new javax.swing.JLabel();
        jTextField1_ingreso = new javax.swing.JTextField();
        jButton1_jugar = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel2.setBackground(new java.awt.Color(255, 255, 255));
        jLabel2.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("Ingresa  tu nombre:");
        getContentPane().add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 170, 190, 30));

        jTextField1_ingreso.setToolTipText("ingresa tu nombre");
        jTextField1_ingreso.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField1_ingresoActionPerformed(evt);
            }
        });
        getContentPane().add(jTextField1_ingreso, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 210, 250, -1));

        jButton1_jugar.setText("Jugar");
        jButton1_jugar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1_jugarActionPerformed(evt);
            }
        });
        getContentPane().add(jButton1_jugar, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 280, 140, 30));

        jLabel1.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("Bienvenido a GeroRace");
        getContentPane().add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 20, 260, 56));

        jLabel3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/prueba/fondoINGRESO.jpg"))); // NOI18N
        getContentPane().add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 800, 600));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1_jugarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1_jugarActionPerformed
        // Obtener el nombre del campo de texto
        String nombre = jTextField1_ingreso.getText();

        // Enviar el nombre al servidor
        if (out != null) {
            out.println(nombre);
        } else {
            JOptionPane.showMessageDialog(this, "No hay conexión con el servidor", "Error", JOptionPane.ERROR_MESSAGE);
        }

        // Iniciar el juego CarGame y pasar la conexión del socket y el nombre del usuario
        new CarGame(socket, nombre).setVisible(true);
        // Cerrar la ventana de ingreso
        this.dispose();
    }//GEN-LAST:event_jButton1_jugarActionPerformed

    private void jTextField1_ingresoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField1_ingresoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField1_ingresoActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(ingreso.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ingreso.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ingreso.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ingreso.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new ingreso().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1_jugar;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JTextField jTextField1_ingreso;
    // End of variables declaration//GEN-END:variables
}
