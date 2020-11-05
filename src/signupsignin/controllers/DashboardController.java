package signupsignin.controllers;

import interfaces.Signable;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Optional;
import javafx.application.Platform;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import user.User;

/**
 * This class handles the interaction of the user with the graphic user
 * interface, on window dashboard.
 *
 * @author Imanol
 */
public class DashboardController {

    //Se crea el stage y un usuario.
    private Stage stage;
    private User user;
    private Signable signableImplementation;

    //Se declaran los elementos de FXML de la ventana.
    @FXML
    private Button btn_Logout;

    @FXML
    private Label lbl_Connection;

    @FXML
    private Label lbl_Welcome;

    public DashboardController() {
    }

    public void setSignableImplementation(Signable signableImplementation) {
        this.signableImplementation = signableImplementation;
    }

    public void setUser(User user) {
        this.user = user;
    }

    /**
     * We initialize the dashboard window, settings the stage params.
     *
     * @param root The base object for all nodes that have children in the scene
     * graph.
     */
    public void initStage(Parent root) {
        Scene scene = new Scene(root);
        stage = new Stage();
        stage.setScene(scene);
        stage.setTitle("Dashboard");
        stage.setResizable(false);

        /*En los dos labels se muestran datos que hemos recibido desde el lado
        servidor*/
        lbl_Connection.setText("Last connection: " + getFormatterDate());
        lbl_Welcome.setText("Hello " + user.getFullName());
        btn_Logout.setTooltip(new Tooltip("Return to Sign In"));
        stage.onCloseRequestProperty().set(this::handleCloseRequest);
        stage.show();
    }

    /**
     * Events related to the Logout button. This method is referenced in
     * SceneBuilder.
     *
     * @param event related to the method is not used.
     * @throws IOException if there is an input / output error
     */
    @FXML
    private void handleOnClickLogout(ActionEvent event) throws IOException {

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Sign Out");
        String s = "Are you sure you want to logout?";
        alert.setContentText(s);
        Optional<ButtonType> result = alert.showAndWait();
        if ((result.isPresent()) && (result.get() == ButtonType.OK)) {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/signupsignin/view/SignIn.fxml"));
            Parent root = (Parent) loader.load();
            SignInController controller = ((SignInController) loader.getController());

            controller.setSignable(this.signableImplementation);
            controller.setStage(new Stage());
            controller.initStage(root);
            stage.close();
        }
    }

    /**
     * Method that changes the data to the desired format
     *
     * @return formatted data
     */
    private String getFormatterDate() {
        String pattern = "dd-M-yyyy HH:mm:ss";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern, new Locale("fr", "FR"));
        String date = simpleDateFormat.format(new Date());
        return date;
    }
    private void handleCloseRequest(WindowEvent event){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
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
