package test;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

import model.Task;
import service.TaskService;
import java.util.ArrayList;

public class TaskServiceTest {

    @Test
    @DisplayName("Test to Update Task Name.")
    void testUpdateTaskName() {
        TaskService service = new TaskService();
        service.createTask("TaskName", "Desc");
        String id = service.getAllTasks().get(0).getTaskId();
        service.updateTaskName("TaskName2", id);
        service.displayTaskList();
        assertEquals("TaskName2", service.getTask(id).getTaskName(), "Task name wasn't updated.");
    }

    @Test
    @DisplayName("Test to Update Task Description.")
    void testUpdateTaskDesc() {
        TaskService service = new TaskService();
        service.createTask("TaskName", "Desc");
        String id = service.getAllTasks().get(0).getTaskId();
        service.updateTaskDescription("Desc2", id);
        service.displayTaskList();
        assertEquals("Desc2", service.getTask(id).getTaskDescription(), "Task description wasn't updated.");
    }

    @Test
    @DisplayName("Test to ensure that service correctly deletes tasks.")
    void testDeleteTask() {
        TaskService service = new TaskService();
        service.createTask("TaskName", "Desc");
        String id = service.getAllTasks().get(0).getTaskId();
        service.deleteTask(id);
        ArrayList<Task> empty = new ArrayList<Task>();
        service.displayTaskList();
        assertTrue(service.getAllTasks().isEmpty(), "The task wasn't deleted.");
    }

    @Test
    @DisplayName("Test to ensure that service can add a task.")
    void testAddTask() {
        TaskService service = new TaskService();
        service.createTask("TaskName", "Desc");
        service.displayTaskList();
        String id = service.getAllTasks().get(0).getTaskId();
        assertNotNull(service.getTask(id), "Task wasn't added correctly.");
    }
}
