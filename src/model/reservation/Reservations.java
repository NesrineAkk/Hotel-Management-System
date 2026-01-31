package model.reservation;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class Reservations {
    private String reservationId;
    private String userId;
    private String roomId;
    private LocalDate checkInDate;
    private LocalDate checkOutDate;
    private long numberOfNights;  // Add this field
    private double totalPrice;
    private String status;
    private LocalDate createdAt;

    public Reservations() {
    }

    public Reservations(String reservationId, String userId, String roomId,
                        LocalDate checkInDate, LocalDate checkOutDate,
                        double totalPrice, String status) {
        this.reservationId = reservationId;
        this.userId = userId;
        this.roomId = roomId;
        this.checkInDate = checkInDate;
        this.checkOutDate = checkOutDate;
        this.numberOfNights = ChronoUnit.DAYS.between(checkInDate, checkOutDate);  // Calculate and store
        this.totalPrice = totalPrice;
        this.status = status;
        this.createdAt = LocalDate.now();
    }

    public long getNumberOfNights() {
        return numberOfNights;  // Return the stored value
    }

    public void setNumberOfNights(long numberOfNights) {
        this.numberOfNights = numberOfNights;
    }

    public boolean isActive() {
        return "active".equalsIgnoreCase(status);
    }

    public void cancel() {
        this.status = "cancelled";
    }

    public void complete() {
        this.status = "completed";
    }

    public String getReservationId() {
        return reservationId;
    }

    public void setReservationId(String reservationId) {
        this.reservationId = reservationId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    public LocalDate getCheckInDate() {
        return checkInDate;
    }

    public void setCheckInDate(LocalDate checkInDate) {
        this.checkInDate = checkInDate;
        // Recalculate nights if both dates are set
        if (this.checkOutDate != null) {
            this.numberOfNights = ChronoUnit.DAYS.between(checkInDate, checkOutDate);
        }
    }

    public LocalDate getCheckOutDate() {
        return checkOutDate;
    }

    public void setCheckOutDate(LocalDate checkOutDate) {
        this.checkOutDate = checkOutDate;
        // Recalculate nights if both dates are set
        if (this.checkInDate != null) {
            this.numberOfNights = ChronoUnit.DAYS.between(checkInDate, checkOutDate);
        }
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDate getCreatedAt() {
        return createdAt;
    }

    @Override
    public String toString() {
        return "RESERVATION ID: " + reservationId +
                "\nUSER ID: " + userId +
                "\nROOM: " + roomId +
                "\nCHECK IN: " + checkInDate +
                "\nCHECK OUT: " + checkOutDate +
                "\nNIGHTS: " + numberOfNights +
                "\nTOTAL PRICE: $" + String.format("%.2f", totalPrice) +
                "\nSTATUS: " + status.toUpperCase() +
                "\n---------------------------";
    }
}