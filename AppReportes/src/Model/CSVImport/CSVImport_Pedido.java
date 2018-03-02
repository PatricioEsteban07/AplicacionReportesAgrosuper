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
public class CSVImport_Pedido extends CSVImport
{

    public CSVImport_Pedido(LocalDB db, String fileDir, String fileName)
    {
        super(db, 8);
        this.fileDir=fileDir;
        this.fileName=fileName;
        this.tableName="pedido";
        completaTypes();
    }

    public void completaTypes()
    {
        this.types.add("ID");
        this.types.add("DATE");
        this.types.add("ID");
        this.types.add("ID");
        this.types.add("ID");
        this.types.add("DATE");
    }

}