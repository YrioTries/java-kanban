package controllers;

import classes.tasks.Epic;
import classes.tasks.Subtask;
import classes.tasks.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class InMemoryTaskManagerTest {

    static InMemoryTaskManager test;

    Epic epic1 = new Epic("1.Эпик", "простой");
    Subtask sub1 = new Subtask("1.1.Подзадача", "средняя", 15, LocalDateTime.now());
    Subtask sub2 = new Subtask("1.2.Подзадача", "тяжелее", 15, sub1.getEndTime());

    Epic epic2 = new Epic("2.Эпик", "простой");
    Subtask sub3 = new Subtask("2.1.Подзадача", "норм", 15, sub2.getEndTime());

    Task task1 = new Task("1.Задание", "сложноватое", 15, sub3.getEndTime());
    Task task2 = new Task("2.Задание", "сложноватое", 15, task1.getEndTime());

    @BeforeEach
    public void pushTasks() throws IOException {
        test = new InMemoryTaskManager();

        test.pushEpic(epic1);
        test.pushSub(sub1);
        test.pushSub(sub2);
        test.addSubToEpic(epic1, sub1);
        test.addSubToEpic(epic1, sub2);

        test.pushEpic(epic2);
        test.pushSub(sub3);
        test.addSubToEpic(epic2,sub3);

        test.pushTask(task1);
        test.pushTask(task2);
    }

    ///////////////////////////////////////////   Task Test   ///////////////////////////////////////////

    @Test
    public void shouldBeEqualsTwoSubsWithSameId() {
        assertNotEquals(sub1, sub2);

        sub1.setId(sub2.getId());

        assertNotNull(sub1);
        assertNotNull(sub2);

        assertEquals(sub1, sub2);
    }

    @Test
    public void shouldBeEqualsTwoTasksWithSameId() {
        assertNotEquals(task1, task2);

        task1.setId(task2.getId());

        assertNotNull(task1);
        assertNotNull(task2);

        assertEquals(task1, task2);
    }

    @Test
    public void shouldBeEqualsTwoEpicsWithSameId() {
        assertNotEquals(epic1, epic2, "Эпики изначально равны друг другу");
        int id = epic1.getId();
        epic1.setId(epic2.getId());

        assertNotNull(epic1);
        assertNotNull(epic2);

        assertEquals(epic1, epic2);
        epic1.setId(id);
    }

    /////////////////////////////////////////// serchTask(id) ///////////////////////////////////////////

    @Test
    void addNewTask() throws IOException { ////////////////////// Работает когда запускатся только он
        Task task3 = new Task("Test addNewTask", "Test addNewTask description", 15, task2.getEndTime());
        test.pushTask(task3);

        Task savedTask = test.serchTask(task3.getId());
        assertNotNull(savedTask, "Задача не найдена.");
        assertEquals(task3, savedTask, "Задачи не совпадают.");


        final ArrayList<Task> tasks = test.getTaskList();

        assertNotNull(tasks, "Задачи не возвращаются.");
        assertEquals(3, tasks.size(), "Неверное количество задач.");
        assertEquals(task3, tasks.get(2), "Задачи не совпадают.");
        test.deleteTask(task3.getId());
    }

    @Test
    public void shouldBeEqualsEpic1() {
        int id = epic1.getId();
        Object result = test.serchTask(id);

        assertNotNull(result);
        assertEquals(epic1, result);
    }

    @Test
    public void shouldNotBeEqualsEpic2() {
        int id = epic2.getId();
        Object result = test.serchTask(id);

        assertNotNull(result);
        assertNotEquals(epic1, result);
    }

    @Test
    public void shouldBeEqualsSub1() {
        int id = sub1.getId();
        Object result = test.serchTask(id);

        assertNotNull(result);
        assertEquals(sub1, result);
        result = null;
    }

    @Test
    public void shouldBeEqualsNull() {
        int id = 100;
        Object result = test.serchTask(id);

        assertNull(result);
    }

    ///////////////////////////////////////////// getMotherID(id) ///////////////////////////////////////////

    @Test
    public void shouldBe_0IDForSub1() {
        Integer result = sub1.getMotherId();

        assertNotNull(result);
        assertEquals(epic1.getId(), result);
        result = null;
    }

    @Test
    public void shouldBe_0IDSub2() {
        Integer result = sub2.getMotherId();

        assertNotNull(result);
        assertEquals(epic1.getId(), result);
    }

    @Test
    public void shouldBe_3IDSub3() {
        Integer result = sub3.getMotherId();

        assertNotNull(result);
        assertEquals(epic2.getId(), result) ;
    }

    @Test
    public void shouldNotBeEquals() {
        int id = sub3.getId();
        Integer result = sub3.getMotherId();

        assertNotNull(result);
        assertEquals(epic2.getId(), result) ;
    }
///////////////////////////////////////////// ============== ///////////////////////////////////////////

    @Test
    public void testGetPrioritizedTasks() throws IllegalArgumentException{
        ArrayList<Task> prioritizedTasks = test.getPrioritizedTasks();

        assertEquals(5, prioritizedTasks.size());
        assertEquals(task1, prioritizedTasks.get(3));
        assertEquals(task2, prioritizedTasks.get(4));
    }

    @Test
    public void testOverlappingTasks() {
        int count = test.getPrioritizedTasks().size();
        Task task = new Task("99.Задание", "сложноватое", 15, task1.getEndTime());
        assertThrows(IllegalArgumentException.class, () -> test.pushTask(task));
        assertEquals(count, test.getPrioritizedTasks().size());
    }
}
