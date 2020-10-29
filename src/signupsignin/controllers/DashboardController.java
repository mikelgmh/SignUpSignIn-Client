/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package signupsignin.controllers;

import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

/**
 *
 * @author Imanol
 */
public class DashboardController {

    private Stage stage;
    
    @FXML
    private Button btn_Logout;
    
    @FXML
    private Label lbl_Connection;

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public DashboardController() {
    }

    public void initStage(Parent root) {
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("Dashboard");
        stage.setResizable(false);
        stage.setOnShowing(this::handleWindowShowing);
        stage.show();  
    }

    private void handleWindowShowing(WindowEvent event) {      
        lbl_Connection.setText("Prueba");
        btn_Logout.setTooltip(new Tooltip("Return to Sign In"));

    }
    
    @FXML
    private void handleOnClickSignIn(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/signupsignin/view/SignIn.fxml"));
        Parent root = (Parent) loader.load();
        SignInController controller = ((SignInController) loader.getController());
        controller.setStage(stage);
        controller.initStage(root);
    }

}
