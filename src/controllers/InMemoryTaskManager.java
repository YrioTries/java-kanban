package controllers;

import classes.enums.Class;
import classes.enums.Status;
import classes.tasks.Epic;
import classes.tasks.Subtask;
import classes.tasks.Task;
import controllers.interfaces.HistoryManager;
import controllers.interfaces.TaskManager;

import java.time.Duration;
import java.util.Comparator;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.TreeSet;
import java.util.stream.Collectors;

public class InMemoryTaskManager implements TaskManager {

    private int id;
    private final HistoryManager historyManager;

    public HashMap<Integer, Task> taskMaster;
    private TreeSet<Task> prioritizedTasks;

    public InMemoryTaskManager() {
        historyManager = Managers.getDefaultHistory();
        taskMaster = new HashMap<>();
        prioritizedTasks = new TreeSet<>(Comparator.comparing(Task::getStartTime, Comparator.nullsLast(Comparator.naturalOrder())));
        id = 0;
    }

    protected void setManagerId(int id) {
        this.id = id + 1;
    }

    @Override
    public ArrayList<Task> getHistory() {
        return (ArrayList<Task>) historyManager.getHistory();
    }

    @Override
    public HashMap<Integer, Task> getTaskMaster() {
        return taskMaster;
    }

    @Override
    public ArrayList<Task> getPrioritizedTasks() {
        return new ArrayList<>(prioritizedTasks);
    }

    @Override
    public int pushTask(Task task) {
        task.setId(id++);
        taskMaster.put(task.getId(), task);
        addPrioritized(task);
        return task.getId();
    }

    @Override
    public int pushEpic(Epic epic) {
        epic.setId(id++);
        taskMaster.put(epic.getId(), epic);
        return epic.getId();
    }

    @Override
    public int pushSub(Subtask sub) {
        sub.setId(id++);
        taskMaster.put(sub.getId(), sub);
        addPrioritized(sub);
        return sub.getId();
    }

    @Override
    public boolean addSubToEpic(Epic epic, Subtask sub) {
        if (epic != null && sub != null && !epic.getSubMap().containsKey(sub.getId())) {
            if (epic.getSubMap().isEmpty()) epic.setStartTime(sub.getStartTime());
            epic.getSubMap().put(sub.getId(), sub);
            sub.setMotherId(epic);
            updateEpicParam(epic);
            return epic.getSubMap().containsKey(sub.getId());
        }
        return false;
    }

    @Override
    public void updateTask(Task task) {
        if (task != null) {
            taskMaster.put(task.getId(), task);
            prioritizedTasks.removeIf(t -> t.getId() == task.getId());
            prioritizedTasks.add(task);
        }
    }

    private boolean isOverlapping(Task task1, Task task2) {
        return task1.getStartTime() != null && task2.getStartTime() != null &&
                task1.getStartTime().isBefore(task2.getEndTime()) && task1.getEndTime().isAfter(task2.getStartTime());
    }

    private void addPrioritized(Task task) {
        if (task != null) {
            for (Task tasking : prioritizedTasks) {
                if (isOverlapping(tasking, task)) {
                    throw new IllegalArgumentException(task + " Пересекается по времени выполнения с " + tasking);
                }
            }
            prioritizedTasks.add(task);
        }
    }

    @Override
    public void delete(Integer id) {
        if (taskMaster.containsKey(id)) {
            Task task = taskMaster.get(id);
            if (task != null) {
                if (task.getTaskClass() == Class.SUBTASK) {
                    Subtask sub = (Subtask) task;
                    int mId = sub.getMotherId();
                    Epic epic = (Epic) serchTask(mId);
                    if (epic != null) {
                        epic.getSubMap().remove(sub.getId());
                    }
                } else if (task.getTaskClass() == Class.EPIC) {
                    Epic epic = (Epic) task;
                    for (Integer subId : epic.getSubMap().keySet()) {
                        prioritizedTasks.remove(taskMaster.get(subId));
                        historyManager.remove(subId);
                        taskMaster.remove(subId);
                    }
                }
                if (task.getTaskClass() != Class.EPIC) prioritizedTasks.remove(task);
                historyManager.remove(id);
                taskMaster.remove(id);
            }
        }
    }

    @Override
    public void deleteTask(int id) {
        if (taskMaster.containsKey(id) && taskMaster.get(id).getTaskClass() == Class.TASK) {
            prioritizedTasks.remove(taskMaster.get(id));
            historyManager.remove(id);
            taskMaster.remove(id);
        }
    }

    @Override
    public void deleteEpic(int id) {
        if (taskMaster.containsKey(id) && taskMaster.get(id).getTaskClass() == Class.EPIC) {
            Epic epic = (Epic) taskMaster.get(id);
            for (Integer subId : epic.getSubMap().keySet()) {
                prioritizedTasks.remove(taskMaster.get(subId));
                historyManager.remove(subId);
                taskMaster.remove(subId);
            }
            historyManager.remove(id);
            taskMaster.remove(id);
        }
    }

    @Override
    public void deleteSubtask(int id) {
        if (taskMaster.containsKey(id) && taskMaster.get(id).getTaskClass() == Class.SUBTASK) {
            Subtask sub = (Subtask) taskMaster.get(id);
            int mId = sub.getMotherId();
            Epic epic = (Epic) serchTask(mId);
            if (epic != null) {
                epic.getSubMap().remove(sub.getId());
            }
            prioritizedTasks.remove(sub);
            historyManager.remove(id);
            taskMaster.remove(id);
        }
    }

    @Override
    public Task serchTask(int searchingId) {
        if (taskMaster.containsKey(searchingId)) {
            historyManager.add(taskMaster.get(searchingId));
            return taskMaster.get(searchingId);
        }
        return null;
    }

    @Override
    public ArrayList<Subtask> getSubtaskList() {
        return taskMaster.values().stream()
                .filter(task -> task.getTaskClass() == Class.SUBTASK)
                .map(task -> (Subtask) task)
                .collect(Collectors.toCollection(ArrayList::new));
    }

    @Override
    public ArrayList<Epic> getEpicList() {
        return taskMaster.values().stream()
                .filter(task -> task.getTaskClass() == Class.EPIC)
                .map(task -> (Epic) task)
                .collect(Collectors.toCollection(ArrayList::new));
    }

    @Override
    public ArrayList<Task> getTaskList() {
        return taskMaster.values().stream()
                .filter(task -> Class.TASK.equals(task.getTaskClass()))
                .collect(Collectors.toCollection(ArrayList::new));
    }

    @Override
    public void changeStatusSub(Status status, Subtask sub) {
        if (sub != null) {
            sub.setStatus(status);
            updateEpicParam((Epic) serchTask(sub.getMotherId()));
            if (status == Status.DONE) {
                sub.setDuration();
            }
        }
    }

    @Override
    public void changeStatusTask(Status status, Task task) {
        if (task != null) {
            task.setStatus(status);
            if (status == Status.DONE) {
                task.setDuration();
            }
        }
    }

    @Override
    public void updateEpicParam(Epic epic) {
        if (epic != null) {
            boolean setNew = true;
            boolean setDone = true;
            Duration duration = Duration.ZERO;

            for (Subtask sub : epic.getSubMap().values()) {
                if (sub.getStatus() != Status.NEW) {
                    setNew = false;
                }
                if (sub.getStatus() != Status.DONE) {
                    setDone = false;
                }
                if (sub.getDuration() != Duration.ZERO) duration = duration.plus(sub.getDuration());
            }

            if (setDone) {
                epic.setStatus(Status.DONE);
                epic.setDuration(duration);
            } else if (setNew) {
                epic.setStatus(Status.NEW);
            } else {
                epic.setStatus(Status.IN_PROGRESS);
            }
        }
    }
}
