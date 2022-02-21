/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package QLNS.Dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Admin
 */
public class JDBCConnection {
    public static Connection getJDBCConnection(){
        Connection conn = null;
        final String url = "jdbc:sqlserver://localhost:1433;databaseName=Swing_QLCHNS;user=sa;password=123123";
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            conn =  DriverManager.getConnection(url);
             return conn;
        } catch (ClassNotFoundException | SQLException ex) {
            ex.printStackTrace();
        }
       return null;
    }
}
