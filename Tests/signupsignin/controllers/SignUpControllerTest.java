/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package signupsignin.controllers;

import signupsignin.SignUpSignInClient;
import org.junit.Test;
import static org.junit.Assert.*;
import java.util.concurrent.TimeoutException;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import static org.testfx.api.FxAssert.verifyThat;
import org.testfx.api.FxToolkit;
import org.testfx.framework.junit.ApplicationTest;
import static org.testfx.matcher.base.NodeMatchers.isDisabled;
import static org.testfx.matcher.base.NodeMatchers.isEnabled;
import static org.testfx.matcher.base.NodeMatchers.isVisible;
import static org.testfx.matcher.control.TextInputControlMatchers.hasText;

/**
 *
 * @author Mikel
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class SignUpControllerTest extends ApplicationTest {

    private static final String OVERSIZED_TEXT = "XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX"
            + "XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX"
            + "XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX"
            + "XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX";
    private static final String VALID_EMAIL = "validEmail@gmail.com";
    private TextField txt_Firstname;
    private TextField txt_Lastname;
    private TextField txt_Email;
    private TextField txt_Username;
    private ProgressIndicator progress_username;
    private PasswordField txt_Password;
    private PasswordField txt_RepeatPassword;
    private Button btn_SignUp;

    public SignUpControllerTest() {
    }

    @BeforeClass
    public static void setUpClass() throws TimeoutException {
        FxToolkit.registerPrimaryStage();
        FxToolkit.setupApplication(SignUpSignInClient.class);
    }

    @Override
    public void start(Stage stage) throws Exception {
        new SignUpSignInClient().start(stage);
        clickOn("#btnSignUp");
    }

    @Override
    public void stop() {
    }

    @Test
    public void testA_SignUpButtonDisabledOnStart() {
        verifyThat("#btn_SignUp", isDisabled());
    }

    @Test
    public void testB_cancelButtonTest() {
        clickOn("#btn_Cancel");
        verifyThat("#signInPane", isVisible());
        clickOn("#btnSignUp");
        verifyThat("#signUpPane", isVisible());
    }

    @Test
    public void testC_emailValidTest() {
        this.txt_Email = lookup("#txt_Email").query();
        clickOn("#txt_Email").write(OVERSIZED_TEXT);
        assertEquals(this.txt_Email.getProperties().get("emailValidator"), false);
        this.txt_Email.clear(); // Clear input text
        clickOn("#txt_Email").write(VALID_EMAIL);
        assertEquals(this.txt_Email.getProperties().get("emailValidator"), true);
    }

}
