package controllers;

import interpolationapplication.coordinatesystem.CoordinateSystem;
import java.net.URL;

import java.util.LinkedHashMap;

import java.util.Map;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.stage.Stage;

import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;
import net.objecthunter.exp4j.tokenizer.UnknownFunctionOrVariableException;
import net.objecthunter.exp4j.function.Function;

public class ExerciseController implements Initializable {
    
    private InterpolationAlgorithms ia;
    
    private Function f;
    private Point interval;
    
    @FXML
    private TextArea resultText;
    
    @FXML
    private TextField xOne, xTwo, xThree, xFour, xFive, yOne, yTwo, yThree, yFour, yFive;
    
    @FXML
    private TextField intervalField;
    
    @FXML
    private RadioButton lagrangeButton, csebisevButton, newtonButton, inverzButton, hermiteButton;
    
    @FXML
    private ToggleGroup radioGroup;
    
    private Map<Double, Double> numberPairs = new LinkedHashMap<>();
    private Map<TextField, TextField> textFields = new LinkedHashMap<>();
    
    @FXML
    void goInterpolation(ActionEvent event) {
        numberPairs.clear();
        resultText.clear();
        readTextFields();
       
        ia = new InterpolationAlgorithms((LinkedHashMap<Double, Double>) numberPairs);
        
        if (lagrangeButton.isSelected()) {
            f = ia.lagrangeInterpolation();
            resultText.setText("L(x)=" + ia.lagrangeStringFunction());
        }
        if (newtonButton.isSelected()) {
            f = ia.newtonInterpolation();
            resultText.setText("Nk(x)=" + ia.newtonStringFunction());
        }
    }
    
    @FXML
    void openCoordinateSystem(ActionEvent event) throws Exception {
        try {
             interval=readInterval();
                          
            new CoordinateSystem(f,interval.getX(),interval.getY()).start(new Stage());
        } catch (NullPointerException npe) {
            somethingWrong("Először nyomd meg a 'Mehet' gombot!");
        }
    }
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        
        lagrangeButton.setToggleGroup(radioGroup);
        newtonButton.setToggleGroup(radioGroup);
        csebisevButton.setToggleGroup(radioGroup);
        inverzButton.setToggleGroup(radioGroup);
        hermiteButton.setToggleGroup(radioGroup);
        
        textFields.put(xOne, yOne);
        textFields.put(xTwo, yTwo);
        textFields.put(xThree, yThree);
        textFields.put(xFour, yFour);
        textFields.put(xFive, yFive);
        
    }
    
    private Map readTextFields() {
        Point p;
        
        for (TextField field : textFields.keySet()) {
            String first = field.getText();
            String second = textFields.get(field).getText();
            
            if (!isEmptyString(first) && !isEmptyString(second)) {
                try {
                    Expression exp1 = new ExpressionBuilder(first).build();
                    Expression exp2 = new ExpressionBuilder(second).build();
                    p = new Point(exp1.evaluate(), exp2.evaluate());
                    numberPairs.put(p.getX(), p.getY());
                } catch (UnknownFunctionOrVariableException ufve) {
                    somethingWrong("Hiba a bevitelben!");
                }
            } else {
                break;
            }
        }
        
        return numberPairs;
        
    }
    
    private Point readInterval() {
        String intervalString = intervalField.getText();
        Expression exp1;
        Expression exp2;
        Point p = new Point(0,0);
        
        if (isEmptyString(intervalString)) {
            p = new Point(-10, 10);
        } else {
            if (goodInterval(intervalString)) {
                try {
                    String[] parts = intervalString.split(",");
                    String s = "";
                    for (int i = 1; i < parts[0].length(); i++) {
                        s += parts[0].charAt(i);
                    }
                    exp1 = new ExpressionBuilder(s).build();
                    s = "";
                    for (int i = 0; i < parts[1].length() - 1; i++) {
                        s += parts[1].charAt(i);
                    }
                    exp2 = new ExpressionBuilder(s).build();
                    p = new Point(exp1.evaluate(), exp2.evaluate());
                    
                } catch (UnknownFunctionOrVariableException ufve) {
                    somethingWrong("Rosszul adtad meg az intervallumot!");
                }           
            } else {
                somethingWrong("Rosszul adtad meg az intervallumot!");
              
            }
        }
      return p;
    }
    
    private boolean isEmptyString(String s) {
        return s == null || s.equals("");
    }
    
    private boolean goodInterval(String s) {
        String[] two = s.split(",");
        
        return (s.charAt(0) == '[' || s.charAt(0) == ']')
                && (s.charAt(s.length() - 1) == '[' || s.charAt(s.length() - 1) == ']')
                && two.length == 2;
    }
    
    private class Point {
        
        double x;
        double y;
        
        public Point(double x, double y) {
            this.x = x;
            this.y = y;
        }
        
        public double getX() {
            return x;
        }
        
        public double getY() {
            return y;
        }
    }
    
    private void somethingWrong(String msg) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Hiba");
        alert.setHeaderText(msg);
        alert.showAndWait();
    }
}
