/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model.RecursosDB;

import Model.LocalDB;
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
public class RecursoDB_Regiones extends RecursoDB
{

    public RecursoDB_Regiones(LocalDB db)
    {
        super("Regiones","SELECT * FROM region",db);
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
            Logger.getLogger(RecursoDB_Empresas.class.getName()).log(Level.SEVERE, null, ex);
        }
        try
        {
            ResultSet result = super.executeQuery();

            //recorrer result para crear objetos
            while (result != null && result.next())
            {
                String idAux = result.getString("id");
                String nombreAux = result.getString("nombre");
                String paisAux = result.getString("pais");
                
                if(!this.db.regiones.containsKey(idAux))
                {
                    Region aux = new Region(idAux,nombreAux,paisAux);
                    this.add(aux);
                    this.db.regiones.put(idAux, aux);
                }
                else
                {
                    this.add(this.db.regiones.get(idAux));
                }
            }
            this.close();
            return true;
        }
        catch (SQLException ex)
        {
            Logger.getLogger(RecursoDB_Empresas.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }
    
}
