/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model.CSVImport;

import Model.LocalDB;

/**
 *
 * @author Patricio
 */
public class CSVImport_DespachoMaterial extends CSVImport
{

    public CSVImport_DespachoMaterial(LocalDB db, String fileDir, String fileName)
    {
        super(db, 9);
        this.fileDir=fileDir;
        this.fileName=fileName;
        this.tableName="despacho_material";
        completaTypes();
    }

    public void completaTypes()
    {
        this.types.add("ID");
        this.types.add("ID");
        this.types.add("FLOAT");
        this.types.add("FLOAT");
    }

}