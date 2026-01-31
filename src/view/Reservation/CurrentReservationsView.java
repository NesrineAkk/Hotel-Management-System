package view.Reservation;

import view.signupsigninGUI.statisticsViews.MainClientMenu;
import controller.ReservationController;
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
import model.reservation.Reservations;
import model.user.UserModel;

import java.util.List;

public class CurrentReservationsView extends Application {

    private Stage primaryStage;
    private Stage mainStage;
    private ReservationController reservationController;
    private final UserModel user;

    public CurrentReservationsView(ReservationController reservationController, UserModel user) {
        this.reservationController = reservationController;
        this.user = user;
    }

    public void setMainStage(Stage mainStage) {
        this.mainStage = mainStage;
    }

    @Override
    public void start(Stage stage) {
        this.primaryStage = stage;
        primaryStage.setTitle("My Reservations");
        showReservationsScreen();
        primaryStage.show();
    }

    public void showInStage(Stage stage) {
        this.primaryStage = stage;
        this.mainStage = stage;
        showReservationsScreen();
    }

    private void showReservationsScreen() {
        StackPane root = new StackPane();
        root.setStyle("-fx-background-color: linear-gradient(to bottom right, #8B7AB8, #B4A5D5);");

        // Main container
        VBox mainContainer = new VBox(20);
        mainContainer.setMaxSize(850, 650);
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
        Label title = new Label("My Reservations");
        title.setStyle(
                "-fx-font-size: 24px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-text-fill: rgba(255, 255, 255, 0.95);"
        );

        // Subtitle
        Label subtitle = new Label("View and manage your hotel reservations");
        subtitle.setStyle(
                "-fx-font-size: 14px;" +
                        "-fx-text-fill: rgba(255, 255, 255, 0.8);"
        );

        // Reservations list
        VBox reservationsList = new VBox(12);
        reservationsList.setAlignment(Pos.CENTER);
        reservationsList.setPadding(new Insets(15));

        List<Reservations> reservations = reservationController != null ?
                reservationController.getUserReservations(user.getId()) : null;

        if (reservations != null && !reservations.isEmpty()) {
            for (Reservations reservation : reservations) {
                HBox reservationCard = createReservationCard(reservation);
                reservationsList.getChildren().add(reservationCard);
            }
        } else {
            VBox emptyState = new VBox(15);
            emptyState.setAlignment(Pos.CENTER);
            emptyState.setPadding(new Insets(40));

            Label emptyIcon = new Label("📋");
            emptyIcon.setStyle("-fx-font-size: 50px;");

            Label emptyLabel = new Label("No reservations found");
            emptyLabel.setStyle(
                    "-fx-font-size: 18px;" +
                            "-fx-font-weight: bold;" +
                            "-fx-text-fill: rgba(255, 255, 255, 0.9);"
            );

            Label emptySubtext = new Label("Make your first reservation to get started");
            emptySubtext.setStyle(
                    "-fx-font-size: 14px;" +
                            "-fx-text-fill: rgba(255, 255, 255, 0.7);"
            );

            emptyState.getChildren().addAll(emptyIcon, emptyLabel, emptySubtext);
            reservationsList.getChildren().add(emptyState);
        }

        ScrollPane scrollPane = new ScrollPane(reservationsList);
        scrollPane.setFitToWidth(true);
        scrollPane.setPrefHeight(400);
        scrollPane.setStyle("-fx-background: transparent; -fx-background-color: transparent;");

        // Back button
        Button backBtn = createStyledButton("← Back to Menu");
        backBtn.setOnAction(e -> {
            MainClientMenu mainMenu = new MainClientMenu();
            mainMenu.showInStage(mainStage != null ? mainStage : primaryStage);
        });

        HBox buttonBox = new HBox(backBtn);
        buttonBox.setAlignment(Pos.CENTER_RIGHT);
        buttonBox.setPadding(new Insets(10, 0, 0, 0));

        mainContainer.getChildren().addAll(header, title, subtitle, scrollPane, buttonBox);

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
        Label iconLabel = new Label("📋");
        iconLabel.setStyle("-fx-font-size: 18px;");
        iconContainer.getChildren().addAll(iconCircle, iconLabel);

        Label headerText = new Label("Reservation History");
        headerText.setStyle(
                "-fx-font-size: 16px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-text-fill: rgba(255, 255, 255, 0.9);"
        );
        HBox.setMargin(headerText, new Insets(0, 0, 0, 10));

        header.getChildren().addAll(iconContainer, headerText);
        return header;
    }

    private HBox createReservationCard(Reservations reservation) {
        HBox card = new HBox(15);
        card.setAlignment(Pos.CENTER_LEFT);
        card.setPadding(new Insets(15, 20, 15, 20));
        card.setPrefWidth(750);
        card.setStyle(
                "-fx-background-color: rgba(255, 255, 255, 0.9);" +
                        "-fx-background-radius: 15;" +
                        "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 5, 0, 0, 2);"
        );

        // Status indicator
        Circle statusCircle = new Circle(8);
        if (reservation.isActive()) {
            statusCircle.setFill(Color.web("#4CAF50"));
        } else {
            statusCircle.setFill(Color.web("#9E9E9E"));
        }

        // Reservation info
        VBox infoBox = new VBox(5);
        Label roomLabel = new Label("Room " + reservation.getRoomId() + " - " + reservation.getStatus());
        roomLabel.setStyle(
                "-fx-font-size: 16px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-text-fill: #333333;"
        );

        Label detailsLabel = new Label(
                "Check-in: " + reservation.getCheckInDate() + " | " +
                        reservation.getNumberOfNights() + " nights | " +
                        String.format("%.2f DA", reservation.getTotalPrice())
        );
        detailsLabel.setStyle(
                "-fx-font-size: 12px;" +
                        "-fx-text-fill: #666666;"
        );

        infoBox.getChildren().addAll(roomLabel, detailsLabel);
        HBox.setHgrow(infoBox, Priority.ALWAYS);

        // View details button
        Button viewBtn = new Button("View Details");
        viewBtn.setStyle(
                "-fx-background-color: #9B8AC4;" +
                        "-fx-text-fill: white;" +
                        "-fx-font-size: 13px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-padding: 8 20;" +
                        "-fx-background-radius: 12;" +
                        "-fx-cursor: hand;"
        );

        viewBtn.setOnMouseEntered(e ->
                viewBtn.setStyle(
                        "-fx-background-color: #8775A3;" +
                                "-fx-text-fill: white;" +
                                "-fx-font-size: 13px;" +
                                "-fx-font-weight: bold;" +
                                "-fx-padding: 8 20;" +
                                "-fx-background-radius: 12;" +
                                "-fx-cursor: hand;"
                )
        );

        viewBtn.setOnMouseExited(e ->
                viewBtn.setStyle(
                        "-fx-background-color: #9B8AC4;" +
                                "-fx-text-fill: white;" +
                                "-fx-font-size: 13px;" +
                                "-fx-font-weight: bold;" +
                                "-fx-padding: 8 20;" +
                                "-fx-background-radius: 12;" +
                                "-fx-cursor: hand;"
                )
        );

        viewBtn.setOnAction(e -> showReservationDetails(reservation));

        card.getChildren().addAll(statusCircle, infoBox, viewBtn);

        card.setOnMouseEntered(e ->
                card.setStyle(
                        "-fx-background-color: white;" +
                                "-fx-background-radius: 15;" +
                                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 10, 0, 0, 4);"
                )
        );

        card.setOnMouseExited(e ->
                card.setStyle(
                        "-fx-background-color: rgba(255, 255, 255, 0.9);" +
                                "-fx-background-radius: 15;" +
                                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 5, 0, 0, 2);"
                )
        );

        return card;
    }

    private void showReservationDetails(Reservations reservation) {
        if (reservation == null) {
            showAlert("No Details", "No reservation details available.");
            return;
        }

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Reservation Details");
        alert.setHeaderText("Reservation #" + reservation.getReservationId());

        String details = String.format(
                "Room ID: %s\n" +
                        "Check-in: %s\n" +
                        "Check-out: %s\n" +
                        "Nights: %d\n" +
                        "Total Price: $%.2f\n" +
                        "Status: %s",
                reservation.getRoomId(),
                reservation.getCheckInDate(),
                reservation.getCheckOutDate(),
                reservation.getNumberOfNights(),
                reservation.getTotalPrice(),
                reservation.getStatus()
        );

        alert.setContentText(details);

        ButtonType cancelBtn = new ButtonType("Cancel Reservation");
        ButtonType completeBtn = new ButtonType("Complete Reservation");
        ButtonType closeBtn = new ButtonType("Close", ButtonBar.ButtonData.CANCEL_CLOSE);

        if (reservation.isActive()) {
            alert.getButtonTypes().setAll(cancelBtn, completeBtn, closeBtn);
        } else {
            alert.getButtonTypes().setAll(closeBtn);
        }

        alert.showAndWait().ifPresent(response -> {
            if (response == cancelBtn) {
                boolean success = reservationController.cancelReservation(reservation.getReservationId());
                if (success) {
                    showAlert("Success", "Reservation cancelled successfully!");
                    showReservationsScreen();
                } else {
                    showAlert("Error", "Failed to cancel reservation.");
                }
            } else if (response == completeBtn) {
                boolean success = reservationController.completeReservation(reservation.getReservationId());
                if (success) {
                    showAlert("Success", "Reservation completed successfully!");
                    showReservationsScreen();
                } else {
                    showAlert("Error", "Failed to complete reservation.");
                }
            }
        });
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private Button createStyledButton(String text) {
        Button btn = new Button(text);
        btn.setStyle(
                "-fx-background-color: rgba(255, 255, 255, 0.9);" +
                        "-fx-text-fill: #333333;" +
                        "-fx-font-size: 14px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-padding: 10 20;" +
                        "-fx-background-radius: 15;" +
                        "-fx-cursor: hand;"
        );

        btn.setOnMouseEntered(e ->
                btn.setStyle(
                        "-fx-background-color: white;" +
                                "-fx-text-fill: #333333;" +
                                "-fx-font-size: 14px;" +
                                "-fx-font-weight: bold;" +
                                "-fx-padding: 10 20;" +
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
                                "-fx-padding: 10 20;" +
                                "-fx-background-radius: 15;" +
                                "-fx-cursor: hand;"
                )
        );

        return btn;
    }

    public static void main(String[] args) {
        launch(args);
    }
}