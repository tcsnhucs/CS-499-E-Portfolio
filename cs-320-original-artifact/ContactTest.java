package test;

import org.junit.jupiter.api.Test;

import main.Contact;

import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

public class ContactTest {
	@Test
	@DisplayName("Id must be less than 10 char")
	void testContactID10plus() {
		Contact contact = new Contact("TestName", "LastTest", "5555555555", "TestStreet");
		if (contact.getContactId().length() > 10) {
			fail("Contact ID has more than 10 characters.");
		}
	}

	@Test
	@DisplayName("FirstName must be less than 10 char")
	void testContactFirstName10plus() {
		Contact contact = new Contact("SuperTestName", "LastTest", "5555555555", "TestStreet");
		if (contact.getFirstName().length() > 10) {
			fail("FirstName > 10 char.");
		}
	}

	@Test
	@DisplayName("LastName must be less than 10 char")
	void testContactLastName10plus() {
		Contact contact = new Contact("TestName", "SuperLastTest", "5555555555", "TestStreet");
		if (contact.getLastName().length() > 10) {
			fail("LastName > 10 char.");
		}
	}

	@Test
	@DisplayName("Phone must be less than 10 char")
	void testContactNumber10plus() {
		Contact contact = new Contact("TestName", "LastTest", "555555555555555555555", "TestStreet");
		if (contact.getPhone().length() != 10) {
			fail("Phone =/= 10 char.");
		}
	}

	@Test
	@DisplayName("Address must be less than 10 char")
	void testContactAddress30plus() {
		Contact contact = new Contact("TestName", "LastTest", "5555555555", "TestStreet TestStreet TestStreet TestStreet TestStreet TestStreet TestStreet TestStreet TestStreet TestStreet");
		if (contact.getAddress().length() > 30) {
			fail("Address > 30 char.");
		}
	}

	@Test
	@DisplayName("FirstName must not be null")
	void testContactFirstNameNull() {
		Contact contact = new Contact(null, "LastTest", "5555555555", "TestStreet");
		assertNotNull(contact.getFirstName(), "First name was null.");
	}

	@Test
	@DisplayName("LastName must not be null")
	void testContactLastNameNull() {
		Contact contact = new Contact("TestName", null, "5555555555", "TestStreet");
		assertNotNull(contact.getLastName(), "Last name was null.");
	}

	@Test
	@DisplayName("Phone must not be null")
	void testContactPhoneNull() {
		Contact contact = new Contact("TestName", "LastTest", null, "TestStreet");
		assertNotNull(contact.getPhone(), "Phone was null.");
	}

	@Test
	@DisplayName("Address must not be null")
	void testContactAddressNull() {
		Contact contact = new Contact("TestName", "LastTest", "5555555555", null);
		assertNotNull(contact.getAddress(), "Address was null.");
	}
}