package org.cftoolsuite.ui;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.service.annotation.PostExchange;

@HttpExchange("/refactor")
public interface RefactorClient {

    @PostExchange
    ResponseEntity<GitResponse> refactor(@RequestBody GitRequest request);
}
