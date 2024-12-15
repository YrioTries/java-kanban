package controllers;

import classes.tasks.Task;
import classes.HandleLinkedHashMap;
import controllers.interfaces.HistoryManager;

import java.util.*;

public class InMemoryHistoryManager implements HistoryManager {

    private HandleLinkedHashMap handleMap;

    public InMemoryHistoryManager() {
        handleMap = new HandleLinkedHashMap();
    }

    @Override
    public void add(Task task) {
        handleMap.addLast(task);
    }

    @Override
    public void remove(int id) {
        handleMap.removeNodeId(id);
    }

    @Override
    public ArrayList<Task> getHistory() {
        return handleMap.getTasks();
    }
}


