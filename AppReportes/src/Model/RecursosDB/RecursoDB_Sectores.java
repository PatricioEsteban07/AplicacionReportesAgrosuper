/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model.RecursosDB;

import Model.LocalDB;
import Model.Sector;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Patricio
 */
public class RecursoDB_Sectores extends RecursoDB
{

    public RecursoDB_Sectores(LocalDB db)
    {
        super("Sectores","SELECT * FROM sector",db);
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
            Logger.getLogger(RecursoDB_Sectores.class.getName()).log(Level.SEVERE, null, ex);
        }

        try
        {
            ResultSet result = super.executeQuery();

            //recorrer result para crear objetos
            while (result != null && result.next())
            {
                String idAux = result.getString("id");
                String nombreAux = result.getString("nombre");
                if(!this.db.sectores.containsKey(idAux))
                {
                    Sector aux = new Sector(idAux, nombreAux);
                    this.add(aux);
                    this.db.sectores.put(idAux, aux);
                }
                else
                {
                    this.add(this.db.sectores.get(idAux));
                }
            }
            this.close();
            return true;
        }
        catch (SQLException ex)
        {
            Logger.getLogger(RecursoDB_Sectores.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }
}