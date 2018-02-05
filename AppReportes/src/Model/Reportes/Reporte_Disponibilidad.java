/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model.Reportes;

import Model.Despacho;
import Model.LocalDB;
import Model.Material;
import Model.Pedido;
import Model.Recurso;
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
import Model.RecursosDB.RecursoDB_Sectores;
import Model.RecursosDB.RecursoDB_Stock;
import Model.RecursosDB.RecursoDB_TipoClientes;
import Model.RecursosDB.RecursoDB_TipoEnvasados;
import Model.Stock;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

/**
 *
 * @author Patricio
 */
public class Reporte_Disponibilidad extends Reporte
{

    public Reporte_Disponibilidad(LocalDB db)
    {
        super("Reporte de Disponibilidad",db);
        
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
        
        if( !generarRecurso(this.recursos.get("Sectores")) 
            || !generarRecurso(this.recursos.get("Estado Refrigerados"))
            || !generarRecurso(this.recursos.get("Agrupados"))
            || !generarRecurso(this.recursos.get("Tipo Envasados"))
            || !generarRecurso(this.recursos.get("Marcas"))
            || !generarRecurso(this.recursos.get("Materiales"))
            || !generarRecurso(this.recursos.get("Centros"))
            || !generarRecurso(this.recursos.get("Oficinas"))
            || !generarRecurso(this.recursos.get("Tipos Cliente"))
            || !generarRecurso(this.recursos.get("Pedidos"))
            || !generarRecurso(this.recursos.get("Pedido-Materiales"))
            || !generarRecurso(this.recursos.get("Regiones"))
            || !generarRecurso(this.recursos.get("Clientes"))
            || !generarRecurso(this.recursos.get("Clientes Locales"))
            || !generarRecurso(this.recursos.get("Despachos"))
            || !generarRecurso(this.recursos.get("Despacho-Materiales"))
            || !generarRecurso(this.recursos.get("Stocks")) )
        {
            return false;
        }
        //generar elementos para tabla
        
        HashMap<String, BaseReporteDisponibilidad> filas = new HashMap<>();
        
        //parte 1 : tabla básica
        
        //sacar centro (se hizo anteriormente)
        //sacar sector_material
        //sacar agrupacion material
        //sacar semana/diaSemana/año
        ArrayList<Recurso> recursosAux = this.recursos.get("Pedidos").getAll();
        for (int i=0; i<recursosAux.size();i++)
        {
            Pedido p = ((Pedido)recursosAux.get(i));
            String preKey = p.centro.id+","+p.fechaEntrega+",";
            Iterator<String> productos = p.materiales.keySet().iterator();
            while(productos.hasNext()){
                String key = productos.next();
                Material m = p.materiales.get(key).material;
                if(!filas.containsKey(preKey+key))
                    filas.put(preKey+key, new BaseReporteDisponibilidad(p.centro.id,p.centro.nombre,m.sector.nombre,
                            m.tipoAgrupado.id,m.tipoAgrupado.nombre,p.fechaEntrega));
                
                filas.get(preKey+key).pedido_Cj+=p.materiales.get(key).cantidadCj;
                filas.get(preKey+key).pedido_Kg+=p.materiales.get(key).pesoKg;
                filas.get(preKey+key).pedido_neto+=p.materiales.get(key).pesoNeto;
            }    
        }
                
        recursosAux = this.recursos.get("Stocks").getAll();
        for (int i=0; i<recursosAux.size();i++)
        {
            Stock s = ((Stock)recursosAux.get(i));
            String preKey = s.centro.id+","+s.fecha+",";
            String key = s.material.id;

            if(!filas.containsKey(preKey+key))
                filas.put(preKey+key, new BaseReporteDisponibilidad(s.centro.id,s.centro.nombre,s.material.sector.nombre,
                s.material.tipoAgrupado.id, s.material.tipoAgrupado.nombre,s.fecha));
            
            filas.get(preKey+key).stock_disponibilidadKg+=s.disponible;
            filas.get(preKey+key).stock_disponibleCj+=s.disponible/s.material.pesoCaja;
        }
        
        HashMap<String, Integer> despachoCjs = new HashMap<>();
        HashMap<String, Integer> despachoKgs = new HashMap<>();
                
        recursosAux = this.recursos.get("Despachos").getAll();
        for (int i=0; i<recursosAux.size();i++)
        {
            Despacho d = ((Despacho)recursosAux.get(i));
            String preKey = d.centro.id+","+d.fecha+",";
            Iterator<String> productos = d.materiales.keySet().iterator();
            while(productos.hasNext()){
                String key = productos.next();
                if(!filas.containsKey(preKey+key))
                    filas.put(preKey+key, new BaseReporteDisponibilidad(d.centro.id,d.centro.nombre,
                            d.materiales.get(key).material.sector.nombre,d.materiales.get(key).material.tipoAgrupado.id,
                            d.materiales.get(key).material.tipoAgrupado.nombre, d.fecha));
                
                filas.get(preKey+key).despacho_Cj+=d.materiales.get(key).despachoCj;
                filas.get(preKey+key).despacho_Kg+=d.materiales.get(key).despachoKg;
            }    
        }
        
        //se calculan sobrantes y faltantes
        Iterator<String> rows = filas.keySet().iterator();
        while(rows.hasNext()){
            String key = rows.next();
            BaseReporteDisponibilidad row = filas.get(key);
            row.faltanteCj = (row.stock_disponibleCj>=row.pedido_Cj) ? 0 : row.pedido_Cj-row.stock_disponibleCj;
            row.faltanteKg = (row.stock_disponibilidadKg>=row.pedido_Kg) ? 0 : row.pedido_Kg-row.stock_disponibilidadKg;
            row.sobranteCj = (row.stock_disponibleCj>row.pedido_Cj) ? row.stock_disponibleCj-row.pedido_Cj : 0 ;
            row.sobranteKg = (row.stock_disponibilidadKg>row.pedido_Kg) ? row.stock_disponibilidadKg-row.pedido_Kg : 0 ;
            row.faltanteDespachoCj = (row.despacho_Cj<row.pedido_Cj) ? row.pedido_Cj-row.despacho_Cj : 0 ;
            row.faltanteAjustadoCj = (row.faltanteDespachoCj<row.faltanteCj) ? row.faltanteDespachoCj : row.faltanteCj ;
            row.faltanteDespachoKg = (row.despacho_Kg<row.pedido_Kg) ? row.pedido_Kg-row.despacho_Kg : 0 ;
            row.faltanteAjustadoKg = (row.faltanteDespachoKg<row.faltanteKg) ? row.faltanteDespachoKg : row.faltanteKg ;
        }
        
        //generar excel con info
        
        
        
        return false;
    }

    @Override
    public boolean generarExcel()
    {
        
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean generarFiltrosBaseCustom()
    {    
        //crear todos los filtros vacios
        //filtro no utilizado, Filtro_Cliente
        generarFiltrosBase();
        this.filtros.remove("Filtro_Cliente");
        return true;
    }

    @Override
    public ArrayList<String> completarColumnasTabla()
    {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}