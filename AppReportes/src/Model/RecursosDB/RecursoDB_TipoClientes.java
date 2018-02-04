/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model.RecursosDB;

import Model.LocalDB;
import Model.TipoCliente;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Patricio
 */
public class RecursoDB_TipoClientes extends RecursoDB
{

    public RecursoDB_TipoClientes(LocalDB db)
    {
        super("TipoClientes","SELECT * FROM tipoCliente",db);
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
            Logger.getLogger(RecursoDB_TipoClientes.class.getName()).log(Level.SEVERE, null, ex);
        }

        try
        {
            ResultSet result = super.executeQuery();

            //recorrer result para crear objetos
            while (result != null && result.next())
            {
                String idAux = result.getString("id");
                String nombreAux = result.getString("nombre");
                
                if(!this.db.tiposClientes.containsKey(idAux))
                {
                    TipoCliente aux = new TipoCliente(idAux, nombreAux);
                    this.add(aux);
                    this.db.tiposClientes.put(idAux, aux);
                }
                else
                {
                    this.add(this.db.tiposClientes.get(idAux));
                }
            }
            this.close();
            return true;
        }
        catch (SQLException ex)
        {
            Logger.getLogger(RecursoDB_TipoClientes.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }
}