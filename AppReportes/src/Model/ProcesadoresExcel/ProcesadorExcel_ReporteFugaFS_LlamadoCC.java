/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model.ProcesadoresExcel;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;

/**
 *
 * @author Patricio
 */
public class ProcesadorExcel_ReporteFugaFS_LlamadoCC extends ProcesadorExcel
{

    public ProcesadorExcel_ReporteFugaFS_LlamadoCC(String fileDir, String nombreHoja)
    {
        super(fileDir,"Datos");
    }

    @Override
    public boolean obtieneDatosXLSX()
    {
        return false;
    }
    
    @Override
    public boolean obtieneDatosXLS()
    {
        int rowTitulos=1;
        int colInicio=1;
        FileInputStream file = null;
        File f = this.abrirArchivo(this.fileDir);
        if(f==null)
        {
            return false;
        }
        try{
            // leer archivo excel
            file = new FileInputStream(f);
            HSSFWorkbook workbook = new HSSFWorkbook(file);
    
            /*
             * Obtenemos la primera pestaña a la que se quiera procesar indicando el indice.
             * Una vez obtenida la hoja excel con las filas que se quieren leer obtenemos el iterator
             * que nos permite recorrer cada una de las filas que contiene.
             */
            HSSFSheet sheet = workbook.getSheet(nombreHoja);
            if (sheet == null)
            {
                return false;
            }
            Row rowAux = sheet.getRow(rowTitulos);
            ArrayList<String> titulos = new ArrayList<>();
            ArrayList<Integer> indices = new ArrayList<>();
            titulos.add("Local");
            titulos.add("Nombre");
            titulos.add("CodEje");
            titulos.add("Ejecutivo");
            titulos.add("Lunes");
            titulos.add("Martes");
            titulos.add("Miercoles");
            titulos.add("Jueves");
            titulos.add("Viernes");
            titulos.add("Sabado");
            titulos.add("Domingo");
            titulos.add("Frecuencia");

            for (int i = 0; i < titulos.size(); i++)
            {
                int aux = this.buscaColumna(sheet, titulos.get(i), rowTitulos, colInicio);
                if (aux == -1)
                {
                    System.out.println("Hay una columna que no se encuentra. No se puede generar CSV con tal info.");
                    return false;
                }
                indices.add(aux);
            }
            int ctRow=0;
            DataFormatter df=new DataFormatter();
            String fileAux=System.getProperty("java.io.tmpdir") + "/CSVExcelTemp-LlamadoCC.csv";
            String fileDestino=System.getProperty("user.home")+"/Desktop/"+"resultadoCSV-LlamadoCC.csv";
            File tempFile = new File(fileAux);
            BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile));
            
            for(Row row : sheet)
            {
                if(ctRow>=rowTitulos)
                {
                    String idCliente=df.formatCellValue(row.getCell(indices.get(0)));
                    String nombreCliente=df.formatCellValue(row.getCell(indices.get(1)));
                    String idEjecutivo=df.formatCellValue(row.getCell(indices.get(2)));
                    String nombreEjecutivo=df.formatCellValue(row.getCell(indices.get(3)));
                    
                    String diaLlamado="";
                    String[] dias=new String[]{"Lu","Ma","Mi","Ju","Vi","Sa","Do"};
                    for (int i = 0; i < 7; i++)
                    {
                        if(!df.formatCellValue(row.getCell(indices.get(i+4))).equals(""))
                        {
                            diaLlamado=diaLlamado+(dias[i]);
                        }
                    }
                    diaLlamado=diaLlamado+(" -"+df.formatCellValue(row.getCell(indices.get(11))).replace(" ", ""));
                    String line='\"'+idCliente+'\"'+";"+'\"'+nombreCliente+'\"'+";"+'\"'+idEjecutivo+'\"'+";"+'\"'
                            +nombreEjecutivo+'\"'+";"+'\"'+diaLlamado+'\"';
                    writer.write(line+"\n");
                }
                else if(ctRow==rowTitulos-1)
                {
                    //titulos
                    writer.write("clienteLocalID;clienteLocal;codEjecutivo;ejecutivo;diaLlamados"+"\n");
                }
                ctRow++;
            }

            writer.close();
            tempFile.renameTo(new File(fileDestino));
            tempFile.delete();
            workbook.close();
            file.close();
        }
        catch (IOException ex)
        {
            System.out.println("problema para obetener datos excel: "+ex);
            return false;
        }
        return true;
    }
}
