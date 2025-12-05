package it.univaq.cdvd.testLogicaBusiness;


import it.univaq.cdvd.dao.CategoriaDAO;
import it.univaq.cdvd.dao.UtenteDAO;
import it.univaq.cdvd.model.Categoria;
import it.univaq.cdvd.model.Utente;
import it.univaq.cdvd.util.HibernateUtil;
import it.univaq.cdvd.util.SessionManager;
import javafx.collections.ObservableList;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.junit.jupiter.api.*;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class TestCategoriaDAO {

    public CategoriaDAO categoriaDAO = new CategoriaDAO();

    @BeforeAll
    public void setup() {
        HibernateUtil.setDbms("/hibernate-test.cfg.xml");
        UtenteDAO utenteDAO = new UtenteDAO();
        Utente user = new Utente();
        user.setUsername("Test 1");
        user.setPassword("Test 1");
        user.setEmail("Test 1");
        user.setSaldo(200.0);
        utenteDAO.save(user);
        SessionManager.getInstance().setUtente(user);

        Categoria cat = new Categoria();
        cat.setNome("Test 1");
        cat.setUtente(user);
        cat.setDescrizione("Test 1");
        CategoriaDAO catDAO = new CategoriaDAO();
        catDAO.save(cat);
    }
    
    @Test
    void testSaveCategoria() {
        // Crea una nuova categoria
        Categoria newCategoria = new Categoria();
        newCategoria.setNome("Francesco");
        newCategoria.setDescrizione("Descrizione Francesco");
        newCategoria.setUtente(SessionManager.getInstance().getUtente());

        // Salva la categoria nel database
        boolean salvata = categoriaDAO.save(newCategoria);
        assertTrue(salvata, "La categoria dovrebbe essere salvata correttamente");

        // Recupera tutte le categorie dal database
        List<Categoria> categorieDalDb = categoriaDAO.getAllCategorie();

        // Verifica che la lista non sia vuota
        assertNotNull(categorieDalDb, "La lista di categorie non dovrebbe essere nulla");
        assertFalse(categorieDalDb.isEmpty(), "La lista di categorie non dovrebbe essere vuota");

        // Verifica che la categoria appena salvata sia presente
        Categoria categoriaSalvata = categorieDalDb.stream()
                .filter(cat -> cat.getNome().equals("Francesco"))
                .findFirst()
                .orElse(null);

        assertNotNull(categoriaSalvata, "La categoria 'Francesco' dovrebbe essere presente nel DB");
        assertEquals("Francesco", categoriaSalvata.getNome(), "Il nome della categoria dovrebbe corrispondere");
        assertEquals("Descrizione Francesco", categoriaSalvata.getDescrizione(), "La descrizione della categoria dovrebbe corrispondere");
    }



    @Test
    void testSaveCategoriaConDescrizioneNull() {
        Categoria cat = new Categoria();
        cat.setNome("CategoriaTest");

        boolean salvata = categoriaDAO.save(cat);
        assertTrue(salvata, "La categoria dovrebbe essere salvata correttamente");

        // Controllo che effettivamente ci sia nel DB
        Session session = HibernateUtil.getSessionFactory().openSession();
        Categoria fromDb = session.get(Categoria.class, cat.getNome());
        assertNotNull(fromDb, "La categoria dovrebbe essere presente nel DB");
        assertEquals("CategoriaTest", fromDb.getNome());
        session.close();

    }


    @Test
    void testListaCategoria() {
        // Il metodo listaCategoria() restituisce un ObservableList<String> con i nomi
        ObservableList<String> lista = categoriaDAO.listaCategoria(SessionManager.getInstance().getUtente().getUsername());
        assertNotNull(lista, "La lista non dovrebbe essere nulla");

        assertTrue(lista.contains("Test 1"), "La lista dovrebbe contenere la categoria precedentemente salvata");
    }

    @Test
    void testCercaCategoria() {
        List<Categoria> trovata = categoriaDAO.getAllCategorie();
        assertNotNull(trovata, "Dovrebbe trovare la categoria con nome 'Test 1'");
        assertEquals("Test 1", trovata.get(0).getNome());
    }

    @Test
    void testFindAll() {
        List<Categoria> all = categoriaDAO.findAll();
        assertFalse(all.isEmpty(), "La lista non deve essere vuota");

    }


    @Test
    void testFindByUtente() {

        ObservableList<Categoria> categorieByUser = categoriaDAO.findByUtente("Test 2");
        assertNotNull(categorieByUser, "La lista di categorie per l'utente 'testUser' non deve essere null");
    }


}

