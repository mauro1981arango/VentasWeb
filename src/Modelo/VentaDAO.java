package Modelo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

public class VentaDAO {
    
     // Creamos un método que nos permite obtener el id(max) de la venta, y poder hacer usu de este para guardar el id de la venta en la tabla detalle.
    public int idVenta()
    {
        // Creamos una variable de tipo int para almacenar el id, lo inicializamos en 0.
        int id = 0;
        // Usamos la variable sql para realizar la condulta.
        sql = "SELECT MAX(id_venta) FROM `ventas`";
        // Usamostry ctch 
        try {
            //Utilizamos la Conexion a la base de datos sistemaventa.
            con = cn.getConnection();
            //Hacemos uso del PreparedStatement con su variable ps.
            ps = con.prepareStatement(sql);
            // Ejecutamos la consulta guardamos el resultado en la variable rs
            rs = ps.executeQuery();
            // Usamos un if
            if (rs.next()) {
                // Se agrega el id en la variable rs.
                id = rs.getInt(1);
            }
        } catch (SQLException e) {
            // Se imprime la excepcion.
            System.err.println(e.toString());
        }   
        return id;
    }
    
     //Creamos las variables que vamos a utilizar en el ProductoDAO e importamos las librerías correspondientes para realizar las consultas.

    Conexion cn = new Conexion();
    Connection con;
    PreparedStatement ps;
    ResultSet rs;
    String sql;
    // Creamos una variable de tipo int r para la respuesta de la consulta.
    int r;
    
    // Creamos un método de tipo entero que nos va a permitir registrar una nueva venta en la base de datos sistemaventa. Enviamos como parámetro la clase Venta con su variable v.
    public int RegistrarVenta(Venta v)
    {
        // Usamos la variable sql para ejecutar la consulta.
        sql = "INSERT INTO `ventas`(`cliente`, `vendedor`, `total`, `fecha`) VALUES (?,?,?,?)";
        // Creamos un try catch para capturar las excesiones.
        try {
            //Utilizamos la Conexion a la base de datos sistemaventa.
            con = cn.getConnection();
            //Hacemos uso del PreparedStatement con su variable ps.
            ps = con.prepareStatement(sql);
            //Enviamos los parámetros que vamos a insertat a la base de datos sistemaventa a la tabla proveedor.
            ps.setString(1, v.getCliente());
            ps.setString(2, v.getVendedor());
            ps.setDouble(3, v.getTotal());
            ps.setString(4, v.getFecha());
            // Ejecutamos la consulta
            ps.execute();
        } catch (SQLException e) {
            // Se inprime el error
            System.err.println(e.toString());
        }finally{
            try {
                // Cerramos la Conexion.
                con.close();
            } catch (SQLException ex) {
                Logger.getLogger(VentaDAO.class.getName()).log(Level.SEVERE, null, ex.toString());
            }
        }
        return r;
    }
    
    // Creamos el método que nos permite registrar el detalle de la nueva venta en la base de datos sistemaventa.
    public int RegistrarDetalle(Detalle detalle)
    {
        // Usamos la variable sql para ejecutar la consulta.
        sql = "INSERT INTO `detalle`(`cod_producto`, `cantidad`, `precio`, `id_venta`) VALUES (?,?,?,?)";
        // Creamos un try catch para capturar las excesiones.
        try {
            //Utilizamos la Conexion a la base de datos sistemaventa.
            con = cn.getConnection();
            //Hacemos uso del PreparedStatement con su variable ps.
            ps = con.prepareStatement(sql);
            //Enviamos los parámetros que vamos a insertat a la base de datos sistemaventa a la tabla proveedor.
            ps.setString(1, detalle.getCod_producto());
            ps.setInt(2, detalle.getCantidad());
            ps.setDouble(3, detalle.getPrecio());
            ps.setInt(4, detalle.getId_venta());
            // Ejecutamos la consulta
            ps.execute();
        } catch (SQLException e) {
            // Se inprime el error
            System.err.println(e.toString());
        }finally{
            try {
                // Cerramos la Conexion.
                con.close();
            } catch (SQLException ex) {
                Logger.getLogger(VentaDAO.class.getName()).log(Level.SEVERE, null, ex.toString());
            }
        }
        return r;
    }
    
    // Creamos un método para actualizar el stock de los productos en la vase de datos sistemaventa. Enviamos dos parámetros, int cantidad, String codigo.
    public boolean ActualizarStock(int cantidad, String codigo)
    {
        // Usamos la variable sql para realizar la consulta a la base de datos
        sql = "UPDATE productos SET stock=? WHERE codigo=?"; 
        // Usamos try catch
        try {
            // Instanciamos la conexion
            con = cn.getConnection();
            //Hacemos uso del PreparedStatement con su variable ps.
            ps = con.prepareStatement(sql);
            // Enviamos los parámetros
            ps.setInt(1, cantidad);
            ps.setString(2, codigo);
            // Ejecutamos la consulta
            ps.execute();
            return true;
        } catch (SQLException e) {
            // Imprimimos el error
            System.err.println(e.toString());
            return false;
        }finally{
            try {
                // Cerramos la Conexion.
                con.close();
            } catch (SQLException ex) {
                Logger.getLogger(VentaDAO.class.getName()).log(Level.SEVERE, null, ex.toString());
            }
        }
    }
    
     //Creamos un método que nos permite listar todas las ventas realizadas.
    public List ListarVentas() {
        List<Venta> lisventas = new ArrayList();
        //Usamos la variable sql para ejcutar la consulta a la vase de datos, a la tabla ventas.
        sql = "SELECT * FROM `ventas`";
        //Creamos try catch para capturar las excepciones.
        try {
            //Llamamos al método Connection.
            con = cn.getConnection();
            //Usamos PreparedStatement con su variable ps.
            ps = con.prepareStatement(sql);
            //Ejecutamos la consulta y guardamos el resultado en la variable rs del ResultSet.
            rs = ps.executeQuery();
            //Con un buque while recorremos todos los registros que tenemos almacenados en la tabla proveedor en la base de datos sistemaventa.
            while (rs.next()) {
                //Instanciamos la clase Proveedor.
                Venta vent = new Venta();
                vent.setId_venta(rs.getInt("id_venta"));
                vent.setCliente(rs.getString("cliente"));
                vent.setVendedor(rs.getString("VENDEDOR"));
                vent.setTotal(rs.getDouble("total"));
                //Todo el resultado se lo agregamos a la lista y lo almacenamos en la variable produ.
                lisventas.add(vent);
            }
        } catch (SQLException e) {
            //Capturamos el error con e.toString.
            System.err.println("Hay problemas " + e.toString());
        }
        //Retornamos la lista de los proveedor.
        return lisventas;
    }
}
