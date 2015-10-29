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
@Path("/hotel")
public class HotelREST {
    
    /**
     * Dados un identificador de hotel y una fecha, retorna el número de habitaciones que están libres
     */
    @GET
    public String consulta_libres (@QueryParam("id_hotel")int id_hotel, @QueryParam("fecha")int fecha) {
        return Integer.toString($consulta_libres(id_hotel, fecha));
    }
    int $consulta_libres(int id_hotel, int fecha) {
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
            ResultSet rs = statement.executeQuery("SELECT num_hab_libres FROM hotel_fecha where id_hotel = "+ id_hotel +" and fecha = "+ fecha );
            return Integer.parseInt(rs.getString("num_hab_libres"));
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
     * Dados un identificador de hotel y una fecha, reserva una habitación si quedan habitaciones libres (incrementa el número de habitaciones ocupadas en esa fecha en el hotel).
     * Si es posible realizar la reserva, esta operación retorna el número de habitaciones ocupadas que hay en el hotel.
     * Si no es posible realizar la reserva, esta operación retorna -1.
     */
    //Es put o post? POST: Para guardar un nuevo objeto en la aplicación. PUT: Para actualizar un objeto.
    @POST
    public String reserva_habitacion (@FormParam("id_vuelo")int id_vuelo, @FormParam("fecha")int fecha) {
        return Integer.toString($reserva_habitacion(id_vuelo, fecha));
    }
    
    int $reserva_habitacion(int id_hotel, int fecha) {
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
            ResultSet res = statement.executeQuery("SELECT * FROM hotel_fecha where id_hotel = "+ id_hotel +" and fecha = "+ fecha);
            int libres = res.getInt("num_hab_libres");
            int ocupadas = res.getInt("num_hab_ocupadas");
            if (libres > 0){
                Integer rs = statement.executeUpdate("UPDATE hotel_fecha SET num_hab_libres = num_hab_libres-1, num_hab_ocupadas = num_hab_ocupadas + 1 WHERE id_hotel = "+ id_hotel +" and fecha = "+ fecha);
                //return Integer.parseInt(rs.getString("num_hab_libres"));
                //if(rs > 0) return rs;
                if(rs > 0) return (ocupadas + 1);
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
