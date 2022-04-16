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
import java.io.File;
import java.sql.ResultSet;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import javax.swing.ImageIcon;
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
public class AdminEmployee extends javax.swing.JFrame {

    Timer timer;
    String month;
    String year;
    String date;
    String today;

    public AdminEmployee() {
        initComponents();
        AdminEmployee.this.setExtendedState(JFrame.MAXIMIZED_BOTH);
        AdminEmployee.this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setIcon();
        setTableheader();
        generateEMPId();
        refreshEmployeeTable();

        AdminEmployee.this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {

                AdminEmployee.this.dispose();
                AdminDashboard ad = new AdminDashboard();
                AdminDashboard.lblAdminUsername.setText(lblAdminUsername.getText());
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
        JTableHeader emptbl = tblEmployee.getTableHeader();
        emptbl.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        emptbl.setForeground(Color.white);
        emptbl.setBackground(new Color(51, 51, 51));
        tblEmployee.getTableHeader().setReorderingAllowed(false);
    }

    private void generateEMPId() {
        try {

            ResultSet rs = DB.DB.getData("SELECT usr_id AS x FROM user ORDER BY usr_id DESC LIMIT 1");
            if (rs.next()) {

                int rowcount = Integer.parseInt(rs.getString("x"));
                rowcount++;

                this.txtEmployeeId.setText("" + rowcount);

            }

        } catch (Exception e) {
        }
    }

    private void addNewEmployee() {

        int option = JOptionPane.showConfirmDialog(this, "Do you want to Add this Employee?", "", JOptionPane.YES_NO_OPTION);
        if (option == 0) {
            if (!txtEmployeeUsername.getText().equals("")
                    & !txtEmployeePosition.getText().equals("")
                    & !txtEmployeePhoneNo.getText().equals("")
                    & (!String.valueOf(pwdEmployeePassword.getPassword()).equals("")
                    & (!String.valueOf(pwdRepeatEmployeePassword.getPassword()).equals("")))) {

                try {
                    ResultSet rs = DB.DB.getData("SELECT * FROM user WHERE usr_username like '" + txtEmployeeUsername.getText() + "%' order by usr_id ");

                    if (rs.next()) {
                        JOptionPane.showMessageDialog(this, "Username Already Exists!", "Invalid Username", JOptionPane.WARNING_MESSAGE);

                    } else if (!(String.valueOf(pwdEmployeePassword.getPassword()).length() >= 4)) {
                        JOptionPane.showMessageDialog(this, "Password is Too Short!", "Invalid Password", JOptionPane.WARNING_MESSAGE); //Short password
                        pwdEmployeePassword.setText(null);
                        pwdRepeatEmployeePassword.setText(null);
                        pwdEmployeePassword.grabFocus();

                    } else if (!String.valueOf(pwdEmployeePassword.getPassword()).equals(String.valueOf(pwdRepeatEmployeePassword.getPassword()))) {
                        JOptionPane.showMessageDialog(this, "Password doesn't match!", "Invalid Password Matching", JOptionPane.WARNING_MESSAGE); //invalid password matching
                        pwdEmployeePassword.setText(null);
                        pwdRepeatEmployeePassword.setText(null);
                        pwdEmployeePassword.grabFocus();

                    } else if (!(txtEmployeeNIC.getText().length() >= 9)) {
                        JOptionPane.showMessageDialog(this, "Invalid NIC!", "Invalid Data", JOptionPane.WARNING_MESSAGE);
                        txtEmployeeNIC.setText(null);
                        txtEmployeeNIC.grabFocus();

                    } else if (txtEmployeePhoneNo.getText().length() != 10) {
                        JOptionPane.showMessageDialog(this, "Invalid Phone Number Length!", "Invalid Data", JOptionPane.WARNING_MESSAGE);
                        txtEmployeePhoneNo.setText(null);
                        txtEmployeePhoneNo.grabFocus();

                    } else {
                        Date DateMonth = new Date();
                        SimpleDateFormat toMonth = new SimpleDateFormat("MMMM");
                        month = "" + toMonth.format(DateMonth);

                        Date DateYear = new Date();
                        SimpleDateFormat toYear = new SimpleDateFormat("yyyy");
                        year = "" + toYear.format(DateYear);

                        Date DateDate = new Date();
                        SimpleDateFormat toDate = new SimpleDateFormat("yyyy-MM-dd");
                        date = "" + toDate.format(DateDate);

                        String addEmployee = "INSERT INTO user(usr_username, usr_password, usr_type, usr_position, usr_nic, usr_phone, usr_stat) VALUES ('" + txtEmployeeUsername.getText().trim() + "','" + String.valueOf(pwdEmployeePassword.getPassword()) + "','Employee','" + txtEmployeePosition.getText() + "','" + txtEmployeeNIC.getText() + "','" + txtEmployeePhoneNo.getText() + "', '1')";
                        String fillDataToPayroll = "INSERT INTO payroll(usr_id, usr_username, pay_month, pay_year, pay_date, pay_attendance, pay_salary, pay_stat) VALUES ('" + txtEmployeeId.getText() + "','" + txtEmployeeUsername.getText() + "','" + month + "','" + year + "','" + date + "',(SELECT COUNT(usr_id) FROM employee_attendance WHERE usr_id = '" + txtEmployeeId.getText() + "' AND att_month = '" + month + "' AND att_year = '" + year + "'),'" + txtEmployeeSalary.getText() + "','UNPAID')";
                        String AddEmployeeActivityLog = "INSERT INTO log(usr_id, usr_type, usr_username, log_activity, log_description, log_state) VALUES ((SELECT usr_id FROM user WHERE usr_username = '" + lblAdminUsername.getText() + "'),'Admin','" + lblAdminUsername.getText() + "','Add New Employee','Admin added new employee " + txtEmployeeUsername.getText() + "','SUCCESS')";
                        try {
                            DB.DB.putData(addEmployee);
                            DB.DB.putData(fillDataToPayroll);
                            DB.DB.putData(AddEmployeeActivityLog);

                            refreshEmployeeTable();
                            ImageIcon icon = new ImageIcon(getClass().getResource("img/done.png"));
                            JOptionPane.showMessageDialog(this, "Employee Added!", "Done", JOptionPane.INFORMATION_MESSAGE, icon);

                            txtEmployeeUsername.setText(null);
                            txtEmployeePosition.setText(null);
                            txtEmployeeNIC.setText(null);
                            txtEmployeePhoneNo.setText(null);
                            txtEmployeeSalary.setText(null);
                            pwdEmployeePassword.setText(null);
                            pwdRepeatEmployeePassword.setText(null);
                            txtEmployeeUsername.grabFocus();

                        } catch (Exception e) {
                        }

                    }
                } catch (Exception ex) {
                }
            } else {
                JOptionPane.showMessageDialog(this, "Please Fill All the Fields!", "Invalid Data", JOptionPane.WARNING_MESSAGE);
                txtEmployeeUsername.grabFocus();
            }
        }
    }

    private void refreshEmployeeTable() {
        try {
            DefaultTableModel dtm = (DefaultTableModel) tblEmployee.getModel();
            ResultSet rs = DB.DB.getData("SELECT * FROM user WHERE usr_type = 'Employee' AND usr_stat = '1' ");
            ResultSet rs1 = DB.DB.getData("SELECT * FROM payroll");
            dtm.setRowCount(0);

            while (rs.next()) {
                String a = (rs.getString(1));
                String b = (rs.getString(2));
                String c = (rs.getString(5));
                String d = (rs.getString(6));
                String e = (rs.getString(7));
                dtm.addRow(new Object[]{a, b, c, d, e});
            }

            lblEmployeeCount.setText("Employee Count : " + tblEmployee.getRowCount());

        } catch (Exception e) {
        }

    }

    private void searchEmployee() {
        switch (cmbSortEmployee.getSelectedIndex()) {
            case 0:

                try {
                DefaultTableModel dtm = (DefaultTableModel) tblEmployee.getModel();
                ResultSet rs = DB.DB.getData("SELECT * FROM user WHERE usr_type = 'Employee' AND usr_stat = '1' AND usr_id like '" + txtSearchEmployee.getText() + "%' order by usr_id ");
                dtm.setRowCount(0);

                while (rs.next()) {
                    String a = (rs.getString(1));
                    String b = (rs.getString(2));
                    String c = (rs.getString(5));
                    String d = (rs.getString(6));
                    String e = (rs.getString(7));
                    dtm.addRow(new Object[]{a, b, c, d, e});
                }

            } catch (Exception e) {
            }
            break;
            case 1:

                try {
                DefaultTableModel dtm = (DefaultTableModel) tblEmployee.getModel();
                ResultSet rs = DB.DB.getData("SELECT * FROM user WHERE usr_type = 'Employee' AND usr_stat = '1' AND usr_username like '" + txtSearchEmployee.getText() + "%' order by usr_username ");
                dtm.setRowCount(0);

                while (rs.next()) {
                    String a = (rs.getString(1));
                    String b = (rs.getString(2));
                    String c = (rs.getString(5));
                    String d = (rs.getString(6));
                    String e = (rs.getString(7));
                    dtm.addRow(new Object[]{a, b, c, d, e});
                }

            } catch (Exception e) {
            }
            break;
            case 2:
                try {
                DefaultTableModel dtm = (DefaultTableModel) tblEmployee.getModel();
                ResultSet rs = DB.DB.getData("SELECT * FROM user WHERE usr_type = 'Employee' AND usr_stat = '1' AND usr_position like '" + txtSearchEmployee.getText() + "%' order by usr_position ");
                dtm.setRowCount(0);

                while (rs.next()) {
                    String a = (rs.getString(1));
                    String b = (rs.getString(2));
                    String c = (rs.getString(5));
                    String d = (rs.getString(6));
                    String e = (rs.getString(7));
                    dtm.addRow(new Object[]{a, b, c, d, e});
                }
            } catch (Exception e) {
            }
            break;
            case 3:
                try {
                DefaultTableModel dtm = (DefaultTableModel) tblEmployee.getModel();
                ResultSet rs = DB.DB.getData("SELECT * FROM user WHERE usr_type = 'Employee' AND usr_stat = '1' AND usr_nic like '" + txtSearchEmployee.getText() + "%' order by usr_nic ");
                dtm.setRowCount(0);

                while (rs.next()) {
                    String a = (rs.getString(1));
                    String b = (rs.getString(2));
                    String c = (rs.getString(5));
                    String d = (rs.getString(6));
                    String e = (rs.getString(7));
                    dtm.addRow(new Object[]{a, b, c, d, e});
                }
            } catch (Exception e) {
            }
            break;
            case 4:
                try {
                DefaultTableModel dtm = (DefaultTableModel) tblEmployee.getModel();
                ResultSet rs = DB.DB.getData("SELECT * FROM user WHERE usr_type = 'Employee' AND usr_stat = '1' AND usr_phone like '" + txtSearchEmployee.getText() + "%' order by usr_phone ");
                dtm.setRowCount(0);

                while (rs.next()) {
                    String a = (rs.getString(1));
                    String b = (rs.getString(2));
                    String c = (rs.getString(5));
                    String d = (rs.getString(6));
                    String e = (rs.getString(7));
                    dtm.addRow(new Object[]{a, b, c, d, e});
                }
            } catch (Exception e) {
            }
            break;
            default:
                break;
        }
    }

    private void deleteEmployee() {
        int option = JOptionPane.showConfirmDialog(this, "Do you want to Delete this Employee?", "", JOptionPane.YES_NO_OPTION);
        if (option == 0) {
            try {
                DB.DB.putData("UPDATE user SET usr_stat = '0' WHERE usr_id ='" + txtSelectedEmployeeId.getText() + "' ");
                String deleteEmployeeActivityLog = "INSERT INTO log(usr_id, usr_type, usr_username, log_activity, log_description, log_state) VALUES ((SELECT usr_id FROM user WHERE usr_username = '" + lblAdminUsername.getText() + "'),'Admin','" + lblAdminUsername.getText() + "','Delete Employee','Admin deleted employee " + txtEmployeeUsername.getText() + "','SUCCESS')";
                DB.DB.putData(deleteEmployeeActivityLog);
                ImageIcon icon = new ImageIcon(getClass().getResource("img/done.png"));
                JOptionPane.showMessageDialog(this, "Employee Deleted!", "Done", JOptionPane.INFORMATION_MESSAGE, icon);

                txtSearchEmployee.setText(null);
                txtSelectedEmployeeId.setText(null);
                txtSelectedEmployeeUsername.setText(null);
                txtSelectedEmployeePosition.setText(null);
                txtSelectedEmployeeNIC.setText(null);
                txtSelectedEmployeePhoneNo.setText(null);
                DefaultTableModel dtm = (DefaultTableModel) tblEmployee.getModel();
                dtm.setRowCount(0);
                refreshEmployeeTable();

            } catch (Exception e) {
            }
        } else {

        }
    }

    private void updateEmployee() {
        int option = JOptionPane.showConfirmDialog(this, "Do you want to Update this Employee?", "", JOptionPane.YES_NO_OPTION);
        if (option == 0) {

            if (!txtSelectedEmployeeUsername.getText().equals("")
                    & !txtSelectedEmployeePosition.getText().equals("")
                    & !txtSelectedEmployeePhoneNo.getText().equals("")) {

                if (!(txtSelectedEmployeePhoneNo.getText().length() == 10)) {
                    JOptionPane.showMessageDialog(this, "Invalid Phone Number!", "Invalid Data", JOptionPane.WARNING_MESSAGE);
                    txtSelectedEmployeePhoneNo.setText(null);
                    txtSelectedEmployeePhoneNo.grabFocus();

                } else if (!(txtSelectedEmployeeNIC.getText().length() >= 9)) {
                    JOptionPane.showMessageDialog(this, "Invalid NIC!", "Invalid Data", JOptionPane.WARNING_MESSAGE);

                    txtSelectedEmployeeNIC.setText(null);
                    txtSelectedEmployeeNIC.grabFocus();

                } else {

                    try {
                        DB.DB.putData("UPDATE user SET position ='" + txtSelectedEmployeePosition.getText() + "', nic ='" + txtSelectedEmployeeNIC.getText() + "', phone_no= '" + txtSelectedEmployeePhoneNo.getText() + "' WHERE usr_id ='" + txtSelectedEmployeeId.getText() + "' ");
                        String updateEmployeeActivityLog = "INSERT INTO log(usr_id, usr_type, usr_username, log_activity, log_description, log_state) VALUES ((SELECT usr_id FROM user WHERE usr_username = '" + lblAdminUsername.getText() + "'),'Admin','" + lblAdminUsername.getText() + "','Update Employee','Admin updated employee " + txtEmployeeUsername.getText() + "','SUCCESS')";

                        DB.DB.putData(updateEmployeeActivityLog);
                        ImageIcon icon = new ImageIcon(getClass().getResource("img/done.png"));
                        JOptionPane.showMessageDialog(this, "Employee Updated Successfully!", "Done", JOptionPane.INFORMATION_MESSAGE, icon);

                        txtSelectedEmployeeId.setText("");
                        txtSelectedEmployeeUsername.setText("");
                        txtSelectedEmployeePosition.setText("");
                        txtSelectedEmployeeNIC.setText("");
                        txtSelectedEmployeePhoneNo.setText("");
                        DefaultTableModel dtm = (DefaultTableModel) tblEmployee.getModel();
                        dtm.setRowCount(0);
                        refreshEmployeeTable();

                    } catch (Exception e) {
                    }
                }
            } else {
                JOptionPane.showMessageDialog(this, "Please Fill All the Fields!", "Invalid Data", JOptionPane.WARNING_MESSAGE);
            }
        }
    }

    public void printAllEmployee() {

        new Thread() {
            @Override
            public void run() {
                try {

                    String reportSource = "C:\\Program Files\\Exfiltro Reports\\viewEmployeeReport.jrxml";

                    JasperDesign jdesign = JRXmlLoader.load(reportSource);
                    JasperReport jreport = JasperCompileManager.compileReport(jdesign);
                    JasperPrint jprint = JasperFillManager.fillReport(jreport, null, DB.DB.getConnection());
                    JasperViewer.viewReport(jprint, false);

                } catch (JRException ex) {
                }
            }
        }.start();
    }

    public void printSelectedEmployee() {

        Date DateMonth = new Date();
        SimpleDateFormat toMonth = new SimpleDateFormat("MMMM");
        month = "" + toMonth.format(DateMonth);

        Date DateYear = new Date();
        SimpleDateFormat toYear = new SimpleDateFormat("yyyy");
        year = "" + toYear.format(DateYear);

        HashMap a = new HashMap();
        a.put("id", txtSelectedEmployeeId.getText());
        a.put("month", month);
        a.put("year", year);

        new Thread() {
            @Override
            public void run() {
                try {

                    String reportSource = "C:\\Program Files\\Exfiltro Reports\\viewSelectedEmployeeReport.jrxml";

                    JasperDesign jdesign = JRXmlLoader.load(reportSource);
                    JasperReport jreport = JasperCompileManager.compileReport(jdesign);
                    JasperPrint jprint = JasperFillManager.fillReport(jreport, a, DB.DB.getConnection());
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
        jPanel11 = new javax.swing.JPanel();
        jTabbedPaneEmployee = new javax.swing.JTabbedPane();
        jPanel2 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        txtEmployeeId = new javax.swing.JTextField();
        txtEmployeeUsername = new javax.swing.JTextField();
        txtEmployeePosition = new javax.swing.JTextField();
        txtEmployeeNIC = new javax.swing.JTextField();
        txtEmployeePhoneNo = new javax.swing.JTextField();
        jLabel12 = new javax.swing.JLabel();
        txtEmployeeSalary = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        pwdEmployeePassword = new javax.swing.JPasswordField();
        jButton3 = new javax.swing.JButton();
        jLabel22 = new javax.swing.JLabel();
        pwdRepeatEmployeePassword = new javax.swing.JPasswordField();
        jPanel3 = new javax.swing.JPanel();
        txtSearchEmployee = new javax.swing.JTextField();
        jLabel11 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblEmployee = new javax.swing.JTable();
        jButton4 = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();
        jButton6 = new javax.swing.JButton();
        jButton7 = new javax.swing.JButton();
        lblEmployeeCount = new javax.swing.JLabel();
        cmbSortEmployee = new javax.swing.JComboBox<>();
        jPanel4 = new javax.swing.JPanel();
        jLabel13 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        txtSelectedEmployeePhoneNo = new javax.swing.JTextField();
        txtSelectedEmployeeNIC = new javax.swing.JTextField();
        txtSelectedEmployeePosition = new javax.swing.JTextField();
        txtSelectedEmployeeUsername = new javax.swing.JTextField();
        txtSelectedEmployeeId = new javax.swing.JTextField();
        jButton8 = new javax.swing.JButton();
        jButton9 = new javax.swing.JButton();
        jButton10 = new javax.swing.JButton();

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
        jLabel2.setText("Employee");
        jLabel2.setToolTipText("");

        jLabel3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/frames/img/icons8_worker_50px.png"))); // NOI18N

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

        lblAdminUsername.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        lblAdminUsername.setForeground(new java.awt.Color(255, 255, 255));
        lblAdminUsername.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblAdminUsername.setText("Admin");

        jPanel11.setBackground(new java.awt.Color(204, 204, 204));

        jTabbedPaneEmployee.setBackground(new java.awt.Color(34, 34, 34));
        jTabbedPaneEmployee.setForeground(new java.awt.Color(255, 255, 255));
        jTabbedPaneEmployee.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jTabbedPaneEmployee.setOpaque(true);

        jPanel2.setBackground(new java.awt.Color(34, 34, 34));

        jLabel1.setBackground(new java.awt.Color(0, 0, 0));
        jLabel1.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("Id");

        jLabel6.setBackground(new java.awt.Color(0, 0, 0));
        jLabel6.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(255, 255, 255));
        jLabel6.setText("Username");

        jLabel7.setBackground(new java.awt.Color(0, 0, 0));
        jLabel7.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(255, 255, 255));
        jLabel7.setText("Password");

        jLabel8.setBackground(new java.awt.Color(0, 0, 0));
        jLabel8.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(255, 255, 255));
        jLabel8.setText("Position");

        jLabel9.setBackground(new java.awt.Color(0, 0, 0));
        jLabel9.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(255, 255, 255));
        jLabel9.setText("NIC");

        jLabel10.setBackground(new java.awt.Color(0, 0, 0));
        jLabel10.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel10.setForeground(new java.awt.Color(255, 255, 255));
        jLabel10.setText("Phone No");

        txtEmployeeId.setBackground(new java.awt.Color(51, 51, 51));
        txtEmployeeId.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        txtEmployeeId.setText("1");
        txtEmployeeId.setEnabled(false);

        txtEmployeeUsername.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        txtEmployeeUsername.setForeground(new java.awt.Color(255, 255, 255));
        txtEmployeeUsername.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtEmployeeUsernameKeyPressed(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtEmployeeUsernameKeyTyped(evt);
            }
        });

        txtEmployeePosition.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        txtEmployeePosition.setForeground(new java.awt.Color(255, 255, 255));
        txtEmployeePosition.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtEmployeePositionKeyPressed(evt);
            }
        });

        txtEmployeeNIC.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        txtEmployeeNIC.setForeground(new java.awt.Color(255, 255, 255));
        txtEmployeeNIC.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtEmployeeNICKeyPressed(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtEmployeeNICKeyTyped(evt);
            }
        });

        txtEmployeePhoneNo.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        txtEmployeePhoneNo.setForeground(new java.awt.Color(255, 255, 255));
        txtEmployeePhoneNo.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtEmployeePhoneNoKeyPressed(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtEmployeePhoneNoKeyTyped(evt);
            }
        });

        jLabel12.setBackground(new java.awt.Color(0, 0, 0));
        jLabel12.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel12.setForeground(new java.awt.Color(255, 255, 255));
        jLabel12.setText("Salary");

        txtEmployeeSalary.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        txtEmployeeSalary.setForeground(new java.awt.Color(255, 255, 255));
        txtEmployeeSalary.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtEmployeeSalaryKeyPressed(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtEmployeeSalaryKeyTyped(evt);
            }
        });

        jButton1.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jButton1.setForeground(new java.awt.Color(255, 255, 255));
        jButton1.setText(" Add New Employee");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jButton2.setForeground(new java.awt.Color(255, 255, 255));
        jButton2.setText(" View Employee");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        pwdEmployeePassword.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        pwdEmployeePassword.setForeground(new java.awt.Color(255, 255, 255));
        pwdEmployeePassword.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                pwdEmployeePasswordKeyPressed(evt);
            }
        });

        jButton3.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jButton3.setForeground(new java.awt.Color(255, 255, 255));
        jButton3.setText(" Clear");
        jButton3.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        jLabel22.setBackground(new java.awt.Color(0, 0, 0));
        jLabel22.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel22.setForeground(new java.awt.Color(255, 255, 255));
        jLabel22.setText("Repeat Password");

        pwdRepeatEmployeePassword.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        pwdRepeatEmployeePassword.setForeground(new java.awt.Color(255, 255, 255));
        pwdRepeatEmployeePassword.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                pwdRepeatEmployeePasswordKeyPressed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(52, 52, 52)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1)
                    .addComponent(jLabel6)
                    .addComponent(jLabel8)
                    .addComponent(jLabel9)
                    .addComponent(jLabel10)
                    .addComponent(jLabel12)
                    .addComponent(jLabel7)
                    .addComponent(jLabel22))
                .addGap(51, 51, 51)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(txtEmployeePhoneNo, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 278, Short.MAX_VALUE)
                    .addComponent(txtEmployeeNIC, javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtEmployeePosition, javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(pwdEmployeePassword, javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtEmployeeId, javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtEmployeeUsername, javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtEmployeeSalary)
                    .addComponent(pwdRepeatEmployeePassword, javax.swing.GroupLayout.Alignment.LEADING))
                .addGap(78, 78, 78)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButton2)
                    .addComponent(jButton1)
                    .addComponent(jButton3))
                .addContainerGap(452, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(45, 45, 45)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(txtEmployeeId, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton1))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(txtEmployeeUsername, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton2))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel7)
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(pwdEmployeePassword, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jButton3)))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel22)
                    .addComponent(pwdRepeatEmployeePassword, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtEmployeePosition, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel8))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtEmployeeNIC, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel9))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtEmployeePhoneNo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel10))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtEmployeeSalary, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel12))
                .addContainerGap(160, Short.MAX_VALUE))
        );

        jTabbedPaneEmployee.addTab("New Employee", jPanel2);

        jPanel3.setBackground(new java.awt.Color(34, 34, 34));

        txtSearchEmployee.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        txtSearchEmployee.setForeground(new java.awt.Color(255, 255, 255));
        txtSearchEmployee.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtSearchEmployeeKeyReleased(evt);
            }
        });

        jLabel11.setIcon(new javax.swing.ImageIcon(getClass().getResource("/frames/img/search.icon.png"))); // NOI18N

        tblEmployee.setBackground(new java.awt.Color(34, 34, 34));
        tblEmployee.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        tblEmployee.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Employee Id", "Username", "Position", "NIC", "Phone No."
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblEmployee.setGridColor(new java.awt.Color(61, 61, 61));
        tblEmployee.setShowGrid(true);
        tblEmployee.getTableHeader().setReorderingAllowed(false);
        tblEmployee.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblEmployeeMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tblEmployee);

        jButton4.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jButton4.setForeground(new java.awt.Color(255, 255, 255));
        jButton4.setText(" Edit");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        jButton5.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jButton5.setForeground(new java.awt.Color(255, 255, 255));
        jButton5.setText(" Delete");
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });

        jButton6.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jButton6.setForeground(new java.awt.Color(255, 255, 255));
        jButton6.setText(" Print All");
        jButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton6ActionPerformed(evt);
            }
        });

        jButton7.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jButton7.setForeground(new java.awt.Color(255, 255, 255));
        jButton7.setText(" Refresh");
        jButton7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton7ActionPerformed(evt);
            }
        });

        lblEmployeeCount.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        lblEmployeeCount.setText("Employee Count : 0");

        cmbSortEmployee.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        cmbSortEmployee.setForeground(new java.awt.Color(255, 255, 255));
        cmbSortEmployee.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Id", "Name", "Position", "NIC", "Phone No." }));
        cmbSortEmployee.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbSortEmployeeActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(52, 52, 52)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 1096, Short.MAX_VALUE)
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addComponent(txtSearchEmployee, javax.swing.GroupLayout.DEFAULT_SIZE, 873, Short.MAX_VALUE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(cmbSortEmployee, 0, 173, Short.MAX_VALUE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel11))))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(65, 65, 65)
                        .addComponent(lblEmployeeCount, javax.swing.GroupLayout.PREFERRED_SIZE, 242, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButton7)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jButton4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jButton5)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jButton6)))
                .addGap(52, 52, 52))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(23, 23, 23)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtSearchEmployee, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel11)
                    .addComponent(cmbSortEmployee, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 388, Short.MAX_VALUE)
                .addGap(30, 30, 30)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton4)
                    .addComponent(jButton5)
                    .addComponent(jButton6)
                    .addComponent(jButton7)
                    .addComponent(lblEmployeeCount))
                .addGap(36, 36, 36))
        );

        jTabbedPaneEmployee.addTab("View Employee", jPanel3);

        jPanel4.setBackground(new java.awt.Color(34, 34, 34));

        jLabel13.setBackground(new java.awt.Color(0, 0, 0));
        jLabel13.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel13.setForeground(new java.awt.Color(255, 255, 255));
        jLabel13.setText("Id");

        jLabel14.setBackground(new java.awt.Color(0, 0, 0));
        jLabel14.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel14.setForeground(new java.awt.Color(255, 255, 255));
        jLabel14.setText("Username");

        jLabel16.setBackground(new java.awt.Color(0, 0, 0));
        jLabel16.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel16.setForeground(new java.awt.Color(255, 255, 255));
        jLabel16.setText("Position");

        jLabel17.setBackground(new java.awt.Color(0, 0, 0));
        jLabel17.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel17.setForeground(new java.awt.Color(255, 255, 255));
        jLabel17.setText("NIC");

        jLabel18.setBackground(new java.awt.Color(0, 0, 0));
        jLabel18.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel18.setForeground(new java.awt.Color(255, 255, 255));
        jLabel18.setText("Phone");

        txtSelectedEmployeePhoneNo.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        txtSelectedEmployeePhoneNo.setForeground(new java.awt.Color(255, 255, 255));
        txtSelectedEmployeePhoneNo.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtSelectedEmployeePhoneNoKeyPressed(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtSelectedEmployeePhoneNoKeyTyped(evt);
            }
        });

        txtSelectedEmployeeNIC.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        txtSelectedEmployeeNIC.setForeground(new java.awt.Color(255, 255, 255));
        txtSelectedEmployeeNIC.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtSelectedEmployeeNICKeyPressed(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtSelectedEmployeeNICKeyTyped(evt);
            }
        });

        txtSelectedEmployeePosition.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        txtSelectedEmployeePosition.setForeground(new java.awt.Color(255, 255, 255));
        txtSelectedEmployeePosition.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtSelectedEmployeePositionKeyPressed(evt);
            }
        });

        txtSelectedEmployeeUsername.setBackground(new java.awt.Color(51, 51, 51));
        txtSelectedEmployeeUsername.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        txtSelectedEmployeeUsername.setEnabled(false);

        txtSelectedEmployeeId.setBackground(new java.awt.Color(51, 51, 51));
        txtSelectedEmployeeId.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        txtSelectedEmployeeId.setEnabled(false);

        jButton8.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jButton8.setForeground(new java.awt.Color(255, 255, 255));
        jButton8.setText(" Update");
        jButton8.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jButton8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton8ActionPerformed(evt);
            }
        });

        jButton9.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jButton9.setForeground(new java.awt.Color(255, 255, 255));
        jButton9.setText(" Delete");
        jButton9.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jButton9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton9ActionPerformed(evt);
            }
        });

        jButton10.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jButton10.setForeground(new java.awt.Color(255, 255, 255));
        jButton10.setText(" Print");
        jButton10.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jButton10.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton10ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(52, 52, 52)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel13)
                    .addComponent(jLabel14)
                    .addComponent(jLabel16)
                    .addComponent(jLabel17)
                    .addComponent(jLabel18))
                .addGap(51, 51, 51)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(txtSelectedEmployeePhoneNo, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 278, Short.MAX_VALUE)
                    .addComponent(txtSelectedEmployeeNIC, javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtSelectedEmployeePosition, javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtSelectedEmployeeId, javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtSelectedEmployeeUsername, javax.swing.GroupLayout.Alignment.LEADING))
                .addGap(78, 78, 78)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButton9)
                    .addComponent(jButton8)
                    .addComponent(jButton10))
                .addContainerGap(586, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(45, 45, 45)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel13)
                    .addComponent(txtSelectedEmployeeId, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton8))
                .addGap(18, 18, 18)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel14)
                    .addComponent(txtSelectedEmployeeUsername, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton9))
                .addGap(18, 18, 18)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButton10)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtSelectedEmployeePosition, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel16))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtSelectedEmployeeNIC, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel17))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtSelectedEmployeePhoneNo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel18))))
                .addContainerGap(298, Short.MAX_VALUE))
        );

        jTabbedPaneEmployee.addTab("Edit Employee", jPanel4);

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
                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 153, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(lblAdminUsername, javax.swing.GroupLayout.PREFERRED_SIZE, 134, javax.swing.GroupLayout.PREFERRED_SIZE)
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
                        .addComponent(lblAdminUsername, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE))
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

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        addNewEmployee();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        jTabbedPaneEmployee.setSelectedIndex(1);
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        txtEmployeeUsername.setText(null);
        txtEmployeePosition.setText(null);
        txtEmployeeNIC.setText(null);
        txtEmployeePhoneNo.setText(null);
        txtEmployeeSalary.setText(null);
        pwdEmployeePassword.setText(null);
        pwdRepeatEmployeePassword.setText(null);
        txtEmployeeUsername.grabFocus();
    }//GEN-LAST:event_jButton3ActionPerformed

    private void txtEmployeeUsernameKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtEmployeeUsernameKeyTyped
        char c = evt.getKeyChar();
        if (!((Character.isDigit(c)) || (Character.isLetter(c)) || (Character.isISOControl(c)))) {
            evt.consume();
        }
    }//GEN-LAST:event_txtEmployeeUsernameKeyTyped

    private void txtEmployeeUsernameKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtEmployeeUsernameKeyPressed
        if (evt.getKeyCode() == 10) {
            pwdEmployeePassword.grabFocus();
        }
    }//GEN-LAST:event_txtEmployeeUsernameKeyPressed

    private void pwdEmployeePasswordKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_pwdEmployeePasswordKeyPressed
        if (evt.getKeyCode() == 10) {
            pwdRepeatEmployeePassword.grabFocus();
        }
    }//GEN-LAST:event_pwdEmployeePasswordKeyPressed

    private void pwdRepeatEmployeePasswordKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_pwdRepeatEmployeePasswordKeyPressed
        if (evt.getKeyCode() == 10) {
            txtEmployeePosition.grabFocus();
        }
    }//GEN-LAST:event_pwdRepeatEmployeePasswordKeyPressed

    private void txtEmployeePositionKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtEmployeePositionKeyPressed
        if (evt.getKeyCode() == 10) {
            txtEmployeeNIC.grabFocus();
        }
    }//GEN-LAST:event_txtEmployeePositionKeyPressed

    private void txtEmployeeNICKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtEmployeeNICKeyPressed
        if (evt.getKeyCode() == 10) {
            txtEmployeePhoneNo.grabFocus();
        }
    }//GEN-LAST:event_txtEmployeeNICKeyPressed

    private void txtEmployeeNICKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtEmployeeNICKeyTyped
        char c = evt.getKeyChar();
        if (!(Character.isDigit(c))) {
            evt.consume();

        }
        String nic = txtEmployeeNIC.getText();
        int length = nic.length();

        if (length >= 12) {
            evt.consume();
        }
    }//GEN-LAST:event_txtEmployeeNICKeyTyped

    private void txtEmployeePhoneNoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtEmployeePhoneNoKeyPressed
        if (evt.getKeyCode() == 10) {
            txtEmployeeSalary.grabFocus();
        }
    }//GEN-LAST:event_txtEmployeePhoneNoKeyPressed

    private void txtEmployeePhoneNoKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtEmployeePhoneNoKeyTyped
        char c = evt.getKeyChar();
        if (!(Character.isDigit(c))) {
            evt.consume();

        }
        String phoneNo = txtEmployeePhoneNo.getText();
        int length = phoneNo.length();
        if (length >= 10) {
            evt.consume();
        }
    }//GEN-LAST:event_txtEmployeePhoneNoKeyTyped

    private void txtEmployeeSalaryKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtEmployeeSalaryKeyTyped
        char c = evt.getKeyChar();
        if (!(Character.isDigit(c))) {
            evt.consume();

        }
        String phoneNo = txtEmployeeSalary.getText();
        int length = phoneNo.length();

        if (length >= 6) {
            evt.consume();
        }
    }//GEN-LAST:event_txtEmployeeSalaryKeyTyped

    private void txtEmployeeSalaryKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtEmployeeSalaryKeyPressed
        if (evt.getKeyCode() == 10) {
            addNewEmployee();
        }
    }//GEN-LAST:event_txtEmployeeSalaryKeyPressed

    private void txtSearchEmployeeKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtSearchEmployeeKeyReleased
        searchEmployee();
    }//GEN-LAST:event_txtSearchEmployeeKeyReleased

    private void cmbSortEmployeeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbSortEmployeeActionPerformed
        searchEmployee();
    }//GEN-LAST:event_cmbSortEmployeeActionPerformed

    private void jButton7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton7ActionPerformed
        refreshEmployeeTable();
        txtSearchEmployee.setText(null);
        txtSelectedEmployeeId.setText(null);
        txtSelectedEmployeeUsername.setText(null);
        txtSelectedEmployeePosition.setText(null);
        txtSelectedEmployeeNIC.setText(null);
        txtSelectedEmployeePhoneNo.setText(null);
    }//GEN-LAST:event_jButton7ActionPerformed

    private void tblEmployeeMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblEmployeeMouseClicked
        txtSelectedEmployeeId.setText((String) tblEmployee.getValueAt(tblEmployee.getSelectedRow(), 0));
        txtSelectedEmployeeUsername.setText((String) tblEmployee.getValueAt(tblEmployee.getSelectedRow(), 1));
        txtSelectedEmployeePosition.setText((String) tblEmployee.getValueAt(tblEmployee.getSelectedRow(), 2));
        txtSelectedEmployeeNIC.setText((String) tblEmployee.getValueAt(tblEmployee.getSelectedRow(), 3));
        txtSelectedEmployeePhoneNo.setText((String) tblEmployee.getValueAt(tblEmployee.getSelectedRow(), 4));

    }//GEN-LAST:event_tblEmployeeMouseClicked

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        if (tblEmployee.getSelectedRowCount() == 0) {
            JOptionPane.showMessageDialog(this, "No Employee Selected!");
        } else {
            jTabbedPaneEmployee.setSelectedIndex(2);
            txtSelectedEmployeePosition.grabFocus();
        }
    }//GEN-LAST:event_jButton4ActionPerformed

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        if (tblEmployee.getSelectedRowCount() == 0) {
            JOptionPane.showMessageDialog(this, "No Employee Selected!");

        } else {
            deleteEmployee();
        }
    }//GEN-LAST:event_jButton5ActionPerformed

    private void jButton8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton8ActionPerformed
        if (tblEmployee.getSelectedRowCount() == 0) {
            JOptionPane.showMessageDialog(this, "No Employee Selected!");
            jTabbedPaneEmployee.setSelectedIndex(0);

        } else {
            updateEmployee();

            jPanel4.setVisible(false);
            jPanel4.setVisible(true);
        }
        updateEmployee();
    }//GEN-LAST:event_jButton8ActionPerformed

    private void jButton9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton9ActionPerformed
        if (tblEmployee.getSelectedRowCount() == 0) {
            JOptionPane.showMessageDialog(this, "No Employee Selected!");
            jTabbedPaneEmployee.setSelectedIndex(0);

        } else {

            deleteEmployee();
            jPanel4.setVisible(false);
            jPanel4.setVisible(true);
        }
    }//GEN-LAST:event_jButton9ActionPerformed

    private void txtSelectedEmployeePositionKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtSelectedEmployeePositionKeyPressed
        if (evt.getKeyCode() == 10) {
            txtSelectedEmployeeNIC.grabFocus();
        }
    }//GEN-LAST:event_txtSelectedEmployeePositionKeyPressed

    private void txtSelectedEmployeeNICKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtSelectedEmployeeNICKeyPressed
        if (evt.getKeyCode() == 10) {
            txtSelectedEmployeePhoneNo.grabFocus();
        }
    }//GEN-LAST:event_txtSelectedEmployeeNICKeyPressed

    private void txtSelectedEmployeeNICKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtSelectedEmployeeNICKeyTyped
        if (!(Character.isDigit(evt.getKeyChar()))) {
            evt.consume();
        }
        String nic = txtSelectedEmployeeNIC.getText();
        int length = nic.length();
        if (length >= 12) {
            evt.consume();
        }
    }//GEN-LAST:event_txtSelectedEmployeeNICKeyTyped

    private void txtSelectedEmployeePhoneNoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtSelectedEmployeePhoneNoKeyPressed
        if (evt.getKeyCode() == 10) {
            updateEmployee();
        }
    }//GEN-LAST:event_txtSelectedEmployeePhoneNoKeyPressed

    private void txtSelectedEmployeePhoneNoKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtSelectedEmployeePhoneNoKeyTyped
        char c = evt.getKeyChar();
        if (!(Character.isDigit(c))) {
            evt.consume();

        }
        String phoneNo = txtSelectedEmployeePhoneNo.getText();
        int length = phoneNo.length();
        if (length >= 10) {
            evt.consume();
        }
    }//GEN-LAST:event_txtSelectedEmployeePhoneNoKeyTyped

    private void jLabel4MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel4MouseClicked
        int choose = JOptionPane.showConfirmDialog(this,
                "Do you want to Logout the application?",
                "Confirm Logout", JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE);
        if (choose == JOptionPane.YES_OPTION) {

            String logoutActivityLog = "INSERT INTO log(usr_id, usr_type, usr_username, log_activity, log_description, log_state) VALUES ((SELECT usr_id FROM user WHERE usr_username = '" + lblAdminUsername.getText() + "'),'Admin','" + lblAdminUsername.getText() + "','Admin Logout', '" + lblAdminUsername.getText() + " logged out' ,'SUCCESS')";
            try {
                DB.DB.putData(logoutActivityLog);
            } catch (Exception e) {
            }

            AdminEmployee.this.dispose();

            Login login = new Login();
            login.setVisible(true);
            for (float j = 0f; j < 0.985; j += 0.000001) {
                login.setOpacity(j);
            }
        } else {

        }
    }//GEN-LAST:event_jLabel4MouseClicked

    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed
        printAllEmployee();
    }//GEN-LAST:event_jButton6ActionPerformed

    private void jButton10ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton10ActionPerformed
        printSelectedEmployee();
    }//GEN-LAST:event_jButton10ActionPerformed

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
                new AdminEmployee().setVisible(true);
            }
        });
    }

    private void setIcon() {
        setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("img/exfiltrat0z-icon.png")));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox<String> cmbSortEmployee;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton10;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    private javax.swing.JButton jButton7;
    private javax.swing.JButton jButton8;
    private javax.swing.JButton jButton9;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTabbedPane jTabbedPaneEmployee;
    public static final javax.swing.JLabel lblAdminUsername = new javax.swing.JLabel();
    private javax.swing.JLabel lblDate;
    private javax.swing.JLabel lblEmployeeCount;
    private javax.swing.JLabel lblTime;
    private javax.swing.JPanel pnlHomeSubSelection;
    private javax.swing.JPanel pnlLogout;
    private javax.swing.JPasswordField pwdEmployeePassword;
    private javax.swing.JPasswordField pwdRepeatEmployeePassword;
    private javax.swing.JTable tblEmployee;
    private javax.swing.JTextField txtEmployeeId;
    private javax.swing.JTextField txtEmployeeNIC;
    private javax.swing.JTextField txtEmployeePhoneNo;
    private javax.swing.JTextField txtEmployeePosition;
    private javax.swing.JTextField txtEmployeeSalary;
    private javax.swing.JTextField txtEmployeeUsername;
    private javax.swing.JTextField txtSearchEmployee;
    private javax.swing.JTextField txtSelectedEmployeeId;
    private javax.swing.JTextField txtSelectedEmployeeNIC;
    private javax.swing.JTextField txtSelectedEmployeePhoneNo;
    private javax.swing.JTextField txtSelectedEmployeePosition;
    private javax.swing.JTextField txtSelectedEmployeeUsername;
    // End of variables declaration//GEN-END:variables
}
