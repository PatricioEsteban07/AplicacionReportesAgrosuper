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
public class CSVImport_Region extends CSVImport
{

    public CSVImport_Region(LocalDB db, String fileDir, String fileName)
    {
        super(db, 1);
        this.fileDir=fileDir;
        this.fileName=fileName;
        this.tableName="region";
        completaTypes();
    }

    public void completaTypes()
    {
        this.types.add("ID");
        this.types.add("STRING");
        this.types.add("ID");
    }


}
