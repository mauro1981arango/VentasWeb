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
import javax.swing.JComboBox;

/**
 *
 * @author godie
 */
public class ProductosDAO {
    //Creamos las variables que vamos a utilizar en el ProductoDAO e importamos las librerías correspondientes para realizar las consultas.

    Conexion cn = new Conexion();
    Connection con;
    PreparedStatement ps;
    ResultSet rs;
    String sql;

    //Creamos el método que nos permite registrar un nuevo Producto en la base de datos, nos retornará un dato de tipo boolean, y enviamos como parámetro la clase Productos, utilizamos la variable prod.
    public boolean RegistrarProducto(Productos prod) {
        //Realizamos la sentencia sql para realizar la nueva inserción de un nuevo cliente a la base de datos, utilizamos la variable sql.
        sql = "INSERT INTO `productos`(`codigo`, `descripcion`, `proveedor`, `stock`, `precio`) VALUES (?,?,?,?,?)";
        //Usamos un try catch para poder captura las excepciones.
        try {
            //Utilizamos la Conexion a la base de datos sistemaventa.
            con = cn.getConnection();
            //Hacemos uso del PreparedStatement con su variable ps.
            ps = con.prepareStatement(sql);
            //Enviamos los parámetros que vamos a insertat a la base de datos sistemaventa a la tabla productos.
            ps.setString(1, prod.getCodigo());
            ps.setString(2, prod.getDescripcion());
            ps.setString(3, prod.getProveedor());
            ps.setInt(4, prod.getStock());
            ps.setDouble(5, prod.getPrecio());
            //Ejecutamos la consulta.
            ps.execute();
            //Retornamos un true.
            return true;
        } catch (SQLException e) {
            //Con un JOptionPane.showMessageDialog capturamos el error con e.toString().
            JOptionPane.showMessageDialog(null, e.toString());
            return false;
        } finally {
            try {
                //Cerramos la Conexion en caso de retornar un false.
                con.close();
            } catch (SQLException ex) {
                Logger.getLogger(ClienteDAO.class.getName()).log(Level.SEVERE, null, ex.toString());
            }
        }
    }

    //Creamos un método de tipo List<> que nos permitirá mostrar en una tabla todos nuestros productos que tenemos almacenados en nuestra base de datos sistemaventa.
    public List ListarProductos() {
        List<Productos> lisprod = new ArrayList();
        //Usamos la variable sql para ejcutar la consulta a la vase de datos, a la tabla proveedor.
        sql = "SELECT `id_producto`, `codigo`, `descripcion`, `proveedor`, `stock`, `precio` FROM `productos`";
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
                Productos produ = new Productos();
                produ.setId_producto(rs.getInt("id_producto"));
                produ.setCodigo(rs.getString("codigo"));
                produ.setDescripcion(rs.getString("descripcion"));
                produ.setProveedor(rs.getString("proveedor"));
                produ.setStock(rs.getInt("stock"));
                produ.setPrecio(rs.getDouble("precio"));
                //Todo el resultado se lo agregamos a la lista y lo almacenamos en la variable produ.
                lisprod.add(produ);
            }
        } catch (SQLException e) {
            //Capturamos el error con e.toString.
            System.err.println("Hay problemas " + e.toString());
        }
        //Retornamos la lista de los proveedor.
        return lisprod;
    }
    
     //Creamos el método que me permite eliminar un producto de mi tabla productos, de mi base de datos sistemventa. Enviamos como paràmetro el id_producto.
    public boolean EliminarProducto(int id_producto)
    {
       //Lamamos la variable sql para ejecutar el DELETE.
        sql = "DELETE FROM `productos` WHERE id_producto=?";
        //Usamos try catch para capturar excesiones.
        try {
            //Hacemos uso del PreparedStatement con su variable ps.
            ps = con.prepareStatement(sql);
            //Enviamos el parámetro de eliminación.
            ps.setInt(1, id_producto);
            //Ejecutamos la setencia.
            ps.execute();
            //Retornamos true.
            return true;
        } catch (SQLException e) {
            //Imprimimos el error.
            System.err.println("Error " + e.toString());
            return false;
        }finally{
            try {
                //Cerramos la conexiion.
                con.close();
            } catch (SQLException ex) {
                Logger.getLogger(ClienteDAO.class.getName()).log(Level.SEVERE, null, ex.toString());
            }
        }
    }
    
     //Creamos el método que nos permite actualizar un producto, le mandamos como parámetro toda la clase Productos con una variable pro.
    public boolean ModificarProducto(Productos pro)
    {
         //Realizamos la sentencia sql para realizar la modificación de un producto en la base de datos, utilizamos la variable sql.
        sql = "UPDATE `productos` SET `codigo`=?,`descripcion`=?,`proveedor`=?,`stock`=?,`precio`=? WHERE `id_producto`=?";
        //Usamos un try catch para poder captura las excepciones.
        try {
            //Hacemos uso del PreparedStatement con su variable ps.
            ps = con.prepareStatement(sql);
            //Enviamos los parámetros que vamos actualizar en la base de datos sistemaventa, a la tabla proveedor.
            ps.setString(1, pro.getCodigo());
            ps.setString(2, pro.getDescripcion());
            ps.setString(3, pro.getProveedor());
            ps.setInt(4, pro.getStock());
            ps.setDouble(5, pro.getPrecio());
            ps.setInt(6, pro.getId_producto());
            //Ejecutamos la consulta.
            ps.executeUpdate();
            //Retornamos un true.
            return true;
        } catch (SQLException e) {
            //Imprimimos el error con e.toString.
            System.out.println(e.toString());
            //Nos retorna un false.
            return false;
        }finally{
            try {
                //Cerramos la conexion.
                con.close();
            } catch (SQLException ex) {
                Logger.getLogger(ClienteDAO.class.getName()).log(Level.SEVERE, null, ex.toString());
            }
        }
    }
    
    //Creamos un método para mostrar en un JComboBox todos los proveedores que tenemos registrados en la base de datos sistemaventa. Al JComboBox le enviamos como parámetro una varaiable proveedor.
    public void ConsultarProveedor(JComboBox proveedor)
    {
        //Usamos la variable sql para ejecutar la consulta a la tabla proveedor.
        sql = "SELECT nombre FROM proveedor";
        //Creamos try catch para capturar excesiones.
        try {
            //Llamamos la conexon.
            con = cn.getConnection();
            //Hacemos uso del PreparedStatement con su variable ps para ejecutar la consulta.
            ps = con.prepareStatement(sql);
            //Ejecutamos la consulta.
            rs = ps.executeQuery();
            //Usamos el buque while para recorrer  los resultados encontrados por nuestra consulta.
           while(rs.next())
           {
               //Pasamos el resultado al proveedor.
               proveedor.addItem(rs.getString("nombre"));
           } 
        } catch (SQLException e) {
            //Imprimimos el error.
            System.err.println("Hay un error " + e.toString());
        }
    }
    
    // Creamos un método que nos permita los buscar los procuctos para realizar un venta por medio dol codigo del producto.
    public Productos BuscarProducto(String codigo)
    {
        // Instanciamos la clase Productos.
        Productos product = new Productos();
        // Ejecuatamos la consulta usando la variable sql.
        sql = "SELECT * FROM productos WHERE codigo=?";
        // Creamos un try catch para capturar excesiones.
        try {
            // Usamos el método Conexion.
            con = cn.getConnection();
            //Hacemos uso del PreparedStatement con su variable ps.
            ps = con.prepareStatement(sql);
            // Enviamos el parámetro de búsqueda codigo.
            ps.setString(1, codigo);
            // Usamos la variable rs para obtener el resultado de la búsqueda.
            rs = ps.executeQuery();
            // Con un if obtenemos el dato, codigo.
            if (rs.next()) {
                // Usamos la variable product para obtener la descripcion, el stock y el precio del producto.
                product.setDescripcion(rs.getString("descripcion"));
                product.setStock(rs.getInt("stock"));
                product.setPrecio(rs.getDouble("precio"));
            } 
        } catch (SQLException e) {
            // Imprimimos el error
            System.err.println("Error " + e.toString());
        }
        return product;
    }
    // Creamos mérodo que permite mostrar los datos del negocio directamente desde la base de datos en el pdf que se genera al lomento de generar una nueva venta. Estos datos se muestran en el encabezado del documento.
    public Config BuscarDatos(){
        // Instanseamos la clase Config.
        Config conf = new Config();
        // Usamos la variable sql para realizar la consulta
        sql = "SELECT * FROM config";
        try {
            con = cn.getConnection();
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            if (rs.next()) {
                conf.setId(rs.getInt("id"));
                conf.setNit(rs.getString("nit"));
                conf.setNombre(rs.getString("nombre"));
                conf.setTelefono(rs.getString("telefono"));
                conf.setDireccion(rs.getString("direccion"));
                conf.setRazon(rs.getString("razon"));
            }
        } catch (SQLException e) {
            System.err.println(""+e.toString());
        }
        return conf;
    }
    // Creamos el método que nos permite modificar los datos de la empresa o negocio.
    public boolean ModificarDatosNegocio(Config conf)
    {
         //Realizamos la sentencia sql para realizar la modificación de los datos del negocio, utilizamos la variable sql.
        sql = "UPDATE `config` SET `nit`=?,`nombre`=?,`telefono`=?,`direccion`=?,`razon`=? WHERE `id`=?";
        //Usamos un try catch para poder captura las excepciones.
        try {
            //Hacemos uso del PreparedStatement con su variable ps.
            ps = con.prepareStatement(sql);
            //Enviamos los parámetros que vamos actualizar en la base de datos sistemaventa, a la tabla config.
            ps.setString(1, conf.getNit());
            ps.setString(2, conf.getNombre());
            ps.setString(3, conf.getTelefono());
            ps.setString(4,conf.getDireccion());
            ps.setString(5,conf.getRazon());
            ps.setInt(6, conf.getId());
            //Ejecutamos la consulta.
            ps.executeUpdate();
            //Retornamos un true.
            return true;
        } catch (SQLException e) {
            //Imprimimos el error con e.toString.
            System.out.println(e.toString());
            //Nos retorna un false.
            return false;
        }finally{
            try {
                //Cerramos la conexion.
                con.close();
            } catch (SQLException ex) {
                Logger.getLogger(ClienteDAO.class.getName()).log(Level.SEVERE, null, ex.toString());
            }
        }
    }
}
