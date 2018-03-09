/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model.RecursosDB;

import Model.LocalDB;
import Model.Recurso;
import java.util.ArrayList;

/**
 * Clase base para Objetos de tipo RecursoDB que trabajan con la base de datos para generar un reporte en específico.
 * @author Patricio
 */
public abstract class RecursoDB
{
    protected String query;
    public String nombre;
    protected ArrayList<Recurso> datos;
    public LocalDB db;

    public RecursoDB(String nombre, String query, LocalDB db)
    {
        this.nombre=nombre;
        this.query=query;
        this.datos = new ArrayList<>();
        this.db=db;
    }
    
    public RecursoDB(String nombre, String query, ArrayList<Recurso> datos, LocalDB db)
    {
        this.nombre=nombre;
        this.query=query;
        this.datos = datos;
        this.db=db;
    }
    
    /**
     * Método para obtener un listado de todos los recursos disponibles en esta clase.
     * @return un listado con objetos de tipo Recurso.
     */
    public ArrayList<Recurso> getAll()
    {
        return this.datos;
    }
}
