package interpolationapplication;

import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class Theory extends Application{
    

    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/TheoryFXML.fxml"));
        
        Scene scene = new Scene(root);
       
        stage.setResizable(false);
        stage.setScene(scene);
        stage.show();
    }

    
    
    
    
    
}
