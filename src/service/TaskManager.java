package service;

import model.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TaskManager {
    private int id;
    private final HashMap<Integer, Task> tasks;
    private final HashMap<Integer, Subtask> subtasks;
    private final HashMap<Integer, Epic> epics;

    public TaskManager() {
        id = 0;
        tasks = new HashMap<>();
        subtasks = new HashMap<>();
        epics = new HashMap<>();
    }


    public void createTask(Task task) {
        task.setId(++id);
        tasks.put(id, task);
    }


    public void updateTask(Task task) {
        tasks.put(task.getId(), task);
    }


    public Task getTaskById(int id) {
        if (tasks.containsKey(id)) {
            return tasks.get(id);
        } else {
            throw new RuntimeException("Такой задачи не существует");
        }
    }

    public List<Task> getAllTasks() {
        return new ArrayList<>(tasks.values());
    }


    public void removeTaskById(int id) {
        if (tasks.containsKey(id)) {
            tasks.remove(id);
        } else {
            throw new RuntimeException("Такой задачи не существует");
        }
    }

    public void removeAllTasks() {
        tasks.clear();
    }


    public void createEpic(Epic epic) {
        epic.setId(++id);
        epic.setStatus(Statuses.NEW);
        epics.put(id, epic);
    }

    public void updateEpic(Epic epic) {
        epic.setEpicSubtasks(epics.get(epic.getId()).getEpicSubtasks());
        epics.put(epic.getId(), epic);
        checkStatus(epic);
    }

    public Epic getEpicById(int id) {
        if (epics.containsKey(id)) {
            return epics.get(id);
        } else {
            throw new RuntimeException("Такого эпика не существует");
        }
    }

    public HashMap<Integer, Epic> getAllEpics() {
        return epics;
    }

    public void removeEpicById(int id) {
        if (epics.containsKey(id)) {
            Epic epic = epics.get(id);
            epics.remove(id);
            for (Integer subtaskId : epic.getEpicSubtasks()) {
                subtasks.remove(subtaskId);
            }
            epic.setEpicSubtasks(new ArrayList<>());
        } else {
            throw new RuntimeException("Такого эпика не существует");
        }
    }

    public void removeAllEpics() {
        epics.clear();
        subtasks.clear();
    }


    public void createSubTask(Subtask subtask) {
        subtask.setId(++id);
        subtasks.put(id, subtask);
        if (subtask.getEpicId() != 0) {
            subtask.getEpic().getEpicSubtasks().add(id);
            checkStatus(subtask.getEpic());
        }
    }

    public void updateSubtask(Subtask subtask) {
        subtasks.put(subtask.getId(), subtask);
        checkStatus(subtask.getEpic());
    }

    public Subtask getSubtasksById(int id) {
        if (subtasks.containsKey(id)) {
            return subtasks.get(id);
        } else {
            throw new RuntimeException("Такой задачи не существует");
        }
    }

    public HashMap<Integer, Subtask> getAllSubtasks() {
        return subtasks;
    }

    public void removeSubtaskById(int id) {
        if (subtasks.containsKey(id)) {
            Epic epic = subtasks.get(id).getEpic();
            epic.getEpicSubtasks().remove((Integer) id);
            checkStatus(epic);
            subtasks.remove(id);
        }
    }

    public void removeAllSubtask() {
        ArrayList<Epic> epicsForSubtasks = new ArrayList<>();
        for (Subtask subtask : subtasks.values()) {
            subtask.getEpic().setEpicSubtasks(new ArrayList<>());
            if (!epicsForSubtasks.contains(subtask.getEpic())) {
                epicsForSubtasks.add(subtask.getEpic());
            }
        }
        subtasks.clear();
        for (Epic epic : epicsForSubtasks) {
            epic.setStatus(Statuses.NEW);
        }
    }


    private void checkStatus(Epic epic) {

        if (epic.getEpicSubtasks().isEmpty()) {
            epic.setStatus(Statuses.NEW);
            return;
        }

        boolean allNew = true;
        boolean allDone = true;

        for (Integer epicSubtaskId : epic.getEpicSubtasks()) {
            Statuses status = subtasks.get(epicSubtaskId).getStatus();
            if (!status.equals(Statuses.NEW)) {
                allNew = false;
            }
            if (!status.equals(Statuses.DONE)) {
                allDone = false;
            }
        }

        if (allDone) {
            epic.setStatus(Statuses.DONE);
        } else if (allNew) {
            epic.setStatus(Statuses.NEW);
        } else {
            epic.setStatus(Statuses.IN_PROGRESS);
        }

    }

}