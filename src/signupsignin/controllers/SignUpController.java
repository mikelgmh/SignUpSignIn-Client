package signupsignin.controllers;

import exceptions.ErrorConnectingDatabaseException;
import exceptions.ErrorConnectingServerException;
import exceptions.QueryException;
import exceptions.UserAlreadyExistException;
import interfaces.Signable;
import javafx.scene.paint.Color;
import java.io.IOException;
import java.util.Collections;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.EventType;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import signupsignin.util.ValidationUtils;
import user.User;
import user.UserPrivilege;
import user.UserStatus;

/**
 * This class handles the interaction of the user with the graphic user
 * interface, on window sign up.
 *
 * @author Mikel
 */
public class SignUpController {

    private static final Logger logger = Logger.getLogger("signupsignin.controllers.SignUpController");
    private ValidationUtils validationUtils = new ValidationUtils(); // Useful to reuse some validations in different controllers.
    private static final String MIN_THREE_CHARACTERS = "Min 3 characters";
    private static final String ENTER_VALID_EMAIL = "Type a valid email.";
    private static final String PASSWORD_CONDITIONS = "- Between 8 and 25 characters\n- Uppercase and Lowercase letters\n- One number and special character at least";
    private final Pattern emailRegexp = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);
    private final Pattern passRegexp = Pattern.compile("^(?=.*[0-9])" + "(?=.*[a-z])(?=.*[A-Z])" + "(?=.*[@#*$%^&+=])" + "(?=\\S+$).{8,25}$");
    private Stage stage;
    private static final Color greyColor = Color.web("#686464");
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
    private ProgressIndicator progress_indicator;

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

    /**
     * The method that is executed when we initialize the sign up.
     *
     * @param parent The base object for all nodes that have children in the
     * scene graph.
     */
    public void initStage(Parent parent) {

        // Sets the hint texts to the inputs.
        hint_Firstname.setText(MIN_THREE_CHARACTERS);
        hint_Lastname.setText(MIN_THREE_CHARACTERS);
        hint_Email.setText(ENTER_VALID_EMAIL);
        hint_Username.setText(MIN_THREE_CHARACTERS);
        hint_Password.setText(PASSWORD_CONDITIONS);
        hint_RepeatPassword.setText("");

        // Adds tooltips
        btn_SignUp.setTooltip(new Tooltip("Click to sign up"));

        // Sets custom properties to the inputs. These properties show the status of the validation of them.
        txt_Email.getProperties().put("emailValidator", false);
        txt_Firstname.getProperties().put("minLengthValidator", false);
        txt_Lastname.getProperties().put("minLengthValidator", false);
        txt_Password.getProperties().put("passwordRequirements", false);
        txt_Username.getProperties().put("minLengthValidator", false);
        txt_RepeatPassword.getProperties().put("passwordRequirements", false);
        txt_RepeatPassword.getProperties().put("passwordsMatch", false);

        // Creates a scena and a stage and opens the window.
        Scene scene = new Scene(parent);
        Stage stage = new Stage();
        this.setListeners();
        this.setStage(stage);
        stage.setScene(scene);
        scene.getStylesheets().add(getClass().getResource("/signupsignin/view/errorStyle.css").toExternalForm()); // Imports the CSS file used for errors in some inputs.
        stage.setTitle("Sign Up"); // Sets the title of the window
        stage.setResizable(false); // Prevents the user to resize the window.
        stage.onCloseRequestProperty().set(this::handleCloseRequest);
        stage.show();
    }

    /**
     * Sets all the listeners used in this window. Here we add the validations.
     */
    public void setListeners() {
        // Listener for the Firstname field.
        this.txt_Firstname.textProperty().addListener((obs, oldText, newText) -> {
            this.validationUtils.minLength(this.txt_Firstname, 3, newText, "minLengthValidator"); // Adds a min lenght validator
            this.validationUtils.textLimiter(this.txt_Firstname, 20, newText); // Limits the input to 20 characters
            this.validate(); // Executes the validation.
        });

        // Listener for the Lastname field.
        this.txt_Lastname.textProperty().addListener((obs, oldText, newText) -> {
            this.validationUtils.minLength(this.txt_Lastname, 3, newText, "minLengthValidator");
            this.validationUtils.textLimiter(this.txt_Lastname, 20, newText);
            this.validate();
        });

        // Validation for the Email field
        this.txt_Email.textProperty().addListener((obs, oldText, newText) -> {
            this.validationUtils.minLength(this.txt_Email, 3, newText, "minLengthValidator");
            this.validationUtils.textLimiter(this.txt_Email, 80, newText);
            this.validationUtils.regexValidator(this.emailRegexp, this.txt_Email, newText.toLowerCase(), "emailValidator"); // Adds a regex validation to check if the email is correct

            // Changes the color of the inputs when the user types.
            this.hint_Email.setTextFill(greyColor);
            hint_Email.setText(ENTER_VALID_EMAIL);
            this.validationUtils.addClass(this.txt_Email, "error", Boolean.FALSE);
            this.validate();
        });

        this.txt_Username.textProperty().addListener((obs, oldText, newText) -> {
            this.validationUtils.minLength(this.txt_Username, 3, newText, "minLengthValidator");
            this.validationUtils.textLimiter(this.txt_Username, 20, newText);
            this.hint_Username.setTextFill(greyColor);
            this.validationUtils.addClass(this.txt_Username, "error", Boolean.FALSE);
            hint_Username.setText(MIN_THREE_CHARACTERS);
            this.validate();
        });
        this.txt_Password.textProperty().addListener((obs, oldText, newText) -> {
            Boolean passwordsMatch = this.validationUtils.comparePasswords(this.txt_Password, this.txt_RepeatPassword, "passwordsMatch");
            this.validationUtils.textLimiter(this.txt_Password, 25, newText);
            this.validationUtils.regexValidator(this.passRegexp, this.txt_Password, newText, "passwordRequirements");
            this.setPasswordFieldsError(passwordsMatch);
            this.validate();
        });
        this.txt_RepeatPassword.textProperty().addListener((obs, oldText, newText) -> {
            Boolean passwordsMatch = this.validationUtils.comparePasswords(this.txt_Password, this.txt_RepeatPassword, "passwordsMatch");
            this.validationUtils.textLimiter(this.txt_RepeatPassword, 25, newText);
            this.validationUtils.regexValidator(this.passRegexp, this.txt_RepeatPassword, newText, "passwordRequirements");
            this.setPasswordFieldsError(passwordsMatch);
            this.validate();
        });

    }

    /**
     * This method sets the password fields in red if they do not match each other
     * 
     * @param passwordsMatch boolean indicating if the password is matching or not
     */
    public void setPasswordFieldsError(Boolean passwordsMatch) {
        if (!passwordsMatch) {
            this.hint_Password.setTextFill(Color.RED);
            this.validationUtils.addClass(this.txt_Password, "error", Boolean.TRUE);
            this.hint_Password.setText("Passwords don't match");
            this.hint_RepeatPassword.setTextFill(Color.RED);
            this.validationUtils.addClass(this.txt_RepeatPassword, "error", Boolean.TRUE);
        } else {
            this.hint_Password.setTextFill(greyColor);
            this.validationUtils.addClass(this.txt_Password, "error", Boolean.FALSE);
            this.hint_Password.setText(PASSWORD_CONDITIONS);
            this.hint_RepeatPassword.setTextFill(greyColor);
            this.validationUtils.addClass(this.txt_RepeatPassword, "error", Boolean.FALSE);
        }
    }

    /**
     * Check if all the fields are correct. If so, enable the signup button
     */
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

    /**
     * Method that indicates that, by pressing the signup button, it registers 
     * the user on database. After that, the client gets one alert sowhing the user
     * the registration has been successful. 
     * @throws exceptions.UserAlreadyExistException user exist
     * @throws exceptions.ErrorConnectingDatabaseException cannot connect to database
     * @throws exceptions.QueryException the query is not correct
     * @throws exceptions.ErrorConnectingServerException cannot connect to the server side
     */
    public void signUpButtonClickHandler() {
        //Barra de progreso que indica el estado del registro.
        progress_indicator.setVisible(true);
        
        //Boton de registro desactivado.
        btn_SignUp.setDisable(true);
        try {
            //Establecer parametros de usuario
            
            User user = new User();
            user.setEmail(this.txt_Email.getText());
            user.setFullName(this.txt_Firstname.getText() + " " + this.txt_Lastname.getText());
            user.setPassword(this.txt_RepeatPassword.getText());
            user.setPrivilege(UserPrivilege.USER);
            user.setStatus(UserStatus.ENABLED);
            user.setLogin(this.txt_Username.getText());
            
            //Llama a la clase implementación para mandar el usuario al servidor.
            this.signableImplementation.signUp(user);
            btn_SignUp.setDisable(false);
            
            //Si no hay error, informa al usuario que el registro ha sido correcto.
            Alert alert = new Alert(AlertType.CONFIRMATION);
            alert.setTitle("Signed Up Successfully");
            
            /*Haciendo click en la alerta y comprobando que no ha habido errores,
            abre la ventana de signin.*/
            
            String s = "You have been signed up. Do you want to go to the login screen?";
            alert.setContentText(s);
            Optional<ButtonType> result = alert.showAndWait();
            if ((result.isPresent()) && (result.get() == ButtonType.OK)) {
                this.changeStageToLogin();
            }
            
            /*Se capturan los distintos errores posibles, mostrando la 
            correspondiente alerta al usuario.*/
        } catch (ErrorConnectingDatabaseException ex) {
            logger.log(Level.SEVERE, "Attempting to sign in.");
            Logger.getLogger(SignUpController.class.getName()).log(Level.SEVERE, null, ex);
            btn_SignUp.setDisable(false);
        } catch (UserAlreadyExistException ex) {
            logger.log(Level.SEVERE, "User already exist.");
            this.btn_SignUp.setDisable(true);
            this.hint_Username.setText("The username or the email already exists.");
            this.hint_Email.setText("The username or the email already exists.");
            this.hint_Email.setTextFill(javafx.scene.paint.Color.RED);
            this.hint_Username.setTextFill(javafx.scene.paint.Color.RED);
            this.validationUtils.addClass(this.txt_Email, "error", Boolean.TRUE);
            this.validationUtils.addClass(this.txt_Username, "error", Boolean.TRUE);
        } catch (QueryException ex) {
            logger.log(Level.SEVERE, "Error doing a query in the database.");
            btn_SignUp.setDisable(false);
        } catch (ErrorConnectingServerException ex) {
            Alert alert = new Alert(AlertType.CONFIRMATION);
            alert.setTitle("Error connecting server");
            String s = "The server couldn't be reached. Try again?";
            alert.setContentText(s);
            Optional<ButtonType> result = alert.showAndWait();
            if ((result.isPresent()) && (result.get() == ButtonType.OK)) {
                this.signUpButtonClickHandler();
            }
            logger.log(Level.SEVERE, "Error connecting to the server.");
            btn_SignUp.setDisable(false);
        } finally {
            //Cuando acaba todo el proceso de registro, desaparece el indicador de progreso.
            progress_indicator.setVisible(false);

        }
    }

    /**
     * Closes the current stage and open a new one with the signin controller.
     */
    public void changeStageToLogin() {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/signupsignin/view/SignIn.fxml"));
        Parent root = null;
        try {
            root = (Parent) loader.load();
            stage.close();
        } catch (IOException ex) {
            logger.log(Level.SEVERE, "Cant change to login stage.");
        }

        SignInController controller = ((SignInController) loader.getController());

        controller.setSignable(this.signableImplementation);
        controller.setStage(this.stage);
        controller.initStage(root);
    }

    /**
     * If the cancel button is pressed, it asks the user if they are sure to 
     * return to the login window.
     */
    public void backButtonClickHandler() {
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Go back");
        String s = "Are you sure you want to go back? The data will be lost.";
        alert.setContentText(s);
        Optional<ButtonType> result = alert.showAndWait();
        if ((result.isPresent()) && (result.get() == ButtonType.OK)) {
            this.changeStageToLogin();
        }
    }
    private void handleCloseRequest(WindowEvent event){
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Close confirmation");
        alert.setHeaderText("Application will be closed");
        alert.setContentText("You will close the application");
        alert.getButtonTypes().setAll(ButtonType.OK, ButtonType.CANCEL);
        Optional<ButtonType> result = alert.showAndWait();
        if(result.get().equals(ButtonType.OK)){
            stage.close();
            Platform.exit();
        }else {
            event.consume();
            alert.close();
        }
    }

}
