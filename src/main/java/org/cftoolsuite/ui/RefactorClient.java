package org.cftoolsuite.ui;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.service.annotation.PostExchange;

@HttpExchange("/refactor")
public interface RefactorClient {

    @PostExchange
    void refactor(@RequestBody GitRequest request);
}
