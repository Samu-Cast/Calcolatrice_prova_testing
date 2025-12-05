package it.univaq.cdvd.controller;

import it.univaq.cdvd.dao.CategoriaDAO;
import it.univaq.cdvd.model.Categoria;
import it.univaq.cdvd.model.Transazione;
import it.univaq.cdvd.util.SessionManager;
import it.univaq.cdvd.util.ShowAlert;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

public class AggiuntaCategoriaController {

    @FXML
    public Label titolo = new Label();
    @FXML
    public Label nome = new Label();
    @FXML
    public TextField nomeCategoria = new TextField();
    @FXML
    public Label desc = new Label();
    @FXML
    public TextField descCategoria = new TextField();
    @FXML
    public Button aggiungiCategoria = new Button();

    ShowAlert sa = new ShowAlert();
    CategoriaDAO cdao = new CategoriaDAO();

    @FXML
    public void aggiuntaCategoria(ActionEvent event) throws Exception {
        try {

            Categoria c = new Categoria();
            c.setNome(nomeCategoria.getText());
            c.setDescrizione(descCategoria.getText());
            c.setUtente(SessionManager.getInstance().getUtente());

            System.out.println(c);

            if(nomeCategoria.getText().isEmpty()){
                sa.showAlert("Errore", "Compila tutti i campi obbligatoriamente", Alert.AlertType.ERROR);
                return;
            }

            if (cdao.save(c)) {
               sa.showAlert("Categoria inserita", "Categoria inserita con successo", Alert.AlertType.INFORMATION);
                nomeCategoria.clear();
                descCategoria.clear();
            } else {
               sa.showAlert("Categoria non inserita", "Categoria non inserita", Alert.AlertType.ERROR);
            }
        } catch (Exception e) {
           sa.showAlert("Si è verificato un errore", e.getMessage(), Alert.AlertType.ERROR);
        }

    }


}
