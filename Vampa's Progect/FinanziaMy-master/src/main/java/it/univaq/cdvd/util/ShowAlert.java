package it.univaq.cdvd.util;

import javafx.scene.control.Alert;

public class ShowAlert {

    private static final Alert alert = new Alert(Alert.AlertType.NONE);

    public static Alert lastAlert;

    /**
     * Mostra un alert in caso di errore o informazione.
     *
     * @param title   titolo dell'alert
     * @param message messaggio da mostrare
     * @param type    tipo enum del messaggio (INFORMATION, ERROR...)
     */
    public void showAlert(String title, String message, Alert.AlertType type) {
        alert.setAlertType(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
        lastAlert = alert;
    }

}

