/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model.PobladorDB;

import java.io.File;

/**
 *
 * @author Patricio
 */
public abstract class PobladorDB
{
    public final String dirBase=System.getProperty("user.home")+"/Desktop/";

    public PobladorDB()
    {

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

}
