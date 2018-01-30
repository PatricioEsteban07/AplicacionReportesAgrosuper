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
public class RecursoDB_TablaPedidos extends RecursoDB
{

    public RecursoDB_TablaPedidos()
    {
        super("Pedidos","SELECT * FROM pedido");
    }

    @Override
    public boolean obtenerDatos(HashMap<String, RecursoDB> resources)
    {
        return false;
    }
    
}
