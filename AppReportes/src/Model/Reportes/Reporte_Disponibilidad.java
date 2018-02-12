/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model.Reportes;

import Model.Filtros.Filtro_Fecha;
import Model.GeneradoresExcel.GeneradorExcel_ReporteDisponibilidad;
import Model.LocalDB;
import Model.Recurso;
import Model.RecursosDB.RecursoDB_ReporteDisponibilidad;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;

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
        this.recursos.put("Reporte Disponibilidad", new RecursoDB_ReporteDisponibilidad(this.db));
        
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
        this.generadorExcel.put("Reporte Disponibilidad", new GeneradorExcel_ReporteDisponibilidad(completarColumnasTabla()));
        try
        {
            if(!this.generadorExcel.get("Reporte Disponibilidad").generarArchivo(this.recursos))
            {
                System.out.println("ERROR: problema generando archivos excel Reporte Disponibilidad");
                return false;
            }
            return true;
        }
        catch (IOException ex)
        {
            Logger.getLogger(Reporte_Disponibilidad.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
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
        columnas.add("sector_id");
        columnas.add("sector_nombre");
        columnas.add("agrupado_id");
        columnas.add("agrupado_nombre");
        columnas.add("fecha");
        columnas.add("pedido_Cj");
        columnas.add("despacho_Cj");
        columnas.add("disponible_Cj");
        columnas.add("pedido_Kg");
        columnas.add("pedido_neto");
        columnas.add("disponibleKg");
        columnas.add("faltanteCj");
        columnas.add("faltanteKg");
        columnas.add("semana");
        columnas.add("sobranteCj");
        columnas.add("sobranteKg");
        columnas.add("faltanteDespachoCj");
        columnas.add("faltanteAjustadoCj");
        columnas.add("faltanteDespachoKg");
        columnas.add("faltanteAjustadoKg");
        columnas.add("diaSemana");
        columnas.add("anio");
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
            Filtro_Fecha ff= ((Filtro_Fecha)this.filtros.get("Filtro_Fecha"));
            ff.prepararFiltro();
            String fechaInicio="2018-01-30";
            String fechaFin=null;
            if(ff != null)
            {
                if(ff.getFechaInicio()!=null)
                {       
                    fechaInicio=ff.getFechaInicio().getYear()+"-"
                            +(((ff.getFechaInicio().getMonth())<9) ? "0"+(ff.getFechaInicio().getMonth()+1): (ff.getFechaInicio().getMonth()+1))+"-"
                            +(((ff.getFechaInicio().getDate())<10) ? "0"+(ff.getFechaInicio().getDate()): (ff.getFechaInicio().getDate()));
                }
                if(ff.getFechaFin()!=null)
                {
                    fechaFin=ff.getFechaFin().getYear()+"-"+(((ff.getFechaFin().getMonth())<9) ? "0"+(ff.getFechaFin().getMonth()+1): (ff.getFechaFin().getMonth()+1))+"-"
                            +(((ff.getFechaFin().getDate())<10) ? "0"+(ff.getFechaFin().getDate()): (ff.getFechaFin().getDate()));
                }
            }
            
            ArrayList<String> resultados = ((RecursoDB_ReporteDisponibilidad)this.recursos.get("Reporte Disponibilidad")).procedimientoAlmacenado(fechaInicio,fechaFin);
            if(resultados==null)
            {
                return false;
            }
            if(!generarExcel())
            {
                System.out.println("No existen datos o algo malo paso :c");
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

    @Override
    public boolean desplegarInfoExcelApp(TableView<Recurso> asdf, AnchorPane panelTabla)
    {
        TableView<BaseReporteDisponibilidad> ReportesTableView = new TableView<>();
        for (int i = 0; i < this.completarColumnasTabla().size(); i++)
        {
            TableColumn<BaseReporteDisponibilidad, String> tc = new TableColumn(this.completarColumnasTabla().get(i));
            tc.setCellValueFactory(new PropertyValueFactory<BaseReporteDisponibilidad,String>(this.completarColumnasTabla().get(i)));
            System.out.println("TC: "+this.completarColumnasTabla().get(i));
            ReportesTableView.getColumns().add(tc);
        }
        
        ObservableList<BaseReporteDisponibilidad> list = FXCollections.observableArrayList();
        //mostrar tabla en app
        ArrayList<Recurso> elements= this.recursos.get("Reporte Disponibilidad").getAll();
        for (int i = 0; i < elements.size(); i++)
        {
            BaseReporteDisponibilidad aux = (BaseReporteDisponibilidad)elements.get(i);
            list.add(aux);
            System.out.println("E: "+aux.centro_nombre);
        }

        ReportesTableView.getSelectionModel().setCellSelectionEnabled(true);
        ReportesTableView.setItems(list);
        
        ReportesTableView.setPrefSize(panelTabla.getWidth(), panelTabla.getHeight());
        
        panelTabla.setTopAnchor(ReportesTableView, 0.0);
        panelTabla.setLeftAnchor(ReportesTableView, 0.0);
        panelTabla.setRightAnchor(ReportesTableView, 0.0);

        panelTabla.getChildren().clear();
        panelTabla.getChildren().add(ReportesTableView);
        
        return false;
    }
    
}