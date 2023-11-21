package nick.itmo.vkapi;

import com.fasterxml.jackson.core.json.JsonReadFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClients;
import java.awt.Desktop;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class StartViewController {

    private int groupId;
    private String token;
    private static final String APP_ID = "51784370";

    @FXML
    private TextField fieldGroupLink;

    @FXML
    private TextField fieldGroupToken;

    @FXML
    private void buttonGetTokenClick() {
        String groupIdStr = fieldGroupLink.getText();
        groupIdStr = groupIdStr.substring(15).trim();
        groupId = getGroupId(groupIdStr);

        String url = "https://oauth.vk.com/authorize?client_id="+ APP_ID +"&scope=wall,offline&redirect_uri=https://oauth.vk.com/blank.html&response_type=token";
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

    private int getGroupId(String groupURL) {

        int id = 0;
        try {
            HttpClient httpClient = HttpClients.createDefault();
            String apiUrl = "https://api.vk.com/method/groups.getById?group_id=" + groupURL + "&access_token=" + token + "&v=5.154";
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

            JsonNode responseNode = jsonResponse.get("response");
            if (responseNode != null && responseNode.has("groups")) {
                JsonNode groupsNode = responseNode.get("groups");

                if (groupsNode.isArray() && !groupsNode.isEmpty()) {
                    JsonNode firstGroupNode = groupsNode.get(0);

                    if (firstGroupNode.has("id")) {
                        id = firstGroupNode.get("id").asInt();
                        System.out.println("ID from link: " + id);
                    }
                }
            } else {
                System.out.println("Error: Unable to retrieve post_id from the response.");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return id;
    }

    @FXML
    private void buttonGetAccessClick() {
        String inputString = fieldGroupToken.getText();
        String fragment = inputString.split("#")[1];
        try {
            String decodedFragment = URLDecoder.decode(fragment, "UTF-8");
            String accessToken = getAccessToken(decodedFragment);
            token = accessToken;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    private static String getAccessToken(String decodedFragment) {
        String[] params = decodedFragment.split("&");
        Map<String, String> paramMap = new HashMap<>();
        for (String param : params) {
            String[] keyValue = param.split("=");
            if (keyValue.length == 2) {
                paramMap.put(keyValue[0], keyValue[1]);
            }
        }
        String accessToken = paramMap.get("access_token");
        return accessToken;
    }
    @FXML
    protected void onHelloButtonClick() {
        String apiUrl = "https://api.vk.com/method/wall.post?owner_id=-223544817&message=d&access_token="+ token +"&v=5.131";
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

    private void processApiResponse(JsonNode jsonResponse) {
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
