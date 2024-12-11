package classes.tasks;

import classes.enums.Class;

import java.time.chrono.ChronoLocalDateTime;
import java.util.HashMap;
import java.time.LocalDateTime;

public class Epic extends Task {

    private HashMap<Integer, Subtask> epicSubtasks;
    private LocalDateTime endTime;

    public Epic(String title, String description) {
        super(title, description);
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
        epicSubtasks.put(subtask.getId(), subtask);
    }

    public void removeSubtask(int subtaskId) {
        epicSubtasks.remove(subtaskId);
    }
}
