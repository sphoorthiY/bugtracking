package view;

import controller.TesterController;
import dao.BugDao;
import service.BugService;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

@SuppressWarnings("unused")
public class TesterView extends JFrame {
    private final int testerId;
    private final TesterController testerController;
    BugDao bugDao = new BugDao();
    BugService bugService = new BugService(bugDao);

    public TesterView(int testerId) {
        this.testerId = testerId;
        this.testerController = new TesterController(bugService, bugDao);

        setTitle("Tester Dashboard");
        setSize(400, 300);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(2, 1));

        JButton createBugTicketButton = new JButton("Create Bug Ticket");
        JButton reviewReportButton = new JButton("Review Report");

        panel.add(createBugTicketButton);
        panel.add(reviewReportButton);

        createBugTicketButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openCreateBugTicketPopup();
            }
        });

        reviewReportButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openReviewReportPopup();
            }
        });

        add(panel);
        setVisible(true);
    }

    private void openCreateBugTicketPopup() {
        JFrame popupFrame = new JFrame("Create Bug Ticket");
        popupFrame.setSize(400, 300);
        popupFrame.setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridLayout(5, 2));

        panel.add(new JLabel("Bug Name:"));
        JTextField bugNameField = new JTextField();
        panel.add(bugNameField);

        panel.add(new JLabel("Description:"));
        JTextField descriptionField = new JTextField();
        panel.add(descriptionField);

        panel.add(new JLabel("Project ID:"));
        JTextField projectIdField = new JTextField();
        panel.add(projectIdField);

        panel.add(new JLabel("Tester ID:"));
        JTextField testerIdField = new JTextField(String.valueOf(testerId));
        testerIdField.setEditable(false);
        panel.add(testerIdField);

        JButton submitButton = new JButton("Submit");
        panel.add(submitButton);

        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String bugName = bugNameField.getText();
                String description = descriptionField.getText();
                int projectId = Integer.parseInt(projectIdField.getText());

                testerController.createBug(bugName, description, projectId, testerId);

                popupFrame.dispose();
            }
        });

        popupFrame.add(panel);
        popupFrame.setVisible(true);
    }

    private void openReviewReportPopup() {
        JFrame popupFrame = new JFrame("Review Bug Report");
        popupFrame.setSize(400, 300);
        popupFrame.setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridLayout(2, 1));

        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileFilter(new FileNameExtensionFilter("Text files", "txt"));
        fileChooser.setCurrentDirectory(new File("/home/keshav/Java_project/bug_tracker/src"));
        int result = fileChooser.showOpenDialog(popupFrame);

        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            String reportContents = readReportFile(selectedFile);
            JTextArea textArea = new JTextArea(reportContents);
            JScrollPane scrollPane = new JScrollPane(textArea);

            JPanel reviewPanel = new JPanel(new GridLayout(3, 1));

            JTextArea reviewTextArea = new JTextArea();
            JCheckBox isFixedCheckBox = new JCheckBox("Is Fixed?");
            JButton submitResponseButton = new JButton("Submit Response");

            reviewPanel.add(scrollPane);
            reviewPanel.add(reviewTextArea);
            reviewPanel.add(isFixedCheckBox);
            reviewPanel.add(submitResponseButton);

            submitResponseButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    String review = reviewTextArea.getText();
                    boolean isFixed = isFixedCheckBox.isSelected();

                    // Extract the Bug ID from the file name
                    String fileName = selectedFile.getName();
                    //int bugId = Integer.parseInt(fileName.substring(fileName.indexOf("_") + 1, fileName.indexOf(".")));
                    @SuppressWarnings("unused")
                    int firstUnderscoreIndex = fileName.indexOf("_", fileName.indexOf("_") + 1);
                    int secondUnderscoreIndex = fileName.indexOf("_", fileName.indexOf("_") + 1);
                    int bugId = Integer.parseInt(fileName.substring(secondUnderscoreIndex + 1, fileName.indexOf(".")));

                    testerController.reviewBugResponse(bugId, review, isFixed);

                    popupFrame.dispose();
                }
            });

            panel.add(reviewPanel);
        }

        popupFrame.add(panel);
        popupFrame.setVisible(true);
    }

    private String readReportFile(File file) {
        StringBuilder contents = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                contents.append(line).append("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return contents.toString();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new TesterView(123); // Replace 123 with the actual tester ID
            }
        });
    }
}
