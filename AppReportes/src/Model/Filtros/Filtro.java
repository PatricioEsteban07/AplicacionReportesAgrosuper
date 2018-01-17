/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model.Filtros;

import java.util.HashMap;

/**
 *
 * @author Patricio
 */
public abstract class Filtro
{
    public String nombre;

    public Filtro(String nombre)
    {
        this.nombre = nombre;
    }
    
    public abstract String generarWhere(HashMap<String,String> data);
    
    public abstract boolean vaciarFiltro();
}
