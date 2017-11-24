
package controllers;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;


public class TextFields {
    
    private TextField text1;
    private TextField text2;
    private TextField text3;
    private TextField text4;

    public TextFields(TextField text1, TextField text2, TextField text3, TextField text4) {
        this.text1 = text1;
        this.text2 = text2;
        this.text3 = text3;
        this.text4 = text4;
    }

  

    public TextField getText1() {
        return text1;
    }

    public TextField getText2() {
        return text2;
    }

    public TextField getText3() {
        return text3;
    }

    public TextField getText4() {
        return text4;
    }

    public void setText1(String value) {
        this.text1.setText(value);
    }

     public void setText2(String value) {
        this.text2.setText(value);
    }


      public void setText3(String value) {
        this.text3.setText(value);
    }

      public void setText4(String value) {
        this.text4.setText(value);
    }

   

    
    
    
    
}
