package nick.itmo.vkapi.controllers;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.AnchorPane;
import nick.itmo.vkapi.data.Data;
import nick.itmo.vkapi.user.Variable;
import nick.itmo.vkapi.user.templates.TemplatesHandle;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


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

        textAreaPost.textProperty().addListener((observable, oldValue, newValue) ->
            analyzeAndPopulateVars(newValue)
        );

        // Инициализация столбцов таблицы
        colVariableName.setCellValueFactory(cellData -> cellData.getValue().nameProperty());
        colVariableValue.setCellValueFactory(cellData -> cellData.getValue().valueProperty());

        // Установка данных в таблицу
        tableViewVars.setItems(vars);

        colVariableValue.setCellFactory(TextFieldTableCell.forTableColumn());

        colVariableValue.setOnEditCommit(event -> {
            Variable variable = event.getRowValue();
            variable.setValue(event.getNewValue());
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

    /**
     * Очистить перменные, найти новые по шаблону __СЛОВО__ (регистр не имеет значение) и загрузить в таблицу
     * */
    private void analyzeAndPopulateVars(String text) {
        vars.clear();

        String regex = "__([а-яА-Яa-zA-Z0-9]+)__";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(text);

        while (matcher.find()) {
            String variableName = matcher.group(1);
            vars.add(new Variable(variableName, ""));
        }
    }

}
