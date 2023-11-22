package nick.itmo.vkapi.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import nick.itmo.vkapi.VKAPI;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class HelpViewController {

    @FXML
    private TextField sceneHelper;


    public void buttonBackClick(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(VKAPI.class.getResource("main-view.fxml"));
            loader.setCharset(StandardCharsets.UTF_8);
            Parent root = loader.load();

            Stage stage = (Stage) sceneHelper.getScene().getWindow();
            Scene scene = new Scene(root, 1280, 720);
            stage.setScene(scene);
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
