package dao;

import model.User;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class UserDao {
    private static final String CSV_FILE_PATH = "csv/users.csv";

    public List<User> readUsers() {
        List<User> users = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(CSV_FILE_PATH))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                int id = Integer.parseInt(data[0]);
                String username = data[1];
                String password = data[2];
                String email = data[3];
                User.UserType userType = User.UserType.valueOf(data[4]);
                List<Integer> assignedProjectIds = new ArrayList<>();
                if (data.length > 5) {
                    String[] assignedIds = data[5].split(";");
                    for (String projectId : assignedIds) {
                        assignedProjectIds.add(Integer.parseInt(projectId));
                    }
                }
                users.add(new User(id, username, password, email, userType, assignedProjectIds));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return users;
    }

    public void writeUsers(List<User> users) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(CSV_FILE_PATH))) {
            for (User user : users) {
                bw.write(user.getId() + "," + user.getUsername() + "," + user.getPassword() + "," +
                        user.getEmail() + "," + user.getUserType() + ",");
                if (!user.getAssignedProjectIds().isEmpty()) {
                    bw.write(String.join(";", user.getAssignedProjectIds().stream().map(Object::toString).toArray(String[]::new)));
                }
                bw.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
