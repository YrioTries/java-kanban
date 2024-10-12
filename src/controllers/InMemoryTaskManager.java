package controllers;

import Classes.*;

import java.util.HashMap;
import java.util.ArrayList;

public class InMemoryTaskManager<T> implements TaskManager<T> {
    private int id;
    private String titleManager;
    private String descriptionManager;

    public  HashMap<Integer, T> taskMaster;
    ArrayList<T> historyList;

    public InMemoryTaskManager() {
        taskMaster = new HashMap<>();
        historyList = new ArrayList<>();
        id = 0;
    }

    @Override
    public ArrayList<T> getHistory() {return historyList;}

    public void historyControl(){
        while (historyList.size() == 10){
            historyList.removeFirst();
        }
    }

    @Override
    public void delete(Integer id) {
        if (taskMaster.containsKey(id)) {
            if (taskMaster.get(id) instanceof Task || taskMaster.get(id) instanceof Epic) {
                taskMaster.remove(id);
            } else {
                ((Epic)serchTask(getMotherID(id))).getSubMap().remove(id);
            }
        }
    }

    @Override
    public void deleteAllSubtasks(){
        for (T epic : taskMaster.values()) {
            if (epic instanceof Epic){
                ((Epic) epic).getSubMap().clear();
                changeStatusEpic((Epic) epic);
            }
        }
    }

    @Override
    public int pushTask(Task task) {
        final int ident = id++;
        task.setId(ident);
        taskMaster.put(task.getId(), (T) task);
        return task.getId();
    }

    @Override
    public int pushEpic(Epic epic) {
        final int ident = id++;
        epic.setId(ident);
        taskMaster.put(epic.getId(), (T) epic);
        return epic.getId();
    }

    @Override
    public int pushSub(Epic epic, Subtask sub) {
        final int ident = id++;
        sub.setId(ident);
        epic.getSubMap().put(sub.getId(), sub);
        changeStatusEpic(epic);
        return sub.getId();
    }

    @Override
    public void updateTask(Task task) {
        if (task != null){
            taskMaster.put(task.getId(), (T) task);
        }

    }

    @Override
    public T serchTask(int searchingId) {
        if (taskMaster.containsKey(searchingId)) {
            if (taskMaster.get(searchingId) instanceof Task || taskMaster.get(searchingId) instanceof Epic) {
                historyList.add(taskMaster.get(searchingId));
                historyControl();
                return taskMaster.get(searchingId);
            }

        } else {
            for (T epic : taskMaster.values()) {
                if (epic instanceof Epic && ((Epic) epic).getSubMap().containsKey(searchingId)) {
                    historyList.add((T) ((Epic) epic).getSubMap().get(searchingId));
                    historyControl();
                    return (T) ((Epic) epic).getSubMap().get(searchingId);
                }
            }
        }
        return null;
    }

    @Override
    public ArrayList<Subtask> getEpicSubtasks(int epicId) {
        ArrayList<Subtask> tasks = new ArrayList<>();
        Epic epic = (Epic) serchTask(epicId);
        if (epic == null) {
            return null;
        }
        for (Integer id : epic.getSubMap().keySet()) {
            tasks.add(epic.getSubMap().get(id));
        }
        return tasks;
    }

    @Override
    public ArrayList<Epic> getEpicList(){
        ArrayList<Epic> tasks = new ArrayList<>();
        for (int i = 0; i < id; i++){
            if (taskMaster.containsKey(i)){
                if (taskMaster.get(i) instanceof Epic) {
                    tasks.add ((Epic) (taskMaster.get(i)));
                }
            }
        }
        return tasks;
    }

    @Override
    public ArrayList<Task> getTaskList(){
        ArrayList<Task> tasks = new ArrayList<>();
        for (int i = 0; i < id; i++){
            if (taskMaster.containsKey(i)){
                if (taskMaster.get(i) instanceof Task) {
                    tasks.add((Task) taskMaster.get(i));
                }
            }
        }
        return tasks;
    }


    @Override
    public void changeStatusSub(Status status, Subtask sub){
        sub.setStatus(status);
        changeStatusEpic((Epic) serchTask(getMotherID(sub.getId())));
    }

    @Override
    public void changeStatusTask(Status status, Task task){
        task.setStatus(status);
    }

    @Override
    public void changeStatusEpic(Epic epic){
        boolean setNew = true;
        boolean setDone = true;

        for (Subtask sub : epic.getSubMap().values()){
            if (sub.getStatus() != Status.NEW){
                setNew = false;
            }
            if (sub.getStatus() != Status.DONE){
                setDone = false;
            }
        }

        if(setDone){
            epic.setStatus(Status.DONE);
        } else if (setNew) {
            epic.setStatus(Status.NEW);

        } else {
            epic.setStatus(Status.IN_PROGRESS);
        }
    }

    @Override
    public Integer getMotherID(Integer id){
        for (T epic : taskMaster.values()){
            if ( epic instanceof Epic){
                for(Subtask subtask : ((Epic) epic).getSubMap().values()){
                    if (id == subtask.getId()){
                        return ((Epic) epic).getId();
                    }
                }
            }
        }
        return null;
    }

}
