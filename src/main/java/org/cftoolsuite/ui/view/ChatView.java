package org.cftoolsuite.ui.view;

import org.cftoolsuite.client.ModeClient;
import org.cftoolsuite.client.RefactorClient;
import org.cftoolsuite.ui.MainLayout;
import org.cftoolsuite.ui.component.Markdown;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;

import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.progressbar.ProgressBar;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@PageTitle("robert-ui » Chat")
@Route(value = "chat", layout = MainLayout.class)
public class ChatView extends BaseView  {

    private static final Logger log = LoggerFactory.getLogger(ChatView.class);

    private Div messageList;
    private TextField messageInput;
    private Button sendButton;
    private Button clearButton;
    private HorizontalLayout buttons;
    private ProgressBar typingIndicator;

    public ChatView(RefactorClient refactorClient, ModeClient modeClient) {
        super(refactorClient, modeClient);
    }

    @Override
    protected void setupUI() {
        setSizeFull();
        setPadding(true);
        setSpacing(true);

        messageList = new Div();
        messageList.setClassName("message-list");
        messageList.getStyle().set("overflow-y", "auto");
        messageList.setHeight("95%");

        typingIndicator = new ProgressBar();
        typingIndicator.setIndeterminate(true);
        typingIndicator.setVisible(false);

        messageInput = new TextField();
        messageInput.setPlaceholder("Type a message...");
        messageInput.setWidth("100%");

        this.buttons = new HorizontalLayout();
        sendButton = new Button("Send");
        sendButton.addClickListener(e -> submitRequest());
        sendButton.addClickShortcut(Key.ENTER);

        this.clearButton = new Button("Clear");
        clearButton.addClickListener(e -> clearAllFields());
        buttons.add(sendButton, clearButton);

        messageInput.addValueChangeListener(event -> {
            if (event.getValue().isEmpty()) {
                hideThinkingIndicator();
            }
        });

        add(new H2("Chat"));

        VerticalLayout inputLayout = new VerticalLayout(typingIndicator, messageInput, buttons);
        inputLayout.setSpacing(false);
        inputLayout.setPadding(false);

        add(messageList, inputLayout);
    }

    @Override
    protected void submitRequest() {
        String message = messageInput.getValue();
        if (!message.isEmpty()) {
            addMessageToList("You:", message);
            messageInput.clear();
            getAiBotResponse(message);
        }
    }

    @Override
    protected void clearAllFields() {
        messageInput.clear();
        messageList.removeAll();
    }

    private void addMessageToList(String title, String message) {
        H4 whom = new H4(title);
        Markdown messageDiv = new Markdown(message);
        messageList.add(whom, messageDiv);
    }

    private void showThinkingIndicator() {
        typingIndicator.setVisible(true);
    }

    private void hideThinkingIndicator() {
        typingIndicator.setVisible(false);
    }

    private void getAiBotResponse(String message) {
        showThinkingIndicator();
        try {
            ResponseEntity<String> response = refactorClient.chat(message);
            if (response.getStatusCode().is2xxSuccessful()) {
                addMessageToList("AI ChatBot:", response.getBody());
            } else {
                String errorMessage = "Error submitting chat request. Status code: " + response.getStatusCode();
                if (response.getBody() != null) {
                    errorMessage += ". Message: " + response.getBody().toString();
                }
                showNotification(errorMessage, NotificationVariant.LUMO_ERROR);
            }
            hideThinkingIndicator();
        } catch (Exception e) {
            String errorMessage = "An unexpected error occurred: " + e.getMessage();
            hideThinkingIndicator();
            showNotification(errorMessage, NotificationVariant.LUMO_ERROR);
            log.error("An unexpected error occurred", e);
        }
    }
}
