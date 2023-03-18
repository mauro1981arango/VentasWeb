
package Modelo;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
/**
 *
 * @author godie
 */
public class LoginDAO {
   //Creamos las variables que vamos a utilizar en el LoginDAO e importamos las librerías correspondientes.
    Conexion cn = new Conexion();
    Connection con;
    PreparedStatement ps;
    ResultSet rs;
    String sql;
    //Llamamos el método login que recibe dos parámetros el correo y el pass
    public login log(String correo, String pass)
    {
        //Instanciamos la clase login.
        login lg = new login();
        //Llamamos la variable sql que hemos creado al inicio del LoginDAO para realizar la consulta a la basse de datos sistemaventa a la tabla usuarios.
        sql = "SELECT * FROM usuarios WHERE correo=? AND pass=?";
        //Creamos un try Catch para captura las posibles exceciones que puedan surgir
        try {
            //Primero ejecutamos la consulta.
            con = cn.getConnection();
            ps = con.prepareStatement(sql);
            //Le enviamos los parámetros de la consulta a la base de datos sistemaventa a la tabla usuarios.
            ps.setString(1, correo);
            ps.setString(2, pass);
            //Ejecutamos la consulta.
            rs = ps.executeQuery();
            //Con un if validamos nuestra consulta y el resultado lo almacenamos en la variable rs.
            if (rs.next()) {
                //Capturamos los datos de la tabla usuarios.
                lg.setId_usuario(rs.getInt("Id_usuario"));
                lg.setNombre(rs.getString("nombre"));
                lg.setCorreo(rs.getString("correo"));
                lg.setPass(rs.getString("pass"));
                lg.setRol(rs.getString("rol"));
            }
        } catch (SQLException e) {
            //Capturamos el error y lo mostramos. 
            System.err.println(e.toString());
        }
        return lg;
    }
    
    // Cremoa un método para registrar usuarios, enviamos como parámetro la clase login.
    public boolean RegistrarUsuarios(login reg)
    {
        sql = "INSERT INTO `usuarios`(`nombre`, `correo`, `pass`, `rol`) VALUES (?,?,?,?)";
        try {
            con = cn.getConnection();
            ps = con.prepareStatement(sql);
            ps.setString(1, reg.getNombre());
            ps.setString(2, reg.getCorreo());
            ps.setString(3, reg.getPass());
            ps.setString(4, reg.getRol());
            ps.execute();
            return true;
        } catch (SQLException e) {
            System.err.println(e.toString());
            return false;
        }
    }
}
