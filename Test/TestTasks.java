import Classes.*;
import controllers.TaskManager;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Scanner;

public class TestTasks<T> {

    TaskManager test = new TaskManager();
    Epic epic1 = new Epic("1.Эпик", "простой");
    Subtask sub1 = new Subtask("1.1.Подзадача", "средняя");
    Subtask sub2 = new Subtask("1.2.Подзадача", "тяжелее");
    Epic epic2 = new Epic("2.Эпик", "простой");
    Task task1 = new Task("1.Задание", "сложноватое");
    Task task2 = new Task("2.Задание", "сложноватое");

    @Test
    public void testFun(){
        System.out.printf("\n%s\n", "///////////////////////////////////////////");
        System.out.printf("%s\n", "//////// Epic_SubTask_Task_MethodsTest ////");
        System.out.printf("%s\n", "/////////////////////////////////////////// \n");
        Scanner scanner = new Scanner(System.in);
        test.pushEpic(epic1);
        test.pushSub(epic1, sub1);
        test.pushSub(epic1, sub2);
        test.pushTask(task1);
        test.pushTask(task2);

        int id;

        System.out.println("\nПроверка метода getAllTasks():");
        test.printAllTasks();

        System.out.println("\nПроверка метода getMotherID(sub1):");
        System.out.println("Материнский id эпика подзадачи '" + sub1.getTitle() + "': id:"+ test.getMotherID(sub1.getId()) + " Эпика: " + ((Epic)test.serchTask(test.getMotherID(sub1.getId()))).getTitle());

        System.out.println("\nПроверка метода serchTask(id)");
        /////////////////////////////////////////////////////////////////////////////////////////////////
        id = epic1.getId();
        Object o = test.serchTask(id);

        if (o instanceof Epic) {
            System.out.println("Эпик найден\n" + ((Epic) o).getTitle() + " класса: " + o.getClass());

        } else if (o instanceof Task) {
            System.out.println("Задача найдена\n" + ((Task) o).getTitle() + " класса: " + o.getClass());
        }  else {
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
        ///////////////////////////////////////////////////////////////////////////////////////////

        System.out.println("\nПроверка метода getEpicList()");
        test.getEpicList();

        System.out.println("\nПроверка метода delete(3)");
        test.delete(3);
        test.printAllTasks();

        System.out.println("\nПроверка changeStatusTask()");
        System.out.println("test.changeStatusTask(Status.DONE, task2);");
        System.out.println("test.changeStatusSub(Status.IN_PROGRESS, epic1.getSubTask(sub1.getId()));");
        System.out.println("test.changeStatusEpic(epic1);\n");
        test.changeStatusTask(Status.DONE, task2);
        test.changeStatusSub(Status.IN_PROGRESS, epic1.getSubTask(sub1.getId()));
        test.changeStatusEpic(epic1);

        test.printAllTasks();;
        System.out.println();
        System.out.println("test.changeStatusSub(Status.DONE, epic1.getSubTask(sub1.getId()))");
        System.out.println("test.changeStatusSub(Status.DONE, epic1.getSubTask(sub2.getId()))");
        System.out.println("test.changeStatusEpic(epic1);\n");

        test.changeStatusSub(Status.DONE, epic1.getSubTask(sub1.getId()));
        test.changeStatusSub(Status.DONE, epic1.getSubTask(sub2.getId()));
        test.changeStatusEpic(epic1);

        test.printAllTasks();
    }
}
