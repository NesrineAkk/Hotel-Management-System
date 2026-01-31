package view.Reservation;

import controller.ReservationController;
import controller.RoomController;
import controller.UserController;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import main.Main;
import model.reservation.Reservations;
import model.services.ReservationService;
import model.services.RoomService;
import model.services.UserService;
import view.signupsigninGUI.statisticsViews.MainClientMenu;

public class PaymentView extends Application {

    private Stage primaryStage;
    private Stage mainStage;
    private ReservationController reservationController;
    private RoomController roomController;
    private Reservations reservation;

    // Add these fields
    private UserController userController;
    private RoomService roomService;
    private ReservationService reservationService;
    private UserService userService;
    private Main app;

    private TextField cardNumberField;
    private TextField expiryField;
    private TextField cvvField;
    private TextField cardHolderField;

    public PaymentView() {
    }

    public PaymentView(ReservationController reservationController,
                       RoomController roomController,
                       Reservations reservation) {
        this.reservationController = reservationController;
        this.roomController = roomController;
        this.reservation = reservation;
    }

    // Add new constructor with services
    public PaymentView(ReservationController reservationController,
                       RoomController roomController,
                       Reservations reservation,
                       UserController userController,
                       RoomService roomService,
                       ReservationService reservationService,
                       UserService userService,
                       Main app) {
        this.reservationController = reservationController;
        this.roomController = roomController;
        this.reservation = reservation;
        this.userController = userController;
        this.roomService = roomService;
        this.reservationService = reservationService;
        this.userService = userService;
        this.app = app;
    }



    public void setMainStage(Stage mainStage) {
        this.mainStage = mainStage;
    }

    @Override
    public void start(Stage stage) {
        this.primaryStage = stage;
        primaryStage.setTitle("Payment");
        showPaymentScreen();
        primaryStage.show();
    }

    public void showInStage(Stage stage) {
        this.primaryStage = stage;
        this.mainStage = stage;
        showPaymentScreen();
    }

    private void showPaymentScreen() {
        StackPane root = new StackPane();
        root.setStyle("-fx-background-color: linear-gradient(to bottom right, #8B7AB8, #B4A5D5);");

        // Main container
        VBox mainContainer = new VBox(20);
        mainContainer.setMaxSize(650, 650);
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

        // Header
        HBox header = createHeader();

        // Title
        Label titleLabel = new Label("Complete Your Payment");
        titleLabel.setStyle(
                "-fx-font-size: 24px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-text-fill: rgba(255, 255, 255, 0.95);"
        );

        // Reservation summary
        VBox summaryBox = new VBox(10);
        summaryBox.setPadding(new Insets(15));
        summaryBox.setStyle(
                "-fx-background-color: rgba(255, 255, 255, 0.3);" +
                        "-fx-background-radius: 15;"
        );

        HBox summaryHeader = new HBox(10);
        summaryHeader.setAlignment(Pos.CENTER_LEFT);

        Circle iconCircle = new Circle(15);
        iconCircle.setFill(Color.web("#9B8AC4"));
        StackPane iconContainer = new StackPane();
        Label iconLabel = new Label("📋");
        iconLabel.setStyle("-fx-font-size: 14px;");
        iconContainer.getChildren().addAll(iconCircle, iconLabel);

        Label summaryLabel = new Label("Reservation Summary");
        summaryLabel.setStyle(
                "-fx-font-size: 16px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-text-fill: rgba(255, 255, 255, 0.95);"
        );

        summaryHeader.getChildren().addAll(iconContainer, summaryLabel);

        Label detailsLabel = new Label();
        if (reservation != null) {
            detailsLabel.setText(String.format(
                    "Reservation ID: %s\n" +
                            "Room: %s\n" +
                            "Check-in: %s\n" +
                            "Check-out: %s\n" +
                            "Total Amount: %.2f DA",
                    reservation.getReservationId(),
                    reservation.getRoomId(),
                    reservation.getCheckInDate(),
                    reservation.getCheckOutDate(),
                    reservation.getTotalPrice()
            ));
        } else {
            detailsLabel.setText("No reservation details available");
        }
        detailsLabel.setStyle(
                "-fx-font-size: 13px;" +
                        "-fx-text-fill: rgba(255, 255, 255, 0.85);"
        );

        summaryBox.getChildren().addAll(summaryHeader, detailsLabel);

        // Payment fields
        VBox paymentFields = new VBox(12);
        paymentFields.setAlignment(Pos.CENTER);

        Label paymentLabel = new Label("Payment Information");
        paymentLabel.setStyle(
                "-fx-font-size: 16px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-text-fill: rgba(255, 255, 255, 0.95);"
        );

        cardHolderField = createStyledTextField("Cardholder Name");
        cardNumberField = createStyledTextField("Card Number (16 digits)");

        HBox expiryAndCvv = new HBox(10);
        expiryField = createStyledTextField("MM/YY");
        expiryField.setPrefWidth(280);
        cvvField = createStyledTextField("CVV");
        cvvField.setPrefWidth(280);
        expiryAndCvv.getChildren().addAll(expiryField, cvvField);

        paymentFields.getChildren().addAll(paymentLabel, cardHolderField, cardNumberField, expiryAndCvv);

        // Buttons
        HBox buttonBox = new HBox(15);
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.setPadding(new Insets(15, 0, 0, 0));

        Button backBtn = createStyledButton("← Back");
        backBtn.setOnAction(e -> {
            Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
            confirm.setTitle("Cancel Payment");
            confirm.setHeaderText("Are you sure you want to cancel this payment?");
            confirm.setContentText("Your reservation will be cancelled.");

            confirm.showAndWait().ifPresent(response -> {
                if (response == ButtonType.OK) {
                    redirectToMainClientMenu();
                }
            });
        });

        Button confirmBtn = createStyledButton("Confirm Payment →");
        confirmBtn.setOnAction(e -> processPayment());

        buttonBox.getChildren().addAll(backBtn, confirmBtn);

        mainContainer.getChildren().addAll(header, titleLabel, summaryBox, paymentFields, buttonBox);

        root.getChildren().add(mainContainer);

        Scene scene = new Scene(root, 1000, 700);
        primaryStage.setScene(scene);
    }

    private HBox createHeader() {
        HBox header = new HBox();
        header.setAlignment(Pos.CENTER_LEFT);
        header.setPadding(new Insets(0, 0, 10, 0));

        Circle iconCircle = new Circle(20);
        iconCircle.setFill(Color.web("#9B8AC4"));
        StackPane iconContainer = new StackPane();
        Label iconLabel = new Label("💳");
        iconLabel.setStyle("-fx-font-size: 18px;");
        iconContainer.getChildren().addAll(iconCircle, iconLabel);

        Label headerText = new Label("Payment");
        headerText.setStyle(
                "-fx-font-size: 16px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-text-fill: rgba(255, 255, 255, 0.9);"
        );
        HBox.setMargin(headerText, new Insets(0, 0, 0, 10));

        header.getChildren().addAll(iconContainer, headerText);
        return header;
    }

    private void processPayment() {
        if (cardHolderField.getText().trim().isEmpty() ||
                cardNumberField.getText().trim().isEmpty() ||
                expiryField.getText().trim().isEmpty() ||
                cvvField.getText().trim().isEmpty()) {
            showAlert("Error", "Please fill in all payment information.", Alert.AlertType.ERROR);
            return;
        }

        String cardNumber = cardNumberField.getText().replaceAll("\\s+", "");
        if (!cardNumber.matches("\\d{16}")) {
            showAlert("Error", "Please enter a valid 16-digit card number.", Alert.AlertType.ERROR);
            return;
        }

        if (!expiryField.getText().matches("\\d{2}/\\d{2}")) {
            showAlert("Error", "Please enter expiry date in MM/YY format.", Alert.AlertType.ERROR);
            return;
        }

        if (!cvvField.getText().matches("\\d{3,4}")) {
            showAlert("Error", "Please enter a valid CVV (3 or 4 digits).", Alert.AlertType.ERROR);
            return;
        }

        if (reservation == null) {
            showAlert("Error", "No reservation found.", Alert.AlertType.ERROR);
            return;
        }

        String paymentMethod = "Credit Card (" + cardNumber.substring(0, 4) + "****)";
        boolean success = reservationController.confirmReservation(
                reservation.getReservationId(),
                paymentMethod
        );

        if (success) {
            showSuccessDialog();
        } else {
            showAlert("Error", "Payment failed. Please try again.", Alert.AlertType.ERROR);
        }
    }

    private void showSuccessDialog() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Payment Successful");
        alert.setHeaderText("🎉 Reservation Confirmed!");

        String content = String.format(
                "Your payment has been processed successfully.\n\n" +
                        "Reservation Details:\n" +
                        "Reservation ID: %s\n" +
                        "Room: %s\n" +
                        "Check-in: %s\n" +
                        "Check-out: %s\n\n" +
                        "A confirmation email will be sent shortly.",
                reservation.getReservationId(),
                reservation.getRoomId(),
                reservation.getCheckInDate(),
                reservation.getCheckOutDate()
        );

        alert.setContentText(content);

        alert.showAndWait().ifPresent(response -> {
            redirectToMainClientMenu();
        });
    }

    private void redirectToMainClientMenu() {
        // Create new stage for MainClientMenu
        Stage menuStage = new Stage();
        MainClientMenu mainClientMenu;

        if (userController != null && roomService != null &&
                reservationService != null && userService != null && app != null) {
            // Create with all services
            mainClientMenu = new MainClientMenu(
                    menuStage,
                    roomController,
                    reservationController,
                    userController,
                    roomService,
                    reservationService,
                    userService,
                    app
            );
        } else {
            // Fallback to basic constructor
            mainClientMenu = new MainClientMenu(
                    menuStage,
                    roomController,
                    reservationController
            );
        }

        mainClientMenu.show();

        // Close the payment stage
        primaryStage.close();

        // Close main stage if it exists
        if (mainStage != null && mainStage != primaryStage) {
            mainStage.close();
        }
    }

    private TextField createStyledTextField(String prompt) {
        TextField field = new TextField();
        field.setPromptText(prompt);
        field.setStyle(
                "-fx-background-color: rgba(255, 255, 255, 0.9);" +
                        "-fx-background-radius: 12;" +
                        "-fx-padding: 12 15;" +
                        "-fx-font-size: 13px;"
        );
        field.setPrefWidth(580);
        return field;
    }

    private Button createStyledButton(String text) {
        Button btn = new Button(text);
        btn.setStyle(
                "-fx-background-color: rgba(255, 255, 255, 0.9);" +
                        "-fx-text-fill: #333333;" +
                        "-fx-font-size: 14px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-padding: 12 30;" +
                        "-fx-background-radius: 15;" +
                        "-fx-cursor: hand;"
        );

        btn.setOnMouseEntered(e ->
                btn.setStyle(
                        "-fx-background-color: white;" +
                                "-fx-text-fill: #333333;" +
                                "-fx-font-size: 14px;" +
                                "-fx-font-weight: bold;" +
                                "-fx-padding: 12 30;" +
                                "-fx-background-radius: 15;" +
                                "-fx-cursor: hand;"
                )
        );

        btn.setOnMouseExited(e ->
                btn.setStyle(
                        "-fx-background-color: rgba(255, 255, 255, 0.9);" +
                                "-fx-text-fill: #333333;" +
                                "-fx-font-size: 14px;" +
                                "-fx-font-weight: bold;" +
                                "-fx-padding: 12 30;" +
                                "-fx-background-radius: 15;" +
                                "-fx-cursor: hand;"
                )
        );

        return btn;
    }

    private void showAlert(String title, String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }
}