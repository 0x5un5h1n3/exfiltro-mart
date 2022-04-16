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
public class EmployeeEmpPayroll extends javax.swing.JFrame {

    Timer timer;
    String month;
    String year;
    String date;
    String time;
    String today;

    public EmployeeEmpPayroll() {
        initComponents();
        EmployeeEmpPayroll.this.setExtendedState(JFrame.MAXIMIZED_BOTH);
        EmployeeEmpPayroll.this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setIcon();
        setTableheader();
        refreshEmployeePayrollHistoryTable();

        EmployeeEmpPayroll.this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {

                EmployeeEmpPayroll.this.dispose();
                EmployeeDashboard em = new EmployeeDashboard();
                EmployeeDashboard.lblEmployeeUsername.setText(lblEmployeeUsername.getText());
                em.setVisible(true);
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
        JTableHeader todayEmpPayHist = tblEmployeePayrollHistory.getTableHeader();
        todayEmpPayHist.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        todayEmpPayHist.setForeground(Color.white);
        todayEmpPayHist.setBackground(new Color(51, 51, 51));
        tblEmployeePayrollHistory.getTableHeader().setReorderingAllowed(false);

    }

    private void refreshEmployeePayrollHistoryTable() {
        try {

            Date DateMonth = new Date();
            SimpleDateFormat toMonth = new SimpleDateFormat("MMMM");
            month = "" + toMonth.format(DateMonth);

            DefaultTableModel dtm = (DefaultTableModel) tblEmployeePayrollHistory.getModel();
            ResultSet rs = DB.DB.getData("SELECT * FROM payroll WHERE pay_stat = 'PAID' AND usr_username = '" + lblEmployeeUsername.getText() + "' order by pay_date desc ");
            dtm.setRowCount(0);

            while (rs.next()) {
                String a = (rs.getString(2));
                String b = (rs.getString(3));
                String c = (rs.getString(4));
                String d = (rs.getString(5));
                String e = (rs.getString(6));
                String f = (rs.getString(7));
                String g = (rs.getString(8));
                String h = (rs.getString(9));
                dtm.addRow(new Object[]{a, b, c, d, e, f, g, h});
            }

        } catch (Exception e) {
        }
    }

    private void searchPaymentHistory() {
        switch (cmbSortEmployeePayrollHistory.getSelectedIndex()) {
            case 0:

                try {
                DefaultTableModel dtm = (DefaultTableModel) tblEmployeePayrollHistory.getModel();
                ResultSet rs = DB.DB.getData("SELECT * FROM payroll WHERE usr_username = '" + lblEmployeeUsername.getText() + "' order by pay_date desc ");
                dtm.setRowCount(0);

                while (rs.next()) {
                    String a = (rs.getString(2));
                    String b = (rs.getString(3));
                    String c = (rs.getString(4));
                    String d = (rs.getString(5));
                    String e = (rs.getString(6));
                    String f = (rs.getString(7));
                    String g = (rs.getString(8));
                    String h = (rs.getString(9));
                    dtm.addRow(new Object[]{a, b, c, d, e, f, g, h});
                }

            } catch (Exception e) {
            }
            break;
            case 1:
                try {
                DefaultTableModel dtm = (DefaultTableModel) tblEmployeePayrollHistory.getModel();
                ResultSet rs = DB.DB.getData("SELECT * FROM payroll WHERE usr_username = '" + lblEmployeeUsername.getText() + "' AND pay_month like '" + txtSearchPayrollHistory.getText() + "%' order by pay_month ");
                dtm.setRowCount(0);

                while (rs.next()) {
                    String a = (rs.getString(2));
                    String b = (rs.getString(3));
                    String c = (rs.getString(4));
                    String d = (rs.getString(5));
                    String e = (rs.getString(6));
                    String f = (rs.getString(7));
                    String g = (rs.getString(8));
                    String h = (rs.getString(9));
                    dtm.addRow(new Object[]{a, b, c, d, e, f, g, h});
                }
            } catch (Exception e) {
            }
            break;
            case 2:
                try {

                DefaultTableModel dtm = (DefaultTableModel) tblEmployeePayrollHistory.getModel();
                ResultSet rs = DB.DB.getData("SELECT * FROM payroll WHERE usr_username = '" + lblEmployeeUsername.getText() + "' AND pay_year like '" + txtSearchPayrollHistory.getText() + "%' order by pay_year ");
                dtm.setRowCount(0);

                while (rs.next()) {
                    String a = (rs.getString(2));
                    String b = (rs.getString(3));
                    String c = (rs.getString(4));
                    String d = (rs.getString(5));
                    String e = (rs.getString(6));
                    String f = (rs.getString(7));
                    String g = (rs.getString(8));
                    String h = (rs.getString(9));
                    dtm.addRow(new Object[]{a, b, c, d, e, f, g, h});
                }
            } catch (Exception e) {
            }
            break;
            case 3:
                try {

                DefaultTableModel dtm = (DefaultTableModel) tblEmployeePayrollHistory.getModel();
                ResultSet rs = DB.DB.getData("SELECT * FROM payroll WHERE usr_username = '" + lblEmployeeUsername.getText() + "' AND pay_date like '" + txtSearchPayrollHistory.getText() + "%' order by pay_date ");
                dtm.setRowCount(0);

                while (rs.next()) {
                    String a = (rs.getString(2));
                    String b = (rs.getString(3));
                    String c = (rs.getString(4));
                    String d = (rs.getString(5));
                    String e = (rs.getString(6));
                    String f = (rs.getString(7));
                    String g = (rs.getString(8));
                    String h = (rs.getString(9));
                    dtm.addRow(new Object[]{a, b, c, d, e, f, g, h});
                }
            } catch (Exception e) {
            }
            break;
            case 4:
                try {

                DefaultTableModel dtm = (DefaultTableModel) tblEmployeePayrollHistory.getModel();
                ResultSet rs = DB.DB.getData("SELECT * FROM payroll WHERE usr_username = '" + lblEmployeeUsername.getText() + "' AND pay_attendance like '" + txtSearchPayrollHistory.getText() + "%' order by pay_attendance ");
                dtm.setRowCount(0);

                while (rs.next()) {
                    String a = (rs.getString(2));
                    String b = (rs.getString(3));
                    String c = (rs.getString(4));
                    String d = (rs.getString(5));
                    String e = (rs.getString(6));
                    String f = (rs.getString(7));
                    String g = (rs.getString(8));
                    String h = (rs.getString(9));
                    dtm.addRow(new Object[]{a, b, c, d, e, f, g, h});
                }
            } catch (Exception e) {
            }
            break;
            case 5:
                try {
                DefaultTableModel dtm = (DefaultTableModel) tblEmployeePayrollHistory.getModel();
                ResultSet rs = DB.DB.getData("SELECT * FROM payroll WHERE usr_username = '" + lblEmployeeUsername.getText() + "' AND pay_salary like '" + txtSearchPayrollHistory.getText() + "%' order by pay_salary ");
                dtm.setRowCount(0);

                while (rs.next()) {
                    String a = (rs.getString(2));
                    String b = (rs.getString(3));
                    String c = (rs.getString(4));
                    String d = (rs.getString(5));
                    String e = (rs.getString(6));
                    String f = (rs.getString(7));
                    String g = (rs.getString(8));
                    String h = (rs.getString(9));
                    dtm.addRow(new Object[]{a, b, c, d, e, f, g, h});
                }
            } catch (Exception e) {
            }
            break;
            default:
                break;
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
        pnlHomeSubSelection = new javax.swing.JPanel();
        lblDate = new javax.swing.JLabel();
        lblTime = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        pnlLogout = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        jPanel11 = new javax.swing.JPanel();
        jTabbedPanePayroll = new javax.swing.JTabbedPane();
        jPanel4 = new javax.swing.JPanel();
        txtSearchPayrollHistory = new javax.swing.JTextField();
        jLabel12 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tblEmployeePayrollHistory = new javax.swing.JTable();
        jButton9 = new javax.swing.JButton();
        cmbSortEmployeePayrollHistory = new javax.swing.JComboBox<>();

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
        jLabel2.setText("Payroll");
        jLabel2.setToolTipText("");

        jLabel3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/frames/img/icons8_money_transfer_50px.png"))); // NOI18N

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

        jTabbedPanePayroll.setBackground(new java.awt.Color(34, 34, 34));
        jTabbedPanePayroll.setForeground(new java.awt.Color(255, 255, 255));
        jTabbedPanePayroll.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jTabbedPanePayroll.setOpaque(true);

        jPanel4.setBackground(new java.awt.Color(34, 34, 34));

        txtSearchPayrollHistory.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        txtSearchPayrollHistory.setForeground(new java.awt.Color(255, 255, 255));
        txtSearchPayrollHistory.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtSearchPayrollHistoryKeyReleased(evt);
            }
        });

        jLabel12.setIcon(new javax.swing.ImageIcon(getClass().getResource("/frames/img/search.icon.png"))); // NOI18N

        tblEmployeePayrollHistory.setBackground(new java.awt.Color(34, 34, 34));
        tblEmployeePayrollHistory.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        tblEmployeePayrollHistory.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Employee Id", "Username", "Month", "Year", "Date", "Attendance", "Payment", "Status"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblEmployeePayrollHistory.setGridColor(new java.awt.Color(61, 61, 61));
        tblEmployeePayrollHistory.setShowGrid(true);
        tblEmployeePayrollHistory.getTableHeader().setReorderingAllowed(false);
        jScrollPane2.setViewportView(tblEmployeePayrollHistory);

        jButton9.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jButton9.setForeground(new java.awt.Color(255, 255, 255));
        jButton9.setText("Refresh");
        jButton9.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jButton9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton9ActionPerformed(evt);
            }
        });

        cmbSortEmployeePayrollHistory.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        cmbSortEmployeePayrollHistory.setForeground(new java.awt.Color(255, 255, 255));
        cmbSortEmployeePayrollHistory.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Recent", "Month", "Year", "Date", "Attendance", "Payment" }));
        cmbSortEmployeePayrollHistory.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbSortEmployeePayrollHistoryActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addGap(52, 52, 52)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jButton9)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 1096, Short.MAX_VALUE)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(txtSearchPayrollHistory, javax.swing.GroupLayout.DEFAULT_SIZE, 873, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(cmbSortEmployeePayrollHistory, 0, 173, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel12)))
                .addGap(52, 52, 52))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(23, 23, 23)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtSearchPayrollHistory, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel12)
                    .addComponent(cmbSortEmployeePayrollHistory, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 393, Short.MAX_VALUE)
                .addGap(25, 25, 25)
                .addComponent(jButton9)
                .addGap(36, 36, 36))
        );

        jTabbedPanePayroll.addTab("Payroll History", jPanel4);

        javax.swing.GroupLayout jPanel11Layout = new javax.swing.GroupLayout(jPanel11);
        jPanel11.setLayout(jPanel11Layout);
        jPanel11Layout.setHorizontalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPanePayroll)
        );
        jPanel11Layout.setVerticalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPanePayroll)
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

    private void txtSearchPayrollHistoryKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtSearchPayrollHistoryKeyReleased
        searchPaymentHistory();
    }//GEN-LAST:event_txtSearchPayrollHistoryKeyReleased

    private void cmbSortEmployeePayrollHistoryActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbSortEmployeePayrollHistoryActionPerformed
        searchPaymentHistory();
    }//GEN-LAST:event_cmbSortEmployeePayrollHistoryActionPerformed

    private void jButton9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton9ActionPerformed
        refreshEmployeePayrollHistoryTable();
        txtSearchPayrollHistory.setText(null);
    }//GEN-LAST:event_jButton9ActionPerformed

    private void jLabel4MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel4MouseClicked
        int choose = JOptionPane.showConfirmDialog(this,
                "Do you want to Logout the application?",
                "Confirm Logout", JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE);

        if (choose == JOptionPane.YES_OPTION) {
            try {

                Date DateDate = new Date();
                SimpleDateFormat toDate = new SimpleDateFormat("yyyy-MM-dd");
                date = "" + toDate.format(DateDate);

                Date DateTime = new Date();
                SimpleDateFormat toTime = new SimpleDateFormat("HH:mm:ss");
                time = "" + toTime.format(DateTime);

                String logoutActivityLog = "INSERT INTO log(usr_id, usr_type, usr_username, log_activity, log_description, log_state) VALUES ((SELECT usr_id FROM user WHERE usr_username = '" + lblEmployeeUsername.getText() + "'),'Employee','" + lblEmployeeUsername.getText() + "','Employee Logout', '" + lblEmployeeUsername.getText() + " logged out' ,'SUCCESS')";
                DB.DB.putData("UPDATE employee_attendance SET att_logout_time = '" + time + "' WHERE usr_username = '" + lblEmployeeUsername.getText() + "' AND  att_date = '" + date + "' ");

                DB.DB.putData(logoutActivityLog);

                EmployeeEmpPayroll.this.dispose();

                Login login = new Login();
                login.setVisible(true);
                for (float j = 0f; j < 0.985; j += 0.000001) {
                    login.setOpacity(j);
                }
            } catch (Exception ex) {
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
                new EmployeeEmpPayroll().setVisible(true);
            }
        });
    }

    private void setIcon() {
        setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("img/exfiltrat0z-icon.png")));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox<String> cmbSortEmployeePayrollHistory;
    private javax.swing.JButton jButton9;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTabbedPane jTabbedPanePayroll;
    private javax.swing.JLabel lblDate;
    public static final javax.swing.JLabel lblEmployeeUsername = new javax.swing.JLabel();
    private javax.swing.JLabel lblTime;
    private javax.swing.JPanel pnlHomeSubSelection;
    private javax.swing.JPanel pnlLogout;
    private javax.swing.JTable tblEmployeePayrollHistory;
    private javax.swing.JTextField txtSearchPayrollHistory;
    // End of variables declaration//GEN-END:variables
}
