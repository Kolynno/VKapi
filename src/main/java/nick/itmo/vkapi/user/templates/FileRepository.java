package nick.itmo.vkapi.user.templates;

import java.io.IOException;
import java.io.PrintWriter;

public class FileRepository {

    public static void saveTemplateToFile(String text, int templateId) {
        try (PrintWriter writer = new PrintWriter("src/main/java/nick/itmo/vkapi/user/templates/template"+ templateId +".txt")) {
            writer.println(text);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
