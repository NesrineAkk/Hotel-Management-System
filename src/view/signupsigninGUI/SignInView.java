package view.signupsigninGUI;

import controller.UserController;
import controller.RoomController;
import controller.ReservationController;
import data.SessionManager;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import main.Main;
import model.services.ReservationService;
import model.services.RoomService;
import model.services.UserService;
import model.user.Role;
import model.user.UserModel;
import view.signupsigninGUI.statisticsViews.MainClientMenu;

public class SignInView {

    private final Main app;
    private final UserController controller;
    private final RoomService roomService;
    private final ReservationService reservationService;
    private final UserService userService;

    public SignInView(Stage stage, UserController controller,
                      RoomService roomService,
                      ReservationService reservationService,
                      UserService userService,
                      Main app) {
        this.app = app;
        this.controller = controller;
        this.roomService = roomService;
        this.reservationService = reservationService;
        this.userService = userService;

        // Main container with gradient background
        StackPane root = new StackPane();
        root.setStyle("-fx-background-color: linear-gradient(to bottom right, #8B7AB8, #B4A5D5);");

        // Center card container
        VBox centerCard = new VBox(25);
        centerCard.setMaxSize(400, 550);
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



        // Title
        Label title = new Label("Sign In");
        title.setStyle(
                "-fx-font-size: 28px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-text-fill: rgba(255, 255, 255, 0.95);"
        );

        // Avatar component
        AvatarComponent avatar = new AvatarComponent(70);

        // Email field
        TextField email = new TextField();
        email.setPromptText("Email");
        email.setStyle(
                "-fx-background-color: rgba(255, 255, 255, 0.9);" +
                        "-fx-background-radius: 15;" +
                        "-fx-padding: 12 20;" +
                        "-fx-font-size: 14px;" +
                        "-fx-prompt-text-fill: rgba(0, 0, 0, 0.4);"
        );
        email.setPrefWidth(320);

        // Password field
        PasswordField password = new PasswordField();
        password.setPromptText("Password");
        password.setStyle(
                "-fx-background-color: rgba(255, 255, 255, 0.9);" +
                        "-fx-background-radius: 15;" +
                        "-fx-padding: 12 20;" +
                        "-fx-font-size: 14px;" +
                        "-fx-prompt-text-fill: rgba(0, 0, 0, 0.4);"
        );
        password.setPrefWidth(320);

        // Login button
        Button loginBtn = new Button("Log in");
        loginBtn.setPrefWidth(320);
        loginBtn.setStyle(
                "-fx-background-color: rgba(255, 255, 255, 0.9);" +
                        "-fx-text-fill: #333333;" +
                        "-fx-font-size: 16px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-padding: 14 30;" +
                        "-fx-background-radius: 20;" +
                        "-fx-cursor: hand;" +
                        "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 5, 0, 0, 2);"
        );

        loginBtn.setOnMouseEntered(e ->
                loginBtn.setStyle(
                        "-fx-background-color: rgba(255, 255, 255, 1.0);" +
                                "-fx-text-fill: #333333;" +
                                "-fx-font-size: 16px;" +
                                "-fx-font-weight: bold;" +
                                "-fx-padding: 14 30;" +
                                "-fx-background-radius: 20;" +
                                "-fx-cursor: hand;" +
                                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 8, 0, 0, 3);"
                )
        );

        loginBtn.setOnMouseExited(e ->
                loginBtn.setStyle(
                        "-fx-background-color: rgba(255, 255, 255, 0.9);" +
                                "-fx-text-fill: #333333;" +
                                "-fx-font-size: 16px;" +
                                "-fx-font-weight: bold;" +
                                "-fx-padding: 14 30;" +
                                "-fx-background-radius: 20;" +
                                "-fx-cursor: hand;" +
                                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 5, 0, 0, 2);"
                )
        );

        loginBtn.setOnAction(e -> {
            UserModel user = controller.signIn(
                    email.getText(),
                    password.getText()
            );

            if (user == null) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Invalid email or password");
                alert.setHeaderText(null);
                alert.showAndWait();
                return;
            }

            SessionManager.getInstance().login(user);
            stage.close();

            if (user.getRole() == Role.ADMIN) {
                if (roomService != null && reservationService != null && userService != null) {
                    new AdminDashboardView(new Stage(), controller,
                            roomService, reservationService, userService, app);
                } else {
                    new Alert(Alert.AlertType.ERROR,
                            "Admin services not initialized. Please restart the application.").show();
                }
            } else {
                // Create MainClientMenu with all necessary services
                Stage menuStage = new Stage();
                RoomController roomController = new RoomController(roomService);
                ReservationController reservationController = new ReservationController(roomController);

                MainClientMenu mainClientMenu = new MainClientMenu(
                        menuStage,
                        roomController,
                        reservationController,
                        controller,
                        roomService,
                        reservationService,
                        userService,
                        app
                );
                mainClientMenu.show();
            }
        });

        // Sign up link
        Label signUpLink = new Label("You don't have an account? Sign up");
        signUpLink.setStyle(
                "-fx-text-fill: rgba(255, 255, 255, 0.9);" +
                        "-fx-underline: true;" +
                        "-fx-cursor: hand;" +
                        "-fx-font-size: 13px;"
        );
        signUpLink.setOnMouseClicked(e -> {
            stage.close();
            new SignUpView(new Stage(), controller, roomService,
                    reservationService, userService, app);
        });

        centerCard.getChildren().addAll(
                title, avatar, email, password, loginBtn, signUpLink
        );

        root.getChildren().add(centerCard);

        stage.setScene(new Scene(root, 1000, 700));
        stage.setTitle("Sign In");
        stage.show();
    }
}