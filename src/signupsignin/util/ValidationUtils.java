/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package signupsignin.util;

import java.util.Collections;
import javafx.collections.ObservableList;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;

/**
 *
 * @author Mikel
 */
public class ValidationUtils {

    public ValidationUtils() {
    }

    //public void inputValidationColors(TextField tf, String labelText, String textFieldCssClass, boolean addTextFieldClass, Label label) {
     //   addClass(tf, textFieldCssClass, addTextFieldClass);
   // }

    public void addClass(TextField tf, String cssClass, Boolean addClass) {
        ObservableList<String> styleClass = tf.getStyleClass();
        if (addClass) {
            styleClass.add(cssClass);
        } else {
            styleClass.removeAll(Collections.singleton(cssClass));
        }
    }
}
