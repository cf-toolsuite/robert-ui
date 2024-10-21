package org.cftoolsuite.ui.view;

import java.util.Set;

import org.cftoolsuite.client.ModeClient;
import org.cftoolsuite.client.RefactorClient;
import org.cftoolsuite.domain.GitRequest;
import org.cftoolsuite.domain.GitResponse;
import org.cftoolsuite.domain.LanguageExtensions;
import org.cftoolsuite.ui.MainLayout;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@PageTitle("robert-ui » Refactor")
@Route(value = "refactor", layout = MainLayout.class)
public class RefactorView extends BaseView {

    private static final Logger log = LoggerFactory.getLogger(RefactorView.class);

    private TextField uriField;
    private TextField baseField;
    private TextField usernameField;
    private PasswordField passwordField;
    private TextField commitField;
    private TextField filePathsField;
    private ComboBox<LanguageExtensions> allowedExtensionsComboBox;
    private Checkbox pushToRemoteEnabledCheckbox;
    private Checkbox pullRequestEnabledCheckbox;
    private TextArea discoveryPromptField;
    private TextArea refactorPromptField;
    private Button submitButton;
    private Button clearButton;
    private HorizontalLayout buttons;
    private HorizontalLayout boxes;
    private boolean isAdvancedModeConfigured;

    public RefactorView(RefactorClient refactorClient, ModeClient modeClient) {
        super(refactorClient, modeClient);
    }

    @Override
    protected void setupUI() {
        this.uriField = new TextField("URI");
        this.baseField = new TextField("Base");
        this.usernameField = new TextField("Username");
        this.passwordField = new PasswordField("Password");
        this.commitField = new TextField("Commit");
        this.filePathsField = new TextField("File Paths");
        this.allowedExtensionsComboBox = new ComboBox<>();
        this.pushToRemoteEnabledCheckbox = new Checkbox("Push to Remote?");
        this.pullRequestEnabledCheckbox = new Checkbox("Create Pull Request?");
        this.discoveryPromptField = new TextArea("Discovery Prompt");
        this.refactorPromptField = new TextArea("Refactor Prompt");
        this.submitButton = new Button("Submit");
        this.clearButton = new Button("Clear");
        this.buttons = new HorizontalLayout();
        this.boxes = new HorizontalLayout();
        this.isAdvancedModeConfigured = modeClient.isAdvancedModeConfigured();

        add(new H2("Refactor"));

        HorizontalLayout gitInfo = new HorizontalLayout();
        HorizontalLayout gitCredentials = new HorizontalLayout();

        uriField.setRequired(true);
        filePathsField.setRequired(true);

        uriField.setHelperText("The URI of a Git repository");
        usernameField.setHelperText("Username");
        passwordField.setHelperText("Password (or Personal Access Token)");
        commitField.setHelperText("The commit hash upon which to execute this request (default: latest commit)");
        filePathsField.setHelperText("""
            Comma separated list of file paths relative to the project root.
            Each file path is explicitly considered, directory walking is not performed.
            Alternatively, for Java projects, one can specify a list of dot-separated package names where each package is walked recursively.
            If no file paths are specified, all files in all directories of the repository will be considered.
            """
        );
        baseField.setHelperText("The name of the target branch for a pull request (default: main)");
        if (this.isAdvancedModeConfigured) {
            discoveryPromptField.setHelperText("This prompt should articulate what you want R*bert to discover in the codebase as candidates for refactoring");
            add(discoveryPromptField);
        }
        refactorPromptField.setHelperText("This prompt should articulate what you want R*bert to refactor in the codebase");

        gitInfo.add(uriField, commitField);
        gitCredentials.add(usernameField, passwordField);

        boxes.add(pushToRemoteEnabledCheckbox, pullRequestEnabledCheckbox);
        buttons.add(submitButton, clearButton);

        initializeAllowedExtensionsComboBox();

        buttons.setAlignItems(Alignment.CENTER);
        buttons.setJustifyContentMode(JustifyContentMode.CENTER);
        submitButton.addClickListener(e -> submitRequest());
        clearButton.addClickListener(e -> clearAllFields());

        add(
            refactorPromptField,
            gitInfo,
            gitCredentials
        );

        if (!this.isAdvancedModeConfigured) { add(filePathsField); }

        add(allowedExtensionsComboBox,
            boxes,
            baseField,
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
        allowedExtensionsComboBox.setHelperText("Candidates for refactoring must match common language file extensions");
        allowedExtensionsComboBox.setClearButtonVisible(true);
        allowedExtensionsComboBox.setWidth("auto");
    }

    @Override
    protected void submitRequest() {
        LanguageExtensions selectedLanguage = allowedExtensionsComboBox.getValue();
        String allowedExtensions = selectedLanguage != null ? selectedLanguage.extensions() : "";
        GitRequest request =
            new GitRequest(
                uriField.getValue(),
                baseField.getValue(),
                usernameField.getValue(),
                passwordField.getValue(),
                commitField.getValue(),
                !this.isAdvancedModeConfigured ? convertToSet(filePathsField.getValue()): null,
                convertToSet(allowedExtensions),
                pushToRemoteEnabledCheckbox.getValue(),
                pullRequestEnabledCheckbox.getValue(),
                this.isAdvancedModeConfigured ? discoveryPromptField.getValue(): null,
                refactorPromptField.getValue()
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
        if (this.isAdvancedModeConfigured) { discoveryPromptField.clear(); }
        refactorPromptField.clear();
        uriField.clear();
        baseField.clear();
        usernameField.clear();
        passwordField.clear();
        commitField.clear();
        if (!this.isAdvancedModeConfigured) { filePathsField.clear(); }
        allowedExtensionsComboBox.clear();
        pushToRemoteEnabledCheckbox.clear();
        pullRequestEnabledCheckbox.clear();
    }

    private void autoSizeFields() {
        if (this.isAdvancedModeConfigured) { discoveryPromptField.setWidth("360px"); }
        refactorPromptField.setWidth("360px");
        uriField.setWidth("250px");
        commitField.setWidth("100px");
        usernameField.setWidth("175px");
        passwordField.setWidth("175px");
        if (!this.isAdvancedModeConfigured) { filePathsField.setWidth("360px"); }
        baseField.setWidth("100px");
    }
}