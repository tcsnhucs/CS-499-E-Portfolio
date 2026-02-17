package service;

import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;

import model.Contact;

public class ContactService {
    // Use a map for fast lookups by id
    public Map<String, Contact> contactMap = new HashMap<>();

    public List<Contact> getAllContacts() {
        List<Contact> list = new ArrayList<>(contactMap.values());
        list.sort((a, b) -> Long.compare(Long.parseLong(a.getContactId()), Long.parseLong(b.getContactId())));
        return list;
    }

    public void clearAll() {
        contactMap.clear();
    }

    public void addContact(Contact c) {
        contactMap.put(c.getContactId(), c);
    }

    // Display the full list of contacts to the console for error checking.
    public void displayContactList() {
        for (Contact c : contactMap.values()) {
            System.out.println("\t Contact ID: " + c.getContactId());
            System.out.println("\t First Name: " + c.getFirstName());
            System.out.println("\t Last Name: " + c.getLastName());
            System.out.println("\t Phone Number: " + c.getPhone());
            System.out.println("\t Address: " + c.getAddress() + "\n");
        }
    }

    // Creating new contact method
    public void createContact(String firstName, String lastName, String phone, String address) {
        Contact contact = new Contact(firstName, lastName, phone, address);
        if (contactMap.containsKey(contact.getContactId())) {
            System.out.println("Contact with ID " + contact.getContactId() + " already exists.");
            return;
        }
        contactMap.put(contact.getContactId(), contact);
        System.out.println("Contact created successfully with ID: " + contact.getContactId());
    }

    // Get Contact Methods
    public Contact getContact(String contactID) {
        return contactMap.get(contactID);
    }

    //Delete contact method
    public void deleteContact(String contactID) {
        if (contactMap.remove(contactID) == null) {
            System.out.println("Contact ID: " + contactID + " not found.");
        }
    }

    //Update Methods
    public void updateFirstName(String updatedFirstName, String contactID) {
        Contact c = contactMap.get(contactID);
        if (c != null) {
            c.setFirstName(updatedFirstName);
        } else {
            System.out.println("Contact ID: " + contactID + " not found.");
        }
    }

    public void updateLastName(String updatedLastName, String contactID) {
        Contact c = contactMap.get(contactID);
        if (c != null) {
            c.setLastName(updatedLastName);
        } else {
            System.out.println("Contact ID: " + contactID + " not found.");
        }
    }

    public void updatePhone(String updatedPhone, String contactID) {
        Contact c = contactMap.get(contactID);
        if (c != null) {
            c.setPhone(updatedPhone);
        } else {
            System.out.println("Contact ID: " + contactID + " not found.");
        }
    }

    public void updateAddress(String updatedAddress, String contactID) {
        Contact c = contactMap.get(contactID);
        if (c != null) {
            c.setAddress(updatedAddress);
        } else {
            System.out.println("Contact ID: " + contactID + " not found.");
        }
    }
}