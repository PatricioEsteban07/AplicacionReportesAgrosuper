/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model.RecursosDB;

import Model.Agrupado;
import Model.Centro;
import Model.ClienteLocal;
import Model.Despacho;
import Model.EstadoRefrigerado;
import Model.LocalDB;
import Model.Marca;
import Model.Material;
import Model.Sector;
import Model.TipoCliente;
import Model.TipoEnvasado;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Patricio
 */
public class RecursoDB_Despachos extends RecursoDB
{

    public RecursoDB_Despachos(LocalDB db)
    {
        super("Despachos","SELECT * FROM despacho",db);
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
            Logger.getLogger(RecursoDB_Empresas.class.getName()).log(Level.SEVERE, null, ex);
        }
        try
        {
            ResultSet result = super.executeQuery();

            //recorrer result para crear objetos
            while (result != null && result.next())
            {
                String idAux = result.getString("id");
                Date fechaAux = result.getDate("fecha");
                
                String idCentro = result.getString("centro_id");//id
                Centro centroAux = (this.db.centros.containsKey(idCentro)) ? 
                        this.db.centros.get(idCentro)
                        : this.db.centros.put(idCentro, new Centro(idCentro));
                
                String idClienteLocal = result.getString("clienteLocal_id");//id
                ClienteLocal clienteLocalAux = (this.db.clientesLocales.containsKey(idClienteLocal)) ? 
                        this.db.clientesLocales.get(idClienteLocal)
                        : this.db.clientesLocales.put(idClienteLocal, new ClienteLocal(idClienteLocal));
                
                if(!this.db.despachos.containsKey(idAux))
                {
                    Despacho aux = new Despacho(idAux,centroAux,fechaAux,clienteLocalAux);
                    this.add(aux);
                    this.db.despachos.put(idAux, aux);
                }
                else
                {
                    this.add(this.db.despachos.get(idAux));
                }
            }
            this.close();
            return true;
        }
        catch (SQLException ex)
        {
            Logger.getLogger(RecursoDB_Empresas.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }
    
}
