package com.gitter.keycloaklistener;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.jboss.logging.Logger;
import org.keycloak.models.UserModel;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;

public class UserHttpRequest {
    private static final Logger log = Logger.getLogger(UserHttpRequest.class);

    public static void sendUserRequest(UserModel user) throws IOException, InterruptedException {
        HashMap<String, String> body = new HashMap<>(){{
            put("keycloakId", user.getId());
            put("username", user.getUsername());
            put("firstName", user.getFirstName());
            put("lastName", user.getLastName());
            put("email", user.getEmail());
        }};
        var objectMapper = new ObjectMapper();
        String requestBody = objectMapper
                .writeValueAsString(body);

        HttpClient client = HttpClient.newHttpClient();
        String uriStr = String.format("%s/user", System.getenv("SOCIAL_API_URL"));
        log.infof("Sending request to: %s", uriStr);
        HttpRequest request = HttpRequest.newBuilder()
                .header("Content-Type", "application/json")
                .uri(URI.create(uriStr))
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .build();
        
        HttpResponse<String> response = client.send(request,
                HttpResponse.BodyHandlers.ofString());
        
        log.info(response.body());
    }
}
