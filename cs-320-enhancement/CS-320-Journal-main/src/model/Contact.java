package model;

import java.util.concurrent.atomic.AtomicLong;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Contact {
	
	//Define Class Variables
	private final String contactId;
	private String firstName;
	private String lastName;
	private String phone;
	private String address;
	private static AtomicLong idGenerator = new AtomicLong();
	
	public Contact(String firstName, String lastName, String phone, String address) { //Ensure variables meet requirements
		
		if (firstName == null || firstName.length() > 10) {
			throw new IllegalArgumentException("Invalid First Name");
		}
		if (lastName == null || lastName.length() > 10) {
			throw new IllegalArgumentException("Invalid Last Name");
		}
		if (phone == null || phone.length() != 10) {
			throw new IllegalArgumentException("Invalid Phone Number");
		}
		if (address == null || address.length() > 30) {
			throw new IllegalArgumentException("Invalid Address");
		}
		this.contactId = String.valueOf(idGenerator.getAndIncrement()); //generates a unique and unchanging id for contact id
		this.firstName = firstName;
		this.lastName = lastName;
		this.phone = phone;
		this.address = address;
	}

	@JsonCreator
	public Contact(@JsonProperty("contactId") String contactId, @JsonProperty("firstName") String firstName,
				@JsonProperty("lastName") String lastName, @JsonProperty("phone") String phone,
				@JsonProperty("address") String address) {
		// Use JSON constructor; validate same as normal constructor
		if (firstName == null || firstName.length() > 10) {
			throw new IllegalArgumentException("Invalid First Name");
		}
		if (lastName == null || lastName.length() > 10) {
			throw new IllegalArgumentException("Invalid Last Name");
		}
		if (phone == null || phone.length() != 10) {
			throw new IllegalArgumentException("Invalid Phone Number");
		}
		if (address == null || address.length() > 30) {
			throw new IllegalArgumentException("Invalid Address");
		}
		this.contactId = contactId;
		this.firstName = firstName;
		this.lastName = lastName;
		this.phone = phone;
		this.address = address;
	}

	public static void setIdCounter(long next) {
		idGenerator = new AtomicLong(next);
	}	
	//Get Methods
	public String getContactId() {
		return contactId;
	}
	public String getFirstName() {
		return firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public String getPhone() {
		return phone;
	}
	public String getAddress() {
		return address;
	}
	
	//Set Methods. No Set Method for contactID as it is unchangeable
	public void setFirstName(String newFirstName) {
		if (newFirstName == null || newFirstName.length() > 10) {
			throw new IllegalArgumentException("Ivalid First Name");
		}
		this.firstName = newFirstName;
	}
	public void setLastName(String newLastName) {
		if (newLastName == null || newLastName.length() > 10) {
			throw new IllegalArgumentException("Invalid Last Name");
		}
		this.lastName = newLastName;
	}
	public void setPhone(String newPhone) {
		if (newPhone == null || newPhone.length() != 10) {
			throw new IllegalArgumentException("Inavlid Phone Number");
		}
		this.phone = newPhone;
	}
	public void setAddress(String newAddress) {
		if (newAddress == null || newAddress.length() > 30) {
			throw new IllegalArgumentException("Invalid Address");
		}
		this.address = newAddress;
	}

}