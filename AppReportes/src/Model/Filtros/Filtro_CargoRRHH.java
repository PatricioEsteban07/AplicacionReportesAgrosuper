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
    private ArrayList<String> tiposRRHHSeleccionados;
    public ArrayList<String> tiposRRHH;

    public Filtro_CargoRRHH()
    {
        super("Filtro_CargoRRHH");
        this.tiposRRHHSeleccionados=new ArrayList<>();
        this.tiposRRHH=new ArrayList<>();
    }

    public Filtro_CargoRRHH(ArrayList<String> titles)
    {
        super("Filtro_CargoRRHH");
        this.tiposRRHHSeleccionados=new ArrayList<>();
        this.tiposRRHH=titles;
    }
    
    public boolean addCargoRRHH(String value)
    {
        boolean flag=true;
        for (String rrhh : this.tiposRRHHSeleccionados)
        {
            if(rrhh.equals(value))
                flag=false;
        }
        if(flag)
        {
            this.tiposRRHHSeleccionados.add(value);
            return true;
        }
        return false;
    }
    
    public boolean setCargoRRHHSeleccionados(ArrayList<String> data)
    {
        this.tiposRRHHSeleccionados=data;
        return true;
    }

    @Override
    public String generarWhere(HashMap<String, String> data)
    {
        String query="";
        if(!this.tiposRRHHSeleccionados.isEmpty())
        {
            query=query+"(";
            for (int i = 0; i < this.tiposRRHHSeleccionados.size(); i++)
            {
                query=query+" "+data.get("CargoRRHH")+" = "+this.tiposRRHHSeleccionados.get(i)+" ";
                if(i<this.tiposRRHHSeleccionados.size()-1)
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
        this.tiposRRHHSeleccionados=new ArrayList<>();
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
