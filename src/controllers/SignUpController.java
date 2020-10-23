/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import interfaces.Signable;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;
import javafx.animation.PauseTransition;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.input.KeyEvent;
import javafx.event.EventTarget;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.Spinner;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.stage.Stage;
import javafx.util.Duration;
import signable.SignableFactory;
import sun.security.util.Length;
import user.User;

/**
 *
 * @author Mikel
 */
public class SignUpController {

    private final Pattern emailRegexp = Pattern.compile("(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])");
    private final Pattern passRegexp = Pattern.compile("^(?=.*[0-9])"
            + "(?=.*[a-z])(?=.*[A-Z])"
            + "(?=.*[@#$%^&+=])"
            + "(?=\\S+$).{8,20}$");
    private Stage stage;

    @FXML
    private TextField txt_Firstname;

    @FXML
    private TextField txt_Lastname;

    @FXML
    private TextField txt_Email;

    @FXML
    private TextField txt_Username;

    @FXML
    private ProgressIndicator progress_username;

    @FXML
    private PasswordField txt_Password;

    @FXML
    private PasswordField txt_RepeatPassword;

    @FXML
    private Button btn_SignUp;

    public Stage getStage() {
        return this.stage;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void initStage(Parent parent) {

        txt_Email.getProperties().put("emailValid", false);
        txt_Password.getProperties().put("passwordRequirements", false);
        txt_RepeatPassword.getProperties().put("passwordRequirements", false);
        txt_RepeatPassword.getProperties().put("passwordsMatch", false);

        Scene scene = new Scene(parent);
        addTextLimiter(txt_Firstname, 20, this);
        addTextLimiter(txt_Lastname, 50, this);
        addTextLimiter(txt_Email, 80, this);
        addTextLimiter(txt_Username, 20, this);

        addRegexp(txt_Email, emailRegexp, this, "emailValid");
        addRegexp(txt_Password, passRegexp, this, "passwordRequirements");
        addRegexp(txt_RepeatPassword, passRegexp, this, "passwordRequirements");
        checkIfPasswordsMatch(txt_Password, txt_RepeatPassword, "passwordsMatch", this);

        stage.setScene(scene);
        stage.setTitle("Register");
        stage.setResizable(false);
        stage.show();
    }

    public static void addTextLimiter(final TextField tf, final int maxLength, SignUpController aThis) {
        tf.textProperty().addListener(new ChangeListener<String>() {

            @Override
            public void changed(final ObservableValue<? extends String> ov, final String oldValue, final String newValue) {
                if (tf.getText().length() > maxLength) {
                    String s = tf.getText().substring(0, maxLength);
                    tf.setText(s);
                }
                aThis.validate();
            }
        });
    }

    public static void checkIfPasswordsMatch(final PasswordField pf1, final PasswordField pf2, String property, SignUpController aThis) {
        pf1.textProperty().addListener((obs, oldText, newText) -> {
            aThis.comparePasswords(pf1, pf2, property, aThis);

        });
        pf2.textProperty().addListener((obs, oldText, newText) -> {
            aThis.comparePasswords(pf1, pf2, property, aThis);

        });
    }

    private void comparePasswords(final PasswordField pf1, final PasswordField pf2, String property, SignUpController aThis) {
        if (pf1.getText().equals(pf2.getText()) && pf1.getText().trim().isEmpty() == false) {
            pf2.getProperties().put(property, true);
        } else {
            pf2.getProperties().put(property, false);
        }
        aThis.validate();
    }

    public static void addRegexp(final TextField tf, final Pattern regexp, SignUpController aThis, String property) {
        tf.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(final ObservableValue<? extends String> ov, final String oldValue, final String newValue) {

                if (regexp.matcher(tf.getText()).matches()) {
                    tf.getProperties().put(property, true);
                } else {
                    // todo: add error message/markup
                    tf.getProperties().put(property, false);
                }
                aThis.validate();
            }
        });
    }

    public void validate() {
        if (Boolean.parseBoolean(this.txt_Email.getProperties().get("emailValid").toString())
                && Boolean.parseBoolean(this.txt_Password.getProperties().get("passwordRequirements").toString())
                && Boolean.parseBoolean(this.txt_RepeatPassword.getProperties().get("passwordRequirements").toString())
                && Boolean.parseBoolean(this.txt_RepeatPassword.getProperties().get("passwordsMatch").toString())) {
            this.btn_SignUp.setDisable(false);
        } else {
            this.btn_SignUp.setDisable(true);
        }
    }

    public void SignUpButtonClickHandler() {
        User user = new User();
        user.setEmail(this.txt_Email.getText());
        user.setFullName(this.txt_Firstname.getText() + this.txt_Lastname.getText());
        user.setPassword(this.txt_RepeatPassword.getText());
        user.setLogin(this.txt_Username.getText());

        SignableFactory signableFactory = new SignableFactory();
        Signable signableImplementarion = signableFactory.getSignableImplementation("CLIENT_SIGNABLE");
        signableImplementarion.signUp(user);

    }

    public void changeStageToLogin() {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/SignIn.fxml"));
        Parent root = null;
        try {
            root = (Parent) loader.load();
        } catch (IOException ex) {
            Logger.getLogger(SignUpController.class.getName()).log(Level.SEVERE, null, ex);
        }

        SignUpController controller = ((SignUpController) loader.getController());

        controller.setStage(this.stage);
        controller.initStage(root);
    }
}
