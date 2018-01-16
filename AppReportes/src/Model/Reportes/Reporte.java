/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model.Reportes;

import Model.Filtros.FiltroAbstract;
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
    public FiltroAbstract filtro;

    public Reporte()
    {
        this.generadorExcel = new HashMap<>();
        this.recursos = new HashMap<>();
    }
    
    public abstract boolean generarRecursos();
    public abstract boolean generarExcel();

}
