/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import controllers.SignUpController;
import interfaces.Signable;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import signable.SignableFactory;

/**
 *
 * @author Mikel
 */
public class SignUpSignInClient extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/SignUp.fxml"));
        Parent root = (Parent) loader.load();

        SignUpController controller = ((SignUpController) loader.getController());
        
        SignableFactory signableFactory = new SignableFactory();
        Signable signableImplementarion = signableFactory.getSignableImplementation("CLIENT_SIGNABLE");
        
        controller.setSignable(signableImplementarion);
        controller.setStage(stage);
        controller.initStage(root);

    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

}
