package service;

import model.Bug;
import model.Bug.BugStatus;
import dao.BugDao;

import java.util.ArrayList;
import java.util.List;

public class BugService {
    private List<Bug> bugs;
    private BugDao bugDao; // Added BugDao for CSV operations

    public BugService(BugDao bugDao) {
        this.bugDao = bugDao;
        this.bugs = bugDao.readBugs(); // Initialize bugs list from CSV
    }
    public BugService(List<Bug> bugs, BugDao bugDao) {
        this.bugs = bugs != null ? bugs : new ArrayList<>();
        this.bugDao = bugDao;
    }
    // Method to create a new bug
    public Bug createBug(String bugName, String description, int projectId, int employeeId) {
        int bugId = generateBugId();
        Bug bug = new Bug(bugId, bugName, description, projectId, BugStatus.OPEN, employeeId);
        bugs.add(bug);
        bugDao.writeBugs(bugs); // Save bugs to CSV after creation
        return bug;
    }

    // Method to assign a bug to a developer
    public void assignBugToDeveloper(int bugId, int developerId) {
        for (Bug bug : bugs) {
            if (bug.getBugId() == bugId) {
                bug.setStatus(BugStatus.IN_PROGRESS);
                bug.setEmployeeId(developerId);
                bugDao.writeBugs(bugs); // Save bugs to CSV after assignment
                break;
            }
        }
    }

    // Method to update the status of a bug
    public void updateBugStatus(int bugId, BugStatus status) {
        for (Bug bug : bugs) {
            if (bug.getBugId() == bugId) {
                bug.setStatus(status);
                bugDao.writeBugs(bugs); // Save bugs to CSV after updating status
                break;
            }
        }
    }

    // Method to get bugs by project ID
    public List<Bug> getBugsByProject(int projectId) {
        List<Bug> projectBugs = new ArrayList<>();
        for (Bug bug : bugs) {
            if (bug.getProjectId() == projectId) {
                projectBugs.add(bug);
            }
        }
        return projectBugs;
    }

    // Method to get bugs by developer ID
    public List<Bug> getAssignedBugs(int developerId) {
        List<Bug> assignedBugs = new ArrayList<>();
        for (Bug bug : bugs) {
            if (bug.getEmployeeId() == developerId) {
                assignedBugs.add(bug);
            }
        }
        return assignedBugs;
    }

    // Method to get bug by ID
    public Bug getBugById(int bugId) {
        for (Bug bug : bugs) {
            if (bug.getBugId() == bugId) {
                return bug;
            }
        }
        return null;
    }

    // Method to get all bugs
    public List<Bug> getAllBugs() {
        return new ArrayList<>(bugs);
    }

    // Method to generate a unique bug ID
    private int generateBugId() {
        // Implement your logic to generate a unique bug ID
        // For simplicity, using a simple increment for demonstration purposes
        return bugs.size() + 1;
    }
    
    // Method to save bugs to CSV
    public void saveBugsToCsv() {
        bugDao.writeBugs(bugs);
    }
}
