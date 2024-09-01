package model;

import java.util.List;

public class Project {
    private int projectId;
    private String projectName;
    private int creatorEmployeeId;
    private List<Integer> assignedEmployeeIds;

    // Constructor
    public Project(int projectId, String projectName, int creatorEmployeeId, List<Integer> assignedEmployeeIds) {
        this.projectId = projectId;
        this.projectName = projectName;
        this.creatorEmployeeId = creatorEmployeeId;
        this.assignedEmployeeIds = assignedEmployeeIds;
    }

    // Getters and setters
    public int getProjectId() {
        return projectId;
    }

    public void setProjectId(int projectId) {
        this.projectId = projectId;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public int getCreatorEmployeeId() {
        return creatorEmployeeId;
    }

    public void setCreatorEmployeeId(int creatorEmployeeId) {
        this.creatorEmployeeId = creatorEmployeeId;
    }

    public List<Integer> getAssignedEmployeeIds() {
        return assignedEmployeeIds;
    }

    public void setAssignedEmployeeIds(List<Integer> assignedEmployeeIds) {
        this.assignedEmployeeIds = assignedEmployeeIds;
    }
}
