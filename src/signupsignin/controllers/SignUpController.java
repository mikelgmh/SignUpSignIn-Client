/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package signupsignin.controllers;

import exceptions.ErrorConnectingDatabaseException;
import exceptions.QueryException;
import exceptions.UserAlreadyExistException;
import interfaces.Signable;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import user.User;
import user.UserPrivilege;
import user.UserStatus;

/**
 *
 * @author Mikel
 */
public class SignUpController {

    private static final String MIN_THREE_CHARACTERS = "Min 3 characters";
    private static final String ENTER_VALID_EMAIL = "Type a valid email.";
    private static final String PASSWORD_CONDITIONS = "- Between 8 and 25 characters\n- Uppercase and Lowercase letters\n- One number and special character at least";
    private final Pattern emailRegexp = Pattern.compile("(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])");
    private final Pattern passRegexp = Pattern.compile("^(?=.*[0-9])"
            + "(?=.*[a-z])(?=.*[A-Z])"
            + "(?=.*[@#$%^&+=])"
            + "(?=\\S+$).{8,25}$");
    private Stage stage;

    private Signable signableImplementation;

    @FXML
    private TextField txt_Firstname;

    @FXML
    private Label hint_Firstname;

    @FXML
    private Label hint_Lastname;

    @FXML
    private Label hint_Email;

    @FXML
    private Label hint_Username;

    @FXML
    private Label hint_Password;

    @FXML
    private Label hint_RepeatPassword;

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

    public void setSignable(Signable signable) {
        this.signableImplementation = signable;
    }

    public void initStage(Parent parent) {

        hint_Firstname.setText(MIN_THREE_CHARACTERS);
        hint_Lastname.setText(MIN_THREE_CHARACTERS);
        hint_Email.setText(ENTER_VALID_EMAIL);
        hint_Username.setText(MIN_THREE_CHARACTERS);
        hint_Password.setText(PASSWORD_CONDITIONS);
        hint_RepeatPassword.setText("");
        txt_Email.getProperties().put("emailValidator", false);
        txt_Firstname.getProperties().put("minLengthValidator", false);
        txt_Lastname.getProperties().put("minLengthValidator", false);
        txt_Password.getProperties().put("passwordRequirements", false);
        txt_Username.getProperties().put("minLengthValidator", false);
        txt_RepeatPassword.getProperties().put("passwordRequirements", false);
        txt_RepeatPassword.getProperties().put("passwordsMatch", false);

        Scene scene = new Scene(parent);

        this.setListeners();
        stage.setScene(scene);
        stage.setTitle("Register");
        stage.setResizable(false);
        stage.show();
    }

    public void setListeners() {
        this.txt_Firstname.textProperty().addListener((obs, oldText, newText) -> {
            minLength(this.txt_Firstname, 3, newText, "minLengthValidator");
            textLimiter(this.txt_Firstname, 20, newText);
            this.validate();
        });

        this.txt_Lastname.textProperty().addListener((obs, oldText, newText) -> {
            minLength(this.txt_Lastname, 3, newText, "minLengthValidator");
            textLimiter(this.txt_Lastname, 20, newText);
            this.validate();
        });

        this.txt_Email.textProperty().addListener((obs, oldText, newText) -> {
            minLength(this.txt_Email, 3, newText, "minLengthValidator");
            textLimiter(this.txt_Email, 80, newText);
            regexValidator(this.emailRegexp, this.txt_Email, newText.toLowerCase(), "emailValidator");
            this.validate();
        });

        this.txt_Username.textProperty().addListener((obs, oldText, newText) -> {
            minLength(this.txt_Username, 3, newText, "minLengthValidator");
            textLimiter(this.txt_Username, 20, newText);
            this.validate();
        });
        this.txt_Password.textProperty().addListener((obs, oldText, newText) -> {
            comparePasswords(this.txt_Password, this.txt_RepeatPassword, "passwordsMatch");
            textLimiter(this.txt_Password, 25, newText);
            regexValidator(this.passRegexp, this.txt_Password, newText, "passwordRequirements");
            this.validate();
        });
        this.txt_RepeatPassword.textProperty().addListener((obs, oldText, newText) -> {
            comparePasswords(this.txt_Password, this.txt_RepeatPassword, "passwordsMatch");
            textLimiter(this.txt_RepeatPassword, 25, newText);
            regexValidator(this.passRegexp, this.txt_RepeatPassword, newText, "passwordRequirements");
            this.validate();
        });

    }

    public void regexValidator(Pattern regexp, TextField tf, String value, String property) {
        if (regexp.matcher(value).matches()) {
            tf.getProperties().put(property, true);
        } else {
            tf.getProperties().put(property, false);
        }
    }

    public void minLength(TextField tf, final int minLength, String currentValue, String property) {
        if (currentValue.length() < minLength) {
            tf.getProperties().put(property, false);
        } else {
            tf.getProperties().put(property, true);
        }
    }

    public void textLimiter(TextField tf, final int maxLength, String currentValue) {
        if (currentValue.length() > maxLength) {
            String s = currentValue.substring(0, maxLength);
            tf.setText(s);
        }
    }

    public void comparePasswords(final PasswordField pf1, final PasswordField pf2, String property) {
        if (pf1.getText().equals(pf2.getText()) && pf1.getText().trim().isEmpty() == false) {
            pf2.getProperties().put(property, true);
        } else {
            pf2.getProperties().put(property, false);
        }
        this.validate();
    }

    public void validate() {
        if (Boolean.parseBoolean(this.txt_Email.getProperties().get("emailValidator").toString())
                && Boolean.parseBoolean(this.txt_Firstname.getProperties().get("minLengthValidator").toString())
                && Boolean.parseBoolean(this.txt_Username.getProperties().get("minLengthValidator").toString())
                && Boolean.parseBoolean(this.txt_Lastname.getProperties().get("minLengthValidator").toString())
                && Boolean.parseBoolean(this.txt_Password.getProperties().get("passwordRequirements").toString())
                && Boolean.parseBoolean(this.txt_RepeatPassword.getProperties().get("passwordRequirements").toString())
                && Boolean.parseBoolean(this.txt_RepeatPassword.getProperties().get("passwordsMatch").toString())) {
            this.btn_SignUp.setDisable(false);
        } else {
            this.btn_SignUp.setDisable(true);
        }
    }

    public void signUpButtonClickHandler() {
        try {
            User user = new User();
            user.setEmail(this.txt_Email.getText());
            user.setFullName(this.txt_Firstname.getText() + this.txt_Lastname.getText());
            user.setPassword(this.txt_RepeatPassword.getText());
            user.setPrivilege(UserPrivilege.USER);
            user.setStatus(UserStatus.ENABLED);
            user.setLogin(this.txt_Username.getText());
            this.signableImplementation.signUp(user);
        } catch (ErrorConnectingDatabaseException ex) {
            Logger.getLogger(SignUpController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (UserAlreadyExistException ex) {
            Logger.getLogger(SignUpController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (QueryException ex) {
            Logger.getLogger(SignUpController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void changeStageToLogin() {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/signupsignin/view/SignIn.fxml"));
        Parent root = null;
        try {
            root = (Parent) loader.load();
        } catch (IOException ex) {
            Logger.getLogger(SignInController.class.getName()).log(Level.SEVERE, null, ex);
        }

        SignInController controller = ((SignInController) loader.getController());

        controller.setSignable(this.signableImplementation);
        controller.setStage(this.stage);
        controller.initStage(root);
    }

    public void backButtonClickHandler() {
        this.changeStageToLogin();
    }

}
