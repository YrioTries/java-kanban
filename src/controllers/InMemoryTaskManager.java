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

    public HashMap<Integer, Task> getTaskMaster() {
        return taskMaster;
    }

    @Override
    public void delete(Integer id) {
        if (taskMaster.containsKey(id)) {
            if (taskMaster.get(id).getTaskClass() == Class.SUBTASK) {
                Subtask sub = (Subtask) taskMaster.get(id);
                int mId = sub.getMotherId();
                Epic epic = (Epic) serchTask(mId);
                epic.getSubMap().remove(sub.getId());
                sub = null;
                epic = null;

            } else if (taskMaster.get(id).getTaskClass() == Class.EPIC) { // Удаление сабов при удалении эпиков
                Epic epic = (Epic) taskMaster.get(id);

                for (Integer subId : epic.getSubMap().keySet()) {
                    prioritizedTasks.remove(taskMaster.get(subId));
                    historyManager.remove(subId);
                    taskMaster.remove(subId);
                }
            }
            if (taskMaster.get(id).getTaskClass() != Class.EPIC) prioritizedTasks.remove(taskMaster.get(id));
            historyManager.remove(id);
            taskMaster.remove(id);
        }
    }

    public void deleteTask(int id) {
        if (taskMaster.containsKey(id) && taskMaster.get(id).getTaskClass() == Class.TASK) {
            prioritizedTasks.remove(taskMaster.get(id));
            historyManager.remove(id);
            taskMaster.remove(id);
        }
    }

    public void deleteEpic(int id) {
        if (taskMaster.containsKey(id) && taskMaster.get(id).getTaskClass() == Class.TASK) {
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

    public void deleteSubtusk(int id) {
        if (taskMaster.containsKey(id) && taskMaster.get(id).getTaskClass() == Class.SUBTASK) {
            Subtask sub = (Subtask) taskMaster.get(id);
            int mId = sub.getMotherId();
            Epic epic = (Epic) serchTask(mId);
            epic.getSubMap().remove(sub.getId());

            prioritizedTasks.remove(sub);
            historyManager.remove(id);
            taskMaster.remove(id);

            sub = null;
            epic = null;
        }
    }

    public ArrayList<Task> getPrioritizedTasks() {
        return new ArrayList<>(prioritizedTasks);
    }

    private boolean isOverlapping(Task task1, Task task2) {
        return task1.getStartTime().isBefore(task2.getEndTime()) && task1.getEndTime().isAfter(task2.getStartTime());
    }

    @Override
    public int pushTask(Task task) {
        task.setId(id++);
        try {
            for (Task tasking : prioritizedTasks) {
                if (isOverlapping(tasking, task)) {
                    throw new IllegalArgumentException(task + " Пересекается по времени выполнения с " + tasking);
                }
            }
        } catch (IllegalArgumentException e) {
            // Логируем ошибку для отладки
            System.err.println(e.getMessage());
            throw e; // прокидаем исключение дальше
        }

        taskMaster.put(task.getId(), task);
        prioritizedTasks.add(task);
        return task.getId();
    }


    @Override
    public int pushEpic(Epic epic) {
        epic.setId(id++);
        taskMaster.put(epic.getId(), epic);
        return epic.getId();
    }
    @Override
    public boolean addSubToEpic(Epic epic, Subtask sub) {
        if (!epic.getSubMap().containsKey(sub.getId())) {
            if (epic.getSubMap().isEmpty()) epic.setStartTime(sub.getStartTime());
            epic.getSubMap().put(sub.getId(), sub);
            sub.setMotherId(epic);
            changeStatusEpic(epic);
            return epic.getSubMap().containsKey(sub.getId());
        }
        return false;
    }

    @Override
    public int pushSub(Subtask sub) {
        sub.setId(id++);

        try {
            for (Task tasking : prioritizedTasks) {
                if (isOverlapping(tasking, sub)) {
                    throw new IllegalArgumentException(sub + " Пересекается по времени выполнения с " + tasking);
                }
            }
        } catch (IllegalArgumentException e) {
            // Логируем ошибку для отладки
            System.err.println(e.getMessage());
            throw e; // прокидаем исключение дальше
        }



        taskMaster.put(sub.getId(), sub);
        prioritizedTasks.add(sub);
        return sub.getId();
    }

    @Override
    public void updateTask(Task task) {
        if (task != null) {
            try {
                for (Task tasking : prioritizedTasks) {
                    if (isOverlapping(tasking, task)) {
                        throw new IllegalArgumentException(task + " Пересекается по времени выполнения с " + tasking);
                    }
                }
            } catch (IllegalArgumentException e) {
                // Логируем ошибку для отладки
                System.err.println(e.getMessage());
                throw e; // прокидаем исключение дальше
            }

            taskMaster.put(task.getId(), task);
            prioritizedTasks.removeIf(t -> t.getId() == task.getId());
            prioritizedTasks.add(task);
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
                .filter(task ->  Class.TASK.equals(task.getTaskClass()))
                .collect(Collectors.toCollection(ArrayList::new));
    }

    @Override
    public void changeStatusSub(Status status, Subtask sub) {
        sub.setStatus(status);
        changeStatusEpic((Epic) serchTask(sub.getMotherId())); // Нужно ли в самом эпике менять статус или это один итот же объект?

        if (status == Status.DONE) {
            sub.setDuration();
        }
    }

    @Override
    public void changeStatusTask(Status status, Task task) {
        task.setStatus(status);

        if (status == Status.DONE) {
            task.setDuration();
        }
    }

    @Override
    public void changeStatusEpic(Epic epic) {
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