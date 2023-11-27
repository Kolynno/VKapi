package nick.itmo.vkapi.controllers;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.AnchorPane;
import javafx.util.Duration;
import nick.itmo.vkapi.data.Data;
import nick.itmo.vkapi.handler.TextConstructor;
import nick.itmo.vkapi.user.templates.vars.Variable;
import nick.itmo.vkapi.user.templates.TemplatesHandle;
import nick.itmo.vkapi.user.templates.vars.VariableHandler;

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
    private TableView<Variable> tableViewVars;

    @FXML
    private TableColumn<Variable, String> colVariableName;

    @FXML
    private TableColumn<Variable, String> colVariableValue;

    @FXML
    private TextArea textAreaTemplatePreview;

    private String textValue;
    private final ObservableList<String> templateNames = FXCollections.observableArrayList(TemplatesHandle.getTemplatesNames());
    private ObservableList<Variable> vars = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        labelNameOfGroup.setText(Data.GROUP_NAME);
        TemplatesHandle.updateTemplatesList(listViewTemplates, templateNames);

        setUpListViewTemplates();
        setUpKeyBindings();
        setUpVars();
    }

    /**
     * Установить слушатель на текст поста, реализовать таблицу переменных и обновлять ее
     * */
    private void setUpVars() {
        textAreaPost.textProperty().addListener((observable, oldValue, newValue) ->
                VariableHandler.analyzeAndPopulateVars(newValue, vars)
        );
        colVariableName.setCellValueFactory(cellData -> cellData.getValue().nameProperty());
        colVariableValue.setCellValueFactory(cellData -> cellData.getValue().valueProperty());
        tableViewVars.setItems(vars);
        colVariableValue.setCellFactory(TextFieldTableCell.forTableColumn());
        colVariableValue.setOnEditCommit(event -> {
            Variable variable = event.getRowValue();
            variable.setValue(event.getNewValue());
        });
    }

    private void setUpKeyBindings() {
        anchorPaneMain.setOnKeyPressed(event -> {
            if (event.isControlDown() && event.getCode() == KeyCode.S) {
                ButtonSaveTemplateClick();
            }
        });
    }

    private void setUpListViewTemplates() {
        listViewTemplates.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) ->
                        showTemplatePreview(newValue));

        listViewTemplates.setOnMouseClicked(event -> {
            if (event.getButton().equals(MouseButton.PRIMARY) && event.getClickCount() == 2) {
                textAreaPost.setText(TemplatesHandle.getFullTextByName(textValue));
            }
        });
    }

    public void showTemplatePreview(String newValue) {
        textValue = newValue;
        textAreaTemplatePreview.setText(TemplatesHandle.getFullTextByName(newValue));
    }

    public void ButtonSendPostClick(ActionEvent actionEvent) {
        String postText = textAreaPost.getText();
        ObservableList<Variable> selectedVariables = tableViewVars.getSelectionModel().getTableView().getItems();
        if (TextConstructor.textConstruct(postText, selectedVariables)) {
            textAreaPost.setText("Успешно отправлено!");
        } else {
            textAreaPost.setText("Пост НЕ отправлен, ошибка!");
        }
        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(3), event -> textAreaPost.clear()));
        timeline.play();
    }

    public void ButtonClearTextClick(ActionEvent actionEvent) {
        textAreaPost.setText("");
    }

    public void ButtonClearVarsClick(ActionEvent actionEvent) {
        for (Variable variable : vars) {
            variable.setValue("");
        }
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
