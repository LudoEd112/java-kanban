package service;

import model.Epic;
import exceptions.InvalidInputException;
import model.Subtask;
import model.Task;


import java.util.List;
import java.util.Map;

public interface TaskManager {
    void createTask(Task task);

    void updateTask(Task task);

    Task getTaskById(int id) throws InvalidInputException;

    List<Task> getAllTasks();

    void removeTaskById(int id) throws InvalidInputException;

    void removeAllTasks();

    void createEpic(Epic epic);

    void updateEpic(Epic epic);

    Epic getEpicById(int id) throws InvalidInputException;

    Map<Integer, Epic> getAllEpics();

    void removeEpicById(int id) throws InvalidInputException;

    void removeAllEpics();

    void createSubTask(Subtask subtask);

    void updateSubtask(Subtask subtask);

    Subtask getSubtasksById(int id) throws InvalidInputException;

    Map<Integer, Subtask> getAllSubtasks();

    void removeSubtaskById(int id);

    void removeAllSubtask();

    List<Task> getHistory();
}
