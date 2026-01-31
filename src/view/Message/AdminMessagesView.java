package view.Message;

import controller.MessageController;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import model.message.MessageModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AdminMessagesView {
    private final Stage stage;
    private final MessageController messageController;
    private final Stage parentStage;
    private VBox conversationsList;
    private Map<String, List<MessageModel>> conversationsByUser;

    public AdminMessagesView(Stage stage, MessageController messageController, Stage parentStage) {
        this.stage = stage;
        this.messageController = messageController;
        this.parentStage = parentStage;
        this.conversationsByUser = new HashMap<>();
        initializeUI();
    }

    private void initializeUI() {
        StackPane root = new StackPane();
        root.setStyle("-fx-background-color: linear-gradient(to bottom right, #8B7AB8, #B4A5D5);");

        VBox mainContainer = new VBox(20);
        mainContainer.setMaxSize(900, 650);
        mainContainer.setAlignment(Pos.TOP_CENTER);
        mainContainer.setPadding(new Insets(30));
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

        HBox header = createHeader();

        Label title = new Label("Messages");
        title.setStyle(
                "-fx-font-size: 24px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-text-fill: rgba(255, 255, 255, 0.95);"
        );

        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background: transparent; -fx-background-color: transparent;");
        scrollPane.setPrefHeight(450);

        conversationsList = new VBox(15);
        conversationsList.setPadding(new Insets(10));
        conversationsList.setAlignment(Pos.TOP_CENTER);

        loadConversations();

        scrollPane.setContent(conversationsList);

        mainContainer.getChildren().addAll(header, title, scrollPane);
        root.getChildren().add(mainContainer);

        Scene scene = new Scene(root, 1000, 700);
        stage.setScene(scene);
        stage.setTitle("Admin Messages");
        stage.show();
    }

    private void loadConversations() {
        conversationsByUser.clear();
        ArrayList<MessageModel> allMessages = messageController.getMessages();

        if (allMessages == null || allMessages.isEmpty()) {
            Label emptyLabel = new Label("No messages yet");
            emptyLabel.setStyle(
                    "-fx-font-size: 16px;" +
                            "-fx-text-fill: rgba(255, 255, 255, 0.8);"
            );
            conversationsList.getChildren().add(emptyLabel);
            return;
        }

        // Group messages by sender
        for (Object obj : allMessages) {
            MessageModel message = (MessageModel) obj;
            String userId = message.getSenderId();

            if (!conversationsByUser.containsKey(userId)) {
                conversationsByUser.put(userId, new ArrayList<>());
            }
            conversationsByUser.get(userId).add(message);
        }

        // Create conversation cards for each user
        for (Map.Entry<String, List<MessageModel>> entry : conversationsByUser.entrySet()) {
            String userId = entry.getKey();
            List<MessageModel> userMessages = entry.getValue();

            if (!userMessages.isEmpty()) {
                MessageModel latestMessage = userMessages.get(userMessages.size() - 1);
                conversationsList.getChildren().add(createConversationCard(userId, latestMessage, userMessages.size()));
            }
        }
    }

    private HBox createHeader() {
        HBox header = new HBox();
        header.setAlignment(Pos.CENTER_LEFT);
        header.setPadding(new Insets(10, 20, 10, 20));
        header.setStyle(
                "-fx-background-color: rgba(255, 255, 255, 0.3);" +
                        "-fx-background-radius: 20;"
        );

        Button backButton = new Button("← Back");
        backButton.setStyle(
                "-fx-background-color: transparent;" +
                        "-fx-text-fill: rgba(0, 0, 0, 0.7);" +
                        "-fx-font-size: 14px;" +
                        "-fx-cursor: hand;" +
                        "-fx-font-weight: bold;"
        );
        backButton.setOnAction(e -> {
            stage.close();
            if (parentStage != null) {
                parentStage.show();
            }
        });

        header.getChildren().add(backButton);
        return header;
    }

    private VBox createConversationCard(String userId, MessageModel latestMessage, int messageCount) {
        VBox card = new VBox(10);
        card.setPadding(new Insets(20));
        card.setStyle(
                "-fx-background-color: rgba(255, 255, 255, 0.9);" +
                        "-fx-background-radius: 15;" +
                        "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 10, 0, 0, 5);"
        );
        card.setMaxWidth(800);

        HBox cardHeader = new HBox(15);
        cardHeader.setAlignment(Pos.CENTER_LEFT);

        // User avatar circle
        StackPane avatar = new StackPane();
        javafx.scene.shape.Circle avatarCircle = new javafx.scene.shape.Circle(25);
        avatarCircle.setFill(Color.web("#9B8AC4"));
        Label avatarLabel = new Label(latestMessage.getUserName().substring(0, 1).toUpperCase());
        avatarLabel.setStyle(
                "-fx-text-fill: white;" +
                        "-fx-font-size: 18px;" +
                        "-fx-font-weight: bold;"
        );
        avatar.getChildren().addAll(avatarCircle, avatarLabel);

        VBox userInfo = new VBox(5);
        Label userName = new Label(latestMessage.getUserName());
        userName.setStyle(
                "-fx-font-size: 18px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-text-fill: #333333;"
        );

        Label messagePreview = new Label(latestMessage.getTitle());
        messagePreview.setStyle(
                "-fx-font-size: 13px;" +
                        "-fx-text-fill: #666666;"
        );
        messagePreview.setMaxWidth(500);

        userInfo.getChildren().addAll(userName, messagePreview);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        VBox rightInfo = new VBox(5);
        rightInfo.setAlignment(Pos.TOP_RIGHT);

        Label dateLabel = new Label(latestMessage.getDate());
        dateLabel.setStyle(
                "-fx-font-size: 11px;" +
                        "-fx-text-fill: #888888;"
        );

        Label countBadge = new Label(String.valueOf(messageCount));
        countBadge.setStyle(
                "-fx-background-color: #9B8AC4;" +
                        "-fx-text-fill: white;" +
                        "-fx-font-size: 11px;" +
                        "-fx-padding: 2 6;" +
                        "-fx-background-radius: 10;"
        );

        rightInfo.getChildren().addAll(dateLabel, countBadge);

        cardHeader.getChildren().addAll(avatar, userInfo, spacer, rightInfo);
        card.getChildren().add(cardHeader);

        // Click handler to open conversation
        card.setOnMouseClicked(e -> openConversation(userId, latestMessage.getUserName()));

        // Hover effect
        card.setOnMouseEntered(e ->
                card.setStyle(
                        "-fx-background-color: rgba(255, 255, 255, 1.0);" +
                                "-fx-background-radius: 15;" +
                                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 15, 0, 0, 7);" +
                                "-fx-cursor: hand;"
                )
        );
        card.setOnMouseExited(e ->
                card.setStyle(
                        "-fx-background-color: rgba(255, 255, 255, 0.9);" +
                                "-fx-background-radius: 15;" +
                                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 10, 0, 0, 5);"
                )
        );

        return card;
    }

    private void openConversation(String userId, String userName) {
        List<MessageModel> userMessages = conversationsByUser.get(userId);
        stage.hide();
        Stage conversationStage = new Stage();
        new ConversationView(conversationStage, messageController, userId, userName, userMessages, stage);
    }
}
