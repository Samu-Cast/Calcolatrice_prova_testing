package it.univaq.cdvd.dao;

import it.univaq.cdvd.model.Categoria;
import it.univaq.cdvd.model.Utente;
import it.univaq.cdvd.util.HibernateUtil;
import it.univaq.cdvd.util.SessionManager;
import it.univaq.cdvd.model.Transazione;
import it.univaq.cdvd.model.Utente;
import it.univaq.cdvd.util.HibernateUtil;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.hibernate.resource.transaction.spi.TransactionStatus;

import java.util.ArrayList;
import java.util.List;


public class CategoriaDAO {

    //per inserire le categoria nella combobox posso fare un metodo che mi torna una lista di categorie e prendere il nome, per quanto riguarda l'inserimento basta inserire solo il nome
    //e volendo posso associare a ogni categoria un colore, si potrebbe fare.

    public boolean save(Categoria categoria) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            session.save(categoria);
            tx.commit();
            return true;
        } catch (HibernateException e) {
            if (tx != null) {
            }
            e.printStackTrace();
        }
        return false;
    }


    public ObservableList<String> listaCategoria(String username) {
        ObservableList<String> categorieList = FXCollections.observableArrayList();
        try(Session session = HibernateUtil.getSessionFactory().openSession()){

            Query query = session.createQuery("from Categoria where utente.username = :username");
            query.setParameter("username", username);
            List<Categoria> categorie = query.list();
            for(Categoria c : categorie){
                categorieList.add(c.getNome());
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return categorieList;
    }

    public Categoria cercaCategoria(String nome){
        Categoria categoria = null;
        try(Session session = HibernateUtil.getSessionFactory().openSession()){
            Query<Categoria> query = session.createQuery("from Categoria where nome = :nome");
            query.setParameter("nome", nome);
            categoria = query.uniqueResult();
        }catch(HibernateException e){
            throw new RuntimeException(e);
        }
        return categoria;
    }


    public List<Categoria> getAllCategorie() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Utente utenteCorrente = SessionManager.getInstance().getUtente();
            return session.createQuery("FROM Categoria WHERE utente = :utenteId", Categoria.class)
                    .setParameter("utenteId", utenteCorrente.getUtente())
                    .list();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<Categoria> findAll() {
        List<Categoria> categorie = new ArrayList<>();

        Session session = null;

        try {
            session = HibernateUtil.getSessionFactory().openSession();
            Query query = session.createQuery("from Categoria");
            for (Object o : query.list()) {
                System.out.println(o.toString());
                categorie.add((Categoria) o);
            }

        }catch(Exception e) {
            e.printStackTrace();
        }
        return categorie;
    }

    public boolean eliminaCategoria(String nome, Utente user) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();

            String hql = "DELETE FROM Categoria c WHERE c.nome = :nome AND c.utente = :utente";
            int result = session.createQuery(hql)
                    .setParameter("nome", nome)
                    .setParameter("utente", user)
                    .executeUpdate();

            if (result == 0) {
                // Se non ha eliminato nulla, puoi decidere cosa fare:
                // lanciare eccezione personalizzata, restituire false, etc.
                throw new RuntimeException("Nessuna categoria trovata con nome: " + nome
                        + " e utente: " + user.getUsername());
            }

            // Se arrivi qui, c’è stato almeno 1 record eliminato -> commit
            transaction.commit();
            return true;

        } catch (Exception e) {
            // Rollback SOLO se la transazione è inizializzata e attiva
            if (transaction != null && transaction.getStatus() == TransactionStatus.ACTIVE) {
                transaction.rollback();
            }
            e.printStackTrace();
            return false;
        }
    }



    public ObservableList<Categoria> findByUtente(String username) {
       List<Categoria> categorie = new ArrayList<>();
       ObservableList<Categoria> categorieList = FXCollections.observableArrayList();

       Session session = null;

       try {
           session = HibernateUtil.getSessionFactory().openSession();
           // Modifica della query per utilizzare il nome corretto della proprietà
           Query query = session.createQuery("from Categoria where utente.username = :username");
           query.setParameter("username", username);  // Impostazione del parametro username
           for (Object o : query.list()) {
               System.out.println(o.toString());
               categorie.add((Categoria) o);
           }
           categorieList.addAll(categorie);

       } catch (Exception e) {
           e.printStackTrace();
       } finally {
           if (session != null) {
               session.close(); // Chiudere la sessione Hibernate
           }
       }
       return categorieList;
   }
    public boolean esistonoCategorie() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();

            // Aggiorna il saldo utilizzando lo username
            String hql = "FROM Categoria";
            Query query = session.createQuery(hql);
            if (query.list().isEmpty()) {
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }
}
