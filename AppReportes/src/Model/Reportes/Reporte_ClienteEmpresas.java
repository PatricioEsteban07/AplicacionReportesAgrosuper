/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model.Reportes;

import Model.GeneradoresExcel.GeneradorExcel_ClienteEmpresas;
import Model.GeneradoresExcel.GeneradorExcel_Clientes;
import Model.GeneradoresExcel.GeneradorExcel_Empresas;
import Model.RecursosDB.RecursoDB;
import Model.RecursosDB.RecursoDB_ClienteEmpresas;
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
        super("Reporte de Asignación Cliente-Empresas");
        this.recursos.put("Clientes",new RecursoDB_Clientes());
        this.recursos.put("Empresas",new RecursoDB_Empresas());
        this.recursos.put("Cliente-Empresas",new RecursoDB_ClienteEmpresas());
        this.generarFiltrosBaseCustom();
    }

    @Override
    public boolean generarRecursos()
    {
        ArrayList<String> nameRecursos=new ArrayList<>();
        nameRecursos.add("Clientes");
        nameRecursos.add("Empresas");
        nameRecursos.add("Cliente-Empresas");
        int i=0;
        
        HashMap<String,RecursoDB> recursosClienteEmpresa = new HashMap<>();
        recursosClienteEmpresa.put("Clientes", this.recursos.get("Clientes"));
        recursosClienteEmpresa.put("Empresas", this.recursos.get("Empresas"));
        
        if( !generarRecurso(this.recursos.get("Clientes"),null) || !generarRecurso(this.recursos.get("Empresas"),null)
                || !generarRecurso(this.recursos.get("Cliente-Empresas"),recursosClienteEmpresa) )
        {
            return false;
        }
        return true;
    }
    
    public boolean generarRecurso(RecursoDB consulta, HashMap<String,RecursoDB> resources)
    {
        if(!consulta.obtenerDatos(resources))
        {
            System.out.println("ERROR: problemas al momento de obtener datos de la DB.");
            return false;
        }
        return true;
    }

    @Override
    public boolean generarExcel()
    {
        this.generadorExcel.put(this.recursos.get("Clientes").nombre, new GeneradorExcel_Clientes());
        this.generadorExcel.put(this.recursos.get("Empresas").nombre, new GeneradorExcel_Empresas());
        this.generadorExcel.put(this.recursos.get("Cliente-Empresas").nombre, new GeneradorExcel_ClienteEmpresas());
        try
        {
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
            aux=new HashMap<>();
            aux.put("Clientes",this.recursos.get("Cliente"));
            aux.put("Empresas",this.recursos.get("Empresas"));
            if(!this.generadorExcel.get("Cliente-Empresas").generarArchivo(aux))
            {
                System.out.println("ERROR: problema generando archivos excel final :c");
            }
        }
        catch (IOException ex)
        {
            Logger.getLogger(Reporte_ClienteEmpresas.class.getName()).log(Level.SEVERE, null, ex);
        }
        return true;
    }

    @Override
    public boolean generarFiltrosBaseCustom()
    {
        //crear todos los filtros vacios
        return generarFiltrosBase();
    }

    @Override
    public ArrayList<String> completarColumnasTabla()
    {
        ArrayList<String> columnas=new ArrayList<>();
        columnas.add("cliente_id");
        columnas.add("cliente_nombre");
        columnas.add("cliente_apellido");
        columnas.add("cliente_edad");
        columnas.add("cliente_sexo");
        columnas.add("cliente_descripcion");
        columnas.add("empresa_id");
        columnas.add("empresa_nombre");
        columnas.add("empresa_direccion");
        columnas.add("empresa_descripcion");
        return columnas;
    }
    
}