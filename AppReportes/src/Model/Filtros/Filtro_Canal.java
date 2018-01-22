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
    private ArrayList<String> canalesSeleccionados;
    public ArrayList<String> canales;

    public Filtro_Canal()
    {
        super("Filtro_Canal");
        this.canalesSeleccionados=new ArrayList<>();
        this.canales=new ArrayList<>();
    }
    
    public Filtro_Canal(ArrayList<String> titles)
    {
        super("Filtro_Canal");
        this.canalesSeleccionados=new ArrayList<>();
        this.canales=titles;
    }
    
    public boolean addCanal(String value)
    {
        boolean flag=true;
        for (String canal : this.canalesSeleccionados)
        {
            if(canal.equals(value))
                flag=false;
        }
        if(flag)
        {
            this.canalesSeleccionados.add(value);
            return true;
        }
        return false;
    }
    
    public boolean setCanalesSeleccionados(ArrayList<String> canalesSeleccionados)
    {
        this.canalesSeleccionados=canalesSeleccionados;
        return true;
    }

    @Override
    public String generarWhere(HashMap<String, String> data)
    {
        String query="";
        if(!this.canalesSeleccionados.isEmpty())
        {
            query=query+"(";
            for (int i = 0; i < this.canalesSeleccionados.size(); i++)
            {
                query=query+" "+data.get("Canal")+" = "+this.canalesSeleccionados.get(i)+" ";
                if(i<this.canalesSeleccionados.size()-1)
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
        this.canalesSeleccionados=new ArrayList<>();
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
        if(this.canalesSeleccionados.isEmpty())
        {
            return null;
        }
        String aux = "Reporte filtrado para canales: ";
        for (int i = 0; i < this.canalesSeleccionados.size(); i++)
        {
            aux=aux+this.canalesSeleccionados.get(i);
            if(i<this.canalesSeleccionados.size()-1)
            {
                aux=aux+", ";
            }
        }
        return aux;
    }
    
}
