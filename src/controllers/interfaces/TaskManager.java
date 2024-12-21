package controllers.interfaces;

import classes.enums.Class;
import classes.tasks.Epic;
import classes.enums.Status;
import classes.tasks.Subtask;
import classes.tasks.Task;

import java.util.ArrayList;
import java.util.HashMap;

public interface TaskManager {

    ArrayList<Task> getHistory();

    public HashMap<Integer, Task> getTaskMaster();

    public ArrayList<Task> getPrioritizedTasks();

    ArrayList<Subtask> getSubtaskList();

    ArrayList<Epic> getEpicList();

    void delete(Integer id);

    public void deleteTask(int id);

    public void deleteEpic(int id);

    public void deleteSubtask(int id);

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
