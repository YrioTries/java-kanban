package controllers;

import java.util.ArrayList;

public class InMemoryHistoryManager<T> implements HistoryManager<T> {
    private ArrayList<T> historyList;

    public InMemoryHistoryManager(){
        historyList = new ArrayList<T>(10);
    }

    @Override
    public void add(T task) {
        historyList.add(task);
        historyControl();
    }

    @Override
    public ArrayList<T> getHistory() {return historyList;}

    private void historyControl(){
        while (historyList.size() > 10){
            historyList.removeFirst();
        }
    }
}
