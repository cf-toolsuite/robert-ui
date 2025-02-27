package org.cftoolsuite.ui.view;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

import org.cftoolsuite.client.ModeClient;
import org.cftoolsuite.client.RefactorClient;

import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.Notification.Position;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

public abstract class BaseView extends VerticalLayout {

    protected final RefactorClient refactorClient;
    protected final ModeClient modeClient;

    public BaseView(RefactorClient refactorClient, ModeClient modeClient) {
        this.refactorClient = refactorClient;
        this.modeClient = modeClient;
        setupUI();
    }

    protected abstract void setupUI();

    protected abstract void submitRequest();

    protected abstract void clearAllFields();

    protected void showNotification(String message, NotificationVariant variant) {
        Notification notification = new Notification(message, 5000, Position.TOP_STRETCH);
        notification.setPosition(Notification.Position.TOP_CENTER);
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
}