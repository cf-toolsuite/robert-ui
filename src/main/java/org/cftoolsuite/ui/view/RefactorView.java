package org.cftoolsuite.ui.view;

import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import org.cftoolsuite.client.RefactorClient;
import org.cftoolsuite.domain.GitRequest;
import org.cftoolsuite.domain.GitResponse;
import org.cftoolsuite.domain.LanguageExtensions;
import org.cftoolsuite.ui.MainLayout;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;

@PageTitle("robert-ui » Refactor")
@Route(value = "refactor", layout = MainLayout.class)
public class RefactorView extends BaseView {

    private static final Logger log = LoggerFactory.getLogger(RefactorView.class);

    private final Checkbox pushToRemoteEnabledCheckbox = new Checkbox("Push to Remote?");
    private final Checkbox pullRequestEnabledCheckbox = new Checkbox("Create Pull Request?");
    HorizontalLayout boxes = new HorizontalLayout();

    public RefactorView(RefactorClient refactorClient) {
        super(refactorClient);

        HorizontalLayout gitInfo = new HorizontalLayout();
        HorizontalLayout gitCredentials = new HorizontalLayout();
        gitInfo.add(uriField, commitField);
        gitCredentials.add(usernameField, passwordField);

        boxes.add(pushToRemoteEnabledCheckbox, pullRequestEnabledCheckbox);
        buttons.add(submitButton, clearButton);

        baseField.setHelperText("The name of the target branch for a pull request (default: main)");

        add(
            gitInfo,
            gitCredentials,
            filePathsField,
            allowedExtensionsComboBox,
            boxes,
            baseField,
            buttons
        );
    }

    @Override
    protected void submitRequest() {
        LanguageExtensions selectedLanguage = allowedExtensionsComboBox.getValue();
        String allowedExtensions = selectedLanguage != null ? selectedLanguage.extensions() : "";
        GitRequest request = new GitRequest(
            uriField.getValue(),
            baseField.getValue(),
            usernameField.getValue(),
            passwordField.getValue(),
            commitField.getValue(),
            convertToSet(filePathsField.getValue()),
            convertToSet(allowedExtensions),
            pushToRemoteEnabledCheckbox.getValue(),
            pullRequestEnabledCheckbox.getValue()
        );

        try {
            ResponseEntity<GitResponse> response = refactorClient.refactor(request);
            if (response.getStatusCode().is2xxSuccessful()) {
                GitResponse body = response.getBody();
                if (body != null) {
                    showNotification("Refactor request successful: " + body.toString(), NotificationVariant.LUMO_SUCCESS);
                } else {
                    showNotification("Refactor request successful, but response body is empty", NotificationVariant.LUMO_WARNING);
                }
            } else {
                String errorMessage = "Error submitting refactor request. Status code: " + response.getStatusCode();
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
        baseField.clear();
        usernameField.clear();
        passwordField.clear();
        commitField.clear();
        filePathsField.clear();
        allowedExtensionsComboBox.clear();
        pushToRemoteEnabledCheckbox.clear();
        pullRequestEnabledCheckbox.clear();
    }

    @Override
    protected void autoSizeFields() {
        uriField.setWidth("250px");
        commitField.setWidth("100px");
        usernameField.setWidth("175px");
        passwordField.setWidth("175px");
        filePathsField.setWidth("360px");
        baseField.setWidth("100px");
    }
}