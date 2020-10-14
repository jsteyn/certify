package com.jannetta.certify.model;

import java.util.ArrayList;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Learners extends ArrayList<Learner> {
	
	@SerializedName("learners")
	@Expose
	private Learners learners = this;

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	

	/**
	 * The number of attributes (columns) in the Workshop class.
	 * @return
	 */
	public int getColumnCount() {
		return Learner.getColumnNumber();
	}
	
	public String[] getColumnNames() {
		return Learner.getColumnNames();
	}

	public Learners getLearners() {
		return learners;
	}

	public void setLearners(Learners learners) {
		this.learners = learners;
	}



}
