package view.Reservation;

import controller.ReservationController;
import controller.RoomController;
import data.SessionManager;
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
import model.room.RoomModel;
import model.reservation.Reservations;
import model.user.UserModel;

import java.time.LocalDate;

public class MakeReservationView extends Application {

    private Stage primaryStage;
    private Stage mainStage;
    private ReservationController reservationController;
    private RoomController roomController;
    private RoomModel selectedRoom;

    private TextField firstNameField;
    private TextField lastNameField;
    private TextField emailField;
    private DatePicker checkInDatePicker;
    private DatePicker checkOutDatePicker;
    private ToggleButton restaurantBtn;
    private ToggleButton spaBtn;
    private ToggleButton parkingBtn;

    public MakeReservationView() {
    }

    public MakeReservationView(ReservationController reservationController, RoomController roomController) {
        this.reservationController = reservationController;
        this.roomController = roomController;
    }

    public MakeReservationView(ReservationController reservationController, RoomController roomController, RoomModel selectedRoom) {
        this.reservationController = reservationController;
        this.roomController = roomController;
        this.selectedRoom = selectedRoom;
    }

    public void setMainStage(Stage mainStage) {
        this.mainStage = mainStage;
    }

    @Override
    public void start(Stage stage) {
        this.primaryStage = stage;
        primaryStage.setTitle("Order Confirmation");
        showOrderConfirmationScreen();
        primaryStage.show();
    }

    public void showInStage(Stage stage) {
        this.primaryStage = stage;
        this.mainStage = stage;
        showOrderConfirmationScreen();
    }

    private void showOrderConfirmationScreen() {
        StackPane root = new StackPane();
        root.setStyle("-fx-background-color: linear-gradient(to bottom right, #8B7AB8, #B4A5D5);");

        // Main container
        VBox mainContainer = new VBox(20);
        mainContainer.setMaxSize(750, 650);
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
        Label title = new Label("Order Confirmation");
        title.setStyle(
                "-fx-font-size: 24px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-text-fill: rgba(255, 255, 255, 0.95);"
        );

        // Scrollable content
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setFitToWidth(true);
        scrollPane.setPrefHeight(420);
        scrollPane.setStyle("-fx-background: transparent; -fx-background-color: transparent;");

        VBox contentPanel = new VBox(18);
        contentPanel.setAlignment(Pos.TOP_CENTER);
        contentPanel.setPadding(new Insets(20));

        // Selected room info
        if (selectedRoom != null) {
            VBox roomInfoBox = new VBox(10);
            roomInfoBox.setPadding(new Insets(15));
            roomInfoBox.setStyle(
                    "-fx-background-color: rgba(255, 255, 255, 0.3);" +
                            "-fx-background-radius: 15;"
            );

            HBox roomHeader = new HBox(10);
            roomHeader.setAlignment(Pos.CENTER_LEFT);

            Circle iconCircle = new Circle(15);
            iconCircle.setFill(Color.web("#9B8AC4"));
            StackPane iconContainer = new StackPane();
            Label iconLabel = new Label("🛏️");
            iconLabel.setStyle("-fx-font-size: 14px;");
            iconContainer.getChildren().addAll(iconCircle, iconLabel);

            Label roomInfoTitle = new Label("Selected Room");
            roomInfoTitle.setStyle(
                    "-fx-font-size: 16px;" +
                            "-fx-font-weight: bold;" +
                            "-fx-text-fill: rgba(255, 255, 255, 0.95);"
            );

            roomHeader.getChildren().addAll(iconContainer, roomInfoTitle);

            Label roomDetails = new Label(
                    "Room " + selectedRoom.getId() + " - " + selectedRoom.getType() + "\n" +
                            "Price: " + selectedRoom.getPrice() + " DA/night\n" +
                            "Features: " + String.join(", ", selectedRoom.getFeatures())
            );
            roomDetails.setStyle(
                    "-fx-font-size: 13px;" +
                            "-fx-text-fill: rgba(255, 255, 255, 0.85);"
            );

            roomInfoBox.getChildren().addAll(roomHeader, roomDetails);
            contentPanel.getChildren().add(roomInfoBox);
        }

        // Personal info section
        VBox personalInfo = new VBox(12);
        firstNameField = createStyledTextField("First name");
        lastNameField = createStyledTextField("Last name");
        emailField = createStyledTextField("Email");

        checkInDatePicker = new DatePicker();
        checkInDatePicker.setPromptText("Check-in date");
        styleControl(checkInDatePicker);
        checkInDatePicker.setDayCellFactory(picker -> new DateCell() {
            @Override
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                setDisable(empty || date.isBefore(LocalDate.now()));
            }
        });

        checkOutDatePicker = new DatePicker();
        checkOutDatePicker.setPromptText("Check-out date");
        styleControl(checkOutDatePicker);
        checkOutDatePicker.setDayCellFactory(picker -> new DateCell() {
            @Override
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                LocalDate checkIn = checkInDatePicker.getValue();
                setDisable(empty || (checkIn != null && date.isBefore(checkIn.plusDays(1))));
            }
        });

        personalInfo.getChildren().addAll(
                firstNameField, lastNameField, emailField,
                checkInDatePicker, checkOutDatePicker
        );

        // Services section
        VBox servicesSection = new VBox(12);
        Label servicesLabel = new Label("Additional Services");
        servicesLabel.setStyle(
                "-fx-font-size: 16px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-text-fill: rgba(255, 255, 255, 0.95);"
        );

        HBox serviceButtons = new HBox(10);
        restaurantBtn = createServiceToggleButton("🍽️ Restaurant");
        spaBtn = createServiceToggleButton("💆 Spa");
        parkingBtn = createServiceToggleButton("🅿️ Parking");
        serviceButtons.getChildren().addAll(restaurantBtn, spaBtn, parkingBtn);

        servicesSection.getChildren().addAll(servicesLabel, serviceButtons);

        contentPanel.getChildren().addAll(personalInfo, servicesSection);
        scrollPane.setContent(contentPanel);

        // Buttons
        HBox buttonBox = new HBox(15);
        buttonBox.setAlignment(Pos.CENTER_RIGHT);
        buttonBox.setPadding(new Insets(10, 0, 0, 0));

        Button backBtn = createStyledButton("← Back");
        backBtn.setOnAction(e -> {
            RoomFilterGui roomFilter = new RoomFilterGui(roomController, reservationController);
            roomFilter.setMainStage(mainStage != null ? mainStage : primaryStage);
            roomFilter.showInStage(primaryStage);
        });

        Button confirmBtn = createStyledButton("Confirm →");
        confirmBtn.setOnAction(e -> confirmReservation());

        buttonBox.getChildren().addAll(backBtn, confirmBtn);

        mainContainer.getChildren().addAll(header, title, scrollPane, buttonBox);

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
        Label iconLabel = new Label("📝");
        iconLabel.setStyle("-fx-font-size: 18px;");
        iconContainer.getChildren().addAll(iconCircle, iconLabel);

        Label headerText = new Label("Reservation Details");
        headerText.setStyle(
                "-fx-font-size: 16px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-text-fill: rgba(255, 255, 255, 0.9);"
        );
        HBox.setMargin(headerText, new Insets(0, 0, 0, 10));

        header.getChildren().addAll(iconContainer, headerText);
        return header;
    }

    private void confirmReservation() {
        if (firstNameField.getText().trim().isEmpty() ||
                lastNameField.getText().trim().isEmpty() ||
                emailField.getText().trim().isEmpty()) {
            showAlert("Error", "Please fill in all personal information.");
            return;
        }

        if (checkInDatePicker.getValue() == null || checkOutDatePicker.getValue() == null) {
            showAlert("Error", "Please select check-in and check-out dates.");
            return;
        }

        if (selectedRoom == null) {
            showAlert("Error", "No room selected. Please go back and select a room.");
            return;
        }

        java.util.List<String> selectedServices = new java.util.ArrayList<>();
        if (restaurantBtn.isSelected()) selectedServices.add("Restaurant");
        if (spaBtn.isSelected()) selectedServices.add("Spa");
        if (parkingBtn.isSelected()) selectedServices.add("Parking");

        String customerName = firstNameField.getText().trim() + " " + lastNameField.getText().trim();
        String email = emailField.getText().trim();
        String checkIn = checkInDatePicker.getValue().toString();
        String checkOut = checkOutDatePicker.getValue().toString();

        try {
            UserModel currentUser = SessionManager.getInstance().getCurrentUser();

            Reservations reservation = reservationController.createReservation(
                    currentUser,
                    selectedRoom.getId(),
                    checkIn,
                    checkOut,
                    selectedServices
            );

            if (reservation != null) {
                proceedToPayment(reservation);
            } else {
                showAlert("Error", "Failed to create reservation. Please try again.");
            }
        } catch (Exception ex) {
            showAlert("Error", "An error occurred: " + ex.getMessage());
        }
    }

    private void proceedToPayment(Reservations reservation) {
        PaymentView paymentView = new PaymentView(reservationController, roomController, reservation);
        paymentView.setMainStage(mainStage != null ? mainStage : primaryStage);
        paymentView.showInStage(primaryStage);
    }

    private TextField createStyledTextField(String prompt) {
        TextField field = new TextField();
        field.setPromptText(prompt);
        styleControl(field);
        return field;
    }

    private void styleControl(Control control) {
        control.setStyle(
                "-fx-background-color: rgba(255, 255, 255, 0.9);" +
                        "-fx-background-radius: 12;" +
                        "-fx-padding: 12 15;"
        );
        control.setPrefWidth(650);
    }

    private ToggleButton createServiceToggleButton(String text) {
        ToggleButton btn = new ToggleButton(text);
        btn.setStyle(
                "-fx-background-color: rgba(255, 255, 255, 0.3);" +
                        "-fx-text-fill: rgba(255, 255, 255, 0.95);" +
                        "-fx-background-radius: 12;" +
                        "-fx-padding: 10 18;" +
                        "-fx-font-size: 13px;" +
                        "-fx-cursor: hand;"
        );

        btn.selectedProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal) {
                btn.setStyle(
                        "-fx-background-color: rgba(255, 255, 255, 0.9);" +
                                "-fx-text-fill: #333333;" +
                                "-fx-background-radius: 12;" +
                                "-fx-padding: 10 18;" +
                                "-fx-font-size: 13px;" +
                                "-fx-cursor: hand;"
                );
            } else {
                btn.setStyle(
                        "-fx-background-color: rgba(255, 255, 255, 0.3);" +
                                "-fx-text-fill: rgba(255, 255, 255, 0.95);" +
                                "-fx-background-radius: 12;" +
                                "-fx-padding: 10 18;" +
                                "-fx-font-size: 13px;" +
                                "-fx-cursor: hand;"
                );
            }
        });

        return btn;
    }

    private Button createStyledButton(String text) {
        Button btn = new Button(text);
        btn.setStyle(
                "-fx-background-color: rgba(255, 255, 255, 0.9);" +
                        "-fx-text-fill: #333333;" +
                        "-fx-font-size: 14px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-padding: 10 25;" +
                        "-fx-background-radius: 15;" +
                        "-fx-cursor: hand;"
        );

        btn.setOnMouseEntered(e ->
                btn.setStyle(
                        "-fx-background-color: white;" +
                                "-fx-text-fill: #333333;" +
                                "-fx-font-size: 14px;" +
                                "-fx-font-weight: bold;" +
                                "-fx-padding: 10 25;" +
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
                                "-fx-padding: 10 25;" +
                                "-fx-background-radius: 15;" +
                                "-fx-cursor: hand;"
                )
        );

        return btn;
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showMainMenuAgain() {
        StackPane root = new StackPane();
        root.setStyle("-fx-background-color: linear-gradient(to bottom right, #8B7AB8, #B4A5D5);");

        VBox centerCard = new VBox(25);
        centerCard.setMaxSize(600, 650);
        centerCard.setAlignment(Pos.CENTER);
        centerCard.setPadding(new Insets(40));
        centerCard.setStyle(
                "-fx-background-color: rgba(255, 255, 255, 0.25);" +
                        "-fx-background-radius: 30;" +
                        "-fx-border-color: rgba(255, 255, 255, 0.4);" +
                        "-fx-border-width: 1.5;" +
                        "-fx-border-radius: 30;"
        );

        Label title = new Label("Hotel Reservation System");
        title.setStyle(
                "-fx-font-size: 26px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-text-fill: rgba(255, 255, 255, 0.95);"
        );

        Button viewReservationsBtn = createMainMenuButton("View My Reservations");
        viewReservationsBtn.setOnAction(e -> {
            UserModel currentUser = SessionManager.getInstance().getCurrentUser();
            if (currentUser == null) {
                new Alert(Alert.AlertType.ERROR, "No user is currently logged in!").show();
                return;
            }
            CurrentReservationsView view = new CurrentReservationsView(reservationController, currentUser);
            view.setMainStage(mainStage);
            view.showInStage(mainStage);
        });

        Button makeReservationBtn = createMainMenuButton("Make a Reservation");
        makeReservationBtn.setOnAction(e -> {
            RoomFilterGui roomFilter = new RoomFilterGui(roomController, reservationController);
            roomFilter.setMainStage(mainStage);
            roomFilter.showInStage(primaryStage);
        });

        Button exitBtn = createMainMenuButton("Exit");
        exitBtn.setOnAction(e -> primaryStage.close());

        centerCard.getChildren().addAll(title, viewReservationsBtn, makeReservationBtn, exitBtn);
        root.getChildren().add(centerCard);

        Scene scene = new Scene(root, 1000, 700);
        primaryStage.setScene(scene);
    }

    private Button createMainMenuButton(String text) {
        Button btn = new Button(text);
        btn.setPrefWidth(400);
        btn.setPrefHeight(55);
        btn.setStyle(
                "-fx-background-color: rgba(255, 255, 255, 0.9);" +
                        "-fx-text-fill: #333333;" +
                        "-fx-font-size: 16px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-background-radius: 20;" +
                        "-fx-cursor: hand;"
        );
        return btn;
    }

    public static void main(String[] args) {
        launch(args);
    }
}