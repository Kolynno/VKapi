package nick.itmo.vkapi;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
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
        SetDataRequests.setToken(tokenURL);
        String groupIdStr = fieldGroupLink.getText();
        SetDataRequests.setGroupId(groupIdStr);
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
        if(!Data.IS_CORRECT_TOKEN) {
            labelTextIncorrectMessage.setText("Ссылка на токен некорректна, попробуйте заново!");
        } else if (!Data.IS_CORRECT_GROUP_ID) {
            labelTextIncorrectMessage.setText("Ссылка на группу некорректна, попробуйте заново!");
        } else {
            labelTextIncorrectMessage.setText("Ссылка на группу и токен некорректны, попробуйте, заново!");
        }
    }
}
