package service;

import model.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

class FileBackedTasksManagerTest {

    public static FileBackedTasksManager fileBackedTasksManager;
    public static Task firstTask;
    public static Task secondTask;
    public static Epic firstEpic;
    public static Epic secondEpic;
    public static Subtask firstSubtask;
    public static Subtask secondSubtask;
    String historyLine;

    @BeforeEach
    public void BeforeEach() throws IOException {
        File fileTest = File.createTempFile("fileTest", ".csv", new File("src/testFiles"));
        File fileTestHistory = File.createTempFile("fileTestHistory", ".csv", new File("src/testFiles"));
        Managers manager = new Managers();
        fileBackedTasksManager = manager.getDefaultFileBackedTaskManager(fileTest.toPath(), fileTestHistory.toPath());
        fileTest.deleteOnExit();
        fileTestHistory.deleteOnExit();
    }

    @Test
    public void loadFromHistoryFileTest() throws IOException, InvalidInputException {

        firstTask = new Task("Test addNewTask1", "Test addNewTask1 description", Statuses.NEW);
        fileBackedTasksManager.createTask(firstTask);
        secondTask = new Task("Test addNewTask2", "Test addNewTask2 description", Statuses.NEW);
        fileBackedTasksManager.createTask(secondTask);
        firstEpic = new Epic("Test addNewEpic1", "Test addNewEpic1 description");
        fileBackedTasksManager.createEpic(firstEpic);
        secondEpic = new Epic("Test addNewEpic2", "Test addNewEpic2 description");
        fileBackedTasksManager.createEpic(secondEpic);
        firstSubtask = new Subtask("Test addNewSubtask1", "Test addNewSubtask1 description", Statuses.NEW, firstEpic);
        fileBackedTasksManager.createSubTask(firstSubtask);
        secondSubtask = new Subtask("Test addNewSubtask2", "Test addNewSubtask2 description", Statuses.NEW, secondEpic);
        fileBackedTasksManager.createSubTask(secondSubtask);

        fileBackedTasksManager.getTaskById(firstTask.getId());
        fileBackedTasksManager.getSubtasksById(firstSubtask.getId());

        List<Integer> checkHistory = new ArrayList<>();
        checkHistory.add(firstTask.getId());
        checkHistory.add(firstSubtask.getId());

        fileBackedTasksManager.fromHistoryFile();
        Assertions.assertNotNull(fileBackedTasksManager.getHistory());
        Assertions.assertEquals(checkHistory.size(), fileBackedTasksManager.getHistory().size());
    }

    @Test
    public void loadFromFileTest() throws IOException{
        fileBackedTasksManager.fromFile();
        Assertions.assertNotNull(fileBackedTasksManager.getAllTasks().size());
    }

    @Test
    public void saveTest() throws IOException {
        firstTask = new Task("Test addNewTask1", "Test addNewTask1 description", Statuses.NEW);
        fileBackedTasksManager.createTask(firstTask);
        secondTask = new Task("Test addNewTask2", "Test addNewTask2 description", Statuses.NEW);
        fileBackedTasksManager.createTask(secondTask);
        firstEpic = new Epic("Test addNewEpic1", "Test addNewEpic1 description");
        fileBackedTasksManager.createEpic(firstEpic);
        secondEpic = new Epic("Test addNewEpic2", "Test addNewEpic2 description");
        fileBackedTasksManager.createEpic(secondEpic);
        firstSubtask = new Subtask("Test addNewSubtask1", "Test addNewSubtask1 description", Statuses.NEW, firstEpic);
        fileBackedTasksManager.createSubTask(firstSubtask);
        secondSubtask = new Subtask("Test addNewSubtask2", "Test addNewSubtask2 description", Statuses.NEW, secondEpic);
        fileBackedTasksManager.createSubTask(secondSubtask);
        List<String> sequenceNumberInFile = new ArrayList<>();
        FileReader fileReader = new FileReader(fileBackedTasksManager.getTasksFilePath().toFile());
        BufferedReader bufferedReader = new BufferedReader(fileReader);
        while (bufferedReader.ready()) {
            String line = bufferedReader.readLine();
            sequenceNumberInFile.add(line);
        }
        bufferedReader.close();

        List<String> sequenceNumber = new ArrayList<>();
        sequenceNumber.add("id,type,name,status,description,epic");
        sequenceNumber.add(firstTask.getId() + ",TASK" + "," + firstTask.getTitle() + "," + firstTask.getStatus() + "," +
                firstTask.getDescription() + ",");
        sequenceNumber.add(secondTask.getId() + ",TASK" + "," + secondTask.getTitle() + "," + secondTask.getStatus() + "," +
                secondTask.getDescription() + ",");
        sequenceNumber.add(firstEpic.getId() + ",EPIC" + "," + firstEpic.getTitle() + "," + firstEpic.getStatus() + "," +
                firstEpic.getDescription() + ",");
        sequenceNumber.add(secondEpic.getId() + ",EPIC" + "," + secondEpic.getTitle() + "," + secondEpic.getStatus() + "," +
                secondEpic.getDescription() + ",");
        sequenceNumber.add(firstSubtask.getId() + ",SUBTASK" + "," + firstSubtask.getTitle() + "," + firstSubtask.getStatus() +
                "," + firstSubtask.getDescription() + "," + firstSubtask.getEpicId());
        sequenceNumber.add(secondSubtask.getId() + ",SUBTASK" + "," + secondSubtask.getTitle() + "," + secondSubtask.getStatus() +
                "," + secondSubtask.getDescription() + "," + secondSubtask.getEpicId());

        Assertions.assertEquals(sequenceNumber, sequenceNumberInFile);
    }

    @Test
    public void saveHistoryTest() throws IOException, InvalidInputException {
        firstTask = new Task("Test addNewTask1", "Test addNewTask1 description", Statuses.NEW);
        fileBackedTasksManager.createTask(firstTask);
        secondTask = new Task("Test addNewTask2", "Test addNewTask2 description", Statuses.NEW);
        fileBackedTasksManager.createTask(secondTask);
        firstEpic = new Epic("Test addNewEpic1", "Test addNewEpic1 description");
        fileBackedTasksManager.createEpic(firstEpic);
        secondEpic = new Epic("Test addNewEpic2", "Test addNewEpic2 description");
        fileBackedTasksManager.createEpic(secondEpic);
        firstSubtask = new Subtask("Test addNewSubtask1", "Test addNewSubtask1 description", Statuses.NEW, firstEpic);
        fileBackedTasksManager.createSubTask(firstSubtask);
        secondSubtask = new Subtask("Test addNewSubtask2", "Test addNewSubtask2 description", Statuses.NEW, secondEpic);
        fileBackedTasksManager.createSubTask(secondSubtask);
        fileBackedTasksManager.getTaskById(firstTask.getId());
        fileBackedTasksManager.getEpicById(firstEpic.getId());
        fileBackedTasksManager.getSubtasksById(firstSubtask.getId());
        fileBackedTasksManager.saveHistory();
        fileBackedTasksManager.removeAllTasks();

        BufferedReader bufferedReader = new BufferedReader(new FileReader(fileBackedTasksManager.getHistoryFilePath().toFile()));
        while (bufferedReader.ready()) {
            historyLine = bufferedReader.readLine();
        }
        bufferedReader.close();

        String checkHistoryLine = firstTask.getId() + "," + firstEpic.getId() + "," + firstSubtask.getId() + ",";

        Assertions.assertNotNull(historyLine);
        Assertions.assertEquals(checkHistoryLine, historyLine);
    }
}