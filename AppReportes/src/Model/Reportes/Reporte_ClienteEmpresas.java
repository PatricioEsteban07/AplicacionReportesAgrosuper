/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model.Reportes;

import Model.GeneradoresExcel.GeneradorExcel_Clientes;
import Model.GeneradoresExcel.GeneradorExcel_Empresas;
import Model.RecursosDB.RecursoDB;
import Model.RecursosDB.RecursoDB_Clientes;
import Model.RecursosDB.RecursoDB_Empresas;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Patricio
 */
public class Reporte_ClienteEmpresas extends Reporte
{
    
    public Reporte_ClienteEmpresas()
    {
        super();
        this.nombre="Reporte de Asignación Cliente-Empresas";
        this.recursos.put("Clientes",new RecursoDB_Clientes());
        this.recursos.put("Empresas",new RecursoDB_Empresas());
       // this.recursos.put("Clientes-Empresas",null);
    }

    @Override
    public boolean generarRecursos()
    {
        ArrayList<String> nameRecursos=new ArrayList<>();
        nameRecursos.add("Clientes");
        nameRecursos.add("Empresas");
        nameRecursos.add("Reporte Clientes-Empresas");
        int i=0;
        
        for (String key : this.recursos.keySet())
        {
            RecursoDB consulta=this.recursos.get(key);
            //validar si los datos ya existen en otro lado !
            if(!consulta.obtenerDatos())
            {
                System.out.println("ERROR: problemas al momento de obtener datos de la DB.");
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean generarExcel()
    {
        this.generadorExcel.put(this.recursos.get("Clientes").nombre, new GeneradorExcel_Clientes());
        this.generadorExcel.put(this.recursos.get("Empresas").nombre, new GeneradorExcel_Empresas());
        try
        {
            //this.generadorExcel.put(this.recursos.get(2).nombre, new GeneradorExcel_Empresas_Cliente());
            HashMap<String,RecursoDB> aux=new HashMap<>();
            aux.put("Clientes",this.recursos.get("Clientes"));
            if(!this.generadorExcel.get("Clientes").generarArchivo(aux))
            {
                System.out.println("ERROR: problema generando archivos excel clientes :c");
            }
            aux=new HashMap<>();
            aux.put("Empresas",this.recursos.get("Empresas"));
            if(!this.generadorExcel.get("Empresas").generarArchivo(aux))
            {
                System.out.println("ERROR: problema generando archivos excel empresas :c");
            }
        }
        catch (IOException ex)
        {
            Logger.getLogger(Reporte_ClienteEmpresas.class.getName()).log(Level.SEVERE, null, ex);
        }
        return true;
    }
    
}