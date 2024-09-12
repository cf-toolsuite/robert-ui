package org.cftoolsuite.ui;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;

import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.StreamResource;

@Route("")
public class RefactorView extends VerticalLayout {

    private static final Logger log = LoggerFactory.getLogger(RefactorView.class);

    private final TextField uriField = new TextField("URI");
    private final TextField baseField = new TextField("Base");
    private final TextField usernameField = new TextField("Username");
    private final PasswordField passwordField = new PasswordField("Password");
    private final TextField commitField = new TextField("Commit");
    private final TextField filePathsField = new TextField("File Paths (comma-separated)");
    private final Select<LanguageExtensions> allowedExtensionsSelect = new Select<>();
    private final Checkbox pushToRemoteEnabledCheckbox = new Checkbox("Activate Push to Remote?");
    private final Checkbox pullRequestEnabledCheckbox = new Checkbox("Activate Pull Request?");
    private final Button submitButton = new Button("Submit");

    private final RefactorClient refactorClient;

    public RefactorView(
        RefactorClient refactorClient) {
        this.refactorClient = refactorClient;

        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.CENTER);

        add(getLogoImage());

        uriField.setRequired(true);
        filePathsField.setRequired(true);

        initializeAllowedExtensionsSelect();

        add(
            uriField,
            baseField,
            usernameField,
            passwordField,
            commitField,
            filePathsField,
            allowedExtensionsSelect,
            pushToRemoteEnabledCheckbox,
            pullRequestEnabledCheckbox,
            submitButton
        );

        submitButton.addClickListener(e -> submitRefactorRequest());
    }


    private void initializeAllowedExtensionsSelect() {
        List<LanguageExtensions> items = refactorClient.languageExtensions().getBody();
        log.info("Fetched language extensions: {}", items);
        allowedExtensionsSelect.setLabel("Allowed Extensions");
        allowedExtensionsSelect.setItems(items);
        allowedExtensionsSelect.setItemLabelGenerator(LanguageExtensions::language);
        allowedExtensionsSelect.setPlaceholder("Select language");
    }

    private void submitRefactorRequest() {
        LanguageExtensions selectedLanguage = allowedExtensionsSelect.getValue();
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

    private void showNotification(String message, NotificationVariant variant) {
        Notification notification = new Notification(message);
        notification.setPosition(Notification.Position.TOP_CENTER);
        notification.setDuration(0);
        notification.addThemeVariants(variant);

        Div content = new Div();
        content.setText(message);
        content.getStyle().set("cursor", "pointer");
        content.addClickListener(event -> notification.close());

        notification.add(content);

        UI.getCurrent().addShortcutListener(
            () -> notification.close(),
            Key.ESCAPE
        );

        notification.open();

        notification.addDetachListener(event ->
            UI.getCurrent().getPage().executeJs(
                "window.Vaadin.Flow.notificationEscListener.remove()"
            )
        );
    }

    private Set<String> convertToSet(String commaSeparatedString) {
        return Arrays.stream(commaSeparatedString.split(","))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .collect(Collectors.toSet());
    }

    private Image getLogoImage() {
        StreamResource imageResource = new StreamResource("robert.png",
            () -> getClass().getResourceAsStream("/static/robert.png"));
        Image logo = new Image(imageResource, "Logo");
        logo.setWidth("200px");
        return logo;
    }
}
