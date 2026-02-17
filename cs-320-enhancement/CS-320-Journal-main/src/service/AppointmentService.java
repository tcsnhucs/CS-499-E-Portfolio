package service;

import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;

import model.Appointment;

public class AppointmentService {

    public Map<String, Appointment> appointmentMap = new HashMap<>();

    public List<Appointment> getAllAppointments() {
        List<Appointment> list = new ArrayList<>(appointmentMap.values());
        list.sort((a, b) -> Long.compare(Long.parseLong(a.getAppointmentId()), Long.parseLong(b.getAppointmentId())));
        return list;
    }

    public void clearAll() {
        appointmentMap.clear();
    }

    public void addAppointment(Appointment a) {
        appointmentMap.put(a.getAppointmentId(), a);
    }

    public void displayAppointmentList() {
        for (Appointment a : appointmentMap.values()) {
            System.out.println("\t Appointment ID: " + a.getAppointmentId());
            System.out.println("\t Appointment Date: " + a.getAppointmentDate());
            System.out.println("\t Appointment Desc: " + a.getAppointmentDesc() + "\n");
        }
    }

    public void createAppointment(String appointmentDate, String appointmentDesc) {
        Appointment appt = new Appointment(appointmentDate, appointmentDesc);
        if (appointmentMap.containsKey(appt.getAppointmentId())) {
            System.out.println("Appointment with ID " + appt.getAppointmentId() + " already exists.");
            return;
        }
        appointmentMap.put(appt.getAppointmentId(), appt);
        System.out.println("Appointment created successfully with ID: " + appt.getAppointmentId());
    }

    public Appointment getAppointment(String appointmentID) {
        return appointmentMap.get(appointmentID);
    }

    public void deleteAppointment(String appointmentID) {
        if (appointmentMap.remove(appointmentID) == null) {
            System.out.println("Appointment ID: " + appointmentID + " not found.");
        }
    }

    public void updateAppointmentDate(String updatedDate, String appointmentID) {
        Appointment a = appointmentMap.get(appointmentID);
        if (a != null) {
            a.setAppointmentDate(updatedDate);
        } else {
            System.out.println("Appointment ID: " + appointmentID + " not found.");
        }
    }

    public void updateAppointmentDesc(String updatedDesc, String appointmentID) {
        Appointment a = appointmentMap.get(appointmentID);
        if (a != null) {
            a.setAppointmentDesc(updatedDesc);
        } else {
            System.out.println("Appointment ID: " + appointmentID + " not found.");
        }
    }
}
