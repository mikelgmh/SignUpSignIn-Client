/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package signupsignin.controllers;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.stage.Stage;
import user.User;

/**
 *
 * @author Imanol
 */
public class DashboardController {

    private Stage stage;
    private User user;
    
    @FXML
    private Button btn_Logout;
    
    @FXML
    private Label lbl_Connection;
    
    @FXML
    private Label lbl_Welcome;

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
        lbl_Connection.setText(getFormatterDate());
        lbl_Welcome.setText(user.getFullName());
        btn_Logout.setTooltip(new Tooltip("Return to Sign In"));
        
        stage.show();  
    }
    
    @FXML
    private void handleOnClickSignIn(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/signupsignin/view/SignIn.fxml"));
        Parent root = (Parent) loader.load();
        SignInController controller = ((SignInController) loader.getController());
        controller.setStage(stage);
        controller.initStage(root);
    }

    public void setUser(User user) {
        this.user=user;
    }
    
    private String getFormatterDate (){
        DateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");
        String strDate = dateFormat.format(user.getLastAccess());
        return strDate;
    }

}
