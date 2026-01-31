package main;

import view.signupsigninGUI.statisticsViews.MainClientMenu;
import controller.ReservationController;
import controller.RoomController;
import controller.UserController;
import model.services.ReservationService;
import model.services.RoomService;
import model.services.UserService;
import javafx.application.Application;
import javafx.stage.Stage;
import view.signupsigninGUI.SignInView;

import java.io.File;
import java.util.Scanner;

public class Main extends Application {

    private static RoomController roomController;
    private static ReservationController reservationController;
    private static RoomService roomService;
    private static ReservationService reservationService;
    private static UserService userService;

    private Stage primaryStage;
    private MainClientMenu mainClientMenu;

    public static void main(String[] args) {
        // Debug: Print working directory
        System.out.println("Working Directory: " + System.getProperty("user.dir"));

        // Get the correct path relative to the project structure
        String projectPath = findProjectRoot();
        System.out.println("Project Root: " + projectPath);

        String roomJsonPath = projectPath + File.separator + "src" + File.separator + "data" + File.separator + "room.json";
        System.out.println("Looking for room.json at: " + roomJsonPath);

        File roomFile = new File(roomJsonPath);
        if (!roomFile.exists()) {
            System.err.println("ERROR: room.json not found at: " + roomJsonPath);
            System.err.println("File exists: " + roomFile.exists());
            System.err.println("Absolute path: " + roomFile.getAbsolutePath());
            return;
        }

        try {
            roomController = new RoomController(roomJsonPath);
            System.out.println("SUCCESS! Loaded " + roomController.getAllRooms().size() + " rooms");
        } catch (Exception e) {
            System.err.println("ERROR loading rooms: " + e.getMessage());
            e.printStackTrace();
            return;
        }

        reservationController = new ReservationController(roomController);

        // Ask user if they want GUI or Console mode
        Scanner in = new Scanner(System.in);
        System.out.println("=== HOTEL RESERVATION SYSTEM ===");
            System.out.println("Launching GUI mode...");
            launch(args);

    }

    private static String findProjectRoot() {
        // Start with current working directory
        File currentDir = new File(System.getProperty("user.dir"));

        // Check if we're already in the project root (has src folder)
        File srcFolder = new File(currentDir, "src");
        if (srcFolder.exists() && srcFolder.isDirectory()) {
            return currentDir.getAbsolutePath();
        }

        // Check if we're in a subfolder and need to go up
        // Look for Hotel-Reservation-System folder
        File parentDir = currentDir;
        for (int i = 0; i < 3; i++) { // Check up to 3 levels up
            File testSrc = new File(parentDir, "src");
            if (testSrc.exists() && testSrc.isDirectory()) {
                return parentDir.getAbsolutePath();
            }

            // Check if there's a Hotel-Reservation-System subfolder
            File hotelDir = new File(parentDir, "Hotel-Reservation-System");
            if (hotelDir.exists() && hotelDir.isDirectory()) {
                File hotelSrc = new File(hotelDir, "src");
                if (hotelSrc.exists() && hotelSrc.isDirectory()) {
                    return hotelDir.getAbsolutePath();
                }
            }

            parentDir = parentDir.getParentFile();
            if (parentDir == null) break;
        }

        return currentDir.getAbsolutePath();
    }

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;

        initializeServices();

        mainClientMenu = new MainClientMenu(primaryStage, roomController, reservationController);

        UserController controller = new UserController();

        new SignInView(primaryStage, controller, roomService,
                reservationService, userService, this);
    }

    private void initializeServices() {
        // Get the correct path
        String basePath = System.getProperty("user.dir");
        String roomPath = basePath + "/src/data/room.json";

        // Initialize services
        roomService = new RoomService(roomPath);
        reservationService = new ReservationService(roomController);
        userService = new UserService();

        System.out.println("✓ All services initialized successfully");
    }

    public void showMainMenu() {
        mainClientMenu.show();
    }
}