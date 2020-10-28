/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package signupsignin.controllers;

import interfaces.Signable;
import javafx.event.ActionEvent;
import java.io.IOException;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import user.User;

/**
 *
 * @author Iker, Aketza
 */
public class SignInController {

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
        //TODO: Comprobar los datos en la base de datos.
        //Guardamos la información de user y password dentro de la clase User
        User user = new User();
        user.setLogin(this.txtUser.getText());
        user.setPassword(this.txtPassword.getText());

        //Enviamos los datos al SignableImplementation para hacer la comprobación con la BD.
        // this.signableImplementation.signIn(user);
        //TODO: Una vez los datos sean correctos, pasar a la ventana de Dashboard con los datos del User.
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/signupsignin/view/Dashboard.fxml"));
        Parent root = (Parent) loader.load();
        DashboardController controller = ((DashboardController) loader.getController());
        controller.setStage(stage);
        controller.initStage(root);
    }
}
