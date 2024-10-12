package controllers;

import Classes.Epic;
import Classes.Status;
import Classes.Subtask;
import Classes.Task;

import java.util.ArrayList;

public interface TaskManager<T> {
    ArrayList<T> getHistory();

    ArrayList<Subtask> getSubtaskList();

    ArrayList<Epic> getEpicList();

    ArrayList<Task> getTaskList();

    void delete(Integer id);

    void deleteAllSubtasks();

    void changeStatusSub(Status status, Subtask sub);

    void changeStatusTask(Status status, Task task);

    void changeStatusEpic(Epic epic);

    void updateTask(Task task);

    int pushTask(Task task);

    int pushEpic(Epic epic);

    int pushSub(Epic epic, Subtask sub);

    T serchTask(int ident);

    Integer getMotherID(Integer id);
}
