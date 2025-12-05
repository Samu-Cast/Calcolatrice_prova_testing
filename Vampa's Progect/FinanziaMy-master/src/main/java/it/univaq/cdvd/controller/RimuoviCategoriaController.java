package it.univaq.cdvd.controller;

import it.univaq.cdvd.dao.CategoriaDAO;
import it.univaq.cdvd.model.Categoria;
import it.univaq.cdvd.util.SessionManager;
import it.univaq.cdvd.util.ShowAlert;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.List;
import java.util.Optional;

public class RimuoviCategoriaController {

    @FXML
    public TableView<Categoria> tabellaCategorie;
    @FXML
    private TableColumn<Categoria, String> nomeCategoriaTabella;
    @FXML
    private TableColumn<Categoria, String> descCategoriaTabella;

    private ObservableList<Categoria> listaCategorie;

    public ShowAlert sa = new ShowAlert();

    @FXML
    public void initialize() {
        try {
            configuraColonne();
            caricaCategoria();
        } catch (Exception e) {
            e.printStackTrace();
            sa.showAlert("Errore", "Errore durante l'inizializzazione: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    /**
     * Configura le colonne della tabella corrente
     */
    private void configuraColonne() {
        nomeCategoriaTabella.setCellValueFactory(new PropertyValueFactory<>("nome"));
        descCategoriaTabella.setCellValueFactory(new PropertyValueFactory<>("descrizione"));
    }

    /**
     * Carica le categorie dell'utente loggato nella tabella
     */
    private void caricaCategoria() {
        try {
            CategoriaDAO categoriaDAO = new CategoriaDAO();
            List<Categoria> categorie = categoriaDAO.findByUtente(SessionManager.getInstance().getUtente().getUsername());
            listaCategorie = FXCollections.observableArrayList(categorie);
            tabellaCategorie.setItems(listaCategorie);
        } catch (Exception e) {
            e.printStackTrace();
            sa.showAlert("Errore", "Errore durante il caricamento delle transazioni: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    /**
     * Metodo che permette di eliminare una categoria selezionata, da una lista di categorie appartenenti all'utente loggato
     * @param event
     */
    @FXML
    public void eliminaCategoriaTabella(ActionEvent event) {
        Categoria categoriaSelezionata = tabellaCategorie.getSelectionModel().getSelectedItem();
        if (categoriaSelezionata == null) {
            sa.showAlert("Errore", "Seleziona una categoria", Alert.AlertType.WARNING);
            return;
        }

        Alert confermaAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confermaAlert.setTitle("Conferma Eliminazione");
        confermaAlert.setHeaderText("Sei sicuro di voler eliminare questa categoria?");
        confermaAlert.setContentText("L'operazione non può essere annullata.");
        Optional<ButtonType> result = confermaAlert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            CategoriaDAO categoriaDAO = new CategoriaDAO();
            boolean eliminata = categoriaDAO.eliminaCategoria(categoriaSelezionata.getNome(), SessionManager.getInstance().getUtente());

            if (eliminata) {
                listaCategorie.remove(categoriaSelezionata);
                sa.showAlert("Successo", "Categoria eliminata", Alert.AlertType.INFORMATION);
            } else {
                sa.showAlert("Errore", "La categoria non può essere eliminata perchè è legata a delle transazioni. Eliminale e riprova.", Alert.AlertType.ERROR);
            }
        } else {
            sa.showAlert("Operazione annullata", "L'eliminazione della categoria è stata annullata.", Alert.AlertType.INFORMATION);
        }
    }
}

