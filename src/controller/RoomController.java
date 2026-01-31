package controller;

import model.services.RoomService;
import model.FilterStrategy.RoomFilter;
import model.room.RoomModel;

import java.util.List;

public class RoomController {

    private final RoomService roomService;

    public RoomController(String filePath) {
        this.roomService = new RoomService(filePath);
    }

    public RoomController(RoomService roomService) {
        this.roomService = roomService;
    }

    public List<RoomModel> getAllRooms() {
        return roomService.getAllRooms();
    }

    public void reloadRooms() {
        roomService.reloadRooms();
    }

    public boolean addRoom(RoomModel newRoom) {
        return roomService.addRoom(newRoom);
    }

    public boolean deleteRoom(int id) {
        return roomService.deleteRoom(id);
    }

    public boolean updateRoomState(int id, String newState) {
        return roomService.updateRoomState(id, newState);
    }

    public boolean updateRoomPrice(int id, int newPrice) {
        return roomService.updateRoomPrice(id, newPrice);
    }

    public boolean updateRoom(RoomModel updatedRoom) {
        return roomService.updateRoom(updatedRoom);
    }

    public List<RoomModel> filterRooms(RoomFilter filter) {
        return roomService.filterRooms(filter);
    }

    public List<RoomModel> filterByType(String type) {
        return roomService.filterByType(type);
    }

    public List<RoomModel> filterByPriceRange(int min, int max) {
        return roomService.filterByPriceRange(min, max);
    }

    public List<RoomModel> filterByState(String state) {
        return roomService.filterByState(state);
    }

    public List<RoomModel> filterByFeatures(List<String> features, boolean requireAll) {
        return roomService.filterByFeatures(features, requireAll);
    }

    public List<RoomModel> filterComposite(List<RoomFilter> filters) {
        return roomService.filterComposite(filters);
    }

    public int getRoomCount() {
        return roomService.getRoomCount();
    }

    public int getOccupiedRoomsCount() {
        return roomService.getOccupiedRoomsCount();
    }

    public int getAvailableRoomsCount() {
        return roomService.getAvailableRoomsCount();
    }

    public int getMaintenanceRoomsCount() {
        return roomService.getMaintenanceRoomsCount();
    }

    public RoomModel getRoomById(int id) {
        return roomService.getRoomById(id);
    }

    public RoomModel getRoomByNumber(int number) {
        return roomService.getRoomByNumber(number);
    }
}
