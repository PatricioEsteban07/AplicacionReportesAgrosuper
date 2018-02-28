/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model.ProcesadoresExcel;

import com.monitorjbl.xlsx.StreamingReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

/**
 *
 * @author Patricio
 */
public class ProcesadorExcel_MaestroMateriales extends ProcesadorExcel
{

    public ProcesadorExcel_MaestroMateriales(String fileDir)
    {
        super(fileDir);
    }

    public boolean obtieneDatosXLSX(String nombreHoja)
    {
        FileInputStream file = null;
        File f = this.abrirArchivo(this.fileDir);
        if(f==null)
        {
            return false;
        }
        try{
            
            // leer archivo excel
            file = new FileInputStream(f);
            Workbook workbook = StreamingReader.builder()
                    .rowCacheSize(100)    // number of rows to keep in memory (defaults to 10)
                    .bufferSize(4096)     // buffer size to use when reading InputStream to file (defaults to 1024)
                    .open(file); 

            Sheet sheet = workbook.getSheet(nombreHoja);
            if (sheet == null)
            {
                return false;
            }
            ArrayList<String> titulosMateriales = new ArrayList<>();
            ArrayList<Integer> indicesMateriales = new ArrayList<>();
            titulosMateriales.add("Material"); indicesMateriales.add(0);
            titulosMateriales.add("Texto breve de material"); indicesMateriales.add(1);
            titulosMateriales.add("Creacion"); indicesMateriales.add(14);
            titulosMateriales.add("Peso Caja"); indicesMateriales.add(19);
            titulosMateriales.add("Cod Sector"); indicesMateriales.add(2);
            titulosMateriales.add("Duracion Material"); indicesMateriales.add(18);
            titulosMateriales.add("Cod Estado"); indicesMateriales.add(17);
            titulosMateriales.add("Cod Agrupacion"); indicesMateriales.add(12);
            titulosMateriales.add("Cod Env-Granel"); indicesMateriales.add(16);
            titulosMateriales.add("Cod Marca"); indicesMateriales.add(10);
            titulosMateriales.add("N4"); indicesMateriales.add(8);

            
            ArrayList<String> titulosN2 = new ArrayList<>();
            ArrayList<Integer> indicesN2 = new ArrayList<>();
            titulosN2.add("N2"); indicesN2.add(4);
            titulosN2.add("Nivel 2"); indicesN2.add(5);
            titulosN2.add("Cod Sector"); indicesN2.add(2);
            
            ArrayList<String> titulosN3 = new ArrayList<>();
            ArrayList<Integer> indicesN3 = new ArrayList<>();
            titulosN3.add("N3"); indicesN3.add(6);
            titulosN3.add("Nivel 3"); indicesN3.add(7);
            titulosN3.add("N2"); indicesN3.add(4);
            
            ArrayList<String> titulosN4 = new ArrayList<>();
            ArrayList<Integer> indicesN4 = new ArrayList<>();
            titulosN4.add("N4"); indicesN4.add(8);
            titulosN4.add("Nivel 4"); indicesN4.add(9);
            titulosN4.add("N3"); indicesN4.add(6);
            
            ArrayList<String> titulosSector = new ArrayList<>();
            ArrayList<Integer> indicesSector = new ArrayList<>();
            titulosSector.add("Cod Sector"); indicesSector.add(2);
            titulosSector.add("Sector"); indicesSector.add(3);
            
            ArrayList<String> titulosMarca = new ArrayList<>();
            ArrayList<Integer> indicesMarca = new ArrayList<>();
            titulosMarca.add("Cod Marca"); indicesMarca.add(10);
            titulosMarca.add("Marca"); indicesMarca.add(11);
            
            ArrayList<String> titulosAgrupado = new ArrayList<>();
            ArrayList<Integer> indicesAgrupado = new ArrayList<>();
            titulosAgrupado.add("Cod Agrupacion"); indicesAgrupado.add(12);
            titulosAgrupado.add("Producto Agrupado"); indicesAgrupado.add(13);
            titulosAgrupado.add("N2"); indicesAgrupado.add(4);
            
            int ctRow=0;
            DataFormatter df=new DataFormatter();
            
            //MATERIALES
            String fileMaterialesAux=System.getProperty("java.io.tmpdir") + "/CSVExcelTemp-Materiales.csv";
            String fileMaterialesDestino=System.getProperty("user.home")+"/Desktop/"+"resultadoCSV-Materiales.csv";
            File tempMaterialesFile = new File(fileMaterialesAux);
            BufferedWriter writerMateriales = new BufferedWriter(new FileWriter(tempMaterialesFile));
            
            //N2
            String fileN2Aux=System.getProperty("java.io.tmpdir") + "/CSVExcelTemp-N2.csv";
            String fileN2Destino=System.getProperty("user.home")+"/Desktop/"+"resultadoCSV-N2.csv";
            File tempN2File = new File(fileN2Aux);
            BufferedWriter writerN2 = new BufferedWriter(new FileWriter(tempN2File));
            
            //N3
            String fileN3Aux=System.getProperty("java.io.tmpdir") + "/CSVExcelTemp-N3.csv";
            String fileN3Destino=System.getProperty("user.home")+"/Desktop/"+"resultadoCSV-N3.csv";
            File tempN3File = new File(fileN3Aux);
            BufferedWriter writerN3 = new BufferedWriter(new FileWriter(tempN3File));
            
            //N4
            String fileN4Aux=System.getProperty("java.io.tmpdir") + "/CSVExcelTemp-N4.csv";
            String fileN4Destino=System.getProperty("user.home")+"/Desktop/"+"resultadoCSV-N4.csv";
            File tempN4File = new File(fileN4Aux);
            BufferedWriter writerN4 = new BufferedWriter(new FileWriter(tempN4File));
            
            //SECTORES
            String fileSectorAux=System.getProperty("java.io.tmpdir") + "/CSVExcelTemp-sector.csv";
            String fileSectorDestino=System.getProperty("user.home")+"/Desktop/"+"resultadoCSV-sector.csv";
            File tempSectorFile = new File(fileSectorAux);
            BufferedWriter writerSector = new BufferedWriter(new FileWriter(tempSectorFile));
            
            //MARCAS
            String fileMarcaAux=System.getProperty("java.io.tmpdir") + "/CSVExcelTemp-marca.csv";
            String fileMarcaDestino=System.getProperty("user.home")+"/Desktop/"+"resultadoCSV-marca.csv";
            File tempMarcaFile = new File(fileMarcaAux);
            BufferedWriter writerMarca = new BufferedWriter(new FileWriter(tempMarcaFile));
            
            //AGRUPADOS
            String fileAgrupadoAux=System.getProperty("java.io.tmpdir") + "/CSVExcelTemp-agrupado.csv";
            String fileAgrupadoDestino=System.getProperty("user.home")+"/Desktop/"+"resultadoCSV-agrupado.csv";
            File tempAgrupadoFile = new File(fileAgrupadoAux);
            BufferedWriter writerAgrupado = new BufferedWriter(new FileWriter(tempAgrupadoFile));
            
            for(Row row : sheet)
            {
                if(ctRow>=1)
                {
                    
                    String material_id=df.formatCellValue(row.getCell(indicesMateriales.get(0)));
                    String material_nombre=df.formatCellValue(row.getCell(indicesMateriales.get(1)));
                    String[] fechaAux=df.formatCellValue(row.getCell(indicesMateriales.get(2))).split("/");
                    String material_fecha=((Integer.parseInt(fechaAux[1])<10)? "0"+fechaAux[1] : fechaAux[1])+"-"
                            +((Integer.parseInt(fechaAux[0])<10)? "0"+fechaAux[0] : fechaAux[0])+"-"
                            +"20"+fechaAux[2];//fecha
                    
                    String material_peso=df.formatCellValue(row.getCell(indicesMateriales.get(3)));
                    String material_duracion=df.formatCellValue(row.getCell(indicesMateriales.get(5)));
                    String material_refrig=df.formatCellValue(row.getCell(indicesMateriales.get(6)));
                    String material_envasado=df.formatCellValue(row.getCell(indicesMateriales.get(8)));
                    
                    String n2_id=df.formatCellValue(row.getCell(indicesN2.get(0)));
                    String n2_nombre=df.formatCellValue(row.getCell(indicesN2.get(1)));
                    
                    String n3_id=df.formatCellValue(row.getCell(indicesN3.get(0)));
                    String n3_nombre=df.formatCellValue(row.getCell(indicesN3.get(1)));
                    
                    String n4_id=df.formatCellValue(row.getCell(indicesN4.get(0)));
                    String n4_nombre=df.formatCellValue(row.getCell(indicesN4.get(1)));
                    
                    String sector_id=df.formatCellValue(row.getCell(indicesSector.get(0)));
                    String sector_nombre=df.formatCellValue(row.getCell(indicesSector.get(1)));
                    
                    String marca_id=df.formatCellValue(row.getCell(indicesMarca.get(0)));
                    String marca_nombre=df.formatCellValue(row.getCell(indicesMarca.get(1)));
                    
                    String agrupado_id=df.formatCellValue(row.getCell(indicesAgrupado.get(0)));
                    String agrupado_nombre=df.formatCellValue(row.getCell(indicesAgrupado.get(1)));
                    
                    String lineMarca='\"'+marca_id+'\"'+";"+'\"'+marca_nombre+'\"';
                    writerMarca.write(lineMarca+"\n");
                    
                    String lineSector='\"'+sector_id+'\"'+";"+'\"'+sector_nombre+'\"';
                    writerSector.write(lineSector+"\n");
                    
                    String lineN2='\"'+n2_id+'\"'+";"+'\"'+n2_nombre+'\"'+";"+'\"'+sector_id+'\"';
                    writerN2.write(lineN2+"\n");
                    
                    String lineN3='\"'+n3_id+'\"'+";"+'\"'+n3_nombre+'\"'+";"+'\"'+n2_id+'\"';
                    writerN3.write(lineN3+"\n");
                    
                    String lineN4='\"'+n4_id+'\"'+";"+'\"'+n4_nombre+'\"'+";"+'\"'+n3_id+'\"';
                    writerN4.write(lineN4+"\n");
                    
                    String lineAgrupado='\"'+agrupado_id+'\"'+";"+'\"'+agrupado_nombre+'\"'+";"+'\"'+n2_id+'\"';
                    writerAgrupado.write(lineAgrupado+"\n");
                    
                    String lineMateriales='\"'+material_id+'\"'+";"+'\"'+material_nombre+'\"'+";"+'\"'+material_fecha+'\"'
                        +";"+'\"'+material_peso+'\"'+";"+'\"'+sector_id+'\"'+";"+'\"'+material_duracion+'\"'
                        +";"+'\"'+material_refrig+'\"'+";"+'\"'+agrupado_id+'\"'+";"+'\"'+material_envasado+'\"'
                        +";"+'\"'+marca_id+'\"'+";"+'\"'+n4_id+'\"';
                    writerMateriales.write(lineMateriales+"\n");
                }
                else if(ctRow==0)
                {
                    //titulos
                    writerMateriales.write("id;nombre;fecha;peso;sector;duracion;refrig;envas;marca;n4"+"\n");
                    writerN2.write("id;nombre;sector"+"\n");
                    writerN3.write("id;nombre;n2"+"\n");
                    writerN4.write("id;nombre;n3"+"\n");
                    writerSector.write("id;nombre"+"\n");
                    writerMarca.write("id;nombre"+"\n");
                    writerAgrupado.write("id;nombre"+"\n");
                }
                ctRow++;
            }

            writerMateriales.close();
            if(new File(fileMaterialesDestino).exists())
                new File(fileMaterialesDestino).delete();
            tempMaterialesFile.renameTo(new File(fileMaterialesDestino));
            tempMaterialesFile.delete();

            writerN2.close();
            if(new File(fileN2Destino).exists())
                new File(fileN2Destino).delete();
            tempN2File.renameTo(new File(fileN2Destino));
            tempN2File.delete();

            writerN3.close();
            if(new File(fileN3Destino).exists())
                new File(fileN3Destino).delete();
            tempN3File.renameTo(new File(fileN3Destino));
            tempN3File.delete();

            writerN4.close();
            if(new File(fileN4Destino).exists())
                new File(fileN4Destino).delete();
            tempN4File.renameTo(new File(fileN4Destino));
            tempN4File.delete();
            
            writerSector.close();
            if(new File(fileSectorDestino).exists())
                new File(fileSectorDestino).delete();
            tempSectorFile.renameTo(new File(fileSectorDestino));
            tempSectorFile.delete();
            
            writerMarca.close();
            if(new File(fileMarcaDestino).exists())
                new File(fileMarcaDestino).delete();
            tempMarcaFile.renameTo(new File(fileMarcaDestino));
            tempMarcaFile.delete();
            
            writerAgrupado.close();
            if(new File(fileAgrupadoDestino).exists())
                new File(fileAgrupadoDestino).delete();
            tempAgrupadoFile.renameTo(new File(fileAgrupadoDestino));
            tempAgrupadoFile.delete();
            
            workbook.close();
        }
        catch (FileNotFoundException ex)
        {
            System.out.println("Archivo no encontrado!: "+ex);
            Logger.getLogger(ProcesadorExcel_MaestroMateriales.class.getName()).log(Level.SEVERE, null, ex);
        }
        catch (IOException ex)
        {
            System.out.println("problema io!: "+ex);
            Logger.getLogger(ProcesadorExcel_MaestroMateriales.class.getName()).log(Level.SEVERE, null, ex);
        }
        finally
        {
            try
            {
                file.close();
            }
            catch (IOException ex)
            {
                System.out.println("problema io!: "+ex);
                Logger.getLogger(ProcesadorExcel_MaestroMateriales.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return false;
    }

}
