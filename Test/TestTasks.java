import Classes.Epic;
import Classes.TaskManager;

import java.util.Scanner;

public class TestTasks {
    public static void testFun(){
        System.out.printf("\n%s\n", "///////////////////////////////////////////");
        System.out.printf("%s\n", "//////// Epic_SubTask_TaskMethodsTest ////////");
        System.out.printf("%s\n", "/////////////////////////////////////////// \n");
        Scanner scanner = new Scanner(System.in);
        TaskManager test = new TaskManager(scanner);
        String title;
        String description;


        test.setTaskTest("Задание", "сложноватое");
        test.setEpicTest("Эпик", "простой");
        test.
    }
}
