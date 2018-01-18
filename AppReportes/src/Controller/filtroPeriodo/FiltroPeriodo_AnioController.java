/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller.filtroPeriodo;

import Model.CommandNames;
import Model.Filtros.Filtro;
import Model.Filtros.Filtro_Fecha;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.RadioButton;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Patricio
 */
public class FiltroPeriodo_AnioController implements Initializable
{
    private int opcion;
    private final int MIN_YEAR = 2015;
    private final int MAX_YEAR = 2031;
    private Filtro_Fecha filtro;

    @FXML
    private Button button_Aceptar;
    @FXML
    private Button button_Cancelar;
    @FXML
    private RadioButton radioButtonA;
    @FXML
    private RadioButton radioButtonB;
    @FXML
    private ComboBox comboBox_fechaInicio;
    @FXML
    private ComboBox comboBox_fechaFin;
    @FXML
    private Text text_FechaInicio;
    @FXML
    private Text text_FechaFin;

    private ObservableList<Integer> itemsInicio;
    private ObservableList<Integer> itemsFin;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
        this.radioButtonA.setSelected(true);
        this.opcion = 1;
        this.itemsInicio = FXCollections.observableArrayList();
        this.itemsFin = FXCollections.observableArrayList();

        for (int i = MIN_YEAR; i < MAX_YEAR; i++)
        {
            this.itemsInicio.add(i);
            this.itemsFin.add(i);
        }
        this.comboBox_fechaInicio.setItems(this.itemsInicio);
        this.comboBox_fechaFin.setItems(this.itemsFin);
        this.comboBox_fechaInicio.getSelectionModel().select(0);
        this.comboBox_fechaFin.getSelectionModel().select(0);
    }

    @FXML
    public void updateOptionCB2()
    {
        int aux = Integer.parseInt(this.comboBox_fechaInicio.getSelectionModel().getSelectedItem().toString());
        this.itemsFin.clear();
        for (int i = aux + 1; i < MAX_YEAR; i++)
        {
            this.itemsFin.add(i);
        }
        this.comboBox_fechaFin.getSelectionModel().select(0);
    }

    @FXML
    public void setRadioButtonOption()
    {
        if (this.opcion == 2 && this.radioButtonA.isSelected())//por especifico
        {
            System.out.println("caso a");
            this.text_FechaInicio.setText("Año:");
            this.text_FechaFin.setVisible(false);
            this.comboBox_fechaFin.setVisible(false);

            this.radioButtonB.setSelected(false);
            this.opcion = 1;
        }
        else if (this.opcion == 1 && this.radioButtonB.isSelected())//por rango
        {
            System.out.println("caso b");
            this.text_FechaInicio.setText("Año Inicio:");
            this.text_FechaFin.setVisible(true);
            this.comboBox_fechaFin.setVisible(true);

            this.radioButtonA.setSelected(false);
            this.opcion = 2;
        }
        else//casos raros, manejar!
        {
            System.out.println("caso raro");
        }
        this.comboBox_fechaInicio.getSelectionModel().select(0);
        this.comboBox_fechaFin.getSelectionModel().select(0);
    }

    @FXML
    public void buttonAceptar()
    {
        //implementar!
        int inicio = Integer.parseInt(this.comboBox_fechaInicio.getSelectionModel().getSelectedItem().toString());
        int fin = Integer.parseInt(this.comboBox_fechaFin.getSelectionModel().getSelectedItem().toString());
        switch(this.opcion)
        {
            case 1:
                this.filtro.vaciarFiltro();
                this.filtro.addAnio(inicio);
                this.filtro.setOpcion(1);
                System.out.println("Num: "+inicio);
                                    for (Integer anio : this.filtro.getAnios())
                                    {
                                        System.out.println("Año: "+anio);
                                    }
                this.buttonCancelar();
                break;
            case 2:
                if(inicio<fin)
                {
                    this.filtro.vaciarFiltro();
                    for (int i = inicio; i < fin+1; i++)
                    {
                        this.filtro.addAnio(i);
                    }
                    System.out.println("paso :D"+inicio+"/"+fin);              
                                    for (Integer anio : this.filtro.getAnios())
                                    {
                                        System.out.println("Año: "+anio);
                                    }
                    this.filtro.setOpcion(2);
                    this.buttonCancelar();
                }
                else
                {
                    CommandNames.generaMensaje("Mensaje del Sistema", Alert.AlertType.INFORMATION, 
                            "Años seleccionados erróneos", "Debe seleccionar el un rango de años tal que el inicio sea menor al final. "
                                    + "En caso que sólo necesite filtrar por un año cambie su opción y reintente nuevamente.");
                }
                break;
        }
    }

    @FXML
    public void buttonCancelar()
    {
        Stage s = (Stage) this.button_Cancelar.getScene().getWindow();
        s.close();
    }

    public void setFiltro(Filtro filtro)
    {
        this.filtro=(Filtro_Fecha)filtro;
    }

}
