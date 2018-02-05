/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model.RecursosDB;

import Model.LocalDB;
import Model.OficinaVentas;
import Model.ZonaVentas;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Patricio
 */
public class RecursoDB_OficinaVentas extends RecursoDB
{

    public RecursoDB_OficinaVentas(LocalDB db)
    {
        super("OficinaVentas","SELECT * FROM oficinaVentas",db);
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
            Logger.getLogger(RecursoDB_OficinaVentas.class.getName()).log(Level.SEVERE, null, ex);
        }

        try
        {
            ResultSet result = super.executeQuery();

            //recorrer result para crear objetos
            while (result != null && result.next())
            {
                String idAux = result.getString("id");
                String nombreAux = result.getString("nombre");
                String idZona = result.getString("zonaVentas_id");//id
                ZonaVentas zonaAux = (this.db.zonaVentas.containsKey(idZona)) ? 
                        this.db.zonaVentas.get(idZona)
                        : this.db.zonaVentas.put(idZona, new ZonaVentas(idZona));
                
                if(!this.db.oficinas.containsKey(idAux))
                {
                    OficinaVentas aux = new OficinaVentas(idAux, nombreAux, zonaAux);
                    this.add(aux);
                    this.db.oficinas.put(idAux, aux);
                    System.out.println("Oficina: "+idAux);
                }
                else
                {
                    this.add(this.db.oficinas.get(idAux));
                }
            }
            this.close();
            return true;
        }
        catch (SQLException ex)
        {
            Logger.getLogger(RecursoDB_OficinaVentas.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }
}