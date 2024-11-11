package controllers;

import classes.*;
import classes.enums.Class;
import classes.enums.Status;
import controllers.interfaces.HistoryManager;
import controllers.interfaces.TaskManager;

import java.util.HashMap;
import java.util.ArrayList;

public class InMemoryTaskManager implements TaskManager {
    private int id;

    private final HistoryManager historyManager;
    public  HashMap<Integer, Task> taskMaster;

    public InMemoryTaskManager() {
        historyManager = Managers.getDefaultHistory();
        taskMaster = new HashMap<>();
        id = 0;
    }

    @Override
    public ArrayList<Task> getHistory() { return (ArrayList<Task>) historyManager.getHistory(); }

    public HashMap<Integer, Task> getTaskMaster(){
        return taskMaster;
    }

    @Override
    public void delete(Integer id) {
        if (taskMaster.containsKey(id)) {
            if (taskMaster.get(id).getTaskClass() == Class.TASK || taskMaster.get(id).getTaskClass() == Class.EPIC) {
                historyManager.add(taskMaster.remove(id));
                taskMaster.remove(id);
            } else {
                historyManager.add(((Epic)serchTask(getMotherID(id))).getSubMap().remove(id));
                ((Epic)serchTask(getMotherID(id))).getSubMap().remove(id);
            }
        }
    }

    @Override
    public void deleteAllSubtasks() {
        for (Task epic : taskMaster.values()) {
            if (epic.getTaskClass() == Class.EPIC) {
                ((Epic) epic).getSubMap().clear();
                changeStatusEpic((Epic) epic);
            }
        }
    }

    @Override
    public int pushTask(Task task) {
        final int ident = id++;
        task.setId(ident);
        taskMaster.put(task.getId(), task);

        return task.getId();
    }

    @Override
    public int pushEpic(Epic epic) {
        final int ident = id++;
        epic.setId(ident);
        taskMaster.put(epic.getId(), epic);

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
            taskMaster.put(task.getId(), task);
        }
    }

    @Override
    public Epic serchEpic(int searchingId) {
        if (taskMaster.containsKey(searchingId)) {
            if (taskMaster.get(searchingId).getTaskClass() == Class.EPIC) {
                historyManager.add(taskMaster.get(searchingId));
                return (Epic) taskMaster.get(searchingId);
            }
        }
        return null;
    }

    @Override
    public Subtask serchSubtask(int searchingId) {
        for (Task task : taskMaster.values()) {
            Epic epic = (Epic) task;
            if (epic.getTaskClass() == Class.EPIC && epic.getSubMap().containsKey(searchingId)) {
                historyManager.add(epic.getSubMap().get(searchingId));
                return epic.getSubMap().get(searchingId);
            }
        }
        return null;
    }

    @Override
    public Task serchTask(int searchingId) {
        if (taskMaster.containsKey(searchingId)) {
            if ((taskMaster.get(searchingId)).getTaskClass() == Class.TASK || (taskMaster.get(searchingId)).getTaskClass() == Class.EPIC) {
                historyManager.add(taskMaster.get(searchingId));
                return (Task) taskMaster.get(searchingId);
            }
        }
        return null;
    }

    @Override
    public ArrayList<Subtask> getSubtaskList() {
        ArrayList<Subtask> subTusk = new ArrayList<>();
        for (Task tasks : taskMaster.values()) {
            Epic epic = (Epic) tasks;
            if (epic.getTaskClass() == Class.EPIC){
                subTusk.addAll(epic.getSubMap().values());
            }
        }
        return subTusk;
    }

    @Override
    public ArrayList<Epic> getEpicList() {
        ArrayList<Epic> tasks = new ArrayList<>();
        for (int i = 0; i < id; i++){
            if (taskMaster.containsKey(i)) {
                if (taskMaster.get(i).getTaskClass() == Class.EPIC) {
                    tasks.add ((Epic) (taskMaster.get(i)));
                }
            }
        }
        return tasks;
    }

    @Override
    public ArrayList<Task> getTaskList() {
        ArrayList<Task> tasks = new ArrayList<>();
        for (int i = 0; i < id; i++){
            if (taskMaster.containsKey(i)) {
                if (taskMaster.get(i).getTaskClass() == Class.TASK ) {
                    tasks.add( taskMaster.get(i));
                }
            }
        }
        return tasks;
    }

    @Override
    public void changeStatusSub(Status status, Subtask sub) {
        sub.setStatus(status);
        changeStatusEpic((Epic) serchTask(getMotherID(sub.getId())));
    }

    @Override
    public void changeStatusTask(Status status, Task task){
        task.setStatus(status);
    }

    @Override
    public void changeStatusEpic(Epic epic) {
        boolean setNew = true;
        boolean setDone = true;

        for (Subtask sub : epic.getSubMap().values()) {
            if (sub.getStatus() != Status.NEW) {
                setNew = false;
            }
            if (sub.getStatus() != Status.DONE) {
                setDone = false;
            }
        }

        if (setDone){
            epic.setStatus(Status.DONE);
        } else if (setNew) {
            epic.setStatus(Status.NEW);

        } else {
            epic.setStatus(Status.IN_PROGRESS);
        }
    }

    @Override
    public Integer getMotherID(Integer id) {
        for (Task ep : getTaskMaster().values()) {
            if (ep.getTaskClass() == Class.EPIC) {
                Epic epic = (Epic) ep;
                for (Subtask subtask : epic.getSubMap().values()) {
                    if (id == subtask.getId()) {
                        return epic.getId();
                    }
                }
            }
        }
        return null;
    }
}
