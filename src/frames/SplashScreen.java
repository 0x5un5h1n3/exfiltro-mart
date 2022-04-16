/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package frames;

import com.formdev.flatlaf.FlatDarkLaf;
import java.awt.Color;
import java.awt.Toolkit;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.LineBorder;

/**
 *
 * @author 0x5un5h1n3
 */
public class SplashScreen extends javax.swing.JFrame {

    /**
     * Creates new form SplashScreen
     */
    public SplashScreen() {
        initComponents();

        SplashScreen.this.getRootPane().setBorder(new LineBorder(new Color(1, 1, 1, 1)));
        setIcon();
        progress(this);

    }

    void progress(SplashScreen sp) {

        jProgressBar1.setStringPainted(true);
        jProgressBar1.setBackground(Color.WHITE);
        jProgressBar1.setForeground(Color.black);

        new Thread(new Runnable() {
            @Override
            public void run() {

                for (int i = 0; i <= 100; i++) {
                    jProgressBar1.setValue(i);
                    jProgressBar1.setFont(new java.awt.Font(null, 0, 0));
                    if (i == 100) {
                        for (float k = 0.985f; k > 0; k -= 0.000001) {
                            SplashScreen.this.setOpacity(k);
                        }
                        sp.dispose();
                        Login login = new Login();
                        login.setVisible(true);

                        for (float j = 0f; j < 0.9851; j += 0.000001) {
                            login.setOpacity(j);
                        }

                    }
                    try {
                        Thread.sleep(30);
                    } catch (InterruptedException e) {

                    }
                }

            }
        }).start();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel4 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jProgressBar1 = new javax.swing.JProgressBar();
        version = new javax.swing.JLabel();
        developer = new javax.swing.JLabel();
        version_details = new javax.swing.JLabel();
        splash_img = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Xeon Inventory | Getting Started");
        setAlwaysOnTop(true);
        setMinimumSize(new java.awt.Dimension(900, 550));
        setUndecorated(true);
        setOpacity(0.9F);
        setResizable(false);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/frames/img/splash.png"))); // NOI18N
        getContentPane().add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 50, 900, 390));

        jPanel1.setBackground(new java.awt.Color(0, 0, 0));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jProgressBar1.setBackground(new java.awt.Color(0, 0, 0));
        jProgressBar1.setFont(new java.awt.Font("Segoe UI", 0, 11)); // NOI18N
        jProgressBar1.setForeground(new java.awt.Color(0, 0, 0));
        jProgressBar1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jProgressBar1.setFocusable(false);
        jProgressBar1.setOpaque(true);
        jProgressBar1.setRequestFocusEnabled(false);
        jPanel1.add(jProgressBar1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 38, 920, 10));

        version.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        version.setForeground(new java.awt.Color(255, 255, 255));
        version.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        version.setText("Version 1.0");
        version.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jPanel1.add(version, new org.netbeans.lib.awtextra.AbsoluteConstraints(760, -6, 150, 20));

        developer.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        developer.setForeground(new java.awt.Color(255, 255, 255));
        developer.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        developer.setText("</> Exfiltrat0z Group");
        developer.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jPanel1.add(developer, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, -6, 210, 20));

        getContentPane().add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(-10, 504, 920, 50));

        version_details.setFont(new java.awt.Font("Segoe UI", 0, 26)); // NOI18N
        version_details.setForeground(new java.awt.Color(255, 255, 255));
        version_details.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        version_details.setText("Exfiltro Mart");
        version_details.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        getContentPane().add(version_details, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 440, 900, 50));

        splash_img.setBackground(new java.awt.Color(0, 0, 0));
        splash_img.setFont(new java.awt.Font("Segoe UI", 0, 36)); // NOI18N
        splash_img.setForeground(new java.awt.Color(255, 255, 255));
        splash_img.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        splash_img.setOpaque(true);
        getContentPane().add(splash_img, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 900, 550));

        setSize(new java.awt.Dimension(900, 550));
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

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
            UIManager.setLookAndFeel(new FlatDarkLaf());
        } catch (UnsupportedLookAndFeelException ex) {
            System.err.println(ex + "Failed to initialize LaF");
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        SplashScreen screen = new SplashScreen();
        screen.setVisible(true);

        for (float i = 0f; i < 0.985; i += 0.000001) {
            screen.setOpacity(i);
        }
    }

    private void setIcon() {
        setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("img/exfiltrat0z-icon.png")));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel developer;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JProgressBar jProgressBar1;
    private javax.swing.JLabel splash_img;
    private javax.swing.JLabel version;
    private javax.swing.JLabel version_details;
    // End of variables declaration//GEN-END:variables
}
