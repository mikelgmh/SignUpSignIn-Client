/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package signupsignin;

import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.testfx.framework.junit.ApplicationTest;

import javafx.stage.Stage;
import signupsignin.SignUpSignInClient;

import org.junit.FixMethodOrder;
import static org.testfx.api.FxAssert.verifyThat;
import static org.testfx.matcher.base.NodeMatchers.isDisabled;
import static org.testfx.matcher.base.NodeMatchers.isEnabled;
import static org.testfx.matcher.base.NodeMatchers.isVisible;
import static org.testfx.matcher.base.NodeMatchers.isNull;
import static org.testfx.matcher.base.NodeMatchers.isInvisible;
import static org.testfx.matcher.base.NodeMatchers.isNotFocused;
import static org.testfx.matcher.base.NodeMatchers.isFocused;
import static org.testfx.matcher.base.NodeMatchers.isNotNull;
import static org.testfx.matcher.control.TextInputControlMatchers.hasText;

/**
 *
 * @author Aketza, Iker
 */
public class SignUpSignInClientTest extends ApplicationTest {

    @Override
    public void start(Stage stage) throws Exception {
        new SignUpSignInClient().start(stage);
    }

    @Test
    public void test() {
        clickOn("#btn_SignUp");
        clickOn("#txt_Firstname");
        write("Iker");
        clickOn("#txt_Lastname");
        write("de la Cruz");
        clickOn("#txt_Email");
        write("ikerdefens@hotmail.com");
        clickOn("#txt_Username");
        write("iker");
        clickOn("#txt_Password");
        write("Qwer1234%");
        clickOn("#txt_RepeatPassword");
        write("Qwer1234%");
        clickOn("#btn_SignUp");
        clickOn("Aceptar");
        clickOn("#txt_User");
        write("iker");
        clickOn("#txt_Password");
        write("Qwer1234%");
        clickOn("#btn_SignIn");
        verifyThat("#paneDashboard", isVisible());
    }
}
