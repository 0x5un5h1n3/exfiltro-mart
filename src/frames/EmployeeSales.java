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
public class EmployeeSales extends javax.swing.JFrame {

    Timer timer;
    String month;
    String year;
    String date;
    String time;
    String today;

    public EmployeeSales() {
        initComponents();
        EmployeeSales.this.setExtendedState(JFrame.MAXIMIZED_BOTH);
        EmployeeSales.this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setIcon();
        setTableheader();
        refreshSalesTable();
        refreshSalesHistoryTable();
        refreshInvoiceHistoryTable();

        EmployeeSales.this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {

                EmployeeSales.this.dispose();
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
        JTableHeader todaySales = tblTodaySales.getTableHeader();
        todaySales.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        todaySales.setForeground(Color.white);
        todaySales.setBackground(new Color(51, 51, 51));
        tblTodaySales.getTableHeader().setReorderingAllowed(false);

        JTableHeader salesHistory = tblSalesHistory.getTableHeader();
        salesHistory.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        salesHistory.setForeground(Color.white);
        salesHistory.setBackground(new Color(51, 51, 51));
        tblSalesHistory.getTableHeader().setReorderingAllowed(false);

        JTableHeader invoiceHistory = tblInvoiceHistory.getTableHeader();
        invoiceHistory.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        invoiceHistory.setForeground(Color.white);
        invoiceHistory.setBackground(new Color(51, 51, 51));
        tblInvoiceHistory.getTableHeader().setReorderingAllowed(false);
    }

    private void refreshSalesTable() {
        try {

            Date DateDate = new Date();
            SimpleDateFormat toDate = new SimpleDateFormat("yyyy-MM-dd");
            date = "" + toDate.format(DateDate);

            Date DateMonth = new Date();
            SimpleDateFormat toMonth = new SimpleDateFormat("MMMM");
            month = "" + toMonth.format(DateMonth);

            Date DateYear = new Date();
            SimpleDateFormat toYear = new SimpleDateFormat("yyyy");
            year = "" + toYear.format(DateYear);

            DefaultTableModel dtm = (DefaultTableModel) tblTodaySales.getModel();
            ResultSet rs = DB.DB.getData("SELECT * FROM sales WHERE sal_date = '" + date + "' AND gin_employee = '" + lblEmployeeUsername.getText() + "' order by sal_id desc ");
            dtm.setRowCount(0);

            while (rs.next()) {
                String a = (rs.getString(2));
                String b = (rs.getString(5));
                String c = (rs.getString(9));
                String d = (rs.getString(10));
                String e = (rs.getString(12));
                String f = (rs.getString(14));
                dtm.addRow(new Object[]{a, b, c, d, e, f});

            }

            cmbSortTodaySales.setSelectedIndex(0);
            txtSearchTodaySales.setText(null);

            ResultSet todaySalesCount = DB.DB.getData("SELECT count(sal_id) FROM sales WHERE gin_employee = '" + lblEmployeeUsername.getText() + "' AND sal_date  = '" + date + "' ");

            if (todaySalesCount.next()) {
                int d = todaySalesCount.getInt(1);
                lblTodaySalesCount.setText("Sales Count : " + d);
            }
        } catch (Exception e) {
        }

    }

    private void searchTodaySalesTable() {
        switch (cmbSortTodaySales.getSelectedIndex()) {

            case 0:

                Date DateDate = new Date();
                SimpleDateFormat toDate = new SimpleDateFormat("yyyy-MM-dd");
                date = "" + toDate.format(DateDate);

                try {
                    DefaultTableModel dtm = (DefaultTableModel) tblTodaySales.getModel();
                    ResultSet rs = DB.DB.getData("SELECT * FROM sales WHERE gin_employee = '" + lblEmployeeUsername.getText() + "' AND  sal_date = '" + date + "' AND invoice_id like '" + txtSearchTodaySales.getText() + "%' order by sal_id desc");
                    dtm.setRowCount(0);

                    while (rs.next()) {
                        String a = (rs.getString(2));
                        String b = (rs.getString(5));
                        String c = (rs.getString(9));
                        String d = (rs.getString(10));
                        String e = (rs.getString(12));
                        String f = (rs.getString(14));
                        dtm.addRow(new Object[]{a, b, c, d, e, f});
                    }

                } catch (Exception e) {
                }
                break;
            case 1:

                

                try {
                DefaultTableModel dtm = (DefaultTableModel) tblTodaySales.getModel();
                ResultSet rs = DB.DB.getData("SELECT * FROM sales WHERE gin_employee = '" + lblEmployeeUsername.getText() + "' AND sal_date = '" + date + "' AND itm_name like '" + txtSearchTodaySales.getText() + "%' order by itm_name desc");
                dtm.setRowCount(0);

                while (rs.next()) {
                    String a = (rs.getString(2));
                    String b = (rs.getString(5));
                    String c = (rs.getString(9));
                    String d = (rs.getString(10));
                    String e = (rs.getString(12));
                    String f = (rs.getString(14));
                    dtm.addRow(new Object[]{a, b, c, d, e, f});
                }
            } catch (Exception e) {
            }
            break;
            default:
                break;
        }
    }

    private void refreshSalesHistoryTable() {
        try {

            DefaultTableModel dtm = (DefaultTableModel) tblSalesHistory.getModel();
            ResultSet rs = DB.DB.getData("SELECT * FROM sales WHERE gin_employee = '" + lblEmployeeUsername.getText() + "' order by sal_id desc ");
            dtm.setRowCount(0);

            while (rs.next()) {
                String a = (rs.getString(2));
                String b = (rs.getString(5));
                String c = (rs.getString(9));
                String d = (rs.getString(10));
                String e = (rs.getString(12));
                String f = (rs.getString(14));
                String g = (rs.getString(19));
                dtm.addRow(new Object[]{a, b, c, d, e, f, g});

            }
            cmbSortSalesHistory.setSelectedIndex(0);
            txtSearchSalesHistory.setText(null);

        } catch (Exception e) {
        }

    }

    private void searchSalesHistoryTable() {
        switch (cmbSortSalesHistory.getSelectedIndex()) {

            case 0:


                try {
                DefaultTableModel dtm = (DefaultTableModel) tblSalesHistory.getModel();
                ResultSet rs = DB.DB.getData("SELECT * FROM sales WHERE gin_employee = '" + lblEmployeeUsername.getText() + "' AND invoice_id like '" + txtSearchSalesHistory.getText() + "%' order by sal_id desc");
                dtm.setRowCount(0);

                while (rs.next()) {
                    String a = (rs.getString(2));
                    String b = (rs.getString(5));
                    String c = (rs.getString(9));
                    String d = (rs.getString(10));
                    String e = (rs.getString(12));
                    String f = (rs.getString(14));
                    String g = (rs.getString(19));
                    dtm.addRow(new Object[]{a, b, c, d, e, f, g});
                }

            } catch (Exception e) {
            }
            break;
            case 1:

                

                try {
                DefaultTableModel dtm = (DefaultTableModel) tblSalesHistory.getModel();
                ResultSet rs = DB.DB.getData("SELECT * FROM sales WHERE gin_employee = '" + lblEmployeeUsername.getText() + "' AND itm_name like '" + txtSearchSalesHistory.getText() + "%' order by itm_name desc");
                dtm.setRowCount(0);

                while (rs.next()) {
                    String a = (rs.getString(2));
                    String b = (rs.getString(5));
                    String c = (rs.getString(9));
                    String d = (rs.getString(10));
                    String e = (rs.getString(12));
                    String f = (rs.getString(14));
                    String g = (rs.getString(19));
                    dtm.addRow(new Object[]{a, b, c, d, e, f, g});
                }
            } catch (Exception e) {
            }
            break;
            case 2:
                
                try {
                DefaultTableModel dtm = (DefaultTableModel) tblSalesHistory.getModel();
                ResultSet rs = DB.DB.getData("SELECT * FROM sales WHERE gin_employee = '" + lblEmployeeUsername.getText() + "' AND sal_date like '" + txtSearchSalesHistory.getText() + "%' order by sal_date desc");
                dtm.setRowCount(0);

                while (rs.next()) {
                    String a = (rs.getString(2));
                    String b = (rs.getString(5));
                    String c = (rs.getString(9));
                    String d = (rs.getString(10));
                    String e = (rs.getString(12));
                    String f = (rs.getString(14));
                    String g = (rs.getString(19));
                    dtm.addRow(new Object[]{a, b, c, d, e, f, g});
                }
            } catch (Exception e) {
            }
            break;
            case 3:
                
                try {
                DefaultTableModel dtm = (DefaultTableModel) tblSalesHistory.getModel();
                ResultSet rs = DB.DB.getData("SELECT * FROM sales WHERE gin_employee = '" + lblEmployeeUsername.getText() + "' AND sal_month like '" + txtSearchSalesHistory.getText() + "%' order by sal_month desc");
                dtm.setRowCount(0);

                while (rs.next()) {
                    String a = (rs.getString(2));
                    String b = (rs.getString(5));
                    String c = (rs.getString(9));
                    String d = (rs.getString(10));
                    String e = (rs.getString(12));
                    String f = (rs.getString(14));
                    String g = (rs.getString(19));
                    dtm.addRow(new Object[]{a, b, c, d, e, f, g});
                }
            } catch (Exception e) {
            }
            break;
            case 4:

                try {
                DefaultTableModel dtm = (DefaultTableModel) tblSalesHistory.getModel();
                ResultSet rs = DB.DB.getData("SELECT * FROM sales WHERE gin_employee = '" + lblEmployeeUsername.getText() + "' AND sal_year like '" + txtSearchSalesHistory.getText() + "%' order by sal_year desc");
                dtm.setRowCount(0);

                while (rs.next()) {
                    String a = (rs.getString(2));
                    String b = (rs.getString(5));
                    String c = (rs.getString(9));
                    String d = (rs.getString(10));
                    String e = (rs.getString(12));
                    String f = (rs.getString(14));
                    String g = (rs.getString(19));
                    dtm.addRow(new Object[]{a, b, c, d, e, f, g});
                }
            } catch (Exception e) {
            }
            break;
            default:
                break;
        }
    }

    private void refreshInvoiceHistoryTable() {
        try {

            DefaultTableModel dtm = (DefaultTableModel) tblInvoiceHistory.getModel();
            ResultSet rs = DB.DB.getData("SELECT * FROM invoice order by invoice_id desc ");
            dtm.setRowCount(0);

            while (rs.next()) {
                String a = (rs.getString(1));
                String b = (rs.getString(2));
                String c = (rs.getString(3));
                String d = (rs.getString(4));
                String e = (rs.getString(5));
                String f = (rs.getString(6));
                String g = (rs.getString(7));
                String h = (rs.getString(8));
                String i = (rs.getString(9));
                dtm.addRow(new Object[]{a, b, c, d, e, f, g, h, i});

            }
            txtSearchInvoiceHistory.setText(null);

        } catch (Exception e) {
        }

    }

    private void searchInvoiceHistoryTable() {
        switch (cmbSortInvoiceHistory.getSelectedIndex()) {

            case 0:
                
                try {
                DefaultTableModel dtm = (DefaultTableModel) tblInvoiceHistory.getModel();
                ResultSet rs = DB.DB.getData("SELECT * FROM invoice WHERE invoice_id like '" + txtSearchInvoiceHistory.getText() + "%' order by invoice_id desc");
                dtm.setRowCount(0);

                while (rs.next()) {
                    String a = (rs.getString(1));
                    String b = (rs.getString(2));
                    String c = (rs.getString(3));
                    String d = (rs.getString(4));
                    String e = (rs.getString(5));
                    String f = (rs.getString(6));
                    String g = (rs.getString(7));
                    String h = (rs.getString(8));
                    String i = (rs.getString(9));
                    dtm.addRow(new Object[]{a, b, c, d, e, f, g, h, i});
                }

            } catch (Exception e) {
            }
            break;

            case 1:
                
                try {
                DefaultTableModel dtm = (DefaultTableModel) tblInvoiceHistory.getModel();
                ResultSet rs = DB.DB.getData("SELECT * FROM invoice WHERE inv_type = 'Cash' order by invoice_id desc");
                dtm.setRowCount(0);

                while (rs.next()) {
                    String a = (rs.getString(1));
                    String b = (rs.getString(2));
                    String c = (rs.getString(3));
                    String d = (rs.getString(4));
                    String e = (rs.getString(5));
                    String f = (rs.getString(6));
                    String g = (rs.getString(7));
                    String h = (rs.getString(8));
                    String i = (rs.getString(9));
                    dtm.addRow(new Object[]{a, b, c, d, e, f, g, h, i});
                }
            } catch (Exception e) {
            }
            break;

            case 2:
                
                try {
                DefaultTableModel dtm = (DefaultTableModel) tblInvoiceHistory.getModel();
                ResultSet rs = DB.DB.getData("SELECT * FROM invoice WHERE inv_type = 'VAT' order by invoice_id desc");
                dtm.setRowCount(0);

                while (rs.next()) {
                    String a = (rs.getString(1));
                    String b = (rs.getString(2));
                    String c = (rs.getString(3));
                    String d = (rs.getString(4));
                    String e = (rs.getString(5));
                    String f = (rs.getString(6));
                    String g = (rs.getString(7));
                    String h = (rs.getString(8));
                    String i = (rs.getString(9));
                    dtm.addRow(new Object[]{a, b, c, d, e, f, g, h, i});
                }
            } catch (Exception e) {
            }
            break;

            case 3:
                
                try {
                DefaultTableModel dtm = (DefaultTableModel) tblInvoiceHistory.getModel();
                ResultSet rs = DB.DB.getData("SELECT * FROM invoice WHERE gin_date like '" + txtSearchInvoiceHistory.getText() + "%' order by gin_date desc");
                dtm.setRowCount(0);

                while (rs.next()) {
                    String a = (rs.getString(1));
                    String b = (rs.getString(2));
                    String c = (rs.getString(3));
                    String d = (rs.getString(4));
                    String e = (rs.getString(5));
                    String f = (rs.getString(6));
                    String g = (rs.getString(7));
                    String h = (rs.getString(8));
                    String i = (rs.getString(9));
                    dtm.addRow(new Object[]{a, b, c, d, e, f, g, h, i});
                }
            } catch (Exception e) {
            }
            break;

            case 4:
                
                try {
                DefaultTableModel dtm = (DefaultTableModel) tblInvoiceHistory.getModel();
                ResultSet rs = DB.DB.getData("SELECT * FROM invoice WHERE gin_month like '" + txtSearchInvoiceHistory.getText() + "%' order by gin_month desc");
                dtm.setRowCount(0);

                while (rs.next()) {
                    String a = (rs.getString(1));
                    String b = (rs.getString(2));
                    String c = (rs.getString(3));
                    String d = (rs.getString(4));
                    String e = (rs.getString(5));
                    String f = (rs.getString(6));
                    String g = (rs.getString(7));
                    String h = (rs.getString(8));
                    String i = (rs.getString(9));
                    dtm.addRow(new Object[]{a, b, c, d, e, f, g, h, i});
                }
            } catch (Exception e) {
            }
            break;

            case 5:

                try {
                DefaultTableModel dtm = (DefaultTableModel) tblInvoiceHistory.getModel();
                ResultSet rs = DB.DB.getData("SELECT * FROM invoice WHERE gin_year like '" + txtSearchInvoiceHistory.getText() + "%' order by gin_year desc");
                dtm.setRowCount(0);

                while (rs.next()) {
                    String a = (rs.getString(1));
                    String b = (rs.getString(2));
                    String c = (rs.getString(3));
                    String d = (rs.getString(4));
                    String e = (rs.getString(5));
                    String f = (rs.getString(6));
                    String g = (rs.getString(7));
                    String h = (rs.getString(8));
                    String i = (rs.getString(9));
                    dtm.addRow(new Object[]{a, b, c, d, e, f, g, h, i});
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
        jTabbedPaneEmployee = new javax.swing.JTabbedPane();
        jPanel3 = new javax.swing.JPanel();
        txtSearchTodaySales = new javax.swing.JTextField();
        jLabel11 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblTodaySales = new javax.swing.JTable();
        cmbSortTodaySales = new javax.swing.JComboBox<>();
        lblTodaySalesCount = new javax.swing.JLabel();
        jButton9 = new javax.swing.JButton();
        jPanel5 = new javax.swing.JPanel();
        txtSearchSalesHistory = new javax.swing.JTextField();
        jLabel12 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tblSalesHistory = new javax.swing.JTable();
        jButton8 = new javax.swing.JButton();
        cmbSortSalesHistory = new javax.swing.JComboBox<>();
        jPanel6 = new javax.swing.JPanel();
        txtSearchInvoiceHistory = new javax.swing.JTextField();
        jLabel13 = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        tblInvoiceHistory = new javax.swing.JTable();
        jButton10 = new javax.swing.JButton();
        cmbSortInvoiceHistory = new javax.swing.JComboBox<>();

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
        jLabel2.setText("Sales");
        jLabel2.setToolTipText("");

        jLabel3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/frames/img/icons8_money_bag_50px.png"))); // NOI18N

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

        jPanel3.setBackground(new java.awt.Color(34, 34, 34));

        txtSearchTodaySales.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        txtSearchTodaySales.setForeground(new java.awt.Color(255, 255, 255));
        txtSearchTodaySales.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtSearchTodaySalesKeyReleased(evt);
            }
        });

        jLabel11.setIcon(new javax.swing.ImageIcon(getClass().getResource("/frames/img/search.icon.png"))); // NOI18N

        tblTodaySales.setBackground(new java.awt.Color(34, 34, 34));
        tblTodaySales.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        tblTodaySales.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Sales / Invoice Id", "Item Name", "Sold Price", "Amount", "Total", "Billed by"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblTodaySales.setGridColor(new java.awt.Color(61, 61, 61));
        tblTodaySales.setShowGrid(true);
        tblTodaySales.getTableHeader().setReorderingAllowed(false);
        jScrollPane1.setViewportView(tblTodaySales);

        cmbSortTodaySales.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        cmbSortTodaySales.setForeground(new java.awt.Color(255, 255, 255));
        cmbSortTodaySales.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Sales / Invoice Id", "Item Name" }));
        cmbSortTodaySales.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbSortTodaySalesActionPerformed(evt);
            }
        });

        lblTodaySalesCount.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        lblTodaySalesCount.setForeground(new java.awt.Color(153, 153, 153));
        lblTodaySalesCount.setText("Sales Count : 0");

        jButton9.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jButton9.setForeground(new java.awt.Color(255, 255, 255));
        jButton9.setText(" Refresh");
        jButton9.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jButton9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton9ActionPerformed(evt);
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
                        .addComponent(lblTodaySalesCount, javax.swing.GroupLayout.PREFERRED_SIZE, 240, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButton9))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 1096, Short.MAX_VALUE)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(txtSearchTodaySales, javax.swing.GroupLayout.DEFAULT_SIZE, 873, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(cmbSortTodaySales, 0, 173, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel11)))
                .addGap(52, 52, 52))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(23, 23, 23)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtSearchTodaySales, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel11)
                    .addComponent(cmbSortTodaySales, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 398, Short.MAX_VALUE)
                .addGap(17, 17, 17)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblTodaySalesCount)
                    .addComponent(jButton9))
                .addGap(39, 39, 39))
        );

        jTabbedPaneEmployee.addTab("Your Today Sales", jPanel3);

        jPanel5.setBackground(new java.awt.Color(34, 34, 34));

        txtSearchSalesHistory.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        txtSearchSalesHistory.setForeground(new java.awt.Color(255, 255, 255));
        txtSearchSalesHistory.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtSearchSalesHistoryKeyReleased(evt);
            }
        });

        jLabel12.setIcon(new javax.swing.ImageIcon(getClass().getResource("/frames/img/search.icon.png"))); // NOI18N

        tblSalesHistory.setBackground(new java.awt.Color(34, 34, 34));
        tblSalesHistory.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        tblSalesHistory.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Sales / Invoice Id", "Item Name", "Sold Price", "Amount", "Total", "Billed by", "Date / Time"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblSalesHistory.setGridColor(new java.awt.Color(61, 61, 61));
        tblSalesHistory.setShowGrid(true);
        tblSalesHistory.getTableHeader().setReorderingAllowed(false);
        jScrollPane2.setViewportView(tblSalesHistory);

        jButton8.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jButton8.setForeground(new java.awt.Color(255, 255, 255));
        jButton8.setText(" Refresh");
        jButton8.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jButton8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton8ActionPerformed(evt);
            }
        });

        cmbSortSalesHistory.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        cmbSortSalesHistory.setForeground(new java.awt.Color(255, 255, 255));
        cmbSortSalesHistory.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Sales / Invoice Id", "Item Name", "Date", "Month", "Year" }));
        cmbSortSalesHistory.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbSortSalesHistoryActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                .addGap(52, 52, 52)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jButton8))
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 1096, Short.MAX_VALUE)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(txtSearchSalesHistory, javax.swing.GroupLayout.DEFAULT_SIZE, 873, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(cmbSortSalesHistory, 0, 173, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel12)))
                .addGap(52, 52, 52))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(23, 23, 23)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtSearchSalesHistory, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel12)
                    .addComponent(cmbSortSalesHistory, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 398, Short.MAX_VALUE)
                .addGap(20, 20, 20)
                .addComponent(jButton8)
                .addGap(36, 36, 36))
        );

        jTabbedPaneEmployee.addTab("Your Sales History", jPanel5);

        jPanel6.setBackground(new java.awt.Color(34, 34, 34));

        txtSearchInvoiceHistory.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        txtSearchInvoiceHistory.setForeground(new java.awt.Color(255, 255, 255));
        txtSearchInvoiceHistory.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtSearchInvoiceHistoryKeyReleased(evt);
            }
        });

        jLabel13.setIcon(new javax.swing.ImageIcon(getClass().getResource("/frames/img/search.icon.png"))); // NOI18N

        tblInvoiceHistory.setBackground(new java.awt.Color(34, 34, 34));
        tblInvoiceHistory.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        tblInvoiceHistory.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Invoice Id", "Invoice Type", "Net Total", "NBT", "VAT", "Grand Total", "Billed By", "Date"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblInvoiceHistory.setGridColor(new java.awt.Color(61, 61, 61));
        tblInvoiceHistory.setShowGrid(true);
        tblInvoiceHistory.getTableHeader().setReorderingAllowed(false);
        jScrollPane3.setViewportView(tblInvoiceHistory);

        jButton10.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jButton10.setForeground(new java.awt.Color(255, 255, 255));
        jButton10.setText(" Refresh");
        jButton10.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jButton10.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton10ActionPerformed(evt);
            }
        });

        cmbSortInvoiceHistory.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        cmbSortInvoiceHistory.setForeground(new java.awt.Color(255, 255, 255));
        cmbSortInvoiceHistory.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Invoice Id", "Cash Invoice", "VAT Invoice", "Date", "Month", "Year" }));
        cmbSortInvoiceHistory.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbSortInvoiceHistoryActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
                .addGap(52, 52, 52)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jButton10))
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 1096, Short.MAX_VALUE)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addComponent(txtSearchInvoiceHistory, javax.swing.GroupLayout.DEFAULT_SIZE, 873, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(cmbSortInvoiceHistory, 0, 173, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel13)))
                .addGap(52, 52, 52))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGap(23, 23, 23)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtSearchInvoiceHistory, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel13)
                    .addComponent(cmbSortInvoiceHistory, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 398, Short.MAX_VALUE)
                .addGap(20, 20, 20)
                .addComponent(jButton10)
                .addGap(36, 36, 36))
        );

        jTabbedPaneEmployee.addTab("Invoice History", jPanel6);

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

    private void cmbSortTodaySalesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbSortTodaySalesActionPerformed
        searchTodaySalesTable();
    }//GEN-LAST:event_cmbSortTodaySalesActionPerformed

    private void txtSearchTodaySalesKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtSearchTodaySalesKeyReleased
        searchTodaySalesTable();
    }//GEN-LAST:event_txtSearchTodaySalesKeyReleased

    private void jButton9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton9ActionPerformed
        refreshSalesTable();
    }//GEN-LAST:event_jButton9ActionPerformed

    private void cmbSortSalesHistoryActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbSortSalesHistoryActionPerformed
        searchSalesHistoryTable();
    }//GEN-LAST:event_cmbSortSalesHistoryActionPerformed

    private void jButton8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton8ActionPerformed
        searchSalesHistoryTable();
    }//GEN-LAST:event_jButton8ActionPerformed

    private void txtSearchSalesHistoryKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtSearchSalesHistoryKeyReleased
        searchSalesHistoryTable();
    }//GEN-LAST:event_txtSearchSalesHistoryKeyReleased

    private void txtSearchInvoiceHistoryKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtSearchInvoiceHistoryKeyReleased
        searchInvoiceHistoryTable();
    }//GEN-LAST:event_txtSearchInvoiceHistoryKeyReleased

    private void jButton10ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton10ActionPerformed
        refreshInvoiceHistoryTable();
    }//GEN-LAST:event_jButton10ActionPerformed

    private void cmbSortInvoiceHistoryActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbSortInvoiceHistoryActionPerformed
        searchInvoiceHistoryTable();
    }//GEN-LAST:event_cmbSortInvoiceHistoryActionPerformed

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

                EmployeeSales.this.dispose();

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
                new EmployeeSales().setVisible(true);
            }
        });
    }

    private void setIcon() {
        setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("img/exfiltrat0z-icon.png")));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox<String> cmbSortInvoiceHistory;
    private javax.swing.JComboBox<String> cmbSortSalesHistory;
    private javax.swing.JComboBox<String> cmbSortTodaySales;
    private javax.swing.JButton jButton10;
    private javax.swing.JButton jButton8;
    private javax.swing.JButton jButton9;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JTabbedPane jTabbedPaneEmployee;
    private javax.swing.JLabel lblDate;
    public static final javax.swing.JLabel lblEmployeeUsername = new javax.swing.JLabel();
    private javax.swing.JLabel lblTime;
    private javax.swing.JLabel lblTodaySalesCount;
    private javax.swing.JPanel pnlHomeSubSelection;
    private javax.swing.JPanel pnlLogout;
    private javax.swing.JTable tblInvoiceHistory;
    private javax.swing.JTable tblSalesHistory;
    private javax.swing.JTable tblTodaySales;
    private javax.swing.JTextField txtSearchInvoiceHistory;
    private javax.swing.JTextField txtSearchSalesHistory;
    private javax.swing.JTextField txtSearchTodaySales;
    // End of variables declaration//GEN-END:variables
}
