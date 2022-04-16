/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package frames;

import com.formdev.flatlaf.FlatDarkLaf;
import java.awt.Color;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.ResultSet;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.Timer;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

/**
 *
 * @author 0x5un5h1n3
 */
public class EmployeeEmpAttendance extends javax.swing.JFrame {

    Timer timer;
    String month;
    String year;
    String date;
    String today;

    public EmployeeEmpAttendance() {
        initComponents();
        EmployeeEmpAttendance.this.setExtendedState(JFrame.MAXIMIZED_BOTH);
        EmployeeEmpAttendance.this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setIcon();
        setTableheader();
        refreshEmployeeAttendanceTable();

        EmployeeEmpAttendance.this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {

                EmployeeEmpAttendance.this.dispose();
                EmployeeDashboard ed = new EmployeeDashboard();
                EmployeeDashboard.lblEmployeeUsername.setText(lblEmployeeUsername.getText());
                ed.setVisible(true);

            }
        });
        ActionListener actionListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Date date1 = new Date();
                DateFormat timeFormat = new SimpleDateFormat("hh:mm a");
                String time = timeFormat.format(date1);
                lblTime.setText(time);

                Date date2 = new Date();
                DateFormat dayInWordsFormat = new SimpleDateFormat("EEEE");
                String dayInWords = dayInWordsFormat.format(date2);
                DateFormat monthFormat = new SimpleDateFormat("MMMM");
                String month = monthFormat.format(date2);
                DateFormat dayFormat = new SimpleDateFormat("dd");
                String day = dayFormat.format(date2);
                DateFormat yearFormat = new SimpleDateFormat("yyyy");
                String year = yearFormat.format(date2);
                lblDate.setText("Today is " + dayInWords + ", " + month + " " + day + ", " + year);

            }
        };
        timer = new Timer(1000, actionListener);

        timer.setInitialDelay(
                0);
        timer.start();
    }

    private void setTableheader() {
        JTableHeader empAtttbl = tblEmployeeAttendance.getTableHeader();
        empAtttbl.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        empAtttbl.setForeground(Color.white);
        empAtttbl.setBackground(new Color(51, 51, 51));
        tblEmployeeAttendance.getTableHeader().setReorderingAllowed(false);
    }

    private void refreshEmployeeAttendanceTable() {
        try {

            Date DateDate = new Date();
            SimpleDateFormat toDate = new SimpleDateFormat("yyyy-MM-dd");
            date = "" + toDate.format(DateDate);

            DefaultTableModel dtm = (DefaultTableModel) tblEmployeeAttendance.getModel();
            ResultSet rs = DB.DB.getData("SELECT * FROM employee_attendance order by att_date desc ");

            dtm.setRowCount(0);

            while (rs.next()) {
                String a = (rs.getString(1));
                String b = (rs.getString(2));
                String c = (rs.getString(3));
                String d = (rs.getString(4));
                String e = (rs.getString(5));
                String f = (rs.getString(6));
                String g = (rs.getString(7));
                dtm.addRow(new Object[]{a, b, c, d, e, f, g});
            }

        } catch (Exception e) {
        }
    }

    private void searchEmployeeAttendance() {

        switch (cmbSortEmployeeAttendance.getSelectedIndex()) {
            case 0:
                Date DateDate = new Date();
                SimpleDateFormat toDate = new SimpleDateFormat("yyyy-MM-dd");
                date = "" + toDate.format(DateDate);

                try {
                    DefaultTableModel dtm = (DefaultTableModel) tblEmployeeAttendance.getModel();
                    ResultSet rs = DB.DB.getData("SELECT * FROM employee_attendance WHERE usr_id like '" + txtSearchEmployeeAttendance.getText() + "%' order by usr_id ");
                    dtm.setRowCount(0);

                    while (rs.next()) {
                        String a = (rs.getString(1));
                        String b = (rs.getString(2));
                        String c = (rs.getString(3));
                        String d = (rs.getString(4));
                        String e = (rs.getString(5));
                        String f = (rs.getString(6));
                        String g = (rs.getString(7));
                        dtm.addRow(new Object[]{a, b, c, d, e, f, g});
                    }

                } catch (Exception e) {
                }
                break;
            case 1:
                try {
                DefaultTableModel dtm = (DefaultTableModel) tblEmployeeAttendance.getModel();
                ResultSet rs = DB.DB.getData("SELECT * FROM employee_attendance WHERE usr_username like '" + txtSearchEmployeeAttendance.getText() + "%' order by usr_username ");
                dtm.setRowCount(0);

                while (rs.next()) {
                    String a = (rs.getString(1));
                    String b = (rs.getString(2));
                    String c = (rs.getString(3));
                    String d = (rs.getString(4));
                    String e = (rs.getString(5));
                    String f = (rs.getString(6));
                    String g = (rs.getString(7));
                    dtm.addRow(new Object[]{a, b, c, d, e, f, g});
                }

            } catch (Exception e) {
            }
            break;
            default:
                break;
        }
        jXDatePicker1.setDate(null);
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
        pnlHomeSubSelection = new javax.swing.JPanel();
        lblDate = new javax.swing.JLabel();
        lblTime = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        pnlLogout = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        jPanel11 = new javax.swing.JPanel();
        jTabbedPaneEmployee = new javax.swing.JTabbedPane();
        jPanel7 = new javax.swing.JPanel();
        txtSearchEmployeeAttendance = new javax.swing.JTextField();
        jLabel20 = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        tblEmployeeAttendance = new javax.swing.JTable();
        jButton18 = new javax.swing.JButton();
        cmbSortEmployeeAttendance = new javax.swing.JComboBox<>();
        jXDatePicker1 = new org.jdesktop.swingx.JXDatePicker();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Exfiltro Mart");

        jPanel1.setBackground(new java.awt.Color(51, 51, 51));

        pnlHomeSubSelection.setBackground(new java.awt.Color(70, 70, 70));

        lblDate.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        lblDate.setForeground(new java.awt.Color(255, 255, 255));
        lblDate.setText("Today is Monday, January 1, 2020");

        lblTime.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        lblTime.setForeground(new java.awt.Color(255, 255, 255));
        lblTime.setText("10:30 AM");

        javax.swing.GroupLayout pnlHomeSubSelectionLayout = new javax.swing.GroupLayout(pnlHomeSubSelection);
        pnlHomeSubSelection.setLayout(pnlHomeSubSelectionLayout);
        pnlHomeSubSelectionLayout.setHorizontalGroup(
            pnlHomeSubSelectionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlHomeSubSelectionLayout.createSequentialGroup()
                .addGap(37, 37, 37)
                .addComponent(lblDate, javax.swing.GroupLayout.PREFERRED_SIZE, 426, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(lblTime, javax.swing.GroupLayout.PREFERRED_SIZE, 96, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(21, 21, 21))
        );
        pnlHomeSubSelectionLayout.setVerticalGroup(
            pnlHomeSubSelectionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlHomeSubSelectionLayout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addGroup(pnlHomeSubSelectionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblDate, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblTime, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );

        jLabel2.setFont(new java.awt.Font("Segoe UI", 0, 20)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("Attendance");
        jLabel2.setToolTipText("");

        jLabel3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/frames/img/icons8_today_50px.png"))); // NOI18N

        pnlLogout.setBackground(new java.awt.Color(51, 51, 51));

        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/frames/img/logout_white.png"))); // NOI18N
        jLabel4.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel4MouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jLabel4MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jLabel4MouseExited(evt);
            }
        });

        javax.swing.GroupLayout pnlLogoutLayout = new javax.swing.GroupLayout(pnlLogout);
        pnlLogout.setLayout(pnlLogoutLayout);
        pnlLogoutLayout.setHorizontalGroup(
            pnlLogoutLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, 54, Short.MAX_VALUE)
        );
        pnlLogoutLayout.setVerticalGroup(
            pnlLogoutLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel4, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 52, Short.MAX_VALUE)
        );

        lblEmployeeUsername.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        lblEmployeeUsername.setForeground(new java.awt.Color(255, 255, 255));
        lblEmployeeUsername.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblEmployeeUsername.setText("Employee");

        jPanel11.setBackground(new java.awt.Color(204, 204, 204));

        jTabbedPaneEmployee.setBackground(new java.awt.Color(34, 34, 34));
        jTabbedPaneEmployee.setForeground(new java.awt.Color(255, 255, 255));
        jTabbedPaneEmployee.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jTabbedPaneEmployee.setOpaque(true);

        jPanel7.setBackground(new java.awt.Color(34, 34, 34));

        txtSearchEmployeeAttendance.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        txtSearchEmployeeAttendance.setForeground(new java.awt.Color(255, 255, 255));
        txtSearchEmployeeAttendance.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtSearchEmployeeAttendanceKeyReleased(evt);
            }
        });

        jLabel20.setIcon(new javax.swing.ImageIcon(getClass().getResource("/frames/img/search.icon.png"))); // NOI18N

        tblEmployeeAttendance.setBackground(new java.awt.Color(34, 34, 34));
        tblEmployeeAttendance.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        tblEmployeeAttendance.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Employee Id", "Username", "Month", "Year", "Date", "Login Time", "Logout Time"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblEmployeeAttendance.setGridColor(new java.awt.Color(61, 61, 61));
        tblEmployeeAttendance.setShowGrid(true);
        tblEmployeeAttendance.getTableHeader().setReorderingAllowed(false);
        jScrollPane3.setViewportView(tblEmployeeAttendance);

        jButton18.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jButton18.setForeground(new java.awt.Color(255, 255, 255));
        jButton18.setText(" Refresh");
        jButton18.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jButton18.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton18ActionPerformed(evt);
            }
        });

        cmbSortEmployeeAttendance.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        cmbSortEmployeeAttendance.setForeground(new java.awt.Color(255, 255, 255));
        cmbSortEmployeeAttendance.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Id", "Name" }));
        cmbSortEmployeeAttendance.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbSortEmployeeAttendanceActionPerformed(evt);
            }
        });

        jXDatePicker1.setForeground(new java.awt.Color(255, 255, 255));
        jXDatePicker1.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jXDatePicker1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jXDatePicker1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel7Layout.createSequentialGroup()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButton18))
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addGap(52, 52, 52)
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane3)
                            .addGroup(jPanel7Layout.createSequentialGroup()
                                .addComponent(txtSearchEmployeeAttendance, javax.swing.GroupLayout.DEFAULT_SIZE, 650, Short.MAX_VALUE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(cmbSortEmployeeAttendance, javax.swing.GroupLayout.PREFERRED_SIZE, 136, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jXDatePicker1, javax.swing.GroupLayout.PREFERRED_SIZE, 242, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jLabel20)))))
                .addGap(52, 52, 52))
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addGap(23, 23, 23)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtSearchEmployeeAttendance, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel20)
                    .addComponent(cmbSortEmployeeAttendance, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jXDatePicker1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 393, Short.MAX_VALUE)
                .addGap(25, 25, 25)
                .addComponent(jButton18)
                .addGap(36, 36, 36))
        );

        jTabbedPaneEmployee.addTab("Attendance History", jPanel7);

        javax.swing.GroupLayout jPanel11Layout = new javax.swing.GroupLayout(jPanel11);
        jPanel11.setLayout(jPanel11Layout);
        jPanel11Layout.setHorizontalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPaneEmployee)
        );
        jPanel11Layout.setVerticalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPaneEmployee)
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(pnlHomeSubSelection, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(33, 33, 33)
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 267, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(lblEmployeeUsername, javax.swing.GroupLayout.PREFERRED_SIZE, 134, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(29, 29, 29)
                .addComponent(pnlLogout, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
            .addComponent(jPanel11, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(lblEmployeeUsername, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                            .addGap(7, 7, 7)
                            .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(jPanel1Layout.createSequentialGroup()
                            .addContainerGap()
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(pnlLogout, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addGap(6, 6, 6)
                .addComponent(pnlHomeSubSelection, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(jPanel11, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        setSize(new java.awt.Dimension(1216, 729));
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void jLabel4MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel4MouseEntered
        pnlLogout.setBackground(new Color(71, 71, 71));
    }//GEN-LAST:event_jLabel4MouseEntered

    private void jLabel4MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel4MouseExited
        pnlLogout.setBackground(new Color(51, 51, 51));
    }//GEN-LAST:event_jLabel4MouseExited

    private void jXDatePicker1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jXDatePicker1ActionPerformed
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date pickedDate = jXDatePicker1.getDate();
        String dateString = dateFormat.format(pickedDate);

        try {
            DefaultTableModel dtm = (DefaultTableModel) tblEmployeeAttendance.getModel();
            ResultSet rs = DB.DB.getData("SELECT * FROM employee_attendance WHERE att_date = '" + dateString + "' AND usr_id like '" + txtSearchEmployeeAttendance.getText() + "%' order by usr_id ");

            dtm.setRowCount(0);

            while (rs.next()) {
                String a = (rs.getString(1));
                String b = (rs.getString(2));
                String c = (rs.getString(3));
                String d = (rs.getString(4));
                String e = (rs.getString(5));
                String f = (rs.getString(6));
                String g = (rs.getString(7));
                dtm.addRow(new Object[]{a, b, c, d, e, f, g});
            }
        } catch (Exception e) {
        }
        txtSearchEmployeeAttendance.setText(null);
    }//GEN-LAST:event_jXDatePicker1ActionPerformed

    private void cmbSortEmployeeAttendanceActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbSortEmployeeAttendanceActionPerformed
        searchEmployeeAttendance();
    }//GEN-LAST:event_cmbSortEmployeeAttendanceActionPerformed

    private void jButton18ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton18ActionPerformed
        txtSearchEmployeeAttendance.setText(null);
        jXDatePicker1.setDate(null);
        refreshEmployeeAttendanceTable();
        cmbSortEmployeeAttendance.setSelectedIndex(0);
    }//GEN-LAST:event_jButton18ActionPerformed

    private void txtSearchEmployeeAttendanceKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtSearchEmployeeAttendanceKeyReleased
        searchEmployeeAttendance();
    }//GEN-LAST:event_txtSearchEmployeeAttendanceKeyReleased

    private void jLabel4MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel4MouseClicked
        int choose = JOptionPane.showConfirmDialog(this,
                "Do you want to Logout the application?",
                "Confirm Logout", JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE);
        if (choose == JOptionPane.YES_OPTION) {

            String logoutActivityLog = "INSERT INTO log(usr_id, usr_type, usr_username, log_activity, log_description, log_state) VALUES ((SELECT usr_id FROM user WHERE usr_username = '" + lblEmployeeUsername.getText() + "'),'Admin','" + lblEmployeeUsername.getText() + "','Admin Logout', '" + lblEmployeeUsername.getText() + " logged out' ,'SUCCESS')";
            try {
                DB.DB.putData(logoutActivityLog);
            } catch (Exception e) {
            }

            EmployeeEmpAttendance.this.dispose();

            Login login = new Login();
            login.setVisible(true);
            for (float j = 0f; j < 0.985; j += 0.000001) {
                login.setOpacity(j);
            }
        } else {

        }
    }//GEN-LAST:event_jLabel4MouseClicked

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

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new EmployeeEmpAttendance().setVisible(true);
            }
        });
    }

    private void setIcon() {
        setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("img/exfiltrat0z-icon.png")));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox<String> cmbSortEmployeeAttendance;
    private javax.swing.JButton jButton18;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JTabbedPane jTabbedPaneEmployee;
    private org.jdesktop.swingx.JXDatePicker jXDatePicker1;
    private javax.swing.JLabel lblDate;
    public static final javax.swing.JLabel lblEmployeeUsername = new javax.swing.JLabel();
    private javax.swing.JLabel lblTime;
    private javax.swing.JPanel pnlHomeSubSelection;
    private javax.swing.JPanel pnlLogout;
    private javax.swing.JTable tblEmployeeAttendance;
    private javax.swing.JTextField txtSearchEmployeeAttendance;
    // End of variables declaration//GEN-END:variables
}
