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
public class BaseReporteFugaFS extends Recurso
{
    public String clienteLocal_id;
    public String clienteLocal_nombre;
    public String comuna;
    public String direccion;
    public String cadena;
    public String categoriaCliente;
    public String subcategoriaCliente;
    public String sucursal;
    public String zonaVenta;
    public String supervisor;
    public String preventa;
    public String kam;
    public String centralizado;
    public String agcnc;
    public String año;
    public String mes;
    public String ejecutivo;
    public String diaLlamado;
    public String tipoClub;
    public String categoriaClub;
    public String segmentoClub;
    public String canje;
    public String clienteFugado;
    public String clienteHistorico;
    public String clienteNuevo;
    public String clienteVigente;
    public String clienteRecuperado;
    public String clienteFugaNeto;
    public String clienteCrecimientoNeto;
    public String clienteRecuperadoKg;
    public String clienteFugaKg;
    public String clienteCrecimientoKg;
    public String brechaFuga_neto;
    public String brechaCrecimiento_neto;
    public String brecha_neto;
    public String brechaFuga_Kg;
    public String brechaCrecimiento_Kg;
    public String brecha_Kg;
    public String ventaClientesFugados;
    public String ventaClientesHistoricos;
    public String ventaClientesNuevos;
    public String ventaClientesVigentes;
    public String clientesFugados_Kg;
    public String clientesHistoricos_Kg;
    public String clientesNuevos_Kg;
    public String clientesVigentes_Kg;
    public String tipoCall;
    public String kamJr;
    public String jefeVentas;

    public BaseReporteFugaFS(String aux)
    {
        super();
        //separacion entre ;
        String content[] = aux.split(";");
        this.clienteLocal_id=content[1];
        this.clienteLocal_nombre=content[2];
        this.comuna=content[3];
        this.direccion=content[4];
        this.cadena=content[5];
        this.categoriaCliente=content[6];
        this.subcategoriaCliente=content[7];
        this.sucursal=content[8];
        this.zonaVenta=content[9];
        this.supervisor=content[10];
        this.preventa=content[11];
        this.kam=content[12];
        this.centralizado=content[13];
        this.agcnc=content[14];
        this.año=content[15];
        this.mes = content[16];
        this.ejecutivo=content[17];
        this.diaLlamado=content[18];
        this.tipoClub=content[19];
        this.categoriaClub=content[20];
        this.segmentoClub=content[21];
        this.canje=content[22];
        this.clienteFugado = content[23];
        this.clienteHistorico = content[24];
        this.clienteNuevo = content[25];
        this.clienteVigente = content[26];
        this.clienteRecuperado = content[27];
        this.clienteFugaNeto = content[28];
        this.clienteCrecimientoNeto = content[29];
        this.clienteRecuperadoKg = content[30];
        this.clienteFugaKg = content[31];
        this.clienteCrecimientoKg = content[33].replace('.', ',');
        this.brechaFuga_neto = content[34].replace('.', ',');
        this.brechaCrecimiento_neto = content[35].replace('.', ',');
        this.brecha_neto = content[36].replace('.', ',');
        this.brechaFuga_Kg = content[37].replace('.', ',');
        this.brechaCrecimiento_Kg = content[38].replace('.', ',');
        this.brecha_Kg = content[39].replace('.', ',');
        this.ventaClientesFugados = content[40].replace('.', ',');
        this.ventaClientesHistoricos = content[41].replace('.', ',');
        this.ventaClientesNuevos = content[42].replace('.', ',');
        this.ventaClientesVigentes = content[43].replace('.', ',');
        this.clientesFugados_Kg = content[44].replace('.', ',');
        this.clientesHistoricos_Kg = content[45].replace('.', ',');
        this.clientesNuevos_Kg = content[46].replace('.', ',');
        this.clientesVigentes_Kg = content[47].replace('.', ',');
        this.tipoCall = content[48];
        this.kamJr = content[49];
        this.jefeVentas = content[50];
    }
    
}
