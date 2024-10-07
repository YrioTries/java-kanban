import Classes.*;

import java.util.Scanner;

public class TestTasks {
    public static void testFun(){
        System.out.printf("\n%s\n", "///////////////////////////////////////////");
        System.out.printf("%s\n", "//////// Epic_SubTask_Task_MethodsTest ////");
        System.out.printf("%s\n", "/////////////////////////////////////////// \n");
        Scanner scanner = new Scanner(System.in);
        TaskManager test = new TaskManager(scanner);
        String title;
        String description;

        Epic epic1 = test.makeEpic("Эпик", "простой");
        SubTask sub1 = test.makeSub(epic1, "1.Подзадача", "средняя");
        SubTask sub2 = test.makeSub(epic1, "2.Подзадача", "тяжелее");

        Task task1 = test.makeTask("Задание 1", "сложноватое");
        Task task2 = test.makeTask("Задание 2", "сложноватое");

        int id = epic1.getId();
        test.serchTask(id);

        id = task1.getId();
        test.serchTask(id);

        test.getList();
        test.delete();
        test.getList();

        test.changeStatusTask(Status.DONE, task2);
        test.changeStatusSub(Status.IN_PROGRESS, epic1.getSubTask(sub1.getId()));
        test.changeStatusEpic(epic1);

        test.getAllTasks();;
        System.out.println();

        test.changeStatusSub(Status.DONE, epic1.getSubTask(sub1.getId()));
        test.changeStatusSub(Status.DONE, epic1.getSubTask(sub2.getId()));
        test.changeStatusEpic(epic1);

        test.getAllTasks();
    }
}
