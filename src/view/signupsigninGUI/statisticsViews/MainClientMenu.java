package view.signupsigninGUI.statisticsViews;

import view.Reservation.CurrentReservationsView;
import view.Reservation.RoomFilterGui;
import view.Message.ContactAdminView;
import view.signupsigninGUI.SignInView;

import controller.MessageController;
import controller.ReservationController;
import controller.RoomController;
import controller.UserController;

import data.SessionManager;

import model.user.UserModel;

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

public class MainClientMenu {

    private Stage primaryStage;
    private RoomController roomController;
    private ReservationController reservationController;
    private MessageController messageController;
    private Scene mainMenuScene;

    private UserController userController;
    private RoomService roomService;
    private ReservationService reservationService;
    private UserService userService;
    private Main app;

 public MainClientMenu(Stage primaryStage, RoomController roomController,
                          ReservationController reservationController) {
        this.primaryStage = primaryStage;
        this.roomController = roomController;
        this.reservationController = reservationController;
        this.messageController = new MessageController();
    }

    public MainClientMenu(Stage primaryStage, RoomController roomController,
                          ReservationController reservationController,
                          UserController userController,
                          RoomService roomService,
                          ReservationService reservationService,
                          UserService userService,
                          Main app) {
        this.primaryStage = primaryStage;
        this.roomController = roomController;
        this.reservationController = reservationController;
        this.messageController = new MessageController();
        this.userController = userController;
        this.roomService = roomService;
        this.reservationService = reservationService;
        this.userService = userService;
        this.app = app;
    }

    public MainClientMenu() {
        this.roomController = new RoomController("src/data/room.json");
        this.reservationController = new ReservationController(roomController);
        this.messageController = new MessageController();
    }

    public void show() {
        primaryStage.setTitle("Hotel Reservation System - Client Menu");

        StackPane root = new StackPane();
        root.setStyle("-fx-background-color: linear-gradient(to bottom right, #8B7AB8, #B4A5D5);");

        VBox centerCard = new VBox(25);
        centerCard.setMaxSize(600, 550);
        centerCard.setAlignment(Pos.CENTER);
        centerCard.setPadding(new Insets(40));
        centerCard.setStyle(
                "-fx-background-color: rgba(255, 255, 255, 0.25);" +
                "-fx-background-radius: 30;" +
                "-fx-border-color: rgba(255, 255, 255, 0.4);" +
                "-fx-border-width: 1.5;" +
                "-fx-border-radius: 30;"
        );

        DropShadow shadow = new DropShadow();
        shadow.setColor(Color.rgb(0, 0, 0, 0.2));
        shadow.setRadius(30);
        centerCard.setEffect(shadow);

        Circle iconCircle = new Circle(40);
        iconCircle.setFill(Color.web("#9B8AC4"));
        Label iconLabel = new Label("🏨");
        iconLabel.setStyle("-fx-font-size: 35px;");
        StackPane iconContainer = new StackPane(iconCircle, iconLabel);

        Label title = new Label("Hotel Reservation System");
        title.setStyle(
                "-fx-font-size: 26px;" +
                "-fx-font-weight: bold;" +
                "-fx-text-fill: rgba(255, 255, 255, 0.95);"
        );

        UserModel currentUser = SessionManager.getInstance().getCurrentUser();
        Label subtitle = new Label(
                currentUser != null
                        ? "Welcome, " + currentUser.getFirstName() + "!"
                        : "Welcome!"
        );
        subtitle.setStyle(
                "-fx-font-size: 16px;" +
                "-fx-text-fill: rgba(255, 255, 255, 0.8);"
        );

        Button viewReservationsBtn = createMenuButton("📋  View My Reservations");
        viewReservationsBtn.setOnAction(e -> showCurrentReservations());

        Button makeReservationBtn = createMenuButton("🏠  Make a Reservation");
        makeReservationBtn.setOnAction(e -> showRoomFilter());

        Button contactAdminBtn = createMenuButton("💬  Contact Admin");
        contactAdminBtn.setOnAction(e -> showContactAdmin());

        Button signOutBtn = createMenuButton("🚪  Sign Out");
        signOutBtn.setOnAction(e -> signOut());

        centerCard.getChildren().addAll(
                iconContainer,
                title,
                subtitle,
                viewReservationsBtn,
                makeReservationBtn,
                contactAdminBtn,
                signOutBtn
        );

        root.getChildren().add(centerCard);

        mainMenuScene = new Scene(root, 1000, 700);
        primaryStage.setScene(mainMenuScene);
        primaryStage.show();
    }

    public void returnToMainMenu() {
        if (mainMenuScene != null) {
            primaryStage.setScene(mainMenuScene);
             primaryStage.setTitle("Hotel Reservation System - Main Menu");
        } else {
            show();
        }
    }

    private void showCurrentReservations() {
        UserModel user = SessionManager.getInstance().getCurrentUser();
        if (user == null) {
            showAlert("Error", "No user is logged in.");
            return;
        }

        CurrentReservationsView view =
                new CurrentReservationsView(reservationController, user);
        view.setMainStage(primaryStage);
        view.showInStage(primaryStage);
    }
     public void showInStage(Stage stage) {
        this.primaryStage = stage;
        show();
    }
   private void showRoomFilter() {
        RoomFilterGui view;

        if (userController != null && roomService != null &&
                reservationService != null && userService != null && app != null) {
            // Pass all services
            view = new RoomFilterGui();
        } else {
            // Fallback to basic constructor
            view = new RoomFilterGui(roomController, reservationController);
        }

        view.setMainStage(primaryStage);
        view.showInStage(primaryStage);
    }

    private void showContactAdmin() {
        UserModel user = SessionManager.getInstance().getCurrentUser();
        if (user == null) {
            showAlert("Error", "Please log in first.");
            return;
        }

        ContactAdminView contactView =
                new ContactAdminView(primaryStage, messageController);
        contactView.setMainMenu(this);
        contactView.show();
    }

    private Button createMenuButton(String text) {
        Button btn = new Button(text);
        btn.setPrefWidth(400);
        btn.setPrefHeight(55);
        btn.setStyle(
                "-fx-background-color: rgba(255, 255, 255, 0.9);" +
                        "-fx-text-fill: #333333;" +
                        "-fx-font-size: 16px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-background-radius: 20;" +
                        "-fx-cursor: hand;" +
                        "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 5, 0, 0, 2);"
        );

        btn.setOnMouseEntered(e ->
                btn.setStyle(
                        "-fx-background-color: rgba(255, 255, 255, 1.0);" +
                                "-fx-text-fill: #333333;" +
                                "-fx-font-size: 16px;" +
                                "-fx-font-weight: bold;" +
                                "-fx-background-radius: 20;" +
                                "-fx-cursor: hand;" +
                                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 8, 0, 0, 3);"
                )
        );

        btn.setOnMouseExited(e ->
                btn.setStyle(
                        "-fx-background-color: rgba(255, 255, 255, 0.9);" +
                                "-fx-text-fill: #333333;" +
                                "-fx-font-size: 16px;" +
                                "-fx-font-weight: bold;" +
                                "-fx-background-radius: 20;" +
                                "-fx-cursor: hand;" +
                                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 5, 0, 0, 2);"
                )
        );

        return btn;
    }

    // ✅ SAME SIGN-OUT LOGIC AS ADMIN
    private void signOut() {
        SessionManager.getInstance().logout();

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Signed Out");
        alert.setHeaderText(null);
        alert.setContentText("You have been successfully signed out.");
        alert.showAndWait();

        primaryStage.close();

        new SignInView(
                new Stage(),
                userController,
                roomService,
                reservationService,
                userService,
                app
        );
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}


/*package view;

import view.Reservation.CurrentReservationsView;
import view.Reservation.RoomFilterGui;
import view.Message.ContactAdminView;
import controller.MessageController;
import controller.ReservationController;
import controller.RoomController;
import controller.UserController;
import data.SessionManager;
import model.user.UserModel;
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

public class MainClientMenu {

    private Stage primaryStage;
    private RoomController roomController;
    private ReservationController reservationController;
    private MessageController messageController;
    private Scene mainMenuScene;
    private UserController userController;
    private RoomService roomService;
    private ReservationService reservationService;
    private UserService userService;
    private Main app;
     private Stage stage;

    public MainClientMenu(Stage primaryStage, RoomController roomController,
                          ReservationController reservationController) {
        this.primaryStage = primaryStage;
        this.roomController = roomController;
        this.reservationController = reservationController;
        this.messageController = new MessageController();
    }

    public MainClientMenu(Stage primaryStage, RoomController roomController,
                          ReservationController reservationController,
                          UserController userController,
                          RoomService roomService,
                          ReservationService reservationService,
                          UserService userService,
                          Main app) {
        this.primaryStage = primaryStage;
        this.roomController = roomController;
        this.reservationController = reservationController;
        this.messageController = new MessageController();
        this.userController = userController;
        this.roomService = roomService;
        this.reservationService = reservationService;
        this.userService = userService;
        this.app = app;
    }

    public MainClientMenu() {
        this.roomController = new RoomController("src/data/room.json");
        this.reservationController = new ReservationController(roomController);
        this.messageController = new MessageController();
    }

    public void show() {
        primaryStage.setTitle("Hotel Reservation System - Main Menu");

        // Main container with gradient background
        StackPane root = new StackPane();
        root.setStyle("-fx-background-color: linear-gradient(to bottom right, #8B7AB8, #B4A5D5);");

        // Center card container
        VBox centerCard = new VBox(25);
        centerCard.setMaxSize(600, 550);
        centerCard.setAlignment(Pos.CENTER);
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

        // Hotel icon
        Circle iconCircle = new Circle(40);
        iconCircle.setFill(Color.web("#9B8AC4"));
        StackPane iconContainer = new StackPane();
        Label iconLabel = new Label("🏨");
        iconLabel.setStyle("-fx-font-size: 35px;");
        iconContainer.getChildren().addAll(iconCircle, iconLabel);

        // Title
        Label title = new Label("Hotel Reservation System");
        title.setStyle(
                "-fx-font-size: 26px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-text-fill: rgba(255, 255, 255, 0.95);"
        );

        // Subtitle
        UserModel currentUser = SessionManager.getInstance().getCurrentUser();
        String welcomeText = currentUser != null ?
                "Welcome, " + currentUser.getFirstName() + "!" : "Welcome!";
        Label subtitle = new Label(welcomeText);
        subtitle.setStyle(
                "-fx-font-size: 16px;" +
                        "-fx-text-fill: rgba(255, 255, 255, 0.8);"
        );

        // Menu buttons
        Button viewReservationsBtn = createMenuButton("📋  View My Reservations");
        viewReservationsBtn.setOnAction(e -> showCurrentReservations());

        Button makeReservationBtn = createMenuButton("🏠  Make a Reservation");
        makeReservationBtn.setOnAction(e -> showRoomFilter());

        Button contactAdminBtn = createMenuButton("💬  Contact Admin");
        contactAdminBtn.setOnAction(e -> showContactAdmin());


        Button exitBtn = createMenuButton("Exit");
        exitBtn.setOnAction(e -> primaryStage.close());

        centerCard.getChildren().addAll(
                iconContainer, title, subtitle,
                viewReservationsBtn, makeReservationBtn,
                contactAdminBtn, exitBtn
        );

        root.getChildren().add(centerCard);

        mainMenuScene = new Scene(root, 1000, 700);
        primaryStage.setScene(mainMenuScene);
        primaryStage.show();
    }

    public void returnToMainMenu() {
        if (mainMenuScene != null) {
            primaryStage.setScene(mainMenuScene);
            primaryStage.setTitle("Hotel Reservation System - Main Menu");
        } else {
            show();
        }
    }

    private void showCurrentReservations() {
        UserModel currentUser = SessionManager.getInstance().getCurrentUser();
        if (currentUser == null) {
            showAlert("Error", "No user is currently logged in.");
            return;
        }
        CurrentReservationsView view = new CurrentReservationsView(reservationController, currentUser);
        view.setMainStage(primaryStage);
        view.showInStage(primaryStage);
    }

    public void showInStage(Stage stage) {
        this.primaryStage = stage;
        show();
    }

    private void showRoomFilter() {
        RoomFilterGui view;

        if (userController != null && roomService != null &&
                reservationService != null && userService != null && app != null) {
            // Pass all services
            view = new RoomFilterGui();
        } else {
            // Fallback to basic constructor
            view = new RoomFilterGui(roomController, reservationController);
        }

        view.setMainStage(primaryStage);
        view.showInStage(primaryStage);
    }

    private void showContactAdmin() {
        UserModel currentUser = SessionManager.getInstance().getCurrentUser();
        if (currentUser == null) {
            showAlert("Error", "Please log in to contact admin.");
            return;
        }

        ContactAdminView contactView;

            contactView = new ContactAdminView(
                    primaryStage,
                    messageController
            );
        contactView.setMainMenu(this);
        contactView.show();
    }



    private Button createMenuButton(String text) {
        Button btn = new Button(text);
        btn.setPrefWidth(400);
        btn.setPrefHeight(55);
        btn.setStyle(
                "-fx-background-color: rgba(255, 255, 255, 0.9);" +
                        "-fx-text-fill: #333333;" +
                        "-fx-font-size: 16px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-background-radius: 20;" +
                        "-fx-cursor: hand;" +
                        "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 5, 0, 0, 2);"
        );

        btn.setOnMouseEntered(e ->
                btn.setStyle(
                        "-fx-background-color: rgba(255, 255, 255, 1.0);" +
                                "-fx-text-fill: #333333;" +
                                "-fx-font-size: 16px;" +
                                "-fx-font-weight: bold;" +
                                "-fx-background-radius: 20;" +
                                "-fx-cursor: hand;" +
                                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 8, 0, 0, 3);"
                )
        );

        btn.setOnMouseExited(e ->
                btn.setStyle(
                        "-fx-background-color: rgba(255, 255, 255, 0.9);" +
                                "-fx-text-fill: #333333;" +
                                "-fx-font-size: 16px;" +
                                "-fx-font-weight: bold;" +
                                "-fx-background-radius: 20;" +
                                "-fx-cursor: hand;" +
                                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 5, 0, 0, 2);"
                )
        );

        return btn;
    }
    // sign out 
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

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}*/