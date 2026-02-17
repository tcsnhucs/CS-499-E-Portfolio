package test;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

import model.Task;

public class TaskTest {

    @Test
    @DisplayName("Id must be less than 10 char")
    void testTaskID10plus() {
        Task task = new Task("TaskName", "Test description");
        if (task.getTaskId().length() > 10) {
            fail("Task ID has more than 10 characters.");
        }
    }

    @Test
    @DisplayName("TaskName must be less than 20 char")
    void testTaskName20plus() {
        // create with a long name should throw
        assertThrows(IllegalArgumentException.class, () -> {
            new Task("ThisNameIsWayTooLongToBeValid", "desc");
        });
    }

    @Test
    @DisplayName("TaskDesc must be less than 50 char")
    void testTaskDesc50plus() {
        String longDesc = "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"; // 54 chars
        assertThrows(IllegalArgumentException.class, () -> {
            new Task("Name", longDesc);
        });
    }

    @Test
    @DisplayName("TaskName must not be null")
    void testTaskNameNull() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Task(null, "desc");
        });
    }

    @Test
    @DisplayName("TaskDesc must not be null")
    void testTaskDescNull() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Task("Name", null);
        });
    }
}
