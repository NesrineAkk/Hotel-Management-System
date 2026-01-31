package model.FilterStrategy;
import model.room.RoomModel;

import java.util.ArrayList;
import java.util.List;

public class TypeFilter implements RoomFilter {
    private final String type;

    public TypeFilter(String type) {
        this.type = type.toLowerCase();
    }

    public List<RoomModel> apply(List<RoomModel> rooms) {
        List<RoomModel> filteredRooms = new ArrayList<>();

        for (RoomModel room : rooms) {
            if (room.getType().equalsIgnoreCase(type)) {
                filteredRooms.add(room);
            }
        }

        return filteredRooms;
    }

}


