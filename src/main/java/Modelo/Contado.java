/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Modelo;
import Conexion.SQLServer;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Contado extends Venta {

    public static int n = 0; // Variable estática para contar la cantidad de objetos

    public Contado(int cantidad, String cliente, String fecha, String hora, String producto, String ruc) {
        super(cantidad, cliente, fecha, hora, producto, ruc);
        Contado.n++; // Incrementar la variable n
    }

    public static int getN() {
        return n;
    }

    public double calculaDescuento() {
        double subtotal = calculaSubtotal();
        if (subtotal > 3000) {
            return subtotal * 0.12;  // Descuento del 12%
        } else if (subtotal >= 1000) {
            return subtotal * 0.08;  // Descuento del 8%
        } else {
            return subtotal * 0.05;  // Descuento del 5%
        }
    }

    public double calculaTotal() {
        return calculaSubtotal() - calculaDescuento();
    }

    // Método para almacenar los datos en SQL Server
    public void almacenarDatosSqlServer() {
        String query = "INSERT INTO Venta (cantidad, cliente, fecha, hora, producto, ruc, tipo) VALUES (?, ?, ?, ?, ?, ?, 'Contado')";

        try (Connection conn = SQLServer.getConnection(); 
             PreparedStatement stmt = conn.prepareStatement(query)) {

            // Setear los valores en la consulta
            stmt.setInt(1, getCantidad());
            stmt.setString(2, getCliente());
            stmt.setString(3, getFecha());
            stmt.setString(4, getHora());
            stmt.setString(5, getProducto());
            stmt.setString(6, getRuc());

            // Ejecutar la consulta
            stmt.executeUpdate();
            System.out.println("Venta al contado registrada correctamente en la base de datos.");

        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error al registrar la venta en la base de datos: " + e.getMessage());
        }
    }
}
