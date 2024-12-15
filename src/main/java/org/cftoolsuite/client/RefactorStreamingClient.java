package org.cftoolsuite.client;

import org.cftoolsuite.domain.chat.Inquiry;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import reactivefeign.spring.config.ReactiveFeignClient;
import reactor.core.publisher.Flux;

@ReactiveFeignClient(name="refactor-streaming-service", url="${refactor.service.url}")
public interface RefactorStreamingClient {

    @PostMapping("/api/stream/chat")
    public Flux<String> streamResponseToQuestion(@RequestBody Inquiry inquiry);
}

