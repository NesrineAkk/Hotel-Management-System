package view.signupsigninGUI.statisticsViews;

import controller.RoomController;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import model.room.RoomModel;

import java.util.List;

public class AvailableRoomsView {
    private final Stage stage;
    private final RoomController roomController;
    private final Stage parentStage;

    public AvailableRoomsView(Stage stage, RoomController roomController, Stage parentStage) {
        this.stage = stage;
        this.roomController = roomController;
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

        Label title = new Label("Available Rooms");
        title.setStyle(
                "-fx-font-size: 24px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-text-fill: rgba(255, 255, 255, 0.95);"
        );

        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background: transparent; -fx-background-color: transparent;");
        scrollPane.setPrefHeight(450);

        VBox roomsList = new VBox(15);
        roomsList.setPadding(new Insets(10));
        roomsList.setAlignment(Pos.TOP_CENTER);

        List<RoomModel> availableRooms = roomController.filterByState("available");

        if (availableRooms.isEmpty()) {
            Label emptyLabel = new Label("No available rooms at the moment");
            emptyLabel.setStyle(
                    "-fx-font-size: 16px;" +
                            "-fx-text-fill: rgba(255, 255, 255, 0.8);"
            );
            roomsList.getChildren().add(emptyLabel);
        } else {
            for (RoomModel room : availableRooms) {
                roomsList.getChildren().add(createRoomCard(room));
            }
        }

        scrollPane.setContent(roomsList);

        mainContainer.getChildren().addAll(header, title, scrollPane);
        root.getChildren().add(mainContainer);

        Scene scene = new Scene(root, 1000, 700);
        stage.setScene(scene);
        stage.setTitle("Available Rooms");
        stage.show();
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

    private VBox createRoomCard(RoomModel room) {
        VBox card = new VBox(10);
        card.setPadding(new Insets(20));
        card.setStyle(
                "-fx-background-color: rgba(255, 255, 255, 0.9);" +
                        "-fx-background-radius: 15;" +
                        "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 10, 0, 0, 5);"
        );
        card.setMaxWidth(800);

        HBox roomHeader = new HBox(15);
        roomHeader.setAlignment(Pos.CENTER_LEFT);

        Label roomNumber = new Label("Room " + room.getNumber());
        roomNumber.setStyle(
                "-fx-font-size: 20px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-text-fill: #333333;"
        );

        String typeText = room.getType() != null ? room.getType().toUpperCase() : "STANDARD";
        Label roomType = new Label(typeText);
        roomType.setStyle(
                "-fx-font-size: 12px;" +
                        "-fx-text-fill: #666666;" +
                        "-fx-background-color: #E8E0F5;" +
                        "-fx-padding: 5 10;" +
                        "-fx-background-radius: 10;"
        );

        roomHeader.getChildren().addAll(roomNumber, roomType);

        GridPane details = new GridPane();
        details.setHgap(20);
        details.setVgap(8);
        details.setPadding(new Insets(10, 0, 0, 0));

        addDetailRow(details, 0, "Price:", "$" + room.getPrice() + "/night");
        addDetailRow(details, 2, "Status:", room.getState() != null ? room.getState() : "available");

        if (room.getFeatures() != null && !room.getFeatures().isEmpty()) {
            Label featuresLabel = new Label("Features:");
            featuresLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: #555555;");

            Label featuresValue = new Label(String.join(", ", room.getFeatures()));
            featuresValue.setStyle("-fx-text-fill: #666666;");
            featuresValue.setWrapText(true);
            featuresValue.setMaxWidth(600);

            details.add(featuresLabel, 0, 3);
            details.add(featuresValue, 1, 3);
        }

        card.getChildren().addAll(roomHeader, new Separator(), details);

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

    private void addDetailRow(GridPane grid, int row, String label, String value) {
        Label labelNode = new Label(label);
        labelNode.setStyle("-fx-font-weight: bold; -fx-text-fill: #555555;");

        Label valueNode = new Label(value);
        valueNode.setStyle("-fx-text-fill: #666666;");

        grid.add(labelNode, 0, row);
        grid.add(valueNode, 1, row);
    }
}