package service;

import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;

import model.Task;

public class TaskService {

    public Map<String, Task> taskMap = new HashMap<>();

    public List<Task> getAllTasks() {
        List<Task> list = new ArrayList<>(taskMap.values());
        list.sort((a, b) -> Long.compare(Long.parseLong(a.getTaskId()), Long.parseLong(b.getTaskId())));
        return list;
    }

    public void clearAll() {
        taskMap.clear();
    }

    public void addTask(Task t) {
        taskMap.put(t.getTaskId(), t);
    }

    public void displayTaskList() {
        for (Task t : taskMap.values()) {
            System.out.println("\t Task ID: " + t.getTaskId());
            System.out.println("\t Task Name: " + t.getTaskName());
            System.out.println("\t Task Description: " + t.getTaskDescription() + "\n");
        }
    }

    public void createTask(String taskName, String taskDescription) {
        Task task = new Task(taskName, taskDescription);
        if (taskMap.containsKey(task.getTaskId())) {
            System.out.println("Task with ID " + task.getTaskId() + " already exists.");
            return;
        }
        taskMap.put(task.getTaskId(), task);
        System.out.println("Task created successfully with ID: " + task.getTaskId());
    }

    public Task getTask(String taskID) {
        return taskMap.get(taskID);
    }

    public void deleteTask(String taskID) {
        if (taskMap.remove(taskID) == null) {
            System.out.println("Task ID: " + taskID + " not found.");
        }
    }

    public void updateTaskName(String updatedName, String taskID) {
        Task t = taskMap.get(taskID);
        if (t != null) {
            t.setTaskName(updatedName);
        } else {
            System.out.println("Task ID: " + taskID + " not found.");
        }
    }

    public void updateTaskDescription(String updatedDesc, String taskID) {
        Task t = taskMap.get(taskID);
        if (t != null) {
            t.setTaskDescription(updatedDesc);
        } else {
            System.out.println("Task ID: " + taskID + " not found.");
        }
    }
}
