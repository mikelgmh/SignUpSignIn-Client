/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package signupsignin.controllers;

import signupsignin.SignUpSignInClient;
import static org.junit.Assert.*;
import java.util.concurrent.TimeoutException;
import javafx.fxml.FXML;
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
import static org.testfx.matcher.base.NodeMatchers.isFocused;
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
            + "XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX"
            + "XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX"
            + "XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX";
    private static final String VALID_EMAIL = "validEmail@gmail.com";

    @FXML
    private TextField txt_Email;

    @FXML
    private TextField txt_Firstname;

    @FXML
    private TextField txt_Lastname;

    @FXML
    private TextField txt_Username;

    @FXML
    private TextField txt_RepeatPassword;
    @FXML
    private TextField txt_Password;

    public SignUpControllerTest() {
    }

    @BeforeClass
    public static void setUpClass() throws TimeoutException, Exception {
        ApplicationTest.launch(SignUpSignInClient.class);

    }

//    @Override
//    public void start(Stage stage) throws Exception {
//        stage.show();
//        clickOn("#btn_SignUp");
//
//    }
    @Override
    public void stop() {
    }

    @Test
    public void testA_SignUpButtonDisabledOnStartTest() {
        clickOn("#btn_SignUp");
        verifyThat("#txt_Firstname", isFocused());
        verifyThat("#txt_Firstname", hasText(""));
        verifyThat("#txt_Lastname", hasText(""));
        verifyThat("#txt_Email", hasText(""));
        verifyThat("#txt_Username", hasText(""));
        verifyThat("#txt_Password", hasText(""));
        verifyThat("#txt_RepeatPassword", hasText(""));
        verifyThat("#btn_Cancel", isEnabled());
        verifyThat("#btn_SignUpRegister", isDisabled());
    }

    @Test
    public void testB_FirstNameMaxLengthTest() {
        txt_Firstname = lookup("#txt_Firstname").query();
        clickOn("#txt_Firstname");
        write(OVERSIZED_TEXT);
        assertTrue(txt_Firstname.getText().length() <= 125);
        txt_Firstname.clear();
    }

    @Test
    public void testC_FirstNameMinLengthTest() {
        txt_Firstname = lookup("#txt_Firstname").query();
        clickOn("#txt_Firstname");
        write("Mi");
        assertTrue(Boolean.parseBoolean(txt_Firstname.getProperties().get("minLengthValidator").toString()) == false);
        txt_Firstname.clear();
        write("Mikel");
        assertTrue(Boolean.parseBoolean(txt_Firstname.getProperties().get("minLengthValidator").toString()) == true);

    }

    @Test
    public void testD_LastNameMaxLengthTest() {
        txt_Lastname = lookup("#txt_Lastname").query();
        clickOn("#txt_Lastname");
        write(OVERSIZED_TEXT);
        assertTrue(txt_Lastname.getText().length() <= 125);
    }

    @Test
    public void testE_LastNameMinLengthTest() {
        txt_Lastname = lookup("#txt_Lastname").query();
        clickOn("#txt_Lastname");
        txt_Lastname.clear();
        write("ef");
        assertTrue(Boolean.parseBoolean(txt_Lastname.getProperties().get("minLengthValidator").toString()) == false);
        txt_Lastname.clear();
        write("Granero");
        assertTrue(Boolean.parseBoolean(txt_Lastname.getProperties().get("minLengthValidator").toString()) == true);
    }

    @Test
    public void testF_EmailMaxLengthTest() {
        txt_Email = lookup("#txt_Email").query();
        clickOn("#txt_Email");
        write(OVERSIZED_TEXT);
        assertTrue(txt_Email.getText().length() <= 255);

    }

    @Test
    public void testG_EmailInvalidTest() {
        txt_Email = lookup("#txt_Email").query();
        clickOn("#txt_Email");
        txt_Email.clear();
        write("invalidEmail@@@@.");
        assertTrue(Boolean.parseBoolean(txt_Email.getProperties().get("emailValidator").toString()) == false);
    }

    @Test
    public void testH_EmailValidTest() {
        txt_Email = lookup("#txt_Email").query();
        clickOn("#txt_Email");
        txt_Email.clear();
        write("mikelgmh@gmail.com");
        assertTrue(Boolean.parseBoolean(txt_Email.getProperties().get("emailValidator").toString()) == true);
    }

    @Test
    public void testI_UsernameInvalidTest() {
        txt_Username = lookup("#txt_Username").query();
        clickOn("#txt_Username");
        write("Mikel Usuario");
        assertTrue(Boolean.parseBoolean(txt_Username.getProperties().get("singleWordValidator").toString()) == false);

    }

    @Test
    public void testJ_validUsernameTest() {
        txt_Username = lookup("#txt_Username").query();
        clickOn("#txt_Username");
        eraseText(20);
        write("Mikelin");
        assertTrue(Boolean.parseBoolean(txt_Username.getProperties().get("singleWordValidator").toString()) == true);
    }

    @Test
    public void testK_passwordMissmatchErrorTest() {
        txt_Password = lookup("#txt_Password").query();
        clickOn("#txt_Password");
        write("1234$%M");
        txt_RepeatPassword = lookup("#txt_RepeatPassword").query();
        clickOn("#txt_RepeatPassword");
        write("1234$%");
        assertTrue(txt_Password.getText().equals(txt_RepeatPassword.getText()) == false);
    }

    @Test
    public void testL_passwordsNotFulfillingRequirementsTest() {
        txt_Password = lookup("#txt_Password").query();
        txt_RepeatPassword = lookup("#txt_RepeatPassword").query();

        clickOn("#txt_Password");
        eraseText(8);
        write("1234$%M");
        clickOn("#txt_RepeatPassword");
        eraseText(8);
        write("1234$%M");

        assertTrue(txt_Password.getText().equals(txt_RepeatPassword.getText()) == true);
        assertTrue(Boolean.parseBoolean(txt_RepeatPassword.getProperties().get("passwordRequirements").toString()) == false);
    }

    @Test
    public void testM_validPasswordTest() {
        txt_Password = lookup("#txt_Password").query();
        txt_RepeatPassword = lookup("#txt_RepeatPassword").query();
        clickOn("#txt_Password");
        eraseText(8);
        write("1234$%Mm");
        clickOn("#txt_RepeatPassword");
        eraseText(8);
        write("1234$%Mm");
        assertTrue(txt_Password.getText().equals(txt_RepeatPassword.getText()) == true);
        assertTrue(Boolean.parseBoolean(txt_Password.getProperties().get("passwordRequirements").toString()) == true);
        verifyThat("#btn_SignUpRegister", isEnabled());
    }

    @Test
    public void testN_signUpButtonClickTest() {
        clickOn("#btn_SignUpRegister");
    }

}
