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
import java.sql.ResultSet;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.Timer;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.LineBorder;
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
public class EmployeePOS extends javax.swing.JFrame {

    Timer timer;
    String month;
    String year;
    String date;
    String time;
    String today;
    int discount;

    public EmployeePOS() {
        initComponents();
        EmployeePOS.this.setExtendedState(JFrame.MAXIMIZED_BOTH);
        EmployeePOS.this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setIcon();
        setTableheader();
        refreshInvoiceTable();
        generateInvoiceId();

        EmployeePOS.this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {

                EmployeePOS.this.dispose();
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
        JTableHeader emptbl = tblInvoice.getTableHeader();
        emptbl.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        emptbl.setForeground(Color.white);
        emptbl.setBackground(new Color(51, 51, 51));
        tblInvoice.getTableHeader().setReorderingAllowed(false);
    }

    private void generateInvoiceId() {
        try {

            ResultSet rs = DB.DB.getData("SELECT invoice_id AS x FROM gin ORDER BY invoice_id DESC LIMIT 1");
            if (rs.next()) {

                int rowcount = Integer.parseInt(rs.getString("x"));
                rowcount++;

                this.txtInvoiceId.setText("" + rowcount);
                txtSearchInvoice.setText(txtInvoiceId.getText());

                lblInvoiceId.setText("Invoice Id : " + txtSearchInvoice.getText());
            }

        } catch (Exception e) {
        }
    }

    private void addInvoiceItem() {
        int option = JOptionPane.showConfirmDialog(this, "Do you want to Add this Item to Invoice Item List?", "", JOptionPane.YES_NO_OPTION);
        if (option == 0) {

            if (!txtInvoiceItemCode.getText().equals("")
                    & (!txtInvoiceItemName.getText().equals(""))
                    & (!txtInvoiceItemPurchasePrice.getText().equals(""))
                    & (!txtInvoiceItemSellingPrice.getText().equals(""))
                    & (!txtInvoiceNewItemPrice.getText().equals(""))
                    & (!txtInvoiceItemAmount.getText().equals(""))) {

                try {

                    ResultSet rs = DB.DB.getData("SELECT itm_name FROM gin WHERE itm_code = '" + txtInvoiceItemCode.getText() + "' AND invoice_id = '" + txtInvoiceId.getText() + "' ");

                    if (rs.next()) {
                        JOptionPane.showMessageDialog(this, "Item Already Added to Invoice!", "Invalid Data", JOptionPane.WARNING_MESSAGE);

                    } else if (Integer.parseInt(txtInvoiceItemAmount.getText()) > Integer.parseInt(txtInvoiceItemStockCount.getText())) {
                        JOptionPane.showMessageDialog(this, "No Sufficient Stock Count Available!", "Invalid Data", JOptionPane.WARNING_MESSAGE);

                    } else if ((Integer.parseInt(txtInvoiceItemPurchasePrice.getText())) > (Integer.parseInt(txtInvoiceNewItemPrice.getText()))) {
                        txtInvoiceItemAmount.setText(null);
                        JOptionPane.showMessageDialog(this, "New Selling Price of this Item is Too Low, Please Lower the Discount!");

                        cmbInvoiceItemDiscount.setPopupVisible(true);
                        cmbInvoiceItemDiscount.grabFocus();

                    } else {

                        switch (cmbInvoiceItemDiscount.getSelectedIndex()) {
                            case 0: {
                                discount = 0;
                                break;
                            }
                            case 1: {
                                discount = 5;
                                break;
                            }
                            case 2: {
                                discount = 10;
                                break;
                            }
                            case 3: {
                                discount = 15;
                                break;
                            }
                            case 4: {
                                discount = 20;
                                break;
                            }
                            case 5: {
                                discount = 25;
                                break;
                            }
                            case 6: {
                                discount = 30;
                                break;
                            }
                            case 7: {
                                discount = 35;
                                break;
                            }
                            default:
                                break;
                        }

                        int totalWithDiscount = Integer.parseInt(txtInvoiceNewItemPrice.getText()) * Integer.parseInt(txtInvoiceItemAmount.getText()); // With Savings 
                        int totalWithoutDiscount = Integer.parseInt(txtInvoiceItemSellingPrice.getText()) * Integer.parseInt(txtInvoiceItemAmount.getText());  // Without Savings [Savings = Rs.0]
                        int savings = totalWithoutDiscount - totalWithDiscount;
                        int profitPerSingleItemPerEachItem = Integer.parseInt(txtInvoiceNewItemPrice.getText()) - Integer.parseInt(txtInvoiceItemPurchasePrice.getText()); // Profit per each item [WITH DISCOUNT]
                        int ptofitPerItemAmountPerEachItem = profitPerSingleItemPerEachItem * Integer.parseInt(txtInvoiceItemAmount.getText()); //Profite per item amount of specific item [WITH DISCOUNT]

                        Date DateMonth = new Date();
                        SimpleDateFormat toMonth = new SimpleDateFormat("MMMM");
                        month = "" + toMonth.format(DateMonth);

                        Date DateYear = new Date();
                        SimpleDateFormat toYear = new SimpleDateFormat("yyyy");
                        year = "" + toYear.format(DateYear);

                        Date DateDate = new Date();
                        SimpleDateFormat toDate = new SimpleDateFormat("yyyy-MM-dd");
                        date = "" + toDate.format(DateDate);

                        String addInvoiceItem = "INSERT INTO gin(invoice_id, itm_id, itm_code, itm_name, stk_itm_price, stk_selling_price, gin_discount, gin_new_item_price, gin_amount, gin_total, gin_new_total, gin_savings, gin_employee, gin_profit, gin_date, gin_month, gin_year) VALUES ('" + txtInvoiceId.getText() + "', (SELECT stk_id FROM stock WHERE itm_code = '" + txtInvoiceItemCode.getText() + "'), '" + txtInvoiceItemCode.getText() + "', '" + txtInvoiceItemName.getText() + "',  '" + txtInvoiceItemPurchasePrice.getText() + "', '" + txtInvoiceItemSellingPrice.getText() + "','" + discount + "', '" + txtInvoiceNewItemPrice.getText() + "', '" + txtInvoiceItemAmount.getText() + "', '" + totalWithoutDiscount + "', '" + totalWithDiscount + "','" + savings + "','" + lblEmployeeUsername.getText() + "','" + ptofitPerItemAmountPerEachItem + "', '" + date + "', '" + month + "', '" + year + "') ";
                        String addSalesItem = "INSERT INTO sales(invoice_id, itm_id, itm_code, itm_name, stk_itm_price, stk_selling_price, gin_discount, gin_new_item_price, gin_amount, gin_total, gin_new_total, gin_savings, gin_employee, sal_profit, sal_date, sal_month, sal_year) VALUES ('" + txtInvoiceId.getText() + "', (SELECT stk_id FROM stock WHERE itm_code = '" + txtInvoiceItemCode.getText() + "'), '" + txtInvoiceItemCode.getText() + "', '" + txtInvoiceItemName.getText() + "',  '" + txtInvoiceItemPurchasePrice.getText() + "', '" + txtInvoiceItemSellingPrice.getText() + "','" + discount + "', '" + txtInvoiceNewItemPrice.getText() + "', '" + txtInvoiceItemAmount.getText() + "', '" + totalWithoutDiscount + "', '" + totalWithDiscount + "','" + savings + "','" + lblEmployeeUsername.getText() + "','" + ptofitPerItemAmountPerEachItem + "', '" + date + "', '" + month + "', '" + year + "') ";
                        String addNewSaleItemActivityLog = "INSERT INTO log(usr_id, usr_type, usr_username, log_activity, log_description, log_state) VALUES ((SELECT usr_id FROM user WHERE usr_username = '" + lblEmployeeUsername.getText() + "'),'Employee','" + lblEmployeeUsername.getText() + "','Add New Sale Item','Employee added new sale item " + txtInvoiceItemName.getText() + "','SUCCESS')";

                        try {

                            DB.DB.putData(addInvoiceItem);
                            DB.DB.putData(addSalesItem);
                            DB.DB.putData(addNewSaleItemActivityLog);

                            ImageIcon icon = new ImageIcon(getClass().getResource("img/done.png"));
                            JOptionPane.showMessageDialog(this, "Item Added to Invoice Item List!", "Done", JOptionPane.INFORMATION_MESSAGE, icon);

                            ResultSet stockCountofSelectedItem = DB.DB.getData("SELECT stk_count FROM stock WHERE stk_id = (SELECT stk_id FROM stock WHERE itm_code = '" + txtInvoiceItemCode.getText() + "') ");
                            if (stockCountofSelectedItem.next()) {

                                int currentStockCount = Integer.parseInt(stockCountofSelectedItem.getString(1));
                                int soldAmount = Integer.parseInt(txtInvoiceItemAmount.getText());
                                int newStockCount = currentStockCount - soldAmount;

                                DB.DB.putData("UPDATE stock SET stk_count ='" + newStockCount + "' WHERE itm_code = '" + txtInvoiceItemCode.getText() + "' ");
                            }

                            txtInvoiceItemCode.setText(null);
                            txtInvoiceItemName.setText(null);
                            txtInvoiceItemPurchasePrice.setText(null);
                            txtInvoiceItemSellingPrice.setText(null);
                            cmbInvoiceItemDiscount.setSelectedIndex(0);
                            txtInvoiceNewItemPrice.setText(null);
                            txtInvoiceItemAmount.setText(null);
                            txtInvoiceItemStockCount.setText(null);
                            txtInvoiceItemLowStockCount.setText(null);
                            txtInvoiceItemStockStatus.setText(null);
                            txtInvoiceItemStockStatus.setBorder(new LineBorder((new Color(100, 100, 100))));
                            txtInvoiceItemCode.grabFocus();

                        } catch (Exception e) {
                        }
                    }

                    refreshInvoiceTable();

                } catch (Exception ex) {
                }

            } else {
                JOptionPane.showMessageDialog(this, "Please Fill All the Fields!", "Invalid Data", JOptionPane.WARNING_MESSAGE);
            }
        }
        txtInvoiceItemCode.grabFocus();
    }

    private void addCashInvoice() {
        try {
            String addInvoice = "INSERT INTO invoice(invoice_id, inv_type, inv_net_total, inv_nbt, inv_vat, inv_grand_total, gin_employee, gin_date, gin_month, gin_year) VALUES ('" + txtInvoiceId.getText() + "', 'Cash', '" + txtInvoiceAmountDue.getText() + "', '0.00','0.00','" + txtInvoiceAmountDue.getText() + "', '" + lblEmployeeUsername.getText() + "', '" + date + "', '" + month + "', '" + year + "') ";
            DB.DB.putData(addInvoice);
        } catch (Exception ex) {
        }

    }

    private void addVATInvoice() {
        try {
            String addInvoice = "INSERT INTO invoice(invoice_id, inv_type, inv_net_total, inv_nbt, inv_vat, inv_grand_total, gin_employee, gin_date, gin_month, gin_year) VALUES ('" + txtInvoiceId.getText() + "', 'VAT', '" + txtInvoiceAmountDue1.getText() + "', '" + txtInvoiceNBT.getText() + "','" + txtInvoiceVAT.getText() + "','" + txtInvoiceGrandTotal.getText() + "', '" + lblEmployeeUsername.getText() + "', '" + date + "', '" + month + "', '" + year + "') ";
            DB.DB.putData(addInvoice);
        } catch (Exception ex) {
            Logger.getLogger(EmployeePOS.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    private void deleteInvoiceItem() {
        int option = JOptionPane.showConfirmDialog(this, "Do you want to Delete this Invoice Item?", "", JOptionPane.YES_NO_OPTION);
        if (option == 0) {
            try {

                if (txtInvoiceId.getText().equals(txtSearchInvoice.getText())) {
                    String selectedInvoiceItemId = ((String) tblInvoice.getValueAt(tblInvoice.getSelectedRow(), 0));
                    DB.DB.putData("DELETE FROM gin WHERE invoice_id  = '" + txtInvoiceId.getText() + "' AND gin_id = '" + selectedInvoiceItemId + "' ");
                    DB.DB.putData("DELETE FROM sales WHERE invoice_id  = '" + txtInvoiceId.getText() + "' AND sal_id = '" + selectedInvoiceItemId + "' ");

                    ImageIcon icon = new ImageIcon(getClass().getResource("img/done.png"));
                    JOptionPane.showMessageDialog(this, "Invoice Item Deleted!", "Done", JOptionPane.INFORMATION_MESSAGE, icon);
                    JOptionPane.showMessageDialog(this, "Stock Updated!", "Done", JOptionPane.INFORMATION_MESSAGE, icon);
                    JOptionPane.showMessageDialog(this, "Sales Updated!", "Done", JOptionPane.INFORMATION_MESSAGE, icon);

                    ResultSet itemCodeFromItemName = DB.DB.getData("SELECT itm_code FROM stock WHERE itm_name = '" + txtInvoiceItemName.getText() + "'  ");

                    if (itemCodeFromItemName.next()) {
                        String itmCode = itemCodeFromItemName.getString(1);

                        ResultSet stockCountofSelectedItem = DB.DB.getData("SELECT stk_count FROM stock WHERE stk_id = (SELECT stk_id FROM stock WHERE itm_code = '" + itmCode + "') ");
                        if (stockCountofSelectedItem.next()) {
                            int currentStockCount = Integer.parseInt(stockCountofSelectedItem.getString(1));
                            int soldAmount = Integer.parseInt(txtInvoiceItemAmount.getText());
                            int newStockCount = currentStockCount + soldAmount;

                            DB.DB.putData("UPDATE stock SET stk_count ='" + newStockCount + "' WHERE itm_code = '" + itmCode + "' ");
                            String removeNewSaleItemActivityLog = "INSERT INTO log(usr_id, usr_type, usr_username, log_activity, log_description, log_state) VALUES ((SELECT usr_id FROM user WHERE usr_username = '" + lblEmployeeUsername.getText() + "'),'Employee','" + lblEmployeeUsername.getText() + "','Remove New Sale Item','Employee removed new sale item " + txtInvoiceItemName.getText() + "','SUCCESS')";
                            DB.DB.putData(removeNewSaleItemActivityLog);
                        }

                    }

                } else {
                    JOptionPane.showMessageDialog(this, "This Invoice Item has Already Issued, Cannot be Deleted!", "Invalid Data", JOptionPane.WARNING_MESSAGE);
                }

                txtInvoiceItemName.setText(null);
                txtInvoiceItemAmount.setText(null);

                refreshInvoiceTable();

            } catch (Exception e) {
            }
        } else {

        }
    }

    private void calculateInvoiceBillPayments() {
        try {

            ResultSet invoiceAmountDue = DB.DB.getData("SELECT sum(gin_new_total) FROM gin WHERE invoice_id  = '" + txtSearchInvoice.getText() + "'");    //Invoice Bill Total [With Discounts/Without Discounts}
            ResultSet invoiceBillSavings = DB.DB.getData("SELECT sum(gin_savings) FROM gin WHERE invoice_id  = '" + txtSearchInvoice.getText() + "'");

            DecimalFormat ft = new DecimalFormat("#.##");

            if (invoiceAmountDue.next()) {
                Double d = invoiceAmountDue.getDouble(1);
                txtInvoiceAmountDue.setText(ft.format(d));
                txtInvoiceAmountDue1.setText(ft.format(d));
                lblInvoiceTotal.setText("Total : " + Double.toString(d));

                Double NBT = d * (2.04 / 100);
                txtInvoiceNBT.setText(ft.format(NBT));

                Double VAT = d * 15 / 100;
                txtInvoiceVAT.setText(ft.format(VAT));

                Double GrandTotal = d + NBT + VAT;
                txtInvoiceGrandTotal.setText(ft.format(GrandTotal));
            }

            if (invoiceBillSavings.next()) {
                Double d = invoiceBillSavings.getDouble(1);
                txtInvoiceAmountSavings.setText(ft.format(d));
                txtInvoiceAmountSavings1.setText(ft.format(d));
            }

        } catch (Exception e) {
        }
    }

    private void refreshInvoiceTable() {
        try {

            DefaultTableModel dtm = (DefaultTableModel) tblInvoice.getModel();
            ResultSet rs = DB.DB.getData("SELECT * FROM gin WHERE invoice_id = '" + txtSearchInvoice.getText() + "'");
            dtm.setRowCount(0);

            while (rs.next()) {
                String a = (rs.getString(1));
                String b = (rs.getString(5));
                String c = (rs.getString(7));
                String d = (rs.getString(8));
                String e = (rs.getString(9));
                String f = (rs.getString(10));
                String g = (rs.getString(12));
                dtm.addRow(new Object[]{a, b, c, d, e, f, g});

            }

            calculateInvoiceBillPayments();

        } catch (Exception e) {
        }

    }

    private void searchInvoiceItem() {
        switch (cmbSortInvoice.getSelectedIndex()) {
            case 0:
                calculateInvoiceBillPayments();

                try {
                    DefaultTableModel dtm = (DefaultTableModel) tblInvoice.getModel();
                    ResultSet rs = DB.DB.getData("SELECT * FROM gin WHERE invoice_id = '" + txtSearchInvoice.getText() + "%' order by invoice_id ");
                    dtm.setRowCount(0);

                    while (rs.next()) {
                        String a = (rs.getString(1));
                        String b = (rs.getString(5));
                        String c = (rs.getString(7));
                        String d = (rs.getString(8));
                        String e = (rs.getString(9));
                        String f = (rs.getString(10));
                        String g = (rs.getString(12));
                        dtm.addRow(new Object[]{a, b, c, d, e, f, g});
                    }

                } catch (Exception e) {
                }
                break;
            case 1:

                try {
                DefaultTableModel dtm = (DefaultTableModel) tblInvoice.getModel();
                ResultSet rs = DB.DB.getData("SELECT * FROM gin WHERE itm_name like '" + txtSearchInvoice.getText() + "%' order by itm_name ");
                dtm.setRowCount(0);

                while (rs.next()) {
                    String a = (rs.getString(1));
                    String b = (rs.getString(5));
                    String c = (rs.getString(7));
                    String d = (rs.getString(8));
                    String e = (rs.getString(9));
                    String f = (rs.getString(10));
                    String g = (rs.getString(12));
                    dtm.addRow(new Object[]{a, b, c, d, e, f, g});
                }

            } catch (Exception e) {
            }
            break;
            case 2:

                try {
                DefaultTableModel dtm = (DefaultTableModel) tblInvoice.getModel();
                ResultSet rs = DB.DB.getData("SELECT * FROM gin WHERE itm_code like '" + txtSearchInvoice.getText() + "%' order by itm_code ");
                dtm.setRowCount(0);

                while (rs.next()) {
                    String a = (rs.getString(1));
                    String b = (rs.getString(5));
                    String c = (rs.getString(7));
                    String d = (rs.getString(8));
                    String e = (rs.getString(9));
                    String f = (rs.getString(10));
                    String g = (rs.getString(12));
                    dtm.addRow(new Object[]{a, b, c, d, e, f, g});
                }
            } catch (Exception e) {
            }
            break;
            default:
                break;
        }
    }

    private void issueItems() {
        int option = JOptionPane.showConfirmDialog(this, "Do you want to Issue Invoice Items?", "", JOptionPane.YES_NO_OPTION);
        if (option == 0) {

            if (txtInvoiceId.getText().equals(txtSearchInvoice.getText())) {

                if (!txtInvoiceAmountPaid.getText().equals("")
                        & (!txtInvoiceAmountBalance.getText().equals(""))
                        & (!txtInvoiceAmountDue.getText().equals(""))) {

                    String issueInvoiceItems = "INSERT INTO log(usr_id, usr_type, usr_username, log_activity, log_description, log_state) VALUES ((SELECT usr_id FROM user WHERE usr_username = '" + lblEmployeeUsername.getText() + "'),'Employee','" + lblEmployeeUsername.getText() + "','Issue Invoice/Items','Employee Issued Invoice No. " + txtInvoiceId.getText() + "','SUCCESS')";
                    try {
                        DB.DB.putData(issueInvoiceItems);
                    } catch (Exception e) {
                    }

                    addCashInvoice();
                    generateInvoiceId();

                    ImageIcon icon = new ImageIcon(getClass().getResource("img/done.png"));
                    JOptionPane.showMessageDialog(this, "Invoice Items Issued!", "Done", JOptionPane.INFORMATION_MESSAGE, icon);

                    refreshInvoiceTable();

                    txtInvoiceAmountPaid.setText(null);
                    jTabbedPaneInvoice.setSelectedIndex(0);
                    txtInvoiceItemCode.setText(null);
                    txtInvoiceItemName.setText(null);
                    txtInvoiceItemPurchasePrice.setText(null);
                    txtInvoiceItemSellingPrice.setText(null);
                    cmbInvoiceItemDiscount.setSelectedIndex(0);
                    txtInvoiceNewItemPrice.setText(null);
                    txtInvoiceItemAmount.setText(null);

                } else {
                    JOptionPane.showMessageDialog(this, "Please Fill All the Empty Fields!", "Invalid Data", JOptionPane.WARNING_MESSAGE);
                }

            } else {
                JOptionPane.showMessageDialog(this, "This Transaction Already Done, Cannot be Issued Again!", "Invalid Data", JOptionPane.WARNING_MESSAGE);
            }

        }
    }

    private void issueItems1() {
        int option = JOptionPane.showConfirmDialog(this, "Do you want to Issue Invoice Items?", "", JOptionPane.YES_NO_OPTION);
        if (option == 0) {

            if (txtInvoiceId.getText().equals(txtSearchInvoice.getText())) {

                if (!txtInvoiceAmountPaid1.getText().equals("")
                        & (!txtInvoiceAmountBalance1.getText().equals(""))
                        & (!txtInvoiceAmountDue1.getText().equals(""))) {

                    String issueInvoiceItems = "INSERT INTO log(usr_id, usr_type, usr_username, log_activity, log_description, log_state) VALUES ((SELECT usr_id FROM user WHERE usr_username = '" + lblEmployeeUsername.getText() + "'),'Employee','" + lblEmployeeUsername.getText() + "','Issue Invoice/Items','Employee Issued Invoice No. " + txtInvoiceId.getText() + "','SUCCESS')";
                    try {
                        DB.DB.putData(issueInvoiceItems);
                    } catch (Exception e) {
                    }

                    addVATInvoice();
                    generateInvoiceId();

                    ImageIcon icon = new ImageIcon(getClass().getResource("img/done.png"));
                    JOptionPane.showMessageDialog(this, "Invoice Items Issued!", "Done", JOptionPane.INFORMATION_MESSAGE, icon);

                    refreshInvoiceTable();

                    txtInvoiceAmountPaid1.setText(null);
                    jTabbedPaneInvoice.setSelectedIndex(0);
                    txtInvoiceItemCode.setText(null);
                    txtInvoiceItemName.setText(null);
                    txtInvoiceItemPurchasePrice.setText(null);
                    txtInvoiceItemSellingPrice.setText(null);
                    cmbInvoiceItemDiscount.setSelectedIndex(0);
                    txtInvoiceNewItemPrice.setText(null);
                    txtInvoiceItemAmount.setText(null);

                } else {
                    JOptionPane.showMessageDialog(this, "Please Fill All the Empty Fields!", "Invalid Data", JOptionPane.WARNING_MESSAGE);
                }

            } else {
                JOptionPane.showMessageDialog(this, "This Transaction Already Done, Cannot be Issued Again!", "Invalid Data", JOptionPane.WARNING_MESSAGE);
            }

        }
    }

    
    public void printSelectedInvoiceBill() {
        HashMap a = new HashMap();
        a.put("invoice_id", txtSearchInvoice.getText());
        a.put("invoice_total", txtInvoiceAmountDue.getText());
        a.put("paid_amount", txtInvoiceAmountPaid.getText());
        a.put("balance", txtInvoiceAmountBalance.getText());
        a.put("total_savings", txtInvoiceAmountSavings.getText());
        a.put("employee", lblEmployeeUsername.getText());

        new Thread() {
            @Override
            public void run() {

                try {

                    String reportSource = "C:\\Program Files\\Exfiltro Reports\\viewSelectedInvoiceBill.jrxml";

                    JasperDesign jdesign = JRXmlLoader.load(reportSource);
                    JasperReport jreport = JasperCompileManager.compileReport(jdesign);
                    JasperPrint jprint = JasperFillManager.fillReport(jreport, a, DB.DB.getConnection());
                    JasperViewer.viewReport(jprint, false);

                } catch (JRException ex) {
                }
            }
        }.start();
    }
    
    public void printSelectedVATInvoiceBill() {
        HashMap a = new HashMap();
        a.put("invoice_id", txtSearchInvoice.getText());
        a.put("invoice_total", txtInvoiceAmountDue1.getText());
        a.put("paid_amount", txtInvoiceAmountPaid1.getText());
        a.put("balance", txtInvoiceAmountBalance1.getText());
        a.put("total_savings", txtInvoiceAmountSavings1.getText());
        a.put("employee", lblEmployeeUsername.getText());
        a.put("nbt", txtInvoiceNBT.getText());
        a.put("vat", txtInvoiceVAT.getText());
        a.put("grand_total", txtInvoiceGrandTotal.getText());
        
        new Thread() {
            @Override
            public void run() {

                try {

                    String reportSource = "C:\\Program Files\\Exfiltro Reports\\viewSelectedVATInvoiceBill.jrxml";

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
        jTabbedPaneInvoice = new javax.swing.JTabbedPane();
        jPanel2 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        txtInvoiceItemCode = new javax.swing.JTextField();
        txtInvoiceItemName = new javax.swing.JTextField();
        txtInvoiceNewItemPrice = new javax.swing.JTextField();
        txtInvoiceItemAmount = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jLabel22 = new javax.swing.JLabel();
        jPanel6 = new javax.swing.JPanel();
        jLabel137 = new javax.swing.JLabel();
        txtInvoiceItemStockCount = new javax.swing.JTextField();
        jLabel138 = new javax.swing.JLabel();
        txtInvoiceItemLowStockCount = new javax.swing.JTextField();
        jLabel143 = new javax.swing.JLabel();
        txtInvoiceItemStockStatus = new javax.swing.JTextField();
        jPanel7 = new javax.swing.JPanel();
        jLabel155 = new javax.swing.JLabel();
        txtInvoiceId = new javax.swing.JTextField();
        cmbInvoiceItemDiscount = new javax.swing.JComboBox<>();
        txtInvoiceItemPurchasePrice = new javax.swing.JTextField();
        txtInvoiceItemSellingPrice = new javax.swing.JTextField();
        jPanel3 = new javax.swing.JPanel();
        txtSearchInvoice = new javax.swing.JTextField();
        jLabel11 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblInvoice = new javax.swing.JTable();
        jButton4 = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();
        jButton6 = new javax.swing.JButton();
        jButton7 = new javax.swing.JButton();
        cmbSortInvoice = new javax.swing.JComboBox<>();
        lblInvoiceTotal = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        lblInvoiceId = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        jTabbedPaneSubInvoice = new javax.swing.JTabbedPane();
        jPanel5 = new javax.swing.JPanel();
        jLabel17 = new javax.swing.JLabel();
        txtInvoiceAmountSavings = new javax.swing.JTextField();
        txtInvoiceAmountBalance = new javax.swing.JTextField();
        jLabel16 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        txtInvoiceAmountPaid = new javax.swing.JTextField();
        txtInvoiceAmountDue = new javax.swing.JTextField();
        jLabel13 = new javax.swing.JLabel();
        jButton10 = new javax.swing.JButton();
        jButton12 = new javax.swing.JButton();
        jPanel8 = new javax.swing.JPanel();
        jLabel18 = new javax.swing.JLabel();
        txtInvoiceAmountDue1 = new javax.swing.JTextField();
        jLabel21 = new javax.swing.JLabel();
        txtInvoiceNBT = new javax.swing.JTextField();
        jLabel23 = new javax.swing.JLabel();
        txtInvoiceVAT = new javax.swing.JTextField();
        jLabel24 = new javax.swing.JLabel();
        txtInvoiceGrandTotal = new javax.swing.JTextField();
        jLabel15 = new javax.swing.JLabel();
        txtInvoiceAmountPaid1 = new javax.swing.JTextField();
        jLabel19 = new javax.swing.JLabel();
        txtInvoiceAmountBalance1 = new javax.swing.JTextField();
        jLabel20 = new javax.swing.JLabel();
        txtInvoiceAmountSavings1 = new javax.swing.JTextField();
        jButton11 = new javax.swing.JButton();
        jButton13 = new javax.swing.JButton();

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
        jLabel2.setText("POS");
        jLabel2.setToolTipText("");

        jLabel3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/frames/img/icons8_buying_50px.png"))); // NOI18N

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

        jTabbedPaneInvoice.setBackground(new java.awt.Color(34, 34, 34));
        jTabbedPaneInvoice.setForeground(new java.awt.Color(255, 255, 255));
        jTabbedPaneInvoice.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jTabbedPaneInvoice.setOpaque(true);

        jPanel2.setBackground(new java.awt.Color(34, 34, 34));

        jLabel1.setBackground(new java.awt.Color(0, 0, 0));
        jLabel1.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("Item Code");

        jLabel6.setBackground(new java.awt.Color(0, 0, 0));
        jLabel6.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(255, 255, 255));
        jLabel6.setText("Item Name");

        jLabel7.setBackground(new java.awt.Color(0, 0, 0));
        jLabel7.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(255, 255, 255));
        jLabel7.setText("Purchase Price");

        jLabel8.setBackground(new java.awt.Color(0, 0, 0));
        jLabel8.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(255, 255, 255));
        jLabel8.setText("Discount");

        jLabel9.setBackground(new java.awt.Color(0, 0, 0));
        jLabel9.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(255, 255, 255));
        jLabel9.setText("New Selling Price");

        jLabel10.setBackground(new java.awt.Color(0, 0, 0));
        jLabel10.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel10.setForeground(new java.awt.Color(255, 255, 255));
        jLabel10.setText("Amount");

        txtInvoiceItemCode.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        txtInvoiceItemCode.setForeground(new java.awt.Color(255, 255, 255));
        txtInvoiceItemCode.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtInvoiceItemCodeKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtInvoiceItemCodeKeyReleased(evt);
            }
        });

        txtInvoiceItemName.setBackground(new java.awt.Color(51, 51, 51));
        txtInvoiceItemName.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        txtInvoiceItemName.setEnabled(false);

        txtInvoiceNewItemPrice.setBackground(new java.awt.Color(51, 51, 51));
        txtInvoiceNewItemPrice.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        txtInvoiceNewItemPrice.setEnabled(false);

        txtInvoiceItemAmount.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        txtInvoiceItemAmount.setForeground(new java.awt.Color(255, 255, 255));
        txtInvoiceItemAmount.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtInvoiceItemAmountKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtInvoiceItemAmountKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtInvoiceItemAmountKeyTyped(evt);
            }
        });

        jButton1.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jButton1.setForeground(new java.awt.Color(255, 255, 255));
        jButton1.setText(" Add Item");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jButton2.setForeground(new java.awt.Color(255, 255, 255));
        jButton2.setText(" View Items");
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

        jLabel22.setBackground(new java.awt.Color(0, 0, 0));
        jLabel22.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel22.setForeground(new java.awt.Color(255, 255, 255));
        jLabel22.setText("Selling Price");

        jPanel6.setBackground(new java.awt.Color(60, 60, 60));
        jPanel6.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(100, 100, 100), 1, true));

        jLabel137.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel137.setForeground(new java.awt.Color(255, 255, 255));
        jLabel137.setText("Stock Count");

        txtInvoiceItemStockCount.setEditable(false);
        txtInvoiceItemStockCount.setBackground(new java.awt.Color(51, 51, 51));
        txtInvoiceItemStockCount.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        txtInvoiceItemStockCount.setForeground(new java.awt.Color(255, 255, 255));
        txtInvoiceItemStockCount.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtInvoiceItemStockCount.setEnabled(false);

        jLabel138.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel138.setForeground(new java.awt.Color(255, 255, 255));
        jLabel138.setText("Low Stock");

        txtInvoiceItemLowStockCount.setEditable(false);
        txtInvoiceItemLowStockCount.setBackground(new java.awt.Color(51, 51, 51));
        txtInvoiceItemLowStockCount.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        txtInvoiceItemLowStockCount.setForeground(new java.awt.Color(255, 255, 255));
        txtInvoiceItemLowStockCount.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtInvoiceItemLowStockCount.setEnabled(false);

        jLabel143.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel143.setForeground(new java.awt.Color(255, 255, 255));
        jLabel143.setText("Stock Status");

        txtInvoiceItemStockStatus.setEditable(false);
        txtInvoiceItemStockStatus.setBackground(new java.awt.Color(51, 51, 51));
        txtInvoiceItemStockStatus.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        txtInvoiceItemStockStatus.setForeground(new java.awt.Color(255, 255, 255));
        txtInvoiceItemStockStatus.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtInvoiceItemStockStatus.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(100, 100, 100)));
        txtInvoiceItemStockStatus.setEnabled(false);

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jLabel138, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel143, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 110, Short.MAX_VALUE)
                    .addComponent(jLabel137, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(txtInvoiceItemStockCount)
                    .addComponent(txtInvoiceItemLowStockCount)
                    .addComponent(txtInvoiceItemStockStatus, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(25, 25, 25))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(txtInvoiceItemStockCount)
                    .addComponent(jLabel137, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(txtInvoiceItemLowStockCount)
                    .addComponent(jLabel138, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(txtInvoiceItemStockStatus)
                    .addComponent(jLabel143, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(25, 25, 25))
        );

        jPanel7.setBackground(new java.awt.Color(60, 60, 60));
        jPanel7.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(100, 100, 100), 1, true));

        jLabel155.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel155.setForeground(new java.awt.Color(255, 255, 255));
        jLabel155.setText("Invoice Id");

        txtInvoiceId.setEditable(false);
        txtInvoiceId.setBackground(new java.awt.Color(51, 51, 51));
        txtInvoiceId.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        txtInvoiceId.setForeground(new java.awt.Color(255, 255, 255));
        txtInvoiceId.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtInvoiceId.setText("1");

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addComponent(jLabel155, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(txtInvoiceId, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(25, 25, 25))
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel155, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtInvoiceId))
                .addGap(25, 25, 25))
        );

        cmbInvoiceItemDiscount.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        cmbInvoiceItemDiscount.setForeground(new java.awt.Color(255, 255, 255));
        cmbInvoiceItemDiscount.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "0%", "5%", "10%", "15%", "20%", "25%", "30%", "35%" }));
        cmbInvoiceItemDiscount.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                cmbInvoiceItemDiscountKeyPressed(evt);
            }
        });

        txtInvoiceItemPurchasePrice.setBackground(new java.awt.Color(51, 51, 51));
        txtInvoiceItemPurchasePrice.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        txtInvoiceItemPurchasePrice.setEnabled(false);

        txtInvoiceItemSellingPrice.setBackground(new java.awt.Color(51, 51, 51));
        txtInvoiceItemSellingPrice.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        txtInvoiceItemSellingPrice.setEnabled(false);

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
                    .addComponent(jLabel7)
                    .addComponent(jLabel22))
                .addGap(51, 51, 51)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(txtInvoiceItemAmount, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 278, Short.MAX_VALUE)
                    .addComponent(txtInvoiceNewItemPrice, javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtInvoiceItemCode, javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(cmbInvoiceItemDiscount, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(txtInvoiceItemPurchasePrice)
                    .addComponent(txtInvoiceItemName)
                    .addComponent(txtInvoiceItemSellingPrice))
                .addGap(78, 78, 78)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButton2)
                    .addComponent(jButton1)
                    .addComponent(jButton3))
                .addGap(95, 95, 95)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(101, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(45, 45, 45)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel1)
                            .addComponent(txtInvoiceItemCode, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton1))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel6)
                            .addComponent(txtInvoiceItemName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton2))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel7)
                            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jButton3)
                                .addComponent(txtInvoiceItemPurchasePrice, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel22)
                            .addComponent(txtInvoiceItemSellingPrice, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel8)
                            .addComponent(cmbInvoiceItemDiscount, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtInvoiceNewItemPrice, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel9))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtInvoiceItemAmount, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel10))))
                .addContainerGap(206, Short.MAX_VALUE))
        );

        jTabbedPaneInvoice.addTab("Add Items", jPanel2);

        jPanel3.setBackground(new java.awt.Color(34, 34, 34));

        txtSearchInvoice.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        txtSearchInvoice.setForeground(new java.awt.Color(255, 255, 255));
        txtSearchInvoice.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtSearchInvoiceKeyReleased(evt);
            }
        });

        jLabel11.setIcon(new javax.swing.ImageIcon(getClass().getResource("/frames/img/search.icon.png"))); // NOI18N

        tblInvoice.setBackground(new java.awt.Color(34, 34, 34));
        tblInvoice.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        tblInvoice.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Invoice Item Id", "Item Name", "Selling Price", "Discount", "New Selling Price", "Amount", "Total"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblInvoice.setGridColor(new java.awt.Color(61, 61, 61));
        tblInvoice.setShowGrid(true);
        tblInvoice.getTableHeader().setReorderingAllowed(false);
        tblInvoice.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblInvoiceMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tblInvoice);

        jButton4.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jButton4.setForeground(new java.awt.Color(255, 255, 255));
        jButton4.setText("Add Item");
        jButton4.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        jButton5.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jButton5.setForeground(new java.awt.Color(255, 255, 255));
        jButton5.setText(" Delete Item");
        jButton5.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });

        jButton6.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jButton6.setForeground(new java.awt.Color(255, 255, 255));
        jButton6.setText("Payments");
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

        cmbSortInvoice.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        cmbSortInvoice.setForeground(new java.awt.Color(255, 255, 255));
        cmbSortInvoice.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Invoice Id", "Item Name", "Item Code" }));
        cmbSortInvoice.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbSortInvoiceActionPerformed(evt);
            }
        });

        lblInvoiceTotal.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        lblInvoiceTotal.setText("Total : 0");

        jLabel5.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(153, 153, 153));
        jLabel5.setText("|");

        lblInvoiceId.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        lblInvoiceId.setText("Invoice Id : ");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addGap(52, 52, 52)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(lblInvoiceId, javax.swing.GroupLayout.PREFERRED_SIZE, 191, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 6, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblInvoiceTotal, javax.swing.GroupLayout.PREFERRED_SIZE, 172, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButton7)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jButton4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jButton5)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jButton6))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 1096, Short.MAX_VALUE)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(txtSearchInvoice, javax.swing.GroupLayout.DEFAULT_SIZE, 873, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(cmbSortInvoice, 0, 173, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel11)))
                .addGap(52, 52, 52))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(23, 23, 23)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtSearchInvoice, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel11)
                    .addComponent(cmbSortInvoice, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 393, Short.MAX_VALUE)
                .addGap(25, 25, 25)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(lblInvoiceId)
                        .addComponent(lblInvoiceTotal)
                        .addComponent(jLabel5))
                    .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jButton4)
                        .addComponent(jButton5)
                        .addComponent(jButton6)
                        .addComponent(jButton7)))
                .addGap(36, 36, 36))
        );

        jTabbedPaneInvoice.addTab("View Invoice", jPanel3);

        jTabbedPaneSubInvoice.setBackground(new java.awt.Color(34, 34, 34));
        jTabbedPaneSubInvoice.setForeground(new java.awt.Color(255, 255, 255));
        jTabbedPaneSubInvoice.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jTabbedPaneSubInvoice.setOpaque(true);
        jTabbedPaneSubInvoice.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTabbedPaneSubInvoiceMouseClicked(evt);
            }
        });

        jPanel5.setBackground(new java.awt.Color(34, 34, 34));

        jLabel17.setBackground(new java.awt.Color(0, 0, 0));
        jLabel17.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel17.setForeground(new java.awt.Color(255, 255, 255));
        jLabel17.setText("Savings");

        txtInvoiceAmountSavings.setBackground(new java.awt.Color(51, 51, 51));
        txtInvoiceAmountSavings.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        txtInvoiceAmountSavings.setEnabled(false);

        txtInvoiceAmountBalance.setBackground(new java.awt.Color(51, 51, 51));
        txtInvoiceAmountBalance.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        txtInvoiceAmountBalance.setEnabled(false);

        jLabel16.setBackground(new java.awt.Color(0, 0, 0));
        jLabel16.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel16.setForeground(new java.awt.Color(255, 255, 255));
        jLabel16.setText("Balance");

        jLabel14.setBackground(new java.awt.Color(0, 0, 0));
        jLabel14.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel14.setForeground(new java.awt.Color(255, 255, 255));
        jLabel14.setText("Paid Amount");

        txtInvoiceAmountPaid.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        txtInvoiceAmountPaid.setForeground(new java.awt.Color(255, 255, 255));
        txtInvoiceAmountPaid.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtInvoiceAmountPaidKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtInvoiceAmountPaidKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtInvoiceAmountPaidKeyTyped(evt);
            }
        });

        txtInvoiceAmountDue.setBackground(new java.awt.Color(51, 51, 51));
        txtInvoiceAmountDue.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        txtInvoiceAmountDue.setEnabled(false);

        jLabel13.setBackground(new java.awt.Color(0, 0, 0));
        jLabel13.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel13.setForeground(new java.awt.Color(255, 255, 255));
        jLabel13.setText("Amount Due");

        jButton10.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jButton10.setForeground(new java.awt.Color(255, 255, 255));
        jButton10.setText("Issue");
        jButton10.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jButton10.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton10ActionPerformed(evt);
            }
        });

        jButton12.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jButton12.setForeground(new java.awt.Color(255, 255, 255));
        jButton12.setText(" Print Invoice");
        jButton12.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jButton12.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton12ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(52, 52, 52)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel13)
                    .addComponent(jLabel14)
                    .addComponent(jLabel16)
                    .addComponent(jLabel17))
                .addGap(51, 51, 51)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(txtInvoiceAmountSavings, javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtInvoiceAmountBalance, javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtInvoiceAmountDue, javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtInvoiceAmountPaid, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 278, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(78, 78, 78)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButton10)
                    .addComponent(jButton12))
                .addContainerGap(531, Short.MAX_VALUE))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(45, 45, 45)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel13)
                    .addComponent(txtInvoiceAmountDue, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton10))
                .addGap(18, 18, 18)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel14)
                    .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(txtInvoiceAmountPaid, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jButton12)))
                .addGap(18, 18, 18)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtInvoiceAmountBalance, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel16))
                .addGap(18, 18, 18)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtInvoiceAmountSavings, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel17))
                .addContainerGap(311, Short.MAX_VALUE))
        );

        jTabbedPaneSubInvoice.addTab("Cash Invoice", jPanel5);

        jPanel8.setBackground(new java.awt.Color(34, 34, 34));

        jLabel18.setBackground(new java.awt.Color(0, 0, 0));
        jLabel18.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel18.setForeground(new java.awt.Color(255, 255, 255));
        jLabel18.setText("Total");

        txtInvoiceAmountDue1.setBackground(new java.awt.Color(51, 51, 51));
        txtInvoiceAmountDue1.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        txtInvoiceAmountDue1.setEnabled(false);

        jLabel21.setBackground(new java.awt.Color(0, 0, 0));
        jLabel21.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel21.setForeground(new java.awt.Color(255, 255, 255));
        jLabel21.setText("NBT (2.04%)");

        txtInvoiceNBT.setBackground(new java.awt.Color(51, 51, 51));
        txtInvoiceNBT.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        txtInvoiceNBT.setEnabled(false);

        jLabel23.setBackground(new java.awt.Color(0, 0, 0));
        jLabel23.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel23.setForeground(new java.awt.Color(255, 255, 255));
        jLabel23.setText("VAT (15%)");

        txtInvoiceVAT.setBackground(new java.awt.Color(51, 51, 51));
        txtInvoiceVAT.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        txtInvoiceVAT.setEnabled(false);

        jLabel24.setBackground(new java.awt.Color(0, 0, 0));
        jLabel24.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel24.setForeground(new java.awt.Color(255, 255, 255));
        jLabel24.setText("Grand Total");

        txtInvoiceGrandTotal.setBackground(new java.awt.Color(51, 51, 51));
        txtInvoiceGrandTotal.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        txtInvoiceGrandTotal.setEnabled(false);

        jLabel15.setBackground(new java.awt.Color(0, 0, 0));
        jLabel15.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel15.setForeground(new java.awt.Color(255, 255, 255));
        jLabel15.setText("Paid Amount");

        txtInvoiceAmountPaid1.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        txtInvoiceAmountPaid1.setForeground(new java.awt.Color(255, 255, 255));
        txtInvoiceAmountPaid1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtInvoiceAmountPaid1KeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtInvoiceAmountPaid1KeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtInvoiceAmountPaid1KeyTyped(evt);
            }
        });

        jLabel19.setBackground(new java.awt.Color(0, 0, 0));
        jLabel19.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel19.setForeground(new java.awt.Color(255, 255, 255));
        jLabel19.setText("Balance");

        txtInvoiceAmountBalance1.setBackground(new java.awt.Color(51, 51, 51));
        txtInvoiceAmountBalance1.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        txtInvoiceAmountBalance1.setEnabled(false);

        jLabel20.setBackground(new java.awt.Color(0, 0, 0));
        jLabel20.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel20.setForeground(new java.awt.Color(255, 255, 255));
        jLabel20.setText("Savings");

        txtInvoiceAmountSavings1.setBackground(new java.awt.Color(51, 51, 51));
        txtInvoiceAmountSavings1.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        txtInvoiceAmountSavings1.setEnabled(false);

        jButton11.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jButton11.setForeground(new java.awt.Color(255, 255, 255));
        jButton11.setText("Issue");
        jButton11.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jButton11.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton11ActionPerformed(evt);
            }
        });

        jButton13.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jButton13.setForeground(new java.awt.Color(255, 255, 255));
        jButton13.setText("Print Invoice");
        jButton13.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jButton13.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton13ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addGap(52, 52, 52)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel18)
                    .addComponent(jLabel15)
                    .addComponent(jLabel19)
                    .addComponent(jLabel20)
                    .addComponent(jLabel21)
                    .addComponent(jLabel23)
                    .addComponent(jLabel24))
                .addGap(51, 51, 51)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(txtInvoiceAmountSavings1, javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtInvoiceAmountBalance1, javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtInvoiceAmountDue1, javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtInvoiceAmountPaid1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 278, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtInvoiceNBT, javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtInvoiceVAT, javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtInvoiceGrandTotal, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 278, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(78, 78, 78)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButton11)
                    .addComponent(jButton13))
                .addContainerGap(535, Short.MAX_VALUE))
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addGap(45, 45, 45)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel18)
                    .addComponent(txtInvoiceAmountDue1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton11))
                .addGap(18, 18, 18)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel21)
                    .addComponent(txtInvoiceNBT, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton13))
                .addGap(18, 18, 18)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel23)
                    .addComponent(txtInvoiceVAT, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel24)
                    .addComponent(txtInvoiceGrandTotal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel15)
                    .addComponent(txtInvoiceAmountPaid1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtInvoiceAmountBalance1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel19))
                .addGap(18, 18, 18)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtInvoiceAmountSavings1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel20))
                .addContainerGap(173, Short.MAX_VALUE))
        );

        jTabbedPaneSubInvoice.addTab("VAT Invoice", jPanel8);

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPaneSubInvoice)
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPaneSubInvoice)
        );

        jTabbedPaneInvoice.addTab("Payments", jPanel4);

        javax.swing.GroupLayout jPanel11Layout = new javax.swing.GroupLayout(jPanel11);
        jPanel11.setLayout(jPanel11Layout);
        jPanel11Layout.setHorizontalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPaneInvoice)
        );
        jPanel11Layout.setVerticalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPaneInvoice)
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

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        addInvoiceItem();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        jTabbedPaneInvoice.setSelectedIndex(1);
        refreshInvoiceTable();
        txtSearchInvoice.setText(txtInvoiceId.getText());
        cmbSortInvoice.setSelectedIndex(0);
        lblInvoiceId.setText("Invoice Id : " + txtSearchInvoice.getText());
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        txtInvoiceItemCode.setText(null);
        txtInvoiceItemName.setText(null);
        txtInvoiceItemPurchasePrice.setText(null);
        txtInvoiceItemSellingPrice.setText(null);
        cmbInvoiceItemDiscount.setSelectedIndex(0);
        txtInvoiceNewItemPrice.setText(null);
        txtInvoiceItemAmount.setText(null);

        txtInvoiceItemStockCount.setText(null);
        txtInvoiceItemLowStockCount.setText(null);

        txtInvoiceItemStockStatus.setText(null);
        txtInvoiceItemStockStatus.setBorder(new LineBorder((new Color(100, 100, 100))));
    }//GEN-LAST:event_jButton3ActionPerformed

    private void txtInvoiceItemAmountKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtInvoiceItemAmountKeyPressed
        if (evt.getKeyCode() == 10) {
            addInvoiceItem();
        }
    }//GEN-LAST:event_txtInvoiceItemAmountKeyPressed

    private void txtInvoiceItemAmountKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtInvoiceItemAmountKeyTyped
        char c = evt.getKeyChar();
        if (!(Character.isDigit(c))) {
            evt.consume();
        }
    }//GEN-LAST:event_txtInvoiceItemAmountKeyTyped

    private void txtSearchInvoiceKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtSearchInvoiceKeyReleased
        searchInvoiceItem();
    }//GEN-LAST:event_txtSearchInvoiceKeyReleased

    private void cmbSortInvoiceActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbSortInvoiceActionPerformed
        searchInvoiceItem();
    }//GEN-LAST:event_cmbSortInvoiceActionPerformed

    private void jButton7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton7ActionPerformed
        refreshInvoiceTable();
        txtSearchInvoice.setText(txtInvoiceId.getText());
        cmbSortInvoice.setSelectedIndex(0);
        lblInvoiceId.setText("Invoice Id : " + txtSearchInvoice.getText());
    }//GEN-LAST:event_jButton7ActionPerformed

    private void tblInvoiceMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblInvoiceMouseClicked
        txtInvoiceItemName.setText((String) tblInvoice.getValueAt(tblInvoice.getSelectedRow(), 1));
        txtInvoiceItemAmount.setText((String) tblInvoice.getValueAt(tblInvoice.getSelectedRow(), 5));

    }//GEN-LAST:event_tblInvoiceMouseClicked

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        jTabbedPaneInvoice.setSelectedIndex(0);
    }//GEN-LAST:event_jButton4ActionPerformed

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        if (!(tblInvoice.getSelectedRowCount() == 0)) {
            deleteInvoiceItem();

        } else {
            JOptionPane.showMessageDialog(this, "No Item Selected!");
        }
    }//GEN-LAST:event_jButton5ActionPerformed

    private void cmbInvoiceItemDiscountKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cmbInvoiceItemDiscountKeyPressed
        try {

            if (evt.getKeyCode() == 10) {

                switch (cmbInvoiceItemDiscount.getSelectedIndex()) {
                    case 0: {
                        discount = 0;
                        break;
                    }
                    case 1: {
                        discount = 5;
                        break;
                    }
                    case 2: {
                        discount = 10;
                        break;
                    }
                    case 3: {
                        discount = 15;
                        break;
                    }
                    case 4: {
                        discount = 20;
                        break;
                    }
                    case 5: {
                        discount = 25;
                        break;
                    }
                    case 6: {
                        discount = 30;
                        break;
                    }
                    case 7: {
                        discount = 35;
                        break;
                    }
                    default:
                        break;
                }

                int sellingPrice = (Integer.parseInt(txtInvoiceItemSellingPrice.getText()));
                int totalWithDiscount = sellingPrice - (sellingPrice * discount / 100);

                String newItemPrice = Integer.toString(totalWithDiscount);
                txtInvoiceNewItemPrice.setText(newItemPrice);

                int purchasePrice = (Integer.parseInt(txtInvoiceItemPurchasePrice.getText()));  //item profit cheching
                int newSellingPrice = (Integer.parseInt(txtInvoiceNewItemPrice.getText()));

                if (purchasePrice > newSellingPrice) {
                    JOptionPane.showMessageDialog(this, "New Selling Price of this Item is Too Low, Please Lower the Discount!");

                }

                txtInvoiceItemAmount.grabFocus();
            }
        } catch (NumberFormatException e) {
        }
    }//GEN-LAST:event_cmbInvoiceItemDiscountKeyPressed

    private void txtInvoiceItemCodeKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtInvoiceItemCodeKeyPressed
        if (evt.getKeyCode() == 10) {
            cmbInvoiceItemDiscount.setPopupVisible(true);
            cmbInvoiceItemDiscount.grabFocus();
        }
    }//GEN-LAST:event_txtInvoiceItemCodeKeyPressed

    private void txtInvoiceItemCodeKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtInvoiceItemCodeKeyReleased
        try {
            ResultSet InvoiceAutoFillDataFromStock = DB.DB.getData("SELECT itm_name, stk_itm_price, stk_selling_price, stk_count, stk_low_stock FROM stock WHERE stk_status = 'Available' AND (stk_count >= 0) AND itm_code = '" + txtInvoiceItemCode.getText() + "'");

            if (InvoiceAutoFillDataFromStock.next()) {
                String InvoiceItemName = InvoiceAutoFillDataFromStock.getString(1);
                String InvoiceItemPurchasePrice = InvoiceAutoFillDataFromStock.getString(2);
                String InvoiceItemSellingPrice = InvoiceAutoFillDataFromStock.getString(3);
                String InvoiceItemStockCount = InvoiceAutoFillDataFromStock.getString(4);
                String InvoiceItemLowStockCount = InvoiceAutoFillDataFromStock.getString(5);

                txtInvoiceItemName.setText(InvoiceItemName);
                txtInvoiceItemPurchasePrice.setText(InvoiceItemPurchasePrice);
                txtInvoiceItemSellingPrice.setText(InvoiceItemSellingPrice);
                txtInvoiceItemStockCount.setText(InvoiceItemStockCount);
                txtInvoiceItemLowStockCount.setText(InvoiceItemLowStockCount);

                ResultSet lowStockStatus = DB.DB.getData("SELECT stk_id FROM stock WHERE stk_status = 'Available' AND (stk_count < stk_low_stock) AND itm_code = '" + txtInvoiceItemCode.getText() + "' ");

                if (lowStockStatus.next()) {
                    txtInvoiceItemStockStatus.setText("Low Stock");
                    txtInvoiceItemStockStatus.setForeground(new Color(255, 0, 51));
                    txtInvoiceItemStockStatus.setBorder(new LineBorder((new Color(255, 0, 51))));

                } else {

                    txtInvoiceItemStockStatus.setText("Available");
                    txtInvoiceItemStockStatus.setForeground(new Color(15, 184, 37));
                    txtInvoiceItemStockStatus.setBorder(new LineBorder((new Color(15, 184, 37))));

                }

            } else {
                txtInvoiceItemName.setText(null);
                txtInvoiceItemPurchasePrice.setText(null);
                txtInvoiceItemSellingPrice.setText(null);
                txtInvoiceItemStockCount.setText(null);
                txtInvoiceItemLowStockCount.setText(null);
                txtInvoiceItemStockStatus.setText(null);
                txtInvoiceItemStockStatus.setBorder(new LineBorder((new Color(100, 100, 100))));

            }

        } catch (Exception ex) {
        }
    }//GEN-LAST:event_txtInvoiceItemCodeKeyReleased

    private void txtInvoiceItemAmountKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtInvoiceItemAmountKeyReleased
        try {

            int amountTyped = Integer.parseInt(txtInvoiceItemAmount.getText());
            int availableStockCount = Integer.parseInt(txtInvoiceItemStockCount.getText());

            if (amountTyped > availableStockCount) {
                JOptionPane.showMessageDialog(this, "No Sufficient Stock Count Available!", "Invalid Data", JOptionPane.WARNING_MESSAGE);
                txtInvoiceItemAmount.setText(null);
            }

        } catch (HeadlessException | NumberFormatException e) {
        }
    }//GEN-LAST:event_txtInvoiceItemAmountKeyReleased

    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed
        jTabbedPaneInvoice.setSelectedIndex(2);
        txtInvoiceAmountPaid.grabFocus();
    }//GEN-LAST:event_jButton6ActionPerformed

    private void txtInvoiceAmountPaidKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtInvoiceAmountPaidKeyPressed
        if (evt.getKeyCode() == 10) {
            if (!(tblInvoice.getRowCount() == 0)) {
                issueItems();

            } else {
                JOptionPane.showMessageDialog(this, "No Items Found on Invoice Bill!");
            }
        }
    }//GEN-LAST:event_txtInvoiceAmountPaidKeyPressed

    private void txtInvoiceAmountPaidKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtInvoiceAmountPaidKeyReleased
        try {

            DecimalFormat ft = new DecimalFormat("#.##");

            double amountDue = Double.parseDouble(txtInvoiceAmountDue.getText());
            double paidAmount = Double.parseDouble(txtInvoiceAmountPaid.getText());
            double balance = paidAmount - amountDue;
            txtInvoiceAmountBalance.setText(ft.format(balance));

        } catch (NumberFormatException e) {
        }
    }//GEN-LAST:event_txtInvoiceAmountPaidKeyReleased

    private void txtInvoiceAmountPaidKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtInvoiceAmountPaidKeyTyped
        char c = evt.getKeyChar();
        if (!(Character.isDigit(c))) {
            evt.consume();
        }
    }//GEN-LAST:event_txtInvoiceAmountPaidKeyTyped

    private void jButton10ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton10ActionPerformed
        if (!(tblInvoice.getRowCount() == 0)) {
            issueItems();

        } else {
            JOptionPane.showMessageDialog(this, "No Items Found on Invoice Bill!");
            jTabbedPaneInvoice.setSelectedIndex(1);
            txtInvoiceItemCode.grabFocus();
        }
    }//GEN-LAST:event_jButton10ActionPerformed

    private void txtInvoiceAmountPaid1KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtInvoiceAmountPaid1KeyPressed
        if (evt.getKeyCode() == 10) {
            if (!(tblInvoice.getRowCount() == 0)) {
                issueItems1();

            } else {
                JOptionPane.showMessageDialog(this, "No Items Found on Invoice Bill!");
            }
        }
    }//GEN-LAST:event_txtInvoiceAmountPaid1KeyPressed

    private void txtInvoiceAmountPaid1KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtInvoiceAmountPaid1KeyReleased
        try {
            DecimalFormat ft = new DecimalFormat("#.##");

            double amountDue = Double.parseDouble(txtInvoiceGrandTotal.getText());
            double paidAmount = Double.parseDouble(txtInvoiceAmountPaid1.getText());
            double balance = paidAmount - amountDue;
            txtInvoiceAmountBalance1.setText(ft.format(balance));

        } catch (NumberFormatException e) {
        }
    }//GEN-LAST:event_txtInvoiceAmountPaid1KeyReleased

    private void txtInvoiceAmountPaid1KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtInvoiceAmountPaid1KeyTyped
        char c = evt.getKeyChar();
        if (!(Character.isDigit(c))) {
            evt.consume();
        }
    }//GEN-LAST:event_txtInvoiceAmountPaid1KeyTyped

    private void jButton11ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton11ActionPerformed
        if (!(tblInvoice.getRowCount() == 0)) {
            issueItems1();

        } else {
            JOptionPane.showMessageDialog(this, "No Items Found on Invoice Bill!");
            jTabbedPaneInvoice.setSelectedIndex(1);
            txtInvoiceItemCode.grabFocus();
        }
    }//GEN-LAST:event_jButton11ActionPerformed

    private void jTabbedPaneSubInvoiceMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTabbedPaneSubInvoiceMouseClicked
        txtInvoiceAmountPaid1.grabFocus();
        txtInvoiceAmountPaid.grabFocus();
    }//GEN-LAST:event_jTabbedPaneSubInvoiceMouseClicked

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

                EmployeePOS.this.dispose();

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

    private void jButton12ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton12ActionPerformed
        printSelectedInvoiceBill();
    }//GEN-LAST:event_jButton12ActionPerformed

    private void jButton13ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton13ActionPerformed
        printSelectedVATInvoiceBill();
    }//GEN-LAST:event_jButton13ActionPerformed

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
                new EmployeePOS().setVisible(true);
            }
        });
    }

    private void setIcon() {
        setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("img/exfiltrat0z-icon.png")));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox<String> cmbInvoiceItemDiscount;
    private javax.swing.JComboBox<String> cmbSortInvoice;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton10;
    private javax.swing.JButton jButton11;
    private javax.swing.JButton jButton12;
    private javax.swing.JButton jButton13;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    private javax.swing.JButton jButton7;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel137;
    private javax.swing.JLabel jLabel138;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel143;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel155;
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
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTabbedPane jTabbedPaneInvoice;
    private javax.swing.JTabbedPane jTabbedPaneSubInvoice;
    private javax.swing.JLabel lblDate;
    public static final javax.swing.JLabel lblEmployeeUsername = new javax.swing.JLabel();
    private javax.swing.JLabel lblInvoiceId;
    private javax.swing.JLabel lblInvoiceTotal;
    private javax.swing.JLabel lblTime;
    private javax.swing.JPanel pnlHomeSubSelection;
    private javax.swing.JPanel pnlLogout;
    private javax.swing.JTable tblInvoice;
    private javax.swing.JTextField txtInvoiceAmountBalance;
    private javax.swing.JTextField txtInvoiceAmountBalance1;
    private javax.swing.JTextField txtInvoiceAmountDue;
    private javax.swing.JTextField txtInvoiceAmountDue1;
    private javax.swing.JTextField txtInvoiceAmountPaid;
    private javax.swing.JTextField txtInvoiceAmountPaid1;
    private javax.swing.JTextField txtInvoiceAmountSavings;
    private javax.swing.JTextField txtInvoiceAmountSavings1;
    private javax.swing.JTextField txtInvoiceGrandTotal;
    private javax.swing.JTextField txtInvoiceId;
    private javax.swing.JTextField txtInvoiceItemAmount;
    private javax.swing.JTextField txtInvoiceItemCode;
    private javax.swing.JTextField txtInvoiceItemLowStockCount;
    private javax.swing.JTextField txtInvoiceItemName;
    private javax.swing.JTextField txtInvoiceItemPurchasePrice;
    private javax.swing.JTextField txtInvoiceItemSellingPrice;
    private javax.swing.JTextField txtInvoiceItemStockCount;
    private javax.swing.JTextField txtInvoiceItemStockStatus;
    private javax.swing.JTextField txtInvoiceNBT;
    private javax.swing.JTextField txtInvoiceNewItemPrice;
    private javax.swing.JTextField txtInvoiceVAT;
    private javax.swing.JTextField txtSearchInvoice;
    // End of variables declaration//GEN-END:variables
}
