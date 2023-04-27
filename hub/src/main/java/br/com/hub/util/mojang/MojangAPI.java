package br.com.hub.util.mojang;


import br.com.core.Core;
import br.com.hub.util.mojang.response.MojangApiResponse;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.InputStreamReader;
import java.net.URI;
import java.util.Base64;
import java.util.UUID;

public class MojangAPI {
    public static String getUserNameFromUuid(String uuid) {
        try (CloseableHttpClient client = HttpClients.createDefault()) {
            URI uri = new URIBuilder()
                    .setScheme("https")
                    .setHost("api.ashcon.app")
                    .setPath("/mojang/v2/user/" + uuid)
                    .build();

            HttpGet request = new HttpGet(uri);
            HttpResponse response = client.execute(request);

            if (response.getStatusLine().getStatusCode() == 200) {
                String responseBody = EntityUtils.toString(response.getEntity());
                JsonObject result = Core.GSON.fromJson(responseBody, JsonObject.class);
                return result.get("username").getAsString();
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }

        return null;
    }


    public static String getSkinUrl(String name) {
        try {

            String username = "Kinoques";
            String apiUrl = "https://api.ashcon.app/mojang/v2/user/" + username;

            HttpClient httpClient = HttpClients.createDefault();
            HttpGet httpGet = new HttpGet(apiUrl);
            HttpResponse httpResponse = httpClient.execute(httpGet);

            String json = EntityUtils.toString(httpResponse.getEntity());

            MojangApiResponse mojangApiResponse = Core.GSON.fromJson(json, MojangApiResponse.class);

            return mojangApiResponse.getTextures().getSkin().getUrl();

        } catch (Exception exception) {
            exception.printStackTrace();
        }

        return null;
    }
}
