package Modelo;

/**
 *
 * @author godie
 */
public class Detalle {
    
    private int id_detalle;
    private String cod_producto;
    private int cantidad;
    private double precio;
    private int id_venta;

    public Detalle() {
    }

    public Detalle(int id_detalle, String cod_producto, int cantidad, double precio, int id_venta) {
        this.id_detalle = id_detalle;
        this.cod_producto = cod_producto;
        this.cantidad = cantidad;
        this.precio = precio;
        this.id_venta = id_venta;
    }

    public int getId_detalle() {
        return id_detalle;
    }

    public void setId_detalle(int id_detalle) {
        this.id_detalle = id_detalle;
    }

    public String getCod_producto() {
        return cod_producto;
    }

    public void setCod_producto(String cod_producto) {
        this.cod_producto = cod_producto;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public int getId_venta() {
        return id_venta;
    }

    public void setId_venta(int id_venta) {
        this.id_venta = id_venta;
    }
    
    
}
