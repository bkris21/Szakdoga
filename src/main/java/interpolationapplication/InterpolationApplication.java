package interpolationapplication;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class InterpolationApplication extends Application {

    
    @Override
    public void start(Stage stage) throws Exception {

        Parent root = FXMLLoader.load(getClass().getResource("/fxml/InterpolationApplicatinFXML.fxml"));

        Scene scene = new Scene(root);

        stage.setScene(scene);
        stage.setResizable(false);
        
        
       stage.getIcons().add(new Image(this.getClass().getResourceAsStream("/img/icon.png")));
       
        
        stage.show();
    }

  

    public static void main(String[] args) {
        launch(args);
    }

}
