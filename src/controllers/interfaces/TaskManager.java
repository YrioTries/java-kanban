package controllers.interfaces;

import classes.tasks.Epic;
import classes.enums.Status;
import classes.tasks.Subtask;
import classes.tasks.Task;

import java.util.ArrayList;

public interface TaskManager {

    ArrayList<Task> getHistory();

    ArrayList<Subtask> getSubtaskList();

    ArrayList<Epic> getEpicList();

    void delete(Integer id);

    void changeStatusSub(Status status, Subtask sub);

    void changeStatusTask(Status status, Task task);

    void updateEpicParam(Epic epic);

    void updateTask(Task task);

    int pushTask(Task task);

    int pushEpic(Epic epic);

    int pushSub(Subtask sub);

    boolean addSubToEpic(Epic epic, Subtask sub);

    Task serchTask(int searchingId);

    ArrayList<Task> getTaskList();
}
