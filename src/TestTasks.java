import Classes.*;
import controllers.TaskManager;

import java.util.Scanner;

public class TestTasks {
    public static void testFun(){
        System.out.printf("\n%s\n", "///////////////////////////////////////////");
        System.out.printf("%s\n", "//////// Epic_SubTask_Task_MethodsTest ////");
        System.out.printf("%s\n", "/////////////////////////////////////////// \n");
        Scanner scanner = new Scanner(System.in);
        TaskManager test = new TaskManager();
        String title;
        String description;
        int id;


        Epic epic1 = test.makeEpic("Эпик1", "простой");
        Subtask sub1 = test.makeSub(epic1, "1.Подзадача", "средняя");
        Subtask sub2 = test.makeSub(epic1, "2.Подзадача", "тяжелее");

        Epic epic2 = test.makeEpic("Эпик2", "простой");

        Task task1 = test.makeTask("Задание 1", "сложноватое");
        Task task2 = test.makeTask("Задание 2", "сложноватое");

        System.out.println("\nПроверка метода getAllTasks():");
        test.getAllTasks();

        System.out.println("\nПроверка метода getMotherID(sub1):");
        System.out.println("Материнский id эпика подзадачи '" + sub1.getTitle() + "': id:"+ test.getMotherID(sub1) + " Эпика: " + ((Epic)test.serchTask(test.getMotherID(sub1))).getTitle());

        System.out.println("\nПроверка метода serchTask(id)");
        id = epic1.getId();
        Object o = test.serchTask(id);

        if (o == Task.class) {
            System.out.println("Задача найдена\n" + ((Task) o).getTitle() + " класса: " + o.getClass());
        } else if (o.getClass() == Epic.class) {
            System.out.println("Эпик найден\n" + ((Epic) o).getTitle() + " класса: " + o.getClass());

        } else {
            System.out.println("Подзадача найдена\n" + ((Subtask)o).getTitle() + " класса: " + o.getClass());
        }

        id = task1.getId();
        o = test.serchTask(id);

        if (o.getClass() == Task.class) {
            System.out.println("Задача найдена\n" + ((Task) o).getTitle() + " класса: " + o.getClass());
        } else if (o.getClass() == Epic.class) {
            System.out.println("Эпик найден\n" + ((Epic) o).getTitle() + " класса: " + o.getClass());

        } else {
            System.out.println("Подзадача найдена\n" + ((Subtask)o).getTitle() + " класса: " + o.getClass());
        }

        System.out.println("\nПроверка метода getEpicList()");
        test.getEpicList();

        System.out.println("\nПроверка метода test.IDelete(3)");
        test.IDelete(3);
        test.getAllTasks();

        System.out.println("Проверка changeStatusTask()");
        System.out.println(" test.changeStatusTask(Status.DONE, task2);");
        System.out.println(" test.changeStatusSub(Status.IN_PROGRESS, epic1.getSubTask(sub1.getId()));");
        System.out.println(" test.changeStatusEpic(epic1);\n");
        test.changeStatusTask(Status.DONE, task2);
        test.changeStatusSub(Status.IN_PROGRESS, epic1.getSubTask(sub1.getId()));
        test.changeStatusEpic(epic1);

        test.getAllTasks();;
        System.out.println();
        System.out.println(" test.changeStatusSub(Status.DONE, epic1.getSubTask(sub1.getId()))");
        System.out.println(" test.changeStatusSub(Status.DONE, epic1.getSubTask(sub2.getId()))");
        System.out.println(" test.changeStatusEpic(epic1);\n");

        test.changeStatusSub(Status.DONE, epic1.getSubTask(sub1.getId()));
        test.changeStatusSub(Status.DONE, epic1.getSubTask(sub2.getId()));
        test.changeStatusEpic(epic1);

        test.getAllTasks();
    }
}
