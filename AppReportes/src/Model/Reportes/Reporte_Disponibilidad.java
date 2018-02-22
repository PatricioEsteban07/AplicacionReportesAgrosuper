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
        this.recursos.put("Reporte Disponibilidad", new RecursoDB_ReporteDisponibilidad(this.db));
        
        this.generarFiltrosBaseCustom();
    }

    @Override
    public boolean generarExcel()
    {
        if(this.generadorExcel==null)
            return false;
        this.generadorExcel.put(this.nombre, new GeneradorExcel_ReporteDisponibilidad(completarColumnasTabla()));
        try
        {
            if(!this.generadorExcel.get(this.nombre).generarArchivo(this.recursos))
            {        
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
        columnas.add("Cod Centro");
        columnas.add("Centro");
        columnas.add("Cod Sector");
        columnas.add("Sector");
        columnas.add("Cod Agrupacion");
        columnas.add("Producto Agrupado");
        columnas.add("Fecha de entrega");
        columnas.add("Pedido CJ");
        columnas.add("Despacho CJ");
        columnas.add("Disponible CJ");
        columnas.add("Pedido KG");
        columnas.add("Pedido Neto");
        columnas.add("Disponible KG");
        columnas.add("Faltante CJ");
        columnas.add("Faltante KG");
        columnas.add("Semana");
        columnas.add("Sobrante CJ");
        columnas.add("Sobrante KG");
        columnas.add("Faltante Despacho CJ");
        columnas.add("Faltante Ajustado CJ");
        columnas.add("Faltante Despacho KG");
        columnas.add("Faltante Ajustado KG");
        columnas.add("Dia semana");
        columnas.add("Año");
        return columnas;
    }

    @Override
    public boolean generarReporte()
    {
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
            return false;
        }
        if(!generarExcel())
        {
            CommandNames.generaMensaje("Información de Aplicación", Alert.AlertType.INFORMATION, "Información del Sistema", 
                "Hubo problemas para generar el excel.");
            System.out.println("No existen datos o algo malo paso :c");
            return false;
        }
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