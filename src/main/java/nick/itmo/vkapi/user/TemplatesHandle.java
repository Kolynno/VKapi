package nick.itmo.vkapi.user;

import javafx.scene.control.ListView;
import nick.itmo.vkapi.user.templates.FileRepository;

public class TemplatesHandle {

    private static int templateId = 1;

    public static void saveTemplate(String text, ListView<String> listViewTemplates) {
        FileRepository.saveTemplateToFile(text, templateId);
        saveTemplateToList(text, listViewTemplates);
        templateId++;
    }

    private static void saveTemplateToList(String text, ListView<String> listViewTemplates) {
        String templateName;
        if (text.length() > 60) {
            templateName = text.substring(0,60);
        } else {
            templateName = text;
        }
        listViewTemplates.getItems().add(templateName);
    }


}
