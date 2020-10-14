package com.jannetta.certify.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Learner {
	private Logger logger = LoggerFactory.getLogger(getClass());
	private static final String[] columnNames = { "Workshop Name", "Badge", "Instructor", "User ID", "Firstname", "Initials", "Lastname",
			"Email", "Date", "Print" };
	
	@SerializedName("workshop")
	@Expose
	private String workshop;
	
	@SerializedName("badge")
	@Expose
	private String badge;

	@SerializedName("instructor")
	@Expose
	private String instructor;

	@SerializedName("user_id")
	@Expose
	private String user_id;

	@SerializedName("firstname")
	@Expose
	private String firstname;

	@SerializedName("initials")
	@Expose
	private String initials;

	@SerializedName("lastname")
	@Expose
	private String lastname;

	@SerializedName("email")
	@Expose
	private String email;

	@SerializedName("date")
	@Expose
	private String date;
	
	private boolean print = false;
	
	public Learner(String workshop, String badge, String instructor, String user_id, String firstname, String initials, String lastname,
			String email, String date) {
		super();
		this.workshop = workshop;
		this.badge = badge;
		this.instructor = instructor;
		this.user_id = user_id;
		this.firstname = firstname;
		this.initials = initials;
		this.lastname = lastname;
		this.email = email;
		this.date = date;
		this.print = false;
		logger.trace("Create a learner");
	}

	public String getWorkshop() {
		return workshop;
	}

	public void setWorkshop(String workshop) {
		this.workshop = workshop;
	}

	public String getBadge() {
		return badge;
	}

	public void setBadge(String badge) {
		this.badge = badge;
	}

	public String getInstructor() {
		return instructor;
	}

	public void setInstructor(String instructor) {
		this.instructor = instructor;
	}

	public String getUser_id() {
		return user_id;
	}

	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}

	public String getFirstname() {
		return firstname;
	}

	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}

	public String getInitials() {
		return initials;
	}

	public void setInitials(String initials) {
		this.initials = initials;
	}

	public String getLastname() {
		return lastname;
	}

	public void setLastname(String lastname) {
		this.lastname = lastname;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}
	
	public static int getColumnNumber() {
		return columnNames.length;
	}
	
	public static String[] getColumnNames() {
		return columnNames;
	}

	public boolean isPrint() {
		return print;
	}

	public void setPrint(boolean print) {
		this.print = print;
	}

}
