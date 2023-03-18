package Reportes;

import Modelo.Conexion;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartFrame;
import org.jfree.chart.JFreeChart;
import org.jfree.data.general.DefaultPieDataset;

/**
 *
 * @author godie
 */
public class Graficos {

    // Creamos un método estático que nos permite a accecer a los reportes gráficos para nuestras ventas. Enviamos como parámetro (String fecha).
    public static void Graficar(String fecha) {
        //Instanciamos variables para nuestra conexion a la base de datos.
        Connection con;
        Conexion cn = new Conexion();
        PreparedStatement ps;
        ResultSet rs;
        String sql;
        // Creamos try catch
        try {
            // Cramos la consulta a la base de datos usanmos la variable sql.
            sql = "SELECT total FROM ventas WHERE fecha=?";
            // Abrimos la conexion.
            con = cn.getConnection();
            ps = con.prepareStatement(sql);
            // Enviamos el parámetro de Búsqueda de la consulta.
            ps.setString(1, fecha);
            // Ejecutamos la consulta y la almacenamos en la variable rs del ResultSet.
            rs = ps.executeQuery();
            // Llamamos al DefaultPieDataset.
            DefaultPieDataset dateset = new DefaultPieDataset();
            // Usamom un ciclo while para recorrer el resultado.
            while (rs.next()) {
                // Dentro de la variable dateset se agregan los valores.
                dateset.setValue(rs.getString("total"), rs.getDouble("total"));
            }
            // Creamos el gráfico con JFreeChart.
            JFreeChart jf = ChartFactory.createPieChart("Reporte de Venta", dateset);
            // Agregamos a nuestro Frame.
            ChartFrame f = new ChartFrame("Total de ventas por día", jf);
            // Ponemos el tamaño a el Frame con f.setSize.
            f.setSize(1000, 500);
            // Lo posicionamos en el centro el Frame.
            f.setLocationRelativeTo(null);
            // Lo hacemos visible el Frame.
            f.setVisible(true);
        } catch (SQLException e) {
            System.err.println(e.toString());
        }
    }
}
