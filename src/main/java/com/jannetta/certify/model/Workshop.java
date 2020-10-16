package com.jannetta.certify.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Workshop {
	private Logger logger = LoggerFactory.getLogger(getClass());
	private static final String[] columnNames = {"Workshop Name", "Badge", "Date From", "Date To", "URL", "Print"};

	@SerializedName("workshop_name")
	@Expose
	private String workshop_name;

	@SerializedName("badge")
	@Expose
	private String badge;

	@SerializedName("date_from")
	@Expose
	private String date_from;

	@SerializedName("date_to")
	@Expose
	private String date_to;
	
	@SerializedName("URL")
	@Expose
	private String url;

	@SerializedName("lessons")
	@Expose 
	private Lessons lessons;
	
	private boolean print = false;

	public Workshop(String workshop_name, String badge, String date_from, String date_to, String url) {
		super();
		this.workshop_name = workshop_name;
		this.badge = badge;
		this.date_from = date_from;
		this.date_to = date_to;
		this.url = url;
		this.print = false;
		logger.trace("Create a workshop");
	}

	public String getWorkshop_name() {
		return workshop_name;
	}

	public void setWorkshop_name(String workshop_name) {
		this.workshop_name = workshop_name;
	}

	public String getBadge() {
		return badge;
	}

	public void setBadge(String badge) {
		this.badge = badge;
	}

	public String getDate_from() {
		return date_from;
	}

	public void setDate_from(String date_from) {
		this.date_from = date_from;
	}

	public String getDate_to() {
		return date_to;
	}

	public void setDate_to(String date_to) {
		this.date_to = date_to;
	}
	
	public static int getColumnCount() {
		return columnNames.length;
	}
	
	public static String[] getColumnNames() {
		return columnNames;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public boolean isPrint() {
		return print;
	}

	public void setPrint(boolean print) {
		this.print = print;
	}

	public Lessons getLessons() {
		return lessons;
	}

	public void setLessons(Lessons lessons) {
		this.lessons = lessons;
	}

	

}
