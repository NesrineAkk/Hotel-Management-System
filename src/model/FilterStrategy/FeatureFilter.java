package model.FilterStrategy;
import model.room.RoomModel;

import java.util.ArrayList;
import java.util.List;

public class FeatureFilter implements RoomFilter {

    private final List<String> features;
    private final boolean requireAll;


    public FeatureFilter(List<String> features, boolean requireAll) {
        this.features = new ArrayList<>();
        for (String feature : features) {
            this.features.add(feature.toLowerCase());
        }
        this.requireAll = requireAll;
    }

    @Override
    public List<RoomModel> apply(List<RoomModel> rooms) {
        List<RoomModel> filteredRooms = new ArrayList<>();

        for (RoomModel room : rooms) {
            List<String> roomFeatures = new ArrayList<>();
            for (String feature : room.getFeatures()) {
                roomFeatures.add(feature.toLowerCase());
            }
            // ALL FEATURES AND ONLY THE ONES THE USER ENTERED MUST BE IN ROOMFEATURES if REQUIRE ALL EQUAL TRUE
            boolean matches;
            if (requireAll) {
                matches = true;
                for (String feature : features) {
                    if (!roomFeatures.contains(feature)) {
                        matches = false;
                        break;
                    }
                }
            } else {
                //AT LEAST ONE FEATURE
                matches = false;
                for (String feature : features) {
                    if (roomFeatures.contains(feature)) {
                        matches = true;
                        break;
                    }
                }
            }

            if (matches) {
                filteredRooms.add(room);
            }
        }

        return filteredRooms;
    }




}
