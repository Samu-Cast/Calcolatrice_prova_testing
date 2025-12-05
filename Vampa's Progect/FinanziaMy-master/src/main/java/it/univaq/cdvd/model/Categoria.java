package it.univaq.cdvd.model;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "categoria")
public class Categoria {

    public Categoria() {
    }


    public Categoria(String nome, String descrizione) {
        this.nome = nome;
        this.descrizione = descrizione;
    }

    @Id
    @Column(nullable = false)
    private String nome;

    @PrimaryKeyJoinColumn
    @ManyToOne
    @JoinColumn(name = "fk_utente")
    private Utente utente;

    @Column(nullable = true)
    private String descrizione;

    @OneToMany(mappedBy = "categoria", cascade = CascadeType.ALL)
    private List<Transazione> transazioni = new ArrayList<>();





    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescrizione() {
        return descrizione;
    }

    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }
    public Utente getUtente() {
        return utente;
    }

    public void setUtente(Utente utente) {
        this.utente = utente;
    }

    public List<Transazione> getTransazioni() {
        return transazioni;
    }

    public void setTransazioni(List<Transazione> transazioni) {
        this.transazioni = transazioni;
    }
}