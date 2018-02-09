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
 * FXML Controller class
 *
 * @author Patricio
 */
public class FiltroPeriodo_SemanaController extends FiltroPeriodoController
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
    private ComboBox comboBox_anioInicio;
    @FXML
    private ComboBox comboBox_semanaInicio;
    @FXML
    private ComboBox comboBox_anioFin;
    @FXML
    private ComboBox comboBox_semanaFin;
    @FXML
    private Text text_periodoInicio;
    @FXML
    private Text text_periodoFin;
    @FXML
    private Text text_mark;

    private ObservableList<Integer> aniosInicio;
    private ObservableList<Integer> aniosFin;
    private ObservableList<Integer> semanaInicio;
    private ObservableList<Integer> semanaFin;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
        this.radioButtonA.setSelected(true);
        this.opcion = 1;
        this.aniosInicio = FXCollections.observableArrayList();
        this.aniosFin = FXCollections.observableArrayList();
        this.semanaInicio = FXCollections.observableArrayList();
        this.semanaFin = FXCollections.observableArrayList();

        this.comboBox_anioInicio.setItems(this.aniosInicio);
        this.comboBox_anioFin.setItems(this.aniosFin);
        this.comboBox_semanaInicio.setItems(this.semanaInicio);
        this.comboBox_semanaFin.setItems(this.semanaFin);
        
        for (int i = MIN_YEAR; i < MAX_YEAR; i++)
        {
            this.aniosInicio.add(i);
            this.aniosFin.add(i);
        }
        for (int i = MIN_WEEKS; i <= MAX_WEEKS; i++)
        {
            this.semanaInicio.add(i);
            this.semanaFin.add(i);
        }
        this.comboBox_anioInicio.getSelectionModel().select(0);
        this.comboBox_anioFin.getSelectionModel().select(0);
        this.comboBox_semanaInicio.getSelectionModel().select(0);
        this.comboBox_semanaFin.getSelectionModel().select(1);
    }
    

    @FXML
    public void updateOptionCB2()
    {
        int auxAnio = Integer.parseInt(this.comboBox_anioInicio.getSelectionModel().getSelectedItem().toString());
        int auxMes = Integer.parseInt(this.comboBox_semanaInicio.getSelectionModel().getSelectedItem().toString());
        this.aniosFin.clear();
        this.semanaFin.clear();
        for (int i = auxAnio ; i < MAX_YEAR+1; i++)
        {
            this.aniosFin.add(i);
        }
        for (int i = auxMes ; i < MAX_WEEKS; i++)
        {
            this.semanaFin.add(i+1);
        }
        this.comboBox_anioFin.getSelectionModel().select(0);
        this.comboBox_semanaFin.getSelectionModel().select(0);
    }

    @FXML
    public void setRadioButtonOption()
    {
        if (this.opcion == 2 && this.radioButtonA.isSelected())//por mes especifico
        {
            System.out.println("caso a");
            this.text_periodoInicio.setText("Período (Mes/Año)");
            this.text_periodoFin.setVisible(false);
            this.text_mark.setVisible(false);
            this.comboBox_semanaFin.setVisible(false);
            this.comboBox_anioFin.setVisible(false);

            this.radioButtonB.setSelected(false);
            this.opcion = 1;
        }
        else if (this.opcion == 1 && this.radioButtonB.isSelected())//por rango mes
        {
            System.out.println("caso b");
            this.text_periodoInicio.setText("Inicio (Mes/Año)");
            this.text_periodoFin.setVisible(true);
            this.text_mark.setVisible(true);
            this.comboBox_semanaFin.setVisible(true);
            this.comboBox_anioFin.setVisible(true);

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
        this.comboBox_anioInicio.getSelectionModel().select(0);
        this.comboBox_semanaInicio.getSelectionModel().select(0);
        this.comboBox_anioFin.getSelectionModel().select(0);
        this.comboBox_semanaFin.getSelectionModel().select(0);
    }

    @FXML
    public void buttonAceptar()
    {
        //implementar!
        int semanaInicio = Integer.parseInt(this.comboBox_semanaInicio.getSelectionModel().getSelectedItem().toString());
        int semanaFin = Integer.parseInt(this.comboBox_semanaFin.getSelectionModel().getSelectedItem().toString());
        int anioInicio = Integer.parseInt(this.comboBox_anioInicio.getSelectionModel().getSelectedItem().toString());
        int anioFin = Integer.parseInt(this.comboBox_anioFin.getSelectionModel().getSelectedItem().toString());
        switch(this.opcion)
        {
            case 1:
                this.filtro.vaciarFiltro();
                this.filtro.setFechaInicio(new Date(anioInicio, 1, 1));
                this.filtro.setSemanas(semanaInicio,0);
                this.filtro.setOpcion(5);
                this.filtro.prepararFiltro();
                this.buttonCancelar();
                break;
            case 2:
                if(validarRango(semanaInicio,anioInicio,semanaFin,anioFin))
                {
                    this.filtro.vaciarFiltro();
                    this.filtro.setFechaInicio(new Date(anioInicio, 1, 1));
                    this.filtro.setFechaFin(new Date(anioFin, 1, 1));
                    this.filtro.setSemanas(semanaInicio,semanaFin);
                    this.filtro.setOpcion(6);
                    this.filtro.prepararFiltro();
                    this.buttonCancelar();
                }
                else
                {
                    CommandNames.generaMensaje("Mensaje del Sistema", Alert.AlertType.INFORMATION, 
                            "Años seleccionados erróneos", "Debe seleccionar el un rango de semanas tal que el tramo inicial sea menor al final. "
                                    + "En caso que sólo necesite filtrar por un mes cambie su opción y reintente nuevamente.");
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

    private boolean validarRango(int semanaInicio, int anioInicio, int semanaFin, int anioFin)
    {
        if(anioInicio<=anioFin)
        {
            //rango de año correcto
            if(anioInicio==anioFin && semanaInicio>=semanaFin)
                return false;
            return true;
        }
        return false;
    }
}
