package service;

import exceptions.FileCreationException;
import model.*;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class FileBackedTasksManager extends InMemoryTaskManager {
    private final Path tasksFilePath;
    private final Path historyFilePath;

    public FileBackedTasksManager(Path tasksFilePath, Path historyFilePath) {
        super();
        this.historyFilePath = historyFilePath;
        this.tasksFilePath = tasksFilePath;
    }

    public Path getTasksFilePath() {
        return tasksFilePath;
    }

    public Path getHistoryFilePath() {
        return historyFilePath;
    }

    private void save() throws FileCreationException {
        List<String> lines = new ArrayList<>();
        try {
            if (!tasks.isEmpty()) {
                for (Task task : tasks.values()) {
                    lines.add(taskToString(task));
                }
            }
            if (!epics.isEmpty()) {
                for (Epic epic : epics.values()) {
                    lines.add(taskToString(epic));
                }
            }
            if (!subtasks.isEmpty()) {
                for (Subtask subtask : subtasks.values()) {
                    lines.add(taskToString(subtask));
                }
            }
            FileWriter fileWriter = new FileWriter(tasksFilePath.toFile(), StandardCharsets.UTF_8);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
            bufferedWriter.write("id,type,name,status,description,epic");
            bufferedWriter.newLine();
            for (String str : lines) {
                bufferedWriter.write(str + "\n");
            }
            bufferedWriter.close();
        } catch (IOException exp) {
            throw new FileCreationException("Ошибка создания записи");
        }
    }

    public String taskToString(Task task) {
        String str = task.getId() + "," + task.getTypesOfTasks() + "," + task.getTitle() + "," + task.getStatus() + "," + task.getDescription() + ",";
        if (task.getTypesOfTasks().equals(TypesOfTasks.SUBTASK)) {
            Subtask subtask = (Subtask) task;
            str = str + subtask.getEpicId();
        }

        return str;
    }

    void saveHistory() throws FileCreationException {
        try {
            FileWriter fileWriter = new FileWriter(historyFilePath.toFile(), StandardCharsets.UTF_8);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
            for (Task task : historyManager.getHistory()) {
                bufferedWriter.write(task.getId() + ",");
            }
            bufferedWriter.close();
        } catch (IOException exp) {
            throw new FileCreationException("Ошибка создания записи");
        }
    }

    public void fromFile() throws IOException {
        List<String> lines = new ArrayList<>();
        FileReader fileReader = new FileReader(tasksFilePath.toFile(), StandardCharsets.UTF_8);
        BufferedReader bufferedReader = new BufferedReader(fileReader);

        while (bufferedReader.ready()) {
            String line = bufferedReader.readLine();
            lines.add(line);
        }
        bufferedReader.close();
        int max = 0;
        for (int i = 1; i < lines.size(); i++) {
            int curId = Integer.parseInt(lines.get(i).split(",")[0]);
            if (max < curId) {
                max = curId;
            }
            fromString(lines.get(i));
        }
        setId(max);
        for (Subtask subtask : subtasks.values()) {
            int epicId = subtask.getEpicId();
            Epic curEpic = epics.get(epicId);
            curEpic.getEpicSubtasks().add(subtask.getId());
        }
    }

    public void fromHistoryFile() throws IOException {
        FileReader fileReader = new FileReader(historyFilePath.toFile(), StandardCharsets.UTF_8);
        BufferedReader bufferedReader = new BufferedReader(fileReader);
        String[] historyIds = new String[0];
        while (bufferedReader.ready()) {
            String line = bufferedReader.readLine();
            historyIds = line.split(",");
        }
        bufferedReader.close();
        for (String id : historyIds) {
            int curId = Integer.parseInt(id);
            if (tasks.containsKey(curId)) {
                historyManager.add(tasks.get(curId));
            }
            if (epics.containsKey(curId)) {
                historyManager.add(epics.get(curId));
            }
            if (subtasks.containsKey(curId)) {
                historyManager.add(subtasks.get(curId));
            }
        }
    }

    private void fromString(String value) {
        String[] split = value.split(",");
        Task taskStr;

        switch (split[1]) {
            case "Task":
                taskStr = new Task(split[2], split[4], Statuses.valueOf(split[3]), Integer.parseInt(split[0]));
                tasks.put(Integer.parseInt(split[0]), taskStr);
                break;
            case "Epic":
                taskStr = new Epic(split[2], split[4], Integer.parseInt(split[0]));
                taskStr.setStatus(Statuses.valueOf(split[3]));
                epics.put(Integer.parseInt(split[0]), (Epic) taskStr);
                break;
            case "Subtask":
                taskStr = new Subtask(split[2], split[4], Integer.parseInt(split[0]), Statuses.valueOf(split[3]),
                        Integer.parseInt(split[5]));
                subtasks.put(Integer.parseInt(split[0]), (Subtask) taskStr);
                break;
        }
    }



    @Override
    public void createTask(Task task) {
        super.createTask(task);
        save();
    }

    @Override
    public void updateTask(Task task) {
        super.updateTask(task);
        save();
    }

    @Override
    public Task getTaskById(int id) throws InvalidInputException {
        Task task = super.getTaskById(id);
        save();
        return task;
    }

    @Override
    public void removeTaskById(int id) throws InvalidInputException {
        super.removeTaskById(id);
        save();
    }

    @Override
    public void removeAllTasks() {
        super.removeAllTasks();
        save();
    }

    @Override
    public void createEpic(Epic epic) {
        super.createEpic(epic);
        save();
    }

    @Override
    public void updateEpic(Epic epic) {
        super.updateEpic(epic);
        save();
    }

    @Override
    public Epic getEpicById(int id) throws InvalidInputException {
        Epic epic = super.getEpicById(id);
        save();
        return epic;
    }

    @Override
    public void removeEpicById(int id) throws InvalidInputException {
        super.removeEpicById(id);
        save();
    }

    @Override
    public void removeAllEpics() {
        super.removeAllEpics();
        save();
    }

    @Override
    public void createSubTask(Subtask subtask) {
        super.createSubTask(subtask);
        save();
    }

    @Override
    public void updateSubtask(Subtask subtask) {
        super.updateSubtask(subtask);
        save();
    }

    @Override
    public Subtask getSubtasksById(int id) throws InvalidInputException {
        Subtask subtask = super.getSubtasksById(id);
        save();
        return subtask;
    }

    @Override
    public void removeSubtaskById(int id) {
        super.removeSubtaskById(id);
        save();
    }

    @Override
    public void removeAllSubtask() {
        super.removeAllSubtask();
        save();
    }
}
