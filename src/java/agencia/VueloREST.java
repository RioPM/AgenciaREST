/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package agencia;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.ejb.Stateless;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;

/**
 *
 * @author Rio
 */

@Stateless
@Path("/vuelo")
public class VueloREST {
    
    /**
     * Dados un identificador de vuelo y una fecha, retorna el número de plazas que están libres
     */
    @GET
    public String consulta_libres (@QueryParam("id_vuelo")int id_vuelo, @QueryParam("fecha")int fecha) {
        return Integer.toString($consulta_libres(id_vuelo, fecha));
    }
    
    int $consulta_libres(int id_vuelo, int fecha) {
        //TODO write your implementation code here:
        try {
            Class.forName("org.sqlite.JDBC");
        }
        catch (ClassNotFoundException e){
            System.err.println(e.getMessage());
        }
        Connection connection = null;
        try {
            connection = DriverManager.getConnection("jdbc:sqlite:C:\\Users\\Rio\\Dropbox\\UPC\\AD\\P4\\practica4.sqlite");
            Statement statement = connection.createStatement();
            String id = Integer.toString(id_vuelo);
            String date = Integer.toString(fecha);
            ResultSet rs = statement.executeQuery("select num_plazas_max, num_plazas_ocupadas from vuelo_fecha where id_vuelo = " + id + " and fecha = " + date);
            int max = rs.getInt("num_plazas_max");
            int ocupadas = rs.getInt("num_plazas_ocupadas");
            int libres = max - ocupadas;
            return libres;
        }
        catch(SQLException e) {
            System.err.println(e.getMessage());
        }
        finally {
            try {
                if(connection != null)
                    connection.close();
            }
            catch(SQLException e) {
                //Error en tancar la connexio
                System.err.println(e.getMessage());
            }
        }
        return 0;
    }
    
    /**
     * Dados un identificador de vuelo y una fecha, reserva una plaza si quedan plazas libres (incrementa el número de plazas ocupadas en un vuelo en una fecha.
     * Si es posible realizar la reserva, esta operación retorna el número de plazas ocupadas que hay en el vuelo.
     * Si no es posible realizar la reserva, esta operación retorna -1.
     */
    //Es put o post? POST: Para guardar un nuevo objeto en la aplicación. PUT: Para actualizar un objeto.
    @POST
    public String reserva_plaza (@FormParam("id_vuelo")int id_vuelo, @FormParam("fecha")int fecha) {
        return Integer.toString($reserva_plaza(id_vuelo, fecha));
    }
    
    int $reserva_plaza(int id_vuelo, int fecha){
        //TODO write your implementation code here:
        try {
            Class.forName("org.sqlite.JDBC");
        }
        catch (ClassNotFoundException e){
            System.err.println(e.getMessage());
        }
        Connection connection = null;
        try {
            connection = DriverManager.getConnection("jdbc:sqlite:C:\\Users\\Rio\\Dropbox\\UPC\\AD\\P4\\practica4.sqlite");
            Statement statement = connection.createStatement();
            String id = Integer.toString(id_vuelo);
            String date = Integer.toString(fecha);
            ResultSet rs = statement.executeQuery("select num_plazas_max, num_plazas_ocupadas from vuelo_fecha where id_vuelo = " + id + " and fecha = " + date);
            int max = rs.getInt("num_plazas_max");
            int ocupadas = rs.getInt("num_plazas_ocupadas");
            int libres = max - ocupadas;
            if (libres > 0) {
                Integer res = statement.executeUpdate("update vuelo_fecha set num_plazas_ocupadas = num_plazas_ocupadas + 1 where id_vuelo = " + id + " and fecha = " + date);
                if (res > 0) return (ocupadas + 1);
                return -1;
            }
            return -1;
        }
        catch(SQLException e) {
            System.err.println(e.getMessage());
        }
        finally {
            try {
                if(connection != null)
                    connection.close();
            }
            catch(SQLException e) {
                //Error en tancar la connexio
                System.err.println(e.getMessage());
            }
        }
        return -1;
    }
    
}
