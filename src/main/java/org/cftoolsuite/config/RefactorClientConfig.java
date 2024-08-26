package org.cftoolsuite.config;

import org.cftoolsuite.ui.RefactorClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.support.WebClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

import reactor.netty.http.client.HttpClient;

@Configuration
public class RefactorClientConfig {

    @Bean
    public RefactorClient refactorClient(WebClient.Builder webClientBuilder, @Value("#{systemProperties['robertUrl'] ?: 'http://localhost:8080'}") String robertUrl) {
        WebClient webClient =
            WebClient
                .builder()
                    .clientConnector(new ReactorClientHttpConnector(
                        HttpClient.create().baseUrl(robertUrl))
                    )
                    .build();
        return HttpServiceProxyFactory
                .builderFor(WebClientAdapter.create(webClient))
                .build()
                .createClient(RefactorClient.class);
    }
}

