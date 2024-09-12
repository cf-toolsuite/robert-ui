package org.cftoolsuite.ui.view;

import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import org.cftoolsuite.client.RefactorClient;
import org.cftoolsuite.domain.GitRequest;
import org.cftoolsuite.domain.LanguageExtensions;
import org.cftoolsuite.ui.MainLayout;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;

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
            filePathsField,
            allowedExtensionsComboBox,
            buttons
        );
    }

    @Override
    protected void submitRequest() {
        LanguageExtensions selectedLanguage = allowedExtensionsComboBox.getValue();
        String allowedExtensions = selectedLanguage != null ? selectedLanguage.extensions() : "";
        GitRequest request = new GitRequest(
            uriField.getValue(),
            null,
            usernameField.getValue(),
            passwordField.getValue(),
            null,
            convertToSet(filePathsField.getValue()),
            convertToSet(allowedExtensions),
            false,
            false
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
        usernameField.clear();
        passwordField.clear();
        commitField.clear();
        filePathsField.clear();
        allowedExtensionsComboBox.clear();
    }

    @Override
    protected void autoSizeFields() {
        uriField.setWidth("250px");
        commitField.setWidth("100px");
        usernameField.setWidth("175px");
        passwordField.setWidth("175px");
        filePathsField.setWidth("360px");
    }
}