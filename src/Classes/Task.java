package Classes;

import java.util.Objects;

public class Task {
    private final int id;
    private String title;
    private String description;
    private Status status;

    public Task(int id, String title, String description){
        this.id = id;
        this.title = title;
        this.description = description;
        this.status = Status.NEW;
    }
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (((Task) o).getId() == this.getId()) return true;
        Task task = (Task) o;
        return Objects.equals(title, task.title) && Objects.equals(description, task.description) && status == task.status;
    }

    @Override
    public int hashCode() {
        return 31 * Objects.hash(title, description, id);
    }
}