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
    private ArrayList<String> clientesSeleccionados;
    public ArrayList<String> clientes;

    public Filtro_Cliente()
    {
        super("Filtro_Cliente");
        this.clientesSeleccionados=new ArrayList<>();
        this.clientes=new ArrayList<>();
    }

    public Filtro_Cliente(ArrayList<String> titles)
    {
        super("Filtro_Cliente");
        this.clientesSeleccionados=new ArrayList<>();
        this.clientes=titles;
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
            this.clientesSeleccionados.add(value);
            return true;
        }
        return false;
    }
    
    public boolean setClientesSeleccionados(ArrayList<String> data)
    {
        this.clientesSeleccionados=data;
        return true;
    }

    @Override
    public String generarWhere(HashMap<String, String> data)
    {
        String query="";
        if(!this.clientesSeleccionados.isEmpty())
        {
            query=query+"(";
            for (int i = 0; i < this.clientesSeleccionados.size(); i++)
            {
                query=query+" "+data.get("Cliente")+" = "+this.clientesSeleccionados.get(i)+" ";
                if(i<this.clientesSeleccionados.size()-1)
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
        this.clientesSeleccionados=new ArrayList<>();
        return true;
    }

    @Override
    public boolean setOpcion(int value)
    {
        return false;
    }

    @Override
    public String generarEtiquetaInfo()
    {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
