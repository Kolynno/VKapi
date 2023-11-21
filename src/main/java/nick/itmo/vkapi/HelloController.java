package nick.itmo.vkapi;

import com.fasterxml.jackson.core.json.JsonReadFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClients;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;


import java.nio.charset.StandardCharsets;
import java.util.Locale;

public class HelloController {

private static final String TOKEN = "";


    @FXML
    public Label welcomeText;

    @FXML
    protected void onHelloButtonClick() {

        HttpClient httpClient = HttpClients.createDefault();
        String apiUrl = "https://api.vk.com/method/wall.post?owner_id=359029697&mute_notifications=1&message=d&access_token="+ TOKEN +"&v=5.131";


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

    private void processApiResponse(JsonNode jsonResponse) {
        JsonNode responseNode = jsonResponse.get("response");
        if (responseNode != null && responseNode.has("post_id")) {
            JsonNode userNode = responseNode.get("post_id");
            int id = userNode.asInt();

            System.out.println("ID: " + id);
            welcomeText.setText(String.valueOf(id));
        } else {
            System.out.println("Error: Unable to retrieve post_id from the response.");
        }
    }

}
