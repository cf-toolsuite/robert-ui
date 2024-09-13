package org.cftoolsuite.ui.view;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

import org.cftoolsuite.client.ModeClient;
import org.cftoolsuite.client.RefactorClient;

import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.server.StreamResource;

public abstract class BaseView extends VerticalLayout {

    protected final RefactorClient refactorClient;
    protected final ModeClient modeClient;

    public BaseView(RefactorClient refactorClient, ModeClient modeClient) {
        this.refactorClient = refactorClient;
        this.modeClient = modeClient;

        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.CENTER);

        add(getLogoImage());
        setupUI();
    }

    protected abstract void setupUI();

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
        logo.setWidth("240px");
        return logo;
    }
}