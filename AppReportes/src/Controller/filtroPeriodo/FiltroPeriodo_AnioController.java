/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller.filtroPeriodo;

import Model.CommandNames;
import java.net.URL;
import java.util.Date;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.RadioButton;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 * FXML Controller class: 
 * Controlador para desplegar una ventana de filtro de fechas de año.
 * @author Patricio
 */
public class FiltroPeriodo_AnioController extends FiltroPeriodoController
{
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

        this.comboBox_fechaInicio.setItems(this.itemsInicio);
        this.comboBox_fechaFin.setItems(this.itemsFin);
        
        for (int i = MIN_YEAR; i < MAX_YEAR; i++)
        {
            this.itemsInicio.add(i);
            this.itemsFin.add(i+1);
        }
        this.comboBox_fechaInicio.getSelectionModel().select(0);
        this.comboBox_fechaFin.getSelectionModel().select(0);
    }
    
    /**
     * Método que modifica los valores permitidos por el campo del año limite para seleccionar: esto de acuerdo a lo 
     * seleccionado por el año inicial.
     */
    @FXML
    public void updateOptionCB2()
    {
        int aux = Integer.parseInt(this.comboBox_fechaInicio.getSelectionModel().getSelectedItem().toString());
        this.itemsFin.clear();
        for (int i = aux ; i < MAX_YEAR; i++)
        {
            this.itemsFin.add(i+1);
        }
        this.comboBox_fechaFin.getSelectionModel().select(0);
    }

    /**
     * Método que realiza modificaciones de acuerdo a la opción seleccionada por el usuario: si es por un año en específico, 
     * se permite sólo modificar el año inicial, en caso que sea por rango de años, se permite modificar año inicio y término.
     */
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
        else//cuando se deselecciona una opcion
        {
            switch(this.opcion)
            {
                case 1:
                    this.radioButtonA.setSelected(true);
                    break;
                case 2:
                    this.radioButtonB.setSelected(true);
                    break;
            }
            System.out.println("caso raro");
        }
        this.comboBox_fechaInicio.getSelectionModel().select(0);
        this.comboBox_fechaFin.getSelectionModel().select(0);
    }

    /**
     * Método invocado por un Button. Limpia el filtro fecha del reporte base, lo modifica de acuerdo a las opciones 
     * ingresadas del usuario y cierra la ventana. Para el caso de rango de años, si el rango es erróneo (año inicio mayor a
     *  año término) arroja un mensaje y no modifica el filtro ya existente.
     */
    @FXML
    public void buttonAceptar()
    {
        int inicio = Integer.parseInt(this.comboBox_fechaInicio.getSelectionModel().getSelectedItem().toString());
        int fin = Integer.parseInt(this.comboBox_fechaFin.getSelectionModel().getSelectedItem().toString());
        switch(this.opcion)
        {
            case 1:
                this.filtro.vaciarFiltro();
                this.filtro.setFechaInicio(new Date(inicio, 1, 1));
                this.filtro.setOpcion(1);
                this.filtro.prepararFiltro();
                this.buttonCancelar();
                break;
            case 2:
                if(inicio<fin)
                {
                    this.filtro.vaciarFiltro();
                    this.filtro.setFechaInicio(new Date(inicio, 1, 1));
                    this.filtro.setFechaFin(new Date(fin, 1, 1));
                    this.filtro.setOpcion(2);
                    this.filtro.prepararFiltro();
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

    /**
     * Método que cierra la ventana asociada al controlador.
     */
    @FXML
    public void buttonCancelar()
    {
        Stage s = (Stage) this.button_Cancelar.getScene().getWindow();
        s.close();
    }

}
