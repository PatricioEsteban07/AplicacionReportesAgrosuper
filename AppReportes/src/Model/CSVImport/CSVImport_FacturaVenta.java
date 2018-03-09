/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model.CSVImport;

import Model.LocalDB;

/**
 * Clase encargada de la importación de CSV para la tabla Factura Ventas.
 * @author Patricio
 */
public class CSVImport_FacturaVenta extends CSVImport
{

    public CSVImport_FacturaVenta(LocalDB db, String fileDir, String fileName)
    {
        super(db, 8);
        this.fileDir=fileDir;
        this.fileName=fileName;
        this.tableName="facturaVentas";
        completaTypes();
    }

    /**
     * Método que completa un listado de CSVImport con los tipos de datos de cada columna para la tabla que le corresponde
     */
    public void completaTypes()
    {
        this.types.add("ID");
        this.types.add("ID");
        this.types.add("ID");
        this.types.add("ID");
        this.types.add("DATE");
        this.types.add("ID");
        this.types.add("STRING");
    }

}
