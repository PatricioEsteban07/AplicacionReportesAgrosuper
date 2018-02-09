/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model.GeneradoresExcel;

import Model.Despacho;
import Model.Material;
import Model.Pedido;
import Model.Recurso;
import Model.RecursosDB.RecursoDB;
import Model.Reportes.BaseReporteDisponibilidad;
import Model.Stock;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;


/**
 *
 * @author Patricio
 */
public class GeneradorExcel_ReporteDisponibilidad extends GeneradorExcel
{
    
    public GeneradorExcel_ReporteDisponibilidad()
    {
        super("Reporte Disponibilidad");
        this.columnas.add("centro_id");
        this.columnas.add("centro_nombre");
        this.columnas.add("sector_nombre");
        this.columnas.add("agrupado_id");
        this.columnas.add("agrupado_nombre");
        this.columnas.add("fecha");
        this.columnas.add("pedido_Cj");
        this.columnas.add("despacho_Cj");
        this.columnas.add("disponible_Cj");
        this.columnas.add("pedido_Kg");
        this.columnas.add("pedido_neto");
        this.columnas.add("disponible_Kg");
        this.columnas.add("faltante_Cj");
        this.columnas.add("faltante_Kg");
        this.columnas.add("semana");
        this.columnas.add("sobrante_Cj");
        this.columnas.add("sobrante_Kg");
        this.columnas.add("faltanteDespacho_Cj");
        this.columnas.add("faltanteAjustado_Cj");
        this.columnas.add("faltanteDespacho_Kg");
        this.columnas.add("faltanteAjustado_Kg");
        this.columnas.add("diaSemana");
        this.columnas.add("año");
    }

    @Override
    public boolean generarArchivo(HashMap<String,RecursoDB> resources) throws FileNotFoundException, IOException
    {
        HashMap<String, BaseReporteDisponibilidad> filas = new HashMap<>();
        
        //parte 1 : tabla básica
            //sacar centro (se hizo anteriormente)
            //sacar sector_material
            //sacar agrupacion material
            //sacar semana/diaSemana/año
        ArrayList<Recurso> recursosAux = resources.get("Pedidos").getAll();
        for (int i=0; i<recursosAux.size();i++)
        {
            Pedido p = ((Pedido)recursosAux.get(i));
            String preKey = p.centro.id+","+p.fechaEntrega+",";
            Iterator<String> productos = p.materiales.keySet().iterator();
            while(productos.hasNext()){
                String key = productos.next();
                Material m = p.materiales.get(key).material;
                if(!filas.containsKey(preKey+key))
                    filas.put(preKey+key, new BaseReporteDisponibilidad(p.centro.id,p.centro.nombre,m.sector.nombre,
                            m.tipoAgrupado.id,m.tipoAgrupado.nombre,p.fechaEntrega));
                
                filas.get(preKey+key).pedido_Cj+=p.materiales.get(key).cantidadCj;
                filas.get(preKey+key).pedido_Kg+=p.materiales.get(key).pesoKg;
                filas.get(preKey+key).pedido_neto+=p.materiales.get(key).pesoNeto;
            }    
        }
                
        recursosAux = resources.get("Stocks").getAll();
        for (int i=0; i<recursosAux.size();i++)
        {
            Stock s = ((Stock)recursosAux.get(i));
            String preKey = s.centro.id+","+s.fecha+",";
            String key = s.material.id;

            if(!filas.containsKey(preKey+key))
                filas.put(preKey+key, new BaseReporteDisponibilidad(s.centro.id,s.centro.nombre,s.material.sector.nombre,
                s.material.tipoAgrupado.id, s.material.tipoAgrupado.nombre,s.fecha));
            
            filas.get(preKey+key).stock_disponibilidadKg+=s.disponible;
            filas.get(preKey+key).stock_disponibleCj+=s.disponible/s.material.pesoCaja;
        }
        
        HashMap<String, Integer> despachoCjs = new HashMap<>();
        HashMap<String, Integer> despachoKgs = new HashMap<>();
                
        recursosAux = resources.get("Despachos").getAll();
        for (int i=0; i<recursosAux.size();i++)
        {
            Despacho d = ((Despacho)recursosAux.get(i));
            String preKey = d.centro.id+","+d.fecha+",";
            Iterator<String> productos = d.materiales.keySet().iterator();
            while(productos.hasNext()){
                String key = productos.next();
                if(!filas.containsKey(preKey+key))
                    filas.put(preKey+key, new BaseReporteDisponibilidad(d.centro.id,d.centro.nombre,
                            d.materiales.get(key).material.sector.nombre,d.materiales.get(key).material.tipoAgrupado.id,
                            d.materiales.get(key).material.tipoAgrupado.nombre, d.fecha));
                
                filas.get(preKey+key).despacho_Cj+=d.materiales.get(key).despachoCj;
                filas.get(preKey+key).despacho_Kg+=d.materiales.get(key).despachoKg;
            }    
        }       
        
        //generación de archivo excel base
        String rutaArchivo = System.getProperty("user.home")+"/Desktop/"+this.nombreTabla+".xlsx";
        File archivoXLS = new File(rutaArchivo);
        
        //Se crea el libro de excel usando el objeto de tipo Workbook
        XSSFWorkbook  libro = new XSSFWorkbook ();
        //Se inicializa el flujo de datos con el archivo xls
        FileOutputStream file = new FileOutputStream(archivoXLS);
        /*
        // Generate fonts
        HSSFFont headerFont  = createFont(libro,HSSFColor.WHITE.index, (short)12, );
        HSSFFont contentFont = createFont(libro,HSSFColor.BLACK.index, (short)10, Font.BOLDWEIGHT_NORMAL);

        // Generate styles
        HSSFCellStyle headerStyle  = createStyle(libro,headerFont,  HSSFCellStyle.ALIGN_CENTER, HSSFColor.BLUE_GREY.index,       true, HSSFColor.WHITE.index);
        HSSFCellStyle oddRowStyle  = createStyle(libro,contentFont, HSSFCellStyle.ALIGN_LEFT,   HSSFColor.WHITE.index, true, HSSFColor.GREY_80_PERCENT.index);
        HSSFCellStyle evenRowStyle = createStyle(libro,contentFont, HSSFCellStyle.ALIGN_LEFT,   HSSFColor.GREY_25_PERCENT.index, true, HSSFColor.GREY_80_PERCENT.index);
        */
        //Utilizamos la clase Sheet para crear una nueva hoja de trabajo dentro del libro que creamos anteriormente
        XSSFSheet  hoja = libro.createSheet(this.nombreTabla);
                
        //inicialiar fila de nombres de columnas
        System.out.println("Creando nombres de columnas...");        
        XSSFRow  fila = hoja.createRow( 0 );
        for (int i = 0; i < this.columnas.size(); i++)
        {
            XSSFCell  celda = fila.createCell(i);
//            celda.setCellStyle(headerStyle);
            celda.setCellValue(this.columnas.get(i));
            hoja.autoSizeColumn(i);
        }
        
        System.out.println("Rellenando tabla con valores...");
        int contReg=1;
        
        //se calculan sobrantes y faltantes
        Iterator<String> rows = filas.keySet().iterator();
        while(rows.hasNext()){
            
            XSSFRow filaAux = hoja.createRow(contReg);
                
            String key = rows.next();
            BaseReporteDisponibilidad row = filas.get(key);
            row.faltanteCj = (row.stock_disponibleCj>=row.pedido_Cj) ? 0 : row.pedido_Cj-row.stock_disponibleCj;
            row.faltanteKg = (row.stock_disponibilidadKg>=row.pedido_Kg) ? 0 : row.pedido_Kg-row.stock_disponibilidadKg;
            row.sobranteCj = (row.stock_disponibleCj>row.pedido_Cj) ? row.stock_disponibleCj-row.pedido_Cj : 0 ;
            row.sobranteKg = (row.stock_disponibilidadKg>row.pedido_Kg) ? row.stock_disponibilidadKg-row.pedido_Kg : 0 ;
            row.faltanteDespachoCj = (row.despacho_Cj<row.pedido_Cj) ? row.pedido_Cj-row.despacho_Cj : 0 ;
            row.faltanteAjustadoCj = (row.faltanteDespachoCj<row.faltanteCj) ? row.faltanteDespachoCj : row.faltanteCj ;
            row.faltanteDespachoKg = (row.despacho_Kg<row.pedido_Kg) ? row.pedido_Kg-row.despacho_Kg : 0 ;
            row.faltanteAjustadoKg = (row.faltanteDespachoKg<row.faltanteKg) ? row.faltanteDespachoKg : row.faltanteKg ;
            
            filaAux.createCell(0).setCellValue(row.centro_id);
            filaAux.createCell(1).setCellValue(row.centro_nombre);
            filaAux.createCell(2).setCellValue(row.material_sector);
            filaAux.createCell(3).setCellValue(row.agrupado_id);
            filaAux.createCell(4).setCellValue(row.agrupado_nombre);
          //  filaAux.createCell(5).setCellValue(row.pedido_fechaEntrega.getDate()+"-"+row.pedido_fechaEntrega.getMonth()
          //          +1+"-"+row.pedido_fechaEntrega.getYear());
            filaAux.createCell(5).setCellValue(row.pedido_fechaEntrega.toString());
            filaAux.createCell(6).setCellValue(row.pedido_Cj);
            filaAux.createCell(7).setCellValue(row.despacho_Cj);
            filaAux.createCell(8).setCellValue(row.stock_disponibleCj);
            filaAux.createCell(9).setCellValue(row.pedido_Kg);
            filaAux.createCell(10).setCellValue(row.pedido_neto);
            filaAux.createCell(11).setCellValue(row.stock_disponibilidadKg);
            filaAux.createCell(12).setCellValue(row.faltanteCj);
            filaAux.createCell(13).setCellValue(row.faltanteKg);
            filaAux.createCell(14).setCellValue(row.semana);
            filaAux.createCell(15).setCellValue(row.sobranteCj);
            filaAux.createCell(16).setCellValue(row.sobranteKg);
            filaAux.createCell(17).setCellValue(row.faltanteDespachoCj);
            filaAux.createCell(18).setCellValue(row.faltanteAjustadoCj);
            filaAux.createCell(19).setCellValue(row.faltanteDespachoKg);
            filaAux.createCell(20).setCellValue(row.faltanteAjustadoKg);
            filaAux.createCell(21).setCellValue(row.diaSemana);
            filaAux.createCell(22).setCellValue(row.anio);
                
            contReg++;
        }
        
        System.out.println("Registros CE: "+(contReg-1));
        libro.write(file);
        file.close();
        return true;
    }

    
}
