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
public class CSVImport_Sector extends CSVImport
{

    public CSVImport_Sector(LocalDB db, String fileDir, String fileName)
    {
        super(db, 8);
        this.fileDir=fileDir;
        this.fileName=fileName;
        this.tableName="sector";
        completaTypes();
    }

    public void completaTypes()
    {
        this.types.add("ID");
        this.types.add("STRING");
    }

}
