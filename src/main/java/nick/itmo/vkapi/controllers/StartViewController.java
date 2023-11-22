package nick.itmo.vkapi.controllers;

import javafx.animation.FadeTransition;
import javafx.animation.PauseTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.util.Duration;
import nick.itmo.vkapi.data.Data;
import nick.itmo.vkapi.requests.SetDataRequests;
import nick.itmo.vkapi.requests.CheckRequests;
import nick.itmo.vkapi.requests.SignOrLoginRequests;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class StartViewController {

    @FXML
    private TextField fieldGroupLink;

    @FXML
    private TextField fieldGroupToken;

    @FXML
    private Label labelTextIncorrectMessage;

    @FXML
    private void buttonGetTokenClick() {
        SignOrLoginRequests.openBrowserToGetTokenURL();
    }

    @FXML
    private void buttonGetAccessClick() {
        String tokenURL = fieldGroupToken.getText();
        if (tokenURL.length() > 0) {
            SetDataRequests.setToken(tokenURL);
        }

        String groupIdStr = fieldGroupLink.getText();
        if (groupIdStr.length() > 0) {
            SetDataRequests.setGroupId(groupIdStr);
        }

        CheckRequests.checkGroupIdAndToken();

        if (Data.IS_CORRECT) {
            changeViewToMain();
        } else {
            setTextToLabel();
            System.out.println("Data isn't correct");
        }
    }

    private void changeViewToMain() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("main-view.fxml"));
            loader.setCharset(StandardCharsets.UTF_8);
            Parent root = loader.load();

            Stage stage = (Stage) fieldGroupLink.getScene().getWindow();
            Scene scene = new Scene(root, 1280, 720);
            stage.setScene(scene);
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void setTextToLabel() {
        labelTextIncorrectMessage.setText("Ссылка на токен или на группу некорректна, попробуйте заново!");
        FadeTransition fadeOut = new FadeTransition(Duration.seconds(3), labelTextIncorrectMessage);
        fadeOut.setFromValue(1.0);
        fadeOut.setToValue(0.0);
        fadeOut.setOnFinished(event -> labelTextIncorrectMessage.setText(""));
        fadeOut.play();
    }

    public void buttonGetHelp(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("help-view.fxml"));
            loader.setCharset(StandardCharsets.UTF_8);
            Parent root = loader.load();

            Stage stage = (Stage) fieldGroupLink.getScene().getWindow();
            Scene scene = new Scene(root, 1280, 720);
            stage.setScene(scene);
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
