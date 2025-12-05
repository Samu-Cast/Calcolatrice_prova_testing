package it.univaq.cdvd.controller;

import it.univaq.cdvd.dao.CategoriaDAO;
import it.univaq.cdvd.dao.TransazioneDAO;
import it.univaq.cdvd.model.Categoria;
import it.univaq.cdvd.model.Utente;
import it.univaq.cdvd.util.PDFGenerator;
import it.univaq.cdvd.model.Transazione;
import it.univaq.cdvd.util.SessionManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

public class ReportController {

    @FXML
    public ChoiceBox<String> categoryChoiceBox;

    @FXML
    public Button creaButton;

    @FXML
    public Button annullaButton;

    @FXML
    public DatePicker datainizioPicker;

    @FXML
    public DatePicker datafinePicker;

    @FXML
    public Label reportMessageLabel;


    @FXML
    public void initialize() {

        categoryChoiceBox.getItems().add("generale");

        // Recupera le categorie dal database
        CategoriaDAO categoriaDAO = new CategoriaDAO();
        for (Categoria categoria : categoriaDAO.getAllCategorie()) {
            categoryChoiceBox.getItems().add(categoria.getNome());
        }
    }

    @FXML
    public void handleCreatePDF() {
        // Ottieni i valori selezionati dall'utente
        String categoria = categoryChoiceBox.getValue();
        LocalDate dataInizio = datainizioPicker.getValue();
        LocalDate dataFine = datafinePicker.getValue();

        if (categoria == null || dataInizio == null || dataFine == null) {
            reportMessageLabel.setText("Tutti i campi devono essere compilati");
            return;
        }

        if (dataFine.isBefore(dataInizio)) {
            reportMessageLabel.setText("La data finale è prima di quella iniziale");
            return;
        }

        // Chiama il metodo DAO per recuperare le transazioni
        TransazioneDAO dao = new TransazioneDAO();
        Utente utenteCorrente = SessionManager.getInstance().getUtente();
        List<Transazione> transazioni;

        // Controlla se la categoria è "Generale"
        if ("Generale".equalsIgnoreCase(categoria)) {
            transazioni = dao.getTransazioni(null,dataInizio, dataFine, utenteCorrente);
        } else {
            transazioni = dao.getTransazioni(categoria, dataInizio, dataFine, utenteCorrente);
        }

        // Genera il PDF con le transazioni
        PDFGenerator pdfGenerator = new PDFGenerator();
        pdfGenerator.generatePDF(transazioni, categoria, dataInizio, dataFine);

        reportMessageLabel.setText("Report generato con successo");
    }

    @FXML
    public void annullaButtonOnAction(ActionEvent event) {

        try {
            // Carica il file auth.fxml
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/home.fxml"));
            Parent root = loader.load();

            // Ottieni la finestra corrente e imposta la nuova scena
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Pagina Home");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
