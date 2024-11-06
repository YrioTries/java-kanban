package controllers;

import Classes.*;
import Classes.Class;

import java.util.HashMap;
import java.util.ArrayList;

public class InMemoryTaskManager<Tasks> implements TaskManager<Tasks> {
    private int id;

// Я сделая рефакторинг во время двухнедельного перерыва, просто боюсь запутаться и не успеть до завтра
    private final HistoryManager<Tasks> historyManager;
    public  HashMap<Integer, Tasks> taskMaster;

    public InMemoryTaskManager() {
        historyManager = Managers.getDefaultHistory();
        taskMaster = new HashMap<>();
        id = 0;
    }

    @Override
    public ArrayList<Tasks> getHistory() {return historyManager.getHistory();}

    public HashMap<Integer, Tasks> getTaskMaster(){
        return taskMaster;
    }

    @Override
    public void delete(Integer id) {
        if (taskMaster.containsKey(id)) {
            if (((Task)taskMaster.get(id)).getTaskClass() == Class.TASK || ((Epic)taskMaster.get(id)).getTaskClass() == Class.EPIC) {
                taskMaster.remove(id);
            }
        } else {
            ((Epic)serchTask(getMotherID(id))).getSubMap().remove(id);
        }
    }

    @Override
    public void deleteAllSubtasks(){
        for (Tasks epic : taskMaster.values()) {
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
        taskMaster.put(task.getId(), (Tasks) task);

        return task.getId();
    }

    @Override
    public int pushEpic(Epic epic) {
        final int ident = id++;
        epic.setId(ident);
        taskMaster.put(epic.getId(), (Tasks) epic);

        return epic.getId();
    }

    @Override
    public int pushSub(Epic epic, Subtask sub) {
        final int ident = id++;
        sub.setId(ident);

        epic.getSubMap().put(sub.getId(), sub);
        sub.setMotherId(epic);

        changeStatusEpic(epic);

        return sub.getId();
    }

    @Override
    public void updateTask(Task task) {
        if (task != null){
            taskMaster.put(task.getId(), (Tasks) task);
        }

    }

    /*@Override
    public Task serchTask(int searchingId) {
        if (taskMaster.containsKey(searchingId)) {
            if (((Task)taskMaster.get(searchingId)).getTaskClass() == Class.TASK || ((Epic)taskMaster.get(searchingId)).getTaskClass() == Class.EPIC) {
                historyManager.add(taskMaster.get(searchingId));
                return taskMaster.get(searchingId);
            }

        } else {
            for (Tasks tasks : taskMaster.values()) {
                Epic epic = (Epic) tasks;
                if (epic.getTaskClass() == Class.EPIC && epic.getSubMap().containsKey(searchingId)) {
                    historyManager.add(epic.getSubMap().get(searchingId));
                    return epic.getSubMap().get(searchingId);
                }
            }
        }
        return null;
    }*/

    @Override
    public Epic serchEpic(int searchingId) {
        if (taskMaster.containsKey(searchingId)) {
            if (((Epic) taskMaster.get(searchingId)).getTaskClass() == Class.EPIC) {
                historyManager.add(taskMaster.get(searchingId));
                return (Epic) taskMaster.get(searchingId);
            }
        }
        return null;
    }

    @Override
    public ArrayList<Subtask> getSubtaskList() {
        ArrayList<Subtask> subTusk = new ArrayList<>();
        for (Tasks tasks : taskMaster.values()){
            Epic epic = (Epic) tasks;
            if (epic.getTaskClass() == Class.EPIC){
                subTusk.addAll(epic.getSubMap().values());
            }
        }
        return subTusk;
    }

    @Override
    public Task serchTask(int searchingId) {
        if (taskMaster.containsKey(searchingId)) {
            if (((Task) taskMaster.get(searchingId)).getTaskClass() == Class.TASK || ((Epic) taskMaster.get(searchingId)).getTaskClass() == Class.EPIC) {
                historyManager.add(taskMaster.get(searchingId));
                return (Task) taskMaster.get(searchingId);
            }

        }
        return null;
    }

    @Override
    public ArrayList<Epic> getEpicList(){
        ArrayList<Epic> tasks = new ArrayList<>();
        for (int i = 0; i < id; i++){
            if (taskMaster.containsKey(i)){
                if (((Epic)taskMaster.get(i)).getTaskClass() == Class.EPIC) {
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
                if (!( ((Task)taskMaster.get(i)).getTaskClass() == Class.TASK )) {
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
        for (Tasks tasks : getTaskMaster().values()){
            Epic epic = (Epic) tasks;
            if (epic.getTaskClass() == Class.EPIC){
                for(Subtask subtask : epic.getSubMap().values()){
                    if (id == subtask.getId()){
                        return epic.getId();
                    }
                }
            }
        }
        return null;
    }

}
