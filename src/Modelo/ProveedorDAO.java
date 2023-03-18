
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
/**
 *
 * @author godie
 */
public class ProveedorDAO {
    
    //Creamos las variables que vamos a utilizar en el ProveedorDAO e importamos las librerías correspondientes para realizar las consultas.
    Conexion cn = new Conexion();
    Connection con;
    PreparedStatement ps;
    ResultSet rs;
    String sql;
    
     //Creamos el método que nos permite registrar un nuevo Proveedor en la base de datos, nos retornará un dato de tipo boolean.
     public boolean RegistrarProveedor(Proveedor pro)
    {
        //Realizamos la sentencia sql para realizar la nueva inserción de un nuevo cliente a la base de datos, utilizamos la variable sql.
        sql = "INSERT INTO `proveedor`(`nit`, `nombre`, `telefono`, `direccion`, `razon`) VALUES (?,?,?,?,?)";
        //Usamos un try catch para poder captura las excepciones.
        try {
            //Utilizamos la Conexion a la base de datos sistemaventa.
            con = cn.getConnection();
            //Hacemos uso del PreparedStatement con su variable ps.
            ps = con.prepareStatement(sql);
            //Enviamos los parámetros que vamos a insertat a la base de datos sistemaventa a la tabla proveedor.
            ps.setString(1, pro.getNit());
            ps.setString(2, pro.getNombre());
            ps.setString(3, pro.getTelefono());
            ps.setString(4, pro.getDireccion());
            ps.setString(5, pro.getRazon());
            //Ejecutamos la consulta.
            ps.execute();
            //Retornamos un true.
            return true;
        } catch (SQLException e) {
            //Con un JOptionPane.showMessageDialog capturamos el error con e.toString().
            JOptionPane.showMessageDialog(null, e.toString());
            return false;
        }finally{
            try {
                //Cerramos la Conexion en caso de retornar un false.
                con.close();
            } catch (SQLException ex) {
                Logger.getLogger(ClienteDAO.class.getName()).log(Level.SEVERE, null, ex.toString());
            }
        }
    }
     
     //Creamos un método de tipo List<> que nos permitirá mostrar en una tabla todos nuestros proveedores que tenemos almacenados en nuestra base de datos sistemaventa.
    public List ListarProveedor()
    {
        List<Proveedor> lisprov = new ArrayList();
        //Usamos la variable sql para ejcutar la consulta a la vase de datos, a la tabla proveedor.
        sql = "SELECT `id_proveedor`, `nit`, `nombre`, `telefono`, `direccion`, `razon` FROM `proveedor`";
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
                Proveedor pr =new Proveedor();
                pr.setId_proveedor(rs.getInt("id_proveedor"));
                pr.setNit(rs.getString("nit"));
                pr.setNombre(rs.getString("nombre"));
                pr.setTelefono(rs.getString("telefono"));
                pr.setDireccion(rs.getString("direccion"));
                pr.setRazon(rs.getString("razon"));
                //Todo el resultado se lo agregamos a la lista y lo almacenamos en la variable pr.
                lisprov.add(pr);
            }
        } catch (SQLException e) {
            //Capturamos el error con e.toString.
            System.err.println("Hay problemas " + e.toString());
        }
        //Retornamos la lista de los proveedor.
        return lisprov;
        
    }
    
     //Creamos el método que me permite eliminar un proveedor de mi tabla proveedor de mi base de datos sistemventa. Enviamos como paràmetro el id_proveedor.
    
    public boolean EliminarProveedor(int id_proveedor)
    {
       //Lamamos la variable sql para ejecutar el DELETE.
        sql = "DELETE FROM `proveedor` WHERE id_proveedor=?";
        //Usamos try catch para capturar excesiones.
        try {
            //Hacemos uso del PreparedStatement con su variable ps.
            ps = con.prepareStatement(sql);
            //Enviamos el parámetro de eliminación.
            ps.setInt(1, id_proveedor);
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
    
    //Creamos el método que nos permite actualizar un proveedor, le mandamos como parámetro toda la clase Proveedor con una variable p.
    
    public boolean ModificarProveedor(Proveedor p)
    {
         //Realizamos la sentencia sql para realizar la modificación de un proveedor en la base de datos, utilizamos la variable sql.
        sql = "UPDATE `proveedor` SET `nit`=?,`nombre`=?,`telefono`=?,`direccion`=?,`razon`=? WHERE `id_proveedor`=?";
        //Usamos un try catch para poder captura las excepciones.
        try {
            //Utilizamos la Conexion a la base de datos sistemaventa.
            //con = cn.getConnection();
            //Hacemos uso del PreparedStatement con su variable ps.
            ps = con.prepareStatement(sql);
            //Enviamos los parámetros que vamos actualizar en la base de datos sistemaventa, a la tabla proveedor.
            ps.setString(1, p.getNit());
            ps.setString(2, p.getNombre());
            ps.setString(3, p.getTelefono());
            ps.setString(4, p.getDireccion());
            ps.setString(5, p.getRazon());
            ps.setInt(6, p.getId_proveedor());
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
