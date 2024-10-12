package controllers;

import Classes.Epic;
import Classes.Status;
import Classes.Subtask;
import Classes.Task;

import java.util.ArrayList;

public interface TaskManager<T> {
    ArrayList<T> getHistory();
    void delete(Integer id);

    void deleteAllSubtasks();

    int pushTask(Task task);

    int pushEpic(Epic epic);

    int pushSub(Epic epic, Subtask sub);

    void updateTask(Task task);

    T serchTask(int ident);

    ArrayList<Subtask> getEpicSubtasks(int epicId);

    ArrayList<Epic> getEpicList();

    ArrayList<Task> getTaskList();

    void changeStatusSub(Status status, Subtask sub);

    void changeStatusTask(Status status, Task task);

    void changeStatusEpic(Epic epic);

    Integer getMotherID(Integer id);
}
