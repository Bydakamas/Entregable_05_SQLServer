/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Modelo;

import Conexion.SQLServer;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Credito extends Venta {

    private int letras;

    public Credito(int cantidad, String cliente, String fecha, String hora, String producto, String ruc, int letras) {
        super(cantidad, cliente, fecha, hora, producto, ruc);
        this.letras = letras;
    }

    // Método para obtener la cantidad de letras
    public int getLetras() {
        return letras;
    }

    // Método para asignar la cantidad de letras
    public void setLetras(int letras) {
        this.letras = letras;
    }

    // Método para calcular el descuento
    public double calculaDescuento() {
        double subtotal = calculaSubtotal();
        double porcentajeDescuento = 0;

        if (subtotal < 1000) {
            porcentajeDescuento = 0.03;
        } else if (subtotal >= 1000 && subtotal <= 3000) {
            porcentajeDescuento = 0.05;
        } else {
            porcentajeDescuento = 0.08;
        }

        return subtotal * porcentajeDescuento;
    }

    // Método para calcular el monto mensual
    public double calculaMontoMensual() {
        double subtotal = calculaSubtotal();
        double descuento = calculaDescuento();
        double montoMensual = (subtotal - descuento) / letras;
        return montoMensual;
    }

    public double calculaMontoTotal() {
        double subtotal = calculaSubtotal(); // Calcular el subtotal
        double descuento = calculaDescuento(); // Aplicar el descuento según las reglas definidas
        return subtotal - descuento; // Retornar el monto total con descuento
    }

    // Método para almacenar los datos en SQL Server
    public void almacenarDatosSqlServer() {
        String insertVenta = "INSERT INTO Venta (cantidad, cliente, fecha, hora, producto, ruc, tipo) VALUES (?, ?, ?, ?, ?, ?, 'Credito')";
        String insertCredito = "INSERT INTO Credito (venta_id, letras) VALUES (?, ?)";

        try (Connection conn = SQLServer.getConnection(); PreparedStatement stmtVenta = conn.prepareStatement(insertVenta, PreparedStatement.RETURN_GENERATED_KEYS); PreparedStatement stmtCredito = conn.prepareStatement(insertCredito)) {

            // Setear los valores en la tabla Venta
            stmtVenta.setInt(1, getCantidad());
            stmtVenta.setString(2, getCliente());
            stmtVenta.setString(3, getFecha());
            stmtVenta.setString(4, getHora());
            stmtVenta.setString(5, getProducto());
            stmtVenta.setString(6, getRuc());

            // Ejecutar la inserción de la venta
            int affectedRows = stmtVenta.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("No se pudo insertar la venta, ningún registro afectado.");
            }

            // Obtener el ID de la venta recién insertada
            try (var generatedKeys = stmtVenta.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    int ventaId = generatedKeys.getInt(1); // Obtener el ID de la venta

                    // Insertar en la tabla Credito
                    stmtCredito.setInt(1, ventaId); // Setear el ID de la venta
                    stmtCredito.setInt(2, letras); // Setear el número de letras
                    stmtCredito.executeUpdate(); // Ejecutar la inserción en Credito

                    System.out.println("Venta a crédito registrada correctamente en la base de datos.");
                } else {
                    throw new SQLException("No se pudo obtener el ID de la venta.");
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error al registrar la venta a crédito en la base de datos: " + e.getMessage());
        }
    }
}