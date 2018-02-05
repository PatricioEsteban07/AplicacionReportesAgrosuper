/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model.RecursosDB;

import Model.Agrupado;
import Model.LocalDB;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Patricio
 */
public class RecursoDB_Agrupados extends RecursoDB
{
    
    public RecursoDB_Agrupados(LocalDB db)
    {
        super("Agrupados","SELECT * FROM agrupado",db);
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
            Logger.getLogger(RecursoDB_Agrupados.class.getName()).log(Level.SEVERE, null, ex);
        }

        try
        {
            ResultSet result = super.executeQuery();

            //recorrer result para crear objetos
            while (result != null && result.next())
            {
                String idAux = result.getString("id");
                String nombreAux = result.getString("nombre");
                if(!this.db.agrupados.containsKey(idAux))
                {
                    Agrupado aux = new Agrupado(idAux, nombreAux);
                    this.add(aux);
                    this.db.agrupados.put(idAux, aux);
                    System.out.println("Agrupado: "+idAux);
                }
                else
                {
                    this.add(this.db.agrupados.get(idAux));
                }
            }
            this.close();
            return true;
        }
        catch (SQLException ex)
        {
            Logger.getLogger(RecursoDB_Agrupados.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }
}