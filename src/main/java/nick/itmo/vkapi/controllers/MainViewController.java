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
import nick.itmo.vkapi.user.templates.TemplatesHandle;


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

    private String textValue;
    private final ObservableList<String> templateNames = FXCollections.observableArrayList(TemplatesHandle.getTemplatesNames());

    @FXML
    public void initialize() {
        labelNameOfGroup.setText(Data.GROUP_NAME);
        TemplatesHandle.updateTemplatesList(listViewTemplates, templateNames);

        listViewTemplates.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) ->
                        showTemplatePreview(newValue));

        listViewTemplates.setOnMouseClicked(event -> {
            if (event.getButton().equals(MouseButton.PRIMARY) && event.getClickCount() == 2) {
                textAreaPost.setText(TemplatesHandle.getFullTextByName(textValue));
            }
        });

        anchorPaneMain.setOnKeyPressed(event -> {
            if (event.isControlDown() && event.getCode() == KeyCode.S) {
                ButtonSaveTemplateClick();
            }
        });
    }

    public void showTemplatePreview(String newValue) {
        textValue = newValue;
        textAreaTemplatePreview.setText(TemplatesHandle.getFullTextByName(newValue));
    }

    public void ButtonSendPostClick(ActionEvent actionEvent) {
    }

    public void ButtonClearTextClick(ActionEvent actionEvent) {
        textAreaPost.setText("");
    }

    public void ButtonClearVarsClick(ActionEvent actionEvent) {
    }

    /**
     * Удаление файла шаблона, удаление шаблона из списка, обновление списка, снятие выделения со списка,
     * очистка и установка текста в поля
     * */
    public void ButtonDeleteTemplateClick(ActionEvent actionEvent) {
        TemplatesHandle.deleteTemplateByName(textValue);
        templateNames.remove(textValue);
        TemplatesHandle.updateTemplatesList(listViewTemplates, templateNames);
        listViewTemplates.getSelectionModel().clearSelection();
        textAreaPost.clear();
        textAreaTemplatePreview.setText("Превью шаблона");
    }

    public void ButtonSaveTemplateClick() {
        if (!textAreaPost.getText().isBlank()) {
            TemplatesHandle.saveTemplate(textAreaPost.getText(), listViewTemplates);
        }
    }
}
