package controller;

import model.Bug;
import service.BugService;
import dao.BugDao;

import java.io.FileWriter;
import java.io.IOException;

public class TesterController {
    private BugService bugService;
    public TesterController(BugService bugService, BugDao bugDao) {
        this.bugService = bugService;
    }

    // Method to create a bug ticket
    public void createBug(String bugName, String description, int projectId, int testerId) {
        Bug bug = bugService.createBug(bugName, description, projectId, testerId);
        bugService.saveBugsToCsv(); // Save bugs to CSV after creation
        System.out.println("Bug ticket created successfully: " + bug);
    }

    // Method to review the bug response made by the developer
    public void reviewBugResponse(int bugId, String response, boolean isFixed) {
        Bug bug = bugService.getBugById(bugId);
        if (bug != null) {
            try (FileWriter writer = new FileWriter("Bug_Response_" + bugId + ".txt", true)) {
                writer.write("\nTester Review:\n");
                writer.write("Response: " + response + "\n");
                writer.write("Fixed Status: " + (isFixed ? "Fixed" : "Not Fixed") + "\n");
                System.out.println("Review added to bug response file.");
            } catch (IOException e) {
                System.out.println("Error: Unable to update bug response file.");
                e.printStackTrace();
            }
        } else {
            System.out.println("Error: Bug ticket not found.");
        }
    }

    // Method to update the status of a bug ticket
    public void updateBugStatus(int bugId, Bug.BugStatus status) {
        bugService.updateBugStatus(bugId, status);
        bugService.saveBugsToCsv(); // Save bugs to CSV after updating status
        System.out.println("Bug ticket status updated successfully.");
    }
}
