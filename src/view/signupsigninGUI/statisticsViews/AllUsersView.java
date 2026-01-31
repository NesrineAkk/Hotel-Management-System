package view.signupsigninGUI.statisticsViews;

import controller.UserController;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import model.user.UserModel;

import java.util.List;
import java.util.Optional;

public class AllUsersView {
    private final Stage stage;
    private final UserController userController;
    private final Stage parentStage;
    private VBox usersList; // Make this a field so we can refresh it

    public AllUsersView(Stage stage, UserController userController, Stage parentStage) {
        this.stage = stage;
        this.userController = userController;
        this.parentStage = parentStage;
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

        Label title = new Label("Registered Users");
        title.setStyle(
                "-fx-font-size: 24px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-text-fill: rgba(255, 255, 255, 0.95);"
        );

        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background: transparent; -fx-background-color: transparent;");
        scrollPane.setPrefHeight(450);

        usersList = new VBox(15);
        usersList.setPadding(new Insets(10));
        usersList.setAlignment(Pos.TOP_CENTER);

        refreshUsersList();

        scrollPane.setContent(usersList);

        mainContainer.getChildren().addAll(header, title, scrollPane);
        root.getChildren().add(mainContainer);

        Scene scene = new Scene(root, 1000, 700);
        stage.setScene(scene);
        stage.setTitle("All Users");
        stage.show();
    }

    private void refreshUsersList() {
        usersList.getChildren().clear();

        List<UserModel> users = userController.getAllUsers();

        if (users.isEmpty()) {
            Label emptyLabel = new Label("No users registered");
            emptyLabel.setStyle(
                    "-fx-font-size: 16px;" +
                            "-fx-text-fill: rgba(255, 255, 255, 0.8);"
            );
            usersList.getChildren().add(emptyLabel);
        } else {
            for (UserModel user : users) {
                usersList.getChildren().add(createUserCard(user));
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

    private VBox createUserCard(UserModel user) {
        VBox card = new VBox(10);
        card.setPadding(new Insets(20));
        card.setStyle(
                "-fx-background-color: rgba(255, 255, 255, 0.9);" +
                        "-fx-background-radius: 15;" +
                        "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 10, 0, 0, 5);"
        );
        card.setMaxWidth(800);

        HBox userHeader = new HBox(15);
        userHeader.setAlignment(Pos.CENTER_LEFT);

        String firstName = user.getFirstName() != null ? user.getFirstName() : "";
        String lastName = user.getLastName() != null ? user.getLastName() : "";
        Label userName = new Label(firstName + " " + lastName);
        userName.setStyle(
                "-fx-font-size: 20px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-text-fill: #333333;"
        );

        String roleText = user.getRole() != null ? user.getRole().toString().toUpperCase() : "USER";
        Label roleLabel = new Label(roleText);
        String roleColor = (user.getRole() != null && user.getRole().toString().equalsIgnoreCase("ADMIN")) ?
                "#FFD700" : "#E0F5E8";
        roleLabel.setStyle(
                "-fx-font-size: 12px;" +
                        "-fx-text-fill: #666666;" +
                        "-fx-background-color: " + roleColor + ";" +
                        "-fx-padding: 5 10;" +
                        "-fx-background-radius: 10;"
        );

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Button deleteButton = new Button("🗑 Delete");
        deleteButton.setStyle(
                "-fx-background-color: #FF6B6B;" +
                        "-fx-text-fill: white;" +
                        "-fx-font-size: 12px;" +
                        "-fx-padding: 8 15;" +
                        "-fx-background-radius: 8;" +
                        "-fx-cursor: hand;" +
                        "-fx-font-weight: bold;"
        );
        deleteButton.setOnMouseEntered(e ->
                deleteButton.setStyle(
                        "-fx-background-color: #FF5252;" +
                                "-fx-text-fill: white;" +
                                "-fx-font-size: 12px;" +
                                "-fx-padding: 8 15;" +
                                "-fx-background-radius: 8;" +
                                "-fx-cursor: hand;" +
                                "-fx-font-weight: bold;"
                )
        );
        deleteButton.setOnMouseExited(e ->
                deleteButton.setStyle(
                        "-fx-background-color: #FF6B6B;" +
                                "-fx-text-fill: white;" +
                                "-fx-font-size: 12px;" +
                                "-fx-padding: 8 15;" +
                                "-fx-background-radius: 8;" +
                                "-fx-cursor: hand;" +
                                "-fx-font-weight: bold;"
                )
        );
        deleteButton.setOnAction(e -> handleDeleteUser(user, card));

        userHeader.getChildren().addAll(userName, roleLabel, spacer, deleteButton);

        GridPane details = new GridPane();
        details.setHgap(20);
        details.setVgap(8);
        details.setPadding(new Insets(10, 0, 0, 0));

        addDetailRow(details, 0, "User ID:", user.getId() != null ? user.getId() : "N/A");
        addDetailRow(details, 1, "Email:", user.getEmail() != null ? user.getEmail() : "N/A");
        addDetailRow(details, 2, "Phone:", user.getPhone() != null ? user.getPhone() : "N/A");
        addDetailRow(details, 3, "Role:", user.getRole() != null ? user.getRole().toString() : "N/A");

        card.getChildren().addAll(userHeader, new Separator(), details);

        card.setOnMouseEntered(e ->
                card.setStyle(
                        "-fx-background-color: rgba(255, 255, 255, 1.0);" +
                                "-fx-background-radius: 15;" +
                                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 15, 0, 0, 7);"
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

    private void handleDeleteUser(UserModel user, VBox card) {
        Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmAlert.setTitle("Delete User");
        confirmAlert.setHeaderText("Are you sure you want to delete this user?");
        confirmAlert.setContentText("User: " + user.getFirstName() + " " + user.getLastName() +
                "\nEmail: " + user.getEmail() +
                "\n\nThis action cannot be undone.");

        Optional<ButtonType> result = confirmAlert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            boolean success = userController.deleteUser(user.getId());

            if (success) {
                // Refresh the list to reflect the deletion
                refreshUsersList();

                Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
                successAlert.setTitle("Success");
                successAlert.setHeaderText(null);
                successAlert.setContentText("User deleted successfully!");
                successAlert.showAndWait();
            } else {
                Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                errorAlert.setTitle("Error");
                errorAlert.setHeaderText(null);
                errorAlert.setContentText("Failed to delete user. You cannot delete yourself while logged in.");
                errorAlert.showAndWait();
            }
        }
    }

    private void addDetailRow(GridPane grid, int row, String label, String value) {
        Label labelNode = new Label(label);
        labelNode.setStyle("-fx-font-weight: bold; -fx-text-fill: #555555;");

        Label valueNode = new Label(value);
        valueNode.setStyle("-fx-text-fill: #666666;");

        grid.add(labelNode, 0, row);
        grid.add(valueNode, 1, row);
    }
}