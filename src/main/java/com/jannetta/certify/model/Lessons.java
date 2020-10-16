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

}
