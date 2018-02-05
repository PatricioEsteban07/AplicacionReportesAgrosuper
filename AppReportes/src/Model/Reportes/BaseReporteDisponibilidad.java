/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model.Reportes;

import java.util.Date;

/**
 *
 * @author Patricio
 */
public class BaseReporteDisponibilidad
{
    public String centro_id;
    public String centro_nombre;
    public String material_sector;
    public String agrupado_id;
    public String agrupado_nombre;
    public Date pedido_fechaEntrega;
    public int pedido_Cj;
    public int despacho_Cj;
    public float stock_disponibleCj;
    public float pedido_Kg;
    public int despacho_Kg;
    public int pedido_neto;
    public float stock_disponibilidadKg;
    public float faltanteCj;
    public float faltanteKg;
    public int semana;
    public float sobranteCj;
    public float sobranteKg;
    public float faltanteDespachoCj;
    public float faltanteAjustadoCj;
    public float faltanteDespachoKg;
    public float faltanteAjustadoKg;
    public int diaSemana;
    public int anio;
    
    public BaseReporteDisponibilidad(String centro_id, String centro_nombre, String sector, String agrupado_id,
            String agrupado_nombre, Date fecha)
    {
        this.centro_id=centro_id;
        this.centro_nombre=centro_nombre;
        this.material_sector=sector;
        this.agrupado_id=agrupado_id;
        this.agrupado_nombre=agrupado_nombre;
        this.pedido_fechaEntrega=fecha;
        this.diaSemana = fecha.getDay()+1;
        this.anio = 1900+fecha.getYear();
        this.pedido_Cj=0;
        this.despacho_Cj=0;
        this.stock_disponibleCj=0;
        this.pedido_Kg=0;
        this.despacho_Kg=0;
        this.pedido_neto=0;
        this.stock_disponibilidadKg=0;
        this.faltanteCj=0;
        this.faltanteKg=0;
        this.sobranteCj=0;
        this.sobranteKg=0;
        this.faltanteDespachoCj=0;
        this.faltanteAjustadoCj=0;
        this.faltanteDespachoKg=0;
        this.faltanteAjustadoKg=0;
    }
    
}
