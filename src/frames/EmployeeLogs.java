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
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.view.JasperViewer;

/**
 *
 * @author 0x5un5h1n3
 */
public class EmployeeLogs extends javax.swing.JFrame {

    Timer timer;
    String month;
    String year;
    String date;
    String today;
    String time;

    public EmployeeLogs() {
        initComponents();
        EmployeeLogs.this.setExtendedState(JFrame.MAXIMIZED_BOTH);
        EmployeeLogs.this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setIcon();
        setTableheader();
        refreshSystemLogsTable();

        EmployeeLogs.this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {

                EmployeeLogs.this.dispose();                
                EmployeeDashboard ad = new EmployeeDashboard();
                EmployeeDashboard.lblEmployeeUsername.setText(lblEmployeeUsername.getText());
                ad.setVisible(true);

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
        JTableHeader emplogtbl = tblSystemLogs.getTableHeader();
        emplogtbl.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        emplogtbl.setForeground(Color.white);
        emplogtbl.setBackground(new Color(51, 51, 51));
        tblSystemLogs.getTableHeader().setReorderingAllowed(false);
    }

    private void calculateSystemLogCount() {
        int count = tblSystemLogs.getRowCount();
        lblLogCount.setText("Log(s) Count : " + Integer.toString(count));

    }

    private void refreshSystemLogsTable() {

        try {
            DefaultTableModel dtm = (DefaultTableModel) tblSystemLogs.getModel();
            ResultSet rs = DB.DB.getData("SELECT * FROM log WHERE usr_type = 'Employee' AND usr_username = '"+lblEmployeeUsername.getText()+"' order by log_timestamp desc ");
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

            calculateSystemLogCount();
            txtSearchSystemLogs.setText(null);
            cmbSortSystemLogs.setSelectedIndex(0);

        } catch (Exception e) {
        }

    }

    private void searchSystemLogs() {
        switch (cmbSortSystemLogs.getSelectedIndex()) {
            case 0:

                try {
                DefaultTableModel dtm = (DefaultTableModel) tblSystemLogs.getModel();
                ResultSet rs = DB.DB.getData("SELECT * FROM log WHERE usr_type = 'Employee' AND usr_username = '"+lblEmployeeUsername.getText()+"' AND usr_id like '" + txtSearchSystemLogs.getText() + "%' AND usr_type = 'Employee' || log_activity like '" + txtSearchSystemLogs.getText() + "%' AND usr_type = 'Employee' || log_description like '" + txtSearchSystemLogs.getText() + "%' AND usr_type = 'Employee' || log_state like '" + txtSearchSystemLogs.getText() + "%' AND usr_type = 'Employee' || log_timestamp like '" + txtSearchSystemLogs.getText() + "%' AND usr_type = 'Employee' order by log_timestamp desc");

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

                calculateSystemLogCount();

            } catch (Exception e) {
            }
            break;
            case 1:

                try {
                DefaultTableModel dtm = (DefaultTableModel) tblSystemLogs.getModel();
                ResultSet rs = DB.DB.getData("SELECT * FROM log WHERE usr_type = 'Employee' AND usr_username = '"+lblEmployeeUsername.getText()+"' AND usr_id like '" + txtSearchSystemLogs.getText() + "%' AND usr_type = 'Employee' AND log_activity = 'Intruder Login' || usr_type like '" + txtSearchSystemLogs.getText() + "%' AND usr_type = 'Employee' AND log_activity = 'Intruder Login' || log_activity like '" + txtSearchSystemLogs.getText() + "%' AND usr_type = 'Employee' AND log_activity = 'Intruder Login' || log_description like '" + txtSearchSystemLogs.getText() + "%' AND usr_type = 'Employee' AND log_activity = 'Intruder Login' || log_state like '" + txtSearchSystemLogs.getText() + "%' AND usr_type = 'Employee' AND log_activity = 'Intruder Login' || log_timestamp like '" + txtSearchSystemLogs.getText() + "%' AND usr_type = 'Employee' AND log_activity = 'Intruder Login' order by log_timestamp desc");

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

                calculateSystemLogCount();

            } catch (Exception e) {
            }
            break;
            case 2:

                try {
                DefaultTableModel dtm = (DefaultTableModel) tblSystemLogs.getModel();
                ResultSet rs = DB.DB.getData("SELECT * FROM log WHERE usr_type = 'Employee' AND usr_username = '"+lblEmployeeUsername.getText()+"' AND usr_id like '" + txtSearchSystemLogs.getText() + "%' AND usr_type = 'Employee' AND log_activity = 'Employee Login' || usr_type like '" + txtSearchSystemLogs.getText() + "%' AND usr_type = 'Employee' AND log_activity = 'Employee Login' || log_activity like '" + txtSearchSystemLogs.getText() + "%' AND usr_type = 'Employee' AND log_activity = 'Employee Login' || log_description like '" + txtSearchSystemLogs.getText() + "%' AND usr_type = 'Employee' AND log_activity = 'Employee Login' || log_state like '" + txtSearchSystemLogs.getText() + "%' AND usr_type = 'Employee' AND log_activity = 'Employee Login' || log_timestamp like '" + txtSearchSystemLogs.getText() + "%' AND usr_type = 'Employee' AND log_activity = 'Employee Login' order by log_timestamp desc");

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

                calculateSystemLogCount();

            } catch (Exception e) {
            }
            break;
            case 3:

                try {
                DefaultTableModel dtm = (DefaultTableModel) tblSystemLogs.getModel();
                ResultSet rs = DB.DB.getData("SELECT * FROM log WHERE usr_type = 'Employee' AND usr_username = '"+lblEmployeeUsername.getText()+"' AND usr_id like '" + txtSearchSystemLogs.getText() + "%' AND usr_type = 'Employee' AND log_activity = 'Employee Logout' || usr_type like '" + txtSearchSystemLogs.getText() + "%' AND usr_type = 'Employee' AND log_activity = 'Employee Logout' || log_activity like '" + txtSearchSystemLogs.getText() + "%' AND usr_type = 'Employee' AND log_activity = 'Employee Logout' || log_description like '" + txtSearchSystemLogs.getText() + "%' AND usr_type = 'Employee' AND log_activity = 'Employee Logout' || log_state like '" + txtSearchSystemLogs.getText() + "%' AND usr_type = 'Employee' AND log_activity = 'Employee Logout' || log_timestamp like '" + txtSearchSystemLogs.getText() + "%' AND usr_type = 'Employee' AND log_activity = 'Employee Logout' order by log_timestamp desc");

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

                calculateSystemLogCount();

            } catch (Exception e) {
            }
            break;
            
            case 4:

                try {
                DefaultTableModel dtm = (DefaultTableModel) tblSystemLogs.getModel();
                ResultSet rs = DB.DB.getData("SELECT * FROM log WHERE usr_type = 'Employee' AND usr_username = '"+lblEmployeeUsername.getText()+"'  AND usr_id like '" + txtSearchSystemLogs.getText() + "%' AND usr_type = 'Employee' AND log_activity = 'Employee System Exit' || usr_type like '" + txtSearchSystemLogs.getText() + "%' AND usr_type = 'Employee' AND log_activity = 'Employee System Exit' || log_activity like '" + txtSearchSystemLogs.getText() + "%' AND usr_type = 'Employee' AND log_activity = 'Employee System Exit' || log_description like '" + txtSearchSystemLogs.getText() + "%' AND usr_type = 'Employee' AND log_activity = 'Employee System Exit' || log_state like '" + txtSearchSystemLogs.getText() + "%' AND usr_type = 'Employee' AND log_activity = 'Employee System Exit' || log_timestamp like '" + txtSearchSystemLogs.getText() + "%' AND usr_type = 'Employee' AND log_activity = 'Employee System Exit' order by log_timestamp desc");

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

                calculateSystemLogCount();

            } catch (Exception e) {
            }
            break;
            case 5:

                try {
                DefaultTableModel dtm = (DefaultTableModel) tblSystemLogs.getModel();
                ResultSet rs = DB.DB.getData("SELECT * FROM log WHERE usr_type = 'Employee' AND usr_username = '"+lblEmployeeUsername.getText()+"' AND usr_type = 'Employee' AND usr_id like '" + txtSearchSystemLogs.getText() + "%' AND usr_type = 'Employee' AND log_activity = 'Employee Payroll' || usr_type like '" + txtSearchSystemLogs.getText() + "%' AND usr_type = 'Employee' AND usr_type = 'Employee' AND log_activity = 'Employee Payroll' || log_activity like '" + txtSearchSystemLogs.getText() + "%' AND usr_type = 'Employee' AND log_activity = 'Employee Payroll' || log_description like '" + txtSearchSystemLogs.getText() + "%' AND usr_type = 'Employee' AND log_activity = 'Employee Payroll' || log_state like '" + txtSearchSystemLogs.getText() + "%' AND usr_type = 'Employee' AND log_activity = 'Employee Payroll' || log_timestamp like '" + txtSearchSystemLogs.getText() + "%'  AND log_activity = 'Employee Payroll' order by log_timestamp desc");

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

                calculateSystemLogCount();

            } catch (Exception e) {
            }
            break;
            case 6:

                try {
                DefaultTableModel dtm = (DefaultTableModel) tblSystemLogs.getModel();
                ResultSet rs = DB.DB.getData("SELECT * FROM log WHERE usr_type = 'Employee' AND usr_username = '"+lblEmployeeUsername.getText()+"' AND usr_id like '" + txtSearchSystemLogs.getText() + "%' AND usr_type = 'Employee' AND log_activity = 'Issue Invoice / Items' || usr_type like '" + txtSearchSystemLogs.getText() + "%' AND usr_type = 'Employee' AND log_activity = 'Issue Invoice / Items' || log_activity like '" + txtSearchSystemLogs.getText() + "%' AND usr_type = 'Employee' AND log_activity = 'Issue Invoice / Items' || log_description like '" + txtSearchSystemLogs.getText() + "%' AND usr_type = 'Employee' AND log_activity = 'Issue Invoice / Items' || log_state like '" + txtSearchSystemLogs.getText() + "%' AND usr_type = 'Employee' AND log_activity = 'Issue Invoice / Items' || log_timestamp like '" + txtSearchSystemLogs.getText() + "%' AND usr_type = 'Employee' AND log_activity = 'Issue Invoice / Items' order by log_timestamp desc");

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

                calculateSystemLogCount();

            } catch (Exception e) {
            }
            break;
            
            case 7:

                try {
                DefaultTableModel dtm = (DefaultTableModel) tblSystemLogs.getModel();
                ResultSet rs = DB.DB.getData("SELECT * FROM log WHERE usr_type = 'Employee' AND usr_username = '"+lblEmployeeUsername.getText()+"' AND usr_id like '" + txtSearchSystemLogs.getText() + "%' AND usr_type = 'Employee' AND log_activity = 'Change Password' || usr_type like '" + txtSearchSystemLogs.getText() + "%' AND usr_type = 'Employee' AND log_activity = 'Change Password' || log_activity like '" + txtSearchSystemLogs.getText() + "%' AND usr_type = 'Employee' AND log_activity = 'Change Password' || log_description like '" + txtSearchSystemLogs.getText() + "%' AND usr_type = 'Employee' AND log_activity = 'Change Password' || log_state like '" + txtSearchSystemLogs.getText() + "%' AND usr_type = 'Employee' AND log_activity = 'Change Password' || log_timestamp like '" + txtSearchSystemLogs.getText() + "%' AND usr_type = 'Employee' AND log_activity = 'Change Password' order by log_timestamp desc");

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

                calculateSystemLogCount();

            } catch (Exception e) {
            }
            break;
            default:
                break;
        }
    }

    public void printLogs() {
        new Thread() {
            @Override
            public void run() {

                try {

                    String reportSource = "C:\\Program Files\\Exfiltro Reports\\viewLogReport.jrxml";

                    JasperDesign jdesign = JRXmlLoader.load(reportSource);
                    JasperReport jreport = JasperCompileManager.compileReport(jdesign);
                    JasperPrint jprint = JasperFillManager.fillReport(jreport, null, DB.DB.getConnection());

                    JasperViewer.viewReport(jprint, false);

                } catch (JRException ex) {

                }
            }
        }.start();
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
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel11 = new javax.swing.JPanel();
        txtSearchSystemLogs = new javax.swing.JTextField();
        cmbSortSystemLogs = new javax.swing.JComboBox<>();
        jLabel11 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblSystemLogs = new javax.swing.JTable();
        lblLogCount = new javax.swing.JLabel();
        jButton7 = new javax.swing.JButton();

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
        jLabel2.setText("Logs");
        jLabel2.setToolTipText("");

        jLabel3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/frames/img/icons8_time_machine_50px.png"))); // NOI18N

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

        jTabbedPane1.setBackground(new java.awt.Color(34, 34, 34));
        jTabbedPane1.setForeground(new java.awt.Color(255, 255, 255));
        jTabbedPane1.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jTabbedPane1.setOpaque(true);

        jPanel11.setBackground(new java.awt.Color(34, 34, 34));

        txtSearchSystemLogs.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        txtSearchSystemLogs.setForeground(new java.awt.Color(255, 255, 255));
        txtSearchSystemLogs.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtSearchSystemLogsKeyReleased(evt);
            }
        });

        cmbSortSystemLogs.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        cmbSortSystemLogs.setForeground(new java.awt.Color(255, 255, 255));
        cmbSortSystemLogs.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "View All Logs", "Intruder Login", "Employee Login", "Employee Logout", "Employee System Exit", "Employee Payroll", "Issue Invoice / Items", "Change Password" }));
        cmbSortSystemLogs.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbSortSystemLogsActionPerformed(evt);
            }
        });

        jLabel11.setIcon(new javax.swing.ImageIcon(getClass().getResource("/frames/img/search.icon.png"))); // NOI18N

        tblSystemLogs.setBackground(new java.awt.Color(34, 34, 34));
        tblSystemLogs.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        tblSystemLogs.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "User Id", "User Type", "Username", "Activity", "Description", "State", "Date / Time"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblSystemLogs.setGridColor(new java.awt.Color(61, 61, 61));
        tblSystemLogs.setShowGrid(true);
        tblSystemLogs.getTableHeader().setReorderingAllowed(false);
        jScrollPane1.setViewportView(tblSystemLogs);

        lblLogCount.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        lblLogCount.setText("Log Count : 0");

        jButton7.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jButton7.setForeground(new java.awt.Color(255, 255, 255));
        jButton7.setText(" Refresh");
        jButton7.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jButton7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton7ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel11Layout = new javax.swing.GroupLayout(jPanel11);
        jPanel11.setLayout(jPanel11Layout);
        jPanel11Layout.setHorizontalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addGap(54, 54, 54)
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel11Layout.createSequentialGroup()
                        .addComponent(lblLogCount, javax.swing.GroupLayout.PREFERRED_SIZE, 374, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButton7))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 1095, Short.MAX_VALUE)
                    .addGroup(jPanel11Layout.createSequentialGroup()
                        .addComponent(txtSearchSystemLogs)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(cmbSortSystemLogs, javax.swing.GroupLayout.PREFERRED_SIZE, 261, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel11)))
                .addGap(51, 51, 51))
        );
        jPanel11Layout.setVerticalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addGap(27, 27, 27)
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel11)
                    .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(txtSearchSystemLogs, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(cmbSortSystemLogs, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 409, Short.MAX_VALUE)
                .addGap(18, 18, 18)
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblLogCount)
                    .addComponent(jButton7))
                .addGap(23, 23, 23))
        );

        jTabbedPane1.addTab("System Logs", jPanel11);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(pnlHomeSubSelection, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(33, 33, 33)
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 153, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(lblEmployeeUsername, javax.swing.GroupLayout.PREFERRED_SIZE, 134, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(29, 29, 29)
                .addComponent(pnlLogout, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
            .addComponent(jTabbedPane1, javax.swing.GroupLayout.Alignment.TRAILING)
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
                .addComponent(jTabbedPane1))
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

                EmployeeLogs.this.dispose();

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

    private void jButton7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton7ActionPerformed
        refreshSystemLogsTable();
    }//GEN-LAST:event_jButton7ActionPerformed

    private void txtSearchSystemLogsKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtSearchSystemLogsKeyReleased
        searchSystemLogs();
    }//GEN-LAST:event_txtSearchSystemLogsKeyReleased

    private void cmbSortSystemLogsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbSortSystemLogsActionPerformed
        searchSystemLogs();
    }//GEN-LAST:event_cmbSortSystemLogsActionPerformed

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
                new EmployeeLogs().setVisible(true);
            }
        });
    }

    private void setIcon() {
        setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("img/exfiltrat0z-icon.png")));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox<String> cmbSortSystemLogs;
    private javax.swing.JButton jButton7;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JLabel lblDate;
    public static final javax.swing.JLabel lblEmployeeUsername = new javax.swing.JLabel();
    private javax.swing.JLabel lblLogCount;
    private javax.swing.JLabel lblTime;
    private javax.swing.JPanel pnlHomeSubSelection;
    private javax.swing.JPanel pnlLogout;
    private javax.swing.JTable tblSystemLogs;
    private javax.swing.JTextField txtSearchSystemLogs;
    // End of variables declaration//GEN-END:variables
}
