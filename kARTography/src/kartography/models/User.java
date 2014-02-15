package kartography.models;

import java.util.Date;

public class User {
	String fullName;
	String firstName;
	String lastName;
	Date DOB;
	String imageUrl;
	
	public User(String fullName, String firstName, String lastName, Date dOB,
			String imageUrl) {
		super();
		this.fullName = fullName;
		this.firstName = firstName;
		this.lastName = lastName;
		DOB = dOB;
		this.imageUrl = imageUrl;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public Date getDOB() {
		return DOB;
	}

	public void setDOB(Date dOB) {
		DOB = dOB;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	@Override
	public String toString() {
		return "User [fullName=" + fullName + ", firstName=" + firstName
				+ ", lastName=" + lastName + ", DOB=" + DOB + ", imageUrl="
				+ imageUrl + "]";
	}

}
