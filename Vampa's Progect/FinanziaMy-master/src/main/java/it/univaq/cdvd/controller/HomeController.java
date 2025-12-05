package it.univaq.cdvd.controller;

import it.univaq.cdvd.dao.CategoriaDAO;
import it.univaq.cdvd.dao.TransazioneDAO;
import it.univaq.cdvd.dao.UtenteDAO;
import it.univaq.cdvd.model.Transazione;
import it.univaq.cdvd.model.Utente;
import it.univaq.cdvd.util.SessionManager;
import it.univaq.cdvd.util.ShowAlert;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableView;
import javafx.scene.layout.Region;
import javafx.stage.Stage;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDate;
import java.util.List;


public class HomeController {

    @FXML
    public MenuBar menu = new MenuBar();
    @FXML
    public Button login = new Button();
    @FXML
    public Label saldo = new Label();
    @FXML
    public TableView<Transazione> tabellaTransazioni = new TableView<>();
    @FXML
    public MenuItem logout = new MenuItem();
    @FXML
    public MenuItem nuovaTransazione = new MenuItem();
    @FXML
    public MenuItem eliminaTransazione = new MenuItem();
    @FXML
    public MenuItem modificaTransazione = new MenuItem();
    @FXML
    public MenuItem aggiungiCategoria = new MenuItem();
    @FXML
    public MenuItem eliminaCategoria = new MenuItem();
    @FXML
    public MenuItem creaReport = new MenuItem();
    @FXML
    public MenuItem repository = new MenuItem();
    @FXML
    private TableColumn<Transazione, String> categoriaColumn;
    @FXML
    private TableColumn<Transazione, Double> importoColumn;
    @FXML
    private TableColumn<Transazione, LocalDate> dataColumn;
    @FXML
    private TableColumn<Transazione, String> causaleColumn;


    SessionManager session = SessionManager.getInstance();
    TransazioneDAO transazioneDAO = new TransazioneDAO();
    UtenteDAO utenteDAO = new UtenteDAO();
    CategoriaDAO cdao = new CategoriaDAO();
    ShowAlert sa = new ShowAlert();

    @FXML
    public void initialize() {
        // Lega i valori alle proprietà della classe Transazione
        categoriaColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty((cellData.getValue().getCategoria().getNome())));

        importoColumn.setCellValueFactory(cellData ->
                new SimpleObjectProperty<>(cellData.getValue().getImporto()));

        dataColumn.setCellValueFactory(cellData ->
                new SimpleObjectProperty<>(cellData.getValue().getData()));

        causaleColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getCausale()));

        render();
        Utente utenteCorrente = SessionManager.getInstance().getUtente();
        double nuovoSaldo = utenteCorrente.getSaldo();
        nuovaTransazione.setOnAction(this::nuovaTransazioneOnAction);
        eliminaTransazione.setOnAction(this::eliminaTransazione);
        modificaTransazione.setOnAction(this::modificaTransazione);
        aggiungiCategoria.setOnAction(this::aggiuntaCategoriaOnAction);
        eliminaCategoria.setOnAction(this::eliminaCategoriaOnAction);
        repository.setOnAction(this::informazioniOnAction);
    }

    @FXML
    public void nuovaTransazioneOnAction(ActionEvent event) {
        if(cdao.esistonoCategorie())
        caricaPagina("/view/inserimento.fxml", "Inserimento Transazione", event);
        else
            sa.showAlert("Errore", "Non ci sono categorie presenti. Aggiungene almeno una", Alert.AlertType.ERROR);
    }

    @FXML
    public void aggiuntaCategoriaOnAction(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/aggiuntacategoria.fxml"));
            Parent root = loader.load();

            Dialog<Void> dialog = new Dialog<>();
            dialog.setTitle("Nuova Categoria");
            dialog.getDialogPane().setContent(root);
            dialog.getDialogPane().setMinHeight(Region.USE_COMPUTED_SIZE);
            dialog.getDialogPane().setMinWidth(Region.USE_COMPUTED_SIZE);
            dialog.getDialogPane().getButtonTypes().add(ButtonType.CLOSE);
            dialog.showAndWait();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    public void eliminaCategoriaOnAction(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/eliminaCategoria.fxml"));
            Parent root = loader.load();

            Dialog<Void> dialog = new Dialog<>();
            dialog.setTitle("Elimina Categoria");
            dialog.getDialogPane().setContent(root);
            dialog.getDialogPane().setMinHeight(Region.USE_COMPUTED_SIZE);
            dialog.getDialogPane().setMinWidth(Region.USE_COMPUTED_SIZE);
            dialog.getDialogPane().getButtonTypes().add(ButtonType.CLOSE);
            dialog.showAndWait();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    public void eliminaTransazione(ActionEvent event) {
        caricaPagina("/view/cancellazione.fxml", "Eliminazione Transazione", event);
    }


    @FXML
    public void handleLogoutClick(ActionEvent event) {
        System.out.println("utente logout" + session.getUtente());
        session.clearSession();
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/landing.fxml"));
            Parent root = loader.load();

            // Ottieni la finestra corrente
            Stage stage = (Stage) tabellaTransazioni.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Login");
            stage.show();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void render() {
        Utente utenteCorrente = SessionManager.getInstance().getUtente();
        double nuovoSaldo = utenteDAO.findSaldoByUsername(utenteCorrente.getUsername());
        saldo.setText("$ " + nuovoSaldo);
        List<Transazione> transazioni = transazioneDAO.findTransactionByUser(utenteCorrente);
        ObservableList<Transazione> transazioniObservable = FXCollections.observableArrayList(transazioni);
        tabellaTransazioni.setItems(transazioniObservable);
    }

    @FXML
    public void reportButtonOnAction(ActionEvent event) {

        try {
            // Carica il file report.fxml
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/report.fxml"));
            Parent root = loader.load();

            // Ottieni lo stage corrente
            Stage stage = (Stage) ((MenuItem) event.getSource()).getParentPopup().getOwnerWindow();

            // Imposta la nuova scena con il contenuto del file FXML
            stage.setScene(new Scene(root));

            // Mostra la scena
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Errore durante il caricamento della pagina report.fxml", e);
        }
    }

    private boolean isUrlOpened = false;

    @FXML
    public void informazioniOnAction(ActionEvent event) {
        if (isUrlOpened) {
            return; // Esce se l'URL è già stato aperto
        }

        try {
 
            String url = "https://github.com/VampaFrancesco/FinanziaMy.git";

            if (Desktop.isDesktopSupported()) {
                Desktop desktop = Desktop.getDesktop();
                URI uri = new URI(url);
                desktop.browse(uri);
                isUrlOpened = true;
            } else {
                System.out.println("Desktop non supportato su questo sistema.");
            }
        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
        }
    }


    @FXML
    public void modificaTransazione(ActionEvent event) {
        caricaPagina("/view/modifica.fxml", "Modifica Transazione", event);
    }


    private void caricaPagina(String percorsoFXML, String titolo, ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(percorsoFXML));
            Parent root = loader.load();

            // Recupera lo stage corrente
            Stage stage;
            if (event.getSource() instanceof MenuItem) {
                // Ottieni lo stage dal MenuItem
                stage = (Stage) saldo.getScene().getWindow(); // Usa un nodo visibile della scena corrente
            } else if (event.getSource() instanceof Control) {
                // Ottieni lo stage da un nodo Control
                stage = (Stage) ((Control) event.getSource()).getScene().getWindow();
            } else {
                throw new IllegalArgumentException("Impossibile determinare lo stage dall'evento");
            }

            // Cambia scena
            stage.setScene(new Scene(root));
            stage.setTitle(titolo);
        } catch (IOException e) {
            e.printStackTrace();
            //throw new RuntimeException("Errore durante il caricamento della pagina " + percorsoFXML, e);
        }
    }
}

