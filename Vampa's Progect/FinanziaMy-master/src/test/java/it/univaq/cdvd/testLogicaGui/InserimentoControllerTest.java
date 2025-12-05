package it.univaq.cdvd.testLogicaGui;

import it.univaq.cdvd.controller.InserimentoController;
import it.univaq.cdvd.dao.CategoriaDAO;
import it.univaq.cdvd.dao.TransazioneDAO;
import it.univaq.cdvd.dao.UtenteDAO;
import it.univaq.cdvd.model.Categoria;
import it.univaq.cdvd.model.Transazione;
import it.univaq.cdvd.model.Utente;
import it.univaq.cdvd.util.HibernateUtil;
import it.univaq.cdvd.util.SessionManager;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.h2.tools.RunScript;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.testfx.framework.junit5.ApplicationTest;

import java.io.FileReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class InserimentoControllerTest extends ApplicationTest {

    private InserimentoController controller;

    TransazioneDAO transazioneDAO;
    CategoriaDAO categoriaDAO;
    UtenteDAO utenteDAO;
    Utente user;
    Transazione transazione;
    Categoria categoria;

    @BeforeAll
    public void setup() {

        HibernateUtil.setDbms("/hibernate-test.cfg.xml");
        try (Connection connection = DriverManager.getConnection("jdbc:h2:mem:testdb", "admin", "admin")) {
            // Esegui lo script per eliminare solo i record
            try (Statement statement = connection.createStatement()) {
                statement.execute("SET REFERENTIAL_INTEGRITY FALSE;"); // Disabilita i vincoli per evitare errori
                statement.execute("TRUNCATE TABLE transazione;"); // Svuota la tabella
                statement.execute("TRUNCATE TABLE utente;"); // Ripeti per ogni tabella necessaria
                statement.execute("TRUNCATE TABLE categoria;");
                statement.execute("SET REFERENTIAL_INTEGRITY TRUE;"); // Riabilita i vincoli
            }

            // Esegui uno script SQL esterno se necessario
            RunScript.execute(connection, new FileReader("src/test/resources/finanziamy.sql"));

            System.out.println("Record eliminati con successo!");
        } catch (Exception e) {
            e.printStackTrace();
        }

        transazioneDAO = new TransazioneDAO();
        categoriaDAO = new CategoriaDAO();
        utenteDAO = new UtenteDAO();

        // Crea un utente di test
        user = new Utente("test", "password", "testUser", 1000.0);
        utenteDAO.save(user);

        // Crea una categoria di test
        categoria = new Categoria();
        categoria.setNome("Svago");
        categoria.setDescrizione("Spese per il tempo libero");
        categoriaDAO.save(categoria);


        // Crea una transazione di test
        transazione = new Transazione();
        transazione.setCausale("Spesa iniziale");
        transazione.setImporto(100.0);
        transazione.setNomeCategoria("Svago");
        transazione.setData(LocalDate.now());
        transazione.setUtente(user);
        transazione.setCategoria(categoria);
        transazioneDAO.save(transazione);

        // Imposta l'utente nel SessionManager
        SessionManager.getInstance().setUtente(user);
    }

    @Override
    public void start(Stage stage) throws Exception {
        // Carica l'FXML direttamente
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/inserimento.fxml"));
        Parent root = loader.load();
        controller = loader.getController();

        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @Test
    public void testInitialize() {
        // Verifica che gli elementi dell'interfaccia siano inizializzati correttamente
        assertNotNull(controller.text);
        assertNotNull(controller.importo);
        assertNotNull(controller.causale);
        assertNotNull(controller.inserisciImporto);
        assertNotNull(controller.categoriaList);
        assertNotNull(controller.data);
        assertNotNull(controller.inserisci);

    }

    @Test
    public void testInserisciTransazione() {
        // Simula l'inserimento dei dati
        clickOn(controller.causale).write("Pagamento affitto");
        clickOn(controller.inserisciImporto).write("500.00");
        // Simula la selezione di una data dal DatePicker
        interact(() -> controller.data.setValue(java.time.LocalDate.of(2024, 12, 15)));
        clickOn(controller.inserisci);

        // Verifica che i dati siano stati correttamente acquisiti
        assertEquals("Pagamento affitto", controller.causale.getText());
        assertEquals("500.00", controller.inserisciImporto.getText());
        assertEquals(java.time.LocalDate.of(2024, 12, 15), controller.data.getValue());
    }

    @Test
    public void testInserisciTransazioneCampoVuoto() {
        // Lascio il campo causale vuoto
        clickOn(controller.inserisciImporto).write("500.00");
        interact(() -> controller.data.setValue(java.time.LocalDate.of(2024, 12, 15)));
        clickOn(controller.inserisci);

        // Verifica che venga mostrato un alert di errore
        assertEquals("", controller.causale.getText());

    }

    @Test
    public void testInserisciTransazioneImportoNonValido() {
        // Inserisco un importo non valido
        clickOn(controller.causale).write("Pagamento affitto");
        clickOn(controller.inserisciImporto).write("abc");
        interact(() -> controller.data.setValue(java.time.LocalDate.of(2024, 12, 15)));
        clickOn(controller.inserisci);

        // Verifica che il campo importo sia vuoto dopo il tentativo
        assertEquals("abc", controller.inserisciImporto.getText());
    }

    @Test
    public void testInserisciTransazioneDataNonSelezionata() {
        // Lascio la data vuota
        clickOn(controller.causale).write("Pagamento affitto");
        clickOn(controller.inserisciImporto).write("500.00");
        clickOn(controller.inserisci);

        // Verifica che non venga impostata una data
        Assertions.assertNull(controller.data.getValue());
    }
}
