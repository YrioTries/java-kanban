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

        SubTask sub = new SubTask(id++, titleManager, descriptionManager);
        return sub;
    }
    private Epic makeEpic(){
        int command = 1;
        System.out.println("Введите название эпика:");
        titleManager = scanner.next();

        System.out.println("Введите описание эпика:");
        descriptionManager = scanner.next();

        Epic epic = new Epic(id++, titleManager, descriptionManager);

        do{
            epic.setEpicTasks(id++, makeSubTask());
            System.out.println("Чтобы добавить ещё одну подзадачу нажмите '1'");
            command = scanner.nextInt();
        } while (command == 1);

        System.out.println("Эпик успешно создан");
        return epic;
    }
    public void setEpic(){
        EpicManager.put(id++,makeEpic());
    }

    public void setTask(){
        System.out.println("Введите название эпика:");
        titleManager = scanner.next();

        System.out.println("Введите описание эпика:");
        descriptionManager = scanner.next();

        Task task = new Task(titleManager, descriptionManager);
        TaskMaster.put(id++, task);
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

            default:
                System.out.println("Неизвестная команда");

        }
    }

}
