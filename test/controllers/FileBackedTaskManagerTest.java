package controllers;

import classes.Epic;
import classes.Subtask;
import classes.Task;
import controllers.interfaces.HistoryManager;
import exeptions.ManagerSaveException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;

public class FileBackedTaskManagerTest {

    static InMemoryTaskManager test = new FileBackedTaskManager("base.csv");

    public static void main(String[] args) throws IOException, ManagerSaveException {

        if (FileBackedTaskManager.isCsvFileEmpty("base.csv")) {
            Epic epic1 = new Epic("1.Эпик", "простой");
            Subtask sub1 = new Subtask("1.1.Подзадача", "средняя");
            Subtask sub2 = new Subtask("1.2.Подзадача", "тяжелее");

            Epic epic2 = new Epic("2.Эпик", "простой");
            Subtask sub3 = new Subtask("2.1.Подзадача", "норм");

            Task task1 = new Task("1.Задание", "сложноватое");
            Task task2 = new Task("2.Задание", "сложноватое");

            test.pushEpic(epic1);
            test.pushSub(epic1, sub1);
            test.pushSub(epic1, sub2);

            test.pushEpic(epic2);
            test.pushSub(epic2, sub3);

            test.pushTask(task1);
            test.pushTask(task2);

        }
            System.out.println(test.serchTask(0));

        Task task3 = new Task("3.Задание", "добавленное третье");
        test.pushTask(task3);

        Task task4 = new Task("4.Задание", "уже четвертое!");
        test.pushTask(task4);

        System.out.println(test.serchTask(7));
    }
}
