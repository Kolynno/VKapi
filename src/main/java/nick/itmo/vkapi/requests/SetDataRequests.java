package nick.itmo.vkapi.requests;

import com.fasterxml.jackson.core.json.JsonReadFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import nick.itmo.vkapi.data.Data;
import nick.itmo.vkapi.user.templates.FileRepository;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClients;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class SetDataRequests {


    /**
     * Если уже в файле есть данные, то обновляется только имя группы, если же нет данных в файле, то еще и id группы.
     * К тому же если в строке с ссылкой на группу есть ссылка, то обновить группу
     * */
    public static void setGroupId(String groupIdStr) {

        if (!FileRepository.getTokenFromFile().isBlank() && !groupIdStr.contains("https://vk.com")) {
            Data.GROUP_ID = FileRepository.getGroupIdFromFile();
            Data.IS_CORRECT_GROUP_ID = true;
            getGroupName(Data.GROUP_ID);
            return;
        }

        if (groupIdStr.length() < 15) {
            return;
        }
        groupIdStr = groupIdStr.substring(15).trim();
        Data.GROUP_ID = getGroupIdAndName(groupIdStr);
    }

    private static String getGroupIdAndName(String groupURL) {

        int id = 0;

        try {
            HttpClient httpClient = HttpClients.createDefault();
            String apiUrl = "https://api.vk.com/method/groups.getById?group_id=" + groupURL + "&access_token=" + Data.TOKEN + "&v=5.154";
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
                        Data.IS_CORRECT_GROUP_ID = true;
                    }
                    if(firstGroupNode.has("name")) {
                        Data.GROUP_NAME = firstGroupNode.get("name").asText();
                    }
                }
            } else {
                System.out.println("ID group isn't correct");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return String.valueOf(id);
    }


    private static String getGroupName(String groupURL) {

        int id = 0;

        try {
            HttpClient httpClient = HttpClients.createDefault();
            String apiUrl = "https://api.vk.com/method/groups.getById?group_id=" + groupURL + "&access_token=" + Data.TOKEN + "&v=5.154";
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

                    if(firstGroupNode.has("name")) {
                        Data.GROUP_NAME = firstGroupNode.get("name").asText();
                    }
                }
            } else {
                System.out.println("ID group isn't correct");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return String.valueOf(id);
    }

    public static void setToken(String inputString) {

        if (!FileRepository.getTokenFromFile().isBlank()) {
            Data.TOKEN = FileRepository.getTokenFromFile();
            Data.IS_CORRECT_TOKEN = true;
            return;
        }
        String fragment = inputString.split("#")[1];
        String decodedFragment = URLDecoder.decode(fragment, StandardCharsets.UTF_8);
        String accessToken = getAccessToken(decodedFragment);
        Data.TOKEN = accessToken;
        Data.IS_CORRECT_TOKEN = true;
    }

    /**
     * Разделяет url по частям и находит значение токена */
    private static String getAccessToken(String url) {
        String[] params = url.split("&");
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

}
