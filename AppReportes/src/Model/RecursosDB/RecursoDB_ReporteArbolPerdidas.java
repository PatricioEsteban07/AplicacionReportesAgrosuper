/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model.RecursosDB;

import Model.CommandNames;
import Model.LocalDB;
import Model.Reportes.BaseReporteArbolPerdidas;
import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.control.Alert;

/**
 * Clase que trabaja con la base de datos para generar el reporte Árbol Pérdidas.
 * @author Patricio
 */
public class RecursoDB_ReporteArbolPerdidas extends RecursoDB
{
    
    public RecursoDB_ReporteArbolPerdidas(LocalDB db)
    {
        super("Reporte Árbol Pérdidas","{call sp_reporte_arbol_perdidas(?,?) }",db);
    }
    
    /**
     * Método que se encarga de llamar al método prepareCall() para ejecutar el procedimiento almacenado de la base de datos 
     * para el reporte en específico. El resultado de trata y se guarda en un listado en el orden que se necesita. Finalmente 
     * este mpetodo retorna el listado de elementos correspondiente al resultado del procedimiento almacenado.
     * @param fechaInicio contiene la fecha inicio para el llamado al procedimiento almacenado en la base de datos.
     * @param fechaFin contiene la fecha término para el llamado al procedimiento almacenado en la base de datos.
     * @return un listado de elementos resultante del llamado del procedimiento almacenado, o null en otro caso.
     */
    public ArrayList<String> procedimientoAlmacenado(String fechaInicio, String fechaFin)
    {
        try
        {
            System.out.println("FI: "+fechaInicio+" / FF: "+fechaFin);
            this.db.connect();
            CallableStatement cs = this.db.conn.prepareCall(this.query);
            cs.setString(1, fechaInicio);
            cs.setString(2, fechaFin);
            if(!cs.execute())
            {
                CommandNames.generaMensaje("Información de Aplicación", Alert.AlertType.ERROR, "Error del Sistema", 
                    "Hubo un problema al ejecutar el procedimiento en la base de datos.");
                System.out.println("error con ejecutar SP RecursoDB reporte arbol perdidas :C");
                return null;
            }
            ResultSet rs = cs.getResultSet();
            int ctRow=0;
            ArrayList<String> resultados = new ArrayList<>();
            while (rs.next())
            {
                String aux = (ctRow++)+";"+rs.getString("mes")+";"+rs.getString("semana")+";"+
                        rs.getString("sector_nombre")+";"+rs.getString("tipoCliente")+";"+
                        rs.getString("centro_id")+";"+rs.getString("centro_nombre")+";"+rs.getString("agrupado_id")+";"+
                        rs.getString("agrupado_nombre")+";"+rs.getString("n2_nombre")+";"+rs.getString("Pedido_Kg")+";"+
                        rs.getString("Factura_Kg")+";"+rs.getString("Demanda_Kg")+";"+rs.getString("NS_Kg")+";"+
                        rs.getString("Faltante_Kg")+";"+rs.getString("Sobrefactura_Kg")+";"+rs.getString("PP_Neto")+";"+
                        rs.getString("Faltante_Neto")+";"+rs.getString("Pedido_Cj")+";"+rs.getString("Factura_Cj")+";"+
                        rs.getString("Demanda_Cj")+";"+rs.getString("NS_Cj")+";"+rs.getString("Sobrefactura_Cj")+";"+
                        rs.getString("Faltante_Cj")+";"+rs.getString("Disp_Pedido_Cj")+";"+rs.getString("Disp_Faltante_Cj")+";"+
                        rs.getString("Disp_Pedido_Kg")+";"+rs.getString("Disp_Faltante_Kg")+";"+
                        rs.getString("Factura_Faltante_Kg")+";"+rs.getString("Factura_Faltante_Cj")+";"+
                        rs.getString("Pedido_Neto")+";"+rs.getString("Año")+";"+rs.getString("semanaAño");
                resultados.add(aux);
                this.datos.add(new BaseReporteArbolPerdidas(aux));
            }
            if(ctRow==0)
            {
                CommandNames.generaMensaje("Información de Aplicación", Alert.AlertType.ERROR, "Error del Sistema", 
                    "Hubo un problema al ejecutar el procedimiento en la base de datos.");
                System.out.println("OJO SE CAE DESDE RECURSODB-SP");
            }
            this.db.close();
            return resultados;
        }
        catch (SQLException | ClassNotFoundException ex)
        {
            CommandNames.generaMensaje("Información de Aplicación", Alert.AlertType.ERROR, "Error del Sistema", 
                "Hubo un problema al ejecutar el procedimiento en la base de datos. El error es el siguiente: "+ex);
            Logger.getLogger(RecursoDB_ReporteArbolPerdidas.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }
    
}