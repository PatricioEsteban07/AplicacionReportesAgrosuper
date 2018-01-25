/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model.PobladorDB;

import Model.CommandNames;
import Model.LocalDB;
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
    public LocalDB db;

    public PobladorDB(LocalDB db)
    {
        this.db=db;
    }
    
    public boolean importarMateriales() throws FileNotFoundException, IOException, InvalidFormatException, SQLException
    {
        int ctRow=1, ct_agrupados=0, ct_envasados=0, ct_refrigerados=0, ct_marcas=0, ct_sectores=0, ct_n2=0, 
                ct_n3=0, ct_n4=0, ct_materiales=0;
        File archivo=this.openFile(this.dirBase, "Maestro Materiales Full.xlsx");
        if(archivo==null)
        {
           // CommandNames.generaMensaje("Información de Aplicación", Alert.AlertType.INFORMATION, CommandNames.ESTADO_INFO, 
           //         CommandNames.MSG_INFO_FILE_DOESNT_EXISTS);
            return false;
        }
        //archivo existe, comienza procesado...

        try (FileInputStream file = new FileInputStream(archivo)) {
            // leer archivo excel
            XSSFWorkbook worbook = new XSSFWorkbook(file);
            //obtener la hoja que se va leer
            XSSFSheet sheet = worbook.getSheet("HojaEstatica");

            //extraer datos materiales
            
            this.connect();
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
                else if(!this.db.agrupados.contains(idAgrupado))
                {
                    nombreAgrupado=tratarCaracteresEspeciales(nombreAgrupado);
                    System.out.println(nombreAgrupado);
                    this.executeInsert("SELECT id FROM agrupado WHERE id='"+idAgrupado+"'",
                            "INSERT INTO agrupado(id,nombre) VALUES ('"+idAgrupado+"','"
                                +nombreAgrupado+"')");
                    this.db.agrupados.add(idAgrupado);
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
                else if(!this.db.tiposEnvasados.contains(idTipoEnvasado))
                {
                    this.executeInsert("SELECT id FROM tipoEnvasado WHERE id='"+idTipoEnvasado+"'",
                            "INSERT INTO tipoEnvasado(id,nombre) VALUES ('"+idTipoEnvasado+"','"
                                +nombreTipoEnvasado+"')");
                    this.db.tiposEnvasados.add(idTipoEnvasado);
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
                else if(!this.db.estadosRefrigerados.contains(idestadoRefrigerado))
                {
                    this.executeInsert("SELECT id FROM estadoRefrigerado WHERE id='"+idestadoRefrigerado+"'",
                            "INSERT INTO estadoRefrigerado(id,nombre) VALUES ('"+idestadoRefrigerado+"','"
                                +nombreEstadoRefrigerado+"')");
                    this.db.estadosRefrigerados.add(idestadoRefrigerado);
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
                else if(!this.db.marcas.contains(idMarca))
                {
                    nombreMarca=tratarCaracteresEspeciales(nombreMarca);
                    this.executeInsert("SELECT id FROM marca WHERE id='"+idMarca+"'",
                            "INSERT INTO marca(id,nombre) VALUES ('"+idMarca+"','"
                                +nombreMarca+"')");
                    this.db.marcas.add(idMarca);
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
                else if(!this.db.sectores.contains(idSector))
                {
                    nombreSector=tratarCaracteresEspeciales(nombreSector);
                    this.executeInsert("SELECT id FROM sector WHERE id='"+idSector+"'",
                            "INSERT INTO sector(id,nombre) VALUES ('"+idSector+"','"
                                +nombreSector+"')");
                    this.db.sectores.add(idSector);
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
                else if(!this.db.n2s.contains(idN2))
                {
                    nombreN2=tratarCaracteresEspeciales(nombreN2);
                    this.executeInsert("SELECT id FROM n2 WHERE id='"+idN2+"'",
                            "INSERT INTO n2(id,nombre,sector_id) VALUES ('"+idN2+"','"+nombreN2+"','"+idSector+"')");
                    this.db.n2s.add(idN2);
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
                else if(!this.db.n3s.contains(idN3))
                {
                    nombreN3=tratarCaracteresEspeciales(nombreN3);
                    this.executeInsert("SELECT id FROM n3 WHERE id='"+idN3+"'",
                            "INSERT INTO n3(id,nombre,sector_id,n2_id) VALUES ('"+idN3+"','"+nombreN3+"','"+idSector
                                +"','"+idN2+"')");
                    this.db.n3s.add(idN3);
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
                else if(!this.db.n4s.contains(idN4))
                {
                    nombreN4=tratarCaracteresEspeciales(nombreN4);
                    this.executeInsert("SELECT id FROM n4 WHERE id='"+idN4+"'",
                            "INSERT INTO n4(id,nombre,sector_id,n2_id,n3_id) VALUES ('"+idN4+"','"+nombreN4+"','"+idSector
                                +"','"+idN2+"','"+idN3+"')");
                    this.db.n4s.add(idN4);
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
                else if(!this.db.materiales.contains(idMaterial))
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
                    this.db.materiales.add(idMaterial);
                }
                System.out.println("-----------------------------");
                ctRow++;
            }
        } catch (Exception e) {
            System.out.println("Excepcion actualizando materiales: "+e);
        }
        this.close();
        System.out.println("Contadores:");
        System.out.println("Agrupados: "+ct_agrupados);
        System.out.println("Envasados: "+ct_envasados);
        System.out.println("Refrigerados: "+ct_refrigerados);
        System.out.println("Marcas: "+ct_marcas);
        System.out.println("Sectores: "+ct_sectores);
        System.out.println("N2: "+ct_n2);
        System.out.println("N3: "+ct_n3);
        System.out.println("N4: "+ct_n4);
        System.out.println("Materiales: "+ct_materiales);
        System.out.println("Total rows: "+ctRow);
        System.out.println("---------------");
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

    public boolean validarContenido(String value)
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

    protected String fomatearMes(String month)
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
