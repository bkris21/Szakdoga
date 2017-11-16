package controllers;

import interpolationapplication.Exercise;
import interpolationapplication.Theory;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.Scanner;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.web.WebView;
import javafx.stage.Stage;


public class TheoryController implements Initializable {
    
   
    
    @FXML
    private WebView theoryText;
    
    
    @FXML
    private void lagrangeTheoryAction(ActionEvent event) throws Exception { 
        
        readTextFromFile("Try.html");
    }
    
     @FXML
    private void newtoneTheoryAction(ActionEvent event) throws Exception {
           
    }
    
    
    
    @FXML
    private void zoom(){
        
    }
     
    @Override
    public void initialize(URL url, ResourceBundle rb) {
      
    }   
    
    public void readTextFromFile(String fileName) throws FileNotFoundException, IOException{
    
        theoryText.getEngine().load(getClass().getResource("/texts/"+fileName).toExternalForm());
                      
    }
    
    
    
}
