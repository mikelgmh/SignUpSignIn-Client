package signupsignin.controllers;

import interfaces.Signable;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Optional;

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
import user.User;

/**
 *
 * @author Imanol
 */
public class DashboardController {

    private Stage stage;
    private User user;
    private Signable signableImplementation;

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

    public void initStage(Parent root) {
        Scene scene = new Scene(root);
        stage = new Stage();
        stage.setScene(scene);
        stage.setTitle("Dashboard");
        stage.setResizable(false);
        lbl_Connection.setText("Last connection: " + getFormatterDate());
        lbl_Welcome.setText("Hello " + user.getFullName());
        btn_Logout.setTooltip(new Tooltip("Return to Sign In"));
        stage.show();
    }

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

    public void setUser(User user) {
        this.user = user;
    }

    private String getFormatterDate() {
        String pattern = "dd-M-yyyy HH:mm:ss";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern, new Locale("fr", "FR"));
        String date = simpleDateFormat.format(new Date());
        return date;
    }
}
