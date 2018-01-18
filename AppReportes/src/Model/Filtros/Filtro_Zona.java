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
    private ArrayList<String> zonasSeleccionadas;
    public ArrayList<String> zonas;

    public Filtro_Zona()
    {
        super("Filtro_Zona");
        this.zonasSeleccionadas=new ArrayList<>();
        this.zonas=new ArrayList<>();
    }

    public Filtro_Zona(ArrayList<String> titles)
    {
        super("Filtro_Zona");
        this.zonasSeleccionadas=new ArrayList<>();
        this.zonas=titles;
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
            this.zonasSeleccionadas.add(value);
            return true;
        }
        return false;
    }
    
    public boolean setZonasSeleccionadas(ArrayList<String> data)
    {
        this.zonasSeleccionadas=data;
        return true;
    }

    @Override
    public String generarWhere(HashMap<String, String> data)
    {
        String query="";
        if(!this.zonasSeleccionadas.isEmpty())
        {
            query=query+"(";
            for (int i = 0; i < this.zonasSeleccionadas.size(); i++)
            {
                query=query+" "+data.get("Zona")+" = "+this.zonasSeleccionadas.get(i)+" ";
                if(i<this.zonasSeleccionadas.size()-1)
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
        this.zonasSeleccionadas=new ArrayList<>();
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
