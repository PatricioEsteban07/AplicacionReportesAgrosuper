/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model.PobladorDB.Threads;

import Model.LocalDB;
import org.apache.poi.ss.usermodel.Row;

/**
 *
 * @author Patricio
 */
public class ThreadProcess_Pedidos extends ThreadProcess
{
    public ThreadProcess_Pedidos(int id, LocalDB db, Buzon buzon)
    {
        super(id,db,buzon);
    }

    @Override
    public void run()
    {
        while(this.flagExterno || this.flagPropio)
        {
            Row row=this.buzon.obtenerJob();
            if(row==null && !this.flagExterno)
            {
                this.flagPropio=false;
            }
            //obtengo una row
            if(row!=null)
            {
            }
        }
    }
    
}
