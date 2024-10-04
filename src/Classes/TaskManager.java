package Classes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class TaskManager {
    HashMap<Integer, Epic> EpicManager;
    HashMap<Integer, Task> TaskMaster;
    Scanner scanner;
    private int id;
    String titleManager;
    String descriptionManager;

    public TaskManager(){
        EpicManager = new HashMap<>();
        TaskMaster = new HashMap<>();
        scanner = new Scanner(System.in);
        id = 0;
    }

    private SupTask makeSubTask(){
        System.out.println("Введите название подзадачи:");
        titleManager = scanner.next();

        System.out.println("Введите описание подзадачи:");
        descriptionManager = scanner.next();

        SupTask sub = new SupTask(id++, titleManager, descriptionManager);
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
}
