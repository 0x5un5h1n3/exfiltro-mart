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
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.regex.Pattern;
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
public class AdminEmpPayroll extends javax.swing.JFrame {

    Timer timer;
    String month;
    String year;
    String date;
    String today;

    public AdminEmpPayroll() {
        initComponents();
        AdminEmpPayroll.this.setExtendedState(JFrame.MAXIMIZED_BOTH);
        AdminEmpPayroll.this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setIcon();
        setTableheader();
        refreshEmployeePayrollTable();
        refreshEmployeePayrollHistoryTable();

        AdminEmpPayroll.this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {

                AdminEmpPayroll.this.dispose();
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
        JTableHeader todayEmpAtttbl = tblEmployeePayroll.getTableHeader();
        todayEmpAtttbl.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        todayEmpAtttbl.setForeground(Color.white);
        todayEmpAtttbl.setBackground(new Color(51, 51, 51));
        tblEmployeePayroll.getTableHeader().setReorderingAllowed(false);

        JTableHeader payrollHis = tblEmployeePayrollHistory.getTableHeader();
        payrollHis.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        payrollHis.setForeground(Color.white);
        payrollHis.setBackground(new Color(51, 51, 51));
        tblEmployeePayrollHistory.getTableHeader().setReorderingAllowed(false);

    }

    private void refreshEmployeePayrollTable() {
        try {

            Date DateMonth = new Date();
            SimpleDateFormat toMonth = new SimpleDateFormat("MMMM");
            month = "" + toMonth.format(DateMonth);

            Date DateYear = new Date();
            SimpleDateFormat toYear = new SimpleDateFormat("YYYY");
            year = "" + toYear.format(DateYear);

            DefaultTableModel dtm = (DefaultTableModel) tblEmployeePayroll.getModel();
            ResultSet rs = DB.DB.getData("SELECT * FROM payroll WHERE pay_month = '" + month + "'  AND pay_year = '" + year + "' order by pay_stat desc");
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
            ResultSet paidCount = DB.DB.getData("SELECT count(pay_id) FROM payroll WHERE pay_stat = 'PAID' AND pay_month = '" + month + "'  AND pay_year = '" + year + "' ");
            if (paidCount.next()) {
                int i = paidCount.getInt(1);
                lblPaidCount.setText("Paid Count : " + i);
            }

            ResultSet unpaidCount = DB.DB.getData("SELECT count(pay_id) FROM payroll WHERE pay_stat = 'UNPAID' AND pay_month = '" + month + "' AND pay_year = '" + year + "' ");
            if (unpaidCount.next()) {
                int j = unpaidCount.getInt(1);
                lblUnpaidCount.setText("Unpaid Count : " + j);
            }

        } catch (Exception e) {
        }
    }

    private void searchPaymentSummary() {
        switch (cmbSortEmployeePayroll.getSelectedIndex()) {
            case 0:

                Date DateMonth = new Date();
                SimpleDateFormat toMonth = new SimpleDateFormat("MMMM");
                month = "" + toMonth.format(DateMonth);

                try {
                    DefaultTableModel dtm = (DefaultTableModel) tblEmployeePayroll.getModel();
                    ResultSet rs = DB.DB.getData("SELECT * FROM payroll WHERE pay_month = '" + month + "'  AND pay_year = '" + year + "' AND usr_id like '" + txtSearchPayrollSummary.getText() + "%' order by usr_id ");
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
                DefaultTableModel dtm = (DefaultTableModel) tblEmployeePayroll.getModel();
                ResultSet rs = DB.DB.getData("SELECT * FROM payroll WHERE pay_month = '" + month + "'  AND pay_year = '" + year + "' AND usr_username like '" + txtSearchPayrollSummary.getText() + "%' order by usr_username ");
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
                DefaultTableModel dtm = (DefaultTableModel) tblEmployeePayroll.getModel();
                ResultSet rs = DB.DB.getData("SELECT * FROM payroll WHERE pay_month = '" + month + "'  AND pay_year = '" + year + "' AND pay_month like '" + txtSearchPayrollSummary.getText() + "%' order by pay_month ");
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
                DefaultTableModel dtm = (DefaultTableModel) tblEmployeePayroll.getModel();
                ResultSet rs = DB.DB.getData("SELECT * FROM payroll WHERE pay_month = '" + month + "' AND pay_year = '" + year + "' AND pay_year like '" + txtSearchPayrollSummary.getText() + "%' order by pay_year ");
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

                DefaultTableModel dtm = (DefaultTableModel) tblEmployeePayroll.getModel();
                ResultSet rs = DB.DB.getData("SELECT * FROM payroll WHERE pay_month = '" + month + "'  AND pay_year = '" + year + "' AND pay_date like '" + txtSearchPayrollSummary.getText() + "%' order by pay_date ");
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

                DefaultTableModel dtm = (DefaultTableModel) tblEmployeePayroll.getModel();
                ResultSet rs = DB.DB.getData("SELECT * FROM payroll WHERE pay_month = '" + month + "' AND  AND pay_year = '" + year + "' pay_attendance like '" + txtSearchPayrollSummary.getText() + "%' order by pay_attendance ");
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
            case 6:
                try {

                DefaultTableModel dtm = (DefaultTableModel) tblEmployeePayroll.getModel();
                ResultSet rs = DB.DB.getData("SELECT * FROM payroll WHERE pay_month = '" + month + "'  AND pay_year = '" + year + "' AND pay_salary like '" + txtSearchPayrollSummary.getText() + "%' order by pay_salary ");
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
            case 7:
                try {
                DefaultTableModel dtm = (DefaultTableModel) tblEmployeePayroll.getModel();
                ResultSet rs = DB.DB.getData("SELECT * FROM payroll WHERE pay_month = '" + month + "'  AND pay_year = '" + year + "' AND pay_stat = 'PAID' AND pay_id like '" + txtSearchPayrollSummary.getText() + "%' order by pay_stat ");
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
            case 8:
                try {
                DefaultTableModel dtm = (DefaultTableModel) tblEmployeePayroll.getModel();
                ResultSet rs = DB.DB.getData("SELECT * FROM payroll WHERE pay_month = '" + month + "'  AND pay_year = '" + year + "' AND pay_stat = 'UNPAID' AND pay_id like '" + txtSearchPayrollSummary.getText() + "%' order by pay_stat ");
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
            default:
                break;
        }
    }

    private void payEmployee() {
        int option = JOptionPane.showConfirmDialog(this, "Do you want to settle payment of this Employee?", "", JOptionPane.YES_NO_OPTION);
        if (option == 0) {

            if (!txtSelectedPayrollEmployeeExtraSalary.getText().equals("")
                    & !txtSelectedPayrollEmployeePayment.getText().equals("")) {

                if ("PAID".equals(txtSelectedPayrollEmployeeStatus.getText())) {
                    JOptionPane.showMessageDialog(this, "Already Paid!", "Invalid Payroll", JOptionPane.WARNING_MESSAGE);

                } else if (!Pattern.matches("^[0-9]+$", txtSelectedPayrollEmployeeExtraSalary.getText())) {
                    JOptionPane.showMessageDialog(this, "Invalid Extra Salary!", "Invalid Data", JOptionPane.WARNING_MESSAGE);
                    txtSelectedPayrollEmployeeExtraSalary.grabFocus();
                    txtSelectedPayrollEmployeeExtraSalary.setText(null);

                } else if (!Pattern.matches("^[0-9]+$", txtSelectedPayrollEmployeeDeductSalary.getText())) {
                    JOptionPane.showMessageDialog(this, "Invalid Deduction Salary!", "Invalid Data", JOptionPane.WARNING_MESSAGE);
                    txtSelectedPayrollEmployeeDeductSalary.grabFocus();
                    txtSelectedPayrollEmployeeDeductSalary.setText(null);

                } else {

                    try {

                        Date DateDate = new Date();
                        SimpleDateFormat toDate = new SimpleDateFormat("yyyy-MM-dd");
                        date = "" + toDate.format(DateDate);

                        DB.DB.putData("UPDATE payroll SET pay_date = '" + date + "', pay_salary ='" + txtSelectedPayrollEmployeePayment.getText() + "', pay_stat = 'PAID' WHERE usr_id ='" + txtSelectedPayrollEmployeeId.getText() + "' ");
                        String employeePayrollActivityLog = "INSERT INTO log(usr_id, usr_type, usr_username, log_activity, log_description, log_state) VALUES ((SELECT usr_id FROM user WHERE usr_username = '" + lblAdminUsername.getText() + "'),'Admin','" + lblAdminUsername.getText() + "','Employee Payroll','Admin paid employee " + txtSelectedPayrollEmployeeUsername.getText() + "','SUCCESS')";

                        DB.DB.putData(employeePayrollActivityLog);
                        ImageIcon icon = new ImageIcon(getClass().getResource("img/done.png"));
                        JOptionPane.showMessageDialog(this, "Employee Payment Settled!", "Done", JOptionPane.INFORMATION_MESSAGE, icon);
                        txtSelectedPayrollEmployeeStatus.setText("PAID");
                        refreshEmployeePayrollTable();

                        refreshEmployeePayrollHistoryTable();

                    } catch (Exception e) {
                    }
                }
            } else {
                JOptionPane.showMessageDialog(this, "Please Fill All the Fields!", "Invalid Data", JOptionPane.WARNING_MESSAGE);
            }
        }
    }

    private void refreshEmployeePayrollHistoryTable() {
        try {

            Date DateMonth = new Date();
            SimpleDateFormat toMonth = new SimpleDateFormat("MMMM");
            month = "" + toMonth.format(DateMonth);

            DefaultTableModel dtm = (DefaultTableModel) tblEmployeePayrollHistory.getModel();
            ResultSet rs = DB.DB.getData("SELECT * FROM payroll WHERE pay_stat = 'PAID' order by pay_date desc ");
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
                ResultSet rs = DB.DB.getData("SELECT * FROM payroll WHERE pay_stat = 'PAID' AND pay_date like '" + txtSearchPayrollHistory.getText() + "%' order by pay_date desc ");
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
                ResultSet rs = DB.DB.getData("SELECT * FROM payroll WHERE pay_stat = 'PAID' AND pay_id like '" + txtSearchPayrollHistory.getText() + "%' order by pay_id ");
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
                ResultSet rs = DB.DB.getData("SELECT * FROM payroll WHERE pay_stat = 'PAID' AND usr_username like '" + txtSearchPayrollHistory.getText() + "%' order by usr_username ");
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
                ResultSet rs = DB.DB.getData("SELECT * FROM payroll WHERE pay_stat = 'PAID' AND pay_month like '" + txtSearchPayrollHistory.getText() + "%' order by pay_month ");
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
                ResultSet rs = DB.DB.getData("SELECT * FROM payroll WHERE pay_stat = 'PAID' AND pay_year like '" + txtSearchPayrollHistory.getText() + "%' order by pay_year ");
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
                ResultSet rs = DB.DB.getData("SELECT * FROM payroll WHERE pay_stat = 'PAID' AND pay_date like '" + txtSearchPayrollHistory.getText() + "%' order by pay_date ");
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
            case 6:
                try {

                DefaultTableModel dtm = (DefaultTableModel) tblEmployeePayrollHistory.getModel();
                ResultSet rs = DB.DB.getData("SELECT * FROM payroll WHERE pay_stat = 'PAID' AND pay_attendance like '" + txtSearchPayrollHistory.getText() + "%' order by pay_attendance ");
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
            case 7:
                try {
                DefaultTableModel dtm = (DefaultTableModel) tblEmployeePayrollHistory.getModel();
                ResultSet rs = DB.DB.getData("SELECT * FROM payroll WHERE pay_stat = 'PAID' AND pay_salary like '" + txtSearchPayrollHistory.getText() + "%' order by pay_salary ");
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

    public void printSelectedEmployeePaymentBill() {

        Date DateMonth = new Date();
        SimpleDateFormat toMonth = new SimpleDateFormat("MMMM");
        month = "" + toMonth.format(DateMonth);

        Date DateYear = new Date();
        SimpleDateFormat toYear = new SimpleDateFormat("yyyy");
        year = "" + toYear.format(DateYear);

        HashMap a = new HashMap();
        a.put("id", txtSelectedPayrollEmployeeId.getText());
        a.put("month", month);
        a.put("year", year);

        new Thread() {
            @Override
            public void run() {
                try {

                    String reportSource = "C:\\Program Files\\Exfiltro Reports\\viewSelectedEmployeePaymentBill.jrxml";

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
        jTabbedPanePayroll = new javax.swing.JTabbedPane();
        jPanel3 = new javax.swing.JPanel();
        txtSearchPayrollSummary = new javax.swing.JTextField();
        jLabel11 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblEmployeePayroll = new javax.swing.JTable();
        jButton7 = new javax.swing.JButton();
        cmbSortEmployeePayroll = new javax.swing.JComboBox<>();
        lblPaidCount = new javax.swing.JLabel();
        jLabel108 = new javax.swing.JLabel();
        lblUnpaidCount = new javax.swing.JLabel();
        jButton8 = new javax.swing.JButton();
        jPanel5 = new javax.swing.JPanel();
        jLabel17 = new javax.swing.JLabel();
        txtSelectedPayrollEmployeeStatus = new javax.swing.JTextField();
        txtSelectedPayrollEmployeeMonth = new javax.swing.JTextField();
        jLabel16 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        txtSelectedPayrollEmployeeUsername = new javax.swing.JTextField();
        txtSelectedPayrollEmployeeId = new javax.swing.JTextField();
        jLabel15 = new javax.swing.JLabel();
        jButton10 = new javax.swing.JButton();
        txtSelectedPayrollEmployeeAttendance = new javax.swing.JTextField();
        jLabel18 = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        txtSelectedPayrollEmployeeSalary = new javax.swing.JTextField();
        txtSelectedPayrollEmployeeExtraSalary = new javax.swing.JTextField();
        jLabel23 = new javax.swing.JLabel();
        txtSelectedPayrollEmployeeDeductSalary = new javax.swing.JTextField();
        jLabel21 = new javax.swing.JLabel();
        jButton12 = new javax.swing.JButton();
        jButton13 = new javax.swing.JButton();
        jLabel22 = new javax.swing.JLabel();
        txtSelectedPayrollEmployeePayment = new javax.swing.JTextField();
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

        lblAdminUsername.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        lblAdminUsername.setForeground(new java.awt.Color(255, 255, 255));
        lblAdminUsername.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblAdminUsername.setText("Admin");

        jPanel11.setBackground(new java.awt.Color(204, 204, 204));

        jTabbedPanePayroll.setBackground(new java.awt.Color(34, 34, 34));
        jTabbedPanePayroll.setForeground(new java.awt.Color(255, 255, 255));
        jTabbedPanePayroll.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jTabbedPanePayroll.setOpaque(true);

        jPanel3.setBackground(new java.awt.Color(34, 34, 34));
        jPanel3.setForeground(new java.awt.Color(255, 255, 255));

        txtSearchPayrollSummary.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        txtSearchPayrollSummary.setForeground(new java.awt.Color(255, 255, 255));
        txtSearchPayrollSummary.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtSearchPayrollSummaryKeyReleased(evt);
            }
        });

        jLabel11.setIcon(new javax.swing.ImageIcon(getClass().getResource("/frames/img/search.icon.png"))); // NOI18N

        tblEmployeePayroll.setBackground(new java.awt.Color(34, 34, 34));
        tblEmployeePayroll.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        tblEmployeePayroll.setModel(new javax.swing.table.DefaultTableModel(
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
        tblEmployeePayroll.setGridColor(new java.awt.Color(61, 61, 61));
        tblEmployeePayroll.setShowGrid(true);
        tblEmployeePayroll.getTableHeader().setReorderingAllowed(false);
        tblEmployeePayroll.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblEmployeePayrollMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tblEmployeePayroll);

        jButton7.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jButton7.setForeground(new java.awt.Color(255, 255, 255));
        jButton7.setText("Pay");
        jButton7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton7ActionPerformed(evt);
            }
        });

        cmbSortEmployeePayroll.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        cmbSortEmployeePayroll.setForeground(new java.awt.Color(255, 255, 255));
        cmbSortEmployeePayroll.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Id", "Username", "Month", "Year", "Date", "Attendance", "Payment", "Paid", "Unpaid" }));
        cmbSortEmployeePayroll.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbSortEmployeePayrollActionPerformed(evt);
            }
        });

        lblPaidCount.setBackground(new java.awt.Color(0, 0, 0));
        lblPaidCount.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        lblPaidCount.setText("Paid Count : 0");

        jLabel108.setBackground(new java.awt.Color(0, 0, 0));
        jLabel108.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel108.setText("|");

        lblUnpaidCount.setBackground(new java.awt.Color(0, 0, 0));
        lblUnpaidCount.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        lblUnpaidCount.setText("Unpaid Count : 0");

        jButton8.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jButton8.setForeground(new java.awt.Color(255, 255, 255));
        jButton8.setText(" Refresh");
        jButton8.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jButton8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton8ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addGap(52, 52, 52)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(lblPaidCount)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel108)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblUnpaidCount)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButton8)
                        .addGap(18, 18, 18)
                        .addComponent(jButton7, javax.swing.GroupLayout.PREFERRED_SIZE, 66, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 1096, Short.MAX_VALUE)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(txtSearchPayrollSummary, javax.swing.GroupLayout.DEFAULT_SIZE, 873, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(cmbSortEmployeePayroll, 0, 173, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel11)))
                .addGap(52, 52, 52))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(23, 23, 23)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtSearchPayrollSummary, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel11)
                    .addComponent(cmbSortEmployeePayroll, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 393, Short.MAX_VALUE)
                .addGap(25, 25, 25)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(lblPaidCount)
                        .addComponent(lblUnpaidCount)
                        .addComponent(jLabel108))
                    .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jButton7)
                        .addComponent(jButton8)))
                .addGap(36, 36, 36))
        );

        jTabbedPanePayroll.addTab("Payroll Summary", jPanel3);

        jPanel5.setBackground(new java.awt.Color(34, 34, 34));

        jLabel17.setBackground(new java.awt.Color(0, 0, 0));
        jLabel17.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel17.setForeground(new java.awt.Color(255, 255, 255));
        jLabel17.setText("Status");

        txtSelectedPayrollEmployeeStatus.setBackground(new java.awt.Color(51, 51, 51));
        txtSelectedPayrollEmployeeStatus.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        txtSelectedPayrollEmployeeStatus.setEnabled(false);

        txtSelectedPayrollEmployeeMonth.setBackground(new java.awt.Color(51, 51, 51));
        txtSelectedPayrollEmployeeMonth.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        txtSelectedPayrollEmployeeMonth.setEnabled(false);

        jLabel16.setBackground(new java.awt.Color(0, 0, 0));
        jLabel16.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel16.setForeground(new java.awt.Color(255, 255, 255));
        jLabel16.setText("Month");

        jLabel14.setBackground(new java.awt.Color(0, 0, 0));
        jLabel14.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel14.setForeground(new java.awt.Color(255, 255, 255));
        jLabel14.setText("Username");

        txtSelectedPayrollEmployeeUsername.setBackground(new java.awt.Color(51, 51, 51));
        txtSelectedPayrollEmployeeUsername.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        txtSelectedPayrollEmployeeUsername.setEnabled(false);

        txtSelectedPayrollEmployeeId.setBackground(new java.awt.Color(51, 51, 51));
        txtSelectedPayrollEmployeeId.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        txtSelectedPayrollEmployeeId.setEnabled(false);

        jLabel15.setBackground(new java.awt.Color(0, 0, 0));
        jLabel15.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel15.setForeground(new java.awt.Color(255, 255, 255));
        jLabel15.setText("Employee Id");

        jButton10.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jButton10.setForeground(new java.awt.Color(255, 255, 255));
        jButton10.setText("Pay");
        jButton10.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton10ActionPerformed(evt);
            }
        });

        txtSelectedPayrollEmployeeAttendance.setBackground(new java.awt.Color(51, 51, 51));
        txtSelectedPayrollEmployeeAttendance.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        txtSelectedPayrollEmployeeAttendance.setEnabled(false);

        jLabel18.setBackground(new java.awt.Color(0, 0, 0));
        jLabel18.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel18.setForeground(new java.awt.Color(255, 255, 255));
        jLabel18.setText("Attendance");

        jLabel19.setBackground(new java.awt.Color(0, 0, 0));
        jLabel19.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel19.setForeground(new java.awt.Color(255, 255, 255));
        jLabel19.setText("Salary + Extra");

        txtSelectedPayrollEmployeeSalary.setBackground(new java.awt.Color(51, 51, 51));
        txtSelectedPayrollEmployeeSalary.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        txtSelectedPayrollEmployeeSalary.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txtSelectedPayrollEmployeeSalary.setEnabled(false);

        txtSelectedPayrollEmployeeExtraSalary.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        txtSelectedPayrollEmployeeExtraSalary.setForeground(new java.awt.Color(255, 255, 255));
        txtSelectedPayrollEmployeeExtraSalary.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txtSelectedPayrollEmployeeExtraSalary.setText("0");
        txtSelectedPayrollEmployeeExtraSalary.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtSelectedPayrollEmployeeExtraSalaryKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtSelectedPayrollEmployeeExtraSalaryKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtSelectedPayrollEmployeeExtraSalaryKeyTyped(evt);
            }
        });

        jLabel23.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel23.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel23.setText("+");

        txtSelectedPayrollEmployeeDeductSalary.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        txtSelectedPayrollEmployeeDeductSalary.setForeground(new java.awt.Color(255, 255, 255));
        txtSelectedPayrollEmployeeDeductSalary.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txtSelectedPayrollEmployeeDeductSalary.setText("0");
        txtSelectedPayrollEmployeeDeductSalary.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtSelectedPayrollEmployeeDeductSalaryKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtSelectedPayrollEmployeeDeductSalaryKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtSelectedPayrollEmployeeDeductSalaryKeyTyped(evt);
            }
        });

        jLabel21.setBackground(new java.awt.Color(0, 0, 0));
        jLabel21.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel21.setForeground(new java.awt.Color(255, 255, 255));
        jLabel21.setText("Deductions");

        jButton12.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jButton12.setForeground(new java.awt.Color(255, 255, 255));
        jButton12.setText(" Print Invoice");
        jButton12.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton12ActionPerformed(evt);
            }
        });

        jButton13.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jButton13.setForeground(new java.awt.Color(255, 255, 255));
        jButton13.setText("Go Back");
        jButton13.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton13ActionPerformed(evt);
            }
        });

        jLabel22.setBackground(new java.awt.Color(0, 0, 0));
        jLabel22.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel22.setForeground(new java.awt.Color(255, 255, 255));
        jLabel22.setText("Payment");

        txtSelectedPayrollEmployeePayment.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        txtSelectedPayrollEmployeePayment.setForeground(new java.awt.Color(255, 255, 255));
        txtSelectedPayrollEmployeePayment.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txtSelectedPayrollEmployeePayment.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtSelectedPayrollEmployeePaymentKeyPressed(evt);
            }
        });

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(52, 52, 52)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel15)
                    .addComponent(jLabel14)
                    .addComponent(jLabel16)
                    .addComponent(jLabel17)
                    .addComponent(jLabel18)
                    .addComponent(jLabel19)
                    .addComponent(jLabel21)
                    .addComponent(jLabel22))
                .addGap(51, 51, 51)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(txtSelectedPayrollEmployeeDeductSalary)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(txtSelectedPayrollEmployeeSalary, javax.swing.GroupLayout.PREFERRED_SIZE, 126, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel23, javax.swing.GroupLayout.PREFERRED_SIZE, 15, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtSelectedPayrollEmployeeExtraSalary, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(txtSelectedPayrollEmployeeStatus, javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtSelectedPayrollEmployeeMonth, javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtSelectedPayrollEmployeeId, javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtSelectedPayrollEmployeeUsername)
                    .addComponent(txtSelectedPayrollEmployeeAttendance, javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtSelectedPayrollEmployeePayment))
                .addGap(78, 78, 78)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jButton10)
                    .addComponent(jButton12)
                    .addComponent(jButton13))
                .addContainerGap(525, Short.MAX_VALUE))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(45, 45, 45)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel15)
                    .addComponent(txtSelectedPayrollEmployeeId, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton10))
                .addGap(18, 18, 18)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel14)
                    .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(txtSelectedPayrollEmployeeUsername, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jButton12)))
                .addGap(18, 18, 18)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtSelectedPayrollEmployeeMonth, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel16))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtSelectedPayrollEmployeeStatus, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel17)))
                    .addComponent(jButton13))
                .addGap(18, 18, 18)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtSelectedPayrollEmployeeAttendance, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel18))
                .addGap(18, 18, 18)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtSelectedPayrollEmployeeSalary, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel19)
                    .addComponent(txtSelectedPayrollEmployeeExtraSalary, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel23))
                .addGap(18, 18, 18)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtSelectedPayrollEmployeeDeductSalary, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel21))
                .addGap(18, 18, 18)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtSelectedPayrollEmployeePayment, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel22))
                .addContainerGap(160, Short.MAX_VALUE))
        );

        jTabbedPanePayroll.addTab("Manage Payroll", jPanel5);

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
        cmbSortEmployeePayrollHistory.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Recent", "Id", "Username", "Month", "Year", "Date", "Attendance", "Payment" }));
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

    private void cmbSortEmployeePayrollActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbSortEmployeePayrollActionPerformed
        searchPaymentSummary();
    }//GEN-LAST:event_cmbSortEmployeePayrollActionPerformed

    private void jButton7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton7ActionPerformed
        String selectedEmployeePayrollStatus = (txtSelectedPayrollEmployeeStatus.getText());

        if (selectedEmployeePayrollStatus.equals("PAID")) {
            JOptionPane.showMessageDialog(this, "Payment Already Settled!");
        } else {

            if ((tblEmployeePayroll.getSelectedRowCount() == 0)) {
                JOptionPane.showMessageDialog(this, "No Employee Selected!");

            } else {
                jTabbedPanePayroll.setSelectedIndex(1);
                txtSelectedPayrollEmployeeExtraSalary.grabFocus();
            }
        }

    }//GEN-LAST:event_jButton7ActionPerformed

    private void txtSearchPayrollSummaryKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtSearchPayrollSummaryKeyReleased
        searchPaymentSummary();
    }//GEN-LAST:event_txtSearchPayrollSummaryKeyReleased

    private void jButton10ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton10ActionPerformed
        if (tblEmployeePayroll.getSelectedRowCount() == 0) {
            JOptionPane.showMessageDialog(this, "No Employee Selected!");
            jTabbedPanePayroll.setSelectedIndex(0);

        } else {
            payEmployee();
        }
    }//GEN-LAST:event_jButton10ActionPerformed

    private void jButton8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton8ActionPerformed
        txtSearchPayrollSummary.setText(null);
        txtSelectedPayrollEmployeeId.setText(null);
        txtSelectedPayrollEmployeeUsername.setText(null);
        txtSelectedPayrollEmployeeMonth.setText(null);
        txtSelectedPayrollEmployeeStatus.setText(null);
        txtSelectedPayrollEmployeeAttendance.setText(null);
        txtSelectedPayrollEmployeeSalary.setText(null);
        txtSelectedPayrollEmployeeExtraSalary.setText(null);
        txtSelectedPayrollEmployeeDeductSalary.setText(null);
        txtSelectedPayrollEmployeePayment.setText(null);
        refreshEmployeePayrollTable();
    }//GEN-LAST:event_jButton8ActionPerformed

    private void txtSearchPayrollHistoryKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtSearchPayrollHistoryKeyReleased
        searchPaymentHistory();
    }//GEN-LAST:event_txtSearchPayrollHistoryKeyReleased

    private void cmbSortEmployeePayrollHistoryActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbSortEmployeePayrollHistoryActionPerformed
        searchPaymentHistory();
    }//GEN-LAST:event_cmbSortEmployeePayrollHistoryActionPerformed

    private void jButton12ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton12ActionPerformed
        printSelectedEmployeePaymentBill();
    }//GEN-LAST:event_jButton12ActionPerformed

    private void jButton13ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton13ActionPerformed
        jTabbedPanePayroll.setSelectedIndex(0);
    }//GEN-LAST:event_jButton13ActionPerformed

    private void jButton9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton9ActionPerformed
        refreshEmployeePayrollHistoryTable();
        txtSearchPayrollHistory.setText(null);
    }//GEN-LAST:event_jButton9ActionPerformed

    private void txtSelectedPayrollEmployeeExtraSalaryKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtSelectedPayrollEmployeeExtraSalaryKeyReleased
        try {
            DecimalFormat ft = new DecimalFormat("#.##");

            double salary = Double.parseDouble(txtSelectedPayrollEmployeeSalary.getText());
            double extra = Double.parseDouble(txtSelectedPayrollEmployeeExtraSalary.getText());
            double total = extra + salary;
            txtSelectedPayrollEmployeePayment.setText(ft.format(total));

        } catch (NumberFormatException e) {
        }
    }//GEN-LAST:event_txtSelectedPayrollEmployeeExtraSalaryKeyReleased

    private void txtSelectedPayrollEmployeeDeductSalaryKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtSelectedPayrollEmployeeDeductSalaryKeyReleased
        try {
            DecimalFormat ft = new DecimalFormat("#.##");

            double salaryAndExtra = Integer.parseInt(txtSelectedPayrollEmployeeSalary.getText()) + Integer.parseInt(txtSelectedPayrollEmployeeExtraSalary.getText());
            double salary = salaryAndExtra;
            double deduction = Double.parseDouble(txtSelectedPayrollEmployeeDeductSalary.getText());
            double total = salary - deduction;
            txtSelectedPayrollEmployeePayment.setText(ft.format(total));

        } catch (NumberFormatException e) {
        }
    }//GEN-LAST:event_txtSelectedPayrollEmployeeDeductSalaryKeyReleased

    private void txtSelectedPayrollEmployeeExtraSalaryKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtSelectedPayrollEmployeeExtraSalaryKeyPressed
        if (evt.getKeyCode() == 10) {

            if (txtSelectedPayrollEmployeeExtraSalary.getText().isEmpty()) {
                txtSelectedPayrollEmployeeExtraSalary.setText("0");
            }

            txtSelectedPayrollEmployeeDeductSalary.grabFocus();

        }
    }//GEN-LAST:event_txtSelectedPayrollEmployeeExtraSalaryKeyPressed

    private void txtSelectedPayrollEmployeeDeductSalaryKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtSelectedPayrollEmployeeDeductSalaryKeyPressed
        if (evt.getKeyCode() == 10) {

            if (txtSelectedPayrollEmployeeDeductSalary.getText().isEmpty()) {
                txtSelectedPayrollEmployeeDeductSalary.setText("0");
            }

            txtSelectedPayrollEmployeePayment.grabFocus();

        }
    }//GEN-LAST:event_txtSelectedPayrollEmployeeDeductSalaryKeyPressed

    private void txtSelectedPayrollEmployeeExtraSalaryKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtSelectedPayrollEmployeeExtraSalaryKeyTyped
        char c = evt.getKeyChar();

        if (!(Character.isDigit(c))) {
            evt.consume();

        }
        String extraSalary = txtSelectedPayrollEmployeeExtraSalary.getText();
        int length = extraSalary.length();
        if (length >= 6) {
            evt.consume();
        }
    }//GEN-LAST:event_txtSelectedPayrollEmployeeExtraSalaryKeyTyped

    private void txtSelectedPayrollEmployeeDeductSalaryKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtSelectedPayrollEmployeeDeductSalaryKeyTyped
        char c = evt.getKeyChar();

        if (!(Character.isDigit(c))) {
            evt.consume();

        }
        String extraSalary = txtSelectedPayrollEmployeeDeductSalary.getText();
        int length = extraSalary.length();
        if (length >= 6) {
            evt.consume();
        }
    }//GEN-LAST:event_txtSelectedPayrollEmployeeDeductSalaryKeyTyped

    private void txtSelectedPayrollEmployeePaymentKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtSelectedPayrollEmployeePaymentKeyPressed
        if (evt.getKeyCode() == 10) {
            payEmployee();
        }
    }//GEN-LAST:event_txtSelectedPayrollEmployeePaymentKeyPressed

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

            AdminEmpPayroll.this.dispose();

            Login login = new Login();
            login.setVisible(true);
            for (float j = 0f; j < 0.985; j += 0.000001) {
                login.setOpacity(j);
            }
        } else {

        }
    }//GEN-LAST:event_jLabel4MouseClicked

    private void tblEmployeePayrollMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblEmployeePayrollMouseClicked
        Date DateToday = new Date();
        SimpleDateFormat toToday = new SimpleDateFormat("yyyy-MM-dd");
        today = "" + toToday.format(DateToday);

        txtSelectedPayrollEmployeeId.setText((String) tblEmployeePayroll.getValueAt(tblEmployeePayroll.getSelectedRow(), 0));
        txtSelectedPayrollEmployeeUsername.setText((String) tblEmployeePayroll.getValueAt(tblEmployeePayroll.getSelectedRow(), 1));
        txtSelectedPayrollEmployeeMonth.setText((String) tblEmployeePayroll.getValueAt(tblEmployeePayroll.getSelectedRow(), 2));
        txtSelectedPayrollEmployeeStatus.setText((String) tblEmployeePayroll.getValueAt(tblEmployeePayroll.getSelectedRow(), 7));
        txtSelectedPayrollEmployeeAttendance.setText((String) tblEmployeePayroll.getValueAt(tblEmployeePayroll.getSelectedRow(), 5));
        txtSelectedPayrollEmployeeSalary.setText((String) tblEmployeePayroll.getValueAt(tblEmployeePayroll.getSelectedRow(), 6));
        txtSelectedPayrollEmployeeExtraSalary.setText("0");
        txtSelectedPayrollEmployeePayment.setText((String) tblEmployeePayroll.getValueAt(tblEmployeePayroll.getSelectedRow(), 6));
    }//GEN-LAST:event_tblEmployeePayrollMouseClicked

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
                new AdminEmpPayroll().setVisible(true);
            }
        });
    }

    private void setIcon() {
        setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("img/exfiltrat0z-icon.png")));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox<String> cmbSortEmployeePayroll;
    private javax.swing.JComboBox<String> cmbSortEmployeePayrollHistory;
    private javax.swing.JButton jButton10;
    private javax.swing.JButton jButton12;
    private javax.swing.JButton jButton13;
    private javax.swing.JButton jButton7;
    private javax.swing.JButton jButton8;
    private javax.swing.JButton jButton9;
    private javax.swing.JLabel jLabel108;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTabbedPane jTabbedPanePayroll;
    public static final javax.swing.JLabel lblAdminUsername = new javax.swing.JLabel();
    private javax.swing.JLabel lblDate;
    private javax.swing.JLabel lblPaidCount;
    private javax.swing.JLabel lblTime;
    private javax.swing.JLabel lblUnpaidCount;
    private javax.swing.JPanel pnlHomeSubSelection;
    private javax.swing.JPanel pnlLogout;
    private javax.swing.JTable tblEmployeePayroll;
    private javax.swing.JTable tblEmployeePayrollHistory;
    private javax.swing.JTextField txtSearchPayrollHistory;
    private javax.swing.JTextField txtSearchPayrollSummary;
    private javax.swing.JTextField txtSelectedPayrollEmployeeAttendance;
    private javax.swing.JTextField txtSelectedPayrollEmployeeDeductSalary;
    private javax.swing.JTextField txtSelectedPayrollEmployeeExtraSalary;
    private javax.swing.JTextField txtSelectedPayrollEmployeeId;
    private javax.swing.JTextField txtSelectedPayrollEmployeeMonth;
    private javax.swing.JTextField txtSelectedPayrollEmployeePayment;
    private javax.swing.JTextField txtSelectedPayrollEmployeeSalary;
    private javax.swing.JTextField txtSelectedPayrollEmployeeStatus;
    private javax.swing.JTextField txtSelectedPayrollEmployeeUsername;
    // End of variables declaration//GEN-END:variables
}
