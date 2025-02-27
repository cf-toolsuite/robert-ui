package org.cftoolsuite.client;

import java.util.Map;
import java.util.Set;

import org.cftoolsuite.domain.GitRequest;
import org.cftoolsuite.domain.GitResponse;
import org.cftoolsuite.domain.IngestRequest;
import org.cftoolsuite.domain.LanguageExtensions;
import org.cftoolsuite.domain.chat.Inquiry;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "refactor-service", url = "${refactor.service.url}")
public interface RefactorClient {

    @PostMapping(value = "/ingest")
    ResponseEntity<Void> ingest(@RequestBody IngestRequest request);

    @PostMapping("/chat")
    public ResponseEntity<String> chat(@RequestBody Inquiry inquiry);

    @PostMapping(value = "/refactor")
    ResponseEntity<GitResponse> refactor(@RequestBody GitRequest request);

    @PostMapping(value = "/fetch", produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<String> fetch(@RequestBody GitRequest request);

    @PostMapping("/search")
    public ResponseEntity<GitResponse> search(@RequestBody GitRequest request);

    @GetMapping(value = "/language-extensions", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<Set<LanguageExtensions>> languageExtensions();

}
