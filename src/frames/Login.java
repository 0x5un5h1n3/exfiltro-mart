/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package frames;

import com.formdev.flatlaf.FlatDarkLaf;
import java.awt.Color;
import java.awt.Toolkit;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.Timer;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.LineBorder;

/**
 *
 * @author 0x5un5h1n3
 */
public class Login extends javax.swing.JFrame {

    /**
     * Creates new form login
     */
    int mousepX;
    int mousepY;
    Timer timer;
    String month;
    String year;
    String date;
    String time;
    String today;
    int counter;

    public Login() {
        setIcon();
        initComponents();
        Login.this.getRootPane().setBorder(new LineBorder(new Color(1, 1, 1, 1)));

    }

    public void setPnlColor(JPanel pnl) {
        pnl.setBackground(new Color(35, 35, 35));
    }

    public void resetPnlColor(JPanel pnl) {
        pnl.setBackground(new Color(20, 20, 20));
    }

    private void employeeLogin() {
        String pwd = String.valueOf(pwdPassword.getPassword());
        counter++;
        if (counter > 3) {
            JOptionPane.showMessageDialog(this, "Intruder Login Detected, System will be Exited!", "Access Denied", JOptionPane.ERROR_MESSAGE);
            String IntruderloginActivityLog = "INSERT INTO log(usr_id, usr_type, usr_username, log_activity, log_description, log_state) VALUES ((SELECT usr_id FROM user WHERE usr_username = '" + txtUsername.getText() + "'),'Employee','" + txtUsername.getText() + "','Intruder Login','" + txtUsername.getText() + " has been detected as an Intruder','FAILED')";
            try {
                DB.DB.putData(IntruderloginActivityLog);
            } catch (Exception ex) {
            }
            System.exit(0);

        } else {

            try {

                ResultSet rs = DB.DB.getData("SELECT * FROM user WHERE usr_type ='Employee' AND usr_stat = '1' AND usr_username = '" + txtUsername.getText() + "' AND usr_password =  '" + pwd + "' ");
                if (rs.next()) {
                    String loginActivitySuccessLog = "INSERT INTO log(usr_id, usr_type, usr_username, log_activity, log_description, log_state) VALUES ((SELECT usr_id FROM user WHERE usr_username = '" + txtUsername.getText() + "'),'Employee','" + txtUsername.getText() + "','Employee Login','" + txtUsername.getText() + " logged in','SUCCESS')";
                    DB.DB.putData(loginActivitySuccessLog);

                    Login.this.dispose();

                    EmployeeDashboard ed = new EmployeeDashboard();
                    EmployeeDashboard.lblEmployeeUsername.setText(txtUsername.getText());

                    ed.setVisible(true);

                    Date DateMonth = new Date();
                    SimpleDateFormat toMonth = new SimpleDateFormat("MMMM");
                    month = "" + toMonth.format(DateMonth);

                    Date DateYear = new Date();
                    SimpleDateFormat toYear = new SimpleDateFormat("yyyy");
                    year = "" + toYear.format(DateYear);

                    Date DateDate = new Date();
                    SimpleDateFormat toDate = new SimpleDateFormat("yyyy-MM-dd");
                    date = "" + toDate.format(DateDate);

                    Date DateTime = new Date();
                    SimpleDateFormat toTime = new SimpleDateFormat("HH:mm:ss");
                    time = "" + toTime.format(DateTime);

                    Date DateToday = new Date();
                    SimpleDateFormat toToday = new SimpleDateFormat("yyyy-MM-dd");
                    today = "" + toToday.format(DateToday);

                    ResultSet at = DB.DB.getData("SELECT att_date FROM employee_attendance WHERE usr_id =(SELECT usr_id FROM user WHERE usr_username = '" + txtUsername.getText() + "') AND  att_date='" + date + "' ");

                    if (!at.isBeforeFirst()) {
                        String employeeAttendance = "INSERT INTO employee_attendance (usr_id, usr_username, att_month, att_year, att_date, att_login_time, att_logout_time) VALUES ((SELECT usr_id FROM user WHERE usr_username = '" + txtUsername.getText() + "'), '" + txtUsername.getText() + "', '" + month + "', '" + year + "','" + today + "', '" + time + "', '" + time + "');";
                        DB.DB.putData(employeeAttendance);
                    }

                    ResultSet payroll = DB.DB.getData("SELECT pay_month FROM payroll WHERE usr_id =(SELECT DISTINCT usr_id FROM employee_attendance WHERE usr_username = '" + txtUsername.getText() + "' ) AND pay_month = '" + month + "' AND pay_year = '" + year + "'");
                    DB.DB.putData("UPDATE payroll SET pay_attendance = (SELECT COUNT(usr_id) FROM employee_attendance WHERE usr_username = '" + txtUsername.getText() + "' AND att_month = '" + month + "' AND att_year = '" + year + "') WHERE usr_username = '" + txtUsername.getText() + "' AND  pay_month = '" + month + "' AND pay_year = '" + year + "'");

                    if (!payroll.isBeforeFirst()) {

                        String fillDataToPayroll = "INSERT INTO payroll(usr_id, usr_username, pay_salary, pay_month, pay_year, pay_date, pay_attendance, pay_stat) VALUES ((SELECT usr_id FROM user WHERE usr_username = '" + txtUsername.getText() + "'),'" + txtUsername.getText() + "','0','" + month + "','" + year + "','" + date + "',(SELECT COUNT(usr_id) FROM employee_attendance WHERE usr_username = '" + txtUsername.getText() + "' AND att_month = '" + month + "' AND att_year = '" + year + "') ,'UNPAID' )";
                        DB.DB.putData(fillDataToPayroll);
                    }

                } else {
                    String loginActivityFailedLog = "INSERT INTO log(usr_id, usr_type, usr_username, log_activity, log_description, log_state) VALUES ((SELECT usr_id FROM user WHERE usr_username = '" + txtUsername.getText() + "'),'Employee','" + txtUsername.getText() + "','Employee Login','" + txtUsername.getText() + " login failed','FAILED')";

                    DB.DB.putData(loginActivityFailedLog);
                    JOptionPane.showMessageDialog(this, "Access Denied", "Access Denied", JOptionPane.WARNING_MESSAGE); //invalid entry message

                    txtUsername.setText(null);
                    pwdPassword.setText(null);
                    txtUsername.grabFocus();

                }

            } catch (Exception ex) {
            }
        }

    }

    private void adminLogin() {
        String pwd = String.valueOf(pwdPassword.getPassword());
        counter++;
        if (counter > 3) {
            JOptionPane.showMessageDialog(this, "Intruder Login Detected, System will be Exited!", "Access Denied", JOptionPane.ERROR_MESSAGE);

            String IntruderloginActivityLog = "INSERT INTO log(usr_id, usr_type, usr_username, log_activity, log_description, log_state) VALUES ((SELECT usr_id FROM user WHERE usr_username = '" + txtUsername.getText() + "'),'Admin','" + txtUsername.getText() + "','Intruder Login','" + txtUsername.getText() + " has been detected as an Intruder','FAILED')";
            try {
                DB.DB.putData(IntruderloginActivityLog);
            } catch (Exception ex) {
            }
            System.exit(0);

        } else {
            try {
                ResultSet rs = DB.DB.getData("SELECT * FROM user WHERE usr_type ='admin' AND usr_stat = '1' AND usr_username = '" + txtUsername.getText() + "' AND usr_password =  '" + pwd + "' ");
                if (rs.next()) {
                    String loginActivitySuccessLog = "INSERT INTO log(usr_id, usr_type, usr_username, log_activity, log_description, log_state) VALUES ((SELECT usr_id FROM user WHERE usr_username = '" + txtUsername.getText() + "'),'Admin','" + txtUsername.getText() + "','Admin Login','" + txtUsername.getText() + " logged in','SUCCESS')";
                    DB.DB.putData(loginActivitySuccessLog);

                    Login.this.dispose();

                    AdminDashboard ad = new AdminDashboard();

                    AdminDashboard.lblAdminUsername.setText(txtUsername.getText());
                    ad.setVisible(true);

                } else {
                    String loginActivityFailedLog = "INSERT INTO log(usr_id, usr_type, usr_username, log_activity, log_description, log_state) VALUES ((SELECT usr_id FROM user WHERE usr_username = '" + txtUsername.getText() + "'),'Admin','" + txtUsername.getText() + "','Admin Login','" + txtUsername.getText() + " login failed','FAILED')";

                    DB.DB.putData(loginActivityFailedLog);
                    JOptionPane.showMessageDialog(this, "Access Denied From Admin", "Access Denied", JOptionPane.WARNING_MESSAGE); //invalid entry message

                    txtUsername.setText(null);
                    pwdPassword.setText(null);
                    txtUsername.grabFocus();

                }

            } catch (Exception e) {
            }
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

        pnlFrames = new javax.swing.JPanel();
        pnlLogin = new javax.swing.JPanel();
        btn_close = new javax.swing.JLabel();
        pnlLeft = new javax.swing.JPanel();
        userTypeImg = new javax.swing.JLabel();
        loginWish = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        mainCard = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        pwdPassword = new javax.swing.JPasswordField();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Xeon Inventory | Sign In");
        setUndecorated(true);
        setOpacity(0.985F);
        setResizable(false);

        pnlFrames.setLayout(new java.awt.CardLayout());

        pnlLogin.setBackground(new java.awt.Color(70, 70, 70));
        pnlLogin.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseDragged(java.awt.event.MouseEvent evt) {
                pnlLoginMouseDragged(evt);
            }
        });
        pnlLogin.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                pnlLoginMousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                pnlLoginMouseReleased(evt);
            }
        });
        pnlLogin.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        btn_close.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        btn_close.setIcon(new javax.swing.ImageIcon(getClass().getResource("/frames/img/transparent_titlebar_close.png"))); // NOI18N
        btn_close.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btn_closeMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btn_closeMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btn_closeMouseExited(evt);
            }
        });
        pnlLogin.add(btn_close, new org.netbeans.lib.awtextra.AbsoluteConstraints(854, 0, -1, -1));

        pnlLeft.setBackground(new java.awt.Color(0, 0, 0));
        pnlLeft.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseDragged(java.awt.event.MouseEvent evt) {
                pnlLeftMouseDragged(evt);
            }
        });
        pnlLeft.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                pnlLeftMousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                pnlLeftMouseReleased(evt);
            }
        });

        userTypeImg.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        userTypeImg.setIcon(new javax.swing.ImageIcon(getClass().getResource("/frames/img/login-logo.png"))); // NOI18N

        loginWish.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        loginWish.setForeground(new java.awt.Color(255, 255, 255));
        loginWish.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        loginWish.setText("Welcome to Exfiltro Mart");
        loginWish.setToolTipText("");

        jLabel3.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(153, 153, 153));
        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel3.setText("Log In");

        javax.swing.GroupLayout pnlLeftLayout = new javax.swing.GroupLayout(pnlLeft);
        pnlLeft.setLayout(pnlLeftLayout);
        pnlLeftLayout.setHorizontalGroup(
            pnlLeftLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlLeftLayout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addComponent(userTypeImg, javax.swing.GroupLayout.PREFERRED_SIZE, 357, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(pnlLeftLayout.createSequentialGroup()
                .addGroup(pnlLeftLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(loginWish, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel3, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 390, Short.MAX_VALUE))
                .addGap(0, 0, Short.MAX_VALUE))
        );
        pnlLeftLayout.setVerticalGroup(
            pnlLeftLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlLeftLayout.createSequentialGroup()
                .addGap(127, 127, 127)
                .addComponent(userTypeImg)
                .addGap(47, 47, 47)
                .addComponent(loginWish)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel3)
                .addContainerGap(123, Short.MAX_VALUE))
        );

        pnlLogin.add(pnlLeft, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 390, 550));

        mainCard.setBackground(new java.awt.Color(153, 153, 153));
        mainCard.setForeground(new java.awt.Color(102, 102, 102));
        mainCard.setLayout(new java.awt.CardLayout());

        jPanel1.setBackground(new java.awt.Color(70, 70, 70));
        jPanel1.setForeground(new java.awt.Color(102, 102, 102));

        jLabel1.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("Username");

        jLabel2.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("Password");

        jButton1.setBackground(new java.awt.Color(34, 34, 34));
        jButton1.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jButton1.setForeground(new java.awt.Color(255, 255, 255));
        jButton1.setText("Login");
        jButton1.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true));
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        pwdPassword.setBackground(new java.awt.Color(34, 34, 34));
        pwdPassword.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        pwdPassword.setForeground(new java.awt.Color(255, 255, 255));
        pwdPassword.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        pwdPassword.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true));
        pwdPassword.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                pwdPasswordKeyPressed(evt);
            }
        });

        txtUsername.setBackground(new java.awt.Color(34, 34, 34));
        txtUsername.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        txtUsername.setForeground(new java.awt.Color(255, 255, 255));
        txtUsername.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtUsername.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true));
        txtUsername.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtUsernameKeyPressed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(81, 81, 81)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel1)
                    .addComponent(jLabel2)
                    .addComponent(pwdPassword)
                    .addComponent(txtUsername)
                    .addComponent(jButton1, javax.swing.GroupLayout.DEFAULT_SIZE, 357, Short.MAX_VALUE))
                .addContainerGap(72, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(52, 52, 52)
                .addComponent(jLabel1)
                .addGap(18, 18, 18)
                .addComponent(txtUsername, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(35, 35, 35)
                .addComponent(jLabel2)
                .addGap(18, 18, 18)
                .addComponent(pwdPassword, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(56, 56, 56)
                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(169, Short.MAX_VALUE))
        );

        mainCard.add(jPanel1, "card2");

        pnlLogin.add(mainCard, new org.netbeans.lib.awtextra.AbsoluteConstraints(390, 38, 510, 510));

        pnlFrames.add(pnlLogin, "card2");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(pnlFrames, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(pnlFrames, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        setSize(new java.awt.Dimension(900, 547));
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void btn_closeMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_closeMouseClicked
        for (float i = 1f; i > 0; i -= 0.000001) {
            Login.this.setOpacity(i);
        }
        System.exit(0);
    }//GEN-LAST:event_btn_closeMouseClicked

    private void btn_closeMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_closeMouseEntered
        ImageIcon ii = new ImageIcon(getClass().getResource("img/titlebar_close_hover.png"));
        btn_close.setIcon(ii);
    }//GEN-LAST:event_btn_closeMouseEntered

    private void btn_closeMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_closeMouseExited
        ImageIcon ii = new ImageIcon(getClass().getResource("img/transparent_titlebar_close.png"));
        btn_close.setIcon(ii);
    }//GEN-LAST:event_btn_closeMouseExited

    private void pnlLeftMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_pnlLeftMousePressed
        mousepX = evt.getX();
        mousepY = evt.getY();
    }//GEN-LAST:event_pnlLeftMousePressed

    private void pnlLeftMouseDragged(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_pnlLeftMouseDragged
        int locX = evt.getXOnScreen();
        int locY = evt.getYOnScreen();

        this.setLocation(locX - mousepX, locY - mousepY);
        setOpacity((float) (0.97));
    }//GEN-LAST:event_pnlLeftMouseDragged

    private void pnlLoginMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_pnlLoginMousePressed
        mousepX = evt.getX();
        mousepY = evt.getY();
    }//GEN-LAST:event_pnlLoginMousePressed

    private void pnlLoginMouseDragged(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_pnlLoginMouseDragged
        int locX = evt.getXOnScreen();
        int locY = evt.getYOnScreen();

        this.setLocation(locX - mousepX, locY - mousepY);
        setOpacity((float) (0.97));
    }//GEN-LAST:event_pnlLoginMouseDragged

    private void pnlLeftMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_pnlLeftMouseReleased
        setOpacity((float) (0.985));
    }//GEN-LAST:event_pnlLeftMouseReleased

    private void pnlLoginMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_pnlLoginMouseReleased
        setOpacity((float) (0.985));
    }//GEN-LAST:event_pnlLoginMouseReleased

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        try {
            ResultSet rs = DB.DB.getData("SELECT * FROM user WHERE usr_type ='Admin' AND usr_username = '" + txtUsername.getText() + "' ");
            if (rs.next()) {
                adminLogin();
            } else {
                employeeLogin();
            }
        } catch (Exception e) {
        }
    }//GEN-LAST:event_jButton1ActionPerformed

    private void txtUsernameKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtUsernameKeyPressed
        if (evt.getKeyCode() == 10) {
            pwdPassword.grabFocus();
        }
    }//GEN-LAST:event_txtUsernameKeyPressed

    private void pwdPasswordKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_pwdPasswordKeyPressed
        if (evt.getKeyCode() == 10) {
            try {
                ResultSet rs = DB.DB.getData("SELECT * FROM user WHERE usr_type ='Admin' AND usr_username = '" + txtUsername.getText() + "' ");
                if (rs.next()) {
                    adminLogin();
                } else {
                    employeeLogin();
                }
            } catch (Exception e) {
            }
        }
    }//GEN-LAST:event_pwdPasswordKeyPressed

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

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Login().setVisible(true);
            }
        });
    }

    private void setIcon() {
        setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("img/exfiltrat0z-icon.png")));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel btn_close;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JLabel loginWish;
    private javax.swing.JPanel mainCard;
    private javax.swing.JPanel pnlFrames;
    private javax.swing.JPanel pnlLeft;
    private javax.swing.JPanel pnlLogin;
    private javax.swing.JPasswordField pwdPassword;
    public static final javax.swing.JTextField txtUsername = new javax.swing.JTextField();
    private javax.swing.JLabel userTypeImg;
    // End of variables declaration//GEN-END:variables
}
