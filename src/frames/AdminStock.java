/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package frames;

import com.formdev.flatlaf.FlatDarkLaf;
import com.formdev.flatlaf.ui.FlatBorder;
import java.awt.Color;
import java.awt.Font;
import java.awt.HeadlessException;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
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
public class AdminStock extends javax.swing.JFrame {

    Timer timer;
    String month;
    String year;
    String date;
    String today;
    String leadTime;
    String itemId;
    String selectedPOItemId;
    String selectedPOItemId1;
    String path = null;

    public AdminStock() {
        initComponents();

        refreshSupplierItemTable();
        refreshPOTable();
        fillCmbSuppliers();
        generatePOId();
        refreshPOTable();
        refreshGRNTable();
        fillCmbSuppliers();
        refreshSupplierItemTable();
        refreshStockTable();
        refreshLowStockTable();
        refreshAvailableStockTable();
        refreshStockReturnTable();
        setTableheader();

        AdminStock.this.setExtendedState(JFrame.MAXIMIZED_BOTH);
        AdminStock.this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setIcon();

        AdminStock.this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {

                AdminStock.this.dispose();
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
        JTableHeader supplierItem = tblSupplierItem.getTableHeader();
        supplierItem.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        supplierItem.setForeground(Color.white);
        supplierItem.setBackground(new Color(51, 51, 51));
        tblSupplierItem.getTableHeader().setReorderingAllowed(false);

        JTableHeader POItem = tblPOItem.getTableHeader();
        POItem.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        POItem.setForeground(Color.white);
        POItem.setBackground(new Color(51, 51, 51));
        tblPOItem.getTableHeader().setReorderingAllowed(false);

        JTableHeader GRN = tblGRN.getTableHeader();
        GRN.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        GRN.setForeground(Color.white);
        GRN.setBackground(new Color(51, 51, 51));
        tblGRN.getTableHeader().setReorderingAllowed(false);

        JTableHeader GRN1 = tblGRN1.getTableHeader();
        GRN1.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        GRN1.setForeground(Color.white);
        GRN1.setBackground(new Color(51, 51, 51));
        tblGRN1.getTableHeader().setReorderingAllowed(false);

        JTableHeader stock = tblStock.getTableHeader();
        stock.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        stock.setForeground(Color.white);
        stock.setBackground(new Color(51, 51, 51));
        tblGRN1.getTableHeader().setReorderingAllowed(false);

        JTableHeader lowStock = tblLowStock.getTableHeader();
        lowStock.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        lowStock.setForeground(Color.white);
        lowStock.setBackground(new Color(51, 51, 51));
        tblLowStock.getTableHeader().setReorderingAllowed(false);

        JTableHeader pOItem1 = tblPOItem1.getTableHeader();
        pOItem1.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        pOItem1.setForeground(Color.white);
        pOItem1.setBackground(new Color(51, 51, 51));
        tblPOItem1.getTableHeader().setReorderingAllowed(false);

        JTableHeader availableStock = tblAvailableStock.getTableHeader();
        availableStock.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        availableStock.setForeground(Color.white);
        availableStock.setBackground(new Color(51, 51, 51));
        tblAvailableStock.getTableHeader().setReorderingAllowed(false);

        JTableHeader stockReturn = tblStockReturn.getTableHeader();
        stockReturn.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        stockReturn.setForeground(Color.white);
        stockReturn.setBackground(new Color(51, 51, 51));
        tblStockReturn.getTableHeader().setReorderingAllowed(false);
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
        jTabbedPaneStockMgt = new javax.swing.JTabbedPane();
        jPanel2 = new javax.swing.JPanel();
        jTabbedPaneAddItem = new javax.swing.JTabbedPane();
        jPanel8 = new javax.swing.JPanel();
        jPanel20 = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel22 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        txtItemCode = new javax.swing.JTextField();
        txtItemName = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        cmbItemCategory = new javax.swing.JComboBox<>();
        cmbItemSupplier = new javax.swing.JComboBox<>();
        jPanel9 = new javax.swing.JPanel();
        txtSearchSupplierItem = new javax.swing.JTextField();
        cmbSortSupplierItem = new javax.swing.JComboBox<>();
        jLabel11 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblSupplierItem = new javax.swing.JTable();
        lblItemCount = new javax.swing.JLabel();
        jButton7 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();
        jPanel10 = new javax.swing.JPanel();
        jLabel13 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        txtSelectedItemSupplierItemCode = new javax.swing.JTextField();
        txtSelectedItemSupplierItemName = new javax.swing.JTextField();
        txtSelectedItemSupplierUsername = new javax.swing.JTextField();
        txtSelectedItemSupplierId = new javax.swing.JTextField();
        jButton8 = new javax.swing.JButton();
        jButton9 = new javax.swing.JButton();
        cmbSelectedItemSupplierItemCategory = new javax.swing.JComboBox<>();
        jPanel3 = new javax.swing.JPanel();
        jTabbedPaneAddNewItems = new javax.swing.JTabbedPane();
        jPanel12 = new javax.swing.JPanel();
        jLabel15 = new javax.swing.JLabel();
        txtPOId = new javax.swing.JTextField();
        jLabel19 = new javax.swing.JLabel();
        jLabel20 = new javax.swing.JLabel();
        jLabel21 = new javax.swing.JLabel();
        jLabel23 = new javax.swing.JLabel();
        txtPOItemQuantity = new javax.swing.JTextField();
        jButton11 = new javax.swing.JButton();
        jButton12 = new javax.swing.JButton();
        jLabel24 = new javax.swing.JLabel();
        cmbPOLeadTime = new javax.swing.JComboBox<>();
        cmbPOItemSupplier = new javax.swing.JComboBox<>();
        cmbPOItemName = new javax.swing.JComboBox<>();
        txtPOItemPrice = new javax.swing.JTextField();
        jSeparator1 = new javax.swing.JSeparator();
        jPanel13 = new javax.swing.JPanel();
        txtSearchPOItem = new javax.swing.JTextField();
        jScrollPane2 = new javax.swing.JScrollPane();
        tblPOItem = new javax.swing.JTable();
        jButton14 = new javax.swing.JButton();
        jButton15 = new javax.swing.JButton();
        jButton16 = new javax.swing.JButton();
        jButton17 = new javax.swing.JButton();
        cmbSortPOItem = new javax.swing.JComboBox<>();
        jLabel12 = new javax.swing.JLabel();
        lblPOId1 = new javax.swing.JLabel();
        lblPOId = new javax.swing.JLabel();
        jLabel25 = new javax.swing.JLabel();
        lblTotal1 = new javax.swing.JLabel();
        lblTotal = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        jTabbedPaneGRNChecking = new javax.swing.JTabbedPane();
        jPanel22 = new javax.swing.JPanel();
        txtSearchGRNItem = new javax.swing.JTextField();
        jScrollPane3 = new javax.swing.JScrollPane();
        tblGRN = new javax.swing.JTable();
        jButton21 = new javax.swing.JButton();
        jButton23 = new javax.swing.JButton();
        jButton24 = new javax.swing.JButton();
        cmbSortGRNItem = new javax.swing.JComboBox<>();
        jLabel32 = new javax.swing.JLabel();
        lblGRNId3 = new javax.swing.JLabel();
        lblGRNId = new javax.swing.JLabel();
        jLabel33 = new javax.swing.JLabel();
        lblTotal2 = new javax.swing.JLabel();
        lblGRNTotal = new javax.swing.JLabel();
        jButton30 = new javax.swing.JButton();
        jPanel21 = new javax.swing.JPanel();
        jLabel26 = new javax.swing.JLabel();
        txtGRNItemName = new javax.swing.JTextField();
        jLabel27 = new javax.swing.JLabel();
        jLabel28 = new javax.swing.JLabel();
        jLabel29 = new javax.swing.JLabel();
        jLabel30 = new javax.swing.JLabel();
        jButton18 = new javax.swing.JButton();
        jButton19 = new javax.swing.JButton();
        jLabel31 = new javax.swing.JLabel();
        txtGRNItemSellingPrice = new javax.swing.JTextField();
        jLabel34 = new javax.swing.JLabel();
        txtGRNStatus = new javax.swing.JTextField();
        txtGRNItemQuantity = new javax.swing.JTextField();
        txtGRNItemPrice = new javax.swing.JTextField();
        txtGRNItemLowStock = new javax.swing.JTextField();
        txtGRNItemStockCount = new javax.swing.JTextField();
        jPanel5 = new javax.swing.JPanel();
        jTabbedPaneStock = new javax.swing.JTabbedPane();
        jPanel25 = new javax.swing.JPanel();
        txtSearchStock = new javax.swing.JTextField();
        jScrollPane6 = new javax.swing.JScrollPane();
        tblStock = new javax.swing.JTable();
        cmbSortStock = new javax.swing.JComboBox<>();
        jLabel52 = new javax.swing.JLabel();
        lblStockCount = new javax.swing.JLabel();
        jButton38 = new javax.swing.JButton();
        jButton39 = new javax.swing.JButton();
        jButton40 = new javax.swing.JButton();
        jButton41 = new javax.swing.JButton();
        jPanel26 = new javax.swing.JPanel();
        txtSearchLowStock = new javax.swing.JTextField();
        jScrollPane7 = new javax.swing.JScrollPane();
        tblLowStock = new javax.swing.JTable();
        cmbSortLowStock = new javax.swing.JComboBox<>();
        jLabel54 = new javax.swing.JLabel();
        lblLowStockCount = new javax.swing.JLabel();
        jButton42 = new javax.swing.JButton();
        jButton43 = new javax.swing.JButton();
        jPanel6 = new javax.swing.JPanel();
        jTabbedPaneAddExistingItems = new javax.swing.JTabbedPane();
        jPanel14 = new javax.swing.JPanel();
        jLabel44 = new javax.swing.JLabel();
        txtPOId1 = new javax.swing.JTextField();
        jLabel45 = new javax.swing.JLabel();
        jLabel46 = new javax.swing.JLabel();
        jLabel47 = new javax.swing.JLabel();
        jLabel48 = new javax.swing.JLabel();
        txtPOItemQuantity1 = new javax.swing.JTextField();
        jButton28 = new javax.swing.JButton();
        jButton29 = new javax.swing.JButton();
        jLabel49 = new javax.swing.JLabel();
        cmbPOLeadTime1 = new javax.swing.JComboBox<>();
        cmbPOItemSupplier1 = new javax.swing.JComboBox<>();
        txtPOItemPrice1 = new javax.swing.JTextField();
        jSeparator2 = new javax.swing.JSeparator();
        cmbPOItemName1 = new javax.swing.JComboBox<>();
        jPanel15 = new javax.swing.JPanel();
        txtSearchPOItem1 = new javax.swing.JTextField();
        jScrollPane5 = new javax.swing.JScrollPane();
        tblPOItem1 = new javax.swing.JTable();
        jButton31 = new javax.swing.JButton();
        jButton32 = new javax.swing.JButton();
        jButton33 = new javax.swing.JButton();
        jButton34 = new javax.swing.JButton();
        cmbSortPOItem1 = new javax.swing.JComboBox<>();
        jLabel50 = new javax.swing.JLabel();
        lblPOId4 = new javax.swing.JLabel();
        lblPOId2 = new javax.swing.JLabel();
        jLabel51 = new javax.swing.JLabel();
        lblTotal4 = new javax.swing.JLabel();
        lblTotal3 = new javax.swing.JLabel();
        jPanel23 = new javax.swing.JPanel();
        txtSearchGRNItem1 = new javax.swing.JTextField();
        jScrollPane4 = new javax.swing.JScrollPane();
        tblGRN1 = new javax.swing.JTable();
        jButton22 = new javax.swing.JButton();
        jButton25 = new javax.swing.JButton();
        jButton26 = new javax.swing.JButton();
        cmbSortGRNItem1 = new javax.swing.JComboBox<>();
        jLabel35 = new javax.swing.JLabel();
        lblPOId3 = new javax.swing.JLabel();
        lblGRNId1 = new javax.swing.JLabel();
        jLabel36 = new javax.swing.JLabel();
        lblTotal33 = new javax.swing.JLabel();
        lblGRNTotal1 = new javax.swing.JLabel();
        jButton35 = new javax.swing.JButton();
        jPanel24 = new javax.swing.JPanel();
        jLabel37 = new javax.swing.JLabel();
        txtGRNItemName1 = new javax.swing.JTextField();
        jLabel38 = new javax.swing.JLabel();
        jLabel39 = new javax.swing.JLabel();
        jLabel40 = new javax.swing.JLabel();
        jLabel41 = new javax.swing.JLabel();
        jButton20 = new javax.swing.JButton();
        jButton27 = new javax.swing.JButton();
        jLabel42 = new javax.swing.JLabel();
        txtGRNItemSellingPrice1 = new javax.swing.JTextField();
        jLabel43 = new javax.swing.JLabel();
        txtGRNStatus1 = new javax.swing.JTextField();
        txtGRNItemQuantity1 = new javax.swing.JTextField();
        txtGRNItemPrice1 = new javax.swing.JTextField();
        txtGRNItemLowStock1 = new javax.swing.JTextField();
        txtGRNItemStockCount1 = new javax.swing.JTextField();
        jPanel7 = new javax.swing.JPanel();
        jTabbedPaneStockReturn = new javax.swing.JTabbedPane();
        jPanel17 = new javax.swing.JPanel();
        txtSearchAvailableStock = new javax.swing.JTextField();
        jScrollPane8 = new javax.swing.JScrollPane();
        tblAvailableStock = new javax.swing.JTable();
        jButton36 = new javax.swing.JButton();
        jButton37 = new javax.swing.JButton();
        jButton44 = new javax.swing.JButton();
        jButton45 = new javax.swing.JButton();
        cmbSortAvailableStock = new javax.swing.JComboBox<>();
        jLabel60 = new javax.swing.JLabel();
        lblStockCount1 = new javax.swing.JLabel();
        jPanel28 = new javax.swing.JPanel();
        jLabel64 = new javax.swing.JLabel();
        txtAvailableSKUId = new javax.swing.JTextField();
        jLabel65 = new javax.swing.JLabel();
        jLabel66 = new javax.swing.JLabel();
        jLabel67 = new javax.swing.JLabel();
        jLabel68 = new javax.swing.JLabel();
        jButton49 = new javax.swing.JButton();
        jLabel69 = new javax.swing.JLabel();
        txtAvailableStockCount = new javax.swing.JTextField();
        txtAvailableItemCode = new javax.swing.JTextField();
        txtAvailableItemName = new javax.swing.JTextField();
        txtAvailableSupplier = new javax.swing.JTextField();
        txtReturnCount = new javax.swing.JTextField();
        jPanel27 = new javax.swing.JPanel();
        txtSearchStockReturn = new javax.swing.JTextField();
        jScrollPane9 = new javax.swing.JScrollPane();
        tblStockReturn = new javax.swing.JTable();
        jButton46 = new javax.swing.JButton();
        jButton48 = new javax.swing.JButton();
        cmbSortStockReturn = new javax.swing.JComboBox<>();
        jLabel62 = new javax.swing.JLabel();
        lblStockReturnCount = new javax.swing.JLabel();

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
        jLabel2.setText("Stock");
        jLabel2.setToolTipText("");

        jLabel3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/frames/img/icons8_product_50px.png"))); // NOI18N

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

        jTabbedPaneStockMgt.setBackground(new java.awt.Color(34, 34, 34));
        jTabbedPaneStockMgt.setForeground(new java.awt.Color(255, 255, 255));
        jTabbedPaneStockMgt.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jTabbedPaneStockMgt.setOpaque(true);

        jTabbedPaneAddItem.setBackground(new java.awt.Color(34, 34, 34));
        jTabbedPaneAddItem.setForeground(new java.awt.Color(255, 255, 255));
        jTabbedPaneAddItem.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jTabbedPaneAddItem.setOpaque(true);

        jPanel20.setBackground(new java.awt.Color(34, 34, 34));
        jPanel20.setForeground(new java.awt.Color(255, 255, 255));

        jLabel6.setBackground(new java.awt.Color(0, 0, 0));
        jLabel6.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(255, 255, 255));
        jLabel6.setText("Item Name");

        jLabel7.setBackground(new java.awt.Color(0, 0, 0));
        jLabel7.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(255, 255, 255));
        jLabel7.setText("Item Code");

        jLabel22.setBackground(new java.awt.Color(0, 0, 0));
        jLabel22.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel22.setForeground(new java.awt.Color(255, 255, 255));
        jLabel22.setText("Item Category");

        jLabel8.setBackground(new java.awt.Color(0, 0, 0));
        jLabel8.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(255, 255, 255));
        jLabel8.setText("Supplier");

        txtItemCode.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        txtItemCode.setForeground(new java.awt.Color(255, 255, 255));
        txtItemCode.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtItemCodeKeyPressed(evt);
            }
        });

        txtItemName.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        txtItemName.setForeground(new java.awt.Color(255, 255, 255));
        txtItemName.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtItemNameKeyPressed(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtItemNameKeyTyped(evt);
            }
        });

        jButton1.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jButton1.setForeground(new java.awt.Color(255, 255, 255));
        jButton1.setText(" Add New Item");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jButton2.setForeground(new java.awt.Color(255, 255, 255));
        jButton2.setText(" View Item");
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

        cmbItemCategory.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        cmbItemCategory.setForeground(new java.awt.Color(255, 255, 255));
        cmbItemCategory.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Computers", "Displays", "Home", "Phones", "Tablets", "Watches", "Other" }));
        cmbItemCategory.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                cmbItemCategoryKeyPressed(evt);
            }
        });

        cmbItemSupplier.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        cmbItemSupplier.setForeground(new java.awt.Color(255, 255, 255));
        cmbItemSupplier.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                cmbItemSupplierKeyPressed(evt);
            }
        });

        javax.swing.GroupLayout jPanel20Layout = new javax.swing.GroupLayout(jPanel20);
        jPanel20.setLayout(jPanel20Layout);
        jPanel20Layout.setHorizontalGroup(
            jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel20Layout.createSequentialGroup()
                .addGap(52, 52, 52)
                .addGroup(jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel6)
                    .addComponent(jLabel8)
                    .addComponent(jLabel7)
                    .addComponent(jLabel22))
                .addGap(51, 51, 51)
                .addGroup(jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(txtItemName)
                    .addComponent(txtItemCode)
                    .addComponent(cmbItemCategory, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(cmbItemSupplier, javax.swing.GroupLayout.Alignment.LEADING, 0, 278, Short.MAX_VALUE))
                .addGap(78, 78, 78)
                .addGroup(jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButton2)
                    .addComponent(jButton1)
                    .addComponent(jButton3))
                .addContainerGap(510, Short.MAX_VALUE))
        );
        jPanel20Layout.setVerticalGroup(
            jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel20Layout.createSequentialGroup()
                .addGap(45, 45, 45)
                .addGroup(jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel20Layout.createSequentialGroup()
                        .addComponent(jButton1)
                        .addGap(18, 18, 18)
                        .addComponent(jButton2)
                        .addGap(18, 18, 18)
                        .addComponent(jButton3))
                    .addGroup(jPanel20Layout.createSequentialGroup()
                        .addGroup(jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel6)
                            .addComponent(txtItemName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel7)
                            .addComponent(txtItemCode, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel22)
                            .addComponent(cmbItemCategory, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel20Layout.createSequentialGroup()
                                .addGap(21, 21, 21)
                                .addComponent(jLabel8))
                            .addGroup(jPanel20Layout.createSequentialGroup()
                                .addGap(18, 18, 18)
                                .addComponent(cmbItemSupplier, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addContainerGap(311, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel20, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addComponent(jPanel20, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(0, 0, 0))
        );

        jTabbedPaneAddItem.addTab("New Item", jPanel8);

        jPanel9.setBackground(new java.awt.Color(34, 34, 34));

        txtSearchSupplierItem.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        txtSearchSupplierItem.setForeground(new java.awt.Color(255, 255, 255));
        txtSearchSupplierItem.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtSearchSupplierItemKeyReleased(evt);
            }
        });

        cmbSortSupplierItem.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        cmbSortSupplierItem.setForeground(new java.awt.Color(255, 255, 255));
        cmbSortSupplierItem.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Supplier Id", "Supplier Name", "Item Name", "Item Code", "Item Category", "Available", "Unavailable", "Whole Items" }));
        cmbSortSupplierItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbSortSupplierItemActionPerformed(evt);
            }
        });

        jLabel11.setIcon(new javax.swing.ImageIcon(getClass().getResource("/frames/img/search.icon.png"))); // NOI18N

        tblSupplierItem.setBackground(new java.awt.Color(34, 34, 34));
        tblSupplierItem.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        tblSupplierItem.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Supplier Id", "Supplier Name", "Item Name", "Item Code", "Item Category", "Availability"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblSupplierItem.setGridColor(new java.awt.Color(61, 61, 61));
        tblSupplierItem.setShowGrid(true);
        tblSupplierItem.getTableHeader().setReorderingAllowed(false);
        tblSupplierItem.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblSupplierItemMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tblSupplierItem);

        lblItemCount.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        lblItemCount.setText("Item Count : *");

        jButton7.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jButton7.setForeground(new java.awt.Color(255, 255, 255));
        jButton7.setText(" Refresh");
        jButton7.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jButton7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton7ActionPerformed(evt);
            }
        });

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

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addGap(52, 52, 52)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel9Layout.createSequentialGroup()
                        .addComponent(lblItemCount, javax.swing.GroupLayout.PREFERRED_SIZE, 242, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButton7)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jButton4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jButton5))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 1096, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel9Layout.createSequentialGroup()
                        .addComponent(txtSearchSupplierItem, javax.swing.GroupLayout.DEFAULT_SIZE, 873, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(cmbSortSupplierItem, 0, 173, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel11)))
                .addGap(52, 52, 52))
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addGap(23, 23, 23)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtSearchSupplierItem, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel11)
                    .addComponent(cmbSortSupplierItem, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 346, Short.MAX_VALUE)
                .addGap(30, 30, 30)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton4)
                    .addComponent(jButton5)
                    .addComponent(jButton7)
                    .addComponent(lblItemCount))
                .addGap(45, 45, 45))
        );

        jTabbedPaneAddItem.addTab("View Item", jPanel9);

        jPanel10.setBackground(new java.awt.Color(34, 34, 34));

        jLabel13.setBackground(new java.awt.Color(0, 0, 0));
        jLabel13.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel13.setForeground(new java.awt.Color(255, 255, 255));
        jLabel13.setText("Supplier Id");

        jLabel14.setBackground(new java.awt.Color(0, 0, 0));
        jLabel14.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel14.setForeground(new java.awt.Color(255, 255, 255));
        jLabel14.setText("Supplier Name");

        jLabel16.setBackground(new java.awt.Color(0, 0, 0));
        jLabel16.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel16.setForeground(new java.awt.Color(255, 255, 255));
        jLabel16.setText("Item Name");

        jLabel17.setBackground(new java.awt.Color(0, 0, 0));
        jLabel17.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel17.setForeground(new java.awt.Color(255, 255, 255));
        jLabel17.setText("Item Code");

        jLabel18.setBackground(new java.awt.Color(0, 0, 0));
        jLabel18.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel18.setForeground(new java.awt.Color(255, 255, 255));
        jLabel18.setText("Item Category");

        txtSelectedItemSupplierItemCode.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        txtSelectedItemSupplierItemCode.setForeground(new java.awt.Color(255, 255, 255));
        txtSelectedItemSupplierItemCode.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtSelectedItemSupplierItemCodeKeyPressed(evt);
            }
        });

        txtSelectedItemSupplierItemName.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        txtSelectedItemSupplierItemName.setForeground(new java.awt.Color(255, 255, 255));
        txtSelectedItemSupplierItemName.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtSelectedItemSupplierItemNameKeyPressed(evt);
            }
        });

        txtSelectedItemSupplierUsername.setBackground(new java.awt.Color(51, 51, 51));
        txtSelectedItemSupplierUsername.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        txtSelectedItemSupplierUsername.setEnabled(false);

        txtSelectedItemSupplierId.setBackground(new java.awt.Color(51, 51, 51));
        txtSelectedItemSupplierId.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        txtSelectedItemSupplierId.setEnabled(false);

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

        cmbSelectedItemSupplierItemCategory.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        cmbSelectedItemSupplierItemCategory.setForeground(new java.awt.Color(255, 255, 255));
        cmbSelectedItemSupplierItemCategory.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Computers", "Displays", "Home", "Phones", "Tablets", "Watches", "Other" }));
        cmbSelectedItemSupplierItemCategory.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                cmbSelectedItemSupplierItemCategoryKeyPressed(evt);
            }
        });

        javax.swing.GroupLayout jPanel10Layout = new javax.swing.GroupLayout(jPanel10);
        jPanel10.setLayout(jPanel10Layout);
        jPanel10Layout.setHorizontalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addGap(52, 52, 52)
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel13)
                    .addComponent(jLabel14)
                    .addComponent(jLabel16)
                    .addComponent(jLabel17)
                    .addComponent(jLabel18))
                .addGap(51, 51, 51)
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(txtSelectedItemSupplierItemCode, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 278, Short.MAX_VALUE)
                    .addComponent(txtSelectedItemSupplierItemName, javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtSelectedItemSupplierId, javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtSelectedItemSupplierUsername, javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(cmbSelectedItemSupplierItemCategory, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(78, 78, 78)
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButton9)
                    .addComponent(jButton8))
                .addContainerGap(553, Short.MAX_VALUE))
        );
        jPanel10Layout.setVerticalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addGap(45, 45, 45)
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel13)
                    .addComponent(txtSelectedItemSupplierId, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton8))
                .addGap(18, 18, 18)
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel14)
                    .addComponent(txtSelectedItemSupplierUsername, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton9))
                .addGap(18, 18, 18)
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtSelectedItemSupplierItemName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel16))
                .addGap(18, 18, 18)
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtSelectedItemSupplierItemCode, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel17))
                .addGap(18, 18, 18)
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel18)
                    .addComponent(cmbSelectedItemSupplierItemCategory, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(265, Short.MAX_VALUE))
        );

        jTabbedPaneAddItem.addTab("Edit Item", jPanel10);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPaneAddItem)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPaneAddItem)
        );

        jTabbedPaneStockMgt.addTab("Add Item", jPanel2);

        jTabbedPaneAddNewItems.setBackground(new java.awt.Color(34, 34, 34));
        jTabbedPaneAddNewItems.setForeground(new java.awt.Color(255, 255, 255));
        jTabbedPaneAddNewItems.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jTabbedPaneAddNewItems.setOpaque(true);

        jPanel12.setBackground(new java.awt.Color(34, 34, 34));

        jLabel15.setBackground(new java.awt.Color(0, 0, 0));
        jLabel15.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel15.setForeground(new java.awt.Color(255, 255, 255));
        jLabel15.setText("PO Id");

        txtPOId.setBackground(new java.awt.Color(51, 51, 51));
        txtPOId.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        txtPOId.setText("1");
        txtPOId.setEnabled(false);

        jLabel19.setBackground(new java.awt.Color(0, 0, 0));
        jLabel19.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel19.setForeground(new java.awt.Color(255, 255, 255));
        jLabel19.setText("Supplier");

        jLabel20.setBackground(new java.awt.Color(0, 0, 0));
        jLabel20.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel20.setForeground(new java.awt.Color(255, 255, 255));
        jLabel20.setText("Item Name");

        jLabel21.setBackground(new java.awt.Color(0, 0, 0));
        jLabel21.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel21.setForeground(new java.awt.Color(255, 255, 255));
        jLabel21.setText("Quantity");

        jLabel23.setBackground(new java.awt.Color(0, 0, 0));
        jLabel23.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel23.setForeground(new java.awt.Color(255, 255, 255));
        jLabel23.setText("Item Price");

        txtPOItemQuantity.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        txtPOItemQuantity.setForeground(new java.awt.Color(255, 255, 255));
        txtPOItemQuantity.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtPOItemQuantityKeyPressed(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtPOItemQuantityKeyTyped(evt);
            }
        });

        jButton11.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jButton11.setForeground(new java.awt.Color(255, 255, 255));
        jButton11.setText("Add to PO List");
        jButton11.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jButton11.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton11ActionPerformed(evt);
            }
        });

        jButton12.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jButton12.setForeground(new java.awt.Color(255, 255, 255));
        jButton12.setText("View PO Items");
        jButton12.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jButton12.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton12ActionPerformed(evt);
            }
        });

        jLabel24.setBackground(new java.awt.Color(0, 0, 0));
        jLabel24.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel24.setForeground(new java.awt.Color(255, 255, 255));
        jLabel24.setText("Lead Time");

        cmbPOLeadTime.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        cmbPOLeadTime.setForeground(new java.awt.Color(255, 255, 255));
        cmbPOLeadTime.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "1 Week", "2 Weeks", "3 Weeks", "1 Month" }));
        cmbPOLeadTime.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                cmbPOLeadTimeKeyPressed(evt);
            }
        });

        cmbPOItemSupplier.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        cmbPOItemSupplier.setForeground(new java.awt.Color(255, 255, 255));
        cmbPOItemSupplier.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbPOItemSupplierActionPerformed(evt);
            }
        });
        cmbPOItemSupplier.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                cmbPOItemSupplierKeyPressed(evt);
            }
        });

        cmbPOItemName.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        cmbPOItemName.setForeground(new java.awt.Color(255, 255, 255));
        cmbPOItemName.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                cmbPOItemNameKeyPressed(evt);
            }
        });

        txtPOItemPrice.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        txtPOItemPrice.setForeground(new java.awt.Color(255, 255, 255));
        txtPOItemPrice.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtPOItemPriceKeyPressed(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtPOItemPriceKeyTyped(evt);
            }
        });

        javax.swing.GroupLayout jPanel12Layout = new javax.swing.GroupLayout(jPanel12);
        jPanel12.setLayout(jPanel12Layout);
        jPanel12Layout.setHorizontalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel12Layout.createSequentialGroup()
                .addGap(52, 52, 52)
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jSeparator1)
                    .addGroup(jPanel12Layout.createSequentialGroup()
                        .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel15)
                            .addComponent(jLabel19)
                            .addComponent(jLabel20)
                            .addComponent(jLabel21)
                            .addComponent(jLabel23)
                            .addComponent(jLabel24))
                        .addGap(51, 51, 51)
                        .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(txtPOItemQuantity, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 278, Short.MAX_VALUE)
                            .addComponent(txtPOId, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(cmbPOLeadTime, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(cmbPOItemSupplier, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(cmbPOItemName, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(txtPOItemPrice, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 278, Short.MAX_VALUE))))
                .addGap(78, 78, 78)
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButton12)
                    .addComponent(jButton11))
                .addContainerGap(533, Short.MAX_VALUE))
        );
        jPanel12Layout.setVerticalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel12Layout.createSequentialGroup()
                .addGap(45, 45, 45)
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel15)
                    .addComponent(txtPOId, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton11))
                .addGap(18, 18, 18)
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel19)
                    .addComponent(jButton12)
                    .addComponent(cmbPOItemSupplier, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(14, 14, 14)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(13, 13, 13)
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel20)
                    .addComponent(cmbPOItemName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtPOItemQuantity, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel21))
                .addGap(18, 18, 18)
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel23)
                    .addComponent(txtPOItemPrice, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel24)
                    .addComponent(cmbPOLeadTime, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(207, Short.MAX_VALUE))
        );

        jTabbedPaneAddNewItems.addTab("New PO Item", jPanel12);

        jPanel13.setBackground(new java.awt.Color(34, 34, 34));

        txtSearchPOItem.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        txtSearchPOItem.setForeground(new java.awt.Color(255, 255, 255));
        txtSearchPOItem.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtSearchPOItemKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtSearchPOItemKeyTyped(evt);
            }
        });

        tblPOItem.setBackground(new java.awt.Color(34, 34, 34));
        tblPOItem.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        tblPOItem.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "PO Item Id", "Item Name", "Item Code", "Qunatity", "Item Price", "Supplier", "Total", "Lead Time"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblPOItem.setGridColor(new java.awt.Color(61, 61, 61));
        tblPOItem.setShowGrid(true);
        tblPOItem.getTableHeader().setReorderingAllowed(false);
        tblPOItem.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblPOItemMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(tblPOItem);

        jButton14.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jButton14.setForeground(new java.awt.Color(255, 255, 255));
        jButton14.setText(" Refresh");
        jButton14.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jButton14.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton14ActionPerformed(evt);
            }
        });

        jButton15.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jButton15.setForeground(new java.awt.Color(255, 255, 255));
        jButton15.setText("Add PO Item");
        jButton15.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jButton15.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton15ActionPerformed(evt);
            }
        });

        jButton16.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jButton16.setForeground(new java.awt.Color(255, 255, 255));
        jButton16.setText("Delete");
        jButton16.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jButton16.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton16ActionPerformed(evt);
            }
        });

        jButton17.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jButton17.setForeground(new java.awt.Color(255, 255, 255));
        jButton17.setText("Print / Issue");
        jButton17.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jButton17.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton17ActionPerformed(evt);
            }
        });

        cmbSortPOItem.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        cmbSortPOItem.setForeground(new java.awt.Color(255, 255, 255));
        cmbSortPOItem.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "PO Id", "Item Name", "Item Code", "Supplier" }));
        cmbSortPOItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbSortPOItemActionPerformed(evt);
            }
        });

        jLabel12.setIcon(new javax.swing.ImageIcon(getClass().getResource("/frames/img/search.icon.png"))); // NOI18N

        lblPOId1.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        lblPOId1.setForeground(new java.awt.Color(153, 153, 153));
        lblPOId1.setText("PO Id :");

        lblPOId.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        lblPOId.setForeground(new java.awt.Color(153, 153, 153));

        jLabel25.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel25.setForeground(new java.awt.Color(153, 153, 153));
        jLabel25.setText("|");

        lblTotal1.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        lblTotal1.setForeground(new java.awt.Color(153, 153, 153));
        lblTotal1.setText("PO Total : ");

        lblTotal.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        lblTotal.setForeground(new java.awt.Color(153, 153, 153));
        lblTotal.setText("Rs. 0.0");

        javax.swing.GroupLayout jPanel13Layout = new javax.swing.GroupLayout(jPanel13);
        jPanel13.setLayout(jPanel13Layout);
        jPanel13Layout.setHorizontalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel13Layout.createSequentialGroup()
                .addGap(52, 52, 52)
                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel13Layout.createSequentialGroup()
                        .addComponent(lblPOId1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblPOId, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel25, javax.swing.GroupLayout.PREFERRED_SIZE, 4, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblTotal1)
                        .addGap(0, 0, 0)
                        .addComponent(lblTotal, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButton14)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jButton15)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jButton16)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jButton17))
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 1096, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel13Layout.createSequentialGroup()
                        .addComponent(txtSearchPOItem, javax.swing.GroupLayout.DEFAULT_SIZE, 873, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(cmbSortPOItem, 0, 173, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel12)))
                .addGap(52, 52, 52))
        );
        jPanel13Layout.setVerticalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel13Layout.createSequentialGroup()
                .addGap(23, 23, 23)
                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtSearchPOItem, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel12)
                    .addComponent(cmbSortPOItem, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 346, Short.MAX_VALUE)
                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel13Layout.createSequentialGroup()
                        .addGap(31, 31, 31)
                        .addComponent(lblPOId1))
                    .addGroup(jPanel13Layout.createSequentialGroup()
                        .addGap(28, 28, 28)
                        .addComponent(lblPOId, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel13Layout.createSequentialGroup()
                        .addGap(31, 31, 31)
                        .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lblTotal)
                            .addComponent(lblTotal1)))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel13Layout.createSequentialGroup()
                        .addGap(25, 25, 25)
                        .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jButton15)
                                .addComponent(jButton16)
                                .addComponent(jButton17)
                                .addComponent(jButton14))
                            .addComponent(jLabel25, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(45, 45, 45))
        );

        jTabbedPaneAddNewItems.addTab("View PO Item", jPanel13);

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPaneAddNewItems)
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPaneAddNewItems)
        );

        jTabbedPaneStockMgt.addTab("Manage PO", jPanel3);

        jTabbedPaneGRNChecking.setBackground(new java.awt.Color(34, 34, 34));
        jTabbedPaneGRNChecking.setForeground(new java.awt.Color(255, 255, 255));
        jTabbedPaneGRNChecking.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jTabbedPaneGRNChecking.setOpaque(true);

        jPanel22.setBackground(new java.awt.Color(34, 34, 34));

        txtSearchGRNItem.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        txtSearchGRNItem.setForeground(new java.awt.Color(255, 255, 255));
        txtSearchGRNItem.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtSearchGRNItemKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtSearchGRNItemKeyTyped(evt);
            }
        });

        tblGRN.setBackground(new java.awt.Color(34, 34, 34));
        tblGRN.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        tblGRN.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "GRN Item Id", "Item Name", "Item Code", "Qunatity", "Item Price", "Supplier", "Status"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblGRN.setGridColor(new java.awt.Color(61, 61, 61));
        tblGRN.setShowGrid(true);
        tblGRN.getTableHeader().setReorderingAllowed(false);
        tblGRN.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblGRNMouseClicked(evt);
            }
        });
        jScrollPane3.setViewportView(tblGRN);

        jButton21.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jButton21.setForeground(new java.awt.Color(255, 255, 255));
        jButton21.setText(" Refresh");
        jButton21.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jButton21.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton21ActionPerformed(evt);
            }
        });

        jButton23.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jButton23.setForeground(new java.awt.Color(255, 255, 255));
        jButton23.setText("Bad Order");
        jButton23.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jButton23.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton23ActionPerformed(evt);
            }
        });

        jButton24.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jButton24.setForeground(new java.awt.Color(255, 255, 255));
        jButton24.setText("Check");
        jButton24.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jButton24.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton24ActionPerformed(evt);
            }
        });

        cmbSortGRNItem.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        cmbSortGRNItem.setForeground(new java.awt.Color(255, 255, 255));
        cmbSortGRNItem.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "PO Id", "GRN Id", "Checked", "Unchecked", "Bad Order" }));
        cmbSortGRNItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbSortGRNItemActionPerformed(evt);
            }
        });

        jLabel32.setIcon(new javax.swing.ImageIcon(getClass().getResource("/frames/img/search.icon.png"))); // NOI18N

        lblGRNId3.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        lblGRNId3.setForeground(new java.awt.Color(153, 153, 153));
        lblGRNId3.setText("GRN Id :");

        lblGRNId.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        lblGRNId.setForeground(new java.awt.Color(153, 153, 153));

        jLabel33.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel33.setForeground(new java.awt.Color(153, 153, 153));
        jLabel33.setText("|");

        lblTotal2.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        lblTotal2.setForeground(new java.awt.Color(153, 153, 153));
        lblTotal2.setText("GRN Total : ");

        lblGRNTotal.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        lblGRNTotal.setForeground(new java.awt.Color(153, 153, 153));
        lblGRNTotal.setText("Rs. 0.0");

        jButton30.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jButton30.setForeground(new java.awt.Color(255, 255, 255));
        jButton30.setText("Print");
        jButton30.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jButton30.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton30ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel22Layout = new javax.swing.GroupLayout(jPanel22);
        jPanel22.setLayout(jPanel22Layout);
        jPanel22Layout.setHorizontalGroup(
            jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel22Layout.createSequentialGroup()
                .addGap(52, 52, 52)
                .addGroup(jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel22Layout.createSequentialGroup()
                        .addComponent(lblGRNId3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblGRNId, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel33, javax.swing.GroupLayout.PREFERRED_SIZE, 4, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblTotal2)
                        .addGap(0, 0, 0)
                        .addComponent(lblGRNTotal, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButton21)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jButton23)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jButton30)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jButton24))
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 1096, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel22Layout.createSequentialGroup()
                        .addComponent(txtSearchGRNItem, javax.swing.GroupLayout.DEFAULT_SIZE, 873, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(cmbSortGRNItem, 0, 173, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel32)))
                .addGap(52, 52, 52))
        );
        jPanel22Layout.setVerticalGroup(
            jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel22Layout.createSequentialGroup()
                .addGap(23, 23, 23)
                .addGroup(jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtSearchGRNItem, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel32)
                    .addComponent(cmbSortGRNItem, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 346, Short.MAX_VALUE)
                .addGroup(jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel22Layout.createSequentialGroup()
                        .addGap(31, 31, 31)
                        .addComponent(lblGRNId3))
                    .addGroup(jPanel22Layout.createSequentialGroup()
                        .addGap(28, 28, 28)
                        .addComponent(lblGRNId, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel22Layout.createSequentialGroup()
                        .addGap(31, 31, 31)
                        .addGroup(jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lblGRNTotal)
                            .addComponent(lblTotal2)))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel22Layout.createSequentialGroup()
                        .addGap(25, 25, 25)
                        .addGroup(jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jButton23)
                                .addComponent(jButton24)
                                .addComponent(jButton21)
                                .addComponent(jButton30))
                            .addComponent(jLabel33, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(45, 45, 45))
        );

        jTabbedPaneGRNChecking.addTab("View GRN", jPanel22);

        jPanel21.setBackground(new java.awt.Color(34, 34, 34));

        jLabel26.setBackground(new java.awt.Color(0, 0, 0));
        jLabel26.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel26.setForeground(new java.awt.Color(255, 255, 255));
        jLabel26.setText("Item Name");

        txtGRNItemName.setBackground(new java.awt.Color(51, 51, 51));
        txtGRNItemName.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        txtGRNItemName.setEnabled(false);

        jLabel27.setBackground(new java.awt.Color(0, 0, 0));
        jLabel27.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel27.setForeground(new java.awt.Color(255, 255, 255));
        jLabel27.setText("GRN Status");

        jLabel28.setBackground(new java.awt.Color(0, 0, 0));
        jLabel28.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel28.setForeground(new java.awt.Color(255, 255, 255));
        jLabel28.setText("Quantity");

        jLabel29.setBackground(new java.awt.Color(0, 0, 0));
        jLabel29.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel29.setForeground(new java.awt.Color(255, 255, 255));
        jLabel29.setText("Item Price");

        jLabel30.setBackground(new java.awt.Color(0, 0, 0));
        jLabel30.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel30.setForeground(new java.awt.Color(255, 255, 255));
        jLabel30.setText("Selling Price");

        jButton18.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jButton18.setForeground(new java.awt.Color(255, 255, 255));
        jButton18.setText("Check and Add to Stock");
        jButton18.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jButton18.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton18ActionPerformed(evt);
            }
        });

        jButton19.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jButton19.setForeground(new java.awt.Color(255, 255, 255));
        jButton19.setText("View GRN");
        jButton19.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jButton19.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton19ActionPerformed(evt);
            }
        });

        jLabel31.setBackground(new java.awt.Color(0, 0, 0));
        jLabel31.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel31.setForeground(new java.awt.Color(255, 255, 255));
        jLabel31.setText("Low Stock");

        txtGRNItemSellingPrice.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        txtGRNItemSellingPrice.setForeground(new java.awt.Color(255, 255, 255));
        txtGRNItemSellingPrice.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtGRNItemSellingPriceKeyPressed(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtGRNItemSellingPriceKeyTyped(evt);
            }
        });

        jLabel34.setBackground(new java.awt.Color(0, 0, 0));
        jLabel34.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel34.setForeground(new java.awt.Color(255, 255, 255));
        jLabel34.setText("Available Stock");

        txtGRNStatus.setBackground(new java.awt.Color(51, 51, 51));
        txtGRNStatus.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        txtGRNStatus.setEnabled(false);

        txtGRNItemQuantity.setBackground(new java.awt.Color(51, 51, 51));
        txtGRNItemQuantity.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        txtGRNItemQuantity.setEnabled(false);

        txtGRNItemPrice.setBackground(new java.awt.Color(51, 51, 51));
        txtGRNItemPrice.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        txtGRNItemPrice.setEnabled(false);

        txtGRNItemLowStock.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        txtGRNItemLowStock.setForeground(new java.awt.Color(255, 255, 255));
        txtGRNItemLowStock.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtGRNItemLowStockKeyPressed(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtGRNItemLowStockKeyTyped(evt);
            }
        });

        txtGRNItemStockCount.setBackground(new java.awt.Color(51, 51, 51));
        txtGRNItemStockCount.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        txtGRNItemStockCount.setText("0");
        txtGRNItemStockCount.setEnabled(false);

        javax.swing.GroupLayout jPanel21Layout = new javax.swing.GroupLayout(jPanel21);
        jPanel21.setLayout(jPanel21Layout);
        jPanel21Layout.setHorizontalGroup(
            jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel21Layout.createSequentialGroup()
                .addGap(52, 52, 52)
                .addGroup(jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel26)
                    .addComponent(jLabel27)
                    .addComponent(jLabel28)
                    .addComponent(jLabel29)
                    .addComponent(jLabel30)
                    .addComponent(jLabel31)
                    .addComponent(jLabel34))
                .addGap(51, 51, 51)
                .addGroup(jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel21Layout.createSequentialGroup()
                        .addGroup(jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addComponent(txtGRNItemSellingPrice, javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(txtGRNItemName)
                                .addComponent(txtGRNStatus, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 278, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(txtGRNItemLowStock, javax.swing.GroupLayout.Alignment.LEADING))
                            .addComponent(txtGRNItemQuantity, javax.swing.GroupLayout.PREFERRED_SIZE, 278, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtGRNItemStockCount, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 278, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(72, 72, 72)
                        .addGroup(jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jButton19)
                            .addComponent(jButton18)))
                    .addComponent(txtGRNItemPrice, javax.swing.GroupLayout.PREFERRED_SIZE, 278, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(448, Short.MAX_VALUE))
        );
        jPanel21Layout.setVerticalGroup(
            jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel21Layout.createSequentialGroup()
                .addGap(45, 45, 45)
                .addGroup(jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel26)
                    .addComponent(txtGRNItemName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton18))
                .addGap(18, 18, 18)
                .addGroup(jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel21Layout.createSequentialGroup()
                        .addGroup(jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel27)
                            .addComponent(jButton19))
                        .addGap(14, 14, 14))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel21Layout.createSequentialGroup()
                        .addComponent(txtGRNStatus, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)))
                .addGroup(jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel21Layout.createSequentialGroup()
                        .addGap(3, 3, 3)
                        .addComponent(jLabel28))
                    .addComponent(txtGRNItemQuantity, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel29)
                    .addComponent(txtGRNItemPrice, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel30)
                    .addComponent(txtGRNItemSellingPrice, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel31)
                    .addComponent(txtGRNItemLowStock, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel34)
                    .addComponent(txtGRNItemStockCount, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(173, Short.MAX_VALUE))
        );

        jTabbedPaneGRNChecking.addTab("GRN Checking", jPanel21);

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPaneGRNChecking)
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPaneGRNChecking)
        );

        jTabbedPaneStockMgt.addTab("Manage GRN", jPanel4);

        jTabbedPaneStock.setBackground(new java.awt.Color(34, 34, 34));
        jTabbedPaneStock.setForeground(new java.awt.Color(255, 255, 255));
        jTabbedPaneStock.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jTabbedPaneStock.setOpaque(true);

        jPanel25.setBackground(new java.awt.Color(34, 34, 34));

        txtSearchStock.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        txtSearchStock.setForeground(new java.awt.Color(255, 255, 255));
        txtSearchStock.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtSearchStockKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtSearchStockKeyTyped(evt);
            }
        });

        tblStock.setBackground(new java.awt.Color(34, 34, 34));
        tblStock.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        tblStock.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "SKU Id", "Item Code", "Item Name", "Purchase Price", "Selling Price", "Supplier", "Stock Count", "Low Stock"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblStock.setGridColor(new java.awt.Color(61, 61, 61));
        tblStock.setShowGrid(true);
        tblStock.getTableHeader().setReorderingAllowed(false);
        tblStock.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblStockMouseClicked(evt);
            }
        });
        jScrollPane6.setViewportView(tblStock);

        cmbSortStock.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        cmbSortStock.setForeground(new java.awt.Color(255, 255, 255));
        cmbSortStock.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "SKU Id", "Item Code", "Item Name", "Supplier", "Selling Stock", "Whole Stock", "Low Stock" }));
        cmbSortStock.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbSortStockActionPerformed(evt);
            }
        });

        jLabel52.setIcon(new javax.swing.ImageIcon(getClass().getResource("/frames/img/search.icon.png"))); // NOI18N

        lblStockCount.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        lblStockCount.setForeground(new java.awt.Color(153, 153, 153));
        lblStockCount.setText("SKU Count : 0");

        jButton38.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jButton38.setForeground(new java.awt.Color(255, 255, 255));
        jButton38.setText(" Refresh");
        jButton38.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jButton38.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton38ActionPerformed(evt);
            }
        });

        jButton39.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jButton39.setForeground(new java.awt.Color(255, 255, 255));
        jButton39.setText("Re-Order");
        jButton39.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jButton39.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton39ActionPerformed(evt);
            }
        });

        jButton40.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jButton40.setForeground(new java.awt.Color(255, 255, 255));
        jButton40.setText("Print Barcodes");
        jButton40.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jButton40.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton40ActionPerformed(evt);
            }
        });

        jButton41.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jButton41.setForeground(new java.awt.Color(255, 255, 255));
        jButton41.setText("Print All");
        jButton41.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jButton41.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton41ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel25Layout = new javax.swing.GroupLayout(jPanel25);
        jPanel25.setLayout(jPanel25Layout);
        jPanel25Layout.setHorizontalGroup(
            jPanel25Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel25Layout.createSequentialGroup()
                .addGap(52, 52, 52)
                .addGroup(jPanel25Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel25Layout.createSequentialGroup()
                        .addComponent(lblStockCount, javax.swing.GroupLayout.PREFERRED_SIZE, 235, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButton38)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jButton39)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jButton40)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jButton41))
                    .addComponent(jScrollPane6, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 1096, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel25Layout.createSequentialGroup()
                        .addComponent(txtSearchStock, javax.swing.GroupLayout.DEFAULT_SIZE, 873, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(cmbSortStock, 0, 173, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel52)))
                .addGap(52, 52, 52))
        );
        jPanel25Layout.setVerticalGroup(
            jPanel25Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel25Layout.createSequentialGroup()
                .addGap(23, 23, 23)
                .addGroup(jPanel25Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtSearchStock, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel52)
                    .addComponent(cmbSortStock, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane6, javax.swing.GroupLayout.DEFAULT_SIZE, 346, Short.MAX_VALUE)
                .addGap(28, 28, 28)
                .addGroup(jPanel25Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel25Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jButton39)
                        .addComponent(jButton40)
                        .addComponent(jButton38)
                        .addComponent(jButton41))
                    .addComponent(lblStockCount))
                .addGap(47, 47, 47))
        );

        jTabbedPaneStock.addTab("Available Stock", jPanel25);

        jPanel26.setBackground(new java.awt.Color(34, 34, 34));

        txtSearchLowStock.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        txtSearchLowStock.setForeground(new java.awt.Color(255, 255, 255));
        txtSearchLowStock.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtSearchLowStockKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtSearchLowStockKeyTyped(evt);
            }
        });

        tblLowStock.setBackground(new java.awt.Color(34, 34, 34));
        tblLowStock.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        tblLowStock.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "SKU Id", "Item Code", "Item Name", "Purchase Price", "Selling Price", "Supplier", "Stock Count", "Low Stock"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblLowStock.setGridColor(new java.awt.Color(61, 61, 61));
        tblLowStock.setShowGrid(true);
        tblLowStock.getTableHeader().setReorderingAllowed(false);
        tblLowStock.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblLowStockMouseClicked(evt);
            }
        });
        jScrollPane7.setViewportView(tblLowStock);

        cmbSortLowStock.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        cmbSortLowStock.setForeground(new java.awt.Color(255, 255, 255));
        cmbSortLowStock.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "SKU Id", "Item Code", "Item Name", "Supplier" }));
        cmbSortLowStock.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbSortLowStockActionPerformed(evt);
            }
        });

        jLabel54.setIcon(new javax.swing.ImageIcon(getClass().getResource("/frames/img/search.icon.png"))); // NOI18N

        lblLowStockCount.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        lblLowStockCount.setForeground(new java.awt.Color(153, 153, 153));
        lblLowStockCount.setText("SKU Count : 0");

        jButton42.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jButton42.setForeground(new java.awt.Color(255, 255, 255));
        jButton42.setText("Re-Order");
        jButton42.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jButton42.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton42ActionPerformed(evt);
            }
        });

        jButton43.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jButton43.setForeground(new java.awt.Color(255, 255, 255));
        jButton43.setText("Print All");
        jButton43.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jButton43.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton43ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel26Layout = new javax.swing.GroupLayout(jPanel26);
        jPanel26.setLayout(jPanel26Layout);
        jPanel26Layout.setHorizontalGroup(
            jPanel26Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel26Layout.createSequentialGroup()
                .addGap(52, 52, 52)
                .addGroup(jPanel26Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel26Layout.createSequentialGroup()
                        .addComponent(lblLowStockCount, javax.swing.GroupLayout.PREFERRED_SIZE, 235, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButton42)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jButton43))
                    .addComponent(jScrollPane7, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 1096, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel26Layout.createSequentialGroup()
                        .addComponent(txtSearchLowStock, javax.swing.GroupLayout.DEFAULT_SIZE, 873, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(cmbSortLowStock, 0, 173, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel54)))
                .addGap(52, 52, 52))
        );
        jPanel26Layout.setVerticalGroup(
            jPanel26Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel26Layout.createSequentialGroup()
                .addGap(23, 23, 23)
                .addGroup(jPanel26Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtSearchLowStock, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel54)
                    .addComponent(cmbSortLowStock, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane7, javax.swing.GroupLayout.DEFAULT_SIZE, 346, Short.MAX_VALUE)
                .addGap(28, 28, 28)
                .addGroup(jPanel26Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel26Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jButton42)
                        .addComponent(jButton43))
                    .addComponent(lblLowStockCount))
                .addGap(47, 47, 47))
        );

        jTabbedPaneStock.addTab("Low Stock", jPanel26);

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPaneStock)
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPaneStock)
        );

        jTabbedPaneStockMgt.addTab("View Stock", jPanel5);

        jTabbedPaneAddExistingItems.setBackground(new java.awt.Color(34, 34, 34));
        jTabbedPaneAddExistingItems.setForeground(new java.awt.Color(255, 255, 255));
        jTabbedPaneAddExistingItems.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jTabbedPaneAddExistingItems.setOpaque(true);

        jPanel14.setBackground(new java.awt.Color(34, 34, 34));
        jPanel14.setForeground(new java.awt.Color(255, 255, 255));

        jLabel44.setBackground(new java.awt.Color(0, 0, 0));
        jLabel44.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel44.setForeground(new java.awt.Color(255, 255, 255));
        jLabel44.setText("PO Id");

        txtPOId1.setBackground(new java.awt.Color(51, 51, 51));
        txtPOId1.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        txtPOId1.setText("1");
        txtPOId1.setEnabled(false);

        jLabel45.setBackground(new java.awt.Color(0, 0, 0));
        jLabel45.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel45.setForeground(new java.awt.Color(255, 255, 255));
        jLabel45.setText("Supplier");

        jLabel46.setBackground(new java.awt.Color(0, 0, 0));
        jLabel46.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel46.setForeground(new java.awt.Color(255, 255, 255));
        jLabel46.setText("Item Name");

        jLabel47.setBackground(new java.awt.Color(0, 0, 0));
        jLabel47.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel47.setForeground(new java.awt.Color(255, 255, 255));
        jLabel47.setText("Quantity");

        jLabel48.setBackground(new java.awt.Color(0, 0, 0));
        jLabel48.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel48.setForeground(new java.awt.Color(255, 255, 255));
        jLabel48.setText("Item Price");

        txtPOItemQuantity1.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        txtPOItemQuantity1.setForeground(new java.awt.Color(255, 255, 255));
        txtPOItemQuantity1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtPOItemQuantity1KeyPressed(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtPOItemQuantity1KeyTyped(evt);
            }
        });

        jButton28.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jButton28.setForeground(new java.awt.Color(255, 255, 255));
        jButton28.setText("Add to PO List");
        jButton28.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jButton28.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton28ActionPerformed(evt);
            }
        });

        jButton29.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jButton29.setForeground(new java.awt.Color(255, 255, 255));
        jButton29.setText("View PO Items");
        jButton29.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jButton29.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton29ActionPerformed(evt);
            }
        });

        jLabel49.setBackground(new java.awt.Color(0, 0, 0));
        jLabel49.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel49.setText("Lead Time");

        cmbPOLeadTime1.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        cmbPOLeadTime1.setForeground(new java.awt.Color(255, 255, 255));
        cmbPOLeadTime1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "1 Week", "2 Weeks", "3 Weeks", "1 Month" }));
        cmbPOLeadTime1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                cmbPOLeadTime1KeyPressed(evt);
            }
        });

        cmbPOItemSupplier1.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        cmbPOItemSupplier1.setForeground(new java.awt.Color(255, 255, 255));
        cmbPOItemSupplier1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbPOItemSupplier1ActionPerformed(evt);
            }
        });
        cmbPOItemSupplier1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                cmbPOItemSupplier1KeyPressed(evt);
            }
        });

        txtPOItemPrice1.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        txtPOItemPrice1.setForeground(new java.awt.Color(255, 255, 255));
        txtPOItemPrice1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtPOItemPrice1KeyPressed(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtPOItemPrice1KeyTyped(evt);
            }
        });

        cmbPOItemName1.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        cmbPOItemName1.setForeground(new java.awt.Color(255, 255, 255));
        cmbPOItemName1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                cmbPOItemName1KeyPressed(evt);
            }
        });

        javax.swing.GroupLayout jPanel14Layout = new javax.swing.GroupLayout(jPanel14);
        jPanel14.setLayout(jPanel14Layout);
        jPanel14Layout.setHorizontalGroup(
            jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel14Layout.createSequentialGroup()
                .addGap(52, 52, 52)
                .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jSeparator2)
                    .addGroup(jPanel14Layout.createSequentialGroup()
                        .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel44)
                            .addComponent(jLabel45)
                            .addComponent(jLabel46)
                            .addComponent(jLabel47)
                            .addComponent(jLabel48)
                            .addComponent(jLabel49))
                        .addGap(51, 51, 51)
                        .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(txtPOItemQuantity1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 278, Short.MAX_VALUE)
                            .addComponent(txtPOId1, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(cmbPOLeadTime1, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(cmbPOItemSupplier1, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(txtPOItemPrice1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 278, Short.MAX_VALUE)
                            .addComponent(cmbPOItemName1, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addGap(78, 78, 78)
                .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButton29)
                    .addComponent(jButton28))
                .addContainerGap(533, Short.MAX_VALUE))
        );
        jPanel14Layout.setVerticalGroup(
            jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel14Layout.createSequentialGroup()
                .addGap(45, 45, 45)
                .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel44)
                    .addComponent(txtPOId1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton28))
                .addGap(18, 18, 18)
                .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel45)
                    .addComponent(jButton29)
                    .addComponent(cmbPOItemSupplier1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(14, 14, 14)
                .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(13, 13, 13)
                .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel46)
                    .addComponent(cmbPOItemName1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtPOItemQuantity1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel47))
                .addGap(18, 18, 18)
                .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel48)
                    .addComponent(txtPOItemPrice1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel49)
                    .addComponent(cmbPOLeadTime1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(207, Short.MAX_VALUE))
        );

        jTabbedPaneAddExistingItems.addTab("New PO Item", jPanel14);

        jPanel15.setBackground(new java.awt.Color(34, 34, 34));

        txtSearchPOItem1.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        txtSearchPOItem1.setForeground(new java.awt.Color(255, 255, 255));
        txtSearchPOItem1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtSearchPOItem1KeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtSearchPOItem1KeyTyped(evt);
            }
        });

        tblPOItem1.setBackground(new java.awt.Color(34, 34, 34));
        tblPOItem1.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        tblPOItem1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "PO Item Id", "Item Name", "Item Code", "Qunatity", "Item Price", "Supplier", "Total", "Lead Time"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblPOItem1.setGridColor(new java.awt.Color(61, 61, 61));
        tblPOItem1.setShowGrid(true);
        tblPOItem1.getTableHeader().setReorderingAllowed(false);
        tblPOItem1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblPOItem1MouseClicked(evt);
            }
        });
        jScrollPane5.setViewportView(tblPOItem1);
        if (tblPOItem1.getColumnModel().getColumnCount() > 0) {
            tblPOItem1.getColumnModel().getColumn(5).setHeaderValue("Supplier");
            tblPOItem1.getColumnModel().getColumn(6).setHeaderValue("Total");
            tblPOItem1.getColumnModel().getColumn(7).setHeaderValue("Lead Time");
        }

        jButton31.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jButton31.setForeground(new java.awt.Color(255, 255, 255));
        jButton31.setText(" Refresh");
        jButton31.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jButton31.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton31ActionPerformed(evt);
            }
        });

        jButton32.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jButton32.setForeground(new java.awt.Color(255, 255, 255));
        jButton32.setText("Add PO Item");
        jButton32.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jButton32.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton32ActionPerformed(evt);
            }
        });

        jButton33.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jButton33.setForeground(new java.awt.Color(255, 255, 255));
        jButton33.setText("Delete");
        jButton33.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jButton33.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton33ActionPerformed(evt);
            }
        });

        jButton34.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jButton34.setForeground(new java.awt.Color(255, 255, 255));
        jButton34.setText("Print / Issue");
        jButton34.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jButton34.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton34ActionPerformed(evt);
            }
        });

        cmbSortPOItem1.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        cmbSortPOItem1.setForeground(new java.awt.Color(255, 255, 255));
        cmbSortPOItem1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "PO Id", "Item Name", "Item Code", "Supplier" }));
        cmbSortPOItem1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbSortPOItem1ActionPerformed(evt);
            }
        });

        jLabel50.setIcon(new javax.swing.ImageIcon(getClass().getResource("/frames/img/search.icon.png"))); // NOI18N

        lblPOId4.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        lblPOId4.setForeground(new java.awt.Color(153, 153, 153));
        lblPOId4.setText("PO Id :");

        lblPOId2.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        lblPOId2.setForeground(new java.awt.Color(153, 153, 153));

        jLabel51.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel51.setForeground(new java.awt.Color(153, 153, 153));
        jLabel51.setText("|");

        lblTotal4.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        lblTotal4.setForeground(new java.awt.Color(153, 153, 153));
        lblTotal4.setText("PO Total : ");

        lblTotal3.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        lblTotal3.setForeground(new java.awt.Color(153, 153, 153));
        lblTotal3.setText("Rs. 0.0");

        javax.swing.GroupLayout jPanel15Layout = new javax.swing.GroupLayout(jPanel15);
        jPanel15.setLayout(jPanel15Layout);
        jPanel15Layout.setHorizontalGroup(
            jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel15Layout.createSequentialGroup()
                .addGap(52, 52, 52)
                .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel15Layout.createSequentialGroup()
                        .addComponent(lblPOId4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblPOId2, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel51, javax.swing.GroupLayout.PREFERRED_SIZE, 4, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblTotal4)
                        .addGap(0, 0, 0)
                        .addComponent(lblTotal3, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButton31)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jButton32)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jButton33)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jButton34))
                    .addComponent(jScrollPane5, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 1096, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel15Layout.createSequentialGroup()
                        .addComponent(txtSearchPOItem1, javax.swing.GroupLayout.DEFAULT_SIZE, 873, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(cmbSortPOItem1, 0, 173, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel50)))
                .addGap(52, 52, 52))
        );
        jPanel15Layout.setVerticalGroup(
            jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel15Layout.createSequentialGroup()
                .addGap(23, 23, 23)
                .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtSearchPOItem1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel50)
                    .addComponent(cmbSortPOItem1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane5, javax.swing.GroupLayout.DEFAULT_SIZE, 346, Short.MAX_VALUE)
                .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel15Layout.createSequentialGroup()
                        .addGap(31, 31, 31)
                        .addComponent(lblPOId4))
                    .addGroup(jPanel15Layout.createSequentialGroup()
                        .addGap(28, 28, 28)
                        .addComponent(lblPOId2, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel15Layout.createSequentialGroup()
                        .addGap(31, 31, 31)
                        .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lblTotal3)
                            .addComponent(lblTotal4)))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel15Layout.createSequentialGroup()
                        .addGap(25, 25, 25)
                        .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jButton32)
                                .addComponent(jButton33)
                                .addComponent(jButton34)
                                .addComponent(jButton31))
                            .addComponent(jLabel51, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(45, 45, 45))
        );

        jTabbedPaneAddExistingItems.addTab("View PO Item", jPanel15);

        jPanel23.setBackground(new java.awt.Color(34, 34, 34));

        txtSearchGRNItem1.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        txtSearchGRNItem1.setForeground(new java.awt.Color(255, 255, 255));
        txtSearchGRNItem1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtSearchGRNItem1KeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtSearchGRNItem1KeyTyped(evt);
            }
        });

        tblGRN1.setBackground(new java.awt.Color(34, 34, 34));
        tblGRN1.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        tblGRN1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "GRN Item Id", "Item Name", "Item Code", "Qunatity", "Item Price", "Supplier", "Status"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblGRN1.setGridColor(new java.awt.Color(61, 61, 61));
        tblGRN1.setShowGrid(true);
        tblGRN1.getTableHeader().setReorderingAllowed(false);
        tblGRN1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblGRN1MouseClicked(evt);
            }
        });
        jScrollPane4.setViewportView(tblGRN1);

        jButton22.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jButton22.setForeground(new java.awt.Color(255, 255, 255));
        jButton22.setText(" Refresh");
        jButton22.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jButton22.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton22ActionPerformed(evt);
            }
        });

        jButton25.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jButton25.setForeground(new java.awt.Color(255, 255, 255));
        jButton25.setText("Bad Order");
        jButton25.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jButton25.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton25ActionPerformed(evt);
            }
        });

        jButton26.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jButton26.setForeground(new java.awt.Color(255, 255, 255));
        jButton26.setText("Check");
        jButton26.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jButton26.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton26ActionPerformed(evt);
            }
        });

        cmbSortGRNItem1.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        cmbSortGRNItem1.setForeground(new java.awt.Color(255, 255, 255));
        cmbSortGRNItem1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "PO Id", "GRN Id", "Checked", "Unchecked", "Bad Order" }));
        cmbSortGRNItem1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbSortGRNItem1ActionPerformed(evt);
            }
        });

        jLabel35.setIcon(new javax.swing.ImageIcon(getClass().getResource("/frames/img/search.icon.png"))); // NOI18N

        lblPOId3.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        lblPOId3.setForeground(new java.awt.Color(153, 153, 153));
        lblPOId3.setText("GRN Id :");

        lblGRNId1.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        lblGRNId1.setForeground(new java.awt.Color(153, 153, 153));

        jLabel36.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel36.setForeground(new java.awt.Color(153, 153, 153));
        jLabel36.setText("|");

        lblTotal33.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        lblTotal33.setForeground(new java.awt.Color(153, 153, 153));
        lblTotal33.setText("GRN Total : ");

        lblGRNTotal1.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        lblGRNTotal1.setForeground(new java.awt.Color(153, 153, 153));
        lblGRNTotal1.setText("Rs. 0.0");

        jButton35.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jButton35.setForeground(new java.awt.Color(255, 255, 255));
        jButton35.setText("Print");
        jButton35.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jButton35.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton35ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel23Layout = new javax.swing.GroupLayout(jPanel23);
        jPanel23.setLayout(jPanel23Layout);
        jPanel23Layout.setHorizontalGroup(
            jPanel23Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel23Layout.createSequentialGroup()
                .addGap(52, 52, 52)
                .addGroup(jPanel23Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel23Layout.createSequentialGroup()
                        .addComponent(lblPOId3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblGRNId1, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel36, javax.swing.GroupLayout.PREFERRED_SIZE, 4, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblTotal33)
                        .addGap(0, 0, 0)
                        .addComponent(lblGRNTotal1, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButton22)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jButton25)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jButton35)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jButton26))
                    .addComponent(jScrollPane4, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 1096, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel23Layout.createSequentialGroup()
                        .addComponent(txtSearchGRNItem1, javax.swing.GroupLayout.DEFAULT_SIZE, 873, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(cmbSortGRNItem1, 0, 173, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel35)))
                .addGap(52, 52, 52))
        );
        jPanel23Layout.setVerticalGroup(
            jPanel23Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel23Layout.createSequentialGroup()
                .addGap(23, 23, 23)
                .addGroup(jPanel23Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtSearchGRNItem1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel35)
                    .addComponent(cmbSortGRNItem1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 346, Short.MAX_VALUE)
                .addGroup(jPanel23Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel23Layout.createSequentialGroup()
                        .addGap(31, 31, 31)
                        .addComponent(lblPOId3))
                    .addGroup(jPanel23Layout.createSequentialGroup()
                        .addGap(28, 28, 28)
                        .addComponent(lblGRNId1, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel23Layout.createSequentialGroup()
                        .addGap(31, 31, 31)
                        .addGroup(jPanel23Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lblGRNTotal1)
                            .addComponent(lblTotal33)))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel23Layout.createSequentialGroup()
                        .addGap(25, 25, 25)
                        .addGroup(jPanel23Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel23Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jButton25)
                                .addComponent(jButton26)
                                .addComponent(jButton22)
                                .addComponent(jButton35))
                            .addComponent(jLabel36, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(45, 45, 45))
        );

        jTabbedPaneAddExistingItems.addTab("View GRN", jPanel23);

        jPanel24.setBackground(new java.awt.Color(34, 34, 34));

        jLabel37.setBackground(new java.awt.Color(0, 0, 0));
        jLabel37.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel37.setForeground(new java.awt.Color(255, 255, 255));
        jLabel37.setText("Item Name");

        txtGRNItemName1.setBackground(new java.awt.Color(51, 51, 51));
        txtGRNItemName1.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        txtGRNItemName1.setEnabled(false);

        jLabel38.setBackground(new java.awt.Color(0, 0, 0));
        jLabel38.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel38.setForeground(new java.awt.Color(255, 255, 255));
        jLabel38.setText("GRN Status");

        jLabel39.setBackground(new java.awt.Color(0, 0, 0));
        jLabel39.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel39.setForeground(new java.awt.Color(255, 255, 255));
        jLabel39.setText("Quantity");

        jLabel40.setBackground(new java.awt.Color(0, 0, 0));
        jLabel40.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel40.setForeground(new java.awt.Color(255, 255, 255));
        jLabel40.setText("Item Price");

        jLabel41.setBackground(new java.awt.Color(0, 0, 0));
        jLabel41.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel41.setForeground(new java.awt.Color(255, 255, 255));
        jLabel41.setText("Selling Price");

        jButton20.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jButton20.setForeground(new java.awt.Color(255, 255, 255));
        jButton20.setText("Check and Add to Stock");
        jButton20.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jButton20.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton20ActionPerformed(evt);
            }
        });

        jButton27.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jButton27.setForeground(new java.awt.Color(255, 255, 255));
        jButton27.setText("View GRN");
        jButton27.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jButton27.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton27ActionPerformed(evt);
            }
        });

        jLabel42.setBackground(new java.awt.Color(0, 0, 0));
        jLabel42.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel42.setForeground(new java.awt.Color(255, 255, 255));
        jLabel42.setText("Low Stock");

        txtGRNItemSellingPrice1.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        txtGRNItemSellingPrice1.setForeground(new java.awt.Color(255, 255, 255));
        txtGRNItemSellingPrice1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtGRNItemSellingPrice1KeyPressed(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtGRNItemSellingPrice1KeyTyped(evt);
            }
        });

        jLabel43.setBackground(new java.awt.Color(0, 0, 0));
        jLabel43.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel43.setForeground(new java.awt.Color(255, 255, 255));
        jLabel43.setText("Available Stock");

        txtGRNStatus1.setBackground(new java.awt.Color(51, 51, 51));
        txtGRNStatus1.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        txtGRNStatus1.setEnabled(false);

        txtGRNItemQuantity1.setBackground(new java.awt.Color(51, 51, 51));
        txtGRNItemQuantity1.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        txtGRNItemQuantity1.setEnabled(false);

        txtGRNItemPrice1.setBackground(new java.awt.Color(51, 51, 51));
        txtGRNItemPrice1.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        txtGRNItemPrice1.setEnabled(false);

        txtGRNItemLowStock1.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        txtGRNItemLowStock1.setForeground(new java.awt.Color(255, 255, 255));
        txtGRNItemLowStock1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtGRNItemLowStock1KeyPressed(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtGRNItemLowStock1KeyTyped(evt);
            }
        });

        txtGRNItemStockCount1.setBackground(new java.awt.Color(51, 51, 51));
        txtGRNItemStockCount1.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        txtGRNItemStockCount1.setText("0");
        txtGRNItemStockCount1.setEnabled(false);

        javax.swing.GroupLayout jPanel24Layout = new javax.swing.GroupLayout(jPanel24);
        jPanel24.setLayout(jPanel24Layout);
        jPanel24Layout.setHorizontalGroup(
            jPanel24Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel24Layout.createSequentialGroup()
                .addGap(52, 52, 52)
                .addGroup(jPanel24Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel37)
                    .addComponent(jLabel38)
                    .addComponent(jLabel39)
                    .addComponent(jLabel40)
                    .addComponent(jLabel41)
                    .addComponent(jLabel42)
                    .addComponent(jLabel43))
                .addGap(51, 51, 51)
                .addGroup(jPanel24Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel24Layout.createSequentialGroup()
                        .addGroup(jPanel24Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel24Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addComponent(txtGRNItemSellingPrice1, javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(txtGRNItemName1)
                                .addComponent(txtGRNStatus1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 278, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(txtGRNItemLowStock1, javax.swing.GroupLayout.Alignment.LEADING))
                            .addComponent(txtGRNItemQuantity1, javax.swing.GroupLayout.PREFERRED_SIZE, 278, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtGRNItemStockCount1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 278, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(72, 72, 72)
                        .addGroup(jPanel24Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jButton27)
                            .addComponent(jButton20)))
                    .addComponent(txtGRNItemPrice1, javax.swing.GroupLayout.PREFERRED_SIZE, 278, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(448, Short.MAX_VALUE))
        );
        jPanel24Layout.setVerticalGroup(
            jPanel24Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel24Layout.createSequentialGroup()
                .addGap(45, 45, 45)
                .addGroup(jPanel24Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel37)
                    .addComponent(txtGRNItemName1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton20))
                .addGap(18, 18, 18)
                .addGroup(jPanel24Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel24Layout.createSequentialGroup()
                        .addGroup(jPanel24Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel38)
                            .addComponent(jButton27))
                        .addGap(14, 14, 14))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel24Layout.createSequentialGroup()
                        .addComponent(txtGRNStatus1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)))
                .addGroup(jPanel24Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel24Layout.createSequentialGroup()
                        .addGap(3, 3, 3)
                        .addComponent(jLabel39))
                    .addComponent(txtGRNItemQuantity1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel24Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel40)
                    .addComponent(txtGRNItemPrice1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel24Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel41)
                    .addComponent(txtGRNItemSellingPrice1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel24Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel42)
                    .addComponent(txtGRNItemLowStock1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel24Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel43)
                    .addComponent(txtGRNItemStockCount1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(173, Short.MAX_VALUE))
        );

        jTabbedPaneAddExistingItems.addTab("GRN Checking", jPanel24);

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPaneAddExistingItems)
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPaneAddExistingItems)
        );

        jTabbedPaneStockMgt.addTab("Re-Orders", jPanel6);

        jTabbedPaneStockReturn.setBackground(new java.awt.Color(34, 34, 34));
        jTabbedPaneStockReturn.setForeground(new java.awt.Color(255, 255, 255));
        jTabbedPaneStockReturn.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jTabbedPaneStockReturn.setOpaque(true);

        jPanel17.setBackground(new java.awt.Color(34, 34, 34));

        txtSearchAvailableStock.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        txtSearchAvailableStock.setForeground(new java.awt.Color(255, 255, 255));
        txtSearchAvailableStock.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtSearchAvailableStockKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtSearchAvailableStockKeyTyped(evt);
            }
        });

        tblAvailableStock.setBackground(new java.awt.Color(34, 34, 34));
        tblAvailableStock.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        tblAvailableStock.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "SKU Id", "Item Code", "Item Name", "Purchase Price", "Selling Price", "Supplier", "Stock Count", "Low Stock"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblAvailableStock.setGridColor(new java.awt.Color(61, 61, 61));
        tblAvailableStock.setShowGrid(true);
        tblAvailableStock.getTableHeader().setReorderingAllowed(false);
        tblAvailableStock.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblAvailableStockMouseClicked(evt);
            }
        });
        jScrollPane8.setViewportView(tblAvailableStock);

        jButton36.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jButton36.setForeground(new java.awt.Color(255, 255, 255));
        jButton36.setText(" Refresh");
        jButton36.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jButton36.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton36ActionPerformed(evt);
            }
        });

        jButton37.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jButton37.setForeground(new java.awt.Color(255, 255, 255));
        jButton37.setText("Return Item(s)");
        jButton37.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jButton37.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton37ActionPerformed(evt);
            }
        });

        jButton44.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jButton44.setForeground(new java.awt.Color(255, 255, 255));
        jButton44.setText("Delete");
        jButton44.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jButton44.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton44ActionPerformed(evt);
            }
        });

        jButton45.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jButton45.setForeground(new java.awt.Color(255, 255, 255));
        jButton45.setText("Print All");
        jButton45.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jButton45.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton45ActionPerformed(evt);
            }
        });

        cmbSortAvailableStock.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        cmbSortAvailableStock.setForeground(new java.awt.Color(255, 255, 255));
        cmbSortAvailableStock.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "SKU Id", "Item Code", "Item Name", "Supplier", "Selling Stock", "Whole Stock", "Low Stock" }));
        cmbSortAvailableStock.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbSortAvailableStockActionPerformed(evt);
            }
        });

        jLabel60.setIcon(new javax.swing.ImageIcon(getClass().getResource("/frames/img/search.icon.png"))); // NOI18N

        lblStockCount1.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        lblStockCount1.setForeground(new java.awt.Color(153, 153, 153));
        lblStockCount1.setText("SKU Count : ");

        javax.swing.GroupLayout jPanel17Layout = new javax.swing.GroupLayout(jPanel17);
        jPanel17.setLayout(jPanel17Layout);
        jPanel17Layout.setHorizontalGroup(
            jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel17Layout.createSequentialGroup()
                .addGap(52, 52, 52)
                .addGroup(jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel17Layout.createSequentialGroup()
                        .addComponent(lblStockCount1, javax.swing.GroupLayout.PREFERRED_SIZE, 488, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButton36)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jButton44)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jButton37)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jButton45))
                    .addComponent(jScrollPane8, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 1096, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel17Layout.createSequentialGroup()
                        .addComponent(txtSearchAvailableStock, javax.swing.GroupLayout.DEFAULT_SIZE, 873, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(cmbSortAvailableStock, 0, 173, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel60)))
                .addGap(52, 52, 52))
        );
        jPanel17Layout.setVerticalGroup(
            jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel17Layout.createSequentialGroup()
                .addGap(23, 23, 23)
                .addGroup(jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtSearchAvailableStock, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel60)
                    .addComponent(cmbSortAvailableStock, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane8, javax.swing.GroupLayout.DEFAULT_SIZE, 346, Short.MAX_VALUE)
                .addGroup(jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel17Layout.createSequentialGroup()
                        .addGap(31, 31, 31)
                        .addComponent(lblStockCount1))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel17Layout.createSequentialGroup()
                        .addGap(30, 30, 30)
                        .addGroup(jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jButton37)
                            .addComponent(jButton44)
                            .addComponent(jButton45)
                            .addComponent(jButton36))))
                .addGap(45, 45, 45))
        );

        jTabbedPaneStockReturn.addTab("Available Stock", jPanel17);

        jPanel28.setBackground(new java.awt.Color(34, 34, 34));

        jLabel64.setBackground(new java.awt.Color(0, 0, 0));
        jLabel64.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel64.setForeground(new java.awt.Color(255, 255, 255));
        jLabel64.setText("SKU Id");

        txtAvailableSKUId.setBackground(new java.awt.Color(51, 51, 51));
        txtAvailableSKUId.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        txtAvailableSKUId.setEnabled(false);

        jLabel65.setBackground(new java.awt.Color(0, 0, 0));
        jLabel65.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel65.setForeground(new java.awt.Color(255, 255, 255));
        jLabel65.setText("Item Code");

        jLabel66.setBackground(new java.awt.Color(0, 0, 0));
        jLabel66.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel66.setForeground(new java.awt.Color(255, 255, 255));
        jLabel66.setText("Item Name");

        jLabel67.setBackground(new java.awt.Color(0, 0, 0));
        jLabel67.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel67.setForeground(new java.awt.Color(255, 255, 255));
        jLabel67.setText("Supplier");

        jLabel68.setBackground(new java.awt.Color(0, 0, 0));
        jLabel68.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel68.setForeground(new java.awt.Color(255, 255, 255));
        jLabel68.setText("Stock Count");

        jButton49.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jButton49.setForeground(new java.awt.Color(255, 255, 255));
        jButton49.setText("Return to Stock / Print");
        jButton49.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jButton49.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton49ActionPerformed(evt);
            }
        });

        jLabel69.setBackground(new java.awt.Color(0, 0, 0));
        jLabel69.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel69.setForeground(new java.awt.Color(255, 255, 255));
        jLabel69.setText("Return Count");

        txtAvailableStockCount.setBackground(new java.awt.Color(51, 51, 51));
        txtAvailableStockCount.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        txtAvailableStockCount.setEnabled(false);

        txtAvailableItemCode.setBackground(new java.awt.Color(51, 51, 51));
        txtAvailableItemCode.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        txtAvailableItemCode.setEnabled(false);

        txtAvailableItemName.setBackground(new java.awt.Color(51, 51, 51));
        txtAvailableItemName.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        txtAvailableItemName.setEnabled(false);

        txtAvailableSupplier.setBackground(new java.awt.Color(51, 51, 51));
        txtAvailableSupplier.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        txtAvailableSupplier.setEnabled(false);

        txtReturnCount.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        txtReturnCount.setForeground(new java.awt.Color(255, 255, 255));
        txtReturnCount.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtReturnCountKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtReturnCountKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtReturnCountKeyTyped(evt);
            }
        });

        javax.swing.GroupLayout jPanel28Layout = new javax.swing.GroupLayout(jPanel28);
        jPanel28.setLayout(jPanel28Layout);
        jPanel28Layout.setHorizontalGroup(
            jPanel28Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel28Layout.createSequentialGroup()
                .addGap(52, 52, 52)
                .addGroup(jPanel28Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel64)
                    .addComponent(jLabel65)
                    .addComponent(jLabel66)
                    .addComponent(jLabel67)
                    .addComponent(jLabel68)
                    .addComponent(jLabel69))
                .addGap(63, 63, 63)
                .addGroup(jPanel28Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel28Layout.createSequentialGroup()
                        .addGroup(jPanel28Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel28Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addComponent(txtAvailableStockCount, javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(txtAvailableSKUId)
                                .addComponent(txtAvailableItemCode, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 278, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(txtReturnCount, javax.swing.GroupLayout.Alignment.LEADING))
                            .addComponent(txtAvailableItemName, javax.swing.GroupLayout.PREFERRED_SIZE, 278, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(72, 72, 72)
                        .addComponent(jButton49))
                    .addComponent(txtAvailableSupplier, javax.swing.GroupLayout.PREFERRED_SIZE, 278, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(458, Short.MAX_VALUE))
        );
        jPanel28Layout.setVerticalGroup(
            jPanel28Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel28Layout.createSequentialGroup()
                .addGap(45, 45, 45)
                .addGroup(jPanel28Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel64)
                    .addComponent(txtAvailableSKUId, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton49))
                .addGap(18, 18, 18)
                .addGroup(jPanel28Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel28Layout.createSequentialGroup()
                        .addComponent(jLabel65)
                        .addGap(21, 21, 21))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel28Layout.createSequentialGroup()
                        .addComponent(txtAvailableItemCode, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)))
                .addGroup(jPanel28Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel28Layout.createSequentialGroup()
                        .addGap(3, 3, 3)
                        .addComponent(jLabel66))
                    .addComponent(txtAvailableItemName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel28Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel67)
                    .addComponent(txtAvailableSupplier, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel28Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel68)
                    .addComponent(txtAvailableStockCount, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel28Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel69)
                    .addComponent(txtReturnCount, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(219, Short.MAX_VALUE))
        );

        jTabbedPaneStockReturn.addTab("Stock Return Details", jPanel28);

        jPanel27.setBackground(new java.awt.Color(34, 34, 34));

        txtSearchStockReturn.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        txtSearchStockReturn.setForeground(new java.awt.Color(255, 255, 255));
        txtSearchStockReturn.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtSearchStockReturnKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtSearchStockReturnKeyTyped(evt);
            }
        });

        tblStockReturn.setBackground(new java.awt.Color(34, 34, 34));
        tblStockReturn.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        tblStockReturn.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "SKU Id", "Item Code", "Item Name", "Supplier", "Return Count", "Date / Time"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblStockReturn.setGridColor(new java.awt.Color(61, 61, 61));
        tblStockReturn.setShowGrid(true);
        tblStockReturn.getTableHeader().setReorderingAllowed(false);
        jScrollPane9.setViewportView(tblStockReturn);

        jButton46.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jButton46.setForeground(new java.awt.Color(255, 255, 255));
        jButton46.setText(" Refresh");
        jButton46.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jButton46.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton46ActionPerformed(evt);
            }
        });

        jButton48.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jButton48.setForeground(new java.awt.Color(255, 255, 255));
        jButton48.setText("Print All");
        jButton48.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jButton48.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton48ActionPerformed(evt);
            }
        });

        cmbSortStockReturn.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        cmbSortStockReturn.setForeground(new java.awt.Color(255, 255, 255));
        cmbSortStockReturn.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "SKU Id", "Item Code", "Item Name", "Supplier" }));
        cmbSortStockReturn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbSortStockReturnActionPerformed(evt);
            }
        });

        jLabel62.setIcon(new javax.swing.ImageIcon(getClass().getResource("/frames/img/search.icon.png"))); // NOI18N

        lblStockReturnCount.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        lblStockReturnCount.setForeground(new java.awt.Color(153, 153, 153));
        lblStockReturnCount.setText("Return Count : ");

        javax.swing.GroupLayout jPanel27Layout = new javax.swing.GroupLayout(jPanel27);
        jPanel27.setLayout(jPanel27Layout);
        jPanel27Layout.setHorizontalGroup(
            jPanel27Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel27Layout.createSequentialGroup()
                .addGap(52, 52, 52)
                .addGroup(jPanel27Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel27Layout.createSequentialGroup()
                        .addComponent(lblStockReturnCount, javax.swing.GroupLayout.PREFERRED_SIZE, 524, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButton46)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jButton48))
                    .addComponent(jScrollPane9, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 1096, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel27Layout.createSequentialGroup()
                        .addComponent(txtSearchStockReturn, javax.swing.GroupLayout.DEFAULT_SIZE, 873, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(cmbSortStockReturn, 0, 173, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel62)))
                .addGap(52, 52, 52))
        );
        jPanel27Layout.setVerticalGroup(
            jPanel27Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel27Layout.createSequentialGroup()
                .addGap(23, 23, 23)
                .addGroup(jPanel27Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtSearchStockReturn, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel62)
                    .addComponent(cmbSortStockReturn, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane9, javax.swing.GroupLayout.DEFAULT_SIZE, 346, Short.MAX_VALUE)
                .addGroup(jPanel27Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel27Layout.createSequentialGroup()
                        .addGap(31, 31, 31)
                        .addComponent(lblStockReturnCount))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel27Layout.createSequentialGroup()
                        .addGap(30, 30, 30)
                        .addGroup(jPanel27Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jButton48)
                            .addComponent(jButton46))))
                .addGap(45, 45, 45))
        );

        jTabbedPaneStockReturn.addTab("Stock Return History", jPanel27);

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPaneStockReturn)
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPaneStockReturn)
        );

        jTabbedPaneStockMgt.addTab("Stock Returns", jPanel7);

        javax.swing.GroupLayout jPanel11Layout = new javax.swing.GroupLayout(jPanel11);
        jPanel11.setLayout(jPanel11Layout);
        jPanel11Layout.setHorizontalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPaneStockMgt)
        );
        jPanel11Layout.setVerticalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPaneStockMgt, javax.swing.GroupLayout.Alignment.TRAILING)
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

    private void txtSearchSupplierItemKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtSearchSupplierItemKeyReleased
        searchSupplierItem();
    }//GEN-LAST:event_txtSearchSupplierItemKeyReleased

    private void cmbSortSupplierItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbSortSupplierItemActionPerformed
        searchSupplierItem();
    }//GEN-LAST:event_cmbSortSupplierItemActionPerformed

    private void tblSupplierItemMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblSupplierItemMouseClicked
        txtSelectedItemSupplierId.setText((String) tblSupplierItem.getValueAt(tblSupplierItem.getSelectedRow(), 0));
        txtSelectedItemSupplierUsername.setText((String) tblSupplierItem.getValueAt(tblSupplierItem.getSelectedRow(), 1));
        txtSelectedItemSupplierItemName.setText((String) tblSupplierItem.getValueAt(tblSupplierItem.getSelectedRow(), 2));
        txtSelectedItemSupplierItemCode.setText((String) tblSupplierItem.getValueAt(tblSupplierItem.getSelectedRow(), 3));
        cmbSelectedItemSupplierItemCategory.setSelectedItem((String) tblSupplierItem.getValueAt(tblSupplierItem.getSelectedRow(), 4));

        try {
            ResultSet rs = DB.DB.getData("SELECT itm_id FROM item WHERE itm_name = '" + txtSelectedItemSupplierItemName.getText() + "'");
            if (rs.next()) {
                String i = rs.getString(1);
                itemId = (i);
            }
        } catch (Exception e) {
        }

    }//GEN-LAST:event_tblSupplierItemMouseClicked

    private void jButton7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton7ActionPerformed
        refreshSupplierItemTable();
        txtSearchSupplierItem.setText(null);
        txtSelectedItemSupplierId.setText(null);
        txtSelectedItemSupplierUsername.setText(null);
        txtSelectedItemSupplierItemName.setText(null);
        txtSelectedItemSupplierItemCode.setText(null);
        cmbSelectedItemSupplierItemCategory.setSelectedIndex(0);
    }//GEN-LAST:event_jButton7ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        if (tblSupplierItem.getSelectedRowCount() == 0) {
            JOptionPane.showMessageDialog(this, "No Supplier Selected!");
        } else {
            jTabbedPaneAddItem.setSelectedIndex(2);
            txtSelectedItemSupplierItemName.grabFocus();
        }
    }//GEN-LAST:event_jButton4ActionPerformed

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        if (tblSupplierItem.getSelectedRowCount() == 0) {
            JOptionPane.showMessageDialog(this, "No Supplier Selected!");

        } else {
            deleteSupplierItem();
        }
    }//GEN-LAST:event_jButton5ActionPerformed

    private void txtSelectedItemSupplierItemCodeKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtSelectedItemSupplierItemCodeKeyPressed
        if (evt.getKeyCode() == 10) {
            cmbSelectedItemSupplierItemCategory.setPopupVisible(true);
            cmbSelectedItemSupplierItemCategory.grabFocus();
        }
    }//GEN-LAST:event_txtSelectedItemSupplierItemCodeKeyPressed

    private void txtSelectedItemSupplierItemNameKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtSelectedItemSupplierItemNameKeyPressed
        if (evt.getKeyCode() == 10) {
            txtSelectedItemSupplierItemCode.grabFocus();
        }
    }//GEN-LAST:event_txtSelectedItemSupplierItemNameKeyPressed

    private void jButton8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton8ActionPerformed
        if (tblSupplierItem.getSelectedRowCount() == 0) {
            JOptionPane.showMessageDialog(this, "No Supplier Selected!");
            jTabbedPaneAddItem.setSelectedIndex(0);

        } else {
            updateSupplierItem();

            jPanel4.setVisible(false);
            jPanel4.setVisible(true);
        }
    }//GEN-LAST:event_jButton8ActionPerformed

    private void jButton9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton9ActionPerformed
        if (tblSupplierItem.getSelectedRowCount() == 0) {
            JOptionPane.showMessageDialog(this, "No Supplier Selected!");
            jTabbedPaneAddItem.setSelectedIndex(0);

        } else {

            deleteSupplierItem();
            jPanel4.setVisible(false);
            jPanel4.setVisible(true);
        }
    }//GEN-LAST:event_jButton9ActionPerformed

    private void txtItemCodeKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtItemCodeKeyPressed
        if (evt.getKeyCode() == 10) {
            cmbItemCategory.setPopupVisible(true);
            cmbItemCategory.grabFocus();
        }
    }//GEN-LAST:event_txtItemCodeKeyPressed

    private void txtItemNameKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtItemNameKeyPressed
        if (evt.getKeyCode() == 10) {
            txtItemCode.grabFocus();
        }
    }//GEN-LAST:event_txtItemNameKeyPressed

    private void txtItemNameKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtItemNameKeyTyped
        char c = evt.getKeyChar();
        if (!((Character.isDigit(c)) || (Character.isLetter(c)) || (Character.isISOControl(c)))) {
            evt.consume();
        }
    }//GEN-LAST:event_txtItemNameKeyTyped

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        addItem();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        jTabbedPaneAddItem.setSelectedIndex(1);
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        txtItemName.setText(null);
        txtItemCode.setText(null);
        cmbItemCategory.setSelectedIndex(0);
        cmbItemSupplier.setSelectedIndex(0);
    }//GEN-LAST:event_jButton3ActionPerformed

    private void txtPOItemQuantityKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtPOItemQuantityKeyPressed
        if (evt.getKeyCode() == 10) {
            txtPOItemPrice.grabFocus();
        }
    }//GEN-LAST:event_txtPOItemQuantityKeyPressed

    private void txtPOItemQuantityKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtPOItemQuantityKeyTyped
        char c = evt.getKeyChar();
        if (!(Character.isDigit(c))) {
            evt.consume();

        }
        String quantity = txtPOItemQuantity.getText();
        int length = quantity.length();
        if (length >= 6) {
            evt.consume();
        }
    }//GEN-LAST:event_txtPOItemQuantityKeyTyped

    private void jButton11ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton11ActionPerformed
        addPOItem();
    }//GEN-LAST:event_jButton11ActionPerformed

    private void jButton12ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton12ActionPerformed
        jTabbedPaneAddNewItems.setSelectedIndex(1);
        txtSearchPOItem.setText(txtPOId.getText());
        lblPOId.setText(txtPOId.getText());
        calculatePOTotal();
        searchPOItem();
        searchPOItem1();
    }//GEN-LAST:event_jButton12ActionPerformed

    private void txtPOItemPriceKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtPOItemPriceKeyPressed
        if (evt.getKeyCode() == 10) {
            cmbPOLeadTime.setPopupVisible(true);
            cmbPOLeadTime.grabFocus();
        }
    }//GEN-LAST:event_txtPOItemPriceKeyPressed

    private void txtPOItemPriceKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtPOItemPriceKeyTyped
        char c = evt.getKeyChar();
        if (!(Character.isDigit(c))) {
            evt.consume();

        }
        String price = txtPOItemPrice.getText();
        int length = price.length();
        if (length >= 7) {
            evt.consume();
        }
    }//GEN-LAST:event_txtPOItemPriceKeyTyped

    private void cmbItemCategoryKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cmbItemCategoryKeyPressed
        if (evt.getKeyCode() == 10) {
            cmbItemSupplier.setPopupVisible(true);
            cmbItemSupplier.grabFocus();
        }
    }//GEN-LAST:event_cmbItemCategoryKeyPressed

    private void txtSearchPOItemKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtSearchPOItemKeyReleased
        searchPOItem();

        switch (cmbSortPOItem.getSelectedIndex()) {
            case 0:

                if (txtSearchPOItem.getText().isEmpty()) {
                    lblPOId.setText(" * ");
                } else {
                    lblPOId.setText(txtSearchPOItem.getText());
                }
                break;
            default:
                lblPOId.setText(" * ");
        }
    }//GEN-LAST:event_txtSearchPOItemKeyReleased

    private void tblPOItemMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblPOItemMouseClicked
        selectedPOItemId = ((String) tblPOItem.getValueAt(tblPOItem.getSelectedRow(), 0));
    }//GEN-LAST:event_tblPOItemMouseClicked

    private void jButton14ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton14ActionPerformed
        refreshPOTable();
        txtSearchPOItem.setText(txtPOId.getText());
        cmbSortPOItem.setSelectedIndex(0);
        lblPOId.setText(txtPOId.getText());
        calculatePOTotal();
    }//GEN-LAST:event_jButton14ActionPerformed

    private void jButton15ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton15ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton15ActionPerformed

    private void jButton16ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton16ActionPerformed
        if (!(tblPOItem.getSelectedRowCount() == 0)) {
            deletePOItem();

        } else {
            JOptionPane.showMessageDialog(this, "No Item Selected!");
        }
    }//GEN-LAST:event_jButton16ActionPerformed

    private void cmbSortPOItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbSortPOItemActionPerformed
        searchPOItem();
    }//GEN-LAST:event_cmbSortPOItemActionPerformed

    private void cmbPOItemSupplierKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cmbPOItemSupplierKeyPressed
        if (evt.getKeyCode() == 10) {
            try {
                String selectedSupplier = cmbPOItemSupplier.getSelectedItem().toString();

                cmbPOItemName.removeAllItems();

                ResultSet rs = DB.DB.getData("SELECT itm_name FROM item WHERE itm_stat = '0' AND sup_username = '" + selectedSupplier + "'");

                while (rs.next()) {
                    cmbPOItemName.addItem(rs.getString(1));
                }
                DB.DB.con.close();
            } catch (Exception e) {
            }
            cmbPOItemName.setPopupVisible(true);
            cmbPOItemName.grabFocus();
        }
    }//GEN-LAST:event_cmbPOItemSupplierKeyPressed

    private void cmbPOItemSupplierActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbPOItemSupplierActionPerformed
        try {
            String selectedSupplier = cmbPOItemSupplier.getSelectedItem().toString();

            cmbPOItemName.removeAllItems();

            ResultSet rs = DB.DB.getData("SELECT itm_name FROM item WHERE itm_stat = '0' AND sup_username = '" + selectedSupplier + "'");

            while (rs.next()) {
                cmbPOItemName.addItem(rs.getString(1));
            }
            DB.DB.con.close();
        } catch (Exception e) {
        }
    }//GEN-LAST:event_cmbPOItemSupplierActionPerformed

    private void cmbPOItemNameKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cmbPOItemNameKeyPressed
        if (evt.getKeyCode() == 10) {
            txtPOItemQuantity.grabFocus();
        }
    }//GEN-LAST:event_cmbPOItemNameKeyPressed

    private void cmbPOLeadTimeKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cmbPOLeadTimeKeyPressed
        if (evt.getKeyCode() == 10) {
            addPOItem();
        }
    }//GEN-LAST:event_cmbPOLeadTimeKeyPressed

    private void txtSearchPOItemKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtSearchPOItemKeyTyped
        switch (cmbSortPOItem.getSelectedIndex()) {
            case 0:

                if (txtSearchPOItem.getText().isEmpty()) {
                    lblPOId.setText(" * ");
                    lblTotal.setText("Rs. *");

                } else {
                    lblPOId.setText(txtSearchPOItem.getText());
                    calculatePOTotal();

                }
                break;
            default:
                lblPOId.setText(" * ");
                lblTotal.setText("Rs. *");
        }
    }//GEN-LAST:event_txtSearchPOItemKeyTyped

    private void jButton17ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton17ActionPerformed
        if (!(tblPOItem.getRowCount() == 0)) {
            purchaseOrder();
        } else {
            JOptionPane.showMessageDialog(this, "No Items Found on PO Bill!");
        }
    }//GEN-LAST:event_jButton17ActionPerformed

    private void txtSearchGRNItemKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtSearchGRNItemKeyReleased
        searchGRNItem();

        switch (cmbSortGRNItem.getSelectedIndex()) {
            case 0:

                if (txtSearchGRNItem.getText().isEmpty()) {
                    lblGRNId.setText(" * ");

                } else {
                    lblGRNId.setText(txtSearchGRNItem.getText());
                }
                break;
            case 1:

                if (txtSearchGRNItem.getText().isEmpty()) {
                    lblGRNId.setText(" * ");

                } else {
                    lblGRNId.setText(txtSearchGRNItem.getText());
                }
                break;
            default:
                lblGRNId.setText(" * ");
        }
    }//GEN-LAST:event_txtSearchGRNItemKeyReleased

    private void txtSearchGRNItemKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtSearchGRNItemKeyTyped
        switch (cmbSortGRNItem.getSelectedIndex()) {
            case 0:

                if (txtSearchGRNItem.getText().isEmpty()) {
                    lblGRNId.setText(" * ");

                } else {
                    lblGRNId.setText(txtSearchGRNItem.getText());
                }
                break;
            case 1:

                if (txtSearchGRNItem.getText().isEmpty()) {
                    lblGRNId.setText(" * ");
                } else {

                    lblGRNId.setText(txtSearchGRNItem.getText());

                }
                break;
            default:
                lblGRNId.setText(" * ");
        }
    }//GEN-LAST:event_txtSearchGRNItemKeyTyped

    private void tblGRNMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblGRNMouseClicked
        txtGRNItemName.setText((String) tblGRN.getValueAt(tblGRN.getSelectedRow(), 1));
        txtGRNItemQuantity.setText((String) tblGRN.getValueAt(tblGRN.getSelectedRow(), 3));
        txtGRNItemPrice.setText((String) tblGRN.getValueAt(tblGRN.getSelectedRow(), 4));
        txtGRNStatus.setText((String) tblGRN.getValueAt(tblGRN.getSelectedRow(), 6));
    }//GEN-LAST:event_tblGRNMouseClicked

    private void jButton21ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton21ActionPerformed
        refreshGRNTable();
        txtSearchGRNItem.setText(txtPOId.getText());
        cmbSortGRNItem.setSelectedIndex(0);
        lblGRNId.setText(txtPOId.getText());
        calculateGRNTotal();
    }//GEN-LAST:event_jButton21ActionPerformed

    private void jButton23ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton23ActionPerformed
        if (tblGRN.getSelectedRowCount() == 0) {
            JOptionPane.showMessageDialog(this, "No GRN Item Selected!");

        } else {
            markAsBadOrder();
        }
    }//GEN-LAST:event_jButton23ActionPerformed

    private void jButton24ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton24ActionPerformed
        String selectedGRNItemStatus = (txtGRNStatus.getText());

        switch (selectedGRNItemStatus) {
            case "Checked":
                JOptionPane.showMessageDialog(this, "Item Already Added to Stock!");
                break;
            case "Bad Order":
                JOptionPane.showMessageDialog(this, "Bad Order Items cannot be Added to Stock!");
                break;
            default:
                if (tblGRN.getSelectedRowCount() == 0) {
                    JOptionPane.showMessageDialog(this, "No GRN Item Selected!");

                } else {
                    jTabbedPaneGRNChecking.setSelectedIndex(1);
                    txtGRNItemSellingPrice.grabFocus();
                }
                break;
        }
    }//GEN-LAST:event_jButton24ActionPerformed

    private void cmbSortGRNItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbSortGRNItemActionPerformed
        searchGRNItem();
    }//GEN-LAST:event_cmbSortGRNItemActionPerformed

    private void txtGRNItemSellingPriceKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtGRNItemSellingPriceKeyTyped
        char c = evt.getKeyChar();
        if (!(Character.isDigit(c))) {
            evt.consume();

        }
        String sellingPrice = txtGRNItemSellingPrice.getText();
        int length = sellingPrice.length();

        if (length >= 7) {
            evt.consume();
        }
    }//GEN-LAST:event_txtGRNItemSellingPriceKeyTyped

    private void txtGRNItemSellingPriceKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtGRNItemSellingPriceKeyPressed
        if (evt.getKeyCode() == 10) {
            txtGRNItemLowStock.grabFocus();
        }
    }//GEN-LAST:event_txtGRNItemSellingPriceKeyPressed

    private void jButton19ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton19ActionPerformed
        jTabbedPaneGRNChecking.setSelectedIndex(0);
    }//GEN-LAST:event_jButton19ActionPerformed

    private void jButton18ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton18ActionPerformed
        if ((tblGRN.getSelectedRowCount() == 0)) {
            JOptionPane.showMessageDialog(this, "No GRN Item Selected!");
            jTabbedPaneGRNChecking.setSelectedIndex(0);

        } else {
            addToStock();
        }
    }//GEN-LAST:event_jButton18ActionPerformed

    private void txtGRNItemLowStockKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtGRNItemLowStockKeyPressed
        if (evt.getKeyCode() == 10) {
            if ((tblGRN.getSelectedRowCount() == 0)) {
                JOptionPane.showMessageDialog(this, "No GRN Item Selected!");
                jTabbedPaneGRNChecking.setSelectedIndex(0);

            } else {
                addToStock();
            }
        }
    }//GEN-LAST:event_txtGRNItemLowStockKeyPressed

    private void txtGRNItemLowStockKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtGRNItemLowStockKeyTyped
        char c = evt.getKeyChar();
        if (!(Character.isDigit(c))) {
            evt.consume();

        }
        String lowStock = txtGRNItemLowStock.getText();
        int length = lowStock.length();
        if (length >= 5) {
            evt.consume();
        }
    }//GEN-LAST:event_txtGRNItemLowStockKeyTyped

    private void txtSearchGRNItem1KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtSearchGRNItem1KeyReleased
        searchGRNItem1();

        switch (cmbSortGRNItem1.getSelectedIndex()) {
            case 0:

                if (txtSearchGRNItem1.getText().isEmpty()) {
                    lblGRNId1.setText(" * ");
                } else {
                    lblGRNId1.setText(txtSearchGRNItem1.getText());
                }
                break;
            case 1:

                if (txtSearchGRNItem1.getText().isEmpty()) {
                    lblGRNId1.setText(" * ");
                } else {
                    lblGRNId1.setText(txtSearchGRNItem1.getText());
                }
                break;
            default:
                lblGRNId1.setText(" * ");
        }
    }//GEN-LAST:event_txtSearchGRNItem1KeyReleased

    private void txtSearchGRNItem1KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtSearchGRNItem1KeyTyped
        switch (cmbSortGRNItem1.getSelectedIndex()) {
            case 0:

                if (txtSearchGRNItem1.getText().isEmpty()) {
                    lblGRNId1.setText(" * ");
                } else {

                    lblGRNId1.setText(txtSearchGRNItem1.getText());
                }
                break;
            case 1:

                if (txtSearchGRNItem1.getText().isEmpty()) {
                    lblGRNId1.setText(" * ");
                } else {

                    lblGRNId1.setText(txtSearchGRNItem1.getText());

                }
                break;
            default:
                lblGRNId1.setText(" * ");
        }
    }//GEN-LAST:event_txtSearchGRNItem1KeyTyped

    private void tblGRN1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblGRN1MouseClicked
        txtGRNItemName1.setText((String) tblGRN1.getValueAt(tblGRN1.getSelectedRow(), 1));
        txtGRNItemQuantity1.setText((String) tblGRN1.getValueAt(tblGRN1.getSelectedRow(), 3));
        txtGRNItemPrice1.setText((String) tblGRN1.getValueAt(tblGRN1.getSelectedRow(), 4));
        txtGRNStatus1.setText((String) tblGRN1.getValueAt(tblGRN1.getSelectedRow(), 6));

        try {
            String selectedGRNItemName = txtGRNItemName1.getText();
            ResultSet rs = DB.DB.getData("SELECT stk_low_stock FROM stock WHERE itm_name = '" + selectedGRNItemName + "' ");

            if (rs.next()) {
                txtGRNItemLowStock1.setText(rs.getString(1));
            }
            DB.DB.con.close();

            ResultSet rs1 = DB.DB.getData("SELECT stk_count FROM stock WHERE itm_name = '" + selectedGRNItemName + "' ");

            if (rs1.next()) {
                txtGRNItemStockCount1.setText(rs1.getString(1));
            }
            DB.DB.con.close();

            ResultSet rs2 = DB.DB.getData("SELECT stk_selling_price FROM stock WHERE itm_name = '" + selectedGRNItemName + "' ");

            if (rs2.next()) {
                txtGRNItemSellingPrice1.setText(rs2.getString(1));
            }
            DB.DB.con.close();
        } catch (Exception e) {
        }
    }//GEN-LAST:event_tblGRN1MouseClicked

    private void jButton22ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton22ActionPerformed
        refreshGRNTable1();
        txtSearchGRNItem1.setText(txtPOId1.getText());
        cmbSortGRNItem1.setSelectedIndex(0);
        lblGRNId1.setText(txtPOId1.getText());
        calculateGRNTotal1();
    }//GEN-LAST:event_jButton22ActionPerformed

    private void jButton25ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton25ActionPerformed
        if (tblGRN1.getSelectedRowCount() == 0) {
            JOptionPane.showMessageDialog(this, "No GRN Item Selected!");

        } else {
            markAsBadOrder1();
        }
    }//GEN-LAST:event_jButton25ActionPerformed

    private void jButton26ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton26ActionPerformed
        String selectedGRNItemStatus = (txtGRNStatus1.getText());

        switch (selectedGRNItemStatus) {
            case "Checked":
                JOptionPane.showMessageDialog(this, "Item Already Added to Stock!");
                break;
            case "Bad Order":
                JOptionPane.showMessageDialog(this, "Bad Order Items cannot be Added to Stock!");
                break;
            default:
                if (tblGRN1.getSelectedRowCount() == 0) {
                    JOptionPane.showMessageDialog(this, "No GRN Item Selected!");

                } else {
                    jTabbedPaneAddExistingItems.setSelectedIndex(3);
                    txtGRNItemSellingPrice1.grabFocus();
                }
                break;
        }
    }//GEN-LAST:event_jButton26ActionPerformed

    private void cmbSortGRNItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbSortGRNItem1ActionPerformed
        searchGRNItem1();
    }//GEN-LAST:event_cmbSortGRNItem1ActionPerformed

    private void jButton20ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton20ActionPerformed
        if ((tblGRN1.getSelectedRowCount() == 0)) {
            JOptionPane.showMessageDialog(this, "No GRN Item Selected!");
            jTabbedPaneAddExistingItems.setSelectedIndex(2);

        } else {
            addReOrdersToStock();
        }
    }//GEN-LAST:event_jButton20ActionPerformed

    private void jButton27ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton27ActionPerformed
        jTabbedPaneAddExistingItems.setSelectedIndex(2);
    }//GEN-LAST:event_jButton27ActionPerformed

    private void txtGRNItemSellingPrice1KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtGRNItemSellingPrice1KeyPressed
        if (evt.getKeyCode() == 10) {
            txtGRNItemLowStock1.grabFocus();
        }
    }//GEN-LAST:event_txtGRNItemSellingPrice1KeyPressed

    private void txtGRNItemSellingPrice1KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtGRNItemSellingPrice1KeyTyped
        char c = evt.getKeyChar();
        if (!(Character.isDigit(c))) {
            evt.consume();
        }

        String sellingPrice = txtGRNItemSellingPrice1.getText();
        int length = sellingPrice.length();

        if (length >= 7) {
            evt.consume();
        }
    }//GEN-LAST:event_txtGRNItemSellingPrice1KeyTyped

    private void txtGRNItemLowStock1KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtGRNItemLowStock1KeyPressed
        if (evt.getKeyCode() == 10) {
            if ((tblGRN1.getSelectedRowCount() == 0)) {
                JOptionPane.showMessageDialog(this, "No GRN Item Selected!");
                jTabbedPaneAddExistingItems.setSelectedIndex(2);

            } else {
                addReOrdersToStock();
            }
        }
    }//GEN-LAST:event_txtGRNItemLowStock1KeyPressed

    private void txtGRNItemLowStock1KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtGRNItemLowStock1KeyTyped
        char c = evt.getKeyChar();
        if (!(Character.isDigit(c))) {
            evt.consume();
        }

        String sellingPrice = txtGRNItemLowStock1.getText();
        int length = sellingPrice.length();

        if (length >= 6) {
            evt.consume();
        }
    }//GEN-LAST:event_txtGRNItemLowStock1KeyTyped

    private void txtPOItemQuantity1KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtPOItemQuantity1KeyPressed
        if (evt.getKeyCode() == 10) {
            txtPOItemPrice1.grabFocus();
        }
    }//GEN-LAST:event_txtPOItemQuantity1KeyPressed

    private void txtPOItemQuantity1KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtPOItemQuantity1KeyTyped
        char c = evt.getKeyChar();
        if (!(Character.isDigit(c))) {
            evt.consume();

        }
        String quantity = txtPOItemQuantity1.getText();
        int length = quantity.length();
        if (length >= 6) {
            evt.consume();
        }
    }//GEN-LAST:event_txtPOItemQuantity1KeyTyped

    private void jButton28ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton28ActionPerformed
        addPOItem1();
    }//GEN-LAST:event_jButton28ActionPerformed

    private void jButton29ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton29ActionPerformed
        jTabbedPaneAddExistingItems.setSelectedIndex(1);
    }//GEN-LAST:event_jButton29ActionPerformed

    private void cmbPOLeadTime1KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cmbPOLeadTime1KeyPressed
        if (evt.getKeyCode() == 10) {
            addPOItem1();
        }
    }//GEN-LAST:event_cmbPOLeadTime1KeyPressed

    private void cmbPOItemSupplier1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbPOItemSupplier1ActionPerformed
        try {
            String selectedSupplier = cmbPOItemSupplier1.getSelectedItem().toString();

            cmbPOItemName1.removeAllItems();

            ResultSet rs = DB.DB.getData("SELECT itm_name FROM item WHERE itm_stat = '1' AND sup_username = '" + selectedSupplier + "'");

            while (rs.next()) {
                cmbPOItemName1.addItem(rs.getString(1));
            }
            DB.DB.con.close();
        } catch (Exception e) {
        }
    }//GEN-LAST:event_cmbPOItemSupplier1ActionPerformed

    private void cmbPOItemSupplier1KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cmbPOItemSupplier1KeyPressed
        if (evt.getKeyCode() == 10) {
            try {
                String selectedSupplier = cmbPOItemSupplier1.getSelectedItem().toString();

                cmbPOItemName1.removeAllItems();

                ResultSet rs = DB.DB.getData("SELECT itm_name FROM item WHERE itm_stat = '1' AND sup_username = '" + selectedSupplier + "'");

                while (rs.next()) {
                    cmbPOItemName1.addItem(rs.getString(1));
                }
                DB.DB.con.close();
            } catch (Exception e) {
            }

            cmbPOItemName1.setPopupVisible(true);
            cmbPOItemName1.grabFocus();
        }
    }//GEN-LAST:event_cmbPOItemSupplier1KeyPressed

    private void txtPOItemPrice1KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtPOItemPrice1KeyPressed
        if (evt.getKeyCode() == 10) {
            cmbPOLeadTime1.setPopupVisible(true);
            cmbPOLeadTime1.grabFocus();
        }
    }//GEN-LAST:event_txtPOItemPrice1KeyPressed

    private void txtPOItemPrice1KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtPOItemPrice1KeyTyped
        char c = evt.getKeyChar();
        if (!(Character.isDigit(c))) {
            evt.consume();

        }
        String price = txtPOItemPrice1.getText();
        int length = price.length();
        if (length >= 7) {
            evt.consume();
        }
    }//GEN-LAST:event_txtPOItemPrice1KeyTyped

    private void txtSearchPOItem1KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtSearchPOItem1KeyReleased
        searchPOItem1();

        switch (cmbSortPOItem1.getSelectedIndex()) {
            case 0:

                if (txtSearchPOItem1.getText().isEmpty()) {
                    lblPOId2.setText(" * ");
                } else {
                    lblPOId2.setText(txtSearchPOItem1.getText());
                }
                break;
            default:
                lblPOId2.setText(" * ");
        }
    }//GEN-LAST:event_txtSearchPOItem1KeyReleased

    private void txtSearchPOItem1KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtSearchPOItem1KeyTyped
        switch (cmbSortPOItem1.getSelectedIndex()) {
            case 0:

                if (txtSearchPOItem1.getText().isEmpty()) {
                    lblPOId2.setText(" * ");
                    lblTotal3.setText("Rs. *");
                } else {

                    lblPOId2.setText(txtSearchPOItem1.getText());
                    calculatePOTotal();

                }
                break;
            default:
                lblPOId2.setText(" * ");
                lblTotal3.setText("Rs. *");
        }
    }//GEN-LAST:event_txtSearchPOItem1KeyTyped

    private void tblPOItem1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblPOItem1MouseClicked
        selectedPOItemId = ((String) tblPOItem1.getValueAt(tblPOItem1.getSelectedRow(), 0));
    }//GEN-LAST:event_tblPOItem1MouseClicked

    private void jButton31ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton31ActionPerformed
        refreshPOTable1();
        txtSearchPOItem1.setText(txtPOId1.getText());
        cmbSortPOItem1.setSelectedIndex(0);
        lblPOId2.setText(txtPOId1.getText());
        calculatePOTotal();
    }//GEN-LAST:event_jButton31ActionPerformed

    private void jButton32ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton32ActionPerformed
        jTabbedPaneAddExistingItems.setSelectedIndex(0);
    }//GEN-LAST:event_jButton32ActionPerformed

    private void jButton33ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton33ActionPerformed
        if (!(tblPOItem1.getSelectedRowCount() == 0)) {
            deletePOItem1();
        } else {
            JOptionPane.showMessageDialog(this, "No Item Selected!");
        }
    }//GEN-LAST:event_jButton33ActionPerformed

    private void jButton34ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton34ActionPerformed
        if (!(tblPOItem1.getRowCount() == 0)) {
            purchaseOrder1();
        } else {
            JOptionPane.showMessageDialog(this, "No Items Found on PO Bill!");
        }
    }//GEN-LAST:event_jButton34ActionPerformed

    private void cmbSortPOItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbSortPOItem1ActionPerformed
        searchPOItem1();
    }//GEN-LAST:event_cmbSortPOItem1ActionPerformed

    private void txtSearchStockKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtSearchStockKeyReleased
        searchStock();
    }//GEN-LAST:event_txtSearchStockKeyReleased

    private void txtSearchStockKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtSearchStockKeyTyped
        calculateStockCount();
    }//GEN-LAST:event_txtSearchStockKeyTyped

    private void tblStockMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblStockMouseClicked
        try {

            String reorderSupplier = ((String) tblStock.getValueAt(tblStock.getSelectedRow(), 5));
            cmbPOItemSupplier1.setSelectedItem(reorderSupplier);
            String reOrderItemName = ((String) tblStock.getValueAt(tblStock.getSelectedRow(), 2));
            ResultSet rs = DB.DB.getData("SELECT stk_itm_price FROM stock WHERE itm_name = '" + reOrderItemName + "' ");

            if (rs.next()) {
                cmbPOItemName1.setSelectedItem(reOrderItemName);
                txtPOItemPrice1.setText(rs.getString(1));

            }
            DB.DB.con.close();

        } catch (Exception e) {
        }
    }//GEN-LAST:event_tblStockMouseClicked

    private void cmbSortStockActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbSortStockActionPerformed
        searchStock();
    }//GEN-LAST:event_cmbSortStockActionPerformed

    private void txtSearchLowStockKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtSearchLowStockKeyReleased
        searchLowStock();
    }//GEN-LAST:event_txtSearchLowStockKeyReleased

    private void txtSearchLowStockKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtSearchLowStockKeyTyped
        calculateLowStockCount();
    }//GEN-LAST:event_txtSearchLowStockKeyTyped

    private void tblLowStockMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblLowStockMouseClicked
        try {

            String reorderSupplier = ((String) tblLowStock.getValueAt(tblLowStock.getSelectedRow(), 5));
            cmbPOItemSupplier1.setSelectedItem(reorderSupplier);

            String reOrderItemName = ((String) tblLowStock.getValueAt(tblLowStock.getSelectedRow(), 2));

            ResultSet rs = DB.DB.getData("SELECT stk_itm_price FROM stock WHERE itm_name = '" + reOrderItemName + "' ");

            if (rs.next()) {
                cmbPOItemName1.setSelectedItem(reOrderItemName);
                txtPOItemPrice1.setText(rs.getString(1));

            }
            DB.DB.con.close();

        } catch (Exception e) {
        }
    }//GEN-LAST:event_tblLowStockMouseClicked

    private void jButton38ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton38ActionPerformed
        refreshStockTable();
    }//GEN-LAST:event_jButton38ActionPerformed

    private void jButton39ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton39ActionPerformed
        if (tblStock.getSelectedRowCount() == 0) {
            JOptionPane.showMessageDialog(this, "No Stock Item Selected!");
        } else {

            jTabbedPaneStockMgt.setSelectedIndex(4);
            jTabbedPaneAddExistingItems.setSelectedIndex(0);
            txtPOItemQuantity1.grabFocus();

        }
    }//GEN-LAST:event_jButton39ActionPerformed

    private void jButton40ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton40ActionPerformed
        printBarcodes();
    }//GEN-LAST:event_jButton40ActionPerformed

    private void cmbSortLowStockActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbSortLowStockActionPerformed
        searchLowStock();
    }//GEN-LAST:event_cmbSortLowStockActionPerformed

    private void jButton41ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton41ActionPerformed
        printStock();
    }//GEN-LAST:event_jButton41ActionPerformed

    private void jButton42ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton42ActionPerformed
        if (tblLowStock.getSelectedRowCount() == 0) {
            JOptionPane.showMessageDialog(this, "No Re-order Item Selected!");
        } else {

            jTabbedPaneStockMgt.setSelectedIndex(4);
            jTabbedPaneAddExistingItems.setSelectedIndex(0);
            txtPOItemQuantity1.grabFocus();
        }
    }//GEN-LAST:event_jButton42ActionPerformed

    private void jButton43ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton43ActionPerformed
        printLowStock();
    }//GEN-LAST:event_jButton43ActionPerformed

    private void cmbPOItemName1KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cmbPOItemName1KeyPressed
        if (evt.getKeyCode() == 10) {
            txtPOItemQuantity1.grabFocus();
        }
    }//GEN-LAST:event_cmbPOItemName1KeyPressed

    private void cmbItemSupplierKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cmbItemSupplierKeyPressed
        if (evt.getKeyCode() == 10) {
            addItem();
        }
    }//GEN-LAST:event_cmbItemSupplierKeyPressed

    private void cmbSelectedItemSupplierItemCategoryKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cmbSelectedItemSupplierItemCategoryKeyPressed
        if (evt.getKeyCode() == 10) {
            if (tblSupplierItem.getSelectedRowCount() == 0) {
                JOptionPane.showMessageDialog(this, "No Item Selected!");
                jTabbedPaneAddItem.setSelectedIndex(1);

            } else {
                updateSupplierItem();
            }
        }
    }//GEN-LAST:event_cmbSelectedItemSupplierItemCategoryKeyPressed

    private void txtSearchAvailableStockKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtSearchAvailableStockKeyReleased
        searchAvailableStock();
    }//GEN-LAST:event_txtSearchAvailableStockKeyReleased

    private void txtSearchAvailableStockKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtSearchAvailableStockKeyTyped
        calculateStockCount();
    }//GEN-LAST:event_txtSearchAvailableStockKeyTyped

    private void tblAvailableStockMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblAvailableStockMouseClicked
        try {

            txtAvailableSKUId.setText((String) tblAvailableStock.getValueAt(tblAvailableStock.getSelectedRow(), 0));
            txtAvailableItemCode.setText((String) tblAvailableStock.getValueAt(tblAvailableStock.getSelectedRow(), 1));
            txtAvailableItemName.setText((String) tblAvailableStock.getValueAt(tblAvailableStock.getSelectedRow(), 2));
            txtAvailableSupplier.setText((String) tblAvailableStock.getValueAt(tblAvailableStock.getSelectedRow(), 5));
            txtAvailableStockCount.setText((String) tblAvailableStock.getValueAt(tblAvailableStock.getSelectedRow(), 6));

            DB.DB.con.close();

        } catch (SQLException e) {
        }
    }//GEN-LAST:event_tblAvailableStockMouseClicked

    private void jButton36ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton36ActionPerformed
        refreshAvailableStockTable();

        txtAvailableSKUId.setText(null);
        txtAvailableItemCode.setText(null);
        txtAvailableItemName.setText(null);
        txtAvailableSupplier.setText(null);
        txtAvailableStockCount.setText(null);
        txtReturnCount.setText(null);
    }//GEN-LAST:event_jButton36ActionPerformed

    private void jButton37ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton37ActionPerformed
        if (tblAvailableStock.getSelectedRowCount() == 0) {
            JOptionPane.showMessageDialog(this, "No Stock Item Selected!");
        } else {

            jTabbedPaneStockReturn.setSelectedIndex(1);
            txtReturnCount.setText("");
            txtReturnCount.grabFocus();

        }
    }//GEN-LAST:event_jButton37ActionPerformed

    private void jButton44ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton44ActionPerformed
        if (tblAvailableStock.getSelectedRowCount() == 0) {
            JOptionPane.showMessageDialog(this, "No Stock Item Selected!");
        } else {

            deleteStockItem();

        }
    }//GEN-LAST:event_jButton44ActionPerformed

    private void jButton45ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton45ActionPerformed
        printStock();
    }//GEN-LAST:event_jButton45ActionPerformed

    private void cmbSortAvailableStockActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbSortAvailableStockActionPerformed
        searchAvailableStock();
    }//GEN-LAST:event_cmbSortAvailableStockActionPerformed

    private void txtSearchStockReturnKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtSearchStockReturnKeyReleased
        searchStockReturn();
    }//GEN-LAST:event_txtSearchStockReturnKeyReleased

    private void txtSearchStockReturnKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtSearchStockReturnKeyTyped
        calculateStockReturnCount();
    }//GEN-LAST:event_txtSearchStockReturnKeyTyped

    private void jButton46ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton46ActionPerformed
        refreshStockReturnTable();
    }//GEN-LAST:event_jButton46ActionPerformed

    private void jButton48ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton48ActionPerformed
        printStockReturn();
    }//GEN-LAST:event_jButton48ActionPerformed

    private void cmbSortStockReturnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbSortStockReturnActionPerformed
        searchStockReturn();
    }//GEN-LAST:event_cmbSortStockReturnActionPerformed

    private void jButton49ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton49ActionPerformed
        returnStockItem();
    }//GEN-LAST:event_jButton49ActionPerformed

    private void txtReturnCountKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtReturnCountKeyPressed
        if (evt.getKeyCode() == 10) {
            returnStockItem();
        }
    }//GEN-LAST:event_txtReturnCountKeyPressed

    private void txtReturnCountKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtReturnCountKeyTyped
        char c = evt.getKeyChar();
        if (!(Character.isDigit(c))) {
            evt.consume();

        }
        String returnCount = txtReturnCount.getText();
        int length = returnCount.length();
        if (length >= 5) {
            evt.consume();
        }
    }//GEN-LAST:event_txtReturnCountKeyTyped

    private void txtReturnCountKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtReturnCountKeyReleased
        try {

            int amountTyped = Integer.parseInt(txtReturnCount.getText());
            int availableStockCount = Integer.parseInt(txtAvailableStockCount.getText());
            if (amountTyped > availableStockCount) {
                JOptionPane.showMessageDialog(this, "No Sufficient Stock Count Available!", "Invalid Data", JOptionPane.WARNING_MESSAGE);
                txtReturnCount.setText(null);
            }
        } catch (HeadlessException | NumberFormatException e) {
        }
    }//GEN-LAST:event_txtReturnCountKeyReleased

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

            AdminStock.this.dispose();

            Login login = new Login();
            login.setVisible(true);
            for (float j = 0f; j < 0.985; j += 0.000001) {
                login.setOpacity(j);
            }
        } else {

        }
    }//GEN-LAST:event_jLabel4MouseClicked

    private void jButton30ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton30ActionPerformed
        printSelectedGRNBill();
    }//GEN-LAST:event_jButton30ActionPerformed

    private void jButton35ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton35ActionPerformed
        printSelectedGRNBill1();
    }//GEN-LAST:event_jButton35ActionPerformed

    private void fillCmbSuppliers() {
        try {

            cmbPOItemSupplier.removeAllItems();
            cmbPOItemSupplier1.removeAllItems();
            cmbItemSupplier.removeAllItems();
            ResultSet rs = DB.DB.getData("SELECT sup_username FROM supplier WHERE sup_stat = '1' ");
            while (rs.next()) {
                cmbPOItemSupplier.addItem(rs.getString(1));
                cmbPOItemSupplier1.addItem(rs.getString(1));
                cmbItemSupplier.addItem(rs.getString(1));
            }

        } catch (Exception e) {
        }
    }

    private void fillCmbItems() {
        try {

            cmbPOItemName.removeAllItems();
            ResultSet rs = DB.DB.getData("SELECT itm_name FROM item WHERE itm_stat = '1' ");
            while (rs.next()) {
                cmbPOItemName.addItem(rs.getString(1));
            }
            DB.DB.con.close();

        } catch (Exception e) {
        }
    }

    private void addItem() {
        int option = JOptionPane.showConfirmDialog(this, "Do you want to Add this Item to the System?", "", JOptionPane.YES_NO_OPTION);
        if (option == 0) {
            if (!txtItemName.getText().equals("")
                    & (!txtItemCode.getText().equals(""))) {

                try {
                    ResultSet rs = DB.DB.getData("SELECT * FROM item WHERE itm_name like '" + txtItemName.getText() + "'");

                    if (rs.next()) {
                        JOptionPane.showMessageDialog(this, "Item Name Already Exists!", "Invalid Item Name", JOptionPane.WARNING_MESSAGE);
                    }

                    ResultSet rs1 = DB.DB.getData("SELECT * FROM item WHERE itm_code like '" + txtItemCode.getText() + "'");
                    if (rs1.next()) {
                        JOptionPane.showMessageDialog(this, "Item Code Already Exists!", "Invalid Item Code", JOptionPane.WARNING_MESSAGE);

                    } else {

                        String addItem = "INSERT INTO item(itm_name, itm_code, itm_category, sup_username, sup_id, itm_stat) VALUES ('" + txtItemName.getText() + "','" + txtItemCode.getText() + "','" + cmbItemCategory.getSelectedItem().toString() + "','" + cmbItemSupplier.getSelectedItem().toString() + "',(SELECT sup_id FROM supplier WHERE sup_username= '" + cmbItemSupplier.getSelectedItem().toString() + "' ),'0')";
                        String AddItemActivityLog = "INSERT INTO log(usr_id, usr_type, usr_username, log_activity, log_description, log_state) VALUES ((SELECT usr_id FROM user WHERE usr_username = '" + lblAdminUsername.getText() + "'),'Admin','" + lblAdminUsername.getText() + "','Add Item to Supplier Item Collection','Admin added new Item " + txtItemName.getText() + "','SUCCESS')";

                        try {
                            DB.DB.putData(addItem);
                            fillCmbItems();
                            refreshSupplierItemTable();
                            DB.DB.putData(AddItemActivityLog);
                            refreshSupplierItemTable();
                            ImageIcon icon = new ImageIcon(getClass().getResource("img/done.png"));
                            JOptionPane.showMessageDialog(this, "Item Added Successfully!", "Done", JOptionPane.INFORMATION_MESSAGE, icon);

                            txtItemName.setText(null);
                            txtItemCode.setText(null);
                            cmbItemCategory.setSelectedIndex(0);
                            cmbItemSupplier.setSelectedIndex(0);
                            txtItemName.setBorder(new FlatBorder());
                            txtItemCode.setBorder(new FlatBorder());
                            
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

    private void calculateSupplierItemsCount() {
        int count = tblSupplierItem.getRowCount();
        lblItemCount.setText("Item Count : " + Integer.toString(count));
    }

    private void refreshSupplierItemTable() {

        try {

            DefaultTableModel dtm = (DefaultTableModel) tblSupplierItem.getModel();
            ResultSet rs = DB.DB.getData("SELECT * FROM item WHERE itm_stat = '1' order by sup_id");
            dtm.setRowCount(0);

            while (rs.next()) {
                String a = (rs.getString(6));
                String b = (rs.getString(5));
                String c = (rs.getString(2));
                String d = (rs.getString(3));
                String e = (rs.getString(4));
                String f = (rs.getString(7));
                dtm.addRow(new Object[]{a, b, c, d, e, f});
            }

            calculateSupplierItemsCount();

        } catch (Exception e) {
        }
    }

    private void updateSupplierItem() {
        int option = JOptionPane.showConfirmDialog(this, "Do you want to Update this Item?", "", JOptionPane.YES_NO_OPTION);
        if (option == 0) {

            if (!txtSelectedItemSupplierItemName.getText().equals("")
                    & !txtSelectedItemSupplierItemCode.getText().equals("")) {

                try {

                    ResultSet stockAvailabilityChecking = DB.DB.getData("SELECT itm_id FROM item WHERE itm_name = '" + txtSelectedItemSupplierItemName.getText() + "' AND itm_stat = '0'");
                    if (stockAvailabilityChecking.next()) {

                        ResultSet rs = DB.DB.getData("SELECT * FROM item WHERE itm_name = '" + txtSelectedItemSupplierItemName.getText() + "' AND itm_id != '" + itemId + "' ");
                        ResultSet rs1 = DB.DB.getData("SELECT * FROM item WHERE itm_code = '" + txtSelectedItemSupplierItemCode.getText() + "' AND itm_id != '" + itemId + "' ");

                        if (rs.next()) {
                            JOptionPane.showMessageDialog(this, "Item Name Already Used!", "Invalid Item Name", JOptionPane.WARNING_MESSAGE);
                        }

                        if (rs1.next()) {
                            JOptionPane.showMessageDialog(this, "Item Code Already Used!", "Invalid Item Code", JOptionPane.WARNING_MESSAGE);

                        } else {

                            try {

                                DB.DB.putData("UPDATE item SET itm_name ='" + txtSelectedItemSupplierItemName.getText() + "', itm_code ='" + txtSelectedItemSupplierItemCode.getText() + "', itm_category ='" + cmbSelectedItemSupplierItemCategory.getSelectedItem().toString() + "' WHERE itm_id = '" + itemId + "'");
                                String updateEmployeeActivityLog = "INSERT INTO log(usr_id, usr_type, usr_username, log_activity, log_description, log_state) VALUES ((SELECT usr_id FROM user WHERE usr_username = '" + lblAdminUsername.getText() + "'),'Admin','" + lblAdminUsername.getText() + "','Update Supplier Item','Admin updated supplier item " + txtSelectedItemSupplierItemName.getText() + "','SUCCESS')";
                                DB.DB.putData(updateEmployeeActivityLog);

                                ImageIcon icon = new ImageIcon(getClass().getResource("img/done.png"));
                                JOptionPane.showMessageDialog(this, "Item Updated Successfully!", "Done", JOptionPane.INFORMATION_MESSAGE, icon);

                                txtSelectedItemSupplierId.setText("");
                                txtSelectedItemSupplierUsername.setText("");
                                txtSelectedItemSupplierItemName.setText("");
                                txtSelectedItemSupplierItemCode.setText("");
                                cmbSelectedItemSupplierItemCategory.setSelectedIndex(0);
                                refreshSupplierItemTable();

                            } catch (Exception e) {
                            }
                        }

                    } else {
                        JOptionPane.showMessageDialog(this, "Available Items Cannot be Modified!", "Invalid Data", JOptionPane.WARNING_MESSAGE);
                    }

                } catch (Exception ex) {
                }
            } else {
                JOptionPane.showMessageDialog(this, "Please Fill All the Fields!", "Invalid Data", JOptionPane.WARNING_MESSAGE);
            }
        }
    }

    private void deleteSupplierItem() {
        int option = JOptionPane.showConfirmDialog(this, "Do you want to Delete this Item?", "", JOptionPane.YES_NO_OPTION);
        if (option == 0) {
            try {

                ResultSet stockAvailabilityChecking = DB.DB.getData("SELECT itm_id FROM item WHERE itm_name = '" + txtSelectedItemSupplierItemName.getText() + "' AND itm_stat = '0'");

                if (stockAvailabilityChecking.next()) {

                    ResultSet rs = DB.DB.getData("SELECT itm_id FROM item WHERE itm_name = '" + txtSelectedItemSupplierItemName.getText() + "'");
                    if (rs.next()) {
                        String i = rs.getString(1);
                        String supplierItemId = i;

                        DB.DB.putData("Delete FROM item WHERE itm_id = '" + supplierItemId + "' AND itm_stat = '0' ");
                    }

                    String deleteSupplierItemLog = "INSERT INTO log(usr_id, usr_type, usr_username, log_activity, log_description, log_state) VALUES ((SELECT usr_id FROM user WHERE usr_username = '" + lblAdminUsername.getText() + "'),'Admin','" + lblAdminUsername.getText() + "','Delete Supplier Item','Admin deleted Item " + txtSelectedItemSupplierItemName.getText() + "','SUCCESS')";
                    DB.DB.putData(deleteSupplierItemLog);

                    ImageIcon icon = new ImageIcon(getClass().getResource("img/done.png"));
                    JOptionPane.showMessageDialog(this, "Item Deleted!", "Done", JOptionPane.INFORMATION_MESSAGE, icon);

                    refreshSupplierItemTable();
                    fillCmbItems();
                    calculateSupplierItemsCount();

                } else {
                    JOptionPane.showMessageDialog(this, "Available Items Cannot be Modified!", "Invalid Data", JOptionPane.WARNING_MESSAGE);
                }

            } catch (Exception e) {
            }
        } else {

        }
    }

    private void searchSupplierItem() {
        switch (cmbSortSupplierItem.getSelectedIndex()) {
            case 0:

                try {
                DefaultTableModel dtm = (DefaultTableModel) tblSupplierItem.getModel();
                ResultSet rs = DB.DB.getData("SELECT * FROM item WHERE itm_stat = '1' AND sup_id like '" + txtSearchSupplierItem.getText() + "' order by sup_id ");
                dtm.setRowCount(0);

                while (rs.next()) {
                    String a = (rs.getString(6));
                    String b = (rs.getString(5));
                    String c = (rs.getString(2));
                    String d = (rs.getString(3));
                    String e = (rs.getString(4));
                    String f = (rs.getString(7));
                    dtm.addRow(new Object[]{a, b, c, d, e, f});
                }
                calculateSupplierItemsCount();

            } catch (Exception e) {
            }
            break;
            case 1:

                try {
                DefaultTableModel dtm = (DefaultTableModel) tblSupplierItem.getModel();
                ResultSet rs = DB.DB.getData("SELECT * FROM item WHERE itm_stat = '1' AND sup_username like '" + txtSearchSupplierItem.getText() + "' order by sup_username ");
                dtm.setRowCount(0);

                while (rs.next()) {
                    String a = (rs.getString(6));
                    String b = (rs.getString(5));
                    String c = (rs.getString(2));
                    String d = (rs.getString(3));
                    String e = (rs.getString(4));
                    String f = (rs.getString(7));
                    dtm.addRow(new Object[]{a, b, c, d, e, f});
                }
                calculateSupplierItemsCount();

            } catch (Exception e) {
            }
            break;
            case 2:

                try {
                DefaultTableModel dtm = (DefaultTableModel) tblSupplierItem.getModel();
                ResultSet rs = DB.DB.getData("SELECT * FROM item WHERE itm_stat = '1' AND itm_name like '" + txtSearchSupplierItem.getText() + "' order by itm_name ");
                dtm.setRowCount(0);

                while (rs.next()) {
                    String a = (rs.getString(6));
                    String b = (rs.getString(5));
                    String c = (rs.getString(2));
                    String d = (rs.getString(3));
                    String e = (rs.getString(4));
                    String f = (rs.getString(7));
                    dtm.addRow(new Object[]{a, b, c, d, e, f});
                }
                calculateSupplierItemsCount();

            } catch (Exception e) {
            }
            break;
            case 3:
                
                try {
                DefaultTableModel dtm = (DefaultTableModel) tblSupplierItem.getModel();
                ResultSet rs = DB.DB.getData("SELECT * FROM item WHERE itm_stat = '1' AND itm_code like '" + txtSearchSupplierItem.getText() + "' order by itm_code ");
                dtm.setRowCount(0);

                while (rs.next()) {
                    String a = (rs.getString(6));
                    String b = (rs.getString(5));
                    String c = (rs.getString(2));
                    String d = (rs.getString(3));
                    String e = (rs.getString(4));
                    String f = (rs.getString(7));
                    dtm.addRow(new Object[]{a, b, c, d, e, f});
                }
                calculateSupplierItemsCount();

            } catch (Exception e) {
            }
            break;
            case 4:
                
                try {

                DefaultTableModel dtm = (DefaultTableModel) tblSupplierItem.getModel();
                ResultSet rs = DB.DB.getData("SELECT * FROM item WHERE itm_stat = '1' AND itm_category like '" + txtSearchSupplierItem.getText() + "' order by itm_category ");
                dtm.setRowCount(0);

                while (rs.next()) {
                    String a = (rs.getString(6));
                    String b = (rs.getString(5));
                    String c = (rs.getString(2));
                    String d = (rs.getString(3));
                    String e = (rs.getString(4));
                    String f = (rs.getString(7));
                    dtm.addRow(new Object[]{a, b, c, d, e, f});
                }
                calculateSupplierItemsCount();

            } catch (Exception e) {
            }
            break;
            case 5:                
                try {
                DefaultTableModel dtm = (DefaultTableModel) tblSupplierItem.getModel();
                ResultSet rs = DB.DB.getData("SELECT * FROM item WHERE itm_stat = '1' order by itm_id ");
                dtm.setRowCount(0);

                while (rs.next()) {
                    String a = (rs.getString(6));
                    String b = (rs.getString(5));
                    String c = (rs.getString(2));
                    String d = (rs.getString(3));
                    String e = (rs.getString(4));
                    String f = (rs.getString(7));
                    dtm.addRow(new Object[]{a, b, c, d, e, f});
                }
                calculateSupplierItemsCount();

            } catch (Exception e) {
            }
            break;
            case 6:
                
                try {
                DefaultTableModel dtm = (DefaultTableModel) tblSupplierItem.getModel();
                ResultSet rs = DB.DB.getData("SELECT * FROM item WHERE itm_stat = '0' order by itm_id ");
                dtm.setRowCount(0);

                while (rs.next()) {
                    String a = (rs.getString(6));
                    String b = (rs.getString(5));
                    String c = (rs.getString(2));
                    String d = (rs.getString(3));
                    String e = (rs.getString(4));
                    String f = (rs.getString(7));
                    dtm.addRow(new Object[]{a, b, c, d, e, f});
                }
                calculateSupplierItemsCount();

            } catch (Exception e) {
            }
            break;
            case 7:
                
                try {
                DefaultTableModel dtm = (DefaultTableModel) tblSupplierItem.getModel();
                ResultSet rs = DB.DB.getData("SELECT * FROM item order by itm_id ");
                dtm.setRowCount(0);

                while (rs.next()) {
                    String a = (rs.getString(6));
                    String b = (rs.getString(5));
                    String c = (rs.getString(2));
                    String d = (rs.getString(3));
                    String e = (rs.getString(4));
                    String f = (rs.getString(7));
                    dtm.addRow(new Object[]{a, b, c, d, e, f});
                }
                calculateSupplierItemsCount();

            } catch (Exception e) {
            }
            break;
        }
    }

    private void generatePOId() {
        try {

            ResultSet rs = DB.DB.getData("SELECT po_id AS x FROM purchase_order ORDER BY po_id DESC LIMIT 1");
            if (rs.next()) {

                int rowcount = Integer.parseInt(rs.getString("x"));
                rowcount++;

                this.txtPOId.setText("" + rowcount);
                this.txtPOId1.setText("" + rowcount);

                txtSearchPOItem.setText(txtPOId.getText());
                txtSearchPOItem1.setText(txtPOId1.getText());

                txtSearchGRNItem.setText(txtPOId.getText());
                txtSearchGRNItem1.setText(txtPOId1.getText());

                lblPOId.setText(txtSearchPOItem.getText());
                lblPOId2.setText(txtSearchPOItem1.getText());

                txtSearchGRNItem.setText(txtPOId.getText());
                txtSearchGRNItem1.setText(txtPOId1.getText());

                lblGRNId.setText(txtSearchGRNItem.getText());
                lblGRNId1.setText(txtSearchGRNItem1.getText());

            }

        } catch (Exception e) {
        }
    }

    private void calculatePOTotal() {
        try {

            ResultSet poTotal = DB.DB.getData("SELECT sum(po_total) FROM purchase_order WHERE po_id = '" + txtSearchPOItem.getText() + "'");
            ResultSet poTotal1 = DB.DB.getData("SELECT sum(po_total) FROM purchase_order WHERE po_id = '" + txtSearchPOItem1.getText() + "'");

            if (poTotal.next()) {
                Double d = poTotal.getDouble(1);
                lblTotal.setText("Rs. " + Double.toString(d));
            }

            if (poTotal1.next()) {
                Double d = poTotal1.getDouble(1);
                lblTotal3.setText("Rs. " + Double.toString(d));
            }
        } catch (Exception e) {
        }
    }

    private void calculateGRNTotal() {
        try {

            ResultSet poTotal = DB.DB.getData("SELECT sum(grn_total) FROM grn WHERE grn_status = 'Checked' AND grn_id = '" + txtSearchGRNItem.getText() + "'");

            if (poTotal.next()) {
                Double d = poTotal.getDouble(1);
                lblGRNTotal.setText("Rs. " + Double.toString(d));
            }

        } catch (Exception e) {
        }
    }

    private void calculateGRNTotal1() {
        try {

            ResultSet poTotal = DB.DB.getData("SELECT sum(grn_total) FROM grn WHERE grn_status = 'Checked' AND grn_id = '" + txtSearchGRNItem1.getText() + "'");

            if (poTotal.next()) {
                Double d = poTotal.getDouble(1);
                lblGRNTotal1.setText("Rs. " + Double.toString(d));
            }

        } catch (Exception e) {
        }
    }

    private void addPOItem() {
        int option = JOptionPane.showConfirmDialog(this, "Do you want to Add this Item to PO List?", "", JOptionPane.YES_NO_OPTION);
        if (option == 0) {

            if (!txtPOItemQuantity.getText().equals("")
                    & (!(cmbPOItemName.getItemCount() == 0))
                    & (!txtPOItemPrice.getText().equals(""))
                    & (!txtPOItemQuantity.getText().equals(""))) {

                try {

                    ResultSet rs = DB.DB.getData("SELECT itm_name FROM purchase_order WHERE itm_name = '" + cmbPOItemName.getSelectedItem().toString() + "' AND po_id = '" + txtPOId.getText() + "' ");

                    if (rs.next()) {
                        JOptionPane.showMessageDialog(this, "Item Already Added to PO Bill!", "Invalid Data", JOptionPane.WARNING_MESSAGE);
                    } else {

                        switch (cmbPOLeadTime.getSelectedIndex()) {
                            case 0: {
                                LocalDate localDate = LocalDate.now().plusWeeks(1);
                                ZoneId defaultZoneId = ZoneId.systemDefault();
                                Date leadTimeSelection = Date.from(localDate.atStartOfDay(defaultZoneId).toInstant());
                                SimpleDateFormat toDate = new SimpleDateFormat("yyyy-MM-dd");
                                date = "" + toDate.format(leadTimeSelection);
                                leadTime = date;
                                break;
                            }
                            case 1: {
                                LocalDate localDate = LocalDate.now().plusWeeks(2);
                                ZoneId defaultZoneId = ZoneId.systemDefault();
                                Date leadTimeSelection = Date.from(localDate.atStartOfDay(defaultZoneId).toInstant());
                                SimpleDateFormat toDate = new SimpleDateFormat("yyyy-MM-dd");
                                date = "" + toDate.format(leadTimeSelection);
                                leadTime = date;
                                break;
                            }
                            case 2: {
                                LocalDate localDate = LocalDate.now().plusWeeks(3);
                                ZoneId defaultZoneId = ZoneId.systemDefault();
                                Date leadTimeSelection = Date.from(localDate.atStartOfDay(defaultZoneId).toInstant());
                                SimpleDateFormat toDate = new SimpleDateFormat("yyyy-MM-dd");
                                date = "" + toDate.format(leadTimeSelection);
                                leadTime = date;
                                break;
                            }
                            case 3: {
                                LocalDate localDate = LocalDate.now().plusMonths(1);
                                ZoneId defaultZoneId = ZoneId.systemDefault();
                                Date leadTimeSelection = Date.from(localDate.atStartOfDay(defaultZoneId).toInstant());
                                SimpleDateFormat toDate = new SimpleDateFormat("yyyy-MM-dd");
                                date = "" + toDate.format(leadTimeSelection);
                                leadTime = date;
                                break;
                            }
                            default:
                                break;
                        }

                        int total = Integer.parseInt(txtPOItemQuantity.getText()) * Integer.parseInt(txtPOItemPrice.getText());

                        String addPOItem = "INSERT INTO purchase_order(po_id, itm_name, itm_code, po_item_quantity, po_item_price, sup_username, po_total, po_lead_time) VALUES ('" + txtPOId.getText() + "','" + cmbPOItemName.getSelectedItem().toString() + "',(SELECT itm_code FROM item WHERE itm_name = '" + cmbPOItemName.getSelectedItem().toString() + "'),'" + txtPOItemQuantity.getText() + "','" + txtPOItemPrice.getText() + "' ,'" + cmbPOItemSupplier.getSelectedItem().toString() + "','" + total + "','" + leadTime + "') ";
                        String addGRNItem = "INSERT INTO grn(grn_id, itm_name, itm_code, grn_quantity, grn_item_price, sup_username, grn_total, grn_lead_time, grn_status) VALUES ('" + txtPOId.getText() + "','" + cmbPOItemName.getSelectedItem().toString() + "',(SELECT itm_code FROM item WHERE itm_name = '" + cmbPOItemName.getSelectedItem().toString() + "'),'" + txtPOItemQuantity.getText() + "','" + txtPOItemPrice.getText() + "' ,'" + cmbPOItemSupplier.getSelectedItem().toString() + "','" + total + "','" + leadTime + "', 'Unchecked') ";

                        try {
                            DB.DB.putData(addPOItem);
                            DB.DB.putData(addGRNItem);

                            refreshPOTable();
                            refreshPOTable1();

                            calculatePOTotal();

                            calculateGRNTotal();
                            calculateGRNTotal1();

                            refreshGRNTable();
                            refreshGRNTable1();
                            ImageIcon icon = new ImageIcon(getClass().getResource("img/done.png"));
                            JOptionPane.showMessageDialog(this, "Item Added to Purchase Order List!", "Done", JOptionPane.INFORMATION_MESSAGE, icon);

                            txtPOItemQuantity.setText(null);
                            txtPOItemPrice.setText(null);
                            cmbPOItemName.setSelectedItem(null);
                            cmbPOLeadTime.setSelectedIndex(0);

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

    private void addPOItem1() {
        int option = JOptionPane.showConfirmDialog(this, "Do you want to Add this Item to PO List?", "", JOptionPane.YES_NO_OPTION);
        if (option == 0) {

            if (!txtPOItemQuantity1.getText().equals("")
                    & (!(cmbPOItemName1.getItemCount() == 0))
                    & (!txtPOItemPrice1.getText().equals(""))
                    & (!txtPOItemQuantity1.getText().equals(""))) {

                try {

                    ResultSet rs = DB.DB.getData("SELECT itm_name FROM purchase_order WHERE itm_name = '" + cmbPOItemName1.getSelectedItem().toString() + "' AND po_id = '" + txtPOId1.getText() + "' ");

                    if (rs.next()) {
                        JOptionPane.showMessageDialog(this, "Item Already Added to PO Bill!", "Invalid Data", JOptionPane.WARNING_MESSAGE);
                    } else {

                        switch (cmbPOLeadTime1.getSelectedIndex()) {
                            case 0: {
                                LocalDate localDate = LocalDate.now().plusWeeks(1);
                                ZoneId defaultZoneId = ZoneId.systemDefault();
                                Date leadTimeSelection = Date.from(localDate.atStartOfDay(defaultZoneId).toInstant());
                                SimpleDateFormat toDate = new SimpleDateFormat("yyyy-MM-dd");
                                date = "" + toDate.format(leadTimeSelection);
                                leadTime = date;
                                break;
                            }
                            case 1: {
                                LocalDate localDate = LocalDate.now().plusWeeks(2);
                                ZoneId defaultZoneId = ZoneId.systemDefault();
                                Date leadTimeSelection = Date.from(localDate.atStartOfDay(defaultZoneId).toInstant());
                                SimpleDateFormat toDate = new SimpleDateFormat("yyyy-MM-dd");
                                date = "" + toDate.format(leadTimeSelection);
                                leadTime = date;
                                break;
                            }
                            case 2: {
                                LocalDate localDate = LocalDate.now().plusWeeks(3);
                                ZoneId defaultZoneId = ZoneId.systemDefault();
                                Date leadTimeSelection = Date.from(localDate.atStartOfDay(defaultZoneId).toInstant());
                                SimpleDateFormat toDate = new SimpleDateFormat("yyyy-MM-dd");
                                date = "" + toDate.format(leadTimeSelection);
                                leadTime = date;
                                break;
                            }
                            case 3: {
                                LocalDate localDate = LocalDate.now().plusMonths(1);
                                ZoneId defaultZoneId = ZoneId.systemDefault();
                                Date leadTimeSelection = Date.from(localDate.atStartOfDay(defaultZoneId).toInstant());
                                SimpleDateFormat toDate = new SimpleDateFormat("yyyy-MM-dd");
                                date = "" + toDate.format(leadTimeSelection);
                                leadTime = date;
                                break;
                            }
                            default:
                                break;
                        }

                        int total = Integer.parseInt(txtPOItemQuantity1.getText()) * Integer.parseInt(txtPOItemPrice1.getText());

                        String addPOItem1 = "INSERT INTO purchase_order(po_id, itm_name, itm_code, po_item_quantity, po_item_price, sup_username, po_total, po_lead_time) VALUES ('" + txtPOId1.getText() + "','" + cmbPOItemName1.getSelectedItem().toString() + "',(SELECT itm_code FROM item WHERE itm_name = '" + cmbPOItemName1.getSelectedItem().toString() + "'),'" + txtPOItemQuantity1.getText() + "','" + txtPOItemPrice1.getText() + "' ,'" + cmbPOItemSupplier1.getSelectedItem().toString() + "','" + total + "','" + leadTime + "') ";
                        String addGRNItem1 = "INSERT INTO grn(grn_id, itm_name, itm_code, grn_quantity, grn_item_price, sup_username, grn_total, grn_lead_time, grn_status) VALUES ('" + txtPOId1.getText() + "','" + cmbPOItemName1.getSelectedItem().toString() + "',(SELECT itm_code FROM item WHERE itm_name = '" + cmbPOItemName1.getSelectedItem().toString() + "'),'" + txtPOItemQuantity1.getText() + "','" + txtPOItemPrice1.getText() + "' ,'" + cmbPOItemSupplier1.getSelectedItem().toString() + "','" + total + "','" + leadTime + "', 'Unchecked') ";

                        try {
                            DB.DB.putData(addPOItem1);
                            DB.DB.putData(addGRNItem1);

                            refreshPOTable();
                            refreshPOTable1();

                            calculatePOTotal();

                            calculateGRNTotal();
                            calculateGRNTotal1();

                            refreshGRNTable();
                            refreshGRNTable1();
                            ImageIcon icon = new ImageIcon(getClass().getResource("img/done.png"));
                            JOptionPane.showMessageDialog(this, "Item Added to Purchase Order List!", "Done", JOptionPane.INFORMATION_MESSAGE, icon);

                            txtPOItemQuantity1.setText(null);
                            txtPOItemPrice1.setText(null);
                            cmbPOItemName1.setSelectedItem(null);
                            cmbPOLeadTime1.setSelectedIndex(0);

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

    private void refreshPOTable() {
        try {

            DefaultTableModel dtm = (DefaultTableModel) tblPOItem.getModel();
            ResultSet rs = DB.DB.getData("SELECT * FROM purchase_order WHERE po_id = '" + txtSearchPOItem.getText() + "' ");
            dtm.setRowCount(0);

            while (rs.next()) {
                String a = (rs.getString(1));
                String b = (rs.getString(3));
                String c = (rs.getString(4));
                String d = (rs.getString(5));
                String e = (rs.getString(6));
                String f = (rs.getString(7));
                String g = (rs.getString(8));
                String h = (rs.getString(9));
                dtm.addRow(new Object[]{a, b, c, d, e, f, g, h});
            }

            calculatePOTotal();

        } catch (Exception e) {
        }
    }

    private void refreshPOTable1() {
        try {

            DefaultTableModel dtm = (DefaultTableModel) tblPOItem1.getModel();
            ResultSet rs = DB.DB.getData("SELECT * FROM purchase_order WHERE po_id = '" + txtSearchPOItem1.getText() + "' ");
            dtm.setRowCount(0);

            while (rs.next()) {
                String a = (rs.getString(1));
                String b = (rs.getString(3));
                String c = (rs.getString(4));
                String d = (rs.getString(5));
                String e = (rs.getString(6));
                String f = (rs.getString(7));
                String g = (rs.getString(8));
                String h = (rs.getString(9));
                dtm.addRow(new Object[]{a, b, c, d, e, f, g, h});
            }

            calculatePOTotal();
        } catch (Exception e) {
        }
    }

    private void searchPOItem() {
        switch (cmbSortPOItem.getSelectedIndex()) {
            case 0:

                calculatePOTotal();

                try {
                    DefaultTableModel dtm = (DefaultTableModel) tblPOItem.getModel();
                    ResultSet rs = DB.DB.getData("SELECT * FROM purchase_order WHERE po_id = '" + txtSearchPOItem.getText() + "%' order by po_id ");
                    dtm.setRowCount(0);

                    while (rs.next()) {
                        String a = (rs.getString(1));
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
                DefaultTableModel dtm = (DefaultTableModel) tblPOItem.getModel();
                ResultSet rs = DB.DB.getData("SELECT * FROM purchase_order WHERE itm_name like '" + txtSearchPOItem.getText() + "%' order by itm_name ");
                dtm.setRowCount(0);

                while (rs.next()) {
                    String a = (rs.getString(1));
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
                DefaultTableModel dtm = (DefaultTableModel) tblPOItem.getModel();
                ResultSet rs = DB.DB.getData("SELECT * FROM purchase_order WHERE itm_code like '" + txtSearchPOItem.getText() + "%' order by itm_code ");
                dtm.setRowCount(0);

                while (rs.next()) {
                    String a = (rs.getString(1));
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
                DefaultTableModel dtm = (DefaultTableModel) tblPOItem.getModel();
                ResultSet rs = DB.DB.getData("SELECT * FROM purchase_order WHERE sup_username like '" + txtSearchPOItem.getText() + "%' order by sup_username ");
                dtm.setRowCount(0);

                while (rs.next()) {
                    String a = (rs.getString(1));
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

    private void searchPOItem1() {
        switch (cmbSortPOItem1.getSelectedIndex()) {
            case 0:

                calculatePOTotal();

                try {
                    DefaultTableModel dtm = (DefaultTableModel) tblPOItem1.getModel();
                    ResultSet rs = DB.DB.getData("SELECT * FROM purchase_order WHERE po_id = '" + txtSearchPOItem1.getText() + "%' order by po_id ");
                    dtm.setRowCount(0);

                    while (rs.next()) {
                        String a = (rs.getString(1));
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
                DefaultTableModel dtm = (DefaultTableModel) tblPOItem1.getModel();
                ResultSet rs = DB.DB.getData("SELECT * FROM purchase_order WHERE itm_name like '" + txtSearchPOItem1.getText() + "%' order by itm_name ");
                dtm.setRowCount(0);

                while (rs.next()) {
                    String a = (rs.getString(1));
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
                DefaultTableModel dtm = (DefaultTableModel) tblPOItem1.getModel();
                ResultSet rs = DB.DB.getData("SELECT * FROM purchase_order WHERE itm_code like '" + txtSearchPOItem1.getText() + "%' order by itm_code ");
                dtm.setRowCount(0);

                while (rs.next()) {
                    String a = (rs.getString(1));
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
                DefaultTableModel dtm = (DefaultTableModel) tblPOItem1.getModel();
                ResultSet rs = DB.DB.getData("SELECT * FROM purchase_order WHERE sup_username like '" + txtSearchPOItem1.getText() + "%' order by sup_username ");
                dtm.setRowCount(0);

                while (rs.next()) {
                    String a = (rs.getString(1));
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

    private void purchaseOrder() {
        int option = JOptionPane.showConfirmDialog(this, "Do you want to Save current PO Bill?", "", JOptionPane.YES_NO_OPTION);

        if (option == 0) {

            if (txtPOId.getText().equals(lblPOId.getText())) {

                String PurchaseOrderLog = "INSERT INTO log(usr_id, usr_type, usr_username, log_activity, log_description, log_state) VALUES ((SELECT usr_id FROM user WHERE usr_username = '" + lblAdminUsername.getText() + "'),'Admin','" + lblAdminUsername.getText() + "','Purchase Order','Admin Ordered PO Bill No. " + txtSearchPOItem.getText() + "','SUCCESS')";
                try {
                    DB.DB.putData(PurchaseOrderLog);
                } catch (Exception e) {
                }

                printSelectedPOBill();
                generatePOId();

                ImageIcon icon = new ImageIcon(getClass().getResource("img/done.png"));
                JOptionPane.showMessageDialog(this, "PO Bill Saved Successfully!", "Done", JOptionPane.INFORMATION_MESSAGE, icon);

                refreshPOTable();
                refreshPOTable1();

                refreshGRNTable();
                refreshGRNTable1();

            } else {
                JOptionPane.showMessageDialog(this, "This PO Bill has Already Saved, Cannot be Modified!!", "Invalid Data", JOptionPane.WARNING_MESSAGE);
            }
        }
    }

    private void purchaseOrder1() {
        int option = JOptionPane.showConfirmDialog(this, "Do you want to Save current PO Bill?", "", JOptionPane.YES_NO_OPTION);

        if (option == 0) {

            if (txtPOId1.getText().equals(lblPOId2.getText())) {

                String PurchaseOrderLog = "INSERT INTO log(usr_id, usr_type, usr_username, log_activity, log_description, log_state) VALUES ((SELECT usr_id FROM user WHERE usr_username = '" + lblAdminUsername.getText() + "'),'Admin','" + lblAdminUsername.getText() + "','Purchase Order','Admin Ordered PO Bill No. " + txtSearchPOItem1.getText() + "','SUCCESS')";
                try {
                    DB.DB.putData(PurchaseOrderLog);
                } catch (Exception e) {
                }

                printSelectedPOBill1();
                generatePOId();

                ImageIcon icon = new ImageIcon(getClass().getResource("img/done.png"));
                JOptionPane.showMessageDialog(this, "PO Bill Saved Successfully!", "Done", JOptionPane.INFORMATION_MESSAGE, icon);

                refreshPOTable();
                refreshPOTable1();

                refreshGRNTable();
                refreshGRNTable1();

            } else {
                JOptionPane.showMessageDialog(this, "This PO Bill has Already Saved, Cannot be Modified!!", "Invalid Data", JOptionPane.WARNING_MESSAGE);
            }
        }
    }

    public void printSelectedPOBill() {
        HashMap a = new HashMap();
        a.put("po_id", txtSearchPOItem.getText());
        a.put("po_total", lblTotal.getText());

        new Thread() {
            @Override
            public void run() {
                try {

                    String reportSource = "C:\\Program Files\\Exfiltro Reports\\viewSelectedPOBill.jrxml";

                    JasperDesign jdesign = JRXmlLoader.load(reportSource);
                    JasperReport jreport = JasperCompileManager.compileReport(jdesign);
                    JasperPrint jprint = JasperFillManager.fillReport(jreport, a, DB.DB.getConnection());
                    JasperViewer.viewReport(jprint, false);

                } catch (JRException ex) {
                }
            }
        }.start();
    }

    public void printSelectedPOBill1() {
        HashMap a = new HashMap();
        a.put("po_id", txtSearchPOItem1.getText());
        a.put("po_total", lblTotal3.getText());

        new Thread() {
            @Override
            public void run() {
                try {

                    String reportSource = "C:\\Program Files\\Exfiltro Reports\\viewSelectedPOBill.jrxml";

                    JasperDesign jdesign = JRXmlLoader.load(reportSource);
                    JasperReport jreport = JasperCompileManager.compileReport(jdesign);
                    JasperPrint jprint = JasperFillManager.fillReport(jreport, a, DB.DB.getConnection());
                    JasperViewer.viewReport(jprint, false);

                } catch (JRException ex) {
                }
            }
        }.start();
    }

    private void deletePOItem() {
        int option = JOptionPane.showConfirmDialog(this, "Do you want to Delete this PO Item?", "", JOptionPane.YES_NO_OPTION);
        if (option == 0) {

            try {

                if (txtPOId.getText().equals(lblPOId.getText())) {

                    DB.DB.putData("DELETE FROM purchase_order WHERE id ='" + selectedPOItemId + "' AND po_id = '" + txtPOId.getText() + "' ");
                    DB.DB.putData("DELETE FROM grn WHERE id ='" + selectedPOItemId + "' AND grn_id = '" + txtPOId.getText() + "' ");

                    ImageIcon icon = new ImageIcon(getClass().getResource("img/done.png"));
                    JOptionPane.showMessageDialog(this, "PO Item Deleted!", "Done", JOptionPane.INFORMATION_MESSAGE, icon);

                } else {
                    JOptionPane.showMessageDialog(this, "This PO Item has Already Ordered, Cannot be Deleted!", "Invalid Data", JOptionPane.WARNING_MESSAGE);
                }

                refreshPOTable();
                refreshPOTable1();

                refreshGRNTable();
                refreshGRNTable1();

                calculatePOTotal();
                calculateGRNTotal();

                calculateGRNTotal1();

            } catch (Exception e) {
            }
        } else {

        }
    }

    private void deletePOItem1() {
        int option = JOptionPane.showConfirmDialog(this, "Do you want to Delete this PO Item?", "", JOptionPane.YES_NO_OPTION);
        if (option == 0) {

            try {

                if (txtPOId1.getText().equals(lblPOId2.getText())) {

                    DB.DB.putData("DELETE FROM purchase_order WHERE id ='" + selectedPOItemId1 + "' AND po_id = '" + txtPOId1.getText() + "' ");
                    DB.DB.putData("DELETE FROM grn WHERE id ='" + selectedPOItemId1 + "' AND grn_id = '" + txtPOId.getText() + "' ");

                    ImageIcon icon = new ImageIcon(getClass().getResource("img/done.png"));
                    JOptionPane.showMessageDialog(this, "PO Item Deleted!", "Done", JOptionPane.INFORMATION_MESSAGE, icon);

                } else {
                    JOptionPane.showMessageDialog(this, "This PO Item has Already Ordered, Cannot be Deleted!", "Invalid Data", JOptionPane.WARNING_MESSAGE);
                }

                refreshPOTable();
                refreshPOTable1();

                refreshGRNTable();
                refreshGRNTable1();

                calculatePOTotal();
                calculateGRNTotal();

                calculateGRNTotal1();

            } catch (Exception e) {
            }
        } else {

        }
    }

    private void refreshGRNTable() {
        try {

            DefaultTableModel dtm = (DefaultTableModel) tblGRN.getModel();
            ResultSet rs = DB.DB.getData("SELECT * FROM grn WHERE grn_id = '" + txtSearchGRNItem.getText() + "' ");
            dtm.setRowCount(0);

            while (rs.next()) {
                String a = (rs.getString(1));
                String b = (rs.getString(3));
                String c = (rs.getString(4));
                String d = (rs.getString(5));
                String e = (rs.getString(6));
                String f = (rs.getString(7));
                String g = (rs.getString(10));
                dtm.addRow(new Object[]{a, b, c, d, e, f, g});
            }

        } catch (Exception e) {
        }

        txtGRNItemName.setText(null);
        txtGRNStatus.setText(null);
        txtGRNItemQuantity.setText(null);
        txtGRNItemPrice.setText(null);
        txtGRNItemSellingPrice.setText(null);
        txtGRNItemLowStock.setText(null);
    }

    private void refreshGRNTable1() {
        try {

            DefaultTableModel dtm = (DefaultTableModel) tblGRN1.getModel();
            ResultSet rs = DB.DB.getData("SELECT * FROM grn WHERE grn_id = '" + txtSearchGRNItem1.getText() + "' ");
            dtm.setRowCount(0);

            while (rs.next()) {
                String a = (rs.getString(1));
                String b = (rs.getString(3));
                String c = (rs.getString(4));
                String d = (rs.getString(5));
                String e = (rs.getString(6));
                String f = (rs.getString(7));
                String g = (rs.getString(10));
                dtm.addRow(new Object[]{a, b, c, d, e, f, g});
            }

        } catch (Exception e) {
        }

        txtGRNItemName1.setText(null);
        txtGRNStatus1.setText(null);
        txtGRNItemQuantity1.setText(null);
        txtGRNItemPrice1.setText(null);
        txtGRNItemSellingPrice1.setText(null);
        txtGRNItemLowStock1.setText(null);
        txtGRNItemStockCount1.setText(null);
    }

    private void searchGRNItem() {
        switch (cmbSortGRNItem.getSelectedIndex()) {
            case 0:
                calculateGRNTotal();

                try {
                    DefaultTableModel dtm = (DefaultTableModel) tblGRN.getModel();
                    ResultSet rs = DB.DB.getData("SELECT * FROM grn WHERE grn_id = '" + txtSearchGRNItem.getText() + "%' order by grn_id ");
                    dtm.setRowCount(0);

                    while (rs.next()) {
                        String a = (rs.getString(1));
                        String b = (rs.getString(3));
                        String c = (rs.getString(4));
                        String d = (rs.getString(5));
                        String e = (rs.getString(6));
                        String f = (rs.getString(7));
                        String g = (rs.getString(10));
                        dtm.addRow(new Object[]{a, b, c, d, e, f, g});
                    }

                } catch (Exception e) {
                }
                break;
            case 1:

                try {
                DefaultTableModel dtm = (DefaultTableModel) tblGRN.getModel();
                ResultSet rs = DB.DB.getData("SELECT * FROM grn WHERE grn_id = '" + txtSearchGRNItem.getText() + "%' order by grn_id ");
                dtm.setRowCount(0);

                while (rs.next()) {
                    String a = (rs.getString(1));
                    String b = (rs.getString(3));
                    String c = (rs.getString(4));
                    String d = (rs.getString(5));
                    String e = (rs.getString(6));
                    String f = (rs.getString(7));
                    String g = (rs.getString(10));
                    dtm.addRow(new Object[]{a, b, c, d, e, f, g});
                }

            } catch (Exception e) {
            }
            break;
            case 2:

                try {
                DefaultTableModel dtm = (DefaultTableModel) tblGRN.getModel();
                ResultSet rs = DB.DB.getData("SELECT * FROM grn WHERE grn_status = 'Checked' AND grn_id = '" + txtSearchGRNItem.getText() + "' order by grn_status ");
                dtm.setRowCount(0);

                while (rs.next()) {
                    String a = (rs.getString(1));
                    String b = (rs.getString(3));
                    String c = (rs.getString(4));
                    String d = (rs.getString(5));
                    String e = (rs.getString(6));
                    String f = (rs.getString(7));
                    String g = (rs.getString(10));
                    dtm.addRow(new Object[]{a, b, c, d, e, f, g});
                }
            } catch (Exception e) {
            }
            break;
            case 3:

                try {
                DefaultTableModel dtm = (DefaultTableModel) tblGRN.getModel();
                ResultSet rs = DB.DB.getData("SELECT * FROM grn WHERE grn_status = 'Unchecked' AND grn_id = '" + txtSearchGRNItem.getText() + "' order by grn_status ");
                dtm.setRowCount(0);

                while (rs.next()) {
                    String a = (rs.getString(1));
                    String b = (rs.getString(3));
                    String c = (rs.getString(4));
                    String d = (rs.getString(5));
                    String e = (rs.getString(6));
                    String f = (rs.getString(7));
                    String g = (rs.getString(10));
                    dtm.addRow(new Object[]{a, b, c, d, e, f, g});
                }
            } catch (Exception e) {
            }
            break;
            case 4:

                try {
                DefaultTableModel dtm = (DefaultTableModel) tblGRN.getModel();
                ResultSet rs = DB.DB.getData("SELECT * FROM grn WHERE grn_status = 'Bad Order' AND grn_id = '" + txtSearchGRNItem.getText() + "'  order by grn_status ");
                dtm.setRowCount(0);

                while (rs.next()) {
                    String a = (rs.getString(1));
                    String b = (rs.getString(3));
                    String c = (rs.getString(4));
                    String d = (rs.getString(5));
                    String e = (rs.getString(6));
                    String f = (rs.getString(7));
                    String g = (rs.getString(10));
                    dtm.addRow(new Object[]{a, b, c, d, e, f, g});
                }
            } catch (Exception e) {
            }
            default:
                break;
        }
    }

    private void searchGRNItem1() {
        switch (cmbSortGRNItem1.getSelectedIndex()) {
            case 0:
                calculateGRNTotal1();

                try {
                    DefaultTableModel dtm = (DefaultTableModel) tblGRN1.getModel();
                    ResultSet rs = DB.DB.getData("SELECT * FROM grn WHERE grn_id = '" + txtSearchGRNItem1.getText() + "%' order by grn_id ");
                    dtm.setRowCount(0);

                    while (rs.next()) {
                        String a = (rs.getString(1));
                        String b = (rs.getString(3));
                        String c = (rs.getString(4));
                        String d = (rs.getString(5));
                        String e = (rs.getString(6));
                        String f = (rs.getString(7));
                        String g = (rs.getString(10));
                        dtm.addRow(new Object[]{a, b, c, d, e, f, g});
                    }

                } catch (Exception e) {
                }
                break;
            case 1:

                try {
                DefaultTableModel dtm = (DefaultTableModel) tblGRN1.getModel();
                ResultSet rs = DB.DB.getData("SELECT * FROM grn WHERE grn_id = '" + txtSearchGRNItem1.getText() + "%' order by grn_id ");
                dtm.setRowCount(0);

                while (rs.next()) {
                    String a = (rs.getString(1));
                    String b = (rs.getString(3));
                    String c = (rs.getString(4));
                    String d = (rs.getString(5));
                    String e = (rs.getString(6));
                    String f = (rs.getString(7));
                    String g = (rs.getString(10));
                    dtm.addRow(new Object[]{a, b, c, d, e, f, g});
                }

            } catch (Exception e) {
            }
            break;
            case 2:
                
                try {
                DefaultTableModel dtm = (DefaultTableModel) tblGRN1.getModel();
                ResultSet rs = DB.DB.getData("SELECT * FROM grn WHERE grn_status = 'Checked' AND grn_id = '" + txtSearchGRNItem1.getText() + "' order by grn_status ");
                dtm.setRowCount(0);

                while (rs.next()) {
                    String a = (rs.getString(1));
                    String b = (rs.getString(3));
                    String c = (rs.getString(4));
                    String d = (rs.getString(5));
                    String e = (rs.getString(6));
                    String f = (rs.getString(7));
                    String g = (rs.getString(10));
                    dtm.addRow(new Object[]{a, b, c, d, e, f, g});
                }
            } catch (Exception e) {
            }
            break;
            case 3:
                
                try {
                DefaultTableModel dtm = (DefaultTableModel) tblGRN1.getModel();
                ResultSet rs = DB.DB.getData("SELECT * FROM grn WHERE grn_status = 'Unchecked' AND grn_id = '" + txtSearchGRNItem1.getText() + "' order by grn_status ");
                dtm.setRowCount(0);

                while (rs.next()) {
                    String a = (rs.getString(1));
                    String b = (rs.getString(3));
                    String c = (rs.getString(4));
                    String d = (rs.getString(5));
                    String e = (rs.getString(6));
                    String f = (rs.getString(7));
                    String g = (rs.getString(10));
                    dtm.addRow(new Object[]{a, b, c, d, e, f, g});
                }
            } catch (Exception e) {
            }
            break;
            case 4:

                try {
                DefaultTableModel dtm = (DefaultTableModel) tblGRN1.getModel();
                ResultSet rs = DB.DB.getData("SELECT * FROM grn WHERE grn_status = 'Bad Order' AND grn_id = '" + txtSearchGRNItem1.getText() + "'  order by grn_status ");
                dtm.setRowCount(0);

                while (rs.next()) {
                    String a = (rs.getString(1));
                    String b = (rs.getString(3));
                    String c = (rs.getString(4));
                    String d = (rs.getString(5));
                    String e = (rs.getString(6));
                    String f = (rs.getString(7));
                    String g = (rs.getString(10));
                    dtm.addRow(new Object[]{a, b, c, d, e, f, g});
                }
            } catch (Exception e) {
            }
            default:
                break;
        }
    }

    private void markAsBadOrder() {
        int option = JOptionPane.showConfirmDialog(this, "Do you want to mark this GRN Item as Bad Order?", "", JOptionPane.YES_NO_OPTION);

        if (option == 0) {

            String checkStatus = txtGRNStatus.getText();
            String checkedStatus = "Checked";
            String uncheckedStatus = "Unchecked";

            if (checkStatus.equals(uncheckedStatus)) {
                try {
                    String selectedGRNItemId = ((String) tblGRN.getValueAt(tblGRN.getSelectedRow(), 0));

                    DB.DB.putData("UPDATE grn SET grn_status = 'Bad Order' WHERE id = '" + selectedGRNItemId + "'");
                    String markingBadOrderActivityLog = "INSERT INTO log(usr_id, usr_type, usr_username, log_activity, log_description, log_state) VALUES ((SELECT usr_id FROM user WHERE usr_username = '" + lblAdminUsername.getText() + "'),'Admin','" + lblAdminUsername.getText() + "','Bad Order','Admin marked bad order item on GRN Bill No. " + txtGRNItemName.getText() + "','SUCCESS')";

                    DB.DB.putData(markingBadOrderActivityLog);
                    ImageIcon icon = new ImageIcon(getClass().getResource("img/done.png"));
                    JOptionPane.showMessageDialog(this, "Bad Order Marked Successfully!", "Done", JOptionPane.INFORMATION_MESSAGE, icon);

                    refreshGRNTable();
                    refreshGRNTable1();

                    calculateGRNTotal();
                    calculateGRNTotal1();

                } catch (Exception e) {
                }

            } else if (checkStatus.equals(checkedStatus)) {
                JOptionPane.showMessageDialog(this, "Checked Items cannot be Marked as Bad Order!");

            } else {
                JOptionPane.showMessageDialog(this, "GRN Item Already marked as Bad Order!");
            }
        }
    }

    private void markAsBadOrder1() {
        int option = JOptionPane.showConfirmDialog(this, "Do you want to mark this GRN Item as Bad Order?", "", JOptionPane.YES_NO_OPTION);
        if (option == 0) {

            String checkStatus = txtGRNStatus1.getText();
            String checkedStatus = "Checked";
            String uncheckedStatus = "Unchecked";

            if (checkStatus.equals(uncheckedStatus)) {
                try {
                    String selectedGRNItemId = ((String) tblGRN1.getValueAt(tblGRN1.getSelectedRow(), 0));

                    DB.DB.putData("UPDATE grn SET grn_status = 'Bad Order' WHERE id = '" + selectedGRNItemId + "'");
                    String markingBadOrderActivityLog = "INSERT INTO log(usr_id, usr_type, usr_username, log_activity, log_description, log_state) VALUES ((SELECT usr_id FROM user WHERE usr_username = '" + lblAdminUsername.getText() + "'),'Admin','" + lblAdminUsername.getText() + "','Bad Order','Admin marked bad order item on GRN Bill No. " + txtGRNItemName.getText() + "','SUCCESS')";

                    DB.DB.putData(markingBadOrderActivityLog);
                    ImageIcon icon = new ImageIcon(getClass().getResource("img/done.png"));
                    JOptionPane.showMessageDialog(this, "Bad Order Marked Successfully!", "Done", JOptionPane.INFORMATION_MESSAGE, icon);

                    refreshGRNTable();
                    refreshGRNTable1();

                    calculateGRNTotal();
                    calculateGRNTotal1();

                } catch (Exception e) {
                }
            } else if (checkStatus.equals(checkedStatus)) {
                JOptionPane.showMessageDialog(this, "Checked Items cannot be Marked as Bad Order!");

            } else {
                JOptionPane.showMessageDialog(this, "GRN Item Already marked as Bad Order!");
            }
        }
    }

    public void printSelectedGRNBill() {
        HashMap a = new HashMap();
        a.put("grn_id", txtSearchGRNItem.getText());
        a.put("grn_total", lblGRNTotal.getText());

        new Thread() {
            @Override
            public void run() {
                try {

                    String reportSource = "C:\\Program Files\\Exfiltro Reports\\viewSelectedGRNBill.jrxml";

                    JasperDesign jdesign = JRXmlLoader.load(reportSource);
                    JasperReport jreport = JasperCompileManager.compileReport(jdesign);
                    JasperPrint jprint = JasperFillManager.fillReport(jreport, a, DB.DB.getConnection());
                    JasperViewer.viewReport(jprint, false);

                } catch (JRException ex) {

                }
            }
        }.start();
    }

    public void printSelectedGRNBill1() {
        HashMap a = new HashMap();
        a.put("grn_id", txtSearchGRNItem1.getText());
        a.put("grn_total", lblGRNTotal1.getText());

        new Thread() {
            @Override
            public void run() {
                try {

                    String reportSource = "C:\\Program Files\\Exfiltro Reports\\viewSelectedGRNBill.jrxml";

                    JasperDesign jdesign = JRXmlLoader.load(reportSource);
                    JasperReport jreport = JasperCompileManager.compileReport(jdesign);
                    JasperPrint jprint = JasperFillManager.fillReport(jreport, a, DB.DB.getConnection());

                    JasperViewer.viewReport(jprint, false);

                } catch (JRException ex) {

                }
            }
        }.start();
    }

    private void addToStock() {
        try {
            ResultSet stockAvailability = DB.DB.getData("SELECT itm_name FROM stock WHERE itm_name = '" + txtGRNItemName.getText() + "' ");
            if (stockAvailability.next()) {
                JOptionPane.showMessageDialog(this, "Existing Items cannot be Added as New Item!", "Invalid Data", JOptionPane.WARNING_MESSAGE);
                jTabbedPaneGRNChecking.setSelectedIndex(0);
            } else {
                int option = JOptionPane.showConfirmDialog(this, "Do you want to Add this Item to Stock?", "", JOptionPane.YES_NO_OPTION);
                if (option == 0) {
                    if (!txtGRNItemSellingPrice.getText().equals("")
                            & !txtGRNItemLowStock.getText().equals("")) {

                        if ("Checked".equals(txtGRNStatus.getText())) {
                            JOptionPane.showMessageDialog(this, "Item Already Added to Stock!", "Invalid Data", JOptionPane.WARNING_MESSAGE);

                        } else {

                            txtGRNItemName.setText((String) tblGRN.getValueAt(tblGRN.getSelectedRow(), 1));
                            String selectedGRNItemCode = ((String) tblGRN.getValueAt(tblGRN.getSelectedRow(), 2));
                            txtGRNItemQuantity.setText((String) tblGRN.getValueAt(tblGRN.getSelectedRow(), 3));
                            txtGRNItemPrice.setText((String) tblGRN.getValueAt(tblGRN.getSelectedRow(), 4));
                            String selectedGRNItemSupplier = ((String) tblGRN.getValueAt(tblGRN.getSelectedRow(), 5));

                            int selling_price = Integer.parseInt(txtGRNItemSellingPrice.getText());
                            int purchase_price = Integer.parseInt(txtGRNItemPrice.getText());
                            int profit = selling_price - purchase_price;

                            String profitPerEachItem = Integer.toString(profit);
                            String selectedGRNItemProfitPerSingleItem = profitPerEachItem;

                            try {

                                String addItemToStock = "INSERT INTO stock(itm_code, itm_name, stk_itm_price, stk_selling_price, stk_profit, sup_username, stk_count, stk_low_stock, stk_status) VALUES ('" + selectedGRNItemCode + "','" + txtGRNItemName.getText() + "','" + txtGRNItemPrice.getText() + "','" + txtGRNItemSellingPrice.getText() + "','" + selectedGRNItemProfitPerSingleItem + "','" + selectedGRNItemSupplier + "','" + txtGRNItemQuantity.getText() + "','" + txtGRNItemLowStock.getText() + "','Available')";
                                String selectedGRNItemId = ((String) tblGRN.getValueAt(tblGRN.getSelectedRow(), 0));

                                ImageIcon icon = new ImageIcon(getClass().getResource("img/done.png"));
                                JOptionPane.showMessageDialog(this, "GRN Item Checked Successfully!", "Done", JOptionPane.INFORMATION_MESSAGE, icon);
                                JOptionPane.showMessageDialog(this, "New Item Added to Stock!", "Done", JOptionPane.INFORMATION_MESSAGE, icon);

                                String addNewStockItemActivityLog = "INSERT INTO log(usr_id, usr_type, usr_username, log_activity, log_description, log_state) VALUES ((SELECT usr_id FROM user WHERE usr_username = '" + lblAdminUsername.getText() + "'),'Admin','" + lblAdminUsername.getText() + "','Add New Stock Item','Admin added new Stock Item " + txtGRNItemName.getText() + "','SUCCESS')";
                                String GRNItemCheckingActivityLog = "INSERT INTO log(usr_id, usr_type, usr_username, log_activity, log_description, log_state) VALUES ((SELECT usr_id FROM user WHERE usr_username = '" + lblAdminUsername.getText() + "'),'Admin','" + lblAdminUsername.getText() + "','GRN Status','Admin checked GRN item " + txtGRNItemName.getText() + "','SUCCESS')";

                                DB.DB.putData(addItemToStock);
                                DB.DB.putData(addNewStockItemActivityLog);
                                DB.DB.putData(GRNItemCheckingActivityLog);

                                DB.DB.putData("UPDATE grn SET grn_status = 'Checked' WHERE id = '" + selectedGRNItemId + "'");
                                txtGRNStatus.setText("Checked");
                                DB.DB.putData("UPDATE item SET itm_stat = '1' WHERE itm_name = '" + txtGRNItemName.getText() + "'");
                                searchGRNItem();
                                refreshStockTable();
                                refreshLowStockTable();
                                refreshSupplierItemTable();
                                jTabbedPaneGRNChecking.setSelectedIndex(0);

                                txtGRNItemName.setText(null);
                                txtGRNStatus.setText(null);
                                txtGRNItemQuantity.setText(null);
                                txtGRNItemPrice.setText(null);
                                txtGRNItemSellingPrice.setText(null);
                                txtGRNItemLowStock.setText(null);

                            } catch (Exception e) {
                            }
                        }

                    } else {
                        JOptionPane.showMessageDialog(this, "Please Fill All the Fields!", "Invalid Data", JOptionPane.WARNING_MESSAGE);
                    }
                }

            }
            DB.DB.con.close();

        } catch (Exception e) {
        }

    }

    private void addReOrdersToStock() {
        try {
            ResultSet stockAvailability = DB.DB.getData("SELECT itm_name FROM stock WHERE itm_name = '" + txtGRNItemName1.getText() + "' ");
            if (!(stockAvailability.next())) {
                JOptionPane.showMessageDialog(this, "Non Existing Items cannot be Added as Existing Item!", "Invalid Data", JOptionPane.WARNING_MESSAGE);
                jTabbedPaneAddExistingItems.setSelectedIndex(2);
            } else {
                int option = JOptionPane.showConfirmDialog(this, "Do you want to Add this Item to Stock?", "", JOptionPane.YES_NO_OPTION);
                if (option == 0) {
                    if (!txtGRNItemSellingPrice1.getText().equals("")
                            & !txtGRNItemLowStock1.getText().equals("")) {

                        if ("Checked".equals(txtGRNStatus1.getText())) {
                            JOptionPane.showMessageDialog(this, "Item Already Added to Stock!", "Invalid Data", JOptionPane.WARNING_MESSAGE);
                        } else {

                            ResultSet rs3 = DB.DB.getData("SELECT stk_id FROM stock WHERE itm_name = '" + txtGRNItemName1.getText() + "' ");

                            if (rs3.next()) {

                                String stockId = (rs3.getString(1));

                                txtGRNItemName1.setText((String) tblGRN1.getValueAt(tblGRN1.getSelectedRow(), 1));
                                txtGRNItemQuantity1.setText((String) tblGRN1.getValueAt(tblGRN1.getSelectedRow(), 3));
                                txtGRNItemPrice1.setText((String) tblGRN1.getValueAt(tblGRN1.getSelectedRow(), 4));

                                int selling_price = Integer.parseInt(txtGRNItemSellingPrice1.getText());
                                int purchase_price = Integer.parseInt(txtGRNItemPrice1.getText());

                                int profit = selling_price - purchase_price;

                                String profitPerEachItem = Integer.toString(profit);
                                String selectedGRNItemProfitPerSingleItem = profitPerEachItem;

                                ResultSet stockCountofSelectedItem = DB.DB.getData("SELECT stk_count FROM stock WHERE stk_id = '" + stockId + "' ");
                                if (stockCountofSelectedItem.next()) {
                                    int currentStockCount = Integer.parseInt(stockCountofSelectedItem.getString(1));
                                    int addedAmount = Integer.parseInt(txtGRNItemQuantity1.getText());
                                    int newStockCount = currentStockCount + addedAmount;

                                    DB.DB.putData("UPDATE stock SET stk_itm_price ='" + txtGRNItemPrice1.getText() + "',stk_selling_price = '" + txtGRNItemSellingPrice1.getText() + "', stk_profit = '" + selectedGRNItemProfitPerSingleItem + "', stk_count ='" + newStockCount + "' WHERE stk_id = '" + stockId + "' ");

                                    String reOrderStockItemActivityLog = "INSERT INTO log(usr_id, usr_type, usr_username, log_activity, log_description, log_state) VALUES ((SELECT usr_id FROM user WHERE usr_username = '" + lblAdminUsername.getText() + "'),'Admin','" + lblAdminUsername.getText() + "','Re-Order Stock Item','Admin Re-Ordered Item " + txtGRNItemName1.getText() + "','SUCCESS')";
                                    String GRNItemCheckingActivityLog = "INSERT INTO log(usr_id, usr_type, usr_username, log_activity, log_description, log_state) VALUES ((SELECT usr_id FROM user WHERE usr_username = '" + lblAdminUsername.getText() + "'),'Admin','" + lblAdminUsername.getText() + "','GRN Status','Admin checked GRN item " + txtGRNItemName1.getText() + "','SUCCESS')";

                                    DB.DB.putData(reOrderStockItemActivityLog);
                                    DB.DB.putData(GRNItemCheckingActivityLog);

                                    ImageIcon icon = new ImageIcon(getClass().getResource("img/done.png"));
                                    JOptionPane.showMessageDialog(this, "GRN Item Checked Successfully!", "Done", JOptionPane.INFORMATION_MESSAGE, icon);
                                    JOptionPane.showMessageDialog(this, "Stock count Updated!", "Done", JOptionPane.INFORMATION_MESSAGE, icon);

                                    String selectedGRNItemId = ((String) tblGRN1.getValueAt(tblGRN1.getSelectedRow(), 0));

                                    DB.DB.putData("UPDATE grn SET grn_status = 'Checked' WHERE id = '" + selectedGRNItemId + "'");
                                    txtGRNStatus1.setText("Checked");
                                    DB.DB.putData("UPDATE item SET itm_stat = '1' WHERE itm_name = '" + txtGRNItemName1.getText() + "'");
                                    searchGRNItem1();
                                    refreshSupplierItemTable();
                                    refreshStockTable();
                                    refreshLowStockTable();
                                    refreshPOTable();
                                    refreshPOTable1();
                                    jTabbedPaneAddExistingItems.setSelectedIndex(2);

                                    String stockCount = Integer.toString(newStockCount);
                                    txtGRNItemStockCount1.setText(stockCount);

                                }
                            }

                            txtGRNItemName1.setText(null);
                            txtGRNStatus1.setText(null);
                            txtGRNItemQuantity1.setText(null);
                            txtGRNItemPrice1.setText(null);
                            txtGRNItemSellingPrice1.setText(null);
                            txtGRNItemLowStock1.setText(null);
                            txtGRNItemStockCount1.setText(null);

                        }

                    } else {
                        JOptionPane.showMessageDialog(this, "Please Fill All the Fields!", "Invalid Data", JOptionPane.WARNING_MESSAGE);
                    }
                }
            }
            DB.DB.con.close();
        } catch (Exception e) {
        }

    }

    private void calculateStockCount() {
        int count = tblStock.getRowCount();
        lblStockCount.setText("SKU Count : " + Integer.toString(count));
        lblStockCount1.setText("SKU Count : " + Integer.toString(count));

    }

    private void refreshStockTable() {
        try {

            DefaultTableModel dtm = (DefaultTableModel) tblStock.getModel();
            ResultSet rs = DB.DB.getData("SELECT * FROM stock WHERE stk_status = 'Available' AND (stk_count > stk_low_stock)");
            dtm.setRowCount(0);

            while (rs.next()) {
                String a = (rs.getString(1));
                String b = (rs.getString(2));
                String c = (rs.getString(3));
                String d = (rs.getString(4));
                String e = (rs.getString(5));
                String f = (rs.getString(7));
                String g = (rs.getString(8));
                String h = (rs.getString(9));
                dtm.addRow(new Object[]{a, b, c, d, e, f, g, h});

            }

            calculateStockCount();
            txtSearchStock.setText(null);
            txtSearchAvailableStock.setText(null);
            
            refreshLowStockTable();
            cmbPOItemName1.setSelectedItem("");
            txtPOItemPrice1.setText(null);

        } catch (Exception e) {
        }

    }

    private void searchStock() {
        switch (cmbSortStock.getSelectedIndex()) {
            case 0:
                
                

                try {
                DefaultTableModel dtm = (DefaultTableModel) tblStock.getModel();
                ResultSet rs = DB.DB.getData("SELECT * FROM stock WHERE stk_status = 'Available' AND (stk_count > stk_low_stock) AND stk_id = '" + txtSearchStock.getText() + "%' order by stk_id ");
                dtm.setRowCount(0);

                while (rs.next()) {
                    String a = (rs.getString(1));
                    String b = (rs.getString(2));
                    String c = (rs.getString(3));
                    String d = (rs.getString(4));
                    String e = (rs.getString(5));
                    String f = (rs.getString(7));
                    String g = (rs.getString(8));
                    String h = (rs.getString(9));
                    dtm.addRow(new Object[]{a, b, c, d, e, f, g, h});
                }
                calculateStockCount();

            } catch (Exception e) {
            }
            break;
            case 1:

                try {
                DefaultTableModel dtm = (DefaultTableModel) tblStock.getModel();
                ResultSet rs = DB.DB.getData("SELECT * FROM stock WHERE stk_status = 'Available' AND (stk_count > stk_low_stock) AND itm_code = '" + txtSearchStock.getText() + "%' order by itm_code ");
                dtm.setRowCount(0);

                while (rs.next()) {
                    String a = (rs.getString(1));
                    String b = (rs.getString(2));
                    String c = (rs.getString(3));
                    String d = (rs.getString(4));
                    String e = (rs.getString(5));
                    String f = (rs.getString(7));
                    String g = (rs.getString(8));
                    String h = (rs.getString(9));
                    dtm.addRow(new Object[]{a, b, c, d, e, f, g, h});
                }
                calculateStockCount();

            } catch (Exception e) {
            }
            break;
            case 2:
                try {
                DefaultTableModel dtm = (DefaultTableModel) tblStock.getModel();
                ResultSet rs = DB.DB.getData("SELECT * FROM stock WHERE stk_status = 'Available' AND (stk_count > stk_low_stock) AND itm_name = '" + txtSearchStock.getText() + "%' order by itm_name ");
                dtm.setRowCount(0);

                while (rs.next()) {
                    String a = (rs.getString(1));
                    String b = (rs.getString(2));
                    String c = (rs.getString(3));
                    String d = (rs.getString(4));
                    String e = (rs.getString(5));
                    String f = (rs.getString(7));
                    String g = (rs.getString(8));
                    String h = (rs.getString(9));
                    dtm.addRow(new Object[]{a, b, c, d, e, f, g, h});
                }
                calculateStockCount();
            } catch (Exception e) {
            }
            break;
            case 3:
                try {
                DefaultTableModel dtm = (DefaultTableModel) tblStock.getModel();
                ResultSet rs = DB.DB.getData("SELECT * FROM stock WHERE stk_status = 'Available' AND (stk_count > stk_low_stock) AND sup_username = '" + txtSearchStock.getText() + "%' order by sup_username ");
                dtm.setRowCount(0);

                while (rs.next()) {
                    String a = (rs.getString(1));
                    String b = (rs.getString(2));
                    String c = (rs.getString(3));
                    String d = (rs.getString(4));
                    String e = (rs.getString(5));
                    String f = (rs.getString(7));
                    String g = (rs.getString(8));
                    String h = (rs.getString(9));
                    dtm.addRow(new Object[]{a, b, c, d, e, f, g, h});
                }
                calculateStockCount();
            } catch (Exception e) {
            }
            break;
            case 4:
                try {

                DefaultTableModel dtm = (DefaultTableModel) tblStock.getModel();
                ResultSet rs = DB.DB.getData("SELECT * FROM stock WHERE stk_status = 'Available' AND (stk_count > stk_low_stock) AND stk_selling_price = '" + txtSearchStock.getText() + "%' order by stk_selling_price ");
                dtm.setRowCount(0);

                while (rs.next()) {
                    String a = (rs.getString(1));
                    String b = (rs.getString(2));
                    String c = (rs.getString(3));
                    String d = (rs.getString(4));
                    String e = (rs.getString(5));
                    String f = (rs.getString(7));
                    String g = (rs.getString(8));
                    String h = (rs.getString(9));
                    dtm.addRow(new Object[]{a, b, c, d, e, f, g, h});
                }
                calculateStockCount();
            } catch (Exception e) {
            }
            break;
            case 5:
                try {
                DefaultTableModel dtm = (DefaultTableModel) tblStock.getModel();
                ResultSet rs = DB.DB.getData("SELECT * FROM stock WHERE stk_status = 'Available' AND stk_id like '" + txtSearchStock.getText() + "%' order by stk_id ");
                dtm.setRowCount(0);

                while (rs.next()) {
                    String a = (rs.getString(1));
                    String b = (rs.getString(2));
                    String c = (rs.getString(3));
                    String d = (rs.getString(4));
                    String e = (rs.getString(5));
                    String f = (rs.getString(7));
                    String g = (rs.getString(8));
                    String h = (rs.getString(9));
                    dtm.addRow(new Object[]{a, b, c, d, e, f, g, h});
                }
                calculateStockCount();
            } catch (Exception e) {
            }
            break;
            case 6:
                try {
                DefaultTableModel dtm = (DefaultTableModel) tblStock.getModel();
                ResultSet rs = DB.DB.getData("SELECT * FROM stock WHERE stk_status = 'Available' AND (stk_count < stk_low_stock) AND stk_id like '" + txtSearchStock.getText() + "%' order by stk_id ");
                dtm.setRowCount(0);

                while (rs.next()) {
                    String a = (rs.getString(1));
                    String b = (rs.getString(2));
                    String c = (rs.getString(3));
                    String d = (rs.getString(4));
                    String e = (rs.getString(5));
                    String f = (rs.getString(7));
                    String g = (rs.getString(8));
                    String h = (rs.getString(9));
                    dtm.addRow(new Object[]{a, b, c, d, e, f, g, h});
                }
                calculateStockCount();
            } catch (Exception e) {
            }
            default:
                break;
        }
    }


    public void printStock() {

        new Thread() {
            @Override
            public void run() {
                try {

                    String reportSource = "C:\\Program Files\\Exfiltro Reports\\viewStockReport.jrxml";

                    JasperDesign jdesign = JRXmlLoader.load(reportSource);
                    JasperReport jreport = JasperCompileManager.compileReport(jdesign);
                    JasperPrint jprint = JasperFillManager.fillReport(jreport, null, DB.DB.getConnection());

                    JasperViewer.viewReport(jprint, false);

                } catch (JRException ex) {
                }
            }
        }.start();
    }

    public void printBarcodes() {

        new Thread() {
            @Override
            public void run() {
                try {

                    String reportSource = "C:\\Program Files\\Exfiltro Reports\\viewBarcodeReport.jrxml";

                    JasperDesign jdesign = JRXmlLoader.load(reportSource);
                    JasperReport jreport = JasperCompileManager.compileReport(jdesign);
                    JasperPrint jprint = JasperFillManager.fillReport(jreport, null, DB.DB.getConnection());

                    JasperViewer.viewReport(jprint, false);

                } catch (JRException ex) {

                }
            }
        }.start();
    }

    private void calculateLowStockCount() {
        int count = tblLowStock.getRowCount();
        lblLowStockCount.setText("SKU Count : " + Integer.toString(count));
    }

    private void refreshLowStockTable() {
        try {

            DefaultTableModel dtm = (DefaultTableModel) tblLowStock.getModel();
            ResultSet rs = DB.DB.getData("SELECT * FROM stock WHERE stk_status = 'Available' AND (stk_count < stk_low_stock)");
            dtm.setRowCount(0);

            while (rs.next()) {
                String a = (rs.getString(1));
                String b = (rs.getString(2));
                String c = (rs.getString(3));
                String d = (rs.getString(4));
                String e = (rs.getString(5));
                String f = (rs.getString(7));
                String g = (rs.getString(8));
                String h = (rs.getString(9));
                dtm.addRow(new Object[]{a, b, c, d, e, f, g, h});

            }

            calculateLowStockCount();
            txtSearchLowStock.setText(null);

        } catch (Exception e) {
        }

    }

    private void searchLowStock() {
        switch (cmbSortLowStock.getSelectedIndex()) {
            case 0:
                
                try {
                DefaultTableModel dtm = (DefaultTableModel) tblLowStock.getModel();
                ResultSet rs = DB.DB.getData("SELECT * FROM stock WHERE stk_status = 'Available' AND (stk_count < stk_low_stock) AND stk_id like '" + txtSearchLowStock.getText() + "%' order by stk_id ");
                dtm.setRowCount(0);

                while (rs.next()) {
                    String a = (rs.getString(1));
                    String b = (rs.getString(2));
                    String c = (rs.getString(3));
                    String d = (rs.getString(4));
                    String e = (rs.getString(5));
                    String f = (rs.getString(7));
                    String g = (rs.getString(8));
                    String h = (rs.getString(9));
                    dtm.addRow(new Object[]{a, b, c, d, e, f, g, h});
                }
                calculateLowStockCount();

            } catch (Exception e) {
            }
            break;
            case 1:

                try {
                DefaultTableModel dtm = (DefaultTableModel) tblLowStock.getModel();
                ResultSet rs = DB.DB.getData("SELECT * FROM stock WHERE stk_status = 'Available' AND (stk_count < stk_low_stock) AND itm_code like '" + txtSearchLowStock.getText() + "%' order by itm_code ");
                dtm.setRowCount(0);

                while (rs.next()) {
                    String a = (rs.getString(1));
                    String b = (rs.getString(2));
                    String c = (rs.getString(3));
                    String d = (rs.getString(4));
                    String e = (rs.getString(5));
                    String f = (rs.getString(7));
                    String g = (rs.getString(8));
                    String h = (rs.getString(9));
                    dtm.addRow(new Object[]{a, b, c, d, e, f, g, h});
                }
                calculateLowStockCount();

            } catch (Exception e) {
            }
            break;
            case 2:
                try {
                DefaultTableModel dtm = (DefaultTableModel) tblLowStock.getModel();
                ResultSet rs = DB.DB.getData("SELECT * FROM stock WHERE stk_status = 'Available' AND (stk_count < stk_low_stock) AND itm_name like '" + txtSearchLowStock.getText() + "%' order by itm_name ");
                dtm.setRowCount(0);

                while (rs.next()) {
                    String a = (rs.getString(1));
                    String b = (rs.getString(2));
                    String c = (rs.getString(3));
                    String d = (rs.getString(4));
                    String e = (rs.getString(5));
                    String f = (rs.getString(7));
                    String g = (rs.getString(8));
                    String h = (rs.getString(9));
                    dtm.addRow(new Object[]{a, b, c, d, e, f, g, h});
                }
                calculateLowStockCount();
            } catch (Exception e) {
            }
            break;
            case 3:
                try {
                DefaultTableModel dtm = (DefaultTableModel) tblLowStock.getModel();
                ResultSet rs = DB.DB.getData("SELECT * FROM stock WHERE stk_status = 'Available' AND (stk_count < stk_low_stock) AND sup_username like '" + txtSearchLowStock.getText() + "%' order by sup_username ");
                dtm.setRowCount(0);

                while (rs.next()) {
                    String a = (rs.getString(1));
                    String b = (rs.getString(2));
                    String c = (rs.getString(3));
                    String d = (rs.getString(4));
                    String e = (rs.getString(5));
                    String f = (rs.getString(7));
                    String g = (rs.getString(8));
                    String h = (rs.getString(9));
                    dtm.addRow(new Object[]{a, b, c, d, e, f, g, h});
                }
                calculateLowStockCount();
            } catch (Exception e) {
            }
            break;
            default:
                break;
        }
    }

    public void printLowStock() {

        new Thread() {
            @Override
            public void run() {
                try {

                    String reportSource = "C:\\Program Files\\Exfiltro Reports\\viewLowStockReport.jrxml";

                    JasperDesign jdesign = JRXmlLoader.load(reportSource);
                    JasperReport jreport = JasperCompileManager.compileReport(jdesign);
                    JasperPrint jprint = JasperFillManager.fillReport(jreport, null, DB.DB.getConnection());

                    JasperViewer.viewReport(jprint, false);

                } catch (JRException ex) {

                }
            }
        }.start();
    }

    private void calculateAvailableStockCount() {
        int count = tblAvailableStock.getRowCount();
        lblStockCount1.setText("SKU Count : " + Integer.toString(count));

    }

    private void refreshAvailableStockTable() {
        try {

            DefaultTableModel dtm = (DefaultTableModel) tblAvailableStock.getModel();
            ResultSet rs = DB.DB.getData("SELECT * FROM stock WHERE stk_status = 'Available' AND (stk_count > stk_low_stock)");
            dtm.setRowCount(0);

            while (rs.next()) {
                String a = (rs.getString(1));
                String b = (rs.getString(2));
                String c = (rs.getString(3));
                String d = (rs.getString(4));
                String e = (rs.getString(5));
                String f = (rs.getString(7));
                String g = (rs.getString(8));
                String h = (rs.getString(9));
                dtm.addRow(new Object[]{a, b, c, d, e, f, g, h});

            }

            calculateAvailableStockCount();
            txtSearchAvailableStock.setText(null);

        } catch (Exception e) {
        }

    }

    private void searchAvailableStock() {
        switch (cmbSortAvailableStock.getSelectedIndex()) {
            case 0:
                
                try {
                DefaultTableModel dtm = (DefaultTableModel) tblAvailableStock.getModel();
                ResultSet rs = DB.DB.getData("SELECT * FROM stock WHERE stk_status = 'Available' AND (stk_count > low_stk) AND stk_id = '" + txtSearchAvailableStock.getText() + "' order by stk_id ");
                dtm.setRowCount(0);

                while (rs.next()) {
                    String a = (rs.getString(1));
                    String b = (rs.getString(2));
                    String c = (rs.getString(3));
                    String d = (rs.getString(4));
                    String e = (rs.getString(5));
                    String f = (rs.getString(7));
                    String g = (rs.getString(8));
                    String h = (rs.getString(9));
                    dtm.addRow(new Object[]{a, b, c, d, e, f, g, h});
                }
                calculateAvailableStockCount();

            } catch (Exception e) {
            }
            break;
            case 1:

                try {
                DefaultTableModel dtm = (DefaultTableModel) tblAvailableStock.getModel();
                ResultSet rs = DB.DB.getData("SELECT * FROM stock WHERE stk_status = 'Available' AND (stk_count > low_stk) AND itm_code like '" + txtSearchAvailableStock.getText() + "%' order by itm_code ");
                dtm.setRowCount(0);

                while (rs.next()) {
                    String a = (rs.getString(1));
                    String b = (rs.getString(2));
                    String c = (rs.getString(3));
                    String d = (rs.getString(4));
                    String e = (rs.getString(5));
                    String f = (rs.getString(7));
                    String g = (rs.getString(8));
                    String h = (rs.getString(9));
                    dtm.addRow(new Object[]{a, b, c, d, e, f, g, h});
                }
                calculateAvailableStockCount();

            } catch (Exception e) {
            }
            break;
            case 2:
                try {
                DefaultTableModel dtm = (DefaultTableModel) tblAvailableStock.getModel();
                ResultSet rs = DB.DB.getData("SELECT * FROM stock WHERE stk_status = 'Available' AND (stk_count > low_stk) AND itm_name like '" + txtSearchAvailableStock.getText() + "%' order by itm_name ");
                dtm.setRowCount(0);

                while (rs.next()) {
                    String a = (rs.getString(1));
                    String b = (rs.getString(2));
                    String c = (rs.getString(3));
                    String d = (rs.getString(4));
                    String e = (rs.getString(5));
                    String f = (rs.getString(7));
                    String g = (rs.getString(8));
                    String h = (rs.getString(9));
                    dtm.addRow(new Object[]{a, b, c, d, e, f, g, h});
                }
                calculateAvailableStockCount();
            } catch (Exception e) {
            }
            break;
            case 3:
                try {
                DefaultTableModel dtm = (DefaultTableModel) tblAvailableStock.getModel();
                ResultSet rs = DB.DB.getData("SELECT * FROM stock WHERE stk_status = 'Available' AND (stk_count > low_stk) AND sup_username like '" + txtSearchAvailableStock.getText() + "%' order by sup_username ");
                dtm.setRowCount(0);

                while (rs.next()) {
                    String a = (rs.getString(1));
                    String b = (rs.getString(2));
                    String c = (rs.getString(3));
                    String d = (rs.getString(4));
                    String e = (rs.getString(5));
                    String f = (rs.getString(7));
                    String g = (rs.getString(8));
                    String h = (rs.getString(9));
                    dtm.addRow(new Object[]{a, b, c, d, e, f, g, h});
                }
                calculateAvailableStockCount();
            } catch (Exception e) {
            }
            break;
            case 4:
                try {

                DefaultTableModel dtm = (DefaultTableModel) tblAvailableStock.getModel();
                ResultSet rs = DB.DB.getData("SELECT * FROM stock WHERE stk_status = 'Available' AND (stk_count > low_stk) AND stk_selling_price like '" + txtSearchAvailableStock.getText() + "%' order by stk_selling_price ");
                dtm.setRowCount(0);

                while (rs.next()) {
                    String a = (rs.getString(1));
                    String b = (rs.getString(2));
                    String c = (rs.getString(3));
                    String d = (rs.getString(4));
                    String e = (rs.getString(5));
                    String f = (rs.getString(7));
                    String g = (rs.getString(8));
                    String h = (rs.getString(9));
                    dtm.addRow(new Object[]{a, b, c, d, e, f, g, h});
                }
                calculateAvailableStockCount();
            } catch (Exception e) {
            }
            break;
            case 5:
                try {
                DefaultTableModel dtm = (DefaultTableModel) tblAvailableStock.getModel();
                ResultSet rs = DB.DB.getData("SELECT * FROM stock WHERE stk_status = 'Available' AND stk_id like '" + txtSearchAvailableStock.getText() + "%' order by stk_id ");
                dtm.setRowCount(0);

                while (rs.next()) {
                    String a = (rs.getString(1));
                    String b = (rs.getString(2));
                    String c = (rs.getString(3));
                    String d = (rs.getString(4));
                    String e = (rs.getString(5));
                    String f = (rs.getString(7));
                    String g = (rs.getString(8));
                    String h = (rs.getString(9));
                    dtm.addRow(new Object[]{a, b, c, d, e, f, g, h});
                }
                calculateAvailableStockCount();
            } catch (Exception e) {
            }
            break;
            case 6:
                try {
                DefaultTableModel dtm = (DefaultTableModel) tblAvailableStock.getModel();
                ResultSet rs = DB.DB.getData("SELECT * FROM stock WHERE stk_status = 'Available' AND (stk_count < low_stk) AND stk_id like '" + txtSearchAvailableStock.getText() + "%' order by stk_id ");
                dtm.setRowCount(0);

                while (rs.next()) {
                    String a = (rs.getString(1));
                    String b = (rs.getString(2));
                    String c = (rs.getString(3));
                    String d = (rs.getString(4));
                    String e = (rs.getString(5));
                    String f = (rs.getString(7));
                    String g = (rs.getString(8));
                    String h = (rs.getString(9));
                    dtm.addRow(new Object[]{a, b, c, d, e, f, g, h});
                }
                calculateAvailableStockCount();
            } catch (Exception e) {
            }
            default:
                break;
        }
    }

    private void deleteStockItem() {
        int option = JOptionPane.showConfirmDialog(this, "Do you want to Permanently Delete this Stock Item?", "", JOptionPane.YES_NO_OPTION);
        if (option == 0) {
            try {

                DB.DB.putData("UPDATE stock SET stk_status = 'Unavailable' WHERE stk_id ='" + txtAvailableSKUId.getText() + "' ");
                String deleteStockItemActivityLog = "INSERT INTO log(usr_id, usr_type, usr_username, log_activity, log_description, log_state) VALUES ((SELECT usr_id FROM user WHERE usr_username = '" + lblAdminUsername.getText() + "'),'Admin','" + lblAdminUsername.getText() + "','Delete Stock Item','Admin deleted stock item " + txtAvailableItemName.getText() + "','SUCCESS')";
                DB.DB.putData(deleteStockItemActivityLog);

                ImageIcon icon = new ImageIcon(getClass().getResource("img/done.png"));
                JOptionPane.showMessageDialog(this, "Stock Item Permanently Deleted!", "Done", JOptionPane.INFORMATION_MESSAGE, icon);

                txtAvailableSKUId.setText(null);
                txtAvailableItemCode.setText(null);
                txtAvailableItemName.setText(null);
                txtAvailableSupplier.setText(null);
                txtAvailableStockCount.setText(null);

                refreshAvailableStockTable();
            } catch (Exception e) {
            }
        } else {

        }
    }

    public void printAvailableStock() {

        new Thread() {
            @Override
            public void run() {
                try {

                    String reportSource = "C:\\Program Files\\Exfiltro Reports\\viewStockReport.jrxml";

                    JasperDesign jdesign = JRXmlLoader.load(reportSource);
                    JasperReport jreport = JasperCompileManager.compileReport(jdesign);
                    JasperPrint jprint = JasperFillManager.fillReport(jreport, null, DB.DB.getConnection());

                    JasperViewer.viewReport(jprint, false);

                } catch (JRException ex) {

                }
            }
        }.start();
    }

    private void calculateStockReturnCount() {
        int count = tblStockReturn.getRowCount();
        lblStockReturnCount.setText("Item Count : " + Integer.toString(count));

    }

    private void refreshStockReturnTable() {
        try {

            DefaultTableModel dtm = (DefaultTableModel) tblStockReturn.getModel();
            ResultSet rs = DB.DB.getData("SELECT * FROM stock_return order by ret_timestamp desc ");
            dtm.setRowCount(0);

            while (rs.next()) {
                String a = (rs.getString(2));
                String b = (rs.getString(3));
                String c = (rs.getString(4));
                String d = (rs.getString(5));
                String e = (rs.getString(6));
                String f = (rs.getString(7));
                dtm.addRow(new Object[]{a, b, c, d, e, f});

            }

            calculateStockReturnCount();
            txtSearchStockReturn.setText(null);

        } catch (Exception e) {
        }

    }

    private void searchStockReturn() {
        switch (cmbSortStockReturn.getSelectedIndex()) {
            case 0:
                
                try {
                DefaultTableModel dtm = (DefaultTableModel) tblStockReturn.getModel();
                ResultSet rs = DB.DB.getData("SELECT * FROM stock_return WHERE stk_id like '" + txtSearchStockReturn.getText() + "%' order by stk_id");
                dtm.setRowCount(0);

                while (rs.next()) {
                    String a = (rs.getString(2));
                    String b = (rs.getString(3));
                    String c = (rs.getString(4));
                    String d = (rs.getString(5));
                    String e = (rs.getString(6));
                    String f = (rs.getString(7));
                    dtm.addRow(new Object[]{a, b, c, d, e, f});

                }

                calculateStockReturnCount();

            } catch (Exception e) {
            }
            break;
            case 1:

                try {
                DefaultTableModel dtm = (DefaultTableModel) tblStockReturn.getModel();
                ResultSet rs = DB.DB.getData("SELECT * FROM stock_return WHERE itm_code like '" + txtSearchStockReturn.getText() + "%' order by itm_code");
                dtm.setRowCount(0);

                while (rs.next()) {
                    String a = (rs.getString(2));
                    String b = (rs.getString(3));
                    String c = (rs.getString(4));
                    String d = (rs.getString(5));
                    String e = (rs.getString(6));
                    String f = (rs.getString(7));
                    dtm.addRow(new Object[]{a, b, c, d, e, f});

                }

                calculateStockReturnCount();

            } catch (Exception e) {
            }
            break;
            case 2:
                try {
                DefaultTableModel dtm = (DefaultTableModel) tblStockReturn.getModel();
                ResultSet rs = DB.DB.getData("SELECT * FROM stock_return WHERE itm_name like '" + txtSearchStockReturn.getText() + "%' order by itm_name");
                dtm.setRowCount(0);

                while (rs.next()) {
                    String a = (rs.getString(2));
                    String b = (rs.getString(3));
                    String c = (rs.getString(4));
                    String d = (rs.getString(5));
                    String e = (rs.getString(6));
                    String f = (rs.getString(7));
                    dtm.addRow(new Object[]{a, b, c, d, e, f});

                }

                calculateStockReturnCount();
            } catch (Exception e) {
            }
            break;
            case 3:
                try {
                DefaultTableModel dtm = (DefaultTableModel) tblStockReturn.getModel();
                ResultSet rs = DB.DB.getData("SELECT * FROM stock_return WHERE sup_username like '" + txtSearchStockReturn.getText() + "%' order by sup_username");
                dtm.setRowCount(0);

                while (rs.next()) {
                    String a = (rs.getString(2));
                    String b = (rs.getString(3));
                    String c = (rs.getString(4));
                    String d = (rs.getString(5));
                    String e = (rs.getString(6));
                    String f = (rs.getString(7));
                    dtm.addRow(new Object[]{a, b, c, d, e, f});

                }

                calculateStockReturnCount();
            } catch (Exception e) {
            }
            break;
            default:
                break;
        }
    }

    private void returnStockItem() {
        int option = JOptionPane.showConfirmDialog(this, "Do you want to Return these Items?", "", JOptionPane.YES_NO_OPTION);
        if (option == 0) {

            if (!txtAvailableSKUId.getText().equals("")
                    & (!txtAvailableItemCode.getText().equals(""))
                    & (!txtAvailableItemName.getText().equals(""))
                    & (!txtAvailableSupplier.getText().equals(""))
                    & (!txtAvailableStockCount.getText().equals(""))
                    & (!txtReturnCount.getText().equals(""))) {

                try {

                    if (Integer.parseInt(txtReturnCount.getText()) > Integer.parseInt(txtAvailableStockCount.getText())) {
                        JOptionPane.showMessageDialog(this, "No Sufficient Stock Count Available!", "Invalid Data", JOptionPane.WARNING_MESSAGE);

                    } else {

                        int currentStockCount = Integer.parseInt(txtAvailableStockCount.getText());
                        int returnAmount = Integer.parseInt(txtReturnCount.getText());
                        int newStockCount = currentStockCount - returnAmount;

                        DB.DB.putData("UPDATE stock SET stk_count ='" + newStockCount + "' WHERE itm_code = '" + txtAvailableItemCode.getText() + "' ");
                        String returnStockItemActivityLog = "INSERT INTO log(usr_id, usr_type, usr_username, log_activity, log_description, log_state) VALUES ((SELECT usr_id FROM user WHERE usr_username = '" + lblAdminUsername.getText() + "'),'Admin','" + lblAdminUsername.getText() + "','Return Stock Item','Admin returned stock item " + txtAvailableItemName.getText() + "','SUCCESS')";

                        DB.DB.putData(returnStockItemActivityLog);

                        String addToReturnHistory = "INSERT INTO stock_return(stk_id, itm_code, itm_name, sup_username, ret_return_count) VALUES ('" + txtAvailableSKUId.getText() + "', '" + txtAvailableItemCode.getText() + "', '" + txtAvailableItemName.getText() + "', '" + txtAvailableSupplier.getText() + "', '" + txtReturnCount.getText() + "') ";
                        DB.DB.putData(addToReturnHistory);

                        ImageIcon icon = new ImageIcon(getClass().getResource("img/done.png"));
                        JOptionPane.showMessageDialog(this, "Stock Item Returned!", "Done", JOptionPane.INFORMATION_MESSAGE, icon);
                        JOptionPane.showMessageDialog(this, "Stock Updated!", "Done", JOptionPane.INFORMATION_MESSAGE, icon);

                    }

                    printSelectedStockReturn();
                    refreshStockTable();
                    refreshStockReturnTable();

                } catch (Exception ex) {
                }

            } else {
                JOptionPane.showMessageDialog(this, "Please Fill All the Fields!", "Invalid Data", JOptionPane.WARNING_MESSAGE);
            }

        }

    }

    public void printSelectedStockReturn() {

        new Thread() {
            @Override
            public void run() {
                try {

                    String reportSource = "C:\\Program Files\\Exfiltro Reports\\viewSelectedStockReturnReport.jrxml";

                    JasperDesign jdesign = JRXmlLoader.load(reportSource);
                    JasperReport jreport = JasperCompileManager.compileReport(jdesign);
                    JasperPrint jprint = JasperFillManager.fillReport(jreport, null, DB.DB.getConnection());

                    JasperViewer.viewReport(jprint, false);

                } catch (JRException ex) {

                }
            }
        }.start();
    }

    public void printStockReturn() {

        new Thread() {
            @Override
            public void run() {
                try {

                    String reportSource = "C:\\Program Files\\Exfiltro Reports\\viewStockReturnReport.jrxml";

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
                new AdminStock().setVisible(true);
            }
        });
    }

    private void setIcon() {
        setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("img/exfiltrat0z-icon.png")));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox<String> cmbItemCategory;
    private javax.swing.JComboBox<String> cmbItemSupplier;
    private javax.swing.JComboBox<String> cmbPOItemName;
    private javax.swing.JComboBox<String> cmbPOItemName1;
    private javax.swing.JComboBox<String> cmbPOItemSupplier;
    private javax.swing.JComboBox<String> cmbPOItemSupplier1;
    private javax.swing.JComboBox<String> cmbPOLeadTime;
    private javax.swing.JComboBox<String> cmbPOLeadTime1;
    private javax.swing.JComboBox<String> cmbSelectedItemSupplierItemCategory;
    private javax.swing.JComboBox<String> cmbSortAvailableStock;
    private javax.swing.JComboBox<String> cmbSortGRNItem;
    private javax.swing.JComboBox<String> cmbSortGRNItem1;
    private javax.swing.JComboBox<String> cmbSortLowStock;
    private javax.swing.JComboBox<String> cmbSortPOItem;
    private javax.swing.JComboBox<String> cmbSortPOItem1;
    private javax.swing.JComboBox<String> cmbSortStock;
    private javax.swing.JComboBox<String> cmbSortStockReturn;
    private javax.swing.JComboBox<String> cmbSortSupplierItem;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton11;
    private javax.swing.JButton jButton12;
    private javax.swing.JButton jButton14;
    private javax.swing.JButton jButton15;
    private javax.swing.JButton jButton16;
    private javax.swing.JButton jButton17;
    private javax.swing.JButton jButton18;
    private javax.swing.JButton jButton19;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton20;
    private javax.swing.JButton jButton21;
    private javax.swing.JButton jButton22;
    private javax.swing.JButton jButton23;
    private javax.swing.JButton jButton24;
    private javax.swing.JButton jButton25;
    private javax.swing.JButton jButton26;
    private javax.swing.JButton jButton27;
    private javax.swing.JButton jButton28;
    private javax.swing.JButton jButton29;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton30;
    private javax.swing.JButton jButton31;
    private javax.swing.JButton jButton32;
    private javax.swing.JButton jButton33;
    private javax.swing.JButton jButton34;
    private javax.swing.JButton jButton35;
    private javax.swing.JButton jButton36;
    private javax.swing.JButton jButton37;
    private javax.swing.JButton jButton38;
    private javax.swing.JButton jButton39;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton40;
    private javax.swing.JButton jButton41;
    private javax.swing.JButton jButton42;
    private javax.swing.JButton jButton43;
    private javax.swing.JButton jButton44;
    private javax.swing.JButton jButton45;
    private javax.swing.JButton jButton46;
    private javax.swing.JButton jButton48;
    private javax.swing.JButton jButton49;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton7;
    private javax.swing.JButton jButton8;
    private javax.swing.JButton jButton9;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
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
    private javax.swing.JLabel jLabel39;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel40;
    private javax.swing.JLabel jLabel41;
    private javax.swing.JLabel jLabel42;
    private javax.swing.JLabel jLabel43;
    private javax.swing.JLabel jLabel44;
    private javax.swing.JLabel jLabel45;
    private javax.swing.JLabel jLabel46;
    private javax.swing.JLabel jLabel47;
    private javax.swing.JLabel jLabel48;
    private javax.swing.JLabel jLabel49;
    private javax.swing.JLabel jLabel50;
    private javax.swing.JLabel jLabel51;
    private javax.swing.JLabel jLabel52;
    private javax.swing.JLabel jLabel54;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel60;
    private javax.swing.JLabel jLabel62;
    private javax.swing.JLabel jLabel64;
    private javax.swing.JLabel jLabel65;
    private javax.swing.JLabel jLabel66;
    private javax.swing.JLabel jLabel67;
    private javax.swing.JLabel jLabel68;
    private javax.swing.JLabel jLabel69;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel13;
    private javax.swing.JPanel jPanel14;
    private javax.swing.JPanel jPanel15;
    private javax.swing.JPanel jPanel17;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel20;
    private javax.swing.JPanel jPanel21;
    private javax.swing.JPanel jPanel22;
    private javax.swing.JPanel jPanel23;
    private javax.swing.JPanel jPanel24;
    private javax.swing.JPanel jPanel25;
    private javax.swing.JPanel jPanel26;
    private javax.swing.JPanel jPanel27;
    private javax.swing.JPanel jPanel28;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JScrollPane jScrollPane7;
    private javax.swing.JScrollPane jScrollPane8;
    private javax.swing.JScrollPane jScrollPane9;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JTabbedPane jTabbedPaneAddExistingItems;
    private javax.swing.JTabbedPane jTabbedPaneAddItem;
    private javax.swing.JTabbedPane jTabbedPaneAddNewItems;
    private javax.swing.JTabbedPane jTabbedPaneGRNChecking;
    private javax.swing.JTabbedPane jTabbedPaneStock;
    private javax.swing.JTabbedPane jTabbedPaneStockMgt;
    private javax.swing.JTabbedPane jTabbedPaneStockReturn;
    public static final javax.swing.JLabel lblAdminUsername = new javax.swing.JLabel();
    private javax.swing.JLabel lblDate;
    private javax.swing.JLabel lblGRNId;
    private javax.swing.JLabel lblGRNId1;
    private javax.swing.JLabel lblGRNId3;
    private javax.swing.JLabel lblGRNTotal;
    private javax.swing.JLabel lblGRNTotal1;
    private javax.swing.JLabel lblItemCount;
    private javax.swing.JLabel lblLowStockCount;
    private javax.swing.JLabel lblPOId;
    private javax.swing.JLabel lblPOId1;
    private javax.swing.JLabel lblPOId2;
    private javax.swing.JLabel lblPOId3;
    private javax.swing.JLabel lblPOId4;
    private javax.swing.JLabel lblStockCount;
    private javax.swing.JLabel lblStockCount1;
    private javax.swing.JLabel lblStockReturnCount;
    private javax.swing.JLabel lblTime;
    private javax.swing.JLabel lblTotal;
    private javax.swing.JLabel lblTotal1;
    private javax.swing.JLabel lblTotal2;
    private javax.swing.JLabel lblTotal3;
    private javax.swing.JLabel lblTotal33;
    private javax.swing.JLabel lblTotal4;
    private javax.swing.JPanel pnlHomeSubSelection;
    private javax.swing.JPanel pnlLogout;
    private javax.swing.JTable tblAvailableStock;
    private javax.swing.JTable tblGRN;
    private javax.swing.JTable tblGRN1;
    private javax.swing.JTable tblLowStock;
    private javax.swing.JTable tblPOItem;
    private javax.swing.JTable tblPOItem1;
    private javax.swing.JTable tblStock;
    private javax.swing.JTable tblStockReturn;
    private javax.swing.JTable tblSupplierItem;
    private javax.swing.JTextField txtAvailableItemCode;
    private javax.swing.JTextField txtAvailableItemName;
    private javax.swing.JTextField txtAvailableSKUId;
    private javax.swing.JTextField txtAvailableStockCount;
    private javax.swing.JTextField txtAvailableSupplier;
    private javax.swing.JTextField txtGRNItemLowStock;
    private javax.swing.JTextField txtGRNItemLowStock1;
    private javax.swing.JTextField txtGRNItemName;
    private javax.swing.JTextField txtGRNItemName1;
    private javax.swing.JTextField txtGRNItemPrice;
    private javax.swing.JTextField txtGRNItemPrice1;
    private javax.swing.JTextField txtGRNItemQuantity;
    private javax.swing.JTextField txtGRNItemQuantity1;
    private javax.swing.JTextField txtGRNItemSellingPrice;
    private javax.swing.JTextField txtGRNItemSellingPrice1;
    private javax.swing.JTextField txtGRNItemStockCount;
    private javax.swing.JTextField txtGRNItemStockCount1;
    private javax.swing.JTextField txtGRNStatus;
    private javax.swing.JTextField txtGRNStatus1;
    private javax.swing.JTextField txtItemCode;
    private javax.swing.JTextField txtItemName;
    private javax.swing.JTextField txtPOId;
    private javax.swing.JTextField txtPOId1;
    private javax.swing.JTextField txtPOItemPrice;
    private javax.swing.JTextField txtPOItemPrice1;
    private javax.swing.JTextField txtPOItemQuantity;
    private javax.swing.JTextField txtPOItemQuantity1;
    private javax.swing.JTextField txtReturnCount;
    private javax.swing.JTextField txtSearchAvailableStock;
    private javax.swing.JTextField txtSearchGRNItem;
    private javax.swing.JTextField txtSearchGRNItem1;
    private javax.swing.JTextField txtSearchLowStock;
    private javax.swing.JTextField txtSearchPOItem;
    private javax.swing.JTextField txtSearchPOItem1;
    private javax.swing.JTextField txtSearchStock;
    private javax.swing.JTextField txtSearchStockReturn;
    private javax.swing.JTextField txtSearchSupplierItem;
    private javax.swing.JTextField txtSelectedItemSupplierId;
    private javax.swing.JTextField txtSelectedItemSupplierItemCode;
    private javax.swing.JTextField txtSelectedItemSupplierItemName;
    private javax.swing.JTextField txtSelectedItemSupplierUsername;
    // End of variables declaration//GEN-END:variables
}
