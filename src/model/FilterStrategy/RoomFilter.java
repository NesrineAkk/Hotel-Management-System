package model.FilterStrategy;
import model.room.RoomModel;

import java.util.List;

public interface RoomFilter {
    List<RoomModel> apply(List<RoomModel> rooms);
}
