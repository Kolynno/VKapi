package nick.itmo.vkapi.requests;

import nick.itmo.vkapi.data.Data;
import java.awt.*;
import java.net.URI;

public class SignOrLoginRequests {

   public static void openBrowserToGetTokenURL() {
        String url = "https://oauth.vk.com/authorize?client_id=" + Data.APPLICATION_ID +
                "&scope=wall,offline&redirect_uri=https://oauth.vk.com/blank.html&response_type=token";
        try {
            if (Desktop.isDesktopSupported()) {
                Desktop desktop = Desktop.getDesktop();
                if (desktop.isSupported(Desktop.Action.BROWSE)) {
                    desktop.browse(new URI(url));
                } else {
                    System.out.println("Открытие ссылок не поддерживается на данной платформе.");
                }
            } else {
                System.out.println("Desktop не поддерживается на данной платформе.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
