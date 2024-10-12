package controllers;

import Classes.*;

import java.util.HashMap;
import java.util.ArrayList;

public class TaskManager<T> {
    private int id;
    private String titleManager;
    private String descriptionManager;

    public final HashMap<Integer, T> taskMaster;

    public TaskManager() {
        taskMaster = new HashMap<>();
        id = 0;
    }

    public void delete(Integer id) {
        if (taskMaster.containsKey(id)) {
            if (taskMaster.get(id) instanceof Task || taskMaster.get(id) instanceof Epic) {
                taskMaster.remove(id);
            } else {
                ((Epic)serchTask(getMotherID(id))).getSubMap().remove(id);
            }
        }
    }

    public void deleteAllSubtasks(){
        for (T epic : taskMaster.values()) {
            if (epic instanceof Epic){
                ((Epic) epic).getSubMap().clear();
                changeStatusEpic((Epic) epic);
            }
        }
    }

    public int pushTask(Task task) {
        final int ident = id++;
        task.setId(ident);
        taskMaster.put(task.getId(), (T) task);
        return task.getId();
    }

    public int pushEpic(Epic epic) {
        final int ident = id++;
        epic.setId(ident);
        taskMaster.put(epic.getId(), (T) epic);
        return epic.getId();
    }

    public int pushSub(Epic epic, Subtask sub) {
        final int ident = id++;
        sub.setId(ident);
        epic.getSubMap().put(sub.getId(), sub);
        changeStatusEpic(epic);
        return sub.getId();
    }

    public void updateTask(Task task) {
        if (task != null){
            taskMaster.put(task.getId(), (T) task);
        }

    }

    public T serchTask(int ident) {
        if (taskMaster.containsKey(ident)) {
            if (taskMaster.get(ident) instanceof Task || taskMaster.get(ident) instanceof Epic) {
                return taskMaster.get(ident);
            }

        } else {
            for (T epic : taskMaster.values()) {
                if (epic instanceof Epic && ((Epic) epic).getSubMap().containsKey(ident)) {
                    return (T) ((Epic) epic).getSubMap().get(ident);
                }
            }
        }
        return null;
    }

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

    public ArrayList<Task> getEpicList(){
        ArrayList<Task> tasks = new ArrayList<>();
        for (int i = 0; i < id; i++){
            if (taskMaster.containsKey(i)){
                if (taskMaster.get(i) instanceof Epic) {
                    tasks.add((Task) taskMaster.get(i));
                }
            }
        }
        return tasks;
    }

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
    public void printAllTasks(){
        System.out.println("///////////////////////////////_ALL_TASKS_///////////////////////////////");
        for (int i = 0; i < id; i++){
            if (taskMaster.containsKey(i) && taskMaster.get(i) != null) {
                 if (taskMaster.get(i) instanceof Epic) {
                    System.out.println("\nЭпик: " + ((Epic) taskMaster.get(i)).getTitle() + " c id: "
                            + ((Epic) taskMaster.get(i)).getId() + " и статусом: "
                            + ((Epic)taskMaster.get(i)).getStatus());

                    for (Subtask sub: getEpicSubtasks(((Epic) taskMaster.get(i)).getId())){
                        System.out.println("Подзадача: " + sub.getTitle() + " c id: " + sub.getId() + " и статусом: "
                                + sub.getStatus());
                    }
                    System.out.println();

                } else if (taskMaster.get(i) instanceof Task) {
                     System.out.println("Задача: " + ((Task) taskMaster.get(i)).getTitle() + " c id: " + i
                             + " и статусом: " + ((Task)taskMaster.get(i)).getStatus());
                 }
            }
        }
        System.out.println("\n////////////////////////////////////////////////////////////////////////");
    }

    public void changeStatusSub(Status status, Subtask sub){
        sub.setStatus(status);
        changeStatusEpic((Epic) serchTask(getMotherID(sub.getId())));
    }

    public void changeStatusTask(Status status, Task task){
        task.setStatus(status);
    }

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

    public Integer getMotherID (Integer id){
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
