/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package signupsignin.controllers;

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
 * @author Iker
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class SignInControllerTest extends ApplicationTest {

    @Override
    public void start(Stage stage) throws Exception {
        new SignUpSignInClient().start(stage);
    }

    @Test
    public void testA_InitialStageIsCorrect() {
        verifyThat("#txt_User", isFocused());
        verifyThat("#txt_User", hasText(""));
        verifyThat("#txt_Password", hasText(""));
        verifyThat("#btn_SignUp", isEnabled());
        verifyThat("#btn_SignIn", isDisabled());
    }

    @Test
    public void testB_UserNotFoundAlert() {
        clickOn("#txt_User");
        write("asojkdn");
        clickOn("#txt_Password");
        write("asujidn%");
        clickOn("#btn_SignIn");
        verifyThat("The user entered does not exist in the database.", isVisible());
        clickOn("Aceptar");  
    }

    @Test
    public void testC_IncorrectPasswordAlert() {
        clickOn("#txt_User");
        write("imamontoro");
        clickOn("#txt_Password");
        write("asujidn%");
        clickOn("#btn_SignIn");
        verifyThat("The password inserted does not match with the current user. Enter a valid password.", isVisible());
        clickOn("Aceptar");
        
    }

    @Test
    public void testD_SignInButtonChecker() {
        clickOn("#txt_User");
        write("Imanol");
        verifyThat("#btn_SignIn", isDisabled());
        clickOn("#txt_Password");
        write("asujidn%");
        verifyThat("#btn_SignIn", isEnabled());
        eraseText(8);
        verifyThat("#btn_SignIn", isDisabled());
        clickOn("#txt_User");
        eraseText(6);
        verifyThat("#btn_SignIn", isDisabled());
    }

    @Test
    public void testE_SignUpStageIsVisible() {
        clickOn("#btn_SignUp");
        verifyThat("#signUpPane", isVisible());
    }

    @Test
    public void testF_DashboardStageIsVisible() {
        clickOn("#txt_User");
        write("imamontoro");
        clickOn("#txt_Password");
        write("Abcd*1234");
        clickOn("#btn_SignIn");
        verifyThat("#paneDashboard", isVisible());
    }
}
