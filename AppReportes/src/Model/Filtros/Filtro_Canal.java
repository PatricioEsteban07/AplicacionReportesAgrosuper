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
public class Filtro_Canal extends Filtro
{
    private ArrayList<String> canales;
    private ArrayList<String> tituloCanales;

    public Filtro_Canal(ArrayList<String> titles)
    {
        super("Canal");
        this.canales=new ArrayList<>();
        this.tituloCanales=titles;
    }
    
    public boolean addCanal(String value)
    {
        boolean flag=true;
        for (String canal : this.canales)
        {
            if(canal.equals(value))
                flag=false;
        }
        if(flag)
        {
            this.canales.add(value);
            return true;
        }
        return false;
    }
    
    public boolean setCanales(ArrayList<String> canales)
    {
        this.canales=canales;
        return true;
    }

    @Override
    public String generarWhere(HashMap<String, String> data)
    {
        String query="";
        if(!this.canales.isEmpty())
        {
            query=query+"(";
            for (int i = 0; i < this.canales.size(); i++)
            {
                query=query+" "+data.get("Canal")+" = "+this.canales.get(i)+" ";
                if(i<this.canales.size()-1)
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
        this.canales=new ArrayList<>();
        return true;
    }
    
}
