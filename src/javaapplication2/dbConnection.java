/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package javaapplication2;
import java.sql.*;
/**
 *
 * @author Tristi
 */
public class dbConnection {
    static String url="jdbc:mysql://localhost:3306/viaragodb";
    static String user="root";
    static String password="Mica170324";
    
    public static Connection conectar()
    {
       Connection con=null;
       try{
           con=DriverManager.getConnection(url,user,password);
           System.out.println("Conexion exitosa");
       }catch(SQLException e)
       {
           e.printStackTrace();           
       }    
    return con; 
}
}
