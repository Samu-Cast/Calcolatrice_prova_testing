package it.univaq.cdvd.testLogicaGui;

import it.univaq.cdvd.controller.HomeController;
import it.univaq.cdvd.dao.UtenteDAO;
import it.univaq.cdvd.model.Transazione;
import it.univaq.cdvd.model.Utente;
import it.univaq.cdvd.util.SessionManager;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableView;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.testfx.framework.junit5.ApplicationTest;

import java.util.concurrent.TimeoutException;

import static it.univaq.cdvd.util.HibernateUtil.setDbms;
import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class HomeControllerTest extends ApplicationTest {

    HomeController controller;
    TableView<Transazione> tabellaTransazioni;
    MenuItem aggiungiCategoria;
    MenuItem eliminaCategoria;
    MenuItem nuovaTransazione;
    MenuItem logout;
    Label saldo;

    @BeforeAll
    public void setup() throws TimeoutException {
        setDbms("hibernate-test.cfg.xml");
        Utente user = createTestUser();
        UtenteDAO udao = new UtenteDAO();
        udao.save(user);
        SessionManager.getInstance().setUtente(user);
    }

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/home.fxml"));
        loader.setControllerFactory(param -> {
            HomeController controller = new HomeController();
            SessionManager.getInstance().setUtente(createTestUser());
            return controller;
        });
        Parent root = loader.load();
        controller = loader.getController();
        stage.setScene(new Scene(root));
        stage.show();

        // Inizializza i componenti della UI da testare
        tabellaTransazioni = controller.tabellaTransazioni;
        aggiungiCategoria = controller.aggiungiCategoria;
        eliminaCategoria = controller.eliminaCategoria;
        nuovaTransazione = controller.nuovaTransazione;
        logout = controller.logout;
        saldo = controller.saldo;
    }

    @Test
    public void testAggiungiCategoria() throws Exception {
        clickOn("Categoria");
        clickOn("Aggiungi Categoria");

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/aggiuntacategoria.fxml"));
        Parent expectedRoot = loader.load();

        Parent actualRoot = getCurrentRoot();

        assertEquals(expectedRoot.getClass(), actualRoot.getClass(), "La pagina aggiuntacategoria.fxml dovrebbe essere visualizzata.");
    }

    @Test
    public void testEliminaCategoria() throws Exception {
        clickOn("Categoria");
        clickOn("Elimina Categoria");

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/eliminaCategoria.fxml"));
        Parent expectedRoot = loader.load();

        Parent actualRoot = getCurrentRoot();

        assertEquals(expectedRoot.getClass(), actualRoot.getClass(), "La pagina eliminaCategoria.fxml dovrebbe essere visualizzata.");
    }

    @Test
    public void testNuovaTransazione() throws Exception {
        clickOn("Transazioni");
        clickOn("Nuova transazione");

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/inserimento.fxml"));
        Parent expectedRoot = loader.load();

        Parent actualRoot = getCurrentRoot();

        assertEquals(expectedRoot.getClass(), actualRoot.getClass(), "La pagina inserimento.fxml dovrebbe essere visualizzata.");
    }


    @Test
    public void testModificaTransazione() throws Exception {
        clickOn("Transazioni");
        clickOn("Modifica transazione");

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/modifica.fxml"));
        Parent expectedRoot = loader.load();

        Parent actualRoot = loader.getRoot();

        assertEquals(expectedRoot,actualRoot, "La pagina modifica.fxml dovrebbe essere visualizzata.");
    }

    @Test
    public void testLogout() throws Exception {
        clickOn("Gestione account");
        clickOn("Logout");

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/landing.fxml"));
        Parent expectedRoot = loader.load();

        Parent actualRoot = loader.getRoot();;

        assertEquals(expectedRoot, actualRoot, "La pagina landing.fxml dovrebbe essere visualizzata.");
    }

    @Test
    public void testSaldoAggiornato() {
        double saldoCorrente = Double.parseDouble(saldo.getText().replace("$ ", ""));
        assertEquals(100.0, saldoCorrente, 0.01, "Il saldo visualizzato dovrebbe essere 100.0.");
    }

    private Parent getCurrentRoot() {
        return tabellaTransazioni.getScene().getRoot();
    }

    private Utente createTestUser() {
        return new Utente("testuser", "password", "testemail", 100.0);
    }
}