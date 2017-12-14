package interpolationapplication;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;


public class Exercise extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/Exercise1FXML.fxml"));
        
        Scene scene = new Scene(root);
        stage.setResizable(false);
        stage.setScene(scene);
         stage.getIcons().add(new Image(this.getClass().getResourceAsStream("/img/icon.png")));
         stage.setTitle("Gyakorlat");
        
        stage.show();
    }
    
}
