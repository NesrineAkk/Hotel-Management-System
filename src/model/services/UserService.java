package model.services;

import com.google.gson.reflect.TypeToken;
import data.DataManager;
import data.SessionManager;
import model.user.Role;
import model.user.UserModel;

import java.lang.reflect.Type;
import java.util.List;
import java.util.UUID;

public class UserService {
    private final DataManager<UserModel> userDataManager;
    private final SessionManager sessionManager = SessionManager.getInstance();

    public UserService() {
        Type listType = new TypeToken<List<UserModel>>(){}.getType();
        this.userDataManager = new DataManager<>("src/data/user.json", listType);
    }

    public UserModel signUp(String firstName, String lastName, String email, String password, String phone) {
        List<UserModel> users = userDataManager.load();

        for (UserModel u : users) {
            if (u.getEmail().equalsIgnoreCase(email)) {
                return null; // email already used
            }
        }

        UserModel newUser = new UserModel(
                UUID.randomUUID().toString(),
                lastName,
                firstName,
                email,
                password,
                phone,
                Role.USER
        );

        users.add(newUser);
        userDataManager.save(users);
        return newUser;
    }

    public UserModel signIn(String email, String password) {
        List<UserModel> users = userDataManager.load();

        for (UserModel u : users) {
            if (u.getEmail().equalsIgnoreCase(email)
                    && u.getPassword().equals(password)) {
                sessionManager.login(u);
                return u;
            }
        }
        return null;
    }

    public void logout() {
        sessionManager.logout();
    }

    public UserModel getCurrentUser() {
        return sessionManager.getCurrentUser();
    }

    // Get all users - ALWAYS reload from file
    public List<UserModel> getAllUsers() {
        return userDataManager.load(); // This should reload from JSON each time
    }

    // Get total user count - ALWAYS reload from file
    public int getUserCount() {
        return userDataManager.load().size();
    }

    // Get user count by role - ALWAYS reload from file
    public int getUserCountByRole(Role role) {
        List<UserModel> users = userDataManager.load(); // Fresh load
        int count = 0;
        for (UserModel user : users) {
            if (user.getRole() == role) {
                count++;
            }
        }
        return count;
    }

    // Get only regular users (not admins)
    public int getRegularUserCount() {
        return getUserCountByRole(Role.USER);
    }

    // Get admin count
    public int getAdminCount() {
        return getUserCountByRole(Role.ADMIN);
    }

    public boolean deleteUser(String userId) {
        List<UserModel> users = userDataManager.load(); // Fresh load

        // Find and remove the user
        UserModel userToDelete = null;
        for (UserModel user : users) {
            if (user.getId().equals(userId)) {
                userToDelete = user;
                break;
            }
        }

        if (userToDelete != null) {
            // Prevent deleting the currently logged-in user
            UserModel currentUser = getCurrentUser();
            if (currentUser != null && currentUser.getId().equals(userId)) {
                return false; // Cannot delete yourself
            }

            users.remove(userToDelete);
            boolean saved = userDataManager.save(users); // Save returns boolean

            // Force a fresh load to verify
            if (saved) {
                userDataManager.load(); // Force reload
            }

            return saved;
        }
        return false;
    }
}