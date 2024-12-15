package classes.tasks;

import classes.enums.Class;

import java.time.LocalDateTime;

public class Subtask extends Task {

    private int motherId;

    public Subtask(String title, String description, long duration, LocalDateTime startTime) {
        super(title, description, duration, startTime);
    }

    @Override
    public Class getTaskClass() {
        return Class.SUBTASK;
    }

    public void setMotherId(Epic epic) {
        if (epic != null) {
            motherId = epic.getId();
        }
    }

    public int getMotherId() {
        return motherId;
    }
}
