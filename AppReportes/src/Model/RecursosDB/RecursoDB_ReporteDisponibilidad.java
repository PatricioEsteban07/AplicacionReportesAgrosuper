/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model.RecursosDB;

import Model.Agrupado;
import Model.LocalDB;
import Model.Reportes.BaseReporteDisponibilidad;
import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Patricio
 */
public class RecursoDB_ReporteDisponibilidad extends RecursoDB
{
    
    public RecursoDB_ReporteDisponibilidad(LocalDB db)
    {
        super("Reporte Disponibilidad","{call sp_generate_reporte_disponibilidad(?,?) }",db);
    }
    
    public ArrayList<String> procedimientoAlmacenado(String fechaInicio, String fechaFin) throws SQLException, ClassNotFoundException
    {
        connect();
        // Step-2: identify the stored procedure (viene en procedure, 
        //cuidar que venga en formato "{ call simpleproc(?) }")
        // Step-3: prepare the callable statement
        CallableStatement cs = this.conn.prepareCall(this.query);
        cs.setString(1, fechaInicio);
        cs.setString(2, fechaFin);
        // Step-4: register output parameters ...
        // Step-5: execute the stored procedures: proc3
        if(!cs.execute())
        {
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
        close();
        return resultados;
    }

    @Override
    public boolean obtenerDatos()
    {
        try
        {
            this.connect();
        }
        catch (SQLException | ClassNotFoundException ex)
        {
            Logger.getLogger(RecursoDB_ReporteDisponibilidad.class.getName()).log(Level.SEVERE, null, ex);
        }

        try
        {
            ResultSet result = super.executeQuery();

            //recorrer result para crear objetos
            while (result != null && result.next())
            {
                String idAux = result.getString("id");
                String nombreAux = result.getString("nombre");
                if(!this.db.agrupados.containsKey(idAux))
                {
                    Agrupado aux = new Agrupado(idAux, nombreAux);
                    this.add(aux);
                    this.db.agrupados.put(idAux, aux);
                    System.out.println("Agrupado: "+idAux);
                }
                else
                {
                    this.add(this.db.agrupados.get(idAux));
                }
            }
            this.close();
            return true;
        }
        catch (SQLException ex)
        {
            Logger.getLogger(RecursoDB_ReporteDisponibilidad.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }
}