/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model.RecursosDB;

import Model.CommandNames;
import Model.LocalDB;
import Model.Reportes.BaseReporteDisponibilidad;
import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.control.Alert;

/**
 *
 * @author Patricio
 */
public class RecursoDB_ReporteDisponibilidad extends RecursoDB
{
    
    public RecursoDB_ReporteDisponibilidad(LocalDB db)
    {
        super("Reporte Disponibilidad","{call sp_reporte_disponibilidad(?,?) }",db);
    }
    
    public ArrayList<String> procedimientoAlmacenado(String fechaInicio, String fechaFin)
    {
        try
        {
            System.out.println("FI: "+fechaInicio+" / FF: "+fechaFin);
            connect();
            CallableStatement cs = this.conn.prepareCall(this.query);
            cs.setString(1, fechaInicio);
            cs.setString(2, fechaFin);
            if(!cs.execute())
            {
                CommandNames.generaMensaje("Información de Aplicación", Alert.AlertType.ERROR, "Error del Sistema", 
                    "Hubo un problema al ejecutar el procedimiento en la base de datos.");
                System.out.println("error con ejecutar SP RecursoDB reporte disponibilidad:C");
                return null;
            }
            ResultSet rs = cs.getResultSet();
            int ctRow=0;
            ArrayList<String> resultados = new ArrayList<>();
            while (rs.next())
            {
                String aux = (ctRow++)+";"+rs.getString("centro_id")+";"+rs.getString("centro_nombre")+";"+
                        rs.getString("sector_id")+";"+rs.getString("sector_nombre")+";"+rs.getString("agrupado_id")+";"+
                        rs.getString("agrupado_nombre")+";"+rs.getString("fecha")+";"+rs.getString("pedido_Cj")+";"+
                        rs.getString("despacho_Cj")+";"+rs.getString("disponible_Cj")+";"+rs.getString("pedido_Kg")+";"+
                        rs.getString("pedido_neto")+";"+rs.getString("disponible_Kg")+";"+rs.getString("faltante_Cj")+";"+
                        rs.getString("faltante_Kg")+";"+rs.getString("semana")+";"+rs.getString("sobrante_Cj")+";"+
                        rs.getString("sobrante_Kg")+";"+rs.getString("faltanteDespacho_Cj")+";"+rs.getString("faltanteAjustado_Cj")+";"+
                        rs.getString("faltanteDespacho_Kg")+";"+rs.getString("faltanteAjustado_Kg")+";"+rs.getString("diaSemana")+";"+
                        rs.getString("año");
                resultados.add(aux);
                this.datos.add(new BaseReporteDisponibilidad(aux));
            }
            if(ctRow==0)
            {
                CommandNames.generaMensaje("Información de Aplicación", Alert.AlertType.INFORMATION, "Información del Sistema", 
                    "No existe información asociada al período seleccionado para el reporte.");
                return null;
            }
            close();
            return resultados;
        }
        catch (SQLException | ClassNotFoundException ex)
        {
            CommandNames.generaMensaje("Información de Aplicación", Alert.AlertType.ERROR, "Error del Sistema", 
                "Hubo un problema al ejecutar el procedimiento en la base de datos. El error es el siguiente: "+ex);
            Logger.getLogger(RecursoDB_ReporteDisponibilidad.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }
}