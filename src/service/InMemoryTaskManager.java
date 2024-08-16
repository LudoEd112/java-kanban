package service;

import exceptions.FileCreationException;
import model.Epic;
import model.Subtask;
import model.Task;
import model.Statuses;
import exceptions.InvalidInputException;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeSet;
import java.util.ArrayList;
import java.util.List;

public class InMemoryTaskManager implements TaskManager {
    private int id;
    protected final Map<Integer, Task> tasks;
    protected final Map<Integer, Subtask> subtasks;
    protected final Map<Integer, Epic> epics;
    protected final HistoryManager historyManager;
    private final TreeSet<Task> prioritizedTasks = new TreeSet<>(Comparator.comparing(Task::getStartTime));

    public InMemoryTaskManager() {
        id = 0;
        tasks = new HashMap<>();
        subtasks = new HashMap<>();
        epics = new HashMap<>();
        historyManager = Managers.getDefaultHistory();
    }

    @Override
    public void createTask(Task task) {
        task.setEndTime(task.getStartTime().plus(task.getDuration()));
        task.setId(++id);
        checkTasksIntersections(task);
        tasks.put(id, task);
        prioritizedTasks.add(task);
    }

    @Override
    public List<Task> getPrioritizedTasks() {
        return new ArrayList<>(prioritizedTasks);
    }

    @Override
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
        prioritizedTasks.remove(tasks.get(id));
        if (tasks.containsKey(id)) {
            tasks.remove(id);
        } else {
            throw new InvalidInputException("Такой задачи не существует");
        }
    }

    @Override
    public void removeAllTasks() {
        prioritizedTasks.removeIf(task -> tasks.containsKey(task.getId()));
        tasks.clear();
    }

    @Override
    public void createEpic(Epic epic) {
        epic.setEndTime(epic.getStartTime().plus(epic.getDuration()));
        epic.setId(++id);
        epic.setStatus(Statuses.NEW);
        epics.put(id, epic);
        prioritizedTasks.add(epic);
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
    public ArrayList<Subtask> getEpicSubtasks(Epic epic) {
        List<Integer> epicSubtasksId = epic.getEpicSubtasks();
        ArrayList<Subtask> epicSubtask = new ArrayList<>();
        for (Integer subtaskIds : epicSubtasksId) {
            epicSubtask.add(subtasks.get(subtaskIds));
        }
        return epicSubtask;
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
        subtask.setEndTime(subtask.getStartTime().plus(subtask.getDuration()));
        subtask.setId(++id);
        subtasks.put(id, subtask);
        subtask.getEpic().getEpicSubtasks().add(id);
        checkStatus(subtask.getEpic());
        prioritizedTasks.add(subtask);
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
        prioritizedTasks.remove(subtasks.get(id));
        if (subtasks.containsKey(id)) {
            Epic epic = subtasks.get(id).getEpic();
            epic.getEpicSubtasks().remove((Integer) id);
            checkStatus(epic);
            subtasks.remove(id);
        }
    }

    @Override
    public void removeAllSubtask() {
        prioritizedTasks.removeIf(subTask -> subtasks.containsKey(subTask.getId()));
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

    @Override
    public List<Task> getHistory() {
        return historyManager.getHistory();
    }

    private void checkTasksIntersections(Task task) {
        for (Task t : prioritizedTasks) {
            if (t.getId() == (task.getId())) {
                continue;
            }
            if ((t.getStartTime().isBefore(task.getStartTime()) && t.getEndTime().isAfter(task.getStartTime())) ||
                    (t.getStartTime().isAfter(task.getStartTime()) && t.getStartTime().isBefore(task.getEndTime())) ||
                    (t.getStartTime().equals(task.getStartTime()))) {
                throw new FileCreationException("\nПересечение новой задачи: \n" +
                        task.getTitle() + " " + task.getStartTime() + " " + task.getEndTime() +
                        "\nс задачей: \n" + t.getTitle() + " " + t.getStartTime() + " " + t.getEndTime());
            }
        }
    }

    private void timeOfTheEpics(Epic epic) {
        LocalDateTime start = null;
        LocalDateTime end = null;
        Duration duration = Duration.of(0, ChronoUnit.MINUTES);
        for (Integer subTaskId : epic.getEpicSubtasks()) {
            Subtask subTask = subtasks.get(subTaskId);
            duration = duration.plus(Duration.between(subTask.getStartTime(), subTask.getEndTime()));
            if (start == null || subTask.getStartTime().isBefore(start)) {
                start = subtasks.get(subTaskId).getStartTime();
            }
            if (end == null || subTask.getEndTime().isAfter(end)) {
                end = subtasks.get(subTaskId).getEndTime();
            }
        }
        epic.setDuration(duration);
        epic.setStartTime(start);
        epic.setEndTime(end);
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
        timeOfTheEpics(epic);
    }
}