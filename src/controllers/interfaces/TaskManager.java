package controllers.interfaces;

import classes.Epic;
import classes.enums.Status;
import classes.Subtask;
import classes.Task;

import java.util.ArrayList;

public interface TaskManager {

    ArrayList<Task> getHistory();

    ArrayList<Subtask> getSubtaskList();

    ArrayList<Epic> getEpicList();

    void delete(Integer id);

    void changeStatusSub(Status status, Subtask sub);

    void changeStatusTask(Status status, Task task);

    void changeStatusEpic(Epic epic);

    void updateTask(Task task);

    int pushTask(Task task);

    int pushEpic(Epic epic);

    int pushSub(Epic epic, Subtask sub);

    Task serchTask(int searchingId);

    ArrayList<Task> getTaskList();

    Integer getMotherID(Integer id);
}
