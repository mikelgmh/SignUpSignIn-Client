package signupsignin;

import signupsignin.controllers.SignInController;

import java.util.logging.Level;
import java.util.logging.Logger;

import interfaces.Signable;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.stage.Stage;
import signupsignin.signable.SignableFactory;

/**
 * Contains main class which calls the first controller and opens signin window.
 * 
 * @author Iker, Aketza
 */
public class SignUpSignInClient extends Application {

    private static final Logger logger = Logger.getLogger("signupsignin.SignUpSignInClient");

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
        logger.log(Level.INFO, "Initializing the program.");
        launch(args);
    }
}
