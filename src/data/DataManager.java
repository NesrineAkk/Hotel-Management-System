package data;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSerializer;
import com.google.gson.JsonDeserializer;
import com.google.gson.reflect.TypeToken;
import model.message.MessageModel;
import model.room.RoomModel;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class DataManager<T> {
    private final Gson gson;
    private final String filePath;
    private final Type listType;


    public DataManager() {
        this.filePath = null;
        this.listType = null;
        this.gson = createGson();
    }

    public DataManager(String fileName, Type listType) {
        this.filePath = fileName;
        this.listType = listType;
        this.gson = createGson();
    }

    
    private Gson createGson() {
        return new GsonBuilder()
                .registerTypeAdapter(LocalDate.class,
                        (JsonSerializer<LocalDate>) (src, type, context) ->
                                new com.google.gson.JsonPrimitive(src.toString()))
                .registerTypeAdapter(LocalDate.class,
                        (JsonDeserializer<LocalDate>) (json, type, context) ->
                                LocalDate.parse(json.getAsString()))
                .setPrettyPrinting()
                .create();
    }

    public boolean save(List<T> data) {
        if (filePath == null) {
            throw new IllegalStateException("filePath is not initialized");
        }

        try (FileWriter writer = new FileWriter(filePath)) {
            gson.toJson(data, writer);
            writer.flush();
            return true;
        } catch (IOException e) {
            System.err.println("✗ Error saving data: " + e.getMessage());
            return false;
        }
    }

    public List<T> load() {
        if (filePath == null || listType == null) {
            throw new IllegalStateException("DataManager is not fully initialized");
        }

        File file = new File(filePath);

        if (!file.exists()) {
            return new ArrayList<>();
        }

        try (FileReader reader = new FileReader(file)) {
            List<T> loaded = gson.fromJson(reader, listType);
            return (loaded != null) ? loaded : new ArrayList<>();
        } catch (IOException e) {
            System.err.println("✗ Error loading data: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    public static List<MessageModel> loadMessages() {
        try {
            Gson gson = new Gson();
            FileReader reader = new FileReader("src/data/messages.json");
            Type messageListType = new TypeToken<List<MessageModel>>() {}.getType();
            List<MessageModel> messages = gson.fromJson(reader, messageListType);
            reader.close();
            return messages;
        } catch (IOException e) {
            System.out.println("Error messages.json : " + e.getMessage());
            return null;
        }
    }

    public void saveMessages(List<MessageModel> message) {
        try (FileWriter writer = new FileWriter("src/data/messages.json")) {
            Gson gson = new Gson();
            gson.toJson(message, writer);
        } catch (IOException e) {
            System.out.println("Error messages.json: " + e.getMessage());
        }
    }
}