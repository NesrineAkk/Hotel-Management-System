package view.signupsigninGUI;

import controller.UserController;
import data.SessionManager;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import main.Main;
import model.services.ReservationService;
import model.services.RoomService;
import model.services.UserService;
import model.user.Role;
import model.user.UserModel;

public class SignUpView {

    private final UserController controller;
    private final RoomService roomService;
    private final ReservationService reservationService;
    private final UserService userService;
    private final Main app;

    public SignUpView(Stage stage, UserController controller, Main app) {
        this.controller = controller;
        this.roomService = null;
        this.reservationService = null;
        this.userService = null;
        this.app = app;
        initializeView(stage);
    }

    public SignUpView(Stage stage, UserController controller,
                      RoomService roomService,
                      ReservationService reservationService,
                      UserService userService,
                      Main app) {
        this.controller = controller;
        this.roomService = roomService;
        this.reservationService = reservationService;
        this.userService = userService;
        this.app = app;
        initializeView(stage);
    }

    private void initializeView(Stage stage) {
        // Main container with gradient background
        StackPane root = new StackPane();
        root.setStyle("-fx-background-color: linear-gradient(to bottom right, #8B7AB8, #B4A5D5);");

        // Center card container - reduced spacing
        VBox centerCard = new VBox(10);
        centerCard.setMaxSize(450, 600);
        centerCard.setAlignment(Pos.CENTER);
        centerCard.setPadding(new Insets(30, 40, 30, 40));
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


        // Title - smaller font
        Label title = new Label("Create Account");
        title.setStyle(
                "-fx-font-size: 22px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-text-fill: rgba(255, 255, 255, 0.95);"
        );

        // Avatar - smaller size
        AvatarComponent avatar = new AvatarComponent(50);

        // Fields - reduced padding
        TextField firstNameField = createStyledTextField("First name");
        TextField lastNameField = createStyledTextField("Last name");
        TextField emailField = createStyledTextField("Email");
        TextField phoneField = createStyledTextField("Phone");
        PasswordField passField = createStyledPasswordField("Password");
        PasswordField confirmField = createStyledPasswordField("Confirm password");

        // Sign up button - reduced padding
        Button signUpBtn = new Button("Create account");
        signUpBtn.setPrefWidth(350);
        signUpBtn.setStyle(
                "-fx-background-color: rgba(255, 255, 255, 0.9);" +
                        "-fx-text-fill: #333333;" +
                        "-fx-font-size: 15px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-padding: 12 30;" +
                        "-fx-background-radius: 20;" +
                        "-fx-cursor: hand;" +
                        "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 5, 0, 0, 2);"
        );

        signUpBtn.setOnMouseEntered(e ->
                signUpBtn.setStyle(
                        "-fx-background-color: rgba(255, 255, 255, 1.0);" +
                                "-fx-text-fill: #333333;" +
                                "-fx-font-size: 15px;" +
                                "-fx-font-weight: bold;" +
                                "-fx-padding: 12 30;" +
                                "-fx-background-radius: 20;" +
                                "-fx-cursor: hand;" +
                                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 8, 0, 0, 3);"
                )
        );

        signUpBtn.setOnMouseExited(e ->
                signUpBtn.setStyle(
                        "-fx-background-color: rgba(255, 255, 255, 0.9);" +
                                "-fx-text-fill: #333333;" +
                                "-fx-font-size: 15px;" +
                                "-fx-font-weight: bold;" +
                                "-fx-padding: 12 30;" +
                                "-fx-background-radius: 20;" +
                                "-fx-cursor: hand;" +
                                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 5, 0, 0, 2);"
                )
        );

        signUpBtn.setOnAction(e -> {
            if (!passField.getText().equals(confirmField.getText())) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Passwords do not match");
                alert.setHeaderText(null);
                alert.showAndWait();
                return;
            }

            UserModel newUser = controller.signUp(
                    firstNameField.getText(),
                    lastNameField.getText(),
                    emailField.getText(),
                    passField.getText(),
                    phoneField.getText()
            );

            if (newUser != null) {
                SessionManager.getInstance().login(newUser);
                Alert alert = new Alert(Alert.AlertType.INFORMATION, "Account created successfully!");
                alert.setHeaderText(null);
                alert.showAndWait();
                stage.close();

                if (newUser.getRole() == Role.ADMIN) {
                    if (roomService != null && reservationService != null && userService != null) {
                        new AdminDashboardView(new Stage(), controller,
                                roomService, reservationService, userService, app);
                    } else {
                        new Alert(Alert.AlertType.WARNING,
                                "Admin dashboard unavailable. Services not initialized.").show();
                        new SignInView(new Stage(), controller, roomService,
                                reservationService, userService, app);
                    }
                } else {
                    app.showMainMenu();
                }
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Email already exists!");
                alert.setHeaderText(null);
                alert.showAndWait();
            }
        });

        // Sign in link - smaller font
        Label signInText = new Label("Already have an account? Sign in");
        signInText.setStyle(
                "-fx-text-fill: rgba(255, 255, 255, 0.9);" +
                        "-fx-underline: true;" +
                        "-fx-cursor: hand;" +
                        "-fx-font-size: 12px;"
        );
        signInText.setOnMouseClicked(e -> {
            stage.close();
            new SignInView(new Stage(), controller, roomService,
                    reservationService, userService, app);
        });

        centerCard.getChildren().addAll(
                title, avatar,
                firstNameField, lastNameField, emailField, phoneField,
                passField, confirmField,
                signUpBtn, signInText
        );

        root.getChildren().add(centerCard);

        stage.setScene(new Scene(root, 1000, 700));
        stage.setTitle("Sign Up");
        stage.show();
    }

    private TextField createStyledTextField(String prompt) {
        TextField field = new TextField();
        field.setPromptText(prompt);
        field.setStyle(
                "-fx-background-color: rgba(255, 255, 255, 0.9);" +
                        "-fx-background-radius: 15;" +
                        "-fx-padding: 10 20;" +
                        "-fx-font-size: 14px;" +
                        "-fx-prompt-text-fill: rgba(0, 0, 0, 0.4);"
        );
        field.setPrefWidth(350);
        return field;
    }

    private PasswordField createStyledPasswordField(String prompt) {
        PasswordField field = new PasswordField();
        field.setPromptText(prompt);
        field.setStyle(
                "-fx-background-color: rgba(255, 255, 255, 0.9);" +
                        "-fx-background-radius: 15;" +
                        "-fx-padding: 10 20;" +
                        "-fx-font-size: 14px;" +
                        "-fx-prompt-text-fill: rgba(0, 0, 0, 0.4);"
        );
        field.setPrefWidth(350);
        return field;
    }
}