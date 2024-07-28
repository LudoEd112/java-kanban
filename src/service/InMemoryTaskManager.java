package service;

import model.Epic;
import model.Subtask;
import model.Task;
import model.Statuses;
import exceptions.InvalidInputException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InMemoryTaskManager implements TaskManager {
    protected int id;
    protected final Map<Integer, Task> tasks;
    protected final Map<Integer, Subtask> subtasks;
    protected final Map<Integer, Epic> epics;
    protected final HistoryManager historyManager;

    public InMemoryTaskManager() {
        id = 0;
        tasks = new HashMap<>();
        subtasks = new HashMap<>();
        epics = new HashMap<>();
        historyManager = Managers.getDefaultHistory();
    }

    @Override
    public void createTask(Task task) {
        task.setId(++id);
        tasks.put(id, task);
    }

    public void setId(int newId) {
        id = newId;
    }

    @Override
    public void updateTask(Task task) {
        tasks.put(task.getId(), task);
    }

    @Override
    public Task getTaskById(int id) throws InvalidInputException {
        if (tasks.containsKey(id)) {
            historyManager.add(tasks.get(id));
            return tasks.get(id);
        } else {
            throw new InvalidInputException("Такой задачи не существует");
        }
    }

    @Override
    public List<Task> getAllTasks() {
        return new ArrayList<>(tasks.values());
    }

    @Override
    public void removeTaskById(int id) throws InvalidInputException {
        if (tasks.containsKey(id)) {
            tasks.remove(id);
        } else {
            throw new InvalidInputException("Такой задачи не существует");
        }
    }

    @Override
    public void removeAllTasks() {
        tasks.clear();
    }

    @Override
    public void createEpic(Epic epic) {
        epic.setId(++id);
        epic.setStatus(Statuses.NEW);
        epics.put(id, epic);
    }

    @Override
    public void updateEpic(Epic epic) {
        epic.setEpicSubtasks(epics.get(epic.getId()).getEpicSubtasks());
        epics.put(epic.getId(), epic);
        checkStatus(epic);
    }

    @Override
    public Epic getEpicById(int id) throws InvalidInputException {
        if (epics.containsKey(id)) {
            historyManager.add(epics.get(id));
            return epics.get(id);
        } else {
            throw new InvalidInputException("Такого эпика не существует");
        }
    }

    @Override
    public Map<Integer, Epic> getAllEpics() {
        return epics;
    }

    @Override
    public void removeEpicById(int id) throws InvalidInputException {
        if (epics.containsKey(id)) {
            Epic epic = epics.get(id);
            epics.remove(id);
            for (Integer subtaskId : epic.getEpicSubtasks()) {
                subtasks.remove(subtaskId);
            }
            epic.setEpicSubtasks(new ArrayList<>());
        } else {
            throw new InvalidInputException("Такого эпика не существует");
        }
    }

    @Override
    public void removeAllEpics() {
        epics.clear();
        subtasks.clear();
    }

    @Override
    public void createSubTask(Subtask subtask) {
        subtask.setId(++id);
        subtasks.put(id, subtask);
        subtask.getEpic().getEpicSubtasks().add(id);
        checkStatus(subtask.getEpic());

    }

    @Override
    public void updateSubtask(Subtask subtask) {
        subtasks.put(subtask.getId(), subtask);
        checkStatus(subtask.getEpic());
    }

    @Override
    public Subtask getSubtasksById(int id) throws InvalidInputException {
        if (subtasks.containsKey(id)) {
            historyManager.add(subtasks.get(id));
            return subtasks.get(id);
        } else {
            throw new InvalidInputException("Такой задачи не существует");
        }
    }

    @Override
    public Map<Integer, Subtask> getAllSubtasks() {
        return subtasks;
    }

    @Override
    public void removeSubtaskById(int id) {
        if (subtasks.containsKey(id)) {
            Epic epic = subtasks.get(id).getEpic();
            epic.getEpicSubtasks().remove((Integer) id);
            checkStatus(epic);
            subtasks.remove(id);
        }
    }

    @Override
    public void removeAllSubtask() {
        List<Epic> epicsForSubtasks = new ArrayList<>();
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

    @Override
    public List<Task> getHistory() {
        return historyManager.getHistory();
    }
}