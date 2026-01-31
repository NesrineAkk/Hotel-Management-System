package controller;

import model.services.UserService;
import model.user.Role;
import model.user.UserModel;

import java.util.List;

public class UserController {
    private final UserService userService;

    public UserController() {
        this.userService = new UserService();
    }

    public UserModel signUp(String firstName, String lastName, String email, String password, String phone) {
        return userService.signUp(firstName, lastName, email, password, phone);
    }

    public UserModel signIn(String email, String motDePasse) {
        return userService.signIn(email, motDePasse);
    }

    public void logout() {
        userService.logout();
    }

    public boolean deleteUser(String userId) {
        return userService.deleteUser(userId);
    }

    public UserModel getCurrentUser() {
        return userService.getCurrentUser();
    }

    // Delegate to UserService for all user data operations
    public List<UserModel> getAllUsers() {
        return userService.getAllUsers();
    }

    public int getUserCount() {
        return userService.getUserCount();
    }

    public int getUserCountByRole(Role role) {
        return userService.getUserCountByRole(role);
    }

    public int getRegularUserCount() {
        return userService.getRegularUserCount();
    }

    public int getAdminCount() {
        return userService.getAdminCount();
    }
}