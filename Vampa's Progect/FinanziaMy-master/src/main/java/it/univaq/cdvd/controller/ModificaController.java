package it.univaq.cdvd.controller;

import it.univaq.cdvd.dao.CategoriaDAO;
import it.univaq.cdvd.dao.TransazioneDAO;
import it.univaq.cdvd.dao.UtenteDAO;
import it.univaq.cdvd.model.Categoria;
import it.univaq.cdvd.model.Transazione;
import it.univaq.cdvd.model.Utente;
import it.univaq.cdvd.util.HibernateUtil;
import it.univaq.cdvd.util.SessionManager;
import it.univaq.cdvd.util.ShowAlert;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.hibernate.Session;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

public class ModificaController {

    @FXML
    AnchorPane anchorModifica = new AnchorPane();
    @FXML
    public TableView<Transazione> lista;
    @FXML
    public TableColumn<Transazione, String> codiceTr;
    @FXML
    public TableColumn<Transazione, Double> importo;
    @FXML
    public TableColumn<Transazione, String> causale;
    @FXML
    public TableColumn<Transazione, LocalDate> data;
    @FXML
    public TableColumn<Transazione, String> categoria;
    @FXML
    public ComboBox<String> categoriaList = new ComboBox<>();
    @FXML
    public TextField nuovoImporto = new TextField();
    @FXML
    public TextField nuovoCausale = new TextField();
    @FXML
    public DatePicker nuovoData = new DatePicker();
    @FXML
    public Button modifica = new Button();
    @FXML
    public Label modificaTransazionelabel = new Label();

    CategoriaDAO cdao = new CategoriaDAO();
    ShowAlert sa = new ShowAlert();


    @FXML
    public void initialize() {
        modificaTransazionelabel.setAlignment(Pos.CENTER);
        configuraColonne();
        caricaTransazioni();
        categoriaList.setItems(cdao.listaCategoria(SessionManager.getInstance().getUtente().getUsername()));

    }

    /**
     * Configura le colonne della tabella corrente
     */
    private void configuraColonne() {
        // Collega le colonne ai campi del modello Transazione
        codiceTr.setCellValueFactory(cellData -> new SimpleStringProperty(String.valueOf(cellData.getValue().getId())));
        importo.setCellValueFactory(new PropertyValueFactory<>("importo"));
        causale.setCellValueFactory(new PropertyValueFactory<>("causale"));
        data.setCellValueFactory(new PropertyValueFactory<>("data"));
        categoria.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getCategoria().getNome()));
    }

    /**
     * Carica le transazioni legate all'utente corrente nella tabella
     */
    private void caricaTransazioni() {
        try {
            // Recupera le transazioni legate all'utente dal DAO
            TransazioneDAO transazioneDAO = new TransazioneDAO();
            List<Transazione> transazioni = transazioneDAO.findTransactionByUser(SessionManager.getInstance().getUtente());
            ObservableList<Transazione> transazioniUtente = FXCollections.observableArrayList(transazioni);
            lista.setItems(transazioniUtente);
        } catch (Exception e) {
            e.printStackTrace();
            sa.showAlert("Errore", "Errore durante il caricamento delle transazioni: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    /**
     * Metodo che permette di modificare una transazione nel database, dalla lista delle transazioni in tabella e esegue dei controlli sui campi vuoti e sul formato dell'importo.
     */
    @FXML
    public void modificaTransazione() {

        try {
            Transazione transazioneSelezionata = lista.getSelectionModel().getSelectedItem();
            if (transazioneSelezionata == null) {
                sa.showAlert("Errore", "Nessuna transazione selezionata", Alert.AlertType.WARNING);
                return;
            }
            if (nuovoImporto.getText().isEmpty() || nuovoCausale.getText().isEmpty() || nuovoData.getValue() == null) {
                sa.showAlert("Errore", "Tutti i campi devono essere compilati.", Alert.AlertType.WARNING);
                return;
            }

            TransazioneDAO transazioneDAO = new TransazioneDAO();
            UtenteDAO utenteDAO = new UtenteDAO();
            double vecchioImporto = transazioneSelezionata.getImporto();

            transazioneSelezionata.setId(lista.getSelectionModel().getSelectedItem().getId());
            transazioneSelezionata.setUtente(SessionManager.getInstance().getUtente());
            transazioneSelezionata.setImporto(Double.parseDouble(nuovoImporto.getText()));
            transazioneSelezionata.setCausale(nuovoCausale.getText().trim());
            transazioneSelezionata.setData(nuovoData.getValue());
            transazioneSelezionata.setCategoria(lista.getSelectionModel().getSelectedItem().getCategoria());
            double nuovoImporto = transazioneSelezionata.getImporto();

            utenteDAO.updateSaldo(SessionManager.getInstance().getUtente(), vecchioImporto, nuovoImporto);
            transazioneDAO.modifica(transazioneSelezionata);

            sa.showAlert("Successo", "Transazione modificata con successo", Alert.AlertType.INFORMATION);
            caricaTransazioni();
        } catch (NumberFormatException e) {
            sa.showAlert("Errore", "Il campo importo deve contenere un valore numerico: " + e.getMessage(), Alert.AlertType.ERROR);
        } catch (Exception e) {
            sa.showAlert("Errore", "Errore durante la modifica della transazione: " + e.getMessage(), Alert.AlertType.ERROR);
        }

    }

    /**
     * Metodo che permette di annullare la modifica di una transazione
     * @param actionEvent
     */
    @FXML
    public void handleAnnulClick(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/home.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Pagina Home");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}

