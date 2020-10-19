package com.jannetta.certify.model;

import java.util.ArrayList;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Lessons extends ArrayList<Lesson> {
    
    /**
     *
     */
    private static final long serialVersionUID = 1L;
    @SerializedName("lessons")
	@Expose
    private Lessons lessons = this;
    
    public Lessons() {
        super();
        // read file with lessons
    }

    public Lesson getLesson(int index) {
        return get(index);
    }

    public boolean exists(String lessonID) {
        for (int i = 0; i < size(); i++) {
            if (get(i).getLessonID().equals(lessonID)) {
                return true;
            }
        }
        return false;
    }

}
