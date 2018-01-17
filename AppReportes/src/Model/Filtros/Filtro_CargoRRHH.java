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
public class Filtro_CargoRRHH extends Filtro
{
    private ArrayList<String> tiposRRHH;
    public ArrayList<String> tituloTiposRRHH;

    public Filtro_CargoRRHH(ArrayList<String> titles)
    {
        super("CargoRRHH");
        this.tiposRRHH=new ArrayList<>();
        this.tituloTiposRRHH=titles;
    }
    
    public boolean addCargoRRHH(String value)
    {
        boolean flag=true;
        for (String rrhh : this.tiposRRHH)
        {
            if(rrhh.equals(value))
                flag=false;
        }
        if(flag)
        {
            this.tiposRRHH.add(value);
            return true;
        }
        return false;
    }
    
    public boolean setCargoRRHH(ArrayList<String> data)
    {
        this.tiposRRHH=data;
        return true;
    }

    @Override
    public String generarWhere(HashMap<String, String> data)
    {
        String query="";
        if(!this.tiposRRHH.isEmpty())
        {
            query=query+"(";
            for (int i = 0; i < this.tiposRRHH.size(); i++)
            {
                query=query+" "+data.get("CargoRRHH")+" = "+this.tiposRRHH.get(i)+" ";
                if(i<this.tiposRRHH.size()-1)
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
        this.tiposRRHH=new ArrayList<>();
        return true;
    }
    
}
