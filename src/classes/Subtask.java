package classes;

import classes.enums.Class;

public class Subtask extends Task {

    private int motherId;

    public Subtask(String title, String description) {
        super(title, description);
    }

    @Override
    public classes.enums.Class getTaskClass() {
        return Class.SUBTASK; // Исправлено на Class.SUBTASK
    }

    public void setMotherId(Epic epic) {
        motherId = epic.getId();
    }

    public int getMotherId() {
        return motherId;
    }
}
