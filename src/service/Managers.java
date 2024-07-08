package service;

import java.nio.file.Path;

public class Managers {

    public static TaskManager getDefault(){
        return new InMemoryTaskManager();
    }

    public static HistoryManager getDefaultHistory(){
        return new InMemoryHistoryManager();
    }

    public FileBackedTasksManager getDefaultFileBackedTaskManager(Path file, Path fileToHistory) {
        return new FileBackedTasksManager(file, fileToHistory);
    }
}