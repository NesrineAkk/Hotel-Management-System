package model.FilterStrategy;

import model.room.RoomModel;

import java.util.ArrayList;
import java.util.List;

public class StateFilter implements RoomFilter {

    private final String state;
    public StateFilter(String state) {
        this.state = state.toLowerCase();
    }

    @Override
    public List<RoomModel> apply(List<RoomModel> rooms) {
        List<RoomModel> filteredRooms = new ArrayList<>();
        for (RoomModel room : rooms) {

            if (room.getState().equalsIgnoreCase(state)) {
                filteredRooms.add(room);
            }
        }
        return filteredRooms;

    }

}
