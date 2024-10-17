package org.cftoolsuite.ui.view;

import java.util.HashSet;
import java.util.Set;

import org.cftoolsuite.client.ModeClient;
import org.cftoolsuite.client.RefactorClient;
import org.cftoolsuite.domain.GitRequest;
import org.cftoolsuite.domain.GitResponse;
import org.cftoolsuite.domain.LanguageExtensions;
import org.cftoolsuite.ui.MainLayout;
import org.cftoolsuite.ui.component.CodeHighlighter;
import org.cftoolsuite.ui.component.CodeHighlighterFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.ColumnTextAlign;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.provider.ListDataProvider;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@PageTitle("robert-ui » Search")
@Route(value = "search", layout = MainLayout.class)
public class SearchView extends BaseView {

    private static final Logger log = LoggerFactory.getLogger(SearchView.class);

    private TextField query;
    private TextField uriField;
    private TextField usernameField;
    private PasswordField passwordField;
    private TextField commitField;
    private Set<LanguageExtensions> allowedExtensions;
    private ComboBox<LanguageExtensions> allowedExtensionsComboBox;
    private Button submitButton;
    private Button clearButton;
    private HorizontalLayout buttons;
    private Grid<GitRequest> grid;

    public SearchView(RefactorClient refactorClient, ModeClient modeClient) {
        super(refactorClient, modeClient);
    }

    @Override
    protected void setupUI() {
        this.uriField = new TextField("URI");
        this.usernameField = new TextField("Username");
        this.passwordField = new PasswordField("Password");
        this.commitField = new TextField("Commit");
        this.query = new TextField("Query");
        this.allowedExtensionsComboBox = new ComboBox<>();
        this.query.setRequired(true);
        this.query.setHelperText("Ask a question. A similarity search will be conducted to find source code whose contents may contain information of interest.  Only the file paths to matching source will be returned.");
        this.submitButton = new Button("Submit");
        this.clearButton = new Button("Clear");
        this.buttons = new HorizontalLayout();

        HorizontalLayout gitInfo = new HorizontalLayout();
        HorizontalLayout gitCredentials = new HorizontalLayout();

        gitInfo.add(uriField, commitField);
        gitCredentials.add(usernameField, passwordField);

        buttons.add(submitButton, clearButton);

        this.allowedExtensions = refactorClient.languageExtensions().getBody();
        initializeAllowedExtensionsComboBox();

        buttons.setAlignItems(Alignment.CENTER);
        buttons.setJustifyContentMode(JustifyContentMode.CENTER);
        submitButton.addClickListener(event -> submitRequest());
        clearButton.addClickListener(event -> clearAllFields());

        grid = new Grid<>(GitRequest.class, false);

        grid.setPageSize(10);
        grid.setHeight("480px");

        grid.addColumn(gr -> gr.filePaths().iterator().next()).setHeader("File path").setSortable(true).setTextAlign(ColumnTextAlign.START);
        grid.addComponentColumn(gr -> {
            Button showButton = new Button(new Icon(VaadinIcon.CODE));
            showButton.addClickListener(e -> showContent(gr));
            return showButton;
        }).setHeader("Contents").setWidth("100px").setFlexGrow(0).setTextAlign(ColumnTextAlign.CENTER);

        add(
            new H2("Search for source code matching query criteria"),
            query,
            gitInfo,
            gitCredentials,
            allowedExtensionsComboBox,
            buttons,
            grid
        );

        autoSizeFields();
    }

    protected void initializeAllowedExtensionsComboBox() {
        allowedExtensionsComboBox.setLabel("Allowed Extensions");
        allowedExtensionsComboBox.setItems(allowedExtensions);
        allowedExtensionsComboBox.setItemLabelGenerator(LanguageExtensions::language);
        allowedExtensionsComboBox.setPlaceholder("Select language");
        allowedExtensionsComboBox.setHelperText("Search results will be filtered to a choice from these common language file extensions. Leave blank to consider all source.");
        allowedExtensionsComboBox.setClearButtonVisible(true);
        allowedExtensionsComboBox.setWidth("auto");
    }

    protected void submitRequest() {
        LanguageExtensions selectedLanguage = allowedExtensionsComboBox.getValue();
        String allowedExtensions = selectedLanguage != null ? selectedLanguage.extensions() : "";
        GitRequest request =
            new GitRequest(
                uriField.getValue(),
                null,
                usernameField.getValue(),
                passwordField.getValue(),
                commitField.getValue(),
                null,
                convertToSet(allowedExtensions),
                false,
                false,
                query.getValue(),
                null
                );
        try {
            ResponseEntity<GitResponse> response = refactorClient.search(request);
            if (response.getStatusCode().is2xxSuccessful()) {
                GitResponse searchResult = response.getBody();
                if (searchResult != null && !searchResult.impactedFileSet().isEmpty()) {
                    Set<GitRequest> items = new HashSet<>();
                    for (String filePath: searchResult.impactedFileSet()) {
                        items.add(
                            new GitRequest(
                                uriField.getValue(),
                                null,
                                usernameField.getValue(),
                                passwordField.getValue(),
                                commitField.getValue(),
                                Set.of(filePath),
                                convertToSet(allowedExtensions),
                                false,
                                false,
                                query.getValue(),
                                null
                            )
                        );
                    }
                    populateGrid(items);
                    showNotification("Source matching query criteria retrieved successfully", NotificationVariant.LUMO_SUCCESS);
                } else {
                    populateGrid(Set.of());
                    showNotification("No source found matching your query", NotificationVariant.LUMO_CONTRAST);
                }
            } else {
                String errorMessage = "Error submitting search request. Status code: " + response.getStatusCode();
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

    protected void showContent(GitRequest request) {
        try {
            ResponseEntity<String> response = refactorClient.fetch(request);
            String fqPath = request.filePaths().iterator().next();
            String fileName = fqPath.substring(fqPath.lastIndexOf('/') + 1);
            String filePath = fqPath.substring(0, fqPath.lastIndexOf('/'));
            String fileExtension = fileName.substring(fileName.lastIndexOf('.') + 1);
            String language = allowedExtensions.stream()
                .filter(ext -> ext.extensions().contains(fileExtension))
                .map(le -> le.language().toLowerCase())
                .findFirst()
                .orElse("txt");
            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                Dialog summaryDialog = new Dialog();
                summaryDialog.setWidth("800px");

                H3 title = new H3(fileName);
                Paragraph subtitle = new Paragraph("in: " + filePath);

                Div contentWrapper = new Div();
                contentWrapper.setWidthFull();
                contentWrapper.getStyle()
                    .set("max-height", "600px")
                    .set("overflow-y", "auto");

                if (language.equals("txt")) {
                    Paragraph text = new Paragraph(response.getBody());
                    contentWrapper.add(text);
                } else {
                    CodeHighlighter highlighter = CodeHighlighterFactory.createHighlighter(response.getBody(), language);
                    contentWrapper.add(highlighter);
                }

                Button closeButton = new Button("Close", e -> summaryDialog.close());
                closeButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

                HorizontalLayout buttonLayout = new HorizontalLayout(closeButton);
                buttonLayout.setJustifyContentMode(JustifyContentMode.END);
                buttonLayout.setWidthFull();

                VerticalLayout layout = new VerticalLayout(title, subtitle, contentWrapper, buttonLayout);
                summaryDialog.add(layout);
                summaryDialog.open();
            } else {
                showNotification("Error fetching contents", NotificationVariant.LUMO_ERROR);
            }
        } catch (Exception e) {
            log.error("Error fetching contents", e);
            showNotification("Error fetching contents: " + e.getMessage(), NotificationVariant.LUMO_ERROR);
        }
    }


    private void populateGrid(Set<GitRequest> items) {
        ListDataProvider<GitRequest> dataProvider = new ListDataProvider<>(items);
        grid.setItems(dataProvider);
    }

    @Override
    protected void clearAllFields() {
        uriField.clear();
        commitField.clear();
        allowedExtensionsComboBox.clear();
        usernameField.clear();
        passwordField.clear();
        query.clear();
        populateGrid(Set.of());
    }

    private void autoSizeFields() {
        uriField.setWidth("250px");
        commitField.setWidth("100px");
        usernameField.setWidth("175px");
        passwordField.setWidth("175px");
        query.setWidth("240px");
        grid.setWidth("100%");
    }
}