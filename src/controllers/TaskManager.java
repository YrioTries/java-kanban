package controllers;

import Classes.*;

import java.util.HashMap;

public class TaskManager {
    private int id;
    private String titleManager;
    private String descriptionManager;

    private HashMap<Integer, Object> taskMaster;


    public TaskManager() {
        taskMaster = new HashMap<>();
        id = 0;
    }

    public void deleteAll() {
        taskMaster.clear();
        System.out.println("Все задания успешно удалены");
    }

    public void deleteAllEpics() {
        taskMaster.clear();
        System.out.println("Все эпики успешно удалены");
    }

    public void deleteAllTasks() {
        taskMaster.clear();
        System.out.println("Все задания успешно удалены");
    }

    public void IDeleteEpic(Integer id) {
        if (taskMaster.containsKey(id)) {
            taskMaster.remove(id);
            System.out.println("Эпик успешно удалён");
        } else {
            System.out.println("Эпика с таким id не найдено");
        }
    }

    public void IDelete(Integer id) {

        if (taskMaster.containsKey(id)) {
            if (taskMaster.get(id).getClass() == Task.class) {
                System.out.println("Задача '" + ((Task)taskMaster.get(id)).getTitle() + "' успешно удалена");
                taskMaster.remove(id);
            } else if (taskMaster.get(id).getClass() == Epic.class) {
                System.out.println("Эпик '" + ((Epic)taskMaster.get(id)).getTitle() + "' успешно удалена");
                taskMaster.remove(id);
            } else {
                for (Object epic : taskMaster.values()) {
                    ((Epic) epic).epicSubtasks.remove(id);
                    System.out.println("Подпункт успешно удалён");
                }
            }
        } else {
            System.out.println("Нет такой задачи");

        }
    }

    public Task makeTask(String title, String description) {
        Task task = new Task(id++, title, description);
        taskMaster.put(task.getId(), task);
        return task;
    }

    public Epic makeEpic(String title, String description) {
        Epic epic = new Epic(id++, title, description);
        taskMaster.put(epic.getId(), epic);
        return epic;
    }

    public Subtask makeSub(Epic epic, String title, String description) {
        Subtask sub = new Subtask(id++, title, description);
        epic.epicSubtasks.put(sub.getId(), sub);
        return sub;
    }

    public void updateTask(Task task) {
        taskMaster.put(task.getId(), task);
    }

    public Object serchTask(int ident) {

        if (taskMaster.containsKey(ident)) {
            if (taskMaster.get(ident).getClass() == Task.class) {
                return ((Task) taskMaster.get(ident));
            } else if (taskMaster.get(ident).getClass() == Epic.class) {
                return ((Epic) taskMaster.get(ident));
            } else {
                for (Object epic : taskMaster.values()) {
                    if (((Epic) epic).epicSubtasks.containsKey(ident)) {
                        return ((Epic) epic).epicSubtasks.get(ident);
                    }
                }
            }
        } else {
            System.out.println("Нет такой задачи");

        }
        return null;
    }


    private void getSubTuskList(Epic epic){
        for (Integer id : epic.epicSubtasks.keySet()){
            System.out.println(epic.epicSubtasks.get(id).getTitle() + " c id: " + id + " и статусом: "
                    + epic.epicSubtasks.get(id).getStatus());
        }
    }

    public void getEpicList(){
        for (int i = 0; i < id; i++){
            if (taskMaster == null){
                System.out.println("У вас нет активных эпиков");
                return;
            }
            if (taskMaster.containsKey(i)){
                if (taskMaster.get(i).getClass() == Epic.class) {
                    System.out.println("Эпик: " + ((Epic)taskMaster.get(id)).getTitle() + " c id: " + id + " и статусом: "
                            + ((Epic)taskMaster.get(id)).getStatus());
                    getSubTuskList((((Epic) taskMaster.get(i))));
                }
            }
        }
    }

    public void getTaskList(){
        for (int i = 0; i < id; i++){
            if (taskMaster == null){
                System.out.println("У вас нет активных задач");
                return;
            }

            if(taskMaster.containsKey(i)){
                if (taskMaster.get(i).getClass() == Task.class) {
                    System.out.println("Задача: " + ((Task) taskMaster.get(id)).getTitle() + " c id: " + id + " и статусом: "
                            + ((Task) taskMaster.get(id)).getStatus());
                }
            }
        }
    }
    public void getAllTasks(){
        for (int i = 0; i < id; i++){
            if (taskMaster == null){
                System.out.println("У вас нет активных задач");
                return;
            }

            if (taskMaster.containsKey(i) && taskMaster.get(i) != null) {
                if (taskMaster.get(i).getClass() == Task.class && taskMaster.get(i) != null) {
                    System.out.println("Задача: " + ((Task) taskMaster.get(id)).getTitle() + " c id: " + id + " и статусом: "
                            + ((Task)taskMaster.get(id)).getStatus());

                } else if (taskMaster.get(i).getClass() == Epic.class) {
                    System.out.println("Эпик: " + ((Epic) taskMaster.get(id)).getTitle() + " c id: " + id + " и статусом: "
                            + ((Epic)taskMaster.get(id)).getStatus());
                    getSubTuskList((((Epic) taskMaster.get(i))));
                }
            }
        }
    }

    public void changeStatusSub(Status status, Object sub){
        ((Subtask) sub).setStatus(status);
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
