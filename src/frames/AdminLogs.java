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
public class AdminLogs extends javax.swing.JFrame {

    Timer timer;
    String month;
    String year;
    String date;
    String today;

    public AdminLogs() {
        initComponents();
        AdminLogs.this.setExtendedState(JFrame.MAXIMIZED_BOTH);
        AdminLogs.this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setIcon();
        setTableheader();
        refreshSystemLogsTable();

        AdminLogs.this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {

                AdminLogs.this.dispose();
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
        JTableHeader emptbl = tblSystemLogs.getTableHeader();
        emptbl.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        emptbl.setForeground(Color.white);
        emptbl.setBackground(new Color(51, 51, 51));
        tblSystemLogs.getTableHeader().setReorderingAllowed(false);
    }

    private void calculateSystemLogCount() {
        int count = tblSystemLogs.getRowCount();
        lblLogCount.setText("Log(s) Count : " + Integer.toString(count));

    }

    private void refreshSystemLogsTable() {

        try {
            DefaultTableModel dtm = (DefaultTableModel) tblSystemLogs.getModel();
            ResultSet rs = DB.DB.getData("SELECT * FROM log order by log_timestamp desc ");
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
                ResultSet rs = DB.DB.getData("SELECT * FROM log WHERE usr_id like '" + txtSearchSystemLogs.getText() + "%' || usr_type like '" + txtSearchSystemLogs.getText() + "%' || log_activity like '" + txtSearchSystemLogs.getText() + "%' || log_description like '" + txtSearchSystemLogs.getText() + "%' || log_state like '" + txtSearchSystemLogs.getText() + "%' || log_timestamp like '" + txtSearchSystemLogs.getText() + "%' order by log_timestamp desc");

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
                ResultSet rs = DB.DB.getData("SELECT * FROM log WHERE usr_id like '" + txtSearchSystemLogs.getText() + "%' AND log_activity = 'Intruder Login' || usr_type like '" + txtSearchSystemLogs.getText() + "%' AND log_activity = 'Intruder Login' || log_activity like '" + txtSearchSystemLogs.getText() + "%' AND log_activity = 'Intruder Login' || log_description like '" + txtSearchSystemLogs.getText() + "%' AND log_activity = 'Intruder Login' || log_state like '" + txtSearchSystemLogs.getText() + "%'  AND log_activity = 'Intruder Login' || log_timestamp like '" + txtSearchSystemLogs.getText() + "%' AND log_activity = 'Intruder Login' order by log_timestamp desc");

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
                ResultSet rs = DB.DB.getData("SELECT * FROM log WHERE usr_id like '" + txtSearchSystemLogs.getText() + "%' AND log_activity = 'Employee Login' || usr_type like '" + txtSearchSystemLogs.getText() + "%' AND log_activity = 'Employee Login' || log_activity like '" + txtSearchSystemLogs.getText() + "%' AND log_activity = 'Employee Login' || log_description like '" + txtSearchSystemLogs.getText() + "%' AND log_activity = 'Employee Login' || log_state like '" + txtSearchSystemLogs.getText() + "%'  AND log_activity = 'Employee Login' || log_timestamp like '" + txtSearchSystemLogs.getText() + "%'  AND log_activity = 'Employee Login' order by log_timestamp desc");

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
                ResultSet rs = DB.DB.getData("SELECT * FROM log WHERE usr_id like '" + txtSearchSystemLogs.getText() + "%' AND log_activity = 'Admin Login' || usr_type like '" + txtSearchSystemLogs.getText() + "%' AND log_activity = 'Admin Login' || log_activity like '" + txtSearchSystemLogs.getText() + "%' AND log_activity = 'Admin Login' || log_description like '" + txtSearchSystemLogs.getText() + "%' AND log_activity = 'Admin Login' || log_state like '" + txtSearchSystemLogs.getText() + "%'  AND log_activity = 'Admin Login' || log_timestamp like '" + txtSearchSystemLogs.getText() + "%'  AND log_activity = 'Admin Login' order by log_timestamp desc");

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
                ResultSet rs = DB.DB.getData("SELECT * FROM log WHERE usr_id like '" + txtSearchSystemLogs.getText() + "%' AND log_activity = 'Employee Logout' || usr_type like '" + txtSearchSystemLogs.getText() + "%' AND log_activity = 'Employee Logout' || log_activity like '" + txtSearchSystemLogs.getText() + "%' AND log_activity = 'Employee Logout' || log_description like '" + txtSearchSystemLogs.getText() + "%' AND log_activity = 'Employee Logout' || log_state like '" + txtSearchSystemLogs.getText() + "%'  AND log_activity = 'Employee Logout' || log_timestamp like '" + txtSearchSystemLogs.getText() + "%'  AND log_activity = 'Employee Logout' order by log_timestamp desc");

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
                ResultSet rs = DB.DB.getData("SELECT * FROM log WHERE usr_id like '" + txtSearchSystemLogs.getText() + "%' AND log_activity = 'Admin Logout' || usr_type like '" + txtSearchSystemLogs.getText() + "%' AND log_activity = 'Admin Logout' || log_activity like '" + txtSearchSystemLogs.getText() + "%' AND log_activity = 'Admin Logout' || log_description like '" + txtSearchSystemLogs.getText() + "%' AND log_activity = 'Admin Logout' || log_state like '" + txtSearchSystemLogs.getText() + "%'  AND log_activity = 'Admin Logout' || log_timestamp like '" + txtSearchSystemLogs.getText() + "%'  AND log_activity = 'Admin Logout' order by log_timestamp desc");

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
                ResultSet rs = DB.DB.getData("SELECT * FROM log WHERE usr_id like '" + txtSearchSystemLogs.getText() + "%' AND log_activity = 'Add New Supplier' || usr_type like '" + txtSearchSystemLogs.getText() + "%' AND log_activity = 'Add New Supplier' || log_activity like '" + txtSearchSystemLogs.getText() + "%' AND log_activity = 'Add New Supplier' || log_description like '" + txtSearchSystemLogs.getText() + "%' AND log_activity = 'Add New Supplier' || log_state like '" + txtSearchSystemLogs.getText() + "%'  AND log_activity = 'Add New Supplier' || log_timestamp like '" + txtSearchSystemLogs.getText() + "%'  AND log_activity = 'Add New Supplier' order by log_timestamp desc");

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
                ResultSet rs = DB.DB.getData("SELECT * FROM log WHERE usr_id like '" + txtSearchSystemLogs.getText() + "%' AND log_activity = 'Update Supplier' || usr_type like '" + txtSearchSystemLogs.getText() + "%' AND log_activity = 'Update Supplier' || log_activity like '" + txtSearchSystemLogs.getText() + "%' AND log_activity = 'Update Supplier' || log_description like '" + txtSearchSystemLogs.getText() + "%' AND log_activity = 'Update Supplier' || log_state like '" + txtSearchSystemLogs.getText() + "%'  AND log_activity = 'Update Supplier' || log_timestamp like '" + txtSearchSystemLogs.getText() + "%'  AND log_activity = 'Update Supplier' order by log_timestamp desc");

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
            
            case 8:

                try {
                DefaultTableModel dtm = (DefaultTableModel) tblSystemLogs.getModel();
                ResultSet rs = DB.DB.getData("SELECT * FROM log WHERE usr_id like '" + txtSearchSystemLogs.getText() + "%' AND log_activity = 'Delete Supplier' || usr_type like '" + txtSearchSystemLogs.getText() + "%' AND log_activity = 'Delete Supplier' || log_activity like '" + txtSearchSystemLogs.getText() + "%' AND log_activity = 'Delete Supplier' || log_description like '" + txtSearchSystemLogs.getText() + "%' AND log_activity = 'Delete Supplier' || log_state like '" + txtSearchSystemLogs.getText() + "%'  AND log_activity = 'Delete Supplier' || log_timestamp like '" + txtSearchSystemLogs.getText() + "%'  AND log_activity = 'Delete Supplier' order by log_timestamp desc");

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
            
            case 9:

                try {
                DefaultTableModel dtm = (DefaultTableModel) tblSystemLogs.getModel();
                ResultSet rs = DB.DB.getData("SELECT * FROM log WHERE usr_id like '" + txtSearchSystemLogs.getText() + "%' AND log_activity = 'Add Item to Supplier Item Collection' || usr_type like '" + txtSearchSystemLogs.getText() + "%' AND log_activity = 'Add Item to Supplier Item Collection' || log_activity like '" + txtSearchSystemLogs.getText() + "%' AND log_activity = 'Add Item to Supplier Item Collection' || log_description like '" + txtSearchSystemLogs.getText() + "%' AND log_activity = 'Add Item to Supplier Item Collection' || log_state like '" + txtSearchSystemLogs.getText() + "%'  AND log_activity = 'Add Item to Supplier Item Collection' || log_timestamp like '" + txtSearchSystemLogs.getText() + "%'  AND log_activity = 'Add Item to Supplier Item Collection' order by log_timestamp desc");

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
            
            case 10:

                try {
                DefaultTableModel dtm = (DefaultTableModel) tblSystemLogs.getModel();
                ResultSet rs = DB.DB.getData("SELECT * FROM log WHERE usr_id like '" + txtSearchSystemLogs.getText() + "%' AND log_activity = 'Update Supplier Item' || usr_type like '" + txtSearchSystemLogs.getText() + "%' AND log_activity = 'Update Supplier Item' || log_activity like '" + txtSearchSystemLogs.getText() + "%' AND log_activity = 'Update Supplier Item' || log_description like '" + txtSearchSystemLogs.getText() + "%' AND log_activity = 'Update Supplier Item' || log_state like '" + txtSearchSystemLogs.getText() + "%'  AND log_activity = 'Update Supplier Item' || log_timestamp like '" + txtSearchSystemLogs.getText() + "%'  AND log_activity = 'Update Supplier Item' order by log_timestamp desc");

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
            
            case 11:

                try {
                DefaultTableModel dtm = (DefaultTableModel) tblSystemLogs.getModel();
                ResultSet rs = DB.DB.getData("SELECT * FROM log WHERE usr_id like '" + txtSearchSystemLogs.getText() + "%' AND log_activity = 'Delete Supplier Item' || usr_type like '" + txtSearchSystemLogs.getText() + "%' AND log_activity = 'Delete Supplier Item' || log_activity like '" + txtSearchSystemLogs.getText() + "%' AND log_activity = 'Delete Supplier Item' || log_description like '" + txtSearchSystemLogs.getText() + "%' AND log_activity = 'Delete Supplier Item' || log_state like '" + txtSearchSystemLogs.getText() + "%'  AND log_activity = 'Delete Supplier Item' || log_timestamp like '" + txtSearchSystemLogs.getText() + "%'  AND log_activity = 'Delete Supplier Item' order by log_timestamp desc");

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
            
            case 12:

                try {
                DefaultTableModel dtm = (DefaultTableModel) tblSystemLogs.getModel();
                ResultSet rs = DB.DB.getData("SELECT * FROM log WHERE usr_id like '" + txtSearchSystemLogs.getText() + "%' AND log_activity = 'Add New Employee' || usr_type like '" + txtSearchSystemLogs.getText() + "%' AND log_activity = 'Add New Employee' || log_activity like '" + txtSearchSystemLogs.getText() + "%' AND log_activity = 'Add New Employee' || log_description like '" + txtSearchSystemLogs.getText() + "%' AND log_activity = 'Add New Employee' || log_state like '" + txtSearchSystemLogs.getText() + "%'  AND log_activity = 'Add New Employee' || log_timestamp like '" + txtSearchSystemLogs.getText() + "%'  AND log_activity = 'Add New Employee' order by log_timestamp desc");

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
            
            case 13:

                try {
                DefaultTableModel dtm = (DefaultTableModel) tblSystemLogs.getModel();
                ResultSet rs = DB.DB.getData("SELECT * FROM log WHERE usr_id like '" + txtSearchSystemLogs.getText() + "%' AND log_activity = 'Update Employee' || usr_type like '" + txtSearchSystemLogs.getText() + "%' AND log_activity = 'Update Employee' || log_activity like '" + txtSearchSystemLogs.getText() + "%' AND log_activity = 'Update Employee' || log_description like '" + txtSearchSystemLogs.getText() + "%' AND log_activity = 'Update Employee' || log_state like '" + txtSearchSystemLogs.getText() + "%'  AND log_activity = 'Update Employee' || log_timestamp like '" + txtSearchSystemLogs.getText() + "%'  AND log_activity = 'Update Employee' order by log_timestamp desc");

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
            case 14:

                try {
                DefaultTableModel dtm = (DefaultTableModel) tblSystemLogs.getModel();
                ResultSet rs = DB.DB.getData("SELECT * FROM log WHERE usr_id like '" + txtSearchSystemLogs.getText() + "%' AND log_activity = 'Delete Employee' || usr_type like '" + txtSearchSystemLogs.getText() + "%' AND log_activity = 'Delete Employee' || log_activity like '" + txtSearchSystemLogs.getText() + "%' AND log_activity = 'Delete Employee' || log_description like '" + txtSearchSystemLogs.getText() + "%' AND log_activity = 'Delete Employee' || log_state like '" + txtSearchSystemLogs.getText() + "%'  AND log_activity = 'Delete Employee' || log_timestamp like '" + txtSearchSystemLogs.getText() + "%'  AND log_activity = 'Delete Employee' order by log_timestamp desc");

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
            case 15:

                try {
                DefaultTableModel dtm = (DefaultTableModel) tblSystemLogs.getModel();
                ResultSet rs = DB.DB.getData("SELECT * FROM log WHERE usr_id like '" + txtSearchSystemLogs.getText() + "%' AND log_activity = 'Employee System Exit' || usr_type like '" + txtSearchSystemLogs.getText() + "%' AND log_activity = 'Employee System Exit' || log_activity like '" + txtSearchSystemLogs.getText() + "%' AND log_activity = 'Employee System Exit' || log_description like '" + txtSearchSystemLogs.getText() + "%' AND log_activity = 'Employee System Exit' || log_state like '" + txtSearchSystemLogs.getText() + "%'  AND log_activity = 'Employee System Exit' || log_timestamp like '" + txtSearchSystemLogs.getText() + "%'  AND log_activity = 'Employee System Exit' order by log_timestamp desc");

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
            case 16:

                try {
                DefaultTableModel dtm = (DefaultTableModel) tblSystemLogs.getModel();
                ResultSet rs = DB.DB.getData("SELECT * FROM log WHERE usr_id like '" + txtSearchSystemLogs.getText() + "%' AND log_activity = 'Admin System Exit' || usr_type like '" + txtSearchSystemLogs.getText() + "%' AND log_activity = 'Admin System Exit' || log_activity like '" + txtSearchSystemLogs.getText() + "%' AND log_activity = 'Admin System Exit' || log_description like '" + txtSearchSystemLogs.getText() + "%' AND log_activity = 'Admin System Exit' || log_state like '" + txtSearchSystemLogs.getText() + "%'  AND log_activity = 'Admin System Exit' || log_timestamp like '" + txtSearchSystemLogs.getText() + "%'  AND log_activity = 'Admin System Exit' order by log_timestamp desc");

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
            case 17:

                try {
                DefaultTableModel dtm = (DefaultTableModel) tblSystemLogs.getModel();
                ResultSet rs = DB.DB.getData("SELECT * FROM log WHERE usr_id like '" + txtSearchSystemLogs.getText() + "%' AND log_activity = 'Employee Payroll' || usr_type like '" + txtSearchSystemLogs.getText() + "%' AND log_activity = 'Employee Payroll' || log_activity like '" + txtSearchSystemLogs.getText() + "%' AND log_activity = 'Employee Payroll' || log_description like '" + txtSearchSystemLogs.getText() + "%' AND log_activity = 'Employee Payroll' || log_state like '" + txtSearchSystemLogs.getText() + "%'  AND log_activity = 'Employee Payroll' || log_timestamp like '" + txtSearchSystemLogs.getText() + "%'  AND log_activity = 'Employee Payroll' order by log_timestamp desc");

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
            case 18:

                try {
                DefaultTableModel dtm = (DefaultTableModel) tblSystemLogs.getModel();
                ResultSet rs = DB.DB.getData("SELECT * FROM log WHERE usr_id like '" + txtSearchSystemLogs.getText() + "%' AND log_activity = 'Purchase Order' || usr_type like '" + txtSearchSystemLogs.getText() + "%' AND log_activity = 'Purchase Order' || log_activity like '" + txtSearchSystemLogs.getText() + "%' AND log_activity = 'Purchase Order' || log_description like '" + txtSearchSystemLogs.getText() + "%' AND log_activity = 'Purchase Order' || log_state like '" + txtSearchSystemLogs.getText() + "%'  AND log_activity = 'Purchase Order' || log_timestamp like '" + txtSearchSystemLogs.getText() + "%'  AND log_activity = 'Purchase Order' order by log_timestamp desc");

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
            case 19:

                try {
                DefaultTableModel dtm = (DefaultTableModel) tblSystemLogs.getModel();
                ResultSet rs = DB.DB.getData("SELECT * FROM log WHERE usr_id like '" + txtSearchSystemLogs.getText() + "%' AND log_activity = 'Bad Order' || usr_type like '" + txtSearchSystemLogs.getText() + "%' AND log_activity = 'Bad Order' || log_activity like '" + txtSearchSystemLogs.getText() + "%' AND log_activity = 'Bad Order' || log_description like '" + txtSearchSystemLogs.getText() + "%' AND log_activity = 'Bad Order' || log_state like '" + txtSearchSystemLogs.getText() + "%'  AND log_activity = 'Bad Order' || log_timestamp like '" + txtSearchSystemLogs.getText() + "%'  AND log_activity = 'Bad Order' order by log_timestamp desc");

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
            case 20:

                try {
                DefaultTableModel dtm = (DefaultTableModel) tblSystemLogs.getModel();
                ResultSet rs = DB.DB.getData("SELECT * FROM log WHERE usr_id like '" + txtSearchSystemLogs.getText() + "%' AND log_activity = 'Add New Stock Item' || usr_type like '" + txtSearchSystemLogs.getText() + "%' AND log_activity = 'Add New Stock Item' || log_activity like '" + txtSearchSystemLogs.getText() + "%' AND log_activity = 'Add New Stock Item' || log_description like '" + txtSearchSystemLogs.getText() + "%' AND log_activity = 'Add New Stock Item' || log_state like '" + txtSearchSystemLogs.getText() + "%'  AND log_activity = 'Add New Stock Item' || log_timestamp like '" + txtSearchSystemLogs.getText() + "%'  AND log_activity = 'Add New Stock Item' order by log_timestamp desc");

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
            case 21:

                try {
                DefaultTableModel dtm = (DefaultTableModel) tblSystemLogs.getModel();
                ResultSet rs = DB.DB.getData("SELECT * FROM log WHERE usr_id like '" + txtSearchSystemLogs.getText() + "%' AND log_activity = 'GRN Checked' || usr_type like '" + txtSearchSystemLogs.getText() + "%' AND log_activity = 'GRN Checked' || log_activity like '" + txtSearchSystemLogs.getText() + "%' AND log_activity = 'GRN Checked' || log_description like '" + txtSearchSystemLogs.getText() + "%' AND log_activity = 'GRN Checked' || log_state like '" + txtSearchSystemLogs.getText() + "%'  AND log_activity = 'GRN Checked' || log_timestamp like '" + txtSearchSystemLogs.getText() + "%'  AND log_activity = 'GRN Checked' order by log_timestamp desc");

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
            case 22:

                try {
                DefaultTableModel dtm = (DefaultTableModel) tblSystemLogs.getModel();
                ResultSet rs = DB.DB.getData("SELECT * FROM log WHERE usr_id like '" + txtSearchSystemLogs.getText() + "%' AND log_activity = 'Re-Order Stock Item' || usr_type like '" + txtSearchSystemLogs.getText() + "%' AND log_activity = 'Re-Order Stock Item' || log_activity like '" + txtSearchSystemLogs.getText() + "%' AND log_activity = 'Re-Order Stock Item' || log_description like '" + txtSearchSystemLogs.getText() + "%' AND log_activity = 'Re-Order Stock Item' || log_state like '" + txtSearchSystemLogs.getText() + "%'  AND log_activity = 'Re-Order Stock Item' || log_timestamp like '" + txtSearchSystemLogs.getText() + "%'  AND log_activity = 'Re-Order Stock Item' order by log_timestamp desc");

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
            case 23:

                try {
                DefaultTableModel dtm = (DefaultTableModel) tblSystemLogs.getModel();
                ResultSet rs = DB.DB.getData("SELECT * FROM log WHERE usr_id like '" + txtSearchSystemLogs.getText() + "%' AND log_activity = 'Add New Sale Item' || usr_type like '" + txtSearchSystemLogs.getText() + "%' AND log_activity = 'Add New Sale Item' || log_activity like '" + txtSearchSystemLogs.getText() + "%' AND log_activity = 'Add New Sale Item' || log_description like '" + txtSearchSystemLogs.getText() + "%' AND log_activity = 'Add New Sale Item' || log_state like '" + txtSearchSystemLogs.getText() + "%'  AND log_activity = 'Add New Sale Item' || log_timestamp like '" + txtSearchSystemLogs.getText() + "%'  AND log_activity = 'Add New Sale Item' order by log_timestamp desc");

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
            case 24:

                try {
                DefaultTableModel dtm = (DefaultTableModel) tblSystemLogs.getModel();
                ResultSet rs = DB.DB.getData("SELECT * FROM log WHERE usr_id like '" + txtSearchSystemLogs.getText() + "%' AND log_activity = 'Remove New Sale Item' || usr_type like '" + txtSearchSystemLogs.getText() + "%' AND log_activity = 'Remove New Sale Item' || log_activity like '" + txtSearchSystemLogs.getText() + "%' AND log_activity = 'Remove New Sale Item' || log_description like '" + txtSearchSystemLogs.getText() + "%' AND log_activity = 'Remove New Sale Item' || log_state like '" + txtSearchSystemLogs.getText() + "%'  AND log_activity = 'Remove New Sale Item' || log_timestamp like '" + txtSearchSystemLogs.getText() + "%'  AND log_activity = 'Remove New Sale Item' order by log_timestamp desc");

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
            case 25:

                try {
                DefaultTableModel dtm = (DefaultTableModel) tblSystemLogs.getModel();
                ResultSet rs = DB.DB.getData("SELECT * FROM log WHERE usr_id like '" + txtSearchSystemLogs.getText() + "%' AND log_activity = 'Issue Invoice / Items' || usr_type like '" + txtSearchSystemLogs.getText() + "%' AND log_activity = 'Issue Invoice / Items' || log_activity like '" + txtSearchSystemLogs.getText() + "%' AND log_activity = 'Issue Invoice / Items' || log_description like '" + txtSearchSystemLogs.getText() + "%' AND log_activity = 'Issue Invoice / Items' || log_state like '" + txtSearchSystemLogs.getText() + "%'  AND log_activity = 'Issue Invoice / Items' || log_timestamp like '" + txtSearchSystemLogs.getText() + "%'  AND log_activity = 'Issue Invoice / Items' order by log_timestamp desc");

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
            case 26:

                try {
                DefaultTableModel dtm = (DefaultTableModel) tblSystemLogs.getModel();
                ResultSet rs = DB.DB.getData("SELECT * FROM log WHERE usr_id like '" + txtSearchSystemLogs.getText() + "%' AND log_activity = 'System Backup' || usr_type like '" + txtSearchSystemLogs.getText() + "%' AND log_activity = 'System Backup' || log_activity like '" + txtSearchSystemLogs.getText() + "%' AND log_activity = 'System Backup' || log_description like '" + txtSearchSystemLogs.getText() + "%' AND log_activity = 'System Backup' || log_state like '" + txtSearchSystemLogs.getText() + "%'  AND log_activity = 'System Backup' || log_timestamp like '" + txtSearchSystemLogs.getText() + "%'  AND log_activity = 'System Backup' order by log_timestamp desc");

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
            case 27:

                try {
                DefaultTableModel dtm = (DefaultTableModel) tblSystemLogs.getModel();
                ResultSet rs = DB.DB.getData("SELECT * FROM log WHERE usr_id like '" + txtSearchSystemLogs.getText() + "%' AND log_activity = 'System Restore' || usr_type like '" + txtSearchSystemLogs.getText() + "%' AND log_activity = 'System Restore' || log_activity like '" + txtSearchSystemLogs.getText() + "%' AND log_activity = 'System Restore' || log_description like '" + txtSearchSystemLogs.getText() + "%' AND log_activity = 'System Restore' || log_state like '" + txtSearchSystemLogs.getText() + "%'  AND log_activity = 'System Restore' || log_timestamp like '" + txtSearchSystemLogs.getText() + "%'  AND log_activity = 'System Restore' order by log_timestamp desc");

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
            case 28:

                try {
                DefaultTableModel dtm = (DefaultTableModel) tblSystemLogs.getModel();
                ResultSet rs = DB.DB.getData("SELECT * FROM log WHERE usr_id like '" + txtSearchSystemLogs.getText() + "%' AND log_activity = 'Add New Admin' || usr_type like '" + txtSearchSystemLogs.getText() + "%' AND log_activity = 'Add New Admin' || log_activity like '" + txtSearchSystemLogs.getText() + "%' AND log_activity = 'Add New Admin' || log_description like '" + txtSearchSystemLogs.getText() + "%' AND log_activity = 'Add New Admin' || log_state like '" + txtSearchSystemLogs.getText() + "%'  AND log_activity = 'Add New Admin' || log_timestamp like '" + txtSearchSystemLogs.getText() + "%'  AND log_activity = 'Add New Admin' order by log_timestamp desc");

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
            case 29:

                try {
                DefaultTableModel dtm = (DefaultTableModel) tblSystemLogs.getModel();
                ResultSet rs = DB.DB.getData("SELECT * FROM log WHERE usr_id like '" + txtSearchSystemLogs.getText() + "%' AND log_activity = 'Update Admin' || usr_type like '" + txtSearchSystemLogs.getText() + "%' AND log_activity = 'Update Admin' || log_activity like '" + txtSearchSystemLogs.getText() + "%' AND log_activity = 'Update Admin' || log_description like '" + txtSearchSystemLogs.getText() + "%' AND log_activity = 'Update Admin' || log_state like '" + txtSearchSystemLogs.getText() + "%'  AND log_activity = 'Update Admin' || log_timestamp like '" + txtSearchSystemLogs.getText() + "%'  AND log_activity = 'Update Admin' order by log_timestamp desc");

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
            case 30:

                try {
                DefaultTableModel dtm = (DefaultTableModel) tblSystemLogs.getModel();
                ResultSet rs = DB.DB.getData("SELECT * FROM log WHERE usr_id like '" + txtSearchSystemLogs.getText() + "%' AND log_activity = 'Delete Admin' || usr_type like '" + txtSearchSystemLogs.getText() + "%' AND log_activity = 'Delete Admin' || log_activity like '" + txtSearchSystemLogs.getText() + "%' AND log_activity = 'Delete Admin' || log_description like '" + txtSearchSystemLogs.getText() + "%' AND log_activity = 'Delete Admin' || log_state like '" + txtSearchSystemLogs.getText() + "%'  AND log_activity = 'Delete Admin' || log_timestamp like '" + txtSearchSystemLogs.getText() + "%'  AND log_activity = 'Delete Admin' order by log_timestamp desc");

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
            case 31:

                try {
                DefaultTableModel dtm = (DefaultTableModel) tblSystemLogs.getModel();
                ResultSet rs = DB.DB.getData("SELECT * FROM log WHERE usr_id like '" + txtSearchSystemLogs.getText() + "%' AND log_activity = 'Delete Stock Item' || usr_type like '" + txtSearchSystemLogs.getText() + "%' AND log_activity = 'Delete Stock Item' || log_activity like '" + txtSearchSystemLogs.getText() + "%' AND log_activity = 'Delete Stock Item' || log_description like '" + txtSearchSystemLogs.getText() + "%' AND log_activity = 'Delete Stock Item' || log_state like '" + txtSearchSystemLogs.getText() + "%'  AND log_activity = 'Delete Stock Item' || log_timestamp like '" + txtSearchSystemLogs.getText() + "%'  AND log_activity = 'Delete Stock Item' order by log_timestamp desc");

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
            case 32:

                try {
                DefaultTableModel dtm = (DefaultTableModel) tblSystemLogs.getModel();
                ResultSet rs = DB.DB.getData("SELECT * FROM log WHERE usr_id like '" + txtSearchSystemLogs.getText() + "%' AND log_activity = 'Return Stock Item' || usr_type like '" + txtSearchSystemLogs.getText() + "%' AND log_activity = 'Return Stock Item' || log_activity like '" + txtSearchSystemLogs.getText() + "%' AND log_activity = 'Return Stock Item' || log_description like '" + txtSearchSystemLogs.getText() + "%' AND log_activity = 'Return Stock Item' || log_state like '" + txtSearchSystemLogs.getText() + "%'  AND log_activity = 'Return Stock Item' || log_timestamp like '" + txtSearchSystemLogs.getText() + "%'  AND log_activity = 'Return Stock Item' order by log_timestamp desc");

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
            case 33:

                try {
                DefaultTableModel dtm = (DefaultTableModel) tblSystemLogs.getModel();
                ResultSet rs = DB.DB.getData("SELECT * FROM log WHERE usr_id like '" + txtSearchSystemLogs.getText() + "%' AND log_activity = 'Change Password' || usr_type like '" + txtSearchSystemLogs.getText() + "%' AND log_activity = 'Change Password' || log_activity like '" + txtSearchSystemLogs.getText() + "%' AND log_activity = 'Change Password' || log_description like '" + txtSearchSystemLogs.getText() + "%' AND log_activity = 'Change Password' || log_state like '" + txtSearchSystemLogs.getText() + "%'  AND log_activity = 'Change Password' || log_timestamp like '" + txtSearchSystemLogs.getText() + "%'  AND log_activity = 'Change Password' order by log_timestamp desc");

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
        jButton6 = new javax.swing.JButton();
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

        lblAdminUsername.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        lblAdminUsername.setForeground(new java.awt.Color(255, 255, 255));
        lblAdminUsername.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblAdminUsername.setText("Admin");

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
        cmbSortSystemLogs.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "View All Logs", "Intruder Login", "Employee Login", "Admin Login", "Employee Logout", "Admin Logout", "Add New Supplier", "Update Supplier", "Delete Supplier", "Add Item to Item Collection", "Update Supplier Item", "Delete Supplier Item", "Add New Employee", "Update Employee", "Delete Employee", "Employee System Exit", "Admin System Exit", "Employee Payroll", "Purchase Order", "Bad Order", "Add New Stock Item", "GRN Checked", "Re-Order Stock Item", "Add New Sale Item", "Remove New Sale Item", "Issue Invoice / Items", "System Backup", "System Restore", "Add New Administrator", "Update Administrator", "Delete Administrator", "Delete Stock Item", "Return Stock Item", "Change Password" }));
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

        jButton6.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jButton6.setForeground(new java.awt.Color(255, 255, 255));
        jButton6.setText(" Print All");
        jButton6.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton6ActionPerformed(evt);
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
                        .addComponent(jButton7)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jButton6))
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
                    .addComponent(jButton7)
                    .addComponent(jButton6))
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
                .addComponent(lblAdminUsername, javax.swing.GroupLayout.PREFERRED_SIZE, 134, javax.swing.GroupLayout.PREFERRED_SIZE)
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

            String logoutActivityLog = "INSERT INTO log(usr_id, usr_type, usr_username, log_activity, log_description, log_state) VALUES ((SELECT usr_id FROM user WHERE usr_username = '" + lblAdminUsername.getText() + "'),'Admin','" + lblAdminUsername.getText() + "','Admin Logout', '" + lblAdminUsername.getText() + " logged out' ,'SUCCESS')";
            try {
                DB.DB.putData(logoutActivityLog);
            } catch (Exception e) {
            }

            AdminLogs.this.dispose();

            Login login = new Login();
            login.setVisible(true);
            for (float j = 0f; j < 0.985; j += 0.000001) {
                login.setOpacity(j);
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

    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed
        printLogs();
    }//GEN-LAST:event_jButton6ActionPerformed

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
                new AdminLogs().setVisible(true);
            }
        });
    }

    private void setIcon() {
        setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("img/exfiltrat0z-icon.png")));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox<String> cmbSortSystemLogs;
    private javax.swing.JButton jButton6;
    private javax.swing.JButton jButton7;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTabbedPane jTabbedPane1;
    public static final javax.swing.JLabel lblAdminUsername = new javax.swing.JLabel();
    private javax.swing.JLabel lblDate;
    private javax.swing.JLabel lblLogCount;
    private javax.swing.JLabel lblTime;
    private javax.swing.JPanel pnlHomeSubSelection;
    private javax.swing.JPanel pnlLogout;
    private javax.swing.JTable tblSystemLogs;
    private javax.swing.JTextField txtSearchSystemLogs;
    // End of variables declaration//GEN-END:variables
}
