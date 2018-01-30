/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model.RecursosDB;

import Model.Cliente;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Patricio
 */
public class RecursoDB_Clientes extends RecursoDB
{

    public RecursoDB_Clientes()
    {
        super("Clientes","SELECT * FROM cliente");
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
            Logger.getLogger(RecursoDB_Clientes.class.getName()).log(Level.SEVERE, null, ex);
        }

        try
        {
            ResultSet result = super.executeQuery();

            //recorrer result para crear objetos
            while (result != null && result.next())
            {
                int idAux = result.getInt("id");
                String nombreAux = result.getString("nombre");
                String apellidoAux = result.getString("apellido");
                int edadAux = result.getInt("edad");
                String sexoAux = result.getString("sexo");
                String descripcionAux = result.getString("descripcion");
                if(add(new Cliente(idAux+"", nombreAux, apellidoAux, edadAux, sexoAux, descripcionAux))==-1)
                {
                    return false;
                }
            }
            this.close();
            return true;
        }
        catch (SQLException ex)
        {
            Logger.getLogger(RecursoDB_Clientes.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }
}