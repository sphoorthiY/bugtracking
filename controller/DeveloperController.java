package controller;

import model.Bug;
import service.BugService;
import dao.BugDao;

import java.io.FileWriter;
import java.io.IOException;

public class DeveloperController {
    private BugService bugService;
    private BugDao bugDao; // Added BugDao for CSV operations

    public DeveloperController(BugService bugService, BugDao bugDao) {
        this.bugService = bugService;
        this.bugDao = bugDao;
    }

    // Method to view assigned bugs
    public void viewAssignedBugs(int developerId) {
        bugService.getAssignedBugs(developerId).forEach(System.out::println);
    }

    // Method to create a response to a bug ticket as a text file
    public void createResponse(int bugId, int developerId, String updatedFileNames, String descriptionOfChanges) {
        Bug bug = bugService.getBugById(bugId);

        // Create response file
        String fileName = "Bug_Response_" + bugId + ".txt";
        try (FileWriter writer = new FileWriter(fileName)) {
            writer.write("Bug Name: " + bug.getBugName() + "\n");
            writer.write("Bug ID: " + bug.getBugId() + "\n");
            writer.write("Developer ID: " + developerId + "\n");
            writer.write("Updated File Names: " + updatedFileNames + "\n");
            writer.write("Description of Changes: " + descriptionOfChanges + "\n");
            System.out.println("Response created successfully: " + fileName);
        } catch (IOException e) {
            System.out.println("Error: Unable to create response file.");
            e.printStackTrace();
        }
    }

    // Method to save assigned bug details to CSV file after creating a response
    public void saveAssignedBugsToCsv() {
        bugDao.writeBugs(bugService.getAllBugs());
        System.out.println("Assigned bug details saved to CSV successfully.");
    }
}
