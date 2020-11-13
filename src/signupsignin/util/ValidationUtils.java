package signupsignin.util;

import java.util.Collections;
import java.util.regex.Pattern;
import javafx.collections.ObservableList;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;

/**
 * Class containing "regex", referenced from controllers to validate data format.
 * 
 * @author Mikel
 */
public class ValidationUtils {

    public ValidationUtils() {
    }

    public void addClass(TextField tf, String cssClass, Boolean addClass) {
        ObservableList<String> styleClass = tf.getStyleClass();
        if (addClass) {
            styleClass.add(cssClass);
        } else {
            styleClass.removeAll(Collections.singleton(cssClass));
        }
    }

    public void regexValidator(Pattern regexp, TextField tf, String value, String property) {
        if (regexp.matcher(value).matches()) {
            tf.getProperties().put(property, true);
        } else {
            tf.getProperties().put(property, false);
        }
    }

    public void minLength(TextField tf, final int minLength, String currentValue, String property) {
        if (currentValue.trim().length() < minLength) {
            tf.getProperties().put(property, false);
        } else {
            tf.getProperties().put(property, true);
        }
    }

    public void textLimiter(TextField tf, final int maxLength, String currentValue) {
        if (currentValue.trim().length() > maxLength) {
            String s = currentValue.substring(0, maxLength);
            tf.setText(s);
        }
    }

    public boolean comparePasswords(final PasswordField pf1, final PasswordField pf2, String property) {
        if (pf1.getText().equals(pf2.getText()) && pf1.getText().trim().isEmpty() == false) {
            pf2.getProperties().put(property, true);
            return true;
        } else {
            pf2.getProperties().put(property, false);
            return false;
        }
    }
}
