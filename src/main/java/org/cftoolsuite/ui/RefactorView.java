package org.cftoolsuite.ui;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;

@Route("")
public class RefactorView extends VerticalLayout {

    private final TextField uriField = new TextField("URI");
    private final TextField baseField = new TextField("Base");
    private final TextField usernameField = new TextField("Username");
    private final PasswordField passwordField = new PasswordField("Password");
    private final TextField commitField = new TextField("Commit");
    private final TextField filePathsField = new TextField("File Paths (comma-separated)");
    private final Checkbox pushToRemoteEnabledCheckbox = new Checkbox("Activate Push to Remote?");
    private final Checkbox pullRequestEnabledCheckbox = new Checkbox("Activate Pull Request?");
    private final Button submitButton = new Button("Submit");

    private final RefactorClient refactorClient;

    public RefactorView(RefactorClient refactorClient) {
        this.refactorClient = refactorClient;

        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.CENTER);

        add(
            uriField,
            baseField,
            usernameField,
            passwordField,
            commitField,
            filePathsField,
            pushToRemoteEnabledCheckbox,
            pullRequestEnabledCheckbox,
            submitButton
        );

        submitButton.addClickListener(e -> submitRefactorRequest());
    }

    private void submitRefactorRequest() {
        GitRequest request = new GitRequest(
            uriField.getValue(),
            baseField.getValue(),
            usernameField.getValue(),
            passwordField.getValue(),
            commitField.getValue(),
            convertToSet(filePathsField.getValue()),
            pushToRemoteEnabledCheckbox.getValue(),
            pullRequestEnabledCheckbox.getValue()
        );

        try {
            refactorClient.refactor(request);
            Notification.show("Refactor request submitted successfully", 3000, Notification.Position.TOP_CENTER);
        } catch (Exception ex) {
            Notification.show("Error submitting refactor request: " + ex.getMessage(), 3000, Notification.Position.TOP_CENTER);
        }
    }

    private Set<String> convertToSet(String commaSeparatedString) {
        return Arrays.stream(commaSeparatedString.split(","))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .collect(Collectors.toSet());
    }
}
