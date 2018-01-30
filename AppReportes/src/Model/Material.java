/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import java.util.Date;

/**
 *
 * @author Patricio
 */
public class Material extends Recurso
{
    public String nombre;
    public Date fecha;
    public int duracion;
    public int pesoCaja;
    public int activo;
    public TipoEnvasado tipoEnvasado;
    public EstadoRefrigerado tipoRefrigerado;
    public TipoAgrupado tipoAgrupado;
    public Sector sector;
    public Marca marca;

    public Material(String id, String nombre)
    {
        super(id);
        this.nombre=nombre;        
    }

    public Material(String id, String nombre, Date fecha, int duracion, int pesoCaja, int activo, 
            TipoEnvasado tipoEnvasado, EstadoRefrigerado tipoRefrigerado, TipoAgrupado tipoAgrupado, 
            Sector sector, Marca marca)
    {
        super(id);
        this.nombre = nombre;
        this.fecha = fecha;
        this.duracion = duracion;
        this.pesoCaja = pesoCaja;
        this.activo = activo;
        this.tipoEnvasado = tipoEnvasado;
        this.tipoRefrigerado = tipoRefrigerado;
        this.tipoAgrupado = tipoAgrupado;
        this.sector = sector;
        this.marca = marca;
    }
    
}
