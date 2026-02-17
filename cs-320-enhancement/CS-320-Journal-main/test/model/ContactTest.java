package test;

import org.junit.jupiter.api.Test;

import model.Contact;

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
		assertThrows(IllegalArgumentException.class, () -> {
			new Contact("SuperTestName", "LastTest", "5555555555", "TestStreet");
		});
	}

	@Test
	@DisplayName("LastName must be less than 10 char")
	void testContactLastName10plus() {
		assertThrows(IllegalArgumentException.class, () -> {
			new Contact("TestName", "SuperLastTest", "5555555555", "TestStreet");
		});
	}

	@Test
	@DisplayName("Phone must be less than 10 char")
	void testContactNumber10plus() {
		assertThrows(IllegalArgumentException.class, () -> {
			new Contact("TestName", "LastTest", "555555555555555555555", "TestStreet");
		});
	}

	@Test
	@DisplayName("Address must be less than 10 char")
	void testContactAddress30plus() {
		assertThrows(IllegalArgumentException.class, () -> {
			new Contact("TestName", "LastTest", "5555555555", "TestStreet TestStreet TestStreet TestStreet TestStreet TestStreet TestStreet TestStreet TestStreet TestStreet");
		});
	}

	@Test
	@DisplayName("FirstName must not be null")
	void testContactFirstNameNull() {
		assertThrows(IllegalArgumentException.class, () -> {
			new Contact(null, "LastTest", "5555555555", "TestStreet");
		});
	}

	@Test
	@DisplayName("LastName must not be null")
	void testContactLastNameNull() {
		assertThrows(IllegalArgumentException.class, () -> {
			new Contact("TestName", null, "5555555555", "TestStreet");
		});
	}

	@Test
	@DisplayName("Phone must not be null")
	void testContactPhoneNull() {
		assertThrows(IllegalArgumentException.class, () -> {
			new Contact("TestName", "LastTest", null, "TestStreet");
		});
	}

	@Test
	@DisplayName("Address must not be null")
	void testContactAddressNull() {
		assertThrows(IllegalArgumentException.class, () -> {
			new Contact("TestName", "LastTest", "5555555555", null);
		});
	}
}