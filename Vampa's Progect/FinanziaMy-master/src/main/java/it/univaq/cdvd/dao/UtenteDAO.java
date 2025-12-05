package it.univaq.cdvd.dao;

import it.univaq.cdvd.model.Transazione;
import it.univaq.cdvd.model.Utente;
import it.univaq.cdvd.util.HibernateUtil;
import it.univaq.cdvd.util.SessionManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.exception.ConstraintViolationException;
import org.hibernate.query.Query;
import org.mindrot.jbcrypt.BCrypt;


public class UtenteDAO {

    /**
     * Metodo che verifica le credenziali dell'utente nel database.
     *
     * @param username      il nome utente fornito.
     * @param plainPassword la password fornita.
     * @return l'oggetto Utente se trovato, altrimenti null.
     */
    public Utente findUserByUsernameAndPassword(String username, String plainPassword) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            // Recupera l'utente con lo username specificato
            String hql = "FROM Utente WHERE username = :username";
            Query<Utente> query = session.createQuery(hql, Utente.class);
            query.setParameter("username", username);
            Utente utente = query.uniqueResult();

            // Verifica se l'utente esiste e se la password corrisponde

            if (utente != null && BCrypt.checkpw(plainPassword, utente.getPassword())) {
                return utente; // Restituisce l'utente se la password corrisponde
            } else {
                return null; // Utente non trovato o password errata
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Metodo per restituire un utente dal DB dato il suo username
     *
     * @param username
     * @return
     */
    public Utente findUserByUsername(String username) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            String hql = "FROM Utente WHERE username = :username";
            Query<Utente> query = session.createQuery(hql, Utente.class);
            query.setParameter("username", username);
            return query.uniqueResult(); // Restituisce l'utente o null se non trovato
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Metodo per inserire un utente nel DB
     *
     * @param username
     * @param email
     * @param password
     * @throws Exception
     */

    public boolean saveUser(String username, String email, String password, Double saldoIniziale) {
        // Verifica se i campi obbligatori sono vuoti
        if (email.trim().isEmpty() || username.trim().isEmpty() || password.trim().isEmpty()) {
            System.err.println("Email, username e password non possono essere vuoti!");
            return false;
        }

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            // Controlla se l'utente esiste già
            Utente utenteEsistente = findUserByUsername(username);
            if (utenteEsistente != null) {
                System.err.println("Username già esistente.");
                return false;
            }

            // Cripta la password usando BCrypt
            String passwordCriptata = BCrypt.hashpw(password, BCrypt.gensalt());

            // Crea un nuovo utente e salva
            Utente nuovoUtente = new Utente(username, email, passwordCriptata, saldoIniziale);
            Transaction transaction = session.beginTransaction();
            session.save(nuovoUtente); // Salva il nuovo utente
            transaction.commit();
            return true; // Salvataggio riuscito

        } catch (ConstraintViolationException e) {
            // Gestione della violazione del vincolo di unicità
            System.err.println("Errore: Username già esistente. " + e.getMessage());
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // per salvare nel db un utente direttamente come ogetto.
    public void save(Utente utente) {
        String username = utente.getUsername();
        String email = utente.getEmail();
        String password = utente.getPassword();
        Double saldo = utente.getSaldo();
        saveUser(username, email, password, saldo);
    }

    /*FONDAMENTALE sincronizzare l'utente ogetto java con l'utente nel DB, in alternativa fai una
     query come in questo caso, cosichhe prendi esplicitamente l'utente con quell'username
     */
    public void updateSaldo(Utente utente, Double transazione) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();

            // Recupera il saldo corrente dal database
            String fetchHql = "SELECT saldo FROM Utente WHERE username = :username";
            Query<Double> fetchQuery = session.createQuery(fetchHql, Double.class);
            fetchQuery.setParameter("username", utente.getUsername());
            Double saldoCorrente = fetchQuery.uniqueResult();

            // Calcola il nuovo saldo
            if (saldoCorrente == null) {
                saldoCorrente = 0.0; // Default nel caso l'utente non abbia un saldo iniziale
            }
            double nuovoSaldo = saldoCorrente + transazione;

            // Aggiorna il saldo nel database
            String updateHql = "UPDATE Utente SET saldo = :nuovoSaldo WHERE username = :username";
            Query updateQuery = session.createQuery(updateHql);
            updateQuery.setParameter("nuovoSaldo", nuovoSaldo);
            updateQuery.setParameter("username", utente.getUsername());

            int rowsAffected = updateQuery.executeUpdate();
            if (rowsAffected > 0) {
                utente.setSaldo(nuovoSaldo); // Aggiorna il saldo nell'oggetto in memoria
            }

            tx.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void updateSaldo(Utente utente, Double vecchioImporto, Double nuovoImporto) {
        double variazioneSaldo = nuovoImporto - vecchioImporto;
        updateSaldo(utente, variazioneSaldo); // Riutilizza il metodo base
    }


    public double findSaldoByUsername(String username) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            String hql = "SELECT u.saldo FROM Utente u WHERE u.username = :username";
            Query<Double> query = session.createQuery(hql, Double.class);
            query.setParameter("username", username);
            return query.uniqueResult(); // Restituisce il saldo o null se non trovato
        } catch (Exception e) {
            e.printStackTrace();
            return 0.0; // Valore di default in caso di errore
        }
    }

    public boolean esistonoUtenti() {

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();

            // Aggiorna il saldo utilizzando lo username
            String hql = "FROM Utente";
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

