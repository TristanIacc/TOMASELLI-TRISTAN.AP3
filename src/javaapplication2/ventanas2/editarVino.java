/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package javaapplication2.ventanas2;
import javaapplication2.dbConnection;
import javax.swing.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.sql.*;

/**
 *
 * @author Tristi
 */
public class editarVino extends javax.swing.JFrame {
    
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(editarVino.class.getName());
public editarVino() {
        // Configuración de la ventana JFrame
        setTitle("Ventana Para Editar Vinos"); // Título de la ventana
        setSize(400, 300); // Tamaño de la ventana (ancho, alto)
        setLocationRelativeTo(null); // Centrar la ventana en la pantalla
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Cerrar solo esta ventana

        // Agregar componentes si es necesario
        JLabel label = new JLabel("Bienvenido a la ventana de Vinos");
        add(label);
    
        initComponents();
        cargarDatos();
    }

private void cargarDatos() {
    jTable1.setModel(obtenerDatosTabla());
} 
    /**
     * Creates new form Vinos
     */
   private DefaultTableModel obtenerDatosTabla() {
    // Crear el modelo con los nombres de las columnas
    DefaultTableModel modelo = new DefaultTableModel();
    modelo.addColumn("id_vino");
    modelo.addColumn("variedad");
    modelo.addColumn("año");
    modelo.addColumn("cantidad _de_botellas");
    modelo.addColumn("precio_por_botella");
    
    // Consulta SQL
    String sql = "SELECT * FROM vinos";
    
    try {
        Connection conn = dbConnection.conectar();
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(sql);
        
        // Recorrer los resultados
        while (rs.next()) {
            Object[] fila = new Object[5];
            fila[0] = rs.getString(1);                    // VARCHAR
            fila[1] = rs.getString(2);                   // VARCHAR
            fila[2] = rs.getString(3);                          // BIGINT
            fila[3] = rs.getString(4);         // BIGINT
            fila[4] = rs.getString(5);           // BIGINT
            
            modelo.addRow(fila);
        }
        
        rs.close();
        stmt.close();
        conn.close();
        
    } catch (SQLException e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(null, "Error al cargar datos: " + e.getMessage());
    }
    
    return modelo;
}
   
   private void editarVinoSeleccionado() {
    int filaSeleccionada = jTable1.getSelectedRow();
    
    if (filaSeleccionada == -1) {
        JOptionPane.showMessageDialog(this, 
            "Por favor, seleccione una fila para editar", 
            "Advertencia", 
            JOptionPane.WARNING_MESSAGE);
        return;
    }
    
    // Obtener datos de la fila seleccionada
    String idVino = jTable1.getValueAt(filaSeleccionada, 0).toString();
    String variedad = jTable1.getValueAt(filaSeleccionada, 1).toString();
    String anio = jTable1.getValueAt(filaSeleccionada, 2).toString();
    String cantidad = jTable1.getValueAt(filaSeleccionada, 3).toString();
    String precio = jTable1.getValueAt(filaSeleccionada, 4).toString();
    
    // Crear panel de edición
    JPanel panel = new JPanel(new java.awt.GridLayout(5, 2, 5, 5));
    
    JTextField txtVariedad = new JTextField(variedad);
    JTextField txtAnio = new JTextField(anio);
    JTextField txtCantidad = new JTextField(cantidad);
    JTextField txtPrecio = new JTextField(precio);
    
    panel.add(new JLabel("ID Vino:"));
    panel.add(new JLabel(idVino));
    panel.add(new JLabel("Variedad:"));
    panel.add(txtVariedad);
    panel.add(new JLabel("Año:"));
    panel.add(txtAnio);
    panel.add(new JLabel("Cantidad de Botellas:"));
    panel.add(txtCantidad);
    panel.add(new JLabel("Precio por Botella:"));
    panel.add(txtPrecio);
    
    int resultado = JOptionPane.showConfirmDialog(this, panel, 
        "Editar Vino - ID: " + idVino, 
        JOptionPane.OK_CANCEL_OPTION, 
        JOptionPane.PLAIN_MESSAGE);
    
    if (resultado == JOptionPane.OK_OPTION) {
        try {
            // Validar datos
            String nuevaVariedad = txtVariedad.getText().trim();
            int nuevoAnio = Integer.parseInt(txtAnio.getText().trim());
            int nuevaCantidad = Integer.parseInt(txtCantidad.getText().trim());
            double nuevoPrecio = Double.parseDouble(txtPrecio.getText().trim());
            
            if (nuevaVariedad.isEmpty()) {
                JOptionPane.showMessageDialog(this, "La variedad no puede estar vacía");
                return;
            }
            
            // Actualizar en la base de datos
            actualizarVino(idVino, nuevaVariedad, nuevoAnio, nuevaCantidad, nuevoPrecio);
            
            // Recargar datos
            cargarDatos();
            
            JOptionPane.showMessageDialog(this, 
                "Vino actualizado correctamente", 
                "Éxito", 
                JOptionPane.INFORMATION_MESSAGE);
            
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, 
                "Error: Verifique que año, cantidad y precio sean valores numéricos válidos", 
                "Error de formato", 
                JOptionPane.ERROR_MESSAGE);
        }
    }
}

   private void actualizarVino(String idVino, String variedad, int anio, int cantidad, double precio) {
    String sql = "UPDATE vinos SET variedad = ?, año = ?, cantidad_de_botellas = ?, precio_por_botella = ? WHERE id_vino = ?";
    
    try {
        Connection conn = dbConnection.conectar();
        PreparedStatement pstmt = conn.prepareStatement(sql);
        
        pstmt.setString(1, variedad);
        pstmt.setInt(2, anio);
        pstmt.setInt(3, cantidad);
        pstmt.setDouble(4, precio);
        pstmt.setString(5, idVino);
        
        int filasActualizadas = pstmt.executeUpdate();
        
        if (filasActualizadas == 0) {
            JOptionPane.showMessageDialog(this, 
                "No se encontró el vino con ID: " + idVino);
        }
        
        pstmt.close();
        conn.close();
        
    } catch (SQLException e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(this, 
            "Error al actualizar en la base de datos: " + e.getMessage(), 
            "Error SQL", 
            JOptionPane.ERROR_MESSAGE);
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

        editorBoton = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        editorBoton.setText("Editar");
        editorBoton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                editorBotonActionPerformed(evt);
            }
        });

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "id_vino", "variedad", "año", "cantidad_de_botellas", "precio_por_botella"
            }
        ));
        jScrollPane1.setViewportView(jTable1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(699, Short.MAX_VALUE)
                .addComponent(editorBoton)
                .addGap(20, 20, 20))
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 665, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(120, Short.MAX_VALUE)))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(128, 128, 128)
                .addComponent(editorBoton)
                .addContainerGap(149, Short.MAX_VALUE))
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addGap(12, 12, 12)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 275, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(13, Short.MAX_VALUE)))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void editorBotonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_editorBotonActionPerformed
editarVinoSeleccionado();        // TODO add your handling code here:
    }//GEN-LAST:event_editorBotonActionPerformed

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
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ReflectiveOperationException | javax.swing.UnsupportedLookAndFeelException ex) {
            logger.log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> new editarVino().setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton editorBoton;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    // End of variables declaration//GEN-END:variables
}
