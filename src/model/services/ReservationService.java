package model.services;

import model.reservation.Reservations;
import model.room.RoomModel;
import model.pricing.PricingStrategyContext;
import model.pricing.NormalPricing;
import model.pricing.DiscountPricing;
import data.DataManager;
import com.google.gson.reflect.TypeToken;
import model.user.UserModel;
import controller.RoomController;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ReservationService {
    private List<Reservations> reservations;
    private PricingStrategyContext pricingContext;
    private DataManager<Reservations> dataManager;
    private RoomController roomController;

    public ReservationService(RoomController roomController) {
        this.reservations = new ArrayList<>();
        this.pricingContext = new PricingStrategyContext();
        this.roomController = roomController;

        String basePath = System.getProperty("user.dir");
        String reservationPath = basePath + "/src/data/reservation.json";
        this.dataManager = new DataManager<>(reservationPath, new TypeToken<ArrayList<Reservations>>(){}.getType());
        this.reservations = dataManager.load();
    }

    public Reservations createReservation(String userId, String roomId,
                                          LocalDate checkIn, LocalDate checkOut,
                                          boolean applyDiscount) {
        reloadReservations(); // Ensure we have fresh data

        RoomModel room = getRoomById(roomId);
        if (room == null || !isValidDateRange(checkIn, checkOut) || !isRoomAvailable(roomId, checkIn, checkOut)
                || !room.getState().equalsIgnoreCase("available")) {
            return null;
        }

        pricingContext.setStrategy(applyDiscount ? new DiscountPricing(10) : new NormalPricing());
        long nights = java.time.temporal.ChronoUnit.DAYS.between(checkIn, checkOut);
        double totalPrice = pricingContext.calculatePrice(room.getPrice(), nights);

        String reservationId = generateReservationId();
        Reservations reservation = new Reservations(reservationId, userId, roomId, checkIn, checkOut, totalPrice, "pending");

        reservations.add(reservation);
        saveReservations();
        return reservation;
    }

    public Reservations createReservation(UserModel user, int roomId, String checkInStr,
                                          String checkOutStr, List<String> services) {
        try {
            reloadReservations(); // Ensure we have fresh data

            LocalDate checkIn = LocalDate.parse(checkInStr);
            LocalDate checkOut = LocalDate.parse(checkOutStr);
            String roomIdStr = String.valueOf(roomId);
            RoomModel room = getRoomById(roomIdStr);

            if (room == null || !isValidDateRange(checkIn, checkOut) || !isRoomAvailable(roomIdStr, checkIn, checkOut)
                    || !room.getState().equalsIgnoreCase("available")) {
                return null;
            }

            pricingContext.setStrategy(new NormalPricing());
            long nights = java.time.temporal.ChronoUnit.DAYS.between(checkIn, checkOut);
            double roomPrice = pricingContext.calculatePrice(room.getPrice(), nights);
            double servicesCost = calculateServicesCost(services, nights);
            double totalPrice = roomPrice + servicesCost;

            String reservationId = generateReservationId();
            Reservations reservation = new Reservations(reservationId, user.getId(), roomIdStr, checkIn, checkOut, totalPrice, "pending");

            reservations.add(reservation);
            saveReservations();
            return reservation;
        } catch (Exception e) {
            return null;
        }
    }

    private double calculateServicesCost(List<String> services, long nights) {
        if (services == null || services.isEmpty()) return 0.0;
        double totalServiceCost = 0.0;
        for (String service : services) {
            switch (service.toLowerCase()) {
                case "restaurant": totalServiceCost += 50.0 * nights; break;
                case "spa": totalServiceCost += 100.0 * nights; break;
                case "parking": totalServiceCost += 30.0 * nights; break;
            }
        }
        return totalServiceCost;
    }

    public boolean confirmReservation(String reservationId, String paymentMethod) {
        reloadReservations(); // Ensure we have fresh data

        Reservations reservation = findReservationById(reservationId);
        if (reservation != null && "pending".equalsIgnoreCase(reservation.getStatus())) {
            reservation.setStatus("active");
            int roomId = Integer.parseInt(reservation.getRoomId());
            roomController.updateRoomState(roomId, "occupied");
            saveReservations();
            return true;
        }
        return false;
    }

    public boolean cancelReservation(String reservationId) {
        reloadReservations(); // Ensure we have fresh data

        Reservations reservation = findReservationById(reservationId);
        if (reservation != null && reservation.isActive()) {
            reservation.cancel();
            int roomId = Integer.parseInt(reservation.getRoomId());
            roomController.updateRoomState(roomId, "available");
            saveReservations();
            return true;
        }
        return false;
    }

    public boolean completeReservation(String reservationId) {
        reloadReservations(); // Ensure we have fresh data

        Reservations reservation = findReservationById(reservationId);
        if (reservation != null && reservation.isActive()) {
            reservation.complete();
            int roomId = Integer.parseInt(reservation.getRoomId());
            roomController.updateRoomState(roomId, "available");
            saveReservations();
            return true;
        }
        return false;
    }

    public List<RoomModel> getAvailableRoomsForDates(LocalDate checkIn, LocalDate checkOut) {
        reloadReservations(); // Ensure we have fresh data

        List<RoomModel> allRooms = roomController.getAllRooms();
        List<RoomModel> availableRooms = new ArrayList<>();
        for (RoomModel room : allRooms) {
            String roomId = String.valueOf(room.getId());
            if (isRoomAvailable(roomId, checkIn, checkOut) && room.getState().equalsIgnoreCase("available")) {
                availableRooms.add(room);
            }
        }
        return availableRooms;
    }

    public List<Reservations> getAllReservations() {
        reloadReservations(); // CRITICAL: Reload fresh data from file before returning
        return new ArrayList<>(reservations);
    }

    public Reservations findReservationById(String reservationId) {
        // Note: This uses the already loaded reservations
        // If you need absolutely fresh data, call reloadReservations() before this
        for (Reservations res : reservations) {
            if (res.getReservationId().equals(reservationId)) return res;
        }
        return null;
    }

    private RoomModel getRoomById(String roomId) {
        try {
            int id = Integer.parseInt(roomId);
            List<RoomModel> allRooms = roomController.getAllRooms();
            for (RoomModel room : allRooms) {
                if (room.getId() == id) return room;
            }
            return null;
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private boolean isValidDateRange(LocalDate checkIn, LocalDate checkOut) {
        return checkOut.isAfter(checkIn);
    }

    public boolean isRoomAvailable(String roomId, LocalDate checkIn, LocalDate checkOut) {
        // Note: This uses the already loaded reservations
        // Should be called after reloadReservations() in parent methods
        for (Reservations res : reservations) {
            if (res.getRoomId().equals(roomId) && res.isActive()) {
                if (!checkOut.isBefore(res.getCheckInDate()) && !checkIn.isAfter(res.getCheckOutDate())) {
                    return false;
                }
            }
        }
        return true;
    }

    private String generateReservationId() {
        return "RES-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }

    public void saveReservations() {
        dataManager.save(reservations);
    }

    public void reloadReservations() {
        this.reservations = dataManager.load();
    }

    public List<Reservations> getUserReservations(String userId) {
        reloadReservations(); // Ensure we have fresh data

        List<Reservations> userReservations = new ArrayList<>();
        for (Reservations res : reservations) {
            if (res.getUserId().equals(userId)) {
                userReservations.add(res);
            }
        }
        return userReservations;
    }

    public boolean deleteReservation(String reservationId) {
        reloadReservations(); // Ensure we have fresh data before deleting

        Reservations reservation = findReservationById(reservationId);
        if (reservation != null) {
            // If reservation is active, make room available again
            if (reservation.isActive()) {
                int roomId = Integer.parseInt(reservation.getRoomId());
                roomController.updateRoomState(roomId, "available");
            }
            reservations.remove(reservation);
            saveReservations();

            // Reload after saving to ensure consistency
            reloadReservations();
            return true;
        }
        return false;
    }

}