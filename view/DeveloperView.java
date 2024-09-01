package view;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import controller.DeveloperController;
import dao.BugDao;
import service.BugService;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DeveloperView extends JFrame {
    private JTable bugTable;
    private JButton responseButton;
    private JTextField updatedFileNamesField;
    private JTextField descriptionField;

    public DeveloperView() {
        setTitle("Developer Bug View");
        setSize(600, 400);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Create a panel to hold components
        JPanel panel = new JPanel(new BorderLayout());

        // Create a table to display bugs
        String[] columnNames = {"Bug ID", "Bug Name", "Description", "Project ID", "Status"};
        Object[][] data = readBugsFromCSV("/home/keshav/Java_project/bug_tracker/src/csv/bug.csv");
        DefaultTableModel model = new DefaultTableModel(data, columnNames);
        bugTable = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(bugTable);
        panel.add(scrollPane, BorderLayout.CENTER);

        // Create a button to open bug response popup
        responseButton = new JButton("Respond");
        responseButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = bugTable.getSelectedRow();
                if (selectedRow != -1) {
                    int bugId = (int) bugTable.getValueAt(selectedRow, 0);
                    int developerId = 123; // Replace with actual developer ID
                    openBugResponsePopup(bugId, developerId);
                } else {
                    JOptionPane.showMessageDialog(DeveloperView.this, "Please select a bug.");
                }
            }
        });
        panel.add(responseButton, BorderLayout.SOUTH);

        add(panel);
        setVisible(true);
    }

    private Object[][] readBugsFromCSV(String fileName) {
        List<Object[]> bugs = new ArrayList<>();
        String line = "";
        String cvsSplitBy = ",";
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            while ((line = br.readLine()) != null) {
                String[] data = line.split(cvsSplitBy);
                Object[] bug = {Integer.parseInt(data[0]), data[1], data[2], Integer.parseInt(data[3]), data[4]};
                bugs.add(bug);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bugs.toArray(new Object[0][]);
    }

    private void openBugResponsePopup(int bugId, int developerId) {
        JFrame popupFrame = new JFrame("Bug Response");
        popupFrame.setSize(400, 200);
        popupFrame.setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridLayout(3, 2));

        // Add labels
        panel.add(new JLabel("Bug ID: "));
        panel.add(new JLabel(String.valueOf(bugId)));
        panel.add(new JLabel("Developer ID: "));
        panel.add(new JLabel(String.valueOf(developerId)));

        // Add text fields
        updatedFileNamesField = new JTextField();
        panel.add(new JLabel("Updated File Names: "));
        panel.add(updatedFileNamesField);

        descriptionField = new JTextField();
        panel.add(new JLabel("Description: "));
        panel.add(descriptionField);

        // Add submit button
        JButton submitButton = new JButton("Submit");
        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                BugDao bugDao = new BugDao();
                BugService bugService = new BugService(bugDao);
                String updatedFileNames = updatedFileNamesField.getText();
                String description = descriptionField.getText();
                DeveloperController controller = new DeveloperController(bugService, bugDao);
                controller.createResponse(bugId, developerId, updatedFileNames, description);
                popupFrame.dispose(); // Close the popup
            }
        });
        panel.add(submitButton);

        popupFrame.add(panel);
        popupFrame.setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new DeveloperView();
            }
        });
    }
}
