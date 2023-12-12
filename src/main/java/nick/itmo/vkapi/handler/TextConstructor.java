package nick.itmo.vkapi.handler;

import javafx.collections.ObservableList;
import nick.itmo.vkapi.requests.VKRequests;
import nick.itmo.vkapi.user.templates.vars.Variable;

import java.nio.charset.StandardCharsets;

public class TextConstructor {

    public static Boolean textConstruct(String originalText, ObservableList<Variable> selectedVariables) {
        String textToPost = originalText;

        for (Variable variable : selectedVariables) {
            String variableName = variable.getName();
            String variableValue = variable.getValue();

            textToPost = textToPost.replace("__" + variableName + "__", variableValue);
        }
        textToPost = new String(textToPost.getBytes(StandardCharsets.UTF_8), StandardCharsets.UTF_8);
        textToPost = textToPost.trim();  // Удаление символа новой строки в конце строки

        return VKRequests.WallPost(textToPost);
    }


}
