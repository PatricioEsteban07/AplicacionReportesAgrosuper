/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model.RecursosDB;

import Model.LocalDB;
import Model.TipoEnvasado;
import Model.ZonaVentas;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Patricio
 */
public class RecursoDB_ZonaVentas extends RecursoDB
{

    public RecursoDB_ZonaVentas(LocalDB db)
    {
        super("Zona Ventas","SELECT * FROM zonaVentas",db);
    }

    @Override
    public boolean obtenerDatos()
    {
        try
        {
            this.connect();
        }
        catch (SQLException | ClassNotFoundException ex)
        {
            Logger.getLogger(RecursoDB_ZonaVentas.class.getName()).log(Level.SEVERE, null, ex);
        }

        try
        {
            ResultSet result = super.executeQuery();

            //recorrer result para crear objetos
            while (result != null && result.next())
            {
                String idAux = result.getString("id");
                String nombreAux = result.getString("nombre");
                if(!this.db.zonaVentas.containsKey(idAux))
                {
                    ZonaVentas aux = new ZonaVentas(idAux, nombreAux);
                    this.add(aux);
                    this.db.zonaVentas.put(idAux, aux);
                    System.out.println("ZonaVenta: "+idAux);
                }
                else
                {
                    this.add(this.db.zonaVentas.get(idAux));
                }
            }
            this.close();
            return true;
        }
        catch (SQLException ex)
        {
            Logger.getLogger(RecursoDB_ZonaVentas.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }
}