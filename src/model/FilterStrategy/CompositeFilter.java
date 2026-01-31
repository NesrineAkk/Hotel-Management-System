package model.FilterStrategy;

import model.room.RoomModel;

import java.util.List;

public class CompositeFilter implements RoomFilter {
    private final List<RoomFilter> filters;

    public CompositeFilter(List<RoomFilter> filters) {
        this.filters = filters;
    }

    @Override
    public List<RoomModel> apply(List<RoomModel> rooms) {
        List<RoomModel> result = rooms;
        for (RoomFilter filter : filters) {
            result = filter.apply(result);
        }
        return result;
    }
}
