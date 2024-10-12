package controllers;

import Classes.*;

import java.util.HashMap;
import java.util.ArrayList;

public class InMemoryTaskManager<T> implements TaskManager<T> {
    private int id;
    private String titleManager;
    private String descriptionManager;

    public Managers<T> manager;
    public  HashMap<Integer, T> taskMaster;

    public InMemoryTaskManager() {
        manager = new Managers<>();
        taskMaster = new HashMap<>();
        id = 0;
    }

    @Override
    public ArrayList<T> getHistory() {return manager.getDefaultHistory().getHistory();}

    public HashMap<Integer, T> getTaskMaster(){
        return taskMaster;
    }

    @Override
    public void delete(Integer id) {
        if (taskMaster.containsKey(id)) {
            if (taskMaster.get(id) instanceof Task || taskMaster.get(id) instanceof Epic) {
                taskMaster.remove(id);
            }
        } else {
            ((Epic)serchTask(getMotherID(id))).getSubMap().remove(id);
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

        manager.getDefaultHistory().add((T) task);
        return task.getId();
    }

    @Override
    public int pushEpic(Epic epic) {
        final int ident = id++;
        epic.setId(ident);
        taskMaster.put(epic.getId(), (T) epic);

        manager.getDefaultHistory().add((T) epic);
        return epic.getId();
    }

    @Override
    public int pushSub(Epic epic, Subtask sub) {
        final int ident = id++;
        sub.setId(ident);

        epic.getSubMap().put(sub.getId(), sub);
        sub.setMotherId(epic);

        changeStatusEpic(epic);

        manager.getDefaultHistory().add((T) sub);
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
                manager.getDefaultHistory().add(taskMaster.get(searchingId));
                return taskMaster.get(searchingId);
            }

        } else {
            for (T epic : taskMaster.values()) {
                if (epic instanceof Epic && ((Epic) epic).getSubMap().containsKey(searchingId)) {
                    manager.getDefaultHistory().add((T) ((Epic) epic).getSubMap().get(searchingId));
                    return (T) ((Epic) epic).getSubMap().get(searchingId);
                }
            }
        }
        return null;
    }

    @Override
    public ArrayList<Subtask> getSubtaskList() {
        ArrayList<Subtask> tasks = new ArrayList<>();
        for (T epic : taskMaster.values()){
            if (epic instanceof Epic){
                tasks.addAll(((Epic)epic).getSubMap().values());
            }
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
                if (!(taskMaster.get(i) instanceof Epic)) {
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
        for (T epic : getTaskMaster().values()){
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
