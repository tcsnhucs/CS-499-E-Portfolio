package test;

import org.junit.jupiter.api.Test;


import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import org.junit.jupiter.api.DisplayName;

import model.Contact;
import service.ContactService;



public class ContactServiceTest {


	@Test
	@DisplayName("Test to Update First Name.")
	void testUpdateFirstName() {
		ContactService service = new ContactService();
		service.createContact("TestName", "LastTest", "5555555555", "TestStreet");
		String id = service.getAllContacts().get(0).getContactId();
		service.updateFirstName("TestName2", id);
		service.displayContactList();
		assertEquals("TestName2", service.getContact(id).getFirstName(), "First name wasn't updated.");
	}

	@Test
	@DisplayName("Test to Update Last Name.")
	void testUpdateLastName() {
		ContactService service = new ContactService();
		service.createContact("TestName", "LastTest", "5555555555", "TestStreet");
		String id = service.getAllContacts().get(0).getContactId();
		service.updateLastName("LastTest2", id);
		service.displayContactList();
		assertEquals("LastTest2", service.getContact(id).getLastName(), "Last name wasn't updated.");
	}

	@Test
	@DisplayName("Test to update phone.")
	void testUpdatePhone() {
		ContactService service = new ContactService();
		service.createContact("TestName", "LastTest", "5555555555", "TestStreet");
		String id = service.getAllContacts().get(0).getContactId();
		service.updatePhone("5555555556", id);
		//service.displayContactList();
		assertEquals("5555555556", service.getContact(id).getPhone(), "Phone wasn't updated.");
	}

	@Test
	@DisplayName("Test to update address.")
	void testUpdateAddress() {
		ContactService service = new ContactService();
		service.createContact("TestName", "LastTest", "5555555555", "TestStreet");
		String id = service.getAllContacts().get(0).getContactId();
		service.updateAddress("TestStreet2", id);
		service.displayContactList();
		assertEquals("TestStreet2", service.getContact(id).getAddress(), "Address wasn't updated.");
	}

	@Test
	@DisplayName("Test to ensure that service correctly deletes contacts.")
	void testDeleteContact() {
		ContactService service = new ContactService();
		service.createContact("TestName", "LastTest", "5555555555", "TestStreet");
		String id = service.getAllContacts().get(0).getContactId();
		service.deleteContact(id);
		// Ensure that the contact list is now empty
		service.displayContactList();
		assertTrue(service.getAllContacts().isEmpty(), "The contact wasn't deleted.");
	}

	@Test
	@DisplayName("Test to ensure that service can add a contact.")
	void testAddContact() {
		ContactService service = new ContactService();
		service.createContact("TestName", "LastTest", "5555555555", "TestStreet");
		service.displayContactList();
		String id = service.getAllContacts().get(0).getContactId();
		assertNotNull(service.getContact(id), "Contact wasn't added correctly.");
	}
}