/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model.RecursosDB.ReporteDisponibilidad;

import Model.LocalDB;
import Model.RecursosDB.RecursoDB;
import java.util.HashMap;

/**
 *
 * @author Patricio
 */
public class RecursoDB_TablaPedidos extends RecursoDB
{

    public RecursoDB_TablaPedidos(LocalDB db)
    {
        super("Pedidos","SELECT * FROM pedido",db);
    }

    @Override
    public boolean obtenerDatos(HashMap<String, RecursoDB> resources)
    {
        return false;
    }
    
}
