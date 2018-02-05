/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model.RecursosDB;

import Model.LocalDB;
import Model.Marca;
import Model.N2;
import Model.Sector;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Patricio
 */
public class RecursoDB_N2 extends RecursoDB
{

    public RecursoDB_N2(LocalDB db)
    {
        super("N2","SELECT * FROM n2",db);
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
            Logger.getLogger(RecursoDB_N2.class.getName()).log(Level.SEVERE, null, ex);
        }

        try
        {
            ResultSet result = super.executeQuery();

            //recorrer result para crear objetos
            while (result != null && result.next())
            {
                String idAux = result.getString("id");
                String nombreAux = result.getString("nombre");
                
                String idSector = result.getString("sector_id");//id
                Sector sectorAux = (this.db.sectores.containsKey(idSector)) ? 
                        this.db.sectores.get(idSector)
                        : this.db.sectores.put(idSector, new Sector(idSector));
                
                if(!this.db.n2s.containsKey(idAux))
                {
                    N2 aux = new N2(idAux, nombreAux, sectorAux);
                    this.add(aux);
                    this.db.n2s.put(idAux, aux);
                    System.out.println("n2: "+idAux);
                }
                else
                {
                    this.add(this.db.n2s.get(idAux));
                }
            }
            this.close();
            return true;
        }
        catch (SQLException ex)
        {
            Logger.getLogger(RecursoDB_N2.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }
}