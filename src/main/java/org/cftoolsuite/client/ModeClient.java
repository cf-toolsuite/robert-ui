package org.cftoolsuite.client;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;


@Component
public class ModeClient {

    private static final Logger log = LoggerFactory.getLogger(ModeClient.class);

    private RestClient client;

    public ModeClient(@Value("#{systemProperties['robertUrl'] ?: 'http://localhost:8080'}") String robertUrl) {
        client = RestClient.builder()
            .baseUrl(robertUrl)
            .build();
    }

    public String getMode() {
        String mode = "simple";
        ResponseEntity<Map<String, Object>> response =
            client
                .get()
                .uri("/actuator/info")
                .retrieve()
                .toEntity(new ParameterizedTypeReference<Map<String, Object>>() {});
        if (response.getStatusCode().is2xxSuccessful()) {
            Map<String, Object> body = response.getBody();
            if (body != null && body.containsKey("mode")) {
                Object modeValue = body.get("mode");
                mode = String.valueOf(modeValue);
            } else {
                log.warn("Could not determine mode.  Defaulting to 'simple'.");
            }
        }
        return mode;
    }

    public boolean isAdvancedModeConfigured() {
        return "advanced".equalsIgnoreCase(getMode());
    }
}
