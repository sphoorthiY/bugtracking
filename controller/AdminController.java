package controller;

import model.*;
import service.*;

import java.util.List;

import dao.UserDao;

public class AdminController {
    private ProjectService projectService;
    private UserService userService;
    private BugService bugService;
    public AdminController(ProjectService projectService, UserService userService, BugService bugService, UserDao userDao) {
        this.projectService = projectService;
        this.userService = userService;
        this.bugService = bugService;
    }

    // Method to create a new project, assign a project manager, and assign project members
    public void createProject(String projectName, int creatorId, int projectManagerId, List<Integer> developerIds, List<Integer> testerIds) {
        // Check if the provided project manager ID exists in the list of developers
        if (!userService.isUserExists(projectManagerId, User.UserType.DEVELOPER)) {
            System.out.println("Error: The provided project manager ID does not exist or is not a developer.");
            return;
        }

        // Create the project, assign project manager, and assign project members
        Project project = projectService.createProject(projectName, creatorId, projectManagerId, developerIds, testerIds);
        projectService.saveProjectsToCsv(); // Save projects to CSV after creation
        System.out.println("Project created successfully: " + project);
    }

    // Method to view details about the bug tickets raised on each project
    public void viewBugTickets(int projectId) {
        List<Bug> bugs = bugService.getBugsByProject(projectId);
        System.out.println("Bug tickets for Project " + projectId + ":");
        for (Bug bug : bugs) {
            System.out.println(bug);
        }
    }
}
