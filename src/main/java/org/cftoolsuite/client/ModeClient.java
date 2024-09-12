package org.cftoolsuite.client;

import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;


@Component
public class ModeClient {

    private RestClient client;

    public ModeClient(@Value("#{systemProperties['robertUrl'] ?: 'http://localhost:8080'}") String robertUrl) {
        client = RestClient.builder()
            .baseUrl(robertUrl)
            .build();
    }

    public ResponseEntity<String> getMode() {
        ResponseEntity<Map<String, Object>> response =
            client
                .get()
                .uri("/actuator/info")
                .retrieve()
                .toEntity(new ParameterizedTypeReference<Map<String, Object>>() {});

        Map<String, Object> body = response.getBody();
        if (body != null && body.containsKey("mode")) {
            Object modeValue = body.get("mode");
            String modeString = String.valueOf(modeValue);
            return ResponseEntity.ok(modeString);
        }

        return ResponseEntity.notFound().build();
    }
}
