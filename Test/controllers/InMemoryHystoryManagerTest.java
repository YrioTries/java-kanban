package controllers;

import Classes.Epic;
import Classes.Subtask;
import Classes.Task;
import Classes.enums.Class;
import controllers.interfaces.HistoryManager;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class InMemoryHystoryManagerTest {
    private final HistoryManager historyManager = Managers.getDefaultHistory();
    static InMemoryTaskManager test = new InMemoryTaskManager();

    static Epic epic1 = new Epic("1.Эпик", "простой");
    static Subtask sub1 = new Subtask("1.1.Подзадача", "средняя");
    static Subtask sub2 = new Subtask("1.2.Подзадача", "тяжелее");

    static Epic epic2 = new Epic("2.Эпик", "простой");
    static Subtask sub3 = new Subtask("2.1.Подзадача", "норм");

    static Task task1 = new Task("1.Задание", "сложноватое");
    static Task task2 = new Task("2.Задание", "сложноватое");

    @BeforeAll
    public static void pushTasks(){
        test.pushEpic(epic1);
        test.pushSub(epic1, sub1);
        test.pushSub(epic1, sub2);
        test.pushEpic(epic2);
        test.pushSub(epic2, sub3);
        test.pushTask(task1);
        test.pushTask(task2);
    }

    @Test
    void HisorySizeNeedToBeFourAfterSerching () {
        test.serchTask(task1.getId());
        test.serchTask(task2.getId());
        test.serchEpic(epic2.getId());
        test.serchEpic(epic1.getId());


        final ArrayList<Task> history = test.getHistory();
        assertNotNull(history, "История пустая.");
        assertEquals(4, history.size(), "История неполная.");
    }

    @Test
    void HisorySizeNeedToBeFourAfterRemove () {
        test.serchTask(task1.getId());
        test.serchTask(task2.getId());
        test.serchEpic(epic2.getId());
        test.serchEpic(epic1.getId());

        test.delete(epic2.getId());

        final ArrayList<Task> history = test.getHistory();
        assertNotNull(history, "История пустая.");
        assertEquals(4, history.size(), "История неполная.");
        assertEquals(epic2, history.get(3));
    }
}
