package model;

import java.util.List;

public class User {
    private int id;
    private String username;
    private String password;
    private String email;
    private UserType userType;
    private List<Integer> assignedProjectIds;

    // Constructor
    public User(int id, String username, String password, String email, UserType userType, List<Integer> assignedProjectIds) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.email = email;
        this.userType = userType;
        this.assignedProjectIds = assignedProjectIds;
    }

    // Getters and setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public UserType getUserType() {
        return userType;
    }

    public void setUserType(UserType userType) {
        this.userType = userType;
    }

    public List<Integer> getAssignedProjectIds() {
        return assignedProjectIds;
    }

    public void setAssignedProjectIds(List<Integer> assignedProjectIds) {
        this.assignedProjectIds = assignedProjectIds;
    }

    // Enum defining user types
    public enum UserType {
        ADMIN,
        PROJECT_MANAGER,
        DEVELOPER,
        TESTER
    }
}
