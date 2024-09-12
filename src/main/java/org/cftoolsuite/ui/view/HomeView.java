package org.cftoolsuite.ui.view;

import org.cftoolsuite.client.ModeClient;
import org.cftoolsuite.ui.MainLayout;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;

import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import io.micrometer.common.util.StringUtils;

@Route(value = "", layout = MainLayout.class)
@PageTitle("robert-ui » Home")
public class HomeView extends Div {

    private static Logger log = LoggerFactory.getLogger(HomeView.class);

    private final ModeClient modeClient;

    public HomeView(ModeClient modeClient) {
        this.modeClient = modeClient;
    }

    @Override
    protected void onAttach(AttachEvent attachEvent) {
        super.onAttach(attachEvent);
        log.trace("Navigated to ROOT... now redirecting!");
        try {
            ResponseEntity<String> response = modeClient.getMode();
            if (response.getStatusCode().is2xxSuccessful()) {
                String mode = response.getBody();
                if (StringUtils.isNotBlank(mode) && mode.equalsIgnoreCase("advanced")) {
                    UI.getCurrent().navigate(IngestView.class);
                } else {
                    UI.getCurrent().navigate(RefactorView.class);
                }
            } else {
                log.warn("Could not determine mode.  Redirecting to RefactorView...");
                UI.getCurrent().navigate(RefactorView.class);
            }
        } catch (Exception e) {
            String errorMessage = "An unexpected error occurred: " + e.getMessage();
            log.error("An unexpected error occurred", e);
        }
  }
}