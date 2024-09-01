package controller;

import model.Bug;
import service.BugService;
import service.ProjectService;
import service.UserService;
import dao.ProjectDao;
import dao.BugDao;

import java.util.List;

public class ManagerController {
    private ProjectService projectService;
    private UserService userService;
    private BugService bugService;
    private ProjectDao projectDao; // Added ProjectDao for CSV operations
    public ManagerController(ProjectService projectService, UserService userService, BugService bugService, ProjectDao projectDao, BugDao bugDao) {
        this.projectService = projectService;
        this.userService = userService;
        this.bugService = bugService;
        this.projectDao = projectDao;
    }

    // Method to assign a bug to a developer of the project
    public void assignBug(int projectId, int bugId, int developerId) {
        if (projectService.isDeveloperOfProject(projectId, developerId)) {
            bugService.assignBugToDeveloper(bugId, developerId);
            bugService.saveBugsToCsv(); // Save bugs to CSV after assignment
            System.out.println("Bug assigned to developer successfully.");
        } else {
            System.out.println("Error: Developer is not assigned to the project.");
        }
    }

    // Method to add an employee to the group of employees of the project
    public void addEmployeeToProject(int projectId, int employeeId) {
        if (userService.isUserExists(employeeId)) {
            projectService.addEmployeeToProject(projectId, employeeId);
            projectService.saveProjectsToCsv(); // Save projects to CSV after adding employee
            System.out.println("Employee added to the project successfully.");
        } else {
            System.out.println("Error: Employee does not exist.");
        }
    }

    // Method to remove an employee from the group of employees of the project
    public void removeEmployeeFromProject(int projectId, int employeeId) {
        if (projectService.isEmployeeOfProject(projectId, employeeId)) {
            projectService.removeEmployeeFromProject(projectId, employeeId);
            projectService.saveProjectsToCsv(); // Save projects to CSV after removing employee
            System.out.println("Employee removed from the project successfully.");
        } else {
            System.out.println("Error: Employee is not part of the project.");
        }
    }

    // Method to view all bugs of the project
    public void viewProjectBugs(int projectId) {
        List<Bug> bugs = bugService.getBugsByProject(projectId);
        System.out.println("Bugs for Project " + projectId + ":");
        for (Bug bug : bugs) {
            System.out.println(bug);
        }
    }

    // Method to save project details to CSV file
    public void saveProjectsToCsv() {
        projectDao.writeProjects(projectService.getAllProjects());
        System.out.println("Project details saved to CSV successfully.");
    }
}
