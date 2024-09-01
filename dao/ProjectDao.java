package dao;

import model.Project;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class ProjectDao {
    private static final String CSV_FILE_PATH = "csv/projects.csv";

    public List<Project> readProjects() {
        List<Project> projects = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(CSV_FILE_PATH))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                int projectId = Integer.parseInt(data[0]);
                String projectName = data[1];
                int creatorId = Integer.parseInt(data[2]);
                List<Integer> assignedEmployeeIds = new ArrayList<>();
                if (data.length > 3) {
                    String[] assignedIds = data[3].split(";");
                    for (String id : assignedIds) {
                        assignedEmployeeIds.add(Integer.parseInt(id));
                    }
                }
                projects.add(new Project(projectId, projectName, creatorId, assignedEmployeeIds));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return projects;
    }

    public void writeProjects(List<Project> projects) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(CSV_FILE_PATH))) {
            for (Project project : projects) {
                bw.write(project.getProjectId() + "," + project.getProjectName() + "," + project.getCreatorEmployeeId() + ",");
                if (!project.getAssignedEmployeeIds().isEmpty()) {
                    bw.write(String.join(";", project.getAssignedEmployeeIds().stream().map(Object::toString).toArray(String[]::new)));
                }
                bw.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
