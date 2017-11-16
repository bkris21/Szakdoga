
package controllers;

import interpolationapplication.Exercise;
import interpolationapplication.InterpolationApplication;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import interpolationapplication.Theory;
import javafx.stage.Stage;

public class InterpolationApplicationController implements Initializable {
    
    private InterpolationApplication interpolationApplication;

    
    @FXML
    private void buttonActionTheory(ActionEvent event) throws Exception {
      interpolationApplication= new InterpolationApplication(new Theory());
      interpolationApplication.getTheory().start(new Stage());
     
    }
    
    
     @FXML
    private void buttonActionExercise(ActionEvent event) throws Exception {
     
         new Exercise().start(new Stage());
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
    }    

   
    
    
}
