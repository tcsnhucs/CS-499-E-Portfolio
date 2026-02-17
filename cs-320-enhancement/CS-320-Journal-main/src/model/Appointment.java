package model;

import java.util.concurrent.atomic.AtomicLong;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Appointment {

    private final String appointmentId;
    private String appointmentDate; // stored as ISO date string (yyyy-MM-dd)
    private String appointmentDesc;
    private static AtomicLong idGenerator = new AtomicLong();

    public Appointment(String appointmentDate, String appointmentDesc) {
        if (appointmentDate == null) {
            throw new IllegalArgumentException("Invalid Date");
        }
        // Validate date
        try {
            LocalDate.parse(appointmentDate);
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Invalid Date");
        }
        if (appointmentDesc == null || appointmentDesc.length() > 50) {
            throw new IllegalArgumentException("Invalid Description");
        }
        this.appointmentId = String.valueOf(idGenerator.getAndIncrement());
        this.appointmentDate = appointmentDate;
        this.appointmentDesc = appointmentDesc;
    }

    @JsonCreator
    public Appointment(@JsonProperty("appointmentId") String appointmentId, @JsonProperty("appointmentDate") String appointmentDate,
            @JsonProperty("appointmentDesc") String appointmentDesc) {
        if (appointmentDate == null) {
            throw new IllegalArgumentException("Invalid Date");
        }
        try {
            LocalDate.parse(appointmentDate);
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Invalid Date");
        }
        if (appointmentDesc == null || appointmentDesc.length() > 50) {
            throw new IllegalArgumentException("Invalid Description");
        }
        this.appointmentId = appointmentId;
        this.appointmentDate = appointmentDate;
        this.appointmentDesc = appointmentDesc;
    }

    // Getters
    public String getAppointmentId() {
        return appointmentId;
    }

    public String getAppointmentDate() {
        return appointmentDate;
    }

    public String getAppointmentDesc() {
        return appointmentDesc;
    }

    // Setters
    public void setAppointmentDate(String appointmentDate) {
        if (appointmentDate == null) {
            throw new IllegalArgumentException("Invalid Date");
        }
        try {
            LocalDate.parse(appointmentDate);
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Invalid Date");
        }
        this.appointmentDate = appointmentDate;
    }

    public void setAppointmentDesc(String appointmentDesc) {
        if (appointmentDesc == null || appointmentDesc.length() > 50) {
            throw new IllegalArgumentException("Invalid Description");
        }
        this.appointmentDesc = appointmentDesc;
    }

    public static void setIdCounter(long next) {
        idGenerator = new AtomicLong(next);
    }
}
