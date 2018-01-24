/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model.PobladorDB;

import Model.CommandNames;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashSet;
import java.util.Set;
import javafx.scene.control.Alert;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 *
 * @author Patricio
 */
public abstract class PobladorDB
{
    
    private Connection conn = null;
    public final String dirBase=System.getProperty("user.home")+"/Desktop/";

    public PobladorDB()
    {

    }
    
    public boolean importarMateriales() throws FileNotFoundException, IOException, InvalidFormatException, SQLException
    {
        File archivo=this.openFile(this.dirBase, "Maestro Materiales Full.xlsx");
        if(archivo==null)
        {
            CommandNames.generaMensaje("Información de Aplicación", Alert.AlertType.INFORMATION, CommandNames.ESTADO_INFO, 
                    CommandNames.MSG_INFO_FILE_DOESNT_EXISTS);
            return false;
        }
        //archivo existe, comienza procesado...

        try (FileInputStream file = new FileInputStream(archivo)) {
            // leer archivo excel
            XSSFWorkbook worbook = new XSSFWorkbook(file);
            //obtener la hoja que se va leer
            XSSFSheet sheet = worbook.getSheet("HojaEstatica");

            //extraer datos materiales
            Set<String> materiales = new HashSet<String>();
            Set<String> marcas = new HashSet<String>();
            Set<String> sectores = new HashSet<String>();
            Set<String> agrupados = new HashSet<String>();
            Set<String> tiposEnvasados = new HashSet<String>();
            Set<String> estadosRefrigerados = new HashSet<String>();
            Set<String> n2s = new HashSet<String>();
            Set<String> n3s = new HashSet<String>();
            Set<String> n4s = new HashSet<String>();
            
            this.connect();
            int ctRow=1;
            while(sheet.getRow(ctRow)!=null)
            {
                System.out.println("Row: "+ctRow);

                //RELLENO TABLA AGRUPADOS
                XSSFCell aux=sheet.getRow(ctRow).getCell(12);
                aux.setCellType(CellType.STRING);
                String idAgrupado=aux.toString();
                String nombreAgrupado=sheet.getRow(ctRow).getCell(13).toString();
                System.out.println("Agrupado: "+idAgrupado+"/"+nombreAgrupado);
                if(!validarContenido(idAgrupado) || !validarContenido(nombreAgrupado))
                {
                    idAgrupado="";
                    nombreAgrupado="";
                }
                else if(!agrupados.contains(idAgrupado))
                {
                    nombreAgrupado=tratarCaracteresEspeciales(nombreAgrupado);
                    System.out.println(nombreAgrupado);
                    this.executeInsert("SELECT id FROM agrupado WHERE id='"+idAgrupado+"'",
                            "INSERT INTO agrupado(id,nombre) VALUES ('"+idAgrupado+"','"
                                +nombreAgrupado+"')");
                    agrupados.add(idAgrupado);
                }
                
                //RELLENO TABLA TIPO ENVASADOS
                aux=sheet.getRow(ctRow).getCell(16);
                aux.setCellType(CellType.STRING);
                String idTipoEnvasado=aux.toString();
                String nombreTipoEnvasado=sheet.getRow(ctRow).getCell(21).toString();
                System.out.println("Tipo Envasado: "+idTipoEnvasado+"/"+nombreTipoEnvasado);
                
                
                if(!validarContenido(idTipoEnvasado) || !validarContenido(nombreTipoEnvasado))
                {
                    idTipoEnvasado="";
                    nombreTipoEnvasado="";
                }
                else if(!tiposEnvasados.contains(idTipoEnvasado))
                {
                    this.executeInsert("SELECT id FROM tipoEnvasado WHERE id='"+idTipoEnvasado+"'",
                            "INSERT INTO tipoEnvasado(id,nombre) VALUES ('"+idTipoEnvasado+"','"
                                +nombreTipoEnvasado+"')");
                    tiposEnvasados.add(idTipoEnvasado);
                }
                
                //RELLENO TABLA ESTADO REFRIGERADO
                aux=sheet.getRow(ctRow).getCell(17);
                aux.setCellType(CellType.STRING);
                String idestadoRefrigerado=aux.toString();
                String nombreEstadoRefrigerado=sheet.getRow(ctRow).getCell(20).toString();
                System.out.println("estadoRefrigerado: "+idestadoRefrigerado+"/"+nombreEstadoRefrigerado);
                if(!validarContenido(idestadoRefrigerado) || !validarContenido(nombreEstadoRefrigerado))
                {
                    idestadoRefrigerado="";
                    nombreEstadoRefrigerado="";
                }
                else if(!estadosRefrigerados.contains(idestadoRefrigerado))
                {
                    this.executeInsert("SELECT id FROM estadoRefrigerado WHERE id='"+idestadoRefrigerado+"'",
                            "INSERT INTO estadoRefrigerado(id,nombre) VALUES ('"+idestadoRefrigerado+"','"
                                +nombreEstadoRefrigerado+"')");
                    estadosRefrigerados.add(idestadoRefrigerado);
                }
                
                //RELLENO TABLA MARCAS
                aux=sheet.getRow(ctRow).getCell(10);
                aux.setCellType(CellType.STRING);
                String idMarca=aux.toString();
                String nombreMarca=sheet.getRow(ctRow).getCell(11).toString();
                System.out.println("Marca: "+idMarca+"/"+nombreMarca);
                if(!validarContenido(idMarca) || !validarContenido(nombreMarca))
                {
                    idMarca="";
                    nombreMarca="";
                }
                else if(!marcas.contains(idMarca))
                {
                    nombreMarca=tratarCaracteresEspeciales(nombreMarca);
                    this.executeInsert("SELECT id FROM marca WHERE id='"+idMarca+"'",
                            "INSERT INTO marca(id,nombre) VALUES ('"+idMarca+"','"
                                +nombreMarca+"')");
                    marcas.add(idMarca);
                }
                
                //RELLENO TABLA SECTORES
                aux=sheet.getRow(ctRow).getCell(2);
                aux.setCellType(CellType.STRING);
                String idSector=aux.toString();
                String nombreSector=sheet.getRow(ctRow).getCell(3).toString();
                System.out.println("Sector: "+idSector+"/"+nombreSector);
                if(!validarContenido(idSector) || !validarContenido(nombreSector))
                {
                    idSector="";
                    nombreSector="";
                }
                else if(!sectores.contains(idSector))
                {
                    nombreSector=tratarCaracteresEspeciales(nombreSector);
                    this.executeInsert("SELECT id FROM sector WHERE id='"+idSector+"'",
                            "INSERT INTO sector(id,nombre) VALUES ('"+idSector+"','"
                                +nombreSector+"')");
                    sectores.add(idSector);
                }
                
                //RELLENO TABLA N2
                aux=sheet.getRow(ctRow).getCell(4);
                aux.setCellType(CellType.STRING);
                String idN2=aux.toString();
                String nombreN2=sheet.getRow(ctRow).getCell(5).toString();
                System.out.println("N2: "+idN2+"/"+nombreN2);
                if(!validarContenido(idN2) || !validarContenido(nombreN2))
                {
                    idN2="";
                    nombreN2="";
                }
                else if(!n2s.contains(idN2))
                {
                    nombreN2=tratarCaracteresEspeciales(nombreN2);
                    this.executeInsert("SELECT id FROM n2 WHERE id='"+idN2+"'",
                            "INSERT INTO n2(id,nombre,sector_id) VALUES ('"+idN2+"','"+nombreN2+"','"+idSector+"')");
                    n2s.add(idN2);
                }
                
                //RELLENO TABLA N3
                aux=sheet.getRow(ctRow).getCell(6);
                aux.setCellType(CellType.STRING);
                String idN3=aux.toString();
                String nombreN3=sheet.getRow(ctRow).getCell(7).toString();
                System.out.println("N3: "+idN3+"/"+nombreN3);
                if(!validarContenido(idN3) || !validarContenido(nombreN3))
                {
                    idN3="";
                    nombreN3="";
                }
                else if(!n3s.contains(idN3))
                {
                    nombreN3=tratarCaracteresEspeciales(nombreN3);
                    this.executeInsert("SELECT id FROM n3 WHERE id='"+idN3+"'",
                            "INSERT INTO n3(id,nombre,sector_id,n2_id) VALUES ('"+idN3+"','"+nombreN3+"','"+idSector
                                +"','"+idN2+"')");
                    n3s.add(idN3);
                }
                
                //RELLENO TABLA N4
                aux=sheet.getRow(ctRow).getCell(8);
                aux.setCellType(CellType.STRING);
                String idN4=aux.toString();
                String nombreN4=sheet.getRow(ctRow).getCell(9).toString();
                System.out.println("N4: "+idN4+"/"+nombreN4);
                if(!validarContenido(idN4) || !validarContenido(nombreN4))
                {
                    idN4="";
                    nombreN4="";
                }
                else if(!n4s.contains(idN4))
                {
                    nombreN4=tratarCaracteresEspeciales(nombreN4);
                    this.executeInsert("SELECT id FROM n4 WHERE id='"+idN4+"'",
                            "INSERT INTO n4(id,nombre,sector_id,n2_id,n3_id) VALUES ('"+idN4+"','"+nombreN4+"','"+idSector
                                +"','"+idN2+"','"+idN3+"')");
                    n4s.add(idN4);
                }
                
                //RELLENO TABLA MATERIALES
                aux=sheet.getRow(ctRow).getCell(0);
                aux.setCellType(CellType.STRING);
                String idMaterial=aux.toString();
                String nombreMaterial=sheet.getRow(ctRow).getCell(1).toString();
                
                //OJO, VERIFICAR QUE FECHAS ESTEN INGRESADAS CORRECTAMENTE
                String fechaCreacionMaterial=sheet.getRow(ctRow).getCell(14).toString();
                String dia=fechaCreacionMaterial.substring(0, 2);
                String mes=fomatearMes(fechaCreacionMaterial.substring(3, 6));
                String anio=fechaCreacionMaterial.substring(7);
                fechaCreacionMaterial=anio+"-"+mes+"-"+dia;
                
                aux=sheet.getRow(ctRow).getCell(18);
                aux.setCellType(CellType.STRING);
                String duracionMaterial=aux.toString();
                String pesoCajaMaterial=sheet.getRow(ctRow).getCell(19).toString();
                
                System.out.println("caja:"+pesoCajaMaterial);
                
                aux=sheet.getRow(ctRow).getCell(15);
                aux.setCellType(CellType.STRING);
                String activoMaterial=aux.toString();
                System.out.println("Material: "+idMaterial+"/"+nombreMaterial);
                if(!validarContenido(idMaterial) || !validarContenido(nombreMaterial))
                {
                    idMaterial="";
                    nombreMaterial="";
                    System.out.println("NO AGREGADO!");
                }
                else if(!materiales.contains(idMaterial))
                {
                    nombreMaterial=tratarCaracteresEspeciales(nombreMaterial);
                    this.executeInsert("SELECT id FROM material WHERE id='"+idMaterial+"'",
                            "INSERT INTO material(id,nombre,fechaCreacion,duracion,pesoCaja,activo,tipoEnvasado_id,"
                                + "estadoRefrigerado_id,sector_id,marca_id) VALUES ('"
                                +idMaterial+"','"+nombreMaterial+"',"
                                +((validarContenido(fechaCreacionMaterial))? "'"+fechaCreacionMaterial+"'" : "null")+","
                                +((validarContenido(duracionMaterial))? "'"+duracionMaterial+"'" : "null")+","
                                +((validarContenido(pesoCajaMaterial))? "'"+pesoCajaMaterial+"'" : "null")+","
                                +((validarContenido(activoMaterial))? "'"+activoMaterial+"'" : "null")+","
                                +((validarContenido(idTipoEnvasado))? "'"+idTipoEnvasado+"'" : "null")+","
                                +((validarContenido(idestadoRefrigerado))? "'"+idestadoRefrigerado+"'" : "null")+","
                                +((validarContenido(idSector))? "'"+idSector+"'" : "null")+","
                                +((validarContenido(idMarca))? "'"+idMarca+"'" : "null")+")");
                    materiales.add(idMaterial);
                }
                
                
              //  System.out.println("Pedido(Sector,Cj,Kg,$): "+sheet.getRow(ctRow).getCell(4)+"/"+sheet.getRow(ctRow).getCell(9)
              //          +"/"+sheet.getRow(ctRow).getCell(10)+"/"+sheet.getRow(ctRow).getCell(11));
                
              //  System.out.println("Fecha/TipoCliente: "+sheet.getRow(ctRow).getCell(7)+"/"+sheet.getRow(ctRow).getCell(8));
                System.out.println("-----------------------------");
                ctRow++;
            }
        } catch (Exception e) {
            System.out.println("Excepcion actualizando materiales: "+e);
        }
        this.close();
        return false;
    }

    protected File openFile(String fileDir, String fileName)
    {
        File archivoExcel = new File(fileDir+fileName);
        if (!archivoExcel.exists())
        {
            System.out.println("Ojo, el archivo no existe :c");
            return null;
        }
        return archivoExcel;
    }
    
    public boolean connect() throws SQLException, ClassNotFoundException
    {
        if (conn == null)
        {
            try
            {
                Class.forName("com.mysql.jdbc.Driver");
                this.conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/db_app_reportes", "root", "12345678");
            }
            catch (SQLException ex)
            {
                System.out.println("connect - SQLException: " + ex);
                return false;
            }
            catch (ClassNotFoundException ex)
            {
                System.out.println("connect - ClassCastException: " + ex);
                return false;
            }
        }
        return true;
    }

    public boolean executeInsert(String verify, String query) throws SQLException
    {
        if (conn != null)
        {
            Statement stmt = conn.createStatement();
            if(!stmt.executeQuery(verify).next())
            {
                stmt.executeUpdate(query);
                return true;
            }
        }
        return false;
    }

    public void close() throws SQLException
    {
        if (this.conn != null)
        {
            this.conn.close();
            this.conn=null;
        }
    }

    private boolean validarContenido(String value)
    {
        switch(value)
        {
            case "(en blanco)":
                return false;
            case "":
                return false;
        }
        return true;
    }

    private String fomatearMes(String month)
    {
        switch(month)
        {
            case "ene":
                return "01";
            case "feb":
                return "02";
            case "mar":
                return "03";
            case "abr":
                return "04";
            case "may":
                return "05";
            case "jun":
                return "06";
            case "jul":
                return "07";
            case "ago":
                return "08";
            case "sep":
                return "09";
            case "oct":
                return "10";
            case "nov":
                return "11";
            case "dic":
                return "12";
        }
        return null;
    }

    private String tratarCaracteresEspeciales(String value)
    {
        return value.replace("'", "");
    }

}
