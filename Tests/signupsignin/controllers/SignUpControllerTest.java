/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package signupsignin.controllers;

import signupsignin.SignUpSignInClient;
import static org.junit.Assert.*;
import java.util.concurrent.TimeoutException;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.hamcrest.Matchers;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import static org.testfx.api.FxAssert.verifyThat;
import org.testfx.api.FxToolkit;
import org.testfx.framework.junit.ApplicationTest;
import static org.testfx.matcher.base.NodeMatchers.isDisabled;
import static org.testfx.matcher.base.NodeMatchers.isEnabled;
import static org.testfx.matcher.base.NodeMatchers.isFocused;
import static org.testfx.matcher.base.NodeMatchers.isVisible;
import static org.testfx.matcher.control.TextInputControlMatchers.hasText;

/**
 *
 * @author Mikel, Imanol
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class SignUpControllerTest extends ApplicationTest {

    private static final String OVERSIZED_TEXT = "XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX"
            + "XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX"
            + "XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX"
            + "XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX";
    private static final String VALID_EMAIL = "validEmail@gmail.com";
    private TextField txt_Email;

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
        clickOn("#btn_SignUp");
    }

    @Override
    public void stop() {
    }

    @Test
    public void test1_SignUpButtonDisabledOnStartTest() {
        verifyThat("#txt_Firstname", isFocused());
        verifyThat("#txt_Firstname", hasText(""));
        verifyThat("#txt_Lastname", hasText(""));
        verifyThat("#txt_Email", hasText(""));
        verifyThat("#txt_Username", hasText(""));
        verifyThat("#txt_Password", hasText(""));
        verifyThat("#txt_RepeatPassword", hasText(""));
        verifyThat("#btn_Cancel", isEnabled());
        verifyThat("#btn_SignUp", isDisabled());
    }

    @Test
    public void test2_cancelButtonTest() {
        clickOn("#btn_Cancel");
        clickOn("Aceptar");
        verifyThat("#paneSignIn", isVisible());
        clickOn("#btn_SignUp");

    }

    @Test
    public void test3_SignUpIncorrectPasswordTest() {
        clickOn("#txt_Firstname");
        write("Jaime");
        clickOn("#txt_Lastname");
        write("San Sebastian");
        clickOn("#txt_Email");
        write("sebas@gmail.com");
        clickOn("#txt_Username");
        write("JSebas");
        clickOn("#txt_Password");
        write("Password01&");
        clickOn("#txt_RepeatPassword");
        write("99Password&");
        verifyThat("#btn_SignUp", isDisabled());
    }

    @Test
    public void test4_emailValidAndRegisterTest() {
        clickOn("#txt_Firstname");
        write("Jaime");
        clickOn("#txt_Lastname");
        write("San Sebastian");
        this.txt_Email = lookup("#txt_Email").query();
        clickOn("#txt_Email").write(OVERSIZED_TEXT);
        assertEquals(Boolean.parseBoolean(this.txt_Email.getProperties().get("emailValidator").toString()), false);
        clickOn("#txt_Username");
        write("JSebas");
        clickOn("#txt_Password");
        write("Password01&");
        clickOn("#txt_RepeatPassword");
        write("Password01&");
        this.txt_Email.clear(); // Clear input text
        clickOn("#txt_Email").write(VALID_EMAIL);
        assertEquals(Boolean.parseBoolean(this.txt_Email.getProperties().get("emailValidator").toString()), true);
        clickOn("#btn_SignUp");
        clickOn("Aceptar");
        verifyThat("#paneSignIn", isVisible());
    }
}
