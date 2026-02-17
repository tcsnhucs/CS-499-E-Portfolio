package test;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

import model.Appointment;

public class AppointmentTest {

    @Test
    @DisplayName("Id must be less than 10 char")
    void testAppointmentID10plus() {
        Appointment appt = new Appointment("2026-01-01", "Test description");
        if (appt.getAppointmentId().length() > 10) {
            fail("Appointment ID has more than 10 characters.");
        }
    }

    @Test
    @DisplayName("Date must be a valid date")
    void testAppointmentDateValid() {
        // valid date should construct
        assertDoesNotThrow(() -> {
            new Appointment("2026-02-28", "Desc");
        });
        // invalid date should throw
        assertThrows(IllegalArgumentException.class, () -> {
            new Appointment("2026-02-30", "Desc");
        });
    }

    @Test
    @DisplayName("Description must be less than 50 char")
    void testAppointmentDesc50plus() {
        String longDesc = "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"; // 54 chars
        assertThrows(IllegalArgumentException.class, () -> {
            new Appointment("2026-01-01", longDesc);
        });
    }

    @Test
    @DisplayName("Date must not be null")
    void testAppointmentDateNull() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Appointment(null, "Desc");
        });
    }

    @Test
    @DisplayName("Description must not be null")
    void testAppointmentDescNull() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Appointment("2026-01-01", null);
        });
    }
}
