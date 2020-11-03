/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package signupsignin.controllers;

// Library
import exceptions.ErrorClosingDatabaseResources;
import exceptions.ErrorConnectingDatabaseException;
import exceptions.ErrorConnectingServerException;
import exceptions.PasswordMissmatchException;
import exceptions.QueryException;
import exceptions.UserNotFoundException;
import interfaces.Signable;
import user.User;

// Java
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

// JavaFX
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

/**
 *
 * @author Iker, Aketza
 */
public class SignInController {

    private static final Logger logger = Logger.getLogger("signupsignin.controllers.SignInController");

    private Stage stage;
    private Signable signableImplementation;

    @FXML
    private Button btnSignIn;
    @FXML
    private Button btnSignUp;
    @FXML
    private TextField txtUser;
    @FXML
    private PasswordField txtPassword;

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public SignInController() {
    }

    public void setSignable(Signable signable) {
        this.signableImplementation = signable;
    }

    public void initStage(Parent root) {
        logger.log(Level.INFO, "Loading the SignIn stage.");
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("Login");
        stage.setResizable(false);
        stage.setOnShowing(this::handleWindowShowing);
        txtUser.textProperty().addListener(this::textChanged);
        txtPassword.textProperty().addListener(this::textChanged);
        stage.show();
        logger.log(Level.INFO, "SignIn stage loaded.");
    }

    private void handleWindowShowing(WindowEvent event) {
        btnSignIn.setDisable(true);
        txtUser.setPromptText("Insert username");
        txtPassword.setPromptText("Insert password");
        btnSignIn.setDefaultButton(true);
        btnSignIn.setTooltip(new Tooltip("Send identification values"));
        btnSignUp.setTooltip(new Tooltip("Create a new account"));
    }

    private void textChanged(ObservableValue observable, String oldValue, String newValue) {
        if (this.txtUser.getText().trim().equals("") || this.txtPassword.getText().trim().equals("")) {
            btnSignIn.setDisable(true);
        } else {
            btnSignIn.setDisable(false);
        }
    }

    @FXML
    private void handleOnClickRegister(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/signupsignin/view/SignUp.fxml"));
        Parent root = (Parent) loader.load();
        SignUpController controller = ((SignUpController) loader.getController());
        controller.setStage(stage);
        controller.setSignable(this.signableImplementation);
        controller.initStage(root);
    }

    @FXML
    private void handleOnClickLogin(ActionEvent event) throws IOException {
        logger.log(Level.INFO, "Signing in.");
        // Guardamos la información de user y password dentro de la clase User
        User user = new User();
        user.setLogin(txtUser.getText());
        user.setPassword(txtPassword.getText());

        try {
            // Enviamos los datos al SignableImplementation para hacer la comprobación con la BD.
            user = this.signableImplementation.signIn(user);
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/signupsignin/view/Dashboard.fxml"));
            Parent root = (Parent) loader.load();
            DashboardController controllerDashboard = ((DashboardController) loader.getController());
            controllerDashboard.setUser(user);
            controllerDashboard.initStage(root);
            // Por último, cerramos la ventana de Login
            stage.close();
            logger.log(Level.INFO, "Signed In successfully");
        } catch (ErrorConnectingDatabaseException ex) {
            logger.log(Level.WARNING, "Error connecting to the database.");
            Alert alertConnectingToDatabase = new Alert(Alert.AlertType.ERROR);
            alertConnectingToDatabase.setTitle("Error with the server.");
            alertConnectingToDatabase.setHeaderText("Error connecting to the database.");
            alertConnectingToDatabase.setContentText("An error ocurred trying to connect to the database. Contact the Administrator.");
            alertConnectingToDatabase.showAndWait();
        } catch (UserNotFoundException ex) {
            logger.log(Level.WARNING, "User not found.");
            Alert alertUserNotFound = new Alert(Alert.AlertType.WARNING);
            alertUserNotFound.setTitle("User not found.");
            alertUserNotFound.setHeaderText("User not found.");
            alertUserNotFound.setContentText("The user entered does not exist in the database.");
            alertUserNotFound.showAndWait();
        } catch (PasswordMissmatchException ex) {
            logger.log(Level.WARNING, "The password inserted does not match with the current user.");
            Alert alertPasswordNoMatch = new Alert(Alert.AlertType.WARNING);
            alertPasswordNoMatch.setTitle("The password does not match.");
            alertPasswordNoMatch.setHeaderText("Incorrect password.");
            alertPasswordNoMatch.setContentText("The password inserted does not match with the current user. Enter a valid password.");
            alertPasswordNoMatch.showAndWait();
        } catch (ErrorClosingDatabaseResources ex) {
            logger.log(Level.WARNING, "Error closing the database resources.");
            Alert alertClosingDatabase = new Alert(Alert.AlertType.ERROR);
            alertClosingDatabase.setTitle("Unexpected error.");
            alertClosingDatabase.setHeaderText("Unexpected error ocurred.");
            alertClosingDatabase.setContentText("An unexpected error ocurred with the database. Contact the server Administrator.");
            alertClosingDatabase.showAndWait();
        } catch (QueryException ex) {
            logger.log(Level.WARNING, "Error doing a query in the database.");
            Alert alertConnectingToDatabase = new Alert(Alert.AlertType.ERROR);
            alertConnectingToDatabase.setTitle("Unexpected error");
            alertConnectingToDatabase.setHeaderText("Unexpected error ocurred.");
            alertConnectingToDatabase.setContentText("An unexpected error ocurred with the database. Contact the server Administrator.");
            alertConnectingToDatabase.showAndWait();
        } catch (ErrorConnectingServerException ex) {
            logger.log(Level.WARNING, "Error connecting to the server.");
            Alert alertConnectingToDatabase = new Alert(Alert.AlertType.ERROR);
            alertConnectingToDatabase.setTitle("Error with the server.");
            alertConnectingToDatabase.setHeaderText("Error connecting to the server.");
            alertConnectingToDatabase.setContentText("Can not connect to the server, try to restart the application or contact the server Administrator.");
            alertConnectingToDatabase.showAndWait();
        }
    }
}
