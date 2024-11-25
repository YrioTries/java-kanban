package controllers;

import classes.Epic;
import classes.Subtask;
import classes.Task;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class InMemoryHystoryManagerTest {

    static InMemoryTaskManager test = new InMemoryTaskManager();

    static Epic epic1 = new Epic("1.Эпик", "простой");
    static Subtask sub1 = new Subtask("1.1.Подзадача", "средняя");
    static Subtask sub2 = new Subtask("1.2.Подзадача", "тяжелее");

    static Epic epic2 = new Epic("2.Эпик", "простой");
    static Subtask sub3 = new Subtask("2.1.Подзадача", "норм");

    static Task task1 = new Task("1.Задание", "сложноватое");
    static Task task2 = new Task("2.Задание", "сложноватое");

    @BeforeAll
    public static void pushTasks() throws IOException {
        test.pushEpic(epic1);
        test.pushSub(epic1, sub1);
        test.pushSub(epic1, sub2);
        test.pushEpic(epic2);
        test.pushSub(epic2, sub3);
        test.pushTask(task1);
        test.pushTask(task2);
    }

    @Test
    void HisorySizeNeedToBeFourAfterSerching() {
        test.serchTask(task1.getId());
        test.serchTask(task2.getId());
        test.serchTask(epic2.getId());
        test.serchTask(epic1.getId());

        final ArrayList<Task> history = test.getHistory();
        assertNotNull(history, "История пустая.");
        assertEquals(4, history.size(), "История неполная.");
    }

    @Test
    void HisorySizeNeedToBeFourAfterRemove() {
        test.serchTask(task1.getId());
        test.serchTask(task2.getId());
        test.serchTask(epic2.getId());
        test.serchTask(epic1.getId());

        test.delete(epic2.getId());

        final ArrayList<Task> history = test.getHistory();
        assertNotNull(history, "История пустая.");
        assertEquals(4, history.size(), "История неполная.");
        assertEquals(epic2, history.get(3));
    }

    @Test
    void HisorySizeNeedToBeFourAfterRemoveTwoTimes() {
        test.serchTask(task1.getId());
        test.serchTask(task2.getId());
        test.serchTask(epic1.getId());
        test.serchTask(epic2.getId());

        test.delete(epic1.getId());
        test.delete(task2.getId());

        final ArrayList<Task> history = test.getHistory();
        assertNotNull(history, "История пустая.");
        assertEquals(4, history.size(), "История неполная.");
        assertEquals(epic1, history.get(2));
        assertEquals(task2, history.get(3));
    }

    @Test
    void HisorySizeNeedToBeFourAfterRemoveTwoAndSearchFiveTimes() {
        test.serchTask(task1.getId());
        test.serchTask(task2.getId());
        test.serchTask(epic1.getId());
        test.serchTask(epic2.getId());

        test.delete(epic1.getId());
        test.delete(task2.getId());
        test.serchTask(sub3.getId());

        final ArrayList<Task> history = test.getHistory();
        assertNotNull(history, "История пустая.");
        assertEquals(5, history.size(), "История неполная.");
        assertEquals(task2, history.get(3));
        assertEquals(sub3, history.get(4)); // sub1 sub2
    }
}
