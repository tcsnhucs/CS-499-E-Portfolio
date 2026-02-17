package model;

import java.util.concurrent.atomic.AtomicLong;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Task {

    private final String taskId;
    private String taskName;
    private String taskDescription;
    private static AtomicLong idGenerator = new AtomicLong();

    public Task(String taskName, String taskDescription) {
        if (taskName == null || taskName.length() > 20) {
            throw new IllegalArgumentException("Invalid Task Name");
        }
        if (taskDescription == null || taskDescription.length() > 50) {
            throw new IllegalArgumentException("Invalid Task Description");
        }
        this.taskId = String.valueOf(idGenerator.getAndIncrement());
        this.taskName = taskName;
        this.taskDescription = taskDescription;
    }

    @JsonCreator
    public Task(@JsonProperty("taskId") String taskId, @JsonProperty("taskName") String taskName,
            @JsonProperty("taskDescription") String taskDescription) {
        if (taskName == null || taskName.length() > 20) {
            throw new IllegalArgumentException("Invalid Task Name");
        }
        if (taskDescription == null || taskDescription.length() > 50) {
            throw new IllegalArgumentException("Invalid Task Description");
        }
        this.taskId = taskId;
        this.taskName = taskName;
        this.taskDescription = taskDescription;
    }

    // Getters
    public String getTaskId() {
        return taskId;
    }

    public String getTaskName() {
        return taskName;
    }

    public String getTaskDescription() {
        return taskDescription;
    }

    // Setters
    public void setTaskName(String taskName) {
        if (taskName == null || taskName.length() > 20) {
            throw new IllegalArgumentException("Invalid Task Name");
        }
        this.taskName = taskName;
    }

    public void setTaskDescription(String taskDescription) {
        if (taskDescription == null || taskDescription.length() > 50) {
            throw new IllegalArgumentException("Invalid Task Description");
        }
        this.taskDescription = taskDescription;
    }

    public static void setIdCounter(long next) {
        idGenerator = new AtomicLong(next);
    }
}