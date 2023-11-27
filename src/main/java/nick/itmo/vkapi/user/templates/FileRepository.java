package nick.itmo.vkapi.user.templates;

import nick.itmo.vkapi.data.Data;

import java.io.*;
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

    public static void saveGroupIdAndToken() {
        try (PrintWriter writer = new PrintWriter("src/main/java/nick/itmo/vkapi/user/templates/groupId.txt")) {
            writer.write(Data.GROUP_ID);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try (PrintWriter writer = new PrintWriter("src/main/java/nick/itmo/vkapi/user/templates/token.txt")) {
            writer.write(Data.TOKEN);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String getGroupIdFromFile() {
        String groupIdFilePath = "src/main/java/nick/itmo/vkapi/user/templates/groupId.txt";
        File groupIdFile = new File(groupIdFilePath);
        if (groupIdFile.exists()) {
            try {
                return Files.readString(Paths.get(groupIdFilePath));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return "";
    }

    public static String getTokenFromFile() {
        String tokenFilePath = "src/main/java/nick/itmo/vkapi/user/templates/token.txt";
        File tokenFile = new File(tokenFilePath);
        if (tokenFile.exists()) {
            try {
                return Files.readString(Paths.get(tokenFilePath));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return "";
    }

}
