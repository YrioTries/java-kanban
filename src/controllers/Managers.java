package controllers;

public class Managers<T> {

    private InMemoryHistoryManager<T> historyManager;

    public Managers(){

        historyManager = new InMemoryHistoryManager<>();
    }

    public TaskManager<T> getDefault(){
        return new InMemoryTaskManager<>();
    }

    public HistoryManager<T> getDefaultHistory(){
        return historyManager;
    }
}
