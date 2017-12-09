package controllers;

import interpolationapplication.coordinatesystem.CoordinateSystem;
import java.net.URL;

import java.util.ArrayList;
import java.util.Comparator;

import java.util.LinkedHashMap;
import java.util.List;

import java.util.Map;
import java.util.ResourceBundle;
import javafx.animation.PauseTransition;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.util.Duration;

import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;
import net.objecthunter.exp4j.function.Function;
import net.objecthunter.exp4j.tokenizer.UnknownFunctionOrVariableException;

public class ExerciseController implements Initializable {

    private InterpolationAlgorithms ia;
    private int numberOfTextFields = 0;

    private List<Function> f = new ArrayList<>();
    private List<Point> interval = new ArrayList<>();

    @FXML
    private Label yLabel, hintLabel1, firstDerivative, secondDerivative, resultOfInverseLabel, splineDX, resultLabel;

    @FXML
    private GridPane fields;

    @FXML
    private TextArea resultText;

    @FXML
    private TextField fxTextField, pointsNumber;

    @FXML
    private TextField intervalField, resultOfInverse;

    @FXML
    private RadioButton lagrangeButton, newtonButton, inverzButton, hermiteButton, functionsButton, splineButton;

    @FXML
    private ToggleGroup interpolationRadioButtonGroup;

    @FXML
    private Button goButton, addPoint, deletePoint, clear;

    private List<Point> numberPairs = new ArrayList<>();
    private List<TextFields> textFields = new ArrayList<>();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        interpolationRadioButtonGroup = new ToggleGroup();
        resultOfInverse.setVisible(false);
        resultOfInverseLabel.setVisible(false);
        fxTextField.setVisible(false);

        firstDerivative.setVisible(false);
        secondDerivative.setVisible(false);

        splineDX.setVisible(false);

        addListenersToButtons();

        lagrangeButton.setToggleGroup(interpolationRadioButtonGroup);
        newtonButton.setToggleGroup(interpolationRadioButtonGroup);

        inverzButton.setToggleGroup(interpolationRadioButtonGroup);
        hermiteButton.setToggleGroup(interpolationRadioButtonGroup);
        splineButton.setToggleGroup(interpolationRadioButtonGroup);

        for (TextFields textField : textFields) {
            textField.getText1().setVisible(false);
            textField.getText2().setVisible(false);
        }

    }

    private void checkButtons() {

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
        if (splineButton.isSelected()) {
            for (TextFields field : textFields) {
                field.getText3().setVisible(true);
                field.getText4().setVisible(false);
            }
        }
    }

    @FXML
    void getPointNumber(ActionEvent event) throws InputException {

        
        try {

            int number = Integer.parseInt(pointsNumber.getText());
            if (number> 6 || number < 2) {
                somethingWrong("Túl sok vagy túl kevés alappontot adtál meg!\n(Legalább 2 legfeljebb 6)");
                pointsNumber.setText("" + numberOfTextFields);
            } else {
                numberOfTextFields=number;
                fields.getChildren().clear();
                textFields.clear();
        
       
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
                checkButtons();
                 pointsNumber.setText("" + numberOfTextFields);
            }

        } catch (NumberFormatException nfe) {
            somethingWrong("Egy pozitív egész számot adjál meg!");
        }
    }

    @FXML
    private void onClickAddPoint(ActionEvent event) {
        
        if (textFields.size() < 2) {
            somethingWrong("Először a kezdeti alappontszámot add meg!");
        } else {
            if (textFields.size() < 6) {

                TextField fieldX = new TextField();
                TextField fieldY = new TextField();
                TextField d1Field = new TextField();
                TextField d2Field = new TextField();
                textFields.add(new TextFields(fieldX, fieldY, d1Field, d2Field));

                fields.setConstraints(fieldX, numberOfTextFields, 0);
                fields.getChildren().add(fieldX);

                fields.setConstraints(fieldY, numberOfTextFields, 1);
                fields.getChildren().add(fieldY);

                fields.setConstraints(d1Field, numberOfTextFields, 2);
                fields.getChildren().add(d1Field);

                fields.setConstraints(d2Field, numberOfTextFields, 3);
                fields.getChildren().add(d2Field);
                checkButtons();
                numberOfTextFields++;

                pointsNumber.setText("" + numberOfTextFields);
            } else {
                somethingWrong("Túl sok az alappont!");
            }
        }
    }

    @FXML
    private void onClickDeletePoint(ActionEvent event) {
        if(numberOfTextFields==0){
            somethingWrong("Először a kezdeti appontszámot add meg!");
        }else{
        if (textFields.size() < 3) {
            somethingWrong("Legalább 2 alappont kell!");
        } else {
            textFields.remove(textFields.get(textFields.size() - 1));
            numberOfTextFields--;
            ObservableList<Node> children = fields.getChildren();
            for (Node node : children) {
                if (node instanceof TextField && fields.getRowIndex(node) == 0 && fields.getColumnIndex(node) == textFields.size()) {
                    fields.getChildren().remove(node);
                    break;
                }
            }
            for (Node node : children) {
                if (node instanceof TextField && fields.getRowIndex(node) == 1 && fields.getColumnIndex(node) == textFields.size()) {
                    fields.getChildren().remove(node);
                    break;
                }
            }
            for (Node node : children) {
                if (node instanceof TextField && fields.getRowIndex(node) == 2 && fields.getColumnIndex(node) == textFields.size()) {
                    fields.getChildren().remove(node);
                    break;
                }
            }
            for (Node node : children) {
                if (node instanceof TextField && fields.getRowIndex(node) == 3 && fields.getColumnIndex(node) == textFields.size()) {
                    fields.getChildren().remove(node);
                    break;
                }
            }
            checkButtons();
            pointsNumber.setText("" + numberOfTextFields);
        }
        }

    }

    @FXML
    private void onClickClear(ActionEvent event) {
           for(TextFields text : textFields){
               text.getText1().setText("");
               text.getText2().setText("");
               text.getText3().setText("");
               text.getText4().setText("");
               fxTextField.setText("");
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
        } else if (textFields.size() == 0) {
            somethingWrong("Alappontokat meg kell adni!");
        } else {

            try {
                if (functionsButton.isSelected()) {
                    readWithFunctionField();
                    intervalField.setText("[" + textFields.get(0).getText1().getText().trim() + "," + textFields.get(textFields.size() - 1).getText1().getText().trim() + "]");
                } else {

                    readTextFields();
                    intervalField.setText("[" + textFields.get(0).getText1().getText().trim() + "," + textFields.get(textFields.size() - 1).getText1().getText().trim() + "]");

                }
                ia = new InterpolationAlgorithms((List<Point>) numberPairs);

                if (lagrangeButton.isSelected()) {
                    f.clear();
                    f.add(ia.lagrangeInterpolation());
                    resultText.setText("L(x)=" + ia.lagrangeStringFunction());
                }
                if (newtonButton.isSelected()) {
                    f.clear();
                    f.add(ia.newtonInterpolation());
                    resultText.setText("N(x)=" + ia.newtonStringFunction());
                }
                if (inverzButton.isSelected()) {
                    f.clear();
                    f.add(ia.inverseInterpolation());
                    resultText.setText("N~(x)=" + ia.inverseStringFunction());

                    String s = ia.inverseStringFunction();
                    s = s.replace("x", "" + 0);
                    Expression exp = new ExpressionBuilder(s).build();
                    double res = exp.evaluate();

                    resultOfInverse.setText("" + ia.round(res, 3));

                }
                if (hermiteButton.isSelected()) {
                    f.clear();
                    f.add(ia.hermiteInterpolation());
                    resultText.setText("H(x)=" + ia.hermiteStringInterpolation());
                }
                if (splineButton.isSelected()) {
                    if (numberOfTextFields == 2) {
                        somethingWrong("Két alapponton csak sima Hermite-interpolációt kapsz!");
                    }
                    resultText.setText(ia.splineStringInterPolation());
                    f = ia.splineInterpolation();
                }

            } catch (InputException ie) {
                somethingWrong(ie.getMessage());
            } catch (UnknownFunctionOrVariableException ufve) {
                somethingWrong("Hiba a bevitelben!");
            } catch (NumberFormatException nfe) {
                somethingWrong("Matematikai Hiba!");
            } catch (ArithmeticException ae) {
                somethingWrong("Matematikai Hiba");
            }

        }
    }

    @FXML
    private void openCoordinateSystem(ActionEvent event) throws Exception {
        interval.clear();
        try {
            if (splineButton.isSelected()) {
                interval = readIntervalForSpline(resultText.getText());
            }

            interval.add(readInterval(intervalField.getText()));
            if (!functionsButton.isSelected()) {

                new CoordinateSystem(f, interval).start(new Stage());

            } else {

                Function f2 = new Function("Original") {
                    @Override
                    public double apply(double... doubles) {
                        String function = fxTextField.getText();

                        function = function.replace("x", "" + doubles[0]);

                        Expression exp = new ExpressionBuilder(function).build();
                        return exp.evaluate();

                    }
                };
                new CoordinateSystem(f, f2, interval).start(new Stage());
            }
        } catch (InputException ie) {
            somethingWrong(ie.getMessage());
        } catch (NullPointerException npe) {
            somethingWrong("Először nyomd meg a 'Mehet' gombot!");
        } catch (ArithmeticException ae) {
            somethingWrong("Matematikai hiba");
        }

    }

    private List<Point> readTextFields() throws InputException {
        Point p;
        for (TextFields field : textFields) {
            String x = field.getText1().getText().trim();
            String y = field.getText2().getText().trim();
            String d1X = field.getText3().getText().trim();
            String d2X = field.getText4().getText().trim();

            if (!isEmptyString(x) && !isEmptyString(y)) {
                try {
                    if (hermiteButton.isSelected() && !isEmptyString(d1X) && !isEmptyString(d2X)) {
                        p = makeExpression(x, y, d1X, d2X);
                    } else if ((hermiteButton.isSelected() || splineButton.isSelected()) && !isEmptyString(d1X)) {
                        p = makeExpression(x, y, d1X);
                    } else {
                        p = makeExpression(x, y);
                    }
                    for (Point p1 : numberPairs) {
                        if (!inverzButton.isSelected()) {

                            if (p1.getX() == p.getX()) {
                                throw new InputException("Kétszer adtál meg egy alappontot!");
                            }
                        } else if (p1.getX() == p.getX() || p1.getY() == p.getY()) {
                            throw new InputException("Nem monoton a függvény!");
                        }

                    }

                    numberPairs.add(p);

                } catch (UnknownFunctionOrVariableException ufve) {

                    throw ufve;
                }
            } else {
                throw new InputException("Nem adtál meg minden mezőt");
            }

        }

        sortNumbers();
        if (splineButton.isSelected()) {
            int db = checkEdgeCondition();
            if (db == 0) {
                throw new InputException("Nem adtál meg sehol peremfeltételt!");
            }
            if (db > 1) {
                throw new InputException("Egynél több peremfeltételt adtál meg!");
            }

        }

        return numberPairs;

    }

    private List<Point> readWithFunctionField() throws InputException {
        Point p;
        for (TextFields field : textFields) {
            String x = field.getText1().getText().trim();
            String fx = fxTextField.getText().trim();
            fx = fx.replace("x", x);
            String d1X = field.getText3().getText().trim();
            String d2X = field.getText4().getText().trim();

            if (!isEmptyString(x) && !isEmptyString(fx)) {
                try {
                    if (hermiteButton.isSelected() && !isEmptyString(d1X) && !isEmptyString(d2X)) {
                        p = makeExpression(x, fx, d1X, d2X);
                    } else if ((hermiteButton.isSelected() || splineButton.isSelected()) && !isEmptyString(d1X)) {
                        p = makeExpression(x, fx, d1X);
                    } else {
                        p = makeExpression(x, fx);
                    }
                    numberPairs.add(p);
                } catch (UnknownFunctionOrVariableException ufve) {

                    throw ufve;
                } catch (IllegalArgumentException iae) {
                    somethingWrong("Rossz a bevitel!");
                } catch (ArithmeticException ae) {
                    somethingWrong("Matematikai hiba!");
                }
            } else {
                throw new InputException("Nem adtál meg minden mezőt");
            }
        }
        sortNumbers();
        if (splineButton.isSelected()) {
            int db = checkEdgeCondition();
            if (db == 0) {
                throw new InputException("Nem adtál meg sehol peremfeltételt!");
            }
            if (db > 1) {
                throw new InputException("Egynél több peremfeltételt adtál meg!");
            }

        }
        return numberPairs;
    }

    private Point readInterval(String intervalString) throws InputException {
        Expression exp1;
        Expression exp2;
        Point p = new Point(0, 0);

        if (isEmptyString(intervalString)) {
            throw new InputException("Nem adtál meg intervallumot!");
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
                throw new InputException("Rosszul adtad meg az intervallumot");

            }
        }
        return p;
    }

    private void sortNumbers() {
        for (int i = 0; i < numberPairs.size(); i++) {
            for (int j = 1; j < numberPairs.size() - i; j++) {
                if (numberPairs.get(j - 1).getX() > numberPairs.get(j).getX()) {
                    Point temp = numberPairs.get(j - 1);
                    numberPairs.set(j - 1, numberPairs.get(j));
                    numberPairs.set(j, temp);

                    String tempX = textFields.get(j - 1).getText1().getText();
                    String tempY = textFields.get(j - 1).getText2().getText();
                    String tempd1X = textFields.get(j - 1).getText3().getText();
                    String tempd2X = textFields.get(j - 1).getText4().getText();
                    textFields.get(j - 1).setText1(textFields.get(j).getText1().getText());
                    textFields.get(j).setText1(tempX);
                    textFields.get(j - 1).setText2(textFields.get(j).getText2().getText());
                    textFields.get(j).setText2(tempY);
                    textFields.get(j - 1).setText3(textFields.get(j).getText3().getText());
                    textFields.get(j).setText3(tempd1X);
                    textFields.get(j - 1).setText4(textFields.get(j).getText4().getText());
                    textFields.get(j).setText4(tempd2X);
                }
            }
        }
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

    private List<Point> readIntervalForSpline(String s) throws InputException {
        List<Point> result = new ArrayList<>();

        String[] splited = s.split("\\n");

        for (int i = 0; i < splited.length; i++) {

            String help = "";
            int j = 0;
            while (splited[i].charAt(j) != '[') {
                j++;
            }
            result.add(readInterval(splited[i].substring(j)));

        }

        return result;
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

    private int checkEdgeCondition() {
        int db = 0;
        for (Point p : numberPairs) {
            if (!Double.isNaN(p.getD1x())) {
                db++;
            }
        }
        return db;
    }

    private void somethingWrong(String msg) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Hiba");
        alert.setHeaderText(msg);
        alert.showAndWait();
    }

    private void addListenersToButtons() {

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

        inverzButton.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if (newValue) {
                    resultOfInverse.setVisible(true);
                    resultOfInverseLabel.setVisible(true);
                } else {
                    resultOfInverse.setVisible(false);
                    resultOfInverseLabel.setVisible(false);
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
                    yLabel.setText("F(x)=");
                    hintLabel1.setText("Add meg az alappontokat és a függvényt!");
                    fxTextField.setVisible(true);

                } else {
                    for (TextFields textField : textFields) {
                        textField.getText2().setVisible(true);
                    }
                    yLabel.setText("Y:");
                    hintLabel1.setText("Add meg az alappont-érték párokat!");
                    fxTextField.setVisible(false);

                }
            }
        });

        splineButton.selectedProperty().addListener(new ChangeListener<Boolean>() {

            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if (newValue) {
                    splineDX.setVisible(true);
                    resultText.setPrefHeight(93);
                    resultText.setLayoutY(175);

                    firstDerivative.setVisible(false);
                    secondDerivative.setVisible(false);

                    for (TextFields field : textFields) {
                        field.getText3().setVisible(true);
                        field.getText4().setVisible(false);
                    }
                    resultLabel.setText("A keresett spline, S(x)=");

                } else {
                    splineDX.setVisible(false);
                    resultText.setPrefHeight(31);
                    resultText.setLayoutY(216);

                    for (TextFields field : textFields) {
                        field.getText3().setVisible(false);
                    }

                    resultLabel.setText("A keresett polinom:");

                }

            }
        });

    }

}
