module nick.itmo.vkapi {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.fasterxml.jackson.databind;
    requires org.apache.httpcomponents.httpclient;
    requires org.apache.httpcomponents.httpcore;
    requires java.desktop;


    opens nick.itmo.vkapi to javafx.fxml;
    exports nick.itmo.vkapi;
    exports nick.itmo.vkapi.data;
    opens nick.itmo.vkapi.data to javafx.fxml;
    exports nick.itmo.vkapi.requests;
    opens nick.itmo.vkapi.requests to javafx.fxml;
    exports nick.itmo.vkapi.controllers;
    opens nick.itmo.vkapi.controllers to javafx.fxml;
}