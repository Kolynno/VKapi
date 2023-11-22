package nick.itmo.vkapi;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import nick.itmo.vkapi.data.SetData;
import nick.itmo.vkapi.requests.Requests;

public class StartViewController {

    @FXML
    private TextField fieldGroupLink;

    @FXML
    private TextField fieldGroupToken;

    @FXML
    private void buttonGetTokenClick() {
        String groupIdStr = fieldGroupLink.getText();
        SetData.setGroupId(groupIdStr);
        Requests.openBrowserToGetTokenURL();
    }

    @FXML
    private void buttonGetAccessClick() {
        String tokenURL = fieldGroupToken.getText();
        SetData.setToken(tokenURL);
    }
}
