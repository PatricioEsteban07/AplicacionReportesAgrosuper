/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model.RecursosDB;

import Model.EstadoRefrigerado;
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
public class RecursoDB_EstadoRefrigerados extends RecursoDB
{

    public RecursoDB_EstadoRefrigerados(LocalDB db)
    {
        super("EstadoRefrigerado","SELECT * FROM estadoRefrigerado",db);
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
            Logger.getLogger(RecursoDB_EstadoRefrigerados.class.getName()).log(Level.SEVERE, null, ex);
        }

        try
        {
            ResultSet result = super.executeQuery();

            //recorrer result para crear objetos
            while (result != null && result.next())
            {
                String idAux = result.getString("id");
                String nombreAux = result.getString("nombre");
                if(!this.db.estadosRefrigerados.containsKey(idAux))
                {
                    EstadoRefrigerado aux = new EstadoRefrigerado(idAux, nombreAux);
                    this.add(aux);
                    this.db.estadosRefrigerados.put(idAux, aux);
                }
                else
                {
                    this.add(this.db.estadosRefrigerados.get(idAux));
                }
            }
            this.close();
            return true;
        }
        catch (SQLException ex)
        {
            Logger.getLogger(RecursoDB_EstadoRefrigerados.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }
}