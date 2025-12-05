package it.univaq.cdvd.model;

import it.univaq.cdvd.dao.TransazioneDAO;
import it.univaq.cdvd.util.SessionManager;
import jakarta.persistence.*;
import org.hibernate.query.sqm.function.SelfRenderingSqmOrderedSetAggregateFunction;

import java.util.ArrayList;
import java.util.List;


@Entity
@Table(name = "Utente") // Nome della tabella nel database
public class Utente {

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "password", nullable = false) // Non può essere null
    private String password;

    @Id
    @Column(name = "username", nullable = false, unique = true) // Non può essere null e deve essere univoco
    private String username;

   /** @OneToMany(mappedBy = "categoria", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Categoria> categoria = new ArrayList<>();*/

    @Column(name = "saldo", nullable = false, columnDefinition = "int default 0" ) // Non può essere null e deve essere univoco
    private Double saldo = 0.0;

    @OneToMany(mappedBy = "utente", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Transazione> transazioni = new ArrayList<>();



    public Utente() {
    }

    public Utente(String username, String email, String password, Double saldo) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.saldo = saldo;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }


    public List<Transazione> getTransazioni() {
        TransazioneDAO dao = new TransazioneDAO();
        return dao.findTransactionByUser(SessionManager.getInstance().getUtente());
    }

    public void setTransazioni(List<Transazione> transazioni) {
        this.transazioni = transazioni;
    }
    public void setEmail(String mail) {
        this.email = mail;
    }

    @Override
    public String toString() {
        return "User{" +

                "  username='" + username + '\'' +
                ", password='" + password + '\'' +
                '}';
    }

    public void setSaldo(double v) {
        this.saldo = saldo;
    }

    public double getSaldo() {
        return this.saldo;
    }

    public String getEmail() {
        return this.email;
    }
    public Utente getUtente() {
        return this;
    }

}