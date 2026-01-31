package view.Message;

import controller.MessageController;
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

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;

public class ConversationView {
    private final Stage stage;
    private final MessageController messageController;
    private final String userId;
    private final String userName;
    private List<MessageModel> messages;
    private final Stage parentStage;
    private VBox messagesContainer;
    private ScrollPane scrollPane;

    public ConversationView(Stage stage, MessageController messageController,
                            String userId, String userName,
                            List<MessageModel> messages, Stage parentStage) {
        this.stage = stage;
        this.messageController = messageController;
        this.userId = userId;
        this.userName = userName;
        this.messages = messages;
        this.parentStage = parentStage;
        initializeUI();
    }

    private void initializeUI() {
        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: linear-gradient(to bottom right, #8B7AB8, #B4A5D5);");

        // Main container - reduced size
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

        // Header - more compact
        VBox header = createHeader();

        // Messages area - adjusted height
        scrollPane = new ScrollPane();
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background: transparent; -fx-background-color: transparent;");
        scrollPane.setPrefHeight(420);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);

        messagesContainer = new VBox(10);
        messagesContainer.setPadding(new Insets(15));
        messagesContainer.setAlignment(Pos.TOP_LEFT);

        loadMessages();

        scrollPane.setContent(messagesContainer);
        VBox.setVgrow(scrollPane, Priority.ALWAYS);

        // Reply area - more compact
        HBox replyArea = createReplyArea();

        mainContainer.getChildren().addAll(header, scrollPane, replyArea);

        StackPane centerPane = new StackPane(mainContainer);
        centerPane.setPadding(new Insets(20));
        root.setCenter(centerPane);

        Scene scene = new Scene(root, 1000, 700);
        stage.setScene(scene);
        stage.setTitle("Conversation with " + userName);
        stage.show();

        // Scroll to bottom after UI is rendered
        Platform.runLater(this::scrollToBottom);
    }

    private VBox createHeader() {
        VBox headerContainer = new VBox(8);
        headerContainer.setPadding(new Insets(15));
        headerContainer.setStyle(
                "-fx-background-color: rgba(255, 255, 255, 0.3);" +
                        "-fx-background-radius: 20 20 0 0;"
        );

        HBox topRow = new HBox();
        topRow.setAlignment(Pos.CENTER_LEFT);

        Button backButton = new Button("← Back");
        backButton.setStyle(
                "-fx-background-color: transparent;" +
                        "-fx-text-fill: rgba(0, 0, 0, 0.7);" +
                        "-fx-font-size: 13px;" +
                        "-fx-cursor: hand;" +
                        "-fx-font-weight: bold;"
        );
        backButton.setOnAction(e -> {
            stage.close();
            if (parentStage != null) {
                parentStage.show();
            }
        });

        topRow.getChildren().add(backButton);

        // User info - more compact
        HBox userInfoBox = new HBox(12);
        userInfoBox.setAlignment(Pos.CENTER_LEFT);
        userInfoBox.setPadding(new Insets(8, 0, 0, 0));

        StackPane avatar = new StackPane();
        javafx.scene.shape.Circle avatarCircle = new javafx.scene.shape.Circle(25);
        avatarCircle.setFill(Color.web("#9B8AC4"));
        Label avatarLabel = new Label(userName.substring(0, 1).toUpperCase());
        avatarLabel.setStyle(
                "-fx-text-fill: white;" +
                        "-fx-font-size: 18px;" +
                        "-fx-font-weight: bold;"
        );
        avatar.getChildren().addAll(avatarCircle, avatarLabel);

        VBox userDetails = new VBox(2);
        Label nameLabel = new Label(userName);
        nameLabel.setStyle(
                "-fx-font-size: 16px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-text-fill: rgba(0, 0, 0, 0.8);"
        );

        Label userIdLabel = new Label("ID: " + userId);
        userIdLabel.setStyle(
                "-fx-font-size: 11px;" +
                        "-fx-text-fill: rgba(0, 0, 0, 0.6);"
        );

        userDetails.getChildren().addAll(nameLabel, userIdLabel);
        userInfoBox.getChildren().addAll(avatar, userDetails);

        headerContainer.getChildren().addAll(topRow, new Separator(), userInfoBox);
        return headerContainer;
    }

    private void loadMessages() {
        messagesContainer.getChildren().clear();

        if (messages == null || messages.isEmpty()) {
            Label emptyLabel = new Label("No messages in this conversation");
            emptyLabel.setStyle(
                    "-fx-font-size: 13px;" +
                            "-fx-text-fill: rgba(255, 255, 255, 0.7);"
            );
            messagesContainer.getChildren().add(emptyLabel);
            return;
        }

        // Sort messages by date
        messages.sort((m1, m2) -> m1.getDate().compareTo(m2.getDate()));

        for (MessageModel message : messages) {
            // Check if message is from admin or user
            boolean isAdminMessage = message.getMessageType().equals("ADMIN_TO_USER") ||
                    message.getMessageType().equals("REPLY") ||
                    message.getUserName().equals("Admin");

            if (isAdminMessage) {
                messagesContainer.getChildren().add(createAdminMessageBubble(message));
            } else {
                messagesContainer.getChildren().add(createUserMessageBubble(message));
            }
        }
    }

    private VBox createUserMessageBubble(MessageModel message) {
        VBox bubble = new VBox(6);
        bubble.setMaxWidth(600);
        bubble.setPadding(new Insets(12));

        // User message (from customer) - align left with light background
        bubble.setAlignment(Pos.CENTER_LEFT);
        bubble.setStyle(
                "-fx-background-color: rgba(255, 255, 255, 0.95);" +
                        "-fx-background-radius: 15 15 15 5;" +
                        "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 5, 0, 0, 2);"
        );

        HBox messageHeader = new HBox(8);
        messageHeader.setAlignment(Pos.CENTER_LEFT);

        Label typeLabel = new Label(message.getMessageType().toUpperCase());
        typeLabel.setStyle(
                "-fx-font-size: 9px;" +
                        "-fx-text-fill: #666666;" +
                        "-fx-background-color: #E8E0F5;" +
                        "-fx-padding: 2 6;" +
                        "-fx-background-radius: 6;"
        );

        Label dateLabel = new Label(message.getDate());
        dateLabel.setStyle(
                "-fx-font-size: 10px;" +
                        "-fx-text-fill: #888888;"
        );

        messageHeader.getChildren().addAll(typeLabel, dateLabel);

        Label titleLabel = new Label(message.getTitle());
        titleLabel.setStyle(
                "-fx-font-size: 14px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-text-fill: #333333;"
        );
        titleLabel.setWrapText(true);

        Label contentLabel = new Label(message.getContent());
        contentLabel.setStyle(
                "-fx-font-size: 12px;" +
                        "-fx-text-fill: #555555;"
        );
        contentLabel.setWrapText(true);
        contentLabel.setMaxWidth(550);

        bubble.getChildren().addAll(messageHeader, titleLabel, contentLabel);

        HBox container = new HBox();
        container.getChildren().add(bubble);
        container.setAlignment(Pos.CENTER_LEFT);

        return new VBox(container);
    }

    private VBox createAdminMessageBubble(MessageModel message) {
        VBox bubble = new VBox(6);
        bubble.setMaxWidth(600);
        bubble.setPadding(new Insets(12));

        // Admin message - align right with purple background
        bubble.setAlignment(Pos.CENTER_RIGHT);
        bubble.setStyle(
                "-fx-background-color: #9B8AC4;" +
                        "-fx-background-radius: 15 15 5 15;" +
                        "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 5, 0, 0, 2);"
        );

        HBox messageHeader = new HBox(8);
        messageHeader.setAlignment(Pos.CENTER_LEFT);

        Label adminLabel = new Label("ADMIN");
        adminLabel.setStyle(
                "-fx-font-size: 9px;" +
                        "-fx-text-fill: white;" +
                        "-fx-background-color: rgba(255, 255, 255, 0.2);" +
                        "-fx-padding: 2 6;" +
                        "-fx-background-radius: 6;" +
                        "-fx-font-weight: bold;"
        );

        Label dateLabel = new Label(message.getDate());
        dateLabel.setStyle(
                "-fx-font-size: 10px;" +
                        "-fx-text-fill: rgba(255, 255, 255, 0.8);"
        );

        messageHeader.getChildren().addAll(adminLabel, dateLabel);

        Label titleLabel = new Label(message.getTitle());
        titleLabel.setStyle(
                "-fx-font-size: 14px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-text-fill: white;"
        );
        titleLabel.setWrapText(true);

        Label contentLabel = new Label(message.getContent());
        contentLabel.setStyle(
                "-fx-font-size: 12px;" +
                        "-fx-text-fill: rgba(255, 255, 255, 0.95);"
        );
        contentLabel.setWrapText(true);
        contentLabel.setMaxWidth(550);

        bubble.getChildren().addAll(messageHeader, titleLabel, contentLabel);

        HBox container = new HBox();
        container.getChildren().add(bubble);
        container.setAlignment(Pos.CENTER_RIGHT);

        return new VBox(container);
    }

    private HBox createReplyArea() {
        HBox replyBox = new HBox(8);
        replyBox.setPadding(new Insets(15));
        replyBox.setAlignment(Pos.CENTER);
        replyBox.setStyle(
                "-fx-background-color: rgba(255, 255, 255, 0.3);" +
                        "-fx-background-radius: 0 0 20 20;"
        );

        TextField titleField = new TextField();
        titleField.setPromptText("Subject...");
        titleField.setPrefWidth(180);
        titleField.setStyle(
                "-fx-background-color: rgba(255, 255, 255, 0.9);" +
                        "-fx-background-radius: 10;" +
                        "-fx-padding: 8;" +
                        "-fx-font-size: 12px;"
        );

        TextArea messageField = new TextArea();
        messageField.setPromptText("Type your reply...");
        messageField.setPrefRowCount(1);
        messageField.setPrefWidth(380);
        messageField.setWrapText(true);
        messageField.setStyle(
                "-fx-background-color: rgba(255, 255, 255, 0.9);" +
                        "-fx-background-radius: 10;" +
                        "-fx-padding: 8;" +
                        "-fx-font-size: 12px;"
        );

        Button sendButton = new Button("Send Reply");
        sendButton.setStyle(
                "-fx-background-color: #9B8AC4;" +
                        "-fx-text-fill: white;" +
                        "-fx-font-size: 12px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-padding: 8 20;" +
                        "-fx-background-radius: 10;" +
                        "-fx-cursor: hand;"
        );

        sendButton.setOnAction(e -> {
            String title = titleField.getText().trim();
            String content = messageField.getText().trim();

            if (!title.isEmpty() && !content.isEmpty()) {
                sendReply(title, content);
                titleField.clear();
                messageField.clear();
            } else {
                showAlert("Error", "Please enter both subject and message");
            }
        });

        sendButton.setOnMouseEntered(e ->
                sendButton.setStyle(
                        "-fx-background-color: #8B7AB8;" +
                                "-fx-text-fill: white;" +
                                "-fx-font-size: 12px;" +
                                "-fx-font-weight: bold;" +
                                "-fx-padding: 8 20;" +
                                "-fx-background-radius: 10;" +
                                "-fx-cursor: hand;"
                )
        );
        sendButton.setOnMouseExited(e ->
                sendButton.setStyle(
                        "-fx-background-color: #9B8AC4;" +
                                "-fx-text-fill: white;" +
                                "-fx-font-size: 12px;" +
                                "-fx-font-weight: bold;" +
                                "-fx-padding: 8 20;" +
                                "-fx-background-radius: 10;" +
                                "-fx-cursor: hand;"
                )
        );

        replyBox.getChildren().addAll(titleField, messageField, sendButton);
        return replyBox;
    }

    private void sendReply(String title, String content) {
        String messageId = UUID.randomUUID().toString();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String currentDate = LocalDateTime.now().format(formatter);

        // Send admin reply - store with the user's ID so it appears in their conversation
        messageController.send(messageId, "Admin", title, content, currentDate, "ADMIN_TO_USER", userId);

        // Reload messages to include the new reply
        messages = messageController.getConversation(userId);
        loadMessages();

        // Scroll to bottom to show new message
        Platform.runLater(this::scrollToBottom);

        showAlert("Success", "Reply sent successfully!");
    }

    private void scrollToBottom() {
        messagesContainer.layout();
        scrollPane.layout();
        scrollPane.setVvalue(scrollPane.getVmax());
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}