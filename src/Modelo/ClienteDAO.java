
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
public class ClienteDAO {
     //Creamos las variables que vamos a utilizar en el ClienteDAO e importamos las librerías correspondientes para realizar las consultas.
    Conexion cn = new Conexion();
    Connection con;
    PreparedStatement ps;
    ResultSet rs;
    String sql;
    //Creamos el método que nos permite registrar un nuevo Cliente en la base de datos, nos retornará un dato de tipo boolean.
    public boolean RegistrarCliente(Cliente cli)
    {
        //Realizamos la sentencia sql para realizar la nueva inserción de un nuevo cliente a la base de datos, utilizamos la variable sql.
        sql = "INSERT INTO clientes(cedula, nombre, telefono, direccion, razon) VALUES (?,?,?,?,?)";
        //Usamos un try catch para poder captura las excepciones.
        try {
            //Utilizamos la Conexion a la base de datos sistemaventa.
            con = cn.getConnection();
            //Hacemos uso del PreparedStatement con su variable ps.
            ps = con.prepareStatement(sql);
            //Enviamos los parámetros que vamos a insertat a la base de datos sistemaventa a la tabla clientes.
            ps.setInt(1, cli.getCedula());
            ps.setString(2, cli.getNombre());
            ps.setInt(3, cli.getTelefono());
            ps.setString(4, cli.getDireccion());
            ps.setString(5, cli.getRazon());
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
    //Creamos un método de tipo List<> que nos permitirá mostrar en una tabla todos nuestros clientes que tenemos almacenados en nuestra base de datos sistemaventa.
    public List ListarCliente()
    {
        List<Cliente> lista_clientes = new ArrayList();
        //Usamos la variable sql para ejcutar la consulta a la vase de datos, a la tabla clientes.
        sql = "SELECT `id_cliente`, `cedula`, `nombre`, `telefono`, `direccion`, `razon` FROM `clientes`";
        //Creamos try catch para capturar las excepciones.
        try {
            //Llamamos al método Connection.
            con = cn.getConnection();
            //Usamos PreparedStatement con su variable ps.
            ps = con.prepareStatement(sql);
            //Ejecutamos la consulta y guardamos el resultado en la variable rs del ResultSet.
            rs = ps.executeQuery();
            //Con un buque while recorremos todos los registros que tenemos almacenados en la tabla clientes en la base de datos sistemaventa.
            while (rs.next()) {
                //Instanciamos la clase Cliente.
                Cliente cli =new Cliente();
                cli.setId_cliente(rs.getInt("id_cliente"));
                cli.setCedula(rs.getInt("cedula"));
                cli.setNombre(rs.getString("nombre"));
                cli.setTelefono(rs.getInt("telefono"));
                cli.setDireccion(rs.getString("direccion"));
                cli.setRazon(rs.getString("razon"));
                //Todo el resultado se lo agregamos a la lista y lo almacenamos en la variable lista_clientes.
                lista_clientes.add(cli);
            }
        } catch (SQLException e) {
            //Capturamos el error con e.toString.
            System.err.println("Hay problemas " + e.toString());
        }
        //Retornamos la lista de los clientes.
        return lista_clientes;
        
    }
    //Creamos el método que me permite eliminar un cliente de mi tabla clientes de mi base de datos sistemventa. Enviamos como paràmetro el id_cliente
    public boolean EliminarCliente(int id_cliente)
    {
       //Lamamos la variable sql para ejecutar el DELETE.
        sql = "DELETE FROM `clientes` WHERE id_cliente=?";
        //Usamos try catch para capturar excesiones.
        try {
            //Hacemos uso del PreparedStatement con su variable ps.
            ps = con.prepareStatement(sql);
            //Enviamos el parámetro de eliminación.
            ps.setInt(1, id_cliente);
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
    //Creamos el método que nos permite actualizar un cliente, le mandamos como parámetro toda la clase Cliente con una variable cl.
    public boolean ModificarCliente(Cliente cl)
    {
         //Realizamos la sentencia sql para realizar la modificación de un cliente en la base de datos, utilizamos la variable sql.
        sql = "UPDATE `clientes` SET `cedula`=?,`nombre`=?,`telefono`=?,`direccion`=?,`razon`=? WHERE `id_cliente`=?";
        //Usamos un try catch para poder captura las excepciones.
        try {
            //Utilizamos la Conexion a la base de datos sistemaventa.
            //con = cn.getConnection();
            //Hacemos uso del PreparedStatement con su variable ps.
            ps = con.prepareStatement(sql);
            //Enviamos los parámetros que vamos actualizar en la base de datos sistemaventa, a la tabla clientes.
            ps.setInt(1, cl.getCedula());
            ps.setString(2, cl.getNombre());
            ps.setInt(3, cl.getTelefono());
            ps.setString(4, cl.getDireccion());
            ps.setString(5, cl.getRazon());
            ps.setInt(6, cl.getId_cliente());
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
    
    // Creamos un método que nos petmite buscar un cliente para realizar una nueva venta. Le enviamos como parámetro de búsqueda la cedula.
    public Cliente BuscarCliente(int cedula)
    {
        // Instanciamos la clase Cliente
        Cliente cliente = new Cliente();
        // Usamos la vriable sql para ejecutar la cunsulta a la bse de datos.
        sql = "SELECT * FROM clientes WHERE cedula=?";
        // Usamos try catch para capturar excesiones
        try {
            // Se llama la clase Conexion.
            con = cn.getConnection();
            // Usamos la variable ps del PrepareStatement
            ps = con.prepareStatement(sql);
            // Enviamos el parámetro de búsqueda
            ps.setInt(1, cedula);
            // Ejecutamos la consulta con la variable rs del ResultSet para almacenar la consulta.
            rs = ps.executeQuery();
            // Usamos un if para obtener los datos del cliente.
            if (rs.next()) {
                cliente.setNombre(rs.getString("nombre"));
                cliente.setTelefono(rs.getInt("telefono"));
                cliente.setDireccion(rs.getString("direccion"));
                cliente.setRazon(rs.getString("razon"));
            }
        } catch (SQLException e) {
            // Capturamos el error
            System.err.println(e.toString());
        }
        return cliente;
    }
}
