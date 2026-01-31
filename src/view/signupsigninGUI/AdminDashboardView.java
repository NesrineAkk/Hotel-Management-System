package view.signupsigninGUI;

import view.Message.AdminMessagesView;
import view.signupsigninGUI.statisticsViews.AllReservationsView;
import view.signupsigninGUI.statisticsViews.AllUsersView;
import view.signupsigninGUI.statisticsViews.AvailableRoomsView;
import view.signupsigninGUI.statisticsViews.OccupiedRoomsView;
import controller.MessageController;
import controller.ReservationController;
import controller.RoomController;
import controller.UserController;
import data.SessionManager;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import main.Main;
import model.services.ReservationService;
import model.services.RoomService;
import model.services.UserService;
import model.user.Role;
import model.user.UserModel;

public class AdminDashboardView {

    private final UserController userController;
    private final RoomController roomController;
    private final ReservationController reservationController;
    private final MessageController messageController;
    private final RoomService roomService;
    private final ReservationService reservationService;
    private final UserService userService;
    private final Main app;
    private Stage stage;
    private VBox statsContainer; // Make this a field so we can refresh it

    public AdminDashboardView(Stage stage, UserController userController,
                              RoomService roomService,
                              ReservationService reservationService,
                              UserService userService,
                              Main app) {
        this.stage = stage;
        this.userController = userController;
        this.roomService = roomService;
        this.reservationService = reservationService;
        this.userService = userService;
        this.app = app;

        // Initialize controllers from services
        this.roomController = new RoomController(roomService);
        this.reservationController = new ReservationController(roomController);
        this.messageController = new MessageController();

        UserModel user = SessionManager.getInstance().getCurrentUser();
        if (user == null || user.getRole() != Role.ADMIN) {
            stage.close();
            new SignInView(new Stage(), userController, roomService, reservationService, userService, app);
            return;
        }

        initializeUI();
    }

    private void initializeUI() {
        // Main container with purple gradient background
        StackPane root = new StackPane();
        root.setStyle("-fx-background-color: linear-gradient(to bottom right, #8B7AB8, #B4A5D5);");

        // Center card container
        VBox centerCard = new VBox(30);
        centerCard.setMaxSize(700, 650);
        centerCard.setAlignment(Pos.TOP_CENTER);
        centerCard.setPadding(new Insets(40));
        centerCard.setStyle(
                "-fx-background-color: rgba(255, 255, 255, 0.25);" +
                        "-fx-background-radius: 30;" +
                        "-fx-border-color: rgba(255, 255, 255, 0.4);" +
                        "-fx-border-width: 1.5;" +
                        "-fx-border-radius: 30;"
        );

        // Add shadow effect
        DropShadow shadow = new DropShadow();
        shadow.setColor(Color.rgb(0, 0, 0, 0.2));
        shadow.setRadius(30);
        centerCard.setEffect(shadow);

        // Header with back button and title
        HBox header = createHeader();

        // Statistics title
        Label statsTitle = new Label("Statistics");
        statsTitle.setStyle(
                "-fx-font-size: 18px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-text-fill: rgba(255, 255, 255, 0.9);"
        );

        // Statistics cards container
        statsContainer = new VBox(15);
        statsContainer.setAlignment(Pos.CENTER);
        statsContainer.setPadding(new Insets(20, 0, 20, 0));

        // Initialize statistics
        refreshStatistics();

        // Bottom buttons
        HBox bottomButtons = new HBox(40);
        bottomButtons.setAlignment(Pos.CENTER);
        bottomButtons.setPadding(new Insets(20, 0, 0, 0));

        VBox chatButton = createBottomButton("💬", "Chat");
        VBox reservationButton = createBottomButton("📁", "Reservation");

        // Chat button action - opens messages view
        chatButton.setOnMouseClicked(e -> {
            openMessagesView();
        });

        // Reservation button action
        reservationButton.setOnMouseClicked(e -> {
            System.out.println("Opening reservations...");
            openAllReservationsView();
        });

        bottomButtons.getChildren().addAll(chatButton, reservationButton);

        // Sign Out button
        Button signOutButton = new Button("🚪 Sign Out");
        signOutButton.setStyle(
                "-fx-background-color: rgba(255, 255, 255, 0.9);" +
                        "-fx-text-fill: #333333;" +
                        "-fx-font-size: 14px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-padding: 12 30;" +
                        "-fx-background-radius: 20;" +
                        "-fx-cursor: hand;" +
                        "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 5, 0, 0, 2);"
        );
        signOutButton.setOnAction(e -> signOut());

        signOutButton.setOnMouseEntered(e ->
                signOutButton.setStyle(
                        "-fx-background-color: rgba(255, 255, 255, 1.0);" +
                                "-fx-text-fill: #333333;" +
                                "-fx-font-size: 14px;" +
                                "-fx-font-weight: bold;" +
                                "-fx-padding: 12 30;" +
                                "-fx-background-radius: 20;" +
                                "-fx-cursor: hand;" +
                                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 8, 0, 0, 3);"
                )
        );
        signOutButton.setOnMouseExited(e ->
                signOutButton.setStyle(
                        "-fx-background-color: rgba(255, 255, 255, 0.9);" +
                                "-fx-text-fill: #333333;" +
                                "-fx-font-size: 14px;" +
                                "-fx-font-weight: bold;" +
                                "-fx-padding: 12 30;" +
                                "-fx-background-radius: 20;" +
                                "-fx-cursor: hand;" +
                                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 5, 0, 0, 2);"
                )
        );

        HBox signOutContainer = new HBox(signOutButton);
        signOutContainer.setAlignment(Pos.CENTER);
        signOutContainer.setPadding(new Insets(10, 0, 0, 0));

        // Add all components to center card
        centerCard.getChildren().addAll(header, statsTitle, statsContainer, bottomButtons, signOutContainer);

        root.getChildren().add(centerCard);

        Scene scene = new Scene(root, 1000, 700);
        stage.setScene(scene);
        stage.setTitle("Admin Dashboard");
        stage.show();
    }

    private void refreshStatistics() {
        statsContainer.getChildren().clear();

        // Reload statistics from services
        int occupiedRooms = roomService.getOccupiedRoomsCount();
        int availableRooms = roomService.getAvailableRoomsCount();
        int totalReservations = reservationService.getAllReservations().size();
        int totalUsers = userController.getUserCount();

        HBox row1 = new HBox(20);
        row1.setAlignment(Pos.CENTER);

        VBox occupiedCard = createStatCard("Occupied Rooms", String.valueOf(occupiedRooms), "✓", "#9B8AC4");
        occupiedCard.setOnMouseClicked(e -> openOccupiedRoomsView());

        VBox availableCard = createStatCard("Available Rooms", String.valueOf(availableRooms), "🏠", "#B8A8D8");
        availableCard.setOnMouseClicked(e -> openAvailableRoomsView());

        row1.getChildren().addAll(occupiedCard, availableCard);

        HBox row2 = new HBox(20);
        row2.setAlignment(Pos.CENTER);

        VBox reservationsCard = createStatCard("Total Reservations", String.valueOf(totalReservations), "📋", "#9B8AC4");
        reservationsCard.setOnMouseClicked(e -> openAllReservationsView());

        VBox usersCard = createStatCard("Registered Users", String.valueOf(totalUsers), "👤", "#B8A8D8");
        usersCard.setOnMouseClicked(e -> openAllUsersView());

        row2.getChildren().addAll(reservationsCard, usersCard);

        statsContainer.getChildren().addAll(row1, row2);
    }

    private void openAvailableRoomsView() {
        stage.hide();
        Stage availableRoomsStage = new Stage();
        new AvailableRoomsView(availableRoomsStage, roomController, stage);

        // Refresh statistics when returning
        availableRoomsStage.setOnHidden(e -> {
            refreshStatistics();
            stage.show();
        });
    }

    private void openOccupiedRoomsView() {
        stage.hide();
        Stage occupiedRoomsStage = new Stage();
        new OccupiedRoomsView(occupiedRoomsStage, roomController, stage);

        // Refresh statistics when returning
        occupiedRoomsStage.setOnHidden(e -> {
            refreshStatistics();
            stage.show();
        });
    }

    private void openAllUsersView() {
        stage.hide();
        Stage allUsersStage = new Stage();
        new AllUsersView(allUsersStage, userController, stage);

        // Refresh statistics when returning
        allUsersStage.setOnHidden(e -> {
            refreshStatistics();
            stage.show();
        });
    }

    private void openAllReservationsView() {
        stage.hide();
        Stage allReservationsStage = new Stage();
        new AllReservationsView(allReservationsStage, reservationController, stage);

        // Refresh statistics when returning
        allReservationsStage.setOnHidden(e -> {
            refreshStatistics();
            stage.show();
        });
    }

    private void openMessagesView() {
        stage.hide();
        Stage messagesStage = new Stage();
        new AdminMessagesView(messagesStage, messageController, stage);

        // Refresh statistics when returning (in case messages affect anything)
        messagesStage.setOnHidden(e -> {
            refreshStatistics();
            stage.show();
        });
    }

    private void signOut() {
        // Clear the session
        SessionManager.getInstance().logout();

        // Show confirmation
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Signed Out");
        alert.setHeaderText(null);
        alert.setContentText("You have been successfully signed out.");
        alert.showAndWait();

        // Close current stage and return to sign in
        stage.close();
        new SignInView(new Stage(), userController, roomService,
                reservationService, userService, app);
    }

    private HBox createHeader() {
        HBox header = new HBox();
        header.setAlignment(Pos.CENTER);
        header.setPadding(new Insets(0, 0, 10, 0));
        header.setStyle(
                "-fx-background-color: rgba(255, 255, 255, 0.3);" +
                        "-fx-background-radius: 20;" +
                        "-fx-padding: 10 20;"
        );
        header.setMaxWidth(400);

        // Back button (refresh functionality)
        Button backButton = new Button("↻");
        backButton.setStyle(
                "-fx-background-color: transparent;" +
                        "-fx-text-fill: rgba(0, 0, 0, 0.6);" +
                        "-fx-font-size: 20px;" +
                        "-fx-cursor: hand;"
        );
        backButton.setOnAction(e -> {
            // Refresh statistics
            refreshStatistics();
        });

        // Title
        Label title = new Label("Admin Dashboard");
        title.setStyle(
                "-fx-font-size: 16px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-text-fill: rgba(0, 0, 0, 0.7);"
        );

        Region spacer1 = new Region();
        HBox.setHgrow(spacer1, Priority.ALWAYS);
        Region spacer2 = new Region();
        HBox.setHgrow(spacer2, Priority.ALWAYS);

        header.getChildren().addAll(backButton, spacer1, title, spacer2);

        return header;
    }

    private VBox createStatCard(String label, String value, String icon, String iconColor) {
        VBox card = new VBox(10);
        card.setAlignment(Pos.CENTER_LEFT);
        card.setPrefSize(280, 80);
        card.setPadding(new Insets(15, 20, 15, 20));
        card.setStyle(
                "-fx-background-color: rgba(255, 255, 255, 0.9);" +
                        "-fx-background-radius: 15;" +
                        "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 10, 0, 0, 5);"
        );

        HBox content = new HBox(15);
        content.setAlignment(Pos.CENTER_LEFT);

        // Icon circle
        Circle iconCircle = new Circle(20);
        iconCircle.setFill(Color.web(iconColor));
        StackPane iconContainer = new StackPane();
        Label iconLabel = new Label(icon);
        iconLabel.setStyle(
                "-fx-text-fill: white;" +
                        "-fx-font-size: 16px;"
        );
        iconContainer.getChildren().addAll(iconCircle, iconLabel);

        // Text content
        VBox textBox = new VBox(3);
        Label valueLabel = new Label(value);
        valueLabel.setStyle(
                "-fx-font-size: 20px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-text-fill: #333333;"
        );
        Label labelText = new Label(label);
        labelText.setStyle(
                "-fx-font-size: 12px;" +
                        "-fx-text-fill: #666666;"
        );
        textBox.getChildren().addAll(labelText, valueLabel);

        content.getChildren().addAll(iconContainer, textBox);
        card.getChildren().add(content);

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

    private VBox createBottomButton(String icon, String label) {
        VBox button = new VBox(8);
        button.setAlignment(Pos.CENTER);
        button.setPrefSize(120, 100);
        button.setStyle(
                "-fx-background-color: rgba(100, 100, 100, 0.4);" +
                        "-fx-background-radius: 20;" +
                        "-fx-cursor: hand;"
        );

        Label iconLabel = new Label(icon);
        iconLabel.setStyle(
                "-fx-font-size: 30px;" +
                        "-fx-text-fill: rgba(255, 255, 255, 0.9);"
        );

        Label textLabel = new Label(label);
        textLabel.setStyle(
                "-fx-font-size: 14px;" +
                        "-fx-text-fill: rgba(255, 255, 255, 0.9);"
        );

        button.getChildren().addAll(iconLabel, textLabel);

        // Hover effect
        button.setOnMouseEntered(e ->
                button.setStyle(
                        "-fx-background-color: rgba(120, 120, 120, 0.5);" +
                                "-fx-background-radius: 20;" +
                                "-fx-cursor: hand;"
                )
        );
        button.setOnMouseExited(e ->
                button.setStyle(
                        "-fx-background-color: rgba(100, 100, 100, 0.4);" +
                                "-fx-background-radius: 20;" +
                                "-fx-cursor: hand;"
                )
        );

        return button;
    }
}