
package it.univaq.cdvd.testLogicaBusiness;



import it.univaq.cdvd.dao.UtenteDAO;
import it.univaq.cdvd.model.Utente;
import it.univaq.cdvd.util.HibernateUtil;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;


import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class RegistrazioneControllerTest {

    @BeforeAll
    public void setup() {
        HibernateUtil.setDbms("/hibernate-test.cfg.xml");

        UtenteDAO utenteDAOTest = new UtenteDAO();
        Utente utenteProva = new Utente("prova","x", "x", 0.0);
        utenteDAOTest.save(utenteProva);
    }
    // test per verificare che non si possano inserire campi vuoti nella registrazione
    @Test
    public void testUnvalidRegistration() {
        UtenteDAO utenteDAOTest= new UtenteDAO();
        assertFalse( utenteDAOTest.saveUser("", "", "",0.0));
    }

    // test che verifica che non si possano inserire utenti già esistenti
    @Test
    public void testExistingUser() {
        UtenteDAO utenteDAOTest = new UtenteDAO();
        assertFalse(utenteDAOTest.saveUser("prova", "x", "x", 0.0));

    }
}
