/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model.RecursosDB;

import Model.LocalDB;
import Model.Marca;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Patricio
 */
public class RecursoDB_Marcas extends RecursoDB
{

    public RecursoDB_Marcas(LocalDB db)
    {
        super("Marcas","SELECT * FROM marca",db);
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
            Logger.getLogger(RecursoDB_Marcas.class.getName()).log(Level.SEVERE, null, ex);
        }

        try
        {
            ResultSet result = super.executeQuery();

            //recorrer result para crear objetos
            while (result != null && result.next())
            {
                String idAux = result.getString("id");
                String nombreAux = result.getString("nombre");
                if(!this.db.marcas.containsKey(idAux))
                {
                    Marca aux = new Marca(idAux, nombreAux);
                    this.add(aux);
                    this.db.marcas.put(idAux, aux);
                    System.out.println("Marca: "+idAux);
                }
                else
                {
                    this.add(this.db.marcas.get(idAux));
                }
            }
            this.close();
            return true;
        }
        catch (SQLException ex)
        {
            Logger.getLogger(RecursoDB_Marcas.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }
}