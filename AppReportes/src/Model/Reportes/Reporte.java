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
import Model.RecursosDB.RecursoDB;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Clase abstracta para trabajar con elementos encargados de generar reportes.
 * @author Patricio
 */
public abstract class Reporte
{
    public LocalDB db;
    public String nombre;
    private String fileDir;
    public HashMap<String, GeneradorExcel> generadorExcel;
    public HashMap<String, RecursoDB> recursos;
    public HashMap<String, Filtro> filtros;
    public ArrayList<String> columnasExcel;

    public Reporte(String nombre, LocalDB db)
    {
        this.db = db;
        this.nombre = nombre;
        this.fileDir= System.getProperty("user.home") + "/Desktop/";
        this.generadorExcel = new HashMap<>();
        this.recursos = new HashMap<>();
        this.filtros = new HashMap<>();
        this.columnasExcel = new ArrayList<>();
        this.columnasExcel = this.completarColumnasTabla();
    }

    /**
     * Método para inicializar un reporte con filtros por defecto.
     * @return true si la operación fué exitosa o false en caso contrario.
     */
    public boolean generarFiltrosBase()
    {
        //crear todos los filtros vacios
        this.filtros = new HashMap<>();
        this.filtros.put("Filtro_Fecha", new Filtro_Fecha());
        return true;
    }
    
    /**
     * Método para modificar la dirección donde se guardará el reporte generado.
     * @param fd la nueva dirección para guardar el reporte generado.
     */
    public void setFileDir(String fd)
    {
        this.fileDir = fd;
    }
    
    /**
     * Método que obtiene la dirección donde el reporte será generado.
     * @return la dirección donde el reporte será generado.
     */
    public String getFileDir()
    {
        return this.fileDir;
    }

    /**
     * Método para generar el reporte como archivo Excel.
     * @return true si la operación fué exitosa o false en caso contrario.
     */
    public abstract boolean generarExcel();

    /**
     * Método para refinar los filtros inicialmente generados, eliminando los que no aplican.
     * @return true si la operación fué exitosa o false en caso contrario.
     */
    public abstract boolean generarFiltrosBaseCustom();

    /**
     * Método que se encarga de rellenar un listado con los títulos de las columnas del reporte.
     * @return un listado con los títulos de las columnas del reporte.
     */
    public abstract ArrayList<String> completarColumnasTabla();

    /**
     * Método que se encarga de hacer llamado a distintos métodos para realizar la petición de datos a la base de datos, 
     * el procesamiento de la información recibida y la generación del reporte en específico.
     * @return true si el reporte fué generado exitosamente o false en otro caso.
     */
    public abstract boolean generarReporte();
}
