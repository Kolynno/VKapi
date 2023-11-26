package nick.itmo.vkapi.user.templates.vars;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Variable {

    private final StringProperty name;
    private final StringProperty value;

    public Variable(String name, String value) {
        this.name = new SimpleStringProperty(name);
        this.value = new SimpleStringProperty(value);
    }

    public StringProperty nameProperty() {
        return name;
    }

    public void setValue(String value) {
        this.value.set(value);
    }

    public StringProperty valueProperty() {
        return value;
    }
}
