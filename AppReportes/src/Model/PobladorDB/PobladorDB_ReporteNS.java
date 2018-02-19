/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model.PobladorDB;

import Model.LocalDB;
import Model.PobladorDB.Threads.Buzon;
import Model.PobladorDB.Threads.PaqueteBuzon;
import Model.PobladorDB.Threads.ThreadProcess;
import Model.PobladorDB.Threads.ThreadProcess_Pedidos;
import com.monitorjbl.xlsx.StreamingReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.ArrayList;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

/**
 *
 * @author Patricio
 */
public class PobladorDB_ReporteNS extends PobladorDB
{
    
    public PobladorDB_ReporteNS(LocalDB db)
    {
        super(db);
    }
    
    public boolean importarPedidosThreads() throws FileNotFoundException, IOException, SQLException, ClassNotFoundException
    {
        this.buzon = new Buzon();
        
        ArrayList<ThreadProcess> threads=new ArrayList<>();
        ArrayList<ThreadProcess> procesadoresDB=new ArrayList<>();
        System.out.println("Creando threads...");
        for (int i = 0; i < 10; i++)
        {
            ThreadProcess_Pedidos aux = new ThreadProcess_Pedidos(i, this.db, this.buzon);
            threads.add(aux);
            aux.start();            
        //    ThreadProcess_DB aux2 = new ThreadProcess_DB(i, db, buzon,threads);
        //    procesadoresDB.add(aux2);
       //     aux2.start();
        }
       // new ThreadProcess_DB(0, db, buzon,threads).start();
        File archivo=this.openFile(this.dirBase, "pedidos.xlsx");
        if(archivo==null)
        {
          //  CommandNames.generaMensaje("Información de Aplicación", Alert.AlertType.INFORMATION, CommandNames.ESTADO_INFO, 
           //         CommandNames.MSG_INFO_FILE_DOESNT_EXISTS);
            return false;
        }
        //archivo existe, comienza procesado...
        try (InputStream is = new FileInputStream(archivo)) {
            
            // leer archivo excel
            Workbook workbook = StreamingReader.builder()
                    .rowCacheSize(100)    // number of rows to keep in memory (defaults to 10)
                    .bufferSize(4096)     // buffer size to use when reading InputStream to file (defaults to 1024)
                    .open(is); 
            //obtener la hoja que se va leer
            Sheet sheet = workbook.getSheetAt(0);
            //obtener todas las filas de la hoja excel
            
            int ctRow=0;
            System.out.println("Iniciando creacion de jobs con rows...");
            for(Row row : sheet)
            {
                if(ctRow!=0)
                    this.buzon.enviarTarea(ctRow, row);
                ctRow++;
            }
        }
        System.out.println("Finalizado creacion de jobs con rows...");
        for (int i = 0; i < 10; i++)
        {
            threads.get(i).flagExterno=false;
      //      procesadoresDB.get(i).flagExterno=false;
        }
        System.out.println("Empezando a procesar paquetes del buzon...");
        
        this.connect();
        while (!this.buzon.contenedorIsEmpty() || !this.buzon.buzonIsEmpty() || !threadsReady(threads))
        {
            PaqueteBuzon aux=this.buzon.obtenerPaquete();
            if(aux!=null)
            {
                if(aux.id.containsKey("Centro") && this.executeInsert("SELECT id FROM centro WHERE id='"
                        +aux.id.get("Centro")+"'", aux.query.get("Centro")))
                   // this.db.centros.add(aux.id.get("Centro"));
                if(aux.id.containsKey("Oficina") && this.executeInsert("SELECT id FROM oficinaventas WHERE id='"
                        +aux.id.get("Oficina")+"'", aux.query.get("Oficina")))
                    //this.db.oficinas.add(aux.id.get("Oficina"));
                if(aux.id.containsKey("Material") && this.executeInsert("SELECT id FROM material WHERE id='"
                        +aux.id.get("Material")+"'", aux.query.get("Material")))
                    //this.db.materiales.add(aux.id.get("Material"));
                if(aux.id.containsKey("Pedido") && this.executeInsert("SELECT id FROM pedido WHERE id='"
                        +aux.id.get("Pedido")+"'", aux.query.get("Pedido")))
                    //this.db.pedidos.add(aux.id.get("Pedido"));
                    aux.id=aux.id;
            }
        }
        this.close();

        return true;
    }
    
    private boolean threadsReady(ArrayList<ThreadProcess> threads)
    {
        for (ThreadProcess thread : threads)
        {
            if(thread.flagPropio)
                return false;
        }
        return true;
    }
    
    public boolean importarPedidos() throws FileNotFoundException, IOException, InvalidFormatException
    {
        int ct_centros=0, ct_oficinas=0, ct_materiales=0, ct_pedidos=0, ctRow=0;
        File archivo=this.openFile(this.dirBase, "pedidos.xlsx");
        if(archivo==null)
        {
          //  CommandNames.generaMensaje("Información de Aplicación", Alert.AlertType.INFORMATION, CommandNames.ESTADO_INFO, 
           //         CommandNames.MSG_INFO_FILE_DOESNT_EXISTS);
            return false;
        }
        //archivo existe, comienza procesado...
        try (InputStream is = new FileInputStream(archivo)) {
            
            // leer archivo excel
            Workbook workbook = StreamingReader.builder()
                    .rowCacheSize(100)    // number of rows to keep in memory (defaults to 10)
                    .bufferSize(4096)     // buffer size to use when reading InputStream to file (defaults to 1024)
                    .open(is); 
            //obtener la hoja que se va leer
            Sheet sheet = workbook.getSheetAt(0);
            //obtener todas las filas de la hoja excel
            
            this.connect();
            for(Row row : sheet)
            {
                //System.out.println("Row: "+ctRow);
                if(ctRow!=0)
                {
                }
                 
                ctRow++;
            }
            this.close();
        } catch (Exception e) {
            System.out.println("upsis :c "+e);
        }
        System.out.println("Contadores:");
        System.out.println("Centros: "+ct_centros);
        System.out.println("Oficinas: "+ct_oficinas);
        System.out.println("Materiales: "+ct_materiales);
        System.out.println("pedidos: "+ct_pedidos);
        System.out.println("Total rows: "+ctRow);
        System.out.println("---------------");
        return false;
    }
    
    public boolean importarStock() throws FileNotFoundException, IOException, InvalidFormatException
    {
        int ct_centros=0, ct_materiales=0, ct_stocks=0, ctRow=0;
        File archivo=this.openFile(this.dirBase, "stock.xlsx");
        if(archivo==null)
        {
          //  CommandNames.generaMensaje("Información de Aplicación", Alert.AlertType.INFORMATION, CommandNames.ESTADO_INFO, 
            //        CommandNames.MSG_INFO_FILE_DOESNT_EXISTS);
            return false;
        }
        //archivo existe, comienza procesado...
        try (InputStream is = new FileInputStream(archivo)) {
            
            // leer archivo excel
            Workbook workbook = StreamingReader.builder()
                    .rowCacheSize(100)    // number of rows to keep in memory (defaults to 10)
                    .bufferSize(4096)     // buffer size to use when reading InputStream to file (defaults to 1024)
                    .open(is); 
            //obtener la hoja que se va leer
            Sheet sheet = workbook.getSheetAt(0);
            //obtener todas las filas de la hoja excel
            
            this.connect();
            for(Row row : sheet)
            {
               // System.out.println("Row: "+ctRow);
                if(ctRow!=0)
                {
                }
                ctRow++;
            }
            this.close();
        } catch (Exception e) {
            System.out.println("upsis :c "+e);
        }
        System.out.println("Contadores:");
        System.out.println("Centros: "+ct_centros);
        System.out.println("Materiales: "+ct_materiales);
        System.out.println("Stocks: "+ct_stocks);
        System.out.println("Total rows: "+ctRow);
        System.out.println("---------------");
        return false;
    }
    
    public boolean importarDespachos() throws FileNotFoundException, IOException, InvalidFormatException
    {
        int ct_centros=0, ct_materiales=0, ct_clientes=0, ct_despachos=0, ctRow=0;
        File archivo=this.openFile(this.dirBase, "despacho-faltantes.xlsx");
        if(archivo==null)
        {
          //  CommandNames.generaMensaje("Información de Aplicación", Alert.AlertType.INFORMATION, CommandNames.ESTADO_INFO, 
            //        CommandNames.MSG_INFO_FILE_DOESNT_EXISTS);
            return false;
        }
        //archivo existe, comienza procesado...
        try (InputStream is = new FileInputStream(archivo)) {
            
            // leer archivo excel
            Workbook workbook = StreamingReader.builder()
                    .rowCacheSize(100)    // number of rows to keep in memory (defaults to 10)
                    .bufferSize(4096)     // buffer size to use when reading InputStream to file (defaults to 1024)
                    .open(is); 
            
            //obtener la hoja que se va leer
            Sheet sheet = workbook.getSheetAt(0);
            //obtener todas las filas de la hoja excel

            //extraer datos Centro Distribucion
            this.connect();
            for(Row row : sheet)
            {
              //  System.out.println("Row: "+ctRow);
                if(ctRow!=0)
                {
                }
                ctRow++;
            }
            this.close();
        } catch (Exception e) {
            System.out.println("upsis :c"+e);
        }
        System.out.println("Contadores:");
        System.out.println("Centros: "+ct_centros);
        System.out.println("Materiales: "+ct_materiales);
        System.out.println("Clientes: "+ct_clientes);
        System.out.println("Despachos: "+ct_despachos);
        System.out.println("Total rows: "+ctRow);
        System.out.println("---------------");
        return false;
    }

}
