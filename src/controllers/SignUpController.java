/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXSpinner;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.validation.RequiredFieldValidator;
import javafx.animation.PauseTransition;
import javafx.scene.input.KeyEvent;
import javafx.event.EventTarget;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Spinner;
import javafx.scene.control.Tooltip;
import javafx.stage.Stage;
import javafx.util.Duration;
import sun.security.util.Length;

/**
 *
 * @author Mikel
 */
public class SignUpController {

    private Stage stage;

    @FXML
    private Button btnSignUp;
    @FXML
    private JFXPasswordField txtPassword;
    @FXML
    private JFXPasswordField txtRepeatPassword;

    @FXML
    private JFXSpinner spinnerUsername;

    @FXML
    private JFXTextField txtUsername;

    private RequiredFieldValidator EmptyFieldValidator = new RequiredFieldValidator();

    public Stage getStage() {
        return this.stage;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void initStage(Parent parent) {
        Scene scene = new Scene(parent);
        stage.setScene(scene);
        stage.setTitle("Register");
        stage.setResizable(false);

        // Default component status on load
        this.spinnerUsername.setVisible(false);
        this.btnSignUp.setDisable(true);

        // Set Validators
        this.setValidators();
        this.txtPassword.setValidators(this.EmptyFieldValidator);
        this.txtRepeatPassword.setValidators(this.EmptyFieldValidator);

        // Events
        this.txtRepeatPassword.setOnKeyPressed(this::repeatPasswordEventHandler);
        this.txtPassword.setOnKeyPressed(this::passwordEventHandler);

        PauseTransition pause = new PauseTransition(Duration.seconds(1)); // Delay de 1s
        this.txtUsername.textProperty().addListener((observable, oldValue, newValue) -> {
            this.spinnerUsername.setVisible(true);
            pause.setOnFinished(event
                    -> this.spinnerUsername.setVisible(false)
            );
            pause.playFromStart();
        }
        );

        stage.show();
    }

    public void passwordEventHandler(KeyEvent event) {
        txtPassword.validate();
    }

    public void repeatPasswordEventHandler(KeyEvent event) {
        txtRepeatPassword.validate();
    }

    public void setValidators() {
        EmptyFieldValidator.setMessage("Field required.");
    }

    public void validateForm() {

    }
}
