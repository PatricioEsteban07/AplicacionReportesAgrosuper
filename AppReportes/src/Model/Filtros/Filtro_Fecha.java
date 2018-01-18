/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model.Filtros;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

/**
 *
 * @author Patricio
 */
public class Filtro_Fecha extends Filtro
{
    private Date fechaInicio;
    private Date fechaFin;
    private ArrayList<Integer> semanas;
    private ArrayList<Integer> meses;
    private ArrayList<Integer> anios;

    public Filtro_Fecha()
    {
        super("Filtro_Fecha");
        this.semanas=new ArrayList<>();
        this.meses=new ArrayList<>();
        this.anios=new ArrayList<>();
        this.fechaInicio=new Date(2000, 1, 1);
        this.fechaFin=new Date(2000, 1, 1);
    }

    public void setFechaInicio(Date fechaInicio)
    {
        this.fechaInicio = fechaInicio;
    }

    public void setFechaFin(Date fechaFin)
    {
        this.fechaFin = fechaFin;
    }

    public boolean setSemanas(ArrayList<Integer> data)
    {
        this.semanas=data;
        return true;
    }
    
    public boolean setMeses(ArrayList<Integer> data)
    {
        this.meses=data;
        return true;
    }
    
    public boolean setAnios(ArrayList<Integer> data)
    {
        this.anios=data;
        return true;
    }
    
    public boolean adSemana(int value)
    {
        boolean flag=true;
        for (Integer semana : this.semanas)
        {
            if(semana==value)
                flag=false;
        }
        if(flag)
        {
            this.semanas.add(value);
            return true;
        }
        return false;
    }

    public boolean addMes(int value)
    {
        boolean flag=true;
        for (Integer mes : this.meses)
        {
            if(mes==value)
                flag=false;
        }
        if(flag)
        {
            this.meses.add(value);
            return true;
        }
        return false;
    }

    public boolean addAnio(int value)
    {
        boolean flag=true;
        for (Integer anio : this.anios)
        {
            if(anio==value)
                flag=false;
        }
        if(flag)
        {
            this.anios.add(value);
            return true;
        }
        return false;
    }

    public Date getFechaInicio()
    {
        return fechaInicio;
    }

    public Date getFechaFin()
    {
        return fechaFin;
    }

    public ArrayList<Integer> getSemanas()
    {
        return semanas;
    }

    public ArrayList<Integer> getMeses()
    {
        return meses;
    }

    public ArrayList<Integer> getAnios()
    {
        return anios;
    }
    
    public boolean setRangoFecha(Date fechaInicio, Date fechaFin)
    {
        //validar fecha
        if(!fechaInicio.before(fechaFin))
        {
            return false;
        }
        this.fechaInicio=fechaInicio;
        this.fechaFin=fechaFin;
        return true;
    }
    
    @Override
    public String generarWhere(HashMap<String,String> data)
    {
        boolean contents[]=new boolean[5];
        contents[0]= !this.semanas.isEmpty();
        contents[1]= !this.meses.isEmpty();
        contents[2]= !this.anios.isEmpty();
        contents[3]= !this.fechaInicio.equals(new Date(2000,1,1));
        contents[4]= !this.fechaFin.equals(new Date(2000,1,1));
        int countElements=countValues(contents);
        if(countElements>0)
        {
            String query="";
            int countAux=0;
            if(contents[0])//semanas
            {
                countAux++;
                query=query+"(";
                for (int i = 0; i < this.semanas.size(); i++)
                {
                    query=query+" "+data.get("Semana")+" = "+this.semanas.get(i)+" ";
                    if(i<this.semanas.size()-1)
                    {
                        query=query+"OR";
                    }
                }
                query=query+")";
                if(countAux<countElements)
                {
                    query=query+" AND ";
                }
            }
            if(contents[1])//meses
            {
                countAux++;
                query=query+"(";
                for (int i = 0; i < this.meses.size(); i++)
                {
                    query=query+" "+data.get("Mes")+" = "+this.meses.get(i)+" ";
                    if(i<this.meses.size()-1)
                    {
                        query=query+"OR";
                    }
                }
                query=query+")";
                if(countAux<countElements)
                {
                    query=query+" AND ";
                }
            }
            if(contents[2])//años
            {
                countAux++;
                query=query+"(";
                for (int i = 0; i < this.anios.size(); i++)
                {
                    query=query+" "+data.get("Año")+" = "+this.anios.get(i)+" ";
                    if(i<this.anios.size()-1)
                    {
                        query=query+"OR";
                    }
                }
                query=query+")";
                if(countAux<countElements)
                {
                    query=query+" AND ";
                }
            }
            if(contents[3] && contents[4])//rangoFechas
            {
                query=query+"( "+data.get("Fecha")+" BETWEEN '"+this.fechaInicio.getYear()+"-"+this.fechaInicio.getMonth()+"-"
                        +this.fechaInicio.getDate()+"' AND '"+this.fechaFin.getYear()+"-"+this.fechaFin.getMonth()+"-"
                        +this.fechaFin.getDate()+"' )";
            }
            else if(contents[3])//fechaInicio
            {
                countAux++;
                query=query+"( "+data.get("Dia")+" = "+this.fechaInicio.getDate()+" AND "+data.get("Mes")+" = "+this.fechaInicio.getMonth()
                        +" AND "+data.get("Año")+" = "+this.fechaInicio.getYear()+" )";
            }
            return query;
        }
        return "";
    }
    
    public int countValues(boolean values[])
    {
        int ct=0;
        for (int i = 0; i < values.length; i++)
        {
            if(values[i])
                ct++;
        }
        return ct;
    }  

    @Override
    public boolean vaciarFiltro()
    {
        this.semanas=new ArrayList<>();
        this.meses=new ArrayList<>();
        this.anios=new ArrayList<>();
        this.fechaInicio=new Date(2000,1,1);
        this.fechaFin=new Date(2000,1,1);
        return true;
    }

    @Override
    public boolean setOpcion(int value)
    {
        /*
            Fecha año: 
                1(1 año)
                2 (rango año)
            Fecha mes: 
                3(1 año, 1 mes), 
                4(1 año, rango meses)
            Fecha semana:
                5(1 año, 1 semana)
                6(1 año, range semanas)
            Fecha dia:
                7(fecha concreta)
                8(rango fecha)
        */
        if(value>0 && value<9)
        {
            this.opcion=value;
            return true;
        }
        return false;
    }

    @Override
    public String generarEtiquetaInfo()
    {
        switch(this.getOpcion())
        {
            case 1://1 año
                return "Reporte para el año "+this.anios.get(0);
            case 2://rango años
                return "Reporte para el período "+this.anios.get(0)+"-"
                        +this.anios.get(this.anios.size()-1);
            case 3:
                
            case 4:
                
            case 5:
                
            case 6:
                
            case 7:
                
            case 8:
                
        }
        return null;
    }
    
}
