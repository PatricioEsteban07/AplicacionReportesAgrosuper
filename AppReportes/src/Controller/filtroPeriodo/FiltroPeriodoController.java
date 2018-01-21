/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller.filtroPeriodo;

import Model.Filtros.Filtro;
import Model.Filtros.Filtro_Fecha;
import javafx.fxml.Initializable;

/**
 * FXML Controller class
 *
 * @author Patricio
 */
public abstract class FiltroPeriodoController implements Initializable
{
    protected int opcion;
    protected final int MIN_YEAR = 2015;
    protected final int MAX_YEAR = 2030;
    protected final int MIN_MONTH = 1;
    protected final int MAX_MONTH = 12;
    protected final int MIN_WEEKS = 1;
    protected final int MAX_WEEKS = 52;
    protected final String MIN_DATE = "01-01-"+MIN_YEAR;
    protected final String MAX_DATE = "01-01-"+MAX_YEAR;
    protected Filtro_Fecha filtro;

    public void setFiltro(Filtro filtro)
    {
        this.filtro=(Filtro_Fecha)filtro;
    }

}
