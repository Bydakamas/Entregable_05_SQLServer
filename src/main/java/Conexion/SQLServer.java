/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Conexion;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author Asus
 */
public class SQLServer {
// URL de conexión a la base de datos
    private static final String URL = "jdbc:sqlserver://localhost:1433;databaseName=Venta";
    
    // Credenciales de acceso
    private static final String USER = "sa";
    private static final String PASSWORD = "123";

    // Método para establecer la conexión
    public static Connection getConnection() throws SQLException {
        Connection connection = null;
        try {
            // Registrar el driver JDBC
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            
            // Establecer la conexión
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("Conexión exitosa a la base de datos.");
        } catch (ClassNotFoundException e) {
            System.out.println("Error: No se encontró el driver JDBC.");
            e.printStackTrace();
        } catch (SQLException e) {
            System.out.println("Error al conectar a la base de datos.");
            e.printStackTrace();
            throw e;
        }
        return connection;
    }

    // Método para cerrar la conexión
    public static void closeConnection(Connection connection) {
        if (connection != null) {
            try {
                connection.close();
                System.out.println("Conexión cerrada.");
            } catch (SQLException e) {
                System.out.println("Error al cerrar la conexión.");
                e.printStackTrace();
            }
        }
    }
}
