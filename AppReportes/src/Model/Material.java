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
    public Date fechaCreacion;
    public int pesoCaja;
    public Sector sector;
    public int duracion;
    public EstadoRefrigerado tipoRefrigerado;
    public Agrupado tipoAgrupado;
    public TipoEnvasado tipoEnvasado;
    public Marca marca;

    public Material(String id)
    {
        super(id);     
    }

    public Material(String id, String nombre, Date fechaCreacion, int pesoCaja, Sector sector, int duracion, 
            EstadoRefrigerado tipoRefrigerado, Agrupado tipoAgrupado, TipoEnvasado tipoEnvasado, Marca marca)
    {
        super(id);
        this.nombre = nombre;
        this.fechaCreacion = fechaCreacion;
        this.pesoCaja = pesoCaja;
        this.sector = sector;
        this.duracion = duracion;
        this.tipoRefrigerado = tipoRefrigerado;
        this.tipoAgrupado = tipoAgrupado;
        this.tipoEnvasado = tipoEnvasado;
        this.marca = marca;
    }
    
    
    
}
