/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model.RecursosDB;

import Model.LocalDB;
import Model.N3;
import Model.N4;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Patricio
 */
public class RecursoDB_N4 extends RecursoDB
{

    public RecursoDB_N4(LocalDB db)
    {
        super("N4","SELECT * FROM n4",db);
    }

    @Override
    public boolean obtenerDatos()
    {
        try
        {
            this.connect();
        }
        catch (SQLException | ClassNotFoundException ex)
        {
            Logger.getLogger(RecursoDB_N4.class.getName()).log(Level.SEVERE, null, ex);
        }

        try
        {
            ResultSet result = super.executeQuery();

            //recorrer result para crear objetos
            while (result != null && result.next())
            {
                String idAux = result.getString("id");
                String nombreAux = result.getString("nombre");
                
                String idN3 = result.getString("n3_id");//id
                N3 n3Aux = (this.db.n3s.containsKey(idN3)) ? 
                        this.db.n3s.get(idN3)
                        : this.db.n3s.put(idN3, new N3(idN3));
                
                if(!this.db.n4s.containsKey(idAux))
                {
                    N4 aux = new N4(idAux, nombreAux, n3Aux);
                    this.add(aux);
                    this.db.n4s.put(idAux, aux);
                    System.out.println("n4: "+idAux);
                }
                else
                {
                    this.add(this.db.n4s.get(idAux));
                }
            }
            this.close();
            return true;
        }
        catch (SQLException ex)
        {
            Logger.getLogger(RecursoDB_N4.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }
}