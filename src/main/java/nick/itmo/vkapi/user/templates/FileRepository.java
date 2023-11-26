package nick.itmo.vkapi.user.templates;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FileRepository {

    public static void saveTemplateToFile(String text, int templateId) {
        try (PrintWriter writer = new PrintWriter("src/main/java/nick/itmo/vkapi/user/templates/template" + templateId + ".txt")) {
            writer.println(templateId + ":" + text);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void saveTemplateId(int id) {
        try (PrintWriter writer = new PrintWriter("src/main/java/nick/itmo/vkapi/user/templates/settings.txt")) {
            writer.println(id);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String getTextById(String id) {
        String filePath = "src/main/java/nick/itmo/vkapi/user/templates/template" + id + ".txt";

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            StringBuilder stringBuilder = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line).append("\n");
            }
            return stringBuilder.toString();
        } catch (IOException e) {
            e.printStackTrace();
            return "Ошибка при чтении файла";
        }
    }

    public static void deleteTextById(String id) {
        String filePath = "src/main/java/nick/itmo/vkapi/user/templates/template" + id + ".txt";
        Path path = Paths.get(filePath);
        try {
            Files.deleteIfExists(path);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
