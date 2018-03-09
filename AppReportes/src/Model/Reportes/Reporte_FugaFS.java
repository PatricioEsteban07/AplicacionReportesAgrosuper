/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model.Reportes;

import Model.CommandNames;
import Model.Filtros.Filtro_Fecha;
import Model.GeneradoresExcel.GeneradorExcel_ReporteFugaFS;
import Model.LocalDB;
import Model.RecursosDB.RecursoDB_ReporteFugaFS;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import javafx.scene.control.Alert;

/**
 * Clase encargada de todo el proceso de generación del reporte Fuga Food Service.
 * @author Patricio
 */
public class Reporte_FugaFS extends Reporte
{
    private HashMap<String, BaseReporteDisponibilidad> elementos;

    public Reporte_FugaFS(LocalDB db)
    {
        super("Reporte Fuga FS",db);
        this.recursos.put("Reporte Fuga FS", new RecursoDB_ReporteFugaFS(this.db));
        
        this.generarFiltrosBaseCustom();
    }

    @Override
    public boolean generarExcel()
    {
        if(this.generadorExcel==null)
            return false;
        this.generadorExcel.put(this.nombre, new GeneradorExcel_ReporteFugaFS(completarColumnasTabla()));
        try
        {
            if(!this.generadorExcel.get(this.nombre).generarArchivo(this.recursos,this.getFileDir()))
            {        
                CommandNames.generaMensaje("Error de Sistema", Alert.AlertType.ERROR, "Error generando Reporte",
                    "Hubo problemas para generar el reporte.");
                System.out.println("ERROR: problema generando archivos excel Reporte Árbol Pérdidas");
                return false;
            }        
            CommandNames.generaMensaje("Aviso de Reporte", Alert.AlertType.INFORMATION, "Reporte generado exitosamente",
                    "El reporte ha sido generado con el nombre '"+this.nombre+"', el cual está ubicado en '"+this.getFileDir()+"' . Por recomendación"
            + " cambiar el nombre del archivo o ubicarlo en alguna carpeta.");
            return true;
        }
        catch (IOException ex)
        {
            CommandNames.generaMensaje("Error de Sistema", Alert.AlertType.ERROR, "Error generando Reporte",
                "Hubo problemas para generar el reporte.");
                System.out.println("ERROR: problema generando archivos excel Reporte Fuga FS. El error es el siguiente: "+ex);
            return false;
        }
    }

    @Override
    public boolean generarFiltrosBaseCustom()
    {    
        //crear todos los filtros vacios
        generarFiltrosBase();
        return true;
    }

    @Override
    public ArrayList<String> completarColumnasTabla()
    {
        ArrayList<String> columnas=new ArrayList<>();
        columnas.add("Local");
        columnas.add("Nombre Local");
        columnas.add("Comuna");
        columnas.add("Direccion");
        columnas.add("Cadena");
        columnas.add("Categoria_Cliente");
        columnas.add("Subtipo_Cliente");
        columnas.add("Sucursal");
        columnas.add("Zona_Vta");
        columnas.add("Supervisor");
        columnas.add("Preventa");
        columnas.add("KAM");
        columnas.add("Centralizado");
        columnas.add("AGCNC");
        columnas.add("Año");
        columnas.add("Mes");
        columnas.add("Ejecutivo");
        columnas.add("Dia_Llamado");
        columnas.add("Tipo_Club");
        columnas.add("Categoria_Club");
        columnas.add("Segmento_Club");
        columnas.add("Canje");
        columnas.add("Clte Fugado");
        columnas.add("Clte Hist");
        columnas.add("Clte Nuevos");
        columnas.add("Clte Vigentes");
        columnas.add("Cli_Recup Netos");
        columnas.add("Cli_Fuga_Netos");
        columnas.add("Cli_Crecim Ventas");
        columnas.add("Cli_Recup Kilos");
        columnas.add("Cli_Fuga_Kilos");
        columnas.add("Cli_Crecim Kilos");
        columnas.add("Brecha_Fuga Ventas");
        columnas.add("Brecha_Crecimiento Ventas");
        columnas.add("Brecha Ventas");
        columnas.add("Brecha_Fuga Kilos");
        columnas.add("Brecha_Crecimiento Kilos");
        columnas.add("Brecha Kilos");
        columnas.add("Vta Cltes Fugados*");
        columnas.add("Vta Cltes Hist");
        columnas.add("Vta Cltes Nuevos");
        columnas.add("Vta Cltes Vigentes");
        columnas.add("Kilos_Fugados");
        columnas.add("Kilos_Hist");
        columnas.add("Kilos_Nuevo");
        columnas.add("Kilos_Vig");
        columnas.add("Tipo_Call");
        columnas.add("KAM Jr");
        columnas.add("Jefe de Venta");
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
        ArrayList<String> resultados = ((RecursoDB_ReporteFugaFS)this.recursos.get(this.nombre)).procedimientoAlmacenado(fechaInicio,fechaFin);
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
        return true;
    }
    
}