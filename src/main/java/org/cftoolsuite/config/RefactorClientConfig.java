package org.cftoolsuite.config;

import org.cftoolsuite.client.RefactorClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.support.RestClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

@Configuration
public class RefactorClientConfig {

    @Bean
    public RefactorClient refactorClient(@Value("#{systemProperties['robertUrl'] ?: 'http://localhost:8080'}") String robertUrl) {
        RestClient restClient = RestClient.builder()
            .baseUrl(robertUrl)
            .build();

        return HttpServiceProxyFactory
            .builderFor(RestClientAdapter.create(restClient))
            .build()
            .createClient(RefactorClient.class);
    }

}

