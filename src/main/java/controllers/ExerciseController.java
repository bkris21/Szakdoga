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
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;
import net.objecthunter.exp4j.tokenizer.UnknownFunctionOrVariableException;
import net.objecthunter.exp4j.function.Function;

public class ExerciseController implements Initializable {

    private InterpolationAlgorithms ia;
    private int numberOfTextFields = 0;

    private Function f;
    private Point interval;

    @FXML
    private Label yLabel, fxLabel, hintLabel1, hintLabel2;

    @FXML
    private GridPane fields;

    @FXML
    private TextArea resultText;

    @FXML
    private TextField fxTextField, pointsNumber;

    @FXML
    private TextField intervalField;

    @FXML
    private RadioButton lagrangeButton, csebisevButton, newtonButton, inverzButton, hermiteButton, functionsButton;

    @FXML
    private ToggleGroup interpolationRadioButtonGroup;

    private Map<Double, Double> numberPairs = new LinkedHashMap<>();
    private Map<TextField, TextField> textFields = new LinkedHashMap<>();

    @FXML
    void getPointNumber(ActionEvent event) {

        fields.getChildren().clear();
        textFields.clear();

        try {
            numberOfTextFields = Integer.parseInt(pointsNumber.getText());

            for (int i = 0; i < numberOfTextFields; i++) {
                TextField fieldX = new TextField();
                TextField fieldY = new TextField();
                textFields.put(fieldX, fieldY);
                fields.setConstraints(fieldX, i, 0);
                fields.getChildren().add(fieldX);
               
                fields.setConstraints(fieldY, i, 1);
                fields.getChildren().add(fieldY);
            }
            
            if(functionsButton.isSelected()){
                for(TextField field : textFields.values()){
                    field.setVisible(false);
                }
            }

        } catch (NumberFormatException nfe) {
            somethingWrong("Egy pozitív egész számot adjál meg!");
        }
    }

    @FXML
    private void functionButtonPressed(ActionEvent event) {
       fxTextField.clear();
        if (functionsButton.isSelected()) {
            for (TextField textField : textFields.values()) {
                textField.setVisible(false);
            }
            yLabel.setVisible(false);
            hintLabel1.setVisible(false);
            fxLabel.setVisible(true);
            fxTextField.setVisible(true);
            hintLabel2.setVisible(true);
        } else {
            for (TextField textField : textFields.values()) {
                textField.setVisible(true);
            }
            yLabel.setVisible(true);
            hintLabel1.setVisible(true);
            fxTextField.setVisible(false);
            fxLabel.setVisible(false);
            hintLabel2.setVisible(false);

        }
    }

    @FXML
    private void goInterpolation(ActionEvent event) {
        numberPairs.clear();
        resultText.clear();

        if (interpolationRadioButtonGroup.getSelectedToggle() == null) {
            somethingWrong("Nem jelölte meg az interpoláció fajtáját!");
        } else if (isEmptyString(pointsNumber.getText())) {
            somethingWrong("Nem adtad meg az alappontok számát!");
        } else {

                try {
                    if (functionsButton.isSelected()) {
                        readWithFunctionField();
                    } else {

                        readTextFields();
                    }
                    ia = new InterpolationAlgorithms((LinkedHashMap<Double, Double>) numberPairs);

                    if (lagrangeButton.isSelected()) {
                        f = ia.lagrangeInterpolation();
                        resultText.setText("L(x)=" + ia.lagrangeStringFunction());
                    }
                    if (newtonButton.isSelected()) {
                        f = ia.newtonInterpolation();
                        resultText.setText("Nl(x)=" + ia.newtonStringFunction());
                    }
                } catch (InputException ie) {
                    somethingWrong(ie.getMessage());
                }
            

        }
    }

    @FXML
    private void openCoordinateSystem(ActionEvent event) throws Exception {
        try {
            interval = readInterval();

            new CoordinateSystem(f, interval.getX(), interval.getY()).start(new Stage());
        } catch (NullPointerException npe) {
            somethingWrong("Először nyomd meg a 'Mehet' gombot!");
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        interpolationRadioButtonGroup = new ToggleGroup();

        lagrangeButton.setToggleGroup(interpolationRadioButtonGroup);
        newtonButton.setToggleGroup(interpolationRadioButtonGroup);
        csebisevButton.setToggleGroup(interpolationRadioButtonGroup);
        inverzButton.setToggleGroup(interpolationRadioButtonGroup);
        hermiteButton.setToggleGroup(interpolationRadioButtonGroup);

        for (TextField textField : textFields.keySet()) {
            textField.setVisible(false);
            textFields.get(textField).setVisible(false);
        }

        fxTextField.setVisible(false);
        fxLabel.setVisible(false);
        hintLabel2.setVisible(false);

    }

    private Map readTextFields() throws InputException {

        for (TextField field : textFields.keySet()) {
            String first = field.getText();
            String second = textFields.get(field).getText();

            if (!isEmptyString(first) && !isEmptyString(second)) {
                try {
                    makeExpression(first, second);
                } catch (UnknownFunctionOrVariableException ufve) {
                    somethingWrong("Hiba a bevitelben!");
                }
            } else {
                throw new InputException("Nem adtál meg minden mezőt");
            }
        }

        return numberPairs;

    }

    private Map readWithFunctionField() throws InputException {

        for (TextField field : textFields.keySet()) {
            String xPoint = field.getText();
            String function = fxTextField.getText();
            if (!isEmptyString(xPoint) && !isEmptyString(function)) {
                try {
                    function = function.replace("x", xPoint);
                    makeExpression(xPoint, function);

                } catch (UnknownFunctionOrVariableException ufve) {
                    somethingWrong("Hiba a bevitelben!");
                }
            } else {
               throw new InputException("Üres mezők!");
            }
        }
        return numberPairs;
    }

    private Point readInterval() {
        String intervalString = intervalField.getText();
        Expression exp1;
        Expression exp2;
        Point p = new Point(0, 0);

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

    private void makeExpression(String first, String second) {
        Point p;
        Expression exp1 = new ExpressionBuilder(first).build();
        Expression exp2 = new ExpressionBuilder(second).build();
        p = new Point(exp1.evaluate(), exp2.evaluate());
        numberPairs.put(p.getX(), p.getY());
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
