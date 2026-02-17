package test.persistence;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import org.junit.jupiter.api.Test;

import model.Contact;
import model.Task;
import model.Appointment;
import service.ContactService;
import service.TaskService;
import service.AppointmentService;
import persistence.PersistenceManager;

public class PersistenceManagerTest {

    @Test
    void testSaveLoadRoundTrip() throws IOException {
        ContactService cs = new ContactService();
        TaskService ts = new TaskService();
        AppointmentService as = new AppointmentService();

        cs.createContact("A","B","1234567890","Addr");
        ts.createTask("Task1","Desc");
        as.createAppointment("2026-01-01","Meeting");

        File tmp = Files.createTempFile("test-data",".json").toFile();
        PersistenceManager.save(tmp, cs, ts, as);

        // clear
        cs.clearAll();
        ts.clearAll();
        as.clearAll();

        assertEquals(0, cs.getAllContacts().size());

        PersistenceManager.load(tmp, cs, ts, as);

        assertEquals(1, cs.getAllContacts().size());
        assertEquals("A", cs.getAllContacts().get(0).getFirstName());
        assertEquals(1, ts.getAllTasks().size());
        assertEquals("Task1", ts.getAllTasks().get(0).getTaskName());
        assertEquals(1, as.getAllAppointments().size());
        assertEquals("2026-01-01", as.getAllAppointments().get(0).getAppointmentDate());

        tmp.delete();
    }
}
