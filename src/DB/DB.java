/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DB;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.JOptionPane;

/**
 *
 * @author 0x5un5h1n3
 */
public class DB {

    public static Connection con;
    public static ResultSet r;

    public static Connection con() throws Exception {
        try {

            Class.forName("com.mysql.jdbc.Driver");
            String url = "jdbc:mysql://localhost/exfiltro_mart";
            con = DriverManager.getConnection(url, "root", "22622");
        } catch (ClassNotFoundException | SQLException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }

        return con;
    }

    public static void putData(String sql) throws Exception {
        Statement s = con().prepareStatement(sql);
        s.executeUpdate(sql);
    }

    public static ResultSet getData(String sql) throws Exception {
        r = con().createStatement().executeQuery(sql);
        return r;
    }

    public static Connection getConnection() {

        Connection cn = null;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            cn = DriverManager.getConnection("jdbc:mysql://localhost/exfiltro_mart", "root", "22622");
        } catch (ClassNotFoundException | SQLException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
        return cn;
    }
}
