package it.univaq.cdvd.controller;

import it.univaq.cdvd.dao.UtenteDAO;
import it.univaq.cdvd.util.ShowAlert;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;

public class LandingController {

    @FXML public Button loginInit = new Button();
    @FXML public Button signupInit = new Button();
    @FXML public Label infoTransazioni = new Label();
    @FXML public Label infoCategoria = new Label();
    @FXML public Label infoReport = new Label();
    @FXML public ImageView imgTransazioni = new ImageView();
    @FXML public ImageView imgCategorie = new ImageView();
    @FXML public ImageView imgReport = new ImageView();

    UtenteDAO udao = new UtenteDAO();
    ShowAlert sa = new ShowAlert();


    @FXML
    public void landingLoginButtonOnAction(ActionEvent event) {

        if (udao.esistonoUtenti()) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/login.fxml"));
                Parent root = loader.load();
                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                stage.setScene(new Scene(root));
                stage.setTitle("Login");
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else{
            sa.showAlert("Errore", "Non ci sono utenti registrati. Registrane almeno uno", Alert.AlertType.ERROR);
        }

    }

    @FXML
    public void landingSignUpButtonOnAction(ActionEvent event) {

        try {
            // Carica il file auth.fxml
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/registrazione.fxml"));
            Parent root = loader.load();

            // Ottieni la finestra corrente e imposta la nuova scena
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Registrazione");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    @FXML
    private void handleImageClickTransazioni(MouseEvent event) {
        // Se l'immagine è visibile, nascondila e mostra la scritta
        if (imgTransazioni.isVisible()) {
            imgTransazioni.setVisible(false);  // Nascondi l'immagine
            infoTransazioni.setVisible(true);  // Mostra la scritta
        } else {
            imgTransazioni.setVisible(true);  // Mostra di nuovo l'immagine
            infoTransazioni.setVisible(false);  // Nascondi la scritta
        }
    }

    @FXML
    private void handleLabelClickTransazioni(MouseEvent event) {
        if (infoTransazioni.isVisible()) {
            infoTransazioni.setVisible(false);
            imgTransazioni.setVisible(true);
        } else {
            infoTransazioni.setVisible(true);
            imgTransazioni.setVisible(false);
        }
    }

    @FXML
    private void handleImageClickCategorie(MouseEvent event) {
        // Se l'immagine è visibile, nascondila e mostra la scritta
        if (imgCategorie.isVisible()) {
            imgCategorie.setVisible(false);  // Nascondi l'immagine
            infoCategoria.setVisible(true);  // Mostra la scritta
        } else {
            imgCategorie.setVisible(true);  // Mostra di nuovo l'immagine
            infoCategoria.setVisible(false);  // Nascondi la scritta
        }
    }

    @FXML
    private void handleLabelClickCategorie(MouseEvent event) {
        if (infoCategoria.isVisible()) {
            infoCategoria.setVisible(false);
            imgCategorie.setVisible(true);
        } else {
            infoCategoria.setVisible(true);
            imgCategorie.setVisible(false);
        }
    }

    @FXML
    private void handleImageClickReport(MouseEvent event) {
        // Se l'immagine è visibile, nascondila e mostra la scritta
        if (imgReport.isVisible()) {
            imgReport.setVisible(false);  // Nascondi l'immagine
            infoReport.setVisible(true);  // Mostra la scritta
        } else {
            imgReport.setVisible(true);  // Mostra di nuovo l'immagine
            infoReport.setVisible(false);  // Nascondi la scritta
        }
    }

    @FXML
    private void handleLabelClickReport(MouseEvent event) {
        if (infoReport.isVisible()) {
            infoReport.setVisible(false);
            imgReport.setVisible(true);
        } else {
            infoReport.setVisible(true);
            imgReport.setVisible(false);
        }
    }

}
