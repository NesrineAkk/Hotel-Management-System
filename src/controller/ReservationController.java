package controller;

import model.services.ReservationService;
import model.reservation.Reservations;
import model.room.RoomModel;
import model.user.UserModel;

import java.time.LocalDate;
import java.util.List;

public class ReservationController {
    private final ReservationService reservationService;

    public ReservationController(RoomController roomController) {
        this.reservationService = new ReservationService(roomController);
    }
    public boolean deleteReservation(String reservationId) {
        return reservationService.deleteReservation(reservationId);
    }

    public Reservations createReservation(String userId, String roomId,
                                          LocalDate checkIn, LocalDate checkOut,
                                          boolean applyDiscount) {
        return reservationService.createReservation(userId, roomId, checkIn, checkOut, applyDiscount);
    }

    public Reservations createReservation(UserModel user, int roomId,
                                          String checkInStr, String checkOutStr,
                                          List<String> services) {
        return reservationService.createReservation(user, roomId, checkInStr, checkOutStr, services);
    }

    public boolean confirmReservation(String reservationId, String paymentMethod) {
        return reservationService.confirmReservation(reservationId, paymentMethod);
    }

    public boolean cancelReservation(String reservationId) {
        return reservationService.cancelReservation(reservationId);
    }

    public boolean completeReservation(String reservationId) {
        return reservationService.completeReservation(reservationId);
    }

    public List<RoomModel> getAvailableRoomsForDates(LocalDate checkIn, LocalDate checkOut) {
        return reservationService.getAvailableRoomsForDates(checkIn, checkOut);
    }

    public List<Reservations> getAllReservations() {
        return reservationService.getAllReservations();
    }

    public Reservations findReservationById(String reservationId) {
        return reservationService.findReservationById(reservationId);
    }

    public boolean isRoomAvailable(String roomId, LocalDate checkIn, LocalDate checkOut) {
        return reservationService.isRoomAvailable(roomId, checkIn, checkOut);
    }

    public void saveReservations() {
        reservationService.saveReservations();
    }

    public void reloadReservations() {
        reservationService.reloadReservations();
    }
    public List<Reservations> getUserReservations(String userId) {
        return reservationService.getUserReservations(userId);
    }

}
