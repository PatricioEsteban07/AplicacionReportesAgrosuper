/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model.PobladorDB.CSVImport;

import Model.LocalDB;

/**
 *
 * @author Patricio
 */
public class CSVImport_Material extends CSVImport
{

    public CSVImport_Material(LocalDB db, String fileDir, String fileName)
    {
        super(db, 8);
        this.fileDir=fileDir;
        this.fileName=fileName;
        this.tableName="material";
        completaTypes();
    }

    public void completaTypes()
    {
        this.types.add("ID");
        this.types.add("STRING");
        this.types.add("DATE");
        this.types.add("FLOAT");
        this.types.add("ID");
        this.types.add("INT");
        this.types.add("ID");
        this.types.add("ID");
        this.types.add("ID");
        this.types.add("ID");
    }

}
