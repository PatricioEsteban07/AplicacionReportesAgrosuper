/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model.RecursosDB;

import Model.Agrupado;
import Model.EstadoRefrigerado;
import Model.LocalDB;
import Model.Marca;
import Model.Material;
import Model.Sector;
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
public class RecursoDB_Materiales extends RecursoDB
{

    public RecursoDB_Materiales(LocalDB db)
    {
        super("Materiales","SELECT * FROM material",db);
    }

    @Override
    public boolean obtenerDatos(HashMap<String, RecursoDB> resources)
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
                String nombreAux = result.getString("nombre");
                Date fechaAux = result.getDate("fechaCreacion");
                int pesoCajaAux = result.getInt("pesoCaja");
                int duracionAux = result.getInt("duracion");
                
                String idEnvasado = result.getString("tipoEnvasado_id");//id
                TipoEnvasado envasadoAux = (this.db.tiposEnvasados.containsKey(idEnvasado)) ? 
                        this.db.tiposEnvasados.get(idEnvasado) 
                        : this.db.tiposEnvasados.put(idEnvasado, new TipoEnvasado(idEnvasado));
                
                String idRefrigerado = result.getString("estadoRefrigerado_id");//id
                EstadoRefrigerado refrigeradoAux = (this.db.estadosRefrigerados.containsKey(idRefrigerado)) ? 
                        this.db.estadosRefrigerados.get(idRefrigerado) 
                        : this.db.estadosRefrigerados.put(idRefrigerado, new EstadoRefrigerado(idRefrigerado));
                
                String idAgrupado = result.getString("agrupado_id");//id
                Agrupado agrupadoAux = (this.db.agrupados.containsKey(idAgrupado)) ? 
                        this.db.agrupados.get(idAgrupado)
                        : this.db.agrupados.put(idAgrupado, new Agrupado(idAgrupado));
                
                String idSector = result.getString("sector_id");//id
                Sector sectorAux = (this.db.sectores.containsKey(idSector)) ? 
                        this.db.sectores.get(idSector)
                        : this.db.sectores.put(idSector, new Sector(idSector));
                
                String idMarca = result.getString("marca_id");//id
                Marca marcaAux = (this.db.marcas.containsKey(idMarca)) ? 
                        this.db.marcas.get(idMarca)
                        : this.db.marcas.put(idMarca, new Marca(idMarca));
                
                if(!this.db.materiales.containsKey(idAux))
                {
                    Material aux = new Material(idAux, nombreAux,fechaAux, pesoCajaAux, sectorAux, duracionAux, 
                            refrigeradoAux, agrupadoAux, envasadoAux, marcaAux);
                    this.add(aux);
                    this.db.materiales.put(idAux, aux);
                }
                else
                {
                    this.add(this.db.materiales.get(idAux));
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
