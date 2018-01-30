/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model.RecursosDB.ReporteDisponibilidad;

import Model.EstadoRefrigerado;
import Model.Marca;
import Model.Material;
import Model.RecursosDB.RecursoDB;
import Model.RecursosDB.RecursoDB_Empresas;
import Model.Sector;
import Model.TipoAgrupado;
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

    public RecursoDB_Materiales()
    {
        super("Materiales","SELECT * FROM material");
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
                Date fechaAux = result.getDate("fecha");
                int duracionAux = result.getInt("duracion");
                int pesoCajaAux = result.getInt("pesoCaja");
                int activoAux = result.getInt("activo");
                String tipoEnvasadoAux = result.getString("tipoEnvasado");//id
                String estadoRefrigeradoAux = result.getString("estadoRefrigerado_id");//id
                String agrupadoAux = result.getString("agrupado_id");//id
                String sectorAux = result.getString("sector_id");//id
                String marcaAux = result.getString("marca_id");//id
                if(add(new Material(idAux, nombreAux, fechaAux, duracionAux, pesoCajaAux, activoAux,
                    new TipoEnvasado(tipoEnvasadoAux, null), new EstadoRefrigerado(estadoRefrigeradoAux, null),
                    new TipoAgrupado(agrupadoAux, null), new Sector(sectorAux, null), new Marca(marcaAux, null)))
                    ==-1)
                {
                    return false;
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
