package test;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

import model.Appointment;
import service.AppointmentService;
import java.util.ArrayList;

public class AppointmentServiceTest {

    @Test
    @DisplayName("Test to Update Appointment Date.")
    void testUpdateAppointmentDate() {
        AppointmentService service = new AppointmentService();
        service.createAppointment("2026-01-01", "Desc");
        String id = service.getAllAppointments().get(0).getAppointmentId();
        service.updateAppointmentDate("2026-01-02", id);
        service.displayAppointmentList();
        assertEquals("2026-01-02", service.getAppointment(id).getAppointmentDate(), "Date wasn't updated.");
    }

    @Test
    @DisplayName("Test to Update Appointment Description.")
    void testUpdateAppointmentDesc() {
        AppointmentService service = new AppointmentService();
        service.createAppointment("2026-01-01", "Desc");
        String id = service.getAllAppointments().get(0).getAppointmentId();
        service.updateAppointmentDesc("Desc2", id);
        service.displayAppointmentList();
        assertEquals("Desc2", service.getAppointment(id).getAppointmentDesc(), "Desc wasn't updated.");
    }

    @Test
    @DisplayName("Test to ensure that service correctly deletes appointments.")
    void testDeleteAppointment() {
        AppointmentService service = new AppointmentService();
        service.createAppointment("2026-01-01", "Desc");
        String id = service.getAllAppointments().get(0).getAppointmentId();
        service.deleteAppointment(id);
        ArrayList<Appointment> empty = new ArrayList<Appointment>();
        service.displayAppointmentList();
        assertTrue(service.getAllAppointments().isEmpty(), "The appointment wasn't deleted.");
    }

    @Test
    @DisplayName("Test to ensure that service can add an appointment.")
    void testAddAppointment() {
        AppointmentService service = new AppointmentService();
        service.createAppointment("2026-01-01", "Desc");
        service.displayAppointmentList();
        String id = service.getAllAppointments().get(0).getAppointmentId();
        assertNotNull(service.getAppointment(id), "Appointment wasn't added correctly.");
    }
}
