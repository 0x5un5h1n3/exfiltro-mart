/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package frames;

import com.formdev.flatlaf.FlatDarkLaf;
import java.awt.Color;
import java.awt.Font;
import java.awt.HeadlessException;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.sql.ResultSet;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
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
public class AdminSettings extends javax.swing.JFrame {

    Timer timer;
    String month;
    String year;
    String date;
    String today;
    String path = null;

    public AdminSettings() {
        initComponents();
        AdminSettings.this.setExtendedState(JFrame.MAXIMIZED_BOTH);
        AdminSettings.this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setIcon();
        setTableheader();
        refreshAdministratorTable();
        refreshUserPasswordTable();
        AdminSettings.this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {

                AdminSettings.this.dispose();
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
        JTableHeader viewAdmins = tblAdmin.getTableHeader();
        viewAdmins.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        viewAdmins.setForeground(Color.white);
        viewAdmins.setBackground(new Color(51, 51, 51));
        tblAdmin.getTableHeader().setReorderingAllowed(false);

        JTableHeader viewUsers = tblUserPassword.getTableHeader();
        viewUsers.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        viewUsers.setForeground(Color.white);
        viewUsers.setBackground(new Color(51, 51, 51));
        tblUserPassword.getTableHeader().setReorderingAllowed(false);
    }

    private void addNewAdministrator() {

        int option = JOptionPane.showConfirmDialog(this, "Do you want to Add this Administrator?", "", JOptionPane.YES_NO_OPTION);
        if (option == 0) {
            if (!txtAdminUsername.getText().equals("")
                    & !txtAdminNIC.getText().equals("")
                    & (!String.valueOf(pwdAdminPassword.getPassword()).equals("")
                    & (!String.valueOf(pwdRepeatAdminPassword.getPassword()).equals("")))) {

                try {
                    ResultSet rs = DB.DB.getData("SELECT * FROM user WHERE usr_username like '" + txtAdminUsername.getText() + "%' order by usr_id ");

                    if (rs.next()) {
                        JOptionPane.showMessageDialog(this, "Username Already Exists!", "Invalid Username", JOptionPane.WARNING_MESSAGE);

                    } else if (!(String.valueOf(pwdAdminPassword.getPassword()).length() >= 4)) {
                        JOptionPane.showMessageDialog(this, "Password is Too Short!", "Invalid Password", JOptionPane.WARNING_MESSAGE); //Short password
                        pwdAdminPassword.setText(null);
                        pwdRepeatAdminPassword.setText(null);
                        pwdAdminPassword.grabFocus();

                    } else if (!String.valueOf(pwdAdminPassword.getPassword()).equals(String.valueOf(pwdRepeatAdminPassword.getPassword()))) {
                        JOptionPane.showMessageDialog(this, "Password doesn't match!", "Invalid Password Matching", JOptionPane.WARNING_MESSAGE); //invalid password matching
                        pwdAdminPassword.setText(null);
                        pwdRepeatAdminPassword.setText(null);
                        pwdAdminPassword.grabFocus();

                    } else if (!(txtAdminNIC.getText().length() >= 9)) {
                        JOptionPane.showMessageDialog(this, "Invalid NIC!", "Invalid Data", JOptionPane.WARNING_MESSAGE);
                        txtAdminNIC.setText(null);
                        txtAdminNIC.grabFocus();

                    } else if (txtAdminPhoneNo.getText().length() != 10) {
                        JOptionPane.showMessageDialog(this, "Invalid Phone Number Length!", "Invalid Data", JOptionPane.WARNING_MESSAGE);
                        txtAdminPhoneNo.setText(null);
                        txtAdminPhoneNo.grabFocus();

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

                        String addAdministrator = "INSERT INTO user(usr_username, usr_password, usr_type, usr_position, usr_nic, usr_phone, usr_stat) VALUES ('" + txtAdminUsername.getText().trim() + "','" + String.valueOf(pwdRepeatAdminPassword.getPassword()) + "','Admin','Admin','" + txtAdminNIC.getText() + "','" + txtAdminPhoneNo.getText() + "', '1')";
                        String AddAdministratorActivityLog = "INSERT INTO log(usr_id, usr_type, usr_username, log_activity, log_description, log_state) VALUES ((SELECT usr_id FROM user WHERE usr_username = '" + lblAdminUsername.getText() + "'),'Admin','" + lblAdminUsername.getText() + "','Add New Admin','Admin added new admin " + txtAdminUsername.getText() + "','SUCCESS')";

                        try {
                            DB.DB.putData(addAdministrator);
                            DB.DB.putData(AddAdministratorActivityLog);

                            refreshAdministratorTable();
                            refreshUserPasswordTable();

                            ImageIcon icon = new ImageIcon(getClass().getResource("img/done.png"));
                            JOptionPane.showMessageDialog(this, "Administrator Added!", "Done", JOptionPane.INFORMATION_MESSAGE, icon);

                            txtAdminUsername.setText(null);
                            txtAdminNIC.setText(null);
                            txtAdminPhoneNo.setText(null);
                            pwdAdminPassword.setText(null);
                            pwdRepeatAdminPassword.setText(null);

                        } catch (Exception e) {
                        }

                    }
                } catch (Exception ex) {
                }
            } else {
                JOptionPane.showMessageDialog(this, "Please Fill All the Fields!", "Invalid Data", JOptionPane.WARNING_MESSAGE);
            }
        }
    }

    private void deleteAdministrator() {
        int option = JOptionPane.showConfirmDialog(this, "Do you want to Delete this Administrator?", "", JOptionPane.YES_NO_OPTION);
        if (option == 0) {

            try {
                DB.DB.putData("UPDATE user SET usr_stat = '0' WHERE usr_id ='" + txtSelectedAdminId.getText() + "' ");
                String deleteAdminActivityLog = "INSERT INTO log(usr_id, usr_type, usr_username, log_activity, log_description, log_state) VALUES ((SELECT usr_id FROM user WHERE usr_username = '" + lblAdminUsername.getText() + "'),'Admin','" + lblAdminUsername.getText() + "','Delete Admin','Admin deleted admin " + txtSelectedAdminUsername.getText() + "','SUCCESS')";
                DB.DB.putData(deleteAdminActivityLog);

                ImageIcon icon = new ImageIcon(getClass().getResource("img/done.png"));
                JOptionPane.showMessageDialog(this, "Administrator Deleted!", "Done", JOptionPane.INFORMATION_MESSAGE, icon);

                txtSearchAdmin.setText(null);
                txtSelectedAdminId.setText(null);
                txtSelectedAdminUsername.setText(null);
                txtSelectedAdminNIC.setText(null);
                txtSelectedAdminPhoneNo.setText(null);
                refreshAdministratorTable();

            } catch (Exception e) {
            }
        } else {

        }
    }

    private void updateAdministrator() {
        int option = JOptionPane.showConfirmDialog(this, "Do you want to Update this Administrator?", "", JOptionPane.YES_NO_OPTION);
        if (option == 0) {

            if (!txtSelectedAdminUsername.getText().equals("")
                    & !txtSelectedAdminNIC.getText().equals("")
                    & !txtSelectedAdminPhoneNo.getText().equals("")) {

                if (!(txtSelectedAdminPhoneNo.getText().length() == 10)) {
                    JOptionPane.showMessageDialog(this, "Invalid Phone Number!", "Invalid Data", JOptionPane.WARNING_MESSAGE);
                    txtSelectedAdminPhoneNo.setText(null);
                    txtSelectedAdminPhoneNo.grabFocus();

                } else if (!(txtSelectedAdminNIC.getText().length() >= 9)) {
                    JOptionPane.showMessageDialog(this, "Invalid NIC!", "Invalid Data", JOptionPane.WARNING_MESSAGE);
                    txtSelectedAdminNIC.setText(null);
                    txtSelectedAdminNIC.grabFocus();

                } else {

                    try {

                        DB.DB.putData("UPDATE user SET usr_nic ='" + txtSelectedAdminNIC.getText() + "', usr_phone= '" + txtSelectedAdminPhoneNo.getText() + "' WHERE usr_id ='" + txtSelectedAdminId.getText() + "' ");

                        String updateAdministratorActivityLog = "INSERT INTO log(usr_id, usr_type, usr_username, log_activity, log_description, log_state) VALUES ((SELECT usr_id FROM user WHERE usr_username = '" + lblAdminUsername.getText() + "'),'Admin','" + lblAdminUsername.getText() + "','Update Admin','Admin updated admin " + txtSelectedAdminUsername.getText() + "','SUCCESS')";

                        DB.DB.putData(updateAdministratorActivityLog);

                        ImageIcon icon = new ImageIcon(getClass().getResource("img/done.png"));
                        JOptionPane.showMessageDialog(this, "Administrator Updated Successfully!", "Done", JOptionPane.INFORMATION_MESSAGE, icon);

                        txtSelectedAdminId.setText("");
                        txtSelectedAdminUsername.setText("");
                        txtSelectedAdminNIC.setText("");
                        txtSelectedAdminPhoneNo.setText("");
                        refreshAdministratorTable();

                    } catch (Exception e) {
                    }
                }

            } else {
                JOptionPane.showMessageDialog(this, "Please Fill All the Fields!", "Invalid Data", JOptionPane.WARNING_MESSAGE);
            }
        }
    }

    private void calculateAdminCount() {

        int count = tblAdmin.getRowCount();
        lblAdminCount.setText("Administrator Count : " + Integer.toString(count));

    }

    private void refreshAdministratorTable() {

        try {
            DefaultTableModel dtm = (DefaultTableModel) tblAdmin.getModel();
            ResultSet rs = DB.DB.getData("SELECT * FROM user WHERE usr_type = 'Admin' AND usr_stat = '1' ");
            dtm.setRowCount(0);

            while (rs.next()) {
                String a = (rs.getString(1));
                String b = (rs.getString(2));
                String c = (rs.getString(4));
                String d = (rs.getString(6));
                String e = (rs.getString(7));
                dtm.addRow(new Object[]{a, b, c, d, e});
            }
            calculateAdminCount();
        } catch (Exception e) {
        }

    }

    private void searchAdministrator() {
        switch (cmbSortAdmin.getSelectedIndex()) {
            case 0:

                try {
                DefaultTableModel dtm = (DefaultTableModel) tblAdmin.getModel();
                ResultSet rs = DB.DB.getData("SELECT * FROM user WHERE usr_type = 'Admin' AND usr_stat = '1' AND usr_id like '" + txtSearchAdmin.getText() + "%' order by usr_id ");
                dtm.setRowCount(0);

                while (rs.next()) {
                    String a = (rs.getString(1));
                    String b = (rs.getString(2));
                    String c = (rs.getString(4));
                    String d = (rs.getString(6));
                    String e = (rs.getString(7));
                    dtm.addRow(new Object[]{a, b, c, d, e});
                }
                calculateAdminCount();

            } catch (Exception e) {
            }
            break;
            case 1:

                try {
                DefaultTableModel dtm = (DefaultTableModel) tblAdmin.getModel();
                ResultSet rs = DB.DB.getData("SELECT * FROM user WHERE usr_type = 'Admin' AND usr_stat = '1' AND  usr_username like '" + txtSearchAdmin.getText() + "%' order by usr_username ");
                dtm.setRowCount(0);

                while (rs.next()) {
                    String a = (rs.getString(1));
                    String b = (rs.getString(2));
                    String c = (rs.getString(4));
                    String d = (rs.getString(6));
                    String e = (rs.getString(7));
                    dtm.addRow(new Object[]{a, b, c, d, e});
                }
                calculateAdminCount();

            } catch (Exception e) {
            }
            break;
            case 2:
                try {
                DefaultTableModel dtm = (DefaultTableModel) tblAdmin.getModel();
                ResultSet rs = DB.DB.getData("SELECT * FROM user WHERE usr_type = 'Admin' AND usr_stat = '1' AND  usr_nic like '" + txtSearchAdmin.getText() + "%' order by usr_nic ");
                dtm.setRowCount(0);

                while (rs.next()) {
                    String a = (rs.getString(1));
                    String b = (rs.getString(2));
                    String c = (rs.getString(4));
                    String d = (rs.getString(6));
                    String e = (rs.getString(7));
                    dtm.addRow(new Object[]{a, b, c, d, e});
                }
                calculateAdminCount();
            } catch (Exception e) {
            }
            break;
            case 3:
                try {

                DefaultTableModel dtm = (DefaultTableModel) tblAdmin.getModel();
                ResultSet rs = DB.DB.getData("SELECT * FROM user WHERE usr_type = 'Admin' AND usr_stat = '1' AND  usr_phone like '" + txtSearchAdmin.getText() + "%' order by usr_phone ");
                dtm.setRowCount(0);

                while (rs.next()) {
                    String a = (rs.getString(1));
                    String b = (rs.getString(2));
                    String c = (rs.getString(4));
                    String d = (rs.getString(6));
                    String e = (rs.getString(7));
                    dtm.addRow(new Object[]{a, b, c, d, e});
                }
                calculateAdminCount();
            } catch (Exception e) {
            }
            break;
            default:
                break;
        }
    }

    public void printAllAdmins() {

        new Thread() {
            @Override
            public void run() {
                try {

                    String reportSource = "C:\\Program Files\\Exfiltro Reports\\viewAdministratorReport.jrxml";

                    JasperDesign jdesign = JRXmlLoader.load(reportSource);
                    JasperReport jreport = JasperCompileManager.compileReport(jdesign);
                    JasperPrint jprint = JasperFillManager.fillReport(jreport, null, DB.DB.getConnection());

                    JasperViewer.viewReport(jprint, false);

                } catch (JRException ex) {

                }
            }
        }.start();
    }

    public void printSelectedAdministrator() {
        HashMap a = new HashMap();
        a.put("id", txtSelectedAdminId.getText());

        new Thread() {
            @Override
            public void run() {
                try {

                    String reportSource = "C:\\Program Files\\Exfiltro Reports\\viewSelectedAdministratorReport.jrxml";

                    JasperDesign jdesign = JRXmlLoader.load(reportSource);
                    JasperReport jreport = JasperCompileManager.compileReport(jdesign);
                    JasperPrint jprint = JasperFillManager.fillReport(jreport, a, DB.DB.getConnection());
                    JasperViewer.viewReport(jprint, false);

                } catch (JRException ex) {

                }
            }
        }.start();
    }

    private void changeUserPassword() {
        int option = JOptionPane.showConfirmDialog(this, "Do you want to Change Password of this User?", "", JOptionPane.YES_NO_OPTION);
        if (option == 0) {

            if (!txtSelectedUserId.getText().equals("")
                    & !txtSelectedUserUsername.getText().equals("")
                    & !pwdSelectedUserNewPassword.getPassword().equals("")
                    & !pwdSelectedUserRepeatPassword.getPassword().equals("")) {

                if (!(String.valueOf(pwdSelectedUserNewPassword.getPassword()).length() >= 4)) {
                    JOptionPane.showMessageDialog(this, "Password is Too Short!", "Invalid Password", JOptionPane.WARNING_MESSAGE); //Short password
                    pwdSelectedUserNewPassword.setText(null);
                    pwdSelectedUserRepeatPassword.setText(null);
                    pwdSelectedUserNewPassword.grabFocus();

                } else if (!String.valueOf(pwdSelectedUserNewPassword.getPassword()).equals(String.valueOf(pwdSelectedUserRepeatPassword.getPassword()))) {
                    JOptionPane.showMessageDialog(this, "Password doesn't match!", "Invalid Password Matching", JOptionPane.WARNING_MESSAGE);
                    pwdSelectedUserNewPassword.setText(null);
                    pwdSelectedUserRepeatPassword.setText(null);
                    pwdSelectedUserNewPassword.grabFocus();

                } else {

                    if ("Employee".equals(txtSelectedUserType.getText())) {
                        try {
                            DB.DB.putData("UPDATE user SET usr_password ='" + String.valueOf(pwdSelectedUserRepeatPassword.getPassword()) + "' WHERE usr_id ='" + txtSelectedUserId.getText() + "' ");
                            String changeEmployeePassowrdActivityLog = "INSERT INTO log(usr_id, usr_type, usr_username, log_activity, log_description, log_state) VALUES ((SELECT usr_id FROM user WHERE usr_username = '" + lblAdminUsername.getText() + "'),'Admin','" + lblAdminUsername.getText() + "','Change Password','Admin updated employee " + txtSelectedUserUsername.getText() + "','SUCCESS')";
                            DB.DB.putData(changeEmployeePassowrdActivityLog);

                            ImageIcon icon = new ImageIcon(getClass().getResource("img/done.png"));
                            JOptionPane.showMessageDialog(this, "Employee Password Changed Successfully!", "Done", JOptionPane.INFORMATION_MESSAGE, icon);

                            jTabbedPaneUserPasswords.setSelectedIndex(0);
                            txtSelectedUserId.setText("");
                            txtSelectedUserUsername.setText("");
                            txtSelectedUserType.setText("");
                            txtSelectedUserPassword.setText("");
                            pwdSelectedUserNewPassword.setText("");
                            pwdSelectedUserRepeatPassword.setText("");
                            refreshUserPasswordTable();

                        } catch (Exception e) {
                        }
                    } else {
                        try {
                            DB.DB.putData("UPDATE user SET usr_password ='" + String.valueOf(pwdSelectedUserRepeatPassword.getPassword()) + "' WHERE usr_id ='" + txtSelectedUserId.getText() + "' ");
                            String changeEmployeePassowrdActivityLog = "INSERT INTO log(usr_id, usr_type, usr_username, log_activity, log_description, log_state) VALUES ((SELECT usr_id FROM user WHERE usr_username = '" + lblAdminUsername.getText() + "'),'Admin','" + lblAdminUsername.getText() + "','Change Password','Admin updated admin " + txtSelectedUserUsername.getText() + "','SUCCESS')";
                            DB.DB.putData(changeEmployeePassowrdActivityLog);

                            ImageIcon icon = new ImageIcon(getClass().getResource("img/done.png"));
                            JOptionPane.showMessageDialog(this, "Admin Password Changed Successfully!", "Done", JOptionPane.INFORMATION_MESSAGE, icon);

                            jTabbedPaneUserPasswords.setSelectedIndex(0);
                            txtSelectedUserId.setText("");
                            txtSelectedUserUsername.setText("");
                            txtSelectedUserType.setText("");
                            txtSelectedUserPassword.setText("");
                            pwdSelectedUserNewPassword.setText("");
                            pwdSelectedUserRepeatPassword.setText("");
                            refreshUserPasswordTable();

                        } catch (Exception e) {
                        }
                    }

                }
            } else {
                JOptionPane.showMessageDialog(this, "Please Fill All the Fields!", "Invalid Data", JOptionPane.WARNING_MESSAGE);
            }
        }
    }

    private void calculateUserCount() {
        int count = tblUserPassword.getRowCount();
        lblUserCount.setText("User Count : " + Integer.toString(count));

    }

    private void refreshUserPasswordTable() {

        try {
            DefaultTableModel dtm = (DefaultTableModel) tblUserPassword.getModel();
            ResultSet rs = DB.DB.getData("SELECT * FROM user WHERE usr_stat = '1' ");
            dtm.setRowCount(0);

            while (rs.next()) {
                String a = (rs.getString(1));
                String b = (rs.getString(2));
                String c = (rs.getString(4));
                String d = (rs.getString(3));
                String e = (rs.getString(8));
                dtm.addRow(new Object[]{a, b, c, d, e});
            }
            calculateUserCount();
        } catch (Exception e) {
        }

    }

    private void searchUserType() {
        switch (cmbSortUserType.getSelectedIndex()) {
            case 0:

                try {
                DefaultTableModel dtm = (DefaultTableModel) tblUserPassword.getModel();
                ResultSet rs = DB.DB.getData("SELECT * FROM user WHERE usr_stat = '1' order by usr_id ");
                dtm.setRowCount(0);

                while (rs.next()) {
                    String a = (rs.getString(1));
                    String b = (rs.getString(2));
                    String c = (rs.getString(4));
                    String d = (rs.getString(3));
                    String e = (rs.getString(8));
                    dtm.addRow(new Object[]{a, b, c, d, e});
                }
                calculateUserCount();
            } catch (Exception e) {
            }
            break;
            case 1:

                try {
                DefaultTableModel dtm = (DefaultTableModel) tblUserPassword.getModel();
                ResultSet rs = DB.DB.getData("SELECT * FROM user WHERE usr_type = 'Employee' AND usr_stat = '1' order by usr_id ");
                dtm.setRowCount(0);

                while (rs.next()) {
                    String a = (rs.getString(1));
                    String b = (rs.getString(2));
                    String c = (rs.getString(4));
                    String d = (rs.getString(3));
                    String e = (rs.getString(8));
                    dtm.addRow(new Object[]{a, b, c, d, e});
                }
                calculateUserCount();
            } catch (Exception e) {
            }
            break;
            case 2:

                try {
                DefaultTableModel dtm = (DefaultTableModel) tblUserPassword.getModel();
                ResultSet rs = DB.DB.getData("SELECT * FROM user WHERE usr_type = 'Admin' AND usr_stat = '1' order by usr_id");
                dtm.setRowCount(0);

                while (rs.next()) {
                    String a = (rs.getString(1));
                    String b = (rs.getString(2));
                    String c = (rs.getString(4));
                    String d = (rs.getString(3));
                    String e = (rs.getString(8));
                    dtm.addRow(new Object[]{a, b, c, d, e});
                }
                calculateUserCount();
            } catch (Exception e) {
            }
            break;
            default:
                break;
        }
    }

    private void searchUserPassowrd() {
        switch (cmbSortUser.getSelectedIndex()) {
            case 0:

                try {
                DefaultTableModel dtm = (DefaultTableModel) tblUserPassword.getModel();
                ResultSet rs = DB.DB.getData("SELECT * FROM user WHERE usr_stat = '1' AND usr_id like '" + txtSearchUser.getText() + "%' order by usr_id ");
                dtm.setRowCount(0);

                while (rs.next()) {
                    String a = (rs.getString(1));
                    String b = (rs.getString(2));
                    String c = (rs.getString(4));
                    String d = (rs.getString(3));
                    String e = (rs.getString(8));
                    dtm.addRow(new Object[]{a, b, c, d, e});
                }
                calculateUserCount();
            } catch (Exception e) {
            }
            break;
            case 1:

                try {
                DefaultTableModel dtm = (DefaultTableModel) tblUserPassword.getModel();
                ResultSet rs = DB.DB.getData("SELECT * FROM user WHERE usr_stat = '1' AND usr_username like '" + txtSearchUser.getText() + "%' order by usr_username ");
                dtm.setRowCount(0);

                while (rs.next()) {
                    String a = (rs.getString(1));
                    String b = (rs.getString(2));
                    String c = (rs.getString(4));
                    String d = (rs.getString(3));
                    String e = (rs.getString(8));
                    dtm.addRow(new Object[]{a, b, c, d, e});
                }
                calculateUserCount();
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
        jPanel14 = new javax.swing.JPanel();
        jTabbedPaneEmployee = new javax.swing.JTabbedPane();
        jPanel3 = new javax.swing.JPanel();
        jTabbedPaneAdministrators = new javax.swing.JTabbedPane();
        jPanel15 = new javax.swing.JPanel();
        jPanel22 = new javax.swing.JPanel();
        jLabel25 = new javax.swing.JLabel();
        jLabel26 = new javax.swing.JLabel();
        txtAdminNIC = new javax.swing.JTextField();
        txtAdminUsername = new javax.swing.JTextField();
        jButton12 = new javax.swing.JButton();
        jLabel9 = new javax.swing.JLabel();
        txtAdminPhoneNo = new javax.swing.JTextField();
        jLabel10 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        pwdAdminPassword = new javax.swing.JPasswordField();
        pwdRepeatAdminPassword = new javax.swing.JPasswordField();
        jPanel9 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblAdmin = new javax.swing.JTable();
        txtSearchAdmin = new javax.swing.JTextField();
        cmbSortAdmin = new javax.swing.JComboBox<>();
        jLabel11 = new javax.swing.JLabel();
        jButton5 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jButton7 = new javax.swing.JButton();
        lblAdminCount = new javax.swing.JLabel();
        jButton13 = new javax.swing.JButton();
        jPanel16 = new javax.swing.JPanel();
        jPanel23 = new javax.swing.JPanel();
        jLabel27 = new javax.swing.JLabel();
        jLabel28 = new javax.swing.JLabel();
        txtSelectedAdminUsername = new javax.swing.JTextField();
        txtSelectedAdminId = new javax.swing.JTextField();
        jLabel29 = new javax.swing.JLabel();
        txtSelectedAdminNIC = new javax.swing.JTextField();
        jLabel30 = new javax.swing.JLabel();
        txtSelectedAdminPhoneNo = new javax.swing.JTextField();
        jButton9 = new javax.swing.JButton();
        jButton15 = new javax.swing.JButton();
        jButton8 = new javax.swing.JButton();
        jPanel4 = new javax.swing.JPanel();
        jTabbedPaneUserPasswords = new javax.swing.JTabbedPane();
        jPanel18 = new javax.swing.JPanel();
        jScrollPane4 = new javax.swing.JScrollPane();
        tblUserPassword = new javax.swing.JTable();
        txtSearchUser = new javax.swing.JTextField();
        cmbSortUser = new javax.swing.JComboBox<>();
        jLabel15 = new javax.swing.JLabel();
        jButton19 = new javax.swing.JButton();
        jButton25 = new javax.swing.JButton();
        lblUserCount = new javax.swing.JLabel();
        cmbSortUserType = new javax.swing.JComboBox<>();
        jPanel17 = new javax.swing.JPanel();
        jPanel24 = new javax.swing.JPanel();
        jLabel13 = new javax.swing.JLabel();
        txtSelectedUserId = new javax.swing.JTextField();
        txtSelectedUserUsername = new javax.swing.JTextField();
        jLabel14 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        txtSelectedUserType = new javax.swing.JTextField();
        jButton22 = new javax.swing.JButton();
        jButton23 = new javax.swing.JButton();
        jLabel17 = new javax.swing.JLabel();
        pwdSelectedUserNewPassword = new javax.swing.JPasswordField();
        jLabel31 = new javax.swing.JLabel();
        pwdSelectedUserRepeatPassword = new javax.swing.JPasswordField();
        txtSelectedUserPassword = new javax.swing.JTextField();
        jLabel18 = new javax.swing.JLabel();
        jPanel7 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        jLabel20 = new javax.swing.JLabel();
        txtBackupLocation = new javax.swing.JTextField();
        btnBackup = new javax.swing.JButton();
        jButton17 = new javax.swing.JButton();
        jLabel32 = new javax.swing.JLabel();
        jLabel33 = new javax.swing.JLabel();
        txtRestoreLocation = new javax.swing.JTextField();
        btnBrowseRestorePath = new javax.swing.JButton();
        btnRestore = new javax.swing.JButton();
        jPanel5 = new javax.swing.JPanel();
        jLabel34 = new javax.swing.JLabel();
        jLabel35 = new javax.swing.JLabel();
        jLabel36 = new javax.swing.JLabel();
        jLabel37 = new javax.swing.JLabel();
        jLabel38 = new javax.swing.JLabel();
        jLabel47 = new javax.swing.JLabel();
        jLabel48 = new javax.swing.JLabel();
        version = new javax.swing.JLabel();

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
        jLabel2.setText("Settings");
        jLabel2.setToolTipText("");

        jLabel3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/frames/img/icons8_settings_50px.png"))); // NOI18N

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

        jPanel14.setBackground(new java.awt.Color(204, 204, 204));

        jTabbedPaneEmployee.setBackground(new java.awt.Color(34, 34, 34));
        jTabbedPaneEmployee.setForeground(new java.awt.Color(255, 255, 255));
        jTabbedPaneEmployee.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jTabbedPaneEmployee.setOpaque(true);

        jTabbedPaneAdministrators.setBackground(new java.awt.Color(34, 34, 34));
        jTabbedPaneAdministrators.setForeground(new java.awt.Color(255, 255, 255));
        jTabbedPaneAdministrators.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jTabbedPaneAdministrators.setOpaque(true);

        jPanel22.setBackground(new java.awt.Color(34, 34, 34));

        jLabel25.setBackground(new java.awt.Color(0, 0, 0));
        jLabel25.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel25.setForeground(new java.awt.Color(255, 255, 255));
        jLabel25.setText("Username");

        jLabel26.setBackground(new java.awt.Color(0, 0, 0));
        jLabel26.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel26.setForeground(new java.awt.Color(255, 255, 255));
        jLabel26.setText("NIC");

        txtAdminNIC.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        txtAdminNIC.setForeground(new java.awt.Color(255, 255, 255));
        txtAdminNIC.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtAdminNICKeyPressed(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtAdminNICKeyTyped(evt);
            }
        });

        txtAdminUsername.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        txtAdminUsername.setForeground(new java.awt.Color(255, 255, 255));
        txtAdminUsername.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtAdminUsernameKeyPressed(evt);
            }
        });

        jButton12.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jButton12.setForeground(new java.awt.Color(255, 255, 255));
        jButton12.setText(" Add New Admin");
        jButton12.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jLabel9.setBackground(new java.awt.Color(0, 0, 0));
        jLabel9.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(255, 255, 255));
        jLabel9.setText("Phone No.");

        txtAdminPhoneNo.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        txtAdminPhoneNo.setForeground(new java.awt.Color(255, 255, 255));
        txtAdminPhoneNo.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtAdminPhoneNoKeyPressed(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtAdminPhoneNoKeyTyped(evt);
            }
        });

        jLabel10.setBackground(new java.awt.Color(0, 0, 0));
        jLabel10.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel10.setForeground(new java.awt.Color(255, 255, 255));
        jLabel10.setText("Password");

        jLabel12.setBackground(new java.awt.Color(0, 0, 0));
        jLabel12.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel12.setForeground(new java.awt.Color(255, 255, 255));
        jLabel12.setText("Repeat Password");

        pwdAdminPassword.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        pwdAdminPassword.setForeground(new java.awt.Color(255, 255, 255));
        pwdAdminPassword.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                pwdAdminPasswordKeyPressed(evt);
            }
        });

        pwdRepeatAdminPassword.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        pwdRepeatAdminPassword.setForeground(new java.awt.Color(255, 255, 255));
        pwdRepeatAdminPassword.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                pwdRepeatAdminPasswordKeyPressed(evt);
            }
        });

        javax.swing.GroupLayout jPanel22Layout = new javax.swing.GroupLayout(jPanel22);
        jPanel22.setLayout(jPanel22Layout);
        jPanel22Layout.setHorizontalGroup(
            jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel22Layout.createSequentialGroup()
                .addGap(52, 52, 52)
                .addGroup(jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel25)
                    .addComponent(jLabel26)
                    .addComponent(jLabel9)
                    .addComponent(jLabel10)
                    .addComponent(jLabel12))
                .addGap(72, 72, 72)
                .addGroup(jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(txtAdminUsername, javax.swing.GroupLayout.DEFAULT_SIZE, 278, Short.MAX_VALUE)
                    .addComponent(txtAdminNIC)
                    .addComponent(txtAdminPhoneNo)
                    .addComponent(pwdAdminPassword)
                    .addComponent(pwdRepeatAdminPassword, javax.swing.GroupLayout.Alignment.LEADING))
                .addGap(78, 78, 78)
                .addComponent(jButton12)
                .addContainerGap(453, Short.MAX_VALUE))
        );
        jPanel22Layout.setVerticalGroup(
            jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel22Layout.createSequentialGroup()
                .addGap(45, 45, 45)
                .addGroup(jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButton12)
                    .addGroup(jPanel22Layout.createSequentialGroup()
                        .addGroup(jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel25)
                            .addComponent(txtAdminUsername, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel26)
                            .addComponent(txtAdminNIC, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel9)
                            .addComponent(txtAdminPhoneNo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel10)
                            .addComponent(pwdAdminPassword, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel12)
                            .addComponent(pwdRepeatAdminPassword, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(265, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel15Layout = new javax.swing.GroupLayout(jPanel15);
        jPanel15.setLayout(jPanel15Layout);
        jPanel15Layout.setHorizontalGroup(
            jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel22, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel15Layout.setVerticalGroup(
            jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel15Layout.createSequentialGroup()
                .addComponent(jPanel22, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(0, 0, 0))
        );

        jTabbedPaneAdministrators.addTab("New Admin", jPanel15);

        jPanel9.setBackground(new java.awt.Color(34, 34, 34));

        tblAdmin.setBackground(new java.awt.Color(34, 34, 34));
        tblAdmin.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        tblAdmin.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Id", "Username", "Account Type", "NIC", "Phone No."
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblAdmin.setGridColor(new java.awt.Color(61, 61, 61));
        tblAdmin.setShowGrid(true);
        tblAdmin.getTableHeader().setReorderingAllowed(false);
        tblAdmin.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblAdminMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tblAdmin);

        txtSearchAdmin.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        txtSearchAdmin.setForeground(new java.awt.Color(255, 255, 255));
        txtSearchAdmin.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtSearchAdminKeyReleased(evt);
            }
        });

        cmbSortAdmin.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        cmbSortAdmin.setForeground(new java.awt.Color(255, 255, 255));
        cmbSortAdmin.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Id", "Name", "NIC", "Phone No." }));
        cmbSortAdmin.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbSortAdminActionPerformed(evt);
            }
        });

        jLabel11.setIcon(new javax.swing.ImageIcon(getClass().getResource("/frames/img/search.icon.png"))); // NOI18N

        jButton5.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jButton5.setForeground(new java.awt.Color(255, 255, 255));
        jButton5.setText(" Delete");
        jButton5.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });

        jButton4.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jButton4.setForeground(new java.awt.Color(255, 255, 255));
        jButton4.setText(" Edit");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        jButton7.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jButton7.setForeground(new java.awt.Color(255, 255, 255));
        jButton7.setText(" Refresh");
        jButton7.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jButton7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton7ActionPerformed(evt);
            }
        });

        lblAdminCount.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        lblAdminCount.setText("Administrator Count : 0");

        jButton13.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jButton13.setForeground(new java.awt.Color(255, 255, 255));
        jButton13.setText(" Print All");
        jButton13.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jButton13.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton13ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addGap(52, 52, 52)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel9Layout.createSequentialGroup()
                        .addComponent(lblAdminCount, javax.swing.GroupLayout.PREFERRED_SIZE, 242, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButton7)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jButton4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jButton5)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jButton13))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 1096, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel9Layout.createSequentialGroup()
                        .addComponent(txtSearchAdmin, javax.swing.GroupLayout.DEFAULT_SIZE, 873, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(cmbSortAdmin, 0, 173, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel11)))
                .addGap(52, 52, 52))
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addGap(23, 23, 23)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtSearchAdmin, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel11)
                    .addComponent(cmbSortAdmin, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 346, Short.MAX_VALUE)
                .addGap(30, 30, 30)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton4)
                    .addComponent(jButton5)
                    .addComponent(jButton7)
                    .addComponent(lblAdminCount)
                    .addComponent(jButton13))
                .addGap(45, 45, 45))
        );

        jTabbedPaneAdministrators.addTab("View Admin", jPanel9);

        jPanel23.setBackground(new java.awt.Color(34, 34, 34));

        jLabel27.setBackground(new java.awt.Color(0, 0, 0));
        jLabel27.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel27.setForeground(new java.awt.Color(255, 255, 255));
        jLabel27.setText("Id");

        jLabel28.setBackground(new java.awt.Color(0, 0, 0));
        jLabel28.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel28.setForeground(new java.awt.Color(255, 255, 255));
        jLabel28.setText("Username");

        txtSelectedAdminUsername.setBackground(new java.awt.Color(51, 51, 51));
        txtSelectedAdminUsername.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        txtSelectedAdminUsername.setEnabled(false);

        txtSelectedAdminId.setBackground(new java.awt.Color(51, 51, 51));
        txtSelectedAdminId.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        txtSelectedAdminId.setEnabled(false);

        jLabel29.setBackground(new java.awt.Color(0, 0, 0));
        jLabel29.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel29.setForeground(new java.awt.Color(255, 255, 255));
        jLabel29.setText("NIC");

        txtSelectedAdminNIC.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        txtSelectedAdminNIC.setForeground(new java.awt.Color(255, 255, 255));
        txtSelectedAdminNIC.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtSelectedAdminNICKeyPressed(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtSelectedAdminNICKeyTyped(evt);
            }
        });

        jLabel30.setBackground(new java.awt.Color(0, 0, 0));
        jLabel30.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel30.setForeground(new java.awt.Color(255, 255, 255));
        jLabel30.setText("Phone No.");

        txtSelectedAdminPhoneNo.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        txtSelectedAdminPhoneNo.setForeground(new java.awt.Color(255, 255, 255));
        txtSelectedAdminPhoneNo.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtSelectedAdminPhoneNoKeyPressed(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtSelectedAdminPhoneNoKeyTyped(evt);
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

        jButton15.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jButton15.setForeground(new java.awt.Color(255, 255, 255));
        jButton15.setText(" Print");
        jButton15.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jButton15.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton15ActionPerformed(evt);
            }
        });

        jButton8.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jButton8.setForeground(new java.awt.Color(255, 255, 255));
        jButton8.setText(" Update");
        jButton8.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jButton8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton8ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel23Layout = new javax.swing.GroupLayout(jPanel23);
        jPanel23.setLayout(jPanel23Layout);
        jPanel23Layout.setHorizontalGroup(
            jPanel23Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel23Layout.createSequentialGroup()
                .addGap(52, 52, 52)
                .addGroup(jPanel23Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel27)
                    .addComponent(jLabel28)
                    .addComponent(jLabel29)
                    .addComponent(jLabel30))
                .addGap(120, 120, 120)
                .addGroup(jPanel23Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(txtSelectedAdminId, javax.swing.GroupLayout.DEFAULT_SIZE, 278, Short.MAX_VALUE)
                    .addComponent(txtSelectedAdminUsername)
                    .addComponent(txtSelectedAdminNIC)
                    .addComponent(txtSelectedAdminPhoneNo))
                .addGap(78, 78, 78)
                .addGroup(jPanel23Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButton9)
                    .addComponent(jButton8)
                    .addComponent(jButton15))
                .addContainerGap(516, Short.MAX_VALUE))
        );
        jPanel23Layout.setVerticalGroup(
            jPanel23Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel23Layout.createSequentialGroup()
                .addGap(45, 45, 45)
                .addGroup(jPanel23Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel23Layout.createSequentialGroup()
                        .addGroup(jPanel23Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel27)
                            .addComponent(txtSelectedAdminId, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel23Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel28)
                            .addComponent(txtSelectedAdminUsername, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel23Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel29)
                            .addComponent(txtSelectedAdminNIC, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel23Layout.createSequentialGroup()
                        .addComponent(jButton8)
                        .addGap(18, 18, 18)
                        .addComponent(jButton9)
                        .addGap(18, 18, 18)
                        .addComponent(jButton15)))
                .addGap(18, 18, 18)
                .addGroup(jPanel23Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel30)
                    .addComponent(txtSelectedAdminPhoneNo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(311, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel16Layout = new javax.swing.GroupLayout(jPanel16);
        jPanel16.setLayout(jPanel16Layout);
        jPanel16Layout.setHorizontalGroup(
            jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel23, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel16Layout.setVerticalGroup(
            jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel16Layout.createSequentialGroup()
                .addComponent(jPanel23, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(0, 0, 0))
        );

        jTabbedPaneAdministrators.addTab("Edit Admin", jPanel16);

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPaneAdministrators)
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPaneAdministrators)
        );

        jTabbedPaneEmployee.addTab("Manage Admins", jPanel3);

        jTabbedPaneUserPasswords.setBackground(new java.awt.Color(34, 34, 34));
        jTabbedPaneUserPasswords.setForeground(new java.awt.Color(255, 255, 255));
        jTabbedPaneUserPasswords.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jTabbedPaneUserPasswords.setOpaque(true);

        jPanel18.setBackground(new java.awt.Color(34, 34, 34));

        tblUserPassword.setBackground(new java.awt.Color(34, 34, 34));
        tblUserPassword.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        tblUserPassword.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Id", "Username", "Account Type", "Password", "Status"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblUserPassword.setGridColor(new java.awt.Color(61, 61, 61));
        tblUserPassword.setShowGrid(true);
        tblUserPassword.getTableHeader().setReorderingAllowed(false);
        tblUserPassword.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblUserPasswordMouseClicked(evt);
            }
        });
        jScrollPane4.setViewportView(tblUserPassword);

        txtSearchUser.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        txtSearchUser.setForeground(new java.awt.Color(255, 255, 255));
        txtSearchUser.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtSearchUserKeyReleased(evt);
            }
        });

        cmbSortUser.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        cmbSortUser.setForeground(new java.awt.Color(255, 255, 255));
        cmbSortUser.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Id", "Name" }));
        cmbSortUser.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbSortUserActionPerformed(evt);
            }
        });

        jLabel15.setIcon(new javax.swing.ImageIcon(getClass().getResource("/frames/img/search.icon.png"))); // NOI18N

        jButton19.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jButton19.setForeground(new java.awt.Color(255, 255, 255));
        jButton19.setText("Change Password");
        jButton19.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jButton19.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton19ActionPerformed(evt);
            }
        });

        jButton25.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jButton25.setForeground(new java.awt.Color(255, 255, 255));
        jButton25.setText(" Refresh");
        jButton25.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jButton25.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton25ActionPerformed(evt);
            }
        });

        lblUserCount.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        lblUserCount.setText("User Count : 0");

        cmbSortUserType.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        cmbSortUserType.setForeground(new java.awt.Color(255, 255, 255));
        cmbSortUserType.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "All Users", "Employee", "Admin" }));
        cmbSortUserType.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbSortUserTypeActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel18Layout = new javax.swing.GroupLayout(jPanel18);
        jPanel18.setLayout(jPanel18Layout);
        jPanel18Layout.setHorizontalGroup(
            jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel18Layout.createSequentialGroup()
                .addGap(52, 52, 52)
                .addGroup(jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel18Layout.createSequentialGroup()
                        .addComponent(lblUserCount, javax.swing.GroupLayout.PREFERRED_SIZE, 242, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 601, Short.MAX_VALUE)
                        .addComponent(jButton25)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jButton19))
                    .addComponent(jScrollPane4, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel18Layout.createSequentialGroup()
                        .addComponent(txtSearchUser)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(cmbSortUserType, javax.swing.GroupLayout.PREFERRED_SIZE, 173, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(cmbSortUser, javax.swing.GroupLayout.PREFERRED_SIZE, 173, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel15)))
                .addGap(52, 52, 52))
        );
        jPanel18Layout.setVerticalGroup(
            jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel18Layout.createSequentialGroup()
                .addGap(23, 23, 23)
                .addGroup(jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtSearchUser, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel15)
                    .addComponent(cmbSortUser, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cmbSortUserType, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 346, Short.MAX_VALUE)
                .addGap(30, 30, 30)
                .addGroup(jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton19)
                    .addComponent(jButton25)
                    .addComponent(lblUserCount))
                .addGap(45, 45, 45))
        );

        jTabbedPaneUserPasswords.addTab("View User", jPanel18);

        jPanel24.setBackground(new java.awt.Color(34, 34, 34));

        jLabel13.setBackground(new java.awt.Color(0, 0, 0));
        jLabel13.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel13.setForeground(new java.awt.Color(255, 255, 255));
        jLabel13.setText("Id");

        txtSelectedUserId.setBackground(new java.awt.Color(51, 51, 51));
        txtSelectedUserId.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        txtSelectedUserId.setForeground(new java.awt.Color(255, 255, 255));
        txtSelectedUserId.setEnabled(false);

        txtSelectedUserUsername.setBackground(new java.awt.Color(51, 51, 51));
        txtSelectedUserUsername.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        txtSelectedUserUsername.setForeground(new java.awt.Color(255, 255, 255));
        txtSelectedUserUsername.setEnabled(false);

        jLabel14.setBackground(new java.awt.Color(0, 0, 0));
        jLabel14.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel14.setForeground(new java.awt.Color(255, 255, 255));
        jLabel14.setText("Username");

        jLabel16.setBackground(new java.awt.Color(0, 0, 0));
        jLabel16.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel16.setForeground(new java.awt.Color(255, 255, 255));
        jLabel16.setText("User Type");

        txtSelectedUserType.setBackground(new java.awt.Color(51, 51, 51));
        txtSelectedUserType.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        txtSelectedUserType.setForeground(new java.awt.Color(255, 255, 255));
        txtSelectedUserType.setEnabled(false);

        jButton22.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jButton22.setForeground(new java.awt.Color(255, 255, 255));
        jButton22.setText("Change Password");
        jButton22.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jButton22.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton22ActionPerformed(evt);
            }
        });

        jButton23.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jButton23.setForeground(new java.awt.Color(255, 255, 255));
        jButton23.setText("Go Back");
        jButton23.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jButton23.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton23ActionPerformed(evt);
            }
        });

        jLabel17.setBackground(new java.awt.Color(0, 0, 0));
        jLabel17.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel17.setForeground(new java.awt.Color(255, 255, 255));
        jLabel17.setText("New Password");

        pwdSelectedUserNewPassword.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        pwdSelectedUserNewPassword.setForeground(new java.awt.Color(255, 255, 255));
        pwdSelectedUserNewPassword.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                pwdSelectedUserNewPasswordKeyPressed(evt);
            }
        });

        jLabel31.setBackground(new java.awt.Color(0, 0, 0));
        jLabel31.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel31.setForeground(new java.awt.Color(255, 255, 255));
        jLabel31.setText("Repeat Password");

        pwdSelectedUserRepeatPassword.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        pwdSelectedUserRepeatPassword.setForeground(new java.awt.Color(255, 255, 255));
        pwdSelectedUserRepeatPassword.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                pwdSelectedUserRepeatPasswordKeyPressed(evt);
            }
        });

        txtSelectedUserPassword.setBackground(new java.awt.Color(51, 51, 51));
        txtSelectedUserPassword.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        txtSelectedUserPassword.setForeground(new java.awt.Color(255, 255, 255));
        txtSelectedUserPassword.setEnabled(false);

        jLabel18.setBackground(new java.awt.Color(0, 0, 0));
        jLabel18.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel18.setForeground(new java.awt.Color(255, 255, 255));
        jLabel18.setText("Current Password");

        javax.swing.GroupLayout jPanel24Layout = new javax.swing.GroupLayout(jPanel24);
        jPanel24.setLayout(jPanel24Layout);
        jPanel24Layout.setHorizontalGroup(
            jPanel24Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel24Layout.createSequentialGroup()
                .addGap(52, 52, 52)
                .addGroup(jPanel24Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel13)
                    .addComponent(jLabel14)
                    .addComponent(jLabel16)
                    .addComponent(jLabel17)
                    .addComponent(jLabel31)
                    .addComponent(jLabel18))
                .addGap(51, 51, 51)
                .addGroup(jPanel24Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(txtSelectedUserType, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 278, Short.MAX_VALUE)
                    .addComponent(txtSelectedUserId, javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtSelectedUserUsername, javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(pwdSelectedUserNewPassword)
                    .addComponent(pwdSelectedUserRepeatPassword)
                    .addComponent(txtSelectedUserPassword, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 278, Short.MAX_VALUE))
                .addGap(78, 78, 78)
                .addGroup(jPanel24Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButton23)
                    .addComponent(jButton22))
                .addContainerGap(463, Short.MAX_VALUE))
        );
        jPanel24Layout.setVerticalGroup(
            jPanel24Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel24Layout.createSequentialGroup()
                .addGap(45, 45, 45)
                .addGroup(jPanel24Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel13)
                    .addComponent(txtSelectedUserId, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton22))
                .addGap(18, 18, 18)
                .addGroup(jPanel24Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel14)
                    .addComponent(txtSelectedUserUsername, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton23))
                .addGap(18, 18, 18)
                .addGroup(jPanel24Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtSelectedUserType, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel16))
                .addGap(18, 18, 18)
                .addGroup(jPanel24Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtSelectedUserPassword, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel18))
                .addGap(18, 18, 18)
                .addGroup(jPanel24Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel17)
                    .addComponent(pwdSelectedUserNewPassword, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel24Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel31)
                    .addComponent(pwdSelectedUserRepeatPassword, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(219, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel17Layout = new javax.swing.GroupLayout(jPanel17);
        jPanel17.setLayout(jPanel17Layout);
        jPanel17Layout.setHorizontalGroup(
            jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel24, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel17Layout.setVerticalGroup(
            jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel17Layout.createSequentialGroup()
                .addComponent(jPanel24, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(0, 0, 0))
        );

        jTabbedPaneUserPasswords.addTab("Change Password", jPanel17);

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPaneUserPasswords)
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPaneUserPasswords)
        );

        jTabbedPaneEmployee.addTab("User Passwords", jPanel4);

        jPanel7.setBackground(new java.awt.Color(34, 34, 34));

        jLabel5.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(255, 255, 255));
        jLabel5.setText("Backup Database");

        jLabel20.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel20.setText("Select Location");

        txtBackupLocation.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        txtBackupLocation.setForeground(new java.awt.Color(255, 255, 255));

        btnBackup.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        btnBackup.setForeground(new java.awt.Color(255, 255, 255));
        btnBackup.setText("Backup");
        btnBackup.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btnBackup.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBackupActionPerformed(evt);
            }
        });

        jButton17.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jButton17.setForeground(new java.awt.Color(255, 255, 255));
        jButton17.setText("Browse Path");
        jButton17.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jButton17.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton17ActionPerformed(evt);
            }
        });

        jLabel32.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel32.setForeground(new java.awt.Color(255, 255, 255));
        jLabel32.setText("Restore Database");

        jLabel33.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel33.setText("Open Location");

        txtRestoreLocation.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        txtRestoreLocation.setForeground(new java.awt.Color(255, 255, 255));

        btnBrowseRestorePath.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        btnBrowseRestorePath.setForeground(new java.awt.Color(255, 255, 255));
        btnBrowseRestorePath.setText("Browse Path");
        btnBrowseRestorePath.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btnBrowseRestorePath.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBrowseRestorePathActionPerformed(evt);
            }
        });

        btnRestore.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        btnRestore.setForeground(new java.awt.Color(255, 255, 255));
        btnRestore.setText("Restore");
        btnRestore.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btnRestore.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRestoreActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addGap(43, 43, 43)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel32, javax.swing.GroupLayout.DEFAULT_SIZE, 430, Short.MAX_VALUE)
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addComponent(btnBrowseRestorePath)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnRestore))
                    .addComponent(jLabel33, javax.swing.GroupLayout.PREFERRED_SIZE, 136, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, 430, Short.MAX_VALUE)
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addComponent(jButton17)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnBackup))
                    .addComponent(jLabel20, javax.swing.GroupLayout.PREFERRED_SIZE, 136, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtBackupLocation)
                    .addComponent(txtRestoreLocation))
                .addContainerGap(727, Short.MAX_VALUE))
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addGap(43, 43, 43)
                .addComponent(jLabel5)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel20)
                .addGap(18, 18, 18)
                .addComponent(txtBackupLocation, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(29, 29, 29)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnBackup)
                    .addComponent(jButton17))
                .addGap(66, 66, 66)
                .addComponent(jLabel32)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel33)
                .addGap(18, 18, 18)
                .addComponent(txtRestoreLocation, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(29, 29, 29)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnRestore)
                    .addComponent(btnBrowseRestorePath))
                .addContainerGap(140, Short.MAX_VALUE))
        );

        jTabbedPaneEmployee.addTab("Backup / Restore", jPanel7);

        jPanel5.setBackground(new java.awt.Color(34, 34, 34));

        jLabel34.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel34.setForeground(new java.awt.Color(255, 255, 255));
        jLabel34.setText("Developed by");

        jLabel35.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        jLabel35.setForeground(new java.awt.Color(255, 255, 255));
        jLabel35.setText("Exfiltrat0z Group");

        jLabel36.setIcon(new javax.swing.ImageIcon(getClass().getResource("/frames/img/logo.png"))); // NOI18N

        jLabel37.setBackground(new java.awt.Color(0, 0, 0));
        jLabel37.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel37.setForeground(new java.awt.Color(180, 180, 180));
        jLabel37.setText("Exfiltro is an Inventory Management software for growing business");

        jLabel38.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel38.setForeground(new java.awt.Color(120, 120, 120));
        jLabel38.setText("Increase your sales and keep track of every unit with our powerful stock management, order fulfillment, ");

        jLabel47.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel47.setForeground(new java.awt.Color(120, 120, 120));
        jLabel47.setText("user management and inventory control software ");

        jLabel48.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel48.setForeground(new java.awt.Color(120, 120, 120));
        jLabel48.setText("Run a more efficient business :)");

        version.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        version.setForeground(new java.awt.Color(180, 180, 180));
        version.setText("© Exfiltrat0z Group | Exfiltro Mart | Version 20.12");
        version.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(58, 58, 58)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel47)
                    .addComponent(jLabel38)
                    .addComponent(version)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(jLabel36)
                        .addGap(18, 18, 18)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel34, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel35, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(jLabel37)
                    .addComponent(jLabel48))
                .addContainerGap(424, Short.MAX_VALUE))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(31, 31, 31)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGap(22, 22, 22)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel34)
                            .addGroup(jPanel5Layout.createSequentialGroup()
                                .addGap(10, 10, 10)
                                .addComponent(jLabel35, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addComponent(jLabel36, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(49, 49, 49)
                .addComponent(jLabel37)
                .addGap(18, 18, 18)
                .addComponent(jLabel38)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel47)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel48)
                .addGap(38, 38, 38)
                .addComponent(version)
                .addContainerGap(207, Short.MAX_VALUE))
        );

        jTabbedPaneEmployee.addTab("About", jPanel5);

        javax.swing.GroupLayout jPanel14Layout = new javax.swing.GroupLayout(jPanel14);
        jPanel14.setLayout(jPanel14Layout);
        jPanel14Layout.setHorizontalGroup(
            jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPaneEmployee)
        );
        jPanel14Layout.setVerticalGroup(
            jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
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
                .addComponent(lblAdminUsername, javax.swing.GroupLayout.PREFERRED_SIZE, 134, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(29, 29, 29)
                .addComponent(pnlLogout, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
            .addComponent(jPanel14, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
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
                .addComponent(jPanel14, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
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

            String logoutActivityLog = "INSERT INTO log(usr_id, usr_type, usr_username, log_activity, log_description, log_state) VALUES ((SELECT usr_id FROM user WHERE usr_username = '" + lblAdminUsername.getText() + "'),'Admin','" + lblAdminUsername.getText() + "','Admin Logout', '" + lblAdminUsername.getText() + " logged out' ,'SUCCESS')";
            try {
                DB.DB.putData(logoutActivityLog);
            } catch (Exception e) {
            }

            AdminSettings.this.dispose();

            Login login = new Login();
            login.setVisible(true);
            for (float j = 0f; j < 0.985; j += 0.000001) {
                login.setOpacity(j);
            }
        } else {

        }
    }//GEN-LAST:event_jLabel4MouseClicked

    private void jButton7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton7ActionPerformed
        txtSearchAdmin.setText(null);
        txtSelectedAdminId.setText(null);
        txtSelectedAdminUsername.setText(null);
        txtSelectedAdminNIC.setText(null);
        txtSelectedAdminPhoneNo.setText(null);
        refreshAdministratorTable();
        cmbSortAdmin.setSelectedIndex(0);

    }//GEN-LAST:event_jButton7ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        if (tblAdmin.getSelectedRowCount() == 0) {
            JOptionPane.showMessageDialog(this, "No Admin Selected!");

        } else {
            jTabbedPaneAdministrators.setSelectedIndex(2);
        }
    }//GEN-LAST:event_jButton4ActionPerformed

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        if (tblAdmin.getSelectedRowCount() == 0) {
            JOptionPane.showMessageDialog(this, "No Admin Selected!");

        } else {
            deleteAdministrator();
        }
    }//GEN-LAST:event_jButton5ActionPerformed

    private void cmbSortAdminActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbSortAdminActionPerformed
        searchAdministrator();
    }//GEN-LAST:event_cmbSortAdminActionPerformed

    private void txtSearchAdminKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtSearchAdminKeyReleased
        searchAdministrator();
    }//GEN-LAST:event_txtSearchAdminKeyReleased

    private void tblAdminMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblAdminMouseClicked
        txtSelectedAdminId.setText((String) tblAdmin.getValueAt(tblAdmin.getSelectedRow(), 0));
        txtSelectedAdminUsername.setText((String) tblAdmin.getValueAt(tblAdmin.getSelectedRow(), 1));
        txtSelectedAdminNIC.setText((String) tblAdmin.getValueAt(tblAdmin.getSelectedRow(), 3));
        txtSelectedAdminPhoneNo.setText((String) tblAdmin.getValueAt(tblAdmin.getSelectedRow(), 4));
    }//GEN-LAST:event_tblAdminMouseClicked

    private void pwdRepeatAdminPasswordKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_pwdRepeatAdminPasswordKeyPressed
        if (evt.getKeyCode() == 10) {
            addNewAdministrator();
        }
    }//GEN-LAST:event_pwdRepeatAdminPasswordKeyPressed

    private void pwdAdminPasswordKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_pwdAdminPasswordKeyPressed
        if (evt.getKeyCode() == 10) {
            pwdRepeatAdminPassword.grabFocus();
        }
    }//GEN-LAST:event_pwdAdminPasswordKeyPressed

    private void txtAdminPhoneNoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtAdminPhoneNoKeyPressed
        if (evt.getKeyCode() == 10) {
            pwdAdminPassword.grabFocus();
        }
    }//GEN-LAST:event_txtAdminPhoneNoKeyPressed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        addNewAdministrator();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void txtAdminUsernameKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtAdminUsernameKeyPressed
        if (evt.getKeyCode() == 10) {
            txtAdminNIC.grabFocus();

        }
    }//GEN-LAST:event_txtAdminUsernameKeyPressed

    private void txtAdminNICKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtAdminNICKeyPressed
        if (evt.getKeyCode() == 10) {
            txtAdminPhoneNo.grabFocus();
        }
    }//GEN-LAST:event_txtAdminNICKeyPressed

    private void txtSelectedAdminNICKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtSelectedAdminNICKeyPressed
        if (evt.getKeyCode() == 10) {
            txtSelectedAdminPhoneNo.grabFocus();
        }
    }//GEN-LAST:event_txtSelectedAdminNICKeyPressed

    private void txtSelectedAdminPhoneNoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtSelectedAdminPhoneNoKeyPressed
        if (evt.getKeyCode() == 10) {
            updateAdministrator();
            jTabbedPaneAdministrators.setSelectedIndex(1);

        }
    }//GEN-LAST:event_txtSelectedAdminPhoneNoKeyPressed

    private void jButton9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton9ActionPerformed
        if (tblAdmin.getSelectedRowCount() == 0) {
            JOptionPane.showMessageDialog(this, "No Admin Selected!");
            jTabbedPaneAdministrators.setSelectedIndex(1);

        } else {

            deleteAdministrator();
            jTabbedPaneAdministrators.setSelectedIndex(1);
        }
    }//GEN-LAST:event_jButton9ActionPerformed

    private void jButton15ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton15ActionPerformed
        printSelectedAdministrator();
    }//GEN-LAST:event_jButton15ActionPerformed

    private void jButton8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton8ActionPerformed
        if (tblAdmin.getSelectedRowCount() == 0) {
            JOptionPane.showMessageDialog(this, "No Admin Selected!");
            jTabbedPaneAdministrators.setSelectedIndex(1);

        } else {
            updateAdministrator();
            jTabbedPaneAdministrators.setSelectedIndex(1);
        }
    }//GEN-LAST:event_jButton8ActionPerformed

    private void txtAdminNICKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtAdminNICKeyTyped
        char c = evt.getKeyChar();
        if (!(Character.isDigit(c))) {
            evt.consume();

        }
        String nic = txtAdminNIC.getText();
        int length = nic.length();
        if (length >= 12) {
            evt.consume();
        }
    }//GEN-LAST:event_txtAdminNICKeyTyped

    private void txtAdminPhoneNoKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtAdminPhoneNoKeyTyped
        char c = evt.getKeyChar();
        if (!(Character.isDigit(c))) {
            evt.consume();

        }
        String phoneNo = txtAdminPhoneNo.getText();
        int length = phoneNo.length();
        if (length >= 10) {
            evt.consume();
        }
    }//GEN-LAST:event_txtAdminPhoneNoKeyTyped

    private void jButton13ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton13ActionPerformed
        printAllAdmins();
    }//GEN-LAST:event_jButton13ActionPerformed

    private void txtSelectedAdminNICKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtSelectedAdminNICKeyTyped
        char c = evt.getKeyChar();
        if (!(Character.isDigit(evt.getKeyChar()))) {
            evt.consume();

        }
        String nic = txtSelectedAdminNIC.getText();
        int length = nic.length();
        if (length >= 12) {
            evt.consume();
        }
    }//GEN-LAST:event_txtSelectedAdminNICKeyTyped

    private void txtSelectedAdminPhoneNoKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtSelectedAdminPhoneNoKeyTyped
        char c = evt.getKeyChar();
        if (!(Character.isDigit(c))) {
            evt.consume();

        }
        String phoneNo = txtSelectedAdminPhoneNo.getText();
        int length = phoneNo.length();
        if (length >= 10) {
            evt.consume();
        }
    }//GEN-LAST:event_txtSelectedAdminPhoneNoKeyTyped

    private void jButton22ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton22ActionPerformed
        if (tblUserPassword.getSelectedRowCount() == 0) {
            JOptionPane.showMessageDialog(this, "No User Selected!");
            jTabbedPaneUserPasswords.setSelectedIndex(0);

        } else {
            changeUserPassword();
        }
    }//GEN-LAST:event_jButton22ActionPerformed

    private void jButton23ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton23ActionPerformed
        jTabbedPaneUserPasswords.setSelectedIndex(0);
    }//GEN-LAST:event_jButton23ActionPerformed

    private void tblUserPasswordMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblUserPasswordMouseClicked
        txtSelectedUserId.setText((String) tblUserPassword.getValueAt(tblUserPassword.getSelectedRow(), 0));
        txtSelectedUserUsername.setText((String) tblUserPassword.getValueAt(tblUserPassword.getSelectedRow(), 1));
        txtSelectedUserType.setText((String) tblUserPassword.getValueAt(tblUserPassword.getSelectedRow(), 2));
        txtSelectedUserPassword.setText((String) tblUserPassword.getValueAt(tblUserPassword.getSelectedRow(), 3));
    }//GEN-LAST:event_tblUserPasswordMouseClicked

    private void txtSearchUserKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtSearchUserKeyReleased
        searchUserPassowrd();
    }//GEN-LAST:event_txtSearchUserKeyReleased

    private void cmbSortUserActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbSortUserActionPerformed
        searchUserPassowrd();
    }//GEN-LAST:event_cmbSortUserActionPerformed

    private void jButton19ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton19ActionPerformed
        if (tblUserPassword.getSelectedRowCount() == 0) {
            JOptionPane.showMessageDialog(this, "No User Selected!");
        } else {
            jTabbedPaneUserPasswords.setSelectedIndex(1);
            pwdSelectedUserNewPassword.grabFocus();
        }
    }//GEN-LAST:event_jButton19ActionPerformed

    private void jButton25ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton25ActionPerformed
        txtSearchUser.setText(null);
        txtSelectedUserId.setText(null);
        txtSelectedUserUsername.setText(null);
        txtSelectedUserPassword.setText(null);
        txtSelectedUserType.setText(null);
        pwdSelectedUserNewPassword.setText(null);
        pwdSelectedUserRepeatPassword.setText(null);
        refreshUserPasswordTable();
        cmbSortUser.setSelectedIndex(0);
        cmbSortUserType.setSelectedIndex(0);
    }//GEN-LAST:event_jButton25ActionPerformed

    private void cmbSortUserTypeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbSortUserTypeActionPerformed
        searchUserType();
    }//GEN-LAST:event_cmbSortUserTypeActionPerformed

    private void pwdSelectedUserNewPasswordKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_pwdSelectedUserNewPasswordKeyPressed
        if (evt.getKeyCode() == 10) {
            pwdSelectedUserRepeatPassword.grabFocus();
        }
    }//GEN-LAST:event_pwdSelectedUserNewPasswordKeyPressed

    private void pwdSelectedUserRepeatPasswordKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_pwdSelectedUserRepeatPasswordKeyPressed
        if (evt.getKeyCode() == 10) {
            changeUserPassword();
        }
    }//GEN-LAST:event_pwdSelectedUserRepeatPasswordKeyPressed

    private void btnBackupActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBackupActionPerformed
        int option = JOptionPane.showConfirmDialog(this, "Do you want to create a New Backup?", "Backup Confirmation", JOptionPane.YES_NO_OPTION);
        if (option == 0) {

            if ("".equals(txtBackupLocation.getText())) {
                JOptionPane.showMessageDialog(this, "Please Choose a Backup Location!", "Invalid Backup Location", JOptionPane.WARNING_MESSAGE);

                JFileChooser fc = new JFileChooser();
                fc.showSaveDialog(this);
                String currentDate = new SimpleDateFormat("dd_MM_yyyy").format(new Date());
                String currentTime = new SimpleDateFormat("hh_mm_a").format(new Date());

                try {
                    File f = fc.getSelectedFile();
                    path = f.getAbsolutePath();
                    path = path.replace('\\', '/');
                    path = path + "_" + currentDate + "-" + currentTime + ".sql";
                    txtBackupLocation.setText(path);
                } catch (Exception e) {
                }

            } else {

                Process p;
                try {
                    Runtime runtime = Runtime.getRuntime();
                    p = runtime.exec("C:/Program Files/MySQL/MySQL Server 5.7/bin/mysqldump.exe --host=localhost --user=root --password=22622 exfiltro_mart -r " + path);

                    int processcomplete = p.waitFor();
                    if (processcomplete == 0) {
                        ImageIcon icon = new ImageIcon(getClass().getResource("img/done.png"));
                        JOptionPane.showMessageDialog(this, "Backup Created Successfully!", "Done", JOptionPane.INFORMATION_MESSAGE, icon);
                        txtBackupLocation.setText(null);

                        String createBackupActivityLog = "INSERT INTO log(usr_id, usr_type, usr_username, log_activity, log_description, log_state) VALUES ((SELECT usr_id FROM user WHERE usr_username = '" + lblAdminUsername.getText() + "'),'Admin','" + lblAdminUsername.getText() + "','Create Backup','Admin created new backup on file location " + txtBackupLocation.getText() + "','SUCCESS')";

                        DB.DB.putData(createBackupActivityLog);

                    } else {
                        JOptionPane.showMessageDialog(this, "Backup Creation Failed!", "Failed", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (Exception e) {
                }
            }
        }
    }//GEN-LAST:event_btnBackupActionPerformed

    private void jButton17ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton17ActionPerformed
        JFileChooser fc = new JFileChooser();
        fc.showSaveDialog(this);
        String currentDate = new SimpleDateFormat("dd_MM_yyyy").format(new Date());
        String currentTime = new SimpleDateFormat("hh_mm_a").format(new Date());

        try {
            File f = fc.getSelectedFile();
            path = f.getAbsolutePath();
            path = path.replace('\\', '/');
            path = path + "_" + currentDate + "-" + currentTime + ".sql";
            txtBackupLocation.setText(path);
        } catch (Exception e) {
        }
    }//GEN-LAST:event_jButton17ActionPerformed

    private void btnBrowseRestorePathActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBrowseRestorePathActionPerformed
        JFileChooser fcrestore = new JFileChooser();
        fcrestore.showOpenDialog(this);
        try {
            File f = fcrestore.getSelectedFile();
            path = f.getAbsolutePath();
            path = path.replace('\\', '/');
            txtRestoreLocation.setText(path);
        } catch (Exception e) {
        }
    }//GEN-LAST:event_btnBrowseRestorePathActionPerformed

    private void btnRestoreActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRestoreActionPerformed
        String user = "root";
        String pwd = "22622";
        String[] restoreCmd = new String[]{"C:/Program Files/MySQL/MySQL Server 5.7/bin/mysql.exe", "--user=" + user, "--password=" + pwd, "-e", "source" + path};
        Process process;
        try {
            process = Runtime.getRuntime().exec(restoreCmd);
            int procCom = process.waitFor();
            if (procCom == 0) {
                ImageIcon icon = new ImageIcon(getClass().getResource("img/done.png"));
                JOptionPane.showMessageDialog(this, "Restore SuccessFul!", "Done", JOptionPane.INFORMATION_MESSAGE, icon);

            } else {
                JOptionPane.showMessageDialog(this, "Restore Failed!", "Failed", JOptionPane.ERROR_MESSAGE);
            }
        } catch (HeadlessException | IOException | InterruptedException e) {
        }
    }//GEN-LAST:event_btnRestoreActionPerformed

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
                new AdminSettings().setVisible(true);
            }
        });
    }

    private void setIcon() {
        setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("img/exfiltrat0z-icon.png")));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnBackup;
    private javax.swing.JButton btnBrowseRestorePath;
    private javax.swing.JButton btnRestore;
    private javax.swing.JComboBox<String> cmbSortAdmin;
    private javax.swing.JComboBox<String> cmbSortUser;
    private javax.swing.JComboBox<String> cmbSortUserType;
    private javax.swing.JButton jButton12;
    private javax.swing.JButton jButton13;
    private javax.swing.JButton jButton15;
    private javax.swing.JButton jButton17;
    private javax.swing.JButton jButton19;
    private javax.swing.JButton jButton22;
    private javax.swing.JButton jButton23;
    private javax.swing.JButton jButton25;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton7;
    private javax.swing.JButton jButton8;
    private javax.swing.JButton jButton9;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel30;
    private javax.swing.JLabel jLabel31;
    private javax.swing.JLabel jLabel32;
    private javax.swing.JLabel jLabel33;
    private javax.swing.JLabel jLabel34;
    private javax.swing.JLabel jLabel35;
    private javax.swing.JLabel jLabel36;
    private javax.swing.JLabel jLabel37;
    private javax.swing.JLabel jLabel38;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel47;
    private javax.swing.JLabel jLabel48;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel14;
    private javax.swing.JPanel jPanel15;
    private javax.swing.JPanel jPanel16;
    private javax.swing.JPanel jPanel17;
    private javax.swing.JPanel jPanel18;
    private javax.swing.JPanel jPanel22;
    private javax.swing.JPanel jPanel23;
    private javax.swing.JPanel jPanel24;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JTabbedPane jTabbedPaneAdministrators;
    private javax.swing.JTabbedPane jTabbedPaneEmployee;
    private javax.swing.JTabbedPane jTabbedPaneUserPasswords;
    private javax.swing.JLabel lblAdminCount;
    public static final javax.swing.JLabel lblAdminUsername = new javax.swing.JLabel();
    private javax.swing.JLabel lblDate;
    private javax.swing.JLabel lblTime;
    private javax.swing.JLabel lblUserCount;
    private javax.swing.JPanel pnlHomeSubSelection;
    private javax.swing.JPanel pnlLogout;
    private javax.swing.JPasswordField pwdAdminPassword;
    private javax.swing.JPasswordField pwdRepeatAdminPassword;
    private javax.swing.JPasswordField pwdSelectedUserNewPassword;
    private javax.swing.JPasswordField pwdSelectedUserRepeatPassword;
    private javax.swing.JTable tblAdmin;
    private javax.swing.JTable tblUserPassword;
    private javax.swing.JTextField txtAdminNIC;
    private javax.swing.JTextField txtAdminPhoneNo;
    private javax.swing.JTextField txtAdminUsername;
    private javax.swing.JTextField txtBackupLocation;
    private javax.swing.JTextField txtRestoreLocation;
    private javax.swing.JTextField txtSearchAdmin;
    private javax.swing.JTextField txtSearchUser;
    private javax.swing.JTextField txtSelectedAdminId;
    private javax.swing.JTextField txtSelectedAdminNIC;
    private javax.swing.JTextField txtSelectedAdminPhoneNo;
    private javax.swing.JTextField txtSelectedAdminUsername;
    private javax.swing.JTextField txtSelectedUserId;
    private javax.swing.JTextField txtSelectedUserPassword;
    private javax.swing.JTextField txtSelectedUserType;
    private javax.swing.JTextField txtSelectedUserUsername;
    private javax.swing.JLabel version;
    // End of variables declaration//GEN-END:variables
}
