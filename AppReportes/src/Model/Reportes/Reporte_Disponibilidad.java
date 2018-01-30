/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model.Reportes;

import Model.RecursosDB.ReporteDisponibilidad.RecursoDB_Materiales;
import Model.RecursosDB.ReporteDisponibilidad.RecursoDB_TablaPedidos;
import java.util.ArrayList;

/**
 *
 * @author Patricio
 */
public class Reporte_Disponibilidad extends Reporte
{

    public Reporte_Disponibilidad()
    {
        super("Reporte de Disponibilidad");
        this.recursos.put("Pedidos", new RecursoDB_TablaPedidos());
  //      this.recursos.put("Materiales",new RecursoDB_Materiales());
  //      this.recursos.put("Pedidos",new RecursoDB_Pedidos());
   //     this.recursos.put("Despachos",new RecursoDB_Despachos());
   //     this.recursos.put("Stock",new RecursoDB_Stock());
  //      this.recursos.put("Oficinas",new RecursoDB_Oficinas());
        this.generarFiltrosBaseCustom();
    }

    @Override
    public boolean generarRecursos()
    {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean generarExcel()
    {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean generarFiltrosBaseCustom()
    {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public ArrayList<String> completarColumnasTabla()
    {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
