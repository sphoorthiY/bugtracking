package view;

import controller.AdminController;
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
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class AdminView extends JFrame {
    private JTable bugTable;

    public AdminView() {
        setTitle("Admin Dashboard");
        setSize(800, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());

        // Create buttons
        JButton createProjectButton = new JButton("Create Project");
        JButton viewBugTicketsButton = new JButton("View Bug Tickets");

        // Add buttons to panel
        panel.add(createProjectButton, BorderLayout.NORTH);
        panel.add(viewBugTicketsButton, BorderLayout.SOUTH);

        // Add action listeners to buttons
        createProjectButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openCreateProjectPopup();
            }
        });

        viewBugTicketsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                displayBugTickets();
            }
        });

        add(panel);

        setVisible(true);
    }

    private void openCreateProjectPopup() {
        JFrame popupFrame = new JFrame("Create Project");
        popupFrame.setSize(400, 300);
        popupFrame.setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridLayout(5, 2));

        // Add labels and text fields
        panel.add(new JLabel("Project Name:"));
        JTextField projectNameField = new JTextField();
        panel.add(projectNameField);

        panel.add(new JLabel("Project Manager ID:"));
        JTextField projectManagerIdField = new JTextField();
        panel.add(projectManagerIdField);

        panel.add(new JLabel("Developer IDs (comma-separated):"));
        JTextField developerIdsField = new JTextField();
        panel.add(developerIdsField);

        panel.add(new JLabel("Tester IDs (comma-separated):"));
        JTextField testerIdsField = new JTextField();
        panel.add(testerIdsField);

        JButton submitButton = new JButton("Submit");
        panel.add(submitButton);

        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Extract input values
                String projectName = projectNameField.getText();
                int projectManagerId = Integer.parseInt(projectManagerIdField.getText());
                String[] developerIds = developerIdsField.getText().split(",");
                String[] testerIds = testerIdsField.getText().split(",");

                // Initialize userDao
                UserDao userDao = new UserDao();
                ProjectDao projectDao = new ProjectDao();
                // Call controller method to create project
                //AdminController adminController = new AdminController(new ProjectService(new ArrayList<>(), null), new UserService(userDao.readUsers(), userDao), null, null);
                AdminController adminController = new AdminController(new ProjectService(new ArrayList<>(), projectDao),new UserService(userDao.readUsers(), userDao), new BugService(new ArrayList<>(), new BugDao()),null);
                List<Integer> developerIdList = new ArrayList<>();
                List<Integer> testerIdList = new ArrayList<>();
                for (String devId : developerIds) {
                    developerIdList.add(Integer.parseInt(devId.trim()));
                }
                for (String testId : testerIds) {
                    testerIdList.add(Integer.parseInt(testId.trim()));
                }
                adminController.createProject(projectName, 1, projectManagerId, developerIdList, testerIdList);

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
                new AdminView();
            }
        });
    }
}
