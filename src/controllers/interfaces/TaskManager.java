package controllers.interfaces;

import classes.Epic;
import classes.enums.Status;
import classes.Subtask;
import classes.Task;
import exeptions.ManagerSaveException;

import java.io.IOException;
import java.util.ArrayList;

public interface TaskManager {

    ArrayList<Task> getHistory();
    ArrayList<Subtask> getSubtaskList();
    ArrayList<Epic> getEpicList();
    void delete(Integer id);
    void deleteAllSubtasks();
    void changeStatusSub(Status status, Subtask sub);
    void changeStatusTask(Status status, Task task);
    void changeStatusEpic(Epic epic);
    void updateTask(Task task);
    int pushTask(Task task) throws IOException, ManagerSaveException;
    int pushEpic(Epic epic) throws IOException, ManagerSaveException;
    int pushSub(Epic epic, Subtask sub) throws IOException, ManagerSaveException;
    Epic serchEpic(int searchingId);
    Task serchTask(int searchingId);
    Subtask serchSubtask(int searchingId);
    ArrayList<Task> getTaskList();
    Integer getMotherID(Integer id);
}
