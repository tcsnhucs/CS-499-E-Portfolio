package main;

import java.util.ArrayList;

public class ContactService {
	// Start with an ArrayList of contacts to hold the list of contacts
	public ArrayList<Contact> contactList = new ArrayList<Contact>();

	// Display the full list of contacts to the console for error checking.
	public void displayContactList() {
		for (int counter = 0; counter < contactList.size(); counter++) {
			System.out.println("\t Contact ID: " + contactList.get(counter).getContactId());
			System.out.println("\t First Name: " + contactList.get(counter).getFirstName());
			System.out.println("\t Last Name: " + contactList.get(counter).getLastName());
			System.out.println("\t Phone Number: " + contactList.get(counter).getPhone());
			System.out.println("\t Address: " + contactList.get(counter).getAddress() + "\n");
		}
	}

	// Creating new contact method
	public void createContact(String firstName, String lastName, String phone, String address) {
		Contact contact = new Contact(firstName, lastName, phone, address);
		contactList.add(contact);
		// Check if AppointmentID already exists in the list
		for (Contact existingContact : contactList) {
			if (existingContact.getContactId().equals(contact.getContactId())) {
				System.out.println("Contact with ID " + contact.getContactId() + " already exists.");
				return;  // Exit the method if ID already exists
			}
		}
		// Add Contact to list if ID is unique
		contactList.add(contact);
		System.out.println("Contact created successfully with ID: " + contact.getContactId());
	}

	// Get Contact Methods
	public Contact getContact(String contactID) {
		Contact contact = new Contact(null, null, null, null);
		for (int counter = 0; counter < contactList.size(); counter++) {
			if (contactList.get(counter).getContactId().contentEquals(contactID)) {
				contact = contactList.get(counter);
			}
		}
		return contact;
	}

	//Delete contact method
	public void deleteContact(String contactID) {
		for (int counter = 0; counter < contactList.size(); counter++) {
			if (contactList.get(counter).getContactId().equals(contactID)) {
				contactList.remove(counter);
				break;
			}
			if (counter == contactList.size() - 1) {
				System.out.println("Contact ID: " + contactID + " not found.");
			}
		}
	}
	
	//Update Methods
	public void updateFirstName(String updatedFirstName, String contactID) {
		for (int counter = 0; counter < contactList.size(); counter++) {
			if (contactList.get(counter).getContactId().equals(contactID)) {
				contactList.get(counter).setFirstName(updatedFirstName);
				break;
			}
			if (counter == contactList.size() - 1) {
				System.out.println("Contact ID: " + contactID + " not found.");
			}
		}
	}
	public void updateLastName(String updatedLastName, String contactID) {
		for (int counter = 0; counter < contactList.size(); counter++) {
			if (contactList.get(counter).getContactId().equals(contactID)) {
				contactList.get(counter).setLastName(updatedLastName);
				break;
			}
			if (counter == contactList.size() - 1) {
				System.out.println("Contact ID: " + contactID + " not found.");
			}
		}
	}
	public void updatePhone(String updatedPhone, String contactID) {
		for (int counter = 0; counter < contactList.size(); counter++) {
			if (contactList.get(counter).getContactId().equals(contactID)) {
				contactList.get(counter).setPhone(updatedPhone);
				break;
			}
			if (counter == contactList.size() - 1) {
				System.out.println("Contact ID: " + contactID + " not found.");
			}
		}
	}
	public void updateAddress(String updatedAddress, String contactID) {
		for (int counter = 0; counter < contactList.size(); counter++) {
			if (contactList.get(counter).getContactId().equals(contactID)) {
				contactList.get(counter).setAddress(updatedAddress);
				break;
			}
			if (counter == contactList.size() - 1) {
				System.out.println("Contact ID: " + contactID + " not found.");
			}
		}
	}
}