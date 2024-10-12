package controllers.TaskManagerTest;

import Classes.Epic;
import Classes.Subtask;
import Classes.Task;
import controllers.TaskManager;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class getMotherIDTest {
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
    public void shouldBe_0IDSub1(){
        Integer id = sub1.getId();
        Integer result = null;

        for (Object epic : test.taskMaster.values()){
            if ( epic instanceof Epic){
                for(Subtask subtask : ((Epic) epic).getSubMap().values()){
                    if (id == subtask.getId()){
                        result = ((Epic) epic).getId();
                    }
                }
            }
        }
        assertNotNull(result);
        assertEquals(0, result) ;
    }

    @Test
    public void shouldBe_0IDSub2(){
        Integer id = sub2.getId();
        Integer result = null;

        for (Object epic : test.taskMaster.values()){
            if ( epic instanceof Epic){
                for(Subtask subtask : ((Epic) epic).getSubMap().values()){
                    if (id == subtask.getId()){
                        result = ((Epic) epic).getId();
                    }
                }
            }
        }
        assertNotNull(result);
        assertEquals(0, result) ;

    }

    @Test
    public void shouldBe_3IDSub3(){
        Integer id = sub3.getId();
        Integer result = null;

        for (Object epic : test.taskMaster.values()){
            if ( epic instanceof Epic){
                for(Subtask subtask : ((Epic) epic).getSubMap().values()){
                    if (id == subtask.getId()){
                        result = ((Epic) epic).getId();
                    }
                }
            }
        }
        assertNotNull(result);
        assertEquals(3, result) ;
    }

    @Test
    public void shouldBeNull(){
        Integer id = 15;
        Integer result = null;

        for (Object epic : test.taskMaster.values()){
            if ( epic instanceof Epic){
                for(Subtask subtask : ((Epic) epic).getSubMap().values()){
                    if (id == subtask.getId()){
                        result = ((Epic) epic).getId();
                    }
                }
            }
        }
        assertNull(result);
    }
}
