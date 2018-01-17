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
public class Filtro_Zona extends Filtro
{
    private ArrayList<String> zonas;
    public ArrayList<String> tituloZonas;

    public Filtro_Zona(ArrayList<String> titles)
    {
        super("Zona");
        this.zonas=new ArrayList<>();
        this.tituloZonas=titles;
    }
    
    public boolean addZona(String value)
    {
        boolean flag=true;
        for (String zona : this.zonas)
        {
            if(zona.equals(value))
                flag=false;
        }
        if(flag)
        {
            this.zonas.add(value);
            return true;
        }
        return false;
    }
    
    public boolean setZonas(ArrayList<String> data)
    {
        this.zonas=data;
        return true;
    }

    @Override
    public String generarWhere(HashMap<String, String> data)
    {
        String query="";
        if(!this.zonas.isEmpty())
        {
            query=query+"(";
            for (int i = 0; i < this.zonas.size(); i++)
            {
                query=query+" "+data.get("Zona")+" = "+this.zonas.get(i)+" ";
                if(i<this.zonas.size()-1)
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
        this.zonas=new ArrayList<>();
        return true;
    }
    
}
