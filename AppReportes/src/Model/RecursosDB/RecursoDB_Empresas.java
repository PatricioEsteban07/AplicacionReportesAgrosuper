/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model.RecursosDB;

import Model.Empresa;
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
public class RecursoDB_Empresas extends RecursoDB
{

    public RecursoDB_Empresas(LocalDB db)
    {
        super("Empresas","SELECT * FROM empresa",db);
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
            Logger.getLogger(RecursoDB_Empresas.class.getName()).log(Level.SEVERE, null, ex);
        }
        try
        {
            ResultSet result = super.executeQuery();

            //recorrer result para crear objetos
            while (result != null && result.next())
            {
                int idAux = result.getInt("id");
                String nombreAux = result.getString("nombre");
                String direccionAux = result.getString("direccion");
                String descripcionAux = result.getString("descripcion");
                if(add(new Empresa(idAux+"", nombreAux, direccionAux, descripcionAux))==-1)
                {
                    return false;
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
