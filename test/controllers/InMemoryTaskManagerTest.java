package controllers;

import classes.enums.Class;
import classes.tasks.Epic;
import classes.tasks.Subtask;
import classes.tasks.Task;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class InMemoryTaskManagerTest {

    static InMemoryTaskManager test = new InMemoryTaskManager();

    static Epic epic1 = new Epic("1.Эпик", "простой");
    static Subtask sub1 = new Subtask("1.1.Подзадача", "средняя");
    static Subtask sub2 = new Subtask("1.2.Подзадача", "тяжелее");

    static Epic epic2 = new Epic("2.Эпик", "простой");
    static Subtask sub3 = new Subtask("2.1.Подзадача", "норм");

    static Task task1 = new Task("1.Задание", "сложноватое");
    static Task task2 = new Task("2.Задание", "сложноватое");

    public void printAllTasks(){
        System.out.println("///////////////////////////////_ALL_TASKS_///////////////////////////////");
        for (int i = 0; i < test.taskMaster.size(); i++) {
            if (test.taskMaster.containsKey(i) && test.taskMaster.get(i) != null) {
                if (test.taskMaster.get(i) instanceof Epic) {
                    Epic epic = (Epic) test.taskMaster.get(i);
                    System.out.println("\nЭпик: " + epic.getTitle() + " c id: "
                            + epic.getId() + " и статусом: "
                            + epic.getStatus());

                    for (Subtask sub : epic.getSubMap().values()) {
                        System.out.println("Подзадача: " + sub.getTitle() + " c id: " + sub.getId() + " и статусом: "
                                + sub.getStatus());
                    }
                    System.out.println();

                } else if (test.taskMaster.get(i).getTaskClass() == Class.TASK) {
                    Task task = (Task) test.taskMaster.get(i);
                    System.out.println("Задача: " + task.getTitle() + " c id: " + task.getId()
                            + " и статусом: " + task.getStatus());
                }
            }

            System.out.println("История посешений: ");
            System.out.println("===============================");
            for (Object obj : test.getHistory()) {

                Task task = (Task) obj;
                if (task instanceof Epic) {

                    System.out.println("Эпик с id: " + task.getId());
                } else if (task instanceof Subtask) {

                    System.out.println("Подзадание с id: " + task.getId());
                } else {

                    System.out.println("Задание с id: " + task.getId());
                }
            }
            System.out.println("===============================");
        }
        System.out.println("\n////////////////////////////////////////////////////////////////////////");
    }

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
    void addNewTask() throws IOException {
        Task task3 = new Task("Test addNewTask", "Test addNewTask description");
        test.pushTask(task3);

        Task savedTask = test.serchTask(task3.getId());
        assertNotNull(savedTask, "Задача не найдена.");
        assertEquals(task3, savedTask, "Задачи не совпадают.");


        final ArrayList<Task> tasks = test.getTaskList();

        assertNotNull(tasks, "Задачи не возвращаются.");
        assertEquals(3, tasks.size(), "Неверное количество задач.");
        assertEquals(task3, tasks.get(2), "Задачи не совпадают.");
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
        assertEquals(epic1.getId(), result) ;
    }

    @Test
    public void shouldBe_0IDSub2() {
        Integer result = sub2.getMotherId();

        assertNotNull(result);
        assertEquals(epic1.getId(), result) ;
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
        assertNotEquals(epic1.getId(), result) ;
    }
///////////////////////////////////////////// ============== ///////////////////////////////////////////
}
