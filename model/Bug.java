package model;

public class Bug {
    private int bugId;
    private String bugName;
    private String description;
    private int projectId; // Reference to the project
    private BugStatus status;
    private int employeeId;

    // Constructor
    public Bug(int bugId, String bugName, String description, int projectId, BugStatus status, int employeeId) {
        this.bugId = bugId;
        this.bugName = bugName;
        this.description = description;
        this.projectId = projectId;
        this.status = status;
        this.employeeId = employeeId;
    }

    // Getters and setters
    public int getBugId() {
        return bugId;
    }

    public void setBugId(int bugId) {
        this.bugId = bugId;
    }

    public String getBugName() {
        return bugName;
    }

    public void setBugName(String bugName) {
        this.bugName = bugName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getProjectId() {
        return projectId;
    }

    public void setProjectId(int projectId) {
        this.projectId = projectId;
    }

    public BugStatus getStatus() {
        return status;
    }

    public void setStatus(BugStatus status) {
        this.status = status;
    }

    public int getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(int employeeId) {
        this.employeeId = employeeId;
    }

    // Enum defining bug status
    public enum BugStatus {
        OPEN,
        IN_PROGRESS,
        RESOLVED,
        CLOSED
    }
}
