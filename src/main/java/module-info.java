module nick.itmo.vkapi {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.fasterxml.jackson.databind;
    requires org.apache.httpcomponents.httpclient;
    requires org.apache.httpcomponents.httpcore;


    opens nick.itmo.vkapi to javafx.fxml;
    exports nick.itmo.vkapi;
}