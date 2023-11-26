package nick.itmo.vkapi.user.templates.vars;

import javafx.collections.ObservableList;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class VariableHandler {

    /**
     * Очистить перменные, найти новые по шаблону __СЛОВО__ (регистр не имеет значение) и загрузить в таблицу
     * */
    public static void analyzeAndPopulateVars(String text, ObservableList<Variable> vars) {
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
