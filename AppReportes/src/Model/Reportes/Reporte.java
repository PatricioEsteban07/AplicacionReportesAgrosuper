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
        this.filtros.put("Filtro_Canal", new Filtro_Canal(generaCanales()));
        this.filtros.put("Filtro_Zona", new Filtro_Zona(generaZonas()));
        this.filtros.put("Filtro_Sucursal", new Filtro_Sucursal(generaSucursales()));
        this.filtros.put("Filtro_Cliente", new Filtro_Cliente(generaClientes()));
        this.filtros.put("Filtro_CargoRRHH", new Filtro_CargoRRHH(generaCargosRRHH()));
        return true;
    }

    public ArrayList<String> generaZonas()
    {
        ArrayList<String> zonas = new ArrayList<>();
        zonas.add("Norte");
        zonas.add("Centro Norte");
        zonas.add("Santiago");
        zonas.add("Centro Sur");
        zonas.add("Sur");
        return zonas;
    }

    public ArrayList<String> generaCanales()
    {
        ArrayList<String> canales = new ArrayList<>();
        canales.add("Supermercado");
        canales.add("Food Service");
        canales.add("Call Center");
        canales.add("Tradicional");
        canales.add("Cliente Importante");
        return canales;
    }

    public abstract boolean generarExcel();

    public abstract boolean generarFiltrosBaseCustom();

    public abstract ArrayList<String> completarColumnasTabla();

    private ArrayList<String> generaSucursales()
    {
        ArrayList<String> aux = new ArrayList<>();
        aux.add("Sucursal 1");
        aux.add("Sucursal 2");
        aux.add("Sucursal 3");
        return aux;
    }

    private ArrayList<String> generaClientes()
    {
        ArrayList<String> aux = new ArrayList<>();
        aux.add("Cliente 1");
        aux.add("Cliente 2");
        aux.add("Cliente 3");
        return aux;
    }

    private ArrayList<String> generaCargosRRHH()
    {
        ArrayList<String> aux = new ArrayList<>();
        aux.add("Cargo 1");
        aux.add("Cargo 2");
        aux.add("Cargo 3");
        return aux;
    }

    public abstract boolean generarReporte();

    public abstract boolean desplegarInfoExcelApp(TableView<Recurso> tableView, AnchorPane ap);
    
}
