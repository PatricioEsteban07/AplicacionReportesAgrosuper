/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model.Filtros;

import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * @author Patricio
 */
public class Filtro_Cliente extends Filtro
{
    private ArrayList<String> clientes;
    public ArrayList<String> tituloClientes;

    public Filtro_Cliente(ArrayList<String> titles)
    {
        super("Cliente");
        this.clientes=new ArrayList<>();
        this.tituloClientes=titles;
    }
    
    public boolean addCliente(String value)
    {
        boolean flag=true;
        for (String cliente : this.clientes)
        {
            if(cliente.equals(value))
                flag=false;
        }
        if(flag)
        {
            this.clientes.add(value);
            return true;
        }
        return false;
    }
    
    public boolean setClientes(ArrayList<String> data)
    {
        this.clientes=data;
        return true;
    }

    @Override
    public String generarWhere(HashMap<String, String> data)
    {
        String query="";
        if(!this.clientes.isEmpty())
        {
            query=query+"(";
            for (int i = 0; i < this.clientes.size(); i++)
            {
                query=query+" "+data.get("Cliente")+" = "+this.clientes.get(i)+" ";
                if(i<this.clientes.size()-1)
                {
                    query=query+"OR";
                }
            }  
            query=query+")";  
        }
        return query;  
    }

    @Override
    public boolean vaciarFiltro()
    {
        this.clientes=new ArrayList<>();
        return true;
    }
    
}
