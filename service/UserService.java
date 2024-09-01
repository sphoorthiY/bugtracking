package service;

import model.User;
import model.User.UserType;
import dao.UserDao;

import java.util.ArrayList;
import java.util.List;

public class UserService {
    private List<User> users;
    private UserDao userDao; // Added UserDao for CSV operations

    public UserService(List<User> users, UserDao userDao) {
        this.users = users != null ? users : new ArrayList<>();
        this.userDao = userDao;
    }

    // Method to add a new user
    public User addUser(String username, String password, String email, UserType userType) {
        int userId = generateUserId();
        User user = new User(userId, username, password, email, userType, new ArrayList<>());
        users.add(user);
        saveUsersToCsv(); // Save users to CSV after addition
        return user;
    }

    // Method to check if a user exists
    public boolean isUserExists(int userId) {
        for (User user : users) {
            if (user.getId() == userId) {
                return true;
            }
        }
        return false;
    }

    // Method to check if a user with a specific type exists
    public boolean isUserExists(int userId, UserType userType) {
        for (User user : users) {
            if (user.getId() == userId && user.getUserType() == userType) {
                return true;
            }
        }
        return false;
    }

    // Method to validate user credentials during login
    public User validateUser(String username, String password) {
        for (User user : users) {
            if (user.getUsername().equals(username) && user.getPassword().equals(password)) {
                return user;
            }
        }
        return null;
    }

    // Method to generate a unique user ID
    private int generateUserId() {
        // Implement your logic to generate a unique user ID
        // For simplicity, using a simple increment for demonstration purposes
        return users.size() + 1;
    }

    // Method to save users to CSV
    public void saveUsersToCsv() {
        if (userDao != null) { // Check if userDao is not null before using it
            userDao.writeUsers(users);
        } else {
            System.out.println("Error: UserDao is not initialized.");
        }
    }
    
    // Method to get all users
    public List<User> getAllUsers() {
        return new ArrayList<>(users);
    }
}
