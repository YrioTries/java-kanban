package Classes;

import java.util.HashMap;
import controllers.TaskManager;

public class Subtask extends Task{
    public Subtask(String title, String description) {
        super(title, description);
    }

    @Override
    public boolean correctClass() { return this.getClass() == Subtask.class; }
}
