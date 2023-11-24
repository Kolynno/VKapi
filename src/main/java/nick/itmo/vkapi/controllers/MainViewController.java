package nick.itmo.vkapi.controllers;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.AnchorPane;
import nick.itmo.vkapi.data.Data;
import nick.itmo.vkapi.user.TemplatesHandle;


public class MainViewController {

    @FXML
    public AnchorPane anchorPaneMain;

    @FXML
    private TextArea textAreaPost;

    @FXML
    private Label labelNameOfGroup;

    @FXML
    private ListView<String> listViewTemplates;

    @FXML
    private TextArea textAreaTemplatePreview;

    @FXML
    public void initialize() {
        labelNameOfGroup.setText(Data.GROUP_NAME);
        listViewTemplates.setItems(templateNames);

        listViewTemplates.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) ->
                        showTemplatePreview(newValue));

        listViewTemplates.setOnMouseClicked(event -> {
            if (event.getButton().equals(MouseButton.PRIMARY) && event.getClickCount() == 2) {
                // Если это был двойной щелчок левой кнопкой мыши, вызываем метод
                //setTextFromTemplate();
            }
        });

        anchorPaneMain.setOnKeyPressed(event -> {
            if (event.isControlDown() && event.getCode() == KeyCode.S) {
                ButtonSaveTemplateClick();
            }
        });
    }

    public void showTemplatePreview(String newValue) {
        textAreaTemplatePreview.setText(newValue);
    }

    public void ButtonSendPostClick(ActionEvent actionEvent) {
    }

    public void ButtonClearTextClick(ActionEvent actionEvent) {
    }

    public void ButtonClearVarsClick(ActionEvent actionEvent) {
    }

    public void ButtonDeleteTemplateClick(ActionEvent actionEvent) {
    }

    private final ObservableList<String> templateNames = FXCollections.observableArrayList(TemplatesHandle.getTemplatesNames());


    public void ButtonSaveTemplateClick() {
        TemplatesHandle.saveTemplate(textAreaPost.getText(), listViewTemplates);

    }
}
