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
public class AdminSupplier extends javax.swing.JFrame {

    Timer timer;
    String month;
    String year;
    String date;
    String today;

    public AdminSupplier() {
        initComponents();
        AdminSupplier.this.setExtendedState(JFrame.MAXIMIZED_BOTH);
        AdminSupplier.this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setIcon();
        setTableheader();
        generateSUPId();
        refreshSupplierTable();

        AdminSupplier.this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {

                AdminSupplier.this.dispose();
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
        JTableHeader suptbl = tblSupplier.getTableHeader();
        suptbl.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        suptbl.setForeground(Color.white);
        suptbl.setBackground(new Color(51, 51, 51));
        tblSupplier.getTableHeader().setReorderingAllowed(false);
    }

    private void generateSUPId() {
        try {

            ResultSet rs = DB.DB.getData("SELECT sup_id AS x FROM supplier ORDER BY sup_id DESC LIMIT 1");
            if (rs.next()) {

                int rowcount = Integer.parseInt(rs.getString("x"));
                rowcount++;

                this.txtSupplierId.setText("" + rowcount);

            }

        } catch (Exception e) {
        }
    }

    private void addNewSupplier() {

        int option = JOptionPane.showConfirmDialog(this, "Do you want to Add this Supplier?", "", JOptionPane.YES_NO_OPTION);
        if (option == 0) {
            if (!txtSupplierUsername.getText().equals("")
                    & !txtSupplierAddress.getText().equals("")
                    & !txtSupplierEmail.getText().equals("")
                    & !txtSupplierPhoneNo.getText().equals("")
                    & (!txtSupplierCompany.getText().equals(""))) {

                try {
                    ResultSet rs = DB.DB.getData("SELECT * FROM supplier WHERE sup_username like '" + txtSupplierUsername.getText() + "%' order by sup_id ");

                    if (rs.next()) {
                        JOptionPane.showMessageDialog(this, "Username Already Exists!", "Invalid Username", JOptionPane.WARNING_MESSAGE);

                    } else if (txtSupplierPhoneNo.getText().length() != 10) {
                        JOptionPane.showMessageDialog(this, "Invalid Phone Number Length!", "Invalid Data", JOptionPane.WARNING_MESSAGE);
                        txtSupplierPhoneNo.setText(null);
                        txtSupplierPhoneNo.grabFocus();

                    } else if (!Pattern.matches("^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$", txtSupplierEmail.getText())) {
                        JOptionPane.showMessageDialog(this, "Invalid Email!", "Invalid Data", JOptionPane.WARNING_MESSAGE);
                        txtSupplierEmail.setText(null);
                        txtSupplierEmail.grabFocus();

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

                        String addSupplier = "INSERT INTO supplier(sup_username, sup_address, sup_email, sup_phone, sup_company, sup_stat) VALUES ('" + txtSupplierUsername.getText().trim() + "','" + txtSupplierAddress.getText() + "','" + txtSupplierEmail.getText() + "','" + txtSupplierPhoneNo.getText() + "','" + txtSupplierCompany.getText() + "', '1')";
                        String AddSupplierActivityLog = "INSERT INTO log(usr_id, usr_type, usr_username, log_activity, log_description, log_state) VALUES ((SELECT usr_id FROM user WHERE usr_username = '" + lblAdminUsername.getText() + "'),'Admin','" + lblAdminUsername.getText() + "','Add New Supplier','Admin added new supplier " + txtSupplierUsername.getText() + "','SUCCESS')";

                        try {
                            DB.DB.putData(addSupplier);
                            DB.DB.putData(AddSupplierActivityLog);

                            refreshSupplierTable();

                            ImageIcon icon = new ImageIcon(getClass().getResource("img/done.png"));
                            JOptionPane.showMessageDialog(this, "Supplier Added!", "Done", JOptionPane.INFORMATION_MESSAGE, icon);

                            txtSupplierUsername.setText(null);
                            txtSupplierAddress.setText(null);
                            txtSupplierEmail.setText(null);
                            txtSupplierPhoneNo.setText(null);
                            txtSupplierCompany.setText(null);

                        } catch (Exception e) {
                        }

                    }
                } catch (Exception ex) {
                }
            } else {
                JOptionPane.showMessageDialog(this, "Please Fill All the Fields!", "Invalid Data", JOptionPane.WARNING_MESSAGE);
                txtSupplierUsername.grabFocus();
            }
        }
    }

    private void refreshSupplierTable() {
        try {
            DefaultTableModel dtm = (DefaultTableModel) tblSupplier.getModel();
            ResultSet rs = DB.DB.getData("SELECT * FROM supplier WHERE sup_stat = '1' ");

            dtm.setRowCount(0);

            while (rs.next()) {
                String a = (rs.getString(1));
                String b = (rs.getString(2));
                String c = (rs.getString(3));
                String d = (rs.getString(4));
                String e = (rs.getString(5));
                String f = (rs.getString(6));
                dtm.addRow(new Object[]{a, b, c, d, e, f});
            }

            lblEmployeeCount.setText("Supplier Count : " + tblSupplier.getRowCount());

        } catch (Exception e) {
        }

    }

    private void searchSupplier() {
        switch (cmbSortSupplier.getSelectedIndex()) {
            case 0:

                try {
                DefaultTableModel dtm = (DefaultTableModel) tblSupplier.getModel();
                ResultSet rs = DB.DB.getData("SELECT * FROM supplier WHERE sup_stat = '1' AND sup_id like '" + txtSearchSupplier.getText() + "%' order by sup_id ");
                dtm.setRowCount(0);

                while (rs.next()) {
                    String a = (rs.getString(1));
                    String b = (rs.getString(2));
                    String c = (rs.getString(3));
                    String d = (rs.getString(4));
                    String e = (rs.getString(5));
                    String f = (rs.getString(6));
                    dtm.addRow(new Object[]{a, b, c, d, e, f});
                }

            } catch (Exception e) {
            }
            break;
            case 1:

                try {
                DefaultTableModel dtm = (DefaultTableModel) tblSupplier.getModel();
                ResultSet rs = DB.DB.getData("SELECT * FROM supplier WHERE sup_stat = '1' AND sup_username like '" + txtSearchSupplier.getText() + "%' order by sup_username ");
                dtm.setRowCount(0);

                while (rs.next()) {
                    String a = (rs.getString(1));
                    String b = (rs.getString(2));
                    String c = (rs.getString(3));
                    String d = (rs.getString(4));
                    String e = (rs.getString(5));
                    String f = (rs.getString(6));
                    dtm.addRow(new Object[]{a, b, c, d, e, f});
                }

            } catch (Exception e) {
            }
            break;
            case 2:
                try {
                DefaultTableModel dtm = (DefaultTableModel) tblSupplier.getModel();
                ResultSet rs = DB.DB.getData("SELECT * FROM supplier WHERE sup_stat = '1' AND sup_address like '" + txtSearchSupplier.getText() + "%' order by sup_address ");
                dtm.setRowCount(0);

                while (rs.next()) {
                    String a = (rs.getString(1));
                    String b = (rs.getString(2));
                    String c = (rs.getString(3));
                    String d = (rs.getString(4));
                    String e = (rs.getString(5));
                    String f = (rs.getString(6));
                    dtm.addRow(new Object[]{a, b, c, d, e, f});
                }

            } catch (Exception e) {
            }
            break;
            case 3:
                try {
                DefaultTableModel dtm = (DefaultTableModel) tblSupplier.getModel();
                ResultSet rs = DB.DB.getData("SELECT * FROM supplier WHERE sup_stat = '1' AND sup_email like '" + txtSearchSupplier.getText() + "%' order by sup_email");
                dtm.setRowCount(0);

                while (rs.next()) {
                    String a = (rs.getString(1));
                    String b = (rs.getString(2));
                    String c = (rs.getString(3));
                    String d = (rs.getString(4));
                    String e = (rs.getString(5));
                    String f = (rs.getString(6));
                    dtm.addRow(new Object[]{a, b, c, d, e, f});
                }
            } catch (Exception e) {
            }
            break;
            case 4:
                try {
                DefaultTableModel dtm = (DefaultTableModel) tblSupplier.getModel();
                ResultSet rs = DB.DB.getData("SELECT * FROM supplier WHERE sup_stat = '1' AND sup_phone like '" + txtSearchSupplier.getText() + "%' order by sup_phone ");
                dtm.setRowCount(0);

                while (rs.next()) {
                    String a = (rs.getString(1));
                    String b = (rs.getString(2));
                    String c = (rs.getString(3));
                    String d = (rs.getString(4));
                    String e = (rs.getString(5));
                    String f = (rs.getString(6));
                    dtm.addRow(new Object[]{a, b, c, d, e, f});
                }
            } catch (Exception e) {
            }
            break;
            case 5:
                try {
                DefaultTableModel dtm = (DefaultTableModel) tblSupplier.getModel();
                ResultSet rs = DB.DB.getData("SELECT * FROM supplier WHERE sup_stat = '1' AND sup_company like '" + txtSearchSupplier.getText() + "%' order by sup_company ");
                dtm.setRowCount(0);

                while (rs.next()) {
                    String a = (rs.getString(1));
                    String b = (rs.getString(2));
                    String c = (rs.getString(3));
                    String d = (rs.getString(4));
                    String e = (rs.getString(5));
                    String f = (rs.getString(6));
                    dtm.addRow(new Object[]{a, b, c, d, e, f});
                }
            } catch (Exception e) {
            }
            break;
            default:
                break;
        }
    }

    private void deleteSupplier() {
        int option = JOptionPane.showConfirmDialog(this, "Do you want to Delete this Supplier?", "", JOptionPane.YES_NO_OPTION);
        if (option == 0) {
            try {
                DB.DB.putData("UPDATE supplier SET sup_stat = '0' WHERE sup_id ='" + txtSelectedSupplierId.getText() + "' ");
                String deleteEmployeeActivityLog = "INSERT INTO log(usr_id, usr_type, usr_username, log_activity, log_description, log_state) VALUES ((SELECT usr_id FROM user WHERE usr_username = '" + lblAdminUsername.getText() + "'),'Admin','" + lblAdminUsername.getText() + "','Delete Supplier','Admin deleted supplier " + txtSupplierUsername.getText() + "','SUCCESS')";
                DB.DB.putData(deleteEmployeeActivityLog);
                ImageIcon icon = new ImageIcon(getClass().getResource("img/done.png"));
                JOptionPane.showMessageDialog(this, "Supplier Deleted!", "Done", JOptionPane.INFORMATION_MESSAGE, icon);

                txtSearchSupplier.setText(null);
                txtSelectedSupplierId.setText(null);
                txtSelectedSupplierUsername.setText(null);
                txtSelectedSupplierAddress.setText(null);
                txtSelectedSupplierEmail.setText(null);
                txtSelectedSupplierPhoneNo.setText(null);
                txtSelectedSupplierCompany.setText(null);
                DefaultTableModel dtm = (DefaultTableModel) tblSupplier.getModel();
                dtm.setRowCount(0);
                refreshSupplierTable();

            } catch (Exception e) {
            }
        } else {

        }
    }

    private void updateSupplier() {
        int option = JOptionPane.showConfirmDialog(this, "Do you want to Update this Supplier?", "", JOptionPane.YES_NO_OPTION);
        if (option == 0) {

            if (!txtSelectedSupplierUsername.getText().equals("")
                    & !txtSelectedSupplierAddress.getText().equals("")
                    & !txtSelectedSupplierEmail.getText().equals("")
                    & !txtSelectedSupplierCompany.getText().equals("")
                    & !txtSelectedSupplierPhoneNo.getText().equals("")) {

                if (!(txtSelectedSupplierPhoneNo.getText().length() == 10)) {
                    JOptionPane.showMessageDialog(this, "Invalid Phone Number!", "Invalid Data", JOptionPane.WARNING_MESSAGE);
                    txtSelectedSupplierPhoneNo.setText(null);
                    txtSelectedSupplierPhoneNo.grabFocus();

                } else if (!Pattern.matches("^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$", txtSelectedSupplierEmail.getText())) {
                    JOptionPane.showMessageDialog(this, "Invalid E-mail!", "Invalid Data", JOptionPane.WARNING_MESSAGE);
                    txtSelectedSupplierEmail.setText(null);
                    txtSelectedSupplierEmail.grabFocus();

                } else {

                    try {
                        DB.DB.putData("UPDATE supplier SET sup_address ='" + txtSelectedSupplierAddress.getText() + "', sup_email ='" + txtSelectedSupplierEmail.getText() + "', sup_phone= '" + txtSelectedSupplierPhoneNo.getText() + "', sup_company ='" + txtSelectedSupplierCompany.getText() + "' where sup_id ='" + txtSelectedSupplierId.getText() + "' ");
                        String updateSupplierActivityLog = "INSERT INTO log(usr_id, usr_type, usr_username, log_activity, log_description, log_state) VALUES ((SELECT usr_id FROM user WHERE usr_username = '" + lblAdminUsername.getText() + "'),'Admin','" + lblAdminUsername.getText() + "','Update Supplier','Admin updated supplier " + txtSupplierUsername.getText() + "','SUCCESS')";

                        DB.DB.putData(updateSupplierActivityLog);
                        ImageIcon icon = new ImageIcon(getClass().getResource("img/done.png"));
                        JOptionPane.showMessageDialog(this, "Supplier Updated Successfully!", "Done", JOptionPane.INFORMATION_MESSAGE, icon);

                        txtSelectedSupplierId.setText("");
                        txtSelectedSupplierUsername.setText("");
                        txtSelectedSupplierAddress.setText("");
                        txtSelectedSupplierEmail.setText("");
                        txtSelectedSupplierPhoneNo.setText("");
                        txtSelectedSupplierCompany.setText("");

                        DefaultTableModel dtm = (DefaultTableModel) tblSupplier.getModel();
                        dtm.setRowCount(0);
                        refreshSupplierTable();
                    } catch (Exception e) {
                    }
                }
            } else {
                JOptionPane.showMessageDialog(this, "Please Fill All the Fields!", "Invalid Data", JOptionPane.WARNING_MESSAGE);
            }
        }
    }

   
    public void printAllSupplier() {

        new Thread() {
            @Override
            public void run() {
                try {

                    String reportSource = "C:\\Program Files\\Exfiltro Reports\\viewSupplierReport.jrxml";

                    JasperDesign jdesign = JRXmlLoader.load(reportSource);
                    JasperReport jreport = JasperCompileManager.compileReport(jdesign);
                    JasperPrint jprint = JasperFillManager.fillReport(jreport, null, DB.DB.getConnection());
                    JasperViewer.viewReport(jprint, false);

                } catch (JRException ex) {

                }
            }
        }.start();
    }

    public void printSelectedSupplier() {
        HashMap a = new HashMap();
        a.put("id", txtSelectedSupplierId.getText());

        new Thread() {
            @Override
            public void run() {
                try {

                    String reportSource = "C:\\Program Files\\Exfiltro Reports\\viewSelectedSupplierReport.jrxml";

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
        jTabbedPaneSupplier = new javax.swing.JTabbedPane();
        jPanel2 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        txtSupplierId = new javax.swing.JTextField();
        txtSupplierUsername = new javax.swing.JTextField();
        txtSupplierPhoneNo = new javax.swing.JTextField();
        txtSupplierCompany = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jLabel22 = new javax.swing.JLabel();
        txtSupplierAddress = new javax.swing.JTextField();
        txtSupplierEmail = new javax.swing.JTextField();
        jPanel3 = new javax.swing.JPanel();
        txtSearchSupplier = new javax.swing.JTextField();
        jLabel11 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblSupplier = new javax.swing.JTable();
        jButton4 = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();
        jButton6 = new javax.swing.JButton();
        jButton7 = new javax.swing.JButton();
        lblEmployeeCount = new javax.swing.JLabel();
        cmbSortSupplier = new javax.swing.JComboBox<>();
        jPanel4 = new javax.swing.JPanel();
        jLabel13 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        txtSelectedSupplierPhoneNo = new javax.swing.JTextField();
        txtSelectedSupplierEmail = new javax.swing.JTextField();
        txtSelectedSupplierAddress = new javax.swing.JTextField();
        txtSelectedSupplierUsername = new javax.swing.JTextField();
        txtSelectedSupplierId = new javax.swing.JTextField();
        jButton8 = new javax.swing.JButton();
        jButton9 = new javax.swing.JButton();
        jButton10 = new javax.swing.JButton();
        jLabel20 = new javax.swing.JLabel();
        txtSelectedSupplierCompany = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Exfiltro Mart");

        jPanel1.setBackground(new java.awt.Color(51, 51, 51));

        pnlHomeSubSelection.setBackground(new java.awt.Color(70, 70, 70));

        lblDate.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        lblDate.setForeground(java.awt.Color.white);
        lblDate.setText("Today is Monday, January 1, 2020");

        lblTime.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        lblTime.setForeground(java.awt.Color.white);
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
        jLabel2.setText("Supplier");
        jLabel2.setToolTipText("");

        jLabel3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/frames/img/icons8_supplier_50px.png"))); // NOI18N

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

        jTabbedPaneSupplier.setBackground(new java.awt.Color(34, 34, 34));
        jTabbedPaneSupplier.setForeground(java.awt.Color.white);
        jTabbedPaneSupplier.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jTabbedPaneSupplier.setOpaque(true);

        jPanel2.setBackground(new java.awt.Color(34, 34, 34));
        jPanel2.setForeground(new java.awt.Color(255, 255, 255));

        jLabel1.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("Id");

        jLabel6.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(255, 255, 255));
        jLabel6.setText("Username");

        jLabel7.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(255, 255, 255));
        jLabel7.setText("Address");

        jLabel8.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(255, 255, 255));
        jLabel8.setText("Phone No");

        jLabel9.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(255, 255, 255));
        jLabel9.setText("Company");

        txtSupplierId.setBackground(new java.awt.Color(51, 51, 51));
        txtSupplierId.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        txtSupplierId.setText("1");
        txtSupplierId.setEnabled(false);

        txtSupplierUsername.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        txtSupplierUsername.setForeground(new java.awt.Color(255, 255, 255));
        txtSupplierUsername.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtSupplierUsernameKeyPressed(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtSupplierUsernameKeyTyped(evt);
            }
        });

        txtSupplierPhoneNo.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        txtSupplierPhoneNo.setForeground(new java.awt.Color(255, 255, 255));
        txtSupplierPhoneNo.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtSupplierPhoneNoKeyPressed(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtSupplierPhoneNoKeyTyped(evt);
            }
        });

        txtSupplierCompany.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        txtSupplierCompany.setForeground(new java.awt.Color(255, 255, 255));
        txtSupplierCompany.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtSupplierCompanyKeyPressed(evt);
            }
        });

        jButton1.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jButton1.setForeground(new java.awt.Color(255, 255, 255));
        jButton1.setText(" Add New Suplier");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jButton2.setForeground(new java.awt.Color(255, 255, 255));
        jButton2.setText(" View Supplier");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
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

        jLabel22.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel22.setForeground(new java.awt.Color(255, 255, 255));
        jLabel22.setText("E-mail");

        txtSupplierAddress.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        txtSupplierAddress.setForeground(new java.awt.Color(255, 255, 255));
        txtSupplierAddress.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtSupplierAddressKeyPressed(evt);
            }
        });

        txtSupplierEmail.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        txtSupplierEmail.setForeground(new java.awt.Color(255, 255, 255));
        txtSupplierEmail.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtSupplierEmailKeyPressed(evt);
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
                    .addComponent(jLabel7)
                    .addComponent(jLabel22))
                .addGap(51, 51, 51)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(txtSupplierCompany, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 278, Short.MAX_VALUE)
                    .addComponent(txtSupplierPhoneNo, javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtSupplierId, javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtSupplierUsername)
                    .addComponent(txtSupplierAddress)
                    .addComponent(txtSupplierEmail))
                .addGap(78, 78, 78)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButton2)
                    .addComponent(jButton1)
                    .addComponent(jButton3))
                .addContainerGap(519, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(45, 45, 45)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(txtSupplierId, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton1))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(txtSupplierUsername, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton2))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel7)
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jButton3)
                        .addComponent(txtSupplierAddress, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel22)
                    .addComponent(txtSupplierEmail, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtSupplierPhoneNo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel8))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtSupplierCompany, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel9))
                .addContainerGap(252, Short.MAX_VALUE))
        );

        jTabbedPaneSupplier.addTab("New Supplier", jPanel2);

        jPanel3.setBackground(new java.awt.Color(34, 34, 34));

        txtSearchSupplier.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        txtSearchSupplier.setForeground(new java.awt.Color(255, 255, 255));
        txtSearchSupplier.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtSearchSupplierKeyReleased(evt);
            }
        });

        jLabel11.setIcon(new javax.swing.ImageIcon(getClass().getResource("/frames/img/search.icon.png"))); // NOI18N

        tblSupplier.setBackground(new java.awt.Color(34, 34, 34));
        tblSupplier.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        tblSupplier.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Supplier Id", "Username", "Address", "E-mail", "Phone No.", "Company"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblSupplier.setGridColor(new java.awt.Color(61, 61, 61));
        tblSupplier.setShowGrid(true);
        tblSupplier.getTableHeader().setReorderingAllowed(false);
        tblSupplier.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblSupplierMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tblSupplier);

        jButton4.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jButton4.setForeground(new java.awt.Color(255, 255, 255));
        jButton4.setText(" Edit");
        jButton4.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        jButton5.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jButton5.setForeground(new java.awt.Color(255, 255, 255));
        jButton5.setText(" Delete");
        jButton5.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });

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

        lblEmployeeCount.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        lblEmployeeCount.setText("Supplier Count : 0");

        cmbSortSupplier.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        cmbSortSupplier.setForeground(new java.awt.Color(255, 255, 255));
        cmbSortSupplier.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Id", "Name", "Address", "E-mail", "Phone No.", "Company" }));
        cmbSortSupplier.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbSortSupplierActionPerformed(evt);
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
                                .addComponent(txtSearchSupplier, javax.swing.GroupLayout.DEFAULT_SIZE, 873, Short.MAX_VALUE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(cmbSortSupplier, 0, 173, Short.MAX_VALUE)
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
                    .addComponent(txtSearchSupplier, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel11)
                    .addComponent(cmbSortSupplier, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
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

        jTabbedPaneSupplier.addTab("View Supplier", jPanel3);

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
        jLabel16.setText("Address");

        jLabel17.setBackground(new java.awt.Color(0, 0, 0));
        jLabel17.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel17.setForeground(new java.awt.Color(255, 255, 255));
        jLabel17.setText("E-mail");

        jLabel18.setBackground(new java.awt.Color(0, 0, 0));
        jLabel18.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel18.setForeground(new java.awt.Color(255, 255, 255));
        jLabel18.setText("Phone");

        txtSelectedSupplierPhoneNo.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        txtSelectedSupplierPhoneNo.setForeground(new java.awt.Color(255, 255, 255));
        txtSelectedSupplierPhoneNo.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtSelectedSupplierPhoneNoKeyPressed(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtSelectedSupplierPhoneNoKeyTyped(evt);
            }
        });

        txtSelectedSupplierEmail.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        txtSelectedSupplierEmail.setForeground(new java.awt.Color(255, 255, 255));
        txtSelectedSupplierEmail.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtSelectedSupplierEmailKeyPressed(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtSelectedSupplierEmailKeyTyped(evt);
            }
        });

        txtSelectedSupplierAddress.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        txtSelectedSupplierAddress.setForeground(new java.awt.Color(255, 255, 255));
        txtSelectedSupplierAddress.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtSelectedSupplierAddressKeyPressed(evt);
            }
        });

        txtSelectedSupplierUsername.setBackground(new java.awt.Color(51, 51, 51));
        txtSelectedSupplierUsername.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        txtSelectedSupplierUsername.setForeground(new java.awt.Color(255, 255, 255));
        txtSelectedSupplierUsername.setEnabled(false);

        txtSelectedSupplierId.setBackground(new java.awt.Color(51, 51, 51));
        txtSelectedSupplierId.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        txtSelectedSupplierId.setForeground(new java.awt.Color(255, 255, 255));
        txtSelectedSupplierId.setEnabled(false);

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

        jLabel20.setBackground(new java.awt.Color(0, 0, 0));
        jLabel20.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel20.setForeground(new java.awt.Color(255, 255, 255));
        jLabel20.setText("Company");

        txtSelectedSupplierCompany.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        txtSelectedSupplierCompany.setForeground(new java.awt.Color(255, 255, 255));
        txtSelectedSupplierCompany.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtSelectedSupplierCompanyKeyPressed(evt);
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
                    .addComponent(jLabel18)
                    .addComponent(jLabel20))
                .addGap(51, 51, 51)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(txtSelectedSupplierPhoneNo, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 278, Short.MAX_VALUE)
                    .addComponent(txtSelectedSupplierEmail, javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtSelectedSupplierAddress, javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtSelectedSupplierId, javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtSelectedSupplierUsername, javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtSelectedSupplierCompany, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 278, Short.MAX_VALUE))
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
                    .addComponent(txtSelectedSupplierId, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton8))
                .addGap(18, 18, 18)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel14)
                    .addComponent(txtSelectedSupplierUsername, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton9))
                .addGap(18, 18, 18)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButton10)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtSelectedSupplierAddress, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel16))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtSelectedSupplierEmail, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel17))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtSelectedSupplierPhoneNo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel18))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtSelectedSupplierCompany, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel20))))
                .addContainerGap(252, Short.MAX_VALUE))
        );

        jTabbedPaneSupplier.addTab("Edit Supplier", jPanel4);

        javax.swing.GroupLayout jPanel11Layout = new javax.swing.GroupLayout(jPanel11);
        jPanel11.setLayout(jPanel11Layout);
        jPanel11Layout.setHorizontalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPaneSupplier)
        );
        jPanel11Layout.setVerticalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPaneSupplier)
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
        addNewSupplier();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        jTabbedPaneSupplier.setSelectedIndex(1);
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        txtSupplierUsername.setText(null);
        txtSupplierAddress.setText(null);
        txtSupplierEmail.setText(null);
        txtSupplierPhoneNo.setText(null);
        txtSupplierCompany.setText(null);
    }//GEN-LAST:event_jButton3ActionPerformed

    private void txtSupplierUsernameKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtSupplierUsernameKeyPressed
        if (evt.getKeyCode() == 10) {
            txtSupplierAddress.grabFocus();
        }
    }//GEN-LAST:event_txtSupplierUsernameKeyPressed

    private void txtSupplierPhoneNoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtSupplierPhoneNoKeyPressed
        if (evt.getKeyCode() == 10) {
            txtSupplierCompany.grabFocus();
        }
    }//GEN-LAST:event_txtSupplierPhoneNoKeyPressed

    private void txtSupplierCompanyKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtSupplierCompanyKeyPressed
        if (evt.getKeyCode() == 10) {
            addNewSupplier();
        }
    }//GEN-LAST:event_txtSupplierCompanyKeyPressed

    private void txtSearchSupplierKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtSearchSupplierKeyReleased
        searchSupplier();
    }//GEN-LAST:event_txtSearchSupplierKeyReleased

    private void cmbSortSupplierActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbSortSupplierActionPerformed
        searchSupplier();
    }//GEN-LAST:event_cmbSortSupplierActionPerformed

    private void jButton7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton7ActionPerformed
        refreshSupplierTable();
        txtSearchSupplier.setText(null);
        txtSelectedSupplierId.setText(null);
        txtSelectedSupplierUsername.setText(null);
        txtSelectedSupplierAddress.setText(null);
        txtSelectedSupplierEmail.setText(null);
        txtSelectedSupplierPhoneNo.setText(null);
    }//GEN-LAST:event_jButton7ActionPerformed

    private void tblSupplierMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblSupplierMouseClicked
        txtSelectedSupplierId.setText((String) tblSupplier.getValueAt(tblSupplier.getSelectedRow(), 0));
        txtSelectedSupplierUsername.setText((String) tblSupplier.getValueAt(tblSupplier.getSelectedRow(), 1));
        txtSelectedSupplierAddress.setText((String) tblSupplier.getValueAt(tblSupplier.getSelectedRow(), 2));
        txtSelectedSupplierEmail.setText((String) tblSupplier.getValueAt(tblSupplier.getSelectedRow(), 3));
        txtSelectedSupplierPhoneNo.setText((String) tblSupplier.getValueAt(tblSupplier.getSelectedRow(), 4));
        txtSelectedSupplierCompany.setText((String) tblSupplier.getValueAt(tblSupplier.getSelectedRow(), 5));

    }//GEN-LAST:event_tblSupplierMouseClicked

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        if (tblSupplier.getSelectedRowCount() == 0) {
            JOptionPane.showMessageDialog(this, "No Supplier Selected!");
        } else {
            jTabbedPaneSupplier.setSelectedIndex(2);
            txtSelectedSupplierAddress.grabFocus();
        }
    }//GEN-LAST:event_jButton4ActionPerformed

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        if (tblSupplier.getSelectedRowCount() == 0) {
            JOptionPane.showMessageDialog(this, "No Supplier Selected!");

        } else {
            deleteSupplier();
        }
    }//GEN-LAST:event_jButton5ActionPerformed

    private void jButton8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton8ActionPerformed
        if (tblSupplier.getSelectedRowCount() == 0) {
            JOptionPane.showMessageDialog(this, "No Supplier Selected!");
            jTabbedPaneSupplier.setSelectedIndex(0);

        } else {
            updateSupplier();

            jPanel4.setVisible(false);
            jPanel4.setVisible(true);
        }
    }//GEN-LAST:event_jButton8ActionPerformed

    private void jButton9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton9ActionPerformed
        if (tblSupplier.getSelectedRowCount() == 0) {
            JOptionPane.showMessageDialog(this, "No Supplier Selected!");
            jTabbedPaneSupplier.setSelectedIndex(0);

        } else {

            deleteSupplier();
            jPanel4.setVisible(false);
            jPanel4.setVisible(true);
        }
    }//GEN-LAST:event_jButton9ActionPerformed

    private void txtSelectedSupplierAddressKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtSelectedSupplierAddressKeyPressed
        if (evt.getKeyCode() == 10) {
            txtSelectedSupplierEmail.grabFocus();
        }
    }//GEN-LAST:event_txtSelectedSupplierAddressKeyPressed

    private void txtSelectedSupplierEmailKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtSelectedSupplierEmailKeyPressed
        if (evt.getKeyCode() == 10) {
            txtSelectedSupplierPhoneNo.grabFocus();
        }
    }//GEN-LAST:event_txtSelectedSupplierEmailKeyPressed

    private void txtSelectedSupplierEmailKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtSelectedSupplierEmailKeyTyped
        if (!(Character.isDigit(evt.getKeyChar()))) {
            evt.consume();
        }
        String nic = txtSelectedSupplierEmail.getText();
        int length = nic.length();
        if (length >= 12) {
            evt.consume();
        }
    }//GEN-LAST:event_txtSelectedSupplierEmailKeyTyped

    private void txtSelectedSupplierPhoneNoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtSelectedSupplierPhoneNoKeyPressed
        if (evt.getKeyCode() == 10) {
            updateSupplier();
        }
    }//GEN-LAST:event_txtSelectedSupplierPhoneNoKeyPressed

    private void txtSelectedSupplierPhoneNoKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtSelectedSupplierPhoneNoKeyTyped
        char c = evt.getKeyChar();
        if (!(Character.isDigit(c))) {
            evt.consume();

        }
        String phoneNo = txtSelectedSupplierPhoneNo.getText();
        int length = phoneNo.length();
        if (length >= 10) {
            evt.consume();
        }
    }//GEN-LAST:event_txtSelectedSupplierPhoneNoKeyTyped

    private void txtSelectedSupplierCompanyKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtSelectedSupplierCompanyKeyPressed
        if (tblSupplier.getSelectedRowCount() == 0) {
            JOptionPane.showMessageDialog(this, "No Supplier Selected!");
            jTabbedPaneSupplier.setSelectedIndex(0);

        } else {
            updateSupplier();

        }
    }//GEN-LAST:event_txtSelectedSupplierCompanyKeyPressed

    private void txtSupplierAddressKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtSupplierAddressKeyPressed
        if (evt.getKeyCode() == 10) {
            txtSupplierEmail.grabFocus();
        }
    }//GEN-LAST:event_txtSupplierAddressKeyPressed

    private void txtSupplierEmailKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtSupplierEmailKeyPressed
        if (evt.getKeyCode() == 10) {
            txtSupplierPhoneNo.grabFocus();
        }
    }//GEN-LAST:event_txtSupplierEmailKeyPressed

    private void txtSupplierUsernameKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtSupplierUsernameKeyTyped
        char c = evt.getKeyChar();
        if (!((Character.isDigit(c)) || (Character.isLetter(c)) || (Character.isISOControl(c)))) {
            evt.consume();
        }
    }//GEN-LAST:event_txtSupplierUsernameKeyTyped

    private void txtSupplierPhoneNoKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtSupplierPhoneNoKeyTyped
        char c = evt.getKeyChar();
        if (!(Character.isDigit(c))) {
            evt.consume();

        }
        String phoneNo = txtSupplierPhoneNo.getText();
        int length = phoneNo.length();
        if (length >= 10) {
            evt.consume();
        }
    }//GEN-LAST:event_txtSupplierPhoneNoKeyTyped

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

            AdminSupplier.this.dispose();

            Login login = new Login();
            login.setVisible(true);
            for (float j = 0f; j < 0.985; j += 0.000001) {
                login.setOpacity(j);
            }
        } else {

        }
    }//GEN-LAST:event_jLabel4MouseClicked

    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed
        printAllSupplier();
    }//GEN-LAST:event_jButton6ActionPerformed

    private void jButton10ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton10ActionPerformed
        printSelectedSupplier();
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
                new AdminSupplier().setVisible(true);
            }
        });
    }

    private void setIcon() {
        setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("img/exfiltrat0z-icon.png")));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox<String> cmbSortSupplier;
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
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
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
    private javax.swing.JTabbedPane jTabbedPaneSupplier;
    public static final javax.swing.JLabel lblAdminUsername = new javax.swing.JLabel();
    private javax.swing.JLabel lblDate;
    private javax.swing.JLabel lblEmployeeCount;
    private javax.swing.JLabel lblTime;
    private javax.swing.JPanel pnlHomeSubSelection;
    private javax.swing.JPanel pnlLogout;
    private javax.swing.JTable tblSupplier;
    private javax.swing.JTextField txtSearchSupplier;
    private javax.swing.JTextField txtSelectedSupplierAddress;
    private javax.swing.JTextField txtSelectedSupplierCompany;
    private javax.swing.JTextField txtSelectedSupplierEmail;
    private javax.swing.JTextField txtSelectedSupplierId;
    private javax.swing.JTextField txtSelectedSupplierPhoneNo;
    private javax.swing.JTextField txtSelectedSupplierUsername;
    private javax.swing.JTextField txtSupplierAddress;
    private javax.swing.JTextField txtSupplierCompany;
    private javax.swing.JTextField txtSupplierEmail;
    private javax.swing.JTextField txtSupplierId;
    private javax.swing.JTextField txtSupplierPhoneNo;
    private javax.swing.JTextField txtSupplierUsername;
    // End of variables declaration//GEN-END:variables
}
