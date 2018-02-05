/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model.RecursosDB;

import Model.LocalDB;
import Model.TipoEnvasado;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Patricio
 */
public class RecursoDB_TipoEnvasados extends RecursoDB
{

    public RecursoDB_TipoEnvasados(LocalDB db)
    {
        super("TipoEnvasados","SELECT * FROM tipoEnvasado",db);
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
            Logger.getLogger(RecursoDB_TipoEnvasados.class.getName()).log(Level.SEVERE, null, ex);
        }

        try
        {
            ResultSet result = super.executeQuery();

            //recorrer result para crear objetos
            while (result != null && result.next())
            {
                String idAux = result.getString("id");
                String nombreAux = result.getString("nombre");
                if(!this.db.tiposEnvasados.containsKey(idAux))
                {
                    TipoEnvasado aux = new TipoEnvasado(idAux, nombreAux);
                    this.add(aux);
                    this.db.tiposEnvasados.put(idAux, aux);
                    System.out.println("Envasado: "+idAux);
                }
                else
                {
                    this.add(this.db.tiposEnvasados.get(idAux));
                }
            }
            this.close();
            return true;
        }
        catch (SQLException ex)
        {
            Logger.getLogger(RecursoDB_TipoEnvasados.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }
}