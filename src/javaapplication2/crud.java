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
public class crud {
    public static boolean registrarVinoC(String idVino, String variedad, long año, long cantidad, long precio){
    String query = "INSERT INTO vinos (id_vino, variedad, año, cantidad_de_botellas, precio_por_botella) VALUES (?,?,?,?,?)"; 
    try {
        Connection con=dbConnection.conectar(); 
        PreparedStatement ps=con.prepareStatement(query); 
        ps.setString(1, idVino);
        ps.setString(2, variedad);
        ps.setLong(3, año);
        ps.setLong(4, cantidad);
        ps.setLong(5, precio);
        
        ps.executeUpdate(); 
        System.out.println("vino registrado con exito");
        return true;
    }
    catch(SQLException ex)
    {
        System.out.println("error al registrar vino");
        ex.printStackTrace();
        return false;
    }}
    
    
    public static boolean registrarVentaC(String idVino, long cantidadBotellas, long montoVenta, String vendedor){
    String query = "INSERT INTO ventas (id_vino, cantidad_de_botellas, monto_de_la_venta, vendedor_medio) VALUES (?,?,?,?)"; 
    try {
        Connection con=dbConnection.conectar(); 
        PreparedStatement ps=con.prepareStatement(query); 
        ps.setString(1, idVino);
        ps.setLong(2, cantidadBotellas);
        ps.setLong(3, montoVenta);
        ps.setString(4, vendedor);
        
        
        ps.executeUpdate(); 
        System.out.println("venta registrada con exito");
        return true;
    }
    catch(SQLException ex)
    {
        System.out.println("error al registrar venta");
        ex.printStackTrace();
        return false;
    }
}}
