package view.Message;

import view.signupsigninGUI.statisticsViews.MainClientMenu;
import controller.MessageController;
import data.SessionManager;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import model.message.MessageModel;
import model.user.UserModel;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class ContactAdminView {

    private Stage primaryStage;
    private MessageController messageController;
    private VBox chatContainer;
    private ScrollPane scrollPane;
    private TextField messageInput;
    private String conversationTitle = "";
    private UserModel currentUser;
    private MainClientMenu mainMenu;

    public ContactAdminView(Stage primaryStage, MessageController messageController) {
        this.primaryStage = primaryStage;
        this.messageController = messageController;
        this.currentUser = SessionManager.getInstance().getCurrentUser();
    }

    public void setMainMenu(MainClientMenu mainMenu) {
        this.mainMenu = mainMenu;
    }

    public void show() {
        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: linear-gradient(to bottom right, #8B7AB8, #B4A5D5);");

        // Main container
        VBox mainContainer = new VBox();
        mainContainer.setMaxSize(900, 650);
        mainContainer.setAlignment(Pos.TOP_CENTER);
        mainContainer.setStyle(
                "-fx-background-color: rgba(255, 255, 255, 0.25);" +
                        "-fx-background-radius: 30;" +
                        "-fx-border-color: rgba(255, 255, 255, 0.4);" +
                        "-fx-border-width: 1.5;" +
                        "-fx-border-radius: 30;"
        );

        DropShadow shadow = new DropShadow();
        shadow.setColor(Color.rgb(0, 0, 0, 0.2));
        shadow.setRadius(30);
        mainContainer.setEffect(shadow);

        // Header
        HBox header = createHeader();

        // Chat area
        VBox chatArea = createChatArea();
        VBox.setVgrow(chatArea, Priority.ALWAYS);

        // Input area
        HBox inputArea = createInputArea();

        mainContainer.getChildren().addAll(header, chatArea, inputArea);

        StackPane centerPane = new StackPane(mainContainer);
        centerPane.setPadding(new Insets(20));
        root.setCenter(centerPane);

        // Load existing conversation or show welcome message
        loadConversation();

        Scene scene = new Scene(root, 1000, 700);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Contact Admin");
        primaryStage.show();
    }

    private HBox createHeader() {
        HBox header = new HBox();
        header.setPadding(new Insets(20));
        header.setAlignment(Pos.CENTER_LEFT);
        header.setStyle(
                "-fx-background-color: rgba(255, 255, 255, 0.3);" +
                        "-fx-background-radius: 20 20 0 0;"
        );
        header.setSpacing(10);

        Button backButton = new Button("← Back");
        backButton.setStyle(
                "-fx-background-color: transparent;" +
                        "-fx-text-fill: rgba(0, 0, 0, 0.7);" +
                        "-fx-font-size: 14px;" +
                        "-fx-cursor: hand;" +
                        "-fx-border-width: 0;" +
                        "-fx-font-weight: bold;"
        );
        backButton.setOnAction(e -> {
            // Return to main menu using the same window
            if (mainMenu != null) {
                mainMenu.returnToMainMenu();
            }
        });

        Label titleLabel = new Label("Admin Support");
        titleLabel.setStyle(
                "-fx-font-size: 18px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-text-fill: rgba(0, 0, 0, 0.8);"
        );

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        // Purple heart icon
        Label iconLabel = new Label("💜");
        iconLabel.setStyle("-fx-font-size: 20px;");

        header.getChildren().addAll(backButton, titleLabel, spacer, iconLabel);
        return header;
    }

    private VBox createChatArea() {
        VBox chatArea = new VBox();
        chatArea.setStyle("-fx-background-color: transparent;");
        chatArea.setPadding(new Insets(10, 0, 10, 0));

        chatContainer = new VBox(15);
        chatContainer.setPadding(new Insets(20));
        chatContainer.setStyle("-fx-background-color: transparent;");

        scrollPane = new ScrollPane(chatContainer);
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background: transparent; -fx-background-color: transparent;");
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);

        VBox.setVgrow(scrollPane, Priority.ALWAYS);
        chatArea.getChildren().add(scrollPane);

        return chatArea;
    }

    private HBox createInputArea() {
        HBox inputArea = new HBox(10);
        inputArea.setPadding(new Insets(20));
        inputArea.setAlignment(Pos.CENTER);
        inputArea.setStyle(
                "-fx-background-color: rgba(255, 255, 255, 0.3);" +
                        "-fx-background-radius: 0 0 20 20;"
        );

        messageInput = new TextField();
        messageInput.setPromptText("Type your message...");
        messageInput.setStyle(
                "-fx-background-color: rgba(255, 255, 255, 0.9);" +
                        "-fx-background-radius: 20;" +
                        "-fx-padding: 10 15 10 15;" +
                        "-fx-font-size: 14px;" +
                        "-fx-border-width: 0;"
        );
        messageInput.setPrefHeight(40);
        HBox.setHgrow(messageInput, Priority.ALWAYS);

        messageInput.setOnAction(e -> sendMessage());

        Button sendButton = new Button("Send");
        sendButton.setStyle(
                "-fx-background-color: #9B8AC4;" +
                        "-fx-text-fill: white;" +
                        "-fx-background-radius: 20;" +
                        "-fx-font-size: 14px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-cursor: hand;" +
                        "-fx-padding: 10 20 10 20;"
        );
        sendButton.setOnAction(e -> sendMessage());

        sendButton.setOnMouseEntered(e ->
                sendButton.setStyle(
                        "-fx-background-color: #8B7AB8;" +
                                "-fx-text-fill: white;" +
                                "-fx-background-radius: 20;" +
                                "-fx-font-size: 14px;" +
                                "-fx-font-weight: bold;" +
                                "-fx-cursor: hand;" +
                                "-fx-padding: 10 20 10 20;"
                )
        );
        sendButton.setOnMouseExited(e ->
                sendButton.setStyle(
                        "-fx-background-color: #9B8AC4;" +
                                "-fx-text-fill: white;" +
                                "-fx-background-radius: 20;" +
                                "-fx-font-size: 14px;" +
                                "-fx-font-weight: bold;" +
                                "-fx-cursor: hand;" +
                                "-fx-padding: 10 20 10 20;"
                )
        );

        inputArea.getChildren().addAll(messageInput, sendButton);
        return inputArea;
    }

    private void loadConversation() {
        chatContainer.getChildren().clear();

        if (currentUser == null) {
            displayWelcomeMessage();
            return;
        }

        // Get all messages for this user
        ArrayList<MessageModel> conversation = messageController.getConversation(currentUser.getId());

        if (conversation.isEmpty()) {
            displayWelcomeMessage();
        } else {
            // Display all messages in conversation
            for (MessageModel message : conversation) {
                boolean isAdminMessage = message.getMessageType().equals("ADMIN_TO_USER") ||
                        message.getMessageType().equals("REPLY") ||
                        message.getUserName().equals("Admin");

                if (isAdminMessage) {
                    addAdminMessageFromDB(message.getContent(), message.getDate());
                } else {
                    addUserMessageFromDB(message.getContent(), message.getDate());
                }
            }

            // Set conversation title from first message
            if (!conversation.isEmpty()) {
                conversationTitle = conversation.get(0).getTitle();
            }
        }

        Platform.runLater(this::scrollToBottom);
    }

    private void displayWelcomeMessage() {
        addAdminMessage("Hi! How can I help you today?");
    }

    private void addAdminMessage(String text) {
        HBox messageBox = new HBox();
        messageBox.setAlignment(Pos.CENTER_LEFT);
        messageBox.setPadding(new Insets(5, 0, 5, 0));

        // Purple heart icon
        Label icon = new Label("💜");
        icon.setStyle("-fx-font-size: 16px;");
        icon.setAlignment(Pos.TOP_LEFT);

        VBox messageContent = new VBox(5);

        Label messageLabel = new Label(text);
        messageLabel.setWrapText(true);
        messageLabel.setMaxWidth(500);
        messageLabel.setStyle(
                "-fx-background-color: rgba(255, 255, 255, 0.95);" +
                        "-fx-background-radius: 15 15 15 5;" +
                        "-fx-padding: 12 15 12 15;" +
                        "-fx-font-size: 14px;" +
                        "-fx-text-fill: #333333;"
        );

        Label timeLabel = new Label(getCurrentTime());
        timeLabel.setStyle(
                "-fx-font-size: 11px;" +
                        "-fx-text-fill: rgba(255, 255, 255, 0.8);"
        );
        timeLabel.setPadding(new Insets(2, 0, 0, 5));

        messageContent.getChildren().addAll(messageLabel, timeLabel);
        messageBox.getChildren().addAll(icon, messageContent);
        messageBox.setSpacing(8);

        chatContainer.getChildren().add(messageBox);
        Platform.runLater(this::scrollToBottom);
    }

    private void addAdminMessageFromDB(String text, String timestamp) {
        HBox messageBox = new HBox();
        messageBox.setAlignment(Pos.CENTER_LEFT);
        messageBox.setPadding(new Insets(5, 0, 5, 0));

        // Purple heart icon
        Label icon = new Label("💜");
        icon.setStyle("-fx-font-size: 16px;");
        icon.setAlignment(Pos.TOP_LEFT);

        VBox messageContent = new VBox(5);

        Label messageLabel = new Label(text);
        messageLabel.setWrapText(true);
        messageLabel.setMaxWidth(500);
        messageLabel.setStyle(
                "-fx-background-color: rgba(255, 255, 255, 0.95);" +
                        "-fx-background-radius: 15 15 15 5;" +
                        "-fx-padding: 12 15 12 15;" +
                        "-fx-font-size: 14px;" +
                        "-fx-text-fill: #333333;"
        );

        // Extract time from timestamp (yyyy-MM-dd HH:mm:ss)
        String time = timestamp.length() >= 16 ? timestamp.substring(11, 16) : timestamp;
        Label timeLabel = new Label(time);
        timeLabel.setStyle(
                "-fx-font-size: 11px;" +
                        "-fx-text-fill: rgba(255, 255, 255, 0.8);"
        );
        timeLabel.setPadding(new Insets(2, 0, 0, 5));

        messageContent.getChildren().addAll(messageLabel, timeLabel);
        messageBox.getChildren().addAll(icon, messageContent);
        messageBox.setSpacing(8);

        chatContainer.getChildren().add(messageBox);
    }

    private void addUserMessage(String text) {
        HBox messageBox = new HBox();
        messageBox.setAlignment(Pos.CENTER_RIGHT);
        messageBox.setPadding(new Insets(5, 0, 5, 0));

        VBox messageContent = new VBox(5);
        messageContent.setAlignment(Pos.TOP_RIGHT);

        Label messageLabel = new Label(text);
        messageLabel.setWrapText(true);
        messageLabel.setMaxWidth(500);
        messageLabel.setStyle(
                "-fx-background-color: #9B8AC4;" +
                        "-fx-background-radius: 15 15 5 15;" +
                        "-fx-padding: 12 15 12 15;" +
                        "-fx-font-size: 14px;" +
                        "-fx-text-fill: white;"
        );

        Label timeLabel = new Label(getCurrentTime());
        timeLabel.setStyle(
                "-fx-font-size: 11px;" +
                        "-fx-text-fill: rgba(255, 255, 255, 0.8);"
        );
        timeLabel.setPadding(new Insets(2, 5, 0, 0));

        messageContent.getChildren().addAll(messageLabel, timeLabel);
        messageBox.getChildren().add(messageContent);

        chatContainer.getChildren().add(messageBox);
        Platform.runLater(this::scrollToBottom);
    }

    private void addUserMessageFromDB(String text, String timestamp) {
        HBox messageBox = new HBox();
        messageBox.setAlignment(Pos.CENTER_RIGHT);
        messageBox.setPadding(new Insets(5, 0, 5, 0));

        VBox messageContent = new VBox(5);
        messageContent.setAlignment(Pos.TOP_RIGHT);

        Label messageLabel = new Label(text);
        messageLabel.setWrapText(true);
        messageLabel.setMaxWidth(500);
        messageLabel.setStyle(
                "-fx-background-color: #9B8AC4;" +
                        "-fx-background-radius: 15 15 5 15;" +
                        "-fx-padding: 12 15 12 15;" +
                        "-fx-font-size: 14px;" +
                        "-fx-text-fill: white;"
        );

        // Extract time from timestamp
        String time = timestamp.length() >= 16 ? timestamp.substring(11, 16) : timestamp;
        Label timeLabel = new Label(time);
        timeLabel.setStyle(
                "-fx-font-size: 11px;" +
                        "-fx-text-fill: rgba(255, 255, 255, 0.8);"
        );
        timeLabel.setPadding(new Insets(2, 5, 0, 0));

        messageContent.getChildren().addAll(messageLabel, timeLabel);
        messageBox.getChildren().add(messageContent);

        chatContainer.getChildren().add(messageBox);
    }

    private void sendMessage() {
        String text = messageInput.getText().trim();

        if (text.isEmpty()) {
            return;
        }

        // Check if this is the first message (title)
        if (conversationTitle.isEmpty()) {
            conversationTitle = text.length() > 50 ? text.substring(0, 50) : text;
        }

        // Add message to UI
        addUserMessage(text);

        // Save message to database
        if (currentUser != null) {
            boolean success = messageController.sendMessageToAdmin(
                    currentUser.getId(),
                    currentUser.getFirstName(),
                    conversationTitle,
                    text
            );

            if (!success) {
                showAlert("Error", "Failed to send message. Please try again.");
            }
        } else {
            showAlert("Error", "You must be logged in to send messages.");
        }

        messageInput.clear();
    }

    private void scrollToBottom() {
        chatContainer.layout();
        scrollPane.layout();
        scrollPane.setVvalue(scrollPane.getVmax());
    }

    private String getCurrentTime() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        return LocalDateTime.now().format(formatter);
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}