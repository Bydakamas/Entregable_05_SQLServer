/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controlador;

import Modelo.Contado;
import Vista.JFVentaContado;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class ControllerContado {

    private ArrayList<Contado> ventas; // Lista para almacenar las ventas
    private JFVentaContado vista; // Vista de la venta al contado

    public ControllerContado(JFVentaContado vista) {
        this.vista = vista;
        this.ventas = new ArrayList<>(); // Inicializar la lista de ventas
        this.iniciarVista(); // Iniciar la vista
    }

    // Método para iniciar la vista
    private void iniciarVista() {
        configurarComboBoxProductos();
        configurarBotonAdquirir();
        actualizarFechaYHora();
    }

    // Configurar el combo box de productos
    private void configurarComboBoxProductos() {
        String[] productos = {"Lavadora", "Refrigeradora", "Licuadora", "Extractora", "Radiograbadora", "DVD", "Blue Ray"};
        vista.cmbProducto.setModel(new DefaultComboBoxModel<>(productos));
    }

    // Configurar el botón "Adquirir"
    private void configurarBotonAdquirir() {
        vista.btnAdquirir.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                adquirirProducto();
            }
        });
    }

    // Método para adquirir un producto
    public void adquirirProducto() {
        String cliente = vista.txtCliente.getText();
        String ruc = vista.txtRuc.getText();
        String producto = (String) vista.cmbProducto.getSelectedItem();
        int cantidad = obtenerCantidad();

        if (producto == null || producto.isEmpty()) {
            mostrarError("Por favor, seleccione un producto.");
            return;
        }

        if (cantidad <= 0) {
            mostrarError("La cantidad debe ser mayor que cero.");
            return;
        }

        if (productoYaAgregado(producto)) {
            mostrarError("El producto ya ha sido agregado.");
            return;
        }

        Contado nuevaVenta = crearNuevaVenta(cantidad, cliente, ruc, producto);
        ventas.add(nuevaVenta);
        mostrarVentaEnTabla(nuevaVenta);
        mostrarResumenDeVenta();
        
        // Guardar la venta en la base de datos
        nuevaVenta.almacenarDatosSqlServer(); // Llamar al método en la clase Contado
    }

    // Método para obtener la cantidad ingresada
    private int obtenerCantidad() {
        try {
            return Integer.parseInt(vista.txtCantidad.getText());
        } catch (NumberFormatException e) {
            return -1; // Indicar un error en la cantidad
        }
    }

    // Método para mostrar un mensaje de error
    private void mostrarError(String mensaje) {
        JOptionPane.showMessageDialog(vista, mensaje, "Error", JOptionPane.ERROR_MESSAGE);
    }

    // Comprobar si el producto ya fue agregado
    private boolean productoYaAgregado(String producto) {
        for (Contado venta : ventas) {
            if (venta.getProducto().trim().equalsIgnoreCase(producto.trim())) {
                return true; // El producto ya existe
            }
        }
        return false; // El producto no existe
    }

    // Crear una nueva venta de tipo Contado
    private Contado crearNuevaVenta(int cantidad, String cliente, String ruc, String producto) {
        SimpleDateFormat formatoFecha = new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat formatoHora = new SimpleDateFormat("HH:mm:ss");
        Date fechaActual = new Date();
        String fecha = formatoFecha.format(fechaActual);
        String hora = formatoHora.format(fechaActual);

        return new Contado(cantidad, cliente, fecha, hora, producto, ruc);
    }

    // Método para mostrar la venta en la tabla
    private void mostrarVentaEnTabla(Contado venta) {
        DefaultTableModel modelo = (DefaultTableModel) vista.tblProducto.getModel();
        int itemCount = modelo.getRowCount() + 1;

        Object[] fila = {
            itemCount, // Contador de productos
            venta.getProducto(),
            venta.getCantidad(),
            venta.asignaPrecio(),
            venta.calculaSubtotal()
        };
        modelo.addRow(fila);
    }

    // Método para mostrar el resumen de la venta
    private void mostrarResumenDeVenta() {
        double subtotalTotal = 0;
        double descuentoTotal = 0;

        for (Contado venta : ventas) {
            subtotalTotal += venta.calculaSubtotal();
            descuentoTotal += venta.calculaDescuento();
        }

        double netoTotal = subtotalTotal - descuentoTotal;
        String resumen = crearResumenDeVenta(subtotalTotal, descuentoTotal, netoTotal);
        vista.txtResumen.setText(resumen);
        vista.txtMontoPago.setText("$" + String.format("%.2f", netoTotal));
    }

    // Crear el resumen de la venta
    private String crearResumenDeVenta(double subtotalTotal, double descuentoTotal, double netoTotal) {
        return "***** RESUMEN DE VENTA *****\n"
                + "-----------------------------------\n"
                + "CLIENTE: " + (ventas.isEmpty() ? "" : ventas.get(0).getCliente()) + "\n"
                + "RUC: " + (ventas.isEmpty() ? "" : ventas.get(0).getRuc()) + "\n"
                + "FECHA: " + (ventas.isEmpty() ? "" : ventas.get(0).getFecha()) + "\n"
                + "HORA: " + (ventas.isEmpty() ? "" : ventas.get(0).getHora()) + "\n"
                + "-----------------------------------\n"
                + "SUBTOTAL: $" + String.format("%.2f", subtotalTotal) + "\n"
                + "DESCUENTO: $" + String.format("%.2f", descuentoTotal) + "\n"
                + "NETO: $" + String.format("%.2f", netoTotal) + "\n";
    }

    // Método para actualizar la fecha y hora en tiempo real
    public void actualizarFechaYHora() {
        java.util.Timer timer = new java.util.Timer();
        timer.scheduleAtFixedRate(new java.util.TimerTask() {
            @Override
            public void run() {
                SimpleDateFormat formatoFecha = new SimpleDateFormat("dd/MM/yyyy");
                SimpleDateFormat formatoHora = new SimpleDateFormat("HH:mm:ss");
                Date fechaActual = new Date();
                String fecha = formatoFecha.format(fechaActual);
                String hora = formatoHora.format(fechaActual);
                vista.lblFecha.setText(fecha);
                vista.lblHora.setText(hora);
            }
        }, 0, 1000); // Actualizar cada segundo
    }
}
