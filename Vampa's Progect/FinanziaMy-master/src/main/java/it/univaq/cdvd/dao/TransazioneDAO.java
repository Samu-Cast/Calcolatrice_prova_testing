package it.univaq.cdvd.dao;

import it.univaq.cdvd.model.Transazione;
import it.univaq.cdvd.model.Utente;
import it.univaq.cdvd.util.HibernateUtil;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

public class TransazioneDAO {


    /**
     * Metodo che si interfaccia con il database per salvare un entità Transazione all'interno di esso.
     * @param transazione che si vuole salvare
     * @return true o false in base all'esito dell'operazione
     */
    public boolean save(Transazione transazione) {
        Transaction tx = null;
        Session session = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            tx = session.beginTransaction();
            if (transazione.getCategoria() != null && transazione.getCategoria().getNome() == null) {
                session.save(transazione.getCategoria());
            }
            session.save(transazione);
            tx.commit();
            return true;
        } catch (HibernateException e) {
            if (tx != null && tx.isActive()) {
                tx.rollback();
            }
            e.printStackTrace();
        } finally {
            if (session != null) {
                session.close();
            }
        }
        return false;
    }

    /**
     * Metodo che si interfaccia con il database per eliminare una transazione specifica dato l'id
     * @param idTransazione id della transazione da eliminare.
     * @return true o false in base all'esito dell'operazione
     */
    public boolean eliminaTransazione(long idTransazione) {
        Transaction transaction = null;

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            Transazione transazione = session.get(Transazione.class, idTransazione);
            if (transazione != null) {
                session.delete(transazione);
                transaction.commit();
                System.out.println("Transazione eliminata con successo.");
                return true;
            } else {
                System.out.println("Transazione non trovata con ID: " + idTransazione);
                return false;
            }
        } catch (Exception e) {
            if (transaction != null && transaction.getStatus().canRollback()) {
                transaction.rollback();
            }
            System.err.println("Errore durante l'eliminazione della transazione: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Metodo che si interfaccia con il database per la modifica di una transazione.
     * @param transazione da modificare
     * @return true o false in base all'esito dell'operazione.
     */
    public boolean modifica(Transazione transazione) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            session.update(transazione);
            tx.commit();
            return true;
        }catch(Exception e) {
            if (tx != null) {
                tx.rollback();
            }
        }
        return false;
    }

    /**
     * Metodo che trova le transazioni associate ad un utente.
     * @param utente utente loggato in questo momento.
     * @return
     */
    public List<Transazione> findTransactionByUser(Utente utente) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            String hql = "FROM Transazione t WHERE t.utente = :utente";
            Query<Transazione> query = session.createQuery(hql, Transazione.class);
            query.setParameter("utente", utente);
            return query.list();
        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    /**
     * Metodo che recupera le transazioni associate ad un utente in un determinato intervallo di tempo.
     * @param categoria categoria della transazione
     * @param dataInizio data di inizio
     * @param dataFine data di fine
     * @param utenteCorrente utente loggato in questo momento
     */
    public List<Transazione> getTransazioni(String categoria, LocalDate dataInizio, LocalDate dataFine, Utente utenteCorrente) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {

            if (categoria == null) {
                return session.createQuery(
                                "FROM Transazione WHERE data >= :dataInizio AND data <= :dataFine AND utente = :utenteId",
                                Transazione.class
                        )
                        .setParameter("dataInizio", dataInizio)
                        .setParameter("dataFine", dataFine)
                        .setParameter("utenteId", utenteCorrente)
                        .list();
            }
            return session.createQuery(
                            "FROM Transazione WHERE nome_categoria = :categoria AND data >= :dataInizio AND data <= :dataFine AND utente = :utenteId",
                            Transazione.class
                    )
                    .setParameter("categoria", categoria)
                    .setParameter("dataInizio", dataInizio)
                    .setParameter("dataFine", dataFine)
                    .setParameter("utenteId", utenteCorrente)
                    .list();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
