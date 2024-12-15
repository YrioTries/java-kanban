package classes.tasks;

import classes.enums.Class;
import classes.enums.Status;
import java.util.Objects;
import java.time.Duration;
import java.time.LocalDateTime;

public class Task {

    private int id;
    private String title;
    private String description;
    private Status status;
    private LocalDateTime startTime;
    private Duration duration;

    public Task(String title, String description, long duration, LocalDateTime startTime) {
        this.title = title;
        this.description = description;
        this.status = Status.NEW;
        this.startTime = startTime;
        this.duration = Duration.ofMinutes(duration);
    }

    public LocalDateTime getEndTime() {
        if (startTime != null && duration != null) {
            return startTime.plus(duration);
        }
        return null;
    }

    public Class getTaskClass() {
        return Class.TASK;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    protected void setStartTime() {
        if (this.getTaskClass() != Class.EPIC && this.startTime == null) {
            this.startTime = LocalDateTime.now();
        }
    }

    public Duration getDuration() {
        return duration;
    }

    public void setDuration(Duration duration) {
        this.duration = duration;
    }

    public void setDuration() {
        if (this.startTime != null) {
            duration = Duration.between(startTime, this.getEndTime());
        }
    }

    @Override
    public String toString() {
        return this.getId() + "," + this.getTitle() + "," + this.getTaskClass() + "," + this.getStatus() + ","
                + this.getDescription() + "," + this.getDuration().toMinutes() + "," + this.getStartTime();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Task task = (Task) o;
        if (task.getId() == this.getId()) {
            return true;
        }

        return Objects.equals(title, task.title) && Objects.equals(description, task.description) && status == task.status;
    }

    @Override
    public int hashCode() {
        return 31 * Objects.hash(title, description, id);
    }
}
