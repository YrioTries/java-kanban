package controllers;

import Classes.Task;
import controllers.interfaces.HistoryManager;

import java.util.ArrayList;

public class InMemoryHistoryManager implements HistoryManager {
    private ArrayList<Task> historyList;

    public InMemoryHistoryManager(){
        historyList = new ArrayList<Task>(10);
    }

    @Override
    public void add(Task task) {
        historyList.add(task);
        historyControl();
    }

    @Override
    public ArrayList<Task> getHistory() {
        ArrayList<Task> history = new ArrayList<>(historyList);
        return history;
    }

    private void historyControl(){
        while (historyList.size() > 10){
            historyList.removeFirst();
        }
    }
}
