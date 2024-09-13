package org.cftoolsuite.ui.view;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.cftoolsuite.client.RefactorClient;
import org.cftoolsuite.domain.LanguageExtensions;

import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.server.StreamResource;

public abstract class BaseView extends VerticalLayout {

    protected final TextField uriField = new TextField("URI");
    protected final TextField baseField = new TextField("Base");
    protected final TextField usernameField = new TextField("Username");
    protected final PasswordField passwordField = new PasswordField("Password");
    protected final TextField commitField = new TextField("Commit");
    protected final TextField filePathsField = new TextField("File Paths");
    protected final ComboBox<LanguageExtensions> allowedExtensionsComboBox = new ComboBox<>();
    protected final Button submitButton = new Button("Submit");
    protected final Button clearButton = new Button("Clear");
    protected final HorizontalLayout buttons = new HorizontalLayout();

    protected final RefactorClient refactorClient;

    public BaseView(RefactorClient refactorClient) {
        this.refactorClient = refactorClient;

        autoSizeFields();

        uriField.setHelperText("The URI of a Git repository");
        usernameField.setHelperText("Username");
        passwordField.setHelperText("Password (or Personal Access Token)");
        commitField.setHelperText("The commit hash upon which to execute this request (default: latest commit)");
        filePathsField.setHelperText(
            """
            Comma separated list of file paths relative to the project root.
            E.g. src/main/java/org/example/Example.java, src/test/java/org/example/AnotherExample.java.
            When specified in this manner, each file path is explicitly considered, directory walking is not performed.
            Alternatively, for Java projects, one can specify a list of dot-separated package names where each package is walked recursively.
            E.g. org.example, org.example.sub.
            If no file paths are specified, all files in all directories of the repository will be considered.
            """);

        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.CENTER);
        buttons.setAlignItems(Alignment.CENTER);
        buttons.setJustifyContentMode(JustifyContentMode.CENTER);

        add(getLogoImage());

        uriField.setRequired(true);
        filePathsField.setRequired(true);

        initializeAllowedExtensionsComboBox();

        submitButton.addClickListener(e -> submitRequest());
        clearButton.addClickListener(e -> clearAllFields());
    }


    protected void initializeAllowedExtensionsComboBox() {
        List<LanguageExtensions> items = refactorClient.languageExtensions().getBody();
        allowedExtensionsComboBox.setLabel("Allowed Extensions");
        allowedExtensionsComboBox.setItems(items);
        allowedExtensionsComboBox.setItemLabelGenerator(LanguageExtensions::language);
        allowedExtensionsComboBox.setHelperText("Select language");
        allowedExtensionsComboBox.setClearButtonVisible(true);
        allowedExtensionsComboBox.setWidth("auto");
    }

    protected abstract void autoSizeFields();

    protected abstract void submitRequest();

    protected abstract void clearAllFields();

    protected void showNotification(String message, NotificationVariant variant) {
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

    protected Set<String> convertToSet(String commaSeparatedString) {
        return Arrays.stream(commaSeparatedString.split(","))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .collect(Collectors.toSet());
    }

    private Image getLogoImage() {
        StreamResource imageResource = new StreamResource("robert.png",
            () -> getClass().getResourceAsStream("/static/robert.png"));
        Image logo = new Image(imageResource, "Logo");
        logo.setWidth("360px");
        return logo;
    }
}