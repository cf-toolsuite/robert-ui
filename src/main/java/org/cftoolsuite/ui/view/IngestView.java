package org.cftoolsuite.ui.view;

import java.util.Set;

import org.cftoolsuite.client.ModeClient;
import org.cftoolsuite.client.RefactorClient;
import org.cftoolsuite.domain.IngestRequest;
import org.cftoolsuite.domain.LanguageExtensions;
import org.cftoolsuite.ui.MainLayout;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@PageTitle("robert-ui » Ingest")
@Route(value = "ingest", layout = MainLayout.class)
public class IngestView extends BaseView {

    private static final Logger log = LoggerFactory.getLogger(IngestView.class);

    private TextField uriField;
    private TextField usernameField;
    private PasswordField passwordField;
    private TextField commitField;
    private ComboBox<LanguageExtensions> allowedExtensionsComboBox;
    private Button submitButton;
    private Button clearButton;
    private HorizontalLayout buttons;

    public IngestView(RefactorClient refactorClient, ModeClient modeClient) {
        super(refactorClient, modeClient);
    }

    @Override
    protected void setupUI() {
        this.uriField = new TextField("URI");
        this.usernameField = new TextField("Username");
        this.passwordField = new PasswordField("Password");
        this.commitField = new TextField("Commit");
        this.allowedExtensionsComboBox = new ComboBox<>();
        this.submitButton = new Button("Submit");
        this.clearButton = new Button("Clear");
        this.buttons = new HorizontalLayout();

        HorizontalLayout gitInfo = new HorizontalLayout();
        HorizontalLayout gitCredentials = new HorizontalLayout();

        uriField.setRequired(true);

        uriField.setHelperText("The URI of a Git repository");
        usernameField.setHelperText("Username");
        passwordField.setHelperText("Password (or Personal Access Token)");
        commitField.setHelperText("The commit hash upon which to execute this request (default: latest commit)");

        gitInfo.add(uriField, commitField);
        gitCredentials.add(usernameField, passwordField);

        buttons.add(submitButton, clearButton);

        initializeAllowedExtensionsComboBox();

        submitButton.addClickListener(event -> submitRequest());
        clearButton.addClickListener(event -> clearAllFields());

        add(
            new H2("Ingest"),
            gitInfo,
            gitCredentials,
            allowedExtensionsComboBox,
            buttons
        );

        autoSizeFields();
    }

    protected void initializeAllowedExtensionsComboBox() {
        Set<LanguageExtensions> items = refactorClient.languageExtensions().getBody();
        allowedExtensionsComboBox.setLabel("Allowed Extensions");
        allowedExtensionsComboBox.setItems(items);
        allowedExtensionsComboBox.setItemLabelGenerator(LanguageExtensions::language);
        allowedExtensionsComboBox.setPlaceholder("Select language");
        allowedExtensionsComboBox.setHelperText("Files in repository must match these common language file extensions. Leave blank to ingest all files.");
        allowedExtensionsComboBox.setClearButtonVisible(true);
        allowedExtensionsComboBox.setWidth("auto");
    }

    @Override
    protected void submitRequest() {
        LanguageExtensions selectedLanguage = allowedExtensionsComboBox.getValue();
        String allowedExtensions = selectedLanguage != null ? selectedLanguage.extensions() : "";
        IngestRequest request =
            new IngestRequest(
                uriField.getValue(),
                usernameField.getValue(),
                passwordField.getValue(),
                commitField.getValue(),
                convertToSet(allowedExtensions)
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
        allowedExtensionsComboBox.clear();
        usernameField.clear();
        passwordField.clear();
    }

    private void autoSizeFields() {
        uriField.setWidth("250px");
        commitField.setWidth("100px");
        usernameField.setWidth("175px");
        passwordField.setWidth("175px");
    }
}