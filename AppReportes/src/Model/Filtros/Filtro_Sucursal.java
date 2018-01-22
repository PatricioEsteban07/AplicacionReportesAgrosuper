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
    private ArrayList<String> sucursalesSeleccionadas;
    public ArrayList<String> sucursales;

    public Filtro_Sucursal()
    {
        super("Filtro_Sucursal");
        this.sucursalesSeleccionadas=new ArrayList<>();
        this.sucursales=new ArrayList<>();
    }

    public Filtro_Sucursal(ArrayList<String> titles)
    {
        super("Filtro_Sucursal");
        this.sucursalesSeleccionadas=new ArrayList<>();
        this.sucursales=titles;
    }
    
    public boolean addSucursal(String value)
    {
        boolean flag=true;
        for (String sucursal : this.sucursalesSeleccionadas)
        {
            if(sucursal.equals(value))
                flag=false;
        }
        if(flag)
        {
            this.sucursalesSeleccionadas.add(value);
            return true;
        }
        return false;
    }
    
    public boolean setSucursalesSeleccionadas(ArrayList<String> data)
    {
        this.sucursalesSeleccionadas=data;
        return true;
    }

    @Override
    public String generarWhere(HashMap<String, String> data)
    {
        String query="";
        if(!this.sucursalesSeleccionadas.isEmpty())
        {
            query=query+"(";
            for (int i = 0; i < this.sucursalesSeleccionadas.size(); i++)
            {
                query=query+" "+data.get("Sucursal")+" = "+this.sucursalesSeleccionadas.get(i)+" ";
                if(i<this.sucursalesSeleccionadas.size()-1)
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
        this.sucursalesSeleccionadas=new ArrayList<>();
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
        if(this.sucursalesSeleccionadas.isEmpty())
        {
            return null;
        }
        String aux = "Reporte filtrado para clientes: ";
        for (int i = 0; i < this.sucursalesSeleccionadas.size(); i++)
        {
            aux=aux+this.sucursalesSeleccionadas.get(i);
            if(i<this.sucursalesSeleccionadas.size()-1)
            {
                aux=aux+", ";
            }
        }
        return aux;
    }
    
}
