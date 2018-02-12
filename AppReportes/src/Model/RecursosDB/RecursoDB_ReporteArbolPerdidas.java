/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model.RecursosDB;

import Model.Agrupado;
import Model.LocalDB;
import Model.Reportes.BaseReporteArbolPerdidas;
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
public class RecursoDB_ReporteArbolPerdidas extends RecursoDB
{
    
    public RecursoDB_ReporteArbolPerdidas(LocalDB db)
    {
        super("Reporte Árbol Pérdidas","{call sp_reporte_arbol_perdidas(?,?) }",db);
    }
    
    public ArrayList<String> procedimientoAlmacenado(String fechaInicio, String fechaFin) throws SQLException, ClassNotFoundException
    {
        System.out.println("FI: "+fechaInicio+" / FF: "+fechaFin);
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
            System.out.println("OJO SE CAE DESDE RECURSODB-SP");
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
            Logger.getLogger(RecursoDB_ReporteArbolPerdidas.class.getName()).log(Level.SEVERE, null, ex);
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
            Logger.getLogger(RecursoDB_ReporteArbolPerdidas.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }
}