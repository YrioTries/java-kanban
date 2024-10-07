package Classes;

import java.util.HashMap;
import java.util.Scanner;

public class TaskManager {
    private int id;
    private String titleManager;
    private String descriptionManager;
    private Scanner scanner;
    private HashMap<Integer, Epic> EpicManager;
    private HashMap<Integer, Task> TaskMaster;


    public TaskManager(Scanner scanner){
        EpicManager = new HashMap<>();
        TaskMaster = new HashMap<>();
        this.scanner = scanner;
        id = 0;
    }

    private void deleteAll(){
        EpicManager.clear();
        TaskMaster.clear();
        System.out.println("Все задания успешно удалены");
    }

    private void deleteAllEpics(){
        EpicManager.clear();
        System.out.println("Все эпики успешно удалены");
    }

    private void deleteAllTasks(){
        TaskMaster.clear();
        System.out.println("Все задания успешно удалены");
    }

    private void IDeleteSubTask(Integer id){
        for (Epic epic : EpicManager.values()){
          if (epic.EpicTasks.containsKey(id)){
              epic.EpicTasks.remove(id);
              System.out.println("Подпункт успешно удалён");
          } else {
              System.out.println("Подзадачи с таким id не найдено");
          }
        }
    }

    private void IDeleteEpic(Integer id){
        if (EpicManager.containsKey(id)){
            EpicManager.remove(id);
            System.out.println("Эпик успешно удалён");
        } else {
            System.out.println("Эпика с таким id не найдено");
        }
    }

    private void IDeleteTask(Integer id){
        if (TaskMaster.containsKey(id)){
            TaskMaster.remove(id);
            System.out.println("Задача успешно удалена");
        } else {
            System.out.println("Задачи с таким id не найдено");
        }
    }

    public void delete(){
        int command;
        int id;

        while (true){
            System.out.println("\n1.Удалить эпик");
            System.out.println("2.Удалить подзадачу");
            System.out.println("3.Удалить задачу");
            System.out.println("4.Удалить все эпики");
            System.out.println("5.Удалить все задачи");
            System.out.println("6.Удалить всё");
            System.out.println("7.Выйти из меню");

            command = scanner.nextInt();
            switch (command){
                case 1:
                    System.out.println("Введите id");
                    id = scanner.nextInt();
                    IDeleteEpic(id);
                    break;

                case 2:
                    System.out.println("Введите id");
                    id = scanner.nextInt();
                    IDeleteSubTask(id);
                    break;

                case 3:
                    System.out.println("Введите id");
                    id = scanner.nextInt();
                    IDeleteTask(id);
                    break;

                case 4:
                    deleteAllEpics();
                    break;

                case 5:
                    deleteAllTasks();
                    break;

                case 6:
                    deleteAll();
                    return;

                case 7:
                    System.out.println("Вы вышли из меню");
                    return;

                default:
                    System.out.println("Неизвестная команда");

            }
        }

    }

    public Task makeTask(String title, String description){
        Task task = new Task(id++, title, description);
        TaskMaster.put(task.getId(), task);
        return task;
    }

    public Epic makeEpic(String title, String description){
        Epic epic = new Epic(id++, title, description);
        EpicManager.put(epic.getId(), epic);
        return epic;
    }

    public SubTask makeSub(Epic epic, String title, String description){
        SubTask sub = new SubTask(id++, title, description);
        epic.EpicTasks.put(sub.getId(), sub);
        return sub;
    }

    public void updateTask(Task task){
        TaskMaster.put(task.getId(), task);
    }

    public void serchTask(int ident){
        if (TaskMaster.containsKey(ident)){
            System.out.println("Задача найдена\n" + TaskMaster.get(ident).getTitle());
        } else if (EpicManager.containsKey(ident)) {
            System.out.println("Эпик найден\n" + EpicManager.get(ident).getTitle());
        } else {
            for (Epic epic : EpicManager.values()){
                if (epic.EpicTasks.containsKey(ident)){
                    System.out.println("Подзадача найдена\n" + epic.EpicTasks.get(ident).getTitle());
                }
            }
        }
    }

    private void getSubTuskList(Epic epic){
        for (Integer id : epic.EpicTasks.keySet()){
            System.out.println( epic.EpicTasks.get(id).getTitle() + " c id: " + id + " и статусом: "
                    + epic.EpicTasks.get(id).getStatus());
        }
    }

    private void getEpicList(int id){
        System.out.println("Эпик: " + EpicManager.get(id).getTitle() + " c id: " + id + " и статусом: "
                + EpicManager.get(id).getStatus());
    }

    private void getTaskList(int id){
        System.out.println("Задача: " + TaskMaster.get(id).getTitle() + " c id: " + id + " и статусом: "
                + TaskMaster.get(id).getStatus());
    }
    public void getAllTasks(){
        for (int i = 0; i < id; i++){
            if (EpicManager == null && TaskMaster == null){
                System.out.println("У вас нет активных задач");
                return;
            }

            if (EpicManager.containsKey(i)){
                getEpicList(i);
                getSubTuskList(EpicManager.get(i));

            } else if (TaskMaster.containsKey(i)){
                getTaskList(i);
            }
        }
    }


    public void getList(){
        int command;

        while (true){
            System.out.println("\n1.Вывести список задач");
            System.out.println("2.Вывести список эпиков");
            System.out.println("3.Вывести список подзадач эпика");
            System.out.println("4.Вывести все задачи, эпики и подзадачи");
            System.out.println("5.Выйти из меню");

            command = scanner.nextInt();
            switch (command){
                case 1:
                    for (Integer id : TaskMaster.keySet()){
                        getTaskList(id);
                    }
                    break;

                case 2:
                    for (Integer id : EpicManager.keySet()){
                        getEpicList(id);
                    }
                    break;

                case 3:
                    System.out.println("Введите id эпика");
                    id = scanner.nextInt();
                    getSubTuskList(EpicManager.get(id));
                    break;

                case 4:
                    getAllTasks();
                    break;

                case 5:
                    System.out.println("Вы вышли из меню");
                    return;

                default:
                    System.out.println("Неизвестная команда");

            }
        }

    }

    public void changeStatusSub(Status status, SubTask sub){
        sub.setStatus(status);
    }

    public void changeStatusTask(Status status, Task task){
        task.setStatus(status);
    }

    public void changeStatusEpic(Epic epic){
        boolean setNew = true;
        boolean setDone = true;

        for (SubTask sub : epic.EpicTasks.values()){
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


}
