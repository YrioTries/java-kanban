package controllers;

import controllers.interfaces.HistoryManager;
import controllers.interfaces.TaskManager;

public class Managers {

    public TaskManager getDefault(){
        return new InMemoryTaskManager();
    }

    public static HistoryManager getDefaultHistory(){
        return new InMemoryHistoryManager();
    }
}
