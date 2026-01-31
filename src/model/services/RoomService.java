package model.services;

import com.google.gson.reflect.TypeToken;
import data.DataManager;
import model.FilterStrategy.*;
import model.room.RoomModel;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class RoomService {

    private final DataManager<RoomModel> dataManager;
    private List<RoomModel> rooms;

    public RoomService(String filePath) {
        Type roomListType = new TypeToken<List<RoomModel>>(){}.getType();
        this.dataManager = new DataManager<>(filePath, roomListType);
        this.rooms = new ArrayList<>(dataManager.load());
    }

    public RoomService(DataManager<RoomModel> dataManager) {
        this.dataManager = dataManager;
        this.rooms = new ArrayList<>(dataManager.load());
    }

    // Get all rooms
    public List<RoomModel> getAllRooms() {
        return new ArrayList<>(rooms);
    }

    // Reload rooms
    public void reloadRooms() {
        this.rooms = new ArrayList<>(dataManager.load());
    }

    // Add room
    public boolean addRoom(RoomModel newRoom) {
        for (RoomModel room : rooms) {
            if (room.getId() == newRoom.getId() || room.getNumber() == newRoom.getNumber()) {
                return false;
            }
        }
        rooms.add(newRoom);
        boolean saved = dataManager.save(rooms);
        if (!saved) rooms.remove(newRoom);
        return saved;
    }

    // Delete room
    public boolean deleteRoom(int id) {
        RoomModel roomToRemove = null;
        for (RoomModel room : rooms) {
            if (room.getId() == id) {
                roomToRemove = room;
                break;
            }
        }
        if (roomToRemove != null) {
            rooms.remove(roomToRemove);
            boolean saved = dataManager.save(rooms);
            if (!saved) {
                rooms.add(roomToRemove);
                return false;
            }
            return true;
        }
        return false;
    }

    // Update state
    public boolean updateRoomState(int id, String newState) {
        for (RoomModel room : rooms) {
            if (room.getId() == id) {
                String oldState = room.getState();
                room.setState(newState);
                boolean saved = dataManager.save(rooms);
                if (!saved) room.setState(oldState);
                return saved;
            }
        }
        return false;
    }

    // Update price
    public boolean updateRoomPrice(int id, int newPrice) {
        for (RoomModel room : rooms) {
            if (room.getId() == id) {
                int oldPrice = room.getPrice();
                room.setPrice(newPrice);
                boolean saved = dataManager.save(rooms);
                if (!saved) room.setPrice(oldPrice);
                return saved;
            }
        }
        return false;
    }

    // Update entire room
    public boolean updateRoom(RoomModel updatedRoom) {
        for (int i = 0; i < rooms.size(); i++) {
            if (rooms.get(i).getId() == updatedRoom.getId()) {
                RoomModel oldRoom = rooms.get(i);
                rooms.set(i, updatedRoom);
                boolean saved = dataManager.save(rooms);
                if (!saved) rooms.set(i, oldRoom);
                return saved;
            }
        }
        return false;
    }

    // Filtering
    public List<RoomModel> filterRooms(RoomFilter filter) {
        return filter.apply(rooms);
    }

    public List<RoomModel> filterByType(String type) {
        return filterRooms(new TypeFilter(type));
    }

    public List<RoomModel> filterByPriceRange(int min, int max) {
        return filterRooms(new PriceFilter(min, max));
    }

    public List<RoomModel> filterByState(String state) {
        return filterRooms(new StateFilter(state));
    }

    public List<RoomModel> filterByFeatures(List<String> features, boolean requireAll) {
        return filterRooms(new FeatureFilter(features, requireAll));
    }

    public List<RoomModel> filterComposite(List<RoomFilter> filters) {
        return filterRooms(new CompositeFilter(filters));
    }

    // Statistics
    public int getRoomCount() {
        return rooms.size();
    }

    public int getOccupiedRoomsCount() {
        int count = 0;
        for (RoomModel room : rooms) {
            if (room.getState().equalsIgnoreCase("occupied")) count++;
        }
        return count;
    }

    public int getAvailableRoomsCount() {
        int count = 0;
        for (RoomModel room : rooms) {
            if (room.getState().equalsIgnoreCase("available")) count++;
        }
        return count;
    }

    public int getMaintenanceRoomsCount() {
        int count = 0;
        for (RoomModel room : rooms) {
            if (room.getState().equalsIgnoreCase("maintenance")) count++;
        }
        return count;
    }

    // Get by ID or number
    public RoomModel getRoomById(int id) {
        for (RoomModel room : rooms) {
            if (room.getId() == id) return room;
        }
        return null;
    }

    public RoomModel getRoomByNumber(int number) {
        for (RoomModel room : rooms) {
            if (room.getNumber() == number) return room;
        }
        return null;
    }
}
