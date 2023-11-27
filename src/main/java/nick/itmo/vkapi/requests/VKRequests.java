package nick.itmo.vkapi.requests;

import nick.itmo.vkapi.data.Data;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.HttpClients;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class VKRequests {

    /**
     *Отправляет API запрос на создание поста в группу. Если все ок, то вернет true
     *  */
    public static boolean WallPost(String textToPost) {
        HttpClient httpClient = HttpClients.createDefault();
        try {
            String apiUrl = "https://api.vk.com/method/wall.post";
            URIBuilder builder = new URIBuilder(apiUrl);
            builder.setParameter("owner_id", "-" + Data.GROUP_ID);
            builder.setParameter("message", textToPost);
            builder.setParameter("access_token", Data.TOKEN);
            builder.setParameter("v", "5.131");

            URI uri = builder.build();
            HttpGet request = new HttpGet(uri);
            HttpResponse response = httpClient.execute(request);

            if (response.getStatusLine().getStatusCode() == 200) {
                return true;
            }
        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
        }
        return false;
    }
}
