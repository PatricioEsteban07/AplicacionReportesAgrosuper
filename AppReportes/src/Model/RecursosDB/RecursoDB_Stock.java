/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model.RecursosDB;

import Model.Agrupado;
import Model.Centro;
import Model.EstadoRefrigerado;
import Model.LocalDB;
import Model.Marca;
import Model.Material;
import Model.Sector;
import Model.Stock;
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
public class RecursoDB_Stock extends RecursoDB
{

    public RecursoDB_Stock(LocalDB db)
    {
        super("Stock","SELECT * FROM stock",db);
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
                Date fechaAux = result.getDate("fecha");
                float salidasAux = result.getFloat("salidas");
                float stockAux = result.getFloat("stock");
                float disponibleAux = result.getFloat("disponible");
                
                String idMaterial = result.getString("material_id");//id
                Material materialAux = (this.db.materiales.containsKey(idMaterial)) ? 
                        this.db.materiales.get(idMaterial)
                        : this.db.materiales.put(idMaterial, new Material(idMaterial));
                
                String idCentro = result.getString("centro_id");//id
                Centro centroAux = (this.db.centros.containsKey(idCentro)) ? 
                        this.db.centros.get(idCentro)
                        : this.db.centros.put(idCentro, new Centro(idCentro));
                
                String idAux = idCentro+fechaAux+idMaterial;
                
                if(!this.db.stocks.containsKey(idAux))
                {
                    Stock aux = new Stock(idAux,centroAux,fechaAux,materialAux,salidasAux,stockAux,disponibleAux);
                    this.add(aux);
                    this.db.stocks.put(idAux, aux);
                }
                else
                {
                    this.add(this.db.stocks.get(idAux));
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
