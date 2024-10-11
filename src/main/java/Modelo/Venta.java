/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Modelo;

public class Venta {

    protected int cantidad;
    protected String cliente, fecha, hora, producto, ruc;

    public Venta(int cantidad, String cliente, String fecha, String hora, String producto, String ruc) {
        this.cantidad = cantidad;
        this.cliente = cliente;
        this.fecha = fecha;
        this.hora = hora;
        this.producto = producto;
        this.ruc = ruc;
    }

    // Getters y Setters
    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public String getCliente() {
        return cliente;
    }

    public void setCliente(String cliente) {
        this.cliente = cliente;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }

    public String getProducto() {
        return producto;
    }

    public void setProducto(String producto) {
        this.producto = producto;
    }

    public String getRuc() {
        return ruc;
    }

    public void setRuc(String ruc) {
        this.ruc = ruc;
    }
    
    
    public double asignaPrecio() {
        switch (producto) {
            //productos
            case "Lavadora":
                return 1500.00;
            case "Refrigeradora":
                return 2500.00;
            case "Licuadora":
                return 500.00;
            case "Extractora":
                return 150.00;
            case "Radiograbadora":
                return 750.00;
            case "DVD":
                return 100.00;
            case "Blue Ray":
                return 250.00;
            default:
                return 0.00;
        }
    }

    public double calculaSubtotal() {
        return cantidad * asignaPrecio();
    }
    
}
