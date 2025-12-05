package it.univaq.cdvd.controller;

import it.univaq.cdvd.dao.UtenteDAO;
import it.univaq.cdvd.model.Utente;
import it.univaq.cdvd.util.HibernateUtil;
import it.univaq.cdvd.util.SessionManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.io.IOException;
import java.math.BigDecimal;


public class RegistrazioneController {


    @FXML
    public Button registratiButton;
    @FXML
    public Button annullaButton;
    @FXML
    public TextField usernameTextField;
    @FXML
    public TextField emailTextField;
    @FXML
    public PasswordField passwordPasswordField;
    @FXML
    public Label registerMessage;
    @FXML
    public TextField saldoTextField;


    private final UtenteDAO utenteDAO = new UtenteDAO();

    SessionManager session = SessionManager.getInstance();

    /**
     * Torna alla pagina di landing se si clicca il button "annulla"
     *
     * @param event
     */
    @FXML
    public void handleAnnullaClick(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/landing.fxml"));
            Parent root = loader.load();

            // Ottieni la finestra corrente e imposta la nuova scena
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Pagina Iniziale");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public boolean handleRegistrazioneClick(ActionEvent event) {
        // Recupera i dati inseriti dall'utente
        String username = usernameTextField.getText();
        String password = passwordPasswordField.getText();
        String email = emailTextField.getText();
        String saldo = saldoTextField.getText();

        // Controlla che i campi non siano vuoti
        if (username.isEmpty() || password.isEmpty() || email.isEmpty()) {
            registerMessage.setText("Username, email o password non validi.");
            return false;
        }

        if (saldo.isEmpty() || !saldo.matches("\\d*(\\.\\d*)?")) {
            registerMessage.setText("Saldo non valido.");
            return false;
        }

        if (email.isEmpty() || !email.matches("^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,}$")) {
            registerMessage.setText("Email non valida.");
            return false;
        }
        if (password.isEmpty() || password.length() < 4) {
            registerMessage.setText("La password deve essere lunga almeno 4 caratteri.");
            return false;
        }
        double saldoIniziale = Double.parseDouble(saldo);

        // Controlla se l'utente esiste già
        Utente utenteEsistente = utenteDAO.findUserByUsername(username);
        if (utenteEsistente != null) {
            registerMessage.setText("Username già esistente.");
            return false;
        }

        // Salva il nuovo utente nel database
        utenteDAO.saveUser(username, email, password, saldoIniziale);
        Utente utente = new Utente(username, email, password, saldoIniziale);

        // Salva l'utente nella sessione
        session.setUtente(utente);
        System.out.println("Utente registrato: " + session.getUtente());
        registerMessage.setText("Benvenuto, " + username + "!");

        // Reindirizza alla home page e aggiorna il saldo
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/home.fxml"));
            Parent root = loader.load();
            // Ottieni la finestra corrente e imposta la nuova scena
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Pagina Main");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return true;
    }

    /*
    @FXML
    public void annulButtonOnAction(ActionEvent event) {

        try {
            // Carica il file auth.fxml
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/landing.fxml"));
            Parent root = loader.load();

            // Ottieni la finestra corrente e imposta la nuova scena
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Pagina Iniziale");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

     */
}


