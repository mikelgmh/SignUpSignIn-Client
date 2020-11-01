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
        verifyThat("#txtUser", isFocused());
        verifyThat("#txtUser", hasText(""));
        verifyThat("#txtPassword", hasText(""));
        verifyThat("#btnSignUp", isEnabled());
        verifyThat("#btnSignIn", isDisabled());
    }

    @Test
    public void testB_UserNotFoundAlert() {
        clickOn("#txtUser");
        write("asojkdn");
        clickOn("#txtPassword");
        write("asujidn%");
        clickOn("#btnSignIn");
        clickOn("OK");
        verifyThat("#paneDashboard", isNull());
    }

    @Test
    public void testC_IncorrectPasswordAlert() {
        clickOn("#txtUser");
        write("Imanol");
        clickOn("#txtPassword");
        write("asujidn%");
        clickOn("#btnSignIn");
        clickOn("OK");
        verifyThat("#paneDashboard", isNull());
    }

    @Test
    public void testD_SignInButtonChecker() {
        clickOn("#txtUser");
        write("Imanol");
        verifyThat("#btnSignIn", isDisabled());
        clickOn("#txtPassword");
        write("asujidn%");
        verifyThat("#btnSignIn", isEnabled());
        eraseText(8);
        verifyThat("#btnSignIn", isDisabled());
        clickOn("#txtUser");
        eraseText(6);
        verifyThat("#btnSignIn", isDisabled());
    }

    @Test
    public void testE_SignUpStageIsVisible() {
        clickOn("#btnSignUp");
        verifyThat("#signUpPane", isVisible());
    }

    @Test
    public void testF_DashboardStageIsVisible() {
        clickOn("#txtUser");
        write("Imanol");
        clickOn("#txtPassword");
        write("Qwer1234%");
        clickOn("#btnSignIn");
        verifyThat("#paneDashboard", isVisible());
    }
}
