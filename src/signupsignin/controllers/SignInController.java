/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package signupsignin.controllers;

import javafx.event.ActionEvent;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.beans.value.ObservableValue;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.input.InputMethodEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

/**
 *
 * @author Iker, Aketza
 */
public class SignInController implements Initializable {

    private Stage stage;

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

    public void initStage(Parent root) {
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("Login");
        stage.setResizable(false);
        stage.setOnShowing(this::handleWindowShowing);
        txtUser.textProperty().addListener(this::textChanged);
        txtPassword.textProperty().addListener(this::textChanged);
        stage.show();
    }

    private void handleWindowShowing(WindowEvent event) {
        btnSignIn.setDisable(true);
        txtUser.setPromptText("Insert username");
        txtPassword.setPromptText("Insert password");
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
        controller.initStage(root);
    }

    @FXML
    private void handleOnClickLogin(ActionEvent event) throws IOException {
        //TODO: Comprobar los datos de la base de datos.
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/signupsignin/view/Dashboard.fxml"));
        Parent root = (Parent) loader.load();
        DashboardController controller = ((DashboardController) loader.getController());
        controller.setStage(stage);
        controller.initStage(root);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //To change body of generated methods, choose Tools | Templates.
    }

}
