package org.cftoolsuite.ui.view;

import org.cftoolsuite.client.RefactorClient;
import org.cftoolsuite.domain.IngestRequest;
import org.cftoolsuite.ui.MainLayout;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;

import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@PageTitle("robert-ui » Ingest")
@Route(value = "ingest", layout = MainLayout.class)
public class IngestView extends BaseView {

    private static final Logger log = LoggerFactory.getLogger(IngestView.class);

    public IngestView(RefactorClient refactorClient) {
        super(refactorClient);

        HorizontalLayout gitInfo = new HorizontalLayout();
        HorizontalLayout gitCredentials = new HorizontalLayout();
        gitInfo.add(uriField, commitField);
        gitCredentials.add(usernameField, passwordField);

        buttons.add(submitButton, clearButton);

        add(
            gitInfo,
            gitCredentials,
            buttons
        );
    }

    @Override
    protected void submitRequest() {
        IngestRequest request = new IngestRequest(
            uriField.getValue(),
            usernameField.getValue(),
            passwordField.getValue(),
            commitField.getValue()
        );

        try {
            ResponseEntity<Void> response = refactorClient.ingest(request);
            if (response.getStatusCode().is2xxSuccessful()) {
                showNotification("Ingest request successful", NotificationVariant.LUMO_SUCCESS);
            } else {
                String errorMessage = "Error submitting ingest request. Status code: " + response.getStatusCode();
                if (response.getBody() != null) {
                    errorMessage += ". Message: " + response.getBody().toString();
                }
                showNotification(errorMessage, NotificationVariant.LUMO_ERROR);
            }
        } catch (Exception e) {
            String errorMessage = "An unexpected error occurred: " + e.getMessage();
            showNotification(errorMessage, NotificationVariant.LUMO_ERROR);
            log.error("An unexpected error occurred", e);
        }
    }

    @Override
    protected void clearAllFields() {
        uriField.clear();
        commitField.clear();
        usernameField.clear();
        passwordField.clear();
    }

    @Override
    protected void autoSizeFields() {
        uriField.setWidth("250px");
        commitField.setWidth("100px");
        usernameField.setWidth("175px");
        passwordField.setWidth("175px");
    }
}