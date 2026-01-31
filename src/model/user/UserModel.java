package model.user;

//import data.UserIdGenerator;

public class UserModel {

    private String id;
    private String lastName;
    private String firstName;
    private String email;
    private String password;
    private String phone;
    private Role role;

    public UserModel(String id, String lastName, String firstName, String email,
                     String password, String phone, Role role) {

        this.id = id;
        // UUID.randomUUID().toString();
        this.lastName = lastName;
        this.firstName = firstName;
        this.email = email;
        this.password = password;
        this.phone = phone;
        this.role = role;
    }

    public UserModel() {}

    public String getId() { return id; }
    public String getLastName() { return lastName; }
    public String getFirstName() { return firstName; }
    public String getEmail() { return email; }
    public String getPassword() { return password; }
    public String getPhone() { return phone; }
    public Role getRole() { return role; }

    public boolean isAdmin() {
        return role == Role.ADMIN;
    }

    public boolean isUser() {
        return role == Role.USER;
    }
}
