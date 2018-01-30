/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model.PobladorDB.Threads;

import Model.LocalDB;
import java.util.HashMap;
import org.apache.poi.ss.usermodel.Row;

/**
 *
 * @author Patricio
 */
public abstract class ThreadProcess implements Runnable
{
    public int id;
    public LocalDB db;
    public boolean flagExterno;
    public boolean flagPropio;
    public Buzon buzon;
    

    public ThreadProcess(int id, LocalDB db, Buzon buzon)
    {
        this.id = id;
        this.db = db;
        this.buzon = buzon;
        
        this.flagExterno=true;
        this.flagPropio=true;
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
}
