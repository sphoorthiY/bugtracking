package dao;

import model.Bug;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class BugDao {
    private static final String CSV_FILE_PATH = "csv/bug.csv";

    public List<Bug> readBugs() {
        List<Bug> bugs = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(CSV_FILE_PATH))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                int bugId = Integer.parseInt(data[0]);
                String bugName = data[1];
                String description = data[2];
                int projectId = Integer.parseInt(data[3]);
                Bug.BugStatus status = Bug.BugStatus.valueOf(data[4]);
                int employeeId = Integer.parseInt(data[5]);
                bugs.add(new Bug(bugId, bugName, description, projectId, status, employeeId));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bugs;
    }

    public void writeBugs(List<Bug> bugs) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(CSV_FILE_PATH))) {
            for (Bug bug : bugs) {
                bw.write(bug.getBugId() + "," + bug.getBugName() + "," + bug.getDescription() + "," +
                        bug.getProjectId() + "," + bug.getStatus() + "," + bug.getEmployeeId());
                bw.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
