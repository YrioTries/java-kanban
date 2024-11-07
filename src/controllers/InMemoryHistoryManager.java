package controllers;

import Classes.Task;
import controllers.interfaces.HistoryManager;

import java.util.Map;
import java.util.TreeMap;
import java.util.ArrayList;

public class InMemoryHistoryManager implements HistoryManager {
    private Map<Integer, Task> historyList;

    public InMemoryHistoryManager(){
        historyList = new TreeMap<Integer, Task>();
    }

    @Override
    public void add(Task task) {
        if(historyList.containsKey(task.getId())) historyList.remove(task.getId());
        historyList.put(task.getId(), task);

        historyControl();
    }

    @Override
    public void remove(int id) {
        historyList.remove(id);
        historyControl();
    }

    @Override
    public ArrayList<Task> getHistory() {
        ArrayList<Task> history = new ArrayList<>(historyList.values());
        return history;
    }

    private void historyControl(){
        while (historyList.size() > 10){
            historyList.remove(getHistory().getFirst().getId());
        }
    }
}
