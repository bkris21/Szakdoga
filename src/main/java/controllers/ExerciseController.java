package controllers;

import interpolationapplication.coordinatesystem.CoordinateSystem;
import java.net.URL;
import java.util.ArrayList;

import java.util.LinkedHashMap;
import java.util.List;

import java.util.Map;
import java.util.ResourceBundle;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

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
    private Label yLabel, fxLabel, hintLabel1, hintLabel2, firstDerivative, secondDerivative;

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

    private List<Point> numberPairs = new ArrayList<>();
    private List<TextFields> textFields = new ArrayList<>();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        interpolationRadioButtonGroup = new ToggleGroup();

        hermiteButton.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if (newValue) {
                    firstDerivative.setVisible(true);
                    secondDerivative.setVisible(true);
                    for (TextFields field : textFields) {
                        field.getText3().setVisible(true);
                        field.getText4().setVisible(true);
                    }

                } else {
                    firstDerivative.setVisible(false);
                    secondDerivative.setVisible(false);
                    for (TextFields field : textFields) {
                        field.getText3().setVisible(false);
                        field.getText4().setVisible(false);
                    }
                }

            }
        });

        functionsButton.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if (newValue) {
                    for (TextFields field : textFields) {
                        field.getText2().setVisible(false);
                    }
                    yLabel.setVisible(false);
                    hintLabel1.setVisible(false);
                    fxLabel.setVisible(true);
                    fxTextField.setVisible(true);
                    hintLabel2.setVisible(true);
                } else {
                    for (TextFields textField : textFields) {
                        textField.getText2().setVisible(true);
                    }
                    yLabel.setVisible(true);
                    hintLabel1.setVisible(true);
                    fxTextField.setVisible(false);
                    fxLabel.setVisible(false);
                    hintLabel2.setVisible(false);
                }
            }
        });

        lagrangeButton.setToggleGroup(interpolationRadioButtonGroup);
        newtonButton.setToggleGroup(interpolationRadioButtonGroup);
        csebisevButton.setToggleGroup(interpolationRadioButtonGroup);
        inverzButton.setToggleGroup(interpolationRadioButtonGroup);
        hermiteButton.setToggleGroup(interpolationRadioButtonGroup);

        for (TextFields textField : textFields) {
            textField.getText1().setVisible(false);
            textField.getText2().setVisible(false);
        }

        fxTextField.setVisible(false);
        fxLabel.setVisible(false);
        hintLabel2.setVisible(false);

        firstDerivative.setVisible(false);
        secondDerivative.setVisible(false);

    }

    @FXML
    void getPointNumber(ActionEvent event) {

        fields.getChildren().clear();
        textFields.clear();

        try {
            numberOfTextFields = Integer.parseInt(pointsNumber.getText());

            for (int i = 0; i < numberOfTextFields; i++) {
                TextField fieldX = new TextField();
                TextField fieldY = new TextField();
                TextField d1Field = new TextField();
                TextField d2Field = new TextField();
                textFields.add(new TextFields(fieldX, fieldY, d1Field, d2Field));

                fields.setConstraints(fieldX, i, 0);
                fields.getChildren().add(fieldX);

                fields.setConstraints(fieldY, i, 1);
                fields.getChildren().add(fieldY);

                fields.setConstraints(d1Field, i, 2);
                fields.getChildren().add(d1Field);

                fields.setConstraints(d2Field, i, 3);
                fields.getChildren().add(d2Field);
            }

            if (functionsButton.isSelected()) {
                for (TextFields field : textFields) {
                    field.getText2().setVisible(false);
                }

            }

            if (!hermiteButton.isSelected()) {
                for (TextFields field : textFields) {
                    field.getText3().setVisible(false);
                    field.getText4().setVisible(false);
                }
            }

        } catch (NumberFormatException nfe) {
            somethingWrong("Egy pozitív egész számot adjál meg!");
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
                ia = new InterpolationAlgorithms((List<Point>) numberPairs);

                if (lagrangeButton.isSelected()) {
                    f = ia.lagrangeInterpolation();
                    resultText.setText("L(x)=" + ia.lagrangeStringFunction());
                }
                if (newtonButton.isSelected()) {
                    f = ia.newtonInterpolation();
                    resultText.setText("N(x)=" + ia.newtonStringFunction());
                }
                if (hermiteButton.isSelected()) {

                }

            } catch (InputException ie) {
                somethingWrong(ie.getMessage());
            } catch (UnknownFunctionOrVariableException ufve) {
                somethingWrong("Hiba a bevitelben!");
            }

        }
    }

    @FXML
    private void openCoordinateSystem(ActionEvent event) throws Exception {
        try {
            interval = readInterval();
            if(!functionsButton.isSelected()){
                new CoordinateSystem(f, interval.getX(), interval.getY()).start(new Stage());
            }else{
               
                Function f2= new Function("Original") {
                    @Override
                    public double apply(double... doubles) {
                      String function = fxTextField.getText();
                      
                      String newString = "";
                      int i=0;
                      while(function.charAt(i)!='x'){
                          newString+=function.charAt(i);
                          i++;
                      }
                      newString+=doubles[0];
                      for(i=i+1;i<function.length();i++){
                           newString+=function.charAt(i);
                      }
                      Expression exp= new ExpressionBuilder(newString).build();
                      return exp.evaluate();
                      
                    }
                };
                new CoordinateSystem(f,f2, interval.getX(), interval.getY()).start(new Stage());
            }
            
           
                    } catch (NullPointerException npe) {
            somethingWrong("Először nyomd meg a 'Mehet' gombot!");
        }

    }

    private List<Point> readTextFields() throws InputException {
        Point p;
        for (TextFields field : textFields) {
            String x = field.getText1().getText();
            String y = field.getText2().getText();
            String d1X = field.getText3().getText();
            String d2X = field.getText4().getText();

            if (!isEmptyString(x) && !isEmptyString(y)) {
                try {
                    if (hermiteButton.isSelected() && !isEmptyString(d1X) && !isEmptyString(d2X)) {
                        p = makeExpression(x, y, d1X, d2X);
                    } else if (hermiteButton.isSelected() && !isEmptyString(d1X)) {
                        p = makeExpression(x, y, d1X);
                    } else {
                        p = makeExpression(x, y);
                    }
                    numberPairs.add(p);
                } catch (UnknownFunctionOrVariableException ufve) {

                    throw ufve;
                }
            } else {
                throw new InputException("Nem adtál meg minden mezőt");
            }

        }

        return numberPairs;

    }

    private List<Point> readWithFunctionField() throws InputException {
        Point p;
        for (TextFields field : textFields) {
            String x = field.getText1().getText();
            String fx = fxTextField.getText();
            fx=fx.replace("x", x);
            String d1X = field.getText3().getText();
            String d2X = field.getText4().getText();

            if (!isEmptyString(x) && !isEmptyString(fx)) {
                try {
                    if (hermiteButton.isSelected() && !isEmptyString(d1X) && !isEmptyString(d2X)) {
                        p = makeExpression(x, fx, d1X, d2X);
                    } else if (hermiteButton.isSelected() && !isEmptyString(d1X)) {
                        p = makeExpression(x, fx, d1X);
                    } else {
                        p = makeExpression(x, fx);
                    }
                    numberPairs.add(p);
                } catch (UnknownFunctionOrVariableException ufve) {

                    throw ufve;
                }
            } else {
                throw new InputException("Nem adtál meg minden mezőt");
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

    private Point makeExpression(String first, String second, String... s) {
        Point p;
        Expression exp1 = new ExpressionBuilder(first).build();
        Expression exp2 = new ExpressionBuilder(second).build();
        p = new Point(exp1.evaluate(), exp2.evaluate());
        if (s.length == 1) {
            Expression exp3 = new ExpressionBuilder(s[0]).build();
            p.setD1x(exp3.evaluate());
        }
        if (s.length == 2) {
            Expression exp3 = new ExpressionBuilder(s[0]).build();
            Expression exp4 = new ExpressionBuilder(s[1]).build();
            p.setD1x(exp3.evaluate());
            p.setD2x(exp4.evaluate());
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

    private void somethingWrong(String msg) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Hiba");
        alert.setHeaderText(msg);
        alert.showAndWait();
    }
}
