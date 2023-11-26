package nick.itmo.vkapi.requests;

import com.fasterxml.jackson.core.json.JsonReadFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import nick.itmo.vkapi.data.Data;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClients;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Locale;

public class VKRequests {

    public static void WallPost() {
        String apiUrl = "https://api.vk.com/method/wall.post?owner_id=-223544817&message=d&access_token="+ Data.TOKEN +"&v=5.131";
        HttpClient httpClient = HttpClients.createDefault();
        try {
            HttpGet request = new HttpGet(apiUrl);
            HttpResponse response = httpClient.execute(request);
            BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), StandardCharsets.UTF_8));
            StringBuilder responseStringBuilder = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                responseStringBuilder.append(line);
            }
            ObjectMapper objectMapper = new ObjectMapper().enable(JsonReadFeature.ALLOW_UNESCAPED_CONTROL_CHARS.mappedFeature());
            objectMapper.setLocale(Locale.forLanguageTag("ru-RU"));
            JsonNode jsonResponse = objectMapper.readTree(responseStringBuilder.toString());
            processApiResponse(jsonResponse);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void processApiResponse(JsonNode jsonResponse) {
        JsonNode responseNode = jsonResponse.get("response");
        if (responseNode != null && responseNode.has("post_id")) {
            JsonNode userNode = responseNode.get("post_id");
            int id = userNode.asInt();

            System.out.println("ID: " + id);
        } else {
            System.out.println("Error: Unable to retrieve post_id from the response.");
        }
    }

}
