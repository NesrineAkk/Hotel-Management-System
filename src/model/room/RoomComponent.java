package model.room;
import java.util.List;


public interface RoomComponent {

    public int getId();
    public int getNumber();
    public String getType(); 
    public int getPrice(); 
    public List<String> getFeatures(); 
    public String getState(); 

} 

