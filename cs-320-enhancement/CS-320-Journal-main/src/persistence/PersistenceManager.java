package persistence;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.OptionalLong;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import model.Contact;
import model.Task;
import model.Appointment;
import service.ContactService;
import service.TaskService;
import service.AppointmentService;

public class PersistenceManager {

    private static class DataContainer {
        public List<Contact> contacts;
        public List<Task> tasks;
        public List<Appointment> appointments;
    }

    private static ObjectMapper mapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        mapper.enable(SerializationFeature.INDENT_OUTPUT);
        return mapper;
    }

    public static void save(File file, ContactService contactService, TaskService taskService,
            AppointmentService appointmentService) throws IOException {
        DataContainer c = new DataContainer();
        c.contacts = contactService.getAllContacts();
        c.tasks = taskService.getAllTasks();
        c.appointments = appointmentService.getAllAppointments();
        mapper().writeValue(file, c);
    }

    public static void load(File file, ContactService contactService, TaskService taskService,
            AppointmentService appointmentService) throws IOException {
        DataContainer c = mapper().readValue(file, DataContainer.class);
        contactService.clearAll();
        taskService.clearAll();
        appointmentService.clearAll();
        if (c.contacts != null) {
            for (Contact ct : c.contacts) contactService.addContact(ct);
            // set next id counter
            OptionalLong maxId = c.contacts.stream().mapToLong(ct -> Long.parseLong(ct.getContactId())).max();
            Contact.setIdCounter(maxId.isPresent() ? maxId.getAsLong() + 1 : 0);
        }
        if (c.tasks != null) {
            for (Task t : c.tasks) taskService.addTask(t);
            OptionalLong maxId = c.tasks.stream().mapToLong(t -> Long.parseLong(t.getTaskId())).max();
            Task.setIdCounter(maxId.isPresent() ? maxId.getAsLong() + 1 : 0);
        }
        if (c.appointments != null) {
            for (Appointment a : c.appointments) appointmentService.addAppointment(a);
            OptionalLong maxId = c.appointments.stream().mapToLong(a -> Long.parseLong(a.getAppointmentId())).max();
            Appointment.setIdCounter(maxId.isPresent() ? maxId.getAsLong() + 1 : 0);
        }
    }
}
