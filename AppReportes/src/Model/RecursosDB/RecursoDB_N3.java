/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model.RecursosDB;

import Model.LocalDB;
import Model.N2;
import Model.N3;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Patricio
 */
public class RecursoDB_N3 extends RecursoDB
{

    public RecursoDB_N3(LocalDB db)
    {
        super("N3","SELECT * FROM n3",db);
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
            Logger.getLogger(RecursoDB_N3.class.getName()).log(Level.SEVERE, null, ex);
        }

        try
        {
            ResultSet result = super.executeQuery();

            //recorrer result para crear objetos
            while (result != null && result.next())
            {
                String idAux = result.getString("id");
                String nombreAux = result.getString("nombre");
                
                String idN2 = result.getString("n2_id");//id
                N2 n2Aux = (this.db.n2s.containsKey(idN2)) ? 
                        this.db.n2s.get(idN2)
                        : this.db.n2s.put(idN2, new N2(idN2));
                
                if(!this.db.n3s.containsKey(idAux))
                {
                    N3 aux = new N3(idAux, nombreAux, n2Aux);
                    this.add(aux);
                    this.db.n3s.put(idAux, aux);
                    System.out.println("n3: "+idAux);
                }
                else
                {
                    this.add(this.db.n3s.get(idAux));
                }
            }
            this.close();
            return true;
        }
        catch (SQLException ex)
        {
            Logger.getLogger(RecursoDB_N3.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }
}