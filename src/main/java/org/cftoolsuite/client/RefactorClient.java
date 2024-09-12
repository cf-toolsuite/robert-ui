package org.cftoolsuite.client;

import java.util.List;

import org.cftoolsuite.domain.GitRequest;
import org.cftoolsuite.domain.GitResponse;
import org.cftoolsuite.domain.LanguageExtensions;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.PostExchange;

public interface RefactorClient {

    @PostExchange("/ingest")
    ResponseEntity<Void> ingest(@RequestBody GitRequest request);

    @PostExchange("/refactor")
    ResponseEntity<GitResponse> refactor(@RequestBody GitRequest request);

    @GetExchange("/language-extensions")
    ResponseEntity<List<LanguageExtensions>> languageExtensions();

}
