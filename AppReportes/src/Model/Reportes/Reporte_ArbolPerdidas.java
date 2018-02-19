/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model.Reportes;

import Model.Filtros.Filtro_Fecha;
import Model.GeneradoresExcel.GeneradorExcel_ReporteArbolPerdidas;
import Model.LocalDB;
import Model.Recurso;
import Model.RecursosDB.RecursoDB_ReporteArbolPerdidas;
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
public class Reporte_ArbolPerdidas extends Reporte
{
    private HashMap<String, BaseReporteDisponibilidad> elementos;

    public Reporte_ArbolPerdidas(LocalDB db)
    {
        super("Reporte Árbol Pérdidas",db);
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
        this.recursos.put("Reporte Árbol Pérdidas", new RecursoDB_ReporteArbolPerdidas(this.db));
        
        this.generarFiltrosBaseCustom();
    }

    @Override
    public boolean generarExcel()
    {
        this.generadorExcel.put(this.nombre, new GeneradorExcel_ReporteArbolPerdidas(completarColumnasTabla()));
        try
        {
            if(!this.generadorExcel.get(this.nombre).generarArchivo(this.recursos))
            {
                System.out.println("ERROR: problema generando archivos excel Reporte Árbol Pérdidas");
                return false;
            }
            return true;
        }
        catch (IOException ex)
        {
            Logger.getLogger(Reporte_ArbolPerdidas.class.getName()).log(Level.SEVERE, null, ex);
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
        this.filtros.remove("Filtro_Canal");
        this.filtros.remove("Filtro_CargoRRHH");
        return true;
    }

    @Override
    public ArrayList<String> completarColumnasTabla()
    {
        ArrayList<String> columnas=new ArrayList<>();
        columnas.add("mes");
        columnas.add("semana");
        columnas.add("sector_nombre");
        columnas.add("tipoCliente");
        columnas.add("centro_id");
        columnas.add("centro_nombre");
        columnas.add("agrupado_id");
        columnas.add("agrupado_nombre");
        columnas.add("n2_nombre");
        columnas.add("Pedido_Kg");
        columnas.add("Factura_Kg");
        columnas.add("Demanda_Kg");
        columnas.add("NS_Kg");
        columnas.add("Faltante_Kg");
        columnas.add("faltanteKg");
        columnas.add("Sobrefactura_Kg");
        columnas.add("PP_Neto");
        columnas.add("Faltante_Neto");
        columnas.add("Pedido_Cj");
        columnas.add("Factura_Cj");
        columnas.add("Demanda_Cj");
        columnas.add("NS_Cj");
        columnas.add("Sobrefactura_Cj");
        columnas.add("Faltante_Cj");
        columnas.add("Disp_Pedido_Cj");
        columnas.add("Disp_Faltante_Cj");
        columnas.add("Disp_Pedido_Kg");
        columnas.add("Disp_Faltante_Kg");
        columnas.add("Factura_Faltante_Kg");
        columnas.add("Factura_Faltante_Cj");
        columnas.add("Pedido_Neto");
        columnas.add("Anio");
        columnas.add("semanaAnio");
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
            
            ArrayList<String> resultados = ((RecursoDB_ReporteArbolPerdidas)this.recursos.get(this.nombre)).procedimientoAlmacenado(fechaInicio,fechaFin);
            if(resultados==null || resultados.isEmpty())
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
            Logger.getLogger(Reporte_ArbolPerdidas.class.getName()).log(Level.SEVERE, null, ex);
        }
        catch (ClassNotFoundException ex)
        {
            Logger.getLogger(Reporte_ArbolPerdidas.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    @Override
    public boolean desplegarInfoExcelApp(TableView<Recurso> asdf, AnchorPane panelTabla)
    {
        TableView<BaseReporteArbolPerdidas> ReportesTableView = new TableView<>();
        for (int i = 0; i < this.completarColumnasTabla().size(); i++)
        {
            TableColumn<BaseReporteArbolPerdidas, String> tc = new TableColumn(this.completarColumnasTabla().get(i));
            tc.setCellValueFactory(new PropertyValueFactory<BaseReporteArbolPerdidas,String>(this.completarColumnasTabla().get(i)));
            System.out.println("TC: "+this.completarColumnasTabla().get(i));
            ReportesTableView.getColumns().add(tc);
        }
        
        ObservableList<BaseReporteArbolPerdidas> list = FXCollections.observableArrayList();
        //mostrar tabla en app
        ArrayList<Recurso> elements= this.recursos.get(this.nombre).getAll();
        for (int i = 0; i < elements.size(); i++)
        {
            BaseReporteArbolPerdidas aux = (BaseReporteArbolPerdidas)elements.get(i);
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