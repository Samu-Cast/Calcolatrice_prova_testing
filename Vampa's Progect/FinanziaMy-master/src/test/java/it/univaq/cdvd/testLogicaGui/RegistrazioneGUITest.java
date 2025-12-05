
package it.univaq.cdvd.testLogicaGui;

import it.univaq.cdvd.controller.RegistrazioneController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.testfx.api.FxToolkit;
import org.testfx.framework.junit5.ApplicationTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class RegistrazioneGUITest extends ApplicationTest {

    RegistrazioneController controller;

    @Override
    public void start(Stage stage) throws Exception {
        // Carica l'FXML direttamente
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/registrazione.fxml"));
        Parent root = loader.load();
        controller = loader.getController();

        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @Test
    void registerUnvalidCredentialButtonTest() throws Exception {

        clickOn(controller.usernameTextField).write("");
        clickOn(controller.passwordPasswordField).write("p");
        clickOn(controller.emailTextField).write("esempio@gmail.com");
        clickOn(controller.saldoTextField).write("0.00");
        clickOn(controller.registratiButton);

        assertEquals("Username, email o password non validi.", controller.registerMessage.getText(), "The auth.fxml page should be displayed.");
    }



    @Test

    void testAnnulButtonDisplaysLandingPage() throws Exception {
        clickOn("#annullaButton");


        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/landing.fxml"));
        Parent expectedRoot = loader.load();

        Parent actualRoot = FxToolkit.toolkitContext().getRegisteredStage().getScene().getRoot();

        assertEquals(expectedRoot.getId(), actualRoot.getId(), "The landing.fxml page should be displayed.");
    }

    /*
    @Test
    public void testSession () {
        clickOn(controller.usernameTextField).write("prova3");
        clickOn(controller.passwordPasswordField).write("p");
        clickOn(controller.emailTextField).write("esempio@gmail.com");
        clickOn(controller.saldoTextField).write("0.00");
        clickOn(controller.registratiButton);

        assertEquals(SessionManager.getInstance().getUtente().getUsername(),"prova3");
    }

     */


}

