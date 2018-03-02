/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model.Filtros;

import java.util.ArrayList;
import java.util.Calendar;
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
    private int semanaInicio;
    private int semanaFin;
    private ArrayList<Integer> semanas;
    private ArrayList<Integer> meses;
    private ArrayList<Integer> anios;

    public Filtro_Fecha()
    {
        super("Filtro_Fecha");
        this.semanas=new ArrayList<>();
        this.meses=new ArrayList<>();
        this.anios=new ArrayList<>();
        this.fechaInicio=new Date(2015, 0, 1);
        this.fechaFin=new Date(2015, 0, 1);
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
    public boolean vaciarFiltro()
    {
        this.semanas=new ArrayList<>();
        this.meses=new ArrayList<>();
        this.anios=new ArrayList<>();
        this.fechaInicio=new Date(2015,0,1);
        this.fechaFin=new Date(2015,0,1);
        this.semanaInicio=0;
        this.semanaFin=0;
        this.opcion=0;
        return true;
    }

    @Override
    public boolean setOpcion(int value)
    {
        /*
            Fecha año: 
                1(1 año)-> 1 año
                2 (rango año) -> muchos años
            Fecha mes: 
                3(1 año, 1 mes), 
                4(rango año, rango meses)
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
    
    public static Calendar toCalendar(Date date){ 
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal;
      }
    
    public static Date toDate(Calendar cal){ 
        return cal.getTime();
      }
    
    public boolean prepararFiltro()
    {
        switch(this.opcion)
        {
            case 1://1 año
                System.out.println("paso");
                this.fechaInicio=new Date(this.fechaInicio.getYear(),0,1);
                this.fechaFin=new Date(this.fechaInicio.getYear(),11,31);
                break;
            case 2://rango año
                this.fechaInicio=new Date(this.fechaInicio.getYear(),0,1);
                this.fechaFin=new Date(this.fechaFin.getYear(),11,31);
                break;
            case 3://un mes
                this.fechaInicio=new Date(this.fechaInicio.getYear(),this.fechaInicio.getMonth(),1);
                Calendar aux=toCalendar(new Date(this.fechaInicio.getYear(),this.fechaInicio.getMonth(),1));
                aux.add(Calendar.MONTH, 1);
                aux.add(Calendar.DAY_OF_MONTH, -1);
                this.fechaFin = toDate(aux);
                break;
            case 4://rango mes
                this.fechaInicio=new Date(this.fechaInicio.getYear(),this.fechaInicio.getMonth(),1);
                aux=toCalendar(new Date(this.fechaFin.getYear(),this.fechaFin.getMonth(),1));
                aux.add(Calendar.MONTH, 1);
                aux.add(Calendar.DAY_OF_MONTH, -1);
                this.fechaFin = toDate(aux);
                break;
            case 5://1 semana
                System.out.println("implementar");
                break;
            case 6://rango semana
                System.out.println("implementar");
                break;
            case 7://fecha
                this.fechaFin=new Date(this.fechaInicio.getYear(),this.fechaInicio.getMonth(),this.fechaInicio.getDate());
                break;
            case 8://rango fecha
                break;
        }
        return true;
    }

    @Override
    public String generarEtiquetaInfo()
    {
        switch(this.getOpcion())
        {
            case 1://1 año
                return "Reporte para el período "+this.fechaInicio.getYear();
            case 2://rango años
                return "Reporte para el período "+this.fechaInicio.getYear()+"-"
                        +this.fechaFin.getYear();
            case 3://1 mes
                return "Reporte para el período "+this.fechaInicio.getYear()+"-"+(this.fechaInicio.getMonth()+1);
            case 4://rango mes
                return "Reporte para el período "+this.fechaInicio.getYear()+"-"+(this.fechaInicio.getMonth()+1)
                        +" / "+this.fechaFin.getYear()+"-"+(this.fechaFin.getMonth()+1);
            case 5://1 semana
                return "Reporte para el período "+this.fechaInicio.getYear()+"-"+this.semanaInicio;
            case 6://rango semanas
                return "Reporte para el período "+this.fechaInicio.getYear()+"-"+this.semanaInicio
                        +" / "+this.fechaFin.getYear()+"-"+this.semanaFin;
            case 7://fecha
                return "Reporte para el dia "+this.fechaInicio.getDate()+"/"+(this.fechaInicio.getMonth()+1)+"/"
                        +this.fechaInicio.getYear();
            case 8://rango fecha
                return "Reporte para el período "+this.fechaInicio.getDate()+"/"+(this.fechaInicio.getMonth()+1)+"/"
                        +this.fechaInicio.getYear()+" - "+this.fechaFin.getDate()+"/"+(this.fechaFin.getMonth()+1)+"/"
                        +this.fechaFin.getYear();
        }
        return null;
    }

    //validar que sean correctas
    public void setSemanas(int semanaInicio, int semanaFinal)
    {
        this.semanaInicio=semanaInicio;
        this.semanaFin=semanaFinal;
    }
    
}
