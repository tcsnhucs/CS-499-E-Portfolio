package test;

import org.junit.jupiter.api.Test;


import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import org.junit.jupiter.api.DisplayName;

import main.Contact;
import main.ContactService;



public class ContactServiceTest {


	@Test
	@DisplayName("Test to Update First Name.")
	void testUpdateFirstName() {
		ContactService service = new ContactService();
		service.createContact("TestName", "LastTest", "5555555555", "TestStreet");
		service.updateFirstName("TestName2", "9");
		service.displayContactList();
		assertEquals("TestName2", service.getContact("9").getFirstName(), "First name wasn't updated.");
	}

	@Test
	@DisplayName("Test to Update Last Name.")
	void testUpdateLastName() {
		ContactService service = new ContactService();
		service.createContact("TestName", "LastTest", "5555555555", "TestStreet");
		service.updateLastName("LastTest2", "11");
		service.displayContactList();
		assertEquals("LastTest2", service.getContact("11").getLastName(), "Last name wasn't updated.");
	}

	@Test
	@DisplayName("Test to update phone.")
	void testUpdatePhone() {
		ContactService service = new ContactService();
		service.createContact("TestName", "LastTest", "5555555555", "TestStreet");
		service.updatePhone("5555555556", "17");
		//service.displayContactList();
		assertEquals("5555555556", service.getContact("17").getPhone(), "Phone wasn't updated.");
	}

	@Test
	@DisplayName("Test to update address.")
	void testUpdateAddress() {
		ContactService service = new ContactService();
		service.createContact("TestName", "LastTest", "5555555555", "TestStreet");
		service.updateAddress("TestStreet2", "15");
		service.displayContactList();
		assertEquals("TestStreet2", service.getContact("15").getAddress(), "Address wasn't updated.");
	}

	@Test
	@DisplayName("Test to ensure that service correctly deletes contacts.")
	void testDeleteContact() {
		ContactService service = new ContactService();
		service.createContact("TestName", "LastTest", "5555555555", "TestStreet");
		service.deleteContact("17");
		// Ensure that the contactList is now empty by creating a new empty contactList to compare it with
		ArrayList<Contact> contactListEmpty = new ArrayList<Contact>();
		service.displayContactList();
		assertEquals(service.contactList, contactListEmpty, "The contact wasn't deleted.");
	}

	@Test
	@DisplayName("Test to ensure that service can add a contact.")
	void testAddContact() {
		ContactService service = new ContactService();
		service.createContact("TestName", "LastTest", "5555555555", "TestStreet");
		service.displayContactList();
		assertNotNull(service.getContact("0"), "Contact wasn't added correctly.");
	}

}