package it.univaq.cdvd.testLogicaGui;

import it.univaq.cdvd.controller.AggiuntaCategoriaController;
import it.univaq.cdvd.util.HibernateUtil;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.testfx.framework.junit5.ApplicationTest;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class AggiuntaCategoriaControllerTest extends ApplicationTest {

    AggiuntaCategoriaController aggiungicontroller;


    @BeforeAll
    public void setup(){
        HibernateUtil.setDbms("/hibernate-test.cfg.xml");
    }

    @Override
    public void start(Stage stage) throws Exception {
        // Carica l'FXML direttamente
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/aggiuntacategoria.fxml"));
        Parent root = loader.load();
        aggiungicontroller = loader.getController();

        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
    @Test
    public void testInitialize() {
        // Verifica che gli elementi dell'interfaccia siano inizializzati correttamente
        assertNotNull(aggiungicontroller.nome);
        assertNotNull(aggiungicontroller.nomeCategoria);
        assertNotNull(aggiungicontroller.desc);
        assertNotNull(aggiungicontroller.descCategoria);
    }

    @Test
    public void testAggingiCategoriaConDati() {
        // Simula l'inserimento dei dati
        clickOn(aggiungicontroller.nomeCategoria).write("Stipendio");
        clickOn(aggiungicontroller.descCategoria).write("Mese Gennaio2025");
        clickOn(aggiungicontroller.aggiungiCategoria);

        // Verifica che i dati siano stati correttamente acquisiti
        assertEquals("Stipendio", aggiungicontroller.nomeCategoria.getText());
        assertEquals("Mese Gennaio2025", aggiungicontroller.descCategoria.getText());
    }
    @Test
    public void testAggingiCategoriaSoloNome() {
        // Simula l'inserimento dei dati
        clickOn(aggiungicontroller.nomeCategoria).write("Stipendio");
        clickOn(aggiungicontroller.aggiungiCategoria);
        // Verifica che i dati siano stati correttamente acquisiti
        assertEquals("Stipendio", aggiungicontroller.nomeCategoria.getText());
    }
}
