/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model.Filtros;

/**
 * Clase abstracta para los Objetos de tipo Filtro.
 * @author Patricio
 */
public abstract class Filtro
{
    public String nombre;
    protected int opcion;

    public Filtro(String nombre)
    {
        this.nombre = nombre;
        this.opcion=0;
    }

    /**
     * Método para obtener la opción seleccionada actualmente.
     * @return un valor entero correspondiente a la opción seleccionada.
     */
    public int getOpcion()
    {
        return opcion;
    }
    
    /**
     * Método abstracto que se encarga de vaciar el filtro correspondiente.
     * @return true si fué exitoso o false en caso contrario.
     */
    public abstract boolean vaciarFiltro();
    
    /**
     * Método abstracto que se encarga de cambiar la opción seleccionada.
     * @return true si fué exitoso o false en caso contrario.
     */
    public abstract boolean setOpcion(int value);
    
    /**
     * Método abstracto que se encarga de vaciar el filtro correspondiente.
     * @return un String con la etiqueta generada si fué exitoso o null en caso contrario.
     */
    public abstract String generarEtiquetaInfo();
}
