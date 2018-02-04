/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model.RecursosDB;

import Model.Centro;
import Model.LocalDB;
import Model.Marca;
import Model.Region;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Patricio
 */
public class RecursoDB_Centros extends RecursoDB
{

    public RecursoDB_Centros(LocalDB db)
    {
        super("Centros","SELECT * FROM centro",db);
    }

    @Override
    public boolean obtenerDatos(HashMap<String,RecursoDB> resources)
    {
        try
        {
            this.connect();
        }
        catch (SQLException | ClassNotFoundException ex)
        {
            Logger.getLogger(RecursoDB_Centros.class.getName()).log(Level.SEVERE, null, ex);
        }

        try
        {
            ResultSet result = super.executeQuery();

            //recorrer result para crear objetos
            while (result != null && result.next())
            {
                String idAux = result.getString("id");
                String nombreAux = result.getString("nombre");
                String idRegion = result.getString("region_id");//id
                Region regionAux = (this.db.regiones.containsKey(idRegion)) ? 
                        this.db.regiones.get(idRegion)
                        : this.db.regiones.put(idRegion, new Region(idRegion));
                
                if(!this.db.centros.containsKey(idAux))
                {
                    Centro aux = new Centro(idAux, nombreAux, regionAux);
                    this.add(aux);
                    this.db.centros.put(idAux, aux);
                }
                else
                {
                    this.add(this.db.centros.get(idAux));
                }
            }
            this.close();
            return true;
        }
        catch (SQLException ex)
        {
            Logger.getLogger(RecursoDB_Centros.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }
}