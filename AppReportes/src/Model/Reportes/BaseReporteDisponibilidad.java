/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model.Reportes;

import Model.Recurso;

/**
 *
 * @author Patricio
 */
public class BaseReporteDisponibilidad extends Recurso
{
    public String centro_id;
    public String centro_nombre;
    public String sector_id;
    public String sector_nombre;
    public String agrupado_id;
    public String agrupado_nombre;
    public String pedido_fechaEntrega;
    public String pedido_Cj;
    public String despacho_Cj;
    public String stock_disponibleCj;
    public String pedido_Kg;
    public String pedido_neto;
    public String stock_disponibilidadKg;
    public String faltanteCj;
    public String faltanteKg;
    public String semana;
    public String sobranteCj;
    public String sobranteKg;
    public String faltanteDespachoCj;
    public String faltanteAjustadoCj;
    public String faltanteDespachoKg;
    public String faltanteAjustadoKg;
    public String diaSemana;
    public String anio;

    public BaseReporteDisponibilidad(String aux)
    {
        super();
        //separacion entre ;
        String content[] = aux.split(";");
        this.centro_id=content[1];
        this.centro_nombre=content[2];
        this.sector_id=content[3];
        this.sector_nombre=content[4];
        this.agrupado_id=content[5];
        this.agrupado_nombre=content[6];
        this.pedido_fechaEntrega=content[7].substring(8)+"-"+content[7].substring(5, 7)+"-"+content[7].substring(0, 4);
        this.pedido_Cj=content[8].replace('.', ',');
        this.despacho_Cj=content[9].replace('.', ',');
        this.stock_disponibleCj=content[10].replace('.', ',');
        this.pedido_Kg=content[11].replace('.', ',');
        this.pedido_neto=content[12].replace('.', ',');
        this.stock_disponibilidadKg=content[13].replace('.', ',');
        this.faltanteCj=content[14].replace('.', ',');
        this.faltanteKg=content[15].replace('.', ',');
        this.semana = content[16];
        this.sobranteCj=content[17].replace('.', ',');
        this.sobranteKg=content[18].replace('.', ',');
        this.faltanteDespachoCj=content[19].replace('.', ',');
        this.faltanteAjustadoCj=content[20].replace('.', ',');
        this.faltanteDespachoKg=content[21].replace('.', ',');
        this.faltanteAjustadoKg=content[22].replace('.', ',');
        this.diaSemana = content[23];
        this.anio = content[24];
        
    }
    
}
