
package com.example.mcstats;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.Map;

public class StatsService {

    private final Map<String, String> cache = new HashMap<>();
    private final HttpClient client = HttpClient.newHttpClient();

    public String resolveName(String uuid) {
        if (cache.containsKey(uuid)) return cache.get(uuid);

        try {
            String clean = uuid.replace("-", "");

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("https://sessionserver.mojang.com/session/minecraft/profile/" + clean))
                    .GET()
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                JsonObject obj = JsonParser.parseString(response.body()).getAsJsonObject();
                String name = obj.get("name").getAsString();
                cache.put(uuid, name);
                return name;
            }
        } catch (Exception ignored) {}

        return uuid;
    }
}
