
package Modelo;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
/**
 *
 * @author godie
 */
public class Conexion {
    //Creamos algunas variables que vamos a utilizar para la Conexion a la base de datos sistemaventa.
    Connection con;
    String myBD;
    public Connection getConnection()
    {
        try {
            // Cadena de conexion access
            // String acces = "jdbc:ucanaccess://D:/ventas.accdb";
            //Instanciamos la cadena de conexion
            myBD = "jdbc:mysql://localhost:3306/sistemaventa?serverTimezone=UTC";
            con = DriverManager.getConnection(myBD, "root", "");//pasr variable access
            //Retornamos la conexion
            return con;
        } catch (SQLException e) {
            //Imprimimos el error de la Exception cuando exista.
            System.out.println(e.toString());
        }
        //Por últimoo retornamos null para cerrar la conexion en caso de que exista un error.
        return null;
    }
}
