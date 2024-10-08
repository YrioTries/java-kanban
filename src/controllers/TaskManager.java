package controllers;

import Classes.*;

import java.util.HashMap;
import java.util.ArrayList;

public class TaskManager {
    private int id;
    private String titleManager;
    private String descriptionManager;

    private HashMap<Integer, Object> taskMaster;

    public TaskManager() {
        taskMaster = new HashMap<>();
        id = 0;
    }

    public void delete(Integer id) {
        if (taskMaster.containsKey(id)) {
            if (taskMaster.get(id).getClass() == Task.class || taskMaster.get(id).getClass() == Epic.class) {
                taskMaster.remove(id);
            } else {
                for (Object epic : taskMaster.values()) {
                    ((Epic) epic).epicSubtasks.remove(id);
                    changeStatusEpic((Epic) epic);
                }
            }
        }
    }

    public void deleteAllSubtasks(){
        for (Object epic : taskMaster.values()) {
            if (epic.getClass() == Epic.class){
                ((Epic) epic).epicSubtasks.clear();
            }
        }
    }

    public int pushTask(Task task) {
        final int ident = id++;
        task.setId(ident);
        taskMaster.put(task.getId(), task);
        return task.getId();
    }

    public int pushEpic(Epic epic) {
        final int ident = id++;
        epic.setId(ident);
        taskMaster.put(epic.getId(), epic);
        return epic.getId();
    }

    public int pushSub(Epic epic, Subtask sub) {
        final int ident = id++;
        sub.setId(ident);
        epic.epicSubtasks.put(sub.getId(), sub);
        changeStatusEpic(epic);
        return sub.getId();
    }

    public void updateTask(Task task) {
        taskMaster.put(task.getId(), task);
    }

    public Object serchTask(int ident) {
        if (taskMaster.containsKey(ident)) {
            if (taskMaster.get(ident).getClass() == Task.class || taskMaster.get(ident).getClass() == Epic.class) {
                return taskMaster.get(ident);
            } else {
                for (Object epic : taskMaster.values()) {
                    if (((Epic) epic).epicSubtasks.containsKey(ident)) {
                        return ((Epic) epic).epicSubtasks.get(ident);
                    }
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
        for (int id : epic.epicSubtasks.keySet()) {
            tasks.add(epic.epicSubtasks.get(id));
        }
        return tasks;
    }

    public ArrayList<Task> getEpicList(){
        ArrayList<Task> tasks = new ArrayList<>();
        for (int i = 0; i < id; i++){
            if (taskMaster == null){
                return null;
            }
            if (taskMaster.containsKey(i)){
                if (taskMaster.get(i).getClass() == Epic.class) {
                    tasks.add((Task) taskMaster.get(i));
                }
            }
        }
        return tasks;
    }

    public ArrayList<Task> getTaskList(){
        ArrayList<Task> tasks = new ArrayList<>();
        for (int i = 0; i < id; i++){
            if (taskMaster == null){
                return null;
            }
            if (taskMaster.containsKey(i)){
                if (taskMaster.get(i).getClass() == Task.class) {
                    tasks.add((Task) taskMaster.get(i));
                }
            }
        }
        return tasks;
    }
    public void printAllTasks(){
        System.out.println("///////////////////////////////_ALL_TASKS_///////////////////////////////");
        for (int i = 0; i < id; i++){
            if (taskMaster == null){
                return;
            }

            if (taskMaster.containsKey(i) && taskMaster.get(i) != null) {
                if (taskMaster.containsKey(i) && taskMaster.get(i).getClass() == Task.class && taskMaster.get(i) != null) {
                    System.out.println("Задача: " + ((Task) taskMaster.get(i)).getTitle() + " c id: " + i + " и статусом: "
                            + ((Task)taskMaster.get(i)).getStatus());

                } else if (taskMaster.get(i).getClass() == Epic.class) {
                    System.out.println("\nЭпик: " + ((Epic) taskMaster.get(i)).getTitle() + " c id: " + i + " и статусом: "
                            + ((Epic)taskMaster.get(i)).getStatus());

                    for (Subtask sub: getEpicSubtasks(((Epic) taskMaster.get(i)).getId())){
                        System.out.println("Подзадача: " + sub.getTitle() + " c id: " + i + " и статусом: "
                                + sub.getStatus());
                    }
                    System.out.println();

                }
            }
        }
        System.out.println("\n////////////////////////////////////////////////////////////////////////");
    }

    public void changeStatusSub(Status status, Subtask sub){
        sub.setStatus(status);
        changeStatusEpic((Epic) serchTask(getMotherID(sub)));
    }

    public void changeStatusTask(Status status, Object task){
        ((Task) task).setStatus(status);
    }

    public void changeStatusEpic(Epic epic){
        boolean setNew = true;
        boolean setDone = true;

        for (Subtask sub : epic.epicSubtasks.values()){
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

    public Integer getMotherID (Subtask sub){
        for (Object epic : taskMaster.values()){
            for(Subtask subtask : ((Epic) epic).epicSubtasks.values()){
                if (sub.getId() == subtask.getId()){
                    return ((Epic) epic).getId();
                }
            }
        }
        return null;
    }

}
