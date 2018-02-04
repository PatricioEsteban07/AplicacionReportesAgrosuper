/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model.RecursosDB;

import Model.Cliente;
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
public class RecursoDB_ClienteEmpresas extends RecursoDB
{

    public RecursoDB_ClienteEmpresas(LocalDB db)
    {
        super("Cliente-Empresas","SELECT * FROM cliente_empresa",db);
    }

    @Override
    public boolean obtenerDatos(HashMap<String,RecursoDB> resources)
    {
        System.out.println("obtenerDatos de clienteEmpresas...");
        try
        {
            this.connect();
        }
        catch (SQLException | ClassNotFoundException ex)
        {
            Logger.getLogger(RecursoDB_ClienteEmpresas.class.getName()).log(Level.SEVERE, null, ex);
        }
        try
        {
            ResultSet result = super.executeQuery();
            RecursoDB clientes=resources.get("Clientes");
            RecursoDB empresas=resources.get("Empresas");

            //recorrer result para crear objetos
            while (result != null && result.next())
            {
                int clienteId = result.getInt("cliente_id");
                int empresaId = result.getInt("empresa_id");
                //asignacion empresa-cliente
                
                Empresa empresaAux=(Empresa) empresas.getById(empresaId+"");
                Cliente clienteAux=(Cliente) clientes.getById(clienteId+"");
                if(empresaAux!=null && clienteAux!=null)
                {
                    empresaAux.clientes.add(clienteAux);
                    this.datos.add(empresaAux);
                }
                else
                {
                    return false;
                }
            }
            this.close();
            return true;
        }
        catch (SQLException ex)
        {
            Logger.getLogger(RecursoDB_ClienteEmpresas.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }
}
