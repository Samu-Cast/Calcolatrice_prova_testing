package it.univaq.cdvd.util;

import it.univaq.cdvd.model.Utente;

/**
 * Classe SINGLETON che si occupa di gestire la sessione dell'utente
 */
public class SessionManager {

    private static SessionManager instance;
    private Utente utenteAttivo;

    private SessionManager(){}

    public static SessionManager getInstance(){
        if(instance == null){
            instance = new SessionManager();
        }
        return instance;
    }

    public void setUtente(Utente utente){
        this.utenteAttivo = utente;
    }

    public Utente getUtente(){
        return utenteAttivo;
    }

    public void clearSession(){
        this.utenteAttivo = null;
    }
}