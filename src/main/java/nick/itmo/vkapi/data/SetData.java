package nick.itmo.vkapi.data;

import com.fasterxml.jackson.core.json.JsonReadFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClients;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class SetData {
    public static void setGroupId(String groupIdStr) {
        groupIdStr = groupIdStr.substring(15).trim();
        Data.GROUP_ID = getGroupId(groupIdStr);
    }

    private static int getGroupId(String groupURL) {

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


    public static void setToken(String inputString) {
        String fragment = inputString.split("#")[1];
        try {
            String decodedFragment = URLDecoder.decode(fragment, "UTF-8");
            String accessToken = getAccessToken(decodedFragment);
            Data.TOKEN = accessToken;
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

}
