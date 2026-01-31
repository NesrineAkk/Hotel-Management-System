package data;

import java.util.UUID;

public class UserIdGenerator {

    private UserIdGenerator() {
        // Prevent instantiation
    }

    public static String generate() {
        return UUID.randomUUID().toString();
    }
}