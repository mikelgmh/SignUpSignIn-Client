package signupsignin;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import signupsignin.controllers.SignInController;
import interfaces.Signable;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.stage.Stage;
import signupsignin.signable.SignableFactory;

/**
 *
 * @author
 */
public class SignUpSignInClient extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/signupsignin/view/SignIn.fxml"));
        Parent root = (Parent) loader.load();
        SignInController controller = ((SignInController) loader.getController());
        SignableFactory signableFactory = new SignableFactory();
        Signable signable = signableFactory.getSignableImplementation();

        controller.setSignable(signable);
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
