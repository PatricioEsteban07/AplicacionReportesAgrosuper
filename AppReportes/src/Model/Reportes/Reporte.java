/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model.Reportes;

import Model.Filtros.Filtro;
import Model.Filtros.Filtro_Canal;
import Model.Filtros.Filtro_CargoRRHH;
import Model.Filtros.Filtro_Cliente;
import Model.Filtros.Filtro_Fecha;
import Model.Filtros.Filtro_Sucursal;
import Model.Filtros.Filtro_Zona;
import Model.GeneradoresExcel.GeneradorExcel;
import Model.RecursosDB.RecursoDB;

import java.util.HashMap;

/**
 *
 * @author Patricio
 */
public abstract class Reporte
{
    public String nombre;
    public HashMap<String,GeneradorExcel> generadorExcel;
    public HashMap<String,RecursoDB> recursos;
    public HashMap<String,Filtro> filtros;

    public Reporte()
    {
        this.generadorExcel = new HashMap<>();
        this.recursos = new HashMap<>();
        this.filtros = new HashMap<>();
    }
    
    public boolean generarFiltrosBase()
    {
        //crear todos los filtros vacios
        this.filtros.put("Filtro_Fecha",new Filtro_Fecha());
        this.filtros.put("Filtro_Canal",new Filtro_Canal());
        this.filtros.put("Filtro_Zona",new Filtro_Zona());
        this.filtros.put("Filtro_Sucursal",new Filtro_Sucursal());
        this.filtros.put("Filtro_Cliente",new Filtro_Cliente());
        this.filtros.put("Filtro_CargoRRHH",new Filtro_CargoRRHH());
        return false;
    }
    
    public abstract boolean generarRecursos();
    public abstract boolean generarExcel();
    public abstract boolean generarFiltrosBaseCustom();

}
