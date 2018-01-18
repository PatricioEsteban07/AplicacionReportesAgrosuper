/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 *
 * @author Patricio
 */
public class Main extends Application {
    private static Main instance;
    
    public static Main getInstance(){
        return instance;
    }
    
    private Stage primaryStage;
    
    @Override
    public void start(Stage stage) throws Exception {
        instance = this;
        primaryStage = stage;
        Parent root = FXMLLoader.load(getClass().getResource("/Views/Main.fxml"));
        
        Scene scene = new Scene(root);
        
        stage.setScene(scene);
        this.updateTitle();
        stage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException {
        launch(args);
        
        /*
        Filtro_Fecha f=new Filtro_Fecha();
        f.setFechaInicio(new Date(2010,04,05));
        //f.fechaFin=new Date(2010,10,05);
        HashMap<String,String> data=new HashMap<>();
        data.put("Dia","dia");
        data.put("Mes","mes");
        data.put("Año","año");
        data.put("Semana","semana");
        data.put("Fecha","fecha");
        System.out.println(f.generarWhere(data));
        System.exit(0);
        */
        
        //código para implementar exportación CSV->DB
        //IMPLEMENTAR
        //lecturaArchivoPrueba();
    }
    
    public void updateTitle(){
        primaryStage.setTitle("Sistema de generación de reportes Agrosuper");
    }
    
    public String getTitle(){
        return primaryStage.getTitle();
    }
 
    public static void lecturaArchivoPrueba() throws FileNotFoundException, IOException
    {
        String fileName="ejemplo";
         BufferedReader br =new BufferedReader(new FileReader(System.getProperty("user.home") + "/Desktop/" + fileName + ".xls"));
         String line = br.readLine();
         while (null!=line) {
            String [] fields = line.split(";");
            System.out.println(Arrays.toString(fields));
            
            fields = removeTrailingQuotes(fields);
            System.out.println(Arrays.toString(fields));
            
         }
    }
    
    private static String[] removeTrailingQuotes(String[] fields) {

      String result[] = new String[fields.length];

      for (int i=0;i<result.length;i++){
         result[i] = fields[i].replaceAll("^"+"\"", "").replaceAll("\""+"$", "");
      }
      return result;
   }
    
}
