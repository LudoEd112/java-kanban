package service;

import exceptions.InvalidInputException;
import model.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
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
        File fileTest = File.createTempFile("fileTest", ".csv", new File("test/testFiles"));
        File fileTestHistory = File.createTempFile("fileTestHistory", ".csv", new File("test/testFiles"));
        Managers manager = new Managers();
        fileBackedTasksManager = manager.getDefaultFileBackedTaskManager(fileTest.toPath(), fileTestHistory.toPath());
        fileTest.deleteOnExit();
        fileTestHistory.deleteOnExit();
    }

    @Test
    public void loadFromHistoryFileTest() throws IOException, InvalidInputException {

        firstTask = new Task("Test addNewTask1", "Test addNewTask1 description", Statuses.NEW, Duration.of(30, ChronoUnit.MINUTES),
                LocalDateTime.of(2024, 7, 20, 10, 0));
        fileBackedTasksManager.createTask(firstTask);
        secondTask = new Task("Test addNewTask2", "Test addNewTask2 description", Statuses.NEW, Duration.of(10, ChronoUnit.MINUTES),
                LocalDateTime.of(2024, 8, 21, 11, 30));
        fileBackedTasksManager.createTask(secondTask);
        firstEpic = new Epic("Test addNewEpic1", "Test addNewEpic1 description", Duration.of(15, ChronoUnit.MINUTES),
                LocalDateTime.of(2024, 9, 15, 20, 15));
        fileBackedTasksManager.createEpic(firstEpic);
        secondEpic = new Epic("Test addNewEpic2", "Test addNewEpic2 description", Duration.of(30, ChronoUnit.MINUTES),
                LocalDateTime.of(2024, 3, 10, 10, 1));
        fileBackedTasksManager.createEpic(secondEpic);
        firstSubtask = new Subtask("Test addNewSubtask1", "Test addNewSubtask1 description", Statuses.NEW, firstEpic, Duration.of(30, ChronoUnit.MINUTES),
                LocalDateTime.of(2024, 9, 15, 20, 15));
        fileBackedTasksManager.createSubTask(firstSubtask);
        secondSubtask = new Subtask("Test addNewSubtask2", "Test addNewSubtask2 description", Statuses.NEW, secondEpic, Duration.of(30, ChronoUnit.MINUTES),
                LocalDateTime.of(2024, 3, 10, 10, 1));
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
    public void saveHistoryTest() throws IOException, InvalidInputException {
        firstTask = new Task("Test addNewTask1", "Test addNewTask1 description", Statuses.NEW, Duration.of(30, ChronoUnit.MINUTES),
                LocalDateTime.of(2024, 7, 20, 10, 0));
        fileBackedTasksManager.createTask(firstTask);
        secondTask = new Task("Test addNewTask2", "Test addNewTask2 description", Statuses.NEW, Duration.of(46, ChronoUnit.MINUTES),
                LocalDateTime.of(2024, 8, 21, 11, 30));
        fileBackedTasksManager.createTask(secondTask);
        firstEpic = new Epic("Test addNewEpic1", "Test addNewEpic1 description", Duration.of(60, ChronoUnit.MINUTES),
                LocalDateTime.of(2024, 9, 15, 20, 15));
        fileBackedTasksManager.createEpic(firstEpic);
        secondEpic = new Epic("Test addNewEpic2", "Test addNewEpic2 description", Duration.of(30, ChronoUnit.MINUTES),
                LocalDateTime.of(2024, 3, 10, 10, 1));
        fileBackedTasksManager.createEpic(secondEpic);
        firstSubtask = new Subtask("Test addNewSubtask1", "Test addNewSubtask1 description", Statuses.NEW, firstEpic, Duration.of(30, ChronoUnit.MINUTES),
                LocalDateTime.of(2024, 3, 15, 20, 45));
        fileBackedTasksManager.createSubTask(firstSubtask);
        secondSubtask = new Subtask("Test addNewSubtask2", "Test addNewSubtask2 description", Statuses.NEW, secondEpic, Duration.of(30, ChronoUnit.MINUTES),
                LocalDateTime.of(2024, 3, 10, 21, 55));
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