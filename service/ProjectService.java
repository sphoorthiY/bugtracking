package service;

import model.Project;
import dao.ProjectDao;

import java.util.ArrayList;
import java.util.List;

public class ProjectService {
    private List<Project> projects;
    private ProjectDao projectDao; // Added ProjectDao for CSV operations

    public ProjectService(List<Project> projects, ProjectDao projectDao) {
        this.projects = projects;
        this.projectDao = projectDao;
    }

    // Method to create a new project
    // 
    public Project createProject(String projectName, int creatorId, int projectManagerId, List<Integer> developerIds, List<Integer> testerIds) {
        int projectId = generateProjectId();
        Project project = new Project(projectId, projectName, creatorId, new ArrayList<>());
        project.setCreatorEmployeeId(projectManagerId);
        List<Integer> assignedEmployeeIds = project.getAssignedEmployeeIds();
        assignedEmployeeIds.addAll(developerIds);
        assignedEmployeeIds.addAll(testerIds);
        projects.add(project);
        projectDao.writeProjects(projects); // Save projects to CSV after creation
        return project;
    }

    // Method to add an employee to a project
    public void addEmployeeToProject(int projectId, int employeeId) {
        for (Project project : projects) {
            if (project.getProjectId() == projectId) {
                project.getAssignedEmployeeIds().add(employeeId);
                break;
            }
        }
        projectDao.writeProjects(projects); // Save projects to CSV after adding employee
    }

    // Method to remove an employee from a project
    public void removeEmployeeFromProject(int projectId, int employeeId) {
        for (Project project : projects) {
            if (project.getProjectId() == projectId) {
                project.getAssignedEmployeeIds().remove((Integer) employeeId);
                break;
            }
        }
        projectDao.writeProjects(projects); // Save projects to CSV after removing employee
    }

    // Method to check if an employee is part of a project
    public boolean isEmployeeOfProject(int projectId, int employeeId) {
        for (Project project : projects) {
            if (project.getProjectId() == projectId) {
                return project.getAssignedEmployeeIds().contains(employeeId);
            }
        }
        return false;
    }
    
    // Method to check if a developer is assigned to a project
    public boolean isDeveloperOfProject(int projectId, int developerId) {
        for (Project project : projects) {
            if (project.getProjectId() == projectId && project.getAssignedEmployeeIds().contains(developerId)) {
                return true;
            }
        }
        return false;
    }

    // Method to generate a unique project ID
    private int generateProjectId() {
        // Implement your logic to generate a unique project ID
        // For simplicity, using a simple increment for demonstration purposes
        return projects.size() + 1;
    }

    // Method to save projects to CSV
    public void saveProjectsToCsv() {
        projectDao.writeProjects(projects);
    }
    
    // Method to get all projects
    public List<Project> getAllProjects() {
        return new ArrayList<>(projects);
    }
}
