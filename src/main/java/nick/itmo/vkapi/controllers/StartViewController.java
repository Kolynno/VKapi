package nick.itmo.vkapi.controllers;

import javafx.animation.FadeTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.util.Duration;
import nick.itmo.vkapi.VKAPI;
import nick.itmo.vkapi.data.Data;
import nick.itmo.vkapi.requests.SetDataRequests;
import nick.itmo.vkapi.requests.CheckRequests;
import nick.itmo.vkapi.requests.SignOrLoginRequests;
import nick.itmo.vkapi.user.templates.FileRepository;

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
    private RadioButton radioButtonRemember;

    @FXML
    private void buttonGetTokenClick() {
        SignOrLoginRequests.openBrowserToGetTokenURL();
    }


    @FXML
    public void initialize() {
        if (!FileRepository.getTokenFromFile().isBlank()) {
            setUpFieldsFromFile();
        }
    }

    private void setUpFieldsFromFile() {
        fieldGroupLink.setText(FileRepository.getGroupIdFromFile());
        fieldGroupToken.setText(FileRepository.getTokenFromFile());
    }

    /**
     * Проверка, что id и token валидны, тогда регистрация успешна, иначе сообщать */
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
            checkRadioButtonRemember();
        } else {
            setTextToLabel();
        }
    }

    private void checkRadioButtonRemember() {
        if (radioButtonRemember.isSelected() ) {
            FileRepository.saveGroupIdAndToken();
        }
    }

    private void changeViewToMain() {
        try {
            FXMLLoader loader = new FXMLLoader(VKAPI.class.getResource("main-view.fxml"));
            loader.setCharset(StandardCharsets.UTF_8);
            Parent root = loader.load();

            Stage stage = (Stage) fieldGroupLink.getScene().getWindow();
            Scene scene = new Scene(root, 1280, 720);
            stage.setScene(scene);
            stage.setResizable(true);
            stage.setMaximized(true);
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void setTextToLabel() {
        labelTextIncorrectMessage.setText("Ссылка на токен или на группу некорректна, попробуйте заново!");
        FadeTransition fadeOut = new FadeTransition(Duration.seconds(5), labelTextIncorrectMessage);
        fadeOut.setFromValue(1.0);
        fadeOut.setToValue(0.0);
        fadeOut.setOnFinished(event -> labelTextIncorrectMessage.setText(""));
        fadeOut.play();
    }

    public void buttonGetHelp(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(VKAPI.class.getResource("help-view.fxml"));
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
