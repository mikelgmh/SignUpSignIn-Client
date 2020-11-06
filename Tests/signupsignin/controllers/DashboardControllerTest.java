/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package signupsignin.controllers;

import javafx.scene.input.KeyCode;
import javafx.stage.Stage;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.testfx.api.FxAssert.verifyThat;
import org.testfx.api.FxRobot;
import org.testfx.framework.junit.ApplicationTest;
import static org.testfx.matcher.base.NodeMatchers.isDisabled;
import signupsignin.SignUpSignInClient;
import static org.testfx.matcher.base.NodeMatchers.isVisible;

/**
 *
 * @author Aketza
 */
public class DashboardControllerTest extends ApplicationTest {

    @Override
    public void start(Stage stage) throws Exception {
        new SignUpSignInClient().start(stage);
    }

    @Test
    public void test() {
        clickOn("#txt_User");
        write("imamontoro");
        clickOn("#txt_Password");
        write("Abcd*1234");
        clickOn("#btn_SignIn");
        clickOn("#btn_Logout");
        clickOn("Aceptar");
        verifyThat("#paneSignIn", isVisible());
    }
}
