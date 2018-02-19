/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model.Reportes;

import Model.CommandNames;
import Model.Filtros.Filtro_Fecha;
import Model.GeneradoresExcel.GeneradorExcel_ReporteDisponibilidad;
import Model.LocalDB;
import Model.Recurso;
import Model.RecursosDB.RecursoDB_ReporteDisponibilidad;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
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
        super("Reporte Disponibilidad",db);
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
    public boolean generarExcel()
    {
        CommandNames.generaMensaje("X", Alert.AlertType.INFORMATION, "X",
                    "DENTRO DE GENERAR EXCEL");
        if(this.generadorExcel==null)
            return false;
        this.generadorExcel.put(this.nombre, new GeneradorExcel_ReporteDisponibilidad(completarColumnasTabla()));
        try
        {
            if(!this.generadorExcel.get(this.nombre).generarArchivo(this.recursos))
            {        
                CommandNames.generaMensaje("Error de Sistema", Alert.AlertType.ERROR, "Error generando Reporte",
                    "Hubo problemas para generar el reporte.");
                System.out.println("ERROR: problema generando archivos excel Reporte Disponibilidad");
                return false;
            }        
            CommandNames.generaMensaje("Aviso de Reporte", Alert.AlertType.INFORMATION, "Reporte generado exitosamente",
                    "El reporte ha sido generado con el nombre '"+this.nombre+"', el cual está ubicado en el Escritorio. Por recomendación"
            + " cambiar el nombre del archivo o ubicarlo en alguna carpeta.");
            return true;
        }
        catch (IOException ex)
        {
            CommandNames.generaMensaje("Error de Sistema", Alert.AlertType.ERROR, "Error generando Reporte",
                "Hubo problemas para generar el reporte.");
                System.out.println("ERROR: problema generando archivos excel Reporte Disponibilidad. El error es el siguiente: "+ex);
            return false;
        }
    }

    @Override
    public boolean generarFiltrosBaseCustom()
    {    
        //crear todos los filtros vacios
        //filtro no utilizado, Filtro_Cliente, Filtro_Sucursal
        generarFiltrosBase();
        this.filtros.remove("Filtro_Cliente");
        this.filtros.remove("Filtro_Sucursal");
        this.filtros.remove("Filtro_Canal");
        this.filtros.remove("Filtro_Zona");
        this.filtros.remove("Filtro_CargoRRHH");
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
        ArrayList<String> resultados = ((RecursoDB_ReporteDisponibilidad)this.recursos.get(this.nombre)).procedimientoAlmacenado(fechaInicio,fechaFin);
                if(resultados==null || resultados.isEmpty())
        {
            CommandNames.generaMensaje("Información de Aplicación", Alert.AlertType.INFORMATION, "Información del Sistema", 
                "No existe información asociada al período seleccionado para el reporte.");
            return false;
        }
        if(!generarExcel())
        {
            CommandNames.generaMensaje("Información de Aplicación", Alert.AlertType.INFORMATION, "Información del Sistema", 
                "Hubo problemas para generar el excel.");
            System.out.println("No existen datos o algo malo paso :c");
            return false;
        }
        //trabajar con arraylist -> separados elementos por ;
        return true;
    }

    @Override
    public boolean desplegarInfoExcelApp(TableView<Recurso> asdf, AnchorPane panelTabla)
    {
        TableView<BaseReporteDisponibilidad> ReportesTableView = new TableView<>();
        for (int i = 0; i < this.completarColumnasTabla().size(); i++)
        {
            TableColumn<BaseReporteDisponibilidad, String> tc = new TableColumn(this.completarColumnasTabla().get(i));
            tc.setCellValueFactory(new PropertyValueFactory<BaseReporteDisponibilidad,String>(this.completarColumnasTabla().get(i)));
            ReportesTableView.getColumns().add(tc);
        }
        
        ObservableList<BaseReporteDisponibilidad> list = FXCollections.observableArrayList();
        //mostrar tabla en app
        ArrayList<Recurso> elements= this.recursos.get(this.nombre).getAll();
        for (int i = 0; i < elements.size(); i++)
        {
            BaseReporteDisponibilidad aux = (BaseReporteDisponibilidad)elements.get(i);
            list.add(aux);
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