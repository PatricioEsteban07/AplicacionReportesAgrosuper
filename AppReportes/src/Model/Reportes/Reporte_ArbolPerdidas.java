/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model.Reportes;

import Model.CommandNames;
import Model.Filtros.Filtro_Fecha;
import Model.GeneradoresExcel.GeneradorExcel_ReporteArbolPerdidas;
import Model.LocalDB;
import Model.RecursosDB.RecursoDB_ReporteArbolPerdidas;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import javafx.scene.control.Alert;

/**
 * Clase encargada de todo el proceso de generación del reporte Árbol Pérdidas.
 * @author Patricio
 */
public class Reporte_ArbolPerdidas extends Reporte
{
    private HashMap<String, BaseReporteDisponibilidad> elementos;

    public Reporte_ArbolPerdidas(LocalDB db)
    {
        super("Reporte Árbol Pérdidas",db);
        this.recursos.put("Reporte Árbol Pérdidas", new RecursoDB_ReporteArbolPerdidas(this.db));
        
        this.generarFiltrosBaseCustom();
    }

    @Override
    public boolean generarExcel()
    {
        if(this.generadorExcel==null)
            return false;
        this.generadorExcel.put(this.nombre, new GeneradorExcel_ReporteArbolPerdidas(completarColumnasTabla()));
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
                System.out.println("ERROR: problema generando archivos excel Reporte Árbol Pérdidas. El error es el siguiente: "+ex);
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
        columnas.add("Mes natural");
        columnas.add("sem");
        columnas.add("Sector");
        columnas.add("Tipo de cliente");
        columnas.add("Cod Centro");
        columnas.add("Centro");
        columnas.add("Cod Agrup");
        columnas.add("Producto Agrupado");
        columnas.add("Nivel 2");
        columnas.add("Pedido (kg)");
        columnas.add("Factura (kg)");
        columnas.add("Demanda (kg)");
        columnas.add("NS (Kg)");
        columnas.add("Faltante (kg)");
        columnas.add("Sobrefactura (kg)");
        columnas.add("PP ($/Kg)");
        columnas.add("Faltante ($)");
        columnas.add("Pedido (Cj)");
        columnas.add("Factura (Cj)");
        columnas.add("Demanda (Cj)");
        columnas.add("NS (Cj)");
        columnas.add("Sobrefactura (Cj)");
        columnas.add("Faltante (Cj)");
        columnas.add("Pedido CJ Disp");
        columnas.add("Faltante CJ Disp");
        columnas.add("Pedido KG Disp");
        columnas.add("Faltante KG Disp");
        columnas.add("Factura Faltante KG");
        columnas.add("Factura Faltante CJ");
        columnas.add("Pedido Neto");
        columnas.add("Año");
        columnas.add("sem-Año");
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
        ArrayList<String> resultados = ((RecursoDB_ReporteArbolPerdidas)this.recursos.get(this.nombre)).procedimientoAlmacenado(fechaInicio,fechaFin);
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