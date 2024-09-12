package org.cftoolsuite.ui;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.PostExchange;

public interface RefactorClient {

    @PostExchange("/refactor")
    ResponseEntity<GitResponse> refactor(@RequestBody GitRequest request);

    @GetExchange("/language-extensions")
    ResponseEntity<List<LanguageExtensions>> languageExtensions();
}
