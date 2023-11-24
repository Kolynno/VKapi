package nick.itmo.vkapi.user;

import javafx.scene.control.ListView;
import nick.itmo.vkapi.user.templates.FileRepository;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class TemplatesHandle {

    private static int templateId;

    static {
        templateId = loadTemplateId();
    }

    /**
     * Загружает данные id из файла. Если файл отсутствует, то он создается и возвращается 1 в качестве id.
     * */
    private static int loadTemplateId() {
        try {
            Path filePath = Paths.get("src/main/java/nick/itmo/vkapi/user/templates/settings.txt");
            if (!Files.exists(filePath)) {
                return 1;
            }
            String content = Files.readString(filePath);
            return Integer.parseInt(content.trim());
        } catch (IOException e) {
            e.printStackTrace();
            return 1;
        }
    }

/**
 * Сохраняет шаблон в файл и в лист */
    public static void saveTemplate(String text, ListView<String> listViewTemplates) {
        FileRepository.saveTemplateToFile(text, templateId);
        saveTemplateToList(text, listViewTemplates);
        templateId++;
        FileRepository.saveTemplateId(templateId);
    }

    private static void saveTemplateToList(String text, ListView<String> listViewTemplates) {
        String templateName = text.substring(0, Math.min(60, text.length()));
        listViewTemplates.getItems().add(templateName);
    }

    /**
     * Получаю из файлов шаблонов их первые 60 символов, чтобы отобразить в списке при загрузке */
    public static List<String> getTemplatesNames() {
        String directoryPath = "src/main/java/nick/itmo/vkapi/user/templates/";
        String filePrefix = "template";

        File directory = new File(directoryPath);
        File[] files = directory.listFiles();

        List<String> templateNames = null;
        if (files != null) {
            templateNames = List.of(files)
                    .stream()
                    .filter(file -> file.getName().startsWith(filePrefix))
                    .map(File::getName)
                    .collect(Collectors.toList());
        }

        List<String> templatesPreview = new ArrayList<>();
        if (templateNames != null) {
            for (String templateName : templateNames) {
                try {
                    String filePath = directoryPath + templateName;
                    byte[] bytes = Files.readAllBytes(new File(filePath).toPath());
                    String content = new String(bytes, Charset.forName("Windows-1251")).substring(0, Math.min(60, bytes.length));
                    templatesPreview.add(content);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return templatesPreview;
    }
}
