/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model.Reportes;

import Model.Filtros.Filtro;
import Model.Filtros.Filtro_Fecha;
import Model.GeneradoresExcel.GeneradorExcel;
import Model.LocalDB;
import Model.Recurso;
import Model.RecursosDB.RecursoDB;
import java.util.ArrayList;

import java.util.HashMap;
import javafx.scene.control.TableView;
import javafx.scene.layout.AnchorPane;

/**
 *
 * @author Patricio
 */
public abstract class Reporte
{
    public LocalDB db;
    public String nombre;
    public HashMap<String, GeneradorExcel> generadorExcel;
    public HashMap<String, RecursoDB> recursos;
    public HashMap<String, Filtro> filtros;
    public ArrayList<String> columnasExcel;

    public Reporte(String nombre, LocalDB db)
    {
        this.db = db;
        this.nombre = nombre;
        this.generadorExcel = new HashMap<>();
        this.recursos = new HashMap<>();
        this.filtros = new HashMap<>();
        this.columnasExcel = new ArrayList<>();
        this.columnasExcel = this.completarColumnasTabla();
    }

    public boolean generarFiltrosBase()
    {
        //crear todos los filtros vacios
        this.filtros = new HashMap<>();
        this.filtros.put("Filtro_Fecha", new Filtro_Fecha());
        return true;
    }

    public abstract boolean generarExcel();

    public abstract boolean generarFiltrosBaseCustom();

    public abstract ArrayList<String> completarColumnasTabla();

    public abstract boolean generarReporte();

    public abstract boolean desplegarInfoExcelApp(TableView<Recurso> tableView, AnchorPane ap);
    
}
