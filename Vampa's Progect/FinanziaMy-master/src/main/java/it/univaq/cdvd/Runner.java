package it.univaq.cdvd;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.scene.media.AudioClip;
import java.util.Objects;

public class Runner extends Application {

    public Button print;

    @Override
    public void start(Stage stage) throws Exception {
        try {
            Parent root = FXMLLoader.load((Objects.requireNonNull(getClass().getResource("/view/landing.fxml"))));

            Scene scene = new Scene(root);
            Image icon = new Image(Objects.requireNonNull(getClass().getResource("/img/logoApp.jpg")).toExternalForm());
            stage.getIcons().add(icon);
            stage.setScene(scene);
            stage.setResizable(false);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static void main(String[] args) {
        launch(args);
    }
}
