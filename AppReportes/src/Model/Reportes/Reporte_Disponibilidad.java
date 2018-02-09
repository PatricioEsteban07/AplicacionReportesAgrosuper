/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model.Reportes;

import Model.GeneradoresExcel.GeneradorExcel_ReporteDisponibilidad;
import Model.LocalDB;
import Model.RecursosDB.RecursoDB_Agrupados;
import Model.RecursosDB.RecursoDB_Centros;
import Model.RecursosDB.RecursoDB_Clientes;
import Model.RecursosDB.RecursoDB_ClientesLocales;
import Model.RecursosDB.RecursoDB_Despachos;
import Model.RecursosDB.RecursoDB_DespachosMaterial;
import Model.RecursosDB.RecursoDB_EstadoRefrigerados;
import Model.RecursosDB.RecursoDB_Marcas;
import Model.RecursosDB.RecursoDB_Materiales;
import Model.RecursosDB.RecursoDB_OficinaVentas;
import Model.RecursosDB.RecursoDB_Pedidos;
import Model.RecursosDB.RecursoDB_PedidosMaterial;
import Model.RecursosDB.RecursoDB_Regiones;
import Model.RecursosDB.RecursoDB_ReporteDisponibilidad;
import Model.RecursosDB.RecursoDB_Sectores;
import Model.RecursosDB.RecursoDB_Stock;
import Model.RecursosDB.RecursoDB_TipoClientes;
import Model.RecursosDB.RecursoDB_TipoEnvasados;
import java.io.IOException;
import java.sql.Date;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Patricio
 */
public class Reporte_Disponibilidad extends Reporte
{

    private HashMap<String, BaseReporteDisponibilidad> elementos;

    public Reporte_Disponibilidad(LocalDB db)
    {
        super("Reporte de Disponibilidad",db);
        /*
        //Materiales
        this.recursos.put("Sectores", new RecursoDB_Sectores(this.db));
        this.recursos.put("Estado Refrigerados", new RecursoDB_EstadoRefrigerados(this.db));
        this.recursos.put("Agrupados", new RecursoDB_Agrupados(this.db));
        this.recursos.put("Tipo Envasados", new RecursoDB_TipoEnvasados(this.db));
        this.recursos.put("Marcas", new RecursoDB_Marcas(this.db));
        this.recursos.put("Materiales", new RecursoDB_Materiales(this.db));
        
        //Pedidos
        this.recursos.put("Centros", new RecursoDB_Centros(this.db));
        this.recursos.put("Oficinas", new RecursoDB_OficinaVentas(this.db));
        this.recursos.put("Tipos Cliente", new RecursoDB_TipoClientes(this.db));
        this.recursos.put("Pedidos", new RecursoDB_Pedidos(this.db));
        this.recursos.put("Pedido-Materiales", new RecursoDB_PedidosMaterial(this.db));
        
        //Despachos
        this.recursos.put("Regiones", new RecursoDB_Regiones(this.db));
        this.recursos.put("Clientes", new RecursoDB_Clientes(this.db));
        this.recursos.put("Clientes Locales", new RecursoDB_ClientesLocales(this.db));
        this.recursos.put("Despachos", new RecursoDB_Despachos(this.db));
        this.recursos.put("Despacho-Materiales", new RecursoDB_DespachosMaterial(this.db));
        
        //Stocks
        this.recursos.put("Stocks", new RecursoDB_Stock(this.db));
        */
        this.recursos.put("DB Reporte", new RecursoDB_ReporteDisponibilidad(this.db));
        
        this.generarFiltrosBaseCustom();
    }

    @Override
    public boolean generarRecursos()
    {
        //obtener recursos de db
        //Material: sector, refrig, agrupado, tipoEnvasado, marca
        //pedido
        //pedido-material
        //despacho
        //despacho-material
        //stock
        
        Iterator<String> itFiltro = this.filtros.keySet().iterator();
        while(itFiltro.hasNext()){
            String key = itFiltro.next();
            if(this.filtros.get(key).getOpcion()!=0)
            {
                
            }
        }
        
        if( !generarRecurso(this.recursos.get("Sectores"))) 
        {
            return false;
        }
        return true;
    }

    @Override
    public boolean generarExcel()
    {
        this.generadorExcel.put("Reporte Disponibilidad", new GeneradorExcel_ReporteDisponibilidad());
        try
        {
            if(!this.generadorExcel.get("Reporte Disponibilidad").generarArchivo(this.recursos))
            {
                System.out.println("ERROR: problema generando archivos excel Reporte Disponibilidad :c");
            }
        }
        catch (IOException ex)
        {
            Logger.getLogger(Reporte_Disponibilidad.class.getName()).log(Level.SEVERE, null, ex);
        }
        return true;
    }

    @Override
    public boolean generarFiltrosBaseCustom()
    {    
        //crear todos los filtros vacios
        //filtro no utilizado, Filtro_Cliente, Filtro_Sucursal
        generarFiltrosBase();
        this.filtros.remove("Filtro_Cliente");
        this.filtros.remove("Filtro_Sucursal");
        return true;
    }

    @Override
    public ArrayList<String> completarColumnasTabla()
    {
        ArrayList<String> columnas=new ArrayList<>();
        columnas.add("centro_id");
        columnas.add("centro_nombre");
        columnas.add("sector_nombre");
        columnas.add("agrupado_id");
        columnas.add("agrupado_nombre");
        columnas.add("fecha");
        columnas.add("pedido_Cj");
        columnas.add("despacho_Cj");
        columnas.add("disponible_Cj");
        columnas.add("pedido_Kg");
        columnas.add("pedido_neto");
        columnas.add("disponible_Kg");
        columnas.add("faltante_Cj");
        columnas.add("faltante_Kg");
        columnas.add("semana");
        columnas.add("sobrante_Cj");
        columnas.add("sobrante_Kg");
        columnas.add("faltanteDespacho_Cj");
        columnas.add("faltanteAjustado_Cj");
        columnas.add("faltanteDespacho_Kg");
        columnas.add("faltanteAjustado_Kg");
        columnas.add("diaSemana");
        columnas.add("año");
        return columnas;
    }

    @Override
    public boolean generarReporte()
    {
        try
        {
            /*
            llamado a sp
            recepcion de res sp
            envio a genExcel para generacion de file
            op: desplegar tabla en app
            */
            String fechaInicio="2018-01-30";
            String fechaFin="2018-01-30";
            ArrayList<String> resultados = ((RecursoDB_ReporteDisponibilidad)this.recursos.get("DB Reporte")).procedimientoAlmacenado(fechaInicio,fechaFin);
            if(resultados==null)
            {
                return false;
            }
            //trabajar con arraylist -> separados elementos por ;
            return true;
        }
        catch (SQLException ex)
        {
            Logger.getLogger(Reporte_Disponibilidad.class.getName()).log(Level.SEVERE, null, ex);
        }
        catch (ClassNotFoundException ex)
        {
            Logger.getLogger(Reporte_Disponibilidad.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }
    
}