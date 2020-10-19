package com.jannetta.certify.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Lesson {
	private static Logger logger = LoggerFactory.getLogger(Learner.class);
	private static final String[] columnNames = { "Lesson ID", "Description", "Select" };


    @SerializedName("lessonID")
    @Expose
    private String lessonID;    
    
    @SerializedName("description")
    @Expose
    private String description;

    boolean print = false;

    public Lesson(String lessonID, String description) {
        this.lessonID = lessonID;
        this.description = description;
        logger.trace("Create a lesson");
	}

	public String getLessonID() {
        return lessonID;
    }

    public void setLessonID(String lessonID) {
        this.lessonID = lessonID;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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
