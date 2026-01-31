package model.FilterStrategy;

import model.room.RoomModel;

import java.util.ArrayList;
import java.util.List;

public class PriceFilter implements RoomFilter {
    private final int min;
    private final int max;

    public PriceFilter(int min, int max) {
        this.min = min;
        this.max = max;
    }


    @Override
    public List<RoomModel> apply(List<RoomModel> rooms) {
        List<RoomModel> filteredRooms = new ArrayList<>();
        for (RoomModel room : rooms) {
            if (room.getPrice() >= min && room.getPrice() <= max) {
                filteredRooms.add(room);
            }
        }
        return filteredRooms;
    }


}
