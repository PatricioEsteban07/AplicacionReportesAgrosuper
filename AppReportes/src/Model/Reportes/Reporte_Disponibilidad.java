/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model.Reportes;

import Model.LocalDB;
import Model.RecursosDB.RecursoDB;
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
import Model.RecursosDB.ReporteDisponibilidad.RecursoDB_TablaPedidos;
import java.util.ArrayList;
import java.util.HashMap;

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
        if( !generarRecurso(this.recursos.get("Sectores"),null) 
                || !generarRecurso(this.recursos.get("Estado Refrigerados"),null)
                || !generarRecurso(this.recursos.get("Agrupados"),null)
                || !generarRecurso(this.recursos.get("Tipo Envasados"),null)
                || !generarRecurso(this.recursos.get("Marcas"),null)
                || !generarRecurso(this.recursos.get("Materiales"),null)
                || !generarRecurso(this.recursos.get("Centros"),null)
                || !generarRecurso(this.recursos.get("Oficinas"),null)
                || !generarRecurso(this.recursos.get("Tipos Cliente"),null)
                || !generarRecurso(this.recursos.get("Pedidos"),null)
                || !generarRecurso(this.recursos.get("Pedido-Materiales"),null)
                || !generarRecurso(this.recursos.get("Regiones"),null)
                || !generarRecurso(this.recursos.get("Clientes"),null)
                || !generarRecurso(this.recursos.get("Clientes Locales"),null)
                || !generarRecurso(this.recursos.get("Despachos"),null)
                || !generarRecurso(this.recursos.get("Despacho-Materiales"),null)
                || !generarRecurso(this.recursos.get("Stocks"),null) )
        {
            return false;
        }
        return true;
        //obtener recursos de db
        //Material: sector, refrig, agrupado, tipoEnvasado, marca
        //pedido
        //pedido-material
        //despacho
        //despacho-material
        //stock
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
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