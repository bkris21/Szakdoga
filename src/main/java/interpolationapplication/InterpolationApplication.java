package interpolationapplication;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class InterpolationApplication extends Application {

    private Theory theory;

    public InterpolationApplication() {

    }

    public InterpolationApplication(Theory theory) {
        this.theory = theory;
    }

    @Override
    public void start(Stage stage) throws Exception {

        Parent root = FXMLLoader.load(getClass().getResource("/fxml/InterpolationApplicatinFXML.fxml"));

        Scene scene = new Scene(root);

        stage.setScene(scene);
        stage.show();
    }

    public Theory getTheory() {
        return theory;
    }

    public static void main(String[] args) {
        launch(args);
    }

}
