package controllers.TaskManagerTest;

import Classes.Epic;
import Classes.Subtask;
import Classes.Task;

import controllers.TaskManager;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class serchTaskTest{

    static TaskManager test = new TaskManager();
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
    public void shouldBeEqualsEpic1() {
        Integer id = epic1.getId();
        Object result = null;
        if (test.taskMaster.containsKey(id)) {
            if (test.taskMaster.get(id) instanceof Task || test.taskMaster.get(id) instanceof Epic) {
                result = test.taskMaster.get(id);
            }

        } else {
            for (Object epic : test.taskMaster.values()) {
                if (epic instanceof Epic && ((Epic) epic).getSubMap().containsKey(id)) {
                    result = ((Epic) epic).getSubMap().get(id);
                }
            }
        }
        assertNotNull(result);
        assertEquals(epic1, result);
    }

    @Test
    public void shouldBeEqualsEpic2() {
        Integer id = epic2.getId();
        Object result = null;
        if (test.taskMaster.containsKey(id)) {
            if (test.taskMaster.get(id) instanceof Task || test.taskMaster.get(id) instanceof Epic) {
                result = test.taskMaster.get(id);
            }

        } else {
            for (Object epic : test.taskMaster.values()) {
                if (epic instanceof Epic && ((Epic) epic).getSubMap().containsKey(id)) {
                    result = ((Epic) epic).getSubMap().get(id);
                }
            }
        }
        assertNotNull(result);
        assertEquals(epic2, result);
    }

    @Test
    public void shouldBeEqualsSub1() {
        Integer id = sub1.getId();
        Object result = null;
        if (test.taskMaster.containsKey(id)) {
            if (test.taskMaster.get(id) instanceof Task || test.taskMaster.get(id) instanceof Epic) {
                result = test.taskMaster.get(id);
            }

        } else {
            for (Object epic : test.taskMaster.values()) {
                if (epic instanceof Epic && ((Epic) epic).getSubMap().containsKey(id)) {
                    result = ((Epic) epic).getSubMap().get(id);
                }
            }
        }
        assertNotNull(result);
        assertEquals(sub1, result);
    }

    @Test
    public void shouldNotBeEqualsSub1() {
        Integer id = sub2.getId();
        Object result = null;
        if (test.taskMaster.containsKey(id)) {
            if (test.taskMaster.get(id) instanceof Task || test.taskMaster.get(id) instanceof Epic) {
                result = test.taskMaster.get(id);
            }

        } else {
            for (Object epic : test.taskMaster.values()) {
                if (epic instanceof Epic && ((Epic) epic).getSubMap().containsKey(id)) {
                    result = ((Epic) epic).getSubMap().get(id);
                }

            }
        }
        assertNotNull(result);
        assertNotEquals(sub1, result);
    }

    @Test
    public void shouldBeEqualsNull() {
        Integer id = 15;
        Object result = null;
        if (test.taskMaster.containsKey(id)) {
            if (test.taskMaster.get(id) instanceof Task || test.taskMaster.get(id) instanceof Epic) {
                result = test.taskMaster.get(id);
            }

        } else {
            for (Object epic : test.taskMaster.values()) {
                if (epic instanceof Epic && ((Epic) epic).getSubMap().containsKey(id)) {
                    result = ((Epic) epic).getSubMap().get(id);
                }
            }
        }
        assertNull(result);
    }
}
