package classes;

import classes.enums.Class;
import classes.enums.Status;

import java.util.Objects;

public class Task {

    private int id;
    private String title;
    private String description;
    private Status status;

    public Task(String title, String description) {
        this.title = title;
        this.description = description;
        this.status = Status.NEW;
    }

    public classes.enums.Class getTaskClass(){
        return Class.TASK;
    }

    public void setId(Integer id) { this.id = id; }

    public Status getStatus() {
        return status;
    }

    public String getDescription() {
        return description;
    }

    public String getTitle() {
        return title;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getId() {
        return id;
    }

    @Override
    public String toString() {
        return this.getId() + "," + this.getTitle() + "," + this.getTaskClass() + "," + this.getStatus() + "," + this.getDescription();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        if (task.getId() == this.getId()) return true;

        return Objects.equals(title, task.title) && Objects.equals(description, task.description) && status == task.status;
    }

    @Override
    public int hashCode() {
        return 31 * Objects.hash(title, description, id);
    }
}
