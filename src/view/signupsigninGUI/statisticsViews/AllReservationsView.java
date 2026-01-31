package view.signupsigninGUI.statisticsViews;

import controller.ReservationController;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import model.reservation.Reservations;

import java.util.List;
import java.util.Optional;

public class AllReservationsView {
    private final Stage stage;
    private final ReservationController reservationController;
    private final Stage parentStage;
    private VBox reservationsList; // Make this a field so we can refresh it

    public AllReservationsView(Stage stage, ReservationController reservationController, Stage parentStage) {
        this.stage = stage;
        this.reservationController = reservationController;
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

        Label title = new Label("All Reservations");
        title.setStyle(
                "-fx-font-size: 24px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-text-fill: rgba(255, 255, 255, 0.95);"
        );

        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background: transparent; -fx-background-color: transparent;");
        scrollPane.setPrefHeight(450);

        reservationsList = new VBox(15);
        reservationsList.setPadding(new Insets(10));
        reservationsList.setAlignment(Pos.TOP_CENTER);

        refreshReservationsList();

        scrollPane.setContent(reservationsList);

        mainContainer.getChildren().addAll(header, title, scrollPane);
        root.getChildren().add(mainContainer);

        Scene scene = new Scene(root, 1000, 700);
        stage.setScene(scene);
        stage.setTitle("All Reservations");
        stage.show();
    }

    private void refreshReservationsList() {
        reservationsList.getChildren().clear();

        List<Reservations> reservations = reservationController.getAllReservations();

        if (reservations.isEmpty()) {
            Label emptyLabel = new Label("No reservations found");
            emptyLabel.setStyle(
                    "-fx-font-size: 16px;" +
                            "-fx-text-fill: rgba(255, 255, 255, 0.8);"
            );
            reservationsList.getChildren().add(emptyLabel);
        } else {
            for (Reservations reservation : reservations) {
                reservationsList.getChildren().add(createReservationCard(reservation));
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

    private VBox createReservationCard(Reservations reservation) {
        VBox card = new VBox(10);
        card.setPadding(new Insets(20));
        card.setStyle(
                "-fx-background-color: rgba(255, 255, 255, 0.9);" +
                        "-fx-background-radius: 15;" +
                        "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 10, 0, 0, 5);"
        );
        card.setMaxWidth(800);

        HBox reservationHeader = new HBox(15);
        reservationHeader.setAlignment(Pos.CENTER_LEFT);

        Label reservationId = new Label("Reservation #" + reservation.getReservationId());
        reservationId.setStyle(
                "-fx-font-size: 18px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-text-fill: #333333;"
        );

        Label statusLabel = new Label(reservation.getStatus().toString().toUpperCase());
        String statusColor = getStatusColor(reservation.getStatus().toString());
        statusLabel.setStyle(
                "-fx-font-size: 12px;" +
                        "-fx-text-fill: #666666;" +
                        "-fx-background-color: " + statusColor + ";" +
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
        deleteButton.setOnAction(e -> handleDeleteReservation(reservation, card));

        reservationHeader.getChildren().addAll(reservationId, statusLabel, spacer, deleteButton);

        GridPane details = new GridPane();
        details.setHgap(20);
        details.setVgap(8);
        details.setPadding(new Insets(10, 0, 0, 0));

        addDetailRow(details, 0, "User ID:", reservation.getUserId());
        addDetailRow(details, 1, "Room ID:", reservation.getRoomId());
        addDetailRow(details, 2, "Check-In:", reservation.getCheckInDate().toString());
        addDetailRow(details, 3, "Check-Out:", reservation.getCheckOutDate().toString());
        addDetailRow(details, 4, "Total Price:", "$" + reservation.getTotalPrice());

        card.getChildren().addAll(reservationHeader, new Separator(), details);

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

    private void handleDeleteReservation(Reservations reservation, VBox card) {
        Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmAlert.setTitle("Delete Reservation");
        confirmAlert.setHeaderText("Are you sure you want to delete this reservation?");
        confirmAlert.setContentText("Reservation #" + reservation.getReservationId() +
                "\nThis action cannot be undone.");

        Optional<ButtonType> result = confirmAlert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            boolean success = reservationController.deleteReservation(reservation.getReservationId());

            if (success) {
                // Refresh the list to reflect the deletion
                refreshReservationsList();

                Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
                successAlert.setTitle("Success");
                successAlert.setHeaderText(null);
                successAlert.setContentText("Reservation deleted successfully!");
                successAlert.showAndWait();
            } else {
                Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                errorAlert.setTitle("Error");
                errorAlert.setHeaderText(null);
                errorAlert.setContentText("Failed to delete reservation. Please try again.");
                errorAlert.showAndWait();
            }
        }
    }

    private String getStatusColor(String status) {
        switch (status.toUpperCase()) {
            case "CONFIRMED":
                return "#C8E6C9";
            case "COMPLETED":
                return "#B3E5FC";
            case "CANCELLED":
                return "#FFCDD2";
            default:
                return "#E0E0E0";
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