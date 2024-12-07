package classes.tasks;

import classes.enums.Class;

public class Subtask extends Task {

    private int motherId;

    public Subtask(String title, String description) {
        super(title, description);
    }

    @Override
    public Class getTaskClass() {
        return Class.SUBTASK;
    }

    public void setMotherId(Epic epic) {
        motherId = epic.getId();
    }

    public int getMotherId() {
        return motherId;
    }
}
