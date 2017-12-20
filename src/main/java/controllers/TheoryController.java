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
import javafx.scene.control.MenuButton;
import javafx.scene.control.TextArea;
import javafx.scene.web.WebView;


public class TheoryController {
    
   
    
    @FXML
    private WebView theoryText;
    
    @FXML
    private MenuButton mainButton;
    
    
    @FXML
    private void lagrangeTheoryAction(ActionEvent event) throws Exception { 
        
        readTextFromFile("Lagrange.htm");
        mainButton.setText("Lagrange interpoláció");
    }
    
     @FXML
    private void newtonTheoryAction(ActionEvent event) throws Exception {
            readTextFromFile("Newton.htm");
            mainButton.setText("Osztott differenciák és Newton alak");
    }
      @FXML
    private void inverzTheoryAction(ActionEvent event) throws Exception {
            readTextFromFile("Inverz.htm");
            mainButton.setText("Inverz interpoláció");
    }
    
      @FXML
    private void hermiteTheoryAction(ActionEvent event) throws Exception {
            readTextFromFile("Hermite.htm");
            mainButton.setText("Hermite-interpoláció");
    }
      @FXML
    private void splineTheoryAction(ActionEvent event) throws Exception {
            readTextFromFile("Spline.htm");
            mainButton.setText("Spline-interpoláció");
    }
    
    
    
   
   private void readTextFromFile(String fileName) throws FileNotFoundException, IOException{
    
        theoryText.getEngine().load(getClass().getResource("/texts/"+fileName).toExternalForm());
                      
    }
    
    
    
}
