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

    private SubTask makeSubTask(){
        System.out.println("Введите название подзадачи:");
        titleManager = scanner.next();

        System.out.println("Введите описание подзадачи:");
        descriptionManager = scanner.next();

        return new SubTask(id++, titleManager, descriptionManager);
    }
    private Epic makeEpic(){
        int command = 1;
        System.out.println("Введите название эпика:");
        titleManager = scanner.next();

        System.out.println("Введите описание эпика:");
        descriptionManager = scanner.next();

        Epic epic = new Epic(id++, titleManager, descriptionManager);

        SubTask sub;
        do{
            sub = makeSubTask();
            epic.setEpicTasks(sub.getId(), sub);
            System.out.println("Чтобы добавить ещё одну подзадачу нажмите '1'");
            command = scanner.nextInt();
        } while (command == 1);

        System.out.println("Эпик успешно создан");
        return epic;
    }
    public void setEpic(){
        Epic epic = makeEpic();
        EpicManager.put(epic.getId(), epic);
    }

    public void setTask(){
        System.out.println("Введите название эпика:");
        titleManager = scanner.next();

        System.out.println("Введите описание эпика:");
        descriptionManager = scanner.next();

        Task task = new Task(id++, titleManager, descriptionManager);
        TaskMaster.put(task.getId(), task);
        System.out.println("Задача успешно создана");
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
        System.out.println("1.Удалить эпик");
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
                break;

            case 7:
                System.out.println("Вы вышли из меню");
                break;

            default:
                System.out.println("Неизвестная команда");

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
    private void getAllTasks(int lastId){
        for (int i = 0; i <= lastId; i++){
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
        System.out.println("1.Вывести список задач");
        System.out.println("2.Вывести список эпиков");
        System.out.println("3.Вывести список подзадач эпика");
        System.out.println("4.Вывести все задачи");
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
                getAllTasks(id);
                break;

            case 5:
                System.out.println("Вы вышли из меню");
                break;

            default:
                System.out.println("Неизвестная команда");

        }
    }

    public void serchTask(int ident){
        if (TaskMaster.containsKey(ident)){
            System.out.println("Задача найдена\n" + TaskMaster.get(ident).getTitle());
            //return TaskMaster.get(ident);
        } else if (EpicManager.containsKey(ident)) {
            System.out.println("Эпик найден\n" + EpicManager.get(ident).getTitle());
            //return EpicManager.get(ident);
        } else {
            for (Epic epic : EpicManager.values()){
                if (epic.EpicTasks.containsKey(ident)){
                    System.out.println("Подзадача найдена\n" + epic.EpicTasks.get(ident).getTitle());
                   //return epic.EpicTasks.get(ident);
                }
            }
        }

    }

    public void updateTask(Task task){
        TaskMaster.put(task.getId(), task);
    }

}
