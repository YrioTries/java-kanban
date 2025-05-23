package classes.tasks;

import classes.enums.Class;

import java.util.HashMap;
import java.time.LocalDateTime;

public class Epic extends Task {

    private HashMap<Integer, Subtask> epicSubtasks;
    private LocalDateTime endTime;

    public Epic(String title, String description) {
        super(title, description, 0, LocalDateTime.MIN);
        epicSubtasks = new HashMap<>();
        endTime = null;
    }

    @Override
    public Class getTaskClass() {
        return Class.EPIC;
    }

    public HashMap<Integer, Subtask> getSubMap() {
        return epicSubtasks;
    }

    public Subtask getSubTask(int id) {
        return epicSubtasks.get(id);
    }

    public void addSubtask(Subtask subtask) {
        if (subtask != null) {
            epicSubtasks.put(subtask.getId(), subtask);
        }
    }

    public void removeSubtask(int subtaskId) {
        epicSubtasks.remove(subtaskId);
    }
}
