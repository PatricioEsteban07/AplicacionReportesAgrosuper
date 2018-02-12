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
public class BaseReporteArbolPerdidas extends Recurso
{
    public String mes;
    public String semana;
    public String sector_nombre;
    public String tipoCliente;
    public String centro_id;
    public String centro_nombre;
    public String agrupado_id;
    public String agrupado_nombre;
    public String n2_nombre;
    public String Pedido_Kg;
    public String Factura_Kg;
    public String Demanda_Kg;
    public String NS_Kg;
    public String Faltante_Kg;
    public String Sobrefactura_Kg;
    public String PP_Neto;
    public String Faltante_Neto;
    public String Pedido_Cj;
    public String Factura_Cj;
    public String Demanda_Cj;
    public String NS_Cj;
    public String Sobrefactura_Cj;
    public String Faltante_Cj;
    public String Disp_Pedido_Cj;
    public String Disp_Faltante_Cj;
    public String Disp_Pedido_Kg;
    public String Disp_Faltante_Kg;
    public String Factura_Faltante_Kg;
    public String Factura_Faltante_Cj;
    public String Pedido_Neto;
    public String Anio;
    public String semanaAnio;

    public BaseReporteArbolPerdidas(String aux)
    {
        super();
        //separacion entre ;
        String content[] = aux.split(";");
        this.mes=content[1];
        this.semana=content[2];
        this.sector_nombre=content[3];
        this.tipoCliente=content[4];
        this.centro_id=content[5];
        this.centro_nombre=content[6];
        this.agrupado_id=content[7];
        this.agrupado_nombre=content[8];
        this.n2_nombre=content[9];
        this.Pedido_Kg=content[10].replace('.', ',');
        this.Factura_Kg=content[11].replace('.', ',');
        this.Demanda_Kg=content[12].replace('.', ',');
        this.NS_Kg=content[13].replace('.', ',');
        this.Faltante_Kg=content[14].replace('.', ',');
        this.Sobrefactura_Kg=content[15].replace('.', ',');
        this.PP_Neto = content[16].replace('.', ',');
        this.Faltante_Neto=content[17].replace('.', ',');
        this.Pedido_Cj=content[18].replace('.', ',');
        this.Factura_Cj=content[19].replace('.', ',');
        this.Demanda_Cj=content[20].replace('.', ',');
        this.NS_Cj=content[21].replace('.', ',');
        this.Sobrefactura_Cj=content[22].replace('.', ',');
        this.Faltante_Cj = content[23].replace('.', ',');
        this.Disp_Pedido_Cj = content[24].replace('.', ',');
        this.Disp_Faltante_Cj = content[25].replace('.', ',');
        this.Disp_Pedido_Kg = content[26].replace('.', ',');
        this.Disp_Faltante_Kg = content[27].replace('.', ',');
        this.Factura_Faltante_Kg = content[28].replace('.', ',');
        this.Factura_Faltante_Cj = content[29].replace('.', ',');
        this.Pedido_Neto = content[30].replace('.', ',');
        this.Anio = content[31];
        this.semanaAnio = content[32];
    }
    
}
