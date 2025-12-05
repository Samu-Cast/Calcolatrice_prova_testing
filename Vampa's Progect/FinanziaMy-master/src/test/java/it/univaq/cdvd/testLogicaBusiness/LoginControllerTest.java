package it.univaq.cdvd.testLogicaBusiness;

import it.univaq.cdvd.dao.UtenteDAO;
import it.univaq.cdvd.model.Utente;
import it.univaq.cdvd.util.HibernateUtil;
import org.junit.jupiter.api.BeforeAll;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class LoginControllerTest{

    public UtenteDAO utenteDAOTest = new UtenteDAO();
    public UtenteDAO loginDao = new UtenteDAO();
    public Utente utente;

    @BeforeAll
    public void setUp() {
        HibernateUtil.setDbms("/hibernate-test.cfg.xml");

        utente = new Utente();
        utente.setUsername("franvam");
        utente.setEmail("test1@example.com");
        utente.setPassword("x");
        utente.setSaldo(0.0);
        utenteDAOTest.save(utente);

    }

    @Test
    public void testLoginWithValidCredentials() {
        Utente user = loginDao.findUserByUsernameAndPassword("franvam", "x");

        assertEquals("franvam", user.getUsername(), "Il nome utente o password non corrisponde.");
    }
    @Test
    public void testLoginWithUnValidCredentials() {
        Utente user = loginDao.findUserByUsernameAndPassword("Abdul", "Rest");

        assertNull(user, "L'utente non dovrebbe essere trovato con credenziali errate.");
    }
    @Test
    public void testLoginWithEmptyCredentials() {
        Utente user = loginDao.findUserByUsernameAndPassword("", "");

        // Verifica che l'utente non venga trovato
        assertNull(user, "L'utente non dovrebbe essere trovato con nome utente e password vuoti.");
    }

}

