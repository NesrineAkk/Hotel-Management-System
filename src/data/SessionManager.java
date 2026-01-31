package data;

import model.user.UserModel;

public class SessionManager {

    private static SessionManager instance;
    private UserModel currentUser;

    private SessionManager() {}

    public static SessionManager getInstance() {
        if (instance == null) {
            instance = new SessionManager();
        }
        return instance;
    }

    public void login(UserModel user) {
        currentUser = user;
    }

    public void logout() {
        currentUser = null;
    }

    public boolean isLoggedIn() {
        return currentUser != null;
    }

    public UserModel getCurrentUser() {
        return currentUser;
    }
}
