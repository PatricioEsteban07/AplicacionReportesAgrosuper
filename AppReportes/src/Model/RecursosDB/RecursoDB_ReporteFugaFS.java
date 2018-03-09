/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model.RecursosDB;

import Model.CommandNames;
import Model.LocalDB;
import Model.Reportes.BaseReporteFugaFS;
import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.control.Alert;

/**
 * Clase que trabaja con la base de datos para generar el reporte Fuga Food Service.
 * @author Patricio
 */
public class RecursoDB_ReporteFugaFS extends RecursoDB
{
    
    public RecursoDB_ReporteFugaFS(LocalDB db)
    {
        super("Reporte Fuga FS","{call sp_reporte_fuga_fs(?,?) }",db);
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
                System.out.println("error con ejecutar SP RecursoDB reporte fuga FS :C");
                return null;
            }
            ResultSet rs = cs.getResultSet();
            int ctRow=0;
            ArrayList<String> resultados = new ArrayList<>();
            while (rs.next())
            {
                String aux = (ctRow++)+";"+rs.getString("clienteLocal_id")+";"+
                        rs.getString("clienteLocal_nombre")+";"+rs.getString("comuna")+";"+
                        rs.getString("direccion")+";"+rs.getString("cadena")+";"+rs.getString("categoriaCliente")+";"+
                        rs.getString("subcategoriaCliente")+";"+rs.getString("sucursal")+";"+rs.getString("zonaVenta")+";"+
                        rs.getString("supervisor")+";"+rs.getString("preventa")+";"+rs.getString("kam")+";"+
                        rs.getString("centralizado")+";"+rs.getString("agcnc")+";"+rs.getString("año")+";"+
                        rs.getString("mes")+";"+rs.getString("ejecutivo")+";"+rs.getString("diaLlamado")+";"+
                        rs.getString("tipoClub")+";"+rs.getString("categoriaClub")+";"+rs.getString("segmentoClub")+";"+
                        rs.getString("canje")+";"+rs.getString("clienteFugado")+";"+rs.getString("clienteHistorico")+";"+
                        rs.getString("clienteNuevo")+";"+rs.getString("clienteVigente")+";"+rs.getString("clienteRecuperado")+";"+
                        rs.getString("clienteFugaNeto")+";"+rs.getString("clienteCrecimientoNeto")+";"+rs.getString("clienteRecuperadoKg")+";"+
                        rs.getString("clienteFugadoKg")+";"+rs.getString("clienteCrecimientoKg")+";"+rs.getString("brechaFuga_neto")+";"+
                        rs.getString("brechaCrecimiento_neto")+";"+rs.getString("brecha_neto")+";"+rs.getString("brechaFuga_Kg")+";"+
                        rs.getString("brechaCrecimiento_Kg")+";"+rs.getString("brecha_Kg")+";"+rs.getString("venta_clienteFugado")+";"+
                        rs.getString("venta_clienteHistorico")+";"+rs.getString("venta_clientesNuevo")+";"+
                        rs.getString("venta_clientesVigente")+";"+rs.getString("fugados_Kg")+";"+rs.getString("historico_Kg")+";"+
                        rs.getString("nuevos_Kg")+";"+rs.getString("vigentes_Kg")+";"+rs.getString("tipoCall")+";"+
                        rs.getString("kamjr")+";"+rs.getString("jefeVentas");
                resultados.add(aux);
                this.datos.add(new BaseReporteFugaFS(aux));
            }
            if(ctRow==0)
            {
                CommandNames.generaMensaje("Información de Aplicación", Alert.AlertType.ERROR, "Error del Sistema", 
                    "Hubo un problema al ejecutar el procedimiento en la base de datos.");
                System.out.println("OJO SE CAE DESDE RECURSODB-FugaFS");
            }
            this.db.close();
            return resultados;
        }
        catch (SQLException | ClassNotFoundException ex)
        {
            CommandNames.generaMensaje("Información de Aplicación", Alert.AlertType.ERROR, "Error del Sistema", 
                "Hubo un problema al ejecutar el procedimiento en la base de datos. El error es el siguiente: "+ex);
            Logger.getLogger(RecursoDB_ReporteFugaFS.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }
    
}