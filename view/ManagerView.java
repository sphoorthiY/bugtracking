package view;

import controller.ManagerController;
import dao.BugDao;
import dao.ProjectDao;
import dao.UserDao;
import service.BugService;
import service.ProjectService;
import service.UserService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ManagerView extends JFrame {
    private JTable bugTable;

    public ManagerView(int managerId) {
        setTitle("Manager Dashboard");
        setSize(800, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());

        // Create buttons
        JButton assignBugsButton = new JButton("Assign Bugs");
        JButton viewBugTicketsButton = new JButton("View Bug Tickets");
        JButton editTeamMembersButton = new JButton("Edit Team Members");

        // Add buttons to panel
        panel.add(assignBugsButton, BorderLayout.NORTH);
        panel.add(viewBugTicketsButton, BorderLayout.CENTER);
        panel.add(editTeamMembersButton, BorderLayout.SOUTH);

        // Add action listeners to buttons
        assignBugsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openAssignBugsPopup(managerId);
            }
        });

        viewBugTicketsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                displayBugTickets();
            }
        });

        editTeamMembersButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openEditTeamMembersPopup(managerId);
            }
        });

        add(panel);

        setVisible(true);
    }

    private void openAssignBugsPopup(int managerId) {
        JFrame popupFrame = new JFrame("Assign Bugs");
        popupFrame.setSize(400, 300);
        popupFrame.setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridLayout(4, 2));

        // Add labels and text fields
        panel.add(new JLabel("Project ID:"));
        JTextField projectIdField = new JTextField();
        projectIdField.setText(String.valueOf(getProjectIdByManagerId(managerId)));
        projectIdField.setEditable(false);
        panel.add(projectIdField);

        panel.add(new JLabel("Bug ID:"));
        JTextField bugIdField = new JTextField();
        panel.add(bugIdField);

        panel.add(new JLabel("Developer ID:"));
        JTextField developerIdField = new JTextField();
        panel.add(developerIdField);

        JButton assignButton = new JButton("Assign");
        panel.add(assignButton);

        assignButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Extract input values
                int projectId = Integer.parseInt(projectIdField.getText());
                int bugId = Integer.parseInt(bugIdField.getText());
                int developerId = Integer.parseInt(developerIdField.getText());

                BugDao bugDao = new BugDao();
            UserDao userDao; // Remove duplicate declaration

            // Instantiate ProjectDao
            ProjectDao projectDao = new ProjectDao(); // Instantiate ProjectDao before using it

            // Load initial data from CSV files
            userDao = new UserDao(); // Instantiate UserDao before using it
            UserService userService = new UserService(userDao.readUsers(), userDao);
            ProjectService projectService = new ProjectService(projectDao.readProjects(), projectDao);
            BugService bugService = new BugService(bugDao);
                // Call controller method to assign bug
                //ManagerController managerController = new ManagerController(new BugService(new BugDao()), new ProjectService(new ArrayList<>(), new ProjectDao()), new UserService(new ArrayList<>(), new UserDao()));
                ManagerController managerController = new ManagerController(projectService, userService, bugService, projectDao, bugDao);
                
                managerController.assignBug(projectId, bugId, developerId);

                // Close the popup
                popupFrame.dispose();
            }
        });

        popupFrame.add(panel);
        popupFrame.setVisible(true);
    }

    private void displayBugTickets() {
        JFrame bugFrame = new JFrame("Bug Tickets");
        bugFrame.setSize(800, 600);
        bugFrame.setLocationRelativeTo(null);

        JPanel bugPanel = new JPanel(new BorderLayout());

        // Read bug data from CSV
        Object[][] data = readBugsFromCSV("/home/keshav/Java_project/bug_tracker/src/csv/bug.csv");
        String[] columnNames = {"Bug ID", "Bug Name", "Description", "Project ID", "Status"};

        // Create table to display bug data
        DefaultTableModel model = new DefaultTableModel(data, columnNames);
        bugTable = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(bugTable);

        bugPanel.add(scrollPane, BorderLayout.CENTER);
        bugFrame.add(bugPanel);
        bugFrame.setVisible(true);
    }

    private void openEditTeamMembersPopup(int managerId) {
        JFrame popupFrame = new JFrame("Edit Team Members");
        popupFrame.setSize(400, 300);
        popupFrame.setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridLayout(4, 1));

        int projectId = getProjectIdByManagerId(managerId);
        List<Integer> currentMembers = getEmployeeIdsByProjectId(projectId);

        JLabel projectLabel = new JLabel("Project ID: " + projectId);
        panel.add(projectLabel);

        JTextArea currentMembersArea = new JTextArea("Current Team Members: " + currentMembers);
        panel.add(currentMembersArea);

        JTextField employeeIdField = new JTextField();
        panel.add(employeeIdField);

        JButton addButton = new JButton("Add");
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int employeeId = Integer.parseInt(employeeIdField.getText());
                addEmployeeToProject(projectId, employeeId);
                currentMembers.add(employeeId);
                currentMembersArea.setText("Current Team Members: " + currentMembers);
            }
        });
        panel.add(addButton);

        JButton removeButton = new JButton("Remove");
        removeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int employeeId = Integer.parseInt(employeeIdField.getText());
                removeEmployeeFromProject(projectId, employeeId);
                currentMembers.remove(Integer.valueOf(employeeId));
                currentMembersArea.setText("Current Team Members: " + currentMembers);
            }
        });
        panel.add(removeButton);

        popupFrame.add(panel);
        popupFrame.setVisible(true);
    }

    private int getProjectIdByManagerId(int managerId) {
        int projectId = -1;
        try (BufferedReader br = new BufferedReader(new FileReader("/home/keshav/Java_project/bug_tracker/src/csv/projects.csv"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                int managerIdFromFile = Integer.parseInt(parts[2].trim()); // Assuming managerId is at index 2
                if (managerId == managerIdFromFile) {
                    // Split parts[0] by semicolons to get individual project IDs
                    String[] projectIds = parts[0].split(";");
                    // Return the first project ID found
                    projectId = Integer.parseInt(projectIds[0].trim());
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NumberFormatException ex) {
            // Handle the case where the project ID cannot be parsed
            ex.printStackTrace();
        }
        return projectId;
    }
    

    // private int getProjectIdByManagerId(int managerId) {
    //     int projectId = -1;
    //     try (BufferedReader br = new BufferedReader(new FileReader("/home/keshav/Java_project/bug_tracker/src/csv/projects.csv"))) {
    //         String line;
    //         while ((line = br.readLine()) != null) {
    //             String[] parts = line.split(",");
    //             int managerIdFromFile = Integer.parseInt(parts[3].trim());
    //             if (managerId == managerIdFromFile) {
    //                 projectId = Integer.parseInt(parts[0].trim());
    //                 break;
    //             }
    //         }
    //     } catch (IOException e) {
    //         e.printStackTrace();
    //     }
    //     return projectId;
    // }

    private List<Integer> getEmployeeIdsByProjectId(int projectId) {
        List<Integer> employeeIds = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader("/home/keshav/Java_project/bug_tracker/src/csv/projects.csv"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (Integer.parseInt(parts[0].trim()) == projectId) {
                    String[] employeeIdsStr = parts[4].split(";");
                    for (String id : employeeIdsStr) {
                        employeeIds.add(Integer.parseInt(id.trim()));
                    }
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return employeeIds;
    }

    private void addEmployeeToProject(int projectId, int employeeId) {
        try {
            BufferedReader reader = new BufferedReader(new FileReader("/home/keshav/Java_project/bug_tracker/src/csv/projects.csv"));
            String line;
            StringBuilder fileContent = new StringBuilder();
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (Integer.parseInt(parts[0].trim()) == projectId) {
                    String employees = parts[4];
                    employees = employees.isEmpty() ? String.valueOf(employeeId) : employees + ";" + employeeId;
                    parts[4] = employees;
                    line = String.join(",", parts);
                }
                fileContent.append(line).append("\n");
            }
            reader.close();

            FileWriter writer = new FileWriter("/home/keshav/Java_project/bug_tracker/src/csv/projects.csv");
            writer.write(fileContent.toString());
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void removeEmployeeFromProject(int projectId, int employeeId) {
        try {
            BufferedReader reader = new BufferedReader(new FileReader("/home/keshav/Java_project/bug_tracker/src/csv/projects.csv"));
            String line;
            StringBuilder fileContent = new StringBuilder();
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (Integer.parseInt(parts[0].trim()) == projectId) {
                    String employees = parts[4];
                    List<String> employeeIds = new ArrayList<>(Arrays.asList(employees.split(";")));
                    employeeIds.remove(String.valueOf(employeeId));
                    employees = String.join(";", employeeIds);
                    parts[4] = employees;
                    line = String.join(",", parts);
                }
                fileContent.append(line).append("\n");
            }
            reader.close();

            FileWriter writer = new FileWriter("/home/keshav/Java_project/bug_tracker/src/csv/projects.csv");
            writer.write(fileContent.toString());
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Object[][] readBugsFromCSV(String fileName) {
        List<Object[]> bugs = new ArrayList<>();
        String line;
        String cvsSplitBy = ",";
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            while ((line = br.readLine()) != null) {
                String[] data = line.split(cvsSplitBy);
                bugs.add(data);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bugs.toArray(new Object[0][]);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new ManagerView(1); // Provide manager ID here
            }
        });
    }
}
