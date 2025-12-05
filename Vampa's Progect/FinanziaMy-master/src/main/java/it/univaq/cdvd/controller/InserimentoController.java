package it.univaq.cdvd.controller;

import it.univaq.cdvd.dao.CategoriaDAO;
import it.univaq.cdvd.dao.TransazioneDAO;
import it.univaq.cdvd.dao.UtenteDAO;
import it.univaq.cdvd.model.Categoria;
import it.univaq.cdvd.model.Transazione;
import it.univaq.cdvd.util.HibernateUtil;
import it.univaq.cdvd.util.SessionManager;
import it.univaq.cdvd.util.ShowAlert;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.media.AudioClip;
import javafx.stage.Stage;
import org.hibernate.Session;

import java.io.IOException;
import java.util.Objects;

public class InserimentoController {

    @FXML public AnchorPane anchorInserimento = new AnchorPane();
    @FXML public Label text = new Label();
    @FXML public Label importo = new Label();
    @FXML public Label labelCausale = new Label();
    @FXML public TextField causale = new TextField();
    @FXML public TextField inserisciImporto = new TextField();
    @FXML public Button inserisci = new Button();
    @FXML public ComboBox<String> categoriaList = new ComboBox<>();
    @FXML public DatePicker data = new DatePicker();
    @FXML public Button annulla = new Button();

    private final TransazioneDAO tdao = new TransazioneDAO();
    private final CategoriaDAO cdao = new CategoriaDAO();
    private final ShowAlert sa = new ShowAlert();
    private final UtenteDAO utenteDAO = new UtenteDAO();

    @FXML
    public void initialize() {
        importo.setAlignment(Pos.CENTER);
        text.setAlignment(Pos.CENTER);
        labelCausale.setAlignment(Pos.CENTER);
        categoriaList.setItems(cdao.listaCategoria(SessionManager.getInstance().getUtente().getUsername()));
    }

    /**
     * Metodo che permette di salvare una transazione nel database, esegue dei controlli sui campi vuoto e sul formato dell'importo.
     * @param event L'evento scatenato dall'interazione con l'interfaccia grafica
     */
    @FXML
    public void inserisciTransazione(ActionEvent event) {
        try {
            if (inserisciImporto.getText().isEmpty() || causale.getText().isEmpty() ||
                    categoriaList.getValue() == null || data.getValue() == null) {
                sa.showAlert("Errore", "Compila tutti i campi obbligatoriamente", Alert.AlertType.ERROR);
                return;
            }
            double importo = Double.parseDouble(inserisciImporto.getText());
            Transazione tx = new Transazione();
            tx.setImporto(importo);
            tx.setCausale(causale.getText());
            tx.setNomeCategoria(categoriaList.getValue());
            tx.setData(data.getValue());
            tx.setCategoria(cdao.cercaCategoria(categoriaList.getValue()));
            tx.setUtente(SessionManager.getInstance().getUtente());
            if (tdao.save(tx)) {
                AudioClip clip = new AudioClip(
                        Objects.requireNonNull(getClass().getResource("/audio/suono_moneta.mp3")).toExternalForm()
                );
                clip.play();
                utenteDAO.updateSaldo(SessionManager.getInstance().getUtente(), importo);
                sa.showAlert("Transazione inserita", "Transazione inserita con successo", Alert.AlertType.INFORMATION);
            } else {
                sa.showAlert("Transazione non inserita", "Transazione non inserita", Alert.AlertType.ERROR);
            }
        } catch (NumberFormatException e) {
            sa.showAlert("Errore", "Inserisci un importo valido (numero)", Alert.AlertType.ERROR);
        } catch (Exception e) {
            sa.showAlert("Errore", "Si è verificato un errore: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    /**
     * Metodo che permette di annullare l'inserimento di una transazione.
     * @param actionEvent
     */
    @FXML
    public void annulla(ActionEvent actionEvent) {
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



