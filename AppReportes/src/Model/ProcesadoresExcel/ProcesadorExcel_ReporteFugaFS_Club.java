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
public class ProcesadorExcel_ReporteFugaFS_Club extends ProcesadorExcel
{

    public ProcesadorExcel_ReporteFugaFS_Club(String fileDir, String nombreHoja)
    {
        super(fileDir,"Datos Clientes");
    }


    @Override
    public boolean obtieneDatosXLS()
    {
        return false;
    }
    
    @Override
    public boolean obtieneDatosXLSX()
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
            
            ArrayList<String> titulos = new ArrayList<>();
            ArrayList<Integer> indices = new ArrayList<>();
            titulos.add("Rut Cliente"); indices.add(4);
            titulos.add("Categoria_Club"); indices.add(2);
            titulos.add("Segmento_Club"); indices.add(1);
            titulos.add("Canal"); indices.add(0);
            titulos.add("Zona"); indices.add(7);
            titulos.add("Sucursal"); indices.add(8);
            titulos.add("Canje"); indices.add(19);
            
            int ctRow=0;
            DataFormatter df=new DataFormatter();
            
            //MATERIALES
            String fileAux=System.getProperty("java.io.tmpdir") + "/CSVExcelTemp-Club.csv";
            String fileDestino=System.getProperty("user.home")+"/Desktop/"+"resultadoCSV-Club.csv";
            File tempFile = new File(fileAux);
            BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile));
            
            for(Row row : sheet)
            {
                if(ctRow>=1)
                {
                    String rutCliente=df.formatCellValue(row.getCell(indices.get(0)));
                    String categoriaClub=df.formatCellValue(row.getCell(indices.get(1)));
                    String segmentoClub=df.formatCellValue(row.getCell(indices.get(2)));
                    String tipoCliente=df.formatCellValue(row.getCell(indices.get(3)));
                    String zona=df.formatCellValue(row.getCell(indices.get(4)));
                    String sucursal=df.formatCellValue(row.getCell(indices.get(5)));
                    String canje=df.formatCellValue(row.getCell(indices.get(6)));
                    
                    String line='\"'+rutCliente+'\"'+";"+'\"'+categoriaClub+'\"'+";"+'\"'+segmentoClub+'\"'+";"+'\"'
                            +tipoCliente+'\"'+";"+'\"'+zona+'\"'+";"+'\"'+sucursal+'\"'+";"+'\"'+canje+'\"';
                    writer.write(line+"\n");
                }
                else if(ctRow==0)
                {
                    //titulos
                    writer.write("rut;catClub;segmClub;tipoCliente;zona;sucursal;canje"+"\n");
                }
                ctRow++;
            }

            writer.close();
            if(new File(fileDestino).exists())
                new File(fileDestino).delete();
            tempFile.renameTo(new File(fileDestino));
            tempFile.delete();

            workbook.close();
            file.close();
        }
        catch (Exception ex)
        {
            System.out.println("problema para obetener datos excel: "+ex);
            return false;
        }
        return true;
    }

}
