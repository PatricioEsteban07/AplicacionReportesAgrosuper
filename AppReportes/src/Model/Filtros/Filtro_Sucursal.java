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
public class Filtro_Sucursal extends Filtro
{
    private ArrayList<String> sucursales;
    public ArrayList<String> tituloSucursales;

    public Filtro_Sucursal(ArrayList<String> titles)
    {
        super("Sucursal");
        this.sucursales=new ArrayList<>();
        this.tituloSucursales=titles;
    }
    
    public boolean addSucursal(String value)
    {
        boolean flag=true;
        for (String sucursal : this.sucursales)
        {
            if(sucursal.equals(value))
                flag=false;
        }
        if(flag)
        {
            this.sucursales.add(value);
            return true;
        }
        return false;
    }
    
    public boolean setSucursales(ArrayList<String> data)
    {
        this.sucursales=data;
        return true;
    }

    @Override
    public String generarWhere(HashMap<String, String> data)
    {
        String query="";
        if(!this.sucursales.isEmpty())
        {
            query=query+"(";
            for (int i = 0; i < this.sucursales.size(); i++)
            {
                query=query+" "+data.get("Sucursal")+" = "+this.sucursales.get(i)+" ";
                if(i<this.sucursales.size()-1)
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
        this.sucursales=new ArrayList<>();
        return true;
    }
    
}
