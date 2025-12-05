package it.univaq.cdvd.controller;

import it.univaq.cdvd.dao.UtenteDAO;
import it.univaq.cdvd.model.Utente;
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

import java.io.IOException;


public class LoginController {

    @FXML
    public Button annulButton;
    @FXML
    public Button loginButton;
    @FXML
    public TextField usernameTextField;
    @FXML
    public PasswordField passwordPasswordField;
    @FXML
    public Label loginMessageLabel;

    SessionManager session = SessionManager.getInstance();

    public LoginController() {
       annulButton = new Button();
       loginButton = new Button();
       usernameTextField = new TextField();
       passwordPasswordField = new PasswordField();
    }

    private final UtenteDAO loginDao = new UtenteDAO();

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

    public void loginButtonOnAction(ActionEvent event) {
        String username = usernameTextField.getText();
        String password = passwordPasswordField.getText();

        if (username.isEmpty() || password.isEmpty()) {
            loginMessageLabel.setText("Inserisci username e password.");
            return;
        }

        // Esegui la query e controlla il risultato
        Utente user = loginDao.findUserByUsernameAndPassword(username, password);

        if (user != null) {
            session.setUtente(user);
            System.out.println("Utente loggato: " + session.getUtente());

            loginMessageLabel.setText("Benvenuto " + user.getUsername() + "!");
            try {
                // Carica il file home.fxml
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/home.fxml"));
                Parent root = loader.load();

                // Ottieni il controller della home
               // HomeController homeController = loader.getController();

                // Imposta i dati iniziali della home
                //homeController.showSaldo(); // Aggiorna il saldo dell'utente

                // Ottieni la finestra corrente e imposta la nuova scena
                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                stage.setScene(new Scene(root));
                stage.setTitle("Pagina Main");
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }

        } else {
            loginMessageLabel.setText("Accesso negato. Username o password errati.");
        }
    }
}


