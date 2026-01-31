package view.Reservation;

import view.signupsigninGUI.statisticsViews.MainClientMenu;
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
import controller.RoomController;
import controller.ReservationController;
import model.FilterStrategy.FeatureFilter;
import model.FilterStrategy.PriceFilter;
import model.FilterStrategy.RoomFilter;
import model.FilterStrategy.TypeFilter;
import model.room.RoomModel;

import java.util.ArrayList;
import java.util.List;

public class RoomFilterGui extends Application {

    private RoomController roomController;
    private ReservationController reservationController;
    private VBox roomListContainer;
    private Stage primaryStage;
    private Stage mainStage;
    private RoomModel selectedRoom;

    public RoomFilterGui() {
    }

    public RoomFilterGui(RoomController roomController, ReservationController reservationController) {
        this.roomController = roomController;
        this.reservationController = reservationController;

        System.out.println("RoomFilterGui constructor called");
        System.out.println("RoomController is null: " + (roomController == null));
        if (roomController != null) {
            System.out.println("Rooms in controller: " + roomController.getAllRooms().size());
        }
    }

    public void setMainStage(Stage mainStage) {
        this.mainStage = mainStage;
    }

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        showRoomFilterScreen();
        primaryStage.show();
    }

    public void showInStage(Stage stage) {
        this.primaryStage = stage;
        showRoomFilterScreen();
    }

    private void showRoomFilterScreen() {
        StackPane root = new StackPane();
        root.setStyle("-fx-background-color: linear-gradient(to bottom right, #8B7AB8, #B4A5D5);");

        // Initialize backend if needed
        if (roomController == null) {
            String[] possiblePaths = {
                    "src/data/room.json",
                    "data/room.json",
                    "./src/data/room.json",
                    "./data/room.json"
            };

            roomController = null;
            for (String path : possiblePaths) {
                try {
                    roomController = new RoomController(path);
                    if (roomController.getAllRooms().size() > 0) {
                        break;
                    }
                } catch (Exception e) {
                    System.out.println("Failed with path: " + path);
                }
            }

            if (roomController == null || roomController.getAllRooms().isEmpty()) {
                if (roomController == null) {
                    roomController = new RoomController("src/data/room.json");
                }
            }

            reservationController = new ReservationController(roomController);
        }

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

        // Header with back button
        HBox header = createHeader();

        // Title
        Label mainTitle = new Label("Available Rooms");
        mainTitle.setStyle(
                "-fx-font-size: 24px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-text-fill: rgba(255, 255, 255, 0.95);"
        );

        // Filter Bar
        HBox filterBar = createFilterBar();

        // Room List Container
        roomListContainer = new VBox(12);
        roomListContainer.setAlignment(Pos.TOP_CENTER);
        roomListContainer.setPadding(new Insets(15));
        loadRoomsFromBackend();

        ScrollPane scrollPane = new ScrollPane(roomListContainer);
        scrollPane.setFitToWidth(true);
        scrollPane.setPrefHeight(350);
        scrollPane.setStyle("-fx-background: transparent; -fx-background-color: transparent;");

        // Bottom buttons
        HBox buttonBox = new HBox(15);
        buttonBox.setAlignment(Pos.CENTER_RIGHT);
        buttonBox.setPadding(new Insets(10, 0, 0, 0));

        Button backBtn = createStyledButton("← Back");
        backBtn.setOnAction(e -> {
            MainClientMenu mainMenu = new MainClientMenu();
            mainMenu.showInStage(mainStage != null ? mainStage : primaryStage);
        });

        Button nextBtn = createStyledButton("Next →");
        nextBtn.setOnAction(e -> {
            if (selectedRoom != null) {
                proceedToReservation();
            } else {
                showAlert("Selection Required", "Please select a room before proceeding.");
            }
        });

        buttonBox.getChildren().addAll(backBtn, nextBtn);

        mainContainer.getChildren().addAll(header, mainTitle, filterBar, scrollPane, buttonBox);

        root.getChildren().add(mainContainer);

        Scene scene = new Scene(root, 1000, 700);
        primaryStage.setTitle("Book a Room");
        primaryStage.setScene(scene);
    }

    private HBox createHeader() {
        HBox header = new HBox();
        header.setAlignment(Pos.CENTER_LEFT);
        header.setPadding(new Insets(0, 0, 10, 0));

        Circle iconCircle = new Circle(20);
        iconCircle.setFill(Color.web("#9B8AC4"));
        StackPane iconContainer = new StackPane();
        Label iconLabel = new Label("🏠");
        iconLabel.setStyle("-fx-font-size: 18px;");
        iconContainer.getChildren().addAll(iconCircle, iconLabel);

        Label headerText = new Label("Room Selection");
        headerText.setStyle(
                "-fx-font-size: 16px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-text-fill: rgba(255, 255, 255, 0.9);"
        );
        HBox.setMargin(headerText, new Insets(0, 0, 0, 10));

        header.getChildren().addAll(iconContainer, headerText);
        return header;
    }

    private HBox createFilterBar() {
        HBox filterBar = new HBox(12);
        filterBar.setAlignment(Pos.CENTER);
        filterBar.setPadding(new Insets(15));
        filterBar.setStyle(
                "-fx-background-color: rgba(255, 255, 255, 0.3);" +
                        "-fx-background-radius: 20;" +
                        "-fx-padding: 12;"
        );

        ComboBox<String> typeCombo = new ComboBox<>();
        typeCombo.getItems().addAll("Type", "Single", "Double", "Triple", "Suite");
        typeCombo.setValue("Type");
        typeCombo.setPrefWidth(100);
        styleControl(typeCombo);

        TextField minPrice = new TextField();
        minPrice.setPromptText("Min Price");
        minPrice.setPrefWidth(90);
        styleControl(minPrice);

        TextField maxPrice = new TextField();
        maxPrice.setPromptText("Max Price");
        maxPrice.setPrefWidth(90);
        styleControl(maxPrice);

        CheckBox wifi = new CheckBox("wifi");
        CheckBox seaView = new CheckBox("Sea View");
        CheckBox balcony = new CheckBox("Balcony");
        CheckBox kitchen = new CheckBox("kitchen");
        CheckBox telephone = new CheckBox("telephone");
        CheckBox air_conditioning = new CheckBox("air conditioning");
        CheckBox jacuzzi = new CheckBox("jacuzzi");

        VBox servicesBox = new VBox(5, wifi, seaView, balcony, kitchen, telephone, air_conditioning, jacuzzi);
        servicesBox.setPadding(new Insets(10));
        servicesBox.setStyle("-fx-background-color: rgba(255, 255, 255, 0.9); -fx-background-radius: 10;");

        TitledPane servicesPane = new TitledPane("Services", servicesBox);
        servicesPane.setExpanded(false);
        servicesPane.setPrefWidth(120);
        servicesPane.setStyle("-fx-background-color: rgba(255, 255, 255, 0.9); -fx-background-radius: 10;");

        Button filterBtn = createStyledButton("Filter");
        filterBtn.setOnAction(e -> {
            List<RoomFilter> filters = new ArrayList<>();

            String selectedType = typeCombo.getValue();
            if (!selectedType.equals("Type")) {
                filters.add(new TypeFilter(selectedType));
            }

            String minPriceText = minPrice.getText();
            String maxPriceText = maxPrice.getText();
            int min = minPriceText.isEmpty() ? 0 : Integer.parseInt(minPriceText);
            int max = maxPriceText.isEmpty() ? Integer.MAX_VALUE : Integer.parseInt(maxPriceText);
            if (!minPriceText.isEmpty() || !maxPriceText.isEmpty()) {
                filters.add(new PriceFilter(min, max));
            }

            List<String> selectedFeatures = new ArrayList<>();
            if (wifi.isSelected()) selectedFeatures.add("wifi");
            if (seaView.isSelected()) selectedFeatures.add("Sea View");
            if (balcony.isSelected()) selectedFeatures.add("Balcony");
            if (kitchen.isSelected()) selectedFeatures.add("Kitchen");
            if (telephone.isSelected()) selectedFeatures.add("Telephone");
            if (air_conditioning.isSelected()) selectedFeatures.add("Air Conditioning");
            if (jacuzzi.isSelected()) selectedFeatures.add("Jacuzzi");

            if (!selectedFeatures.isEmpty()) {
                filters.add(new FeatureFilter(selectedFeatures, true));
            }

            List<RoomModel> filteredRooms = roomController.filterComposite(filters);
            roomListContainer.getChildren().clear();

            if (filteredRooms.isEmpty()) {
                Label noRoomLabel = new Label("No rooms match your criteria");
                noRoomLabel.setStyle(
                        "-fx-font-size: 16px;" +
                                "-fx-font-weight: bold;" +
                                "-fx-text-fill: rgba(255, 255, 255, 0.9);"
                );
                roomListContainer.getChildren().add(noRoomLabel);
            } else {
                for (RoomModel room : filteredRooms) {
                    addRoomCard(room);
                }
            }
        });

        Button resetBtn = createStyledButton("Reset");
        resetBtn.setOnAction(e -> {
            typeCombo.setValue("Type");
            minPrice.clear();
            maxPrice.clear();
            wifi.setSelected(false);
            seaView.setSelected(false);
            balcony.setSelected(false);
            kitchen.setSelected(false);
            telephone.setSelected(false);
            air_conditioning.setSelected(false);
            jacuzzi.setSelected(false);
            servicesPane.setExpanded(false);
            loadRoomsFromBackend();
        });

        filterBar.getChildren().addAll(typeCombo, minPrice, maxPrice, servicesPane, filterBtn, resetBtn);
        return filterBar;
    }

    private void styleControl(Control control) {
        control.setStyle(
                "-fx-background-color: rgba(255, 255, 255, 0.9);" +
                        "-fx-background-radius: 10;" +
                        "-fx-padding: 8 12;"
        );
    }

    private void loadRoomsFromBackend() {
        roomListContainer.getChildren().clear();
        List<RoomModel> allRooms = roomController.getAllRooms();

        if (allRooms.isEmpty()) {
            Label noRoomsLabel = new Label("No rooms available");
            noRoomsLabel.setStyle(
                    "-fx-font-size: 16px;" +
                            "-fx-text-fill: rgba(255, 255, 255, 0.9);"
            );
            roomListContainer.getChildren().add(noRoomsLabel);
        } else {
            for (RoomModel room : allRooms) {
                addRoomCard(room);
            }
        }
    }

    private void addRoomCard(RoomModel room) {
        HBox roomCard = new HBox(15);
        roomCard.setAlignment(Pos.CENTER_LEFT);
        roomCard.setPadding(new Insets(15, 20, 15, 20));
        roomCard.setPrefWidth(750);
        roomCard.setStyle(
                "-fx-background-color: rgba(255, 255, 255, 0.9);" +
                        "-fx-background-radius: 15;" +
                        "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 5, 0, 0, 2);"
        );

        // Room icon
        Circle iconCircle = new Circle(25);
        iconCircle.setFill(Color.web("#9B8AC4"));
        StackPane iconContainer = new StackPane();
        Label iconLabel = new Label("🛏️");
        iconLabel.setStyle("-fx-font-size: 20px;");
        iconContainer.getChildren().addAll(iconCircle, iconLabel);

        // Room info
        VBox infoBox = new VBox(5);
        Label roomTitle = new Label("Room " + room.getId() + " - " + room.getType());
        roomTitle.setStyle(
                "-fx-font-size: 16px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-text-fill: #333333;"
        );

        Label roomDetails = new Label(
                room.getPrice() + " DA/night | " + room.getState() + "\n" +
                        "Features: " + String.join(", ", room.getFeatures())
        );
        roomDetails.setStyle(
                "-fx-font-size: 12px;" +
                        "-fx-text-fill: #666666;"
        );

        infoBox.getChildren().addAll(roomTitle, roomDetails);
        HBox.setHgrow(infoBox, Priority.ALWAYS);

        // Reserve button
        Button reserveBtn = new Button("Reserve");
        reserveBtn.setStyle(
                "-fx-background-color: #9B8AC4;" +
                        "-fx-text-fill: white;" +
                        "-fx-font-size: 14px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-padding: 10 25;" +
                        "-fx-background-radius: 15;" +
                        "-fx-cursor: hand;"
        );

        if (room.getState().equalsIgnoreCase("Occupied")) {
            reserveBtn.setDisable(true);
            reserveBtn.setText("Occupied");
            reserveBtn.setStyle(
                    "-fx-background-color: #cccccc;" +
                            "-fx-text-fill: #666666;" +
                            "-fx-font-size: 14px;" +
                            "-fx-padding: 10 25;" +
                            "-fx-background-radius: 15;"
            );
        } else {
            reserveBtn.setOnAction(e -> {
                selectedRoom = room;
                proceedToReservation();
            });

            reserveBtn.setOnMouseEntered(e ->
                    reserveBtn.setStyle(
                            "-fx-background-color: #8775A3;" +
                                    "-fx-text-fill: white;" +
                                    "-fx-font-size: 14px;" +
                                    "-fx-font-weight: bold;" +
                                    "-fx-padding: 10 25;" +
                                    "-fx-background-radius: 15;" +
                                    "-fx-cursor: hand;"
                    )
            );

            reserveBtn.setOnMouseExited(e ->
                    reserveBtn.setStyle(
                            "-fx-background-color: #9B8AC4;" +
                                    "-fx-text-fill: white;" +
                                    "-fx-font-size: 14px;" +
                                    "-fx-font-weight: bold;" +
                                    "-fx-padding: 10 25;" +
                                    "-fx-background-radius: 15;" +
                                    "-fx-cursor: hand;"
                    )
            );
        }

        roomCard.getChildren().addAll(iconContainer, infoBox, reserveBtn);

        roomCard.setOnMouseEntered(e ->
                roomCard.setStyle(
                        "-fx-background-color: white;" +
                                "-fx-background-radius: 15;" +
                                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 10, 0, 0, 4);"
                )
        );

        roomCard.setOnMouseExited(e ->
                roomCard.setStyle(
                        "-fx-background-color: rgba(255, 255, 255, 0.9);" +
                                "-fx-background-radius: 15;" +
                                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 5, 0, 0, 2);"
                )
        );

        roomListContainer.getChildren().add(roomCard);
    }

    private void proceedToReservation() {
        MakeReservationView reservationView = new MakeReservationView(reservationController, roomController, selectedRoom);
        reservationView.setMainStage(mainStage != null ? mainStage : primaryStage);
        reservationView.showInStage(primaryStage);
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

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}